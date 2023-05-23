package com.example.syscodetest.student.models.dtos;

import com.example.syscodetest.student.models.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StudentListDTO {

  private List<Student> students;

}
