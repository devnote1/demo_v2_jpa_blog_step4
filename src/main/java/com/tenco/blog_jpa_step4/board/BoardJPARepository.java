package com.tenco.blog_jpa_step4.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// BoardJPARepository는 Board 엔티티에 대한 CRUD 기능을 제공한다.
public interface BoardJPARepository extends JpaRepository<Board, Integer> {

    // JPQL - Fetch JOIN 사용
    // 커스텀 쿼리 메서드: Board와 User를 조인하여 특정 Board 조회
    @Query("select b from Board b join fetch b.user u where b.id = :id")
    Optional<Board> findByIdJoinUser(@Param("id") int id);
}