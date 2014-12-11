/**   
 * browseFolder.js   
 * 该文件定义了BrowseFolder()函数，它将提供一个文件夹选择对话框   
 * 以供用户实现对系统文件夹选择的功能   
 * 文件夹选择对话框起始目录由   
 * Shell.BrowseForFolder(WINDOW_HANDLE, Message, OPTIONS, strPath)函数   
 * 的strPath参数设置   
 * 例如：0x11--我的电脑   
 *   0 --桌面   
 *  "c:\\"--系统C盘   
 *    
 * 用如下代码把该函数应用到一个HTML文件中：   
 *  <script src="browseFolder.js" tpa=http://www.kuanter.com/wx/js/browseFolder.js></script>   
 * 或把下面代码直接COPY到<script language="javascript">...</script>标签中；   
 *   
 * 用一般的触发函数均可在HTML文件中使用该函数   
 * 例如：<input type="button" onclick="BrowseFolder()" />   
 * 注意：请定义NAME为savePath的输入框接收或显示返回的值，例如：   
 *    <input type="text" name="savePath" />    
 *   
 * 特别注意的是,由于安全方面的问题,你还需要如下设置才能使本JS代码正确运行,   
 * 否者会出现"没有权限"的问题.   
 *   
 * 1、设置可信任站点（例如本地的可以为：http://localhost）    
 * 2、其次：可信任站点安全级别自定义设置中：设置下面的选项    
 * "对没有标记为安全的ActiveX控件进行初始化和脚本运行"----"启用"     
 */  


function BrowseFolder(){    
 try{    
  var Message = "请选择文件夹";  //选择框提示信息    
  var Shell = new ActiveXObject( "Shell.Application" );    
  var Folder = Shell.BrowseForFolder(0,Message,0x4000,0x11);//起始目录为：我的电脑    
 // var Folder = Shell.BrowseForFolder(0,Message,0x0040,0x11);//起始目录为：我的电脑    
    //var Folder = Shell.BrowseForFolder(0,Message,0); //起始目录为：桌面    
  if(Folder != null){    
    Folder = Folder.items();  // 返回 FolderItems 对象    
    Folder = Folder.item();  // 返回 Folderitem 对象    
    Folder = Folder.Path;   // 返回路径    
  // if(Folder.charAt(Folder.length-1) != "\\"){    
   //   Folder = Folder + "\\";    
  //  }    
   // alert(Folder);
    document.all.savePath.innerText = Folder;    
    document.all.savePath.value = Folder;    
    //document.getElementById( "lbl ").innerText   =   "Hello   world "; 
    ///document.getElementById("savePath").innerText   =   "Hello   world "; 

    return Folder;    
  }    
 }catch(e){     
  alert(e.message);    
 }    
}   

/**
 * js Map 实现
 * 
 * @returns {Map}
 */
function Map() {
	this.keys = [];
	this.data = {};

	/**
	 * 放入一个键值对
	 * 
	 * @param {String}
	 *            key
	 * @param {Object}
	 *            value
	 */
	this.put = function(key, value) {
		if (this.data[key] == null) {
			this.keys.push(key);
		}
		this.data[key] = value;
	};

	/**
	 * 获取某键对应的值
	 * 
	 * @param {String}
	 *            key
	 * @return {Object} value
	 */
	this.get = function(key) {
		return this.data[key];
	};

	/**
	 * 是否包含该键
	 */
	this.contain = function(keyValue) {

		var value = this.keys[keyValue];
		if (value)
			return true;
		else
			return false;
	};

	/**
	 * 删除一个键值对
	 * 
	 * @param {String}
	 *            key
	 */
	this.remove = function(key) {
		for (var index = 0;index < this.keys.length; index++) {
			if (this.keys[index] == key) {
				this.keys.splice(index, 1);
				break;
			}
		}
		// this.keys.remove(key);
		this.data[key] = null;
	};

	/**
	 * 遍历Map,执行处理函数
	 * 
	 * @param {Function}
	 *            回调函数 function(key,value,index){..}
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		var len = this.keys.length;
		for (var i = 0;i < len; i++) {
			var k = this.keys[i];
			fn(k, this.data[k], i);
		}
	};

	/**
	 * 获取键值数组(类似Java的entrySet())
	 * 
	 * @return 键值对象{key,value}的数组
	 */
	this.entrys = function() {
		var len = this.keys.length;
		var entrys = new Array(len);
		for (var i = 0;i < len; i++) {
			entrys[i] = {
				key : this.keys[i],
				value : this.data[i]
			};
		}
		return entrys;
	};
	
	/**
	 * 清空Map
	 */
	this.clear = function(){
		this.keys = [];
		this.data = {};
	};

	/**
	 * 判断Map是否为空
	 */
	this.isEmpty = function() {
		return this.keys.length == 0;
	};

	/**
	 * 获取键值对数量
	 */
	this.size = function() {
		return this.keys.length;
	};

	/**
	 * 重写toString
	 */
	this.toString = function() {
		var s = "{";
		for (var i = 0;i < this.keys.length; i++, s += ',') {
			var k = this.keys[i];
			s += k + "=" + this.data[k];
		}
		s += "}";
		return s;
	};
	//获取Map中所有value的数组（array） 
	this.values = function() { 
		var arr = new Array(); 
		for (var i = 0; i < this.keys.length; i++) {   
			arr.push(this.get( this.keys[i])); 
		} 
	return arr; 
	}; 
}
/**
 * js Set实现
 */

function Set(){
	this.map =new Map();
	/**
	 * 增加
	 */
	this.put = function(value){
		this.map.put(value, "");
	};
	/**
	 * 删除一个值
	 * 
	 * @param {String}
	 *            key
	 */
	this.remove = function(key) {
		this.map.remove(key);
	};
	/**
	 * 清空Set
	 */
	this.clear = function(){
		this.map = new Map();
	};
	/**
	 * 遍历Set,执行处理函数
	 * 
	 * @param {Function}
	 *            回调函数 function(key,index){..}
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		for (var i = 0;i < this.map.size(); i++) {
			fn(this.map.keys[i], i);
		}
	};
	/**
	 * 转为数组
	 */
	this.toArray=function(){
		return this.map.keys;	
	};
	/**
	 * 重写toString
	 * separator 分隔符
	 */
	this.toString = function(separator) {
		if(typeof separator =="undefined"){
			separator="";
		}
		var s = "";
		this.each(function(val){
			s+=val+separator;
		});
		return s;
	};
	/**
	 * 获Set长度
	 */
	this.size = function() {
		return this.map.size();
	};
	
}
/**
 * js实现list
 * 
 */
function List() {

	this.value = [];

	/* 添加 */
	this.add = function(obj) {
		return this.value.push(obj);
	};

	/* 大小 */
	this.size = function() {
		return this.value.length;
	};

	/* 返回指定索引的值 */
	this.get = function(index) {
		return this.value[index];
	};

	/* 删除指定索引的值 */
	this.remove = function(index) {
		this.value1 = [];
		this.value[index] = '';
		for (var i = 0;i < this.size(); i++) {
			if (this.value[i] != '') {
				this.value1.push(this.value[i]);
			}
		}
		this.value = this.value1;
		delete this.value1;
		return this.value;
	};
	
	this.removeObject = function(object){
		this.value1 = [];
		for(var i = 0; i < this.size(); i++){
			if(this.value[i] != object){
				this.value1.push(this.value[i]);
			}
		}
		this.value = this.value1;
		delete this.value1;
		return this.value;
	};

	/* 删除全部值 */
	this.removeAll = function() {
		return this.value = [];
	};

	/* 是否包含某个对象 */
	this.constains = function(obj) {
		for (var i in this.value) {
			if (obj == this.value[i]) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	};

}
function clone(obj){
    var objClone;
    if (obj.constructor == Object){
        objClone = new obj.constructor();
    }else{
        objClone = new obj.constructor(obj.valueOf());
    }
    for(var key in obj){
        if ( objClone[key] != obj[key] ){
            if ( typeof(obj[key]) == "object" ){
                objClone[key] = clone(obj[key]);
            }else{
                objClone[key] = obj[key];
            }
        }
    }
    objClone.toString = obj.toString;
    objClone.valueOf = obj.valueOf;
    return objClone;
}
//附带不用修改浏览器安全配置的javascript代码，兼容ie， firefox全系列   

function getPath(obj)     
{     
	alert(window.navigator.userAgent);
  if(obj)     
    {     
    
    if (window.navigator.userAgent.indexOf("MSIE")>=1)     
      {    
    	//alert("MSIE");
        
        obj.select();     
        //alert(obj.value);
         //alert(document.selection.createRange().text);
    	
     // return document.selection.createRange().text;     上面这个也可以。
        return obj.value;
      }     
    
    else if(window.navigator.userAgent.indexOf("Firefox")>=1)     
      {     
    	 alert("Firefox");
    	
      if(obj.files)     
        {     
    
    	  
        return obj.files.item(0).getAsDataURL();     
        }     
      return obj.value;     
      }     
     
    return obj.value;     
    }     
}     
//参数obj为input file对象  

function excelutil(filename){
 
//var filename = $("#savePath1").val(); 
 // var filename = document.all.savePath1.value;
  if(filename=="")
	  {
	  alert("请先选择excel文件");
	  return;
	  }
  var file_type = filename.substring(filename.lastIndexOf(".")+1);  
  if(file_type!="xls"&&file_type!="xlsx")
	  {
	  alert("您所选择的文件不是excel文件");
	  return;
	  }
  	var oXL = new ActiveXObject("Excel.Application");    
//	var oWB = oXL.Workbooks.open("C:\\Users\\yh\\Desktop\\网点.xls");    
	var oWB = oXL.Workbooks.open(filename);    
 
	oWB.worksheets(1).select();  
	var oSheet = oWB.ActiveSheet;  
	var colcount=oWB.Worksheets(1).UsedRange.Cells.Rows.Count;  
	var colcolumn=oWB.Worksheets(1).UsedRange.Columns.Count;  
	 
	try  
	{  
	var htmltemp = "";
	for (var j=1;j<=colcount;j++)  
	{  
	    for (var i=1;i<=colcolumn;i++)  
	    {  
	    
	    htmltemp+=oSheet.Cells(j,i).value + ",";
	  //       document.all.excelshow.write(oSheet.Cells(j,i).value + ",");  
	    	 
	  	    }  
	  	     htmltemp+="<br/>";
	  //  document.all.excelshow.write("<br/>");  
	  }
	//  alert(htmltemp);
	  document.all.excelshow.innerHTML=htmltemp;  
	}  
	catch(e)  
	{  
	    oWB.close();  
	}  
	oWB.close(); 
	  }

//+++++++++++发送验证按钮较果begin+++++++++++
var secs = 180;
var wait = secs * 1000;
var verifyTimeOut=[];
function getVerifyCode() {
	verifyTimeOut=[];
	for (var i = 1; i <= secs; i++) {
		verifyTimeOut.push(window.setTimeout("doUpdate(" + i + ")", i * 1000));
	}
	verifyTimeOut.push(window.setTimeout("Timer()", wait));
}
function doUpdate(num) {
	if (num == '180') {
		$.ajax({
	        type:"POST",
	        url:"frontPageAction_randomSessioninvalidate",
	        async:false,
	        dataType:"json",
	        success: function(data,index){									
	        }
	    });	
		$("#getVerifyCodeButton").val(" 重新发送 ");
	} else {
		document.getElementById("getVerifyCodeButton").disabled = true;
		wut = secs - num;
		$("#getVerifyCodeButton").val(" 正在发送" + wut + "..");
	}
}
function Timer() {
	$("#getVerifyCodeButton").removeAttr("disabled").removeClass("disable");
}
// +++++++++++发送验证按钮较果end+++++++++++


function lockButton(obj){
	var o=$(obj);
	if($("#lockButton").size()==0){
		$("body").append('<div id="lockButton" style="z-index: 1000;position: absolute;"></div>');
	};
	$("#lockButton").css("left",o.offset().left+ "px"); 
	$("#lockButton").css("top",o.offset().top+ "px");	
	$("#lockButton").css("width",o.width()+ "px");
	$("#lockButton").css("height",o.height()+ "px");
	$("#lockButton").css("display","block");
}

function unLockButton(){
	$("#lockButton").css("display","none");
}


/********************校验数据***************************/

/**
 * 用户密码(6为数字)
 */
function isUserPassword(str){
	return (/^[0-9]{6}$/.test($.trim(str)));	
}

/**
 * 校验座机号码
 */
 function isPhone(str) {
	return (/^(0[0-9]{2}[0-9]{8})|(0[0-9]{3}[0-9]{8})|(0[0-9]{3}[0-9]{7})$/.test($.trim(str)));
}

/**
 * 校验手机号码
 */
 function isMobile(str) {
	return (/^1[3|4|5|8][0-9]{9}$/.test($.trim(str)));
}

/**
 * 校验邮箱地址
 */
function isEmail(str) {
	return (/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]{1,3}$/.test($.trim(str)));
}

/**
 * 校验卡号(8为数字)
 */
function isCardNumber(str) {
	return (/^[0-9]{8}$/.test($.trim(str)));
}
/**
 * 简单校验身份证号码，包括15位和18位
 */
 function isIDCard(str) {
	return (/(^\d{15}$)|(^\d{17}([0-9]|X|x)$)/.test($.trim(str)));//身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。 

}
 /**
  * 验证中文
  * @returns {Boolean}
  */
 function isChinese(str){
	return !(/[^\u4e00-\u9fa5]/.test($.trim(str)));
	
	 }
 /**
  *验证字母
  * @returns
  */
 function isLetter(str){
	 return (/^[a-zA-Z]+$/.test($.trim(str))); 
 }
 /**
  * 验证正整数
  * @returns
  */
 function isPositiveInteger(str){
	 return !isNaN($.trim(str)); 
 }
 /**
  * 验证车牌号
  * @returns
  */
 function isLicensePlateNumber(str){
	 return (/^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{3,9}$/.test($.trim(str))); 
 }
 /**
  * 验证密码复杂度 
  * 不允许6个连续的数字（如123456、012345、654321、543210），6个相同的数字（如111111）
  * @param str
  * @returns
  */
 function checkComplexity(s){
	    if (!/^\d{6}$/.test(s)) return false; // 不是6位数字
	    if (/^(\d)\1+$/.test(s)) return false;  // 全一样
	     
	    var str = s.replace(/\d/g, function($0, pos) {
	        return parseInt($0)-pos;
	    });
	    if (/^(\d)\1+$/.test(str)) return false;  // 顺增
	     
	    str = s.replace(/\d/g, function($0, pos) {
	        return parseInt($0)+pos;
	    });
	    if (/^(\d)\1+$/.test(str)) return false;  // 顺减
	    return true;

	 //return !(/\d{6}/.test(str) && (/^(\d)\1{5}$/.test(str) || str == str.replace(/\d/g, function(v, i){return f+i;})));
 }
/********************校验数据***************************/
 
 
/**********************输入校验*******************************/
 //只能输入数字
 function checkPositiveInteger(obj){
	 obj.value=obj.value.replace(/\D/g,''); 
 }
//只能输入字母
 function checkLetter(obj){
	 obj.value=obj.value.replace(/[^a-zA-Z]+$/,'');
 }
 
//只能输入汉字
 function checkChinese(obj){
	 obj.value=obj.value.replace(/[^\u4E00-\u9FA5]/g,''); 
 }
 
//只能输入字母和汉字
 function checkLettersAndChinese(obj){
	 obj.value=obj.value.replace(/[\d]/g,''); 
 }
//只能输入英文字母和数字
 function checkLettersAndNumbers(obj){
	 obj.value=obj.value.replace(/[^\w\.\/]/ig,''); 
 }
//座机
 function checkPhone(obj){
 	 obj.value=obj.value.replace(/[^\d-]/g,''); 
 }


  
 /**********************输入校验end****************************/
 
 //显示提示 parma id：提示所在id；content：内容
function showPrompt(id,content){
	$("#"+id).html(content);
	setTimeout(function(){
		$("#"+id).html("");
	}, 2000);
} 

/*****************按钮倒计时******************/
var verifyButtonCount = 180;
var verifyButtonInterval=null;
var verifyButtonId="";
/**buttonId 按钮id，startCount 开始时间*/
function verifyButton(buttonId,startCount){
	verifyButtonId = buttonId;
	verifyButtonCount = startCount||180;
	if(null!=verifyButtonInterval){
		 clearInterval(verifyButtonInterval);
	}
	
	$("#"+verifyButtonId).attr("disabled","disabled");	
	$("#"+verifyButtonId).html(verifyButtonCount+"秒后重新发送");
	verifyButtonInterval=setInterval(function(){
	if(verifyButtonCount<=0){
		$("#"+verifyButtonId).removeAttr("disabled");
		$("#"+verifyButtonId).html("重新发送");
	return;
	}else{
		$("#"+verifyButtonId).html((--verifyButtonCount)+"秒后重新发送");
	}
	
	},1000);
	
}
/*****************end按钮倒计时******************/

//到计时，count秒后返回到首页
//parm id：显示id；count：时间s；content：内容
countdown={
	id:"",
	count:0,
	content:"",
	init :function(id,count,content){
		 this.id=id;
		 this.count=count;
		 this.content=content;	
	},
  start :function(){		  	
	 if (this.count <=0) {
		 location.href = "http://www.yirenyijia.com:80";
	 	}
	 $("#oTime").css("display","block");
	 $("#"+ this.id).html(this.content+"，将在"+ this.count+"秒内回到首页");
	 setInterval(function(){
	 		countdown.count--;		 	
		 	if ( countdown.count <= 0) {
		 		location.href = "http://www.yirenyijia.com:80";
		 	} else {
		 	$("#"+ countdown.id).html(countdown.content+"，将在"+ countdown.count+"秒内回到首页");
		 	}
	 	}, 1000);
	 	
 }
};

function loadJs(url,sync){
	sync = sync||true;
	var s = document.createElement('script'); 
	s.type = 'text/javascript'; 
	s.async = sync; 
	s.src = url; 
	document.body.appendChild(s);
}

 