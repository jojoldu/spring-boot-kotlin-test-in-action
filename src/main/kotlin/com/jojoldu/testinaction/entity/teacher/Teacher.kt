package com.jojoldu.testinaction.entity.teacher

import jakarta.persistence.*

@Entity
class Teacher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,
    val email: String,

    @OneToMany(mappedBy = "teacher", cascade = [CascadeType.ALL])
    val students: MutableList<Student> = mutableListOf(),
) {

    fun addStudent(student: Student) {
        students.add(student)
        student.teacher = this
    }
}