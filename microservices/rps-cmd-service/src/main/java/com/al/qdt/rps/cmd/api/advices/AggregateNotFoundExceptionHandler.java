package com.al.qdt.rps.cmd.api.advices;

import com.al.qdt.common.api.advices.GlobalExceptionHandler;
import com.al.qdt.common.api.errors.ApiError;
import com.al.qdt.cqrs.exceptions.AggregateNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * Exception handler class for the {@link AggregateNotFoundException} custom exception.
 */
@Order(HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AggregateNotFoundExceptionHandler extends ResponseEntityExceptionHandler implements GlobalExceptionHandler {

    /**
     * Handle AggregateNotFoundException.
     *
     * @param e the AggregateNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(AggregateNotFoundException.class)
    protected ResponseEntity<Object> handleAggregateNotFoundException(
            AggregateNotFoundException e) {
        final var apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(e.getMessage());
        return buildResponseEntity(apiError);
    }
}
