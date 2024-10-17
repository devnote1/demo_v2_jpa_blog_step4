package com.tenco.blog_jpa_step4.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @Getter
    @Setter
    public static class LoginDTO {
        private String username;
        private String password;
    }

    @Getter
    @Setter
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

    @Getter
    @Setter
    public static class UpdateDTO {
        private String password;
        private String email;
    }
}
