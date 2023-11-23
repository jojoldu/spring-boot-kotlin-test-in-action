package com.jojoldu.testinaction.entity.teacher

import jakarta.persistence.*

@Entity
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,
    val email: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    var teacher: Teacher
) {

    fun updateTeacher(teacher: Teacher) {
        this.teacher = teacher
    }
}
