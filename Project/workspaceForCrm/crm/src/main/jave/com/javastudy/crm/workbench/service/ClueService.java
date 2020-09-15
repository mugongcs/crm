package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.Activity;
import com.javastudy.crm.workbench.domain.Clue;
import com.javastudy.crm.workbench.domain.ClueActivityRelation;
import com.javastudy.crm.workbench.domain.ClueRemark;
import com.javastudy.crm.workbench.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Clue getById(String id);

    boolean update(Clue c);

    Clue detail(String id);

    List<ClueRemark> getRemarkListByCid(String clueId);

    boolean deleteRemark(String id);

    Map<String, Object> updateRemark(ClueRemark cr);

    Map<String, Object> saveRemark(ClueRemark cr);

    List<Activity> getActivityRelation(String clueId);

    boolean deleteRelation(String id);

    boolean bundActivity(String clueId, String[] activityIds);

    List<ClueActivityRelation> getRelationsByCid(String clueId);
}
