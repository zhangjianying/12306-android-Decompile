
/* JavaScript content from js/controller/stationSet.js in folder common */
(function(){
	jq("#stationSetView").live("pageinit", function(){
		sortStation(mor.ticket.cache.stations);
		generateAnchor();
	});
	
	jq("#stationSetView").live("pagebeforeshow", function(){
		jq("#selectStation_content", this).bind({
			"iscroll_onpulldown" : onPullDown,
			"iscroll_onpullup" : onPullUp,
			"iscroll_onpullupreset" : onPullUpReset
		});
		registerSearchTrainInputChangeHandler();
		
		registerStationListItemClickHandler();
		
		
		StaticBackBtnSeatBtnListeners();
		
		anchorA2ZStation();
		
		
		/*
		   if(mor.ticket.cache.stations[0].valueSM==null||mor.ticket.cache.stations[0].valueSM==''){
			jq.mobile.showPageLoadingMsg();
			var reg  = /^[A-Z]+/g;
			for(var i=0;i<mor.ticket.cache.stations.length;i++){
				jq("#pingyinSet").val(mor.ticket.cache.stations[i].value);
				
				var pingyin = jq("#pingyinSet").toPinyin();
				
				var sm = '';
				for (var index=0; index<pingyin.length; index++) 
				{
					if(pingyin.charAt(index).match(reg)){
						sm += pingyin.charAt(index);
						mor.ticket.cache.stations[i].valueSM = sm;
					}
				}				
	      }
			jq.mobile.hidePageLoadingMsg();
		}*/
		//sortStation(mor.ticket.cache.stations);
		//jq("#stationSetList").css("margin-top", "40px");
		jq("#stationSetList").html(generateStationSetTpl(mor.ticket.cache)).listview("refresh");
	});
	
	function generateAnchor() {
		
		jq("#sliderSetNav ul").html(stationSet_navList(mor.ticket.cache));
		var size = jq("#sliderSetNav ul li").length;

		var deviceHeight = document.documentElement.clientHeight;
		var headerHeight = 44;
		var footerHeight = 50;
		var topHeight = 40;
		var sliderHeight = deviceHeight - footerHeight - topHeight - headerHeight;
		jq("#sliderSetNav ul li").height(sliderHeight/size);
	}
	
	
	
	
	
	
	function onPullDown() {
//		if (jq("#oftenTab").hasClass("ui-btn-active")
//				|| jq("#hotStaTab").hasClass("ui-btn-active")) {
//			jq("#stationList").css("margin-top", "0px");
//		}
//		if (jq("#detailTab").hasClass("ui-btn-active")) {
		//jq("#stationSetList").css("margin-top", "40px");
			if (jq("#searchStationSetInput").val() == "") {
				if (!busy.isVisible()) {
					busy.show();
				}
				if (mor.ticket.viewControl.station_num < 20) {
					mor.ticket.viewControl.station_num = 10;
				} else {
					mor.ticket.viewControl.station_num -= 10;
				}
				var stations = fetchStationSubSet(false);
				if (mor.ticket.viewControl.station_num < 11)
					jq("#stationSetList").html(generateStationSetTpl(mor.ticket.cache)).listview("refresh");
				else
					jq("#stationSetList").html(generateStationPullSetList(stations)).listview("refresh");
			} else {
				//jq("#stationSetList").css("margin-top", "40px");
			}

		}
//	}
	
	function onPullUp() {
//		if (jq("#detailTab").hasClass("ui-btn-active")) {
			if (jq("#searchStationSetInput").val() == "") {
				if (!busy.isVisible()) {
					busy.show();
				}
				mor.ticket.viewControl.station_num -= 10;
				var stations = fetchStationSubSet(true);
				jq("#stationSetList").html(generateStationPullSetList(stations))
						.listview("refresh");
			}
		}
	
	function onPullUpReset(){
		setTimeout(function(){
			if(busy.isVisible()){
				busy.hide();
			}
    	}, 500);
	}
	
	function fetchStationSubSet(forward) {
		var start = 0;
		var end = 0;
		if (forward) {
			start = mor.ticket.viewControl.station_num;
			end = start + 21;
			mor.ticket.viewControl.station_num = end;
		} else {
			end = mor.ticket.viewControl.station_num;
			start = end - 21;
			mor.ticket.viewControl.station_num = end;
		}

		var filteredResult = mor.ticket.cache.stations.filter(function(item,
				index, array) {
			return (index > start && index < end);
		});

		return filteredResult;
	}
//	}
	
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
			jq("#stationSetList").html(generateStationSetTpl(stations)).listview("refresh");
			var dis = jq("#stationSetView .iscroll-scroller").position().top;
			jq("#selectStation_content").iscrollview("scrollTo", 0, dis, 0, true);
		}
	}
	
/*	function registerStationListFilterCallback(){
		jq("#stationSetList").listview('option', 'filterCallback', function(text, searchValue){
			return text.toLowerCase().indexOf(searchValue) === -1;
		});
	};*/
	
	function anchorA2ZStation(){
		var arr = ['OAnchor','AAnchor','BAnchor','CAnchor','DAnchor','EAnchor','FAnchor','GAnchor','HAnchor' ,'JAnchor','KAnchor','LAnchor','MAnchor','NAnchor','PAnchor','QAnchor','RAnchor','SAnchor','TAnchor','WAnchor','XAnchor','YAnchor','ZAnchor'];
		for(var i=0;i<arr.length;i++){
			jq("#stationSetView #"+arr[i]).off().on("tap",function(){
				searchAndUpdateList(jq(this).attr("id").charAt(0));
				return false;
			});
		}

	}
	
	function registerSearchTrainInputChangeHandler(){
		jq(document).on("input", "#searchStationSetInput", function(e) {
			e.stopImmediatePropagation();
			var key = jq(this).val();
			searchAndUpdateList(key);
			return false;
		});
		jq(document).on("change", "#searchStationSetInput", function(e) {
			e.stopImmediatePropagation();
			var key = jq(this).val();
			searchAndUpdateList(key);
			return false;
		});
	};
	
	function registerStationListItemClickHandler(){
		jq("#stationSetList").off().on("tap", "li", function(e){
			e.stopImmediatePropagation();
			if(jq(this).attr("data-role")!=null&&jq(this).attr("data-role")=='list-divider'){
				return;
			}
			var id = jq(this).attr("data-id");
			if (mor.ticket.views.selectStation.isFromStation){
				window.localStorage.setItem("set_from_station_telecode",id);
			} else {
				window.localStorage.setItem("set_to_station_telecode",id);
			};
			
			
		    
			jq.mobile.changePage("settings.html");
			/*if(mor.ticket.util.isAndroid()&& (parseFloat(device.version) > 3.0)){
		    	mor.ticket.util.transitionFlag = true;
		    	setTimeout(function(){
		    		if(mor.ticket.util.transitionFlag){
		    			history.back();
		    			mor.ticket.util.transitionFlag = false;
		    		}
		    	}, 300);
		    }else{
		    	history.back();
		    }*/

			return false;
		});
	};
	
	
	
	
		function StaticBackBtnSeatBtnListeners(){
			
			jq("#StaticBackBtn").off().bind("tap", function(){

			  
				 jq.mobile.changePage(vPathCallBack()+"settings.html");
			 
				
				
				return;
				
		
			});
	}

	
		
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
	
	function searchAndUpdateList(key){
		if (!key){
			//jq("#stationSetList").css("margin-top", "40px");
			// clear list if search keyword is empty
			jq("#stationSetList").html(generateStationSetTpl(mor.ticket.cache)).listview("refresh");
			
//			mor.ticket.util.contentIscrollTo(0,0,0);
			
			return;
						
		} else {
			var stations;
			var hotStations;
			if(key == 'O'){
				stations = [];
				hotStations = mor.ticket.cache.hotStations;
						
			}else{
				var results = jq.grep(mor.ticket.cache.stations, 
						function(station, index, pinyin){
						return station.valueSM.indexOf(key.toUpperCase()) === 0
						|| station.value.indexOf(key) === 0 || station.pinyin.indexOf(key.toLowerCase()) === 0;
					    });
				stations = results;
				hotStations = [];
			}
		var allResults = {
				stations: stations,
				hotStations: hotStations			
		};
			
		jq("#stationSetList").html(generateStationSetTpl(allResults)).listview("refresh");
		//jq("#stationSetList").css("margin-top", "40px");
		}
	};
	
	var stationSetTpl = 
			"{{if(it.hotStations!=null&&it.hotStations.length!=0){}}" +
				"<li data-role='list-divider'>热点车站</li>" +
			"{{}}}" +
			"{{ for (var i = 0, l = it.hotStations.length; i < l; i++) { }} " +
				"{{if(it.hotStations[i].value != undefined){ }}"+
					"<li data-id='{{=it.hotStations[i].id}}'><a>{{=it.hotStations[i].value}}</a></li>" +
				"{{ } }}"+
			"{{}}}" +
			"{{ for (var i = 0, l = it.stations.length; i < l && i < 20; i++) { }} " +
				"{{if(i==0){}}" +
					"<li data-role='list-divider' id='{{=it.stations[i].valueSM.charAt(0)}}'><a>{{=it.stations[i].valueSM.charAt(0)}}</a></li>" +
				"{{}}}" +
				"<li data-id='{{=it.stations[i].id}}'><a>{{=it.stations[i].value}}</a></li>" +
				"{{if((i+1)<l && it.stations[i].valueSM.charCodeAt(0)!=it.stations[i+1].valueSM.charCodeAt(0)){}}" +
					"<li data-role='list-divider' id='{{=it.stations[i+1].valueSM.charAt(0)}}'>{{=it.stations[i+1].valueSM.charAt(0)}}</li>" +
				"{{}}}" +
			"{{}}}";    
	var generateStationSetTpl = doT.template(stationSetTpl);	
	var stationListCrossSetTemplate ="{{ for (var i = 0, l = it.length; i<(l<20?l:20); i++) { }}"
		+ "{{if(mor.ticket.cache.stations[0].valueSM==it[i].valueSM){}}"
		+"<li data-role='list-divider' id='{{=it[i+1].valueSM.charAt(0)}}'>{{=it[i+1].valueSM.charAt(0)}}</li>"
		+ "{{}}}"
		+ "<li data-id='{{=it[i].id}}' data-filtertext='{{=it[i].pinyin}}'><a>{{=it[i].value}}</a></li>"
		+ "{{if((i+1)<l && it[i].valueSM.charCodeAt(0)!=it[i+1].valueSM.charCodeAt(0)&&it[i-1].valueSM.charCodeAt(0)!=null){}}"
		+ "<li data-role='list-divider' id='{{=it[i+1].valueSM.charAt(0)}}'>{{=it[i+1].valueSM.charAt(0)}}</li>"
		+ "{{}}}" + "{{}}}";
		
var generateStationPullSetList = doT.template(stationListCrossSetTemplate);

var stationSet_navListTempate = 
	"{{ var anchor,last_anchor,hotStations=it.hotStations,stations=it.stations; }}"+
	"{{ if(hotStations.length>0) { }}" +
		 	"<li><a id='OAnchor' href='#hot' class='anchor'>热</a></li>"+
	"{{ } }}"+
	"{{ for (var i = 0; i < stations.length; i++) { }}" +
		"{{ anchor=stations[i].valueSM.charAt(0);if(anchor!=last_anchor){ }}" +
		 		"<li><a id='{{=anchor}}Anchor' href='#{{=anchor}}' class='anchor'>{{=anchor}}</a></li>"+
		 "{{ last_anchor=anchor;} }}"+
	"{{}}}";
var stationSet_navList = doT.template(stationSet_navListTempate);

})();