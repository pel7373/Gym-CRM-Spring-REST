package org.gym;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
//@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        LOGGER.debug("Exception thrown {}, webRequest {}", ex, request);
        String bodyOfErrorResponse = "Argument or State of resource has a illegal value.";
        return handleExceptionInternal(ex, bodyOfErrorResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
//        LOGGER.debug("Exception thrown {}", e);
//        ErrorResponse errorResponse = ErrorResponse
//                .builder(e, ProblemDetail.forStatus(UNPROCESSABLE_ENTITY))
//                .build();
//        return ResponseEntity.unprocessableEntity().body(errorResponse);
//    }
}
