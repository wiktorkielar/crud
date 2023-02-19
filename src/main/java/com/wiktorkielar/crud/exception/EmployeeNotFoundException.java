package com.wiktorkielar.crud.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String uuid) {
        super("employee with uuid " + ((uuid != null && uuid.equals("")) ? "of empty string" : uuid) + " not found");
    }
}
