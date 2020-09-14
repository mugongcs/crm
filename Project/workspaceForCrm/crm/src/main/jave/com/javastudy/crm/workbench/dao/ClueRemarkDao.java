package com.javastudy.crm.workbench.dao;

import com.javastudy.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);

    List<ClueRemark> getRemarkListByCid(String clueId);

    int deleteRemark(String id);

    int updateRemark(ClueRemark cr);

    int saveRemark(ClueRemark cr);
}
