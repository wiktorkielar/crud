package com.wiktorkielar.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiktorkielar.crud.exception.EmployeeAlreadyExistsException;
import com.wiktorkielar.crud.exception.EmployeeNotFoundException;
import com.wiktorkielar.crud.exception.NoEmployeesFoundException;
import com.wiktorkielar.crud.model.EmployeeRequest;
import com.wiktorkielar.crud.model.EmployeeResponse;
import com.wiktorkielar.crud.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static com.wiktorkielar.crud.CommonStrings.*;
import static com.wiktorkielar.crud.model.EmployeeRequest.MESSAGE_AT_LEAST_2_CHARACTERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JsonEmployeeController.class)
class JsonEmployeeControllerTest {

    @Value("${api.base.path}")
    private String apiBasePath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

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
    @DisplayName("createEmployee Test")
    class CreateEmployeeTest {

        @Test
        @DisplayName("Should Serialize And Validate Correct Object With Null UUID And Return Http Status 201 For createEmployee")
        void shouldSerializeAndValidateCorrectObjectWithNullUuidAndReturnHttpStatus201ForCreateEmployee() throws Exception {

            //given
            EmployeeRequest employeeRequest = EmployeeRequest.builder()
                    .uuid(null)
                    .firstName(FIRST_NAME_1)
                    .lastName(LAST_NAME_1)
                    .jobRole(JOB_ROLE_1)
                    .build();

            //given
            //when
            //then
            performTestWithCustomEmployeeRequestAndReturnHttpStatus201ForCreateEmployee(employeeRequest);
        }

        @Test
        @DisplayName("Should Serialize And Validate Correct Object With Empty UUID And Return Http Status 201 For createEmployee")
        void shouldSerializeAndValidateCorrectObjectWithEmptyUuidAndReturnHttpStatus201ForCreateEmployee() throws Exception {

            //given
            EmployeeRequest employeeRequest = EmployeeRequest.builder()
                    .uuid(EMPTY_STRING)
                    .firstName(FIRST_NAME_1)
                    .lastName(LAST_NAME_1)
                    .jobRole(JOB_ROLE_1)
                    .build();

            //given
            //when
            //then
            performTestWithCustomEmployeeRequestAndReturnHttpStatus201ForCreateEmployee(employeeRequest);
        }

        @Test
        @DisplayName("Should Serialize And Validate Correct Object With UUID Not Matching Existing One And Return Http Status 201 For createEmployee")
        void shouldSerializeAndValidateCorrectObjectWithUuidNotMatchingExistingOneAndReturnHttpStatus201ForCreateEmployee() throws Exception {

            //given
            EmployeeRequest employeeRequest = EmployeeRequest.builder()
                    .uuid(UUID_2)
                    .firstName(FIRST_NAME_1)
                    .lastName(LAST_NAME_1)
                    .jobRole(JOB_ROLE_1)
                    .build();

            //given
            //when
            //then
            performTestWithCustomEmployeeRequestAndReturnHttpStatus201ForCreateEmployee(employeeRequest);
        }

        @Test
        @DisplayName("Should Not Serialize Object With Existing UUID Value And Return Http Status 302 For createEmployee")
        void shouldNotSerializeObjectWithExistingUuidValueAndReturnHttpStatus302ForCreateEmployee() throws Exception {

            //given
            EmployeeRequest employeeRequest = EmployeeRequest.builder()
                    .uuid(UUID_1)
                    .firstName(FIRST_NAME_1)
                    .lastName(LAST_NAME_1)
                    .jobRole(JOB_ROLE_1)
                    .build();

            String expectedMessage = "employee with uuid 7fe7acdb-0bf4-428a-81ec-689fd2942084 already exists";

            when(employeeService.createEmployee(employeeRequest)).thenThrow(new EmployeeAlreadyExistsException(UUID_1));

            //when
            ResultActions resultActions = mockMvc.perform(post(apiBasePath + "/json/employees")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(employeeRequest)));

            //then
            resultActions.andExpect(MockMvcResultMatchers.status().isSeeOther())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
        }

        @Test
        @DisplayName("Should Not Serialize Object With Null Value And Return Http Status 400 For createEmployee")
        void shouldNotSerializeObjectWithNullValueAndReturnHttpStatus400ForCreateEmployee() throws Exception {

            //given
            EmployeeRequest employeeRequest = null;

            //when
            ResultActions resultActions = mockMvc.perform(post(apiBasePath + "/json/employees")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(employeeRequest)));
            //then
            resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        @DisplayName("Should Validate Incorrect Object And Return Http Status 400 for createEmployee")
        void shouldValidateIncorrectObjectAndReturnHttpStatus400forCreateEmployee() throws Exception {

            //given
            EmployeeRequest employeeRequest = EmployeeRequest.builder()
                    .firstName(FIRST_NAME_1_FIRST_CHAR)
                    .lastName(LAST_NAME_1_FIRST_CHAR)
                    .jobRole(JOB_ROLE_1_FIRST_CHAR)
                    .build();

            //when
            ResultActions resultActions = mockMvc.perform(post(apiBasePath + "/json/employees")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(employeeRequest)));

            //then
            resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(MESSAGE_AT_LEAST_2_CHARACTERS)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(MESSAGE_AT_LEAST_2_CHARACTERS)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.jobRole", CoreMatchers.is(MESSAGE_AT_LEAST_2_CHARACTERS)));
        }

        private void performTestWithCustomEmployeeRequestAndReturnHttpStatus201ForCreateEmployee(EmployeeRequest employeeRequest) throws Exception {

            //given
            String expectedResponse = "{\n" +
                    "    \"uuid\": \"7fe7acdb-0bf4-428a-81ec-689fd2942084\",\n" +
                    "    \"firstName\": \"John\",\n" +
                    "    \"lastName\": \"Doe\",\n" +
                    "    \"jobRole\": \"Java Developer\",\n" +
                    "    \"created\": \"2023-02-14T18:48:27.314759\"\n" +
                    "}";

            when(employeeService.createEmployee(employeeRequest)).thenReturn(employeeResponse);

            //when
            MvcResult mvcResult = mockMvc.perform(post(apiBasePath + "/json/employees")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(employeeRequest)))
                    .andExpect(status().isCreated())
                    .andReturn();

            String actualResponse = mvcResult.getResponse().getContentAsString();

            //then
            assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);
        }
    }


    @Nested
    @DisplayName("getEmployee Test")
    class GetEmployeeTest {

        @Test
        @DisplayName("Should Serialize Object And Return Http Status 200 for getEmployee")
        void shouldSerializeObjectAndReturnHttpStatus200forGetEmployee() throws Exception {

            //given
            String expectedResponse = "{\n" +
                    "    \"uuid\": \"7fe7acdb-0bf4-428a-81ec-689fd2942084\",\n" +
                    "    \"firstName\": \"John\",\n" +
                    "    \"lastName\": \"Doe\",\n" +
                    "    \"jobRole\": \"Java Developer\",\n" +
                    "    \"created\": \"2023-02-14T18:48:27.314759\"\n" +
                    "}";

            when(employeeService.getEmployee(UUID_1)).thenReturn(employeeResponse);

            //when
            MvcResult mvcResult = mockMvc.perform(get(apiBasePath + "/json/employees/{uuid}", UUID_1)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponse = mvcResult.getResponse().getContentAsString();

            //then
            assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);
        }

        @Test
        @DisplayName("Should Not Serialize Object And Return Http Status 400 For getEmployee")
        void shouldNotSerializeObjectAndReturnHttpStatus400ForGetEmployee() throws Exception {

            //given
            String expectedMessage = "employee with uuid " + UUID_1 + " not found";

            when(employeeService.getEmployee(UUID_1)).thenThrow(new EmployeeNotFoundException(UUID_1));

            //when
            ResultActions resultActions = mockMvc.perform(get(apiBasePath + "/json/employees/{uuid}", UUID_1)
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            //then
            resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
        }
    }

    @Nested
    @DisplayName("getAllEmployees Test")
    class GetAllEmployeesTest {

        @Test
        @DisplayName("Should Serialize Objects And Return Http Status 200 For getAllEmployees")
        void shouldSerializeObjectsAndReturnHttpStatus200ForGetAllEmployees() throws Exception {

            //given
            String expectedResponse = "[\n" +
                    "    {\n" +
                    "        \"uuid\": \"7fe7acdb-0bf4-428a-81ec-689fd2942084\",\n" +
                    "        \"firstName\": \"John\",\n" +
                    "        \"lastName\": \"Doe\",\n" +
                    "        \"jobRole\": \"Java Developer\",\n" +
                    "        \"created\": \"2023-02-14T18:48:27.314759\"\n" +
                    "    }\n" +
                    "]";

            when(employeeService.getAllEmployees()).thenReturn(List.of(employeeResponse));

            //when
            MvcResult mvcResult = mockMvc.perform(get(apiBasePath + "/json/employees")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResponse = mvcResult.getResponse().getContentAsString();

            //then
            assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);
        }

        @Test
        @DisplayName("Should Not Serialize Object And Return Http Status 404 For getAllEmployees")
        void shouldNotSerializeObjectAndReturnHttpStatus404ForGetAllEmployees() throws Exception {

            //given
            String expectedMessage = "no employees found";

            when(employeeService.getAllEmployees()).thenThrow(new NoEmployeesFoundException());

            //when
            ResultActions resultActions = mockMvc.perform(get(apiBasePath + "/json/employees")
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            //then
            resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
        }


        @Nested
        @DisplayName("updateEmployee Test")
        class UpdateEmployeeTest {

            @Test
            @DisplayName("Should Serialize And Validate Correct Object With UUID Matching Existing One And Return Http Status 200 For updateEmployee")
            void shouldSerializeAndValidateCorrectObjectWithUUIDMatchingExistingOneAndReturnHttpStatus200ForUpdateEmployee() throws Exception {

                //given
                EmployeeRequest employeeRequest = EmployeeRequest.builder()
                        .uuid(UUID_1)
                        .firstName(FIRST_NAME_1)
                        .lastName(LAST_NAME_1)
                        .jobRole(JOB_ROLE_1)
                        .build();

                String expectedResponse = "{\n" +
                        "    \"uuid\": \"7fe7acdb-0bf4-428a-81ec-689fd2942084\",\n" +
                        "    \"firstName\": \"John\",\n" +
                        "    \"lastName\": \"Doe\",\n" +
                        "    \"jobRole\": \"Java Developer\",\n" +
                        "    \"created\": \"2023-02-14T18:48:27.314759\"\n" +
                        "}";

                when(employeeService.updateEmployee(employeeRequest)).thenReturn(EmployeeResponse.builder()
                        .uuid(UUID_1)
                        .firstName(employeeRequest.getFirstName())
                        .lastName(employeeRequest.getLastName())
                        .jobRole(employeeRequest.getJobRole())
                        .created(LocalDateTime.parse(CREATED_1))
                        .build());

                //when
                MvcResult mvcResult = mockMvc.perform(put(apiBasePath + "/json/employees")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(employeeRequest)))
                        .andExpect(status().isOk())
                        .andReturn();

                String actualResponse = mvcResult.getResponse().getContentAsString();

                //then
                assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse);
            }

            @Test
            @DisplayName("Should Validate Incorrect Object And Return Http Status 400 for updateEmployee")
            void shouldValidateIncorrectObjectAndReturnHttpStatus400ForUpdateEmployee() throws Exception {

                //given
                EmployeeRequest employeeRequest = EmployeeRequest.builder()
                        .firstName(FIRST_NAME_1_FIRST_CHAR)
                        .lastName(LAST_NAME_1_FIRST_CHAR)
                        .jobRole(JOB_ROLE_1_FIRST_CHAR)
                        .build();

                //when
                ResultActions resultActions = mockMvc.perform(post(apiBasePath + "/json/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(employeeRequest)));

                //then
                resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(MESSAGE_AT_LEAST_2_CHARACTERS)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(MESSAGE_AT_LEAST_2_CHARACTERS)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.jobRole", CoreMatchers.is(MESSAGE_AT_LEAST_2_CHARACTERS)));
            }

            @Test
            @DisplayName("Should Not Serialize Object With Null Uuid And Return Http Status 404 For updateEmployee")
            void shouldNotSerializeObjectWithNullUuidAndReturnHttpStatus404ForUpdateEmployee() throws Exception {

                //given
                EmployeeRequest employeeRequest = EmployeeRequest.builder()
                        .uuid(null)
                        .firstName(FIRST_NAME_1)
                        .lastName(LAST_NAME_1)
                        .jobRole(JOB_ROLE_1)
                        .build();

                String expectedMessage = "employee with uuid null not found";

                when(employeeService.updateEmployee(employeeRequest)).thenThrow(new EmployeeNotFoundException(employeeRequest.getUuid()));

                //when
                ResultActions resultActions = mockMvc.perform(put(apiBasePath + "/json/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(employeeRequest)));

                //then
                resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
            }

            @Test
            @DisplayName("Should Not Serialize Object With Empty UUID And Return Http Status 404 For updateEmployee")
            void shouldNotSerializeObjectWithEmptyUUIDAndReturnHttpStatus404ForUpdateEmployee() throws Exception {

                //given
                EmployeeRequest employeeRequest = EmployeeRequest.builder()
                        .uuid(EMPTY_STRING)
                        .firstName(FIRST_NAME_1)
                        .lastName(LAST_NAME_1)
                        .jobRole(JOB_ROLE_1)
                        .build();

                String expectedMessage = "employee with uuid of empty string not found";

                when(employeeService.updateEmployee(employeeRequest)).thenThrow(new EmployeeNotFoundException(employeeRequest.getUuid()));

                //when
                ResultActions resultActions = mockMvc.perform(put(apiBasePath + "/json/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(employeeRequest)));

                //then
                resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
            }

            @Test
            @DisplayName("Should Not Serialize Object With UUID Not Matching Existing One And Return Http Status 404 For updateEmployee")
            void shouldNotSerializeObjectWithUUIDNotMatchingExistingOneAndReturnHttpStatus404ForUpdateEmployee() throws Exception {

                //given
                EmployeeRequest employeeRequest = EmployeeRequest.builder()
                        .uuid(UUID_2)
                        .firstName(FIRST_NAME_1)
                        .lastName(LAST_NAME_1)
                        .jobRole(JOB_ROLE_1)
                        .build();

                String expectedMessage = "employee with uuid " + UUID_2 + " not found";

                when(employeeService.updateEmployee(employeeRequest)).thenThrow(new EmployeeNotFoundException(employeeRequest.getUuid()));

                //when
                ResultActions resultActions = mockMvc.perform(put(apiBasePath + "/json/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(employeeRequest)));

                //then
                resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
            }

            @Nested
            @DisplayName("deleteEmployee Test")
            class DeleteEmployeeTest {

                @Test
                @DisplayName("Should Not Delete Object With UUID Not Matching Existing One And Return Http Status 404 For deleteEmployee")
                void shouldNotSerializeObjectWithUUIDNotMatchingExistingOneAndReturnHttpStatus404ForDeleteEmployee() throws Exception {

                    //given
                    String expectedMessage = "employee with uuid " + UUID_1 + " not found";

                    doThrow(new EmployeeNotFoundException(UUID_1)).when(employeeService).deleteEmployee(UUID_1);

                    //when
                    ResultActions resultActions = mockMvc.perform(delete(apiBasePath + "/json/employees/" + UUID_1)
                            .contentType(MediaType.APPLICATION_JSON_VALUE));

                    //then
                    resultActions.andExpect(MockMvcResultMatchers.status().isNotFound())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(expectedMessage)));
                }

                @Test
                @DisplayName("Should Delete Object With UUID Matching Existing One And Return Http Status 200 For deleteEmployee")
                void shouldDeleteObjectWithUUIDMatchingExistingOneAndReturnHttpStatus200ForDeleteEmployee() throws Exception {

                    //when
                    //then
                    mockMvc.perform(delete(apiBasePath + "/json/employees/" + UUID_1)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                            .andExpect(status().isOk());
                }
            }
        }
    }
}
