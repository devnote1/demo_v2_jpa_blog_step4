package com.tenco.blog_jpa_step4.commom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인 인터셉터 적용
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")  // 모든 API 요청에 대해 인터셉터 적용
                .excludePathPatterns("/api/boards/{id:\\d+}/detail"); // 특정 경로는 제외
        // 인터셉터 적용에서 제외할 URL 패턴을 지정
        // /board/ 뒤에 숫자로 이루어진 id를 갖는 URL을 의미 한다.
        // ex) /board/1, /board/42
        // \d+는 숫자 하나 이상을 의미하는 정규표현식 패턴
        // 백슬래시(\)는 이스케이프 문자



        // 관리자 인터셉터 적용
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**"); // /admin/** 경로에만 관리자 인터셉터 적용}
    }
}