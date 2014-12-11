
<%@page import="org.apache.tomcat.util.http.Parameters"%>
<%@page import="java.util.Locale"%>
<%@page import="com.control.JspActionUtils"%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
Integer locale = (Integer) JspActionUtils.getMagicNum(request);
%>
<title><spring:message code="login.welcome"/> </title>
</head>
<body>
{string:<%=locale%>}
{title:<spring:message code="login.welcome"/>}

<SCRIPT>
	var locale_login_str = "<%=locale%>";
</SCRIPT>
<br><br><br><br>请确保参数写入正确！
</body>
</html>


