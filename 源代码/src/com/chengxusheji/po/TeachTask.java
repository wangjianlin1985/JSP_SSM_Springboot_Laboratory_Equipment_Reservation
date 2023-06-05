package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class TeachTask {
    /*任务id*/
    private Integer taskId;
    public Integer getTaskId(){
        return taskId;
    }
    public void setTaskId(Integer taskId){
        this.taskId = taskId;
    }

    /*任务标题*/
    @NotEmpty(message="任务标题不能为空")
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    /*任务内容*/
    @NotEmpty(message="任务内容不能为空")
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    /*发布的老师*/
    private Teacher teacherObj;
    public Teacher getTeacherObj() {
        return teacherObj;
    }
    public void setTeacherObj(Teacher teacherObj) {
        this.teacherObj = teacherObj;
    }

    /*发布时间*/
    @NotEmpty(message="发布时间不能为空")
    private String addTime;
    public String getAddTime() {
        return addTime;
    }
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonTeachTask=new JSONObject(); 
		jsonTeachTask.accumulate("taskId", this.getTaskId());
		jsonTeachTask.accumulate("title", this.getTitle());
		jsonTeachTask.accumulate("content", this.getContent());
		jsonTeachTask.accumulate("teacherObj", this.getTeacherObj().getTeacherName());
		jsonTeachTask.accumulate("teacherObjPri", this.getTeacherObj().getTeacherNo());
		jsonTeachTask.accumulate("addTime", this.getAddTime().length()>19?this.getAddTime().substring(0,19):this.getAddTime());
		return jsonTeachTask;
    }}