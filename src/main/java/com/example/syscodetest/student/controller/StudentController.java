package com.example.syscodetest.student.controller;

import com.example.syscodetest.exceptions.InvalidUUIDException;
import com.example.syscodetest.student.models.dtos.NewStudentRequestDTO;
import com.example.syscodetest.student.models.dtos.StudentListDTO;
import com.example.syscodetest.student.models.entities.Student;
import com.example.syscodetest.student.services.StudentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

  private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
  private StudentService studentService;


  @PostMapping
  public ResponseEntity<Student> addStudent(@Valid @RequestBody NewStudentRequestDTO newStudentRequest) {
    logger.info("Received request to create student: Name={}, Email={}",
            newStudentRequest.getName(), newStudentRequest.getEmail());
    Student savedStudent = studentService.addNewStudent(newStudentRequest);
    logger.info("Student created with id: {}", savedStudent.getId());
    return ResponseEntity.status(201).body(savedStudent);
  }

  @GetMapping
  public ResponseEntity<StudentListDTO> getAllStudent() {
    logger.info("Received request to get all students");
    StudentListDTO students = studentService.getAllStudent();
    logger.info("Returning {} students", students.getStudents().size());
    return ResponseEntity.status(200).body(students);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Student> modifyStudentData(@PathVariable(name = "id") String id,
                                                   @Valid @RequestBody NewStudentRequestDTO studentNewData) {
      logger.info("Received request to modify student data with ID: {}", id);
      return ResponseEntity.ok(studentService.modifyStudentData(id, studentNewData));
  }

}
