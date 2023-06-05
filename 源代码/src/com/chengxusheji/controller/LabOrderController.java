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
import com.chengxusheji.service.LabOrderService;
import com.chengxusheji.po.LabOrder;
import com.chengxusheji.service.LabInfoService;
import com.chengxusheji.po.LabInfo;
import com.chengxusheji.service.TeacherService;
import com.chengxusheji.po.Teacher;

//LabOrder管理控制层
@Controller
@RequestMapping("/LabOrder")
public class LabOrderController extends BaseController {

    /*业务层对象*/
    @Resource LabOrderService labOrderService;

    @Resource LabInfoService labInfoService;
    @Resource TeacherService teacherService;
	@InitBinder("labObj")
	public void initBinderlabObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("labObj.");
	}
	@InitBinder("teacherObj")
	public void initBinderteacherObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("teacherObj.");
	}
	@InitBinder("labOrder")
	public void initBinderLabOrder(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("labOrder.");
	}
	/*跳转到添加LabOrder视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new LabOrder());
		/*查询所有的LabInfo信息*/
		List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
		request.setAttribute("labInfoList", labInfoList);
		/*查询所有的Teacher信息*/
		List<Teacher> teacherList = teacherService.queryAllTeacher();
		request.setAttribute("teacherList", teacherList);
		return "LabOrder_add";
	}

	/*客户端ajax方式提交添加实验室预约信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated LabOrder labOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        labOrderService.addLabOrder(labOrder);
        message = "实验室预约添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	

	/*客户端ajax方式提交添加实验室预约信息*/
	@RequestMapping(value = "/teacherAdd", method = RequestMethod.POST)
	public void teacherAdd(LabOrder labOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		 
		Teacher teacher = new Teacher();
		teacher.setTeacherNo(session.getAttribute("teacherNo").toString());
		labOrder.setTeacherObj(teacher);
		labOrder.setShenHeState("待审核");
		labOrder.setReply("--");
		
        labOrderService.addLabOrder(labOrder);
        message = "实验室预约添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	/*ajax方式按照查询条件分页查询实验室预约信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("labObj") LabInfo labObj,@ModelAttribute("teacherObj") Teacher teacherObj,String orderDate,String shenHeState,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (orderDate == null) orderDate = "";
		if (shenHeState == null) shenHeState = "";
		if(rows != 0)labOrderService.setRows(rows);
		List<LabOrder> labOrderList = labOrderService.queryLabOrder(labObj, teacherObj, orderDate, shenHeState, page);
	    /*计算总的页数和总的记录数*/
	    labOrderService.queryTotalPageAndRecordNumber(labObj, teacherObj, orderDate, shenHeState);
	    /*获取到总的页码数目*/
	    int totalPage = labOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = labOrderService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(LabOrder labOrder:labOrderList) {
			JSONObject jsonLabOrder = labOrder.getJsonObject();
			jsonArray.put(jsonLabOrder);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询实验室预约信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<LabOrder> labOrderList = labOrderService.queryAllLabOrder();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(LabOrder labOrder:labOrderList) {
			JSONObject jsonLabOrder = new JSONObject();
			jsonLabOrder.accumulate("orderId", labOrder.getOrderId());
			jsonArray.put(jsonLabOrder);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询实验室预约信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("labObj") LabInfo labObj,@ModelAttribute("teacherObj") Teacher teacherObj,String orderDate,String shenHeState,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (orderDate == null) orderDate = "";
		if (shenHeState == null) shenHeState = "";
		List<LabOrder> labOrderList = labOrderService.queryLabOrder(labObj, teacherObj, orderDate, shenHeState, currentPage);
	    /*计算总的页数和总的记录数*/
	    labOrderService.queryTotalPageAndRecordNumber(labObj, teacherObj, orderDate, shenHeState);
	    /*获取到总的页码数目*/
	    int totalPage = labOrderService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = labOrderService.getRecordNumber();
	    request.setAttribute("labOrderList",  labOrderList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("labObj", labObj);
	    request.setAttribute("teacherObj", teacherObj);
	    request.setAttribute("orderDate", orderDate);
	    request.setAttribute("shenHeState", shenHeState);
	    List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
	    request.setAttribute("labInfoList", labInfoList);
	    List<Teacher> teacherList = teacherService.queryAllTeacher();
	    request.setAttribute("teacherList", teacherList);
		return "LabOrder/labOrder_frontquery_result"; 
	}

     /*前台查询LabOrder信息*/
	@RequestMapping(value="/{orderId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer orderId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键orderId获取LabOrder对象*/
        LabOrder labOrder = labOrderService.getLabOrder(orderId);

        List<LabInfo> labInfoList = labInfoService.queryAllLabInfo();
        request.setAttribute("labInfoList", labInfoList);
        List<Teacher> teacherList = teacherService.queryAllTeacher();
        request.setAttribute("teacherList", teacherList);
        request.setAttribute("labOrder",  labOrder);
        return "LabOrder/labOrder_frontshow";
	}

	/*ajax方式显示实验室预约修改jsp视图页*/
	@RequestMapping(value="/{orderId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer orderId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键orderId获取LabOrder对象*/
        LabOrder labOrder = labOrderService.getLabOrder(orderId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonLabOrder = labOrder.getJsonObject();
		out.println(jsonLabOrder.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新实验室预约信息*/
	@RequestMapping(value = "/{orderId}/update", method = RequestMethod.POST)
	public void update(@Validated LabOrder labOrder, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			labOrderService.updateLabOrder(labOrder);
			message = "实验室预约更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "实验室预约更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除实验室预约信息*/
	@RequestMapping(value="/{orderId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer orderId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  labOrderService.deleteLabOrder(orderId);
	            request.setAttribute("message", "实验室预约删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "实验室预约删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条实验室预约记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String orderIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = labOrderService.deleteLabOrders(orderIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出实验室预约信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("labObj") LabInfo labObj,@ModelAttribute("teacherObj") Teacher teacherObj,String orderDate,String shenHeState, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(orderDate == null) orderDate = "";
        if(shenHeState == null) shenHeState = "";
        List<LabOrder> labOrderList = labOrderService.queryLabOrder(labObj,teacherObj,orderDate,shenHeState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "LabOrder信息记录"; 
        String[] headers = { "预约编号","预约实验室","预约的老师","预约日期","预约时间","预约用途","审核状态"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<labOrderList.size();i++) {
        	LabOrder labOrder = labOrderList.get(i); 
        	dataset.add(new String[]{labOrder.getOrderId() + "",labOrder.getLabObj().getLabName(),labOrder.getTeacherObj().getTeacherName(),labOrder.getOrderDate(),labOrder.getOrderTime(),labOrder.getPurpose(),labOrder.getShenHeState()});
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
			response.setHeader("Content-disposition","attachment; filename="+"LabOrder.xls");//filename是下载的xls的名，建议最好用英文 
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
