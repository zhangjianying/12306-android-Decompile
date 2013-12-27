
/* JavaScript content from js/controller/finishedOrderDetail.js in folder common */
(function() {		
	jq("#FinishedOrderDetailView").live("pageinit", function() {
		registerChangeTrainListener();
		registerCancelTrainListener();
		registerChangeTicketCheckedBoxListener();
		registerChangeTicketBtnListener();
		registerCancelTicketBtnListener();
		jq("#FinishedOrderDetailViewBackBtn").bind("tap",function(){
			jq.mobile.changePage("finishedOrderList.html");
			return false;
		});
	});
	
	
	//var resignWoPuNum=0;//用来记录保存卧铺车票改签数量
	var WoPuSeats = ["3","4","5","6","A","C","L"];
	function finishedOrderDetailsFn(){
		var user = mor.ticket.loginUser;		

		mor.ticket.viewControl.current_tab="queryOrderTab";
		if(mor.ticket.viewControl.queryFinishedOrderType == '0'){
			jq("#FinishedOrderDetailViewBackBtn .ui-btn-text").html("今日订单");
			jq("#FinishedOrderDetailViewBackBtn").removeClass("head-btn-long");
		} else {
			if(mor.ticket.viewControl.queryFinishedOrderType == '1') {
				jq("#FinishedOrderDetailViewBackBtn .ui-btn-text").html("7日订单");
				jq("#FinishedOrderDetailViewBackBtn").removeClass("head-btn-long");
			} else {
				jq("#FinishedOrderDetailViewBackBtn .ui-btn-text").html("已完成订单");
				jq("#FinishedOrderDetailViewBackBtn").addClass("head-btn-long");
			}
		}			
		var orderDetailList = mor.ticket.queryOrder.getCurrentFinishedOrdersMyTicketList();
		jq("#ticketSquenceNo").html(orderDetailList[0].sequence_no);
		jq("#ticketBookedDate").html(mor.ticket.util.getLocalDateString2(orderDetailList[0].reserve_time));
		jq("#finishedOrderDetail").html(generateOrdersDetailList(orderDetailList));	
		for(var i=0;i<orderDetailList.length;i++){
			if(orderDetailList[i].resign_flag=='N' && orderDetailList[i].return_flag == 'N'){
				jq("#finishedOrderDetail li").eq(i).addClass("ui-disabled");
			}
		}
		initContentHeight();
		contentIscrollRefresh();
	}
	jq("#FinishedOrderDetailView").live("pagebeforeshow", function() {
		var user = mor.ticket.loginUser;		

		if(user.isAuthenticated === "Y"){
			finishedOrderDetailsFn();
		}else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
			} else {
				registerAutoLoginHandler(finishedOrderDetailsFn, autologinFailJump);
			}

			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	function initContentHeight(){
		//TODO .outerHeight
		var height = jq("#FinishedOrderDetailView .ui-content").height();
		height -= jq("#regulationInfo").height();
		jq("#FinishedOrderDetailView .ui-content").height(height);
	}
	
	function contentIscrollRefresh(){
		jq("#FinishedOrderDetailView .ui-content").iscrollview("refresh");
	}
	
	function registerChangeTrainListener(){
		jq("#changeTicket").bind("tap",function(){
			jq(this).addClass("ui-btn-active ui-state-persist")
				.siblings().removeClass("ui-btn-active ui-state-persist");
			jq(".cancelTicketBtn").hide();
			var ticketList = mor.ticket.queryOrder.getCurrentFinishedOrdersMyTicketList();
			ChangeTicketStatuse(ticketList);
			contentIscrollRefresh();
			return false;
		});
	}
	
	function ChangeTicketStatuse(ticketList){
		var num = 0;
		if(ticketList.length == 1){
			if(ticketList[0].resign_flag === "Y"){
				jq(".change").eq(0).show();
				//jq("#finishedOrderDetail li [type='checkbox']").attr("checked","checked");
				num ++;
			}
		}else{
			for(var i=0; i<ticketList.length; i++){
				if(ticketList[i].resign_flag === "Y"){
					jq(".change").eq(i).show();
					num ++;
				}
			}	
		}		
		if(num){
			jq("#changeTicketBtn").html(getChangeTicketBtnText());		
			jq("#changeTicketBtn").parent().show();
			/*jq("#regulationInfo").show();
			jq("#regulationInfo h6 .ui-btn-text").html("点击查看改签相关规定");
			jq("#regulationInfo .tips2 div").html(mor.ticket.cache.promptMsg.changeRregulationMap.change_regulation_1+'<br>'+
					mor.ticket.cache.promptMsg.changeRregulationMap.change_regulation_2+'<br>'+
					mor.ticket.cache.promptMsg.changeRregulationMap.change_regulation_3+'<br>'+
					mor.ticket.cache.promptMsg.changeRregulationMap.change_regulation_4);*/
		}else{
			jq("#changeTicket").removeClass("ui-btn-active ui-state-persist");
			/*jq("#regulationInfo").show();
			jq("#regulationInfo h6").addClass("noChangeTicket");
			jq("#regulationInfo h6 .ui-btn-text").html("点击查看不能改签相关规定");
			jq("#regulationInfo .tips2 div").html(mor.ticket.cache.promptMsg.changeNoRregulationMap.no_change_regulation_1+'<br>'+
					mor.ticket.cache.promptMsg.changeNoRregulationMap.no_change_regulation_2+'<br>'+
					mor.ticket.cache.promptMsg.changeNoRregulationMap.no_change_regulation_3+'<br>'+
					mor.ticket.cache.promptMsg.changeNoRregulationMap.no_change_regulation_4);*/
		}
	}
	
	function registerCancelTrainListener(){
		jq("#cancleTicket").bind("tap",function(){
			jq(this).addClass("ui-btn-active ui-state-persist")
				.siblings().removeClass("ui-btn-active ui-state-persist");
			jq(".change").hide();
			var ticketList = mor.ticket.queryOrder.getCurrentFinishedOrdersMyTicketList();
			CancelTicketStatuse(ticketList);
			jq("#changeTicketBtn").parent().hide();
			jq("#changeTicketBtn").html(getChangeTicketBtnText());
			contentIscrollRefresh();
			return false;
		});
	}
	
	function CancelTicketStatuse(ticketList){
		var num = 0;
		for(var i=0; i<ticketList.length; i++){
			if(ticketList[i].return_flag === "Y"){
				jq(".cancelTicketBtn").eq(i).show();
				num ++;
			}
		}
		if(!num){
			jq("#cancleTicket").removeClass("ui-btn-active ui-state-persist");
		}
		/*if(num){
			jq("#regulationInfo").show();
			jq("#regulationInfo h6 .ui-btn-text").html("点击查看退票相关规定");
			jq("#regulationInfo .tips2 div").html(mor.ticket.cache.promptMsg.resignRegulationMap.resign_regulation_1+'<br>'+
					mor.ticket.cache.promptMsg.resignRegulationMap.resign_regulation_2+'<br>'+
					mor.ticket.cache.promptMsg.resignRegulationMap.resign_regulation_3+'<br>'+
					mor.ticket.cache.promptMsg.resignRegulationMap.resign_regulation_4);
		}else{
			jq("#cancleTicket").removeClass("ui-btn-active ui-state-persist");
			/*jq("#regulationInfo").show();
			jq("#regulationInfo h6").addClass("noChangeTicket");
			jq("#regulationInfo h6 .ui-btn-text").html("点击查看不能退票相关规定");
			jq("#regulationInfo .tips2 div").html(mor.ticket.cache.promptMsg.resignNoRegulationMap.no_resign_regulation_1+'<br>'+
					mor.ticket.cache.promptMsg.resignNoRegulationMap.no_resign_regulation_2+'<br>'+
					mor.ticket.cache.promptMsg.resignNoRegulationMap.no_resign_regulation_3+'<br>'+
					mor.ticket.cache.promptMsg.resignNoRegulationMap.no_resign_regulation_4);
		}*/
	}
	
	function registerChangeTicketCheckedBoxListener(){
		jq("#finishedOrderDetail").on("change", ".changeTicketChkbox", function(e){
			e.stopImmediatePropagation();
			var checkedList = jq(".changeTicketChkbox:checked");
			if(checkedList.length > 1){
				var resignWoPuNum=0;//用来记录保存卧铺车票改签数量
				var batchNos = [];
				var ticketList = mor.ticket.queryOrder.getCurrentFinishedOrdersMyTicketList();	
				var currentChangeTicketOrder = ticketList[jq(this).parents('li').index()];
				//如果同时改签两张卧铺票，弹出提示信息不允许
				for(var i = 0;i<checkedList.length;i++){
					
					var seatTypeCode = ticketList[jq(checkedList[i]).parents('li').index()].seat_type_code;
					if(jq.inArray(seatTypeCode,WoPuSeats)!=-1){
						resignWoPuNum++;
					}
					batchNos.push(ticketList[jq(checkedList[i]).parents('li').index()].batch_no);
				}
				
				/**
				
				batch_no = currentChangeTicketOrder.batch_no;
				batch_no0 = ticketList[jq(checkedList[0]).parents('li').index()].batch_no;
				if(batch_no0 != batch_no){
					mor.ticket.util.alertMessage("  您选择的车票【" + currentChangeTicketOrder.station_train_code + "车次，"+
							currentChangeTicketOrder.coach_name + "车，"+currentChangeTicketOrder.seat_name +"号】不能和其他车票同时改签，" +
							"请不要勾选。请选择同一订单、同一日期、同一车次、相同发到站、相同席别的车票进行改签。");
					jq(this).removeAttr("checked");
					
				}
				
				**/
				jq.unique(batchNos);
				if(batchNos.length!=1){
					mor.ticket.util.alertMessage("  您选择的车票【" + currentChangeTicketOrder.station_train_code + "车次，"+
							currentChangeTicketOrder.coach_name + "车，"+currentChangeTicketOrder.seat_name +"】不能和其他车票同时改签，" +
							"请不要勾选。请选择同一订单、同一日期、同一车次、相同发到站、相同席别的车票进行改签。");
					jq(this).removeAttr("checked");
					
				}
				//如果同时改签两张卧铺票，弹出提示信息不允许
				else if(resignWoPuNum>1){
					mor.ticket.util.alertMessage("  您选择的车票【" + currentChangeTicketOrder.station_train_code + "车次，"+
							currentChangeTicketOrder.coach_name + "车，"+currentChangeTicketOrder.seat_name +"】不能和其他卧铺车票同时改签，" +
							"请不要勾选。请一次改签一张卧铺车票。");
					jq(this).removeAttr("checked");
				}else{
					jq("#changeTicketBtn").html(getChangeTicketBtnText());
				}
			}
			else{
				
				jq("#changeTicketBtn").html(getChangeTicketBtnText());		
			}	
			return false;
		});
	}
	
	function registerChangeTicketBtnListener(){
		jq("#changeTicketBtn").bind("tap",function(){
			if(countCheckedBoxNum()) {
				mor.ticket.viewControl.bookMode = "gc";
				var queryOrder = mor.ticket.queryOrder;
				var ticketList = queryOrder.getCurrentFinishedOrdersMyTicketList();	
				queryOrder.initChangeTicketOrderList();
				for(var i=0;i<ticketList.length;i++){
					if (jq(".changeTicketChkbox:eq("+i+")").attr("checked") == "checked"){
						queryOrder.setChangeTicketOrderList(ticketList[i]);
					}
				}
				initChangeTicketInfo(queryOrder.changeTicketOrderList);
				jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");							
			}
			else {
				mor.ticket.util.alertMessage("请勾选需要改签的车票！");
			}
			return false;
		});
	}
	
	function countCheckedBoxNum(){
		return jq(".changeTicketChkbox:checked").length;
	}
	
	function getChangeTicketBtnText(){
		var changeTicketChkboxNum = countCheckedBoxNum();
		var str;
		if(changeTicketChkboxNum){
			str = "改签 (" + changeTicketChkboxNum + ")";
		}else{
		    str = "改签";
		}		
		return str;
	}
	
	function initChangeTicketInfo(ticketInfoList){
		var model = mor.ticket.leftTicketQuery;
		model.from_station_telecode = ticketInfoList[0].from_station_telecode;
		model.to_station_telecode = ticketInfoList[0].to_station_telecode;
		var date = mor.ticket.util.getNewDate();
		var setTrainDate = window.ticketStorage.getItem("set_train_date_type")==null ? "1":window.ticketStorage.getItem("set_train_date_type");
		var dateNew = new Date(date.setDate(date.getDate()+parseInt(setTrainDate,10)));
		model.train_date = dateNew.format("yyyy-MM-dd");
		model.time_period = 0;
	}
	
	function registerCancelTicketBtnListener(){
		jq("#finishedOrderDetail").on("tap", ".cancelTicketBtn", function(){	
			mor.ticket.viewControl.cancelTicketIndex = jq(this).parents("li").index();
			requestCancleChangeTicket();
			
			/** 去掉对话框
			WL.SimpleDialog.show(
					"温馨提示", 
					"确定要退票吗？", 
					[ {text : '确定', handler: requestCancleChangeTicket},
					  {text : '取消', handler: function(){}}]
				);
				
			**/
		});
	}

	function requestCancleChangeTicket(){		
		var ticketList = mor.ticket.queryOrder.getCurrentFinishedOrdersMyTicketList();	
		var index = mor.ticket.viewControl.cancelTicketIndex;
		var currentTicketDetail = ticketList[index];
		var util = mor.ticket.util;
		var commonParameters = {			
			'sequence_no': currentTicketDetail.sequence_no,
			'batch_no':currentTicketDetail.batch_no,
			'coach_no':currentTicketDetail.coach_no,
			'seat_no':currentTicketDetail.seat_no
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "refundTicketRequest"
		};
		
		var options =  {
				onSuccess: requestCancleTicketSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function  requestCancleTicketSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {		
			mor.ticket.queryOrder.setCancelTicketInfo(invocationResult);
			jq.mobile.changePage("cancelOrder.html");
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}		
	}
	
	var ordersDetailListTemplate =
		"{{~it :orderDetail:index}}" +
		"<li data-index='{{=index}}'>" +
			"<div class='ui-grid-a' >" +
				"<div class='ui-block-a'>{{=mor.ticket.util.changeDateType(orderDetail.train_date)}}</div>" +
				"<div class='ui-block-b'>" +
					"<div class='ui-grid-b'>" +
						"<div class='ui-block-a'>{{=mor.ticket.cache.getStationNameByCode(orderDetail.from_station_telecode)}}" +
								"<span>{{=mor.ticket.util.formateTrainTime(orderDetail.start_time)}}</span></div>" +
						"<div class='ui-block-b trainDetailArrow'></div>" +
						"<div class='ui-block-c'>{{=mor.ticket.cache.getStationNameByCode(orderDetail.to_station_telecode)}}" +
								"<span>{{=mor.ticket.util.formateTrainTime(orderDetail.arrive_time)}}</span></div>" +
					"</div>" +
				"</div>" +
			"</div>" +
			"<div class='ui-grid-c'>" +
				"<div class='ui-block-a'>{{=orderDetail.station_train_code}}</div>" +
				"<div class='ui-block-b'>{{=mor.ticket.util.getSeatTypeName(orderDetail.seat_type_code,orderDetail.seat_no)}}</div>" +
				"<div class='ui-block-c'>{{=orderDetail.coach_name}}车</div>" +
				"<div class='ui-block-d'>{{=orderDetail.seat_name}}</div>" +
				"<div class='ui-block-a'>{{=orderDetail.passenger_name}}</div>" +
				"<div class='ui-block-b'>{{=orderDetail.passenger_id_type_name}}</div>" +
				"<div class='ui-block-c'>{{=mor.ticket.util.getTicketTypeName(orderDetail.ticket_type_code,orderDetail.seat_no)}}</div>" +
				"<div class='ui-block-d'>{{=orderDetail.ticket_price}}元</div>" +
			"</div>" +
			"<div class='ui-grid-a'>" +
				"<div class='ui-block-a'>车票状态 ：" +
				"{{ if(orderDetail.ticket_status_code =='c'){ }}" +//退票状态下判断
					"{{=orderDetail.ticket_status_name.substring(0,3)}}" +
				"{{ } else { }}" +
					"{{=orderDetail.ticket_status_name}}" +
				"{{ } }}" +
				"</div>" +
				"<div class='ui-block-b' style='text-align: right;'>" +
					"<span class='change' style='display:none;'>" +
					"	<input class='changeTicketChkbox' type='checkbox'/><span></span></span>" +
					"<a data-role='button' class='cancelTicketBtn' style='display:none'>退票</a>" +
				"</div>" +
				"{{ if(orderDetail.ticket_status_code =='c' && orderDetail.ticket_status_name.substring(3) != null ){ }}" +//退票状态下判断
					"<div class='ui-block-a' style='width:100%'>{{=orderDetail.ticket_status_name.substring(3)}}</div>" +
				"{{ } }}" +
			"</div>" +
		"</li>" +
		"{{~}}";
	var generateOrdersDetailList = doT.template(ordersDetailListTemplate);
})();
	