package com.tenco.blog_jpa_step4.user;

import lombok.Data;

@Data
public class UserDTO {

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role("USER")
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private String password;
        private String email;
    }
}
