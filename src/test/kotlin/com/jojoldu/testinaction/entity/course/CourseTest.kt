package com.jojoldu.testinaction.entity.course

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CourseTest {

    @Test
    fun `초기 가격 getter 검증`() {
        val course = Course(100)

        // 초기 가격이 100인지 확인
        assertEquals(100, course.price)
    }

    @Test
    fun `가격 setter 검증`() {
        val course = Course(100)

        // 가격을 200으로 업데이트하고 확인
        course.price = 200
        assertEquals(200, course.price)
    }

    @Test
    fun `잘못된 값으로 가격 설정 시 예외 발생`() {
        val course = Course(100)

        // 잘못된 가격 설정 시도 및 예외 발생 확인
        val exception = assertThrows<IllegalArgumentException> {
            course.price = -50
        }

        // 예외 메시지 확인
        assertEquals("Price must be greater than 0", exception.message)
    }
}