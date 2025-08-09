package com.contoso.socialapp.service;

import com.contoso.socialapp.dto.LikeRequest;
import com.contoso.socialapp.dto.LikeResponse;
import com.contoso.socialapp.entity.Like;
import com.contoso.socialapp.exception.AlreadyLikedException;
import com.contoso.socialapp.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LikeService {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final LikeRepository likeRepository;
    
    @Transactional
    public LikeResponse likePost(String postId, LikeRequest request) {
        if (likeRepository.existsByPostIdAndUsername(postId, request.getUsername())) {
            throw new AlreadyLikedException("Already liked");
        }
        
        Like like = new Like();
        like.setPostId(postId);
        like.setUsername(request.getUsername());
        // Let @PrePersist handle setting createdAt timestamp
        
        Like savedLike = likeRepository.save(like);
        
        return new LikeResponse(postId, request.getUsername(), LocalDateTime.parse(savedLike.getCreatedAt(), FORMATTER));
    }
    
    @Transactional
    public void unlikePost(String postId, String username) {
        likeRepository.deleteByPostIdAndUsername(postId, username);
    }
}
