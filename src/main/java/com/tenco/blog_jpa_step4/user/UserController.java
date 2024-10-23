package com.tenco.blog_jpa_step4.user;

import com.tenco.blog_jpa_step4.commom.errors.Exception403;
import com.tenco.blog_jpa_step4.commom.errors.Exception404;
import com.tenco.blog_jpa_step4.commom.utils.ApiUtil;
import com.tenco.blog_jpa_step4.commom.utils.Define;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController 사용자(User)와 관련된 HTTP 요청을 처리하는 컨트롤러 계층입니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입

    // 회원 정보 조회 -- > 추후 api/users/{id} 로 수정
    @GetMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> userinfo(@PathVariable(name = "id") Integer id,
                                                              HttpServletRequest request) {
        // 인터셉터에서 설정한 사용자 정보를 가져오기
        User sessionUser = (User) request.getAttribute(Define.SESSION_USER);

        if (sessionUser == null) {
            throw new Exception404("사용자를 찾을 수 없습니다."); // 사용자가 존재하지 않는 경우 예외 던지기
        }

        UserResponse.DTO resDTO = userService.findUserById(sessionUser.getId());
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    /**
     * 사용자 정보 수정 요청 처리
     *
     * @param id      수정할 사용자 ID
     * @param reqDTO  수정된 사용자 정보 DTO
     * @return 수정된 사용자 정보의 DTO
     */
    @PutMapping("/api/users/{id}")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> updateUser(@PathVariable int id,
                                                                @RequestBody UserRequest.UpdateDTO reqDTO,
                                                                HttpServletRequest request) {

        // 인터셉터에서 설정한 사용자 정보를 가져오기
        User sessionUser = (User) request.getAttribute(Define.SESSION_USER);

        if (sessionUser == null) {
            throw new Exception404("사용자를 찾을 수 없습니다."); // 사용자가 존재하지 않는 경우 예외 던지기
        }

        if (sessionUser.getId() != id) {
            throw new Exception403("해당 사용자를 수정할 권한이 없습니다."); // 권한 없음 예외 던지기
        }

        UserResponse.DTO resDTO = userService.updateUser(id, reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiUtil<UserResponse.DTO>> join(@RequestBody UserRequest.JoinDTO reqDTO) {
        UserResponse.DTO resDTO = userService.signUp(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(resDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDTO reqDTO) {
        String jwt = userService.signIn(reqDTO);
        return ResponseEntity.ok()
                // 반드시 주의!!! Bearer 문자열 뒤에 반드시 한칸에 공백을 넣어 주세요 ~~
                .header("Authorization", "Bearer " + jwt)
                .body(new ApiUtil<>(null));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new ApiUtil<>(null));
    }
}