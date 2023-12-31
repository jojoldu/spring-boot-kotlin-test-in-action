package com.jojoldu.testinaction.service.teacher

import com.jojoldu.testinaction.entity.teacher.Student
import com.jojoldu.testinaction.entity.teacher.Teacher
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OriginalTest1 {

    @Autowired
    private lateinit var teacherService: TeacherService

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    @AfterEach
    fun tearDown() {
        teacherRepository.deleteAll()
    }

    @Test
    fun `teacher의 students count를 조회할 수 있다`() {
        val email = "jojoldu@gmail.com"
        val teacher = Teacher(name = "jojoldu", email = email)
        teacher.addStudent(Student(name = "John", email = "John@gmail.com", teacher = teacher))
        teacher.addStudent(Student(name = "Jane", email = "Jane@gmail.com", teacher = teacher))

        teacherRepository.save(teacher)

        val result = teacherService.countStudents(teacher.id!!)

        assertThat(result).isEqualTo(2)
    }
}