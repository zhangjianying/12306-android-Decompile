
/* JavaScript content from js/controller/advanceQueryOrder.js in folder common */
(function() {	
	/*jq("#AdvanceQueryOrderView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#AdvanceQueryOrderView");
	});*/
	var focusArray=[];
	jq("#AdvanceQueryOrderView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#trainCode',focusArray);
		util.enableAutoScroll('#passengerName',focusArray);
		util.enableAutoScroll('#sequenceNo',focusArray);
	}
	jq("#AdvanceQueryOrderView").live("pageinit", function() {
		registerAutoScroll();
		initAdvanceQuerySelectScroller();
		registerFromDateInputChangeListener();
		registerToDateInputChangeListener();
		registerAdvancedQueryOrderBtnListener();
		registerBtnGroupListener();
		jq("#advanceQueryOrderViewBackBtn").bind("tap",function(){
			jq.mobile.changePage("queryOrder.html");
		});
		checkFormAQ();
	});
	
	function advanceQueryOrderFn(){
		var advanceQuery = mor.queryOrder.views.advanceQuery;
		if(advanceQuery.fromDate == "" || advanceQuery.toDate == ""){
			var date= mor.ticket.util.getNewDate();
			var dateStr = date.format("yyyy-MM-dd");
			jq("#fromDateInputShow").val(dateStr);
			advanceQuery.fromDate = dateStr;
			jq("#toDateInputShow").val(dateStr);
			advanceQuery.toDate = dateStr;
			advanceQuery.isSelectTrainDate = false;
		}else {
			jq("#fromDateInputShow").val(advanceQuery.fromDate);
			jq("#toDateInputShow").val(advanceQuery.toDate);
			if(!advanceQuery.isSelectTrainDate){
				jq("#searchBookDate").addClass("ui-btn-active ui-state-persist");
				jq("#searchTrainDate").removeClass("ui-btn-active ui-state-persist");
			}else{
				jq("#searchTrainDate").addClass("ui-btn-active ui-state-persist");
				jq("#searchBookDate").removeClass("ui-btn-active ui-state-persist");
			}
		}
		jq('#trainCode').val(advanceQuery.trainCode);
		jq('#passengerName').val(advanceQuery.passengerName);
		jq('#sequenceNo').val(advanceQuery.sequenceNo);	
	}
	jq("#AdvanceQueryOrderView").live("pagebeforeshow", function() {
		if(mor.ticket.loginUser.isAuthenticated !== "Y"){
			if (window.ticketStorage.getItem("autologin") != "true") {
					autologinFailJump()
				} else {
					registerAutoLoginHandler(advanceQueryOrderFn, autologinFailJump);
				}
		}else{
			advanceQueryOrderFn();
		}			
	});
	
	jq("#AdvanceQueryOrderView").live("pageshow", function() {
		if(busy.isVisible()){
			busy.hide();
		}
	});
	function checkFormAQ(){
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		jq("#trainCode").off('blur').on('blur',function () { 
			if(jq("#trainCode").val()!==''&&!checkFormUtil.checkTrainCode(jq("#trainCode").val())){
				util.alertMessage("车次首字符为英文字母或者数字，后面最多4位数字!");
				return;
			}
		});
		jq("#passengerName").off('blur').on('blur',function () { 
			if(jq("#passengerName").val()!==''&&!checkFormUtil.checkChar(jq("#passengerName").val())){
				util.alertMessage("真实姓名只能填写英文字母或者中文!");
				return;
			}
		});
		jq("#sequenceNo").off('blur').on('blur',function () { 
			if(jq("#sequenceNo").val()!==''&&!checkFormUtil.checkSequenceNo(jq("#sequenceNo").val())){
				util.alertMessage("订单号首字符为英文字母，后面9位数字!");
				return;
			}
		});
	}
	function initAdvanceQuerySelectScroller(){		
		jq('#fromDateInput,#toDateInput').scroller({
	        preset: 'date',
	        theme: 'ios',
	        yearText:'年',
	        monthText:'月',
	        dayText:'日',
	        setText:'确定',
	        cancelText:'取消',
	        display: 'modal',
	        mode: 'scroller',
	        dateOrder: 'yy mm dd',
	        dateFormat: 'yy-mm-dd',
	        height:40,
	        showLabel:true
		});
	}
	
	function registerBtnGroupListener(){
		jq("#searchBookDate").bind("tap",function(){
			jq(this).addClass("ui-btn-active ui-state-persist")
				.siblings().removeClass("ui-btn-active ui-state-persist");
			mor.queryOrder.views.advanceQuery.isSelectTrainDate = false;
			var date= mor.ticket.util.getNewDate();
			var dateStr = date.format("yyyy-MM-dd");
			jq("#fromDateInputShow").val(dateStr);
			jq("#toDateInputShow").val(dateStr);
			mor.queryOrder.views.advanceQuery.fromDate = dateStr;
			mor.queryOrder.views.advanceQuery.toDate = dateStr;
			return false;
		});
		
		jq("#searchTrainDate").bind("tap",function(){
			jq(this).addClass("ui-btn-active ui-state-persist")
				.siblings().removeClass("ui-btn-active ui-state-persist");
			mor.queryOrder.views.advanceQuery.isSelectTrainDate = true;
			var date= mor.ticket.util.getNewDate();
			var dateStr = date.format("yyyy-MM-dd");
			jq("#fromDateInputShow").val(dateStr);
			//modified by yiguo 2013-11-6
			var reservePeriod = parseInt(window.ticketStorage.getItem("reservePeriod"),10);
			var date1 = new Date(date.setDate(date.getDate()+reservePeriod-1));
			var date1Str = date1.format("yyyy-MM-dd");
			jq("#toDateInputShow").val(date1Str);
			mor.queryOrder.views.advanceQuery.fromDate = dateStr;
			mor.queryOrder.views.advanceQuery.toDate = date1Str;
			return false;
		});
	}
	
	function registerFromDateInputChangeListener(){
		jq("#fromDateInputShow").bind("tap",function(){
			jq('#fromDateInput').scroller('show');
			return false;
		});
		jq('#fromDateInput').bind("change",function(){
			var date = jq(this).val();
			jq("#fromDateInputShow").val(date);
			mor.queryOrder.views.advanceQuery.fromDate = jq(this).val();
		});
	}
	
	function registerToDateInputChangeListener(){
		jq("#toDateInputShow").bind("tap",function(){
			jq('#toDateInput').scroller('show');
			return false;
		});
		jq('#toDateInput').bind("change",function(){
			var date = jq(this).val();
			jq("#toDateInputShow").val(date);
			mor.queryOrder.views.advanceQuery.toDate = jq(this).val();
		});
	}
	function registerAdvancedQueryOrderBtnListener(){
		jq('#advancedQueryOrderBtn').off('tap').on("tap",function(){
			var advanceQuery = mor.queryOrder.views.advanceQuery;
			var util = mor.ticket.util;
			var checkFormUtil = mor.ticket.checkForm;
			if(jq("#trainCode").val()!==''&&!checkFormUtil.checkTrainCode(jq("#trainCode").val())){
				util.alertMessage("车次首字符为英文字母或者数字，后面最多4位数字!");
				return false;
			}
			if(jq("#passengerName").val()!==''&&!checkFormUtil.checkChar(jq("#passengerName").val())){
				util.alertMessage("真实姓名只能填写英文字母或者中文!");
				return false;
			}
			if(jq("#sequenceNo").val()!==''&&!checkFormUtil.checkSequenceNo(jq("#sequenceNo").val())){
				util.alertMessage("订单号首字符为英文字母，后面9位数字!");
				return false;
			}


			if(util.setMyDate(advanceQuery.toDate) >= util.setMyDate(advanceQuery.fromDate)){
				advanceQuery.trainCode = jq('#trainCode').val();
				advanceQuery.passengerName = jq('#passengerName').val();
				advanceQuery.sequenceNo = jq('#sequenceNo').val();
				jq.mobile.changePage("finishedOrderList.html");
			} else {
				util.alertMessage("起始日期不能在截止日期之后！");				
			}
			return false;
		});
	}
	
})();