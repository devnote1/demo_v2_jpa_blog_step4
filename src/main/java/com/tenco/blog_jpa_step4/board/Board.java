package com.tenco.blog_jpa_step4.board;

import com.tenco.blog_jpa_step4.reply.Reply;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Board 엔티티는 블로그 게시글을 나타내는 JPA 엔티티 클래스입니다.
 */
@Getter // Lombok을 이용해 getter, setter, toString 등을 자동으로 생성합니다.
@Setter
@Entity // JPA에게 이 클래스가 엔티티임을 알립니다.
@Table(name = "board_tb") // 실제 데이터베이스 테이블 이름을 지정합니다.
@NoArgsConstructor //  반드시 JPA/Hibernate에서는 엔티티 클래스를 인스턴스화하기 위해 기본 생성자가 필요
@AllArgsConstructor
public class Board {

    @Id // 기본 키를 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임합니다.
    private Integer id; // 게시글 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용

    // created_at 컬럼과 매핑하며, 이 필드는 삽입 시 자동으로 설정됨.
    // insertable = false: 삽입 시 개발자가 값을 넣지 않고 DB에서 자동으로 설정.
    // updatable = false: 업데이트 시 수정되지 않도록 설정 (생성 시점에만 값이 설정됨).
    @Column(name = "created_at") 
    private Timestamp createdAt; // 게시글 작성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자 정보

    // 데이터베이스 테이블에 매핑되지 않으며, 애플리케이션 내에서만 사용
    // CRUD 연산 시 자동으로 데이터베이스에 저장되거나 조회되지 않음
    @Transient
    private boolean isOwner;
    // 추후 수정 - 게시글 삭제시에 댓글이 있다면 fk 제약 조건 때문에 삭제가 안된다.
    // cascade = CascadeType.REMOVE 설정 하기
    // 양방향 맵핑 - 연관관계에 주인은 (Reply) 이다
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    //@OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Board(Integer id, String title, String content, User user, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }
}



