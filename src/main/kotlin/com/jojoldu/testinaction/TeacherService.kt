package com.jojoldu.testinaction

import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TeacherService (private val teacherRepository: TeacherRepository){

    fun countStudents (teacherId : Long) : Int {
        return teacherRepository.findById(teacherId).get().countStudents()
    }
}