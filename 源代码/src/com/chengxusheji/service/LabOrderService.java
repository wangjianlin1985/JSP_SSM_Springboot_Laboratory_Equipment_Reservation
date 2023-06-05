package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.LabInfo;
import com.chengxusheji.po.Teacher;
import com.chengxusheji.po.LabOrder;

import com.chengxusheji.mapper.LabOrderMapper;
@Service
public class LabOrderService {

	@Resource LabOrderMapper labOrderMapper;
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

    /*添加实验室预约记录*/
    public void addLabOrder(LabOrder labOrder) throws Exception {
    	labOrderMapper.addLabOrder(labOrder);
    }

    /*按照查询条件分页查询实验室预约记录*/
    public ArrayList<LabOrder> queryLabOrder(LabInfo labObj,Teacher teacherObj,String orderDate,String shenHeState,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != labObj &&  labObj.getLabNumber() != null  && !labObj.getLabNumber().equals(""))  where += " and t_labOrder.labObj='" + labObj.getLabNumber() + "'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null  && !teacherObj.getTeacherNo().equals(""))  where += " and t_labOrder.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!orderDate.equals("")) where = where + " and t_labOrder.orderDate like '%" + orderDate + "%'";
    	if(!shenHeState.equals("")) where = where + " and t_labOrder.shenHeState like '%" + shenHeState + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return labOrderMapper.queryLabOrder(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<LabOrder> queryLabOrder(LabInfo labObj,Teacher teacherObj,String orderDate,String shenHeState) throws Exception  { 
     	String where = "where 1=1";
    	if(null != labObj &&  labObj.getLabNumber() != null && !labObj.getLabNumber().equals(""))  where += " and t_labOrder.labObj='" + labObj.getLabNumber() + "'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null && !teacherObj.getTeacherNo().equals(""))  where += " and t_labOrder.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!orderDate.equals("")) where = where + " and t_labOrder.orderDate like '%" + orderDate + "%'";
    	if(!shenHeState.equals("")) where = where + " and t_labOrder.shenHeState like '%" + shenHeState + "%'";
    	return labOrderMapper.queryLabOrderList(where);
    }

    /*查询所有实验室预约记录*/
    public ArrayList<LabOrder> queryAllLabOrder()  throws Exception {
        return labOrderMapper.queryLabOrderList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(LabInfo labObj,Teacher teacherObj,String orderDate,String shenHeState) throws Exception {
     	String where = "where 1=1";
    	if(null != labObj &&  labObj.getLabNumber() != null && !labObj.getLabNumber().equals(""))  where += " and t_labOrder.labObj='" + labObj.getLabNumber() + "'";
    	if(null != teacherObj &&  teacherObj.getTeacherNo() != null && !teacherObj.getTeacherNo().equals(""))  where += " and t_labOrder.teacherObj='" + teacherObj.getTeacherNo() + "'";
    	if(!orderDate.equals("")) where = where + " and t_labOrder.orderDate like '%" + orderDate + "%'";
    	if(!shenHeState.equals("")) where = where + " and t_labOrder.shenHeState like '%" + shenHeState + "%'";
        recordNumber = labOrderMapper.queryLabOrderCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取实验室预约记录*/
    public LabOrder getLabOrder(int orderId) throws Exception  {
        LabOrder labOrder = labOrderMapper.getLabOrder(orderId);
        return labOrder;
    }

    /*更新实验室预约记录*/
    public void updateLabOrder(LabOrder labOrder) throws Exception {
        labOrderMapper.updateLabOrder(labOrder);
    }

    /*删除一条实验室预约记录*/
    public void deleteLabOrder (int orderId) throws Exception {
        labOrderMapper.deleteLabOrder(orderId);
    }

    /*删除多条实验室预约信息*/
    public int deleteLabOrders (String orderIds) throws Exception {
    	String _orderIds[] = orderIds.split(",");
    	for(String _orderId: _orderIds) {
    		labOrderMapper.deleteLabOrder(Integer.parseInt(_orderId));
    	}
    	return _orderIds.length;
    }
}
