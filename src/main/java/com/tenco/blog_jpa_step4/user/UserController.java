package com.tenco.blog_jpa_step4.user;

import com.tenco.blog_jpa_step4.commom.errors.Exception400;
import com.tenco.blog_jpa_step4.commom.errors.Exception401;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * UserController는 사용자(User)와 관련된 HTTP 요청을 처리하는 컨트롤러 계층입니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입

    /**
     * 회원 수정 폼을 표시하는 메서드
     * 요청 주소: **GET http://localhost:8080/user/update-form**
     *
     * @param request HTTP 요청 객체
     * @param session HTTP 세션 객체
     * @return 회원 수정 페이지 뷰
     */
    @GetMapping("/user/update-form")
    public String updateForm(HttpServletRequest request, HttpSession session) {
        log.info("회원 수정 페이지 이동");

        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 서비스 레이어를 통해 사용자 정보 조회
        User user = userService.readUser(sessionUser.getId());

        // 뷰에 사용자 정보 전달
        request.setAttribute("name", "회원정보 수정");
        request.setAttribute("user", user);
        return "user/update-form";
    }

    /**
     * 회원정보 수정 처리 메서드
     * 요청 주소: **POST http://localhost:8080/user/update**
     *
     * @param updateDTO 수정된 데이터를 담은 DTO
     * @param session   HTTP 세션 객체
     * @return 메인 페이지로 리다이렉트
     */
    @PostMapping("/user/update")
    public String update(@ModelAttribute(name = "updateDTO") UserDTO.UpdateDTO updateDTO, HttpSession session) {
        // 세션에서 로그인한 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login-form"; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
        }

        // 서비스 레이어를 통해 사용자 정보 수정
        User updatedUser = userService.updateUser(sessionUser.getId(), updateDTO);

        // 세션 정보 동기화
        session.setAttribute("sessionUser", updatedUser);

        // 수정 완료 후 메인 페이지로 리다이렉트
        return "redirect:/";
    }

    /**
     * 회원가입 폼을 표시하는 메서드
     * 요청 주소: **GET http://localhost:8080/join-form**
     *
     * @param model 뷰에 전달할 모델 객체
     * @return 회원가입 폼 뷰
     */
    @GetMapping("/join-form")
    public String joinForm(Model model) {
        log.info("회원가입 페이지 이동");
        model.addAttribute("name", "회원가입 페이지");
        return "user/join-form";
    }

    /**
     * 회원가입 처리 메서드
     * 요청 주소: **POST http://localhost:8080/join**
     *
     * @param joinDTO 회원가입 데이터를 담은 DTO
     * @return 로그인 폼 페이지로 리다이렉트
     */
    @PostMapping("/join")
    public String join(@ModelAttribute(name = "joinDTO") UserDTO.JoinDTO joinDTO) {
        try {
            // 서비스 레이어를 통해 회원가입 처리
            userService.signUp(joinDTO);
        } catch (DataIntegrityViolationException e) {
            // 유저네임 중복 시 예외 처리
            throw new Exception400("동일한 유저네임이 존재합니다");
        }

        // 회원가입 완료 후 로그인 폼 페이지로 리다이렉트
        return "redirect:/login-form";
    }

    /**
     * 로그인 처리 메서드
     * 요청 주소: **POST http://localhost:8080/login**
     *
     * @param dto     로그인 데이터를 담은 DTO
     * @param session HTTP 세션 객체
     * @return 메인 페이지로 리다이렉트
     * @throws Exception401 인증 실패 시 발생
     */
    @PostMapping("/login")
    public String login(UserDTO.LoginDTO dto, HttpSession session) {
        try {
            // 서비스 레이어를 통해 로그인 처리
            User sessionUser = userService.signIn(dto);
            // 세션에 로그인한 사용자 정보 저장
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/";
        } catch (EmptyResultDataAccessException e) {
            // 인증 실패 시 예외 처리
            throw new Exception401("유저네임 혹은 비밀번호가 틀렸습니다");
        }
    }

    /**
     * 로그아웃 처리 메서드
     * 요청 주소: **GET http://localhost:8080/logout**
     *
     * @param session HTTP 세션 객체
     * @return 메인 페이지로 리다이렉트
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화 (로그아웃)
        return "redirect:/"; // 메인 페이지로 리다이렉트
    }

    /**
     * 로그인 페이지로 이동하는 메서드
     * 요청 주소: **GET http://localhost:8080/login-form**
     *
     * @param model 뷰에 전달할 모델 객체
     * @return 로그인 페이지 뷰
     */
    @GetMapping("/login-form")
    public String loginForm(Model model) {
        log.info("로그인 페이지 이동");
        model.addAttribute("name", "로그인 페이지");
        return "user/login-form"; // Mustache 템플릿 경로: user/login-form.mustache
    }
}