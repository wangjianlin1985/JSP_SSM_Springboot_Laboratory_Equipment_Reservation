﻿package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ClassInfo;

public interface ClassInfoMapper {
	/*添加班级信息*/
	public void addClassInfo(ClassInfo classInfo) throws Exception;

	/*按照查询条件分页查询班级记录*/
	public ArrayList<ClassInfo> queryClassInfo(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有班级记录*/
	public ArrayList<ClassInfo> queryClassInfoList(@Param("where") String where) throws Exception;

	/*按照查询条件的班级记录数*/
	public int queryClassInfoCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条班级记录*/
	public ClassInfo getClassInfo(String classNo) throws Exception;

	/*更新班级记录*/
	public void updateClassInfo(ClassInfo classInfo) throws Exception;

	/*删除班级记录*/
	public void deleteClassInfo(String classNo) throws Exception;

}
