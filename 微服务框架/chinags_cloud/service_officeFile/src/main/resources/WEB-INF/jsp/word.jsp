<%@ page language="java" import="java.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
//设置服务器页面
poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
//打开Word文档
String fileType = request.getParameter("fileType");//用request得到
String filePath = request.getParameter("filePath");//用request得到
String filePaths = filePath;
if(filePath!=null) {
    filePaths = URLEncoder.encode(filePath);
}
//设置保存页面
poCtrl.setSaveFilePage("http://127.0.0.3:9999/office/save?filePath="+filePaths);
if(fileType!=null){
    if(fileType.equals("doc")){
        poCtrl.webOpen(filePath ,OpenModeType.docNormalEdit,"JDDS");
    }else if(fileType.equals("xls")){
        poCtrl.webOpen(filePath ,OpenModeType.xlsNormalEdit,"JDDS");
    }else if(fileType.equals("ppt")){
        poCtrl.webOpen(filePath ,OpenModeType.pptNormalEdit,"JDDS");
    }
}else{
    poCtrl.webOpen(filePath ,OpenModeType.docNormalEdit,"JDDS");
}
    String pageOfficeCtrl1 = poCtrl.getHtmlCode("PageOfficeCtrl1");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'word.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <div style="height:850px;width:1036px;overflow:hidden;">
        <%=pageOfficeCtrl1%>
	</div>
  </body>
</html>
