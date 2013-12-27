
/* JavaScript content from js/controller/passengerList.js in folder common */
(function() {
	/*jq("#passengerListView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#passengerListView");
	});*/
	function passengerlistAddFn(){
			jq("#addPassengerBtn").off();
			var util = mor.ticket.util;
			util.clearObject(mor.ticket.passengerDetail);
			mor.ticket.viewControl.isModifyPassenger = false;
			jq("#modifyPassengerView").remove();
			jq.mobile.changePage("modifyPassenger.html", true);
			setTimeout(function(){
				jq("#addPassengerBtn").off().on("tap", passengerlistAddFn);
			},1000);
			return false;
	}
	jq("#passengerListView").live("pageinit", function() {
		jq("#passengerListBackBtn").bind("tap",function(){
			jq.mobile.changePage("my12306.html");
			return false;
		});
		jq("#addPassengerBtn").off().on("tap", passengerlistAddFn);
		var passengers = mor.ticket.passengersCache.passengers;
		registerPassengerListItemClickHandler(passengers);
		registSlider_navClickHandler();
	});
	function psgListFn(){
		var passengers = mor.ticket.passengersCache.passengers;
		if(passengers != null && passengers.length>0){
			jq("#passengerLists").html(generatePassengerList(passengers))
				.listview("refresh");
			/**
			 * 暂时不需要名字首字母排序.
			jq("#slider_navLists").html(slider_navList(passengers));
			var deviceHeight = document.body.clientHeight + window.screenTop;
			var headerHeight = 44;
			var footerHeight = 50;
			var topHeight = 120;
			var sliderHeight = deviceHeight - footerHeight - topHeight - headerHeight;
			var size = parseInt(jq("#passengerListNavLength").val());
			jq('passengerList_slider_nav').height(sliderHeight);
			jq('#slider_navLists').height(sliderHeight);
			jq("#slider_navLists li").height(sliderHeight/size);
			**/
		}
		mor.ticket.viewControl.tab3_cur_page="passengerList.html";
		
		mor.ticket.history.url = 'passengerList';
	
	}
	jq("#passengerListView").live("pageinit", function() {
		if(mor.ticket.loginUser.isAuthenticated === "Y"){
				psgListFn();
			}else{
				if (window.ticketStorage.getItem("autologin") != "true") {
					autologinFailJump()
				} else {
					registerAutoLoginHandler(psgListFn, autologinFailJump);
				}
	
			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	function registerPassengerListItemClickHandler(passengers) {
		jq("#passengerLists").off().on("tap", "li",function(e) {
			e.stopImmediatePropagation();
		    var index = jq(this).attr("data-index");
			var selectedPassenger = passengers[index];
			var util = mor.ticket.util;
			util.clearObject(mor.ticket.passengerDetail);
			if (selectedPassenger.user_name == mor.ticket.loginUser.realName
					&& selectedPassenger.id_type == mor.ticket.loginUser.id_type
				    && selectedPassenger.id_no == mor.ticket.loginUser.id_no) {
				  util.alertMessage("当前用户不可编辑！");
				  return;
				}
			var commonParameters = {
				'name' : selectedPassenger.user_name,
				'card_type' : selectedPassenger.id_type,
				'card_no' : selectedPassenger.id_no,
				'passenger_type' : selectedPassenger.user_type
			};

			var invocationData = {
				adapter : "CARSMobileServiceAdapter",
				procedure : "querySinglePassenger"
			};
			mor.ticket.viewControl.isModifyPassenger = true;
			var options = {
				onSuccess : requestSucceeded,
				onFailure : util.creatCommonRequestFailureHandler()
			};

			mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		});
	};

	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			var passengerDetail = mor.ticket.passengerDetail;
			passengerDetail.name = invocationResult.name;
			passengerDetail.sex_code = invocationResult.sex_code;
			passengerDetail.born_date = invocationResult.born_date;
			passengerDetail.country_code = invocationResult.country_code;
			passengerDetail.card_type = invocationResult.card_type;
			passengerDetail.card_no = invocationResult.card_no;
			passengerDetail.passenger_type = invocationResult.passenger_type;
			passengerDetail.mobile_no = invocationResult.mobile_no;
			passengerDetail.phone_no = invocationResult.phone_no;
			passengerDetail.email = invocationResult.email;
			passengerDetail.address = invocationResult.address;
			passengerDetail.postalcode = invocationResult.postalcode;
			if(invocationResult.studentInfoList.length>0){
				passengerDetail.province_code = invocationResult.studentInfoList[0].province_code;
				passengerDetail.school_code = invocationResult.studentInfoList[0].school_code;
				passengerDetail.department = invocationResult.studentInfoList[0].department;
				passengerDetail.school_class = invocationResult.studentInfoList[0].school_class;
				passengerDetail.student_no = invocationResult.studentInfoList[0].student_no;
				passengerDetail.enter_year = invocationResult.studentInfoList[0].enter_year;
				passengerDetail.school_system = invocationResult.studentInfoList[0].school_system;
				passengerDetail.preference_from_station_code = invocationResult.studentInfoList[0].preference_from_station_code;
				passengerDetail.preference_to_station_code = invocationResult.studentInfoList[0].preference_to_station_code;
				passengerDetail.preference_card_no = invocationResult.studentInfoList[0].preference_card_no;
			}
			jq("#modifyPassengerView").remove();
			jq.mobile.changePage("modifyPassenger.html",true);
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function registSlider_navClickHandler(){
		jq("#passengerListView a[id$='_Anchor']").live("tap",function(){
			var anchor = jq(this).attr("href").charAt(1);
				var curSelector = "#passengerListView #slectpassengers_"+anchor;
				var jqNode=jq(curSelector);
				var top=0;
				while(!jqNode.hasClass('iscroll-scroller')){
					top+=jqNode.position().top;
					jqNode=jqNode.offsetParent();
				}
				
				var screenHeight = jq("#passengerListView .iscroll-wrapper").height();
				var contentHeight = jq("#passengerListView .iscroll-scroller").height();
				var y=(top+screenHeight<=contentHeight)?top:contentHeight-screenHeight;
				jq("#passengerListView .iscroll-wrapper").iscrollview('scrollTo',0,-y,0);
		    return false;
		});
	};
	
	var passengerListTempate = "{{~it :passenger:index}}"
		+ "{{ if(index==0) { }}"
		+ "	<li data-role='list-divider' id='{{=passenger.user_nameSM.toUpperCase().charAt(0)}}'>当前用户</li>"
		+ "{{ } }}"
		+ "{{else if(index==1) { }}"//两个名字首字母相同情况下纠正
		+ "	<li data-role='list-divider' id='slectpassengers_{{=it[index].user_nameSM.toUpperCase().charAt(0)}}'>{{=it[index].user_nameSM.toUpperCase().charAt(0)}}</li>"
		+ "{{ } }}"
		+ "{{else if(passenger.user_nameSM.toUpperCase().charCodeAt(0)!=it[index-1].user_nameSM.toUpperCase().charCodeAt(0)){}}"
		+ "	<li data-role='list-divider' id='slectpassengers_{{=passenger.user_nameSM.toUpperCase().charAt(0)}}'>{{=passenger.user_nameSM.toUpperCase().charAt(0)}}</li>"
		+ "{{}}}" 
		+ "<li data-index='{{=index}}'>"
		+ "<div class='ui-grid-b'>"
		+ "<div class='ui-block-a'>{{=passenger.user_name}}</div>"
		+ "<div class='ui-block-b'>{{=mor.ticket.util.getPassengerTypeName(passenger.user_type)}}</div>"
		+ "<div class='ui-block-c'>{{=passenger.id_no}}</div>"
		+ "<div class='ui-block-d' style='display:none'>{{=passenger.user_nameSM}}</div>"
		+ "</div>"
		+ "</li>"
		+ "{{~}}";
	var generatePassengerList = doT.template(passengerListTempate);

	var slider_navListTempate = 
		"{{var size=0;}}"
		+ "{{ for(var i=1;i<it.length;i++){ }}"
		+ "	{{if(i==1){}}"
		+ "	    <li><a id='{{=it[i].user_nameSM.toUpperCase().charAt(0)}}_Anchor' href='#{{=it[i].user_nameSM.toUpperCase().charAt(0)}}' class='anchor'>{{=it[i].user_nameSM.toUpperCase().charAt(0)}}</a></li>"
		+ " {{size++;}}"
		+ "	{{}}}"
		+ "	{{else if(it[i].user_nameSM.toUpperCase().charCodeAt(0)!=it[i-1].user_nameSM.toUpperCase().charCodeAt(0)){}}"
		+ "	    <li><a id='{{=it[i].user_nameSM.toUpperCase().charAt(0)}}_Anchor' href='#{{=it[i].user_nameSM.toUpperCase().charAt(0)}}' class='anchor'>{{=it[i].user_nameSM.toUpperCase().charAt(0)}}</a></li>"
		+ " {{size++;}}"
		+ "	{{}}}"
		+ "{{ } }}"
		+ " <input id='passengerListNavLength'  type='hidden' value='{{=size}}'></input>";
	var slider_navList = doT.template(slider_navListTempate);
})();