package com.blog.blog.exceptions;

import com.blog.blog.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String customException = e.getBindingResult().getFieldErrors().stream()
                .map(x -> x.getField() + " " + x.getDefaultMessage())
                .collect(Collectors.joining(","));

        return ResponseEntity.badRequest().body(new ExceptionResponse(customException));
    }

    @ExceptionHandler(InvalidPaginationException.class)
    public ProblemDetail handleInvalidPaginationException(InvalidPaginationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(LikeAlreadyExistsException.class)
    public ProblemDetail handleLikeAlreadyExistsException(LikeAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ProblemDetail handleLikeNotFoundException(LikeNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(PostIllegalArgumentException.class)
    public ProblemDetail handlePostIllegalArgumentException(PostIllegalArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ProblemDetail handlePostNotFoundException(PostNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(UserNotExistingException.class)
    public ProblemDetail handleUserNotExistingException(UserNotExistingException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserNotOwnerException.class)
    public ProblemDetail handleUserNotOwnerException(UserNotOwnerException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }


}
