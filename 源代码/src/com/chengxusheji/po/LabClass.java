package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class LabClass {
    /*类别编号*/
    private Integer classId;
    public Integer getClassId(){
        return classId;
    }
    public void setClassId(Integer classId){
        this.classId = classId;
    }

    /*类别名称*/
    @NotEmpty(message="类别名称不能为空")
    private String className;
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonLabClass=new JSONObject(); 
		jsonLabClass.accumulate("classId", this.getClassId());
		jsonLabClass.accumulate("className", this.getClassName());
		return jsonLabClass;
    }}