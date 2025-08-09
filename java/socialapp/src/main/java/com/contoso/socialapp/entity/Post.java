package com.contoso.socialapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false, length = 2000)
    private String content;
    
    @Column(name = "createdAt", nullable = false, updatable = false)
    private String createdAt;
    
    @Column(name = "updatedAt", nullable = false)
    private String updatedAt;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;

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
