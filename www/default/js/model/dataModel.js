
/* JavaScript content from js/model/dataModel.js in folder common */
(function() {

	jq.extendModule("mor.ticket.registInfo", {
		"user_name" : "",
		"succ_flag" : "",
		"error_msg" : "",
		"name" : "",
		"id_type_code" : "",
		"country_code" : "",
		"address" : "",
		"user_type" : "",
		"id_no" : "",
		"mobile_no" : "",
		"phone_no" : "",
		"email" : "",
		"born_date" : "",
		"ivr_passwd" : "",
		"postalcode" : "",
		"pwd_question" : "",
		"sex_code" : "",
		"student_province_code" : "",
		"student_school_code" : "",
		"student_department" : "",
		"student_school_class" : "",
		"student_student_no" : "",
		"student_enter_year" : "",
		"student_school_system" : "",
		"student_from_station_code" : "",
		"student_to_station_code" : "",
		"student_card_no" : "",
		"pass_code" : ""
	});
	jq.extendModule("mor.ticket.loginUser", {
		"username" : "",// need to change to loginName to prevent
						// confusing,could be email or account name
		"password" : "",
		"isKeepUserPW" : true,
		"isAuthenticated" : "N",
		"id_type" : "",
		"id_no " : "",
		"email " : "",
		"mobile_no " : "",
		"user_type" : "",
		"realName" : "", // real name
		"accountName" : "", // 12306 account name
		"activeUser" : "Y"
	});

	jq.extendModule("mor.ticket.userInfo", {
		"user_name" : "",
		"succ_flag" : "",
		"error_msg" : "",
		"name" : "",
		"id_type_code" : "",
		"country_code" : "",
		"address" : "",
		"user_type" : "",
		"id_no" : "",
		"mobile_no" : "",
		"phone_no" : "",
		"email" : "",
		"born_date" : "",
		"ivr_passwd" : "",
		"postalcode" : "",
		"pwd_question" : "",
		"sex_code" : "",
		"student_province_code" : "",
		"student_school_code" : "",
		"student_department" : "",
		"student_school_class" : "",
		"student_student_no" : "",
		"student_enter_year" : "",
		"student_school_system" : "",
		"student_from_station_code" : "",
		"student_to_station_code" : "",
		"student_card_no" : "",
		"pass_code" : ""
	});

	jq.extendModule("mor.ticket.passengerDetail", {
		"name" : "",
		"old_name" : "",
		"sex_code" : "",
		"born_date" : "",
		"country_code" : "",
		"card_type" : "",
		"old_card_type" : "",
		"card_name" : "",
		"card_no" : "",
		"old_card_no" : "",
		"passenger_type" : "",
		"mobile_no" : "",
		"phone_no" : "",
		"email" : "",
		"address" : "",
		"postalcode" : "",
		"province_code" : "",
		"school_code" : "",
		"department" : "",
		"school_class" : "",
		"student_no" : "",
		"enter_year" : "",
		"school_system" : "",
		"preference_from_station_code" : "",
		"preference_to_station_code" : "",
		"preference_card_no" : ""
	});



	jq.extendModule("mor.ticket.passenger", {
      result : ""
	});

	jq.extendModule("mor.ticket.seat_Type", {
      result : []
	});
	
	jq.extendModule("mor.ticket.yplist", {
      result : []
	});
	
	// 存储来源路径
	jq.extendModule("mor.ticket.history", {
       "url" : ""
	});
	// 存储票类型
	jq.extendModule("mor.ticket.ticketTypeList", {
      result : []
	});
	jq.extendModule("mor.ticket.reservePeriodList",{
		result : []
	});
	// 存储席
	jq.extendModule("mor.ticket.seat_Type", {
       result : []
	});


	// 存储席
	jq.extendModule("mor.ticket.seat_Type_index", {
       result : []
	});




	jq.extendModule("mor.ticket.leftTicketQuery", {
		from_station_telecode : "",
		to_station_telecode : "",
		seat_Type : "",
		purpose_codes : '00',
		seatBack_Type : "",
		train_date : "", // YYYY-MM-DD
		train_date_type : 0,
		time_period : '0',
		station_train_code : "",
		train_headers : "",
		train_flag : "",
		/* back ticket */
		from_station_telecode_back : "",
		to_station_telecode_back : "",
		train_date_back : "",
		time_period_back : 0,
		station_train_code_back : "",
		result : []
	});

	jq.extendModule("mor.ticket.currentTicket", {
		arrive_time : "",
		end_station_telecode : "",
		from_station_telecode : "",
		lishi : "",
		start_station_telecode : "",
		start_time : "",
		train_date : "", // YYYY-MM-DD
		train_date_back : "",
		station_train_code : "",
		to_station_telecode : "",
		train_class_name : "",
		yp_info : "",
		yplist : []
	});

	jq.extendModule("mor.ticket.currentPassenger", {
		user_name : "", // real name
		id_type : "",
		id_no : "",
		mobile_no : "",
		ticket_type : "",
		seat_type : ""
	});

	jq.extendModule("mor.ticket.passengerList", {
		seat_type:"",
		id_type:"",
		user_type:"",
		ticket_type:"",
		id_no:"",
		user_name:"",
		seat_type:"",
		mobile_no:""			
	});


	// ticket order after confirm
	jq.extendModule("mor.ticket.confirmTicket", {
		sequence_no : "",
		batch_no : "",
		coach_name : "",
		coach_no : "",
		distance : "",
		limit_time : "",
		lose_time : "",
		passenger_id_no : "",
		passenger_id_type_code : "",
		passenger_name : "",
		pay_limit_time : "",
		pay_mode_code : "",
		reserve_time : "",
		seat_name : "",
		seat_no : "",
		seat_type_code : "",
		ticket_price : "",
		ticket_type_code : "",
		station_train_code : "",
		ticket_flag : false
	// indicate if back ticket for round trip
	});

	/*
	 * Go to js/controlller/orderManager for detail
	 * jq.extendModule("mor.ticket.orderManager", {
	 * 
	 * });
	 */

	jq.extendModule("mor.ticket.payment", {
		'interfaceName' : '',
		'interfaceVersion' : '',
		'tranData' : '',
		'merSignMsg' : '',
		'appId' : '',
		'transType' : '',
		'epayurl' : '',
		'batch_nos' : []
	});

	jq.extendModule("mor.ticket.passengersCache", {
		passengers : [],
		sortPassengers : function() {
			var passengers = mor.ticket.passengersCache.passengers;
			var newpassengers = [];
			var user_passenger = [];
			var user = mor.ticket.loginUser;
			// 添加拼音字段
			
		//	WL.Logger.debug("Successfully  1");
			
			
		
			
			
			for ( var i = 0; i < passengers.length; i++) {
				if(passengers[i] != null){
				newpassengers[i] = {};
				newpassengers[i]["checked"]   = 0;
				newpassengers[i]["seat"]      = "";
				newpassengers[i]["user_name"] = passengers[i].user_name;
				newpassengers[i]["id_type"] = passengers[i].id_type;
				newpassengers[i]["id_no"] = passengers[i].id_no;
				newpassengers[i]["mobile_no"] = passengers[i].mobile_no;
				newpassengers[i]["user_type"] = passengers[i].user_type;
				newpassengers[i]["id_type"] = passengers[i].id_type;
				newpassengers[i]["user_nameSM"] = jq("#py").val(
						passengers[i].user_name).toPinyin();
				}
			}
			
		//	WL.Logger.debug("Successfully  2");
			// 取出当前用户
			for ( var i = 0; i < newpassengers.length; i++) {
				if (newpassengers[i].user_name == user.realName
						&& newpassengers[i].id_type == user.id_type
						&& newpassengers[i].id_no == user.id_no) {
					user_passenger = newpassengers[i];
					newpassengers.splice(i, 1);
					break;
				}
			}
		//	WL.Logger.debug("Successfully  3");
			// 对newpassengers按拼音排序
			mor.ticket.util.sortPassengers(newpassengers);
			// 重组数据 当前用放第一位
		//	WL.Logger.debug("Successfully  4");
			passengers = [];
			for ( var i = 0; i < newpassengers.length + 1; i++) {
				if (i == 0) {
					passengers[0] = user_passenger;
				} else {
					passengers[i] = newpassengers[i - 1];
				}
			}
			mor.ticket.passengersCache.passengers = passengers;
		
		return mor.ticket.passengersCache.passengers;
		
		}
	});

	jq.extendModule("mor.cache.seatTypeMap", {});

	jq.extendModule("mor.cache.ticketTypeMap", {});

	jq.extendModule("mor.cache.idTypeMap", {});

	jq.extendModule("mor.cache.stationMap", {});

	jq.extendModule("mor.cache.countryMap", {});

	jq.extendModule("mor.cache.provinceMap", {});

	jq.extendModule("mor.cache.universityMap", {});

	jq.extendModule("mor.cache.cityMap", {});

	jq.extendModule("mor.ticket.common", {
		'baseDTO.os_type' : "a",
		'baseDTO.device_no' : "abcdaabbccdd",
		'baseDTO.mobile_no' : "123444",
		'baseDTO.time_str' : "20120430",
		'baseDTO.time_offset' : 0,
		'baseDTO.check_code' : "20120430",
		'baseDTO.version_no' : "1.1"
	});

	jq.extendModule("mor.ticket.cache", {
		needSync : "N",
		syncList : "",
		syncVersionList : "",
		station_version : "Dec 25 2013  3:20PM",
		hotStation_version : 1.9,
		city_version : "Dec 21 2013  1:01PM",
		university_version :"Dec 21 2013  5:00PM",
		stations : [],
		seats : [],
		getStationNameByCode : function(id) {
			return mor.cache.stationMap[id];
		},
		getSeatTypeByCode : function(id) {
			return mor.cache.seatTypeMap[id];
		},
		getSeatTypeRateByCode : function(id) {
			return mor.cache.seatTypeRateMap[id];
		},
		getSeatTypeByCode : function(id) {
			return mor.cache.seatTypeMap[id];
		},
		passengers : []
	});

	jq.extendModule("mor.ticket.viewControl", {
		tab1_cur_page : "",
		tab2_cur_page : "",
		tab3_cur_page : "",
		tab4_cur_page : "",
		current_tab : "",

		// 登录超时页面记录
		session_out_page : "",
		// show busy indicator
		show_busy : true,
		// 订票类型：单程"dc"、往程"wc"、返程"fc"、改签"gc"
		bookMode : "dc",
		// 查询页面，查询类型：0今日订单，1表现7日内订单，2表示高级查询订单
		queryFinishedOrderType : "",

		// 找回密码的方式 email表示通过邮件 passwd表示通过密码提示
		findPwdMode : "email",

		isCityGo : false,
		isModifyPassenger : false,
		// 是否需要重新加载dom节点中的selectPassenger页面
		isNeedRefreshSelectPassengers : false,
		// 订单查询页面 刷新
		isNeedRefreshUnfinishedOrder : false,
		// 已完成订单详情页面 退票 按钮 index
		cancelTicketIndex : 0,
		// 支付成功页面状态:true 为支付完成，false 为退票完成
		isPayfinishMode : true,

		// 每次显示的第一个车站的索引
		station_num : 0,
		// 车站显示总数
		station_show_num : 30,
		// 滑动后保留车站数
		station_keep_num : 10,
		// 搜索车站页面使用导航功能
		isStationNevClick : false,
		// 上拉下拉标记
		station_is_up : true,
		// 清除搜索框标记
		station_searchClean : false,
		// 显示出的车站
		station_show : [],

		// 每次显示第一个城市的索引
		city_num : -1,
		// 未完成订单 排队查询tourFlag
		unFinishedOrderTourFlag : "",
		// 是否需要重新请求数据
		isNeedRequest : true
	});

	jq.extendModule("mor.ticket.views.selectStation", {
		isFromStation : true
	});
	jq.extendModule("mor.ticket.views.selectTrain", {
		isFromTrain : true
	});

	jq
			.extendModule(
					"mor.ticket.queryOrder",
					{
						queryOrderList : [],
						currentQueryOrder : {},
						currentUnfinishOrderIndex : 0,
						originPaidQrderList : [],
						setUnfinishedOrderList : function(orderList) {
							this.queryOrderList = orderList;
							//this.setCurrentUnfinishedOrders(0);
							return this.queryOrderList;
						},
						setCurrentUnfinishedOrders : function(index) {
							this.currentQueryOrder = this.queryOrderList[index];
						},
						getCurrentUnfinishedOrder : function() {
							return this.currentQueryOrder;
						},
						updateCurrentQueryOrder : function(orderList) {
							this.currentQueryOrder = orderList;
						},

						hasChangeTicket : function() {
							var orders = this.queryOrderList;
							var originOrderList = this.originPaidQrderList;
							var originList = [];
							for ( var i = 0; i < orders.length; i++) {
								var length = orders[i].myTicketList.length;
								for ( var j = 0; j < length; j++) {
									if (orders[i].myTicketList[j].ticket_status_code == 'j') {
										originList = this
												.splicePaidOriginTicketList(
														orders[i].myTicketList,
														i);
										break;
									}
								}
								originOrderList.push(originList);
							}
						},

						splicePaidOriginTicketList : function(ticketList, index) {
							var length = ticketList.length;
							var originOrderList = [];
							for ( var i = 0; i < length; i++) {
								if (ticketList[i].ticket_status_code == 'e') {// 状态为“改签中”则放到originOrderList
									var ticket = ticketList.slice(i, i + 1)[0];
									originOrderList.push(ticket);
									ticketList.splice(i, 1);
									i--;
									length--;
								} else if (ticketList[i].ticket_status_code != 'j') {// 状态不为“改签待支付”或者
																						// “改签中”或者则删除该ticket
									ticketList.splice(i, 1);
									i--;
									length--;
								}
							}
							return originOrderList;
						},

						getOrderPrice : function() {
							var unFinishedOrder = this.currentQueryOrder;
							var myTicketList = unFinishedOrder.myTicketList;
							var newPrice = 0;
							for ( var i = 0; i < myTicketList.length; i++) {
								newPrice += parseFloat(myTicketList[i].ticket_price);
							}
							unFinishedOrder.newTotalPrice = newPrice.toFixed(2);// 得到现在票价总金额
							var originPaidQrderList = mor.ticket.queryOrder.originPaidQrderList[this.currentUnfinishOrderIndex];
							var originPrice = 0;
							for ( var i = 0; i < originPaidQrderList.length; i++) {
								originPrice += parseFloat(originPaidQrderList[i].ticket_price);
							}
							unFinishedOrder.originTotalPrice = originPrice
									.toFixed(2);// 得到原改签票票价总金额
							// 得到应返回金额
							unFinishedOrder.returnTotalPrice = (unFinishedOrder.originTotalPrice - unFinishedOrder.newTotalPrice)
									.toFixed(2);
						},

						finishedOrderList : [],
						currentFinishQueryOrder : [],
						changeTicketOrderList : [],
						setFinishedOrderList : function(orderList) {
							this.finishedOrderList = orderList;
							return this.finishedOrderList;
						},
						pushFinishedOrderList : function(orderList) {
							for ( var i = 0; i < orderList.length; i++) {
								this.finishedOrderList.push(orderList[i]);
							}
							return this.finishedOrderList;
						},
						// 根据提供的 sequence_no 替换最新的 order 信息.
						replaceOrderBySequenceNo: function(no, order) {
							var orderList = this.finishedOrderList;
							for ( var i = 0; i < orderList.length; i++) {
								var orderInfo = orderList[i];
								if (orderInfo.sequence_no === no) {
									this.finishedOrderList.splice(i, 1, order);
									this.setCurrentFinishedOrders(i);
									return;
								}
							}
						},
						spliceChangingTicketList : function() {
							/*
							 * var orders = this.finishedOrderList; for(var
							 * i=0;i<orders.length;i++){ for(var j=0; j<orders[i].myTicketList.length;j++){
							 * if(orders[i].myTicketList[j].ticket_status_code ==
							 * 'j'){ orders[i].myTicketList.splice(j,1); } } }
							 */
						},
						setCurrentFinishedOrders : function(index) {
							this.currentFinishQueryOrder = this.finishedOrderList[index];
						},
						getCurrentFinishedOrders : function() {
							return this.currentFinishQueryOrder;
						},
						getCurrentFinishedOrdersMyTicketList : function() {
							return this.currentFinishQueryOrder.myTicketList;
						},

						initChangeTicketOrderList : function() {
							this.changeTicketOrderList = [];
						},
						setChangeTicketOrderList : function(orderList) {
							this.changeTicketOrderList.push(orderList);
						},
						
						// 获取改签的联系人列表.
						getGcPassengers: function() {
							var passengerList = [];
							jq.each(this.changeTicketOrderList, function(i, orderTicket) {
								var passenger = {};
								passenger.id_no = orderTicket.passenger_id_no;
								passenger.id_type = orderTicket.passenger_id_type_code;
								passenger.mobile_no = '';
								passenger.user_name = orderTicket.passenger_name;
								passenger.ticket_type = orderTicket.ticket_type_code;
								passenger.seat_type = orderTicket.seat_type;
								passengerList.push(passenger);
							});
							return passengerList;
						},
						cancelTicket : {},
						setCancelTicketInfo : function(result) {
							this.cancelTicket = this.currentFinishQueryOrder.myTicketList[mor.ticket.viewControl.cancelTicketIndex];
							var info = this.cancelTicket;
							info.return_cost = result.return_cost;
							info.rate = result.rate;
							info.pay_limit_time = result.pay_limit_time;
							info.realize_time_char = result.realize_time_char;
						},
						getCancelTicketInfo : function() {
							return this.cancelTicket;
						}
					});

	jq.extendModule("mor.queryOrder.views.advanceQuery", {
		fromDate : "",
		toDate : "",
		trainCode : "",
		passengerName : "",
		passengerId : "",

		isSelectTrainDate : false
	// 查询已完成订单按日期选择标记
	});

	jq.extendModule("mor.ticket.poll", {
		intervalId : "",
		endTime : "",
		timer : ""
	});

	jq.extendModule("mor.ticket.paytimer", {
		intervalId : "",
		endTime : ""
	});
})();