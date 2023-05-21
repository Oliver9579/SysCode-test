package com.example.syscodetest.student.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class NewStudentRequestDTO {

  @NotBlank
  private String name;
  @Email
  @NotBlank
  private String email;

}
