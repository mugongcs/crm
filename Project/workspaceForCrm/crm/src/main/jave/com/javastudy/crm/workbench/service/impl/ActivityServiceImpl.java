package com.javastudy.crm.workbench.service.impl;

import com.javastudy.crm.settings.dao.UserDao;
import com.javastudy.crm.settings.domain.User;
import com.javastudy.crm.utils.SqlSessionUtil;
import com.javastudy.crm.workbench.dao.ActivityDao;
import com.javastudy.crm.workbench.dao.ActivityRemarkDao;
import com.javastudy.crm.workbench.domain.Activity;
import com.javastudy.crm.workbench.domain.ActivityRemark;
import com.javastudy.crm.workbench.service.ActivityService;
import com.javastudy.crm.workbench.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        PaginationVO<Activity> activityVO = new PaginationVO<Activity>();
        activityVO.setTotal(total);
        activityVO.setDataList(dataList);
        return activityVO;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag =true;
        //查询出需要删除的备注的数量
        int remarkCount = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回受到影响的数目
        int remarkChanges = activityRemarkDao.deletByAids(ids);
        if(remarkChanges != remarkCount){
            flag = false;
        }
        //删除市场活动
        int count = activityDao.delete(ids);
        if(count != ids.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        Map<String,Object> map = new HashMap<String,Object>();

        List<User> uList = userDao.getUserList();
        Activity a = activityDao.getById(id);

        map.put("uList", uList);
        map.put("a", a);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;

        int count = activityDao.update(activity);
        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;

        int count = activityRemarkDao.deleteById(id);
        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);
        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);
        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityByName(Map<String, String> map) {
        List<Activity> aList = activityDao.getActivityByName(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityByAName(String aname) {
        List<Activity> aList = activityDao.getActivityByAName(aname);
        return aList;
    }
}
