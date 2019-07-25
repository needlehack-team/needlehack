package org.needlehack.searchapi.controller

import com.weddini.throttling.ThrottlingException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {

    val log = LoggerFactory.getLogger(SearchController::class.java)

    @ExceptionHandler(value = arrayOf(Exception::class))
    fun internalError(oops: Exception): ResponseEntity<String> {
        log.error("Unexpected error ", oops)
        return ResponseEntity.status(500).body("Internal Error")
    }

    @ExceptionHandler(value = arrayOf(ThrottlingException::class))
    fun limitReached(oops: ThrottlingException): ResponseEntity<String> {
        log.error("Limit has been reached.", oops)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests. Limit has been reached. Please, perform request  less frequently")
    }

}