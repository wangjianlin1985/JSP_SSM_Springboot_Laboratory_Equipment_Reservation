package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.LabClass;
import com.chengxusheji.po.LabInfo;

import com.chengxusheji.mapper.LabInfoMapper;
@Service
public class LabInfoService {

	@Resource LabInfoMapper labInfoMapper;
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

    /*添加实验室记录*/
    public void addLabInfo(LabInfo labInfo) throws Exception {
    	labInfoMapper.addLabInfo(labInfo);
    }

    /*按照查询条件分页查询实验室记录*/
    public ArrayList<LabInfo> queryLabInfo(String labNumber,String labName,LabClass labClassObj,String labState,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!labNumber.equals("")) where = where + " and t_labInfo.labNumber like '%" + labNumber + "%'";
    	if(!labName.equals("")) where = where + " and t_labInfo.labName like '%" + labName + "%'";
    	if(null != labClassObj && labClassObj.getClassId()!= null && labClassObj.getClassId()!= 0)  where += " and t_labInfo.labClassObj=" + labClassObj.getClassId();
    	if(!labState.equals("")) where = where + " and t_labInfo.labState like '%" + labState + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return labInfoMapper.queryLabInfo(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<LabInfo> queryLabInfo(String labNumber,String labName,LabClass labClassObj,String labState) throws Exception  { 
     	String where = "where 1=1";
    	if(!labNumber.equals("")) where = where + " and t_labInfo.labNumber like '%" + labNumber + "%'";
    	if(!labName.equals("")) where = where + " and t_labInfo.labName like '%" + labName + "%'";
    	if(null != labClassObj && labClassObj.getClassId()!= null && labClassObj.getClassId()!= 0)  where += " and t_labInfo.labClassObj=" + labClassObj.getClassId();
    	if(!labState.equals("")) where = where + " and t_labInfo.labState like '%" + labState + "%'";
    	return labInfoMapper.queryLabInfoList(where);
    }

    /*查询所有实验室记录*/
    public ArrayList<LabInfo> queryAllLabInfo()  throws Exception {
        return labInfoMapper.queryLabInfoList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String labNumber,String labName,LabClass labClassObj,String labState) throws Exception {
     	String where = "where 1=1";
    	if(!labNumber.equals("")) where = where + " and t_labInfo.labNumber like '%" + labNumber + "%'";
    	if(!labName.equals("")) where = where + " and t_labInfo.labName like '%" + labName + "%'";
    	if(null != labClassObj && labClassObj.getClassId()!= null && labClassObj.getClassId()!= 0)  where += " and t_labInfo.labClassObj=" + labClassObj.getClassId();
    	if(!labState.equals("")) where = where + " and t_labInfo.labState like '%" + labState + "%'";
        recordNumber = labInfoMapper.queryLabInfoCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取实验室记录*/
    public LabInfo getLabInfo(String labNumber) throws Exception  {
        LabInfo labInfo = labInfoMapper.getLabInfo(labNumber);
        return labInfo;
    }

    /*更新实验室记录*/
    public void updateLabInfo(LabInfo labInfo) throws Exception {
        labInfoMapper.updateLabInfo(labInfo);
    }

    /*删除一条实验室记录*/
    public void deleteLabInfo (String labNumber) throws Exception {
        labInfoMapper.deleteLabInfo(labNumber);
    }

    /*删除多条实验室信息*/
    public int deleteLabInfos (String labNumbers) throws Exception {
    	String _labNumbers[] = labNumbers.split(",");
    	for(String _labNumber: _labNumbers) {
    		labInfoMapper.deleteLabInfo(_labNumber);
    	}
    	return _labNumbers.length;
    }
}
