package com.jojoldu.testinaction.service.teacher

import com.jojoldu.testinaction.entity.teacher.Teacher
import com.jojoldu.testinaction.entity.teacher.TeacherRepository
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture

@Service
class NoTxTeacherService (private val teacherRepository: TeacherRepository,
                          private val eventPublisher: ApplicationEventPublisher
){
    companion object {
        private val logger = LoggerFactory.getLogger(NoTxTeacherService::class.java)
    }

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

    fun asyncSave(teacher: Teacher): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            teacherRepository.save(teacher)
            Thread.sleep(500)
            teacher.email
        }
    }

    @Transactional
    fun saveAndPublish(teacher: Teacher) {
        teacherRepository.save(teacher)
        // 트랜잭션이 커밋되면 처리될 이벤트 발행
        eventPublisher.publishEvent(TeacherEvent("saved teacherEmail=${teacher.email}"))
    }

    @Transactional
    fun saveWithException (teacher: Teacher)  {
        try {
            saveTeacher(teacher)
        } catch (e: Exception) {
            logger.error("save Teacher (${teacher.email}) Exception", e)
        }
    }

    private fun saveTeacher(teacher: Teacher) {
        teacherRepository.save(teacher)
        throw RuntimeException("save 이후 Exception")
    }
}