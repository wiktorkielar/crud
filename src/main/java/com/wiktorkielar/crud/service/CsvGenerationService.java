package com.wiktorkielar.crud.service;

import org.springframework.core.io.InputStreamResource;

public interface CsvGenerationService {
    InputStreamResource getAllEmployeesAsCsv();
}
