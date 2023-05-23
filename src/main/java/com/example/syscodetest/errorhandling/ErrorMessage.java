package com.example.syscodetest.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

  private String status;
  private String message;

  public ErrorMessage(String message) {
    this.status = "error";
    this.message = message;
  }

}
