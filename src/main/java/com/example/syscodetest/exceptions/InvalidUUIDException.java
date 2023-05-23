package com.example.syscodetest.exceptions;

public class InvalidUUIDException extends RuntimeException {

  public static final String message = "invalid UUID format!";

  public InvalidUUIDException() {
    super(message);
  }

}
