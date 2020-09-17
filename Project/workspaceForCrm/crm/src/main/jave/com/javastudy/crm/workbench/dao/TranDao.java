package com.javastudy.crm.workbench.dao;

import com.javastudy.crm.workbench.domain.Tran;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);
}
