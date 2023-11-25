package com.jojoldu.testinaction

import com.jojoldu.testinaction.entity.teacher.StudentRepository
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CleanUp(
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
    ) {

    @Transactional
    fun all() {
        teacherRepository.deleteAllInBatch()
        studentRepository.deleteAllInBatch()
    }
}