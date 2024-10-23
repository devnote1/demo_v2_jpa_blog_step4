package com.tenco.blog_jpa_step4.user;

import com.tenco.blog_jpa_step4.commom.errors.Exception400;
import com.tenco.blog_jpa_step4.commom.errors.Exception401;
import com.tenco.blog_jpa_step4.commom.errors.Exception404;
import com.tenco.blog_jpa_step4.commom.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service // 서비스 계층으로 등록 및 IoC
public class UserService {

    private final UserJPARepository userJPARepository;

    /**
     * 회원 정보 조회 서비스
     *
     * @param id 조회할 사용자 ID
     * @return 조회된 사용자 객체의 DTO
     * @throws Exception404 사용자를 찾을 수 없는 경우 발생
     */
    public UserResponse.DTO findUserById(int id){
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다"));
        return new UserResponse.DTO(user);
    }

    /**
     * 회원가입 서비스
     *
     * @param reqDTO 회원가입 요청 DTO
     * @return 회원가입 완료된 사용자 정보의 DTO
     * @throws Exception400 중복된 유저네임인 경우 발생
     */
    @Transactional // 트랜잭션 관리
    public UserResponse.DTO signUp(UserRequest.JoinDTO reqDTO) {
        // 1. 유저네임 중복검사 (DB 연결이 필요한 것은 Controller 에서 작성하지 말자)
        Optional<User> userOP = userJPARepository.findByUsername(reqDTO.getUsername());

        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다");
        }
        // 2. 회원가입
        User savedUser = userJPARepository.save(reqDTO.toEntity());
        return new UserResponse.DTO(savedUser);
    }

    /**
     * 로그인 서비스
     *
     * @throws Exception401 인증 실패 시 발생
     */
    // 리턴 타입 변경
    public String signIn(UserRequest.LoginDTO reqDTO) {
        User user = userJPARepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));


        // session.setAttribute("sessionUser", user); // 세션에 사용자 정보 저장
        // jwt 문자열 반환 처리
        return JwtUtil.create(user); // 로그인 시 이메일 정보 제외
    }

    /**
     * 회원 정보 수정 서비스
     *
     * @param id 수정할 사용자 ID
     * @param reqDTO 수정된 사용자 정보 DTO
     * @return 수정된 사용자 객체의 DTO
     * @throws Exception404 사용자를 찾을 수 없는 경우 발생
     */
    @Transactional // 트랜잭션 관리
    public UserResponse.DTO updateUser(int id, UserRequest.UpdateDTO reqDTO,  User sessionUser) {
        // 1. 사용자 조회 및 예외 처리
        User user = userJPARepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다"));
        // 2. 사용자 정보 수정
        user.setPassword(reqDTO.getPassword());
        user.setEmail(reqDTO.getEmail());
        // 더티 체킹을 통해 변경 사항이 자동으로 반영됩니다.

        return new UserResponse.DTO(user);
    }
}
