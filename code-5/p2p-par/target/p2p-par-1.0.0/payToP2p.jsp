<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/2
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="${par_p2p_return_url}">
    //接收查询接口的参数值【验签结果，map集合】
    <input type="hidden" name="signVerified" value="${signVerified}"/>
    <c:choose>
        <c:when test="${not empty params}">
        <c:forEach items="${params}" var="params">
            <input type="hidden" name="${params.key}" value="${params.value}" />
        </c:forEach>
        </c:when>
    </c:choose>
</form>
<script>document.forms[0].submit();</script>
</body>
</html>
