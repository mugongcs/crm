package com.javastudy.crm.workbench.dao;

import com.javastudy.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts cont);

    List<Contacts> getContactsListByName(String fullname);
}
