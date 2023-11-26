package com.jojoldu.testinaction.service.teacher

import com.jojoldu.testinaction.entity.teacher.Student
import com.jojoldu.testinaction.entity.teacher.Teacher
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import com.jojoldu.testinaction.service.teacher.NoTxTeacherService
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
class TxRollbackTest2 {

    @Autowired
    private lateinit var teacherService: NoTxTeacherService

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    @Test
    @Order(1)
    fun `여러건의 teacher와 student가 일괄 저장된다`() {
        // given
        val email = "jojoldu@gmail.com"
        val teacher1 = Teacher(name = "jojoldu", email = email)
        teacher1.addStudent(Student(name = "John", email = "John@gmail.com", teacher = teacher1))
        teacher1.addStudent(Student(name = "Jane", email = "Jane@gmail.com", teacher = teacher1))

        val teacher2 = Teacher(name = "jojoldu2", email = email)
        teacher2.addStudent(Student(name = "John2", email = "John2@gmail.com", teacher = teacher2))
        teacher2.addStudent(Student(name = "Jane2", email = "Jane2@gmail.com", teacher = teacher2))

        // when
        val result = teacherService.saveAllNew(listOf(teacher1, teacher2))

        assertThat(result).isEqualTo(2)
    }

    @Test
    @Order(2)
    fun `롤백 검증`() {
        val count = teacherRepository.count()
        println("DB에서 사라지지 않은 데이터: $count")

        assertThat(count).isEqualTo(0)
    }
}