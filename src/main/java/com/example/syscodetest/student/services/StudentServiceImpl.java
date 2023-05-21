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
      isEmailExist(studentNewData.getEmail());
      Student student = studentRepository.findById(formatUUIDString(id)).orElseThrow(StudentIdNotFoundException::new);
      student.setEmail(studentNewData.getEmail());
      student.setName(studentNewData.getName());
      return studentRepository.save(student);
  }

  @Override
  public void deleteStudent(String id) {
    studentRepository.findById(formatUUIDString(id)).orElseThrow(StudentIdNotFoundException::new);
    studentRepository.deleteById(formatUUIDString(id));
  }

  private Student convertToStudent(NewStudentRequestDTO newStudentRequest) {
    return new Student(newStudentRequest.getName(), newStudentRequest.getEmail());
  }

  private UUID formatUUIDString(String id) {
    if (id.replace("-","").length() < 32) throw new InvalidUUIDException();
    try {
      if (id.contains("-")) return UUID.fromString(id);
      StringBuilder sb = new StringBuilder(id);
      sb.insert(8, "-")
              .insert(13, "-")
              .insert(18, "-")
              .insert(23, "-");
      return UUID.fromString(sb.toString());
    } catch (IllegalArgumentException e) {
      throw new InvalidUUIDException();
    }
  }
}
