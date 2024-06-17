package com.jeiu.capstone.service;

import com.jeiu.capstone.application.PostService;
import com.jeiu.capstone.configANDjpa.jpa.PostRepository;
import com.jeiu.capstone.domain.Role;
import com.jeiu.capstone.domain.User;
import com.jeiu.capstone.configANDjpa.jpa.UserRepository;
import com.jeiu.capstone.application.dto.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void clear() {
        postRepository.deleteAll();
    }

    @Test
    public void 게시글_생성() {
        User user = User.builder().username("coco").nickname("coco").email("coco@coco.co").role(Role.USER).build();

        PostDto.Request posts = PostDto.Request.builder()
                .title("Test Title")
                .writer(user.getNickname())
                .content("Test Content")
                .view(0)
                .user(user)
                .build();

        postService.save(posts, user.getNickname());

        log.info(String.valueOf(posts));
    }
}