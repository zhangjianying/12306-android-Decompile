
/* JavaScript content from js/controller/farePassengers.js in folder common */
(function(){
	// var changeSeatTypeIndex = 0;
	/*
	 * jq("#passengersView").live("pagecreate", function() {
	 * //mor.ticket.util.androidRemoveIscroll("#passengersView"); });
	 */
	var focusArray=[];
	jq("#passengersView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){
		var util = mor.ticket.util;
		util.enableAutoScroll('#captchaInput',focusArray);
		if(util.isIPhone()){
			util.enableAutoScroll('.changeSeatType',focusArray);
			util.enableAutoScroll('.changeyplistType',focusArray);
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
				mor.ticket.viewControl.tab1_cur_page=vPathViewCallBack()+"MobileTicket.html";
			}
		}
	});
	jq("#passengersView").live("pageinit", function(){

		if(mor.ticket.util.isAndroid()&& (parseFloat(device.version) > 3.0)){
		   	jq("#passengersView .iscroll-wrapper").addClass("smoothSlide");
		}
		ticketDeletePassengerBtnListeners();
		ticketAddPassengerBtnListeners();
		registerFormChangeListeners();
		registerPassengersButtonsListener();
		registerAutoScroll();
		registershowAllpassengerInfoTipsListeners();
	});

	function farePsgFn(e,data){
		var ticket = mor.ticket.currentTicket;
		// 提前预处理。
		processSeatTypeInCurrentTicket(ticket);

		var cache = mor.ticket.cache;
		var train_date = ticket.train_date;

		regsiterYpListFunHandher();
		mor.ticket.viewControl.tab1_cur_page="passengers.html";
		idTypeControl();
		jq("#bookTicketModifyPassengerDiv").hide();
		jq("#showAllpassengerInfoTips").show();
		jq("#passengerInfoTips2").hide();
		var mode = mor.ticket.viewControl.bookMode;
		if(data.prevPage.attr("id") === "searchSingleTicketResultsView"){
			mor.ticket.orderManager.refreshCaptcha2();
			mor.ticket.history.inputyzm='';
			mor.ticket.currentPassenger = {};
			// mor.ticket.passengerList=[];
			contentIscrollTo(0,0,0);
		}


		var default_seat_type = ticket.getDefaultSeatType();
		var query = mor.ticket.leftTicketQuery;
		var str="单程:";
		if(query.purpose_codes==="0X"){
			jq("#passengersView .ui-header>h1").text("确认订单(学生)");
		}else if(query.purpose_codes==="1F"){
			jq("#passengersView .ui-header>h1").text("确认订单(农民工)");
		}else{
			jq("#passengersView .ui-header>h1").text("确认订单");
		}
		if(mode === "fc") {
			str="返程:";
			showPassengersFunHandher('fc');
			updateButtonState();
			jq("#choosePassenger").hide();
			jq(".deletePassenger").hide();
			jq(".addContact").hide();
			jq(".changeTicket").hide();
			jq('#TicketAddPassengerBtn').parent().hide();
			// jq(".changeSeatType").show();
			jq(".bookBackTicket").show();
		}else if(mode === "gc"){
			jq("#passengersView .ui-header>h1").text("确认订单");
			// 获取到改签车票的坐席编号
			str="改签:";
			showPassengersFunHandher('gc');
			updateButtonState();
			jq("#choosePassenger").hide();
			jq(".deletePassenger").hide();
			jq(".addContact").hide();
			jq(".bookBackTicket").hide();

			// jq(".changeSeatType").show();
			jq(".changeTicket").show();
			jq("#submitOrderBtn").parent().hide();
			jq("#submitChangeTicketBtn").parent().show();
			jq('#TicketAddPassengerBtn').parent().hide();
		}else {
			if(mode === "wc") {
				str="往程:";
			}

			var currentPassenger=mor.ticket.currentPassenger;
			currentPassenger.seat_type = default_seat_type;

			updateForm(currentPassenger);

			jq("#choosePassenger").show();
			jq(".deletePassenger").show();
			jq(".addContact").show();

			jq(".changeTicket").hide();
			// jq(".changeSeatType").hide();
			jq(".bookBackTicket").hide();
			jq("#submitOrderBtn").parent().show();
			jq("#submitChangeTicketBtn").parent().hide();
			jq('#TicketAddPassengerBtn').parent().show();

			showPassengersFunHandher();
			updateButtonState();
		}

		// mjl fix席别选不中
		if(mor.ticket.util.isIPhone()){
//jq("#passengersView select.changeSeatType").bind('vmouseup',function(e){
//e.preventDefault();
//jq("#passengersView select.changeSeatType").focus();
//});
			mor.ticket.util.enableAutoScroll("#passengersView select.changeSeatType",focusArray);
		}
		// init prompt list
		var util = mor.ticket.util;
		jq("#passengersPrompt .ui-block-a").html(str + util.getLocalDateString1(train_date));
		// alert(train_date);
        /**
		 *
		 * //jq(".car-head
		 * .car-head-number-box").html(ticket.station_train_code);
		 *
		 * //jq(".car-head
		 * .car-head-starandover").html(cache.getStationNameByCode(ticket.from_station_telecode)+'<br />
		 * <span class="span">'+ticket.start_time+'出发</span>');
		 *
		 * //jq(".car-head
		 * .car-head-endandover").html(cache.getStationNameByCode(ticket.to_station_telecode)+'<br />
		 * <span class="span">'+ticket.arrive_time+'到达</span>');
		 *
		 * //alert(ticket.lishiHour); var LishTime=""; if
		 * (ticket.lishiHour!= '00'){ LishTime+=ticket.lishiHour+"小时";
		 * }else{ LishTime+=ticket.lishiMinute+"分钟";
		 *  } jq(".car-head .car-head-number-time").html(LishTime);
		 */

		/*
		 * if(mode === "fc"){ initSelectsFC(oldSeatTypeArray); }else{
		 * initSelects(oldSeatType); }
		 */

        jq("#pfromStationName").html(cache.getStationNameByCode(ticket.from_station_telecode));
		jq("#ptrainStartTime").html(ticket.start_time + " 出发");
		jq("#ptrainCodeName").html(ticket.station_train_code);
		jq("#ptrainDurationTime").html(util.getLiShiStr(ticket.lishi));
		jq("#ptoStationName").html(cache.getStationNameByCode(ticket.to_station_telecode));
		jq("#ptrainReachTime").html(ticket.arrive_time + " 到达");
		if(ticket.start_station_telecode && ticket.from_station_telecode != ticket.start_station_telecode){
			jq("#pstartStationName").css("display","block");
			jq("#innerStartStation").html(cache.getStationNameByCode(ticket.start_station_telecode));
		}else{
			jq("#pstartStationName").css("display","none");
		}
		
		if(ticket.end_station_telecode && ticket.to_station_telecode != ticket.end_station_telecode){
			jq("#pendStationName").css("display","block");
			jq("#innerEndStation").html(cache.getStationNameByCode(ticket.end_station_telecode));
		}else{
			jq("#pendStationName").css("display","none");
		}
		
		if(mor.ticket.history.inputyzm&&mor.ticket.history.inputyzm!==''){
			jq('#captchaInput').val(mor.ticket.history.inputyzm);
		}
		//mod by yiguo
		ticketEditSeatBtnListeners();
	}
	jq("#passengersView").live("pagebeforeshow", function(e,data){

		//add by yiguo
		mor.ticket.viewControl.unFinishedOrderTourFlag = "";

		jq('#passengersView .iscroll-wrapper').iscrollview('refresh');
		if(mor.ticket.loginUser.isAuthenticated === "Y"){
			farePsgFn(e,data);
			}else{
				mor.ticket.util.keepPageURL();
				var ticket = mor.ticket.currentTicket;
				// 提前预处理。
				processSeatTypeInCurrentTicket(ticket);

				var cache = mor.ticket.cache;
				//var train_date = ticket.train_date;
				jq("#pfromStationName").html(cache.getStationNameByCode(ticket.from_station_telecode));
				jq("#ptrainStartTime").html(ticket.start_time + " 出发");
				jq("#ptrainCodeName").html(ticket.station_train_code);
				jq("#ptrainDurationTime").html(util.getLiShiStr(ticket.lishi));
				jq("#ptoStationName").html(cache.getStationNameByCode(ticket.to_station_telecode));
				jq("#ptrainReachTime").html(ticket.arrive_time + " 到达");
				if(ticket.start_station_telecode && ticket.from_station_telecode != ticket.start_station_telecode){
					jq("#pstartStationName").css("display","block");
					jq("#innerStartStation").html(cache.getStationNameByCode(ticket.start_station_telecode));
				}else{
					jq("#pstartStationName").css("display","none");
				}
				
				if(ticket.end_station_telecode && ticket.to_station_telecode != ticket.end_station_telecode){
					jq("#pendStationName").css("display","block");
					jq("#innerEndStation").html(cache.getStationNameByCode(ticket.end_station_telecode));
				}else{
					jq("#pendStationName").css("display","none");
				}
				
				if (window.ticketStorage.getItem("autologin") != "true") {
					autologinFailJump()
			} else {
				registerAutoLoginHandler(function(){farePsgFn(e,data)}, autologinFailJump);
			}

			
		}

		

	});

	// 购票用户身份控制
	function idTypeControl(){
		if(mor.ticket.loginUser.id_type == "1" || mor.ticket.loginUser.id_type == "2"){
			jq("#idTypeSelect").html('<option value="1" selected>二代身份证</option>'+
          		'<option value="2">一代身份证</option>'+
         		'<option value="C">港澳通行证</option>'+
          		'<option value="G">台湾通行证</option>'+
          		'<option value="B">护照</option>');
		}else{
			jq("#idTypeSelect").html('<option value="C" selected>港澳通行证</option>'+
	          		'<option value="G">台湾通行证</option>'+
	          		'<option value="B">护照</option>');
		}

	}

	function getPassengerList(type) {
		type = type || mor.ticket.viewControl.bookMode;
		var ticket = mor.ticket.currentTicket;

		if (type === 'gc') {
			return ticket.gcPassengerList || (ticket.gcPassengerList = mor.ticket.queryOrder.getGcPassengers());
		}

		if (type === 'fc') {
			return ticket.fcPassengerList || (ticket.fcPassengerList = mor.ticket.orderManager.getFcPassengers());
		}

		return mor.ticket.passengerList;
	}
	// 展示用户席位选择列表
	function showPassengersFunHandher(type){
		type = type || mor.ticket.viewControl.bookMode;
		var passengerList = getPassengerList(type);
		var ticket = mor.ticket.currentTicket;
	    var util = mor.ticket.util;
	    var temp = '';
	    jq.each(passengerList, function(i, passenger) {
			var ticketTypeName = util.getTicketTypeName(passenger.ticket_type);
			passenger.seat_type = ticket.getDefaultSeatType(passenger.seat_type);
			var seat_name = util.repalceYpInfoType(ticket.getTypeName(passenger.seat_type));
			temp+='<div class="car-peop-list" style="overflow:hidden">';
			temp+='<div class="car-peop-list-name text-ellipsis" style="width:70px">' + (passenger.user_name) + '</div>';
			temp+='<div class="car-peop-list-numb">' + (passenger.id_no) + '</div>';
			temp+= getPassengerSeatElem(type, seat_name);

			var displayStyle = ' style="filter: Alpha(Opacity=0);cursor: pointer;-webkit-appearance: none;opacity: 0;" ';
			var disableStyle = ' style="filter: Alpha(Opacity=0);-webkit-appearance: none;opacity: 0;" ';
			var seatTypes = ticket.seat_types;
			var xbDisabled = ' '; // 席别 disabled
			var xbStyle = displayStyle;
			var zwDisabled = ' '; // 座位 disabled
			var zwStyle = displayStyle;
			// 如果
			if (type == 'fc') {
				zwDisabled = ' disabled ';
				zwStyle = disableStyle;
			}

			if (type == 'gc') {
				xbDisabled = ' disabled ';
				zwDisabled = ' disabled ';
//				zwStyle = disableStyle;
//				xbStyle = disableStyle;
                zwStyle = xbStyle = ' style="display:none;"';
			}

			temp+='<select data-index="'+i+'"' + xbDisabled + 'class="changeyplistType" ' + xbStyle + ' name="席别">';
			jq.each(seatTypes, function(index, typeId) {
				temp += '<option ';
				if (passenger.seat_type == typeId) {
					temp += 'selected="selected"';
				}
				temp += ' value="'+ typeId +'">'+util.repalceYpInfoType(ticket.getTypeName(typeId))+'</option>';

			});
			temp+='</select>';

			temp+='</div>';
			temp+='<div class="car-peop-list-pz">';

			if (zwStyle == disableStyle) {
				temp += ticketTypeName;
			} else {
                var ticketTypeColor= (type == 'gc') ? ' style="color:#000;" ' :"";
				temp += '<span class="seat text-ellipsis" '+ ticketTypeColor +'>'+ticketTypeName+'</span>';
			}

			temp+='<select data-index="'+i+'"' + zwDisabled + 'class="changeSeatType" ' + zwStyle + ' name="车票类型">"';
			temp+='';

			var ticketTypeList = [["1","成人票"],["2","儿童票"],["3","学生票"],["4","残军票"]];

			if (passenger.ticket_type!=3){
				ticketTypeList.splice(2,1);
			}

			jq.each(ticketTypeList, function(index, ticketType) {
				temp += '<option ';
				if (passenger.ticket_type == ticketType[0]) {
					temp += 'selected="selected"'  ;
				}
				temp += ' value="' + ticketType[0]+'">' +ticketType[1] + '</option>';
			});

			temp += '</select>';
			temp += '</div>';

			if (type === 'fc') {
				temp+='<div class="car-peop-list-input" data-index="'+i+'" ><input id=\"list_'+i+'\" type=\"checkbox\"  class="bookBackTicketCheckBox"  checked value=\"'+i+'\"></div>';
			} else if (type === 'gc') {
				temp+='<div class="car-peop-list-input" style="position:relative;" data-index="'+i+'" >' +
                    '<label style="position: absolute;top:0;right:0;bottom:0;left:0;z-index: 10;" for=\"list_'+i+'\"></label>'+
                    '<input id=\"list_'+i+'\" type=\"checkbox\"  class="changeTicketCheckBox"  checked value=\"'+i+'\">' +
                    '</div>';
			} else {
				temp+='<div class="car-peop-list-cz" data-index="'+i+'" ><a></a></div>';
			}

			temp+='</div>';
		});
		jq(".car-peop").html(temp);
		setTimeout(function(){refrechSelect();contentIscrollRefresh();},20);

		return;
	}
	// add by yiguo 用来在选择框生成后为其绑定相应的滚动功能，方式键盘呼出时被navbar被顶上去
	function refrechSelect(){
		// add by yiguo 2013.11.18
		if(util.isIPhone()){
			util.enableAutoScroll('.changeSeatType', focusArray);
			util.enableAutoScroll('.changeyplistType', focusArray);
		}
		// add by yiguo 2013.11.18
		//jq(".changeSeatType").selectmenu('refresh', true);
		//jq(".changeyplistType").selectmenu('refresh', true);
		//mor.ticket.util.bindSelectFocusBlurListener(".changeSeatType");
		//mor.ticket.util.bindSelectFocusBlurListener(".changeyplistType");

	}

	// 主要是用于改签的时候进行区分， 改签的时候座位不可改。
	function getPassengerSeatElem(type, seat_name) {
		var temp = '';
		temp+='<div class="car-peop-list-zx"><span class="seat text-ellipsis"';
		if (type === 'gc') {
			 temp += ' style="color:black;width:35px"';
		} else {
			temp += ' style="width:35px"';
		}
		return temp + '>' + seat_name +'</span>';;
	}

	// 删除
	function ticketDeletePassengerBtnListeners(){
		jq(".car-peop").off('tap.deletePassenger').on("tap.deletePassenger", ".car-peop-list-cz",function(e){
			var that = jq(this);
			var data_index = that.attr("data-index");
			if (data_index!=undefined ||  data_index!=''){
					 WL.SimpleDialog.show(
						"温馨提示", "确定删除该乘客吗?",
						 [{text: "取消", handler: function(){
							return false;
						} },
						 {text: "确认", handler: function(){
							  mor.ticket.passengerList.splice(data_index,1);
							  showPassengersFunHandher();
							  updateButtonState();
							  return;
						}}]
					);
				 }
			});

		// 选择人员类型
		jq(".changeSeatType").live("change", function(e){
			  e.stopImmediatePropagation();
			  var data_index  = jq(this).attr("data-index");
			  var data_value  = jq(this).attr("value");
			  if (data_index!=undefined ||  data_index!=''){
				  getPassengerList()[data_index]['ticket_type'] = data_value;
				  var that = this;
				  jq(that).trigger("blur");
				  setTimeout(function(){
					  jq(that).parent().find('.seat').html(that.options[that.selectedIndex].innerHTML);
					  //contentIscrollRefresh();

				  }, 20);
				return;
			 }
		});

		// 选择座席
		jq(".changeyplistType").live("change", function(e){
			  e.stopImmediatePropagation();
	   		  var data_index  = jq(this).attr("data-index");
			  var data_value  = jq(this).attr("value");
			  if (data_index!=undefined ||  data_index!=''){

				getPassengerList()[data_index]['seat_type'] = data_value;
				var that = this;
				  jq(that).trigger("blur");
				setTimeout(function(){
					jq(that).parent().find(".seat").html(that.options[that.selectedIndex].innerHTML);
					//contentIscrollRefresh();
                },20);
				return;
			 }
		});
	}

	
	//add by yiguo 
	function  psgViewAddPsg(){
		jq("#TicketAddPassengerBtn").off();
		            
	    jq("#selectFarePassengerView").remove();
		mor.ticket.history.url='fareList';
		mor.ticket.history.inputyzm=jq('#captchaInput').val();
		mor.ticket.util.keepPageURL();
		jq.mobile.changePage(vPathCallBack()+"fareList.html");
		setTimeout(function(){
	       	 jq("#TicketAddPassengerBtn").off().on("tap",psgViewAddPsg)
	        },1000);
		return false;
	}
	
	
	
	// 跳转
	function ticketAddPassengerBtnListeners(){
		jq("#TicketAddPassengerBtn").off().on("tap", psgViewAddPsg);
	}


	// 监控修改座席类型
	function ticketEditSeatBtnListeners(){
		jq(".editBtnSeat").off().bind("tap", function(e){
			var that = jq(this);
			if (that.hasClass('disabled')) return;
			var data_val = that.attr("data-val");
			jq.each(getPassengerList(), function(i, passenger) {
			    passenger.seat_type = data_val;
			});
			changeDefaultSeatType(data_val);
			jq('.editBtnSeat').removeClass('hover');
			that.addClass('hover');
		});
	}

	// 修改乘客系别类型.
	function changeDefaultSeatType(seat_type) {
		jq('.car-peop .changeyplistType').val(seat_type);
		var ticket = mor.ticket.currentTicket;
		var util = mor.ticket.util;
		setTimeout(function() {
			jq('.car-peop-list-zx .seat').html(util.repalceYpInfoType(ticket.getTypeName(seat_type)));
		}, 20)

		ticket.default_seat_type = seat_type;
		mor.ticket.default_seat_type = seat_type;
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

			// 判断如果当前的模式是针对于修改联系人的情况，不应该显示追加联系人按钮
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
			// 判断如果当前的模式是针对于修改联系人的情况，不应该显示追加联系人按钮
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
			// 判断当前的旅客类型是否和车票类型相符，只有学生才能买学生票
			// currentPassenger变量不要在其他业务逻辑中使用
			if(mor.ticket.currentPassenger.ticket_type!='3'&&jq(this).val()=='3'&&mor.ticket.currentPassenger.ticket_type!=undefined){
				// util.alertMessage("tt"+mor.ticket.currentPassenger.ticket_type);
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
            e.stopImmediatePropagation();
            e.stopPropagation();
            e.preventDefault();
			jq.mobile.changePage("searchSingleTicketResults.html");
			return false;
		});



		jq("#refreshCaptcha").off().bind("tap", mor.ticket.orderManager.refreshCaptcha);

		jq("#waitTimeOKbtn").off().on("tap",function(){
			jq("#counter_line").hide();
			mor.ticket.viewControl.tab1_cur_page = vPathViewCallBack()+"MobileTicket.html";
//			var intervalId = mor.ticket.poll.intervalId;
//			if(intervalId != ""){
//				mor.ticket.poll.timer.stop();
//				clearInterval(mor.ticket.poll.intervalId);
//			}
//			// 如果当前的mode为gc那么在改签提交订单后更改mode为dc
//			if(mor.ticket.viewControl.bookMode == "gc"){
//				mor.ticket.viewControl.bookMode = "dc";
//			}
//			//mod by yiguo
//			//jq.mobile.changePage("queryOrder.html");
//			jq.mobile.changePage("orderList.html");

			var intervalId = mor.ticket.poll.intervalId;
			if(intervalId != ""){
				mor.ticket.poll.timer.stop();
				clearInterval(mor.ticket.poll.intervalId);
			}
			window.plugins.childBrowser.closeDialog();
			//如果当前的mode为gc那么在改签提交订单后更改mode为dc
			if(mor.ticket.viewControl.bookMode == "gc"){
				mor.ticket.viewControl.bookMode = "dc";
			}

			mor.ticket.viewControl.tab1_cur_page = vPathViewCallBack()+"MobileTicket.html";
			//mod by yiguo
			//jq.mobile.changePage("queryOrder.html");
			
			if (!busy.isVisible()) {
				busy.show();
			}

			window.pullOrderData(function(result){
				if(busy.isVisible()){
					busy.hide();
				}
				//mod by yiguo

				//TODO get current order index & jump to orderlist.html
				 var invocationResult = result.invocationResult;
			        if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			        	if (invocationResult.orderList.length == 0) {
			        		jq.mobile.changePage("queryOrder.html");
			     			setTimeout(function(){
			     				jq("#refreshUnfinishedorder").trigger("tap");
			     			},1000);
			        		return;
			        	}
			            var queryOrder = mor.ticket.queryOrder;
			            //init queryOrder queryOrderList
			            queryOrder.queryOrderList = [];
			            queryOrder.originPaidQrderList = [];

			            queryOrder.setUnfinishedOrderList(invocationResult.orderList);
			            //displayNoFinishedOrders();


			            var noFinishedOrderList = queryOrder.queryOrderList;


			            if (noFinishedOrderList && noFinishedOrderList[0]) {

			                queryOrder.hasChangeTicket();

			            }



			            	// init queryOrder content
			            	queryOrder.currentQueryOrder = {};
			            	queryOrder.currentUnfinishOrderIndex = noFinishedOrderList.length -1;
			            	mor.ticket.queryOrder.setCurrentUnfinishedOrders(queryOrder.currentUnfinishOrderIndex);
			            	jq.mobile.changePage("orderList.html",{reloadPage:true,transition:"none"});



			            //jq.mobile.changePage("orderList.html",{reloadPage:true});
			        } else {
			            mor.ticket.util.alertMessage(invocationResult.error_msg);
			        }


				//jq.mobile.changePage(vPathCallBack() + "orderList.html");

			},
			function(){
				if(busy.isVisible()){
					busy.hide();
				}
				util.alertMessage("订单状态获取失败，暂时无法转入订单查询页面！");
			});


			return false;
		});


		jq("#submitOrderBtn").off().on("tap", function(){
			// mjl
			// by yiguo
			// 判断当前用户是否为激活用户，未激活用户不可以提交订单
			if (mor.ticket.loginUser.activeUser != 'Y') {
				mor.ticket.util.alertMessage("当前用户未激活，请到您的注册邮箱收取12306激活邮件后按提示激活用户后再登录！");
				return false;
			}
			if(mor.ticket.viewControl.bookMode=="fc"&&
				jq('#passengersView .bookBackTicketCheckBox:visible:checked').length==0){
				mor.ticket.util.alertMessage("请选择需要购买返程车票的乘车人！");
				return false;
			}
			if(getPassengerList().length>=1 ){

				if(jq("#captchaInput").val() === ""){
					mor.ticket.util.alertMessage("请输入验证码!");
					return false;
				}
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
								var length = getPassengerList().length;
								orderManager.setTotalTicketNum(length);
								var parameters=prepareSubmitOrderParameters();
								orderManager.confirmPassengerInfo(parameters);
								return;
						  }}]
					);
				}
			} else{
				mor.ticket.util.alertMessage("请至少选择一位乘客!");
			}
			return false;
		});

		/*
		 * jq("#candidateList").on("tap", "a", function(){
		 * //jq("#seatTypeSelectBack").scroller('show');
		 * //jq("#seatTypeSelectBack").tap();
		 * //jq("#seatTypeSelectBack").selectmenu('open'); changeSeatTypeIndex =
		 * jq(this).parents('.passengerRecord').index(); return false; });
		 *
		 * jq("#seatTypeSelectBack").bind("change",function(){ var passengerList =
		 * mor.ticket.passengerList;
		 * passengerList[changeSeatTypeIndex].seat_type = jq(this).val();
		 * jq('.passengerInfoHead').eq(changeSeatTypeIndex).children('#seatType').html(mor.ticket.util.getSeatTypeName(jq(this).val()));
		 * return false; });
		 *
		 * jq("#candidateList").on("change",".changeSeatType",function(e){ var
		 * passengerList = mor.ticket.passengerList; changeSeatTypeIndex =
		 * jq(this).parents('.passengerRecord').index();
		 * passengerList[changeSeatTypeIndex].seat_type = jq(this).val();
		 * e.stopImmediatePropagation(); return false; });
		 */

		jq("#submitChangeTicketBtn").off().on("tap",function(){
			// by yiguo
			// 判断当前用户是否为激活用户，未激活用户不可以提交订单
			if (mor.ticket.loginUser.activeUser != 'Y') {
				mor.ticket.util.alertMessage("当前用户未激活，请到您的注册邮箱收取12306激活邮件后按提示激活用户后再登录！");
				return false;
			}
			// 必须勾选一个改签票
			if(jq('#passengersView .changeTicketCheckBox:visible:checked').length==0){
				mor.ticket.util.alertMessage("请勾选需要改签的车票！");
				return false;
			}

			// 改签时必须选择相同座位
			var passengerList = getPassengerList();
			var seat_array = [];

			jq.each(passengerList, function(i, passenger) {
				if (jq(".car-peop-list:eq("+i+") .changeTicketCheckBox").attr("checked") == "checked"){
					seat_array.push(passenger.seat_type);
				}
			});
			jq.unique(seat_array);
			if(seat_array.length>1){
				mor.ticket.util.alertMessage("改签时，必须选择相同席别！");
				return false;
			}

			// 增加验证码的校验
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

	// 提前的预处理方法。
	// 对无座票进行处理，不显示无座，按照id显示座位类型.也就是显示硬座或者一等座/二等座
	// 同时在数组中删除无座的成员，无座的情况按照rate = 100判断。
	// 如果只有无座的票，那么按照真实的席别显示
	// TODO 确定每次页面进入时， currentTicket 能够初始化确定的值.
	function processSeatTypeInCurrentTicket(ticket){
		// 只处理一次.
		var cache = mor.ticket.cache;
		var yplist = ticket.yplist;

		// 通过 type_id type_rate 来把type_id 给映射为中文信息
		// 通过再把无座的 type_id 后面添加一位？然后所有需要通过 type_id 来计算的都需要经过这个
		// 映射才能进行后续的处理.
		var seatTypeMapping = ticket.seatTypeMapping = {};
		var type_ids = ticket.seat_types = [];// 用于储存席别数组

		jq.each(yplist, function(i, yp) {
			var type_id = yp.type_id.replace(/_none/, '');

			yp.type = cache.getSeatTypeByCode(type_id);

			if (yp.type_rate == 100) {
				yp.type_alias = '无座';
				// 保存无票的标示.
				if (!/_none/.test(yp.type_id)) {
					yp.type_id = yp.type_id + '_none';
				}
			}
			seatTypeMapping[yp.type_id] = {
			    type: yp.type,
			    alias: yp.type_alias
			};

			if(yp.num){
				type_ids.push(yplist[i].type_id);
			}
		});

		// 获取系统类型
		ticket.getSeatType = function(id) {
			return id.replace('_none', '');
		};

		// 获取票的详细信息.
		ticket.getType = function(id) {
			return seatTypeMapping[id];
		};

		ticket.getTypeName = function(id) {
			var type = ticket.getType(id);
			return type.alias || type.type;
		};

		var default_seat_type;// 获取默认的席别编号
		var mode = mor.ticket.viewControl.bookMode;
		if(mode === "fc"){
			default_seat_type = mor.ticket.leftTicketQuery.seatBack_Type;
		}else{
			default_seat_type = mor.ticket.leftTicketQuery.seat_Type;
		}

		ticket.getDefaultSeatType = function(id) {
			// 如果用户指定的类型存在用户可以用的席别列表，那就返回.
			if (id && type_ids.indexOf(id) > -1) {
				return id;
			} else if (ticket.default_seat_type) {
				// 如果存在默认的系别列表就返回默认的。
				return ticket.default_seat_type;
			} else {
				// 最后倒序选择第一个非无座的座位.
				var firstNoneSeat = type_ids[type_ids.length - 1];
				var sid;
				for (var len = type_ids.length - 1; len > -1; len--) {
					sid = type_ids[len];
					if (!/_none/.test(sid)) {
						firstNoneSeat = sid;
						break;
					}
				}
				return firstNoneSeat;
			}
		};

		// default_seat_type 用来保存用户选中的席别
		ticket.default_seat_type = ticket.getDefaultSeatType(ticket.default_seat_type || default_seat_type);
	}


	function updateButtonState(){
		// var submitDiv = jq("#orderSubmitDiv"), tip = jq('#passengersView .tips3');
		// if (getPassengerList().length > 0 ){
		// 	submitDiv.show();
		// 	//tip.show();
		// } else {
		// 	tip.hide();
		// }
		if(typeof mor.ticket.history.checkCodeImg === 'undefined'){
			mor.ticket.orderManager.refreshCaptcha2();
		}else{
			jq('#captchaImg').attr('src',mor.ticket.history.checkCodeImg);
		}
		contentIscrollRefresh();
	}


	function validationForSubmit(){
		var util = mor.ticket.util;
		var passengers = getPassengerList();
		var mode = mor.ticket.viewControl.bookMode;
		var model = mor.ticket.leftTicketQuery;
		// 一笔订单，不允许都是小孩票，必须至少绑定一张成人票
		if( mode == "dc" || mode == "wc" || mode == "fc")  {
			var childFlag = false;
			var adultFlag = false;
			for (var i = 0; i < passengers.length; i ++ ){
				var passenger = passengers[i];
				if(model.purpose_codes == '0X' && passenger.ticket_type != "3"){
					util.alertMessage("您选择的是学生票类型,请重新选择乘车人。");
					break;
				}
				if(model.purpose_codes == '1F' && passenger.ticket_type == "3"){
					util.alertMessage("您选择的是农民工类型,请重新选择乘车人。");
					break;
				}
				if(model.purpose_codes == '1F' && passenger.id_type != "1"&& passenger.id_type != "2"){
					util.alertMessage("您选择的是农民工类型,乘车人证件只能是一,二代身份证。");
					break;
				}
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
		var mode = mor.ticket.viewControl.bookMode;
		var passengers = getPassengerList(mode);
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

		jq.each(getPassengerList(), function(i, passenger) {
			var seat_type = ticket.getSeatType(passenger.seat_type);

			if(mode === "fc") {
				if (jq(".car-peop-list:eq("+i+") .bookBackTicketCheckBox").attr("checked") == "checked"){
					seatTypes.push(seat_type);
					ticketTypes.push(passenger.ticket_type);
					passenger_id_types.push(passenger.id_type);
					passenger_id_nos.push(passenger.id_no);
					passenger_names.push(passenger.user_name);
				}
			}else {
				seatTypes.push(seat_type);
				ticketTypes.push(passenger.ticket_type);
				passenger_id_types.push(passenger.id_type);
				passenger_id_nos.push(passenger.id_no);
				passenger_names.push(passenger.user_name);
				mobile_nos.push(passenger.mobile_no);
				save_passenger_flag.push("Y");
			}
		});

		jq.extend(parameters, {
			"seat_type_codes": seatTypes.join(","),
			"ticket_types": ticketTypes.join(","),
			"passenger_id_types": passenger_id_types.join(","),
			"passenger_id_nos": passenger_id_nos.join(","),
			"passenger_names": passenger_names.join(","),
			"ticket_type_order_num": mor.ticket.passengerList.length.toString(),
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
				"save_passenger_flag": save_passenger_flag.join("Y,")
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

		jq.each(getPassengerList(), function(i, passenger) {
			if (jq(".car-peop-list:eq("+i+") .changeTicketCheckBox").attr("checked") == "checked"){
				var seat_type = ticket.getSeatType(passenger.seat_type);
				seatTypes.push(seat_type);
				ticketTypes.push(passenger.ticket_type);
				passenger_id_types.push(passenger.id_type);
				passenger_id_nos.push(passenger.id_no);
				passenger_names.push(passenger.user_name);
			}
		});

		var originTicket = mor.ticket.queryOrder.changeTicketOrderList;

		for(var i=0;i<originTicket.length;i++){
			if (jq(".car-peop-list:eq("+i+") .changeTicketCheckBox").attr("checked") == "checked"){
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

	// 生成用户系别选择列表
	function regsiterYpListFunHandher(){
		var boxTemp = "";
		var ticket = mor.ticket.currentTicket;

		var yplist = ticket.yplist;
		var defaultSeatType = ticket.getDefaultSeatType(mor.ticket.default_seat_type);

		for (var i = 0, len = yplist.length ; i < len ; i++) {
			var yp = yplist[i],
				color = 'black',
				ticketNum = '无',
				num = yp.num,
				price = yp.price,
				disabled = ' disabled',
				seat_class = ' editBtnSeat';

	        if (defaultSeatType === yp.type_id) {
	        	seat_class = ' editBtnSeat hover';
	        }

			if (num > 0) {
				disabled = '';

				if (num <= 20) {
					ticketNum = num +"张";
					color="black";
				} else {
					ticketNum = "有票";
					color="green";
				}
			}

			// 获取实际的 type 名称，包括无座.
			var typeName = ticket.getTypeName(yp.type_id);
			// 简化显示信息.
			var typeInfo = mor.ticket.util.repalceYpInfoType(typeName); // 用于获取车票信息简称
			/**
			 * if (typeName === '无座') { ticketNum = yp.type + ticketNum; }
			 */

			var lastDivClass;
			if (i === len -1) {
				lastDivClass = ' car-box-last';
			} else {
				lastDivClass = ' ';
			}

			var firstDivClass;
			if (i === 0) {
				firstDivClass = ' car-box-first ';
			} else {
				firstDivClass = ' ';
			}

			// 生成席位切换区域.
			boxTemp += '<div class="'+lastDivClass+firstDivClass+' car-box-five car-box-' + len  +
						'"><a data-val="' + yp.type_id + '"  class="'+ seat_class + lastDivClass + firstDivClass + disabled +
						'" ><span class="car-txt-zw text-ellipsis" style="width:40px">'+ typeInfo +
						'</span><br /><span class="car-txt-num '+color+'">' + ticketNum +
						'</span><br /><span class="car-txt-jg">'+price.substr(0, price.length-1)+'</span></a></div>';
		 }
		 jq(".car-box-list").html(boxTemp);
		 return;
    }
})();