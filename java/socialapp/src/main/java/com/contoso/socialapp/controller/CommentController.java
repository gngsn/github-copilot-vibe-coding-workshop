package com.contoso.socialapp.controller;

import com.contoso.socialapp.dto.*;
import com.contoso.socialapp.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Operations related to comments management")
public class CommentController {
    
    private final CommentService commentService;
    
    @GetMapping
    @Operation(
            summary = "List comments for a post",
            description = "Retrieve all comments on a specific post",
            operationId = "getCommentsByPostId"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved comments",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }
    
    @PostMapping
    @Operation(
            summary = "Create a comment",
            description = "Add a comment to a post to share your thoughts",
            operationId = "createComment"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comment created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - invalid input or validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<CommentDto> createComment(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId,
            @Valid @RequestBody NewCommentRequest request) {
        CommentDto comment = commentService.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
    
    @GetMapping("/{commentId}")
    @Operation(
            summary = "Get a specific comment",
            description = "Retrieve a specific comment by its ID",
            operationId = "getCommentById"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the comment",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<CommentDto> getCommentById(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId,
            @Parameter(description = "Unique identifier of the comment", example = "987fcdeb-51a2-43d1-9f6b-123456789abc")
            @PathVariable String commentId) {
        CommentDto comment = commentService.getCommentById(postId, commentId);
        return ResponseEntity.ok(comment);
    }
    
    @PatchMapping("/{commentId}")
    @Operation(
            summary = "Update a comment",
            description = "Update an existing comment to correct or revise it",
            operationId = "updateComment"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - invalid input or validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<CommentDto> updateComment(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId,
            @Parameter(description = "Unique identifier of the comment", example = "987fcdeb-51a2-43d1-9f6b-123456789abc")
            @PathVariable String commentId,
            @Valid @RequestBody UpdateCommentRequest request) {
        CommentDto comment = commentService.updateComment(postId, commentId, request);
        return ResponseEntity.ok(comment);
    }
    
    @DeleteMapping("/{commentId}")
    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment if necessary",
            operationId = "deleteComment"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Comment deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId,
            @Parameter(description = "Unique identifier of the comment", example = "987fcdeb-51a2-43d1-9f6b-123456789abc")
            @PathVariable String commentId) {
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
