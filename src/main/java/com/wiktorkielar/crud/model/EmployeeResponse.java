package com.wiktorkielar.crud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class EmployeeResponse {
    private String uuid;
    private String firstName;
    private String lastName;
    private String jobRole;
    private LocalDateTime created;
}
