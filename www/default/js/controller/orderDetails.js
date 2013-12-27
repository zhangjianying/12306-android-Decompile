
/* JavaScript content from js/controller/orderDetails.js in folder common */
(function(){
	jq("#orderDetailsView").live("pageinit", function(){
		registerCancelBtnLisener(); 
		registerBuyBackTicketBtnLisener(); 
		registerConfirmPaymentBtnListener();
		//线下支付btn
		//registerOffLinePayConfirmTicketBtnListener();
		//改签btn
		registerConfirmChangeTicketBtnListener();
		//var proMsg = mor.ticket.cache.promptMsg.confirmOrder;
		//jq("#confirmOrderTips").html(proMsg);
	});
	jq("#orderDetailsView").live("pagehide", function(e, data){
		clearInterval(mor.ticket.paytimer.intervalId);
		
		//如果是gc，那么当前页面代表改签订单已经成功提交这时候将mode改成dc
		
		if(mor.ticket.viewControl.bookMode == "gc"){
			
			mor.ticket.viewControl.bookMode = "dc";
		}
		if(mor.ticket.viewControl.bookMode != "fc"){
			
			//不更改mode
			//mor.ticket.viewControl.bookMode = "dc";
		}else{
			if(data.nextPage.attr("id") != "bookTicketView"){
				mor.ticket.viewControl.bookMode = "dc";
			}
		}
	});
	
	function orderDetailsFn(){
		mor.ticket.viewControl.tab1_cur_page=vPathViewCallBack()+"MobileTicket.html";
		var ticket = mor.ticket.currentTicket;
		var train_date = ticket.train_date;
		var util=mor.ticket.util;		
		var cache = mor.ticket.cache;		
		var orderManager=mor.ticket.orderManager;
		var ticketList=orderManager.getTicketList();
		var preOrderLen = 0;
		var mode = mor.ticket.viewControl.bookMode;
		var str="单程：";	
		
		var endTime=mor.ticket.paytimer.endTime;
    	var strs = "";
    	if(mor.ticket.paytimer.intervalId){
		    clearInterval(mor.ticket.paytimer.intervalId);
			mor.ticket.paytimer.intervalId = null;
		}
    	
    	var paddingFn = mor.ticket.util.paddingWidth;
    	mor.ticket.paytimer.intervalId=setInterval(function(){
	          var nowTime = mor.ticket.util.getNewDate();
	          var nMS=endTime.getTime() - nowTime.getTime() ;
	          var myD=Math.floor(nMS/(1000 * 60 * 60 * 24));
	          var myM=paddingFn(Math.floor(nMS/(1000*60)) % 60, 2, '0');
	          var myS=paddingFn(Math.floor(nMS/1000) % 60, 2, '0');
	          if(myD>= 0){
		  			strs = myM+":"+myS;
		  			jq("#payTime").html(strs);
		          }else{
		        	//取消时钟倒计时
		        	clearInterval(mor.ticket.paytimer.intervalId);
		        	mor.ticket.paytimer.intervalId = null;
		  		    jq("#pay_line").css('display','none'); 
		  		    WL.SimpleDialog.show(
							"温馨提示", 
							"支付有效时间已过，席位已自动释放给其他乘客", 
							[ {text : '确定', handler: function() {
								mor.ticket.viewControl.bookMode = "dc";
								 //add by yiguo
					            mor.ticket.queryOrder.currentQueryOrder = null;
					            mor.ticket.queryOrder.queryOrderList = null;
					            
								jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
							}}]
						);
		  		}
	      }, 1000);
    	jq("#orderDetailPre").hide();	
		if(mode === "wc"){
			str="往程：";
			jq("#confirmPaymentBtn").hide();
			jq("#offLinePayConfirmTicket").hide();
			jq("#buyBackTicketBtn").show();	
		} else if(mode === "fc") {
			jq("#orderDetailPre").show();	
			var preTicket = orderManager.getPreTicketDetail();
			jq("#orderDetailsPromptPre").html("往程：" + mor.ticket.util.getLocalDateString1(ticket.pre_train_date));
			jq("#fromStationNamePre").html(cache.getStationNameByCode(preTicket.from_station_telecode));
			jq("#trainStartTimePre").html(preTicket.start_time + " 出发");
			jq("#trainCodeNamePre").html(preTicket.station_train_code);
			jq("#trainDurationTimePre").html(util.getLiShiStr(ticket.lishi));
			jq("#toStationNamePre").html(cache.getStationNameByCode(preTicket.to_station_telecode));
			jq("#trainReachTimePre").html(preTicket.arrive_time + " 到达");				
			str="返程：";
			var roundTripOrder = orderManager.getRoundTripOrderList();
			var preOrderDetail = roundTripOrder.pre;
			ticketList = roundTripOrder.current;
			preOrderLen = preOrderDetail.length;
			jq("#bookedTicketDetailsGridPre").html(generateTicketDetailsGrid(preOrderDetail));
			jq("#confirmPaymentBtn").show();
			jq("#buyBackTicketBtn").hide();	
			jq("#offLinePayConfirmTicket").show();
		}else{
			jq("#buyBackTicketBtn").hide();	
			jq("#confirmPaymentBtn").show();
			if(mode === "dc"){
				jq("#offLinePayConfirmTicket").hide();	
			}else{
				jq("#offLinePayConfirmTicket").hide();
			}
			
		}	
		
		jq("#orderDetailsPrompt").html(str +mor.ticket.util.getLocalDateString1(train_date));
		jq("#fromStationName").html(cache.getStationNameByCode(ticket.from_station_telecode));
		jq("#trainStartTime").html(ticket.start_time + " 出发");
		jq("#trainCodeName").html(ticket.station_train_code);
		jq("#trainDurationTime").html(util.getLiShiStr(ticket.lishi));
		jq("#toStationName").html(cache.getStationNameByCode(ticket.to_station_telecode));
		jq("#trainReachTime").html(ticket.arrive_time + " 到达");	
		
		if(mode === "gc"){
			jq("#orderDetailsView .ui-header>h1").html("确认支付(改签)");
			jq("#ticketPriceInfo").hide();
			if(ticketList[0].pay_mode != 'Y'){
				jq("#orderDifPrice").html(ticketList[0].return_total + "元");
				jq("#orderRepayPriceInfo").hide();	
				jq("#orderDifPriceInfo").show();
				jq("#confirmChangeTicket .ui-btn-text").html("确认改签");
			}else{
				var newPrice = 0;
				for(var i=0; i<ticketList[0].myTicketList.length;i++){
					newPrice += parseFloat(ticketList[0].myTicketList[i].ticket_price);
				}
				jq("#newTicketPrice").html(newPrice.toFixed(2) + "元");
				jq("#oldTicketPrice").html(ticketList[0].old_ticket_price + "元");
				jq("#confirmChangeTicket .ui-btn-text").html("立即支付");
				jq("#orderDifPriceInfo").hide();
				jq("#orderRepayPriceInfo").show();			
			}			
			jq("#cancelOrderBtn .ui-btn-text").html("取消改签");
			jq("#confirmPaymentBtn").hide();
			jq("#offLinePayConfirmTicket").show();
			jq("#confirmChangeTicket").show();
			jq("#bookedTicketDetailsGrid").html(generateTicketDetailsGrid(ticketList[0].myTicketList));
		}else {
			jq("#orderDetailsView .ui-header>h1").html("确认支付");
			jq("#orderTicketNumber").html(orderManager.totalTicketNum);
			jq("#orderSuccessTicktNumber").html(ticketList.length + preOrderLen);
			jq("#orderTotalPrice").html((orderManager.getActualTotalTicketPrice()).toFixed(2));
			for(var i=0; i<ticketList.length;i++){
				var seatNo = ticketList[i].seat_no;
				var seatType = ticketList[i].seat_type_code;
				var lastnum = ticketList[i].seat_no.substring(3, 4);
				if(lastnum >= 'a' && lastnum <= 'z'){
					   WL.SimpleDialog.show(
								"温馨提示", 
								"本次申请席位的结果为"+mor.ticket.util.getSeatTypeName(seatType,seatNo)+"，请确认。", 
								[ {text : '确定', handler: function() {}}]
							);
					   break;
				}
			}
			jq("#bookedTicketDetailsGrid").html(generateTicketDetailsGrid(ticketList));
			jq("#orderRepayPriceInfo").hide();
			jq("#orderDifPriceInfo").hide();
			jq("#ticketPriceInfo").show();
			jq("#cancelOrderBtn .ui-btn-text").html("取消订单");
			jq("#confirmChangeTicket").hide();			
		}
		contentIscrollTo(0,0,0);
		jq("#orderDetailsView .ui-content").iscrollview("refresh");
		noSeatNoTip();
	
	}
	jq("#orderDetailsView").live("pagebeforeshow", function(){
		if(mor.ticket.loginUser.isAuthenticated === "Y"){
			orderDetailsFn();
		}else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
				} else {
				registerAutoLoginHandler(orderDetailsFn, autologinFailJump);
				}

			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	function contentIscrollTo(x,y,time){
		if(jq("#orderDetailsView .ui-content").attr("data-iscroll")!=undefined){
			jq("#orderDetailsView .ui-content").jqmData('iscrollview').iscroll.scrollTo(x,y,time);
		}
	}
	
	function updateTicketDetails(passengers,tickets){		
		for(var i=0;i<passengers.length;i++){
			var seat_type=passengers[i].seat_type;
			for(var m=0;m<tickets.length;m++){
				if(tickets[m].type_id==seat_type){
					passengers[i].ticket_price=tickets[m].price;
					break;
				}
			}
		}
		
	};
	
	function registerConfirmPaymentBtnListener(){
		jq("#confirmPaymentBtn").off().on("tap", function(){
			// by yiguo
			// 判断当前用户是否为激活用户，未激活用户不可以提交订单
			if (mor.ticket.loginUser.activeUser != 'Y') {
				mor.ticket.util.alertMessage("当前用户未激活，请到您的注册邮箱收取12306激活邮件后按提示激活用户后再登录！");
				return false;
			}
			WL.SimpleDialog.show(
					"温馨提示", 
					"确定要支付吗？", 
					[ {text : '取消', handler: function(){}},
					  {text : '确定', handler: submitPaymentGateWay}]
				);
				
		}); 		
	}
	
	function submitPaymentGateWay(){	
		var orderManager = mor.ticket.orderManager;
		orderManager.paymentOrder.myTicketList = [];
		var ticketList = orderManager.getTicketList();
		for(var i=0; i<ticketList.length; i++){
			var ticket = ticketList[i];
			orderManager.paymentOrder.myTicketList.push(ticket);
		}
		//获取batch no用来在支付完成后过滤对应车票
		var myTicketList = orderManager.paymentOrder.myTicketList;
		mor.ticket.payment.batch_nos = [];
		for(var i=0;i<myTicketList.length;i++){
			var batch_no = myTicketList[i].batch_no;
			mor.ticket.payment.batch_nos.push(batch_no);
		}
		orderManager.clearTicketList();
		var payment=mor.ticket.payment;
		var parameters={
				         "showLocationBar": true,
				         "interfaceName":payment['interfaceName'],
				         "interfaceVersion":payment['interfaceVersion'],
				         "tranData":payment['tranData'],
				         "merSignMsg":payment['merSignMsg'],
				         "appId":payment['appId'],
				         "transType":payment['transType'],
						 "epayurl":payment['epayurl']
		  };
		/*for(var i=0;i<mor.ticket.payment.batch_nos.length;i++){
			WL.Logger.debug("#######################order detail payment.batch_nos:"+mor.ticket.payment.batch_nos[i]);
		}*/
		window.plugins.childBrowser.showWebPage(payment['epayurl'], parameters);
		//jq.mobile.changePage("queryOrder.html");
		mor.ticket.viewControl.bookMode = "dc";
  	    return false;
	}
	
	function registerBuyBackTicketBtnLisener(){
		jq("#buyBackTicketBtn").bind("tap",function(){
			var orderManager = mor.ticket.orderManager;
			orderManager.confirmRoundTripTicketList(mor.ticket.passengerList);
			orderManager.setPreTicketDetail(mor.ticket.currentTicket);
			mor.ticket.viewControl.bookMode = "fc";
			jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
			return false;
		});
	}

	function registerCancelBtnLisener(){
		jq("#cancelOrderBtn").bind("tap", function(){
			WL.SimpleDialog.show(
    				"温馨提示", 
    				"一天内3次申请车票成功后取消订单，当日将不能在12306继续购票！", 
    				[ {text : '取消', handler: function(){}},
    				  {text : '确定', handler: function(){sendCancelOrderRequest();}}]
    		);
		});
	};
	
	function sendCancelOrderRequest() {  	  	
  	  	var orderManager = mor.ticket.orderManager;
  	    var ticketList=orderManager.getTicketList();
  	    
		var util = mor.ticket.util;
		var commonParameters;
		if(mor.ticket.viewControl.bookMode == "gc"){
			commonParameters = {			
				'sequence_no': ticketList[0].myTicketList[0].sequence_no,
				'batch_no': '0',
				'cancel_flag':'1'// 0:取消普通待支付订单;1取消改签待支付订单
			};
		}else{
			commonParameters = {			
				'sequence_no': ticketList[0].sequence_no,
				'batch_no': ticketList[0].batch_no,
				'cancel_flag':'0'// 0:取消普通待支付订单;1取消改签待支付订单
			};
		}		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "cancelOrder"
		};
		
		var options =  {
				onSuccess: cancelOrderRequestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};	
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function cancelOrderRequestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			
		//不更改mode
			
		//mor.ticket.viewControl.bookMode = "dc";
		mor.ticket.orderManager.clearTicketList();
		//取消时钟倒计时
			WL.SimpleDialog.show(
					"温馨提示", 
					"订单取消成功!", 
					[ {text : '确定', handler: function(){
						if(mor.ticket.viewControl.bookMode === "gc"){
							jq.mobile.changePage("queryOrder.html");				
						}else{
							jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
						}	
					}}]
				);	
		}else {
			if(invocationResult.isSuccessful && invocationResult.succ_flag == "-1"){
				mor.ticket.viewControl.bookMode = "dc";
				jq.mobile.changePage("queryOrder.html");				
			}
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function registerConfirmChangeTicketBtnListener(){
		jq("#confirmChangeTicket").bind("tap",function(){
			var orderManager=mor.ticket.orderManager;
			var ticketList=orderManager.getTicketList();
			var currentTicketDetail = ticketList[0].myTicketList[0];
			var util = mor.ticket.util;
			if(ticketList[0].pay_mode != 'Y'){
				changeTicketNormalProcedure(ticketList);
			}else{
				WL.SimpleDialog.show(
						"温馨提示", 
						"确定要支付吗？", 
						[ {text : '取消', handler: function(){}},
						  {text : '确定', handler: function(){
							  var parameters = {			
										'sequence_no': currentTicketDetail.sequence_no,
										'batch_no': currentTicketDetail.batch_no,
										'order_timeout_date':currentTicketDetail.pay_limit_time,
										'pay_start':'2'//改签为2
									};	
									getEpayParameter(parameters);
						  }}]
					);
			}
		});
	}
	
	//线下支付--Begin
	//周自昌
	

		function registerOffLinePayConfirmTicketBtnListener() {
				jq("#offLinePayConfirmTicket").bind("tap",function(){
					var orderManager=mor.ticket.orderManager;
					var ticketList=orderManager.getTicketList();
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
														getOffLinePayParameters(parameter);
													}
												} ]);
					});	
	}
		
	function getOffLinePayParameters(parameter){
		var util = mor.ticket.util;
		var commonParameters = parameter;
//		WL.Logger.debug("#######################order detail payment.sequence_no:"+commonParameters.sequence_no);
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "offLinePay"
		};
		
		var options =  {
				onSuccess: offLinePaySucceededs,
				onFailure: util.creatCommonRequestFailureHandler()
		};
//		WL.Client.invokeProcedure(invocationData, options);
//		busy.show();
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function offLinePaySucceededs(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var orderManager = mor.ticket.orderManager;
		var ticketList = orderManager.orderTicketList;
			var invocationResult = result.invocationResult;
			if (mor.ticket.util.invocationIsSuccessful(invocationResult)){
				orderManager.paymentOrder.myTicketList = [];
				for(var i=0;i<ticketList.length;i++){
					orderManager.paymentOrder.myTicketList.push(ticketList[i]);
					//var ticket = mor.ticket.currentTicket;
					ticketList[i]._ticket = mor.ticket.currentTicket;
					//WL.Logger.error(ticketList[i]._ticket.ticket_type_code);
				}
				for(var i=0;i<ticketList.length;i++){
				if(ticketList[i].sequence_no!=invocationResult.sequence_no){
					mor.ticket.orderManager.paymentOrder.myTicketList[i].sequence_no=invocationResult.sequence_no;
					}
				}
			jq.mobile.changePage("offLinePayFinishEnter.html");
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}	
	}
	
	//线下支付--End
	
	
	
	
	function changeTicketNormalProcedure(ticketList){//平改、或者高改低时流程
		var currentTicketDetail = ticketList[0].myTicketList[0];
		var util = mor.ticket.util;
		var commonParameters = {			
			'sequence_no': currentTicketDetail.sequence_no,
			'batch_no':currentTicketDetail.batch_no,
			'lose_time':currentTicketDetail.lose_time,
			'pay_type':ticketList[0].pay_mode
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
			mor.ticket.viewControl.bookMode = "dc";
			jq.mobile.changePage("queryOrder.html");
			mor.ticket.util.alertMessage("改签成功！");
			mor.ticket.orderManager.clearTicketList();
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function getEpayParameter(parameters){
			var util = mor.ticket.util;
			var commonParameters = parameters;
//			WL.Logger.debug("#######################order detail payment.sequence_no:"+commonParameters.sequence_no);
			
			var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "epay"
			};
			
			var options =  {
					onSuccess: epaySucceededNew,
					onFailure: util.creatCommonRequestFailureHandler()
			};
//			WL.Client.invokeProcedure(invocationData, options);
//			busy.show();
			mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
			return false;
	}
	function epaySucceededNew(result){
		if(busy.isVisible()){
			busy.hide();
		}	
		var orderManager = mor.ticket.orderManager;
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {				
		orderManager.paymentOrder.myTicketList = [];
		var ticketList = orderManager.getTicketList();
		for(var i=0; i<ticketList[0].myTicketList.length; i++){
		var ticket = ticketList[0].myTicketList[i];
			orderManager.paymentOrder.myTicketList.push(ticket);
		}
		
		//获取batch no用来在支付完成后过滤对应车票
		var myTicketList = orderManager.paymentOrder.myTicketList;
		mor.ticket.payment.batch_nos = [];
		for(var i=0;i<myTicketList.length;i++){
			var batch_no = myTicketList[i].batch_no;
			mor.ticket.payment.batch_nos.push(batch_no);
		}
		/*for(var i=0;i<mor.ticket.payment.batch_nos.length;i++){
			WL.Logger.debug("#######################order detail payment.batch_nos:"+mor.ticket.payment.batch_nos[i]);
		}*/
		
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
		window.plugins.childBrowser.showWebPage(invocationResult['epayurl'], parameters); 
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
		orderManager.clearTicketList();
	}
	function noSeatNoTip(){
	    var util = mor.ticket.util;
	  	var orderManager = mor.ticket.orderManager;
	  	if(mor.ticket.viewControl.bookMode == "dc"){
	  	    var ticketList=orderManager.getTicketList();
	  	}else if(mor.ticket.viewControl.bookMode == "wc"){
	  	    var ticketList=orderManager.getTicketList();
	  	}else if(mor.ticket.viewControl.bookMode == "fc"){
	  	    var ticketList=orderManager.getRoundTripOrderList().current;
	  	}else if(mor.ticket.viewControl.bookMode == "gc"){
	  	    var ticketList=orderManager.getTicketList()[0].myTicketList;
	  	}
  	    
  	    for(var i=0;i< ticketList.length;i++){
			var ticket = ticketList[i];
			if(ticket.seat_name.indexOf("无座")>=0){
				util.alertMessage("您所购车次的"+util.getSeatTypeName(ticket.seat_type_code)+"已售完，系统自动为您分配了无座票");
				break;
			}
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
				"<span style='color:red'>{{=parseFloat(ticket.ticket_price).toFixed(2)}}元</span>" +
			"</div>" +
		"</div>" +
		"{{~}}";
	var generateTicketDetailsGrid = doT.template(ticketDetailsGridTemplate);
	
})();