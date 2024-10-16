package com.tenco.blog_jpa_step4.board;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * BoardJPARepositoryTest는 BoardJPARepository의 기능을 테스트하기 위한 클래스입니다.
 * @DataJpaTest 어노테이션을 사용하여 JPA 관련 컴포넌트만 로드하여 테스트 환경을 최적화합니다.
 */
@DataJpaTest
public class BoardJPARepositoryTest {
//    @Autowired
//    private BoardJPARepository boardJPARepository; // BoardJPARepository 빈을 주입받습니다.
//
//    @Autowired
//    private UserJPARepository userJPARepository; // UserJPARepository 빈을 주입받습니다.
//
//    @Autowired
//    private EntityManager em; // EntityManager를 주입받습니다.
//
//    private User user; // 테스트에 사용할 사용자 객체를 저장할 변수입니다.
//
//
//    /**
//     * 새로운 게시글을 저장하는 테스트입니다.
//     * 게시글이 정상적으로 저장되고, 저장된 게시글의 속성이 올바른지 검증합니다.
//     */
//    @Test
//    @DisplayName("게시글 저장 테스트")
//    public void save_test() {
//        // given: 테스트에 필요한 초기 조건 설정
//        Board board = Board.builder()
//                .title("9999번째게시글")
//                .content("내용3")
//                .user(user) // 기존에 저장된 사용자와 게시글을 연관시킵니다.
//                .build();
//
//        // when: 테스트 대상 메서드 실행
//        Board savedBoard = boardJPARepository.save(board);
//
//        // then: 예상 결과와 실제 결과를 비교하여 검증
//        assertNotNull(savedBoard.getId(), "저장된 게시글은 ID를 가져야 합니다."); // 게시글 ID가 생성되었는지 확인
//        assertEquals("9999번째게시글", savedBoard.getTitle(), "게시글 제목이 일치해야 합니다."); // 게시글 제목이 일치하는지 확인
//        assertEquals("세 번째 게시글  ", savedBoard.getTitle(), "게시글 제목이 일치해야 합니다."); // 게시글 제목이 일치하는지 확인
//    }
}
