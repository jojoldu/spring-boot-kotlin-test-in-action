package com.jojoldu.testinaction.entity.teacher

import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository : JpaRepository <Teacher, Long>{
    fun findByEmail(email: String): Teacher?
}