package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.ClueRemark;
import com.javastudy.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    Customer getByName(String company);

    boolean create(Customer cust);

    boolean saveRemark(ClueRemark cr);

    List<String> getCustomerName(String name);
}
