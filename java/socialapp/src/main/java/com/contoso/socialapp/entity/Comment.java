package com.contoso.socialapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Id
    private String id;
    
    @Column(name = "postId", nullable = false)
    private String postId;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false, length = 1000)
    private String content;
    
    @Column(name = "createdAt", nullable = false, updatable = false)
    private String createdAt;
    
    @Column(name = "updatedAt", nullable = false)
    private String updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", insertable = false, updatable = false)
    private Post post;

    @PrePersist
    protected void onCreate() {
        String now = LocalDateTime.now().format(FORMATTER);
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now().format(FORMATTER);
    }
}
