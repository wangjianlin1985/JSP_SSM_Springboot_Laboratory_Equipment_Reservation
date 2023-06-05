package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.LabOrder;

public interface LabOrderMapper {
	/*添加实验室预约信息*/
	public void addLabOrder(LabOrder labOrder) throws Exception;

	/*按照查询条件分页查询实验室预约记录*/
	public ArrayList<LabOrder> queryLabOrder(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有实验室预约记录*/
	public ArrayList<LabOrder> queryLabOrderList(@Param("where") String where) throws Exception;

	/*按照查询条件的实验室预约记录数*/
	public int queryLabOrderCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条实验室预约记录*/
	public LabOrder getLabOrder(int orderId) throws Exception;

	/*更新实验室预约记录*/
	public void updateLabOrder(LabOrder labOrder) throws Exception;

	/*删除实验室预约记录*/
	public void deleteLabOrder(int orderId) throws Exception;

}
