package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ClassInfo;
import com.chengxusheji.po.LabInfo;
import com.chengxusheji.po.Experiment;

import com.chengxusheji.mapper.ExperimentMapper;
@Service
public class ExperimentService {

	@Resource ExperimentMapper experimentMapper;
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

    /*添加实验项目记录*/
    public void addExperiment(Experiment experiment) throws Exception {
    	experimentMapper.addExperiment(experiment);
    }

    /*按照查询条件分页查询实验项目记录*/
    public ArrayList<Experiment> queryExperiment(String experimentName,ClassInfo classObj,LabInfo labObj,String experimentDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!experimentName.equals("")) where = where + " and t_experiment.experimentName like '%" + experimentName + "%'";
    	if(null != classObj &&  classObj.getClassNo() != null  && !classObj.getClassNo().equals(""))  where += " and t_experiment.classObj='" + classObj.getClassNo() + "'";
    	if(null != labObj &&  labObj.getLabNumber() != null  && !labObj.getLabNumber().equals(""))  where += " and t_experiment.labObj='" + labObj.getLabNumber() + "'";
    	if(!experimentDate.equals("")) where = where + " and t_experiment.experimentDate like '%" + experimentDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return experimentMapper.queryExperiment(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Experiment> queryExperiment(String experimentName,ClassInfo classObj,LabInfo labObj,String experimentDate) throws Exception  { 
     	String where = "where 1=1";
    	if(!experimentName.equals("")) where = where + " and t_experiment.experimentName like '%" + experimentName + "%'";
    	if(null != classObj &&  classObj.getClassNo() != null && !classObj.getClassNo().equals(""))  where += " and t_experiment.classObj='" + classObj.getClassNo() + "'";
    	if(null != labObj &&  labObj.getLabNumber() != null && !labObj.getLabNumber().equals(""))  where += " and t_experiment.labObj='" + labObj.getLabNumber() + "'";
    	if(!experimentDate.equals("")) where = where + " and t_experiment.experimentDate like '%" + experimentDate + "%'";
    	return experimentMapper.queryExperimentList(where);
    }

    /*查询所有实验项目记录*/
    public ArrayList<Experiment> queryAllExperiment()  throws Exception {
        return experimentMapper.queryExperimentList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String experimentName,ClassInfo classObj,LabInfo labObj,String experimentDate) throws Exception {
     	String where = "where 1=1";
    	if(!experimentName.equals("")) where = where + " and t_experiment.experimentName like '%" + experimentName + "%'";
    	if(null != classObj &&  classObj.getClassNo() != null && !classObj.getClassNo().equals(""))  where += " and t_experiment.classObj='" + classObj.getClassNo() + "'";
    	if(null != labObj &&  labObj.getLabNumber() != null && !labObj.getLabNumber().equals(""))  where += " and t_experiment.labObj='" + labObj.getLabNumber() + "'";
    	if(!experimentDate.equals("")) where = where + " and t_experiment.experimentDate like '%" + experimentDate + "%'";
        recordNumber = experimentMapper.queryExperimentCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取实验项目记录*/
    public Experiment getExperiment(int experimentId) throws Exception  {
        Experiment experiment = experimentMapper.getExperiment(experimentId);
        return experiment;
    }

    /*更新实验项目记录*/
    public void updateExperiment(Experiment experiment) throws Exception {
        experimentMapper.updateExperiment(experiment);
    }

    /*删除一条实验项目记录*/
    public void deleteExperiment (int experimentId) throws Exception {
        experimentMapper.deleteExperiment(experimentId);
    }

    /*删除多条实验项目信息*/
    public int deleteExperiments (String experimentIds) throws Exception {
    	String _experimentIds[] = experimentIds.split(",");
    	for(String _experimentId: _experimentIds) {
    		experimentMapper.deleteExperiment(Integer.parseInt(_experimentId));
    	}
    	return _experimentIds.length;
    }
}
