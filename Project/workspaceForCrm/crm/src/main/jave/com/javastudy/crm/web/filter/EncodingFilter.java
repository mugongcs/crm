package com.javastudy.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("字符编码过滤器");
        //过滤post请求中的中文参数乱码
        servletRequest.setCharacterEncoding("UTF-8");
        //过滤响应流中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");
        //放行过滤器
        filterChain.doFilter(servletRequest, servletResponse);
   }
}
