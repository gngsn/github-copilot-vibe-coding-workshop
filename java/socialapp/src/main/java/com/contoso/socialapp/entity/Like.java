package com.contoso.socialapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LikeId.class)
public class Like {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Id
    @Column(name = "postId", nullable = false)
    private String postId;
    
    @Id
    @Column(nullable = false)
    private String username;
    
    @Column(name = "createdAt", nullable = false, updatable = false)
    private String createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", insertable = false, updatable = false)
    private Post post;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now().format(FORMATTER);
    }
}
