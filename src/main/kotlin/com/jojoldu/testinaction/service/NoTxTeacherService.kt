package com.jojoldu.testinaction.service

import com.jojoldu.testinaction.entity.teacher.Teacher
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class NoTxTeacherService (private val teacherRepository: TeacherRepository){

    fun countStudents (teacherId : Long) : Int {
        return teacherRepository.findById(teacherId).get().countStudents()
    }

    @Transactional
    fun saveAll (teachers: List<Teacher>) : Int {
        return teacherRepository.saveAll(teachers).size
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveAllNew (teachers: List<Teacher>) : Int {
        return teacherRepository.saveAll(teachers).size
    }
}