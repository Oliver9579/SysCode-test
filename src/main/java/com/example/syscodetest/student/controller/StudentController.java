package com.example.syscodetest.student.controller;

import com.example.syscodetest.student.models.dtos.NewStudentRequestDTO;
import com.example.syscodetest.student.models.entities.Student;
import com.example.syscodetest.student.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

  private StudentService studentService;

  @PostMapping
  public ResponseEntity<Student> addStudent(@Valid @RequestBody NewStudentRequestDTO newStudentRequest) {
    return ResponseEntity.status(201).body(studentService.addNewStudent(newStudentRequest));
  }

}
