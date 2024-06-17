package com.jeiu.capstone.controller;

import com.jeiu.capstone.application.security.auth.LoginUser;
import com.jeiu.capstone.application.dto.CommentDto;
import com.jeiu.capstone.application.dto.PostDto;
import com.jeiu.capstone.application.dto.UserDto;
import com.jeiu.capstone.domain.Post;
import com.jeiu.capstone.application.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 화면 연결 Controller
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsIndexController {

    private final PostService postService;


    @GetMapping("/posts/list")
    public String postsList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> list = postService.pageList(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", postService.pageList(pageable));
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/project/list";
    }


    @GetMapping("/")                 /* default page = 0, size = 10  */
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable, @LoginUser UserDto.Response user) {
        Page<Post> list = postService.pageList(pageable);

//        if (user != null) {
//            model.addAttribute("user", user);
//        }

        model.addAttribute("posts", list);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", list.hasNext());
        model.addAttribute("hasPrev", list.hasPrevious());

        return "index";
    }

    /* 글 작성 */
    @GetMapping("/posts/write")
    public String write(@LoginUser UserDto.Response user, Model model) {
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "/board/project/upload";
    }

    /* 글 상세보기 */
    @GetMapping("/posts/read/{id}")
    public String read(@PathVariable Long id, Model model) {
        PostDto.Response dto = postService.findById(id);
        List<CommentDto.Response> comments = dto.getComments();


        /* 댓글 관련 */
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        /* 사용자 관련 */
//        if (user != null) {
//            model.addAttribute("user", user);
//
//            /* 게시글 작성자 본인인지 확인 */
//            if (dto.getUserId().equals(user.getId())) {
//                model.addAttribute("writer", true);
//            }
//
//            /* 댓글 작성자 본인인지 확인 */
//            if (comments.stream().anyMatch(s -> s.getUserId().equals(user.getId()))) {
//                model.addAttribute("isWriter", true);
//            }
///*            for (int i = 0; i < comments.size(); i++) {
//                boolean isWriter = comments.get(i).getUserId().equals(user.getId());
//                model.addAttribute("isWriter",isWriter);
//            }*/
//        }


        model.addAttribute("post", dto);
        postService.updateView(id); // views ++
        return "board/project/detail";
    }

    @GetMapping("/posts/update/{id}")
    public String update(@PathVariable Long id, @LoginUser UserDto.Response user, Model model) {
        PostDto.Response dto = postService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("posts", dto);

        return "posts/posts-update";
    }

    @GetMapping("/posts/search")
    public String search(String keyword, Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable, @LoginUser UserDto.Response user) {
        Page<Post> searchList = postService.search(keyword, pageable);

        if (user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("searchList", searchList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", searchList.hasNext());
        model.addAttribute("hasPrev", searchList.hasPrevious());

        return "posts/posts-search";
    }
}

