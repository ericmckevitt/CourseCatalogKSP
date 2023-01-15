package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun addCourse() {

        // Build CourseDTO to pass to the POST endpoint
        val courseDTO = CourseDTO(
            null,
            "Building Restful APIs using SpringBoot and Kotlin",
            "Dilip Sundarraj"
        )

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
}