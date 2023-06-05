package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.LabClass;

public interface LabClassMapper {
	/*添加实验室类别信息*/
	public void addLabClass(LabClass labClass) throws Exception;

	/*按照查询条件分页查询实验室类别记录*/
	public ArrayList<LabClass> queryLabClass(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有实验室类别记录*/
	public ArrayList<LabClass> queryLabClassList(@Param("where") String where) throws Exception;

	/*按照查询条件的实验室类别记录数*/
	public int queryLabClassCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条实验室类别记录*/
	public LabClass getLabClass(int classId) throws Exception;

	/*更新实验室类别记录*/
	public void updateLabClass(LabClass labClass) throws Exception;

	/*删除实验室类别记录*/
	public void deleteLabClass(int classId) throws Exception;

}
