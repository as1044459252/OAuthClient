package com.circles.bookstore.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    拦截器，防止在未登录情况下操作某些需要登录的操作
 */
public class LoginHandlerInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser==null){
            request.setAttribute("err_msg","请在登录后使用该功能");
            request.getRequestDispatcher("/toLogin").forward(request,response);
            return false;
        }
        return true;
    }
}
