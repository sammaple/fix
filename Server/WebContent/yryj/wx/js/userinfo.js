function findUserInfo(){
	$.ajax({
		type : "POST",
		url : "wxuser_findUserBaseInfo",
		data : null,
		dataType : "json",
		success : function(data, index) {
			var userInfo = data.userInfo;
			$("#realName").html(userInfo.realName);
			$("#mobileNumber").html(userInfo.mobileNumber);
			$("#email").html(userInfo.email);
			$("#address").html(userInfo.address);
			$("#gender").html(userInfo.gender==2?"女":"男");
			$("#gender_select").val(userInfo.gender);
		}
	});	
}


function editMobile(){
	if($("#verifyCode").val() ==""){
	alert("请输入验证码");
	return;
	}
	$.ajax({
		type : "POST",
		url : "wxuser_ediMobile",
		data : "verifyCode="+$("#verifyCode").val(),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"] == "true"){
					window.top.location.href=$("#basePath").val()+"wx/userInfo/info.jsp";
					
				}else{
					alert(data["errorMsg"]);	
					$("#sendSMSButton").removeAttr("disabled");
				}
				
			}

		}
	});
	//window.top.location.href=$("#basePath").val()+"";
}

function sendSMS(){
	if($("#mobileNumber").val()==""){
		alert("请输入原手机号");
		return;
		
	}
	if($("#newMobileNumber").val()==""){
		alert("请输入新手机号");
		return;
		
	}
	
	$("#sendSMSButton").attr("disabled","disabled");
	$.ajax({
		type : "POST",
		url : "wxuser_sendSMS",
		data : "userInfo.newMobileNumber="+$("#newMobileNumber").val()+"&userInfo.mobileNumber="+$("#mobileNumber").val(),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"] == "true"){
					verifyButton("sendSMSButton",120);
				}else{
					alert(data["errorMsg"]);	
					$("#sendSMSButton").removeAttr("disabled");
				}
				
			}

		}
	});
}

function editEmail(){
	if($("#verifyCode").val() ==""){
		alert("请输入验证码");
		return;
		}
	$.ajax({
		type : "POST",
		url : "wxuser_ediEmail",
		data : "verifyCode="+$("#verifyCode").val(),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"] == "true"){
					window.top.location.href=$("#basePath").val()+"wx/userInfo/info.jsp";
					
				}else{
					alert(data["errorMsg"]);	
					$("#sendSMSButton").removeAttr("disabled");
				}
				
			}

		}
	});
}
function sendEmailCode(){
	if($("#email").val()==""){
		alert("请输入原邮箱");
		return;
		
	}
	if($("#newEmail").val()==""){
		alert("请输入新邮箱");
		return;
		
	}
	
	$("#sendSMSButton").attr("disabled","disabled");
	$.ajax({
		type : "POST",
		url : "wxuser_sendEmailCode",
		data : "userInfo.newEmail="+$("#newEmail").val()+"&userInfo.email="+$("#email").val(),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"] == "true"){
					verifyButton("sendSMSButton",60);
				}else{
					alert(data["errorMsg"]);	
					$("#sendSMSButton").removeAttr("disabled");
				}
				
			}

		}
	});
}

function editGender(gender){
	$.ajax({
		type : "POST",
		url : "wxuser_ediGender",
		data : "userInfo.gender="+gender,
		dataType : "json",
		success : function(data, index) {

		}
	});
}

function editPassword(){
	if($("#passWord").val() ==""){
		alert("请输入新密码");
		$("#passWord").focus();
		return;
	}
	if($("#passWord").val() !=$("#repassWord").val()){
		alert("两次密码不一致");
		$("#repassWord").focus();
		return;
	}
	
	if(!checkComplexity($("#passWord").val())){		
		alert("为了提高您账号的安全，请勿使用过于简单的密码");
		$("#passWord").focus();
		return;
	}
	$.ajax({
		type : "POST",
		url : "wxuser_editPassword",
		data : "userInfo.passWord="+$("#passWord").val(),
		dataType : "json",
		success : function(data, index) {
			if(data["errorMsg"] == "true"){
				alert("密码修改成功");
				window.top.location.href=$("#basePath").val()+"wx/userInfo/info.jsp";
			}else{
				alert(data["errorMsg"]);	
			}
		}
	});
}


