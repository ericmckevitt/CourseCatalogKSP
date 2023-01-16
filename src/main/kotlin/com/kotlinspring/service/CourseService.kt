package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository) {

    companion object: KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        // convert from courseDTO to course Entity
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category)
        }

        courseRepository.save(courseEntity)

        logger.info("Saved course is: $courseEntity")

        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {

        // if courseName is null, then courses = cr.findAll()
        val courses = courseName?.let {
            courseRepository.findCoursesByName(courseName)
        } ?: courseRepository.findAll()

        return courses
            .map {
                // Need a CourseDTO from Course Entity
                CourseDTO(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {

        // findById returns Optional containing a Course
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    // Update the data with the passed information
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    // Save the updated Course to DB
                    courseRepository.save(it)
                    // Return CourseDTO to propagate upward to return statement
                    CourseDTO(it.id, it.name, it.category)
                }
        } else {
             throw CourseNotFoundException("No course found for the passed in Id: $courseId")
        }
    }

    fun deleteCourse(courseId: Int) {
        // findById returns Optional containing a Course
        val existingCourse = courseRepository.findById(courseId)

        if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }
        } else {
            throw CourseNotFoundException("No course found for the passed in Id: $courseId")
        }
    }
}
