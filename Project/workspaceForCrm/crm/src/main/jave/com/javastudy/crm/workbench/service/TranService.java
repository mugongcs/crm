package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.Tran;
import com.javastudy.crm.workbench.domain.TranHistory;

public interface TranService {
    boolean save(Tran t);

    boolean saveHistory(TranHistory th);
}
