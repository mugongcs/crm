package com.javastudy.crm.workbench.dao;


import com.javastudy.crm.workbench.domain.Activity;
import com.javastudy.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);

    List<Activity> getActivityRelation(String clueId);

    int deleteRelation(String id);

    int bundActivity(ClueActivityRelation car);
}
