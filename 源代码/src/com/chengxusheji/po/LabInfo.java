package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class LabInfo {
    /*实验室编号*/
    @NotEmpty(message="实验室编号不能为空")
    private String labNumber;
    public String getLabNumber(){
        return labNumber;
    }
    public void setLabNumber(String labNumber){
        this.labNumber = labNumber;
    }

    /*实验室名称*/
    @NotEmpty(message="实验室名称不能为空")
    private String labName;
    public String getLabName() {
        return labName;
    }
    public void setLabName(String labName) {
        this.labName = labName;
    }

    /*实验室类别*/
    private LabClass labClassObj;
    public LabClass getLabClassObj() {
        return labClassObj;
    }
    public void setLabClassObj(LabClass labClassObj) {
        this.labClassObj = labClassObj;
    }

    /*实验室面积*/
    @NotNull(message="必须输入实验室面积")
    private Float labArea;
    public Float getLabArea() {
        return labArea;
    }
    public void setLabArea(Float labArea) {
        this.labArea = labArea;
    }

    /*实验室图片*/
    private String labPhoto;
    public String getLabPhoto() {
        return labPhoto;
    }
    public void setLabPhoto(String labPhoto) {
        this.labPhoto = labPhoto;
    }

    /*实验室地址*/
    @NotEmpty(message="实验室地址不能为空")
    private String labAddress;
    public String getLabAddress() {
        return labAddress;
    }
    public void setLabAddress(String labAddress) {
        this.labAddress = labAddress;
    }

    /*实验室状态*/
    @NotEmpty(message="实验室状态不能为空")
    private String labState;
    public String getLabState() {
        return labState;
    }
    public void setLabState(String labState) {
        this.labState = labState;
    }

    /*实验室介绍*/
    @NotEmpty(message="实验室介绍不能为空")
    private String labDesc;
    public String getLabDesc() {
        return labDesc;
    }
    public void setLabDesc(String labDesc) {
        this.labDesc = labDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonLabInfo=new JSONObject(); 
		jsonLabInfo.accumulate("labNumber", this.getLabNumber());
		jsonLabInfo.accumulate("labName", this.getLabName());
		jsonLabInfo.accumulate("labClassObj", this.getLabClassObj().getClassName());
		jsonLabInfo.accumulate("labClassObjPri", this.getLabClassObj().getClassId());
		jsonLabInfo.accumulate("labArea", this.getLabArea());
		jsonLabInfo.accumulate("labPhoto", this.getLabPhoto());
		jsonLabInfo.accumulate("labAddress", this.getLabAddress());
		jsonLabInfo.accumulate("labState", this.getLabState());
		jsonLabInfo.accumulate("labDesc", this.getLabDesc());
		return jsonLabInfo;
    }}