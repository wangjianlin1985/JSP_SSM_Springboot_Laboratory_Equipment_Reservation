package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Teacher;
import com.chengxusheji.po.TeachTask;

import com.chengxusheji.mapper.TeachTaskMapper;
@Service
public class TeachTaskService {

	@Resource TeachTaskMapper teachTaskMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加教学任务记录*/
    public void addTeachTask(TeachTask teachTask) throws Exception {
    	teachTaskMapper.addTeachTask(teachTask);
    }

    /*按照查询条件分页查询教学任务记录*/
    public ArrayList<TeachTask> queryTeachTask(String title,Teacher teacherObj,String addTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!title.equals("")) where = where + " and t_teachTask.title like '%" + title + "%'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null  && !teacherObj.getTeacherNo().equals(""))  where += " and t_teachTask.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_teachTask.addTime like '%" + addTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return teachTaskMapper.queryTeachTask(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<TeachTask> queryTeachTask(String title,Teacher teacherObj,String addTime) throws Exception  { 
     	String where = "where 1=1";
    	if(!title.equals("")) where = where + " and t_teachTask.title like '%" + title + "%'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null && !teacherObj.getTeacherNo().equals(""))  where += " and t_teachTask.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_teachTask.addTime like '%" + addTime + "%'";
    	return teachTaskMapper.queryTeachTaskList(where);
    }

    /*查询所有教学任务记录*/
    public ArrayList<TeachTask> queryAllTeachTask()  throws Exception {
        return teachTaskMapper.queryTeachTaskList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String title,Teacher teacherObj,String addTime) throws Exception {
     	String where = "where 1=1";
    	if(!title.equals("")) where = where + " and t_teachTask.title like '%" + title + "%'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null && !teacherObj.getTeacherNo().equals(""))  where += " and t_teachTask.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!addTime.equals("")) where = where + " and t_teachTask.addTime like '%" + addTime + "%'";
        recordNumber = teachTaskMapper.queryTeachTaskCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取教学任务记录*/
    public TeachTask getTeachTask(int taskId) throws Exception  {
        TeachTask teachTask = teachTaskMapper.getTeachTask(taskId);
        return teachTask;
    }

    /*更新教学任务记录*/
    public void updateTeachTask(TeachTask teachTask) throws Exception {
        teachTaskMapper.updateTeachTask(teachTask);
    }

    /*删除一条教学任务记录*/
    public void deleteTeachTask (int taskId) throws Exception {
        teachTaskMapper.deleteTeachTask(taskId);
    }

    /*删除多条教学任务信息*/
    public int deleteTeachTasks (String taskIds) throws Exception {
    	String _taskIds[] = taskIds.split(",");
    	for(String _taskId: _taskIds) {
    		teachTaskMapper.deleteTeachTask(Integer.parseInt(_taskId));
    	}
    	return _taskIds.length;
    }
}
