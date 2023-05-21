package com.example.syscodetest.student.repositories;

import com.example.syscodetest.student.models.entities.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends CrudRepository<Student, UUID> {

  Optional<Student> findByEmail(String email);
}
