package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.ClueRemark;
import com.javastudy.crm.workbench.domain.Contacts;
import com.javastudy.crm.workbench.domain.ContactsActivityRelation;

public interface ContactsService {
    boolean save(Contacts cont);

    boolean saveRemark(ClueRemark cr);

    boolean bund(ContactsActivityRelation contar);
}
