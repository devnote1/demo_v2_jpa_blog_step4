package com.tenco.blog_jpa_step4.reply;

import com.tenco.blog_jpa_step4.board.Board;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reply_tb")
@ToString(exclude = {"user", "board"}) // 연관된 엔티티를 제외하여 순환 참조 방지 및 보안 강화
public class Reply {
    // 연관된 엔티티를 제외하여 순환 참조 방지 및 보안 강화
    //일반적으로 id는 Long 타입을 사용하는 것이 권장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // not null 지정
    @Column(nullable = false)
    private String comment;

    /**
     *  댓글 작성자 - 다대일 단방향 관게로 만들어 보자
     *  FetchType.LAZY를 사용하여 필요할 때만 사용자 정보를 로드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 댓글이 달린 게시글 - 다대일 단방향 관계로 설정
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @CreationTimestamp // 엔티티가 생성될 때 자동으로 현재 시간 시간으로 설정
    @Column(name = "created_at")
    private LocalDateTime createAt;

    // JPA 엔티티에서 데이터베이스에 저장할 필요가 없는 필드를 정의할 때 사용
    // 이 녀석은 DB 에 컬럼을 만들지마!
    @Transient
    private boolean isReplyOwner; // 현재 로그인한 사용자가 작성한 댓글 인지

    @Builder.Default
    private String status = "ACTIVE";// 댓글 상태 (예: ACTIVE, DELETED 등)

    /**
     * 엔티티가 데이터베이스에 영속화되기 전에 호출되는 메서드
     * @PrePersist 어노테이션은 JPA 라이프사이클 이벤트 중 하나로, 엔티티가 영속화되기 전에 실행됨
     * 이 메서드는 엔티티가 저장되기 전에 기본값을 설정하거나 추가적인 초기화 작업을 수행하는 데 사용됨
     */
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = "ACTIVE";
        }
        if(this.createAt == null) {
            this.createAt = LocalDateTime.now();
        }
    }
}