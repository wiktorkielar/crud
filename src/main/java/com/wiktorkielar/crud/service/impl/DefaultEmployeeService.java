package com.wiktorkielar.crud.service.impl;

import com.wiktorkielar.crud.exception.EmployeeAlreadyExistsException;
import com.wiktorkielar.crud.exception.EmployeeNotFoundException;
import com.wiktorkielar.crud.exception.NoEmployeesFoundException;
import com.wiktorkielar.crud.model.EmployeeEntity;
import com.wiktorkielar.crud.model.EmployeeRequest;
import com.wiktorkielar.crud.model.EmployeeResponse;
import com.wiktorkielar.crud.repository.EmployeeRepository;
import com.wiktorkielar.crud.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {

        Optional<EmployeeEntity> savedEmployeeEntityOptional = employeeRepository.findEmployeeEntityByUuid(employeeRequest.getUuid());
        if (savedEmployeeEntityOptional.isPresent()) {
            throw new EmployeeAlreadyExistsException(employeeRequest.getUuid());
        } else {
            EmployeeEntity savedEmployeeEntity = employeeRepository.save(EmployeeEntity.builder()
                    .uuid(UUID.randomUUID().toString())
                    .firstName(employeeRequest.getFirstName())
                    .lastName(employeeRequest.getLastName())
                    .jobRole(employeeRequest.getJobRole())
                    .created(LocalDateTime.now())
                    .build());

            return EmployeeResponse.builder()
                    .uuid(savedEmployeeEntity.getUuid())
                    .firstName(savedEmployeeEntity.getFirstName())
                    .lastName(savedEmployeeEntity.getLastName())
                    .jobRole(savedEmployeeEntity.getJobRole())
                    .created(savedEmployeeEntity.getCreated())
                    .build();
        }
    }

    @Override
    public EmployeeResponse getEmployee(String uuid) {
        EmployeeEntity employeeEntity = employeeRepository
                .findEmployeeEntityByUuid(uuid)
                .orElseThrow(() -> new EmployeeNotFoundException(uuid));

        return EmployeeResponse.builder()
                .uuid(employeeEntity.getUuid())
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .jobRole(employeeEntity.getJobRole())
                .created(employeeEntity.getCreated())
                .build();
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<EmployeeResponse> employeeResponseList = employeeRepository.findAll()
                .stream()
                .map(employeeEntity -> EmployeeResponse.builder()
                        .uuid(employeeEntity.getUuid())
                        .firstName(employeeEntity.getFirstName())
                        .lastName(employeeEntity.getLastName())
                        .jobRole(employeeEntity.getJobRole())
                        .created(employeeEntity.getCreated())
                        .build())
                .toList();

        if (employeeResponseList.isEmpty()) {
            throw new NoEmployeesFoundException();
        }

        return employeeResponseList;
    }

    @Override
    public EmployeeResponse updateEmployee(EmployeeRequest employeeRequest) {

        EmployeeEntity employeeEntity = employeeRepository
                .findEmployeeEntityByUuid(employeeRequest.getUuid())
                .orElseThrow(() -> new EmployeeNotFoundException(employeeRequest.getUuid()));

        EmployeeEntity updatedEmployeeEntity = employeeRepository.save(EmployeeEntity.builder()
                .id(employeeEntity.getId())
                .uuid(employeeEntity.getUuid())
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .jobRole(employeeRequest.getJobRole())
                .created(employeeEntity.getCreated())
                .build());

        return EmployeeResponse.builder()
                .uuid(updatedEmployeeEntity.getUuid())
                .firstName(updatedEmployeeEntity.getFirstName())
                .lastName(updatedEmployeeEntity.getLastName())
                .jobRole(updatedEmployeeEntity.getJobRole())
                .created(updatedEmployeeEntity.getCreated())
                .build();
    }

    @Override
    public void deleteEmployee(String uuid) {
        EmployeeEntity employeeEntity = employeeRepository
                .findEmployeeEntityByUuid(uuid)
                .orElseThrow(() -> new EmployeeNotFoundException(uuid));
        employeeRepository.deleteByUuid(employeeEntity.getUuid());
    }
}
