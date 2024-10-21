package com.tenco.blog_jpa_step4.board;

import com.tenco.blog_jpa_step4.commom.utils.ApiUtil;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BoardController 블로그 게시글과 관련된 HTTP 요청을 처리하는 REST 컨트롤러 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@RestController // @Controller -> @RestController로 변경
@RequestMapping("/api/boards") // 공통 경로 설정
public class BoardController {

    private final BoardService boardService; // BoardService 주입

    /**
     * 게시글 목록 조회 처리 메서드
     * 요청 주소: **GET http://localhost:8080/api/boards**
     *
     * @return 게시글 목록 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<BoardResponse.ListDTO>> getAllBoards() {
        List<BoardResponse.ListDTO> boardList = boardService.getAllBoards();
        return ResponseEntity.ok(boardList);
    }

    /**
     * 게시글 상세보기 처리 메서드
     * 요청 주소: **GET http://localhost:8080/api/boards/{id}**
     *
     * @param id 게시글의 ID
     * @param session HTTP 세션 객체
     * @return 게시글 상세보기 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse.DetailDTO> getBoardDetail(@PathVariable Integer id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO boardDetail = boardService.getBoardDetails(id, sessionUser);
        return ResponseEntity.ok(boardDetail);
    }

    /**
     * 게시글 작성 처리 메서드
     * 요청 주소: **POST http://localhost:8080/api/boards**
     *
     * @param dto     게시글 작성 요청 DTO
     * @param session HTTP 세션 객체
     * @return 작성된 게시글 DTO
     */
    @PostMapping
    public ResponseEntity<BoardResponse.DTO> createBoard(@RequestBody BoardDTO.SaveDTO dto, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return ResponseEntity.status(401).build(); // 인증되지 않은 경우 401 반환
        }

        // 게시글 작성 서비스 호출
        BoardResponse.DTO savedBoard = boardService.createBoard(dto, sessionUser);
        return ResponseEntity.ok(savedBoard);
    }

    /**
     * 게시글 수정 처리 메서드
     * 요청 주소: **PUT http://localhost:8080/api/boards/{id}**
     *
     * @param id        수정할 게시글의 ID
     * @param updateDTO 수정된 데이터를 담은 DTO
     * @param session   HTTP 세션 객체
     * @return 수정된 게시글 DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse.DTO> updateBoard(@PathVariable(name = "id") Integer id,
                                                         @RequestBody BoardDTO.UpdateDTO updateDTO, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return ResponseEntity.status(401).build(); // 인증되지 않은 경우 401 반환
        }

        // 게시글 수정 서비스 호출
        BoardResponse.DTO updatedBoard = boardService.updateBoard(id, sessionUser.getId(), updateDTO);
        return ResponseEntity.ok(updatedBoard);
    }

    /**
     * 게시글 삭제 처리 메서드
     * 요청 주소: **DELETE http://localhost:8080/api/boards/{id}**
     *
     * @param id      삭제할 게시글의 ID
     * @param session HTTP 세션 객체
     * @return 성공적으로 삭제된 경우 204 No Content 응답
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiUtil<?>> deleteBoard(@PathVariable(name = "id") Integer id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return ResponseEntity.status(401).build(); // 인증되지 않은 경우 401 반환
        }

        // 게시글 삭제 서비스 호출
        boardService.deleteBoard(id, sessionUser.getId());

        // 삭제 후 응답
        return ResponseEntity.ok(null);// 204 No Content 응답
    }
}