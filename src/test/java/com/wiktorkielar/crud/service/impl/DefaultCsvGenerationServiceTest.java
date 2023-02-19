package com.wiktorkielar.crud.service.impl;

import com.wiktorkielar.crud.model.EmployeeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static com.wiktorkielar.crud.CommonStrings.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToCompressingWhiteSpace;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCsvGenerationServiceTest {

    @Mock
    private DefaultEmployeeService employeeService;

    @InjectMocks
    private DefaultCsvGenerationService defaultCsvGenerationService;

    @Nested
    @DisplayName("getAllEmployeesAsCsv Test")
    class GetAllEmployeesAsCsvTest {

        @Test
        @DisplayName("Should Serialize Objects And Return CSV For getAllEmployeesAsCsv")
        void shouldSerializeObjectsAndReturnCsvForGetAllEmployeesAsCsv() throws IOException {

            //given
            EmployeeResponse employeeResponse = EmployeeResponse.builder()
                    .uuid(UUID_1)
                    .firstName(FIRST_NAME_1)
                    .lastName(LAST_NAME_1)
                    .jobRole(JOB_ROLE_1)
                    .created(LocalDateTime.parse(CREATED_1))
                    .build();

            String expectedCsv = "uuid,firstName,lastName,jobRole,created\n" +
                    "7fe7acdb-0bf4-428a-81ec-689fd2942084,John,Doe,Java Developer,2023-02-14T18:48:27.314759\n";

            when(employeeService.getAllEmployees()).thenReturn(List.of(employeeResponse));

            //when
            InputStreamResource inputStreamResource = defaultCsvGenerationService.getAllEmployeesAsCsv();
            String actualCsv = new String(inputStreamResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            //then
            assertThat(actualCsv, equalToCompressingWhiteSpace(expectedCsv));
        }
    }
}