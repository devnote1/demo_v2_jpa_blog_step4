package com.tenco.blog_jpa_step4.board;

import com.tenco.blog_jpa_step4.commom.utils.ApiUtil;
import com.tenco.blog_jpa_step4.commom.utils.Define;
import com.tenco.blog_jpa_step4.commom.utils.JwtUtil;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpServletRequest;
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
public class BoardController {

    private final BoardService boardService; // BoardService 주입

    /**
     * 게시글 목록 조회 처리 메서드
     * 요청 주소: **GET http://localhost:8080/api/boards**
     *
     * @return 게시글 목록 DTO 리스트
     */
    @GetMapping({"/boards", "/"})
    public ResponseEntity<List<BoardResponse.ListDTO>> getAllBoards() {
        List<BoardResponse.ListDTO> boardList = boardService.getAllBoards();
        return ResponseEntity.ok(boardList);
    }

    /**
     * 게시글 상세보기 처리 메서드
     * 요청 주소: **GET http://localhost:8080/boards/{id}**
     *
     * @param id 게시글의 ID
     * @param request HTTP 요청 객체
     * @return 게시글 상세보기 DTO
     */
    @GetMapping("/boards/{id}")
    public ResponseEntity<ApiUtil<BoardResponse.DetailDTO>> getBoardDetail(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        User sessionUser = null;

        // JWT 토큰이 있는 경우에만 검증
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            sessionUser = JwtUtil.verify(token); // 검증된 사용자를 설정
        }

        // 게시글 상세보기 로직
        BoardResponse.DetailDTO boardDetail = boardService.getBoardDetails(id, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(boardDetail));
    }

    /**
     * 게시글 작성 처리 메서드
     * 요청 주소: POST http://localhost:8080/api/boards
     * @param dto     게시글 작성 요청 DTO
     * @return 작성된 게시글 DTO
     */
    @PostMapping("/api/boards")
    public ResponseEntity<?> createBoard(@RequestBody BoardDTO.SaveDTO dto, HttpServletRequest request) {
        User sessionUser = null;

        // JWT 토큰이 있는 경우에만 검증 - 추후 인터셉터 활용 예정
        String authorizationHeader = request.getHeader(Define.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(Define.BEARER)) {
            String token = authorizationHeader.replace(Define.BEARER, "");
            sessionUser = JwtUtil.verify(token); // 검증된 사용자를 설정
        }

        // 게시글 작성 서비스 호출
        BoardResponse.DTO savedBoard = boardService.createBoard(dto, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(savedBoard));
    }

    /**
     * 게시글 수정 처리 메서드
     * 요청 주소: PUT http://localhost:8080/api/boards/{id}
     * @param id        수정할 게시글의 ID
     * @param updateDTO 수정된 데이터를 담은 DTO
     * @return 수정된 게시글 DTO
     */
    @PutMapping("/api/boards/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable(name = "id") Integer id,
                                                         @RequestBody BoardDTO.UpdateDTO updateDTO, HttpServletRequest request) {
        User sessionUser = null;
        String authorizationHeader = request.getHeader(Define.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(Define.BEARER)) {
            String token = authorizationHeader.replace(Define.BEARER, "");
            sessionUser = JwtUtil.verify(token);
        }

        // 인증 사용자 여부 검사
        if (sessionUser == null) {
            return ResponseEntity.status(401).build(); // 인증되지 않은 경우 401 반환
        }
        // 게시글 수정 서비스 호출
        BoardResponse.DTO updatedBoard = boardService.updateBoard(id, sessionUser.getId(), updateDTO);
        return ResponseEntity.ok(new ApiUtil<>(updatedBoard));
    }

    /**
     * 게시글 삭제 처리 메서드
     * 요청 주소: **DELETE http://localhost:8080/api/boards/{id}**
     * @param id      삭제할 게시글의 ID
     * @return 성공적으로 삭제된 경우 204 No Content 응답
     */
    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable(name = "id") Integer id, HttpServletRequest request) {
        User sessionUser = null;

        String authorizationHeader = request.getHeader(Define.AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith(Define.BEARER)) {
            String token = authorizationHeader.replace(Define.BEARER, "");
            sessionUser = JwtUtil.verify(token);
        }
        // 세션 유효성 검증
        if (sessionUser == null) {
            return ResponseEntity.status(401).build(); // 인증되지 않은 경우 401 반환
        }
        // 게시글 삭제 서비스 호출
        boardService.deleteBoard(id, sessionUser.getId());
        // 삭제 후 응답
        return ResponseEntity.ok(new ApiUtil<>(null));// 204 No Content 응답
    }
}