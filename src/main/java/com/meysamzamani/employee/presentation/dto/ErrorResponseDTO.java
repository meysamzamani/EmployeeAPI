package com.meysamzamani.employee.presentation.dto;

import org.springframework.http.HttpStatus;

public class ErrorResponseDTO {

    private HttpStatus status;
    private String message;

    public ErrorResponseDTO(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
