<%@ page language="java" import="java.util.*" pageEncoding="Utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  </head>
  <script src="/public/js/jquery.1.3.2.min.js"></script>
  <script>
  $(function(){
  	console.info("you are ture");
  	
  	$(".delbutton").click(function(){
  		console.info($(this).parent().parent().find(".yunFileId").val());
  	});
  	
  });
  function checkProcessInstanceDetail(){
  	console.info($(this).attr("class"));	
  }
  </script>
  <body>
    This is my JSP page. <br>

    <a href="/appBorrowQual/findAssociateQualBorrowProcessInstance/oagT60OGJTufBpWhbBjhrYBzVxKE/qualBorrow">测试启动流程</a>
  </body>
</html>
