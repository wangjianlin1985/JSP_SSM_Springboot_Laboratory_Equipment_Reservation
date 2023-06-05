package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class LabOrder {
    /*预约编号*/
    private Integer orderId;
    public Integer getOrderId(){
        return orderId;
    }
    public void setOrderId(Integer orderId){
        this.orderId = orderId;
    }

    /*预约实验室*/
    private LabInfo labObj;
    public LabInfo getLabObj() {
        return labObj;
    }
    public void setLabObj(LabInfo labObj) {
        this.labObj = labObj;
    }

    /*预约的老师*/
    private Teacher teacherObj;
    public Teacher getTeacherObj() {
        return teacherObj;
    }
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }

    /*预约日期*/
    @NotEmpty(message="预约日期不能为空")
    private String orderDate;
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /*预约时间*/
    @NotEmpty(message="预约时间不能为空")
    private String orderTime;
    public String getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /*预约用途*/
    @NotEmpty(message="预约用途不能为空")
    private String purpose;
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /*审核状态*/
    @NotEmpty(message="审核状态不能为空")
    private String shenHeState;
    public String getShenHeState() {
        return shenHeState;
    }
    public void setShenHeState(String shenHeState) {
        this.shenHeState = shenHeState;
    }

    /*审核回复*/
    private String reply;
    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonLabOrder=new JSONObject(); 
		jsonLabOrder.accumulate("orderId", this.getOrderId());
		jsonLabOrder.accumulate("labObj", this.getLabObj().getLabName());
		jsonLabOrder.accumulate("labObjPri", this.getLabObj().getLabNumber());
		jsonLabOrder.accumulate("teacherObj", this.getTeacherObj().getTeacherName());
		jsonLabOrder.accumulate("teacherObjPri", this.getTeacherObj().getTeacherNo());
		jsonLabOrder.accumulate("orderDate", this.getOrderDate().length()>19?this.getOrderDate().substring(0,19):this.getOrderDate());
		jsonLabOrder.accumulate("orderTime", this.getOrderTime());
		jsonLabOrder.accumulate("purpose", this.getPurpose());
		jsonLabOrder.accumulate("shenHeState", this.getShenHeState());
		jsonLabOrder.accumulate("reply", this.getReply());
		return jsonLabOrder;
    }}