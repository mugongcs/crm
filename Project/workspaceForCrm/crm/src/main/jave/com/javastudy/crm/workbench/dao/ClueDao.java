package com.javastudy.crm.workbench.dao;


import com.javastudy.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Clue getById(String id);

    int update(Clue c);

    Clue detail(String id);
}
