package com.tenco.blog_jpa_step4.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyJPARepository extends JpaRepository<Reply, Integer> {

    // 커스텀 쿼리 (Query 어노테이션 활용 JPQL 쿼리 사용
    @Query("select r from Reply r where r.board.id = :boardId")
    List<Reply> findByBoardId(@Param("boardId") Integer boardId);

    // 메서드 네이밍 전략을 활용하여 쿼리 자동 생성
    // List<Reply> findByBoard_Id(Integer boardId);
}
