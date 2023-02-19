package com.wiktorkielar.crud.handler;

import com.wiktorkielar.crud.exception.EmployeeAlreadyExistsException;
import com.wiktorkielar.crud.exception.EmployeeNotFoundException;
import com.wiktorkielar.crud.exception.NoEmployeesFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.SEE_OTHER)
    @ExceptionHandler(value = {EmployeeAlreadyExistsException.class})
    public Map<String, String> handleEmployeeAlreadyExistsException(RuntimeException ex) {

        return handleEmployeeExceptions(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {EmployeeNotFoundException.class, NoEmployeesFoundException.class})
    public Map<String, String> handleEmployeeAndEmployeesNotFoundExceptions(RuntimeException ex) {

        return handleEmployeeExceptions(ex);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> fieldMessagePairs = new HashMap<>();
        fieldMessagePairs.put("timestamp", LocalDateTime.now().toString());
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> fieldMessagePairs.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return fieldMessagePairs;
    }

    private Map<String, String> handleEmployeeExceptions(RuntimeException ex) {
        Map<String, String> fieldMessagePairs = new HashMap<>();
        fieldMessagePairs.put("timestamp", LocalDateTime.now().toString());
        fieldMessagePairs.put("message", ex.getMessage());

        return fieldMessagePairs;
    }
}
