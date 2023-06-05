<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.LabInfo" %>
<%@ page import="com.chengxusheji.po.LabClass" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<LabInfo> labInfoList = (List<LabInfo>)request.getAttribute("labInfoList");
    //获取所有的labClassObj信息
    List<LabClass> labClassList = (List<LabClass>)request.getAttribute("labClassList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String labNumber = (String)request.getAttribute("labNumber"); //实验室编号查询关键字
    String labName = (String)request.getAttribute("labName"); //实验室名称查询关键字
    LabClass labClassObj = (LabClass)request.getAttribute("labClassObj");
    String labState = (String)request.getAttribute("labState"); //实验室状态查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>实验室查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>LabInfo/frontlist">实验室信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>LabInfo/labInfo_frontAdd.jsp" style="display:none;">添加实验室</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<labInfoList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		LabInfo labInfo = labInfoList.get(i); //获取到实验室对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>LabInfo/<%=labInfo.getLabNumber() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=labInfo.getLabPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		实验室编号:<%=labInfo.getLabNumber() %>
			     	</div>
			     	<div class="field">
	            		实验室名称:<%=labInfo.getLabName() %>
			     	</div>
			     	<div class="field">
	            		实验室类别:<%=labInfo.getLabClassObj().getClassName() %>
			     	</div>
			     	<div class="field">
	            		实验室面积:<%=labInfo.getLabArea() %>
			     	</div>
			     	<div class="field">
	            		实验室地址:<%=labInfo.getLabAddress() %>
			     	</div>
			     	<div class="field">
	            		实验室状态:<%=labInfo.getLabState() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>LabInfo/<%=labInfo.getLabNumber() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="labInfoEdit('<%=labInfo.getLabNumber() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="labInfoDelete('<%=labInfo.getLabNumber() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>实验室查询</h1>
		</div>
		<form name="labInfoQueryForm" id="labInfoQueryForm" action="<%=basePath %>LabInfo/frontlist" class="mar_t15">
			<div class="form-group">
				<label for="labNumber">实验室编号:</label>
				<input type="text" id="labNumber" name="labNumber" value="<%=labNumber %>" class="form-control" placeholder="请输入实验室编号">
			</div>
			<div class="form-group">
				<label for="labName">实验室名称:</label>
				<input type="text" id="labName" name="labName" value="<%=labName %>" class="form-control" placeholder="请输入实验室名称">
			</div>
            <div class="form-group">
            	<label for="labClassObj_classId">实验室类别：</label>
                <select id="labClassObj_classId" name="labClassObj.classId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(LabClass labClassTemp:labClassList) {
	 					String selected = "";
 					if(labClassObj!=null && labClassObj.getClassId()!=null && labClassObj.getClassId().intValue()==labClassTemp.getClassId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=labClassTemp.getClassId() %>" <%=selected %>><%=labClassTemp.getClassName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="labState">实验室状态:</label>
				<input type="text" id="labState" name="labState" value="<%=labState %>" class="form-control" placeholder="请输入实验室状态">
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="labInfoEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;实验室信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="labInfoEditForm" id="labInfoEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="labInfo_labNumber_edit" class="col-md-3 text-right">实验室编号:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="labInfo_labNumber_edit" name="labInfo.labNumber" class="form-control" placeholder="请输入实验室编号" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="labInfo_labName_edit" class="col-md-3 text-right">实验室名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="labInfo_labName_edit" name="labInfo.labName" class="form-control" placeholder="请输入实验室名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="labInfo_labClassObj_classId_edit" class="col-md-3 text-right">实验室类别:</label>
		  	 <div class="col-md-9">
			    <select id="labInfo_labClassObj_classId_edit" name="labInfo.labClassObj.classId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="labInfo_labArea_edit" class="col-md-3 text-right">实验室面积:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="labInfo_labArea_edit" name="labInfo.labArea" class="form-control" placeholder="请输入实验室面积">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="labInfo_labPhoto_edit" class="col-md-3 text-right">实验室图片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="labInfo_labPhotoImg" border="0px"/><br/>
			    <input type="hidden" id="labInfo_labPhoto" name="labInfo.labPhoto"/>
			    <input id="labPhotoFile" name="labPhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="labInfo_labAddress_edit" class="col-md-3 text-right">实验室地址:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="labInfo_labAddress_edit" name="labInfo.labAddress" class="form-control" placeholder="请输入实验室地址">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="labInfo_labState_edit" class="col-md-3 text-right">实验室状态:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="labInfo_labState_edit" name="labInfo.labState" class="form-control" placeholder="请输入实验室状态">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="labInfo_labDesc_edit" class="col-md-3 text-right">实验室介绍:</label>
		  	 <div class="col-md-9">
			 	<textarea name="labInfo.labDesc" id="labInfo_labDesc_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#labInfoEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxLabInfoModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script>
//实例化编辑器
var labInfo_labDesc_edit = UE.getEditor('labInfo_labDesc_edit'); //实验室介绍编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.labInfoQueryForm.currentPage.value = currentPage;
    document.labInfoQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.labInfoQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.labInfoQueryForm.currentPage.value = pageValue;
    documentlabInfoQueryForm.submit();
}

/*弹出修改实验室界面并初始化数据*/
function labInfoEdit(labNumber) {
	$.ajax({
		url :  basePath + "LabInfo/" + labNumber + "/update",
		type : "get",
		dataType: "json",
		success : function (labInfo, response, status) {
			if (labInfo) {
				$("#labInfo_labNumber_edit").val(labInfo.labNumber);
				$("#labInfo_labName_edit").val(labInfo.labName);
				$.ajax({
					url: basePath + "LabClass/listAll",
					type: "get",
					success: function(labClasss,response,status) { 
						$("#labInfo_labClassObj_classId_edit").empty();
						var html="";
		        		$(labClasss).each(function(i,labClass){
		        			html += "<option value='" + labClass.classId + "'>" + labClass.className + "</option>";
		        		});
		        		$("#labInfo_labClassObj_classId_edit").html(html);
		        		$("#labInfo_labClassObj_classId_edit").val(labInfo.labClassObjPri);
					}
				});
				$("#labInfo_labArea_edit").val(labInfo.labArea);
				$("#labInfo_labPhoto").val(labInfo.labPhoto);
				$("#labInfo_labPhotoImg").attr("src", basePath +　labInfo.labPhoto);
				$("#labInfo_labAddress_edit").val(labInfo.labAddress);
				$("#labInfo_labState_edit").val(labInfo.labState);
				labInfo_labDesc_edit.setContent(labInfo.labDesc, false);
				$('#labInfoEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除实验室信息*/
function labInfoDelete(labNumber) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "LabInfo/deletes",
			data : {
				labNumbers : labNumber,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#labInfoQueryForm").submit();
					//location.href= basePath + "LabInfo/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交实验室信息表单给服务器端修改*/
function ajaxLabInfoModify() {
	$.ajax({
		url :  basePath + "LabInfo/" + $("#labInfo_labNumber_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#labInfoEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#labInfoQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

})
</script>
</body>
</html>

