package com.contoso.socialapp.controller;

import com.contoso.socialapp.dto.*;
import com.contoso.socialapp.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/posts/{postId}/likes")
@RequiredArgsConstructor
@Tag(name = "Likes", description = "Operations related to likes management")
public class LikeController {
    
    private final LikeService likeService;
    
    @PostMapping
    @Operation(
            summary = "Like a post",
            description = "Like a post to show appreciation",
            operationId = "likePost"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post liked successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LikeResponse.class)
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
    public ResponseEntity<LikeResponse> likePost(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId,
            @Valid @RequestBody LikeRequest request) {
        LikeResponse like = likeService.likePost(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }
    
    @DeleteMapping
    @Operation(
            summary = "Unlike a post",
            description = "Remove your like from a post if you change your mind",
            operationId = "unlikePost"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Like removed successfully"
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
    public ResponseEntity<Void> unlikePost(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId,
            @Valid @RequestBody LikeRequest request) {
        likeService.unlikePost(postId, request.getUsername());
        return ResponseEntity.noContent().build();
    }
}
