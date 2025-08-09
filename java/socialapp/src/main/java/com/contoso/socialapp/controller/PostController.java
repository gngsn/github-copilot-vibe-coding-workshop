package com.contoso.socialapp.controller;

import com.contoso.socialapp.dto.*;
import com.contoso.socialapp.service.PostService;
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
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Operations related to posts management")
public class PostController {
    
    private final PostService postService;
    
    @GetMapping
    @Operation(
            summary = "List all posts",
            description = "Retrieve all recent posts to browse what others are sharing",
            operationId = "getPosts"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved posts",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostDto.class))
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
    public ResponseEntity<List<PostDto>> getPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
    @PostMapping
    @Operation(
            summary = "Create a new post",
            description = "Create a new post to share something with others",
            operationId = "createPost"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)
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
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody NewPostRequest request) {
        PostDto post = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
    
    @GetMapping("/{postId}")
    @Operation(
            summary = "Get a specific post",
            description = "Retrieve a specific post by its ID to read in detail",
            operationId = "getPostById"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the post",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)
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
    public ResponseEntity<PostDto> getPostById(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId) {
        PostDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }
    
    @PatchMapping("/{postId}")
    @Operation(
            summary = "Update a post",
            description = "Update an existing post if you made a mistake or have something to add",
            operationId = "updatePost"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostDto.class)
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
    public ResponseEntity<PostDto> updatePost(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId,
            @Valid @RequestBody UpdatePostRequest request) {
        PostDto post = postService.updatePost(postId, request);
        return ResponseEntity.ok(post);
    }
    
    @DeleteMapping("/{postId}")
    @Operation(
            summary = "Delete a post",
            description = "Delete a post if you no longer want it shared",
            operationId = "deletePost"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Post deleted successfully"
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
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
