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
@Schema(description = "A comment on a post")
public class CommentDto {
    
    @Schema(description = "Unique identifier for the comment", example = "987fcdeb-51a2-43d1-9f6b-123456789abc")
    private String id;
    
    @Schema(description = "ID of the post this comment belongs to", example = "123e4567-e89b-12d3-a456-426614174000")
    private String postId;
    
    @Schema(description = "Username of the comment author", example = "jane_smith")
    private String username;
    
    @Schema(description = "Content of the comment", example = "Great photo! Where was this taken?")
    private String content;
    
    @Schema(description = "Timestamp when the comment was created", example = "2025-06-01T11:15:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the comment was last updated", example = "2025-06-01T11:15:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
