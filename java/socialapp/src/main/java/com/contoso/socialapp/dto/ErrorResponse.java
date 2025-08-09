package com.contoso.socialapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {
    
    @Schema(description = "Error code or type", example = "VALIDATION_ERROR")
    private String error;
    
    @Schema(description = "Human-readable error message", example = "The request body is invalid")
    private String message;
    
    @Schema(description = "Additional error details (optional)", example = "[\"username is required\", \"content must not be empty\"]")
    private List<String> details;
    
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
