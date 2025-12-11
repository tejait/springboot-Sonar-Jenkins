package com.sonar.practice.service.impl;

import com.sonar.practice.dto.StudentRequest;
import com.sonar.practice.dto.StudentResponse;
import com.sonar.practice.entity.Student;
import com.sonar.practice.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl studentService;

    @Test
    void createStudent_success() {
        StudentRequest req = new StudentRequest();
        req.setFirstName("John");
        req.setLastName("Doe");
        req.setEmail("john.doe@example.com");
        req.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(studentRepository.existsByEmail(req.getEmail())).thenReturn(false);
        Student saved = new Student();
        saved.setId(1L);
        saved.setFirstName(req.getFirstName());
        saved.setLastName(req.getLastName());
        saved.setEmail(req.getEmail());
        saved.setDateOfBirth(req.getDateOfBirth());
        saved.setCreatedAt(OffsetDateTime.now());
        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        StudentResponse resp = studentService.createStudent(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getEmail()).isEqualTo(req.getEmail());
    }

    @Test
    void createStudent_duplicateEmail_throws() {
        StudentRequest req = new StudentRequest();
        req.setEmail("exists@example.com");
        when(studentRepository.existsByEmail(req.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(req));
    }

    @Test
    void updateStudent_notFound_throws() {
        when(studentRepository.findById(42L)).thenReturn(Optional.empty());
        StudentRequest req = new StudentRequest();
        assertThrows(EntityNotFoundException.class, () -> studentService.updateStudent(42L, req));
    }

    @Test
    void getStudent_and_listStudents() {
        Student s = new Student();
        s.setId(5L);
        s.setFirstName("A");
        s.setLastName("B");
        s.setEmail("a.b@example.com");
        s.setDateOfBirth(LocalDate.of(1980, 5, 5));
        s.setCreatedAt(OffsetDateTime.now());

        when(studentRepository.findById(5L)).thenReturn(Optional.of(s));
        StudentResponse resp = studentService.getStudent(5L);
        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(5L);

        when(studentRepository.findAll()).thenReturn(List.of(s));
        List<StudentResponse> list = studentService.listStudents();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getEmail()).isEqualTo(s.getEmail());
    }

    @Test
    void deleteStudent_callsRepository() {
        studentService.deleteStudent(9L);
        verify(studentRepository).deleteById(9L);
    }
}
