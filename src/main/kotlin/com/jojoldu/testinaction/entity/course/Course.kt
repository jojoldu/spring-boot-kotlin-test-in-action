package com.jojoldu.testinaction.entity.course

class Course(private var _price: Int) {

    var price: Int
        get() = _price
        set(value) {
            if (value > 0) { // 간단한 검증 논리
                _price = value
            } else {
                throw IllegalArgumentException("Price must be greater than 0")
            }
        }
}