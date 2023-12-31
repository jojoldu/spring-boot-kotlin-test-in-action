package com.jojoldu.testinaction.service.teacher

import com.jojoldu.testinaction.CleanUp
import com.jojoldu.testinaction.entity.teacher.Student
import com.jojoldu.testinaction.entity.teacher.Teacher
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class)
@SpringBootTest
class OriginalTest2 {

    @Autowired
    private lateinit var cleanUp: CleanUp

    @Autowired
    private lateinit var teacherService: NoTxTeacherService

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    @AfterEach
    fun tearDown() {
        cleanUp.all()
    }

    @Test
    @Order(1)
    fun `여러건의 teacher와 student가 일괄 저장된다`() {
        val email = "jojoldu@gmail.com"
        val teacher1 = Teacher(name = "jojoldu", email = email)
        teacher1.addStudent(Student(name = "John", email = "John@gmail.com", teacher = teacher1))
        teacher1.addStudent(Student(name = "Jane", email = "Jane@gmail.com", teacher = teacher1))

        val result = teacherService.saveAllNew(listOf(teacher1))

        assertThat(result).isEqualTo(1)
    }

    @Test
    @Order(2)
    fun `롤백 검증`() {
        val count = teacherRepository.count()
        println("DB에서 사라지지 않은 데이터: $count")

        assertThat(count).isEqualTo(0)
    }
}