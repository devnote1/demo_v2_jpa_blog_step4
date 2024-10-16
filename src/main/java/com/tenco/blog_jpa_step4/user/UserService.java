package com.tenco.blog_jpa_step4.user;

import com.tenco.blog_jpa_step4.commom.errors.Exception400;
import com.tenco.blog_jpa_step4.commom.errors.Exception401;
import com.tenco.blog_jpa_step4.commom.errors.Exception404;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserService는 사용자(User)와 관련된 비즈니스 로직을 처리하는 서비스 계층입니다.
 * @Service 어노테이션을 통해 스프링의 IoC 컨테이너에 등록됩니다.
 */
@RequiredArgsConstructor
@Service // 서비스 계층으로 등록 및 IoC
public class UserService {

    private final UserJPARepository userJPARepository;

    /**
     * 회원가입 서비스
     *
     * @param reqDTO 회원가입 요청 DTO
     * @throws Exception400 중복된 유저네임인 경우 발생
     */
    @Transactional // 트랜잭션 관리
    public void signUp(UserDTO.JoinDTO reqDTO){
        // 1. 유저네임 중복검사 (DB연결이 필요한 것은 Controller에서 작성하지 말자)
        Optional<User> userOP = userJPARepository.findByUsername(reqDTO.getUsername());

        if(userOP.isPresent()){
            throw new Exception400("중복된 유저네임입니다");
        }

        // 2. 회원가입
        userJPARepository.save(reqDTO.toEntity());
    }

    /**
     * 로그인 서비스
     *
     * @param reqDTO 로그인 요청 DTO
     * @return 인증된 사용자 객체
     * @throws Exception401 인증 실패 시 발생
     */
    public User signIn(UserDTO.LoginDTO reqDTO){
        User sessionUser = userJPARepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));
        return sessionUser;
    }

    /**
     * 회원 정보 조회 서비스
     *
     * @param id 조회할 사용자 ID
     * @return 조회된 사용자 객체
     * @throws Exception404 사용자를 찾을 수 없는 경우 발생
     */
    public User readUser(int id){
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다"));
        return user;
    }

    /**
     * 회원 정보 수정 서비스
     *
     * @param id 수정할 사용자 ID
     * @param reqDTO 수정된 사용자 정보 DTO
     * @return 수정된 사용자 객체
     * @throws Exception404 사용자를 찾을 수 없는 경우 발생
     */
    @Transactional // 트랜잭션 관리
    public User updateUser(int id, UserDTO.UpdateDTO reqDTO){
        // 1. 사용자 조회 및 예외 처리
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다"));

        // 2. 사용자 정보 수정
        user.setPassword(reqDTO.getPassword());
        user.setEmail(reqDTO.getEmail());

        // 더티 체킹을 통해 변경 사항이 자동으로 반영됩니다.
        return user;
    }
}
