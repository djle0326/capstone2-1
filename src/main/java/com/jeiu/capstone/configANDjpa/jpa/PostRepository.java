package com.jeiu.capstone.configANDjpa.jpa;

import com.jeiu.capstone.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//Long타입(id) 게시물 관리
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {  /* posts"entity, 식별사는 Long(id) */
    @Modifying //insert, update, delete에서 사용되는 어노테이션(select제외)
    @Query("update Post p set p.view = p.view + 1 where p.id = :id") //조회수 중가
    int updateView(@Param("id") Long id);

    Page<Post> findByTitleContaining(String keyword, Pageable pageable); //제목으로 검색
}
