
/* JavaScript content from js/controller/ticketDetails.js in folder common */
(function(){
	jq("#ticketDetailsView").live("pageinit", function(){	

		registerAddPassengerButtonClickHandler();
		jq.mobile.loadPage(vPathCallBack()+"passengers.html");
	});
	
	jq("#ticketDetailsView").live("pagebeforeshow", function(){
		mor.ticket.viewControl.tab1_cur_page="ticketDetails.html";
		initTicketDetails();
	});
	
	function initTicketDetails(){
		var ticket = mor.ticket.currentTicket;
		var cache = mor.ticket.cache;
		var util = mor.ticket.util;
		
		var mode = mor.ticket.viewControl.bookMode;
		var train_date = ticket.train_date;
		var str="单程：";
		if(mode === "gc"){
			jq("#ticketDetailsView .ui-header>h1").html("车次详情(改签)");
		}
		if(mode === "wc") {
			str="往程：";
		}
		if(mode === "fc") {
			str="返程：";
			train_date = mor.ticket.leftTicketQuery.train_date_back;
		}
		jq("#ticketDetailsPrompt").html(str + train_date);
		
		jq("#fromStationName").html(cache.getStationNameByCode(ticket.from_station_telecode));
		jq("#trainStartTime").html(ticket.start_time + " 出发");
		jq("#trainCodeName").html(ticket.station_train_code);
		jq("#trainDurationTime").html(util.getLiShiStr(ticket.lishi));
		jq("#toStationName").html(cache.getStationNameByCode(ticket.to_station_telecode));
		jq("#trainReachTime").html(ticket.arrive_time + " 到达");
		
		//无票时候不显示，判断seatDetailsGrid长度
		var len = 0;
		for(var i=0;i<ticket.yplist.length; i++){
			if(ticket.yplist[i].num){
				len ++;
			}
		}
		var px=30 * len+'px';
		jq("#seatDetailsGrid").css('height',px);
		jq("#seatDetailsGridUl").html(generateSeatsInfo(ticket));
	};
	
	function registerAddPassengerButtonClickHandler(){
		jq("#addPassengerButton").bind("tap", function(){
			jq.mobile.changePage(vPathCallBack()+"passengers.html");
			return false;
		});
	};
	
	var seatsInfoTemplate =
	 "{{ for(var i=0;i<it.yplist.length;i++) { }}"+ 
	 	"{{ if(it.yplist[i].num) { }}" +
			"<li data-index='{{=i}}'>" +
				"<span>{{=it.yplist[i].type}}</span>" +
				"<span>{{=it.yplist[i].price}}元</span>" +
				"{{ if(it.yplist[i].num > 20) { }}" +
					"<span>有票</span>" +
				"{{ } else { }}" +
					"<span>{{=it.yplist[i].num}}张</span>" +
				"{{ } }}" +
			"</li>" +
		"{{ } }}" +
	 "{{ } }}";
	var generateSeatsInfo = doT.template(seatsInfoTemplate);
})();