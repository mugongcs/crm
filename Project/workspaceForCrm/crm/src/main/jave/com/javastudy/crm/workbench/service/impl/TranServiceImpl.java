package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.workbench.dao.TranDao;
import com.javastudy.crm.workbench.dao.TranHistoryDao;
import com.javastudy.crm.workbench.dao.TranRemarkDao;
import com.javastudy.crm.workbench.domain.Tran;
import com.javastudy.crm.workbench.domain.TranHistory;
import com.javastudy.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private TranRemarkDao tranRemarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);

    @Override
    public boolean save(Tran t) {
        boolean flag = true;

        int count = tranDao.save(t);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveHistory(TranHistory th) {
        boolean flag = true;

        int count = tranHistoryDao.saveHistory(th);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}
