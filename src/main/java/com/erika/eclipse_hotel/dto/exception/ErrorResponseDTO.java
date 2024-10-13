package com.erika.eclipse_hotel.dto.exception;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private String timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponseDTO(String error, String message, int status) {
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
