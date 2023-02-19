package com.wiktorkielar.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiktorkielar.crud.exception.CsvGenerationException;
import com.wiktorkielar.crud.exception.NoEmployeesFoundException;
import com.wiktorkielar.crud.model.EmployeeResponse;
import com.wiktorkielar.crud.service.CsvGenerationService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

import static com.wiktorkielar.crud.util.CommonStrings.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CsvEmployeeController.class)
class CsvEmployeeControllerTest {

    @Value("${api.base.path}")
    private String apiBasePath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CsvGenerationService csvGenerationService;

    private EmployeeResponse employeeResponse;

    @BeforeEach
    public void setup() {
        employeeResponse = EmployeeResponse.builder()
                .uuid(UUID_1)
                .firstName(FIRST_NAME_1)
                .lastName(LAST_NAME_1)
                .jobRole(JOB_ROLE_1)
                .created(LocalDateTime.parse(CREATED_1))
                .build();
    }

    @Nested
    @DisplayName("getAllEmployees Test")
    class GetAllEmployeesTest {

        @Test
        @DisplayName("Should Return Correct Headers And Http Status 200 For getAllEmployees")
        void shouldReturnCorrectHeadersAndHttpStatus200ForGetAllEmployees() throws Exception {

            when(csvGenerationService.getAllEmployeesAsCsv()).thenReturn(new InputStreamResource(new ByteArrayInputStream(EMPTY_STRING.getBytes())));

            //when
            //then
            mockMvc.perform(get(apiBasePath + "/csv/employees")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=all_employees.csv"))
                    .andExpect(header().string("Content-Type", "text/csv"));
        }

        @Test
        @DisplayName("Should Not Serialize Object And Return Http Status 404 For getAllEmployees")
        void shouldNotSerializeObjectAndReturnHttpStatus404ForGetAllEmployees() throws Exception {

            //given
            String expectedMessage = "no employees found";

            when(csvGenerationService.getAllEmployeesAsCsv()).thenThrow(new NoEmployeesFoundException());

            //when
            ResultActions resultActions = mockMvc.perform(get(apiBasePath + "/csv/employees")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            //then
            resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
        }

        @Test
        @DisplayName("Should Not Serialize Object And Return Http Status 500 For getAllEmployees")
        void shouldNotSerializeObjectAndReturnHttpStatus500ForGetAllEmployees() throws Exception {

            //given
            String expectedMessage = "CSV generation error for " + ALL_EMPLOYEES;

            when(csvGenerationService.getAllEmployeesAsCsv()).thenThrow(new CsvGenerationException(ALL_EMPLOYEES));

            //when
            ResultActions resultActions = mockMvc.perform(get(apiBasePath + "/csv/employees")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            //then
            resultActions.andExpect(MockMvcResultMatchers.status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
        }
    }
}