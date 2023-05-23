package com.example.syscodetest.student.services;

import com.example.syscodetest.exceptions.EmailAlreadyUsedException;
import com.example.syscodetest.exceptions.InvalidUUIDException;
import com.example.syscodetest.exceptions.StudentIdNotFoundException;
import com.example.syscodetest.student.models.dtos.NewStudentRequestDTO;
import com.example.syscodetest.student.models.dtos.StudentListDTO;
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
    } else {
      return false;
    }
  }

  @Override
  public StudentListDTO getAllStudent() {
    return new StudentListDTO(studentRepository.findAll());
  }

  @Override
  public Student modifyStudentData(String id, NewStudentRequestDTO studentNewData) {
    try {
      Student student = studentRepository.findById(UUID.fromString(id)).orElseThrow(StudentIdNotFoundException::new);
      isEmailExist(studentNewData.getEmail());
      student.setEmail(studentNewData.getEmail());
      student.setName(studentNewData.getName());
      return studentRepository.save(student);
    } catch (IllegalArgumentException e) {
      throw new InvalidUUIDException();
    }
  }

  @Override
  public void deleteStudent(String id) {
    try {
      studentRepository.findById(UUID.fromString(id)).orElseThrow(StudentIdNotFoundException::new);
      studentRepository.deleteById(UUID.fromString(id));
    } catch (IllegalArgumentException e) {
      throw new InvalidUUIDException();
    }
  }

  private Student convertToStudent(NewStudentRequestDTO newStudentRequest) {
    return new Student(newStudentRequest.getName(), newStudentRequest.getEmail());
  }

}
