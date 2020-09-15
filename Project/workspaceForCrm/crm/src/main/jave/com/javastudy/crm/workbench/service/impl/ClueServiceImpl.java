package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.settings.dao.UserDao;
import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.utils.UUIDUtil;
import com.javastudy.crm.workbench.dao.ClueActivityRelationDao;
import com.javastudy.crm.workbench.dao.ClueDao;
import com.javastudy.crm.workbench.dao.ClueRemarkDao;
import com.javastudy.crm.workbench.domain.Activity;
import com.javastudy.crm.workbench.domain.Clue;
import com.javastudy.crm.workbench.domain.ClueActivityRelation;
import com.javastudy.crm.workbench.domain.ClueRemark;
import com.javastudy.crm.workbench.service.ClueService;
import com.javastudy.crm.workbench.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao cARDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public boolean save(Clue clue) {
        boolean flag = true;

        int count = clueDao.save(clue);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {
        PaginationVO<Clue> clueVO = new PaginationVO<Clue>();

        int total = clueDao.getTotalByCondition(map);
        List<Clue> dataList = clueDao.getClueListByCondition(map);

        clueVO.setTotal(total);
        clueVO.setDataList(dataList);

        return clueVO;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;

        //删除线索相关联的备注
        int remarkCount = clueRemarkDao.getCountByCids(ids);
        int remarkChanges = clueRemarkDao.deleteByCids(ids);
        if(remarkCount != remarkChanges){
            flag = false;
        }

        //解除与之关联的市场活动
        int activityCount = cARDao.getCountByCids(ids);
        int activityChanges = cARDao.deleteByCids(ids);
        if(activityCount != activityChanges){
            flag = false;
        }

        //删除线索
        int count = clueDao.delete(ids);
        if(count != ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue getById(String id) {

        Clue c = clueDao.getById(id);
        return c;
    }

    @Override
    public boolean update(Clue c) {
        boolean flag = true;

        int count = clueDao.update(c);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    @Override
    public List<ClueRemark> getRemarkListByCid(String clueId) {
        List<ClueRemark> crList = clueRemarkDao.getRemarkListByCid(clueId);
        return crList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;

        int count = clueRemarkDao.deleteRemark(id);
        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> updateRemark(ClueRemark cr) {
        Map<String,Object> map = new HashMap<String, Object>();

        boolean flag = true;
        int count = clueRemarkDao.updateRemark(cr);
        if(count != 1){
            flag = false;
        }

        map.put("flag", flag);
        map.put("cr", cr);
        return map;
    }

    @Override
    public Map<String, Object> saveRemark(ClueRemark cr) {
        Map<String, Object> map = new HashMap<String, Object>();

        boolean flag = true;
        int count = clueRemarkDao.saveRemark(cr);
        if(count != 1){
            flag = false;
        }
        map.put("flag", flag);
        map.put("cr", cr);
        return map;
    }

    @Override
    public List<Activity> getActivityRelation(String clueId) {

        List<Activity> carList = cARDao.getActivityRelation(clueId);

        return carList;
    }

    @Override
    public boolean deleteRelation(String id) {
        boolean flag = true;

        int count = cARDao.deleteRelation(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bundActivity(String clueId, String[] activityIds) {
        boolean flag = true;

        for(String activityId : activityIds){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(activityId);
            System.out.println(activityId.length());

            int count = cARDao.bundActivity(car);
            if(count != 1){
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public List<ClueActivityRelation> getRelationsByCid(String clueId) {
        List<ClueActivityRelation> carList = cARDao.getRelationsByCid(clueId);
        return carList;
    }
}
