package com.example.syscodetest.student.services;

import com.example.syscodetest.student.models.dtos.NewStudentRequestDTO;
import com.example.syscodetest.student.models.entities.Student;

import java.util.UUID;

public interface StudentService {

  Student addNewStudent(NewStudentRequestDTO newStudentRequest);

  Student findById(UUID id);

  boolean isEmailExist(String email);
}
