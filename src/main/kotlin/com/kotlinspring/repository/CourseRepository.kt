package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CourseRepository: CrudRepository<Course, Int> {

    // To write functions that translate to JPA SQL queries:
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    fun findByNameContaining(courseName: String): List<Course>

    // Implementing the same function using raw SQL
    @Query(value = "SELECT * FROM COURSES WHERE name LIKE %?1%", nativeQuery = true)
    fun findCoursesByName(courseName: String): List<Course>
}