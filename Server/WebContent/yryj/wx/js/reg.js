function bound() {

	if ($("#bound_mob").val() == "") {
		alert("请输入卡号/手机号");
		return;
	}
	if ($("#bound_pass").val() == "") {
		alert("请输入密码");
		return;
	}

	$.ajax({
		type : "POST",
		url : $("#boundForm").attr("action"),
		data : "openId=" + $("#openId").val() + "&mobileNumber="
				+ $("#bound_mob").val() + "&password="
				+ $.md5($("#bound_pass").val()),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"].indexOf("success") ==0){
					$("#bound_balance").html(data["errorMsg"].split("-")[1]);
					BindingSuccess();
				}else{
					alert(data["errorMsg"]);	
				}
				
			}

		}
	});
};
//发送验证码
function showCodeDiv(){	
	if ($("#reg_mob").val() == "") {
		alert("请输入手机号");
		return;
	}
	
	if (!isMobile($("#reg_mob").val())) {
		alert("请输入正确手机号");
		return;
	}
	
	if ($("#reg_pass").val() == "") {
		alert("请输入密码");
		return;
	}
	if (!isUserPassword($("#reg_pass").val()) ) {
		alert("密码为6位数字");
		return;
	}
	if (!checkComplexity($("#reg_pass").val()) ) {
		alert("密码太简单");
		return;
	}	
	
	$("#reg_sendMob").html($("#reg_mob").val().substring(0,4)+"*****"+$("#reg_mob").val().substring(8));
	
	$.ajax({
		type : "POST",
		url : "wxreg_sendCode",
		data : "mobileNumber="+ $("#reg_mob").val(),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"] =="success"){
					$("#register1").hide();
					$("#register2").show();
					verifyButton("sendCodeButton",60);
				}else{
					alert(data["errorMsg"]);	
				}
				
			}

		}
	});
}

function resendCode(){
	$("#sendCodeButton").attr("disabled","disabled");
	$.ajax({
		type : "POST",
		url : "wxreg_sendCode",
		data : "mobileNumber="+ $("#reg_mob").val(),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"] =="success"){
					verifyButton("sendCodeButton",60);	
				}else{
					$("#sendCodeButton").removeAttr("disabled");
					alert(data["errorMsg"]);	
				}
				
			}

		}
	});
}

function reg() {

	if ($("#reg_mob").val() == "") {
		alert("请输入手机号");
		return;
	}
	if (!isMobile($("#reg_mob").val())) {
		alert("请输入正确手机号");
		return;
	}
	if ($("#reg_pass").val() == "") {
		alert("请输入密码");
		return;
	}
	if (!isUserPassword($("#reg_pass").val()) ) {
		alert("密码为6位数字");
		return;
	}
	if (!checkComplexity($("#reg_pass").val()) ) {
		alert("密码太简单");
		return;
	}
	

	$.ajax({
		type : "POST",
		url : "wxreg_reg",
		data : "openId=" 
				+ $("#openId").val() + "&mobileNumber="
				+ $("#reg_mob").val() + "&password="
				+ $("#reg_pass").val()+"&verifyCode="
				+ $("#reg_code").val(),
		dataType : "json",
		success : function(data, index) {

			if (null != data["errorMsg"]) {
				if(data["errorMsg"].indexOf("success") ==0){
					//$("#bound_balance").html(data["errorMsg"].split("-")[1]);
					regSuccess();
				}else{
					alert(data["errorMsg"]);
				}
				
			}

		}
	});
};

function BindingSuccess(){
	$("#register4").show();
	$(".switchUl,#bindAccount").hide();
	}


function regSuccess(){
	$(".switchUl,#register").hide();
	$("#register3").show();
	}
function mouseDownFun(addclass){
	$(addclass).addClass("active");
}
function mouseUpFun(removeclass){
	$(removeclass).removeClass("active");
}

$(".switchUl li").on(
		"click",function(){
			$(this).addClass("active").siblings("li").removeClass("active");
			if($(".switchUl li").eq(1).hasClass("active"))
			{
				$("#register").show();
				$("#bindAccount").hide();
				}
			else if($(".switchUl li").eq(0).hasClass("active"))
			{
				$("#register").hide();
				$("#bindAccount").show();
				}
			}	
		);
