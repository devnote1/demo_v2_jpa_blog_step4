package com.tenco.blog_jpa_step4.user;

import com.tenco.blog_jpa_step4.commom.utils.ApiUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController는 사용자(User)와 관련된 HTTP 요청을 처리하는 컨트롤러 계층입니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입

    // 회원 정보 수정 -- > 추후 api/users/{id} 로 수정
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> userinfo(@PathVariable(name = "id") Integer id) {
        UserResponse.DTO resDTO = userService.findUserById(id);
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    /**
     * 사용자 정보 수정 요청 처리
     *
     * @param id      수정할 사용자 ID
     * @param reqDTO  수정된 사용자 정보 DTO
     * @param session HTTP 세션 객체
     * @return 수정된 사용자 정보의 DTO
     */
    @PutMapping("/api/user/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> updateUser(@PathVariable int id,
                                                       @RequestBody UserRequest.UpdateDTO reqDTO,
                                                       HttpSession session) {
        UserResponse.DTO resDTO = userService.updateUser(id, reqDTO, session);
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> join(@RequestBody UserRequest.JoinDTO reqDTO) {
        UserResponse.DTO resDTO = userService.signUp(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> login(@RequestBody UserRequest.LoginDTO reqDTO, HttpSession session) {
        UserResponse.DTO resDTO = userService.signIn(reqDTO, session);
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

}