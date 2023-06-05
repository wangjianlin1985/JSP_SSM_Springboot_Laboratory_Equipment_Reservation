package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Device;
import com.chengxusheji.po.Teacher;
import com.chengxusheji.po.DeviceLend;

import com.chengxusheji.mapper.DeviceLendMapper;
@Service
public class DeviceLendService {

	@Resource DeviceLendMapper deviceLendMapper;
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

    /*添加设备借用记录*/
    public void addDeviceLend(DeviceLend deviceLend) throws Exception {
    	deviceLendMapper.addDeviceLend(deviceLend);
    }

    /*按照查询条件分页查询设备借用记录*/
    public ArrayList<DeviceLend> queryDeviceLend(Device deviceObj,Teacher teacherObj,String lendTime,String returnTime,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != deviceObj &&  deviceObj.getDeviceNo() != null  && !deviceObj.getDeviceNo().equals(""))  where += " and t_deviceLend.deviceObj='" + deviceObj.getDeviceNo() + "'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null  && !teacherObj.getTeacherNo().equals(""))  where += " and t_deviceLend.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!lendTime.equals("")) where = where + " and t_deviceLend.lendTime like '%" + lendTime + "%'";
    	if(!returnTime.equals("")) where = where + " and t_deviceLend.returnTime like '%" + returnTime + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return deviceLendMapper.queryDeviceLend(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<DeviceLend> queryDeviceLend(Device deviceObj,Teacher teacherObj,String lendTime,String returnTime) throws Exception  { 
     	String where = "where 1=1";
    	if(null != deviceObj &&  deviceObj.getDeviceNo() != null && !deviceObj.getDeviceNo().equals(""))  where += " and t_deviceLend.deviceObj='" + deviceObj.getDeviceNo() + "'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null && !teacherObj.getTeacherNo().equals(""))  where += " and t_deviceLend.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!lendTime.equals("")) where = where + " and t_deviceLend.lendTime like '%" + lendTime + "%'";
    	if(!returnTime.equals("")) where = where + " and t_deviceLend.returnTime like '%" + returnTime + "%'";
    	return deviceLendMapper.queryDeviceLendList(where);
    }

    /*查询所有设备借用记录*/
    public ArrayList<DeviceLend> queryAllDeviceLend()  throws Exception {
        return deviceLendMapper.queryDeviceLendList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Device deviceObj,Teacher teacherObj,String lendTime,String returnTime) throws Exception {
     	String where = "where 1=1";
    	if(null != deviceObj &&  deviceObj.getDeviceNo() != null && !deviceObj.getDeviceNo().equals(""))  where += " and t_deviceLend.deviceObj='" + deviceObj.getDeviceNo() + "'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null && !teacherObj.getTeacherNo().equals(""))  where += " and t_deviceLend.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!lendTime.equals("")) where = where + " and t_deviceLend.lendTime like '%" + lendTime + "%'";
    	if(!returnTime.equals("")) where = where + " and t_deviceLend.returnTime like '%" + returnTime + "%'";
        recordNumber = deviceLendMapper.queryDeviceLendCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取设备借用记录*/
    public DeviceLend getDeviceLend(int deviceLendId) throws Exception  {
        DeviceLend deviceLend = deviceLendMapper.getDeviceLend(deviceLendId);
        return deviceLend;
    }

    /*更新设备借用记录*/
    public void updateDeviceLend(DeviceLend deviceLend) throws Exception {
        deviceLendMapper.updateDeviceLend(deviceLend);
    }

    /*删除一条设备借用记录*/
    public void deleteDeviceLend (int deviceLendId) throws Exception {
        deviceLendMapper.deleteDeviceLend(deviceLendId);
    }

    /*删除多条设备借用信息*/
    public int deleteDeviceLends (String deviceLendIds) throws Exception {
    	String _deviceLendIds[] = deviceLendIds.split(",");
    	for(String _deviceLendId: _deviceLendIds) {
    		deviceLendMapper.deleteDeviceLend(Integer.parseInt(_deviceLendId));
    	}
    	return _deviceLendIds.length;
    }
}
