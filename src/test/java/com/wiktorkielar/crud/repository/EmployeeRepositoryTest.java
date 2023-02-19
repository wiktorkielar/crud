package com.wiktorkielar.crud.repository;

import com.wiktorkielar.crud.model.EmployeeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wiktorkielar.crud.util.CommonStrings.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmployeeEntity employeeEntity;

    @BeforeEach
    void setup() {
        employeeEntity = EmployeeEntity.builder()
                .uuid(UUID_1)
                .firstName(FIRST_NAME_1)
                .lastName(LAST_NAME_1)
                .jobRole(JOB_ROLE_1)
                .created(LocalDateTime.parse(CREATED_1))
                .build();
    }

    @Nested
    @DisplayName("findEmployeeEntityByUuid Test")
    class FindEmployeeEntityByUuidTest {

        @Test
        @DisplayName("Should Return Not Empty Optional Of EmployeeEntity For findEmployeeEntityByUuid")
        void shouldReturnNotEmptyOptionalOfEmployeeEntityForfindEmployeeEntityByUuid() {

            //given
            employeeRepository.save(employeeEntity);

            //when
            Optional<EmployeeEntity> foundEmployeeEntity = employeeRepository.findEmployeeEntityByUuid(employeeEntity.getUuid());

            //then
            assertThat(foundEmployeeEntity).isNotEmpty();
        }

        @Test
        @DisplayName("Should Return Empty Optional Of EmployeeEntity For findEmployeeEntityByUuid")
        void shouldReturnEmptyOptionalOfEmployeeEntityForfindEmployeeEntityByUuid() {

            //when
            Optional<EmployeeEntity> foundEmployeeEntity = employeeRepository.findEmployeeEntityByUuid(employeeEntity.getUuid());

            //then
            assertThat(foundEmployeeEntity).isEmpty();
        }
    }

    @Nested
    @DisplayName("Repository Methods Test")
    class RepositoryMethodsTest {

        @Test
        @DisplayName("Should Return Saved EmployeeEntity For save")
        void shouldReturnSavedEmployeeEntity() {

            //when
            EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);

            //then
            assertThat(savedEmployeeEntity).isNotNull();
            assertThat(savedEmployeeEntity.getId()).isPositive();
        }

        @Test
        @DisplayName("Should Return Updated EmployeeEntity For save")
        void shouldReturnUpdatedEmployeeEntity() {

            //given
            employeeRepository.save(employeeEntity);

            //when
            EmployeeEntity savedEmployeeEntity = employeeRepository.findEmployeeEntityByUuid(employeeEntity.getUuid()).get();
            savedEmployeeEntity.setFirstName(FIRST_NAME_2);
            savedEmployeeEntity.setLastName(LAST_NAME_2);
            savedEmployeeEntity.setJobRole(JOB_ROLE_2);
            EmployeeEntity updatedEmployeeEntity = employeeRepository.save(savedEmployeeEntity);

            //then
            assertThat(updatedEmployeeEntity).isNotNull();
            assertThat(updatedEmployeeEntity.getFirstName()).isEqualTo(FIRST_NAME_2);
            assertThat(updatedEmployeeEntity.getLastName()).isEqualTo(LAST_NAME_2);
            assertThat(updatedEmployeeEntity.getJobRole()).isEqualTo(JOB_ROLE_2);
            assertThat(savedEmployeeEntity.getId()).isEqualTo(updatedEmployeeEntity.getId());
        }

        @Test
        @DisplayName("Should Return EmployeeEntity List for findAll")
        void shouldReturnEmployeeEntityList() {

            //given
            employeeRepository.save(employeeEntity);

            //when
            List<EmployeeEntity> employeeEntities = employeeRepository.findAll();

            //then
            assertThat(employeeEntities).isNotNull().hasSize(1);
        }
    }


}