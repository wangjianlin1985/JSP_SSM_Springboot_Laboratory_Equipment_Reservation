package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.ExperimentService;
import com.chengxusheji.service.StudentService;
import com.chengxusheji.po.Experiment;
import com.chengxusheji.service.ClassInfoService;
import com.chengxusheji.po.ClassInfo;
import com.chengxusheji.service.LabInfoService;
import com.chengxusheji.po.LabInfo;

//Experiment管理控制层
@Controller
@RequestMapping("/Experiment")
public class ExperimentController extends BaseController {

    /*业务层对象*/
    @Resource ExperimentService experimentService;

    @Resource ClassInfoService classInfoService;
    @Resource LabInfoService labInfoService;
    @Resource StudentService studentService;
    
	@InitBinder("classObj")
	public void initBinderclassObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("classObj.");
	}
	@InitBinder("labObj")
	public void initBinderlabObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("labObj.");
	}
	@InitBinder("experiment")
	public void initBinderExperiment(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("experiment.");
	}
	/*跳转到添加Experiment视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Experiment());
		/*查询所有的ClassInfo信息*/
		List<ClassInfo> classInfoList = classInfoService.queryAllClassInfo();
		request.setAttribute("classInfoList", classInfoList);
		/*查询所有的LabInfo信息*/
		List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
		request.setAttribute("labInfoList", labInfoList);
		return "Experiment_add";
	}

	/*客户端ajax方式提交添加实验项目信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Experiment experiment, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        experimentService.addExperiment(experiment);
        message = "实验项目添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询实验项目信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String experimentName,@ModelAttribute("classObj") ClassInfo classObj,@ModelAttribute("labObj") LabInfo labObj,String experimentDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (experimentName == null) experimentName = "";
		if (experimentDate == null) experimentDate = "";
		if(rows != 0)experimentService.setRows(rows);
		List<Experiment> experimentList = experimentService.queryExperiment(experimentName, classObj, labObj, experimentDate, page);
	    /*计算总的页数和总的记录数*/
	    experimentService.queryTotalPageAndRecordNumber(experimentName, classObj, labObj, experimentDate);
	    /*获取到总的页码数目*/
	    int totalPage = experimentService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = experimentService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Experiment experiment:experimentList) {
			JSONObject jsonExperiment = experiment.getJsonObject();
			jsonArray.put(jsonExperiment);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询实验项目信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Experiment> experimentList = experimentService.queryAllExperiment();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Experiment experiment:experimentList) {
			JSONObject jsonExperiment = new JSONObject();
			jsonExperiment.accumulate("experimentId", experiment.getExperimentId());
			jsonExperiment.accumulate("experimentName", experiment.getExperimentName());
			jsonArray.put(jsonExperiment);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询实验项目信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String experimentName,@ModelAttribute("classObj") ClassInfo classObj,@ModelAttribute("labObj") LabInfo labObj,String experimentDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (experimentName == null) experimentName = "";
		if (experimentDate == null) experimentDate = "";
		List<Experiment> experimentList = experimentService.queryExperiment(experimentName, classObj, labObj, experimentDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    experimentService.queryTotalPageAndRecordNumber(experimentName, classObj, labObj, experimentDate);
	    /*获取到总的页码数目*/
	    int totalPage = experimentService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = experimentService.getRecordNumber();
	    request.setAttribute("experimentList",  experimentList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("experimentName", experimentName);
	    request.setAttribute("classObj", classObj);
	    request.setAttribute("labObj", labObj);
	    request.setAttribute("experimentDate", experimentDate);
	    List<ClassInfo> classInfoList = classInfoService.queryAllClassInfo();
	    request.setAttribute("classInfoList", classInfoList);
	    List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
	    request.setAttribute("labInfoList", labInfoList);
		return "Experiment/experiment_frontquery_result"; 
	}
	
	
	/*前台学生按照查询条件分页查询实验项目上课信息*/
	@RequestMapping(value = { "/studentFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String studentFrontlist(String experimentName,@ModelAttribute("classObj") ClassInfo classObj,@ModelAttribute("labObj") LabInfo labObj,String experimentDate,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (experimentName == null) experimentName = "";
		if (experimentDate == null) experimentDate = "";
		String user_name = session.getAttribute("user_name").toString();
		classObj = studentService.getStudent(user_name).getClassObj(); 
		List<Experiment> experimentList = experimentService.queryExperiment(experimentName, classObj, labObj, experimentDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    experimentService.queryTotalPageAndRecordNumber(experimentName, classObj, labObj, experimentDate);
	    /*获取到总的页码数目*/
	    int totalPage = experimentService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = experimentService.getRecordNumber();
	    request.setAttribute("experimentList",  experimentList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("experimentName", experimentName);
	    request.setAttribute("classObj", classObj);
	    request.setAttribute("labObj", labObj);
	    request.setAttribute("experimentDate", experimentDate);
	    List<ClassInfo> classInfoList = classInfoService.queryAllClassInfo();
	    request.setAttribute("classInfoList", classInfoList);
	    List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
	    request.setAttribute("labInfoList", labInfoList);
		return "Experiment/experiment_studentFrontquery_result"; 
	}
	

     /*前台查询Experiment信息*/
	@RequestMapping(value="/{experimentId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer experimentId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键experimentId获取Experiment对象*/
        Experiment experiment = experimentService.getExperiment(experimentId);

        List<ClassInfo> classInfoList = classInfoService.queryAllClassInfo();
        request.setAttribute("classInfoList", classInfoList);
        List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
        request.setAttribute("labInfoList", labInfoList);
        request.setAttribute("experiment",  experiment);
        return "Experiment/experiment_frontshow";
	}

	/*ajax方式显示实验项目修改jsp视图页*/
	@RequestMapping(value="/{experimentId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer experimentId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键experimentId获取Experiment对象*/
        Experiment experiment = experimentService.getExperiment(experimentId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonExperiment = experiment.getJsonObject();
		out.println(jsonExperiment.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新实验项目信息*/
	@RequestMapping(value = "/{experimentId}/update", method = RequestMethod.POST)
	public void update(@Validated Experiment experiment, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			experimentService.updateExperiment(experiment);
			message = "实验项目更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "实验项目更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除实验项目信息*/
	@RequestMapping(value="/{experimentId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer experimentId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  experimentService.deleteExperiment(experimentId);
	            request.setAttribute("message", "实验项目删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "实验项目删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条实验项目记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String experimentIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = experimentService.deleteExperiments(experimentIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出实验项目信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String experimentName,@ModelAttribute("classObj") ClassInfo classObj,@ModelAttribute("labObj") LabInfo labObj,String experimentDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(experimentName == null) experimentName = "";
        if(experimentDate == null) experimentDate = "";
        List<Experiment> experimentList = experimentService.queryExperiment(experimentName,classObj,labObj,experimentDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Experiment信息记录"; 
        String[] headers = { "实验项目id","实验项目名称","上课班级","上课实验室","实验日期","实验时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<experimentList.size();i++) {
        	Experiment experiment = experimentList.get(i); 
        	dataset.add(new String[]{experiment.getExperimentId() + "",experiment.getExperimentName(),experiment.getClassObj().getClassName(),experiment.getLabObj().getLabName(),experiment.getExperimentDate(),experiment.getExperimentTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Experiment.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
