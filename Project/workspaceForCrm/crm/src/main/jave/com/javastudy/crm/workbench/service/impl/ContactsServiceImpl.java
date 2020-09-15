package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.workbench.dao.ContactsDao;
import com.javastudy.crm.workbench.service.ContactsService;

public class ContactsServiceImpl  implements ContactsService {
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);


}
