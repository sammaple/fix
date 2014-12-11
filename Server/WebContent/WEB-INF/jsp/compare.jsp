
<%@page import="org.apache.tomcat.util.http.Parameters"%>
<%@page import="java.util.Locale"%>
<%@page import="com.control.JspActionUtils"%>

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="../image/favicon.ico" type="image/x-icon" rel="icon">
<link href="../image/favicon.ico" type="image/x-icon" rel="shortcut icon">

<script src="../js/jquery-2.0.0.min.js"></script>

<title>对比结果</title>

<%
Integer locale = (Integer) JspActionUtils.getMagicNum(request);
%>
<title><spring:message code="login.welcome"/> </title>

<style type="text/css">
table {
	border: 1px solid #B1CDE3;
	padding: 0;
	margin: 0 0 0 0;
	border-collapse: collapse;
}

td {
	border: 1px solid #B1CDE3;
	background: #fff;
	font-size: 12px;
	padding: 3px 3px 3px 8px;
	color: #4f6b72;
}
</style>
<script type="text/javascript">

$(document).ready(function () {

	var hashmap = '${hashmap}';
	
	function stringinfo(info){
		var strbuild = "";
		strbuild += info.position+":"+info.title+":"+info.action;
		return strbuild;
	}
	
	function compareinfo(info,info2){
		var issame = true;
		if(info.position != info2.position||info.title != info2.title|| info.action != info2.action){
			issame = false;
		}
		return issame;
	}

	//$("#comparediv").append("对比结果如下:<br><br>");
	
	var mapobj = JSON.parse(hashmap);

	//$("#comparediv").append("尝试打印:"+JSON.stringify(mapobj[1][0])+"<br><br>");
	
	//$("#comparediv").append("对比个数为:"+mapobj[1][1].title+"<br><br>");
	//$("#comparediv").append("对比个数为:"+mapobj[1][1].action+"<br><br>");
	//$("#comparediv").append("对比个数为:"+mapobj[1][1].position+"<br><br>");
	//$("#comparediv").append("对比个数为:"+mapobj[1][1].comment+"<br><br>");
	//$("#comparediv").append("对比个数为:"+mapobj[1][1].board+"<br><br>");
	
	console.log(hashmap);//print to debug

	console.log(list);//print to debug
	console.log(list.length);//print to debug

	var row = document.getElementById('snapinfo_table').insertRow(-1);
	var first = row.insertCell(0);
	var sencond = row.insertCell(1);
	var result = row.insertCell(2);
	first.innerHTML = comparelist1info;
	sencond.innerHTML = comparelist2info;
	result.innerHTML = "备注说明";
	
	for ( var i = 0; i < list.length; i++) {
		var row = document.getElementById('snapinfo_table').insertRow(-1);
		
		var result_str = "PASS";
		var first = row.insertCell(0);
		var sencond = row.insertCell(1);
		var result = row.insertCell(2);
		
		//name.innerHTML = base64.decode(myObject[i].name);
		//alert(JSON.stringify(myObject[i].id));
		if(mapobj[list[i]][0].title == "@@snap@@"){
			result_str = "FAIL";
			first.innerHTML = "缺失";
		}else{
			//first.innerHTML = JSON.stringify(mapobj[list[i]][0]);
			first.innerHTML = stringinfo(mapobj[list[i]][0]);
			
		}
		
		if(mapobj[list[i]][1] == undefined){
			result_str = "FAIL";
			sencond.innerHTML = "缺失";
		}else{

			//sencond.innerHTML = JSON.stringify(mapobj[list[i]][1]);
			sencond.innerHTML = stringinfo(mapobj[list[i]][1]);
			
		}
		
		if(result_str == "PASS"){
			if(!compareinfo(mapobj[list[i]][0],mapobj[list[i]][1])){
				result_str = "FAIL";
			}
		}
		
		if(result_str == "PASS"){
			result_str = "<font size='3' color='green'>"+result_str+"</font>";
		}else{
			result_str = "<font size='3' color='red'>"+result_str+"</font>"
		}

		
		result.innerHTML = result_str;
		
		/*check.innerHTML = "<input type='checkbox' class='orange' name='"+
	 	myObject[i].id+"' onchange='checkMarkedRow(this)'>";
	 	
		op.innerHTML = "<input type='button' class='orange' name='"+
		 	myObject[i].id+"' value='删除' onclick='delMarkedRow(this)'>";*/
	  
	}
	



	$("#comparediv").append("<br><br>Owened by Jhy@@");
	//$("#comparediv").append(locale_login_str);
	
	console.log(comparelist1info);
	console.log(comparelist2info);
});


</script>
</head>
<body>

<SCRIPT>
	var locale_login_str = "<%=locale%>";
	var list = <%=request.getSession().getAttribute("comparelist") %>;
	var comparelist1info = "<%=request.getSession().getAttribute("comparelist1info")%>";
	var comparelist2info = "<%=request.getSession().getAttribute("comparelist2info")%>";
</SCRIPT>

<div id="comparediv">
		对比结果如下:<br><br>
		<table id="snapinfo_table">
			<tr>
				<td width="45%">第一列</td>
				<td width="45%">第二列</td>
				
				<td width="10%">结论</td>
			</tr>
		</table>
</div>


</body>
</html>


