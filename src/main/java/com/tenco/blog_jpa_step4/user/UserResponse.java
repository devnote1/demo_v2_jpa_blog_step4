package com.tenco.blog_jpa_step4.user;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    @Getter
    @Setter
    public static class DTO {

        private int id;
        private String username;
        private String email;

        // 엔티티에서 --> DTO 로 변환시 생성자 활용
        // 기본 생성자
        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }

        // 특정 필드만 초기화하는 생성자 (이메일 포함 여부를 선택)
        public DTO(User user, boolean includeEmail) {
            this.id = user.getId();
            this.username = user.getUsername();
            if (includeEmail) {
                this.email = user.getEmail();
            } else {
                this.email = null; // 이메일 정보 제외 시 null로 설정
            }
            // this.email = includeEmail ? user.getEmail() : null;
        }
    }
}
