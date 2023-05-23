package com.example.syscodetest.exceptions;

public class StudentIdNotFoundException extends RuntimeException {

  public static final String message = "Student not found";

  public StudentIdNotFoundException() {
    super(message);
  }

}
