package com.tenco.blog_jpa_step4.reply;

import com.tenco.blog_jpa_step4.board.Board;
import com.tenco.blog_jpa_step4.user.User;
import lombok.Data;

public class ReplyDTO {

    @Data
    public static class SaveDTO {

        private Integer boardId;
        private String comment;

        public Reply toEntity(User sessionUser, Board board){
            return Reply.builder()
                    .comment(comment)
                    .board(board)
                    .user(sessionUser)
                    .build();
        }
    }
}
