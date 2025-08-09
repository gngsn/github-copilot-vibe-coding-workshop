package com.contoso.socialapp.service;

import com.contoso.socialapp.dto.*;
import com.contoso.socialapp.entity.Comment;
import com.contoso.socialapp.exception.ResourceNotFoundException;
import com.contoso.socialapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final CommentRepository commentRepository;
    
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPostId(String postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CommentDto createComment(String postId, NewCommentRequest request) {
        String commentId = generateId();
        
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPostId(postId);
        comment.setUsername(request.getUsername());
        comment.setContent(request.getContent());
        // Let @PrePersist handle setting timestamps
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }
    
    @Transactional(readOnly = true)
    public CommentDto getCommentById(String postId, String commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return convertToDto(comment);
    }
    
    @Transactional
    public CommentDto updateComment(String postId, String commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        
        comment.setUsername(request.getUsername());
        comment.setContent(request.getContent());
        // Let @PreUpdate handle setting updatedAt timestamp
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }
    
    @Transactional
    public void deleteComment(String postId, String commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }
    
    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPostId(),
                comment.getUsername(),
                comment.getContent(),
                LocalDateTime.parse(comment.getCreatedAt(), FORMATTER),
                LocalDateTime.parse(comment.getUpdatedAt(), FORMATTER)
        );
    }
    
    private String generateId() {
        return String.valueOf(System.currentTimeMillis()).replace(".", "");
    }
}
