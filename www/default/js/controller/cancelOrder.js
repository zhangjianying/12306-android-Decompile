
/* JavaScript content from js/controller/cancelOrder.js in folder common */
(function(){
	jq("#cancelOrderView").live("pageinit", function(){
		registerCancelOrderCancelBtnLisener(); 
		registerCancelOrderConfirmBtnListener();
	});
	
	function cancelOrderFn(){
		var util = mor.ticket.util;
		var cache = mor.ticket.cache;
		var queryOrder = mor.ticket.queryOrder;
		var ticket = queryOrder.getCancelTicketInfo();
		var newRate = ticket.rate;
		var str = "单程：";
		
		//TODO reflow 优化  position:absolute
			jq("#cancelOrderDetailsPrompt").html(str + util.changeDateType(ticket.train_date));
			jq("#cancelOrderFromStationName").html(cache.getStationNameByCode(ticket.from_station_telecode));
			jq("#cancelOrderTrainStartTime").html(util.formateTrainTime(ticket.start_time) + " 出发");
			jq("#cancelOrderTrainCodeName").html(ticket.station_train_code);
			jq("#cancelOrderToStationName").html(cache.getStationNameByCode(ticket.to_station_telecode));
			jq("#cancelOrderTrainReachTime").html(util.formateTrainTime(ticket.arrive_time) + " 到达");	
			jq("#cancelOrderTicketDetailsGrid").html(generateTicketDetailsGrid(ticket));
			jq("#cancelOrderCostPrice").html(ticket.return_cost);
			if(newRate === "-1"){
				jq("#cancelOrderCostRateId").hide();
				//jq('#cancelOrderCostPriceId').css('margin-right','20px');
			}else{
				jq("#cancelOrderCostRate").html(ticket.rate);
			}
			jq("#cancelOrderReturnPrice").html((parseFloat(ticket.ticket_price)-parseFloat(ticket.return_cost)).toFixed(2));
	}
	jq("#cancelOrderView").live("pagebeforeshow", function(){
		
		jq('#cancelOrderView .iscroll-wrapper').iscrollview('refresh');
		if(mor.ticket.loginUser.isAuthenticated === "Y"){
				cancelOrderFn();
		}else{
				if (window.ticketStorage.getItem("autologin") != "true") {
					autologinFailJump();
				} else {
					registerAutoLoginHandler(cancelOrderFn, autologinFailJump);
				}
		}
	});
		
	function registerCancelOrderCancelBtnLisener(){
		jq("#cancelOrderCancelBtn").bind("tap", function(){
			jq.mobile.changePage("finishedOrderDetail.html");
			return false;
		});
	}

	function registerCancelOrderConfirmBtnListener(){
		jq("#cancelOrderConfirmBtn").bind("tap", function(){
			// by yiguo
			// 判断当前用户是否为激活用户，未激活用户不可以提交订单
			if (mor.ticket.loginUser.activeUser != 'Y') {
				mor.ticket.util.alertMessage("当前用户未激活，请到您的注册邮箱收取12306激活邮件后按提示激活用户后再登录！");
				return false;
			}
			//弹出确认提示对话框
			WL.SimpleDialog.show(
					"温馨提示", 
					"您确定要退票吗？", 
					[ {text : '取消', handler: function(){}},
					  {text : '确定', handler: requestCanelTicket}]
				);	
		});
	}
	
	
	function requestCanelTicket(){
		var queryOrder = mor.ticket.queryOrder;
		var ticket = queryOrder.getCancelTicketInfo();
		var util = mor.ticket.util;
		
		var commonParameters = {			
			'sequence_no': ticket.sequence_no,
			'batch_no':ticket.batch_no,
			'coach_no':ticket.coach_no,
			'seat_no':ticket.seat_no
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "submitTicketRequest",
		};
		
		var options =  {
				onSuccess: requestCancleTicketConfirmSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		
		util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	
	function  requestCancleTicketConfirmSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {	
			var queryOrder = mor.ticket.queryOrder;
			var ticket = queryOrder.getCancelTicketInfo();
			ticket.payOrderID = invocationResult.payOrderID;
			if (typeof (invocationResult.ori_price) == "undefined"
					|| typeof (invocationResult.rate) == "undefined"
					|| typeof (invocationResult.return_cost) == "undefined"
					|| !invocationResult.ori_price || !invocationResult.rate
					|| !invocationResult.return_cost) {
				ticket.ticket_price = "";
				ticket.rate = "";
				ticket.return_cost = "";
			}else{
				ticket.ticket_price = invocationResult.ori_price;
				ticket.rate = invocationResult.rate;
				ticket.return_cost = invocationResult.return_cost;
			}
			mor.ticket.viewControl.isPayfinishMode = false;
			jq.mobile.changePage("payFinish.html");
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}		
	}
	
	var ticketDetailsGridTemplate =
		"<div class='orderDetailList'>" +
			"<div class='ui-grid-a'>" +
				"<div class='ui-block-a'><img src='../images/xiaoren.png'><span>{{=it.passenger_name}}</span></div>" +
				"<div class='ui-block-b'>" +
					"<table>" +
					"<tr>"+
					"{{=mor.ticket.util.getTicketTypeName(it.ticket_type_code)}}&nbsp;&nbsp;&nbsp;&nbsp;{{=mor.ticket.util.getIdTypeName(it.passenger_id_type_code)}}"+
					"</tr>" +
					"<tr>" +
					"{{=it.passenger_id_no}}"+
					"</tr>"+
					"</table>"+
				"</div>" +
			"</div>" +
			"<div class='ui-grid-b'>" +
				"<div class='ui-block-a'>{{=it.station_train_code}}</div>" +
				"<div class='ui-block-b'>{{=mor.ticket.util.getSeatTypeName(it.seat_type_code,it.seat_no)}}</div>" +
				"<div class='ui-block-c'><span class='text_orange'>{{=it.coach_name}}</span>车</div>" + 
				"<div class='ui-block-d'><span class='text_orange'>{{=it.seat_name}}</span></div>" + 
			"</div>" +
			"<div style='text-align:right;'>" +
				"<span style='color:grey'>票价：</span>" +
				"<span style='color:red'>{{=it.ticket_price}}元</span>" +
			"</div>" +
		"</div>" ;
	var generateTicketDetailsGrid = doT.template(ticketDetailsGridTemplate);
	
})();