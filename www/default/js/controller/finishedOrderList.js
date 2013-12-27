
/* JavaScript content from js/controller/finishedOrderList.js in folder common */
(function() {		
	var page_no = 0; 
	var showMore =false;
	jq("#FinishedOrderListView").live("pageinit", function() {
		registerFinishedOrderListItemClickHandler();
		jq("#orderList_content", this).bind( { 
	        "iscroll_onpulldown" : onPullDown,    
	        "iscroll_onpullup"   : onPullUp});
		jq("#finishedOrderListViewBackBtn").bind("tap",function(){
			if(mor.ticket.viewControl.queryFinishedOrderType == '2'){
				jq.mobile.changePage("advanceQueryOrder.html");
			} else {
				jq.mobile.changePage("queryOrder.html");
			}
			return false;
		});			
	});
	

	//往下拉是重新刷新
	function onPullDown(event, data){
		setTimeout(function fakeRetrieveDataTimeout() { 
		      gotPullDownData(event, data); }, 
		      1500); 
	};
	
	function  gotPullDownData(event, data){
		page_no = 0;
		showMore =false;
		if(mor.ticket.viewControl.queryFinishedOrderType == '0'){
			//今日已完成订单
			initQueryTodayOrder();
		} else {
			initQueryOrder();
		}	
	  data.iscrollview.refresh();
	}
	//往上拉是加载更多
	function onPullUp(){
		++page_no;
		showMore =true;
		if(mor.ticket.viewControl.queryFinishedOrderType == '0'){
			//今日已完成订单
			initQueryTodayOrder();
		} else {
			initQueryOrder();
		}		
	};
	
	function finishedOrderListFn(e, data){
		var user = mor.ticket.loginUser;
		page_no = 0;
		showMore = false;
		var prePage;
		if(mor.ticket.viewControl.queryFinishedOrderType == '0'){//今日订单查询
			//如果从finishedOrderDetails页面返回，那么不重新查询订单
			prePage = data.prevPage.attr("id");
			if(prePage !="FinishedOrderDetailView"){
				initQueryTodayOrder();
			}else{
				var orders = mor.ticket.queryOrder.finishedOrderList;	
				if(orders){//查询订单成功
					if(orders.length){			
						//判断是否有改签的车票，若有，则删除
						mor.ticket.queryOrder.spliceChangingTicketList();
						//对orders数组按照时间进行排序
						var sortedOrders = sortOrdersByDate(orders);
						jq("#orderLength").html(sortedOrders.length);
						jq("#orderPrompt").show();
						jq("#finishedOrderList").html(generateFinishedOrdersList(sortedOrders)).listview("refresh");
						jq("#orderList-pullup").show();
						jq("#iscroll-pulldown").show();
					}else {//订单为空
						mor.ticket.util.alertMessage("没有已完成订单。");
						jq("#orderList-pullup").hide();
					}			
				} else {//查询订单失败
					mor.ticket.util.alertMessage(invocationResult.error_msg);
				}	
			}
			jq("#finishedOrderListHeader").html("今日已完成订单");
			jq("#finishedOrderListViewBackBtn .ui-btn-text").html("订单查询");
		} else {
			
			//如果从finishedOrderDetails页面返回，那么不重新查询订单
			prePage = data.prevPage.attr("id");
			if(prePage !="FinishedOrderDetailView"){
				initQueryOrder();
			}else{
				var orders = mor.ticket.queryOrder.finishedOrderList;	
				if(orders){//查询订单成功
					if(orders.length){			
						//判断是否有改签的车票，若有，则删除
						mor.ticket.queryOrder.spliceChangingTicketList();
						//对orders数组按照时间进行排序
						var sortedOrders = sortOrdersByDate(orders);
						jq("#orderLength").html(sortedOrders.length);
						jq("#orderPrompt").show();
						jq("#finishedOrderList").html(generateFinishedOrdersList(sortedOrders)).listview("refresh");
						jq("#orderList-pullup").show();
						jq("#iscroll-pulldown").show();
					}else {//订单为空
						mor.ticket.util.alertMessage("没有已完成订单。");
						jq("#orderList-pullup").hide();
					}			
				} else {//查询订单失败
					mor.ticket.util.alertMessage(invocationResult.error_msg);
				}	
			}
			if(mor.ticket.viewControl.queryFinishedOrderType == '1') {//7日内订单查询
				jq("#finishedOrderListHeader").html("7日内已完成订单");
				jq("#finishedOrderListViewBackBtn .ui-btn-text").html("订单查询");
			} else {//高级查询
				jq("#finishedOrderListHeader").html("已完成订单");
				jq("#finishedOrderListViewBackBtn .ui-btn-text").html("高级查询");
			}
		}			

	}
	jq("#FinishedOrderListView").live("pageshow", function(e, data) {
		//jq("FinishedOrderListView .iscroll-pulldown").hide();
		
		var user = mor.ticket.loginUser;
		
		

		if(user.isAuthenticated === "Y"){
			finishedOrderListFn(e, data);
		}else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump();
				} else {
				registerAutoLoginHandler(function(){finishedOrderListFn(e, data)}, autologinFailJump);
				}

			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	function initQueryTodayOrder(){//今天已完成订单
		var util = mor.ticket.util;
		var today = mor.ticket.util.getNewDate();
		var todayStr = today.format("yyyyMMdd");
		var commonParameters = {
			'from_reserve_date':todayStr,
			'to_reserve_date':todayStr,
			'page_no':page_no.toString(),
			'rows_number':'8',
			'query_class': '2'
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "queryOrder"
		};
		
		var options =  {
				onSuccess: showMore?requestAddMoreSucceeded:requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	}
	
	function prepareQueryOrderPrompt(){
		var jsonPrompt;
		var util = mor.ticket.util;
		if(mor.ticket.viewControl.queryFinishedOrderType == '1'){
			//七日内订单
			var today = mor.ticket.util.getNewDate();
			var todayStr = today.format("yyyyMMdd");
			var sevenDayStr = new Date(today.getTime() - 6*24*60*60*1000).format("yyyyMMdd");
			jsonPrompt = {
				'from_reserve_date':sevenDayStr,
				'to_reserve_date':todayStr,
				'page_no':page_no.toString(),
				'rows_number':'8',
				'query_class': '2' //全部已完成订单
			};
		} else {	
			//高级查询    车次日期查询
			if(mor.queryOrder.views.advanceQuery.isSelectTrainDate){
				jsonPrompt = {
						'from_train_date':util.processDateCode(mor.queryOrder.views.advanceQuery.fromDate),
						'to_train_date':util.processDateCode(mor.queryOrder.views.advanceQuery.toDate),
						'train_code':mor.queryOrder.views.advanceQuery.trainCode,
						'passenger_name':mor.queryOrder.views.advanceQuery.passengerName,
						'sequence_no':mor.queryOrder.views.advanceQuery.sequenceNo,
						'page_no':page_no.toString(),
						'rows_number':'8',
						'query_class': '2'//全部已完成订单
				};
			} else {// 高级查询    订单日期查询				
				jsonPrompt = {
						'from_reserve_date':util.processDateCode(mor.queryOrder.views.advanceQuery.fromDate),
						'to_reserve_date':util.processDateCode(mor.queryOrder.views.advanceQuery.toDate),
						'train_code':mor.queryOrder.views.advanceQuery.trainCode,
						'passenger_name':mor.queryOrder.views.advanceQuery.passengerName,
						'sequence_no':mor.queryOrder.views.advanceQuery.sequenceNo,
						'page_no':page_no.toString(),
						'rows_number':'8',
						'query_class': '2'//全部已完成订单
				};
			}
		}
		return jsonPrompt;
	}
	
	function initQueryOrder(){
		var util = mor.ticket.util;
		var commonParameters = prepareQueryOrderPrompt();
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "queryOrderTwo"
		};
		
		var options =  {
				onSuccess: showMore?requestAddMoreSucceeded:requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	}
	
	function requestSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)){
			var orders = mor.ticket.queryOrder.setFinishedOrderList(invocationResult.orderList);	
			if(orders){//查询订单成功
				if(orders.length){			
					//判断是否有改签的车票，若有，则删除
					mor.ticket.queryOrder.spliceChangingTicketList();
					//对orders数组按照时间进行排序
					var sortedOrders = sortOrdersByDate(orders);
					jq("#orderLength").html(sortedOrders.length);
					jq("#orderPrompt").show();
					jq("#finishedOrderList").html(generateFinishedOrdersList(sortedOrders)).listview("refresh");
					jq("#orderList-pullup").show();
					jq("#iscroll-pulldown").show();
				}else {//订单为空
					mor.ticket.util.alertMessage("没有已完成订单。");
					jq("#orderList-pullup").hide();
				}			
			} else {//查询订单失败
				mor.ticket.util.alertMessage(invocationResult.error_msg);
			}	
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	//对订单按照时间进行排序
	function sortOrdersByDate(orders){
		var sortedOrders = orders;
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
					if (typeof a === typeof b) { return a < b ? 1 : -1;}
					return typeof a < typeof b ? 1 : -1;
				}
				else {throw ("error"); }
			};
		};

		orders.sort(by("order_date"));
		return orders;
	}
	
	function requestAddMoreSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)){
			var orders = invocationResult.orderList;
			if(orders){//查询订单成功
				if(orders.length){			
					//判断是否有改签的车票，若有，则删除
					orders = mor.ticket.queryOrder.pushFinishedOrderList(invocationResult.orderList);
					mor.ticket.queryOrder.spliceChangingTicketList();
					//对orders数组按照时间进行排序
					var sortedOrders = sortOrdersByDate(orders);
					jq("#orderLength").html(sortedOrders.length);
					jq("#orderPrompt").show();
					jq("#finishedOrderList").html(generateFinishedOrdersList(sortedOrders));
					//jq("#orderList-pullup").show();
				}else {//订单为空
					mor.ticket.util.alertMessage("已经是最后一页了。");
					jq("#orderList-pullup").hide();
				}
				jq("#FinishedOrderListView .ui-content").iscrollview("refresh");
			} else {//查询订单失败
				mor.ticket.util.alertMessage(invocationResult.error_msg);
			}	
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	var finishedOrdersListTemplate =
		"{{~it :order:index}}" +
		"<li data-index='{{=index}}'><a>" +
			"<div class='ui-grid-a'>" +
				"<div class='ui-block-a'>订单号：<span>{{=order.sequence_no}}</span></div>" +
				"<div class='ui-block-a' style='width: 73%;'>订单时间：{{=mor.ticket.util.getLocalDateString3(order.myTicketList[0].reserve_time)}}</div>" +
				"<div class='ui-block-b' style='width: 27%;'>总张数：<span>{{=order.myTicketList.length}}</span></div>" +
			"</div></a>" +
		"</li>" +
		"{{~}}";
	var generateFinishedOrdersList = doT.template(finishedOrdersListTemplate);
	
	function registerFinishedOrderListItemClickHandler(){
		jq("#finishedOrderList").off().on("tap", "li", function(e){
			e.stopImmediatePropagation();
			mor.ticket.queryOrder.setCurrentFinishedOrders(jq(this).index());
			jq.mobile.changePage("finishedOrderDetail.html");			
			return false;
		});
	}
})();