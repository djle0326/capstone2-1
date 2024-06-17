package com.jeiu.capstone.application;

import com.jeiu.capstone.application.dto.PostDto;
//import com.jeiu.capstone.configANDjpa.jpa.FileStore;
import com.jeiu.capstone.domain.Post;
import com.jeiu.capstone.configANDjpa.jpa.PostRepository;
import com.jeiu.capstone.domain.User;
import com.jeiu.capstone.configANDjpa.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    //싱글톤
    private final PostRepository postRepository;
    private final UserRepository userRepository;
//    private final FileStore fileStore;


    /* CREATE */
    @Transactional //DB의 Transactional할때 그 Transactional(roll back등등)
    public Long save(PostDto.Request dto, String nickname) {
        /* User 정보를 가져와 dto에 담아준다. */
        User user = userRepository.findByNickname(nickname); //닉네임으로 사용사를 찾는다
        dto.setUser(user); //받은 값 dto의 setter로 설정
        log.info("PostsService save() 실행");
        Post post = dto.toEntity(); //DTO에 있는 entitiy를 반환해서 Post타입으로 받는다
        postRepository.save(post); //받은 엔티티를 DB에 저장한다

        return post.getId(); //받아온 ID를 Return한다
    }


    /* READ 게시글 리스트 조회 readOnly 속성으로 조회속도 개선 */
    @Transactional(readOnly = true)
    public PostDto.Response findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));

        return new PostDto.Response(post);
    }


    /* UPDATE (dirty checking 영속성 컨텍스트)
     *  User 객체를 영속화시키고, 영속화된 User 객체를 가져와 데이터를 변경하면
     * 트랜잭션이 끝날 때 자동으로 DB에 저장해준다. */
    @Transactional
    public void update(Long id, PostDto.Request dto) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        post.update(dto.getTitle(), dto.getContent());
    }

    /* DELETE */
    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        postRepository.delete(post);
    }

    /* Views Counting */
    @Transactional
    public int updateView(Long id) {
        return postRepository.updateView(id);
    }


    /* Paging and Sort */
    @Transactional(readOnly = true)
    public Page<Post> pageList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /* search */
    @Transactional(readOnly = true)
    public Page<Post> search(String keyword, Pageable pageable) {
        Page<Post> postsList = postRepository.findByTitleContaining(keyword, pageable);
        return postsList;
    }

}

