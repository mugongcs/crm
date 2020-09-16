package com.javastudy.crm.workbench.dao;

import com.javastudy.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getByName(String company);

    int create(Customer cust);

    List<String> getCustomerName(String name);
}
