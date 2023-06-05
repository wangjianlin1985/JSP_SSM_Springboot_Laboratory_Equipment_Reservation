package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.LabClass;

import com.chengxusheji.mapper.LabClassMapper;
@Service
public class LabClassService {

	@Resource LabClassMapper labClassMapper;
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

    /*添加实验室类别记录*/
    public void addLabClass(LabClass labClass) throws Exception {
    	labClassMapper.addLabClass(labClass);
    }

    /*按照查询条件分页查询实验室类别记录*/
    public ArrayList<LabClass> queryLabClass(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return labClassMapper.queryLabClass(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<LabClass> queryLabClass() throws Exception  { 
     	String where = "where 1=1";
    	return labClassMapper.queryLabClassList(where);
    }

    /*查询所有实验室类别记录*/
    public ArrayList<LabClass> queryAllLabClass()  throws Exception {
        return labClassMapper.queryLabClassList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = labClassMapper.queryLabClassCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取实验室类别记录*/
    public LabClass getLabClass(int classId) throws Exception  {
        LabClass labClass = labClassMapper.getLabClass(classId);
        return labClass;
    }

    /*更新实验室类别记录*/
    public void updateLabClass(LabClass labClass) throws Exception {
        labClassMapper.updateLabClass(labClass);
    }

    /*删除一条实验室类别记录*/
    public void deleteLabClass (int classId) throws Exception {
        labClassMapper.deleteLabClass(classId);
    }

    /*删除多条实验室类别信息*/
    public int deleteLabClasss (String classIds) throws Exception {
    	String _classIds[] = classIds.split(",");
    	for(String _classId: _classIds) {
    		labClassMapper.deleteLabClass(Integer.parseInt(_classId));
    	}
    	return _classIds.length;
    }
}
