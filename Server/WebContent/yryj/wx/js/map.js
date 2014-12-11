allCategory = new Map();
shopMap = new Map();
wxmap = new weixinMap();
shopMap = new Map();


curScId = 330;
curOrderType = 1;
curIndex = 1;
searchType = 2;
curShopId = 1;
nearbyShop = null;
var userMarker = null;
maxResult = 9999;

function weixinMap(options) {
	this.map = null;

	this.zoom = 12;
	this.minZoom = 10;
	this.swne = "";
	this.currentShops = null;
	this.geolocation = new BMap.Geolocation();//定位控件
	
	this.createMap = function(center) {
		
		this.map = new BMap.Map("map");
		
		this.map.centerAndZoom(eval("new BMap.Point(" + center + ")"),this.zoom);
		
		//this.map.setMinZoom(this.minZoom);// 设置最小缩放级别

		/** *******添加控件************** */
		// 向地图中添加缩放控件
		this.map.addControl(new BMap.NavigationControl({
			anchor : BMAP_ANCHOR_TOP_LEFT,
			type : BMAP_NAVIGATION_CONTROL_LARGE,
			offset : new BMap.Size(0, 0)
		}));

//		// 向地图中添加比例尺控件
//		this.map.addControl(new BMap.ScaleControl({
//			anchor : BMAP_ANCHOR_BOTTOM_LEFT
//		}));

		/** *******添加控件end************** */

		/** *******添加事件件************** */
		this.map.enableDragging();// 启用地图拖拽事件，默认启用(可不写)
		this.map.enableScrollWheelZoom();// 启用地图滚轮放大缩小
		this.map.enableDoubleClickZoom();// 启用鼠标双击放大，默认启用(可不写)
		this.map.enableKeyboard();// 启用键盘上下左右键移动地图
		this.map.enableInertialDragging();// 启用地图惯性拖拽，默认禁用。
		var this_ = this;
		this.map.addEventListener("dragend", function() {
			this_.moveend();
		});
//		this.map.addEventListener("zoomend", function() { // 窗口事件
//			this_.moveend();
//		});

		/** *******添加事件end************** */
		this.swne = this.getSwne();
		return this;
	};
	this.getSwne = function() {
		this.swne = this.map.getBounds().getSouthWest().lng + ","
				+ this.map.getBounds().getSouthWest().lat + ","
				+ this.map.getBounds().getNorthEast().lng + ","
				+ this.map.getBounds().getNorthEast().lat;
		return this.swne;
	};
	this.addMarker = function(shopInfo) {
		var point = new BMap.Point(shopInfo.shopLon,shopInfo.shopIat);
		var marker = new BMap.Marker(point);
		marker.setTitle(shopInfo.shopName);
		var infoWindow = new BMap.InfoWindow("<a href='javascript:void(0);' onclick='showShopinfo("+shopInfo.id+")'>"+shopInfo.shopName +"</a>"); // 创建信息窗口对象

		var this_ = this;

		marker.addEventListener("onclick", function() {
			this.openInfoWindow(infoWindow);
			// infoWindow.redraw();
		});

		marker.addEventListener("mouseover", function() {
			//this_.findAll();
		});
		marker.addEventListener("mouseout", function() {
		});
		// marker.hide();
		this.map.addOverlay(marker);
		// this.markersMap.put(shopInfo.id, marker);
		return point;
	};
	
	//根据区域查询网点
	this.findShopByCondition  = function(){
		var formParm = {};
		formParm.url = "shopInfo/findShopByCondition";
		formParm.areaCode = curAreaCode;
		formParm.curCoord = userGps;
		formParm.scId = curScId;
		formParm.orderType = curOrderType;
		formParm.startIndex = 1;
		formParm.fetchSize = "10";
		//formParm.disRange ="1";
		domainRequest(formParm, function(data, index) {
			if(data.status){
				var this_ = wxmap;
				this_.map = null;
				
				if(null == this_.map){
					this_.createMap(cityGps);
				}
				
				this_.currentShops=data.shopInfoList;
				
				//清除覆盖物
				this_.map.clearOverlays();
				
				var points = [];
				
				for ( var i = 0; i < this_.currentShops.length; i++) {
					shopMap.put(this_.currentShops[i].id, this_.currentShops[i]);
					points.push(this_.addMarker(this_.currentShops[i]));
				}
				this_.map.setViewport(points);
				//
				this_.setUserMarker();

			}
			
		});
		
	}

	//根据gps和服务查询网点
	this.moveend = function(){
		
		var formParm = {};
		formParm.url = "shopInfo/findShopByGpsAndServiceCategoryId";
		
		formParm.southwestCoord = this.map.getBounds().getSouthWest().lng + ","
		+ this.map.getBounds().getSouthWest().lat ;
		
		formParm.northeastCoord = this.map.getBounds().getNorthEast().lng + ","
		+ this.map.getBounds().getNorthEast().lat;
		
		formParm.scId = curScId;
		formParm.version = "android";
		
		domainRequest(formParm, function(data, index) {
			if(data.status){
				var this_ = wxmap;
				this_.currentShops=data.shopInfoList;				
				
				//清除覆盖物
				this_.map.clearOverlays();								
				this_.setUserMarker();				
				//var points = [];				
				for ( var i = 0; i < this_.currentShops.length; i++) {
					shopMap.put(this_.currentShops[i].id, this_.currentShops[i]);
					this_.addMarker(this_.currentShops[i]);
					//points.push(this_.addMarker(this_.currentShops[i]));
				}
				//this_.map.setViewport(points);
			}
		});
	}

	this.setUserMarker = function(){
			if(this.map != null){
				this.map.removeOverlay(userMarker);
			}
			var marker = new BMap.Marker(new BMap.Point(userGps.split(",")[0],userGps.split(",")[1]),{
				icon : new BMap.Icon(basePath+"/wx/images/dingwei@2x.png", new BMap.Size(26, 26))
			});
			marker.setTitle("我的位置");
			userMarker = marker;
			if(this.map != null){
				this.map.addOverlay(marker);	
			}
	}
	this.issuegeo = function (){//百度定位
		var this_ = this;
		this.geolocation.getCurrentPosition(function(r){
					if(this.getStatus() == BMAP_STATUS_SUCCESS){						
						//this_.map.panTo(r.point);
						var lng = r.point.lng;
						var lat = r.point.lat;
						userGps = lng+","+lat;
						this_.setUserMarker();
					}
					else {
						switch(this.getStatus()){
						case 6 :
							//alert("请求未授权");
							break;
						default :
							//alert("无法获取当前位置，您可移动地图查看网点");
							break;
						}
					}
				},{enableHighAccuracy: true,timeout: 10000,maximumAge: 60*1000});
	}

}
var driveMap = null;
	function drive(){
		//$("#map,.shoplist,#listicon,#mapicon,#shopinfoWrap").hide();
		//alert(1)
		$("#driveMapWrap").show();
		$("#shopinfoWrap").hide();
		var shopInfo = shopMap.get(curShopId);
		
		var strPoint = userGps;
		var endPoint = shopInfo.shopLon +"," + shopInfo.shopIat;
		
		strPoint = strPoint.split(",");
		endPoint = endPoint.split(",");
		
		if(null == driveMap){
			driveMap = new BMap.Map("driveMapDiv");
			driveMap.centerAndZoom(new BMap.Point((strPoint[0]+endPoint[0])/2,(strPoint[1]+endPoint[1])/2), 11);
			driveMap.addControl(new BMap.NavigationControl({
				anchor : BMAP_ANCHOR_TOP_LEFT,
				type : BMAP_NAVIGATION_CONTROL_LARGE,
				offset : new BMap.Size(0, 0)
			}));
			
			driveMap.enableDragging();// 启用地图拖拽事件，默认启用(可不写)
			driveMap.enableScrollWheelZoom();// 启用地图滚轮放大缩小
			driveMap.enableDoubleClickZoom();// 启用鼠标双击放大，默认启用(可不写)
			driveMap.enableKeyboard();// 启用键盘上下左右键移动地图
			driveMap.enableInertialDragging();// 启用地图惯性拖拽，默认禁用。
			
			//driveMap.centerAndZoom(new BMap.Point(strPoint[0],strPoint[1]), 11);
		}
		driveMap.clearOverlays();
		

	var p1 = new BMap.Point(strPoint[0],strPoint[1]);
	var p2 = new BMap.Point(endPoint[0],endPoint[1]);
	var driving = new BMap.DrivingRoute(driveMap, {renderOptions:{map: driveMap, autoViewport: true}});
	driving.search(p1, p2);	
	
	
	
}

function goBackShopList(){
	$("#driveMapWrap").hide();
	$("#shopinfoWrap").show();
}
	
function init() {
	//服务分类
	if (categoryList) {
		$.each(categoryList, function(dex, da) {
			allCategory.put(da.id, da);
			$.each(da.subCategory, function(dex1, da1) {
				allCategory.put(da1.id, da1);
			});
		});
		setFirstSC();
	}
	categoryList = null;
	
	if (areaList) {
		wdsearchAreaList(curAreaCode, 1);
	}
	
	
		inter = setInterval(function(){
			if(userGps != null){
				window.clearInterval(inter)
				shopList();	
			}
		},100)
	
	
	wxmap.issuegeo();
	//
//	setTimeout(function(){
//		//wxmap.removeViewAt(1)
//		$('#map div.anchorBL').hide();
//		//$('#map div.BMap_cpyCtrl').remove();
//	}, 1000);


	
};


function shopList(){
	var formParm = {};
	formParm.url = "shopInfo/findShopByCondition";
	formParm.areaCode = curAreaCode;
	formParm.curCoord = userGps;
	formParm.scId = curScId;
	formParm.orderType = curOrderType;
	formParm.startIndex =curIndex;
	formParm.fetchSize = "10";
	//formParm.disRange ="1";
	var this_ = this;
	
	
	
	curIndex=curIndex+10;
	
	if(maxResult==0 && curIndex!=11){
		$("#loadeData").hide();	
		return;
	}else{
		$("#loadeData").show();		
	}
	domainRequest(formParm, function(data, index) {
		$("#loadeData").hide();	
		if(curIndex == 11){
			$("#shoplist").html("");			
		}
		if(data.status){
			maxResult = data.maxResult;
			var list = data.shopInfoList;
			if(list.length == 0 &&curIndex ==11){
				$("#shoplist").html('<div class="noShop">附近没有网点</div>');	
				return;
			}			
			
				
			
			
			var html = "";
			var shop;
			for ( var i = 0; i < list.length; i++) {				
				shop = list[i];
				shopMap.put(shop.id, shop);
				
				html+='<ul class="clearfix" onClick="showShopinfo('+shop.id+')" onmousedown="mouseDownFun(this);" onmouseup="mouseUpFun(this);"><li><h1>'+
				shop.shopName+'</h1><p><em class="star'+
				(Math.round(shop.score))+'"></em><span class="integral">'+
				shop.minServicePrice+'-'+
				shop.maxServicePrice+'</span><span class="gray">积分</span><span class="distance">'+
				(shop.distance>50?">50":shop.distance)+'km</span></p></li></ul>';
				
				//setTimeout(function(){appendShoplist(html);}, i*100);	
			}
			$("#shoplist").append(html);		
			$("#loadeData").hide();	
				
		}
		
	});
}


function appendShoplist(temp){
	$("#shoplist").append(temp);	
}

//-------------




/*
domainRequest = function(formParm, callback) {
	$.ajax({
		type : "POST",
		url : "domainRequest",
		data : formParm,
		// async : false,
		dataType : "json",
		success : callback
	});
}*/

//jhy temp stub
var json_tmp = {"status":true,"shopInfoList":[{"id":875,"shopName":"神州汽车美容养护中心","score":3.0,"distance":0.69,"shopIat":32.059157,"shopLon":118.81177,"maxServicePrice":19.00,"minServicePrice":14.00,"rechargeAmount":0},{"id":876,"shopName":"南京恒信汽车装潢美容中心","score":4.6,"distance":1.72,"shopIat":32.069099,"shopLon":118.79246,"maxServicePrice":25.00,"minServicePrice":19.00,"rechargeAmount":0},{"id":1571,"shopName":"南京道龙汽车维修有限公司（逸仙桥分店）","score":3.0,"distance":2.05,"shopIat":32.046259,"shopLon":118.809542,"maxServicePrice":19.00,"minServicePrice":14.00,"rechargeAmount":0},{"id":2395,"shopName":"红星美凯龙洗车场","score":3.0,"distance":2.51,"shopIat":32.080201,"shopLon":118.79104,"maxServicePrice":20.00,"minServicePrice":17.00,"rechargeAmount":0},{"id":1969,"shopName":"南京金鹰苏星汽车美容服务有限公司","score":3.0,"distance":2.82,"shopIat":32.047219,"shopLon":118.788574,"maxServicePrice":30.00,"minServicePrice":30.00,"rechargeAmount":200.0},{"id":1570,"shopName":"南京道龙汽车维修有限公司（五台山总店）","score":4.2,"distance":2.89,"shopIat":32.056458,"shopLon":118.781789,"maxServicePrice":19.00,"minServicePrice":14.00,"rechargeAmount":0},{"id":1574,"shopName":"南京车之轩汽车美容服务中心","score":5.0,"distance":2.97,"shopIat":32.046888,"shopLon":118.830114,"maxServicePrice":20.00,"minServicePrice":16.00,"rechargeAmount":0},{"id":821,"shopName":"中环洗车场 ","score":3.0,"distance":3.15,"shopIat":32.072856,"shopLon":118.779261,"maxServicePrice":25.00,"minServicePrice":17.00,"rechargeAmount":0},{"id":807,"shopName":"车之悦汽车美容装璜中心（原金浩汽车服务）","score":3.0,"distance":3.5,"shopIat":32.034715,"shopLon":118.819028,"maxServicePrice":24.00,"minServicePrice":19.00,"rechargeAmount":100.0},{"id":823,"shopName":"金鹏汽车清洗美容服务中心","score":3.0,"distance":3.5,"shopIat":32.049953,"shopLon":118.778385,"maxServicePrice":20.00,"minServicePrice":15.00,"rechargeAmount":0}],"maxResult":75};
domainRequest = function(formParm, callback) {
		//var json_tmp = {"status":true,"shopInfoList":[{"id":2216,"shopName":"斌利汽车装饰美容","score":3.4,"distance":433.54,"shopIat":30.8389,"shopLon":114.808396,"maxServicePrice":18.00,"minServicePrice":15.00,"rechargeAmount":0},{"id":660,"shopName":"酷车汽车美容装饰","score":3.0,"distance":461.67,"shopIat":30.65524,"shopLon":114.588138,"maxServicePrice":18.00,"minServicePrice":16.00,"rechargeAmount":100.0},{"id":155,"shopName":"宏达汽车修理厂","score":3.4,"distance":475.0,"shopIat":30.880675,"shopLon":114.372074,"maxServicePrice":16.00,"minServicePrice":16.00,"rechargeAmount":0},{"id":1669,"shopName":"志英汽车美容","score":3.0,"distance":476.66,"shopIat":30.584847,"shopLon":114.460918,"maxServicePrice":18.00,"minServicePrice":15.00,"rechargeAmount":0},{"id":236,"shopName":"柯尼卡汽车美容中心","score":5.0,"distance":477.63,"shopIat":30.648483,"shopLon":114.425975,"maxServicePrice":20.00,"minServicePrice":18.00,"rechargeAmount":705.0},{"id":2432,"shopName":"尚路安汽车服务中心","score":3.0,"distance":477.68,"shopIat":30.594152,"shopLon":114.446661,"maxServicePrice":18.00,"minServicePrice":15.00,"rechargeAmount":0},{"id":2035,"shopName":"爱车屋（杨春湖花园店）","score":3.0,"distance":478.95,"shopIat":30.625272,"shopLon":114.421335,"maxServicePrice":20.00,"minServicePrice":15.00,"rechargeAmount":200.0},{"id":445,"shopName":"武钢兴达汽修","score":4.2,"distance":479.44,"shopIat":30.650022,"shopLon":114.406803,"maxServicePrice":20.00,"minServicePrice":18.00,"rechargeAmount":101.0},{"id":220,"shopName":"英国尼尔森专业汽车美容装饰护理中心","score":3.0,"distance":479.71,"shopIat":30.63098,"shopLon":114.411214,"maxServicePrice":20.00,"minServicePrice":18.00,"rechargeAmount":3550.0},{"id":1600,"shopName":"强盛汽车装饰美容中心","score":3.0,"distance":480.0,"shopIat":30.624868,"shopLon":114.410582,"maxServicePrice":18.00,"minServicePrice":15.00,"rechargeAmount":950.0}],"maxResult":201}
		setTimeout(function(){callback(json_tmp),3000});
}


translateCallback = function(point) {

	// alert(point.lng + "," + point.lat);
}

convertor = function(gpsPoint) {
	BMap.Convertor.translate(gpsPoint, 0, translateCallback); // 真实经纬度转成百度坐标
}





//查询区域商圈
function wdsearchAreaList(code, type) {
	curAreaCode = code + "";
	type++;
	var html = "";// 区域
	var html2 = "";// 商圈
	if (curAreaCode.length == 3) {
		html += '<li onclick="areaListClick(this);" gps="' + cityGps + '" areaCode="'
		+ cityCode + '" type="1">'+cityName+'</li>';
	}
	
	$.each(areaList, function(dataindex, entry) {

		if (entry.areaCode.indexOf(curAreaCode) < 0 || entry.areaType != type)
			return;
		if (curAreaCode.length == 3) {
			html += '<li onclick="areaListClick(this);" gps="' + entry.gpsGoordinate + '" areaCode="'
					+ entry.areaCode + '" type="' + entry.areaType + '">'
					+ entry.areaName + '</li>';

		}
		if (curAreaCode.length == 5) {
			html2 += '<li onclick="areaList2Click(this);" gps="' + entry.gpsGoordinate + '" areaCode="'
					+ entry.areaCode + '">' + entry.areaName + '</li>';
		}
	});
	if (curAreaCode.length == 3) {
		$("#areaList").html(html);
		$("#areaList2").html(html2);

	}
	if (curAreaCode.length == 5) {
		$("#areaList2").html(html2);
		if(html2==""){
			$("#list1").slideUp();
		}

	}
	// markArea();

}


//---------





function mouseDownFun(addclass){
	$(addclass).addClass("active");
	}
function mouseUpFun(removeclass){
	$(removeclass).removeClass("active");
	}
function showList1(){
	$("#list1").slideToggle();
	$("#list2,#list3").hide();
};
function showList2(){
	$("#list2").slideToggle();
	$("#list1,#list3").hide();
};
function showList3(){
	$("#list3").slideToggle();
	$("#list2,#list1").hide();
};
function hideList(){
	$(".list").slideUp();
};

$("#listicon,#mapicon").click(function(){
	$("#map,.shoplist,#listicon,#mapicon").toggle();
});

//jhy temp stub for商店详情
var json_shoplist_tmp = {"shop":{"queueingProbability":1,"shopAddress":"龙蟠中路37号百仕园4栋101室 ","serviceList":[{"id":1245,"serviceName":"小型车清洗","facePrice":14.00,"cashPrice":20.00,"categoryId":330},{"id":1246,"serviceName":"大型车清洗","facePrice":19.00,"cashPrice":25.00,"categoryId":330},{"id":6434,"serviceName":"内饰清洗","facePrice":280.00,"cashPrice":400.00,"categoryId":332},{"id":6435,"serviceName":"内饰桑拿","facePrice":160.00,"cashPrice":200.00,"categoryId":332},{"id":6436,"serviceName":"车身打蜡","facePrice":70.00,"cashPrice":100.00,"categoryId":350},{"id":6437,"serviceName":"车身抛光","facePrice":224.00,"cashPrice":280.00,"categoryId":358},{"id":6438,"serviceName":"车身封釉","facePrice":266.00,"cashPrice":380.00,"categoryId":356},{"id":7515,"serviceName":"团购打蜡","facePrice":39.00,"cashPrice":160.00,"categoryId":401}],"picAddr":"resources/shopPicture/42635720130718151901.JPG","shopPhone":"025-83602120 "},"status":true};
domainRequest_ShopInfo = function(formParm, callback) {
		setTimeout(function(){callback(json_shoplist_tmp),3000});
}

function showShopinfo(id){	
	curShopId = id;
	
	var shopInfo = shopMap.get(id);
	$("#shop_name").html(shopInfo.shopName);
	$("#shop_score").removeClass().addClass("star"+(Math.round(shopInfo.score)));
	
	
		var formParm = {};
		formParm.url = "shopInfo/findShopById";
		formParm.shopId = id;
		
		domainRequest_ShopInfo(formParm, function(data, index) {
			if(data.status){			
				var shop = data.shop;

				//$("#shop_name").html(shop.name);
				$("#shop_picAddr").attr("src",shop.picAddr);
				$("#shop_shopPhone").html(shop.shopPhone);
				$("#shop_shopPhone_1").attr("href","tel:"+shop.shopPhone);
				$("#shop_shopAddress").html(shop.shopAddress);
				
				
				$("#shop_queueingProbability").removeClass();
				if(shop.queueingProbability == 1){
					$("#shop_queueingProbability").addClass("green").html("低");	
				}else if(shop.queueingProbability == 2){
					$("#shop_queueingProbability").addClass("yellow").html("中");
				}else if(shop.queueingProbability ==3){
					$("#shop_queueingProbability").addClass("red").html("高");
				}
				
				
				var serviceList = shop.serviceList;
				var html ="";
				var ser ;
				for(var i=0;i<serviceList.length;i++){
					ser = serviceList[i]; 
					html +='<li class="clearfix"><span class="type1">'+
					ser.serviceName+'</span><del class="price">￥'+
					ser.cashPrice+'</del><span class="integral">'+
					ser.facePrice+'<i>积分</i></span></li>';
				}
				$("#serviceList").html(html);
				$('.serviceType ul li:gt(1)').hide();
				$('.serviceType .more').html('更多');
				$("#shopinfoWrap").show();
				$("#shopWrap").hide();
				
			}
			
		});
		
		if(nearbyShop == null){
			setTimeout(function(){
				//附近网点
				formParm = {};
				formParm.url = "shopInfo/findShopByCondition";
				formParm.areaCode = cityCode;
				formParm.curCoord = userGps;
				
				formParm.scId = curScId;
				formParm.orderType = 1;
				formParm.startIndex =1;
				formParm.fetchSize = 3;
				//formParm.disRange ="1";
				//var this_ = this;
				domainRequest(formParm, function(data, index) {
					if(data.status){			
						nearbyShop = data.shopInfoList;
						doNearbyShop();
					}				
				});	
			}, 100)

		}else{
			doNearbyShop();
		}	
}

function doNearbyShop(){
	$("#otherShopList").html("");
	var list = nearbyShop;
	var html = "";
	var shop;
	for ( var i = 0; i < list.length; i++) {				
		shop = list[i];
		shopMap.put(shop.id, shop);		
		if(shop.id == curShopId)
			continue;		
		html='<div onClick="showShopinfo('+shop.id+')"><h1>'+shop.shopName+'</h1>	<div class="otherShop clearfix"><em class="star'+(Math.round(shop.score))+'"></em><span class="price">'+
		shop.minServicePrice+'-'+
		shop.maxServicePrice+'</span><span class="distance">'+(shop.distance>50?">50":shop.distance)+'km</span></div></div>';
		$("#otherShopList").append(html);
	}
}


$("#shopinfoWrap .left").click(function(){
	$("#shopinfoWrap ,#shopWrap").toggle();
});

function setCenterArea(areaGps, areaCode) {
	curAreaCode = areaCode + "";
	if (curAreaCode.length == 3) {

	} else if (curAreaCode.length == 5) {
		var point = new BMap.Point(areaGps.split(",")[0], areaGps.split(",")[1]);
		wxmap.map.setZoom(findAreaByCode(curAreaCode).areaLevel||13);
		wxmap.map.panTo(point);
	} else if (curAreaCode.length == 7) {
		var point = new BMap.Point(areaGps.split(",")[0], areaGps.split(",")[1]);
		wxmap.map.setZoom(findAreaByCode(curAreaCode).areaLevel||15);
		wxmap.map.panTo(point);
	}

}

function findAreaByCode(code){
	for(var i=0;i<areaList.length;i++){
		if(areaList[i].areaCode==code)
			return areaList[i];
	}
}

function setFirstSC() {
	var html = '';
	for ( var i = 0; i < categoryList.length; i++) {
		html += '<li onclick="category1Click(this);" id=category1_' + categoryList[i].id + ' scId="'
				+ categoryList[i].id + '">' + categoryList[i].categoryName
				+ '</li>';
	}
	$("#category1").html(html);
	setTimeout(function(){
		$("#category1 li:first").click();
		$("#category2 li:first").click();
	}, 100);

}
function selectFirstSC(id) {
	
	$("#category2").remove();
	var html = '<ul class="ul2" id="category2">';
	$("#selectedFirestSC").html(allCategory.get(id).categoryName + ">>");
	$.each(allCategory.get(id).subCategory, function(dex, da) {
		html += (dex == 0 ? "" : ' ') + '<li onclick="category2Click(this)" id=category2_' + da.id + ' scId="'
				+ da.id + '">' + da.categoryName + '</li>';
	});

	html+="</ul>"
	$("#list2").append(html);


}
function selectSecondSC(id) {
	var ser = allCategory.get(id);
	$("#serType").html(ser.categoryName);

}

function findAll() {
	var formParm = {};
	formParm.url = "shopInfo/findShopByArea";

	domainRequest(formParm, function(data, index) {
		var a = data;
	});
}
var firstLode = true;
function changeType(type){
	searchType = type;
	if(searchType ==2){
		curIndex =1;
		shopList();
	}else{
		//if(firstLode){
			//wxmap.createMap(userGps);
			//firstLode = false;
		//}
		wxmap.findShopByCondition();
	}
}

function areaListClick(areaList){
		$("#areaList li").removeClass("current");
		$(areaList).addClass("current");
		var obj = $(areaList);
		
		wdsearchAreaList(obj.attr("areaCode"), obj.attr("type"));
		$("#selectArea").html(obj.html());
		
		if(obj.attr("type") == "1"){
			$("#list1").slideUp();
		}
		
		curAreaCode = obj.attr("areaCode");
		curGps = obj.attr("gps");
		
		if(searchType ==2){
			curIndex =1;
			//if(firstclick)
			shopList();	
		}else{
			setCenterArea(obj.attr("gps"), obj.attr("areaCode"));
			wxmap.findShopByCondition();
		}
	}
function areaList2Click(areaList2){
	$(areaList2).addClass("current").siblings().removeClass("current");
	var obj = $(areaList2);
		
	$("#selectArea").html(obj.html());
	
	curAreaCode = obj.attr("areaCode");
	curGps = obj.attr("gps");
	
	if(searchType ==2){
		curIndex =1;
		//if(firstclick)
			shopList();	
	}else{
		setCenterArea(obj.attr("gps"), obj.attr("areaCode"));	
		wxmap.findShopByCondition();
	}
	hideList();
	
}
var firstclick = false;
function category1Click(category1){
	$(category1).addClass("current").siblings().removeClass("current");
	selectFirstSC($(category1).attr("scid"));
	if(searchType ==2){
//		curIndex =1;
//		if(firstclick){
//			shopList();		
//		}
		
	}
}
function category2Click(category2){
	$(category2).addClass("current").siblings().removeClass("current");
	selectSecondSC($(category2).attr("scid"));
	curScId = $(category2).attr("scid");
	
	if(searchType ==2){
		
		if(firstclick){	
			curIndex =1;
			shopList();				
		}
		firstclick = true;
		
	}else{
		wxmap.findShopByCondition();
	}
	hideList();
}
$("#list3 ul li").click(function() {
	$(this).addClass("current").siblings().removeClass("current");
	curOrderType = $(this).attr("val");
	$("#orderType").html($(this).html());
	
	if(searchType ==2){
		curIndex =1;
		shopList();	
	}else{
		wxmap.findShopByCondition();
	}
	hideList();
});
function showMoreService(){
	var $category = $('.serviceType ul li:gt(1)');   
	if($('.serviceType .more').html() == '更多' && $('.serviceType ul li').length > 2){
		//if($category.length>0){
			$category.show(); 
			$('.serviceType .more').html('收起');
		//}
	}else{
		
		$category.hide();
		$('.serviceType .more').html('更多');
		
		
	}
	
}
function selectCity(){
	$("#selectCityWrap").show();
	$("#shopWrap").hide();
}
function goShowShopWrap(){
	$("#selectCityWrap").hide();
	$("#shopWrap").show();
}
function setCitySpan(city){
	//jhy temp 
	window.top.location.href = basePath+"wx/shop/map.html?cityCode="+city;
	//alert("切换城市");
	
//	$city = $(city).text();
//	$("#cityName").html($city);
//	goShowShopWrap();
	
}
