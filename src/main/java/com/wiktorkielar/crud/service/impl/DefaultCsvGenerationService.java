package com.wiktorkielar.crud.service.impl;

import com.wiktorkielar.crud.exception.CsvGenerationException;
import com.wiktorkielar.crud.model.EmployeeResponse;
import com.wiktorkielar.crud.service.CsvGenerationService;
import com.wiktorkielar.crud.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static com.wiktorkielar.crud.util.CommonStrings.ALL_EMPLOYEES;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCsvGenerationService implements CsvGenerationService {

    private final EmployeeService employeeService;

    @Override
    public InputStreamResource getAllEmployeesAsCsv() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(
                     new PrintWriter(out),
                     CSVFormat.Builder.create()
                             .setHeader("uuid", "firstName", "lastName", "jobRole", "created")
                             .build()
             )) {
            for (EmployeeResponse employeeResponse : employeeService.getAllEmployees()) {
                csvPrinter.printRecord(
                        employeeResponse.getUuid(),
                        employeeResponse.getFirstName(),
                        employeeResponse.getLastName(),
                        employeeResponse.getJobRole(),
                        employeeResponse.getCreated().toString());
            }

            csvPrinter.flush();

            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

        } catch (IOException e) {
            throw new CsvGenerationException(ALL_EMPLOYEES);
        }
    }
}

