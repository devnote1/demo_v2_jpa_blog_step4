package com.tenco.blog_jpa_step4.board;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository // Spring에서 관리하는 '레포지토리 빈'으로 등록, 데이터베이스와의 상호작용을 담당
public class BoardRepository {

    // EntityManager: JPA에서 데이터베이스와 상호작용을 관리하는 핵심 클래스.
    private final EntityManager em;


    /**
     * 게시글 수정 메서드 (JPQL 사용)
     * @param id 수정할 게시글의 ID
     * @param title 수정할 제목
     * @param content 수정할 내용
     * @param username 수정할 작성자 이름
     */
    @Transactional // 트랜잭션 내에서 실행되도록 보장
    public void updateByIdJPQL(int id, String title, String content, String username) {
        // JPQL을 사용하여 게시글 수정 쿼리 작성
        String jpql = "UPDATE Board b SET b.title = :title, b.content = :content  WHERE b.id = :id";
        Query query = em.createQuery(jpql);
        query.setParameter("title", title);
        query.setParameter("content", content);
        query.setParameter("id", id);
        query.executeUpdate(); // 수정 쿼리 실행
    }

    /**
     * 게시글 수정 메서드 (JPA API 사용)
     * @param id 수정할 게시글의 ID
     * @param title 수정할 제목
     * @param content 수정할 내용
     * @param username 수정할 작성자 이름
     */
    @Transactional // 트랜잭션 내에서 실행되도록 보장
    public void updateByIdJPA(int id, String title, String content, String username) {
        // EntityManager를 사용하여 게시글 엔티티 조회
        Board board = em.find(Board.class, id);
        if(board != null){
            // 엔티티의 필드를 직접 수정
            board.setTitle(title);
            board.setContent(content); 
            // 변경 사항은 트랜잭션 커밋 시 자동으로 반영됩니다.
        }
    }

    /**
     * 게시글 조회 메서드
     * @param id 조회할 게시글의 ID
     * @return 조회된 Board 엔티티, 존재하지 않으면 null 반환
     */
    public Board findById(int id) {
        // EntityManager의 find 메서드를 사용하여 게시글 조회
        Board board = em.find(Board.class, id);
        return board;
    }

    /**
     * 게시글 삭제 메서드
     * @param id 삭제할 게시글의 ID
     */
    @Transactional // 트랜잭션 내에서 실행되도록 보장
    public void deleteById(int id){
        // JPQL을 사용하여 게시글 삭제 쿼리 작성
        Query query = em.createQuery("DELETE FROM Board b WHERE b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate(); // 삭제 쿼리 실행
    }

    /**
     * 게시글 저장 메서드
     * @param board 저장할 게시글 엔티티
     * @return 저장된 게시글 엔티티
     */
    @Transactional // 이 메서드가 데이터베이스와의 트랜잭션 내에서 실행되도록 보장
    public Board save(Board board){
        em.persist(board);
        return board;
    }

    

    /**
     * JPQL의 FETCH 조인 사용 - 성능 최적화
     * 한번에 쿼리도 직접 조인해서 가져옵니다.
     * ID로 게시글 조회 (User 엔티티를 함께 로딩)
     * @param id 게시글의 ID
     * @return 조회된 Board 엔티티
     */
    public Board findByIdJoinUser(int id) {
        // Fetch Join을 사용하여 Board와 User를 한 번에 로딩
        String jpql = "SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id";
        return em.createQuery(jpql, Board.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * 모든 게시글 조회 (Eager Fetching)
     * @return 게시글 리스트
     */
    public List<Board> findAll() {
        TypedQuery<Board> jpql = em.createQuery(
            "SELECT b FROM Board b ORDER BY b.id DESC", Board.class);
        return jpql.getResultList();
    }
}
