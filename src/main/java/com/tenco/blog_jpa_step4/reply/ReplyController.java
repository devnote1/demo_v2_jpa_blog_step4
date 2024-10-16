package com.tenco.blog_jpa_step4.reply;

import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final ReplyService replyService;

    /**
     * 댓글 생성
     *
     * @param reqDTO  댓글 작성 정보
     * @param session HTTP 세션 객체
     * @return 리다이렉트 URL
     */
    // @PostMapping("/reply/save")
    @PostMapping("/api/replies")
    public String save(ReplyDTO.SaveDTO reqDTO, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        replyService.saveReply(reqDTO, sessionUser);
        return "redirect:/board/" + reqDTO.getBoardId();
    }

    /**
     * 댓글 삭제
     *
     * @param boardId 게시글 ID
     * @param replyId 댓글 ID
     * @param session HTTP 세션 객체
     * @return 리다이렉트 URL
     */
    @DeleteMapping("/api/replies/{id}")
    //@PostMapping("/board/{boardId}/reply/{replyId}/delete")
    public String delete(@PathVariable Integer boardId, @PathVariable Integer replyId, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        replyService.deleteReply(replyId, sessionUser.getId(), boardId);
        return "redirect:/board/" + boardId;
    }
}
