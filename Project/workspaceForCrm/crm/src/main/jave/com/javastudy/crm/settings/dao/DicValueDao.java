package com.javastudy.crm.settings.dao;

import com.javastudy.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getListByCode(String code);
}
