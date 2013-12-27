
/* JavaScript content from js/controller/bookTicket.js in folder common */
(function() {
	/*
	 * jq("#bookTicketView").live("pagecreate", function() {
	 * //mor.ticket.util.androidRemoveIscroll("#bookTicketView"); });
	 */
	var focusArray = [];
	jq("#bookTicketView").live("pageshow", function() {
		focusArray = [];
	});
	function registerAutoScroll() {
		var util = mor.ticket.util;
		if (util.isIPhone()) {
			util.enableAutoScroll('#trainDateInput', focusArray);
			util.enableAutoScroll('#trainDateInputBack', focusArray);
			util.enableAutoScroll('#trainSeatInput', focusArray);
			util.enableAutoScroll('#trainSeatInputBack', focusArray);
		}
	}

	jq("#bookTicketView").live("pagehide", function() {
		jq("#bookTicketView").css("pointer-events", "auto");
	});

	jq("#bookTicketView").live(
			"pageinit",
			function() {
				jq.mobile.defaultHomeScroll = 0;
				var user = mor.ticket.loginUser;
				user.isKeepUserPW = (window.ticketStorage
						.getItem("isKeepUserPW") == null ? true
						: window.ticketStorage.getItem("isKeepUserPW"));
				user.username = window.ticketStorage.getItem("username");
				// 后续优化
				myPrepareCacheMap();
				registerAutoScroll();

				registerchangeSeatClickHandler();
				registerExitChangeTicketBtnHandler();
				registerDoubleTripClickHandler();
				registerFromStationInputClickHandler();
				registerTrainCodeInputClickHandler();
				registerTrainDateInputBackChangeHandler();
				registerTrainCodeInputBackClickHandler();
				registerTrainHeadersBtnGroupClickHandler();
				registerSingleTripClickHandler();
				registerToStationInputClickHandler();
				registerTrainDateInputChangeHandler();
				registerQuerySingleTicketBtnClickHandler();
				registerSeatSelectChangeHandler(); // 选择席别控制
				registerSeatSelectBackChangeHandler();
				mor.ticket.passengerList = [];
				registerSelectUserType();
			});

	jq("#bookTicketView")
			.live(
					"pagebeforeshow",
					function(e, data) {
						showAddedPassager();
						mor.ticket.viewControl.current_tab = "bookTicketTab";
						mor.ticket.viewControl.tab1_cur_page = vPathViewCallBack()
								+ "MobileTicket.html";
						mor.ticket.util.contentIscrollTo(0, 0, 0);
						registerAddFare2fareListBtnClickHandler();// 添加乘客
						initSeatSelectScroller();
						initTimePeriodSelectScroller();
						util = mor.ticket.util;

						util.syncDef = util.syncDef || jq.Deferred();
						util.syncDef
								.then(function(serverCache) {
									var st = new Date(window.ticketStorage
											.getItem('serverTime'));
									var ct = new Date();
									var type =jq('#TicketTypeBtnGroup a.ui-btn-active').attr('data-type');
									var reservePeriod = calReservedPeriod(type);
									// 如果系统时间和服务时间不一致， 需要重新渲染。
									if (st.getFullYear() !== ct.getFullYear()
											|| st.getMonth() != ct.getMonth()
											|| st.getDate() != ct.getDate() || reservePeriod !== mor.ticket.history.reservePeriod) {
										// 因为在初始化的时候， 这个内容已经被赋值了，所以有可能是错误的值.
										mor.ticket.leftTicketQuery.train_date = null;
										mor.ticket.leftTicketQuery.train_date_back = '';
										initTimePeriodSelectScroller();
										refreshFormFromModel();
									}
								});

						initPageComponent();
						refreshFormFromModel();
						/*
						 * 设置初始化选择席别
						 */
						mor.ticket.seat_Type = "";
						mor.ticket.seat_Type_index = "";
					});

	function refreshFormFromModel() {
		/*
		 * 初始化查询车站
		 */
		var model = mor.ticket.leftTicketQuery;
		if (!model.from_station_telecode) {

			model.from_station_telecode = (window.ticketStorage
					.getItem("set_from_station_telecode") == null ? "BJP"
					: window.ticketStorage.getItem("set_from_station_telecode"));
		}
		if (!model.to_station_telecode) {
			model.to_station_telecode = (window.ticketStorage
					.getItem("set_to_station_telecode") == null ? "SHH"
					: window.ticketStorage.getItem("set_to_station_telecode"));
		}
		if (!model.train_date) {
			if (window.ticketStorage.getItem("set_train_date_type") != null) {
				var date = mor.ticket.util.getNewDate();
				var queryDate = new Date(date.setDate(date.getDate()
						+ parseInt(window.ticketStorage
								.getItem("set_train_date_type"))));
				model.train_date = queryDate.format("yyyy-MM-dd");
			} else {
				var date = mor.ticket.util.getNewDate();
				date.setDate(date.getDate() + 1);
				model.train_date = date.format("yyyy-MM-dd");
			}
		}

		/*
		 * 页面初始化出发地,目的地,日期,席别等
		 */
		var cache = mor.ticket.cache;
		jq("#fromStationInput").val(
				cache.getStationNameByCode(model.from_station_telecode));
		jq("#toStationInput").val(
				cache.getStationNameByCode(model.to_station_telecode));
		jq("#trainDateInput option[value=" + model.train_date + "]").attr(
				"selected", "selected");
		jq("#trainDateInput").selectmenu('refresh', true);
		jq("#trainSeatInput").selectmenu('refresh', true);
		jq("#trainSeatInputBack").selectmenu('refresh', true);
		var mode = mor.ticket.viewControl.bookMode;

		if (model.train_date_back === "") {
			var date = mor.ticket.util.setMyDate(model.train_date);
			var today = mor.ticket.util.getNewDate();
			var todayDate = today.getDate();
			today = today.setDate(todayDate
					+ parseInt(window.ticketStorage.getItem("reservePeriod"),
							10) - 1);
			var reserveDay = new Date(today);
			var date_back;
			if (date < reserveDay || !jq.isNumeric(window.ticketStorage.getItem("reservePeriod"))) {
				date_back = new Date(date.setDate(date.getDate() + 1));
			} else {
				date_back = date;
			}
			model.train_date_back = date_back.format("yyyy-MM-dd");
		}

		if (mode === "wc" || mode === "fc") {
			jq(
					"#trainDateInputBack option[value=" + model.train_date_back
							+ "]").attr("selected", "selected");
			jq("#trainDateInputBack").selectmenu('refresh', true);
			jq("#trainCodeInputBack")
					.val(
							(model.station_train_code_back ? model.station_train_code_back
									: "不限"));
		}
	}

	/*
	 * 初始化CacheMap 数据
	 */
	function myPrepareCacheMap() {
		prepareCacheMap();
		myPrepareCacheMap = function() {
		};
	}

	/*
	 * 初始化页面状态
	 */

	function initPageComponent() {
		var mode = mor.ticket.viewControl.bookMode;
		jq("#bookTicketView .ui-header>h1").html("车票预订");
		if (mode === "wc") {
			jq("#exitChangeTicket").hide();

			jq("#backInfo").show();
			jq("#roundTripBtnGroup").children().removeClass("ui-disabled");
			jq("#doubleTrip").addClass("ui-btn-active ui-state-persist");
			jq("#singleTrip").removeClass("ui-btn-active ui-state-persist");
			jq('#FareHeadersBtnGroup').show();
		} else if (mode === "fc") {
			jq("#exitChangeTicket .ui-btn-text").html("退出往返");
			jq("#exitChangeTicket").show();

			var model = mor.ticket.leftTicketQuery;
			model.from_station_telecode_back = model.to_station_telecode;
			model.to_station_telecode_back = model.from_station_telecode;
			jq("#goInfo").children().addClass("ui-disabled");
			jq("#roundTripBtnGroup").children().addClass("ui-disabled");
			jq('#FareHeadersBtnGroup').hide();
		} else if (mode === "gc") {
			jq("#exitChangeTicket .ui-btn-text").html("退出改签");
			jq("#bookTicketView .ui-header>h1").html("车票预订(改签)");
			jq("#exitChangeTicket").show();
			jq("#goInfo").children().addClass("ui-disabled");
			jq("#roundTripBtnGroup").children().addClass("ui-disabled");
			jq("#fromStationInput").addClass("ui-disabled");
			jq(".z-startoff-date").removeClass("ui-disabled");
			jq("#toStationInput").addClass("ui-disabled");
			jq(".z-seat-type").removeClass("ui-disabled");

			jq("#backInfo").hide();
			jq("#singleTrip").addClass("ui-btn-active ui-state-persist");
			jq("#doubleTrip").removeClass("ui-btn-active ui-state-persist");
			// jq("#goInfo").children().removeClass("ui-disabled");
			jq("#roundTripBtnGroup").removeClass("ui-disabled");
			jq('#FareHeadersBtnGroup').hide();
		} else {// single trip
			jq("#backInfo").hide();
			jq("#exitChangeTicket").hide();

			jq("#singleTrip").addClass("ui-btn-active ui-state-persist");
			jq("#doubleTrip").removeClass("ui-btn-active ui-state-persist");
			jq("#goInfo").children().removeClass("ui-disabled");
			jq("#roundTripBtnGroup").children().removeClass("ui-disabled");
			jq("#fromStationInput").removeClass("ui-disabled");
			jq("#toStationInput").removeClass("ui-disabled");
			jq('#FareHeadersBtnGroup').show();
		}
	}
	function calReservedPeriod(type){
		var model = mor.ticket.leftTicketQuery;
		var reservedPeriodType = '';
		if (type === '2' || (model.purpose_codes == '0X' && type == undefined)) {
			reservedPeriodType = '3';
		} else if (type === '3'
				|| (model.purpose_codes == '1F' && type == undefined)) {
			reservedPeriodType = 'n';
		} else {
			reservedPeriodType = '1';
		}
		var reservePeriod = parseInt(window.ticketStorage
				.getItem("reservePeriod_" + reservedPeriodType), 10);
		if(!jq.isNumeric(reservePeriod)){
			reservePeriod = parseInt(window.ticketStorage
		.getItem("reservePeriod_" + '1'), 10);
		}
		return reservePeriod;
	}
	// 1. 初始化日期列表。type 用来判断不同的乘客类型.(普通，学生，农民工). 不同的类型，预售期不同。
	// 2. 如果服务器日期没有取到的花，需要用本地时间，如果服务器最后获取的时间和本地时间不一致，需要重新渲染。
	// reservePeriodList":[{"ticket_type":"1","from_period":"2013-11-18
	// 15:59:20","to_period":"2013-12-08 15:59:20"}," +
	// "{"ticket_type":"2","from_period":"2013-11-18
	// 15:59:20","to_period":"2013-11-18 15:59:20"}," +
	// "{"ticket_type":"3","from_period":"2013-11-18
	// 15:59:20","to_period":"2013-12-07 15:59:20"}," +
	// "{"ticket_type":"4","from_period":"2013-11-18
	// 15:59:20","to_period":"2013-12-08 15:59:20"}]
	function initTimePeriodSelectScroller(type) {
		var model = mor.ticket.leftTicketQuery;
		
		var reservePeriod = calReservedPeriod(type) || 20;
		mor.ticket.history.reservePeriod = reservePeriod;
		var jq_dateInput = jq("#trainDateInput");
		jq_dateInput.empty();
		var jq_dateInputBack = jq("#trainDateInputBack");
		jq_dateInputBack.empty();
		for ( var i = 0; i < reservePeriod + 1; i++) {
			var date = new Date(mor.ticket.util.getNewDate());
			var date1 = new Date(date.setDate(date.getDate() + i));
			var week = getWeek(date1);
			var date1Str = date1.format("yyyy-MM-dd");
			var htmlStr = "<option value='" + date1Str + "'>" + date1Str
					+ "      " + week + "</option>";
			jq_dateInput.append(htmlStr);
			jq_dateInputBack.append(htmlStr);
		}
		switch (type) {
		case '1':
			return model.purpose_codes = '00';
			break;
		case '2':
			return model.purpose_codes = '0X';
			break;
		case '3':
			return model.purpose_codes = '1F';
			break;
		default:
			return '00';
		}
		
	}

	/*
	 * 初始化席位
	 */
	function initSeatSelectScroller() {
		var jq_dateInput = jq("#trainSeatInput");
		var jq_trainSeatInputBack = jq("#trainSeatInputBack");
		jq_dateInput.empty();
		jq_trainSeatInputBack.empty();
		mor.ticket.seatName = [ '不限', '特等座', '一等座', '二等座', '高级软卧', '软卧', '硬卧',
				'软座', '硬座', '无座' ];
		mor.ticket.seatId = [ '0', '9', 'P', 'M', 'O', '6', '4', '3', '2', '1' ];
		// 商务座
		var htmlStr = null;
		htmlStr += "<option " + trainSeatSelectd('') + " value='0'>不限</option>";
		htmlStr += "<option " + trainSeatSelectd('9')
				+ " value='9'>商务座</option>";
		htmlStr += "<option " + trainSeatSelectd('P')
				+ " value='P'>特等座</option>";
		htmlStr += "<option " + trainSeatSelectd('M')
				+ " value='M'>一等座</option>";
		htmlStr += "<option " + trainSeatSelectd('O')
				+ " value='O'>二等座</option>";
		htmlStr += "<option " + trainSeatSelectd('6')
				+ " value='6'>高级软卧</option>";
		htmlStr += "<option " + trainSeatSelectd('4')
				+ " value='4'>软卧</option>";
		htmlStr += "<option " + trainSeatSelectd('3')
				+ " value='3'>硬卧</option>";
		htmlStr += "<option " + trainSeatSelectd('2')
				+ " value='2'>软座</option>";
		htmlStr += "<option " + trainSeatSelectd('1')
				+ " value='1'>硬座</option>";

		jq_dateInput.append(htmlStr);

		var htmlStrBack = null;
		htmlStrBack += "<option " + trainSeatInputBackSelectd('')
				+ " value='0'>不限</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('9')
				+ " value='9'>商务座</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('P')
				+ " value='P'>特等座</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('M')
				+ " value='M'>一等座</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('O')
				+ " value='O'>二等座</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('6')
				+ " value='6'>高级软卧</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('4')
				+ " value='4'>软卧</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('3')
				+ " value='3'>硬卧</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('2')
				+ " value='2'>软座</option>";
		htmlStrBack += "<option " + trainSeatInputBackSelectd('1')
				+ " value='1'>硬座</option>";
		jq_trainSeatInputBack.append(htmlStrBack);

	}
	;

	/*
	 * 返回单程出发席位状态选择
	 */

	function trainSeatSelectd(val) {
		if (mor.ticket.leftTicketQuery.seat_Type == "" && val == "") {
			return "selected='selected'";
		} else if (mor.ticket.leftTicketQuery.seat_Type == val) {
			return "selected='selected'";
		} else {
			return;
		}
	}

	/*
	 * 返回往返席位状态选择
	 */
	function trainSeatInputBackSelectd(val) {
		if (mor.ticket.leftTicketQuery.seatBack_Type == "" && val == "") {
			return "selected='selected'";
		} else if (mor.ticket.leftTicketQuery.seatBack_Type == val) {
			return "selected='selected'";
		} else {
			return;
		}
	}

	/*
	 * 返回日期的星期
	 */
	function getWeek(prompt) {
		var desc = [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ];
		if (typeof prompt == "string") {
			var date = mor.ticket.util.setMyDate(prompt);
			return desc[date.getDay()];
		} else {
			return desc[prompt.getDay()];
		}
	}

	/*
	 * 返回乘客的左右样式
	 */
	function getFareBtnStyle(i) {
		if (i == 1) {
			return "ui-corner-left";
		} else if (i == 5) {
			return "ui-corner-right";
		} else {
			return '';
		}
	}

	/*
	 * 显示选择乘客列表
	 */
	function showAddedPassager() {
		var auto_i = 0;
		var html = '';
		mor.ticket.passengerList.sort();
		jq.each(mor.ticket.passengerList,
				function(i, passenger) {
					if (passenger.id_no && passenger.id_no != 'undefined') {
						auto_i++;
						html += createBtn(auto_i,
								'ui-exit-btn ui-btn ui-btn-up-c text-ellipsis '
										+ getFareBtnStyle(auto_i),
								passenger.user_name);
					}
				});
		if (auto_i < 5) {
			auto_i++;
			html += createBtn(auto_i, 'ui-btn add-btn ui-btn-up-c '
					+ getFareBtnStyle(auto_i));
		}

		var index = 5 - auto_i;

		for ( var autos = 1; autos <= index; autos++) {
			auto_i++;
			html += createBtn(auto_i, 'ui-btn ui-btn-up-c '
					+ getFareBtnStyle(auto_i));
		}

		jq("#FareHeadersBtnGroup .ui-controlgroup-controls").html(html);
		setTimeout(registerAddFare2fareListBtnClickHandler, 20);
		return;
	}

	function createBtn(index, clazz, val) {
		var html = '<a ';
		if (val) {
			html += 'data-index="' + index + '" ';
		}
		html += 'data-role="button" data-theme="c" href="#" id="A_' + index
				+ '" ';
		if (clazz) {
			html += 'class="' + clazz + '"';
		}
		html += '>';
		if (val) {
			html += val;
		}
		return html += '</a>';
	}

	function registerSingleTripClickHandler() {
		jq("#singleTrip").bind("tap", function() {
			jq("#singleTrip").addClass("ui-btn-active ui-state-persist");
			jq("#doubleTrip").removeClass("ui-btn-active ui-state-persist");
			jq("#backInfo").hide();
			mor.ticket.viewControl.bookMode = "dc";
			var model = mor.ticket.leftTicketQuery;
			model.from_station_telecode_back = model.to_station_telecode;
			model.to_station_telecode_back = model.from_station_telecode;
			refreshFormFromModel();
			jq("#trainDateInputShowBack").val("");
			jq("#bookTicketView .iscroll-wrapper").iscrollview('refresh');
			return false;
		});
	}

	/*
	 * 出发地和目的地交换数据
	 */

	function registerchangeSeatClickHandler() {

		jq("#changeSeat").live("tap", function() {

			var model = mor.ticket.leftTicketQuery;

			var fromStationInput = jq("#toStationInput").val();
			var toStationInput = jq("#fromStationInput").val();

			jq("#toStationInput").val(toStationInput);
			jq("#fromStationInput").val(fromStationInput);

			var from_station = model.to_station_telecode;
			var to_station = model.from_station_telecode;

			model.from_station_telecode = from_station;
			model.to_station_telecode = to_station;

			return false;
		});
	}

	function registerDoubleTripClickHandler() {
		jq("#doubleTrip")
				.bind(
						"tap",
						function() {
							jq("#doubleTrip").addClass(
									"ui-btn-active ui-state-persist");
							jq("#singleTrip").removeClass(
									"ui-btn-active ui-state-persist");
							jq("#backInfo").show();
							mor.ticket.viewControl.bookMode = "wc";
							var model = mor.ticket.leftTicketQuery;
							model.from_station_telecode_back = model.to_station_telecode;
							model.to_station_telecode_back = model.from_station_telecode;
							refreshFormFromModel();
							// by yiguo
							// jq("#bookTicketView
							// .iscroll-wrapper").iscrollview('refresh');
							jq(
									"#bookTicketView .iscroll-wrapper>.iscroll-scroller")
									.height(600);
							setTimeout(function() {

								jq("#bookTicketView .iscroll-wrapper")
										.iscrollview('refresh');
							}, 20);
							return false;
						});
	}

	function registerFromStationInputClickHandler() {

		jq("#fromStationInput").bind("tap", function(e) {
			e.stopImmediatePropagation();
			jq("#bookTicketView").css("pointer-events", "none");
			mor.ticket.views.selectStation.isFromStation = true;
			jq.mobile.changePage(vPathCallBack() + "selectStation.html");
			return false;
		});
	}
	;

	function registerToStationInputClickHandler() {

		jq("#toStationInput").bind("tap", function(e) {
			e.stopImmediatePropagation();
			jq("#bookTicketView").css("pointer-events", "none");
			mor.ticket.views.selectStation.isFromStation = false;
			jq.mobile.changePage(vPathCallBack() + "selectStation.html");
			return false;
		});
	}
	;

	function registerTrainDateInputChangeHandler() {
		mor.ticket.util.bindSelectFocusBlurListener("#trainDateInput");
		jq("#trainDateInput").bind("change", function() {
			mor.ticket.leftTicketQuery.train_date = jq(this).val();
			return false;
		});
	}
	;

	function registerSeatSelectChangeHandler() {

		mor.ticket.util.bindSelectFocusBlurListener("#trainSeatInput");

		jq("#trainSeatInput").bind("change", function() {

			if (jq(this).val() == 0) {
				mor.ticket.leftTicketQuery.seat_Type = '';
			} else {
				mor.ticket.leftTicketQuery.seat_Type = jq(this).val();
			}
			return false;
		});

	}
	;
	function registerSeatSelectBackChangeHandler() {
		mor.ticket.util.bindSelectFocusBlurListener("#trainSeatInputBack");
		jq("#trainSeatInputBack").bind("change", function() {
			if (jq(this).val() == 0) {
				mor.ticket.leftTicketQuery.seatBack_Type = '';
			} else {
				mor.ticket.leftTicketQuery.seatBack_Type = jq(this).val();
			}
			return false;
		});
	}

	function registerTrainCodeInputClickHandler() {

		jq("#trainCodeInput").bind("tap", function() {
			jq("#bookTicketView").css("pointer-events", "none");
			var model = mor.ticket.leftTicketQuery;
			if (model.from_station_telecode == model.to_station_telecode) {
				mor.ticket.util.alertMessage("目的地不能与出发地相同！");
				return false;
			}
			mor.ticket.views.selectTrain.isFromTrain = true;
			jq.mobile.changePage(vPathCallBack() + "selectTrain.html");
			return false;
		});
	}
	;

	function registerTrainCodeInputBackClickHandler() {

		jq(" #trainCodeInputBack").bind("tap", function() {
			jq("#bookTicketView").css("pointer-events", "none");
			var model = mor.ticket.leftTicketQuery;

			if (model.from_station_telecode == model.to_station_telecode) {
				mor.ticket.util.alertMessage("目的地不能与出发地相同！");
				return false;
			}
			mor.ticket.views.selectTrain.isFromTrain = false;
			jq.mobile.changePage(vPathCallBack() + "selectTrain.html");
		});
	}
	;

	function registerTrainDateInputBackChangeHandler() {
		mor.ticket.util.bindSelectFocusBlurListener("#trainDateInputBack");
		jq("#trainDateInputBack").bind("change", function() {
			mor.ticket.leftTicketQuery.train_date_back = jq(this).val();
			return false;
		});
	}
	;

	function registerTrainHeadersBtnGroupClickHandler() {
		jq("#trainHeadersBtnGroup").on("tap", "a", function() {
			var activeCls = "ui-btn-active ui-state-persist";
			var that = jq(this);
			var id = that.attr('id');
			if (that.hasClass(activeCls)) {
				if (id != "QB") {
					that.removeClass(activeCls);
					// 判断如果出了全部之外的alink都没有ui-btn-active
					// ui-state-persist，那么给QB添加此属性
					if (!jq("#D,#Z,#T,#K,#QT").hasClass(activeCls)) {
						jq("#QB").addClass(activeCls);
					}
				}
			} else {
				if (id == "QB") {
					that.addClass(activeCls).siblings().removeClass(activeCls);
				} else {
					that.addClass(activeCls);
					if (jq("#QB").hasClass(activeCls)) {
						jq("#QB").removeClass(activeCls);
					}
				}
			}
			return false;
		});
	}

	/*
	 * 验证输入框是否选择数据
	 */
	function validateInputs() {
		var query = mor.ticket.leftTicketQuery;
		if (mor.ticket.viewControl.bookMode === "fc") {
			if (query.from_station_telecode_back == '') {
				mor.ticket.util.alertMessage("出发地不能为空");
				return false;
			} else if (query.to_station_telecode_back == '') {
				mor.ticket.util.alertMessage("目的地不能为空");
				return false;
			}
			;
		} else {
			if (query.from_station_telecode == ''
					|| jq('#fromStationInput').val() == '') {
				mor.ticket.util.alertMessage("出发地不能为空");
				return false;
			} else if (query.to_station_telecode == ''
					|| jq('#toStationInput').val() == '') {
				mor.ticket.util.alertMessage("目的地不能为空");
				return false;
			}
		}
		if (mor.ticket.viewControl.bookMode == "wc"
				|| mor.ticket.viewControl.bookMode == "fc") {
			var model = mor.ticket.leftTicketQuery;
			var wcDate = mor.ticket.util.setMyDate(model.train_date);
			var fcDate = mor.ticket.util.setMyDate(model.train_date_back);
			if (fcDate < wcDate) {
				mor.ticket.util.alertMessage("返回日期需要大于出发日期!");
				return false;
			}
		}
		return true;
	}

	function registerQuerySingleTicketBtnClickHandler() {
		jq("#querySingleTicketBtn")
				.bind(
						"tap",
						function(e) {
							e.stopImmediatePropagation();

							
							var model = mor.ticket.leftTicketQuery;

							if (model.from_station_telecode == model.to_station_telecode) {
								mor.ticket.util.alertMessage("目的地不能与出发地相同！");
								return false;
							}

							if (validateInputs()) {
								var query = mor.ticket.leftTicketQuery;
								query.result = [];
								query.train_headers = "";
								query.train_flag = "";

								for ( var i = 0, len = jq("#trainHeadersBtnGroup a").length; i < len; i++) {
									var jq_a = jq("#trainHeadersBtnGroup a:eq("
											+ i + ")");
									if (jq_a
											.hasClass("ui-btn-active ui-state-persist")) {
										query.train_headers += jq_a.attr("id")
												+ "#";
									}
								}
								if (query.train_headers == "") {
									query.train_headers = "QB#";
									jq("#QB").addClass(
											"ui-btn-active ui-state-persist");
								}

								if (!busy.isVisible()) {
									busy.show();
								}

								setTimeout(
										function() {
											mor.ticket.history.url == '';
											jq.mobile
													.changePage(vPathCallBack()
															+ "searchSingleTicketResults.html");
										}, 300);
							}

							return false;
						});
	}

	function registerExitChangeTicketBtnHandler() {
		jq("#exitChangeTicket").off().bind(
				"tap",
				function(e) {
					e.stopImmediatePropagation();
					if (mor.ticket.viewControl.bookMode === "gc") {
						mor.ticket.viewControl.bookMode = "dc";
						jq.mobile.changePage(vPathCallBack()
								+ "finishedOrderDetail.html");
					}
					if (mor.ticket.viewControl.bookMode === "fc") {
						mor.ticket.viewControl.bookMode = "dc";

						jq.mobile.changePage(vPathViewCallBack()
								+ "MobileTicket.html", {
							allowSamePageTransition : true,
							transition : 'none',
							reloadPage : false
						});
					}
					return false;
				});
	}
	/**
	 * function refreshPage(page){ // Page refresh page.trigger('pagecreate');
	 * //page.listview('refresh'); }
	 */
	// add by yiguo
	function addPassenger() {
		// mod by yiguo
		// if(mor.ticket.history.url !== 'bookticket'){
		jq("#FareHeadersBtnGroup a.add-btn").off();
		mor.ticket.history.url = 'bookticket';
		jq("#selectFarePassengerView").remove();
		jq.mobile.changePage(vPathCallBack() + "fareList.html");
		setTimeout(function() {
			jq("#FareHeadersBtnGroup a.add-btn").off().on("tap", addPassenger)
		}, 1000);
		// }
		return false;
	}
	function registerAddFare2fareListBtnClickHandler() {
		// mod by yiguo
		jq("#FareHeadersBtnGroup a.add-btn").off().on("tap", addPassenger);
		jq("#FareHeadersBtnGroup a.ui-exit-btn").off().on(
				"tap",
				function(e) {
					var node = jq(this);
					var index = jq(this).attr("data-index");
					if (index) {
						WL.SimpleDialog.show("温馨提示", "确定删除该乘客吗?", [
								{
									text : "取消",
									handler : function() {
										node.removeClass('ui-btn-hover-c')
												.removeClass('ui-btn-down-c')
												.addClass('ui-btn-up-c');
										return false;
									}
								},
								{
									text : "确认",
									handler : function() {
										node.removeClass('ui-btn-hover-c')
												.removeClass('ui-btn-down-c')
												.addClass('ui-btn-up-c');
										mor.ticket.passengerList.splice(
												index - 1, 1);
										showAddedPassager();
										return;
									}
								} ]);
					}
				}).on(
				"taphold",
				function() {
					var node = jq(this);
					WL.SimpleDialog.show("温馨提示", "确定删除所有的乘客吗?", [
							{
								text : "取消",
								handler : function() {
									node.removeClass('ui-btn-hover-c')
											.removeClass('ui-btn-down-c')
											.addClass('ui-btn-up-c');
									return false;
								}
							},
							{
								text : "确认",
								handler : function() {
									node.removeClass('ui-btn-hover-c')
											.removeClass('ui-btn-down-c')
											.addClass('ui-btn-up-c');
									mor.ticket.passengerList = [];
									showAddedPassager();
									return;
								}
							} ]);
				});
	}

	function registerSelectUserType() {
		var model = mor.ticket.leftTicketQuery;
		
		jq('#TicketTypeBtnGroup').off().on('tap', 'a', function(e) {
			var current = jq('#TicketTypeBtnGroup a.ui-btn-active');
			if (current.attr('data-type') == jq(this).attr('data-type')) {
				return;
			}
			current.removeClass('ui-btn-active');
			jq(this).addClass('ui-btn-active');
			initTimePeriodSelectScroller(jq(this).attr('data-type'));
			var today = mor.ticket.util.getNewDate();
			var findate = new Date(today.setDate(today.getDate()
					+ mor.ticket.history.reservePeriod));
			var dateEnd = findate.format("yyyy-MM-dd");
			var todays = mor.ticket.util.getNewDate();
			var queryDate = new Date(todays.setDate(todays.getDate()
					+ parseInt(window.ticketStorage
							.getItem("set_train_date_type")||1)));
			var trainDate = queryDate.format("yyyy-MM-dd");
			var mode = mor.ticket.viewControl.bookMode;
				if ((mode === "wc" || mode === "fc") && model.train_date_back > dateEnd) {
					var newDate = mor.ticket.util.getNewDate();
					var back_Date = new Date(newDate.setDate(newDate.getDate()
							+ parseInt(window.ticketStorage
									.getItem("set_train_date_type")||1)+1));
					var backDate = back_Date.format("yyyy-MM-dd");
					jq(
							"#trainDateInputBack option[value=" + backDate
									+ "]").attr("selected", "selected");
					jq("#trainDateInputBack").selectmenu('refresh', true);
					model.train_date_back = backDate ; 
				} 
				if (model.train_date > dateEnd) {
				jq("#trainDateInput option[value=" + trainDate + "]").attr(
						"selected", "selected");
				jq("#trainDateInput").selectmenu('refresh', true);
				model.train_date = trainDate ; 
			} 
			
		});
	}

})();