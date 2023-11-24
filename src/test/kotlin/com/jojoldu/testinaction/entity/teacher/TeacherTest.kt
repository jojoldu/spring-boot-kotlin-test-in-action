package com.jojoldu.testinaction.entity.teacher

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class TeacherTest {

    @Autowired
    private lateinit var teacherRepository: TeacherRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var entityManager: EntityManager

    @AfterEach
    @Transactional
    fun cleanup() {
        val tables = entityManager.metamodel.entities.map { it.name }

        tables.forEach { table ->
            jdbcTemplate.execute("TRUNCATE table $table")
        }
    }

    @Test
    fun `test adding students to a teacher`() {
        val email = "jojoldu@gmail.com"
        val teacher = Teacher(name = "jojoldu", email = email)
        val student1 = Student(name = "John", email = "John@gmail.com", teacher = teacher)
        val student2 = Student(name = "Jane", email = "Jane@gmail.com", teacher = teacher)

        teacher.addStudent(student1)
        teacher.addStudent(student2)

        teacherRepository.save(teacher)

        val result = teacherRepository.findByEmail(email)
        assertThat(result?.name).isEqualTo("jojoldu")
    }
}