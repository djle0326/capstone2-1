package com.jeiu.capstone.controller;

import com.jeiu.capstone.application.AwsS3Uploader;
import com.jeiu.capstone.application.security.auth.LoginUser;
import com.jeiu.capstone.application.PostService;
import com.jeiu.capstone.application.dto.PostDto;
import com.jeiu.capstone.application.dto.UserDto;
import com.jeiu.capstone.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;

/**
 * REST API Controller
 */
@RequestMapping("/api")//공통 URI지정(ex: /api/posts)
@RequiredArgsConstructor
@Controller  //JSON으로 응답되게 하는거
public class PostsApiController {

    private final PostService postService;
    private final AwsS3Uploader uploader;

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
                       @RequestParam("poster") MultipartFile poster,
                       @RequestParam("teamMem") String teamMem,
                       @RequestParam("yt_url") String yt_url,
//                       @RequestParam("video") MultipartFile video,
//                       @RequestParam("projectTechnologies") String projectTechnologies,
                       @LoginUser UserDto.Response user) throws IOException {
        PostDto.Request dto = new PostDto.Request();
        String postername = Normalizer.normalize(poster.getOriginalFilename(), Normalizer.Form.NFC);
        dto.setWriter(writer);
        dto.setTitle(title);
        dto.setContent(content);
        dto.setTeamMem(teamMem);
        dto.setFilename(postername);
        dto.setYt_url(yt_url);

        String posterUrl = uploader.upload(poster, "poster");
        dto.setFileUrl(posterUrl);
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
    @PutMapping("/posts/update/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         @RequestParam("teamMem") String teamMem,
                         @RequestParam("yt_url") String yt_url,
                         @RequestParam("poster") MultipartFile poster
    ) throws IOException {
        System.out.println("id" + id);
        System.out.println("title" + title);
        System.out.println("content" + content);

        PostDto.Request dto = new PostDto.Request();
        dto.setContent(content);
        dto.setTitle(title);
        dto.setTeamMem(teamMem);
        dto.setYt_url(yt_url);
        if (poster != null) {
            String postername = Normalizer.normalize(poster.getOriginalFilename(), Normalizer.Form.NFC);
            dto.setFilename(postername);
            String posterUrl = uploader.upload(poster, "poster");
            dto.setFileUrl(posterUrl);
        }

        postService.update(id, dto);

        return "redirect:/posts/list";
    }

    /* DELETE */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        postService.delete(id);
        return ResponseEntity.ok(id);
    }
}
