package com.blastoff.campaign_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The global exception handler for the campaign service.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles the exception where the campaign is not found.
     *
     * @param exception The exception containing details of the campaign.
     *
     * @return A {@link ProblemDetail} with HTTP {@link HttpStatus#NOT_FOUND}
     * and the exception message
     */
    @ExceptionHandler(CampaignNotFoundException.class)
    public ProblemDetail notFound(CampaignNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND
            , exception.getMessage());
    }
    /**
     * Handles bad requests.
     *
     * @param exception The exception containing details about the invalid
     * request.
     *
     * @return A {@link ProblemDetail} with HTTP {@link HttpStatus#BAD_REQUEST}
     * and the exception message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail badRequest(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST
            , exception.getMessage());
    }
}