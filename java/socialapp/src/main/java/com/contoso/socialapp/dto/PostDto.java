package com.contoso.socialapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A social media post")
public class PostDto {
    
    @Schema(description = "Unique identifier for the post", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    
    @Schema(description = "Username of the post author", example = "john_doe")
    private String username;
    
    @Schema(description = "Content of the post", example = "Just had an amazing hike in the mountains! #outdoorlife")
    private String content;
    
    @Schema(description = "Timestamp when the post was created", example = "2025-06-01T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the post was last updated", example = "2025-06-01T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Number of likes on the post", example = "15", minimum = "0")
    private int likesCount;
    
    @Schema(description = "Number of comments on the post", example = "3", minimum = "0")
    private int commentsCount;
}
