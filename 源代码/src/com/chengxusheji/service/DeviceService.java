package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Device;

import com.chengxusheji.mapper.DeviceMapper;
@Service
public class DeviceService {

	@Resource DeviceMapper deviceMapper;
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

    /*添加实验设备记录*/
    public void addDevice(Device device) throws Exception {
    	deviceMapper.addDevice(device);
    }

    /*按照查询条件分页查询实验设备记录*/
    public ArrayList<Device> queryDevice(String deviceNo,String deviceName,String madePlace,String outDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!deviceNo.equals("")) where = where + " and t_device.deviceNo like '%" + deviceNo + "%'";
    	if(!deviceName.equals("")) where = where + " and t_device.deviceName like '%" + deviceName + "%'";
    	if(!madePlace.equals("")) where = where + " and t_device.madePlace like '%" + madePlace + "%'";
    	if(!outDate.equals("")) where = where + " and t_device.outDate like '%" + outDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return deviceMapper.queryDevice(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Device> queryDevice(String deviceNo,String deviceName,String madePlace,String outDate) throws Exception  { 
     	String where = "where 1=1";
    	if(!deviceNo.equals("")) where = where + " and t_device.deviceNo like '%" + deviceNo + "%'";
    	if(!deviceName.equals("")) where = where + " and t_device.deviceName like '%" + deviceName + "%'";
    	if(!madePlace.equals("")) where = where + " and t_device.madePlace like '%" + madePlace + "%'";
    	if(!outDate.equals("")) where = where + " and t_device.outDate like '%" + outDate + "%'";
    	return deviceMapper.queryDeviceList(where);
    }

    /*查询所有实验设备记录*/
    public ArrayList<Device> queryAllDevice()  throws Exception {
        return deviceMapper.queryDeviceList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String deviceNo,String deviceName,String madePlace,String outDate) throws Exception {
     	String where = "where 1=1";
    	if(!deviceNo.equals("")) where = where + " and t_device.deviceNo like '%" + deviceNo + "%'";
    	if(!deviceName.equals("")) where = where + " and t_device.deviceName like '%" + deviceName + "%'";
    	if(!madePlace.equals("")) where = where + " and t_device.madePlace like '%" + madePlace + "%'";
    	if(!outDate.equals("")) where = where + " and t_device.outDate like '%" + outDate + "%'";
        recordNumber = deviceMapper.queryDeviceCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取实验设备记录*/
    public Device getDevice(String deviceNo) throws Exception  {
        Device device = deviceMapper.getDevice(deviceNo);
        return device;
    }

    /*更新实验设备记录*/
    public void updateDevice(Device device) throws Exception {
        deviceMapper.updateDevice(device);
    }

    /*删除一条实验设备记录*/
    public void deleteDevice (String deviceNo) throws Exception {
        deviceMapper.deleteDevice(deviceNo);
    }

    /*删除多条实验设备信息*/
    public int deleteDevices (String deviceNos) throws Exception {
    	String _deviceNos[] = deviceNos.split(",");
    	for(String _deviceNo: _deviceNos) {
    		deviceMapper.deleteDevice(_deviceNo);
    	}
    	return _deviceNos.length;
    }
}
