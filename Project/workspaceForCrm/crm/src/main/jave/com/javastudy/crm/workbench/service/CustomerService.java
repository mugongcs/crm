package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.ClueRemark;
import com.javastudy.crm.workbench.domain.Customer;

public interface CustomerService {
    Customer getByName(String company);

    boolean create(Customer cust);

    boolean saveRemark(ClueRemark cr);
}
