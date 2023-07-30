package com.meysamzamani.employee.exceptions;

import com.meysamzamani.employee.dto.ErrorResponseDTO;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.CONFLICT, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectError error : errors) {
            stringBuilder.append(error.getCode())
                         .append(":")
                         .append(error.getDefaultMessage())
                         .append("\n");
        }
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, stringBuilder.toString());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.NOT_FOUND, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.getStatus());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            IllegalArgumentException.class,
            ConversionFailedException.class,
            HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponseDTO, errorResponseDTO.getStatus());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.NOT_ACCEPTABLE, exception.getMessage());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(errorResponseDTO, responseHeaders, errorResponseDTO.getStatus());
    }
}
