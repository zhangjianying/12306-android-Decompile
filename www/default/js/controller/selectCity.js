
/* JavaScript content from js/controller/selectCity.js in folder common */
(function(){
	var prevPage;
	var cityList = mor.ticket.cache.city;
	jq("#selectCityView").live("pagebeforeshow", function(e,data){
		jq("#selectCity_content", this).bind({
			"iscroll_onpulldown" : onPullDown,
			"iscroll_onpullup" : onPullUp,
			"iscroll_onpullupreset" : onPullUpReset
		});
		
		prevPage = data.prevPage.attr("id"); 
		
		mor.ticket.viewControl.city_num = -1;
		
		registerCityListItemClickHandler();
		
		registerSearchCityInputChangeHandler();
		
		anchorA2ZStation();
		
		sortCity(cityList);
		
		var initCity = fetchStationSubSet(true);
		
		jq("#cityList").html(generateCityOption(initCity)).listview("refresh");
		
		jq("#selectCityView .slider-nav").css("top","100px");
		
	});
	
	function onPullUp() {
			if (jq("#searchCityInput").val() == "") {
				if (!busy.isVisible()) {
					busy.show();
				}
				mor.ticket.viewControl.city_num -= 10;
				var city = fetchStationSubSet(true);
				jq("#cityList").html(generateCityPullList(city))
						.listview("refresh");
			}
	};

	function onPullDown() {
			if (jq("#searchCityInput").val() == "") {
				if (!busy.isVisible()) {
					busy.show();
				}
				if (mor.ticket.viewControl.city_num < 30) {
					mor.ticket.viewControl.city_num = 20;
				} else {
					mor.ticket.viewControl.city_num -= 10;
				}
				var city = fetchStationSubSet(false);
				if (mor.ticket.viewControl.city_num < 21)
					jq("#cityList").html(generateCityOption(city)).listview("refresh");
				else
					jq("#cityList").html(generateCityPullList(city)).listview("refresh");
			} else{
					jq("#cityList").css("margin-top", "0px");
			}
	};
	
	function onPullUpReset() {
		setTimeout(function(){
			if(busy.isVisible()){
				busy.hide();
			}
    	}, 500);
	}
	
	function registerSearchCityInputChangeHandler() {
		jq(document).on("input", "#searchCityInput", function(e) {
			e.stopImmediatePropagation();
			var key = jq(this).val();
			searchAndUpdateList(key);
			return false;
		});
		jq(document).on("change", "#searchCityInput", function(e) {
			e.stopImmediatePropagation();
			var key = jq(this).val();
			searchAndUpdateList(key);
			return false;
		});

	};
	
	function searchAndUpdateList(key) {
		if (!key) {
			mor.ticket.viewControl.city_num = -1;
			var city = fetchStationSubSet(true);
			jq("#cityList").html(generateCityOption(city))
					.listview("refresh");
			return;
		} else {
			var results = jq.grep(cityList, function(city,
					index) {
				return city.valueSM.indexOf(key.toUpperCase()) === 0 
				|| city.city_name.indexOf(key) === 0
				|| city.pinyin.indexOf(key.toLowerCase()) === 0;
			});
			jq("#cityList").html(generateCityOption(results))
					.listview("refresh");
		}
	};
	
	function fetchStationSubSet(forward) {
		var start = 0;
		var end = 0;
		if (forward) {
			start = mor.ticket.viewControl.city_num;
			end = start + 21;
			mor.ticket.viewControl.city_num = end;
		} else {
			end = mor.ticket.viewControl.city_num;
			start = end - 21;
			mor.ticket.viewControl.city_num = end;
		}

		var filteredResult = cityList.filter(function(item,
				index, array) {
			return (index > start && index < end);
		});

		return filteredResult;
	}
	
	function anchorA2ZStation() {
		var arr = [ 'AAnchor', 'BAnchor', 'CAnchor', 'DAnchor', 'EAnchor',
				'FAnchor', 'GAnchor', 'HAnchor', 'IAnchor', 'JAnchor',
				'KAnchor', 'LAnchor', 'MAnchor', 'NAnchor', 'OAnchor',
				'PAnchor', 'QAnchor', 'RAnchor', 'SAnchor', 'TAnchor',
				'UAnchor', 'VAnchor', 'WAnchor', 'XAnchor', 'YAnchor',
				'ZAnchor' ];
		for ( var i = 0; i < arr.length; i++) {
			jq("#" + arr[i]).live("tap", function() {
				if (jq("#searchCityInput").val() != "") {
					jq("#searchCityInput").val("");
					jq("#searchCityInput").change();
				}
				searchAndUpdateList2(jq(this).attr("id").charAt(0));

				return false;
			});
		}
	}
	
	function searchAndUpdateList2(key) {
		if (!key) {
			return;
		} else {
			var viewControl = mor.ticket.viewControl;
			for ( var i = 0, l = cityList.length; i < l; i++) {
				if (cityList[i].valueSM.indexOf(key
						.toUpperCase()) === 0) {
					viewControl.city_num = i-1;
					break;
				}
			}
			var city = fetchStationSubSet(true);
			jq("#cityList").html(generateCityOption(city))
					.listview("refresh");
			var dis = jq("#selectCityView .iscroll-scroller").position().top;
			jq("#selectCity_content").iscrollview("scrollTo", 0, dis, 0,
					true);
			//
		}
	};
	
//	function sortCity(city) {
//		city.sort(sortfunction);
//	}
//	
//	function sortfunction(x, y) {
//		return x.valueSM.charCodeAt(0) - y.valueSM.charCodeAt(0);
//	}
	
	function sortCity(city){
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

		city.sort(by("pinyin"));
		return city;
	}
	
	function registerCityListItemClickHandler(){
		jq("#cityList").off().on("tap", "li", function(e){
			e.stopImmediatePropagation();
			jq(this).addClass("ui-btn-active")
				.siblings().removeClass("ui-btn-active");
			
			var util = mor.ticket.util;
			var city_code =  jq(this).attr("data-id");
			if(prevPage=== "modifyPassengerView"){
				if(mor.ticket.viewControl.isCityGo){
					jq("#modify_pPreferenceFromStation").val(util.getCityByCode(city_code));
					jq("#modify_pPreferenceFromStation_code").val(city_code);
					mor.ticket.viewControl.isCityGo=false;
				}else{
					jq("#modify_pPreferenceToStation").val(util.getCityByCode(city_code));
					jq("#modify_pPreferenceToStation_code").val(city_code);
				}
//			}else if(prevPage=== "registView_other"){
			}else if(prevPage=== "registView"){
				if(mor.ticket.viewControl.isCityGo){
					jq("#regist_preferenceFromStation").val(util.getCityByCode(city_code));
					jq("#regist_preferenceFromStation_code").val(city_code);
					mor.ticket.viewControl.isCityGo=false;
				}else{
					jq("#regist_preferenceToStation").val(util.getCityByCode(city_code));
					jq("#regist_preferenceToStation_code").val(city_code);
				}
			}else if(prevPage=== "modifyuserInfoView"){
				if(mor.ticket.viewControl.isCityGo){
					jq("#modify_preferenceFromStation").val(util.getCityByCode(city_code));
					jq("#modify_preferenceFromStation_code").val(city_code);
					mor.ticket.viewControl.isCityGo=false;
				}else{
					jq("#modify_preferenceToStation").val(util.getCityByCode(city_code));
					jq("#modify_preferenceToStation_code").val(city_code);
				}
			}else{
				
			}
			// fix '点透' 问题
		    if(mor.ticket.util.isAndroid()&& (parseFloat(device.version) > 3.0)){
		    	mor.ticket.util.transitionFlag = true;
		    	setTimeout(function(){
		    		if(mor.ticket.util.transitionFlag){
		    			history.back();
		    			mor.ticket.util.transitionFlag = false;
		    		}
		    	}, 300);
		    }else{
		    	history.back();
		    }

			return false;
		});
	};
	
	var cityOptionsTemplate =
		"{{ for (var i = 0, l = it.length; i<(l<20?l:20); i++) { }}"
		+ "{{if(i==0){}}"
		+ "<li data-role='list-divider' id='{{=it[i].valueSM.charAt(0)}}'>{{=it[i].valueSM.charAt(0)}}</li>"
		+ "{{}}}"
		+ "<li data-id='{{=it[i].city_code}}' data-filtertext='{{=it[i].pinyin}}'><a>{{=it[i].city_name}}</a></li>"
		+ "{{if((i+1)<l && it[i].valueSM.charCodeAt(0)!=it[i+1].valueSM.charCodeAt(0)){}}"
		+ "<li data-role='list-divider' id='{{=it[i+1].valueSM.charAt(0)}}'>{{=it[i+1].valueSM.charAt(0)}}</li>"
		+ "{{}}}" + "{{}}}";
	var generateCityOption = doT.template(cityOptionsTemplate);
	
	var cityListCrossTemplate = "{{ for (var i = 0, l = it.length; i<(l<20?l:20); i++) { }}"
		+ "<li data-id='{{=it[i].city_code}}' data-filtertext='{{=it[i].pinyin}}'><a>{{=it[i].city_name}}</a></li>"
		+ "{{if((i+1)<l && it[i].valueSM.charCodeAt(0)!=it[i+1].valueSM.charCodeAt(0)){}}"
		+ "<li data-role='list-divider' id='{{=it[i+1].valueSM.charAt(0)}}'>{{=it[i+1].valueSM.charAt(0)}}</li>"
		+ "{{}}}" + "{{}}}";
	var generateCityPullList = doT.template(cityListCrossTemplate);
})();