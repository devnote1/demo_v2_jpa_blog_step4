package com.tenco.blog_jpa_step4.board;

import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

/**
 * BoardController는 블로그 게시글과 관련된 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService; // BoardService 주입

    /**
     * 게시글 수정 처리 메서드
     * 요청 주소: **POST http://localhost:8080/board/{id}/update**
     *
     * @param id        수정할 게시글의 ID
     * @param updateDTO 수정된 데이터를 담은 DTO
     * @param session   HTTP 세션 객체
     * @return 게시글 상세보기 페이지로 리다이렉트
     */
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable(name = "id") Integer id,
                         @ModelAttribute(name = "updateDTO") BoardDTO.UpdateDTO updateDTO,
                         HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 게시글 수정 서비스 호출
        boardService.updateBoard(id, sessionUser.getId(), updateDTO);

        // 수정 완료 후 게시글 상세보기 페이지로 리다이렉트
        return "redirect:/board/" + id;
    }



    /**
     * 게시글 삭제 처리 메서드
     * 요청 주소: **POST http://localhost:8080/board/{id}/delete**
     *
     * @param id      삭제할 게시글의 ID
     * @param session HTTP 세션 객체
     * @return 메인 페이지로 리다이렉트
     */
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id,
                         HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인 페이지로 리다이렉트
        }

        // 게시글 삭제 서비스 호출
        boardService.deleteBoard(id, sessionUser.getId());

        // 메인 페이지로 리다이렉트
        return "redirect:/";
    }

    /**
     * 게시글 작성 처리 메서드
     * 요청 주소: **POST http://localhost:8080/board/save**
     *
     * @param dto     게시글 작성 요청 DTO
     * @param session HTTP 세션 객체
     * @return 메인 페이지로 리다이렉트
     */
    @PostMapping("/board/save")
    public String save(@ModelAttribute BoardDTO.SaveDTO dto,
                       HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 세션 유효성 검증
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인 페이지로 리다이렉트
        }

        // 게시글 작성 서비스 호출
        boardService.createBoard(dto, sessionUser);

        // 메인 페이지로 리다이렉트
        return "redirect:/";
    }


    /**
     * 게시글 상세보기 처리 메서드
     * 요청 주소: **GET http://localhost:8080/board/{id}**
     *
     * @param id      게시글의 ID
     * @param request HTTP 요청 객체
     * @param session HTTP 세션 객체
     * @return 게시글 상세보기 페이지 뷰
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id,
                         HttpServletRequest request,
                         HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.getBoardDetails(id, sessionUser);

        // 현재 사용자가 게시글의 작성자인지 확인하여 isOwner 필드 설정
        boolean isOwner = false;
        if (sessionUser != null && board != null && board.getUser() != null) {
            if (Objects.equals(sessionUser.getId(), board.getUser().getId())) {
                isOwner = true;
            }
        }

        // 뷰에 데이터 전달
        request.setAttribute("isOwner", isOwner);
        request.setAttribute("board", board);
        return "board/detail";
    }

}