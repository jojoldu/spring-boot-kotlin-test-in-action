package com.jojoldu.testinaction.service

import com.jojoldu.testinaction.entity.teacher.Student
import com.jojoldu.testinaction.entity.teacher.Teacher
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import com.jojoldu.testinaction.service.teacher.NoTxTeacherService
import com.jojoldu.testinaction.service.teacher.TeacherEventListener
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class TxRollbackTest4 {

    @Autowired
    private lateinit var teacherService: NoTxTeacherService

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    @MockBean
    private lateinit var teacherEventListener: TeacherEventListener

    @Test
    fun `teacher가 save되면 event가 발행된다`() {
        val email = "jojoldu@gmail.com"
        val teacher = Teacher(name = "jojoldu", email = email)
        teacher.addStudent(Student(name = "John", email = "John@gmail.com", teacher = teacher))
        teacher.addStudent(Student(name = "Jane", email = "Jane@gmail.com", teacher = teacher))

        teacherService.saveAndPublish(teacher)

        // 이벤트 리스너가 호출되었는지 확인
        verify(teacherEventListener)
            .handleCustomEvent(any())
    }
}