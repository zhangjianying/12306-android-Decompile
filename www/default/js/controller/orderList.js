
/* JavaScript content from js/controller/orderList.js in folder common */
(function () {
    jq("#orderListView").live("pagehide", function () {
        var intervalId = mor.ticket.poll.intervalId;
        if (intervalId != "") {
            mor.ticket.poll.timer.stop();
            clearInterval(intervalId);
        }
    });

    jq("#orderListView").live("pageinit", function (e, data) {
        initButtonListener();
    });
    function orderListFn(e, data){
    	if (data.prevPage.attr("id") != "orderListView") {
            initPageContent();
        } else {
            noSeatNoTip();
        }
    }
    jq("#orderListView").live("pagebeforeshow", function (e, data) {
        if (mor.ticket.loginUser.isAuthenticated === "Y") {
        	orderListFn(e, data);
        } else {
        	if (window.ticketStorage.getItem("autologin") != "true") {
        		autologinFailJump();
        		} else {
        		registerAutoLoginHandler(function(){orderListFn(e, data);}, autologinFailJump);
        		}

            //jq.mobile.changePage(vPathCallBack() + "loginTicket.html");
        }
    });

    jq("#orderListView").live("pageshow", function (e, data) {
    	 var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
    	 var sequence_no = unFinishedOrder.myTicketList[0].sequence_no;
    	if(sequence_no && sequence_no.substring(0,1)=="S"){
    		jq("#offLineOderDate").html(mor.ticket.util.getLocalDateString2(unFinishedOrder.order_date));
			jq("#unFinishedOrderDateId").hide();
			jq("#unFinishedOrderNumberId").hide();
			jq("#offLinePaySequenceNoId").show();
			jq("#offLineOderDateId").show();
			jq("#offLineSuccMessageId").show();
			jq("#continuePayId").hide();
			var getYear = unFinishedOrder.myTicketList[0].pay_limit_time.substring(0,4);
			var getMonth = unFinishedOrder.myTicketList[0].pay_limit_time.substring(4,6);
			var getDay = unFinishedOrder.myTicketList[0].pay_limit_time.substring(6,8);
			var getHour = unFinishedOrder.myTicketList[0].pay_limit_time.substring(8,10);
			var getSecond = unFinishedOrder.myTicketList[0].pay_limit_time.substring(10,12);
			var dateStr = getYear+"年"+getMonth+"月"+getDay+"日"+getHour+"时"+getSecond+"分";
			jq("#offLineSuccMessage").html(dateStr);
			jq("#offLinePaySequenceNo").html(unFinishedOrder.myTicketList[0].sequence_no);
		}
        if (data.prevPage.attr("id") === "orderListView") {
            //需要放在这里，在排队刷新完页面之后，按钮才有用。
            initButtonListener();
            initPageContent();
        }
        jq("#orderListView .ui-content").iscrollview("refresh");
    });

    function initPageContent() {
        mor.ticket.viewControl.tab2_cur_page = "orderList.html";
        var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
        jq("#unFinishedOrderDate").html(mor.ticket.util.getLocalDateString2(unFinishedOrder.order_date));
        jq("#unFinishedOrderNumber").html(unFinishedOrder.myTicketList.length);
        if (unFinishedOrder.myTicketList.length == 0) {
            jq("#cancelOrder").parent().hide();
            jq("#continuePay").parent().hide();
            mor.ticket.util.alertMessage("订单错误，请重新查询。");
            mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
            jq.mobile.changePage("queryOrder.html");
        }
        if (unFinishedOrder.order_flag === '1') {
            initNullValue(unFinishedOrder);
            for (var i = 0; i < unFinishedOrder.myTicketList.length; i++) {
                initNullValue(unFinishedOrder.myTicketList[i]);
            }
        } 
        jq("#orderListTrainDetailsGrid").html(generateNoFinishedTicketDetailsGrid(unFinishedOrder.myTicketList)).listview("refresh");
        if (unFinishedOrder.order_flag === '1') {
            if (unFinishedOrder.queue_cancel_flag === "1") {
                mor.ticket.viewControl.unFinishedOrderTourFlag = unFinishedOrder.tourFlag;
                mor.ticket.orderManager.getWaitTimeProcedure();
                jq("#continuePay").parent().hide();
            } else {
                jq("#cancelOrder").parent().hide();
                jq("#continuePay").parent().hide();
            }
        }
        
    }

    function initNullValue(order) {
        for (o in order) {
            if (!order[o]) {
                order[o] = '';
            }
        }
    }
	function initPageContent(){
		mor.ticket.viewControl.tab2_cur_page="orderList.html";
		var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
		jq("#unFinishedOrderDate").html(mor.ticket.util.getLocalDateString2(unFinishedOrder.order_date));
		jq("#unFinishedOrderNumber").html(unFinishedOrder.myTicketList.length);
		if(unFinishedOrder.myTicketList.length==0){
			jq("#cancelOrder").parent().hide();
			jq("#continuePay").parent().hide();
			mor.ticket.util.alertMessage("订单错误，请重新查询。");
			mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
			jq.mobile.changePage("queryOrder.html");
		}
		if(unFinishedOrder.order_flag === '1'){
			initNullValue(unFinishedOrder);
			for(var i=0; i<unFinishedOrder.myTicketList.length; i++){
				initNullValue(unFinishedOrder.myTicketList[i]);
			}
		}
		jq("#orderListTrainDetailsGrid").html(generateNoFinishedTicketDetailsGrid(unFinishedOrder.myTicketList)).listview("refresh");
		if(unFinishedOrder.order_flag === '1'){
			if(unFinishedOrder.queue_cancel_flag ==="1"){
				mor.ticket.viewControl.unFinishedOrderTourFlag = unFinishedOrder.tourFlag;
				mor.ticket.orderManager.getWaitTimeProcedure();
				jq("#continuePay").parent().hide();
				//add by yiguo 
				jq("#cancelOrder>.ui-btn-inner>.ui-btn-text").html("取消排队");
				return;

			}else{
				jq("#cancelOrder").parent().hide();
				jq("#continuePay").parent().hide();
			}
		}
		//add by yiguo 
		jq("#cancelOrder>.ui-btn-inner>.ui-btn-text").html("取消订单");
		//提示分配的是硬代座
		for(var i=0; i<unFinishedOrder.myTicketList.length;i++){
			var seatNo = unFinishedOrder.myTicketList[i].seat_no;
			var seatType = unFinishedOrder.myTicketList[i].seat_type_code;
			var lastnum = unFinishedOrder.myTicketList[i].seat_no.substring(3, 4);
			if(lastnum >= 'a' && lastnum <= 'z'){
				   WL.SimpleDialog.show(
							"温馨提示", 
							"本次申请席位的结果为"+mor.ticket.util.getSeatTypeName(seatType,seatNo)+"，请确认。", 
							[ {text : '确定', handler: function() {}}]
						);
				   break;
			}
		}

	}

    function initButtonListener() {
        jq("#orderListViewBackBtn").off().on("tap", function (e) {
            jq.mobile.changePage("queryOrder.html");
            return false;
        });
        jq("#cancelOrder").off().bind("tap", function(){
        		if(jq(this).find(".ui-btn-text").html() == "取消排队"){
        			 WL.SimpleDialog.show(
        		                "温馨提示",
        		                "确定取消排队？",
        		                [
        		                    {text: '取消', handler: function () {
        		                    }},
        		                    {text: '确定', handler: function () {
        		                		sendCancelOrderRequest();
        		                    }}
        		                ]
        		            );
        		}else{
            		sendCancelOrderRequest();
        		}
        	});
        jq("#continuePay").off().bind("tap", function (e) {
            var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
            var ticketList = unFinishedOrder.myTicketList;
        	var ticket = ticketList[0];
		    var pay_limit_time = ticket.pay_limit_time;
		    mor.ticket.paytimer.endTime = mor.ticket.util.setMyDate3(pay_limit_time);
	        jq.mobile.changePage("continuePayment.html");
            return false;
        });
    }


    function sendCancelOrderRequest(e) {
        var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
        if (unFinishedOrder.order_flag === '1') {
            if (unFinishedOrder.queue_cancel_flag === "1") {//排队中订单取消
                queueOrderCancel();
            }
        } else {//未排队中订单取消
            WL.SimpleDialog.show(
                "温馨提示",
                "一天内3次申请车票成功后取消订单，当日将不能在12306继续购票！",
                [
                    {text: '取消', handler: function () {
                    }},
                    {text: '确定', handler: function () {
                        unfinishedOrderCancel();
                    }}
                ]
            );
        }
        return false;
    }

    function queueOrderCancel() {//排队中订单取消
        var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
        var util = mor.ticket.util;

        var commonParameters = {
            'tour_flag': unFinishedOrder.tourFlag
        };

        var invocationData = {
            adapter: "CARSMobileServiceAdapter",
            procedure: "cancelInQueueOrder"
        };

        var options = {
            onSuccess: requestSucceeded,
            onFailure: util.creatCommonRequestFailureHandler()
        };
        mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
    }

    function unfinishedOrderCancel() {//排队后，未完成订单取消
        var unFinishedOrder = mor.ticket.queryOrder.getCurrentUnfinishedOrder();
        var util = mor.ticket.util;
        var cancelFlag = '0';// 0:取消普通待支付订单;1取消改签待支付订单
        for (var i = 0; i < unFinishedOrder.myTicketList.length; i++) {
            if (unFinishedOrder.myTicketList[i].ticket_status_code == 'j') {
                cancelFlag = '1';
                break;
            }
        }
        var commonParameters = {
            'sequence_no': unFinishedOrder.myTicketList[0].sequence_no,
            'coach_no': '00',
            'seat_no': '0000',
            'batch_no': '0',
            'cancel_flag': cancelFlag
        };

        var invocationData = {
            adapter: "CARSMobileServiceAdapter",
            procedure: "cancelOrder"
        };

        var options = {
            onSuccess: requestSucceeded,
            onFailure: util.creatCommonRequestFailureHandler()
        };

        mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
    }

    function requestSucceeded(result) {
        if (busy.isVisible()) {
            busy.hide();
        }
        var intervalId = mor.ticket.poll.intervalId;
        if (intervalId != "") {
            mor.ticket.poll.timer.stop();
            clearInterval(mor.ticket.poll.intervalId);
        }
        var invocationResult = result.invocationResult;
        if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
            mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;


            //不更改mode,沿用mor.ticket.viewControl.bookMode的值，也就是保持当前的订票模式
            //mor.ticket.viewControl.bookMode = "dc";
            
            //add by yiguo
            mor.ticket.queryOrder.currentQueryOrder = null;
            mor.ticket.queryOrder.queryOrderList = null;
            jq.mobile.changePage("../MobileTicket.html");
            //jq.mobile.changePage("queryOrder.html");
            //mor.ticket.util.alertMessage("订单取消成功");
        } else {
            mor.ticket.util.alertMessage(invocationResult.error_msg);
            jq.mobile.changePage("queryOrder.html");
			setTimeout(function(){
				jq("#refreshUnfinishedorder").trigger("tap");
			},1000);
        }
    }

    function noSeatNoTip() {
        var util = mor.ticket.util;
        var queryOrder = mor.ticket.queryOrder;
        var unFinishedOrder = queryOrder.getCurrentUnfinishedOrder();
        var ticketList = unFinishedOrder.myTicketList;

        if (isRoundTripOrder(ticketList)) {
            var RoundTripOrder = getRoundTripTicketList(ticketList);
            ticketList = RoundTripOrder.backList;
        }

        for (var i = 0; i < ticketList.length; i++) {
            var ticket = ticketList[i];
            if (ticket.seat_name && ticket.seat_name.indexOf("无座") >= 0) {
                util.alertMessage("您所购车次的" + util.getSeatTypeName(ticket.seat_type_code) + "已售完，系统自动为您分配了无座票");
                break;
            }
        }

    }

    function isRoundTripOrder(order) {
        if (order.length > 1) {
            var ticket = order[0];
            for (var i = 1; i < order.length; i++) {
                if (ticket.from_station_telecode != order[i].from_station_telecode) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }

    }

    function getRoundTripTicketList(order) {
        var list1 = [];
        var list2 = [];
        var ticket = order[0];
        list1.push(ticket);
        for (var i = 1; i < order.length; i++) {
            if (ticket.from_station_telecode == order[i].from_station_telecode) {
                list1.push(order[i]);
            } else {
                list2.push(order[i]);
            }
        }
        var util = mor.ticket.util;
        var list1Date = util.setMyDate2(list1[0].train_date);
        var list2Date = util.setMyDate2(list2[0].train_date);
        if (list1Date < list2Date) {
            return {
                goList: list1,
                backList: list2
            };
        } else {
            return {
                goList: list2,
                backList: list1
            };
        }
    }

    var noFinishedticketDetailsGridTemplate =
        "{{~it :orderDetail:index}}" +
            "{{if(orderDetail.ticket_status_code != 'e') { }}" +

            "{{if((mor.ticket.queryOrder.currentQueryOrder.order_flag == '1')" +
            "&&(mor.ticket.queryOrder.currentQueryOrder.queue_cancel_flag != '1') ){ }}" +
            "<li style='padding: .7em 15px;display: {{ if (index==0){ }} block{{ }else{ }} none {{ } }};'>" +
            "<div class='ticketStatuse'>车票状态 ：" +
            "{{=mor.ticket.queryOrder.currentQueryOrder.queue_message}}" +
            "<p style='color:red;font-size:14px; white-space:normal; padding-top:8px;'>订单未生成。以下内容为您上次的订票需求，再次订票前无须处理。<p/></div>" +
            "{{ } }}" +

            "{{if((mor.ticket.queryOrder.currentQueryOrder.order_flag == '1')" +
            "&&(mor.ticket.queryOrder.currentQueryOrder.queue_cancel_flag == '1' && index==0) ){ }}" +
            "<li style='padding: .7em 15px;display: {{ if (index==0){ }} block{{ }else{ }} none {{ } }};'>" +
            "{{if (index==0){ }}" +
            "<div class='ticketStatuse'>车票状态 ：" +
            "{{if(mor.ticket.queryOrder.currentQueryOrder.order_flag != '1'){ }}" +
            "{{=orderDetail.ticket_status_name}}" +
            "{{ } else { }}" +
            "{{=mor.ticket.queryOrder.currentQueryOrder.queue_message}}" +
            "{{ } }}" +
            "</div>" +
            "{{ } }}" +

            "<div class='unfinishedOrderWaitTimeContain'>最新预估排队等待时间 ：<span class=unfinishedOrderWaitTime></span></div>" +

            "</li>" +
            "{{ } }}" +
            "<li data-index='{{=index}}'>" +
            "<div class='ui-grid-a' >" +
            "<div class='ui-block-a'>{{=mor.ticket.util.changeDateType(orderDetail.train_date)}}</div>" +
            "<div class='ui-block-b'>" +
            "<div class='ui-grid-b'>" +
            "{{=(mor.ticket.cache.getStationNameByCode(orderDetail.from_station_telecode) || '    ')}}" +
            "{{ if(mor.ticket.queryOrder.currentQueryOrder.order_flag == '2'){ }}" +
            "<span>{{=(mor.ticket.util.formateTrainTime(orderDetail.start_time)|| '    ')}}</span>" +
            "{{ } }}" +
            "<span class='trainDetailArrow'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>" +
            "{{=(mor.ticket.cache.getStationNameByCode(orderDetail.to_station_telecode) || '    ')}}" +
            "{{ if(mor.ticket.queryOrder.currentQueryOrder.order_flag == '2'){ }}" +
            "<span>{{=(mor.ticket.util.formateTrainTime(orderDetail.arrive_time) || '    ')}}</span>" +
            "{{ }else{ }}" +
            "<span>&nbsp;&nbsp;&nbsp;{{=(mor.ticket.util.formateTrainTime(orderDetail.start_time) || '    ')}} 开</span>" +
            "{{ } }}" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<div class='ui-grid-c'>" +
            "<div class='ui-block-a'>{{=orderDetail.station_train_code}}</div>" +
            "<div class='ui-block-b'>{{=mor.ticket.util.getSeatTypeName(orderDetail.seat_type_code,orderDetail.seat_no)}}</div>" +
            "<div class='ui-block-c'>" +
            "{{if(orderDetail.coach_name) { }}" +
            "{{=orderDetail.coach_name}}车" +
            "{{ } }}" +
            "</div>" +
            "<div class='ui-block-d'>{{=orderDetail.seat_name}}</div>" +
            "<div class='ui-block-a'>{{=orderDetail.passenger_name}}</div>" +
            "<div class='ui-block-b'>{{=orderDetail.passenger_id_type_name}}</div>" +
            "<div class='ui-block-c'>{{=mor.ticket.util.getTicketTypeName(orderDetail.ticket_type_code)}}</div>" +
            "<div class='ui-block-d'>" +
            "{{ if (orderDetail.ticket_price) { }}" +
            "{{=orderDetail.ticket_price}}元" +
            "{{ } }}" +
            "</div>" +
            "</div>" +
            "{{if((mor.ticket.queryOrder.currentQueryOrder.order_flag != '1')" +
            "&&(mor.ticket.queryOrder.currentQueryOrder.queue_cancel_flag != '1') ){ }}" +
            "<div class='ticketStatuse'>车票状态 ：" +
            "{{if(mor.ticket.queryOrder.currentQueryOrder.order_flag != '1'){ }}" +
            "{{=orderDetail.ticket_status_name}}" +
            "{{ } else { }}" +
            "{{=mor.ticket.queryOrder.currentQueryOrder.queue_message}}" +
            "{{ } }}" +
            "</div>" +
            "{{ } }}" +

            "</li>" +
            "{{ } }}" +
            "{{~}}";
    var generateNoFinishedTicketDetailsGrid = doT.template(noFinishedticketDetailsGridTemplate);
})();