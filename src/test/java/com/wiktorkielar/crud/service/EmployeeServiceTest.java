package com.wiktorkielar.crud.service;

import com.wiktorkielar.crud.exception.EmployeeAlreadyExistsException;
import com.wiktorkielar.crud.exception.EmployeeNotFoundException;
import com.wiktorkielar.crud.model.EmployeeEntity;
import com.wiktorkielar.crud.model.EmployeeRequest;
import com.wiktorkielar.crud.model.EmployeeResponse;
import com.wiktorkielar.crud.repository.EmployeeRepository;
import com.wiktorkielar.crud.service.impl.DefaultEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.wiktorkielar.crud.CommonStrings.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DefaultEmployeeService employeeService;

    private EmployeeRequest employeeRequest;

    private EmployeeEntity employeeEntity;

    @BeforeEach
    public void setup() {

        employeeRequest = EmployeeRequest.builder()
                .uuid(EMPTY_STRING)
                .firstName(FIRST_NAME_1)
                .lastName(LAST_NAME_1)
                .jobRole(JOB_ROLE_1)
                .build();

        employeeEntity = EmployeeEntity.builder()
                .id(1L)
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
        @DisplayName("Should Serialize Object And Return EmployeeResponse For createEmployee")
        void shouldSerializeObjectAndReturnEmployeeResponseForCreateEmployee() {

            //given
            when(employeeRepository.findEmployeeEntityByUuid(employeeRequest.getUuid())).thenReturn(Optional.empty());
            when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

            //when
            EmployeeResponse employeeResponse = employeeService.createEmployee(employeeRequest);

            //then
            assertThat(employeeResponse).isNotNull();
            assertThat(employeeResponse.getUuid()).isEqualTo(UUID_1);
            assertThat(employeeResponse.getFirstName()).isEqualTo(employeeRequest.getFirstName());
            assertThat(employeeResponse.getLastName()).isEqualTo(employeeRequest.getLastName());
            assertThat(employeeResponse.getJobRole()).isEqualTo(employeeRequest.getJobRole());
            assertThat(employeeResponse.getCreated()).isEqualTo(CREATED_1);
        }

        @Test
        @DisplayName("Should Throw EmployeeAlreadyExistsException For createEmployee")
        void shouldThrowEmployeeAlreadyExistsExceptionForCreateEmployee() {

            //given
            when(employeeRepository.findEmployeeEntityByUuid(employeeRequest.getUuid())).thenReturn(Optional.of(employeeEntity));

            //when
            //then
            assertThatThrownBy(() -> employeeService.createEmployee(employeeRequest)).isInstanceOf(EmployeeAlreadyExistsException.class);
        }
    }

    @Nested
    @DisplayName("getEmployee Test")
    class GetEmployeeTest {

        @Test
        @DisplayName("Should Serialize Object And Return EmployeeResponse For getEmployee")
        void shouldSerializeObjectAndReturnEmployeeResponseForGetEmployee() {

            //given
            when(employeeRepository.findEmployeeEntityByUuid(UUID_1)).thenReturn(Optional.of(employeeEntity));

            //when
            EmployeeResponse employeeResponse = employeeService.getEmployee(UUID_1);

            //then
            assertThat(employeeResponse).isNotNull();
            assertThat(employeeResponse.getUuid()).isEqualTo(UUID_1);
            assertThat(employeeResponse.getFirstName()).isEqualTo(employeeEntity.getFirstName());
            assertThat(employeeResponse.getLastName()).isEqualTo(employeeEntity.getLastName());
            assertThat(employeeResponse.getJobRole()).isEqualTo(employeeEntity.getJobRole());
            assertThat(employeeResponse.getCreated()).isEqualTo(CREATED_1);
        }

        @Test
        @DisplayName("Should Throw EmployeeNotFoundException For getEmployee")
        void shouldThrowEmployeeNotFoundExceptionForGetEmployee() {

            //given
            when(employeeRepository.findEmployeeEntityByUuid(UUID_1)).thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> employeeService.getEmployee(UUID_1)).isInstanceOf(EmployeeNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updateEmployee Test")
    class UpdateEmployeeTest {

        @Test
        @DisplayName("Should Serialize Object And Return Updated EmployeeResponse For updateEmployee")
        void shouldSerializeObjectAndReturnUpdatedEmployeeResponseForUpdateEmployee() {

            //given
            EmployeeEntity updatedEmployeeEntity = EmployeeEntity.builder()
                    .id(1L)
                    .uuid(UUID_1)
                    .firstName(FIRST_NAME_2)
                    .lastName(LAST_NAME_2)
                    .jobRole(JOB_ROLE_2)
                    .created(LocalDateTime.parse(CREATED_1))
                    .build();

            when(employeeRepository.findEmployeeEntityByUuid(employeeRequest.getUuid())).thenReturn(Optional.of(employeeEntity));
            when(employeeRepository.save(employeeEntity)).thenReturn(updatedEmployeeEntity);

            //when
            EmployeeResponse employeeResponse = employeeService.updateEmployee(employeeRequest);

            //then
            assertThat(employeeResponse).isNotNull();
            assertThat(employeeResponse.getUuid()).isEqualTo(UUID_1);
            assertThat(employeeResponse.getFirstName()).isEqualTo(updatedEmployeeEntity.getFirstName());
            assertThat(employeeResponse.getLastName()).isEqualTo(updatedEmployeeEntity.getLastName());
            assertThat(employeeResponse.getJobRole()).isEqualTo(updatedEmployeeEntity.getJobRole());
            assertThat(employeeResponse.getCreated()).isEqualTo(CREATED_1);
        }

        @Test
        @DisplayName("Should Throw EmployeeNotFoundException For updateEmployee")
        void shouldThrowEmployeeNotFoundExceptionForUpdateEmployee() {

            //given
            when(employeeRepository.findEmployeeEntityByUuid(UUID_1)).thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> employeeService.getEmployee(UUID_1)).isInstanceOf(EmployeeNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteEmployee Test")
    class DeleteEmployeeTest {

        @Test
        @DisplayName("Should Call deleteByUuid For deleteEmployee")
        void shouldCallDeleteByUuidForDeleteEmployee() {

            //given
            when(employeeRepository.findEmployeeEntityByUuid(UUID_1)).thenReturn(Optional.of(employeeEntity));

            //when
            employeeService.deleteEmployee(UUID_1);

            //then
            verify(employeeRepository, times(1)).deleteByUuid(UUID_1);
        }

        @Test
        @DisplayName("Should Throw EmployeeNotFoundException For deleteEmployee")
        void shouldThrowEmployeeNotFoundExceptionForDeleteEmployee() {

            //given
            when(employeeRepository.findEmployeeEntityByUuid(UUID_1)).thenReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> employeeService.deleteEmployee(UUID_1)).isInstanceOf(EmployeeNotFoundException.class);
        }
    }
}