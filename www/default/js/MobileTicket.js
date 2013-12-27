
/* JavaScript content from js/MobileTicket.js in folder common */
/*
 * Licensed Materials - Property of IBM
 * 5725-G92 (C) Copyright IBM Corp. 2006, 2012. All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

// Worklight comes with the jQuery 1.8.1 framework bundled inside. If you do not want to use it, please comment out the line below.
//window.$ = window.jQuery = WLJQ;
var busy = new WL.BusyIndicator(null, {
	text : "加载中...",
	opacity : 0.5,
	fullScreen : false,
	boxLength : 2.0
});

function wlCommonInit() {

	/*
	 * Application is started in offline mode as defined by a connectOnStartup
	 * property in initOptions.js file. In order to begin communicating with
	 * Worklight Server you need to either:
	 *
	 * 1. Change connectOnStartup property in initOptions.js to true. This will
	 * make Worklight framework automatically attempt to connect to Worklight
	 * Server as a part of application start-up. Keep in mind - this may
	 * increase application start-up time.
	 *
	 * 2. Use WL.Client.connect() API once connectivity to a Worklight Server is
	 * required. This API needs to be called only once, before any other
	 * WL.Client methods that communicate with the Worklight Server. Don't
	 * forget to specify and implement onSuccess and onFailure callback
	 * functions for WL.Client.connect(), e.g:
	 *
	 * WL.Client.connect({ onSuccess: onConnectSuccess, onFailure:
	 * onConnectFailure });
	 *
	 */

	var util = mor.ticket.util;
	util.userDef = jq.Deferred();
	util.syncDef = util.syncDef || jq.Deferred();

	// Common initialization code goes here
	if (!busy.isVisible()) {
		busy.show();
	}

	// 摇一摇
	if (util.isAndroid()) {
		registEventForWave();
	}
	document.addEventListener(WL.Events.WORKLIGHT_IS_DISCONNECTED,
			handleConnectionDown, false);
	initCommonData();

	prepareCacheMap();

	syncLocalCache();
	registerTabListener();

	handleAndriodBackbutton();
	handleNativeCodeCallBack();

	jq.when(util.userDef, util.syncDef).done(function() {
		if (busy.isVisible()) {
			// WL.Logger.debug('******* Hide busy indicator in wlCommonInit.');
			busy.hide();
		}
	});

	setTimeout(function() {
		// WL.Client.__hideBusy();
		if (util.userDef.state() != 'resolved') {
			// WL.Logger.debug('------------------- reslove util.userDef.');
			util.userDef.resolve();
		}
		if (util.syncDef.state() != 'resolved') {
			// WL.Logger.debug('------------------- reslove util.syncDef.');
			util.syncDef.resolve();
		}
		if (busy.isVisible()) {
			// WL.Logger.debug('------------------- Hide busy indicator in
			// timer.');
			busy.hide();
		}
	}, 6000);

	// 自动登录
	//registerAutoLoginHandler();

	jq("#content").css("pointer-events", "auto");
}

// authenticity challenge failed callback
if (wl_authenticityChallengeHandler.handleFailure) {
	wl_authenticityChallengeHandler.handleFailure = function(err) {
		WL.SimpleDialog.show(WL.ClientMessages.wlclientInitFailure,
				WL.ClientMessages.authenticityFailure, [ {
					text : WL.ClientMessages.exit,
					handler : function() {
						// exit App if authenticity chanllenge failed.
						WL.App.close();
					}
				} ]);
	};
}

function registEventForWave() {
	if (window.DeviceMotionEvent) {
		window.addEventListener('devicemotion', deviceMotionHandler, true);
	}
	// var options={frequency:100};
	// navigator.accelerometer.watchAcceleration(deviceMotionHandler, null,
	// options);
}

var SHAKE_THRESHOLD = 10;
var last_update = 0;
var last_x, last_y, last_z;
function deviceMotionHandler(eventData) {
	var curTime = new Date().getTime();
	// different time
	if ((curTime - last_update) > 100) {
		var acceleration = eventData.accelerationIncludingGravity;
		var x = acceleration.x;
		var y = acceleration.y;
		var z = acceleration.z;
		var diffTime = curTime - last_update;
		var rate = Math.abs(x + y + z - last_x - last_y - last_z) / 3 / diffTime * 100;
		if (rate > SHAKE_THRESHOLD) {
			waveHandler();
		}
		last_update = curTime;
		last_x = x;
		last_y = y;
		last_z = z;
	}
}

function waveHandler() {
	if (document.activeElement.nodeName == 'INPUT'
			|| document.activeElement.nodeName == 'TEXTAREA') {
		navigator.notification.vibrate(300);
		WL.SimpleDialog.show("温馨提示", "是否确认清空所有输入?", [ {
			text : "取消",
			handler : function() {
			}
		}, {
			text : "确认",
			handler : function() {
				document.activeElement.value = "";
			}
		} ]);
	}
}

function handleConnectionDown() {
	if (mor.ticket.util.isPreview) {
		return;
	}

	WL.Device.getNetworkInfo(function(networkInfo) {
		if (busy.isVisible()) {
			busy.hide();
		}
		if (networkInfo.isNetworkConnected == "false") {
			WL.SimpleDialog.show("温馨提示", "哎呀，您的网络有问题，请检查网络连接。", [ {
				text : '确定',
				handler : function() {
				}
			} ]);
		} else {
			WL.SimpleDialog.show("温馨提示", "哎呀，您的网络好像有问题，请检查网络连接。", [ {
				text : '确定',
				handler : function() {
				}
			} ]);
		}
	});
}

function handleNativeCodeCallBack() {
	window.plugins.childBrowser.onCounterDialogOk = mor.ticket.payment.onCounterDialogOk;
	window.plugins.childBrowser.onOrderList = mor.ticket.payment.orderList;
	window.plugins.childBrowser.onOrderComplete = mor.ticket.payment.orderComplete;
	window.orderComplete = mor.ticket.payment.orderComplete;
}

function vPathCallBack() {

	var pat = RegExp('views');
	var path = null;
	var Hpath = window.location;
	if (pat.test(Hpath)) {
		path = "../views/";
	} else {
		path = "views/";
	}
	return (path);
}

function vPathViewCallBack() {

	var pat = RegExp('views');
	var path = null;
	var Hpath = window.location;
	if (pat.test(Hpath)) {
		path = "../";
	} else {
		path = "";
	}
	return (path);
}

// 1. 根据当前路径返回基准路径.
// 2. 如果提供目标路径，返回完整的目标路径地址。
function getViewBasePath(target) {
	var base = '';
	if (/views/.test(window.location)) {
		base = '../';
	} else {
		base = '';
	}
	if(!target) {
		return base;
	} else {
		if (/^MobileTicket\.html$/.test(target)) {
			return base + target;
		} else {
			return base + 'views/' + target;
		}
	}
}


function registerTabListener() {	
	var footTabMapping = {
	    'bookTicketTab': {
	    	needLogin: false,
	    	url: 'MobileTicket.html'
	    },
	    'queryOrderTab': {
	    	needLogin: true,
	    	url: 'views/queryOrder.html'
	    },
	    'my12306Tab': {
	    	needLogin: true,
	    	url: 'views/my12306.html'
	    },
	    'moreOptionsTab': {
	    	needLogin: false,
	    	url: 'views/moreOption.html'
	    }
	};
	var footTabIds = [];
	jq.each(footTabMapping, function(key) {
		footTabIds.push(key);
	});
	
	jq('.footer_bar .f_nav ul li a').live("tap", function(e) {
		//e.stopImmediatePropagation();
		var tab = jq(this);
		var id = tab.attr('id');
		if (id === 'bookTicketTab') {
			jq("#bookTicketTab").addClass("ui-btn-active ui-state-persist");	
		}
		if (!id || footTabIds.indexOf(id) < 0) return;
		
		var tabObj = footTabMapping[id];

		if (mor.ticket.viewControl.current_tab == id) {
			return false;
		}
	
		mor.ticket.viewControl.current_tab = id;
		resetTab();
		
		if (tabObj.needLogin) {
			if (mor.ticket.loginUser.isAuthenticated != "Y" && window.ticketStorage.getItem("autologin") != "true") {
				mor.ticket.viewControl.session_out_page= tabObj.url;
				jq.mobile.changePage(getViewBasePath() + "views/loginTicket.html");
				return;
			} 
		}
	    jq.mobile.changePage(getViewBasePath() + tabObj.url);
		return false;
	});
	
}

function resetTab() {
	var activeClass = 'ui-btn-active ui-state-persist';
	//jq("#bookTicketTab").removeClass(activeClass);
	jq("#queryOrderTab").removeClass(activeClass);
	jq("#my12306Tab").removeClass(activeClass);
	jq("#moreOptionsTab").removeClass(activeClass);

}
// invoke when press android's back button

function handleAndriodBackbutton() {
	WL.App.overrideBackButton(backFunc);
}

function backFunc() {
	// 按退出键隐藏键盘,fix issue for android2.3

	if (mor.ticket.util.isAndroid()) {
		if (jq.mobile.activePage.find("input").is(":focus")) {
			jq("input:focus")[0].blur();
			// jq.mobile.activePage.find("input").blur();
			return false;
		}
	}
	WL.SimpleDialog.show("温馨提示", "您是要退出应用吗?", [ {
		text : "取消",
		handler : function() {
		}
	}, {
		text : "确认",
		handler : quitHandler
	} ]);
}
function quitHandler() {
	WL.Logger.debug("quit button pressed");
	WL.Client.logout();
	WL.App.close();
}

function restoreUserNameAndPass() {
	var user = mor.ticket.loginUser;
	// busy.show();
	// mod by yiguo
	if (user["username"]) {
		jq("#usernameInput").val(user["username"]);
	}

	// add by yiguo
	var latestTime = window.ticketStorage.getItem("pwdTime");
	if(latestTime && (Date.now() - latestTime  >= 7*24*3600*1000)){
		return;
	}
	
	if (user.isKeepUserPW === "true" || user.isKeepUserPW == true) {
		WL.EncryptedCache.open("wlkey", true, onOpenOk, onOpenError);
	}

	}

function onOpenOk() {
	// busy.hide();
	WL.EncryptedCache.read("userPW", function(value) {
		// WL.Logger.debug("Successfully read from storage");
		if(value=="*"){
			value="";
		}
		mor.ticket.loginUser.password = value;
		jq("#passwordInput").val(mor.ticket.loginUser.password);
		WL.EncryptedCache.close(function() {/*
		 * WL.Logger.debug("Encrypted Cache
		 * Closed.");
		 */
		}, function() {/* WL.Logger.debug("Failed to close Encrypted Cache."); */
		});
		// WL.Logger.debug('******* resolve util.userDef in OK.');
		mor.ticket.util.userDef.resolve();
	}, function() {/*
	 * WL.Logger.debug("Failed to write to ticketStorage");
	 * WL.Logger.debug('******* resolve util.userDef in read
	 * failed.');
	 */
		mor.ticket.util.userDef.resolve();
	}

	);

};



function prepareCacheMap() {
	// init station map
	var stations = mor.ticket.cache.stations;
	var stationMap = mor.cache.stationMap;
	for ( var i = 0; i < stations.length; i++) {
		stationMap[stations[i].id] = stations[i].value;
	}
	;
	// init province id value
	var provinceList = mor.ticket.cache.province;
	var provinceMap = mor.cache.provinceMap;
	for ( var y = 0; y < provinceList.length; y++) {
		provinceMap[provinceList[y].id] = provinceList[y].value;
	}
	;

}

function initCommonData() {

	// fetch device info
	var common = mor.ticket.common;
	common["baseDTO.os_type"] = deviceDetection();
	if (mor.ticket.util.isPreview) {
		common["baseDTO.device_no"] = '1213123123';
	} else {
		common["baseDTO.device_no"] = device.uuid;
	}
	// alert("device.version"+device.version);
	// convertCity();
	// common["baseDTO.mobile_no"]=device.? to do , can not get user mobile num
	// now
}

function syncLocalCache() {
	// WL.Logger.debug("sysCache called");
	var util = mor.ticket.util;
	/*
	 * var commonParameters = util.prepareRequestCommonParameters(); var
	 * invocationData = { adapter: "CARSMobileServiceAdapter", procedure:
	 * "sysCache", parameters: [commonParameters] };
	 */
	var invocationData = {
		adapter : "CARSMobileServiceAdapter",
		procedure : "sysCache"
	};

	var options = {
		onSuccess : requestSucceeded,
		onFailure : util.creatCommonRequestFailureHandler()
	};
	mor.ticket.viewControl.show_busy = false;
	mor.ticket.util.invokeWLProcedure(null, invocationData, options);
	// WL.Client.invokeProcedure(invocationData, options);
}

function requestSucceeded(result) {
    //add by yiguo
	var jvFmt = mor.ticket.util.jsVersionFormat;
	
	
	var serverCache = result.invocationResult;
	var common = mor.ticket.common;
	common["baseDTO.time_offset"] = Date.now()
			- new Date(serverCache.serverTime);

	// 自动登录
	//mod by yigou
	//registerAutoLoginHandler();
	
	
	window.ticketStorage.setItem("serverTime", serverCache.serverTime);

	var reservePeriodList = serverCache.reservePeriodList;

	for ( var i = 0; i < reservePeriodList.length; i++) {
		var ticket_type = reservePeriodList[i].ticket_type;

		if (ticket_type == "1" || ticket_type == "2" || ticket_type == "3" || ticket_type == "n") {// 只获取到成人（ticket_type=1）的时间
			var util = mor.ticket.util;
			var from_date = util.setMyDate(reservePeriodList[i].from_period);
			var to_date = util.setMyDate(reservePeriodList[i].to_period);
			var reservePeriod = ((to_date - from_date) / 86400000);
			window.ticketStorage.setItem("reservePeriod_" + ticket_type,
					reservePeriod);
		}

		window.ticketStorage.setItem("reservePeriod", window.ticketStorage
				.getItem('reservePeriod_1'));
	}

	// init seat Type
	var seatTypeMap = mor.cache.seatTypeMap;
	var seatsType = serverCache.seatTypeList;
	for ( var i = 0; i < seatsType.length; i++) {
		seatTypeMap[seatsType[i].seat_type_code] = seatsType[i].seat_type_name;
	}
	;

	// init ticket type
	var ticketType = serverCache.ticketTypeList;
	var ticketTypeMap = mor.cache.ticketTypeMap;
	for ( var i = 0; i < ticketType.length; i++) {
		ticketTypeMap[ticketType[i].ticket_type_code] = ticketType[i].ticket_type_name;
	}
	;

	// init id type
	var idTypeList = serverCache.cardTypeList;
	var idTypeMap = mor.cache.idTypeMap;
	for ( var i = 0; i < idTypeList.length; i++) {
		idTypeMap[idTypeList[i].card_type_code] = idTypeList[i].card_type_name;
	}
	;

	
	// check station version
	var station_version = window.ticketStorage.getItem("station_version")
			|| mor.ticket.cache.station_version.toString();
	//TODO remove it
	if(jvFmt(mor.ticket.cache.station_version) > jvFmt(station_version)){
		station_version = mor.ticket.cache.station_version;
	}
	
	if (jvFmt(serverCache.station_version_no) > jvFmt(station_version)) {
		mor.ticket.cache["needSync"] = "Y";
		mor.ticket.cache["syncList"] += "station";
		mor.ticket.cache["syncVersionList"] = station_version;
	} else {
		// match, load data from local storage
		if (window.ticketStorage.getItem("stations") != null && jvFmt(mor.ticket.cache.station_version) < jvFmt(window.ticketStorage.getItem("station_version"))) {
			mor.ticket.cache.stations = JSON.parse(window.ticketStorage
					.getItem("stations"));
		}
		prepareCacheMap();
	}

	// check hot station version and init hot station
	var hotstation_version = window.ticketStorage.getItem("hotstation_version")
			|| mor.ticket.cache.hotStation_version.toString();
	// check if verion match
	if (serverCache.hotstation_version != hotstation_version) {
		// not match, need sync
		mor.ticket.cache["needSync"] = "Y";
		mor.ticket.cache["syncList"] += "|hotstation";
		mor.ticket.cache["syncVersionList"] += "|" + hotstation_version;
	} else {
		// match, load data from local storage
		if (window.ticketStorage.getItem("hot_station") != null) {
			mor.ticket.cache.hotStations = JSON.parse(window.ticketStorage
					.getItem("hot_station"));
		}
	}

	// check city version
	var city_version_no = window.ticketStorage.getItem("city_version")
			|| mor.ticket.cache.city_version.toString();
	
	//TODO remove it
	if(jvFmt(mor.ticket.cache.city_version) > jvFmt(city_version_no)){
		city_version_no = mor.ticket.cache.city_version;
	}
	
	
	if (jvFmt(serverCache.city_version_no) > jvFmt(city_version_no)) {
		mor.ticket.cache["needSync"] = "Y";
		mor.ticket.cache["syncList"] += "|city";
		mor.ticket.cache["syncVersionList"] += "|" + city_version_no;
	} else {
		// match, load data from local storage
		if (window.ticketStorage.getItem("city") != null && jvFmt(mor.ticket.cache.city_version) < jvFmt(window.ticketStorage.getItem("city_version"))) {
			mor.ticket.cache.city = JSON.parse(window.ticketStorage
					.getItem("city"));
		}
	}

	// check university version
	var university_version_no = window.ticketStorage
			.getItem("university_version")
			|| mor.ticket.cache.university_version.toString();
	//TODO remove it
	if(jvFmt(mor.ticket.cache.university_version) > jvFmt(university_version_no)){
		university_version_no = mor.ticket.cache.university_version;
	}
	
	
	if (jvFmt(serverCache.university_version_no) > jvFmt(university_version_no)) {
		mor.ticket.cache["needSync"] = "Y";
		mor.ticket.cache["syncList"] += "|university";
		mor.ticket.cache["syncVersionList"] += "|" + university_version_no;
	} else {
		// match, load data from local storage
		if (window.ticketStorage.getItem("university") != null  && jvFmt(mor.ticket.cache.university_version) < jvFmt(window.ticketStorage.getItem("university_version"))) {
			mor.ticket.cache.university = JSON.parse(window.ticketStorage
					.getItem("university"));
		}
	}

	// sync with server with unmatched version
	if (mor.ticket.cache["needSync"] == "Y") {
		var util = mor.ticket.util;
		/*
		 * var commonParameters = util.prepareRequestCommonParameters({
		 * 'syncList': mor.ticket.cache["syncList"], 'syncVersionList':
		 * mor.ticket.cache["syncVersionList"], }); var invocationData = {
		 * adapter: "CARSMobileServiceAdapter", procedure: "syncCache",
		 * parameters: [commonParameters] };
		 */
		var commonParameters = {
			'syncList' : mor.ticket.cache["syncList"],
			'syncVersionList' : mor.ticket.cache["syncVersionList"],
		};
		var invocationData = {
			adapter : "CARSMobileServiceAdapter",
			procedure : "syncCache"
		};
		var options = {
			onSuccess : syncSucceeded,
			onFailure : util.creatCommonRequestFailureHandler()
		};

		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData,
				options, true);
		// WL.Client.invokeProcedure(invocationData, options);
	} else {
		// WL.Logger.debug('******* resolve util.syncDef in adapter1.');
		mor.ticket.util.syncDef.resolve();
	}
}

function syncSucceeded(result) {

	// WL.Logger.debug(result);
	// prepare station and pinying
	if (result.invocationResult.station) {
		var stations = result.invocationResult.station;
		var version_no = result.invocationResult.station_v;
		mor.ticket.cache.stations = [];
		var cache_station = mor.ticket.cache.stations;
		for ( var i = 0; i < stations.length; i++) {
			cache_station[i] = {};
			cache_station[i]["id"] = stations[i].id;
			cache_station[i]["value"] = stations[i].value;
			jq("#py").val(stations[i].value);
			var pingyin = jq("#py").toPinyin();
			cache_station[i].pinyin = pingyin.toLowerCase();
			cache_station[i]["valueSM"] = generatePinYing(stations[i].value);
		}
		window.ticketStorage.setItem("stations", JSON.stringify(cache_station));
		window.ticketStorage.setItem("station_version", version_no);
		// WL.Logger.debug("staion list synced");
		prepareCacheMap();
	}
	// prepare hot station and sort
	if (result.invocationResult.hotStation) {
		var hotStations = result.invocationResult.hotStation;
		var version_no = result.invocationResult.hotStation_v;
		hotStations.sort(sortHotStationfunction);
		mor.ticket.cache.hotStations = [];
		var cache_hotStation = mor.ticket.cache.hotStations;
		var cache = mor.ticket.cache;
		for ( var i = 0; i < hotStations.length; i++) {
			cache_hotStation[i] = {};
			cache_hotStation[i]["id"] = hotStations[i].tel_code;
			cache_hotStation[i]["value"] = cache
					.getStationNameByCode(hotStations[i].tel_code);
		}

		window.ticketStorage.setItem("hot_station", JSON
				.stringify(cache_hotStation));
		window.ticketStorage.setItem("hotstation_version", version_no);
		// WL.Logger.debug("hot staion list synced");
	}
	// prepare city and pinying
	if (result.invocationResult.city) {
		var city = result.invocationResult.city;
		var version_no = result.invocationResult.city_v;
		mor.ticket.cache.city = [];
		var cache_city = mor.ticket.cache.city;
		for ( var i = 0; i < city.length; i++) {
			cache_city[i] = {};
			cache_city[i]["city_code"] = city[i].city_code;
			cache_city[i]["city_name"] = city[i].city_name;
			jq("#py").val(city[i].city_name);
			var pingyin = jq("#py").toPinyin();
			cache_city[i].pinyin = pingyin.toLowerCase();
			cache_city[i]["valueSM"] = generatePinYing(city[i].city_name);
		}
		window.ticketStorage.setItem("city", JSON.stringify(cache_city));
		window.ticketStorage.setItem("city_version", version_no);
		// WL.Logger.debug("city list synced");
	}
	// prepare university and pinying
	if (result.invocationResult.university) {
		var university = result.invocationResult.university;
		var version_no = result.invocationResult.university_v;
		mor.ticket.cache.university = [];
		var cache_university = mor.ticket.cache.university;
		for ( var i = 0; i < university.length; i++) {
			cache_university[i] = {};
			cache_university[i]["province_code"] = university[i].province_code;
			cache_university[i]["university_code"] = university[i].university_code;
			cache_university[i]["university_name"] = university[i].university_name;
			jq("#py").val(university[i].university_name);
			var pingyin = jq("#py").toPinyin();
			cache_university[i].pinyin = pingyin.toLowerCase();
		}
		window.ticketStorage.setItem("university", JSON
				.stringify(cache_university));
		window.ticketStorage.setItem("university_version", version_no);
		// WL.Logger.debug("university list synced");
	}

	mor.ticket.cache["needSync"] = "N";
	// WL.Logger.debug('******* resolve util.syncDef in adapter2.');
	mor.ticket.util.syncDef.resolve();
}

function sortHotStationfunction(x, y) {
	if (x.hot_class == y.hot_class) {
		return x.tel_code.charCodeAt(0) - y.tel_code.charCodeAt(0);
	} else {
		return y.hot_class - x.hot_class;
	}

}

function generatePinYing(input) {
	var reg = /^[A-Z]+/g;
	jq("#py").val(input);
	var pingyin = jq("#py").toPinyin();
	var sm = '';
	for ( var index = 0; index < pingyin.length; index++) {
		if (pingyin.charAt(index).match(reg)) {
			sm += pingyin.charAt(index);
		}
	}
	return sm;
}

function deviceDetection() {
	switch (WL.Client.getEnvironment()) {
	case "android":
		return "a";
		break;
	case "iphone":
		return "i";
		break;
	default:
		return "a";
	}
}

// 以下为自动登录

// 自动登录

function registerAutoLoginHandler(suc, fail ) {
	var autologin = window.ticketStorage.getItem("autologin");

	if (autologin == 'true') {

		AutoRestoreUserNameAndPass(suc, fail);
	}
	/*
	 * else{ //by yiguo WL.Logger.error( "当前页面#" +
	 * jq.mobile.activePage.attr("data-url"));
	 * if(jq.mobile.activePage.attr("data-url").indexOf("loginTicket") == -1 &&
	 * jq.mobile.activePage.attr("data-url").indexOf("bookTicket") == -1){
	 * //mor.ticket.util.timeoutLogin(); jq.mobile.changePage(vPathCallBack() +
	 * "loginTicket.html"); if (busy.isVisible()) { busy.hide(); } } }
	 */
	return;

}

function AutoSendLoginRequest(suc, fail) {
	var _cfail = mor.ticket.util.creatCommonRequestFailureHandler();
	function _suc(result){
		AutoRequestSucceeded(result,suc,fail);
	}

	function _fail(result){
		_cfail(result);
		fail();
	}
	
	var util = mor.ticket.util;
	if(mor.ticket.loginUser.password  == "*"){
		autologinFailJump();
		return;
	}
	var commonParameters = {
		'baseDTO.user_name' : mor.ticket.loginUser.username,
		'password' : hex_md5(mor.ticket.loginUser.password)
	// 'autologinChkbox': jq("#autologinChkbox").val()
	};

	var invocationData = {
		adapter : "CARSMobileServiceAdapter",
		procedure : "login"
	};

	var options = {
		onSuccess : _suc,
		onFailure: _fail//util.creatCommonRequestFailureHandler()
	};

	mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	return false;
}

function AutoRequestSucceeded(result, suc ,fail) {

	if (busy.isVisible()) {
		busy.hide();
	}

	var invocationResult = result.invocationResult;
	if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
		mor.ticket.loginUser["isAuthenticated"] = "Y";
		if (mor.ticket.loginUser["username"] == null
				|| mor.ticket.loginUser["username"] == '') {
			mor.ticket.loginUser["username"] = window.ticketStorage
					.getItem("username");
		}

		var loginUser = mor.ticket.loginUser;
		loginUser.accountName = invocationResult.user_name;
		loginUser.realName = invocationResult.name;
		loginUser.id_type = invocationResult.id_type_code;
		loginUser.id_no = invocationResult.id_no;
		loginUser.mobile_no = invocationResult.mobileNo;
		loginUser.user_type = invocationResult.user_type;
		loginUser.email = invocationResult.email;
		loginUser.activeUser = invocationResult.is_active;
		// init passengerinfo

		if (invocationResult.passengerResult) {

			mor.ticket.passengersCache.passengers = [];


			mor.ticket.passengersCache.passengers = invocationResult.passengerResult;

			mor.ticket.passengersCache.sortPassengers();
			// WL.Logger.debug("Successfully 5");
		}
		suc && suc();
		return;
	}
	fail && fail();
	return;

}

function AutoRestoreUserNameAndPass(suc,fail) {
	
	var user = mor.ticket.loginUser;
	if (user.isKeepUserPW === "true" || user.isKeepUserPW == true) {
		user.username = window.ticketStorage.getItem("username");
        if(user.username){
		WL.EncryptedCache.open("wlkey", true,function(){ AutoOnOpenOk(suc,fail)}, function(){onOpenError(suc,fail)});}else{
			fail();
		}
	}else{
		fail();
	}

}


function AutoOnOpenOk(suc,fail) {
	WL.EncryptedCache
			.read(
					"userPW",
					function(value) {
						mor.ticket.loginUser.password = value;
						//by yiguo
						if(value){
						AutoSendLoginRequest(suc,fail);
						}else{
							fail();
						}

					},
					function() {
						fail();
						WL.Logger.debug("Failed to write to ticketStorage");
						WL.Logger.debug('*******  resolve util.userDef in read failed.');
						// mor.ticket.util.userDef.resolve();
					}
			);

}

function onOpenError(status) {
	// busy.hide();
	// WL.Logger.debug("Can not open Encrypted Cache.");
	// WL.Logger.debug('******* resolve util.userDef in open failed.');
	mor.ticket.util.userDef.resolve();
}


//add by yiguo
function autologinFailJump(){
	jq.mobile.changePage(vPathCallBack() + "loginTicket.html");
	}

// 以上为自动登录 另外定制

/*
 * function convertCity(){ var cache_city = []; var city =
 * mor.ticket.cache.city; for(var i=0;i < city.length;i++){ cache_city[i] = {};
 * cache_city[i].city_code = city[i].city_code; cache_city[i].city_name =
 * city[i].city_name; cache_city[i].valueSM =
 * generatePinYing(city[i].city_name); } WL.Logger.debug("CITY IS " +
 * JSON.stringify(cache_city)); // mor.ticket.cache.city = cache_city;
 * window.ticketStorage.setItem("city",JSON.stringify(cache_city)); }
 */


/* JavaScript content from js/MobileTicket.js in folder android */
/**
 *  @license
 *  Licensed Materials - Property of IBM
 *  5725-G92 (C) Copyright IBM Corp. 2011, 2013. All Rights Reserved.
 *  US Government Users Restricted Rights - Use, duplication or
 *  disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

// This method is invoked after loading the main HTML and successful initialization of the Worklight runtime.
function wlEnvInit(){
    wlCommonInit();
    // Environment initialization code goes here
}