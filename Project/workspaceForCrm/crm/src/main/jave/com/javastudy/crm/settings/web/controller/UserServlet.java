package com.javastudy.crm.settings.web.controller;

import com.javastudy.crm.settings.domain.User;
import com.javastudy.crm.settings.service.UserService;
import com.javastudy.crm.settings.service.impl.UserServiceImpl;
import com.javastudy.crm.utils.MD5Util;
import com.javastudy.crm.utils.PrintJson;
import com.javastudy.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");

        String path = request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if ("/settings/user/save.do".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到后台登录验证环节");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文装换成密文
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的IP地址
        String ip = request.getRemoteAddr();
        System.out.println("=========="+ip);

        //使用代理类接口对象
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            User user = userService.login(loginAct,loginPwd,ip);
            //程序顺序执行，表示登录成功，负责抛出异常，转到捕捉异常，不会后续执行
            request.getSession().setAttribute("user", user);
            PrintJson.printJsonFlag(response, true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("msg", msg);
            map.put("flag", false);
            PrintJson.printJsonObj(response, map);
        }

    }
}
