package com.jojoldu.testinaction.service.teacher

import com.jojoldu.testinaction.entity.teacher.Student
import com.jojoldu.testinaction.entity.teacher.Teacher
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class)
@SpringBootTest
@Transactional
class TxRollbackTest3 {

    @Autowired
    private lateinit var teacherService: NoTxTeacherService

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    @Test
    @Order(1)
    fun `비동기로 저장된다`() {
        // given
        val email = "jojoldu@gmail.com"
        val teacher = Teacher(name = "jojoldu", email = email)
        teacher.addStudent(Student(name = "John", email = "John@gmail.com", teacher = teacher))
        teacher.addStudent(Student(name = "Jane", email = "Jane@gmail.com", teacher = teacher))

        // when
        val futureResult = teacherService.asyncSave(teacher)
        val result = futureResult.get()

        assertThat(result).isEqualTo(email)
    }

    @Test
    @Order(2)
    fun `롤백 검증`() {
        val count = teacherRepository.count()
        println("DB에서 사라지지 않은 데이터: $count")

        assertThat(count).isEqualTo(0)
    }

}