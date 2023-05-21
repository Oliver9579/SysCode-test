package com.example.syscodetest.errorhandling;

import com.example.syscodetest.exceptions.EmailAlreadyUsedException;
import com.example.syscodetest.exceptions.InvalidUUIDException;
import com.example.syscodetest.exceptions.StudentIdNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    if (e.getBindingResult().getFieldError().getCode().equals("Email")) {
      logger.error("The given email format is invalid!");
      String message = "invalid email format";
      return ResponseEntity.status(400).body(new ErrorMessage(message));
    }
    logger.error("Not every necessary detail provided!");
    String message = "please provide every necessary detail";
    return ResponseEntity.status(400).body(new ErrorMessage(message));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorMessage> handleEmptyRequestBodyException() {
    logger.error("The request body is empty!");
    return ResponseEntity.status(400).body(new ErrorMessage("Request body is empty!"));
  }

  @ExceptionHandler(EmailAlreadyUsedException.class)
  public ResponseEntity<ErrorMessage> handleEmailAlreadyUsedExceptions(EmailAlreadyUsedException e) {
    logger.error("The given email is already exist!");
    return ResponseEntity.status(400).body(new ErrorMessage(e.getMessage()));
  }

  @ExceptionHandler(InvalidUUIDException.class)
  public ResponseEntity<ErrorMessage> handleInvalidUUIDException(InvalidUUIDException e) {
    logger.error("The given UUID is not valid!");
    return ResponseEntity.status(400).body(new ErrorMessage(e.getMessage()));
  }

  @ExceptionHandler(StudentIdNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleStudentIdNotFoundException(StudentIdNotFoundException e) {
    logger.error("The student does not exist!");
    return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
  }

}
