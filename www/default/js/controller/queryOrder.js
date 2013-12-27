
/* JavaScript content from js/controller/queryOrder.js in folder common */
(function () {
    jq("#queryOrderView").live("pageinit", function (e, data) {
        registerTodayOrderBtnListener();
        registerSevenDaysOrderBtnListener();
        registerAdvanceQueryOrderBtnListener();
        registerNoFinishedOrdersItemListener();
    	registerUnfinishedOrderRefreshButtonListener();
    });
    jq("#queryOrderView").live("pagebeforeshow", function (e, data) {
        mor.ticket.viewControl.current_tab = "queryOrderTab";
        mor.ticket.viewControl.tab2_cur_page = "queryOrder.html";
        var user = mor.ticket.loginUser; 
        if (user.isAuthenticated === "Y") {
//			WL.Logger.debug("queryOrderView show");		
        } else {
        	// add by yiguo
			var latestTime = window.ticketStorage.getItem("pwdTime");
			// mod by yiguo
			if (window.ticketStorage
					.getItem("autologin") == "true" && (!latestTime || (Date.now() - latestTime   < 7*24*3600*1000))) {

				registerAutoLoginHandler(function(){
     				//jq("#refreshUnfinishedorder").trigger("tap");
        		},function(){
            		jq.mobile.changePage(vPathCallBack() + "loginTicket.html");
        		});
			}else{
        		jq.mobile.changePage(vPathCallBack() + "loginTicket.html");
			}
				
//        	if(window.ticketStorage.getItem("autologin") != "true"){
//        		jq.mobile.changePage(vPathCallBack() + "loginTicket.html");
//        	}else{
//        		registerAutoLoginHandler(function(){
//     				//jq("#refreshUnfinishedorder").trigger("tap");
//        		},function(){
//            		jq.mobile.changePage(vPathCallBack() + "loginTicket.html");
//        		});
//        	}
        }

    });

    jq("#queryOrderView").live("pageshow", function (e, data) {
        var prevPageId = data.prevPage.attr("id");
        if (mor.ticket.viewControl.isNeedRefreshUnfinishedOrder ||
            prevPageId === "passengersView" ||
            prevPageId === "orderDetailsView") {
            if (mor.ticket.viewControl.isNeedRefreshUnfinishedOrder) {
                jq('#noFinishedOrdersDesc').text('未完成订单(0)');
                jq('#noFinishedOrdersQuery').empty();
                mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = false;
            }
            else if (prevPageId != "bookTicketView") {
                initQueryOrder();
            }
        } else {
            displayNoFinishedOrders();
        }
    });

    function registerNoFinishedOrdersItemListener() {
        jq("#noFinishedOrdersQuery").off().on("tap", "li", function (e) {
            e.stopImmediatePropagation();
            var queryOrder = mor.ticket.queryOrder;
            //init queryOrder content
            queryOrder.currentQueryOrder = {};
            queryOrder.currentUnfinishOrderIndex = 0;
            queryOrder.currentUnfinishOrderIndex = jq(this).index();
            mor.ticket.queryOrder.setCurrentUnfinishedOrders(jq(this).index());
            jq.mobile.changePage(vPathCallBack() + "orderList.html");
            return false;
        });
    }
    // mod by yiguo
    window.pullOrderData =  initQueryOrder;
    function initQueryOrder(fn, err) {
        var util = mor.ticket.util;
        var commonParameters = {
            'query_class': '1'
        };

        var invocationData = {
            adapter: "CARSMobileServiceAdapter",
            procedure: "queryOrder"
        };
        // add by yiguo
        var _rqtSuc,_rqtErr;
        if(!fn){
        	_rqtSuc = requestSucceeded;
        	_rqtErr = util.creatCommonRequestFailureHandler();
        }else{
        	_rqtSuc = function(result){
        		//requestSucceeded(result);
        		fn(result);
        	};
        	
        	var _failFn = util.creatCommonRequestFailureHandler();
        	_rqtErr = function(){
        		_failFn(ret);
        		err();
        		
        	};
        }
        var options = {
            onSuccess: _rqtSuc,
            onFailure: _rqtErr
        };
        mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
    }

    function requestSucceeded(result) {
        if (busy.isVisible()) {
            busy.hide();
        }
        var invocationResult = result.invocationResult;
        if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
            var queryOrder = mor.ticket.queryOrder;
            //init queryOrder queryOrderList
            queryOrder.queryOrderList = [];
            queryOrder.originPaidQrderList = [];
            queryOrder.setUnfinishedOrderList(invocationResult.orderList);
            displayNoFinishedOrders();
        } else {
            mor.ticket.util.alertMessage(invocationResult.error_msg);
        }
    }

    function displayNoFinishedOrders() {
        var queryOrder = mor.ticket.queryOrder;
        var noFinishedOrderList = queryOrder.queryOrderList;

        jq("#noFinishedOrdersDesc").html("未完成订单(" + (noFinishedOrderList != null ? noFinishedOrderList.length : "0") + ")");

        if (noFinishedOrderList && noFinishedOrderList[0]) {
            queryOrder.hasChangeTicket();
            jq("#noFinishedOrdersQuery").html(generateUnfinishedOrdersDetailList(noFinishedOrderList)).show().listview("refresh");
            contentIscrollRefresh();
        } else {
            jq("#noFinishedOrdersQuery").hide();
            jq("#noFinishedOrdersQuery").html("").listview("refresh");
        }
    }

    function contentIscrollRefresh() {
        if (jq("#queryOrderView .ui-content").attr("data-iscroll") != undefined) {
            jq("#queryOrderView .ui-content").iscrollview("refresh");
        }
    }

    function registerTodayOrderBtnListener() {
        jq("#todayOrder").bind("tap", function () {
        	if (!busy.isVisible()) {
				busy.show();
			}
            mor.ticket.viewControl.queryFinishedOrderType = '0';// query today finished order
            jq.mobile.changePage(vPathCallBack() + "finishedOrderList.html");
            return false;
        });
    }

    function registerSevenDaysOrderBtnListener() {
        jq("#sevenDaysOrder").bind("tap", function () {
        	if (!busy.isVisible()) {
				busy.show();
			}
            mor.ticket.viewControl.queryFinishedOrderType = '1';// query seven days finished order
            jq.mobile.changePage(vPathCallBack() + "finishedOrderList.html");
            return false;
        });
    }


    function registerAdvanceQueryOrderBtnListener() {
        jq("#advanceQuery").bind("tap", function () {
        	if (!busy.isVisible()) {
				busy.show();
			}
            mor.ticket.viewControl.queryFinishedOrderType = '2';//advance query finished order
            jq.mobile.changePage(vPathCallBack() + "advanceQueryOrder.html");
            return false;
        });
    }

    function registerUnfinishedOrderRefreshButtonListener() {
        jq("#refreshUnfinishedorder").bind("tap", function () {
            initQueryOrder();
            return false;
        });
    }

    var unfinishedOrdersDetailListTemplate =
        "{{~it :order:index}}" +
            "<li data-index='{{=index}}'><a>" +
            "<span>订单时间：{{=mor.ticket.util.getLocalDateString3(order.order_date)}}</span>" +
            "总张数：" +
            "<em>{{=order.myTicketList.length}}</em>" +
            "</a></li>" +
            "{{~}}";
    var generateUnfinishedOrdersDetailList = doT.template(unfinishedOrdersDetailListTemplate);
})();