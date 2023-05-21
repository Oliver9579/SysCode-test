package com.example.syscodetest.student.services;

import com.example.syscodetest.exceptions.EmailAlreadyUsedException;
import com.example.syscodetest.exceptions.StudentIdNotFoundException;
import com.example.syscodetest.student.models.dtos.NewStudentRequestDTO;
import com.example.syscodetest.student.models.entities.Student;
import com.example.syscodetest.student.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

  private StudentRepository studentRepository;

  @Override
  public Student addNewStudent(NewStudentRequestDTO newStudentRequest) {
    isEmailExist(newStudentRequest.getEmail());
    return studentRepository.save(convertToStudent(newStudentRequest));
  }

  @Override
  public Student findById(UUID id) {
    return studentRepository.findById(id).orElseThrow(StudentIdNotFoundException::new);
  }

  @Override
  public boolean isEmailExist(String email) {
    if (studentRepository.findByEmail(email).isPresent()) {
      throw new EmailAlreadyUsedException();
    }else {
      return false;
    }
  }

  private Student convertToStudent(NewStudentRequestDTO newStudentRequest) {
    return new Student(newStudentRequest.getName(), newStudentRequest.getEmail());
  }
}
