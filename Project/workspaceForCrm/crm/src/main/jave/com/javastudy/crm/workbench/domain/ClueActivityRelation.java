package com.javastudy.crm.workbench.domain;

public class ClueActivityRelation {
    private String id;          //主键
    private String clueId;      //线索ID
    private String activityId;  //市场活动ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
