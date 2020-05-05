package com.rach.tollparking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

  private static final String INTERNAL_ERROR_MESSAGE = "Internal System error occurred";

  @ExceptionHandler public ResponseEntity<ErrorResponse> handleException(ParkingSlotCheckinException exc) {
    ErrorResponse error = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, exc.getMessage(), LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler public ResponseEntity<ErrorResponse> handleException(ParkingSlotCheckoutException exc) {
    ErrorResponse error = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, exc.getMessage(), LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler public ResponseEntity<ErrorResponse> handleException(Exception exc) {
    ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE,
        LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler public ResponseEntity<ErrorResponse> handleException(ParkingSlotAvailabilityException exc) {
    ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exc.getMessage(), LocalDateTime.now());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}
