package com.jeiu.capstone.controller;

import com.jeiu.capstone.application.security.auth.LoginUser;
import com.jeiu.capstone.application.PostService;
import com.jeiu.capstone.application.dto.PostDto;
import com.jeiu.capstone.application.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * REST API Controller
 */
@RequestMapping("/api")//공통 URI지정(ex: /api/posts)
@RequiredArgsConstructor
@Controller  //JSON으로 응답되게 하는거
public class PostsApiController {

    private final PostService postService;

    /*
    GET: 조회
    POST: 생성
    PUT: 업데이트
    DELETE: 삭제
     */

    /*
    @RequestBody: HTTP body내용을 통으로 자바 객체로 변환
    @LoginUSer: SpringSecurity annotation, 로그인한 사용자 정보를 불러 옴
     */

    /* CREATE */
    @PostMapping("/posts") //POST방식이라는 의미
    public String save(@RequestParam("writer") String writer,
                                  @RequestParam("title") String title,
                                  @RequestParam("content") String content,
//                                  @RequestParam("projectTechnologies") String projectTechnologies,
                                  @LoginUser UserDto.Response user) {
        PostDto.Request dto = new PostDto.Request();
        dto.setWriter(writer);
        dto.setTitle(title);
        dto.setContent(content);
        postService.save(dto, user.getNickname());
//        return ResponseEntity.ok(postService.save(dto, user.getNickname())); //HTTP상태 200 return
        return "redirect:/posts/list";
    }

    /* READ */
    @GetMapping("/posts/{id}")
    public ResponseEntity read(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    /* UPDATE */
    @PutMapping("/posts/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody PostDto.Request dto) {
        postService.update(id, dto);
        return ResponseEntity.ok(id);
    }

    /* DELETE */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok(id);
    }
}
