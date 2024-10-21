package com.tenco.blog_jpa_step4.user;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final EntityManager em; // DI

    /**
     * 사용자 조회 메서드
     * 
     * @param id 조회할 사용자의 ID
     * @return 조회된 User 엔티티, 존재하지 않으면 null 반환
     */
    public User findById(int id) {
        return em.find(User.class, id);
    }

     /**
     * 사용자 수정 메서드 (더티 체킹 사용)
     * 
     * @param id       수정할 사용자의 ID
     * @param password 수정할 비밀번호
     * @param email    수정할 이메일
     * @return 수정된 User 엔티티
     */
    @Transactional
    public User updateById(int id, String password, String email){
        User user = findById(id);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }

    // 회원 등록 (JPA API 사용) , 반환 : User 엔티티
    public User save(User user) {
        em.persist(user);
        return user;
    }

    /**
     * 사용자 이름과 비밀번호로 사용자 조회
     * 
     * @param username 사용자 이름
     * @param password 사용자 비밀번호
     * @return 조회된 User 엔티티
     */
    public User findByUsernameAndPassword(String username, String password) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getSingleResult();
    }
}