
/* JavaScript content from js/controller/selectStation.js in folder common */
(function() {

	jq("#selectStationView").live("pageinit", function() {
													   
													   
		jq("#selectStation_content", this).bind({
			"iscroll_onpulldown" : onPullDown,
			"iscroll_onpullup" : onPullUp,
			"iscroll_onpullupreset":onpullupreset //will fire after iscroll refresh no matter pull up or pull down
		});
		
		

		
		sortStation(mor.ticket.cache.stations);
		
		registerSearchTrainInputChangeHandler();
		registerStationListItemClickHandler();
		registeroftenTabHandler();
		registerhotStaTabHandler();
		registerdetailTabHandler();

		jq("#selectStationBackBtn").bind("tap", function() {
			jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
		});
		

		generateAnchor();
		anchorA2ZStation();
	});

	jq("#selectStationView").live("pagebeforeshow",	function(e, data) {
															 
															 
		setTimeout(refreshoftenTab,0);
		/*if(mor.ticket.loginUser.isAuthenticated === "Y"){
			//jq("selectStationView .iscroll-pulldown").hide();
	        //sortStation(mor.ticket.cache.stations);
			
		}else{
			
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}*/
	});
	
	function generateAnchor(){
		jq("#sliderNav ul").html(station_navList(mor.ticket.cache.stations));
		var size = jq("#sliderNav ul li").length;
		//solve many alpha index on ANDROID DEVICE  
		var deviceHeight = document.documentElement.clientHeight;
		var headerHeight = 44;
		var footerHeight = 50;
		var topHeight = 80;
		var sliderHeight = deviceHeight - footerHeight - topHeight - headerHeight;
		jq('#sliderNav ul').height(sliderHeight);
		jq("#sliderNav ul li").height(sliderHeight/size);
	}

	function registerSearchTrainInputChangeHandler() {
		// add by kevin to accomplish the function of chinese suggestion
		jq("#searchStationInput").on("input", function(e) {
			e.stopImmediatePropagation();
			var key = jq(this).val();
			searchAndUpdateList(key);
			mor.ticket.viewControl.station_searchClean = false;
		    mor.ticket.viewControl.isStationNevClick = false;
			return false;
		});
		jq("#searchStationInput").on("change", function(e) {
			e.stopImmediatePropagation();
			var key = jq(this).val();
			jq("#detailTab").addClass("first-click");
			searchAndUpdateList(key);
			mor.ticket.viewControl.station_searchClean = true;
		    mor.ticket.viewControl.isStationNevClick = false;
			return false;
		});
	}
	function registeroftenTabHandler() {
		jq("#oftenTab").bind("tap",	function(e) {
			e.stopImmediatePropagation();
			changeScrollUI();
			jq("#detailTab").removeClass("ui-btn-active ui-state-persist");
			jq("#detailTab").addClass("first-click");
			jq("#hotStaTab").removeClass("ui-btn-active ui-state-persist");
			jq(this).addClass("ui-btn-active ui-state-persist");
			mor.ticket.viewControl.station_searchClean = false;
		    mor.ticket.viewControl.isStationNevClick = false;
			var recentStationCodeList = window.ticketStorage
					.getItem("recentStation") == null ? [] : JSON
					.parse(ticketStorage.getItem("recentStation"));
			recentStationCodeList.reverse();
			jq("#stationList").html(generateStationRecentList(recentStationCodeList)).listview("refresh");
			jq("#sliderNav").hide();
			//jq("#selectStation_content").iscrollview("refresh");
			return false;
		});
	}
	
	function registerhotStaTabHandler() {
		jq("#hotStaTab").bind("tap", function(e) {
			e.stopImmediatePropagation();
			changeScrollUI();
			jq("#oftenTab").removeClass("ui-btn-active ui-state-persist");
			jq("#detailTab").removeClass("ui-btn-active ui-state-persist");
			jq("#detailTab").addClass("first-click");
			jq(this).addClass("ui-btn-active ui-state-persist");
			mor.ticket.viewControl.station_searchClean = false;
		    mor.ticket.viewControl.isStationNevClick = false;
			jq("#stationList").html(generateStationHotList(mor.ticket.cache.hotStations)).listview("refresh");
			jq("#sliderNav").hide();
			//jq("#selectStation_content").iscrollview("resizeWrapper");
			return false;
		});
	}

	function registerStationListItemClickHandler() {
		jq("#stationList").off().on("tap", "li",function(e) {
			e.stopImmediatePropagation();
			if (jq(this).attr("data-role") != null
					&& jq(this).attr("data-role") == 'list-divider') {
				jq(this).removeClass("ui-btn-active");
				return;
			}
			var id = jq(this).attr("data-id");
			var model = mor.ticket.leftTicketQuery;
			if (mor.ticket.views.selectStation.isFromStation) {
				model.from_station_telecode = id;
			} else {
				model.to_station_telecode = id;
			}
			
			
			mor.ticket.viewControl.station_searchClean = false;
		    mor.ticket.viewControl.isStationNevClick = false;
			
			
			// 修改
			jq.mobile.changePage( vPathViewCallBack()+"MobileTicket.html");
			
			return false;
		});
	}
	
	
	function registerdetailTabHandler() {
		jq("#detailTab").bind("tap", function(e) {
			e.stopImmediatePropagation();
			changeScrollUI();
			jq("#oftenTab").removeClass("ui-btn-active ui-state-persist");
			jq("#hotStaTab").removeClass("ui-btn-active ui-state-persist");
			jq(this).addClass("ui-btn-active ui-state-persist");
			mor.ticket.viewControl.station_searchClean = false;
		    mor.ticket.viewControl.isStationNevClick = false;
			mor.ticket.viewControl.station_num = -1;
			var stations = fetchStationSubSet(true);
			
			jq("#stationList").html(generateStationDetailList(stations)).listview("refresh");
			jq("#sliderNav").show();
			//jq("#selectStation_content").iscrollview("resizeWrapper");
			return false;
		});
	}
	
	// 刷新常用车站列表
	function refreshoftenTab() {
		var recentStationCodeLists = window.ticketStorage
				.getItem("recentStation") == null ? [] : JSON
				.parse(ticketStorage.getItem("recentStation"));
		var recentStationCodeList = [];
		for ( var i = 0; i < recentStationCodeLists.length; i++) {
			var station_name = mor.ticket.cache
					.getStationNameByCode(recentStationCodeLists[i]);
			if (station_name == undefined) {
				continue;
			}
			recentStationCodeList.push(recentStationCodeLists[i]);
		}
		if(recentStationCodeLists.length>recentStationCodeList.length){
			window.ticketStorage.setItem("recentStation", JSON.stringify(recentStationCodeList));
		}
		recentStationCodeList.reverse();
		jq("#stationList").html(generateStationRecentList(recentStationCodeList)).listview("refresh");
		jq("#detailTab").addClass("first-click");
	    mor.ticket.viewControl.isStationNevClick = false;
		jq("#sliderNav").hide();
	}
	
/*	function sortStation(stations) {
		stations.sort(sortfunction);
	}

	function sortfunction(x, y) {
		return x.valueSM.charCodeAt(0) - y.valueSM.charCodeAt(0);
	}*/
	
	function sortStation(stations){
		var by = function(name)
		{
			return function(o, p)
			{
				var a, b;
				if (typeof o === "object" && typeof p === "object" && o && p) 
				{
					a = o[name];
					b = p[name];
					if (a === b) {return 0;}
					if (typeof a === typeof b) { return a > b ? 1 : -1;}
					return typeof a > typeof b ? 1 : -1;
				}
				else {throw ("error"); }
			};
		};

		stations.sort(by("pinyin"));
		return stations;
	}

	function searchAndUpdateList(key) {
		changeScrollUI();
		if (!key) {
			jq("#selectStationNavbar").show().addClass("iscroll-fixed");
			jq("#sliderNav").show();
			
			jq("#detailTab").addClass("ui-btn-active ui-state-persist");
			jq("#oftenTab").removeClass("ui-btn-active ui-state-persist");
			jq("#hotStaTab").removeClass("ui-btn-active ui-state-persist");
			mor.ticket.viewControl.station_num = -1;
			var stations = fetchStationSubSet(true);
			jq("#stationList").html(generateStationDetailList(stations)).listview("refresh");
			
		} else {
			jq("#selectStationNavbar").hide().removeClass("iscroll-fixed");
			jq("#sliderNav").show();
			
			var results = jq.grep(mor.ticket.cache.stations, function(station,
					index, pinyin) {
				return station.valueSM.indexOf(key.toUpperCase()) === 0
						|| station.value.indexOf(key) === 0 || station.pinyin.indexOf(key.toLowerCase()) === 0;
			});
			jq("#stationList").html(generateStationDetailList(results)).listview("refresh");
			
		}
		jq("#selectStation_content").iscrollview("resizeWrapper");
		jq("#selectStation_content").iscrollview("refresh");
	}
	
	function onPullDown() {
		var viewControl = mor.ticket.viewControl;
		viewControl.station_searchClean = false;
	    viewControl.isStationNevClick = false;
		if (jq("#oftenTab").hasClass("ui-btn-active")
				|| jq("#hotStaTab").hasClass("ui-btn-active")) {
			jq("#stationList").css("margin-top", "0px");
		}
		if (jq("#detailTab").hasClass("ui-btn-active")) {
			if (jq("#searchStationInput").val() == "") {
				var station_show = viewControl.station_show;
				var cache = mor.ticket.cache;
				if(station_show[0].id != cache.stations[0].id){//判断station是否到头
					if (!busy.isVisible()) {
						busy.show();
					}
				}
				if (viewControl.station_num < viewControl.station_show_num + viewControl.station_keep_num) {
					viewControl.station_num = viewControl.station_show_num;
				} else {
					viewControl.station_num -= (viewControl.station_show_num - viewControl.station_keep_num);
				}
				var stations = fetchStationSubSet(false);
				if (viewControl.station_num <= viewControl.station_show_num)
					jq("#stationList").html(generateStationDetailList(stations)).listview("refresh");
				else
					jq("#stationList").html(generateStationPullList(stations)).listview("refresh");				
				viewControl.station_is_up = false;
			} else {
				jq("#stationList").css("margin-top", "0px");
			}
		}
	}
	
	function onPullUp(e,d) {
		var viewControl = mor.ticket.viewControl;
		viewControl.station_searchClean = false;
	    viewControl.isStationNevClick = false;
		if (jq("#detailTab").hasClass("ui-btn-active")) {
			if (jq("#searchStationInput").val() == "") {
				var stations = viewControl.station_show;
				var cache = mor.ticket.cache;
				if(stations[stations.length-1].id != cache.stations[cache.stations.length-1].id){
					if (!busy.isVisible()) {
						busy.show();
					}
					viewControl.station_num -= viewControl.station_keep_num;	
					stations = fetchStationSubSet(true);
					jq("#stationList").html(generateStationPullList(stations)).listview("refresh");
				}
				viewControl.station_is_up = true;
			}
		}
	}
	
	function onpullupreset(e,d){
		var viewControl = mor.ticket.viewControl;
		if(viewControl.isStationNevClick){
			dis = jq("#selectStationView .iscroll-scroller").position().top;
		    jq("#selectStation_content").iscrollview("scrollTo", 0, dis, 0, true);
		    return;
		}
		if(viewControl.station_searchClean){
			dis = jq("#selectStationView .iscroll-scroller").position().top;
		    jq("#selectStation_content").iscrollview("scrollTo", 0, dis, 0, true);
		    return;
		}
		var keepContentHeight = (viewControl.station_keep_num - 1)*35;
		if (jq("#detailTab").hasClass("ui-btn-active") && viewControl.station_is_up) {
			if (jq("#searchStationInput").val() == "") {
				var dis;
				if(jq("#detailTab").hasClass("first-click")){
					dis = jq("#selectStationView .iscroll-scroller").position().top;
					jq("#detailTab").removeClass("first-click");
				}else{
					dis = jq("#selectStationView .iscroll-scroller").position().top + keepContentHeight;
				}
				jq("#selectStation_content").iscrollview("scrollTo", 0, dis, 0, true);
			}
		}else if(jq("#detailTab").hasClass("ui-btn-active") && !viewControl.station_is_up){
			if (jq("#searchStationInput").val() == "") {
				var dis;
				if (viewControl.station_num <= viewControl.station_show_num){
					dis = 0;
				}else{
					var deviceHeight = document.body.clientHeight + window.screenTop;
					var headerHeight = 44;
					var footerHeight = 50;
					var searchHeight = 40;
					var buttonTabHeight = 39;
					var contentHeight = deviceHeight - footerHeight - headerHeight - searchHeight -buttonTabHeight;
					var pullDownHeight = 40;			
					dis = jq("#selectStationView .iscroll-scroller").height() - pullDownHeight - keepContentHeight - contentHeight;				
				}	
				jq("#selectStation_content").iscrollview("scrollTo", 0, dis, 0, true);
			}
		}
		else{
			jq("#stationList").attr("style","margin-top:40px;margin-bottom: 40px;");
		    dis = jq("#selectStationView .iscroll-scroller").position().top;
		    jq("#selectStation_content").iscrollview("scrollTo", 0, dis, 0, true);
		}
		setTimeout(function(){
			if(busy.isVisible()){
				busy.hide();
			}
    	}, 500);
		return false;
	}

	function fetchStationSubSet(forward) {
		var start = 0;
		var end = 0;
		var viewControl = mor.ticket.viewControl;
		if (forward) {
			start = viewControl.station_num;
			end = start + viewControl.station_show_num + 1;
			var length = mor.ticket.cache.stations.length;
			//if the end num bigger then stations.length recode end else get station.length
			if(end <= length){
				viewControl.station_num = end;
			}else{
				end = length;
				viewControl.station_num = length;
			}
			
		} else {
			end = viewControl.station_num;
			start = end - viewControl.station_show_num - 1;
			viewControl.station_num = end;
		}

		var filteredResult = mor.ticket.cache.stations.filter(function(item,
				index, array) {
			return (index > start && index < end);
		});
		viewControl.station_show = filteredResult;
		return filteredResult;
	}
	
	function changeScrollUI() {
		jq("#stationList").css("margin-top", "40px");
		jq("#stationList").css("margin-bottom", "40px");
	}
	
	function anchorA2ZStation() {
		var arr = [ 'AAnchor', 'BAnchor', 'CAnchor', 'DAnchor', 'EAnchor',
				'FAnchor', 'GAnchor', 'HAnchor', 'JAnchor',
				'KAnchor', 'LAnchor', 'MAnchor', 'NAnchor', 
				'PAnchor', 'QAnchor', 'RAnchor', 'SAnchor', 'TAnchor',
			    'WAnchor', 'XAnchor', 'YAnchor', 'ZAnchor' ];
		for ( var i = 0; i < arr.length; i++) {
			jq("#selectStationView #" + arr[i]).live("tap", function() {
				if (jq("#searchStationInput").val() != "") {
					jq("#searchStationInput").val("");
					jq("#searchStationInput").change();
				}
				searchAndUpdateList2(jq(this).attr("id").charAt(0));
				mor.ticket.viewControl.isStationNevClick = true;
				mor.ticket.viewControl.station_searchClean = false;
				return false;
			});
		}
	}
	function searchAndUpdateList2(key) {
		if (!key) {
			return;
		} else {
			var viewControl = mor.ticket.viewControl;
			for ( var i = 0, l = mor.ticket.cache.stations.length; i < l; i++) {
				if (mor.ticket.cache.stations[i].valueSM.indexOf(key
						.toUpperCase()) === 0) {
					viewControl.station_num = i-1;
					break;
				}
			}
			var stations = fetchStationSubSet(true);
			jq("#stationList").html(generateStationDetailList(stations)).listview("refresh");
			var dis = jq("#selectStationView .iscroll-scroller").position().top;
			jq("#selectStation_content").iscrollview("scrollTo", 0, dis, 0, true);
		}
	}
	

	var stationListOftenTemplate = "{{ for (var i = 0, l = it.length; i<l; i++) { }}"
			+ "<li data-id='{{=it[i]}}'><a>{{=mor.ticket.cache.getStationNameByCode(it[i])}}</a></li>"
			+ "{{}}}";
	var generateStationRecentList = doT.template(stationListOftenTemplate);

	var stationListHotTemplate = "{{ for (var i = 0, l = it.length; i < l; i++) { }}"
			+ "{{if(it[i].value != undefined){ }}"
			+ "<li data-id='{{=it[i].id}}'>{{=it[i].value}}</li>" 
			+ "{{ } } }}";
	var generateStationHotList = doT.template(stationListHotTemplate);

	var stationListDetailTemplate = "{{ var viewControl = mor.ticket.viewControl; }}"
			+ "{{ for (var i = 0, l = it.length; i<(l<viewControl.station_show_num?l:viewControl.station_show_num); i++) { }}"
			+ "{{if(i==0){}}"
			+ "<li data-role='list-divider' id='{{=it[i].valueSM.charAt(0)}}'>{{=it[i].valueSM.charAt(0)}}</li>"
			+ "{{}}}"
			+ "<li data-id='{{=it[i].id}}' data-filtertext='{{=it[i].pinyin}}'><a>{{=it[i].value}}</a></li>"
			+ "{{if((i+1)<l && it[i].valueSM.charCodeAt(0)!=it[i+1].valueSM.charCodeAt(0)){}}"
			+ "<li data-role='list-divider' id='{{=it[i+1].valueSM.charAt(0)}}'>{{=it[i+1].valueSM.charAt(0)}}</li>"
			+ "{{}}}" + "{{}}}";
	var generateStationDetailList = doT.template(stationListDetailTemplate);

	var stationListCrossTemplate = "{{ var viewControl = mor.ticket.viewControl; }}"
			+ "{{ for (var i = 0, l = it.length; i<(l<viewControl.station_show_num?l:viewControl.station_show_num); i++) { }}"
			+ "<li data-id='{{=it[i].id}}' data-filtertext='{{=it[i].pinyin}}'><a>{{=it[i].value}}</a></li>"
			+ "{{if((i+1)<l && it[i].valueSM.charCodeAt(0)!=it[i+1].valueSM.charCodeAt(0)){}}"
			+ "<li data-role='list-divider' id='{{=it[i+1].valueSM.charAt(0)}}'>{{=it[i+1].valueSM.charAt(0)}}</li>"
			+ "{{}}}" + "{{}}}";
	var generateStationPullList = doT.template(stationListCrossTemplate);
	
	var station_navListTempate = 
		"{{ var anchor,last_anchor; }}"+
		"{{ for (var i = 0; i < it.length; i++) { }}" +
			"{{ anchor=it[i].valueSM.charAt(0);if(anchor!=last_anchor){ }}" +
			 		"<li><a id='{{=anchor}}Anchor' href='#{{=anchor}}' class='anchor'>{{=anchor}}</a></li>"+
			 "{{ last_anchor=anchor;} }}"+
		"{{}}}";
	var station_navList = doT.template(station_navListTempate);
	// Detail and Cross not join together because search result's
	// mor.ticket.viewControl.station_num will be larger than 21,so can not show the divider
})();