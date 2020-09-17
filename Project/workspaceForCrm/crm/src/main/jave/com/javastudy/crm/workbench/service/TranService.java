package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.Tran;
import com.javastudy.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranService {
    boolean save(Tran t);

    boolean saveHistory(TranHistory th);

    boolean saveByBtn(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran t);
}
