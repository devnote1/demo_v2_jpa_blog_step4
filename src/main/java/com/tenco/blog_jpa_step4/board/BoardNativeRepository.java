package com.tenco.blog_jpa_step4.board;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

/**
 * 네이티비 쿼리로 데이터를 가져 오는 방법 !!! 
 * BoardNativeRepository는 게시글과 관련된 데이터베이스 작업을 수행하는 레포지토리 클래스입니다.
 */
@RequiredArgsConstructor
@Repository
public class BoardNativeRepository {
    private final EntityManager em;
    
    /**
     * 새로운 게시글을 데이터베이스에 저장합니다.
     */
    @Transactional
    public void save(String title, String content) {
        // 1. 게시글 삽입 SQL 작성
        Query query = em.createNativeQuery(
            "INSERT INTO board_tb(title, content, created_at) VALUES (?, ?, NOW())");
        // 2. SQL 파라미터 설정
        query.setParameter(1, title);
        query.setParameter(2, content);
        // 3. SQL 실행
        query.executeUpdate();
    }

    /**
     * 특정 ID의 게시글을 조회합니다.
     */
    public Board findById(int id) {
        // 1. 게시글 조회 SQL 작성
        Query query = em.createNativeQuery(
            "SELECT * FROM board_tb WHERE id = ?", Board.class);
        // 2. SQL 파라미터 설정
        query.setParameter(1, id);
        // 3. 단일 결과 반환
        return (Board) query.getSingleResult();
    }

    /**
     * 모든 게시글을 조회합니다.
     */
    public List<Board> findAll() {
        // 1. 모든 게시글 조회 SQL 작성
        Query query = em.createNativeQuery(
            "SELECT * FROM board_tb ORDER BY id DESC", Board.class);
        // 2. 결과 리스트 반환
        return query.getResultList();
    }

    /**
     * 특정 ID의 게시글을 업데이트합니다.
     */
    @Transactional
    public void updateById(int id, String title, String content) {
        // 1. 게시글 업데이트 SQL 작성
        Query query = em.createNativeQuery(
            "UPDATE board_tb SET title = ?, content = ? WHERE id = ?");
        // 2. SQL 파라미터 설정
        query.setParameter(1, title);
        query.setParameter(2, content);
        query.setParameter(3, id);
        // 3. SQL 실행
        query.executeUpdate();
    }

    /**
     * 특정 ID의 게시글을 삭제합니다.
     */
    @Transactional
    public void deleteById(int id) {
        // 1. 게시글 삭제 SQL 작성
        Query query = em.createNativeQuery(
            "DELETE FROM board_tb WHERE id = ?");
        // 2. SQL 파라미터 설정
        query.setParameter(1, id);
        // 3. SQL 실행
        query.executeUpdate();
    }
}

