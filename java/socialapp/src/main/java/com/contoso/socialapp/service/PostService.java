package com.contoso.socialapp.service;

import com.contoso.socialapp.dto.*;
import com.contoso.socialapp.entity.Post;
import com.contoso.socialapp.exception.ResourceNotFoundException;
import com.contoso.socialapp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final PostRepository postRepository;
    
    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public PostDto createPost(NewPostRequest request) {
        String postId = generateId();
        
        Post post = new Post();
        post.setId(postId);
        post.setUsername(request.getUsername());
        post.setContent(request.getContent());
        // Let @PrePersist handle setting timestamps
        
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }
    
    @Transactional(readOnly = true)
    public PostDto getPostById(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return convertToDto(post);
    }
    
    @Transactional
    public PostDto updatePost(String postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        post.setUsername(request.getUsername());
        post.setContent(request.getContent());
        // Let @PreUpdate handle setting updatedAt timestamp
        
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }
    
    @Transactional
    public void deletePost(String postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postRepository.deleteById(postId);
    }
    
    private PostDto convertToDto(Post post) {
        int likesCount = postRepository.countLikesByPostId(post.getId());
        int commentsCount = postRepository.countCommentsByPostId(post.getId());
        
        return new PostDto(
                post.getId(),
                post.getUsername(),
                post.getContent(),
                LocalDateTime.parse(post.getCreatedAt(), FORMATTER),
                LocalDateTime.parse(post.getUpdatedAt(), FORMATTER),
                likesCount,
                commentsCount
        );
    }
    
    private String generateId() {
        return String.valueOf(System.currentTimeMillis()).replace(".", "");
    }
}
