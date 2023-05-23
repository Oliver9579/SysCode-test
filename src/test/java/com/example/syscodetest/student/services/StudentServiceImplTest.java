package com.example.syscodetest.student.services;

import com.example.syscodetest.exceptions.EmailAlreadyUsedException;
import com.example.syscodetest.exceptions.InvalidUUIDException;
import com.example.syscodetest.exceptions.StudentIdNotFoundException;
import com.example.syscodetest.student.models.dtos.NewStudentRequestDTO;
import com.example.syscodetest.student.models.dtos.StudentListDTO;
import com.example.syscodetest.student.models.entities.Student;
import com.example.syscodetest.student.repositories.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class StudentServiceImplTest {

  private StudentService studentService;

  @Mock
  private StudentRepository mockStudentRepository;

  private Student expectedStudent;
  private Student receivedStudent;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    this.studentService = Mockito.spy(new StudentServiceImpl(mockStudentRepository));
    expectedStudent = new Student();
    receivedStudent = new Student();
  }

  @Test
  public void testAddNewStudent() {
    NewStudentRequestDTO newStudentRequest = new NewStudentRequestDTO("Oli", "oli@gmail.com");
    Student savedStudent = new Student(UUID.randomUUID(), "Oli", "oli@gmail.com");
    Student newStudent = new Student("Oli", "oli@gmail.com");
    expectedStudent = new Student("Oli", "oli@gmail.com");
    doReturn(false).when(studentService).isEmailExist(anyString());
    doReturn(newStudent).when(studentService).convertToStudent(newStudentRequest);
    when(mockStudentRepository.save(newStudent)).thenReturn(savedStudent);

    receivedStudent = studentService.addNewStudent(new NewStudentRequestDTO("Oli", "oli@gmail.com"));

    expectedStudent = new Student("Oli", "oli@gmail.com");

    assertEquals(expectedStudent.getName(), receivedStudent.getName());
    assertEquals(expectedStudent.getEmail(), receivedStudent.getEmail());
    verify(mockStudentRepository, times(1)).save(newStudent);

  }

  @Test(expected = EmailAlreadyUsedException.class)
  public void testAddNewStudent_emailIsAlreadyExist() {
    doThrow(new EmailAlreadyUsedException()).when(studentService).isEmailExist(anyString());

    studentService.addNewStudent(new NewStudentRequestDTO("Oli", "oli@gmail.com"));

    verify(mockStudentRepository, times(1)).save(any());

  }

  @Test
  public void testFindById() {
    UUID randomUUID = UUID.randomUUID();
    Student student = new Student(randomUUID, "Oli", "oli@gmail.com");
    when(mockStudentRepository.findById(randomUUID)).thenReturn(Optional.of(student));

    receivedStudent = studentService.findById(randomUUID);

    expectedStudent = new Student("Oli", "oli@gmail.com");

    assertEquals(expectedStudent.getName(), receivedStudent.getName());
    assertEquals(expectedStudent.getEmail(), receivedStudent.getEmail());
    verify(mockStudentRepository, times(1)).findById(randomUUID);

  }

  @Test(expected = StudentIdNotFoundException.class)
  public void testFindById_whenStudentDoesNotExist() {
    when(mockStudentRepository.findById(any())).thenThrow(StudentIdNotFoundException.class);

    studentService.findById(any());

    verify(mockStudentRepository, times(0)).findById(any());

  }

  @Test
  public void testIsEmailExist() {
    when(mockStudentRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    boolean isExist = studentService.isEmailExist(anyString());

    assertFalse(isExist);
    verify(mockStudentRepository, times(1)).findByEmail(anyString());

  }

  @Test(expected = EmailAlreadyUsedException.class)
  public void testIsEmailExist_whenEmailAlreadyExist() {
    Student student = new Student("Oli", "oli@gmail.com");
    when(mockStudentRepository.findByEmail(anyString())).thenReturn(Optional.of(student));

    studentService.isEmailExist(anyString());

    verify(mockStudentRepository, times(0)).findByEmail(anyString());

  }

  @Test
  public void testGetAllStudent() {
    Student student1 = new Student("Oli", "oli@gmail.com");
    Student student2 = new Student("Oli2", "oli2@gmail.com");
    ArrayList<Student> studentList = new ArrayList<>(Arrays.asList(student1, student2));

    when(mockStudentRepository.findAll()).thenReturn(studentList);

    StudentListDTO receivedList = studentService.getAllStudent();

    StudentListDTO expectedList = new StudentListDTO(Arrays.asList(student1, student2));

    assertEquals(expectedList.getStudents().size(), receivedList.getStudents().size());
    assertEquals(expectedList.getStudents().get(0).getName(), receivedList.getStudents().get(0).getName());
    assertEquals(expectedList.getStudents().get(0).getEmail(), receivedList.getStudents().get(0).getEmail());

    verify(mockStudentRepository, times(1)).findAll();

  }

  @Test
  public void testGetAllStudent_whenListIsEmpty() {
    ArrayList<Student> studentList = new ArrayList<>();

    when(mockStudentRepository.findAll()).thenReturn(studentList);

    StudentListDTO receivedList = studentService.getAllStudent();

    StudentListDTO expectedList = new StudentListDTO(new ArrayList<>());

    assertEquals(expectedList.getStudents().size(), receivedList.getStudents().size());

    verify(mockStudentRepository, times(1)).findAll();

  }

  @Test
  public void testModifyStudentData() {
    UUID randomUUID = UUID.randomUUID();
    Student student = new Student(randomUUID, "Oli", "oli@gmail.com");
    NewStudentRequestDTO studentNewData = new NewStudentRequestDTO("newOli", "newoli@gmail.com");
    Student studentWithNewData = new Student(randomUUID, "newOli", "newoli@gmail.com");

    when(mockStudentRepository.findById(randomUUID)).thenReturn(Optional.of(student));
    doReturn(false).when(studentService).isEmailExist(studentNewData.getEmail());
    when(mockStudentRepository.save(student)).thenReturn(studentWithNewData);

    Student receivedStudent = studentService.modifyStudentData(randomUUID.toString(), studentNewData);

    Student expectedStudent = new Student(randomUUID, "newOli", "newoli@gmail.com");

    assertEquals(expectedStudent.getName(), receivedStudent.getName());
    assertEquals(expectedStudent.getEmail(), receivedStudent.getEmail());

    verify(mockStudentRepository, times(1)).findById(randomUUID);
    verify(mockStudentRepository, times(1)).save(student);

  }

  @Test(expected = InvalidUUIDException.class)
  public void testModifyStudentData_whenUUIDFormatIsInvalid() {

    when(mockStudentRepository.findById(any())).thenThrow(IllegalArgumentException.class);

    studentService.modifyStudentData(anyString(), any());

    verify(mockStudentRepository, times(1)).findById(any());
    verify(mockStudentRepository, times(0)).save(any());

  }

  @Test(expected = StudentIdNotFoundException.class)
  public void testModifyStudentData_whenStudentDoesNotExist() {

    when(mockStudentRepository.findById(any())).thenThrow(StudentIdNotFoundException.class);

    studentService.modifyStudentData(
            UUID.randomUUID().toString(), new NewStudentRequestDTO("Oli", "oli@gmail.com"));

    verify(mockStudentRepository, times(1)).findById(any());
    verify(mockStudentRepository, times(0)).save(any());

  }

  @Test(expected = EmailAlreadyUsedException.class)
  public void testModifyStudentData_whenEmailIsAlreadyExist() {
    UUID randomUUID = UUID.randomUUID();

    when(mockStudentRepository.findById(randomUUID))
            .thenReturn(Optional.of(new Student(randomUUID, "Oli", "oli@gmail.com")));
    doThrow(EmailAlreadyUsedException.class).when(studentService).isEmailExist("oli@gmail.com");

    studentService.modifyStudentData(
            randomUUID.toString(), new NewStudentRequestDTO("Oli", "oli@gmail.com"));

    verify(mockStudentRepository, times(1)).findById(randomUUID);
    verify(mockStudentRepository, times(0)).save(any());

  }

  @Test
  public void testDeleteStudent() {
    UUID randomUUID = UUID.randomUUID();

    when(mockStudentRepository.findById(randomUUID))
            .thenReturn(Optional.of(new Student(randomUUID, "Oli", "oli@gmail.com")));
    doNothing().when(mockStudentRepository).deleteById(randomUUID);

    studentService.deleteStudent(randomUUID.toString());

    verify(mockStudentRepository, times(1)).deleteById(randomUUID);
  }

  @Test(expected = InvalidUUIDException.class)
  public void testDeleteStudent_whenUUIDFormatIsInvalid() {

    when(mockStudentRepository.findById(any())).thenThrow(IllegalArgumentException.class);

    studentService.modifyStudentData(anyString(), any());

    verify(mockStudentRepository, times(1)).findById(any());
    verify(mockStudentRepository, times(0)).deleteById(any());

  }

  @Test(expected = StudentIdNotFoundException.class)
  public void testDeleteStudent_whenStudentDoesNotExist() {

    when(mockStudentRepository.findById(any())).thenThrow(StudentIdNotFoundException.class);

    studentService.modifyStudentData(
            UUID.randomUUID().toString(), new NewStudentRequestDTO("Oli", "oli@gmail.com"));

    verify(mockStudentRepository, times(1)).findById(any());
    verify(mockStudentRepository, times(0)).deleteById(any());

  }

  @Test
  public void testConvertToStudent() {
    expectedStudent = new Student("Oli", "oli@gmail.com");

    receivedStudent = studentService.convertToStudent(new NewStudentRequestDTO("Oli", "oli@gmail.com"));

    assertEquals(expectedStudent.getName(), receivedStudent.getName());
    assertEquals(expectedStudent.getEmail(), receivedStudent.getEmail());
  }

}