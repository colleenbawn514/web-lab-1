package com.colleen.letdown.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.colleen.letdown.entities.ErrorBind;
import com.colleen.letdown.entities.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

@SuppressWarnings({"unchecked","rawtypes"})
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler
{
   /* @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Server Error", details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
   @ExceptionHandler(ConstraintViolationException.class)
   public final ResponseEntity<?> handleAllExceptions(ConstraintViolationException ex, WebRequest request) {
       Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
       ArrayList<ErrorBind> errors = new ArrayList<>();
       for (ConstraintViolation<?> violation : violations ) {
           String field = null;
           for (Path.Node node : violation.getPropertyPath()) {
               field = node.getName();
           }
           errors.add(new ErrorBind(field, violation.getMessage()));
       }
       return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
   }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        System.out.println("valid method");
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}