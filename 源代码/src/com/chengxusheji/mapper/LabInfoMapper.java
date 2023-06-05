package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.LabInfo;

public interface LabInfoMapper {
	/*添加实验室信息*/
	public void addLabInfo(LabInfo labInfo) throws Exception;

	/*按照查询条件分页查询实验室记录*/
	public ArrayList<LabInfo> queryLabInfo(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有实验室记录*/
	public ArrayList<LabInfo> queryLabInfoList(@Param("where") String where) throws Exception;

	/*按照查询条件的实验室记录数*/
	public int queryLabInfoCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条实验室记录*/
	public LabInfo getLabInfo(String labNumber) throws Exception;

	/*更新实验室记录*/
	public void updateLabInfo(LabInfo labInfo) throws Exception;

	/*删除实验室记录*/
	public void deleteLabInfo(String labNumber) throws Exception;

}
