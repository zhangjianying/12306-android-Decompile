
/* JavaScript content from js/controller/moreOption.js in folder common */
(function(){
	
	function moreOptionRegistFn(){
		jq("#registOption").off();
		jq("#registView").remove();
//		jq("#registView_basicInfo").remove();
		mor.ticket.registInfo=[];
//		jq.mobile.changePage("regist_basicInfo.html");
		if (!busy.isVisible()) {
			busy.show();
		}
		jq.mobile.changePage("regist.html");
		setTimeout(function(){jq("#registOption").off().on("tap",moreOptionRegistFn);},1000);
		return false;
	}
	
	jq("#moreOptionView").live("pageinit", function(){
													
		registerLogoutBtnLisener();
		
		jq("#registOption").off().on("tap",moreOptionRegistFn);
		
		
		jq("#loginDoOption").off().bind("tap",function(){
			//busy.show();
			mor.ticket.viewControl.session_out_page="";
			jq.mobile.changePage(getViewBasePath() + "views/loginTicket.html");
			//setTimeout(function(){jq.mobile.changePage("regist.html");},0);
			return false;
		});
		
		
		
		
	});
	function moreOptionFn(){
		var user = mor.ticket.loginUser;
		var name = user.realName;
		jq("#logoutBtn").before('<span class="loginName">'+name+"</span>");
		jq("#logoutOption").show();
	}
	jq("#moreOptionView").live("pagebeforeshow", function(){
		mor.ticket.viewControl.tab4_cur_page="";
		var user = mor.ticket.loginUser;
		if (user.isAuthenticated === "Y") {
			moreOptionFn();
		} else {
				jq("#loginOption").show();
		}
	});
	
	function registerLogoutBtnLisener(){
		jq("#logoutBtn").off().bind("tap", sendLogoutRequest);
		jq("#adsview").off().bind("tap", showAdsView);
	};
	
	function showAdsView(){
		window.plugins.childBrowser.showAdsView("", {}); 
	}
	
	function sendLogoutRequest(e) {
		mor.ticket.viewControl.bookMode = "dc";
		var util = mor.ticket.util;
		var user = mor.ticket.loginUser;
		var commonParameters = {
			'baseDTO.user_name': user["username"]
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "logout"
		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: requestSucceeded
		};
		
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	function requestFailure(result){
		//clean session time out page url
		mor.ticket.viewControl.session_out_page = "";
		mor.ticket.util.creatCommonRequestFailureHandler(result);
	}
	
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}	
		
		
		//window.ticketStorage.setItem("autologin",false);
		
		//jq("#autologinChkbox").attr("checked", false);
		//clean session time out page url
		mor.ticket.viewControl.session_out_page = "";
		mor.ticket.loginUser["isAuthenticated"]="N";
		//mor.ticket.util.alertMessage("您已成功退出");
		mor.ticket.viewControl.tab1_cur_page="";
		mor.ticket.viewControl.tab2_cur_page="";
		mor.ticket.viewControl.tab3_cur_page="";
		mor.ticket.viewControl.tab4_cur_page="";
		mor.ticket.viewControl.current_tab="";
		mor.ticket.leftTicketQuery.train_date = "";
		mor.ticket.leftTicketQuery.from_station_telecode = "";
		mor.ticket.leftTicketQuery.to_station_telecode = "";
		mor.ticket.loginUser.password="";	
		mor.ticket.loginUser['password']="";
		WL.EncryptedCache.open("wlkey",true,onOpenComplete,onOpenError);
		clearUserCache();
		jq("#selectPassengerView").remove();
		jq("#queryOrderView").remove();
		jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
	}
	function clearUserCache() {
				//window.ticketStorage.setItem("password", user.password);
				WL.EncryptedCache.open("wlkey",true,onOpenComplete,onOpenError);
	}

	function onOpenComplete(){ 
		WL.EncryptedCache.write("userPW", "*", 
				function(){
//					WL.Logger.debug("Successfully write to storage");
					WL.EncryptedCache.close(function(){
//						WL.Logger.debug("Encrypted Cache Closed.");
					}, 
				function(){/*WL.Logger.debug("Failed to close Encrypted Cache.");*/});},
				function(){/*WL.Logger.debug("Failed to write to ticketStorage");*/});
	}
	
	function onOpenError(status){
//		WL.Logger.debug("Cannot open encrypted cache");
	}
})();