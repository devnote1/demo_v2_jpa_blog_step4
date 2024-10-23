package com.tenco.blog_jpa_step4.board;

import com.tenco.blog_jpa_step4.reply.Reply;
import com.tenco.blog_jpa_step4.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


public class BoardResponse {

    @Getter
    @Setter
    public static class DTO {
        private int id;
        private String title;
        private String content;

        // 게시글의 기본 정보를 담은 생성자
        public DTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }

    // 게시글 상세보기 화면을 위한 DTO 클래스
    @Getter
    @Setter
    @ToString
    public static class DetailDTO {
        private int id;
        private String title;
        private String content;
        private int userId;
        private String username; // 게시글 작성자 이름
        private boolean isOwner; // 현재 사용자가 작성자인지 여부
        private List<ReplyDTO> replies = new ArrayList<>(); // 댓글 목록

        // 게시글 상세 정보를 담은 생성자
        public DetailDTO(Board board, User sessionUser) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername(); // join 해서 가져왔음
            this.isOwner = false;
            if(sessionUser != null){
                if(sessionUser.getId() == userId) isOwner = true;
            }

            // 게시글의 댓글 목록을 ReplyDTO로 변환하여 설정
            for (Reply reply : board.getReplies()) {
                this.replies.add(new ReplyDTO(reply, sessionUser));
            }
            //this.replies = board.getReplies().stream().map(reply -> new ReplyDTO(reply, sessionUser)).toList();
        }

        @Getter
        @Setter
        public static class ReplyDTO {
            private int id;
            private String comment;
            private int userId; // 댓글 작성자 아이디
            private String username; // 댓글 작성자 이름
            private boolean isOwner; // 현재 사용자가 댓글 작성자인지 여부

            // 댓글의 기본 정보를 담은 생성자
            public ReplyDTO(Reply reply, User sessionUser) {
                this.id = reply.getId(); // lazy loading 발동
                this.comment = reply.getComment();
                this.userId = reply.getUser().getId();
                this.username = reply.getUser().getUsername(); // lazy loading 발동 (in query)
                this.isOwner = sessionUser != null && sessionUser.getId().equals(userId);
            }
        }
    }

    // 게시글 목록보기 화면을 위한 DTO 클래스
    @Getter
    @Setter
    public static class ListDTO {
        private int id;
        private String title;

        // 게시글의 기본 정보를 담은 생성자
        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
        }
    }
}
