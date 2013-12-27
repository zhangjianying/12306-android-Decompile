
/* JavaScript content from js/controller/selectTrain.js in folder common */

(function(){
	var searchResult = [];
	/*jq("#selectTrainView").live("pagecreate", function() {
		//mor.ticket.util.androidRemoveIscroll("#selectTrainView");
	});*/
	jq("#selectTrainView").live("pageinit", function(){
		registerTrainListFilterCallback();		
		registerTrainListItemClickHandler();	
		registSlider_navClickHandler();
		jq("#selectTrainBackBtn").bind("tap",function(){
			// fix '点透' 问题
		    if(mor.ticket.util.isAndroid()&& (parseFloat(device.version) > 3.0)){
		    	mor.ticket.util.transitionFlag = true;
		    	setTimeout(function(){
		    		if(mor.ticket.util.transitionFlag){
		    			jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
		    			mor.ticket.util.transitionFlag = false;
		    		}
		    	}, 300);
		    }else if(mor.ticket.util.isIPhone()){
		    	jq.mobile.changePage( vPathViewCallBack()+"MobileTicket.html", { transition: "slide",reverse: true} );
		    }else{
		    	jq.mobile.changePage( vPathViewCallBack()+"MobileTicket.html");
		    }
		});
	});
	jq("#selectTrainView").live("pagebeforeshow", function(){
		
			if( mor.ticket.views.selectTrain.isFromTrain){
				mor.ticket.leftTicketQuery.station_train_code = "";
			}else{
				mor.ticket.leftTicketQuery.station_train_code_back= "";
			}		
			searchAndUpdateTrainList();
		/*
		if(mor.ticket.loginUser.isAuthenticated === "Y"){
		}else{
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}*/
	});
	
	function registerTrainListFilterCallback(){
		jq("#trainList").listview('option', 'filterCallback', function(text, searchValue){
			return text.toLowerCase().indexOf(searchValue) === -1;
		});
	};
	
	function registerTrainListItemClickHandler(){
		jq("#trainList").off().on("tap", "li", function(e){
			e.stopImmediatePropagation();
			var code = jq(this).attr("data-code");			
			if( mor.ticket.views.selectTrain.isFromTrain){
				mor.ticket.leftTicketQuery.station_train_code = code;
			}
			else {
				mor.ticket.leftTicketQuery.station_train_code_back= code;
				mor.ticket.views.selectTrain.isFromTrain = true;
			}			
			// fix '点透' 问题
		    if(mor.ticket.util.isAndroid()&& (parseFloat(device.version) > 3.0)){
		    	mor.ticket.util.transitionFlag = true;
		    	setTimeout(function(){
		    		if(mor.ticket.util.transitionFlag){
		    			jq.mobile.changePage( vPathViewCallBack()+"MobileTicket.html");
		    			mor.ticket.util.transitionFlag = false;
		    		}
		    	}, 300);
		    }else if(mor.ticket.util.isIPhone()){
		    	jq.mobile.changePage( vPathViewCallBack()+"MobileTicket.html", { transition: "slide",reverse: true} );
		    }else{
		    	jq.mobile.changePage( vPathViewCallBack()+"MobileTicket.html");
		    }
			return false;
		});
	};
		
	function searchAndUpdateTrainList(){
		var util = mor.ticket.util;
		var model = mor.ticket.leftTicketQuery;
		var commonParameters = null;
		if(mor.ticket.views.selectTrain.isFromTrain){//单程、往程查询
			commonParameters = {
					'from_station': model.from_station_telecode,
					'to_station': model.to_station_telecode,
					'start_date': util.processDateCode(model.train_date),
					'start_time': util.getTimePeriodDescription(model.time_period)
				};	
		} else {
			commonParameters = {//返程车次查询
					'from_station': model.from_station_telecode_back,
					'to_station': model.to_station_telecode_back,
					'start_date': util.processDateCode(model.train_date_back),
					'start_time': util.getTimePeriodDescription(model.time_period_back)
				};	
		}
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "queryststrainall"
		};
		
		var options = {
			onSuccess: requestSucceeded,
			onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	};
	
	function requestSucceeded(result) {
		var invocationResult = result.invocationResult;
		searchResult = invocationResult.selectOptions;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			sortTrain(invocationResult.selectOptions);
			jq("#trainList").html(generateTrainList(invocationResult.selectOptions)).listview("refresh");
			jq("#train_navList").html(train_navList(invocationResult.selectOptions));
			var size = parseInt(jq("#trainNavLength").val());
			//solve many alpha index on ANDROID DEVICE  
			var deviceHeight = document.body.clientHeight + window.screenTop;
			var headerHeight = 44;
			var footerHeight = 50;
			var topHeight = jq("#train_slider-nav").position().top;
			var sliderHeight = deviceHeight - footerHeight - topHeight - headerHeight;
			//jq("#selectTrainView .anchor").css("height",sliderHeight/size);
			//fix navList height bug
			jq('#train_slider-nav').height(sliderHeight);
			jq('#train_navList').height(sliderHeight);
			jq("#train_navList li").height(sliderHeight/size);
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
		if(busy.isVisible()){
			busy.hide();
		}
	};
	
	function sortTrain(trains){
		trains.sort(sortfunction);
	}
	
	function sortfunction(x,y){
		return x.station_train_code.charCodeAt(0)-y.station_train_code.charCodeAt(0);
	}	
	
	function registSlider_navClickHandler(){
		jq("a[id^='train_']").live("tap",function(){
			var anchor = jq(this).attr("href").charAt(1);
			var curSelector = "#trainList_"+anchor;
			var jqNode=jq(curSelector);
			var top=0;
			while(!jqNode.hasClass('iscroll-scroller')){
				top+=jqNode.position().top;
				jqNode=jqNode.offsetParent();
			}
			
			var screenHeight = jq("#selectTrainView .iscroll-wrapper").height();
			var contentHeight = jq("#selectTrainView .iscroll-scroller").height();
			var y=(top+screenHeight<=contentHeight)?top:contentHeight-screenHeight;
			jq("#selectTrainView .iscroll-wrapper").iscrollview('scrollTo',0,-y,0);
		    return false;
		});
	};
	
	var trainListTemplate = 
			"{{ for (var i = 0, l = it.length; i < l; i++) { }}" +
				"{{if(i==0){}}" +
					"<li data-role='list-divider' id='trainList_{{=it[i].station_train_code.charAt(0)}}'>{{=it[i].station_train_code.charAt(0)}}</li>" +
				"{{}}}" +
				"<li data-code='{{=it[i].station_train_code}}'><a>" +
					"<div class='ui-grid-a'>" +
					"<div class='ui-block-a' style='width:30%;'>{{=it[i].station_train_code}}</div>" +
					"<div class='ui-block-b' style='width:70%;'>" +
						"({{=it[i].start_station_name}}{{=it[i].start_time}}--{{=it[i].end_station_name}}{{=it[i].end_time}})</div>" +
					"</div>" +
				"</a></li>" +
				"{{if((i+1)<l && it[i].station_train_code.charCodeAt(0)!=it[i+1].station_train_code.charCodeAt(0)){}}" +
					" <li data-role='list-divider' id='trainList_{{=it[i+1].station_train_code.charAt(0)}}'>{{=it[i+1].station_train_code.charAt(0)}}</li>" +
				"{{}}}" +
			"{{}}}";
	var generateTrainList = doT.template(trainListTemplate);
	
	var train_navListTempate = 
		"{{var size=0;}}"+
		"{{ for (var i = 0, l = it.length; i < l; i++) { }}" +
			"{{if(i==0){}}" +
			 	"<li><a id='train_{{=it[i].station_train_code.charAt(0)}}' href='#{{=it[i].station_train_code.charAt(0)}}' class='anchor'>{{=it[i].station_train_code.charAt(0)}}</a></li>"+
			     "{{size++;}}"+
			 	"{{ } }}" +
			"{{if((i+1)<l && it[i].station_train_code.charCodeAt(0)!=it[i+1].station_train_code.charCodeAt(0)){}}" +
				"<li><a id='train_{{=it[i+1].station_train_code.charAt(0)}}' href='#{{=it[i+1].station_train_code.charAt(0)}}' class='anchor'>{{=it[i+1].station_train_code.charAt(0)}}</a></li>"+
				"{{size++;}}"+
			"{{}}}" +
		"{{}}}"+
		" <input id='trainNavLength'  type='hidden' value='{{=size}}'></input>";
	var train_navList = doT.template(train_navListTempate);
})();