package com.jeiu.capstone.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 공통적으로 사용되는 컬럼이므로, 이를 상속한 클래스에서 컬럼을 추가
 */

@Getter
@MappedSuperclass  //상속시 해당 클래스들의 필드를 column으로 인식하게 함
@EntityListeners(AuditingEntityListener.class) //entity가 생성, 변경될때 시간등이 자동으로 저장되게 함
abstract class BaseTimeEntity { //BaseTimeEntitiy는 JPA관련 클래스이다 /

    @Column(name = "created_date", nullable = false) //column세팅
    @CreatedDate //entity가 처음 저장될때 날자가 채워지게 함
    private String createdDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate //수정된 날짜 업데이트
    private String modifiedDate;

    /* 해당 엔티티를 저장하기 이전에 실행 */ /* 생성된 날짜 필드 초기화 */
    @PrePersist
    public void onPrePersist(){ //생성 날짜 지정
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.modifiedDate = this.createdDate;
    }

    /* 해당 엔티티를 업데이트 하기 이전에 실행*/ /* 수정시  */
    @PreUpdate
    public void onPreUpdate(){ //수정 날짜 지정
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")); //날짜 형식 지정(수정날자 저장)
    }
}