
/* JavaScript content from js/controller/passengers.1.js in folder common */
(function(){
	var changeSeatTypeIndex = 0;
	var passengerListIndex = -1;
	var firstclick = true;
	
	/*jq("#passengersView").live("pagecreate", function() {
		//mor.ticket.util.androidRemoveIscroll("#passengersView");
	});*/
	var focusArray=[];
	jq("#passengersView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#passengerNameInput',focusArray);
		util.enableAutoScroll('#idNoInput',focusArray);
		util.enableAutoScroll('#mobileNoInput',focusArray);
		util.enableAutoScroll('#captchaInput',focusArray);
		if(util.isIPhone()){
			util.enableAutoScroll('#idTypeSelect',focusArray);
			util.enableAutoScroll('#ticketTypeSelect',focusArray);
			util.enableAutoScroll('#seatTypeSelect',focusArray);
		}
	}
	
	jq("#passengersView").live("pagehide", function(e, data){
		jq("#passengersView").css("pointer-events","auto");
		var intervalId = mor.ticket.poll.intervalId;
		if(intervalId != ""){
			mor.ticket.poll.timer.stop();
			clearInterval(mor.ticket.poll.intervalId);
		}
		if(mor.ticket.viewControl.bookMode == "fc"){
			if(data.nextPage.attr("id") != "searchSingleTicketResultsView" && data.nextPage.attr("id") != "orderDetailsView"){
				mor.ticket.viewControl.bookMode = "dc";
				mor.ticket.viewControl.tab1_cur_page=vPathCallBack()+"loginTicket.html";
			}
		}
	});
	jq("#passengersView").live("pageinit", function(){
		if(mor.ticket.util.isAndroid()&& (parseFloat(device.version) > 3.0)){
		   	jq("#passengersView .iscroll-wrapper").addClass("smoothSlide");
		}
		
		/*
		
		if(mor.ticket.loginUser.isAuthenticated === "N"){
			//alert(vPathCallBack()+"loginTicket.html");
			
			 window.ticketStorage.setItem("isKeepTicket", 'true');
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}*/
		
		
		
		
		
		registerFormChangeListeners();
		registerPassengersButtonsListener();
		registerAutoScroll();
		registershowAllpassengerInfoTipsListeners();
		//var proMsg = mor.ticket.cache.promptMsg.trainTicketBook_web;
		//jq("#passengerInfoTips").html(proMsg);	
	});
	
	jq("#passengersView").live("pagebeforeshow", function(e,data){
		//jq('#passengersView .iscroll-wrapper').iscrollview('refresh');
		//alert(mor.ticket.loginUser.isAuthenticated);
		
			mor.ticket.viewControl.tab1_cur_page="passengers.html";	
			idTypeControl();
			jq("#bookTicketModifyPassengerDiv").hide();
			jq("#showAllpassengerInfoTips").show();
			jq("#passengerInfoTips2").hide();
			var mode = mor.ticket.viewControl.bookMode;
			//dom cache, refresh check code each time from ticket detail view		
			if(data.prevPage.attr("id") === "searchSingleTicketResultsView"){
				mor.ticket.orderManager.refreshCaptcha2();
				mor.ticket.currentPassenger = {};		
				mor.ticket.passengerList=[];
				contentIscrollTo(0,0,0);
			}
			
			var type_ids=[];//用于储存席别数组
			for(var i=0; i<mor.ticket.currentTicket.yplist.length; i++){
				if(mor.ticket.currentTicket.yplist[i].num){
					var seat_type = mor.ticket.currentTicket.yplist[i].type_id;
					type_ids.push(seat_type);
				}
			}
			var type_id = type_ids[0];//获取默认的席别编号		
			var passengerList=mor.ticket.passengerList;			
			var ticket = mor.ticket.currentTicket;
			var cache = mor.ticket.cache;			
			var train_date = ticket.train_date;
			var str="单程:";
			jq("#passengersView .ui-header>h1").html("乘车人信息");
			var oldSeatType;
			if(mode === "fc") {
				var oldSeatTypeArray = [];
				str="返程:";
				var orderManager = mor.ticket.orderManager;
				for(var i=0;i<orderManager.orderTicketList.length;i++){
					oldSeatTypeArray[i] = orderManager.orderTicketList[i].seat_type_code;
					//判断现有的车有没有原来的座位类型
					if(jq.inArray(oldSeatTypeArray[i],type_ids)<0){
						oldSeatTypeArray[i] = type_id;
					}
				}
				
				orderManager.getConfirmOrderListPassengersFC(passengerList, oldSeatTypeArray);
				updateCandidateList(passengerList);	
				jq("#choosePassenger").hide();
				jq(".deletePassenger").hide();
				jq(".addContact").hide();
				jq(".changeTicket").hide();
				
				//jq(".changeSeatType").show();
				jq(".bookBackTicket").show();			
			}else if(mode === "gc"){
				jq("#passengersView .ui-header>h1").html("乘车人信息(改签)");
				//获取到改签车票的坐席编号
				oldSeatType = mor.ticket.queryOrder.changeTicketOrderList[0].seat_type_code;
				//判断现有的车有没有原来的座位类型
				if(jq.inArray(oldSeatType,type_ids)<0){
					oldSeatType = type_id;
				}
				passengerList = [];
				mor.ticket.queryOrder.getChangeTicketPassengers(passengerList, oldSeatType);
				mor.ticket.passengerList = passengerList;
				updateCandidateList(passengerList);

				jq("#choosePassenger").hide();
				jq(".deletePassenger").hide();
				jq(".addContact").hide();
				jq(".bookBackTicket").hide();
				
				//jq(".changeSeatType").show();
				jq(".changeTicket").show();		
				jq("#submitOrderBtn").parent().hide();
				jq("#submitChangeTicketBtn").parent().show();	
				
			}else {
				if(mode === "wc") {
					str="往程:";
				}
				var currentPassenger=mor.ticket.currentPassenger;			
				currentPassenger.seat_type = type_id;
				updateForm(currentPassenger);
				updateCandidateList(passengerList);	
				jq("#choosePassenger").show();
				jq(".deletePassenger").show();
				jq(".addContact").show();
				
				jq(".changeTicket").hide();			
				//jq(".changeSeatType").hide();
				jq(".bookBackTicket").hide();	
				jq("#submitOrderBtn").parent().show();
				jq("#submitChangeTicketBtn").parent().hide();	
			}
			//mjl fix席别选不中
			if(mor.ticket.util.isIPhone()){
//				jq("#passengersView select.changeSeatType").bind('vmouseup',function(e){
//					e.preventDefault();
//					jq("#passengersView select.changeSeatType").focus();
//				});
				mor.ticket.util.enableAutoScroll("#passengersView select.changeSeatType",focusArray);
			}
			
			//init prompt list
			var util = mor.ticket.util;
			
			
			jq("#passengersPrompt .ui-block-a").html(str + util.getLocalDateString1(train_date));
			/*jq("#passengersPrompt .ui-block-b").html(ticket.station_train_code);
			jq("#passengersPrompt .ui-block-c").html(cache.getStationNameByCode(ticket.from_station_telecode) +
					"-" + cache.getStationNameByCode(ticket.to_station_telecode));
			
			*/
			if(mode === "fc"){
				initSelectsFC(oldSeatTypeArray);
			}else{
				initSelects(oldSeatType);
			}
			
			updateButtonState();
			//jq.mobile.loadPage("selectPassenger.html");	
	
	});
	
	//购票用户身份控制
	function idTypeControl(){
		if(mor.ticket.loginUser.id_type == "1" || mor.ticket.loginUser.id_type == "2"){
			jq("#idTypeSelect").html('<optgroup label="请选择证件类型">'+
	          	'<option value="1" selected>二代身份证</option>'+
          		'<option value="2">一代身份证</option>'+
         		'<option value="C">港澳通行证</option>'+
          		'<option value="G">台湾通行证</option>'+
          		'<option value="B">护照</option>'+
          	'</optgroup>');
		}else{
			jq("#idTypeSelect").html('<optgroup label="请选择证件类型">'+
	         		'<option value="C" selected>港澳通行证</option>'+
	          		'<option value="G">台湾通行证</option>'+
	          		'<option value="B">护照</option>'+
	          	'</optgroup>');
		}

	}
	function registershowAllpassengerInfoTipsListeners(){
		jq("#showAllpassengerInfoTips").bind("tap", function(){
			jq("#passengerInfoTips2").show();
			jq("#showAllpassengerInfoTips").hide();
			jq('#passengersView .iscroll-wrapper').iscrollview('refresh');
			return false;
		});
	}
	function registerFormChangeListeners(){
		var currentPassenger=mor.ticket.currentPassenger;
		var util = mor.ticket.util;
		jq("#passengerNameInput").bind("change", function(){
			currentPassenger.user_name = jq(this).val();
			
			//判断如果当前的模式是针对于修改联系人的情况，不应该显示追加联系人按钮
			if(!jq("#bookTicketModifyPassengerDiv").is(":visible")){
				if(currentPassenger.user_name != ""){
					jq("#bookTicketAddPassengerBtn").parent().show();
				}
			}
			return false;
		});
		util.bindSelectFocusBlurListener("#idTypeSelect");
		jq("#idTypeSelect").bind("change", function(){
			currentPassenger.id_type = jq(this).val();
			return false;
		});
		jq("#idNoInput").bind("change", function(){
			currentPassenger.id_no = jq(this).val();
			//判断如果当前的模式是针对于修改联系人的情况，不应该显示追加联系人按钮
			if(!jq("#bookTicketModifyPassengerDiv").is(":visible")){
			
				if(currentPassenger.id_no != ""){
					jq("#bookTicketAddPassengerBtn").parent().show();
				}
			}
			return false;
		});
		jq("#mobileNoInput").bind("change", function(){
			currentPassenger.mobile_no = jq(this).val();
			return false;
		});
		util.bindSelectFocusBlurListener("#ticketTypeSelect");
		jq("#ticketTypeSelect").bind("change", function(){
			//判断当前的旅客类型是否和车票类型相符，只有学生才能买学生票
			//currentPassenger变量不要在其他业务逻辑中使用
			if(mor.ticket.currentPassenger.ticket_type!='3'&&jq(this).val()=='3'&&mor.ticket.currentPassenger.ticket_type!=undefined){
				//util.alertMessage("tt"+mor.ticket.currentPassenger.ticket_type);
				jq("#ticketTypeSelect option[value="+mor.ticket.currentPassenger.ticket_type+"]").attr("selected","selected");
				jq("#ticketTypeSelect").selectmenu('refresh', true);
				util.alertMessage("只有学生类型的常用联系人可以购买学生票！");
				
			}else{
				currentPassenger.ticket_type = jq(this).val();
			}
			return false;
		});
		util.bindSelectFocusBlurListener("#seatTypeSelect");
		jq("#seatTypeSelect").bind("change", function(){
			currentPassenger.seat_type = jq(this).val();
			return false;
		});
	}
	
	function registerPassengersButtonsListener(){
		jq("#passengersBackBtn").off().bind("click",function(e){
            e.preventDefault();
			e.stopImmediatePropagation();
            e.stopPropagation();
			jq.mobile.changePage("searchSingleTicketResults.html");
			return false;
		});
		
		jq("#selectPassengerImg").bind("tap", function(){
			jq("#passengersView").css("pointer-events","none");
			jq.mobile.changePage("selectPassenger.html");
			return false;
		});	

		jq("#candidateList").on("tap", ".deletePassenger", function(){
			var record = jq(this).parents(".passengerRecord");
			mor.ticket.passengerList.splice(record.index(), 1);
			record.remove();
			//resetForm();
			jq("#bookTicketModifyPassengerDiv").hide();
			updateButtonState();	
			return false;
		});
		
		jq("#candidateList").on("tap", "table", function(e){
			//强制让当前form的input框失去焦点
			if(jq("input").is(":focus")){
				//jq("input").blur();
				jq("input:focus")[0].blur();
			}

			if(mor.ticket.viewControl.bookMode == 'wc' || mor.ticket.viewControl.bookMode == 'dc'){
				var index = jq(this).parent().index();
				var jq_li = jq(this).parent();
				if(passengerListIndex == index){
					if(firstclick){
						jq_li.addClass("ui-btn-active")
							.siblings().removeClass("ui-btn-active");
						passengerListIndex = index;
						var modifypassenger = mor.ticket.passengerList[index];
						fillPasserger(modifypassenger);
						firstclick = false;
						contentIscrollTo(0,0,200);
					}else{
						jq_li.removeClass("ui-btn-active");
						jq("#bookTicketModifyPassengerDiv").hide();
						resetForm();
						firstclick = true;
						y = jq("#choosePassenger").height();
						contentIscrollTo(0,-y,200);
					}
				}else{
					//第一次点击
					jq_li.addClass("ui-btn-active")
						.siblings().removeClass("ui-btn-active");
					passengerListIndex = index;
					var modifypassenger = mor.ticket.passengerList[index];
					fillPasserger(modifypassenger);
					firstclick = false;
					contentIscrollTo(0,0,200);
				}
			}
			
			return false;
		});
		
		jq("#bookTicketAddPassengerBtn").bind("tap", function(){
			var util = mor.ticket.util;
			if(mor.ticket.passengerList.length>4){
				util.alertMessage("单程每次最多可购5张车票");
				return false;
			}
			if(validation()){
				var passenger = getPassengerInfo();
				if(!hasPassenger(passenger)){
					jq("#candidateList").append(generatePassengerRecord(passenger));
					mor.ticket.passengerList.push(passenger);
					resetForm();							
					updateButtonState();
					//jq("#candidateList").parents(".iscroll-wrapper").iscrollview("refresh");
					jq('#passengersView .iscroll-wrapper').iscrollview('refresh');
					y = jq("#choosePassenger").height();
					bh = jq(".tips3").height()+jq("#orderSubmitDiv").height()+jq("#candidateList").height()-jq("#passengersView .iscroll-wrapper").height();
					if(bh<0){
						y = y+bh;
					}
					contentIscrollTo(0,-y,200);
					/*
					WL.Logger.debug(bh);
					WL.Logger.debug(jq("#choosePassenger").height());
					WL.Logger.debug(jq("#candidateList").height());
					WL.Logger.debug(jq("#orderSubmitDiv").height());
					WL.Logger.debug(jq(".tips3").height());
					WL.Logger.debug(jq("#passengersView .iscroll-wrapper").height());
					WL.Logger.debug(document.documentElement.clientHeight);
					*/
				}
				else {
					util.alertMessage("互联网售票实名制："+passenger.id_no+"只能购买相同日期相同车次的一张车票");
				}
			}
			return false;
		});
		
		jq("#bookTicketModifyPassengerBtn").bind("tap", function(){
			if(validation()){
				var passenger = getPassengerInfo();
				if(!hasPassenger2(passenger)){
					mor.ticket.passengerList[passengerListIndex]=passenger;
					updateCandidateList(mor.ticket.passengerList);
					resetForm();
					updateButtonState();
					jq("#bookTicketModifyPassengerDiv").hide();
					y = jq("#choosePassenger").height();
					contentIscrollTo(0,-y,200);
				}else {
					var util = mor.ticket.util;
					util.alertMessage("互联网售票实名制："+passenger.id_no+"只能购买相同日期相同车次的一张车票");
				}
			}
			return false;
		});
		
		jq("#refreshCaptcha").bind("tap", mor.ticket.orderManager.refreshCaptcha);
		
		jq("#waitTimeOKbtn").off().on("tap",function(){
			jq("#counter_line").hide();
			mor.ticket.viewControl.tab1_cur_page = vPathViewCallBack()+"MobileTicket.html";
			var intervalId = mor.ticket.poll.intervalId;
			if(intervalId != ""){
				mor.ticket.poll.timer.stop();
				clearInterval(mor.ticket.poll.intervalId);
			}
			//如果当前的mode为gc那么在改签提交订单后更改mode为dc
			if(mor.ticket.viewControl.bookMode == "gc"){
				mor.ticket.viewControl.bookMode = "dc";
			}
			jq.mobile.changePage("queryOrder.html");
			return false;
		});
		
		jq("#submitOrderBtn").bind("tap", function(){
			//mjl
			if(mor.ticket.viewControl.bookMode=="fc"&&
					jq('#passengersView .bookBackTicketCheckBox:visible:checked').length==0){
				mor.ticket.util.alertMessage("请勾选需要购买的车票！");
				return false;
			}
			if(jq("#captchaInput").val() != "" ){
				  if(validationForSubmit()){
					  var tipMessage = "确定要提交订单吗？";
					  if(validationTibet()){
						  tipMessage = "根据现行规定，外国人在购买进西藏火车票时，须出示西藏自治区外事办公室或旅游局、商务厅的批准函（电），或者出示中国内地司局级接待单位出具的、已征得自治区上述部门同意的证明信函。台湾同胞赴藏从事旅游、商务活动，须事先向西藏自治区旅游局或商务厅提出申请，购买进藏火车票时须出示有关批准函。\n确定要提交订单吗？";
					  }
						WL.SimpleDialog.show(
								"温馨提示", 
								tipMessage, 
								[ {text : '取消', handler: function(){}},
								  {text : '确定', handler: function(){
									  var orderManager=mor.ticket.orderManager;
										orderManager.clearTicketList();
										var mode = mor.ticket.viewControl.bookMode;
										var length;
										if(mode == "fc"){
											length = jq(".bookBackTicketCheckBox:checked").length;
										}else if(mode == "gc"){
											length = jq(".changeTicketCheckBox:checked").length;
										}else{
											length =mor.ticket.passengerList.length;
										}
										orderManager.getTotalTicketNum(length);
										var parameters=prepareSubmitOrderParameters();
										orderManager.confirmPassengerInfo(parameters);
								  }}]
							);  
				  }
			} else{
				mor.ticket.util.alertMessage("请输入验证码!");
			}
			return false;
		});
		
		/*jq("#candidateList").on("tap", "a", function(){
			//jq("#seatTypeSelectBack").scroller('show');
			//jq("#seatTypeSelectBack").tap();
			//jq("#seatTypeSelectBack").selectmenu('open');
			changeSeatTypeIndex = jq(this).parents('.passengerRecord').index();
			return false;
		});
		
		jq("#seatTypeSelectBack").bind("change",function(){
			var passengerList = mor.ticket.passengerList;
			passengerList[changeSeatTypeIndex].seat_type = jq(this).val();
			jq('.passengerInfoHead').eq(changeSeatTypeIndex).children('#seatType').html(mor.ticket.util.getSeatTypeName(jq(this).val()));
			return false;
		});*/
		
		jq("#candidateList").on("change",".changeSeatType",function(e){
			var passengerList = mor.ticket.passengerList;
			changeSeatTypeIndex = jq(this).parents('.passengerRecord').index();
			passengerList[changeSeatTypeIndex].seat_type = jq(this).val();
			e.stopImmediatePropagation();
			return false;
		});
		
		jq("#submitChangeTicketBtn").live("tap",function(){
			//必须勾选一个改签票
			if(jq('#passengersView .changeTicketCheckBox:visible:checked').length==0){
				mor.ticket.util.alertMessage("请勾选需要改签的车票！");
				return false;
			}
			
			//改签时必须选择相同座位
			var passengerList = mor.ticket.passengerList;
			var seat_array = [];
			for(var i=0;i<passengerList.length;i++){
				if (jq(".passengerRecord:eq("+i+") .changeTicketCheckBox").attr("checked") == "checked"){
					var seat = passengerList[i].seat_type;
					seat_array.push(seat);
				}
			}			
			jq.unique(seat_array);
			if(seat_array.length>1){
				mor.ticket.util.alertMessage("改签时，必须选择相同席别！");
				return false;
			}
			
			//增加验证码的校验
			if(jq("#captchaInput").val() != "" ){
				var parameters=prepareChangeTicketParameters();
				var orderManager=mor.ticket.orderManager;
				orderManager.confirmPassengerInfo(parameters);
			}else{
				mor.ticket.util.alertMessage("请输入验证码!");
			}
			return false;
		});
	}
	
	function initSelectsFC(fcSeatTypeArray){
		var ticket = mor.ticket.currentTicket;
		//对无座票进行处理，按照id显示座位
		var processedTicket = processSeatTypeInCurrentTicket(ticket);
		jq(".changeSeatType optgroup").html(generateSeatsTypeOption(processedTicket));
		var seatTypeArray=[];//用于储存席别数组
		for(var i=0; i<processedTicket.yplist.length; i++){
			var seat_type = processedTicket.yplist[i].type_id;
			seatTypeArray.push(seat_type);
		}
		for(var i = 0;i<ticket.yplist.length;i++){
			if(jq.inArray(fcSeatTypeArray[i],seatTypeArray)>=0){
				jq(".changeSeatType:eq("+i+") option[value="+fcSeatTypeArray[i]+"]").attr("selected", true);
			}
		}
	}
	
	function initSelects(gcSeatType){
		var ticket = mor.ticket.currentTicket;
		//对无座票进行处理，按照id显示座位
		var processedTicket = processSeatTypeInCurrentTicket(ticket);
		var mode = mor.ticket.viewControl.bookMode;
		if(mode == "gc"){
			jq(".changeSeatType optgroup").html(generateSeatsTypeOption(processedTicket));
			//使用原来改签前旧的票的席别初始化改签车票的席别
			var tag = false;//标示是否改签车票包含原车票的坐席
			for(var i = 0;i<ticket.yplist.length;i++){
				if(ticket.yplist[i].type_id === gcSeatType){
					tag = true;
				}
			}
			if(tag){
				//按照gcSeatType的值初始化select的选中选项
				jq(".changeSeatType option[value="+gcSeatType+"]").attr("selected", true);
			}
		}else{
			jq("#seatTypeSelect optgroup").html(generateSeatsTypeOption(processedTicket));
			jq("#seatTypeSelect").selectmenu('refresh', true);
		}		
	}
	//对无座票进行处理，不显示无座，按照id显示座位类型.也就是显示硬座或者一等座/二等座
	//同时在数组中删除无座的成员，无座的情况按照rate = 100判断。
	//如果只有无座的票，那么按照真实的席别显示
	function processSeatTypeInCurrentTicket(ticket){
		var cache = mor.ticket.cache;
		var yplist = ticket.yplist;
		for(var i=0 ; i<yplist.length ; i++){
			var seatTypeId = yplist[i].type_id;
			yplist[i].type = cache.getSeatTypeByCode(seatTypeId);
		}
		//var n = 1;
		//如果有两个元素的id一样，那么只保留无座对应的元素
		for(var i=0 ; i<yplist.length ; i++){
			for(var j = i+1 ; j < yplist.length ; j++){
				if(yplist[i].type_id === yplist[j].type_id){
					if(yplist[i].type_rate !== 100){
						yplist.splice(i,1);
					}
				}
			}
			
		}
		return ticket;
	}
	
	
	function updateCandidateList(passengers){
		var html = "";
		for (var i = 0; i < passengers.length; i++){
			html += generatePassengerRecord(passengers[i]);
		}
		jq("#candidateList").html(html);		
	}
	
	function updateButtonState(){
		var userNameInput = jq("#passengerNameInput").val();
		var userIdNoInput = jq("#idNoInput").val();
		if(userNameInput != "" || userIdNoInput != ""){
			jq("#bookTicketAddPassengerBtn").parent().show();
		}else if(userNameInput == "" && userIdNoInput == ""){
			jq("#bookTicketAddPassengerBtn").parent().hide();
		}		
		var submitDiv = jq("#orderSubmitDiv");
		var addPassenger = jq("#bookTicketAddPassengerBtn .ui-btn-text");
		if (mor.ticket.passengerList.length > 0 ){
			addPassenger.html("追加乘车人");
			submitDiv.show();
		} else {
			submitDiv.hide();
			addPassenger.html("添加乘车人");			
		}		
		contentIscrollRefresh();
	}
	
	function getPassengerInfo(){
		var price = jq("#seatTypeSelect").find("option:selected").text();
		var priceArray = price.split("   ");
		return {
			"user_name" : jq("#passengerNameInput").val(),
			"id_type" : jq("#idTypeSelect").val(),
			"id_no" : jq("#idNoInput").val(),
			"mobile_no" : jq("#mobileNoInput").val(),
			"ticket_type" : jq("#ticketTypeSelect").val(),
			"seat_type" : jq("#seatTypeSelect").val(),
			"ticket_price" : priceArray[1]
		};
	}
	
	function validation(){
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		if(util.isNoValue(jq("#passengerNameInput").val())){
			util.alertMessage("请输入乘车人姓名");
			return false;
		}
		if(!checkFormUtil.checkChar(jq("#passengerNameInput").val())){
			util.alertMessage("姓名只能填写英文字母或者中文");
			return false;
		}
		if(util.isNoValue(jq("#idTypeSelect").val())){
			util.alertMessage("请选择乘车人证件类型");
			return false;
		}
		if(util.isNoValue(jq("#idNoInput").val())){
			util.alertMessage("请输入证件号码");
			return false;
		}
		
		if(!checkFormUtil.checkIdValidStr(jq("#idNoInput").val())){
			util.alertMessage("输入的证件编号中包含中文信息或特殊字符");
			return false;
		}
		
		//1 2 B C G 身份证验证
		if(jq("#idTypeSelect").val()=="1"){
			if(!checkFormUtil.validateSecIdCard(jq("#idNoInput").val())){
				util.alertMessage("请正确输入18位的身份证号");
				return false;
			}
		}
		
		if(jq("#idTypeSelect").val()=="2"){
			if(!checkFormUtil.validateFirIdCard(jq("#idNoInput").val())){
				util.alertMessage("请正确输入15或者18位的身份证号");
				return false;
			}
		}
		
		if(jq("#idTypeSelect").val()=="B"){
			if(!checkFormUtil.checkPassport(jq("#idNoInput").val())){
				util.alertMessage("请输入有效的护照号码");
				return false;
			}
		}
		
		if(jq("#idTypeSelect").val()=="C"){
			if(!checkFormUtil.checkHkongMacao(jq("#idNoInput").val())){
				util.alertMessage("请输入有效的港澳居民通行证号码");
				return false;
			}
		}
		
		if(jq("#idTypeSelect").val()=="G"){
			if(!checkFormUtil.checkTaiw(jq("#idNoInput").val())){
				util.alertMessage("请输入有效的台湾居民通行证号码");
				return false;
			}
		}
		if(util.isNoValue(jq("#ticketTypeSelect").val())){
			util.alertMessage("请选择票种");
			return false;
		}
		
		if(util.isNoValue(jq("#seatTypeSelect").val())){
			util.alertMessage("请选择席别");
			return false;
		}
		if(jq("#idTypeSelect").val() !="1" && jq("#idTypeSelect").val() !="2" && jq("#ticketTypeSelect").val() == "4"){
			util.alertMessage("只能用身份证购买残军票");
			return false;
		}
		return true;
	}
	function validationForSubmit(){
		var util = mor.ticket.util;
		var passengers = mor.ticket.passengerList;
		var mode = mor.ticket.viewControl.bookMode;
		//一笔订单，不允许都是小孩票，必须至少绑定一张成人票
		if( mode == "dc" || mode == "wc")  {
			var childFlag = false;
			var adultFlag = false;
			for (var i = 0; i < passengers.length; i ++ ){			
				var passenger = passengers[i];	
				if( passenger.ticket_type == "1"){
					adultFlag = true;
					break;
				}
				if(passenger.ticket_type == "2"){
					childFlag = true;
				}					
			}
			if(childFlag&&!adultFlag){
				util.alertMessage("儿童不能单独旅行，请与成人票一同购买");
				return false;
			}
		}
		return true;
	}
	function validationTibet(){
		var passengers = mor.ticket.passengerList;
		var mode = mor.ticket.viewControl.bookMode;
		var ticket = mor.ticket.currentTicket;
		var cache = mor.ticket.cache;
		if( mode == "dc" || mode == "wc" || mode == "fc")  {
			var toStation = cache.getStationNameByCode(ticket.to_station_telecode);
			if(toStation == "拉萨"|| toStation == "当雄"||toStation == "安多"||toStation == "那曲"||toStation == "沱沱河"){
				for (var i = 0; i < passengers.length; i ++ ){			
					var passenger = passengers[i];	
					if( passenger.id_type == "B"){
						return true;
					}					
				}
			}else{
				return false;
			}
		}
		return false;
	}
	
	//添加时重复联系人判断 +学生类型常用联系人信息不能编辑修改
	function hasPassenger(passenger){
		var util = mor.ticket.util;
		var ticket_type = passenger.ticket_type;
		if(ticket_type!="2"){
			var passengerID = passenger.id_no;
			var idtype = passenger.id_type;
			var passengersList = mor.ticket.passengerList;
			for(var i=0; i<passengersList.length; i++){
				if(passengersList[i].ticket_type != "2"){
					if(passengerID == passengersList[i].id_no){
						return true;				
					}
				}
			}
		}
		
		if(ticket_type=="3"){//当前是学生票
			if(passenger.id_no == mor.ticket.currentPassenger.id_no&&passenger.user_name == mor.ticket.currentPassenger.user_name&&passenger.id_type == mor.ticket.currentPassenger.id_type){
				return false;
			}else{
				util.alertMessage("请从常用联系人列表中选择学生类型常用联系人");
				return true;
			}
			
		}
		return false;
	}
	
	//修改时重复联系人判断+学生类型常用联系人信息不能编辑修改
	function hasPassenger2(passenger){
		var util = mor.ticket.util;
		var ticket_type = passenger.ticket_type;
		var passengersList = mor.ticket.passengerList;
		if(ticket_type!="2"){
			var passengerID = passenger.id_no;
			var idtype = passenger.id_type;
			
			for(var i=0;i<passengersList.length; i++){
				if(i==passengerListIndex){
					continue;
				}
				if(passengersList[i].ticket_type != "2"){
					if(passengerID == passengersList[i].id_no){
						return true;				
					}
				}
			}
		}
		
		if(ticket_type=="3"){//当前是学生票
			if(passenger.id_no == passengersList[passengerListIndex].id_no&&passenger.user_name == passengersList[passengerListIndex].user_name&&passenger.id_type == passengersList[passengerListIndex].id_type){
				return false;
			}else{
				util.alertMessage("请从常用联系人列表中选择学生类型常用联系人");
				return true;
			}
			
		}
		
		
		return false;
	}
	
	function resetForm(){
		jq("#passengerNameInput, #idNoInput, #mobileNoInput").val("");
	}
	
	function updateForm(passenger){
		jq("#passengerNameInput").val(passenger.user_name);
		jq("#idNoInput").val(passenger.id_no);
		jq("#mobileNoInput").val(passenger.mobile_no);
		jq("#idTypeSelect option[value="+passenger.id_type+"]").attr("selected","selected");
		jq("#idTypeSelect").selectmenu('refresh', true);
		jq("#ticketTypeSelect option[value="+passenger.ticket_type+"]").attr("selected","selected");
		jq("#ticketTypeSelect").selectmenu('refresh', true);
					
	}
	
	function fillPasserger(modifypassenger){
		updateForm(modifypassenger);
		jq("#seatTypeSelect option[value="+modifypassenger.seat_type+"]").attr("selected","selected");
		jq("#seatTypeSelect").selectmenu('refresh', true);
		jq("#bookTicketAddPassengerDiv").hide();
		jq("#bookTicketModifyPassengerDiv").show();	
	}
	
	function contentIscrollRefresh(){
		if(jq("#passengersView .ui-content").attr("data-iscroll")!=undefined){
			jq("#passengersView .ui-content").iscrollview("refresh");
		}
	}
	
	function contentIscrollTo(x,y,time){
		if(jq("#passengersView .ui-content").attr("data-iscroll")!=undefined){
			jq("#passengersView .ui-content").jqmData('iscrollview').iscroll.scrollTo(x,y,time);
		}
	}
	
	function prepareSubmitOrderParameters(){
		var util = mor.ticket.util;
		var ticket = mor.ticket.currentTicket;
		
		var mode = mor.ticket.viewControl.bookMode;
		var train_date = ticket.train_date;
			
		var parameters = {
			train_date: util.processDateCode(train_date),
			station_train_code: ticket.station_train_code,
			from_station: ticket.from_station_telecode,
			to_station: ticket.to_station_telecode,
			pass_code: jq("#captchaInput").val(),
			location_code:ticket.location_code,
			yp_info: ticket.yp_info
		};
				
		var seatTypes = [];
		var ticketTypes = [];
		var passenger_id_types = [];
		var passenger_id_nos = [];
		var passenger_names = [];
		var mobile_nos = [];
		var save_passenger_flag = [];
	
		var passengers = mor.ticket.passengerList;
		for (var i = 0; i < passengers.length; i ++ ){			
			var passenger = passengers[i];			
			if(mode === "fc") { 
				if (jq(".passengerRecord:eq("+i+") .bookBackTicketCheckBox").attr("checked") == "checked"){
					seatTypes.push(passenger.seat_type);
					ticketTypes.push(passenger.ticket_type);
					passenger_id_types.push(passenger.id_type);
					passenger_id_nos.push(passenger.id_no);
					passenger_names.push(passenger.user_name);
				}
			}else {				
				seatTypes.push(passenger.seat_type);
				ticketTypes.push(passenger.ticket_type);
				passenger_id_types.push(passenger.id_type);
				passenger_id_nos.push(passenger.id_no);
				passenger_names.push(passenger.user_name);
				mobile_nos.push(passenger.mobile_no);
				if (jq(".passengerRecord:eq("+i+") .addContactCheckBox").attr("checked") == "checked"){
					save_passenger_flag.push("Y");
				} else {
					save_passenger_flag.push("N");
				}				
			}						
		}
		
		jq.extend(parameters, {
			"seat_type_codes": seatTypes.join(","),
			"ticket_types": ticketTypes.join(","),
			"passenger_id_types": passenger_id_types.join(","),
			"passenger_id_nos": passenger_id_nos.join(","),
			"passenger_names": passenger_names.join(","),
			"ticket_type_order_num": passengers.length.toString(),
			"bed_level_order_num": "0", // 无用参数
			"start_time" : ticket.start_time,
			"tour_flag" : mode
		});
		
		if(mode === "fc"){
			jq.extend(parameters, {
				"sequence_no" :mor.ticket.orderManager.orderTicketList[0].sequence_no
			});
		} else {
			jq.extend(parameters, {
				"mobile_nos": mobile_nos.join(","),
				"save_passenger_flag": save_passenger_flag.join(",")
			});
		}
		
		return parameters;
	}
	
	function prepareChangeTicketParameters(){
		var util = mor.ticket.util;	
		var ticket = mor.ticket.currentTicket;
		
		var seatTypes = [];
		var ticketTypes = [];
		var passenger_id_types = [];
		var passenger_id_nos = [];
		var passenger_names = [];
		
		var passengers = mor.ticket.passengerList;
		
		var ori_coach_nos=[];
		var ori_seat_nos=[];
		var ori_batch_nos=[];
		
		var parameters = {
			train_date: util.processDateCode(ticket.train_date),
			station_train_code: ticket.station_train_code,
			from_station_telecode: ticket.from_station_telecode,
			to_station_telecode: ticket.to_station_telecode,
			rand_code: jq("#captchaInput").val(),
			location_code:ticket.location_code,
			start_time: ticket.start_time,
			yp_info: ticket.yp_info,
			tour_flag : "gc"
		};
		
		for (var i = 0; i < passengers.length; i ++ ){						
			if (jq(".passengerRecord:eq("+i+") .changeTicketCheckBox").attr("checked") == "checked"){
				seatTypes.push(passengers[i].seat_type);
				ticketTypes.push(passengers[i].ticket_type);
				passenger_id_types.push(passengers[i].id_type);
				passenger_id_nos.push(passengers[i].id_no);
				passenger_names.push(passengers[i].user_name);
			}		
		}	
		
		var originTicket = mor.ticket.queryOrder.changeTicketOrderList;				
		
		for(var i=0;i<originTicket.length;i++){
			if (jq(".passengerRecord:eq("+i+") .changeTicketCheckBox").attr("checked") == "checked"){
				ori_coach_nos.push(originTicket[i].coach_no);
				ori_seat_nos.push(originTicket[i].seat_no);
				ori_batch_nos.push(originTicket[i].batch_no);
			}
		}
		
		jq.extend(parameters, {
			"seat_type_codes": seatTypes.join(","),
			"ticket_type_codes":ticketTypes.join(","),
			"ori_passenger_id_type_codes": passenger_id_types.join(","),
			"ori_passenger_id_nos": passenger_id_nos.join(","),
			"ori_passenger_names": passenger_names.join(","),
			"sequence_no" : originTicket[0].sequence_no,
			"ori_batch_nos" : ori_batch_nos.join(","),
			"ori_coach_nos" : ori_coach_nos.join(","),
			"ori_seat_nos" : ori_seat_nos.join(",")	
		});
		
		return parameters;
	}
	
	var seatsOptionTemplate =
        "{{ for(var i=0;i<it.yplist.length;i++) { }}" +
        	"{{ if(it.yplist[i].num){ }}"+ 
            "<option value='{{=it.yplist[i].type_id}}'>{{=it.yplist[i].type}}"+"{{ if(it.yplist[i].price>0){}}"+"   {{=it.yplist[i].price}}元"+"{{ } }}"+"</option>" +
            "{{ } }}"+
        "{{ } }}";	
	var generateSeatsTypeOption = doT.template(seatsOptionTemplate);
	
	var passengerListTemplate =
		"{{ var mode = mor.ticket.viewControl.bookMode; }}"+
		"<li class='passengerRecord'>" +
			"<table class='passengerDetail'>" +
			"<tr>" +
				"<td class='passengerName'>{{=it.user_name}}</td>" +
				"<td class='passengerInfoHead'>" +
					"<div id='ticketType'>{{=mor.ticket.util.getTicketTypeName(it.ticket_type)}}<br/></div>" +
					"<div>{{=mor.ticket.util.getIdTypeName(it.id_type)}}<br/></div>" +
					"{{ if(mode=='dc' || mode =='wc') {}}" +
						"<div>手机号<br/></div>" +
					"{{ } }}" +
				"</td>" +
				"<td class='passengerInfoDetail'>" +
					"{{ if(mode=='dc' || mode =='wc') {}}" +
						"<div id='seatType'>{{=mor.ticket.util.getSeatTypeName(it.seat_type)}}<br/></div>" +
					"{{ }else{ }}" +
						"<div><select class='changeSeatType' name='席别'>" +
						"<optgroup label='请选择席别'><option value=''></option></optgroup>" +
						"</select></div>"+
					"{{ } }}" +
					/*"<a data-role='button' class='changeSeatType' style='display:none'>更改席别</a><br/></div>"+*/
					"<div>{{=it.id_no}}<br/></div>" +
					"{{ if(mode=='dc' || mode =='wc') {}}" +
					"<div>{{=it.mobile_no}}<br/></div>" +
					"{{ } }}" +
				"</td>" +
				"<td class='deletePassenger'>" +
					"<img src='../images/recycle.png'/>" +
				"</td>" +
			"</tr>" +
			"</table>"+
			"<div class='addContact'><label>加为联系人<input class='addContactCheckBox' type='checkbox' data-role='none' checked/></label></div>" +
			"" +
			"<div class='bookBackTicket' style='text-align:right;display:none'>" +
			"<label>购买返程车票<input class='bookBackTicketCheckBox' type='checkbox' data-role='none' checked/></label>" +
			"</div>" +
			"<div class='changeTicket' style='text-align:right;display:none'>" +
			"<label>改签<input class='changeTicketCheckBox' type='checkbox' data-role='none' checked></label>" +
			"</div>" +
		"</li>";
	
	var generatePassengerRecord = doT.template(passengerListTemplate);
})();