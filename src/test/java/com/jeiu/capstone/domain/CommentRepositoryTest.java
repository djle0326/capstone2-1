package com.jeiu.capstone.domain;

import com.jeiu.capstone.configANDjpa.jpa.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    public void clear() {
        commentRepository.deleteAll();
    }

    @Test
    public void 게시글_댓글_생성_조회() {
        String content = "댓글 입니다.";

        Post post = Post.builder().id(1L).build();
        User user = User.builder().id(1L).build();

            commentRepository.save(Comment.builder()
                    .comment(content)
                    .user(user)
                    .post(post)
                    .build());

            List<Comment> comments = commentRepository.getCommentByPostOrderById(post);

            Comment comment = comments.get(0);

            assertThat(comment.getComment()).isEqualTo(content);
    }

    @Test
    public void 랜덤_댓글_생성() {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            long id = (long)(Math.random() * 22) + 1;

            Post post = Post.builder().id(id).build();
            User user = User.builder().id(id).build();
            Comment comment = Comment.builder()
                    .comment(i + "번째 댓글입니다.")
                    .user(user)
                    .post(post)
                    .build();

            commentRepository.save(comment);
        });
    }
}
