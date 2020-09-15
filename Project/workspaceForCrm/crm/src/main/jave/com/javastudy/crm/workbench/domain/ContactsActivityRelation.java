package com.javastudy.crm.workbench.domain;

public class ContactsActivityRelation {
    /*
        id;
        contactsId;
        activityId;
     */

    private String id;          //主键
    private String contactsId;  //联系人ID
    private String activityId;  //市场活动ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactsId() {
        return contactsId;
    }

    public void setContactsId(String contactsId) {
        this.contactsId = contactsId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
