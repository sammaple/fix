<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>用户管理</title>

<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

body {
	margin: 0
}

.div_center {
	margin: 10px auto 0 auto;
	border: 0px solid red;
	width: 90%;
}

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
/* rosy */  
.rosy {  
    color: #fae7e9;  
    border: solid 1px #b73948;  
    background: #da5867;  
    background: -webkit-gradient(linear, left top, left bottom, from(#f16c7c), to(#bf404f));  
    background: -moz-linear-gradient(top,  #f16c7c,  #bf404f);  
    filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#f16c7c', endColorstr='#bf404f');  
}  
.rosy:hover {  
    background: #ba4b58;  
    background: -webkit-gradient(linear, left top, left bottom, from(#cf5d6a), to(#a53845));  
    background: -moz-linear-gradient(top,  #cf5d6a,  #a53845);  
    filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#cf5d6a', endColorstr='#a53845');  
}  
.rosy:active {  
    color: #dca4ab;  
    background: -webkit-gradient(linear, left top, left bottom, from(#bf404f), to(#f16c7c));  
    background: -moz-linear-gradient(top,  #bf404f,  #f16c7c);  
    filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#bf404f', endColorstr='#f16c7c');  
}

/* orange */  
.orange {  
    color: #fef4e9;  
    border: solid 1px #da7c0c;  
    background: #f78d1d;  
    background: -webkit-gradient(linear, left top, left bottom, from(#faa51a), to(#f47a20));  
    background: -moz-linear-gradient(top,  #faa51a,  #f47a20);  
    filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#faa51a', endColorstr='#f47a20');  
}  
.orange:hover {  
    background: #f47c20;  
    background: -webkit-gradient(linear, left top, left bottom, from(#f88e11), to(#f06015));  
    background: -moz-linear-gradient(top,  #f88e11,  #f06015);  
    filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#f88e11', endColorstr='#f06015');  
}  
.orange:active {  
    color: #fcd3a5;  
    background: -webkit-gradient(linear, left top, left bottom, from(#f47a20), to(#faa51a));  
    background: -moz-linear-gradient(top,  #f47a20,  #faa51a);  
    filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#f47a20', endColorstr='#faa51a');  
} 

</style>
<script src="/ELifeManagerServer/js/base64.js"></script>
<script src="/ELifeManagerServer/js/json.js"></script>
<script type="text/javascript">
	var startindex = 0;
	var limit = 50;
	var key = "";

	var request = false;
	try {
		request = new XMLHttpRequest();
	} catch (trymicrosoft) {
		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (othermicrosoft) {
			try {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (failed) {
				request = false;
			}
		}
	}
	
	function updateMarkedRow(tobj){
		//alert(tobj.name);
		var url = "/ELifeManagerServer/elifemanager/web_setUserForbid?userId="+tobj.name;
		{
			 request.open("POST", url, true);
			 request.onreadystatechange = getupdateMarkedRowResp;
			 request.send(null);
		}
	}
	
	//修改用户信息
	function updateUserInfo(tobj){
		//alert(tobj.name);
		//var url = "/ELifeManagerServer/elifemanager/web_updateUserInfo?userId="+tobj.name;
			 //request.open("POST", url, true);
			 //request.onreadystatechange = getupdateMarkedRowResp;
			 //request.send(null);
		
		//location.href = "/ELifeManagerServer/elifemanager/web_updateUserInfo?userId="+tobj.name;
		location.href ="newUsers.jsp?userId="+tobj.name;
	}

	function getupdateMarkedRowResp(){
	    if (request.readyState == 4) {
	        if (request.status == 200) {
	        	
	            //alert(request.responseText);
	            
	            if(request.responseText == "true"){
	                alert("操作成功请重新刷新");
	            }else{
	                alert("操作失败");
	            }
	            	
	        } else
	          alert("操作失败");
	      }
	}
	
	var base64 = new Base64();

	function getUserInfoList() {

		if(document.getElementById('usermanager_table') != null){

			var rowscount = document.getElementById('usermanager_table').rows.length;
		    //循环删除行,从最后一行往前删除
		    for (var i = rowscount - 1; i >= 1; i--) {
		    	document.getElementById('usermanager_table').deleteRow(i);
		    }
		}
		
		if (document.getElementById('key') != null) {
			key = document.getElementById('key').value;
		}

		var url = "/ELifeManagerServer/elifemanager/web_getUserInfoList?startindex=" + startindex
				+ "&limit=" + limit + "&key=" + key;
		{
			request.open("POST", encodeURI(url), true);
			request.onreadystatechange = getUserInfoListResponese;
			request.send(null);
		}
	};
	
	window.onload = getUserInfoList();


	function getUserInfoListResponese() {
		if (request.readyState == 4) {
			if (request.status == 200) {
				try {
					var myObject = JSON.parse(request.responseText);
					for ( var i = 0; i < myObject.length; i++) {
						var row = document.getElementById('usermanager_table').insertRow(-1);
						var name = row.insertCell(0);
						var logname = row.insertCell(1);
						var passwd = row.insertCell(2);
						var forbid = row.insertCell(3);
						var telenumber = row.insertCell(4);
						var comment = row.insertCell(5);
						
						var op = row.insertCell(6);
						var modify = row.insertCell(7);
						var opr = row.insertCell(8);//运营商
						
						name.innerHTML = base64.decode(myObject[i].name);
						logname.innerHTML = base64.decode(myObject[i].logname);
						passwd.innerHTML = base64.decode(myObject[i].passwd);
						forbid.innerHTML = base64.decode(myObject[i].forbid);
						telenumber.innerHTML = base64.decode(myObject[i].telenumber);
						comment.innerHTML = base64.decode(myObject[i].comment);
						
						op.innerHTML = "<input type='button' class='orange' name='"+
						 	myObject[i].userId+"' value='权限操作' onclick='updateMarkedRow(this)'>";
	            	  
						modify.innerHTML = "<input type='button' class='orange' name='"+
					 		myObject[i].userId+"' value='修改用户资料' onclick='updateUserInfo(this)'>";

					 	opr.innerHTML = base64.decode(myObject[i].op);
					}
				}
				catch (err) {
					alert(err);
				}
			} else
				alert("加载失败");
		}
	}
</script>
</head>
<body>
	<div class="div_center">
		<br /> <br /> 
		<input type="text" id="key" /> 
		<input type="button" id="search" class="rosy" onclick="getUserInfoList()" value="搜索" /> 
		<br /> <br />

		<table id="usermanager_table">
			<tr>
				<td width="10%">姓名</td>
				<td width="10%">登录名</td>
				<td width="8%">密码</td>
				<td width="10%">权限</td>
				<td width="10%">电话</td>
				<td width="20%">备注</td>
				<td width="10%">权限操作</td>
				<td width="10%">修改用户信息</td>
				<td width="10%">运营商</td>
			</tr>
		</table>

		<br /> <br /> <br />

	</div>

</body>
</html>