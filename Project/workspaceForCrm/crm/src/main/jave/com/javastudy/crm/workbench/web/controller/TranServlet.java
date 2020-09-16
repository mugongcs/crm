package com.javastudy.crm.workbench.web.controller;

import com.javastudy.crm.settings.domain.User;
import com.javastudy.crm.settings.service.UserService;
import com.javastudy.crm.settings.service.impl.UserServiceImpl;
import com.javastudy.crm.utils.PrintJson;
import com.javastudy.crm.utils.ServiceFactory;
import com.javastudy.crm.workbench.domain.Activity;
import com.javastudy.crm.workbench.domain.Contacts;
import com.javastudy.crm.workbench.service.ActivityService;
import com.javastudy.crm.workbench.service.ContactsService;
import com.javastudy.crm.workbench.service.CustomerService;
import com.javastudy.crm.workbench.service.impl.ActivityServiceImpl;
import com.javastudy.crm.workbench.service.impl.ContactsServiceImpl;
import com.javastudy.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TranServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入交易活动控制器");

        String path = request.getServletPath();

        if("/workbench/transaction/create.do".equals(path)){
            create(request,response);
        }else if("/workbench/transaction/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if("/workbench/transaction/getContactsListByName.do".equals(path)){
            getContactsListByName(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取客户名称列表操作");

        String name = request.getParameter("name");
        CustomerService custs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> sList = custs.getCustomerName(name);
        PrintJson.printJsonObj(response, sList);
    }

    private void getContactsListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过联系人名获取模糊查询操作");

        String fullname = request.getParameter("fullname");
        ContactsService conts = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        List<Contacts> contList = conts.getContactsListByName(fullname);
        PrintJson.printJsonObj(response, contList);
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过市场活动名获取模糊查询操作");

        String aname = request.getParameter("aname");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityByAName(aname);

        PrintJson.printJsonObj(response, aList);
    }

    private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入跳转到交易添加操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}
