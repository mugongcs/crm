package com.javastudy.crm.workbench.web.controller;

import com.javastudy.crm.settings.domain.User;
import com.javastudy.crm.settings.service.UserService;
import com.javastudy.crm.settings.service.impl.UserServiceImpl;
import com.javastudy.crm.utils.DateTimeUtil;
import com.javastudy.crm.utils.PrintJson;
import com.javastudy.crm.utils.ServiceFactory;
import com.javastudy.crm.utils.UUIDUtil;
import com.javastudy.crm.workbench.domain.Activity;
import com.javastudy.crm.workbench.domain.Contacts;
import com.javastudy.crm.workbench.domain.Tran;
import com.javastudy.crm.workbench.domain.TranHistory;
import com.javastudy.crm.workbench.service.ActivityService;
import com.javastudy.crm.workbench.service.ContactsService;
import com.javastudy.crm.workbench.service.CustomerService;
import com.javastudy.crm.workbench.service.TranService;
import com.javastudy.crm.workbench.service.impl.ActivityServiceImpl;
import com.javastudy.crm.workbench.service.impl.ContactsServiceImpl;
import com.javastudy.crm.workbench.service.impl.CustomerServiceImpl;
import com.javastudy.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        }else if("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)){
            getHistoryListByTranId(request,response);
        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("修改阶段操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);

        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flag", flag);
        map.put("t", t);
        PrintJson.printJsonObj(response, map);
    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取历史交易列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList = ts.getHistoryListByTranId(tranId);
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        for(TranHistory th : thList){
            th.setPossibility(pMap.get(th.getStage()));
        }

        PrintJson.printJsonObj(response, thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t = ts.detail(id);

        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("创建交易操作");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.saveByBtn(t,customerName);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
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
