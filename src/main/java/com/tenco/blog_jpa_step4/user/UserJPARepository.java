package com.tenco.blog_jpa_step4.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// JPARepository를 상속받아 기본적인 CRUD 메서드를 사용할 수 있다.
// Entity 클래스와 기본 키 타입을 지정한다.
public interface UserJPARepository extends JpaRepository<User, Integer> {

    /**
     * 순서 2
     * 메서드 이름 기반 쿼리(Method Name Query):
     * - findByUsernameAndPassword
     * - JPQL 자동 생성:
     *   SELECT u FROM User u WHERE u.username = ?1 AND u.password = ?2
     *
     * @param username 사용자 이름
     * @param password 사용자 비밀번호
     * @return Optional<User> - 조건에 맞는 사용자가 있으면 Optional에 담아 반환, 없으면 Optional.empty()
     */
    Optional<User> findByUsernameAndPassword(
            @Param("username") String username,
            @Param("password") String password
    );

    /**
     * 순서 1
     * 사용자 이름과 비밀번호를 기반으로 사용자를 조회하는 메서드입니다.
     * 메서드 이름을 통해 Spring Data JPA가 자동으로 JPQL 쿼리를 생성합니다.
     * 메서드 이름 기반 쿼리(Method Name Query)
     * - findByUsername
     * - JPQL 자동 생성:
     *   SELECT u FROM User u WHERE u.username = ?1
     *
     *
     * @param username 사용자 이름
     * @return Optional<User> - 조건에 맞는 사용자가 있으면 Optional에 담아 반환, 없으면 Optional.empty()
     */
    Optional<User> findByUsername(@Param("username") String username);

}