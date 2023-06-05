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
import com.chengxusheji.service.LabInfoService;
import com.chengxusheji.po.LabInfo;
import com.chengxusheji.service.LabClassService;
import com.chengxusheji.po.LabClass;

//LabInfo管理控制层
@Controller
@RequestMapping("/LabInfo")
public class LabInfoController extends BaseController {

    /*业务层对象*/
    @Resource LabInfoService labInfoService;

    @Resource LabClassService labClassService;
	@InitBinder("labClassObj")
	public void initBinderlabClassObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("labClassObj.");
	}
	@InitBinder("labInfo")
	public void initBinderLabInfo(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("labInfo.");
	}
	/*跳转到添加LabInfo视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new LabInfo());
		/*查询所有的LabClass信息*/
		List<LabClass> labClassList = labClassService.queryAllLabClass();
		request.setAttribute("labClassList", labClassList);
		return "LabInfo_add";
	}

	/*客户端ajax方式提交添加实验室信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated LabInfo labInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(labInfoService.getLabInfo(labInfo.getLabNumber()) != null) {
			message = "实验室编号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			labInfo.setLabPhoto(this.handlePhotoUpload(request, "labPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        labInfoService.addLabInfo(labInfo);
        message = "实验室添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询实验室信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String labNumber,String labName,@ModelAttribute("labClassObj") LabClass labClassObj,String labState,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (labNumber == null) labNumber = "";
		if (labName == null) labName = "";
		if (labState == null) labState = "";
		if(rows != 0)labInfoService.setRows(rows);
		List<LabInfo> labInfoList = labInfoService.queryLabInfo(labNumber, labName, labClassObj, labState, page);
	    /*计算总的页数和总的记录数*/
	    labInfoService.queryTotalPageAndRecordNumber(labNumber, labName, labClassObj, labState);
	    /*获取到总的页码数目*/
	    int totalPage = labInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = labInfoService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(LabInfo labInfo:labInfoList) {
			JSONObject jsonLabInfo = labInfo.getJsonObject();
			jsonArray.put(jsonLabInfo);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询实验室信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(LabInfo labInfo:labInfoList) {
			JSONObject jsonLabInfo = new JSONObject();
			jsonLabInfo.accumulate("labNumber", labInfo.getLabNumber());
			jsonLabInfo.accumulate("labName", labInfo.getLabName());
			jsonArray.put(jsonLabInfo);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询实验室信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String labNumber,String labName,@ModelAttribute("labClassObj") LabClass labClassObj,String labState,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (labNumber == null) labNumber = "";
		if (labName == null) labName = "";
		if (labState == null) labState = "";
		List<LabInfo> labInfoList = labInfoService.queryLabInfo(labNumber, labName, labClassObj, labState, currentPage);
	    /*计算总的页数和总的记录数*/
	    labInfoService.queryTotalPageAndRecordNumber(labNumber, labName, labClassObj, labState);
	    /*获取到总的页码数目*/
	    int totalPage = labInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = labInfoService.getRecordNumber();
	    request.setAttribute("labInfoList",  labInfoList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("labNumber", labNumber);
	    request.setAttribute("labName", labName);
	    request.setAttribute("labClassObj", labClassObj);
	    request.setAttribute("labState", labState);
	    List<LabClass> labClassList = labClassService.queryAllLabClass();
	    request.setAttribute("labClassList", labClassList);
		return "LabInfo/labInfo_frontquery_result"; 
	}

     /*前台查询LabInfo信息*/
	@RequestMapping(value="/{labNumber}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String labNumber,Model model,HttpServletRequest request) throws Exception {
		/*根据主键labNumber获取LabInfo对象*/
        LabInfo labInfo = labInfoService.getLabInfo(labNumber);

        List<LabClass> labClassList = labClassService.queryAllLabClass();
        request.setAttribute("labClassList", labClassList);
        request.setAttribute("labInfo",  labInfo);
        return "LabInfo/labInfo_frontshow";
	}

	/*ajax方式显示实验室修改jsp视图页*/
	@RequestMapping(value="/{labNumber}/update",method=RequestMethod.GET)
	public void update(@PathVariable String labNumber,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键labNumber获取LabInfo对象*/
        LabInfo labInfo = labInfoService.getLabInfo(labNumber);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonLabInfo = labInfo.getJsonObject();
		out.println(jsonLabInfo.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新实验室信息*/
	@RequestMapping(value = "/{labNumber}/update", method = RequestMethod.POST)
	public void update(@Validated LabInfo labInfo, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String labPhotoFileName = this.handlePhotoUpload(request, "labPhotoFile");
		if(!labPhotoFileName.equals("upload/NoImage.jpg"))labInfo.setLabPhoto(labPhotoFileName); 


		try {
			labInfoService.updateLabInfo(labInfo);
			message = "实验室更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "实验室更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除实验室信息*/
	@RequestMapping(value="/{labNumber}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String labNumber,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  labInfoService.deleteLabInfo(labNumber);
	            request.setAttribute("message", "实验室删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "实验室删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条实验室记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String labNumbers,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = labInfoService.deleteLabInfos(labNumbers);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出实验室信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String labNumber,String labName,@ModelAttribute("labClassObj") LabClass labClassObj,String labState, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(labNumber == null) labNumber = "";
        if(labName == null) labName = "";
        if(labState == null) labState = "";
        List<LabInfo> labInfoList = labInfoService.queryLabInfo(labNumber,labName,labClassObj,labState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "LabInfo信息记录"; 
        String[] headers = { "实验室编号","实验室名称","实验室类别","实验室面积","实验室图片","实验室地址","实验室状态"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<labInfoList.size();i++) {
        	LabInfo labInfo = labInfoList.get(i); 
        	dataset.add(new String[]{labInfo.getLabNumber(),labInfo.getLabName(),labInfo.getLabClassObj().getClassName(),labInfo.getLabArea() + "",labInfo.getLabPhoto(),labInfo.getLabAddress(),labInfo.getLabState()});
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
			response.setHeader("Content-disposition","attachment; filename="+"LabInfo.xls");//filename是下载的xls的名，建议最好用英文 
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
