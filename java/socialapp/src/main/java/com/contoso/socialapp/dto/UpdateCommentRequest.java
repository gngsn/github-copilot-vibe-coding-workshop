package com.contoso.socialapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing comment")
public class UpdateCommentRequest {
    
    @Schema(description = "Username of the comment author (for validation)", example = "jane_smith", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;
    
    @Schema(description = "Updated content of the comment", example = "Great photo! Where was this taken? The scenery looks amazing!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String content;
}
