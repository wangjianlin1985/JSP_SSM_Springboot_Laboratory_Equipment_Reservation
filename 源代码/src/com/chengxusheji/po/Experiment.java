package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Experiment {
    /*实验项目id*/
    private Integer experimentId;
    public Integer getExperimentId(){
        return experimentId;
    }
    public void setExperimentId(Integer experimentId){
        this.experimentId = experimentId;
    }

    /*实验项目名称*/
    @NotEmpty(message="实验项目名称不能为空")
    private String experimentName;
    public String getExperimentName() {
        return experimentName;
    }
    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    /*上课班级*/
    private ClassInfo classObj;
    public ClassInfo getClassObj() {
        return classObj;
    }
    public void setClassObj(ClassInfo classObj) {
        this.classObj = classObj;
    }

    /*上课实验室*/
    private LabInfo labObj;
    public LabInfo getLabObj() {
        return labObj;
    }
    public void setLabObj(LabInfo labObj) {
        this.labObj = labObj;
    }

    /*实验内容*/
    @NotEmpty(message="实验内容不能为空")
    private String experimentContent;
    public String getExperimentContent() {
        return experimentContent;
    }
    public void setExperimentContent(String experimentContent) {
        this.experimentContent = experimentContent;
    }

    /*实验日期*/
    @NotEmpty(message="实验日期不能为空")
    private String experimentDate;
    public String getExperimentDate() {
        return experimentDate;
    }
    public void setExperimentDate(String experimentDate) {
        this.experimentDate = experimentDate;
    }

    /*实验时间*/
    @NotEmpty(message="实验时间不能为空")
    private String experimentTime;
    public String getExperimentTime() {
        return experimentTime;
    }
    public void setExperimentTime(String experimentTime) {
        this.experimentTime = experimentTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonExperiment=new JSONObject(); 
		jsonExperiment.accumulate("experimentId", this.getExperimentId());
		jsonExperiment.accumulate("experimentName", this.getExperimentName());
		jsonExperiment.accumulate("classObj", this.getClassObj().getClassName());
		jsonExperiment.accumulate("classObjPri", this.getClassObj().getClassNo());
		jsonExperiment.accumulate("labObj", this.getLabObj().getLabName());
		jsonExperiment.accumulate("labObjPri", this.getLabObj().getLabNumber());
		jsonExperiment.accumulate("experimentContent", this.getExperimentContent());
		jsonExperiment.accumulate("experimentDate", this.getExperimentDate().length()>19?this.getExperimentDate().substring(0,19):this.getExperimentDate());
		jsonExperiment.accumulate("experimentTime", this.getExperimentTime());
		return jsonExperiment;
    }}