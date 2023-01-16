package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDTO
import com.kotlinspring.util.courseEntityList
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService

    @Test
    fun addCourse() {

        // Build CourseDTO to pass to the POST endpoint
        val courseDTO = CourseDTO(
            null,
            "Building Restful APIs using SpringBoot and Kotlin",
            "Dilip Sundarraj"
        )

        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id = 1)

        // Perform the POST request
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        // If ID is not null, then save was successful
        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    @Test
    fun addCourse_validation() {

        // Build CourseDTO to pass to the POST endpoint
        val courseDTO = CourseDTO(
            null,
            "",
            ""
        )

        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id = 1)

        // Perform the POST request
        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun retrieveAllCourses() {

        // use returnsMany() because GET all returns List (Collection) of Courses
        every { courseServiceMockk.retrieveAllCourses() }.returnsMany(
            listOf(courseDTO(id = 1),
                courseDTO(id = 2, name = "Build Reactive Microservices using Spring WebFlux/SpringBoot"))
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Course::class.java)
            .returnResult()
            .responseBody

        println("courseDTOs: $courseDTOs")
        Assertions.assertEquals(2, courseDTOs!!.size)
    }

    @Test
    fun updateCourse() {
        // Create a course and save it to DB
        val course = Course(null,
            "Build Restful APIs using SpringBoot and Kotlin", "Development")

        // mock the update function
        every { courseServiceMockk.updateCourse(any(), any()) } returns courseDTO(id = 100,
            name = "Build Restful APIs using SpringBoot and Kotlin1")

        // Create a new course to update it with
        val updatedCourseDTO = CourseDTO(null,
            "Build Restful APIs using SpringBoot and Kotlin1", "Development")

        // perform PUT req and store response
        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build Restful APIs using SpringBoot and Kotlin1", updatedCourse!!.name)
    }

    @Test
    fun deleteCourse() {

        // mock a function call that does not return anything
        every { courseServiceMockk.deleteCourse(any()) } just runs

        // perform DELETE req and ensure isNoContent status
        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent
    }
}