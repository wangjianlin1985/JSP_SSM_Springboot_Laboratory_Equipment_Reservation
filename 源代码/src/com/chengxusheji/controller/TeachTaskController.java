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
import com.chengxusheji.service.TeachTaskService;
import com.chengxusheji.po.TeachTask;
import com.chengxusheji.service.TeacherService;
import com.chengxusheji.po.Teacher;

//TeachTask管理控制层
@Controller
@RequestMapping("/TeachTask")
public class TeachTaskController extends BaseController {

    /*业务层对象*/
    @Resource TeachTaskService teachTaskService;

    @Resource TeacherService teacherService;
	@InitBinder("teacherObj")
	public void initBinderteacherObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("teacherObj.");
	}
	@InitBinder("teachTask")
	public void initBinderTeachTask(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("teachTask.");
	}
	/*跳转到添加TeachTask视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new TeachTask());
		/*查询所有的Teacher信息*/
		List<Teacher> teacherList = teacherService.queryAllTeacher();
		request.setAttribute("teacherList", teacherList);
		return "TeachTask_add";
	}

	/*客户端ajax方式提交添加教学任务信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated TeachTask teachTask, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        teachTaskService.addTeachTask(teachTask);
        message = "教学任务添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询教学任务信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String title,@ModelAttribute("teacherObj") Teacher teacherObj,String addTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (title == null) title = "";
		if (addTime == null) addTime = "";
		if(rows != 0)teachTaskService.setRows(rows);
		List<TeachTask> teachTaskList = teachTaskService.queryTeachTask(title, teacherObj, addTime, page);
	    /*计算总的页数和总的记录数*/
	    teachTaskService.queryTotalPageAndRecordNumber(title, teacherObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = teachTaskService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = teachTaskService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(TeachTask teachTask:teachTaskList) {
			JSONObject jsonTeachTask = teachTask.getJsonObject();
			jsonArray.put(jsonTeachTask);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询教学任务信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<TeachTask> teachTaskList = teachTaskService.queryAllTeachTask();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(TeachTask teachTask:teachTaskList) {
			JSONObject jsonTeachTask = new JSONObject();
			jsonTeachTask.accumulate("taskId", teachTask.getTaskId());
			jsonTeachTask.accumulate("title", teachTask.getTitle());
			jsonArray.put(jsonTeachTask);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询教学任务信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String title,@ModelAttribute("teacherObj") Teacher teacherObj,String addTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (title == null) title = "";
		if (addTime == null) addTime = "";
		List<TeachTask> teachTaskList = teachTaskService.queryTeachTask(title, teacherObj, addTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    teachTaskService.queryTotalPageAndRecordNumber(title, teacherObj, addTime);
	    /*获取到总的页码数目*/
	    int totalPage = teachTaskService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = teachTaskService.getRecordNumber();
	    request.setAttribute("teachTaskList",  teachTaskList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("title", title);
	    request.setAttribute("teacherObj", teacherObj);
	    request.setAttribute("addTime", addTime);
	    List<Teacher> teacherList = teacherService.queryAllTeacher();
	    request.setAttribute("teacherList", teacherList);
		return "TeachTask/teachTask_frontquery_result"; 
	}

     /*前台查询TeachTask信息*/
	@RequestMapping(value="/{taskId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer taskId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键taskId获取TeachTask对象*/
        TeachTask teachTask = teachTaskService.getTeachTask(taskId);

        List<Teacher> teacherList = teacherService.queryAllTeacher();
        request.setAttribute("teacherList", teacherList);
        request.setAttribute("teachTask",  teachTask);
        return "TeachTask/teachTask_frontshow";
	}

	/*ajax方式显示教学任务修改jsp视图页*/
	@RequestMapping(value="/{taskId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer taskId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键taskId获取TeachTask对象*/
        TeachTask teachTask = teachTaskService.getTeachTask(taskId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonTeachTask = teachTask.getJsonObject();
		out.println(jsonTeachTask.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新教学任务信息*/
	@RequestMapping(value = "/{taskId}/update", method = RequestMethod.POST)
	public void update(@Validated TeachTask teachTask, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			teachTaskService.updateTeachTask(teachTask);
			message = "教学任务更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "教学任务更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除教学任务信息*/
	@RequestMapping(value="/{taskId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer taskId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  teachTaskService.deleteTeachTask(taskId);
	            request.setAttribute("message", "教学任务删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "教学任务删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条教学任务记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String taskIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = teachTaskService.deleteTeachTasks(taskIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出教学任务信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String title,@ModelAttribute("teacherObj") Teacher teacherObj,String addTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<TeachTask> teachTaskList = teachTaskService.queryTeachTask(title,teacherObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "TeachTask信息记录"; 
        String[] headers = { "任务id","任务标题","发布的老师","发布时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<teachTaskList.size();i++) {
        	TeachTask teachTask = teachTaskList.get(i); 
        	dataset.add(new String[]{teachTask.getTaskId() + "",teachTask.getTitle(),teachTask.getTeacherObj().getTeacherName(),teachTask.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"TeachTask.xls");//filename是下载的xls的名，建议最好用英文 
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
