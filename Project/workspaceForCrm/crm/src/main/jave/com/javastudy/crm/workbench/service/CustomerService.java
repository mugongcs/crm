package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.Customer;

public interface CustomerService {
    boolean getByName(String company);

    boolean create(Customer cust);
}
