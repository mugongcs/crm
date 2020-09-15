package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.workbench.dao.CustomerDao;
import com.javastudy.crm.workbench.dao.CustomerRemarkDao;
import com.javastudy.crm.workbench.domain.ClueRemark;
import com.javastudy.crm.workbench.domain.Customer;
import com.javastudy.crm.workbench.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    @Override
    public Customer getByName(String company) {
        Customer customer = null;
        try {
            customer = customerDao.getByName(company);
        }catch (Exception e) {
            customer = null;
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public boolean create(Customer cust) {
        boolean flag = true;

        int count = customerDao.create(cust);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ClueRemark cr) {
        boolean flag = true;

        int count = customerRemarkDao.saveRemark(cr);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}
