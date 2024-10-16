package com.tenco.blog_jpa_step4.commom;

import com.tenco.blog_jpa_step4.commom.errors.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 400 Bad Request 예외 처리
     */
    @ExceptionHandler(Exception400.class)
    public ModelAndView handleException400(Exception400 ex, Model model) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }


    /**
     * 401 Unauthorized 예외 처리
     */
    @ExceptionHandler(Exception401.class)
    public ModelAndView handleException401(Exception401 ex) {
        return new ModelAndView("redirect:/login-form");
    }

    /**
     * 403 Forbidden 예외 처리
     */
    @ExceptionHandler(Exception403.class)
    public ModelAndView handleException403(Exception403 ex, Model model) {
        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }

    /**
     * 404 Not Found 예외 처리
     */
    @ExceptionHandler(Exception404.class)
    public ModelAndView handleException404(Exception404 ex, Model model) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }

    /**
     * 500 Internal Server Error 예외 처리
     */
    @ExceptionHandler(Exception500.class)
    public ModelAndView handleException500(Exception500 ex, Model model) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }

    /**
     * 모든 예외를 처리하여 500 에러 페이지로 이동
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("msg", ex.getMessage());
        return mav;
    }
}
