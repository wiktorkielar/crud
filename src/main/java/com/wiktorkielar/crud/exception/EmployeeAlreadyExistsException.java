package com.wiktorkielar.crud.exception;

public class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String uuid) {
        super("employee with uuid " + uuid + " already exists");
    }
}
