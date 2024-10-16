package com.tenco.blog_jpa_step4.board;

import com.tenco.blog_jpa_step4.user.User;

import lombok.Data;

public class BoardDTO {
    
    @Data
    public static class SaveDTO {
        private String title;
        private String content;

        /**
         * DTO를 엔티티로 변환하는 메서드
         * @param user 작성자 User 엔티티
         * @return 생성된 Board 엔티티
         */
        public Board toEntity(User user){
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user) // 영속화된 User 엔티티를 설정
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private String username;
        private String title;
        private String content;
    }
}
