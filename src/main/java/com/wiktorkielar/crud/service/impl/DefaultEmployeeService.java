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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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

            return getEmployeeResponse(savedEmployeeEntity);
        }
    }

    @Override
    public EmployeeResponse getEmployee(String uuid) {
        EmployeeEntity employeeEntity = employeeRepository
                .findEmployeeEntityByUuid(uuid)
                .orElseThrow(() -> new EmployeeNotFoundException(uuid));

        return getEmployeeResponse(employeeEntity);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<EmployeeResponse> employeeResponseList = employeeRepository.findAll()
                .stream()
                .map(this::getEmployeeResponse)
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

        return getEmployeeResponse(updatedEmployeeEntity);
    }

    @Override
    public void deleteEmployee(String uuid) {
        EmployeeEntity employeeEntity = employeeRepository
                .findEmployeeEntityByUuid(uuid)
                .orElseThrow(() -> new EmployeeNotFoundException(uuid));
        employeeRepository.deleteByUuid(employeeEntity.getUuid());
    }

    private EmployeeResponse getEmployeeResponse(EmployeeEntity employeeEntity) {
        return EmployeeResponse.builder()
                .uuid(employeeEntity.getUuid())
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .jobRole(employeeEntity.getJobRole())
                .created(employeeEntity.getCreated())
                .build();
    }
}
