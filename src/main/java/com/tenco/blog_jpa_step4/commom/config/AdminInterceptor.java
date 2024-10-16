package com.tenco.blog_jpa_step4.commom.config;

import com.tenco.blog_jpa_step4.commom.errors.Exception403;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("AdminInterceptor preHandle 실행");
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new Exception403("접근이 금지되었습니다.");
        }

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null || sessionUser.getUsername().equals("ADMIN")) {
        //if (sessionUser == null || ! sessionUser.isAdmin()) { // isAdmin() 메서드는 User 클래스에 구현되어 있다고 가정
            throw new Exception403("관리자 권한이 필요합니다.");
        }

        // 관리자 권한이 있는 경우 계속 진행
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("AdminInterceptor postHandle 실행");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("AdminInterceptor afterCompletion 실행");
    }
}
