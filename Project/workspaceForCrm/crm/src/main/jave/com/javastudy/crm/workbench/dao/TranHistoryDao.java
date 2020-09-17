package com.javastudy.crm.workbench.dao;

import com.javastudy.crm.workbench.domain.TranHistory;

import java.util.List;


public interface TranHistoryDao {

    int saveHistory(TranHistory ts);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
