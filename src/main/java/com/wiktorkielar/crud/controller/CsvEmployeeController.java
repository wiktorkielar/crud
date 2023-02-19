package com.wiktorkielar.crud.controller;

import com.wiktorkielar.crud.service.CsvGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wiktorkielar.crud.util.CommonStrings.ALL_EMPLOYEES;

@RestController
@RequestMapping("${api.base.path}/csv/employees")
@RequiredArgsConstructor
public class CsvEmployeeController {

    private final CsvGenerationService csvGenerationService;

    @GetMapping(produces = "text/csv")
    public ResponseEntity<Resource> getAllEmployees() {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ALL_EMPLOYEES);
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(csvGenerationService.getAllEmployeesAsCsv(), headers, HttpStatus.OK);
    }
}
