package com.contoso.socialapp.config;

import com.contoso.socialapp.entity.Post;
import com.contoso.socialapp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final PostRepository postRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {
            log.info("Initializing database with mock data...");
            
            Post post1 = new Post();
            post1.setId("1");
            post1.setUsername("alice");
            post1.setContent("Hello world!");
            
            Post post2 = new Post();
            post2.setId("2");
            post2.setUsername("bob");
            post2.setContent("This is my first post.");
            
            Post post3 = new Post();
            post3.setId("3");
            post3.setUsername("carol");
            post3.setContent("Excited to join this community!");
            
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);
            
            log.info("Mock data initialized successfully.");
        }
    }
}
