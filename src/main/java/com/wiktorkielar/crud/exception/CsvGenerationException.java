package com.wiktorkielar.crud.exception;

public class CsvGenerationException extends RuntimeException {
    public CsvGenerationException(String fileName) {
        super("CSV generation error for " + fileName);
    }
}
