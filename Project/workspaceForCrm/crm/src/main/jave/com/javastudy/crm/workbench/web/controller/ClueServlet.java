package com.javastudy.crm.workbench.web.controller;

import com.javastudy.crm.settings.domain.User;
import com.javastudy.crm.settings.service.UserService;
import com.javastudy.crm.settings.service.impl.UserServiceImpl;
import com.javastudy.crm.utils.DateTimeUtil;
import com.javastudy.crm.utils.PrintJson;
import com.javastudy.crm.utils.ServiceFactory;
import com.javastudy.crm.utils.UUIDUtil;
import com.javastudy.crm.workbench.dao.ContactsActivityRelationDao;
import com.javastudy.crm.workbench.domain.*;
import com.javastudy.crm.workbench.service.*;
import com.javastudy.crm.workbench.service.impl.*;
import com.javastudy.crm.workbench.vo.PaginationVO;
import com.mysql.cj.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索控制器");

        String path = request.getServletPath();
        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/clue/delete.do".equals(path)){
            delete(request,response);
        }else if("/workbench/clue/getUserListAndClue.do".equals(path)){
            getUserListAndClue(request,response);
        }else if("/workbench/clue/update.do".equals(path)){
            update(request,response);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/clue/getRemarkListByCid.do".equals(path)){
            getRemarkListByCid(request,response);
        }else if("/workbench/clue/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if("/workbench/clue/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }else if("/workbench/clue/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if("/workbench/clue/getActivityRelationListByCid.do".equals(path)){
            getActivityRelationListByCid(request,response);
        }else if("/workbench/clue/deleteRelation.do".equals(path)){
            deleteRelation(request,response);
        }else if("/workbench/clue/getActivityByName.do".equals(path)){
            getActivityByName(request,response);
        }else if("/workbench/clue/bundActivity.do".equals(path)){
            bundActivity(request,response);
        }else if("/workbench/clue/getActivityByAName.do".equals(path)){
            getActivityByAName(request,response);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索转换操作");

        String flag = request.getParameter("flag");
        String clueId = request.getParameter("clueId");
        String money = null;
        String name = null;
        String expectedDate = null;
        String stage = null;
        String activityId = null;
        if("tran".equals(flag)){
            money = request.getParameter("money");
            name = request.getParameter("name");
            expectedDate = request.getParameter("expectedDate");
            stage = request.getParameter("stage");
            activityId = request.getParameter("activityId");
        }

        //通过线索Id获取线索详细信息
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.getById(clueId);

        //查询是否存在线索对应的客户信息
        CustomerService custs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        Customer customer = custs.getByName(c.getCompany());
        String customerId = null;
        if(customer != null){
            customerId = customer.getId();
            System.out.println("线索对应客户已存在");
        }else{
            customerId = UUIDUtil.getUUID();
            String owner = c.getOwner();
            name = c.getCompany();
            String createBy = ((User) request.getSession().getAttribute("user")).getName();
            String createTime = DateTimeUtil.getSysTime();

            Customer cust = new Customer();
            cust.setId(customerId);
            cust.setOwner(owner);
            cust.setName(name);
            cust.setCreateBy(createBy);
            cust.setCreateTime(createTime);
            customer = cust;

            custs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
            boolean result = custs.create(cust);
            if(result){
                System.out.println("通过线索创建客户信息成功");
            }else{
                System.out.println("通过线索创建客户信息失败");
            }
        }

        //保存联系人
        Contacts cont = new Contacts();
        String contactsId = UUIDUtil.getUUID();
        cont.setId(UUIDUtil.getUUID());
        cont.setOwner(c.getOwner());
        cont.setSource(c.getSource());
        cont.setCustomerId(customer.getId());
        cont.setFullname(c.getFullname());
        cont.setAppellation(c.getAppellation());
        cont.setEmail(c.getEmail());
        cont.setMphone(c.getMphone());
        cont.setJob(c.getJob());
        cont.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
        cont.setCreateTime(DateTimeUtil.getSysTime());
        cont.setDescription(c.getDescription());
        cont.setContactSummary(c.getContactSummary());
        cont.setNextContactTime(c.getNextContactTime());
        cont.setAddress(c.getAddress());

        ContactsService conts = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        boolean result = conts.save(cont);
        if(result){
            System.out.println("创建联系人成功");
        }else{
            System.out.println("创建联系人失败");
        }

        //将线索备注转换到客户备注和联系人备注
        cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> crList = cs.getRemarkListByCid(clueId);
        for(ClueRemark cr : crList){
            cr.setId(UUIDUtil.getUUID());
            cr.setClueId(customerId);
            custs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
            result = custs.saveRemark(cr);
            if(!result){
                System.out.println("创建客户备注失败");
            }
            cr.setId(UUIDUtil.getUUID());
            cr.setClueId(contactsId);
            conts = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
            result = conts.saveRemark(cr);
            if(!result){
                System.out.println("创建联系人备注失败");
            }
        }

        //将线索关联的市场活动转换到联系人关联的市场活动
        cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueActivityRelation> carList = cs.getRelationsByCid(clueId);
        for(ClueActivityRelation car : carList){
            ContactsActivityRelation contar = new ContactsActivityRelation();
            contar.setId(UUIDUtil.getUUID());
            contar.setActivityId(car.getActivityId());
            contar.setContactsId(contactsId);
            conts = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
            result = conts.bund(contar);
            if(!result){
                System.out.println("联系人关联市场活动失败");
            }
        }

        //创建交易相关内容
        if("tran".equals(flag)){
            //创建交易
            Tran t = new Tran();
            String tranId = UUIDUtil.getUUID();
            t.setId(tranId);
            t.setOwner(c.getOwner());
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setCustomerId(customerId);
            t.setStage(stage);
            t.setSource(c.getSource());
            t.setActivityId(activityId);
            t.setContactsId(contactsId);
            t.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
            t.setCreateTime(DateTimeUtil.getSysTime());
            t.setDescription(c.getDescription());
            t.setContactSummary(c.getContactSummary());
            t.setNextContactTime(c.getNextContactTime());

            TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
            result = ts.save(t);
            if(!result){
                System.out.println("创建交易失败");
            }else{
                //创建交易历史

                TranHistory tranH = new TranHistory();
                tranH.setId(UUIDUtil.getUUID());
                tranH.setStage(stage);
                tranH.setMoney(money);
                tranH.setExpectedDate(expectedDate);
                tranH.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
                tranH.setCreateTime(DateTimeUtil.getSysTime());
                tranH.setTranId(tranId);

                ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
                result = ts.saveHistory(tranH);
                if(!result){
                    System.out.println("创建交易历史失败");
                }
            }

        }
        //删除线索以及相关的信息
        cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String[] ids = {clueId};
        result = cs.delete(ids);
        if(result){
            System.out.println("线索转换成功");
        }else{
            System.out.println("线索转换失败");
        }

    }

    private void getActivityByAName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("通过名称获取所有的市场活动信息");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityByAName(aname);
        PrintJson.printJsonObj(response, aList);
    }

    private void bundActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入市场活动关联操作");

        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bundActivity(clueId,activityIds);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("搜索与查询名字相关的市场活动列表，除去已关联的市场活动");

        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityByName(map);
        PrintJson.printJsonObj(response, aList);
    }

    private void deleteRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("解除管理操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.deleteRelation(id);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityRelationListByCid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取线索关联市场活动操作");

        String clueId = request.getParameter("clueId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> aList = cs.getActivityRelation(clueId);

        PrintJson.printJsonObj(response, aList);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索备注创建操作");

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String editFlag = "0";
        String clueId = request.getParameter("clueId");

        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setCreateBy(createBy);
        cr.setCreateTime(createTime);
        cr.setEditFlag(editFlag);
        cr.setClueId(clueId);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Map<String, Object> map = cs.saveRemark(cr);
        PrintJson.printJsonObj(response, map);

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索备注修改操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String editFlag = "1";

        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);
        cr.setEditFlag(editFlag);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Map<String, Object> map = cs.updateRemark(cr);
        PrintJson.printJsonObj(response, map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索备注删除操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.deleteRemark(id);
        PrintJson.printJsonFlag(response, flag);

    }

    private void getRemarkListByCid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取线索备注列表操作");

        String clueId = request.getParameter("clueId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> crList = cs.getRemarkListByCid(clueId);
        PrintJson.printJsonObj(response, crList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入线索详细页跳转操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);
        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索修改操作");

        //获取传递参数
        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setEditBy(editBy);
        c.setEditTime(editTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.update(c);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取用户信息列表和单例线索操作");

        String id = request.getParameter("id");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.getById(id);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList", uList);
        map.put("c", c);
        PrintJson.printJsonObj(response, map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索删除操作");

        String[] ids = request.getParameterValues("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.delete(ids);
        PrintJson.printJsonFlag(response, flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索分页操作");

        Integer pageNo = Integer.valueOf(request.getParameter("pageNo"));
        Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));
        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");

        int skipCount = (pageNo - 1)*pageSize;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);
        map.put("fullname", fullname);
        map.put("owner", owner);
        map.put("company", company);
        map.put("phone", phone);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("source", source);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Clue> clueVO = cs.pageList(map);
        PrintJson.printJsonObj(response, clueVO);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.save(clue);
        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = us.getUserList();

        PrintJson.printJsonObj(response, userList);
    }
}
