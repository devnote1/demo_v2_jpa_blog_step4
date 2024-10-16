package com.tenco.blog_jpa_step4.user;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * 패키지명이 동일해야 한다!
 * UserJPARepositoryTest는 UserJPARepository의 기능을 테스트하기 위한 클래스입니다.
 *
 * @DataJpaTest 어노테이션을 사용하여 JPA 관련 컴포넌트만 로드하여 테스트 환경을 최적화합니다.
 */
@DataJpaTest
public class UserJPARepositoryTest {

    // UserJPARepository 빈을 주입받습니다.
//    @Autowired
//    private UserJPARepository userJPARepository;
//
//    /**
//     * @BeforeEach 각 테스트 전에 실행되며,
//     * 기존 데이터를 삭제하고 테스트용 데이터를 초기화합니다.
//     */
//    @BeforeEach
//    public void setUp() {
//        System.out.println("@Test 시 동작");
//    }
//
//    @Test
//    @DisplayName("사용자 이름으로 사용자 조회 테스트")
//    public void findByUsername_test() {
//        // given: 테스트에 필요한 초기 조건 설정
//        String username = "카리나";
//
//        // when: 테스트 대상 메서드 실행
//        Optional<User> userOpt = userJPARepository.findByUsername(username);
//
//        // then: 예상 결과와 실제 결과를 비교하여 검증
//        assertTrue(userOpt.isPresent(), "사용자가 존재해야 합니다."); // 사용자가 존재하는지 확인
//        User user = userOpt.get();
//        assertEquals(username, user.getUsername(), "사용자 이름이 일치해야 합니다."); // 사용자 이름이 일치하는지 확인
//    }
//
//    /**
//     * 사용자 이름과 비밀번호로 사용자를 조회하는 테스트입니다.
//     * 정상적으로 사용자가 조회되는지 검증합니다.
//     */
//    @Test
//    @DisplayName("사용자 이름과 비밀번호로 사용자 조회 테스트")
//    public void findByUsernameAndPassword_test() {
//        // given: 테스트에 필요한 초기 조건 설정
//        String username = "길동";
//        String password = "1234";
//
//        // when: 테스트 대상 메서드 실행
//        Optional<User> userOpt = userJPARepository.findByUsernameAndPassword(username, password);
//
//        // eye
//        System.out.println(userOpt.get().getUsername());
//
//        // then: 예상 결과와 실제 결과를 비교하여 검증
//        // 값이 있으면 true를 반환하고, 그렇지 않으면 false를 반환합니다
//        assertTrue(userOpt.isPresent(), "사용자가 존재해야 합니다.");
//        User user = userOpt.get();
//        assertEquals(username, user.getUsername(), "사용자 이름이 일치해야 합니다."); // 사용자 이름이 일치하는지 확인
//        assertEquals(password, user.getPassword(), "비밀번호가 일치해야 합니다."); // 비밀번호가 일치하는지 확인
//    }

}