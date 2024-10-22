package com.tenco.blog_jpa_step4.commom.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tenco.blog_jpa_step4.user.User;

import java.util.Date;

/**
 * JwtUtil 클래스는 JWT 토큰의 생성 및 검증을 위한 유틸리티 클래스입니다.
 * 여기서는 HMAC512 알고리즘을 사용하여 JWT를 생성하고 검증합니다.
 */
public class JwtUtil {

    /**
     * 주어진 사용자 정보(User)로 JWT 토큰을 생성합니다.
     * @param user 토큰에 포함할 사용자 정보
     * @return 생성된 JWT 토큰 문자열
     */
    // JWT 생성하기 - String 값
    //
    public static String create(User user) {
       return JWT.create()
                .withSubject("blog") // 토큰 주제 설정
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                // 페이로드 포함된 데이터 조각 - 클레임(사용자, id 와 사용자 이름을 포함)
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
               // 서명 HMAC512 알고리즘을 사용
                .sign(Algorithm.HMAC512("tencoding"));
    }

    /**
     * 전달된 JWT 토큰을 검증하고, 그 안에 포함된 사용자 정보를 반환합니다.
     * @param jwt 검증할 JWT 토큰
     * @return SessionUser 객체 (JWT에서 파싱한 사용자 정보)
     */
    // Jwt 검증하기
    public static User verify(String jwt) {
        // JWT 디코딩
        DecodedJWT decodedJWT
                // 동일한 비밀키로 서명 검증
                = JWT.require(Algorithm.HMAC512("tencoding"))
                .build().verify(jwt);

        // 검증된 JWT 에서 사용자 ID와 이름을 추출
        int id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getClaim("username").asString();

        return User
                .builder()
                .id(id)
                .username(username)
                .build();
    }
}
