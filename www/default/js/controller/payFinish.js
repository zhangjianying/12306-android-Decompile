
/* JavaScript content from js/controller/payFinish.js in folder common */
(function(){
	
	function payFinishFn(){
		   var user = mor.ticket.loginUser;

		mor.ticket.viewControl.tab2_cur_page="queryOrder.html";
		var payMode = mor.ticket.viewControl.isPayfinishMode;
		if(payMode){
			var orderManager=mor.ticket.orderManager;
			var myTicketList = orderManager.paymentOrder.myTicketList;
			var sequence_no = myTicketList[0].sequence_no;
			jq("#sequence_no").html(sequence_no);
			
			var msg = gettiketmessage(myTicketList);
			jq("#customer_name").html(msg);
			jq("#trainDetailsGridAll").html(payFinishgenerateTicketDetailsGrid(myTicketList));
			
			var priceSum = 0;
			for(var i=0;i<myTicketList.length; i++){
				priceSum += parseFloat(myTicketList[i].ticket_price);
			}
			jq("#payfinishTotalPrice").html(priceSum.toFixed(2) + "元");
			var warmStr = "如您拟于乘车前到车站售票窗口换票，请预留足够的换票时间，以免窗口排队人数较多，耽误您检票乘车。";
			jq("#payFinishtView .tips2 p").html(warmStr);
			jq(".tipWarm").show();
			jq('#queryOrder').attr('href', 'queryOrder.html');
			 //add by yiguo
            mor.ticket.queryOrder.currentQueryOrder = null;
            mor.ticket.queryOrder.queryOrderList = null;
		}else{
			jq("#payFinishtView .ui-header h1").html("退票完成");
			jq(".tipWarm").hide();
			var queryOrder = mor.ticket.queryOrder;
			var ticket = queryOrder.getCancelTicketInfo();
			jq("#payFinish").hide();
			jq("#cancelFinish").show();
			jq("#payOrderID").html(ticket.payOrderID);
		    if(ticket.ticket_price ==""){
				jq("#firstTicketReturnPrice").hide();
				jq("#secondTicketReturnPrice").hide();
		    }else{
				jq("#firstTicketReturnPrice").show();
				jq("#secondTicketReturnPrice").show();
				jq("#oriTicketPrice").html(ticket.ticket_price);
				if(ticket.rate=="-1"){
					jq("#ticketPriceRateId").hide();
				}else{
					jq("#ticketPriceRate").html(ticket.rate);
				}
				jq("#ticketReturnCost").html(ticket.return_cost);
				jq("#shouldTicketPrice").html((ticket.ticket_price - ticket.return_cost).toFixed(2));
		    }
			var warmStr = 
					"1、应退票款按银行规定时限退还至购票时所使用的银行卡，请注意查收。<br/>" +
					"2、如果需要退票报销凭证，请凭购票所使用的乘车人有效身份证件原件和订单号码在办理退票之日起10日内到车站退票窗口索取。<br/>" +
					"3、退票成功后，将向您注册时提供的有效邮箱和手机发送退票信息，请稍后查询。";
			jq("#cancelFinish .tips2 p").html(warmStr);
			jq("#payFinishTips").hide();
			jq('#queryOrder').attr('href', 'finishedOrderDetail.html');
			jq('#queryOrder').on('tap', function(e) {
				// 如果用户点击. 重新更新原有订单信息.
				e.stopImmediatePropagation();
				e.preventDefault();

				var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "queryOrder"
				};
				var onSuccess = function(result) {
					// 更新原有的订单信息.
					var orderList = result.invocationResult.orderList;
					
					if (orderList && orderList.length > 0) {
						 
						
						var order = orderList[0];
						mor.ticket.queryOrder.replaceOrderBySequenceNo(ticket.sequence_no, order);
						setTimeout(function() {
							jq.mobile.changePage(getViewBasePath('finishedOrderDetail.html'));
						}, 1);
					} else {
						onFailure();
					}
				};
				var onFailure = function() {
					// 如果获取数据出错， 返回到订单查询页面.
					jq.mobile.changePage(getViewBasePath('queryOrder.html'));
				};
				var options =  {
					onSuccess: onSuccess,
				    onFailure: onFailure
				};
				mor.ticket.util.invokeWLProcedure({'sequence_no': ticket.sequence_no},invocationData, options);
			});
			mor.ticket.viewControl.isPayfinishMode = true;
		}
		
		contentIscrollRefresh();

	}
   jq("#payFinishtView").live("pagebeforeshow", function(){
	   var user = mor.ticket.loginUser;

		if (user.isAuthenticated === "Y") {
			payFinishFn();
		} else {
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
			} else {
				registerAutoLoginHandler(payFinishFn, autologinFailJump);
			}

			// jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
   
   function gettiketmessage(myTicketList){
	  var passenger1=[],passenger2=[],passenger3=[],passenger4=[],passenger5=[],passenger6=[];
	  for(var i=0;i<myTicketList.length;i++){
		  var  ticket = myTicketList[i];
		// 学生票，提示换票
		  if(ticket.ticket_type_code=='3'){
				  passenger1.push(ticket.passenger_name);
		  }//残军票，提示换票
		  else if(ticket.ticket_type_code=='4'){
			  passenger2.push(ticket.passenger_name);
		  }//电子票
		  else if(ticket.eticket_flag=='Y'){
			  passenger3.push(ticket.passenger_name);
//			  WL.Logger.debug("###################################ticket.eticket_flag###:"+ticket.eticket_flag);
		  }//二代证，不是电子票， 是儿童票的
		  else if(ticket.eticket_flag=='N'&&ticket.passenger_id_type_code=='1'&&ticket.ticket_type_code=='2'){
			  passenger4.push(ticket.passenger_name);
		  }//二代证，不是电子票， 不是儿童票的
		  else if(ticket.eticket_flag=='N'&&ticket.passenger_id_type_code=='1'&&ticket.ticket_type_code=='1'){
			  passenger5.push(ticket.passenger_name);
		  }else if(ticket.eticket_flag=='N'&&ticket.passenger_id_type_code!='1'&&ticket.ticket_type_code!='3'&&ticket.ticket_type_code!='4') {
			  passenger6.push(ticket.passenger_name);
		  }
	  }
	  var msg = "<p style='text-indent:2em;'>本应用将在“订单查询”中显示订单信息，并向您注册时提供的邮箱和手机发送订单信息，请稍后查询。</p>";
//	  WL.Logger.debug("###################################踩踩踩踩踩踩踩踩踩###:"+msg);
	  var msg_one="";
	for(var i=0;i<passenger1.length;i++){
		if(msg_one!=""){
			msg_one += "、"+passenger1[i];
		}else{
			msg_one +=passenger1[i];
		}
	}
	if(msg_one!=""){
		msg_one="<p style='text-indent:2em;'>"+msg_one +"  女士/先生请持购票时所使用的有效身份证件、附有“学生火车票优惠卡”的学生证（均为原件）和订单号码尽快到安装有学生火车票优惠卡识别器的车站售票窗口、铁路客票代售点或具有办理学生票功能的自动售取票机换取纸质车票后乘车，换票前请确保学生优惠卡内的优惠区间与网站填写的信息一致。</p>";
	}
	
	
	var msg_two="";
	for(var i=0;i<passenger2.length;i++){
		if(msg_two!=""){
			msg_two += "、"+passenger2[i];
		}else{
			msg_two +=passenger2[i];
		}
	}
	if(msg_two!=""){
		msg_two="<p style='text-indent:2em;'>"+ msg_two +"  女士/先生请持购票时所使用的有效身份证件、“中华人民共和国残疾军人证”、“中华人民共和国伤残人民警察证”（均为原件）和订单号码尽快到车站售票窗口换取纸质车票。</p>";
	}
	
	var msg_three="";
	for(var i=0;i<passenger3.length;i++){
		if(msg_three!=""){
			msg_three += "、"+passenger3[i];
		}else{
			msg_three +=passenger3[i];
		}
	}
	if(msg_three!=""){
		msg_three = "<p style='text-indent:2em;'>"+msg_three+ "  女士/先生可持购票时所使用的二代居民身份证原件于购票后、列车开车前到车站直接检票乘车。</p>";
	}
	
	var msg_four="";
	for(var i=0;i<passenger4.length;i++){
		if(msg_four!=""){
			msg_four += "、"+passenger4[i];
		}else{
			msg_four +=passenger4[i];
		}
	}
	if(msg_four!=""){
		msg_four = "<p style='text-indent:2em;'>"+msg_four+  "  女士/先生请持二代居民身份证原件和订单号尽快到铁路代售点、车站自动售票机或车站售票窗口换取儿童纸质车票。</p>";
	}
	
	
	var msg_five="";
	for(var i=0;i<passenger5.length;i++){
		if(msg_five!=""){
			msg_five += "、"+passenger5[i];
		}else{
			msg_five +=passenger5[i];
		}
	}
	if(msg_five!=""){
		msg_five = "<p style='text-indent:2em;'>"+msg_five+"  女士/先生请持二代居民身份证原件和订单号尽快到铁路代售点、车站自动售票机或车站售票窗口换取纸质车票。</p>";
	}
	
	var msg_six="";
	for(var i=0;i<passenger6.length;i++){
		if(msg_six!=""){
			msg_six += "、"+passenger6[i];
		}else{
			msg_six +=passenger6[i];
		}
	}
	if(msg_six!=""){
		msg_six ="<p style='text-indent:2em;'>"+msg_six+ "  女士/先生请持购票时所使用的有效身份证件原件和订单号码尽快到车站售票窗口换取纸质车票。</p>";
	}
	msg = msg + msg_one + msg_two + msg_three + msg_four + msg_five + msg_six ;
	return msg;
   }
   function contentIscrollRefresh(){
		if(jq("#payFinishtView .ui-content").attr("data-iscroll")!=undefined){
			jq("#payFinishtView .ui-content").iscrollview("refresh");
		}
	}
   
   var payFinishTicketDetailsGridTemplate =
	   "{{~it :orderDetail:index}}" +
		"<li data-index='{{=index}}'>" +
			"<div class='ui-grid-a' >" +
				"<div class='ui-block-a' style='width:30%'>{{=mor.ticket.util.changeDateType(orderDetail.train_date)}}</div>" +
				"<div class='ui-block-b' style='width:70%'>" +
					"<div class='ui-grid-b'>" +
						"<div class='ui-block-a'>{{=mor.ticket.cache.getStationNameByCode(orderDetail.from_station_telecode)}}" +
								"{{=mor.ticket.util.formateTrainTime(orderDetail.start_time)}}</div>" +
						"<div class='ui-block-b trainDetailArrow'></div>" +
						"<div class='ui-block-c'>{{=mor.ticket.cache.getStationNameByCode(orderDetail.to_station_telecode)}}" +
								"{{=mor.ticket.util.formateTrainTime(orderDetail.arrive_time)}}</div>" +
					"</div>" +
				"</div>" +
			"</div>" +
			"<div class='ui-grid-c'>" +
				"<div class='ui-block-a'>{{=orderDetail.station_train_code}}</div>" +
				"<div class='ui-block-b'>{{=mor.ticket.cache.getSeatTypeByCode(orderDetail.seat_type_code)}}</div>" +
				"<div class='ui-block-c'>{{=orderDetail.coach_name}}车</div>" +
				"<div class='ui-block-d'>{{=orderDetail.seat_name}}</div>" +
				"<div class='ui-block-a'>{{=orderDetail.passenger_name}}</div>" +
				"<div class='ui-block-b'>{{=orderDetail.passenger_id_type_name}}</div>" +
				"<div class='ui-block-c'>{{=mor.ticket.util.getTicketTypeName(orderDetail.ticket_type_code)}}</div>" +
				"<div class='ui-block-d'>{{=orderDetail.ticket_price}}元</div>" +
			"</div>" +
		"</li>" +
		"{{~}}";
	var payFinishgenerateTicketDetailsGrid = doT.template(payFinishTicketDetailsGridTemplate);
})();