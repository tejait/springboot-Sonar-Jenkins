package com.sonar.practice.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StudentResponse {

    Long id;
    String firstName;
    String lastName;
    String email;
    LocalDate dateOfBirth;
    OffsetDateTime createdAt;
}
