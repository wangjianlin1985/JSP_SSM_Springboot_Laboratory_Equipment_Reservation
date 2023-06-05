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
import com.chengxusheji.service.DeviceLendService;
import com.chengxusheji.po.DeviceLend;
import com.chengxusheji.service.DeviceService;
import com.chengxusheji.po.Device;
import com.chengxusheji.service.TeacherService;
import com.chengxusheji.po.Teacher;

//DeviceLend管理控制层
@Controller
@RequestMapping("/DeviceLend")
public class DeviceLendController extends BaseController {

    /*业务层对象*/
    @Resource DeviceLendService deviceLendService;

    @Resource DeviceService deviceService;
    @Resource TeacherService teacherService;
	@InitBinder("deviceObj")
	public void initBinderdeviceObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("deviceObj.");
	}
	@InitBinder("teacherObj")
	public void initBinderteacherObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("teacherObj.");
	}
	@InitBinder("deviceLend")
	public void initBinderDeviceLend(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("deviceLend.");
	}
	/*跳转到添加DeviceLend视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new DeviceLend());
		/*查询所有的Device信息*/
		List<Device> deviceList = deviceService.queryAllDevice();
		request.setAttribute("deviceList", deviceList);
		/*查询所有的Teacher信息*/
		List<Teacher> teacherList = teacherService.queryAllTeacher();
		request.setAttribute("teacherList", teacherList);
		return "DeviceLend_add";
	}

	/*客户端ajax方式提交添加设备借用信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated DeviceLend deviceLend, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		deviceLend.setReturnTime("--");
        deviceLendService.addDeviceLend(deviceLend);
        message = "设备借用添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询设备借用信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("deviceObj") Device deviceObj,@ModelAttribute("teacherObj") Teacher teacherObj,String lendTime,String returnTime,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (lendTime == null) lendTime = "";
		if (returnTime == null) returnTime = "";
		if(rows != 0)deviceLendService.setRows(rows);
		List<DeviceLend> deviceLendList = deviceLendService.queryDeviceLend(deviceObj, teacherObj, lendTime, returnTime, page);
	    /*计算总的页数和总的记录数*/
	    deviceLendService.queryTotalPageAndRecordNumber(deviceObj, teacherObj, lendTime, returnTime);
	    /*获取到总的页码数目*/
	    int totalPage = deviceLendService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = deviceLendService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(DeviceLend deviceLend:deviceLendList) {
			JSONObject jsonDeviceLend = deviceLend.getJsonObject();
			jsonArray.put(jsonDeviceLend);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询设备借用信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<DeviceLend> deviceLendList = deviceLendService.queryAllDeviceLend();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(DeviceLend deviceLend:deviceLendList) {
			JSONObject jsonDeviceLend = new JSONObject();
			jsonDeviceLend.accumulate("deviceLendId", deviceLend.getDeviceLendId());
			jsonArray.put(jsonDeviceLend);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询设备借用信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("deviceObj") Device deviceObj,@ModelAttribute("teacherObj") Teacher teacherObj,String lendTime,String returnTime,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (lendTime == null) lendTime = "";
		if (returnTime == null) returnTime = "";
		List<DeviceLend> deviceLendList = deviceLendService.queryDeviceLend(deviceObj, teacherObj, lendTime, returnTime, currentPage);
	    /*计算总的页数和总的记录数*/
	    deviceLendService.queryTotalPageAndRecordNumber(deviceObj, teacherObj, lendTime, returnTime);
	    /*获取到总的页码数目*/
	    int totalPage = deviceLendService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = deviceLendService.getRecordNumber();
	    request.setAttribute("deviceLendList",  deviceLendList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("deviceObj", deviceObj);
	    request.setAttribute("teacherObj", teacherObj);
	    request.setAttribute("lendTime", lendTime);
	    request.setAttribute("returnTime", returnTime);
	    List<Device> deviceList = deviceService.queryAllDevice();
	    request.setAttribute("deviceList", deviceList);
	    List<Teacher> teacherList = teacherService.queryAllTeacher();
	    request.setAttribute("teacherList", teacherList);
		return "DeviceLend/deviceLend_frontquery_result"; 
	}

     /*前台查询DeviceLend信息*/
	@RequestMapping(value="/{deviceLendId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer deviceLendId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键deviceLendId获取DeviceLend对象*/
        DeviceLend deviceLend = deviceLendService.getDeviceLend(deviceLendId);

        List<Device> deviceList = deviceService.queryAllDevice();
        request.setAttribute("deviceList", deviceList);
        List<Teacher> teacherList = teacherService.queryAllTeacher();
        request.setAttribute("teacherList", teacherList);
        request.setAttribute("deviceLend",  deviceLend);
        return "DeviceLend/deviceLend_frontshow";
	}

	/*ajax方式显示设备借用修改jsp视图页*/
	@RequestMapping(value="/{deviceLendId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer deviceLendId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键deviceLendId获取DeviceLend对象*/
        DeviceLend deviceLend = deviceLendService.getDeviceLend(deviceLendId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonDeviceLend = deviceLend.getJsonObject();
		out.println(jsonDeviceLend.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新设备借用信息*/
	@RequestMapping(value = "/{deviceLendId}/update", method = RequestMethod.POST)
	public void update(@Validated DeviceLend deviceLend, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			deviceLendService.updateDeviceLend(deviceLend);
			message = "设备借用更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "设备借用更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除设备借用信息*/
	@RequestMapping(value="/{deviceLendId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer deviceLendId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  deviceLendService.deleteDeviceLend(deviceLendId);
	            request.setAttribute("message", "设备借用删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "设备借用删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条设备借用记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String deviceLendIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = deviceLendService.deleteDeviceLends(deviceLendIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出设备借用信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("deviceObj") Device deviceObj,@ModelAttribute("teacherObj") Teacher teacherObj,String lendTime,String returnTime, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(lendTime == null) lendTime = "";
        if(returnTime == null) returnTime = "";
        List<DeviceLend> deviceLendList = deviceLendService.queryDeviceLend(deviceObj,teacherObj,lendTime,returnTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "DeviceLend信息记录"; 
        String[] headers = { "设备借用id","借用设备","借用的老师","借用用途","借用时间","归还时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<deviceLendList.size();i++) {
        	DeviceLend deviceLend = deviceLendList.get(i); 
        	dataset.add(new String[]{deviceLend.getDeviceLendId() + "",deviceLend.getDeviceObj().getDeviceName(),deviceLend.getTeacherObj().getTeacherName(),deviceLend.getLendUse(),deviceLend.getLendTime(),deviceLend.getReturnTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"DeviceLend.xls");//filename是下载的xls的名，建议最好用英文 
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
