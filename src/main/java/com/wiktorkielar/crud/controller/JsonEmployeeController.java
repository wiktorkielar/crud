package com.wiktorkielar.crud.controller;

import com.wiktorkielar.crud.model.EmployeeRequest;
import com.wiktorkielar.crud.model.EmployeeResponse;
import com.wiktorkielar.crud.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base.path}/json/employees")
@RequiredArgsConstructor
public class JsonEmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        return new ResponseEntity<>(employeeService.createEmployee(employeeRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable String uuid) {
        return new ResponseEntity<>(employeeService.getEmployee(uuid), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<EmployeeResponse> updateEmployee(@RequestBody @Valid EmployeeRequest employeeRequest) {
        return new ResponseEntity<>(employeeService.updateEmployee(employeeRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String uuid) {
        employeeService.deleteEmployee(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
