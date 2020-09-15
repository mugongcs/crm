package com.javastudy.crm.workbench.domain;

public class TranHistory {
    /*
        id;
        stage;
        money;
        expectedDate;
        createTime;
        createBy;
        tranId;
     */
    private String id;          //主键
    private String stage;       //阶段
    private String money;       //金额
    private String expectedDate;//预计成交日期
    private String createTime;  //创建时间
    private String createBy;    //创建人
    private String tranId;      //交易ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }
}
