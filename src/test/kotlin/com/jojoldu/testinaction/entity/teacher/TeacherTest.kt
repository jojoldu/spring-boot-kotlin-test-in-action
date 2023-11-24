package com.jojoldu.testinaction.entity.teacher

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class TeacherTest {

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @AfterEach
    fun cleanup() {
        // 모든 데이터를 클린업합니다.
        testEntityManager.entityManager.createNativeQuery("TRUNCATE TABLE student RESTART IDENTITY CASCADE").executeUpdate()
        testEntityManager.entityManager.createNativeQuery("TRUNCATE TABLE teacher RESTART IDENTITY CASCADE").executeUpdate()
    }

    @Test
    fun `test adding students to a teacher`() {
        val teacher = Teacher(name = "Mr. Smith")
        val student1 = Student(name = "John Doe", teacher = teacher)
        val student2 = Student(name = "Jane Doe", teacher = teacher)

        teacher.addStudent(student1)
        teacher.addStudent(student2)

        testEntityManager.persist(teacher)
        testEntityManager.flush()
        testEntityManager.clear()

        val foundTeacher = testEntityManager.find(Teacher::class.java, teacher.id)
        assert(foundTeacher?.students?.size == 2)
        assert(foundTeacher?.students?.contains(student1) == true)
        assert(foundTeacher?.students?.contains(student2) == true)
    }
}