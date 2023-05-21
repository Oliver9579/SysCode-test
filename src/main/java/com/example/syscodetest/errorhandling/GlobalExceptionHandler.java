package com.example.syscodetest.errorhandling;

import com.example.syscodetest.exceptions.EmailAlreadyUsedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    if (e.getBindingResult().getFieldError().getCode().equals("Email")) {
      String message = "invalid email format";
      return ResponseEntity.status(400).body(new ErrorMessage(message));
    }
    String message = "please provide every necessary detail";
    return ResponseEntity.status(400).body(new ErrorMessage(message));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorMessage> handleEmptyRequestBodyException(HttpMessageNotReadableException e) {
    return ResponseEntity.status(400).body(new ErrorMessage("Request body is empty!"));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorMessage> handleInvalidEmailException(DataIntegrityViolationException e) {
    return ResponseEntity.status(400).body(new ErrorMessage(e.getLocalizedMessage()));
  }

  @ExceptionHandler(EmailAlreadyUsedException.class)
  public ResponseEntity<ErrorMessage> handlePlateAlreadyUsedExceptions(EmailAlreadyUsedException e) {
    return ResponseEntity.status(400).body(new ErrorMessage(e.getMessage()));
  }

}
