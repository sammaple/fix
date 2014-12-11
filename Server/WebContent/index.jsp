<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">

<link href="image/favicon.ico" type="image/x-icon" rel="icon">
<link href="image/favicon.ico" type="image/x-icon" rel="shortcut icon">

<script src="js/jquery-2.0.0.min.js"></script>

<title>LauncherSnap</title>

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
<!-- script src="/ELifeManagerServer/js/base64.js"></script>
<script src="/ELifeManagerServer/js/json.js"></script-->
<script type="text/javascript">


	var startindex = 0;
	var limit = 50;
	var key = "";
	
	var comparearray = new Array();

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
	
	function compareSnapInfoList(tobj){

		//alert(JSON.stringify(comparearray));
		
		
		var url = "/LauncherSnapServer/snap/compareMarkedRow?id="+comparearray.pop()+"&id2="+comparearray.pop();
		console.log(url);
		
		window.open(url);
		/*{
			 request.open("POST", url, true);
			 request.onreadystatechange = compareSnapInfoListResp;
			 request.send(null);
		}*/
	}

	function checkMarkedRow(tobj){
		//alert(JSON.stringify(comparearray));
		if(tobj.checked){
			if(comparearray.length==0||comparearray.indexOf(tobj.name) < 0 ){
				comparearray.push(tobj.name);
			}
		}else{
			var index = comparearray.indexOf(tobj.name);
			console.log(index);
			comparearray.splice(index, 1);
		}
		
		if(comparearray.length<=1||comparearray.length>=3){
			if(comparearray.length>=3){
				alert("一口吃不成胖纸@@");
			}
			$("#compare").attr("disabled",true);
		}else{
			$("#compare").attr("disabled",false);
		}
	}
	
	
	function delMarkedRow(tobj){
		//alert(tobj.name);
		var url = "/LauncherSnapServer/snap/delMarkedRow?id="+tobj.name;
		{
			 request.open("POST", url, true);
			 request.onreadystatechange = delMarkedRowResp;
			 request.send(null);
		}
	}
	

	function delMarkedRowResp(){
	    if (request.readyState == 4) {
	        if (request.status == 200) {
	        	
	            //alert(request.responseText);
	            
	            if(request.responseText == "true"){
	                //alert("操作成功请重新刷新@@");
	                getSnapInfoList();
	            }else{
	                alert("操作失败@@");
	            }
	            	
	        } else
	          alert("操作失败@@");
	      }
	}
	
	//var base64 = new Base64();

	function getSnapInfoList() {

		if(document.getElementById('snapinfo_table') != null){

			var rowscount = document.getElementById('snapinfo_table').rows.length;
		    //循环删除行,从最后一行往前删除
		    for (var i = rowscount - 1; i >= 1; i--) {
		    	document.getElementById('snapinfo_table').deleteRow(i);
		    }
		}
		
		if (document.getElementById('key') != null) {
			key = document.getElementById('key').value;
		}

		var url = "/LauncherSnapServer/snap/web_getSnapInfoList?id=" + key;
		{
			request.open("POST", encodeURI(url), true);
			request.onreadystatechange = getSnapInfoListResponese;
			request.send(null);
		}
	};
	
	window.onload = getSnapInfoList();


	function getSnapInfoListResponese() {
		if (request.readyState == 4) {
			if (request.status == 200) {
				comparearray.length = 0;
				try {
					var myObject = JSON.parse(request.responseText);
					for ( var i = 0; i < myObject.length; i++) {
						var row = document.getElementById('snapinfo_table').insertRow(-1);
						var check = row.insertCell(0);
						var systemid = row.insertCell(1);
						var id = row.insertCell(2);
						var board = row.insertCell(3);
						var time = row.insertCell(4);
						var from = row.insertCell(5);
						var others = row.insertCell(6);
						var op = row.insertCell(7);
						
						//name.innerHTML = base64.decode(myObject[i].name);
						//alert(JSON.stringify(myObject[i].id));
						systemid.innerHTML = JSON.stringify(myObject[i].id);
						id.innerHTML = myObject[i].compareid;
						from.innerHTML = myObject[i].from;
						board.innerHTML = myObject[i].board;
						time.innerHTML = myObject[i].datetime;
						others.innerHTML = myObject[i].comment;
						
						check.innerHTML = "<input type='checkbox' class='orange' name='"+
					 	myObject[i].id+"' onchange='checkMarkedRow(this)'>";
					 	
						op.innerHTML = "<input type='button' class='orange' name='"+
						 	myObject[i].id+"' value='删除' onclick='delMarkedRow(this)'>";
	            	  

					 	//opr.innerHTML = base64.decode(myObject[i].op);
					}
				}
				catch (err) {
					alert(err);
				}
			} else
				alert("加载失败");
		}
	}
	

	$(document).ready(function () {

		$("#compare").attr("disabled",true);
	})
</script>
</head>
<body>
	<div class="div_center">
		<br /> <br /> 
		输入用户id查询@@
		<input type="text" id="key" value="j1"/> 
		<input type="button" id="search" class="rosy" onclick="getSnapInfoList()" value="查找" /> 
		   <br />
		（在用户id输入框 可以使用j1来进行本系统的试用 ）
		<br /> <br />
		说明:
		<br />
		   系统id:系统自动分配的唯一ID
		   <br />
		 用户id:类似于个人账号，用于区分操作者（在Web以及终端都有输入框，建议以个人的姓名缩写加数字区分，比如张三 用户id可以为zs13）
		<br /> <br />
		<input type="button" id="compare" class="rosy" onclick="compareSnapInfoList()" value="比较" /> 
                             只支持2个对象进行比较
		<table id="snapinfo_table">
			<tr>
				<td width="5%">check</td>
				<td width="10%">系统id@@</td>
				<td width="10%">用户id@@</td>
				<td width="10%">板型</td>
				<td width="10%">时间</td>
				<td width="10%">来源</td>
				<td width="20%">备注</td>
				<td width="10%">权限操作</td>
			</tr>
		</table>

		<br /> <br /> <br />

	</div>

</body>
</html>