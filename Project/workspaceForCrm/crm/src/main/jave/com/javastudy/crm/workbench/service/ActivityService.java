package com.javastudy.crm.workbench.service;

import com.javastudy.crm.workbench.domain.Activity;
import com.javastudy.crm.workbench.domain.ActivityRemark;
import com.javastudy.crm.workbench.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityByName(Map<String, String> map);
}
