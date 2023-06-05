package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceLend {
    /*设备借用id*/
    private Integer deviceLendId;
    public Integer getDeviceLendId(){
        return deviceLendId;
    }
    public void setDeviceLendId(Integer deviceLendId){
        this.deviceLendId = deviceLendId;
    }

    /*借用设备*/
    private Device deviceObj;
    public Device getDeviceObj() {
        return deviceObj;
    }
    public void setDeviceObj(Device deviceObj) {
        this.deviceObj = deviceObj;
    }

    /*借用的老师*/
    private Teacher teacherObj;
    public Teacher getTeacherObj() {
        return teacherObj;
    }
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }

    /*借用用途*/
    @NotEmpty(message="借用用途不能为空")
    private String lendUse;
    public String getLendUse() {
        return lendUse;
    }
    public void setLendUse(String lendUse) {
        this.lendUse = lendUse;
    }

    /*借用时间*/
    @NotEmpty(message="借用时间不能为空")
    private String lendTime;
    public String getLendTime() {
        return lendTime;
    }
    public void setLendTime(String lendTime) {
        this.lendTime = lendTime;
    }

    /*归还时间*/
    private String returnTime;
    public String getReturnTime() {
        return returnTime;
    }
    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonDeviceLend=new JSONObject(); 
		jsonDeviceLend.accumulate("deviceLendId", this.getDeviceLendId());
		jsonDeviceLend.accumulate("deviceObj", this.getDeviceObj().getDeviceName());
		jsonDeviceLend.accumulate("deviceObjPri", this.getDeviceObj().getDeviceNo());
		jsonDeviceLend.accumulate("teacherObj", this.getTeacherObj().getTeacherName());
		jsonDeviceLend.accumulate("teacherObjPri", this.getTeacherObj().getTeacherNo());
		jsonDeviceLend.accumulate("lendUse", this.getLendUse());
		jsonDeviceLend.accumulate("lendTime", this.getLendTime().length()>19?this.getLendTime().substring(0,19):this.getLendTime());
		jsonDeviceLend.accumulate("returnTime", this.getReturnTime().length()>19?this.getReturnTime().substring(0,19):this.getReturnTime());
		return jsonDeviceLend;
    }}