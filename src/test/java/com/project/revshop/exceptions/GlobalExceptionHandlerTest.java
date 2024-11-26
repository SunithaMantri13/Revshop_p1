package com.project.revshop.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

 public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleValidationExceptions() {
        // Create a mock BindingResult and MethodArgumentNotValidException
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "field1", "Field1 error message");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Field2 error message");
        
        List<ObjectError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError1);
        fieldErrors.add(fieldError2);
        
        when(bindingResult.getAllErrors()).thenReturn(fieldErrors);
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                null, bindingResult);
        
        ResponseEntity<Map<String, String>> responseEntity = globalExceptionHandler.handleValidationExceptions(exception);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Map<String, String> errors = responseEntity.getBody();
        assertEquals(2, errors.size());
        assertEquals("Field1 error message", errors.get("field1"));
        assertEquals("Field2 error message", errors.get("field2"));
    }
}
