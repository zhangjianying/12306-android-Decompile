
/* JavaScript content from js/controller/continuePayment.js in folder common */
(function(){
	function continuePaymentFn(){
		mor.ticket.viewControl.tab2_cur_page="continuePayment.html";
		//var proMsg = mor.ticket.cache.promptMsg.confirmOrder;
		//jq("#confirm_OrderTips").html(proMsg);
		var util = mor.ticket.util;
		var cache = mor.ticket.cache;
		var queryOrder = mor.ticket.queryOrder;
		var unFinishedOrder = queryOrder.getCurrentUnfinishedOrder();
		var ticketList = unFinishedOrder.myTicketList;
		var ticket = ticketList[0];
		var str = "单程：";
		if(isRoundTripOrder(ticketList)){
			jq("#continuePaymentOrderDetailPre").show();
			var RoundTripOrder = getRoundTripTicketList(ticketList);
			var goTicketList = RoundTripOrder.goList;
			ticket = goTicketList[0];
			jq("#continuePaymentOrderDetailsPromptPre").html("往程：" + util.changeDateType(ticket.train_date));
			jq("#continuePaymentFromStationNamePre").html(cache.getStationNameByCode(ticket.from_station_telecode));
			jq("#continuePaymentTrainStartTimePre").html(util.formateTrainTime(ticket.start_time) + " 出发");
			jq("#continuePaymentTrainCodeNamePre").html(ticket.station_train_code);
			jq("#continuePaymentToStationNamePre").html(cache.getStationNameByCode(ticket.to_station_telecode));
			jq("#continuePaymentTrainReachTimePre").html(util.formateTrainTime(ticket.arrive_time) + " 到达");	
			jq("#continuePaymentBookedTicketDetailsGridPre").html(generateTicketDetailsGrid(goTicketList));
						
			ticketList  = RoundTripOrder.backList;			
			ticket = ticketList[0];
			str = "返程：";			
		}
    	var endTime=mor.ticket.paytimer.endTime;
    	if(mor.ticket.paytimer.intervalId){
			clearInterval(mor.ticket.paytimer.intervalId);
			mor.ticket.paytimer.intervalId = null;
		}
    	
    	var paddingFn = mor.ticket.util.paddingWidth;
    	mor.ticket.paytimer.intervalId=setInterval(function(){
    		//TODO position:abslote
		    var nowTime = mor.ticket.util.getNewDate();
		    var nMS=endTime.getTime() - nowTime.getTime() ;
		          var myD=Math.floor(nMS/(1000 * 60 * 60 * 24));
		          var myM=paddingFn(Math.floor(nMS/(1000*60)) % 60, 2, 0);
		          var myS=paddingFn(Math.floor(nMS/1000) % 60, 2, 0);
		          if(myD>=0){
//		  			WL.Logger.debug(myM+":"+myS);
		  			jq("#continuePayTime").html(myM+":"+myS);
		          }else{
			          clearInterval(mor.ticket.paytimer.intervalId);
			          mor.ticket.paytimer.intervalId = null;
			          mor.ticket.viewControl.tab2_cur_page="queryOrder.html";
			          jq("#continuePay_line").css('display','none'); 
			  		  WL.SimpleDialog.show(
								"温馨提示", 
								"支付有效时间已过，席位已自动释放给其他乘客", 
								[ {text : '确定', handler: function() {
									mor.ticket.viewControl.bookMode = "dc";
									jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
								}}]
					  );
		  		 }
	      }, 1000);
		
		jq("#continuePaymentOrderDetailsPrompt").html(str + util.changeDateType(ticket.train_date));
		jq("#continuePaymentFromStationName").html(cache.getStationNameByCode(ticket.from_station_telecode));
		jq("#continuePaymentTrainStartTime").html(util.formateTrainTime(ticket.start_time) + " 出发");
		jq("#continuePaymentTrainCodeName").html(ticket.station_train_code);
		jq("#continuePaymentToStationName").html(cache.getStationNameByCode(ticket.to_station_telecode));
		jq("#continuePaymentTrainReachTime").html(util.formateTrainTime(ticket.arrive_time) + " 到达");	
		jq("#continuePaymentBookedTicketDetailsGrid").html(generateTicketDetailsGrid(ticketList));
		var myTicketList = unFinishedOrder.myTicketList;
		var ticket_status = myTicketList[0].ticket_status_code;
		if(ticket_status == 'j'){
			jq("#continuePaymentOrderTotalPriceInfo").hide();
			queryOrder.getOrderPrice();
			if(unFinishedOrder.returnTotalPrice >= 0){
				jq("#continuePaymentOrderDifPrice").html(unFinishedOrder.returnTotalPrice+"元");
				jq("#continuePaymentOrderDifPriceInfo").show();
				jq("#continuePaymentConfirmPaymentBtn .ui-btn-text").html("确认改签");
			}else {
				jq("#continuePaymentNewTicketPrice").html(unFinishedOrder.newTotalPrice+"元");
				jq("#continuePaymentOldTicketPrice").html(unFinishedOrder.originTotalPrice+"元");
				jq("#continuePaymentConfirmPaymentBtn .ui-btn-text").html("立即支付");
				jq("#continuePaymentOrderRepayPriceInfo").show();
			}
			jq("#continuePaymentCancelOrderBtn .ui-btn-text").html("取消改签");
			
		}else{
			jq("#continuePaymentOrderTotalPrice").html(unFinishedOrder.ticket_price_all);
			jq("#continuePaymentOrderTotalPrice").show();
		}		
		jq("#continuePaymentView .ui-content").iscrollview("refresh");
	}
	
	
	jq("#continuePaymentView").live("pagehide", function(){
		clearInterval(mor.ticket.paytimer.intervalId);
	});
	
	jq("#continuePaymentView").live("pageinit", function(){
		registerContinuePaymentBackBtnListener();
		registerContinuePaymentCancelOrderBtnLisener(); 
		registercontinuePaymentConfirmPaymentBtnListener();
		//线下支付btn
		//registerOffLinePayConfirmTicketBtnListeners();
		if(mor.ticket.loginUser.isAuthenticated === "Y"){
			continuePaymentFn();
		}else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
			} else {
				registerAutoLoginHandler(continuePaymentFn, autologinFailJump);
			}

//			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	function isRoundTripOrder(order){
		if(order.length > 1){
			var ticket = order[0];
			for(var i=1; i< order.length ; i++){
				if(ticket.from_station_telecode != order[i].from_station_telecode){
					return true;
				}
			}
			return false;
		}else {
			return false;
		}
		
	}
	
	function getRoundTripTicketList(order){
		var list1 = [];
		var list2 = [];
		var ticket = order[0];	
		list1.push(ticket);
		for(var i=1; i< order.length ; i++){
			if(ticket.from_station_telecode == order[i].from_station_telecode){
				list1.push(order[i]);
			} else {
				list2.push(order[i]);
			}
		}
		var list1Date = list1[0].train_date+list1[0].start_time;
		var list2Date = list2[0].train_date+list2[0].start_time;
		if(list1Date < list2Date){
			return {
				goList:list1,
				backList:list2
			};
		}else{
			return {
				goList:list2,
				backList:list1
			};
		}
	}
	
	function registerContinuePaymentBackBtnListener(){
		jq("#continuePaymentBackBtn").bind("tap",function(e){
			jq.mobile.changePage("orderList.html");
			return false;
		});
	}
	
	function registerContinuePaymentCancelOrderBtnLisener(){
		jq("#continuePaymentCancelOrderBtn").bind("tap", function(){
			WL.SimpleDialog.show(
    				"温馨提示", 
    				"一天内3次申请车票成功后取消订单，当日将不能在12306继续购票！", 
    				[ {text : '取消', handler: function(){}},
    				  {text : '确定', handler: function(){cancelOrderProcedure();}}]
    		);
			return false;
		});
	}
	
	function cancelOrderProcedure(){
		var order = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
		var util = mor.ticket.util;
		var cancelFlag = '0';// 0:取消普通待支付订单;1取消改签待支付订单
		for(var i=0; i< order.myTicketList.length; i++){
			if(order.myTicketList[i].ticket_status_code == 'j'){//改签待支付
				cancelFlag = '1';
				break;
			}
		}		
		var commonParameters = {			
			'sequence_no': order.myTicketList[0].sequence_no,
			'coach_no': '00',
			'seat_no':'0000',
			'batch_no':'0',
			'cancel_flag':cancelFlag
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "cancelOrder"
		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	}
	
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {	
			//不用更改mode，保持原来模型的订票模式
			//mor.ticket.viewControl.bookMode = "dc";
			mor.ticket.viewControl.tab2_cur_page="queryOrder.html";
			mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
			jq.mobile.changePage("queryOrder.html");
			mor.ticket.util.alertMessage("订单取消成功");
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function registercontinuePaymentConfirmPaymentBtnListener(){
		jq("#continuePaymentConfirmPaymentBtn").bind("tap", function(){
			//mjl
			var message=(jq("#continuePaymentConfirmPaymentBtn .ui-btn-text").html()=="确认改签")?"确定要改签吗？":"确定要支付吗？";
			WL.SimpleDialog.show(
					"温馨提示", 
					message, 
					[ {text : '取消', handler: function(){}},
					  {text : '确定', handler: comfirmPayment}]
				);
			/**
			mor.ticket.viewControl.tab2_cur_page="queryOrder.html";
			var order = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
			if(order.returnTotalPrice == undefined ||order.returnTotalPrice < 0){//普通订单支付，或者改签 低改高
				requestEpayProcedure(order);
			}else{
				changeTicketNormalProcedure(order);	//平改，高改低	
			}
			return false;
			**/
		});
	}
	
	
	function comfirmPayment(){
		mor.ticket.viewControl.tab2_cur_page="queryOrder.html";
		var order = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
		if(order.returnTotalPrice == undefined ||order.returnTotalPrice < 0){//普通订单支付，或者改签 低改高
			requestEpayProcedure(order);
		}else{
			changeTicketNormalProcedure(order);	//平改，高改低	
		}
		return false;
	}
	
	
	
	function requestEpayProcedure(order){
		
		var util = mor.ticket.util;
		var pay_start = 1;//普通付费
		if(order.myTicketList[0].ticket_status_code == "j"){
			pay_start = 2;//改签付费
		}
//		WL.Logger.debug("#######################continue payment.sequence_no:"+order.myTicketList[0].sequence_no);
		var commonParameters = {			
			'sequence_no': order.myTicketList[0].sequence_no,
			'batch_no':order.myTicketList[0].batch_no,
			'order_timeout_date':order.myTicketList[0].pay_limit_time,
			'pay_start':pay_start.toString()
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "epay"
		};
		
		var options =  {
				onSuccess: epaySucceededNew,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	}
	
	function epaySucceededNew(result){
		if(busy.isVisible()){
			busy.hide();
		}	
		var invocationResult = result.invocationResult;
		var orderManager = mor.ticket.orderManager;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			orderManager.paymentOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
			//获取batch no用来在支付完成后过滤对应车票
			var myTicketList = orderManager.paymentOrder.myTicketList;
			mor.ticket.payment.batch_nos = [];
			for(var i=0;i<myTicketList.length;i++){
				var batch_no = myTicketList[i].batch_no;
				mor.ticket.payment.batch_nos.push(batch_no);
			}
			var parameters={
		         "showLocationBar": true,
		         "interfaceName":invocationResult['interfaceName'],
		         "interfaceVersion":invocationResult['interfaceVersion'],
		         "tranData":invocationResult['tranData'],
		         "merSignMsg":invocationResult['merSignMsg'],
		         "appId":invocationResult['appId'],
		         "transType":invocationResult['transType'],
				 "epayurl":invocationResult['epayurl']
			};
			/*for(var i=0;i<mor.ticket.payment.batch_nos.length;i++){
//				WL.Logger.debug("#######################continue payment.batch_nos:"+mor.ticket.payment.batch_nos[i]);
			}*/
			window.plugins.childBrowser.showWebPage(invocationResult['epayurl'], parameters); 
			
			//jq.mobile.changePage("queryOrder.html");
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	//线下支付--Begin
	//周自昌
	

		function registerOffLinePayConfirmTicketBtnListeners() {
			jq("#offLinePayConfirmTickets").bind("tap",function(){
			var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
			var ticketList = unFinishedOrder.myTicketList;;
				WL.SimpleDialog
						.show(
								"温馨提示",
								"确定要线下支付吗?",
								[
										{
											text : '取消',
											handler : function() {
											}
										},
										{
											text : '确定',
											handler : function() {
												var parameter = {
													'sequence_no' : ticketList[0].sequence_no
												};
												getOffLinePayParameter(parameter);
											}
										} ]);
			});
	}
		
	function getOffLinePayParameter(parameter){
		var util = mor.ticket.util;
		var commonParameters = parameter;
//		WL.Logger.debug("#######################order detail payment.sequence_no:"+commonParameters.sequence_no);
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "offLinePay"
		};
		
		var options =  {
				onSuccess: offLinePaySucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
//		WL.Client.invokeProcedure(invocationData, options);
//		busy.show();
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function offLinePaySucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var orderManager=mor.ticket.orderManager;
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)){
			orderManager.paymentOrder.myTicketList = [];
			var queryOrder = mor.ticket.queryOrder;
			var unFinishedOrder = queryOrder.getCurrentUnfinishedOrder();
			var ticketList = unFinishedOrder.myTicketList;
			for(var i=0; i<ticketList.length; i++){
				var ticket = ticketList[i];
				orderManager.paymentOrder.myTicketList.push(ticket);
			}
			for(var i=0;i<ticketList.length;i++){
			if(ticketList[i].sequence_no!=invocationResult.sequence_no){
				ticketList[i].sequence_no=invocationResult.sequence_no;
				}
			}
			jq.mobile.changePage("offLinePayFinish.html");
		}else{
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	//线下支付--End
	
	
	
	function changeTicketNormalProcedure(ticketList){//平改、或者高改低时流程
		var currentTicketDetail = ticketList.myTicketList[0];
		var util = mor.ticket.util;
		var pay_mode;
		if(ticketList.returnTotalPrice == 0){
			pay_mode = 'N';
		}else{
			pay_mode = 'T';
		}
		var commonParameters = {			
			'sequence_no': currentTicketDetail.sequence_no,
			'batch_no':currentTicketDetail.batch_no,
			'lose_time':currentTicketDetail.limit_time,
			'pay_type':pay_mode
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "confirmChangeTicket"
		};
		
		var options =  {
				onSuccess: requestConfirmChangeTicketSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};

		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function  requestConfirmChangeTicketSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {		
			mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
			mor.ticket.viewControl.bookMode = "dc";
			jq.mobile.changePage("queryOrder.html");
			mor.ticket.util.alertMessage("改签成功！");
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	var ticketDetailsGridTemplate =
		"{{~it :ticket:index}}" +
		"<div class='orderDetailList'>" +
			"<div class='ui-grid-a'>" +
				"<div class='ui-block-a'><img src='../images/xiaoren.png'><span>{{=ticket.passenger_name}}</span></div>" +
				"<div class='ui-block-b'>" +
					"<table>" +
					"<tr>"+
					"{{=mor.ticket.util.getTicketTypeName(ticket.ticket_type_code)}}&nbsp;&nbsp;&nbsp;&nbsp;{{=mor.ticket.util.getIdTypeName(ticket.passenger_id_type_code)}}"+
					"</tr>" +
					"<tr>" +
					"<br>" +
					"{{=ticket.passenger_id_no}}"+
					"</tr>"+
					"</table>"+
				"</div>" +
			"</div>" +
			"<div class='ui-grid-b'>" +
				"<div class='ui-block-a'>{{=ticket.station_train_code}}</div>" +
				"<div class='ui-block-b'>{{=mor.ticket.util.getSeatTypeName(ticket.seat_type_code,ticket.seat_no)}}</div>" +
				"<div class='ui-block-c'><span class='text_orange'>{{=ticket.coach_name}}</span>车</div>" + 
				"<div class='ui-block-d'><span class='text_orange'>{{=ticket.seat_name}}</span></div>" + 
			"</div>" +
			"<div style='text-align:right;'>" +
				"<span style='color:grey'>票价：</span>" +
				"<span style='color:red'>{{=ticket.ticket_price}}元</span>" +
			"</div>" +
		"</div>" +
		"{{~}}";
	var generateTicketDetailsGrid = doT.template(ticketDetailsGridTemplate);
	
})();