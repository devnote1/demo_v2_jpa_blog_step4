package com.tenco.blog_jpa_step4.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_tb")
@Entity
@Builder
// @Data // 가능한 지양하자 - 권장 사항
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 사용자 ID

    @Column(nullable = false, unique = true)
    private String username; // 사용자 이름

    private String email;

    @Column(nullable = false)
    private String password; // 비밀번호

    // 역할 변경 시 사용하는 메서드 (필요 시)
    @Column(nullable = false)
    private String role; // 기본값 설정. @Builder.Default를 추가하여 빌더 패턴에서도 기본값이 유지되도록 함

    @CreationTimestamp // pc -> db (날짜주입)
    private Timestamp createdAt;

}
