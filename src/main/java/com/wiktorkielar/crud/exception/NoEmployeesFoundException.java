package com.wiktorkielar.crud.exception;

public class NoEmployeesFoundException extends RuntimeException {
    public NoEmployeesFoundException() {
        super("no employees found");
    }
}
