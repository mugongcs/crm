package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.workbench.dao.CustomerDao;
import com.javastudy.crm.workbench.dao.CustomerRemarkDao;
import com.javastudy.crm.workbench.domain.Customer;
import com.javastudy.crm.workbench.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    @Override
    public boolean getByName(String company) {
        boolean flag = true;
        int count = customerDao.getByName(company);
        if(count != 1){
            flag = false;
        }
        return flag;
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
}
