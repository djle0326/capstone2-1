package com.jeiu.capstone.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모드 필드 세팅하는 생성자
@Builder //빌더패턴
@Getter
@Entity //JPA가 관리한다는 의미
public class Post extends BaseTimeEntity {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincreasement
    private Long id;

    @Column(length = 500, nullable = false) //Column세팅 / not null
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false) //text타입 설정
    private String content;

    @Column(nullable = false)
    private String writer;

    @Column(columnDefinition = "integer default 0", nullable = false) //int 설정
    private int view;

    @ManyToOne(fetch = FetchType.LAZY) //다대일 매핑, 지연로딩
    @JoinColumn(name = "user_id") //FK
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //일대다
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;

    @Column
    private String fileUrl;

    @Column(name = "team_mem")
    private String teamMem;

    /* 게시글 수정 */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;

    }
}