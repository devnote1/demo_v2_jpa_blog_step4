package com.tenco.blog_jpa_step4.board;

import com.tenco.blog_jpa_step4.commom.errors.Exception403;
import com.tenco.blog_jpa_step4.commom.errors.Exception404;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service // 서비스 계층으로 등록
public class BoardService {

    private final BoardJPARepository boardJPARepository;

    /**
     * 게시글 목록 조회 서비스
     * @return 게시글 목록의 DTO 리스트
     */
    public List<BoardResponse.ListDTO> getAllBoards() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Board> boards = boardJPARepository.findAll(sort);
        return boards.stream().map(BoardResponse.ListDTO::new).toList();
    }

    /**
     * 게시글 상세 조회 서비스
     * @param boardId 조회할 게시글의 ID
     * @param sessionUser 현재 세션 사용자 정보
     * @return 게시글 상세 정보의 DTO
     */
    // 메서드 종료까지 영속성 컨텍스 즉 connection 열어 있음
    // @Transactional 없는 경우 오류 발생 (LazyInitializationException)
    @Transactional
    public BoardResponse.DetailDTO getBoardDetails(int boardId, User sessionUser) {
        Board board = boardJPARepository.findByIdJoinUser(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        return new BoardResponse.DetailDTO(board, sessionUser);
    }

    /**
     * 게시글 작성 서비스
     * @param reqDTO 게시글 작성 요청 DTO
     * @param sessionUser 현재 세션 사용자 정보
     * @return 작성된 게시글의 DTO
     */
    @Transactional
    public BoardResponse.DTO createBoard(BoardDTO.SaveDTO reqDTO, User sessionUser) {
        Board savedBoard = boardJPARepository.save(reqDTO.toEntity(sessionUser));
        return new BoardResponse.DTO(savedBoard);
    }

    /**
     * 게시글 수정 서비스
     * @param boardId 수정할 게시글의 ID
     * @param sessionUserId 현재 세션 사용자 ID
     * @param reqDTO 수정된 게시글 정보의 DTO
     * @return 수정된 게시글의 DTO
     * @throws Exception404 게시글을 찾을 수 없는 경우 발생
     * @throws Exception403 권한이 없는 사용자가 수정하려는 경우 발생
     */
    @Transactional
    public BoardResponse.DTO updateBoard(int boardId, int sessionUserId, BoardDTO.UpdateDTO reqDTO) {
        Board board = boardJPARepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        if (sessionUserId != board.getUser().getId()) {
            throw new Exception403("게시글을 수정할 권한이 없습니다");
        }

        board.setTitle(reqDTO.getTitle());
        board.setContent(reqDTO.getContent());
        return new BoardResponse.DTO(board);
    }

    /**
     * 게시글 삭제 서비스
     * @param boardId 삭제할 게시글의 ID
     * @param sessionUserId 현재 세션 사용자 ID
     * @throws Exception404 게시글을 찾을 수 없는 경우 발생
     * @throws Exception403 권한이 없는 사용자가 삭제하려는 경우 발생
     */
    @Transactional
    public void deleteBoard(int boardId, int sessionUserId) {
        Board board = boardJPARepository.findById(boardId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        if (sessionUserId != board.getUser().getId()) {
            throw new Exception403("게시글을 삭제할 권한이 없습니다");
        }

        boardJPARepository.deleteById(boardId);
    }
}
