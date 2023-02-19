package com.wiktorkielar.crud.service;

import com.wiktorkielar.crud.model.EmployeeRequest;
import com.wiktorkielar.crud.model.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

    EmployeeResponse getEmployee(String uuid);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse updateEmployee(EmployeeRequest employeeRequest);

    void deleteEmployee(String uuid);
}
