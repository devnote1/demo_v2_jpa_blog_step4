package com.tenco.blog_jpa_step4.commom.utils;

// static 메서드로 유틸리티 클래스를 사용하는 것은
// 스프링 부트에서 문제가 되지 않습니다.
// 다만, static 사용 시 주의점
// static 메서드는 상태를 저장하지 않는 메서드여야 합니다.
// 상태를 저장하거나 관리해야 하는 경우에는 @Component
// 같은 스프링 빈으로 관리하는 것이 더 좋습니다.
public class Define {
    public static String AUTHORIZATION = "Authorization";
    // 주의! 공백 반드시 포함
    public static String BEARER = "Bearer ";
    public static String SESSION_USER = "sessionUser";
}
