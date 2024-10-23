package com.tenco.blog_jpa_step4.reply;

import com.tenco.blog_jpa_step4.board.BoardResponse;
import com.tenco.blog_jpa_step4.commom.utils.ApiUtil;
import com.tenco.blog_jpa_step4.commom.utils.Define;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글 생성
     *
     * @param reqDTO  댓글 작성 정보
     * @param request HTTP 요청 객체
     * @return 댓글 작성 후 최신 게시글 상세 정보 반환
     */
    @PostMapping("/api/replies")
    public ResponseEntity<?> save(@RequestBody ReplyRequest.SaveDTO reqDTO, HttpServletRequest request) {
        // JWT 로그인한 사용자 정보 가져오기
        User sessionUser = (User) request.getAttribute(Define.SESSION_USER); // 인터셉터에서 설정한 사용자 정보 가져오기
        BoardResponse.DetailDTO boardDetail = replyService.saveReply(reqDTO, sessionUser);
        return ResponseEntity.ok(boardDetail);
    }

    /**
     * 댓글 삭제
     *
     * @param boardId 게시글 ID
     * @param replyId 댓글 ID
     * @param request HTTP 요청 객체
     * @return 리다이렉트 URL
     */
    @DeleteMapping("/api/replies/{replyId}/boards/{boardId}")
    public ResponseEntity<?> delete(@PathVariable(name = "boardId") Integer boardId, @PathVariable(name = "replyId") Integer replyId, HttpServletRequest request) {
        // JWT 로그인한 사용자 정보 가져오기
        User sessionUser = (User) request.getAttribute(Define.SESSION_USER);
        if (sessionUser == null) {
            return ResponseEntity.status(401).body(new ApiUtil<>(400, "댓글 삭제 실패")); // 로그인하지 않은 경우 401 응답
        }
        replyService.deleteReply(replyId, sessionUser.getId(), boardId);
        return ResponseEntity.ok( new ApiUtil<>("댓글 삭제 성공"));
    }
}