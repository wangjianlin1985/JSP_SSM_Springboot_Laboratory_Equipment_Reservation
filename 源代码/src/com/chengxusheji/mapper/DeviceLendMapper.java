package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.DeviceLend;

public interface DeviceLendMapper {
	/*添加设备借用信息*/
	public void addDeviceLend(DeviceLend deviceLend) throws Exception;

	/*按照查询条件分页查询设备借用记录*/
	public ArrayList<DeviceLend> queryDeviceLend(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有设备借用记录*/
	public ArrayList<DeviceLend> queryDeviceLendList(@Param("where") String where) throws Exception;

	/*按照查询条件的设备借用记录数*/
	public int queryDeviceLendCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条设备借用记录*/
	public DeviceLend getDeviceLend(int deviceLendId) throws Exception;

	/*更新设备借用记录*/
	public void updateDeviceLend(DeviceLend deviceLend) throws Exception;

	/*删除设备借用记录*/
	public void deleteDeviceLend(int deviceLendId) throws Exception;

}
