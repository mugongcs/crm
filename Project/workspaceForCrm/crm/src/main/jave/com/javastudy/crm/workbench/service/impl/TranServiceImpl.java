package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.utils.UUIDUtil;
import com.javastudy.crm.workbench.dao.CustomerDao;
import com.javastudy.crm.workbench.dao.TranDao;
import com.javastudy.crm.workbench.dao.TranHistoryDao;
import com.javastudy.crm.workbench.dao.TranRemarkDao;
import com.javastudy.crm.workbench.domain.Customer;
import com.javastudy.crm.workbench.domain.Tran;
import com.javastudy.crm.workbench.domain.TranHistory;
import com.javastudy.crm.workbench.service.TranService;

import java.util.List;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private TranRemarkDao tranRemarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t) {
        boolean flag = true;

        int count = tranDao.save(t);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveHistory(TranHistory th) {
        boolean flag = true;

        int count = tranHistoryDao.saveHistory(th);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveByBtn(Tran t, String customerName) {
        boolean flag = true;
        //根据客户名称在客户表进行精确查询
        //如果存在客户则封装在t里面，否则新建一条客户信息
        Customer customer = customerDao.getByName(customerName);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(t.getCreateTime());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            //添加客户
            int count1 = customerDao.create(customer);
            if(count1 != 1){
                flag = false;
            }
        }

        //添加交易
        t.setCustomerId(customer.getId());
        int count2 = tranDao.save(t);
        if(count2 != 1){
            flag = false;
        }
        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(t.getCreateTime());
        int count3 = tranHistoryDao.saveHistory(th);
        if(count3 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        System.out.println(t);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;

        //改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1 != 1){
            flag = false;
        }

        //生成交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(t.getEditTime());

        int count2 = tranHistoryDao.saveHistory(th);
        if(count2 != 1){
            flag = false;
        }
        return flag;
    }
}
