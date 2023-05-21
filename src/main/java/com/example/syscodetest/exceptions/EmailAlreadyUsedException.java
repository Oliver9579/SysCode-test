package com.example.syscodetest.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {

  public static final String message = "there is already a student in the database with the provided email";

  public EmailAlreadyUsedException() {
    super(message);
  }

}
