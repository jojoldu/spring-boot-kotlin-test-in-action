package com.jojoldu.testinaction.entity.teacher

import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long> {
    fun findByEmail(email: String): Student?
}