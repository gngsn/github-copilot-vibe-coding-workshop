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
@Schema(description = "Request to update an existing post")
public class UpdatePostRequest {
    
    @Schema(description = "Username of the post author (for validation)", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;
    
    @Schema(description = "Updated content of the post", example = "Just had an amazing hike in the mountains! The view was breathtaking. #outdoorlife #hiking", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 2000, message = "Content must be between 1 and 2000 characters")
    private String content;
}
