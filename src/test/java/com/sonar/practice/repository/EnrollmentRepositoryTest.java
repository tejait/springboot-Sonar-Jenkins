package com.sonar.practice.repository;

import com.sonar.practice.entity.Course;
import com.sonar.practice.entity.Enrollment;
import com.sonar.practice.entity.Student;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
 class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void saveAndFindByStudentId() {
        Student s = new Student();
        s.setFirstName("Enroll");
        s.setLastName("Student");
        s.setEmail("enroll.student@example.com");
        s.setDateOfBirth(LocalDate.of(1995, 5, 5));
        s = studentRepository.save(s);

        Course c = new Course();
        c.setCode("CS201");
        c.setName("Algorithms");
        c.setFee(new BigDecimal("200.00"));
        c = courseRepository.save(c);

        Enrollment e = new Enrollment();
        e.setStudent(s);
        e.setCourse(c);

        enrollmentRepository.save(e);

        List<Enrollment> results = enrollmentRepository.findByStudentId(s.getId());
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getStudent().getId()).isEqualTo(s.getId());
    }
}