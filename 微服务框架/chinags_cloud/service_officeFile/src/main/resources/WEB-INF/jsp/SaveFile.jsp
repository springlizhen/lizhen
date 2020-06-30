<%@ page language="java" import="java.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="UTF-8"%>
<%
String filePath = request.getParameter("filePath");//用request得到
    System.out.println(filePath);
FileSaver fs=new FileSaver(request,response);
fs.saveToFile(filePath);
fs.close();
%>

