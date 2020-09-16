package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.workbench.dao.ContactsActivityRelationDao;
import com.javastudy.crm.workbench.dao.ContactsDao;
import com.javastudy.crm.workbench.dao.ContactsRemarkDao;
import com.javastudy.crm.workbench.domain.ClueRemark;
import com.javastudy.crm.workbench.domain.Contacts;
import com.javastudy.crm.workbench.domain.ContactsActivityRelation;
import com.javastudy.crm.workbench.domain.ContactsRemark;
import com.javastudy.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl  implements ContactsService {
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contARDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    @Override
    public boolean save(Contacts cont) {
        boolean flag = true;

        int count = contactsDao.save(cont);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ClueRemark cr) {
        boolean flag = true;

        int count = contactsRemarkDao.saveRemark(cr);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(ContactsActivityRelation contar) {
        boolean flag = true;

        int count = contARDao.bund(contar);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Contacts> getContactsListByName(String fullname) {
        List<Contacts> contList = contactsDao.getContactsListByName(fullname);
        return contList;
    }
}
