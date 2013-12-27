
/* JavaScript content from js/controller/searchSingleTicketResults.js in folder common */
(function() {
	jq("#searchSingleTicketResultsView").live("pageinit", function() {

		initChooseDateInputScroller();
		registerTicketListItemClickHandler();
		registerPrevDateBtnClickHandler();
		registerNextDateBtnClickHandler();

		registerChooseDateBtnClickHandler();
		registerChooseDateScrollerChangeListener();

		jq.mobile.loadPage(vPathCallBack() + "passengers.html");

	});

	jq("#searchSingleTicketResultsView")
			.live(
					"pagehide",
					function(e, data) {
						if (mor.ticket.viewControl.bookMode == "fc") {
							if (data.nextPage.attr("id") != "bookTicketView"
									&& data.nextPage.attr("id") != "passengersView") {
								mor.ticket.viewControl.bookMode = "dc";
								mor.ticket.viewControl.tab1_cur_page = vPathViewCallBack()
										+ "MobileTicket.html";
							}
						}
					});

	jq("#searchSingleTicketResultsView")
			.live(
					"pagebeforeshow",
					function() {
						var query = mor.ticket.leftTicketQuery;
						jq(
								"#searchSingleResultsButtonGroup .ui-select .ui-btn-text span")
								.text("选择日期");
						if (query.purpose_codes === "0X") {
							jq("#searchSingleTicketResultsView .ui-header>h1")
									.text("查询结果(学生)");
						} else if (query.purpose_codes === "1F") {
							jq("#searchSingleTicketResultsView .ui-header>h1")
									.text("查询结果(农民工)");
						} else {
							jq("#searchSingleTicketResultsView .ui-header>h1")
									.text("查询结果");
						}
						registerBackButtonHandler();
					});

	jq("#searchSingleTicketResultsView")
			.live(
					"pageshow",
					function() {

						// mor.ticket.seat_Type_index="";

						mor.ticket.viewControl.tab1_cur_page = "searchSingleTicketResults.html";
						var query = mor.ticket.leftTicketQuery;

						if (query.result.length == 0) {
							if (mor.ticket.viewControl.bookMode === "fc") {
								if (query.from_station_telecode_back == '') {
									mor.ticket.util.alertMessage("出发地不能为空");
									jq.mobile.changePage(vPathViewCallBack()
											+ "MobileTicket.html");
									return false;
								} else if (query.to_station_telecode_back == '') {
									mor.ticket.util.alertMessage("目的地不能为空");
									jq.mobile.changePage(vPathViewCallBack()
											+ "MobileTicket.html");
									return false;
								}
								;
							} else {
								if (query.from_station_telecode == '') {
									mor.ticket.util.alertMessage("出发地不能为空");
									jq.mobile.changePage(vPathViewCallBack()
											+ "MobileTicket.html");
									return false;
								} else if (query.to_station_telecode == '') {
									mor.ticket.util.alertMessage("目的地不能为空");
									jq.mobile.changePage(vPathViewCallBack()
											+ "MobileTicket.html");
									return false;
								}
							}
							searchAndShowResult(query);
						} else {
							showSearchResults(query.result);
						}
					});

	function searchAndShowResult(query) {
		var util = mor.ticket.util;
		var mode = mor.ticket.viewControl.bookMode;
		var commonParameters = null;
		if (mode === "fc") {
			commonParameters = {
				"train_date" : util.processDateCode(query.train_date_back),
				"purpose_codes" : query.purpose_codes,
				"from_station" : query.from_station_telecode_back,
				"to_station" : query.to_station_telecode_back,
				"station_train_code" : query.station_train_code_back,
				"start_time_begin" : util
						.getStartTimeBeginCode(query.time_period_back),
				"start_time_end" : util
						.getStartTimeEndCode(query.time_period_back),
				"train_headers" : query.train_headers,
				"train_flag" : query.train_flag,
				"seat_type" : mor.ticket.leftTicketQuery.seat_Type,
				"seatBack_Type" : mor.ticket.leftTicketQuery.seatBack_Type,
				"ticket_num" : ""
			};
		} else {
			commonParameters = {
				"train_date" : util.processDateCode(query.train_date),
				"purpose_codes" : query.purpose_codes,
				"from_station" : query.from_station_telecode,
				"to_station" : query.to_station_telecode,
				"station_train_code" : query.station_train_code,
				"start_time_begin" : util
						.getStartTimeBeginCode(query.time_period),
				"start_time_end" : util.getStartTimeEndCode(query.time_period),
				"train_headers" : query.train_headers,
				"train_flag" : query.train_flag,
				"seat_type" : mor.ticket.leftTicketQuery.seat_Type,
				"seatBack_Type" : mor.ticket.leftTicketQuery.seatBack_Type,
				"ticket_num" : ""
			};
		}

		var invocationData = {
			adapter : "CARSMobileServiceAdapter",
			procedure : "queryLeftTicket"
		};
		var options = {
			onSuccess : requestSucceeded,
			onFailure : util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData,
				options, true);
		storeUserRecentSearchStation(query.from_station_telecode,
				query.to_station_telecode);
		jq("#searchSingleTicketResultList").hide();

	}

	function storeUserRecentSearchStation(from_station_telecode,
			to_station_telecode) {
		var recentStationCodeList = window.ticketStorage
				.getItem("recentStation") == null ? [] : JSON
				.parse(ticketStorage.getItem("recentStation"));
		for ( var i = 0, l = recentStationCodeList.length; i < l; i++) {
			if (recentStationCodeList[i] == from_station_telecode) {
				recentStationCodeList.splice(i, 1);
				i--;
				continue;
			}
			if (recentStationCodeList[i] == to_station_telecode) {
				recentStationCodeList.splice(i, 1);
				i--;
				continue;
			}
		}
		recentStationCodeList.push(to_station_telecode);
		recentStationCodeList.push(from_station_telecode);
		if (recentStationCodeList.length > 10)
			recentStationCodeList.shift();
		window.ticketStorage.setItem("recentStation", JSON
				.stringify(recentStationCodeList));
	}

	function requestSucceeded(result) {
		initChooseDateInputScroller();
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			showSearchResults(invocationResult.ticketResult);
		} else {
			jq.mobile.changePage(vPathViewCallBack() + "MobileTicket.html");
			mor.ticket.util.alertMessage(invocationResult.error_msg);

			return;

		}
		if (busy.isVisible()) {
			busy.hide();
		}
	}
	;

	function showSearchResults(results) {
		var util = mor.ticket.util;
		var seatType = "";
		var mode = mor.ticket.viewControl.bookMode;
		if (mode === "fc") {
			seatType = mor.ticket.leftTicketQuery.seatBack_Type;
		} else {
			seatType = mor.ticket.leftTicketQuery.seat_Type;
		}
		for ( var i = 0; i < results.length; i++) {
			var number = 0;
			if (results[i].yp_info && results[i].yp_info.length > 0) {
				results[i].yplist = processYpInfo(results[i].yp_info);
			} else {
				results[i].yplist = "";
			}
			// 判断内部是否有优惠票
			// if (results[i].yp_ex&&results[i].yp_ex.length > 0) {
			// results[i].discount = '0';
			// for(var di = 1;di<results[i].yp_ex.length;di=di+2){
			// if(results[i].yp_ex[di]==='1'){
			// results[i].discount = '1';
			// break;
			// }
			// }
			// }else{
			// results[i].discount = '0';
			// }
			results[i].lishiHour = results[i].lishi.substring(0, 2);
			results[i].lishiMinute = results[i].lishi.substring(3, 5);
			/*
			 * 选择席位后从结果集里挑选合适的席位
			 */
			if (seatType != "") {
				if (results[i].yplist.length === 0) {
					results.splice(i, 1);
					--i;
				} else {
					for ( var j = 0; j < results[i].yplist.length; j++) {
						// 席位条件符合后计数
						if (results[i].yplist[j].type_id === seatType) {
							number++;
							// 如果合适席位的席位数位0删除
							if (results[i].yplist[j].num === 0) {
								results.splice(i, 1);
								--i;
								break;
							}
						}
					}
					// 计数后如果席位不包含选择席位，删除
					if (number === 0) {
						results.splice(i, 1);
						--i;
					}
				}

			}
		}

		var model = mor.ticket.leftTicketQuery;
		var cache = mor.ticket.cache;
		var train_date = model.train_date;
		var from_station_telecode = model.from_station_telecode;
		var to_station_telecode = model.to_station_telecode;
		if (mode === "fc") {
			train_date = model.train_date_back;
			from_station_telecode = model.from_station_telecode_back;
			to_station_telecode = model.to_station_telecode_back;
		}

		results = jq
				.grep(
						results,
						function(item, index) {
							return cache
									.getStationNameByCode(item.from_station_telecode) === undefined
									|| cache
											.getStationNameByCode(item.to_station_telecode) === undefined;
						}, true);

		jq("#searchSingleResultsPrompt .ui-block-a").html(
				util.getLocalDateString1(train_date));
		jq("#searchSingleResultsPrompt .ui-block-b").html(
				cache.getStationNameByCode(from_station_telecode) + "-"
						+ cache.getStationNameByCode(to_station_telecode));
		jq("#searchSingleResultsPrompt .ui-block-c").html(
				"共" + results.length + "趟车");
		// 输出车次信息模板
		jq("#searchSingleTicketResultList").html(generateListContent(results))
				.listview("refresh");

		jq("#searchSingleTicketResultList").show();

		initDateBtn();
		model.result = results;
	}

	function registerTicketListItemClickHandler() {
		jq("#searchSingleTicketResultList")
				.on(
						"tap",
						"li",
						function(e) {

							e.stopImmediatePropagation();
							var query = mor.ticket.leftTicketQuery;
							mor.ticket.currentTicket = query.result[jq(this)
									.index()];
							var ticket = mor.ticket.currentTicket;
							if (mor.ticket.viewControl.bookMode === "fc") {
								ticket.train_date = mor.ticket.leftTicketQuery.train_date_back;
								ticket.pre_train_date = mor.ticket.leftTicketQuery.train_date;
							} else {
								ticket.train_date = mor.ticket.leftTicketQuery.train_date;
							}

							if (ticket.flag == "0") {
								mor.ticket.util.alertMessage(ticket.message);
								return false;
							}
							var hasticket = ticket.yplist.length;
							for ( var i = 0; i < ticket.yplist.length; i++) {
								if (!ticket.yplist[i].num) {
									hasticket--;
								}
							}
							if (hasticket) {
								// WL.Logger.debug("ticket
								// price:"+ticket.yplist[0].price);
								if (ticket.yplist[0].price == "NaN"
										|| ticket.yplist[0].price == null
										|| ticket.yplist[0].price === "undefined") {
									// WL.Logger.debug("no ticket price need
									// call server");
									var util = mor.ticket.util;
									var parameters = util
											.prepareRequestCommonParameters({
												"train_date" : util
														.processDateCode(ticket.train_date),
												"from_station" : ticket.from_station_telecode,
												"to_station" : ticket.to_station_telecode,
												"station_train_code" : ticket.station_train_code,
												"yp_info" : ticket.yp_info
											});
									/*
									 * var invocationData = { adapter:
									 * "CARSMobileServiceAdapter", procedure:
									 * "queryLeftTicketDetail", "parameters":
									 * [parameters] };
									 */
									var invocationData = {
										adapter : "CARSMobileServiceAdapter",
										procedure : "queryLeftTicketDetail",
									};
									var options = {
										onSuccess : requestYPInfoSucceeded,
										onFailure : util
												.creatCommonRequestFailureHandler()
									};
									// WL.Client.invokeProcedure(invocationData,
									// options);
									mor.ticket.util
											.invokeWLProcedure(parameters,
													invocationData, options);
									// busy.show();
								} else {
									jq.mobile.changePage(vPathCallBack()
											+ "passengers.html");
								}
							} else {
								mor.ticket.util.alertMessage("很抱歉，当日该车次票已售完");
							}
							return false;
						});
	}
	;

	function requestYPInfoSucceeded(result) {
		if (busy.isVisible()) {
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			var ticket = mor.ticket.currentTicket;
			ticket.yp_info = invocationResult.yp_info;
			ticket.yplist = processYpInfo(ticket.yp_info);
			// alert(mor.ticket.loginUser.isAuthenticated);

			jq.mobile.changePage(vPathCallBack() + "passengers.html");

			/*
			 * if(mor.ticket.loginUser.isAuthenticated === "N"){
			 * mor.ticket.util.keepPageURL();
			 * jq.mobile.changePage(vPathCallBack()+"loginTicket.html"); }else{
			 * jq.mobile.changePage(vPathCallBack()+"passengers.html"); }
			 */

			// jq.mobile.changePage(vPathCallBack()+"passengers.html");
		} else {

			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	;

	function registerBackButtonHandler() {
		jq("#searchSingleBackBtn").off().on("tap", function(e) {
			e.stopImmediatePropagation();
			e.stopPropagation();
			e.preventDefault();
			jq.mobile.changePage(vPathViewCallBack() + "MobileTicket.html");
			return false;
		});
	}
	function processYpInfo(ypinfo) {
		var cache = mor.ticket.cache;
		var arrayLength = ypinfo.length / 10;
		var obj = new Array();
		var temp_seat_rate = 50;// 对于没有定义的座位，rate值从50开始递增
		for ( var i = 0, m = 6, n = 10, x = 0, y = 1; i < arrayLength; i++, m = m + 10, n = n + 10, x = x + 10, y = y + 10) {
			var seat_type_id = ypinfo.substring(x, y);
			var seat_type = null;
			var seat_type_rate = null;
			var seat_num = 0;
			if (parseInt(ypinfo.substring(m, m + 1), 10) >= 3) {
				seat_type = "无座";
				seat_type_rate = 100;
				seat_num = parseInt(ypinfo.substring(m, n), 10)-3000;
			} else {
				seat_type = cache.getSeatTypeByCode(seat_type_id);
				seat_type_rate = cache.getSeatTypeRateByCode(seat_type_id);
				if (seat_type_rate === null || seat_type_rate === "undefined") {
					seat_type_rate = temp_seat_rate;
					temp_seat_rate = temp_seat_rate + 1;
				}
				seat_num = parseInt(ypinfo.substring(m, n), 10);
			}

			obj[i] = {
				type_id : seat_type_id,
				type : seat_type,
				type_rate : seat_type_rate,// 保存座位rate用于排序
				num : seat_num,
				uclass : getLiClass(i),
				price : (parseFloat(ypinfo.substring(y, m), 10) / 10)
						.toFixed(2)
			};
		}
		var sortedObjs = sortYpInfo(obj);

		// mjl，过滤num为0的object
		// for ( var i = 0; i < sortedObjs.length; i++) {
		// if (sortedObjs[i].num == 0) {
		// sortedObjs.splice(i, 1);
		// --i;
		// }
		// }

		for ( var i = 0; i < sortedObjs.length; i++) {
			sortedObjs[i].uclass = getLiClass(i);
		}
		sortedObjs = repalceYpInfoType(sortedObjs);
		return sortedObjs;
	}
	// 把两个字数以上的座位类型改为两位
	function repalceYpInfoType(objs) {
		for ( var i in objs) {
			var type = objs[i].type;
			switch (type) {
			case "商务座":
				objs[i].type = "商务";
				break;
			case "特等座":
				objs[i].type = "特等";
				break;
			case "一等座":
				objs[i].type = "一等";
				break;
			case "二等座":
				objs[i].type = "二等";
				break;
			case "高级软卧":
				objs[i].type = "高软";
				break;
			case "观光座":
				objs[i].type = "观光";
				break;
			}
		}
		return objs;
	}

	// 按照座位的type_rate进行排序，规则如下
	// 商务座、特等座、一等座、二等座、高级软卧、软卧、硬卧、软座、硬座、（...）、无座 的顺序显示；
	// 如果出现其余席别，显示在这些席别的后面，顺序暂不限，其中 无座 总放在最后面。
	function sortYpInfo(ypinfoArray) {

		var by = function(name) {
			return function(o, p) {
				var a, b;
				if (typeof o === "object" && typeof p === "object" && o && p) {
					a = o[name];
					b = p[name];
					if (a === b) {
						return 0;
					}
					if (typeof a === typeof b) {
						return a < b ? -1 : 1;
					}
					return typeof a < typeof b ? -1 : 1;
				} else {
					throw ("error");
				}
			};
		};

		ypinfoArray.sort(by("type_rate"));
		return ypinfoArray;

	}
	
	function initChooseDateInputScroller() {

		var date = mor.ticket.util.getNewDate();
		
		var findate = new Date(date.setDate(date.getDate() + initReservedPeriod()));
		jq('#chooseDateInput').scroller({
			preset : 'date',
			theme : 'ios',
			yearText : '年',
			monthText : '月',
			dayText : '日',
			setText : '确认',
			cancelText : '取消',
			display : 'modal',
			mode : 'scroller',
			dateOrder : 'yy mm dd',
			dateFormat : 'yy-mm-dd',
			startYear : mor.ticket.util.getNewDate().getFullYear(),
			minDate : mor.ticket.util.getNewDate(),
			maxDate : findate,
			height : 40,
			showLabel : true
		});
		jq("#chooseDateInput")
				.scroller('setDate', mor.ticket.util.getNewDate());
		/*
		 * var reservePeriod =
		 * parseInt(window.ticketStorage.getItem("reservePeriod"),10); var
		 * jq_dateInput = jq("#chooseDateInput optgroup");
		 * jq_dateInput.html(""); for(var i=0; i<reservePeriod; i++){ var date=
		 * new Date(); var date1 = new Date(date.setDate(date.getDate()+i)); var
		 * date1Str = date1.format("yyyy-MM-dd"); var htmlStr = "<option
		 * value='"+date1Str+"'>"+date1Str+"</option>";
		 * jq_dateInput.append(htmlStr); }
		 */
	}

	function initDateBtn() {
		var date = mor.ticket.util
				.setMyDate(mor.ticket.leftTicketQuery.train_date);
		var nowDate = mor.ticket.util.getNewDate();
		if (mor.ticket.viewControl.bookMode == "fc") {
			var date_back = mor.ticket.util
					.setMyDate(mor.ticket.leftTicketQuery.train_date_back);
			if (date_back <= date) {
				jq("#prevDateBtn").addClass("ui-disabled");
				jq("#nextDateBtn").removeClass("ui-disabled");
			} else if (date_back - nowDate > (initReservedPeriod() - 1) * 86400000) {
				jq("#nextDateBtn").addClass("ui-disabled");
				jq("#prevDateBtn").removeClass("ui-disabled");
			} else {
				jq("#prevDateBtn").removeClass("ui-disabled");
				jq("#nextDateBtn").removeClass("ui-disabled");
			}
		} else {
			if (date < nowDate) {
				jq("#prevDateBtn").addClass("ui-disabled");
				jq("#nextDateBtn").removeClass("ui-disabled");
			} else if (date - nowDate > (initReservedPeriod() - 1) * 86400000) {
				jq("#nextDateBtn").addClass("ui-disabled");
				jq("#prevDateBtn").removeClass("ui-disabled");
			} else {
				jq("#prevDateBtn").removeClass("ui-disabled");
				jq("#nextDateBtn").removeClass("ui-disabled");
			}
		}
	}

	function registerPrevDateBtnClickHandler() {
		jq("#prevDateBtn")
				.off()
				.on(
						"tap",
						function() {
							var date = mor.ticket.util
									.setMyDate(mor.ticket.leftTicketQuery.train_date);
							if (mor.ticket.viewControl.bookMode == "fc") {
								date = mor.ticket.util
										.setMyDate(mor.ticket.leftTicketQuery.train_date_back);
							}
							// var dateBack =
							// mor.ticket.util.setMyDate(mor.ticket.leftTicketQuery.train_date_back);
							var nowDate = mor.ticket.util.getNewDate();
							if (date > nowDate) {
								var preDate = new Date(date.getTime() - 24 * 60
										* 60 * 1000);
								if (mor.ticket.viewControl.bookMode == "fc") {
									mor.ticket.leftTicketQuery.train_date_back = preDate
											.format("yyyy-MM-dd");
								} else {
									mor.ticket.leftTicketQuery.train_date = preDate
											.format("yyyy-MM-dd");
								}
								searchAndShowResult(mor.ticket.leftTicketQuery);
								// mor.ticket.util.contentIscrollTo(0, 0, 0);
							}
							return false;
						});
	}

	function registerNextDateBtnClickHandler() {
		jq("#nextDateBtn")
				.off()
				.on(
						"tap",
						function() {
							var date = mor.ticket.util
									.setMyDate(mor.ticket.leftTicketQuery.train_date);
							if (mor.ticket.viewControl.bookMode == "fc") {
								date = mor.ticket.util
										.setMyDate(mor.ticket.leftTicketQuery.train_date_back);
							}
							var nowDate = mor.ticket.util.getNewDate();
							if (date - nowDate < (initReservedPeriod() - 1) * 86400000) {
								var nextDate = new Date(date.getTime() + 24
										* 60 * 60 * 1000);

								if (mor.ticket.viewControl.bookMode == "fc") {
									mor.ticket.leftTicketQuery.train_date_back = nextDate
											.format("yyyy-MM-dd");
								} else {
									mor.ticket.leftTicketQuery.train_date = nextDate
											.format("yyyy-MM-dd");
								}

								searchAndShowResult(mor.ticket.leftTicketQuery);
								// mor.ticket.util.contentIscrollTo(0, 0, 0);

							}
							return false;
						});
	}

	function registerChooseDateBtnClickHandler() {
		jq("#chooseDateBtn").bind("tap", function(e) {
			jq('#chooseDateInput').scroller('show');
			// jq("#chooseDateInput").scroller('setDate',
			// mor.ticket.util.getNewDate(), true);
			/*
			 * var sel = document.getElementById("chooseDateInput"); var
			 * mousedownEvent = document.createEvent ("MouseEvent");
			 * mousedownEvent.initMouseEvent ("mousedown", true, true, window,
			 * 0, 149, 149, 127, 127, false, false, false, false, 0, null);
			 * sel.dispatchEvent (mousedownEvent); var mousedownEvent =
			 * document.createEvent ("MouseEvent");
			 * mousedownEvent.initMouseEvent ( "mousedown", true, true, window,
			 * 1, 0, 0, 0, 0, false, false, false, false, 0, null);
			 * sel.dispatchEvent (mousedownEvent);
			 */
			return false;
		});
	}

	function registerChooseDateScrollerChangeListener() {
		jq("#chooseDateInput")
				.bind(
						"change",
						function() {
							if (mor.ticket.leftTicketQuery.train_date != jq(
									this).val()) {
								if (mor.ticket.viewControl.bookMode == "fc") {
									mor.ticket.leftTicketQuery.train_date_back = jq(
											this).val();
								} else {
									mor.ticket.leftTicketQuery.train_date = jq(
											this).val();
								}
								searchAndShowResult(mor.ticket.leftTicketQuery);
							}
						});
	}

	function getLiClass(index) {
		switch (index % 4) {
		case 0:
			return "ui-block-a";
		case 1:
			return "ui-block-b";
		case 2:
			return "ui-block-c";
		case 3:
			return "ui-block-d";
		default:
			return "ui-block-e";

		}
	}
	function initReservedPeriod() {
		var model = mor.ticket.leftTicketQuery;
		var reservedPeriodType = '';
		if (model.purpose_codes == '0X') {
			reservedPeriodType = '3';
		} else if (model.purpose_codes == '1F') {
			reservedPeriodType = 'n';
		} else {
			reservedPeriodType = '1';
		}
		var reservePeriod = parseInt(window.ticketStorage
				.getItem("reservePeriod_" + reservedPeriodType), 10);
		return reservePeriod;
	}

	var ticketListTemplate = "{{~it :ticket:index}}"
			+ "<li data-index='{{=index}}' data-iconshadow='false' data-icon='none'>"
			+ "<a><div class='ticket-train-row'>"
			+ "<div class='ticket-train-code-col'>{{=ticket.station_train_code}}</div>"
			+ "<div class='ticket-android-fix'><div class='ticket-train-code-txt'><div class='ui-grid-b'>"
			+ "<div class='ui-block-a'>"
			+ "<div class='ticket-station-div'>"
			+ "{{ if(ticket.from_station_telecode == ticket.start_station_telecode) { }}"
			+ "<span class='ticket-station-img ticket-from-station-img'/>"
			+ "{{ }else{ }}"
			+ "<span class='ticket-station-img ticket-pass-station-img'/>"
			+ "{{ } }}"
			+ "{{=mor.ticket.cache.getStationNameByCode(ticket.from_station_telecode)}}</div>"
			+ "<div class='ticket-time'>{{=ticket.start_time}}</div>"
			+ "</div>"
			+ "<div class='ui-block-b'>"
			+ "<div class='ticket-right-arrow'></div>"
			+ "<div class='ticket-duration'>"
			+ "{{ if(ticket.lishiHour != '00'){ }}"
			+ "{{=ticket.lishiHour}}小时"
			+ "{{ } }}"
			+ "{{=ticket.lishiMinute}}分钟"
			+ "</div>"
			+ "</div>"
			+ "<div class='ui-block-c'>"
			+ "<div class='ticket-station-div'><span style='text-align:left;display:inline-block;width:80px;vertical-align:top'>"
			+ "{{ if(ticket.to_station_telecode == ticket.end_station_telecode) { }}"
			+ "<span class='ticket-station-img ticket-to-station-img'/>"
			+ "{{ }else{ }}"
			+ "<span class='ticket-station-img ticket-pass-station-img'/>"
			+ "{{ } }}"
			+ "{{=mor.ticket.cache.getStationNameByCode(ticket.to_station_telecode)}}</span></div>"
			+ "<div class='ticket-time'>{{=ticket.arrive_time}}</div>"
			+ "</div>"
			+ "</div>"
			+ "</div></div></div>"

			+ "<div class='ui-grid-c ticket-info-grid'>"
			+ "{{ for(var i=0;i<ticket.yplist.length;i++) { }}"
			+ "{{ if(ticket.flag != '0'){if(ticket.yplist[i].num ){if(ticket.yplist[i].num <= 20) { }}"
			+ "<span class='{{=ticket.yplist[i].uclass}} s-span-black' ><span>{{=ticket.yplist[i].type}}:</span>{{=ticket.yplist[i].num}}</span>"
			+ "{{ }else{ }}"
			+ "<span class='{{=ticket.yplist[i].uclass}} s-span-green' ><span>{{=ticket.yplist[i].type}}:</span>有</span>"
			+ "{{ }  }}"
			+ "{{ }else{ }}"
			+ "<span class='{{=ticket.yplist[i].uclass}} s-span-grey' ><span>{{=ticket.yplist[i].type}}:</span>无</span>"
			+ "{{ } }}"
			+ "{{ }else{ }}"
			+ "<span class='{{=ticket.yplist[i].uclass}} s-span-grey' ><span>{{=ticket.yplist[i].type}}:</span><span class='ticket-icon-presell'>*</span></span>"
			+ "{{ } }}" + "{{ } }}" + "</div>"
			+ "{{ if(ticket.flag == '0'){ }}"
			+ "<div class='ticket-info-presell'>{{=ticket.message}}</div>"
			+ "{{ } }}"
			// + "{{ if(ticket.discount == '1'){ }}"
			// + "<div class='ticket-train-discount'>折</div>"
			// + "{{ } }}"
			+ "</a>" + "</li>" + "{{~}}";
	var generateListContent = doT.template(ticketListTemplate);
})();