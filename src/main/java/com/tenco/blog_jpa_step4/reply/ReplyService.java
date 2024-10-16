package com.tenco.blog_jpa_step4.reply;

import com.tenco.blog_jpa_step4.board.Board;
import com.tenco.blog_jpa_step4.board.BoardJPARepository;
import com.tenco.blog_jpa_step4.commom.errors.Exception403;
import com.tenco.blog_jpa_step4.commom.errors.Exception404;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final BoardJPARepository boardJPARepository;
    private final ReplyJPARepository replyJPARepository;

    @Transactional
    public void saveReply(ReplyDTO.SaveDTO reqDTO, User sessionUser) {
        Board board = boardJPARepository.findById(reqDTO.getBoardId())
                .orElseThrow(() -> new Exception404("없는 게시글에 댓글을 작성할 수 없어요"));
        Reply reply = reqDTO.toEntity(sessionUser, board);

        replyJPARepository.save(reply);
    }

    @Transactional
    public void deleteReply(Integer replyId, Integer sessionUserId, Integer boardId) {
        Reply reply = replyJPARepository.findById(replyId)
                .orElseThrow(() -> new Exception404("없는 댓글을 삭제할 수 없어요"));

        if(!reply.getUser().getId().equals(sessionUserId)){
            throw new Exception403("댓글을 삭제할 권한이 없어요");
        }

        if(!reply.getBoard().getId().equals(boardId)){
            throw new Exception403("해당 게시글의 댓글이 아닙니다.");
        }

        replyJPARepository.deleteById(replyId);
    }

}
