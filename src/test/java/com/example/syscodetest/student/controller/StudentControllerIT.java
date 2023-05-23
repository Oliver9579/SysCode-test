package com.example.syscodetest.student.controller;

import com.example.syscodetest.student.models.dtos.NewStudentRequestDTO;
import com.example.syscodetest.student.models.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerIT {

  @Autowired
  private MockMvc mockMvc;
  private ObjectMapper mapper;
  private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          StandardCharsets.UTF_8);

  @Before
  public void setUp() throws Exception {
    mapper = new ObjectMapper();
  }

  @Test
  public void addStudent_should_returnAStudentWithGeneratedUUID_when_everyInputAreValid() throws Exception {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Oli2", "oli2@gmail.com");
    MvcResult result = mockMvc.perform(post("/api/students")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(newStudentRequest)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name", is("Oli2")))
            .andExpect(jsonPath("$.email", is("oli2@gmail.com")))
            .andReturn();

    Student expectedStudent = new Student("Oli2", "oli2@gmail.com");

    Student receivedStudent = mapper.readValue(result.getResponse().getContentAsString(), Student.class);

    assertNotNull(receivedStudent.getId());
    assertEquals(expectedStudent.getName(), receivedStudent.getName());
    assertEquals(expectedStudent.getEmail(), receivedStudent.getEmail());
  }

  @Test
  public void addStudent_should_returnAnError_when_givenEmailAlreadyExists() throws Exception {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Juli2", "juli@gmail.com");
    mockMvc.perform(post("/api/students")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(newStudentRequest)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("there is already a student in the database with the provided email")));
  }

  @Test
  public void addStudent_should_returnAnError_when_requestBodyIsEmpty() throws Exception {
    mockMvc.perform(post("/api/students")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString("{}")))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Request body is empty!")));
  }

  @Test
  public void addStudent_should_returnAnError_when_givenEmailIsInvalid() throws Exception {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Oli2", "oligmail.com");
    mockMvc.perform(post("/api/students")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(newStudentRequest)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("invalid email format")));
  }

  @Test
  public void addStudent_should_returnAnError_when_someInputIsMissing() throws Exception {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Oli2", "");
    mockMvc.perform(post("/api/students")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(newStudentRequest)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("please provide every necessary detail")));
  }

  @Test
  public void getAllStudent_should_returnAListWithTheStudent_when_theDatabaseNotEmpty() throws Exception {
    mockMvc.perform(get("/api/students"))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.students").isArray())
            .andExpect(jsonPath("$.students").isNotEmpty());
  }

  @Test
  public void modifyStudentData_should_returnTheStudentWithTheModifiedData_when_everyInputAreValidAndTheStudentIsExist()
          throws Exception {
    NewStudentRequestDTO studentNewData = new NewStudentRequestDTO("New Oli", "newoli@gmail.com");
    MvcResult result = mockMvc.perform(put("/api/students/2d629e0e-2c1d-458b-b936-4ba68ff3d59c")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(studentNewData)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is("2d629e0e-2c1d-458b-b936-4ba68ff3d59c")))
            .andExpect(jsonPath("$.name", is("New Oli")))
            .andExpect(jsonPath("$.email", is("newoli@gmail.com")))
            .andReturn();

    Student expectedStudent = new Student("New Oli", "newoli@gmail.com");

    Student receivedStudent = mapper.readValue(result.getResponse().getContentAsString(), Student.class);

    assertNotNull(receivedStudent.getId());
    assertEquals(expectedStudent.getName(), receivedStudent.getName());
    assertEquals(expectedStudent.getEmail(), receivedStudent.getEmail());
  }

  @Test
  public void modifyStudentData_should_returnAnError_when_givenUUIDFormatIsInvalid() throws Exception {
    NewStudentRequestDTO studentNewData = new NewStudentRequestDTO("New Oli", "newoli@gmail.com");
    mockMvc.perform(put("/api/students/wrong uuid format")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(studentNewData)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("invalid UUID format!")));
  }

  @Test
  public void modifyStudentData_should_returnAnError_when_givenUUIDFormatIsValidButStudentDoesNotExist()
          throws Exception {
    NewStudentRequestDTO studentNewData = new NewStudentRequestDTO("New Oli", "newoli@gmail.com");
    mockMvc.perform(put("/api/students/73482d64-262d-4f7d-9a10-ec5ee0a77687")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(studentNewData)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Student not found")));
  }

  @Test
  public void modifyStudentData_should_returnAnError_when_givenNewEmailAlreadyExists() throws Exception {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Juli2", "juli@gmail.com");
    mockMvc.perform(put("/api/students/9f22e3a5-4589-4516-8966-21cef7a80580")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(newStudentRequest)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("there is already a student in the database with the provided email")));
  }

  @Test
  public void modifyStudentData_should_returnAnError_when_requestBodyIsEmpty() throws Exception {
    mockMvc.perform(put("/api/students/9f22e3a5-4589-4516-8966-21cef7a80580")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString("{}")))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Request body is empty!")));
  }

  @Test
  public void modifyStudentData_should_returnAnError_when_givenEmailIsInvalid() throws Exception {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Oli2", "oligmail.com");
    mockMvc.perform(put("/api/students/9f22e3a5-4589-4516-8966-21cef7a80580")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(newStudentRequest)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("invalid email format")));
  }

  @Test
  public void modifyStudentData_should_returnAnError_when_someInputIsMissing() throws Exception {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Oli2", "");
    mockMvc.perform(put("/api/students/9f22e3a5-4589-4516-8966-21cef7a80580")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(newStudentRequest)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("please provide every necessary detail")));
  }

  @Test
  public void deleteStudent_should_returnNoContentStatus_whenDeleteAStudent() throws Exception {
    mockMvc.perform(delete("/api/students/9ad00b0b-cd3d-445e-85bb-309ee9d312cc"))
            .andExpect(status().isNoContent());
  }

  @Test
  public void deleteStudent_should_returnAnError_when_givenUUIDFormatIsInvalid() throws Exception {
    NewStudentRequestDTO studentNewData = new NewStudentRequestDTO("New Oli", "newoli@gmail.com");
    mockMvc.perform(delete("/api/students/wrong uuid format")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(studentNewData)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("invalid UUID format!")));
  }

  @Test
  public void deleteStudent_should_returnAnError_when_givenUUIDFormatIsValidButStudentDoesNotExist()
          throws Exception {
    NewStudentRequestDTO studentNewData = new NewStudentRequestDTO("New Oli", "newoli@gmail.com");
    mockMvc.perform(delete("/api/students/73482d64-262d-4f7d-9a10-ec5ee0a77687")
                    .contentType(contentType)
                    .content(mapper.writeValueAsString(studentNewData)))
            .andExpect(content().contentTypeCompatibleWith(contentType))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is("error")))
            .andExpect(jsonPath("$.message", is("Student not found")));
  }

}