package com.kotlinspring.exceptionhandler

import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Component
@ControllerAdvice
class GlobalErrorHandler: ResponseEntityExceptionHandler() {

    companion object: KLogging()

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        // Still print out the really long and ugly default error message
        logger.error("MethodArgumentNotValidException observed: ${ex.message}", ex)

        // Prepare a better set of error messages
        val errors = ex.bindingResult.allErrors
            .map { error -> error.defaultMessage!! }
            .sorted()

        // Output the better and more condensed set of error messages
        logger.info("errors: $errors")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors.joinToString(", ") { it })
    }
}