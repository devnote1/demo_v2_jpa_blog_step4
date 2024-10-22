package com.tenco.blog_jpa_step4.commom.config;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tenco.blog_jpa_step4.commom.errors.Exception401;
import com.tenco.blog_jpa_step4.commom.errors.Exception500;
import com.tenco.blog_jpa_step4.commom.utils.Define;
import com.tenco.blog_jpa_step4.commom.utils.JwtUtil;
import com.tenco.blog_jpa_step4.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

// Ioc 하지 않음 상태 임
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 컨트롤러 메서드 호출 전에 실행되는 메서드
     *
     * @return true: 다음 인터셉터나 컨트롤러로 진행
     * false: 요청을 종료
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = request.getHeader(Define.AUTHORIZATION);

        if (jwt == null || !jwt.startsWith(Define.BEARER)) {
            throw new Exception401("JWT 토큰을 전달해주세요");
        }

        jwt = jwt.replace(Define.BEARER, "");

        try {
            User sessionUser = JwtUtil.verify(jwt);  // JWT 검증 후 User 객체 생성
            // 생성된 Request 객체에 키  sessionUser 할당,
            // 값으로 Jwt 에 담겨있는 사용자 정보를 활용해 User 객체 생성 후 활용
            request.setAttribute(Define.SESSION_USER, sessionUser);

            // 검증 성공 후, 추가 작업이 없다면 true 반환
            return true;
        } catch (TokenExpiredException e) {
            throw new Exception401("토큰 만료 시간이 지났습니다. 다시 로그인하세요.");
        } catch (JWTDecodeException e) {
            throw new Exception401("유효하지 않은 토큰입니다.");
        } catch (Exception e) {
            throw new Exception500("서버 오류: " + e.getMessage());
        }
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
