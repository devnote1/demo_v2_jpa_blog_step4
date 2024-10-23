package com.tenco.blog_jpa_step4.reply;

import lombok.*;

/**
 * ReplyResponse.DTO 댓글 정보에 대한 응답을 처리하는 DTO 클래스입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyResponse {

    private Integer id; // 댓글 ID
    private String comment; // 댓글 내용
    private Integer userId; // 댓글 작성자 ID
    private String username; // 댓글 작성자 이름
    private Boolean owner; // 댓글 작성자가 현재 사용자와 동일한지 여부


}
