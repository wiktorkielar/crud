package com.wiktorkielar.crud.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    public static final String MESSAGE_AT_LEAST_2_CHARACTERS = "must have at least 2 characters";

    private String uuid;
    @NotBlank
    @Size(min = 2, message = MESSAGE_AT_LEAST_2_CHARACTERS)
    private String firstName;
    @NotBlank
    @Size(min = 2, message = MESSAGE_AT_LEAST_2_CHARACTERS)
    private String lastName;
    @NotBlank
    @Size(min = 2, message = MESSAGE_AT_LEAST_2_CHARACTERS)
    private String jobRole;
}