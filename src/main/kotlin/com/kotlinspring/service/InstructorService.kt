package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(
    val instructorRepository: InstructorRepository
) {
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {

        // Convert instructorDTO to an instructor entity before saving
        val instructorEntity = instructorDTO.let {
            Instructor(it.id, it.name)
        }

        // save entity to db
        instructorRepository.save(instructorEntity)

        // Get an instructorDTO to pass back up
        return instructorEntity.let {
            InstructorDTO(it.id, it.name)
        }
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }
}
