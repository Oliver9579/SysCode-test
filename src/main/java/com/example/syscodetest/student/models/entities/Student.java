package com.example.syscodetest.student.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(generator = "uuid4")
  @Type(type = "uuid-char")
  private UUID id;

  @NotBlank
  @Column(name = "student_name", length = 50)
  private String name;

  @NotBlank
  @Column(name = "student_email", unique = true)
  private String email;

  public Student(String name, String email) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.email = email;
  }

}
