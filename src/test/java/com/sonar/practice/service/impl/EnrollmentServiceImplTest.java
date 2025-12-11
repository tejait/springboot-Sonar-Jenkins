package com.sonar.practice.service.impl;

import com.sonar.practice.dto.EnrollmentRequest;
import com.sonar.practice.dto.EnrollmentResponse;
import com.sonar.practice.entity.Course;
import com.sonar.practice.entity.Enrollment;
import com.sonar.practice.entity.Student;
import com.sonar.practice.repository.CourseRepository;
import com.sonar.practice.repository.EnrollmentRepository;
import com.sonar.practice.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    EnrollmentServiceImpl enrollmentService;

    @Test
    void enrollStudent_success() {
        EnrollmentRequest req = new EnrollmentRequest();
        req.setStudentId(1L);
        req.setCourseId(2L);

        Student s = new Student(); s.setId(1L);
        Course c = new Course(); c.setId(2L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(s));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(c));

        Enrollment saved = new Enrollment(); saved.setId(10L); saved.setStudent(s); saved.setCourse(c);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(saved);

        EnrollmentResponse resp = enrollmentService.enrollStudent(req);
        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getStudentId()).isEqualTo(1L);
    }

    @Test
    void enrollStudent_missingStudent_throws() {
        EnrollmentRequest req = new EnrollmentRequest(); req.setStudentId(99L); req.setCourseId(2L);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> enrollmentService.enrollStudent(req));
    }

    @Test
    void cancelEnrollment_success() {
        Enrollment e = new Enrollment(); e.setId(7L); e.setStatus("ACTIVE");
        when(enrollmentRepository.findById(7L)).thenReturn(Optional.of(e));
        enrollmentService.cancelEnrollment(7L);
        assertThat(e.getStatus()).isEqualTo("CANCELLED");
    }

    @Test
    void getEnrollmentsForStudent_returnsList() {
        Enrollment e = new Enrollment(); e.setId(3L);
        when(enrollmentRepository.findByStudentId(1L)).thenReturn(List.of(e));
        List<EnrollmentResponse> list = enrollmentService.getEnrollmentsForStudent(1L);
        assertThat(list).hasSize(1);
    }
}
