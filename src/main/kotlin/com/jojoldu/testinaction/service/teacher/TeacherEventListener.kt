package com.jojoldu.testinaction.service.teacher

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class TeacherEventListener {
    companion object {
        private val logger = LoggerFactory.getLogger(TeacherEventListener::class.java)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleCustomEvent(event: TeacherEvent) {
        logger.info("TeacherEvent 발행 after transaction commit: ${event.message}")
    }
}