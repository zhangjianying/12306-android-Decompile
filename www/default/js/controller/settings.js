
/* JavaScript content from js/controller/settings.js in folder common */
(function() {
	/*
	 * jq("#settingsView").live("pagecreate", function() {
	 * mor.ticket.util.androidRemoveIscroll("#settingsView"); });
	 */

	var focusArray = [];
	jq("#settingsView").live("pageshow", function() {
		focusArray = [];
	});
	function registerAutoScroll() {
		var util = mor.ticket.util;
		if (util.isIPhone()) {
			util.enableAutoScroll('#setting_train_date', focusArray);
		}
	}

	jq("#settingsView").live(
			"pageinit",
			function() {

				jq.mobile.defaultHomeScroll = 0;

				registerAutoScroll();

				jq("#settingsBackBtn").off().bind("tap", function() {
					jq.mobile.changePage("moreOption.html");
					return false;
				});

				jq("#setting_train_date").off().bind(
						"change",
						function() {
							jq(this).closest('.ui-btn').find(
									'.ui-btn-text span').html(
									jq(this).find(':checked').text());
							window.ticketStorage.setItem("set_train_date_type",
									jq(this).val());
						});
				mor.ticket.util
						.bindSelectFocusBlurListener("#setting_train_date");

				jq("#setting_from_station").off().bind("tap", function() {

					mor.ticket.views.selectStation.isFromStation = true;

					jq.mobile.changePage("stationSet.html");
					return false;
				});

				jq("#setting_to_station").off().bind("tap", function() {
					mor.ticket.views.selectStation.isFromStation = false;
					jq.mobile.changePage("stationSet.html");
					return false;
				});

				// jq('#setting_train_date').scroller({
				// preset : 'select',
				// theme : 'ios',
				// display : 'modal',
				// mode : 'scroller',
				// setText:'确认',
				// cancelText:'取消',
				// inputClass : 'i-txt',
				// height:40,
				// showLabel:true
				// });

				var set_train_date_type = window.ticketStorage
						.getItem("set_train_date_type");
				var util = mor.ticket.util;
				util.setCustomSelectScrollerValue({
					id : "setting_train_date",
					value : set_train_date_type,
					label : util.getTrainDateTypeByCode(set_train_date_type)
				});

			});

	jq("#settingsView").live(
			"pagebeforeshow",
			function() {

				mor.ticket.viewControl.tab4_cur_page = "settings.html";

				var model = mor.ticket.leftTicketQuery;
				var cache = mor.ticket.cache;
				var set_from_station_telecode = window.ticketStorage
						.getItem("set_from_station_telecode");
				if (set_from_station_telecode == null
						|| set_from_station_telecode == "") {
					set_from_station_telecode = "BJP";
					window.ticketStorage.setItem("set_from_station_telecode",
							set_from_station_telecode);
				}
				var set_to_station_telecode = window.ticketStorage
						.getItem("set_to_station_telecode");
				if (set_to_station_telecode == null
						|| set_to_station_telecode == "") {
					set_to_station_telecode = "SHH";
					window.ticketStorage.setItem("set_to_station_telecode",
							set_to_station_telecode);
				}
				var set_train_date_type = window.ticketStorage
						.getItem("set_train_date_type");
				if (set_train_date_type == null || set_train_date_type == "") {
					set_train_date_type = "1";
					window.ticketStorage.setItem("set_train_date_type",
							set_train_date_type);
				} else {
					jq(
							"#setting_train_date option[value="
									+ set_train_date_type + "]").attr(
							"selected", "selected");
					jq("#setting_train_date").selectmenu('refresh', true);
				}

				jq("#setting_from_station").val(
						cache.getStationNameByCode(set_from_station_telecode));
				jq("#setting_to_station").val(
						cache.getStationNameByCode(set_to_station_telecode));
				model.from_station_telecode = set_from_station_telecode;
				model.to_station_telecode = set_to_station_telecode;

				/*
				 * util.setCustomSelectScrollerValue({ id: "setting_train_date",
				 * value: set_train_date_type, label:
				 * util.getTrainDateTypeByCode(set_train_date_type) });
				 */

			});

})();