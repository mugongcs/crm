package com.javastudy.crm.workbench.dao;

import com.javastudy.crm.workbench.domain.Customer;

public interface CustomerDao {

    int getByName(String company);

    int create(Customer cust);
}
