package com.tenco.blog_jpa_step4.commom.config;

import com.tenco.blog_jpa_step4.commom.errors.Exception401;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

// Ioc 하지 않음 상태 임 
public class LoginInterceptor  implements HandlerInterceptor {
    /**
     * 컨트롤러 메서드 호출 전에 실행되는 메서드
     *
     * @return true: 다음 인터셉터나 컨트롤러로 진행
     * false: 요청을 종료
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("LoginInterceptor preHandle 실행");
        HttpSession session = request.getSession(false); // 기존 세션이 없으면 null 반환

        if (session == null) {
            throw new Exception401("로그인이 필요합니다.");
        }

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            throw new Exception401("로그인이 필요합니다.");
        }

        // 로그인 상태인 경우 계속 진행
        return true;
    }

    /**
     * 컨트롤러 메서드 실행 후, 뷰가 렌더링되기 전에 실행되는 메서드
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           org.springframework.web.servlet.ModelAndView modelAndView) throws Exception {
        System.out.println("LoginInterceptor postHandle 실행");
    }

    /**
     * 뷰가 렌더링된 후 실행되는 메서드
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("LoginInterceptor afterCompletion 실행");
    }

}
