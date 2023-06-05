package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.TeachTask;

public interface TeachTaskMapper {
	/*添加教学任务信息*/
	public void addTeachTask(TeachTask teachTask) throws Exception;

	/*按照查询条件分页查询教学任务记录*/
	public ArrayList<TeachTask> queryTeachTask(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有教学任务记录*/
	public ArrayList<TeachTask> queryTeachTaskList(@Param("where") String where) throws Exception;

	/*按照查询条件的教学任务记录数*/
	public int queryTeachTaskCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条教学任务记录*/
	public TeachTask getTeachTask(int taskId) throws Exception;

	/*更新教学任务记录*/
	public void updateTeachTask(TeachTask teachTask) throws Exception;

	/*删除教学任务记录*/
	public void deleteTeachTask(int taskId) throws Exception;

}
