package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Experiment;

public interface ExperimentMapper {
	/*添加实验项目信息*/
	public void addExperiment(Experiment experiment) throws Exception;

	/*按照查询条件分页查询实验项目记录*/
	public ArrayList<Experiment> queryExperiment(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有实验项目记录*/
	public ArrayList<Experiment> queryExperimentList(@Param("where") String where) throws Exception;

	/*按照查询条件的实验项目记录数*/
	public int queryExperimentCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条实验项目记录*/
	public Experiment getExperiment(int experimentId) throws Exception;

	/*更新实验项目记录*/
	public void updateExperiment(Experiment experiment) throws Exception;

	/*删除实验项目记录*/
	public void deleteExperiment(int experimentId) throws Exception;

}
