
/* JavaScript content from js/controller/login1.js in folder common */
(function(){
	/*jq("#loginView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#loginView");
	});*/
	var focusArray=[];
	jq("#loginView").live("pageshow", function() {
		focusArray=[];
	});
	
	function registerAutoScroll(){	
		var util = mor.ticket.util;	
		if(util.isIPhone()){	
			util.enableAutoScroll('#usernameInput',focusArray);
			util.enableAutoScroll('#passwordInput',focusArray);
		}
	}
	
	jq("#loginView").live("pageinit", function(){
		jq.mobile.defaultHomeScroll = 0;
		
		var user=mor.ticket.loginUser;		
		user.isKeepUserPW = (window.ticketStorage.getItem("isKeepUserPW") == null ? true : window.ticketStorage.getItem("isKeepUserPW"));
		user.username     = window.ticketStorage.getItem("username");
		
		
	//	WL.Logger.debug("rizhi");

		registerMoreOptionBtnLisener();
		
		initKeepUsrChkbox(user); 		
		registerKeepUsrChkboxListener(user);			
		registerLoginBtnLisener();		
		registerRegisterBtnLisener();	
		registerAutoScroll();
		
		initAutoLoginChkbox();
		
		
		//registerInputChangeLisener();	
		//registerCleanInputLisener();
		
	   //jq.mobile.loadPage(vPathViewCallBack()+"MobileTicket.html");//pre-load
		
		
		
	});
	

	
	
	jq("#loginView").live("pagebeforeshow", function(){
		//clear pass if no need store pass
		var user =  mor.ticket.loginUser;
		if (!user.isKeepUserPW) {
			jq("#passwordInput").val("");
		}
		if(window.ticketStorage.getItem("isKeepUserPW") == null){
			window.ticketStorage.setItem("isKeepUserPW", true);
		}
		


	});
	
	function registerInputChangeLisener(){		
		jq("#usernameInput").bind("keyup  focus",function(){
			jq("#deletePassword").hide();
			if(jq(this).val()){
				jq("#deleteUserName").show();
			} else {
				jq("#deleteUserName").hide();
			}
		});
		
		jq("#passwordInput").bind("keyup  focus",function(){
			jq("#deleteUserName").hide();
			if(jq(this).val()){
				jq("#deletePassword").show();
			}else {
				jq("#deletePassword").hide();
			}
		});
	}
	
	function registerCleanInputLisener(){
		jq("#deleteUserName").bind("tap",function(){
			jq("#usernameInput").val("");
			jq("#deleteUserName").hide();
			jq("#usernameInput").focus();			return false;
		});
		jq("#deletePassword").bind("tap",function(){
			jq("#passwordInput").val("");
			jq("#deletePassword").hide();
			jq("#passwordInput").focus();			return false;
		});
	}
	
	function initKeepUsrChkbox(user){
		var isChecked;
		if(user.isKeepUserPW === "true" || user.isKeepUserPW == true){
			isChecked = true;
		}
		else{
			isChecked = false;
		}
		jq("#keepUsrChkbox").attr("checked", isChecked);
	};
	
	
	function initAutoLoginChkbox(){

		var isChecked;
		
		var autologin =  window.ticketStorage.getItem("autologin");
	
		
		if(autologin === "true" || autologin == true){
			isChecked = true;
		}
		else{
			isChecked = false;
		}
		jq("#autologinChkbox").attr("checked", isChecked);

	};
	
	
	
	
	function registerKeepUsrChkboxListener(user){
		jq("#keepUsrChkbox").bind("change", function(event){
			user.isKeepUserPW = event.target.checked;
			window.ticketStorage.setItem("isKeepUserPW", user.isKeepUserPW);
			return false;
		});
		
		
		 //  注册 自动登录方法
		jq("#autologinChkbox").bind("change", function(event){
			if ( event.target.checked){
				window.ticketStorage.setItem("autologin",true);
			}else{
				window.ticketStorage.setItem("autologin",false);
			}
			return false;
		});
		
		
					//window.ticketStorage.setItem("autologin", true);

	};
	
	function registerLoginBtnLisener(){
		jq("#loginBtn").bind("tap", sendLoginRequest);
	};
	
	
	
	function registerMoreOptionBtnLisener(){
		
		jq("#moreOptionBtn").bind("tap", function(){
			jq.mobile.changePage(vPathCallBack()+"moreOption.html");
		});
		
	};
	
	
	
	

	
	
	function registerRegisterBtnLisener(){
		jq("#registerBtn").bind("tap", function(){
			jq("#registView").remove();
//			jq("#registView_basicInfo").remove();
			mor.ticket.registInfo=[];
			//单页注册
//			jq.mobile.changePage("regist_basicInfo.html",true);
			jq.mobile.changePage(vPathCallBack()+"regist.html",true);
			return false;
		});
	};
	
	
	
	
	function sendLoginRequest(e) {
		if(jq("#usernameInput").val()==""){
			mor.ticket.util.alertMessage("请填写用户名");
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
			return;
		}
		
		if(jq("#passwordInput").val()==""){
			mor.ticket.util.alertMessage("请填写密码");
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
			return;
		}
		
			
		var util = mor.ticket.util;		
//		set commonParameters and let mor.ticket.util.invokeWLProcedure method to merge the parameters
		var commonParameters = {
			'baseDTO.user_name': jq("#usernameInput").val(),
			'password': hex_md5(jq("#passwordInput").val())
			//'autologinChkbox': jq("#autologinChkbox").val()
		};
		
//		var commonParameters = util.prepareRequestCommonParameters({
//			'baseDTO.user_name': jq("#usernameInput").val(),
//			'password': hex_md5(jq("#passwordInput").val())
//		});
		
		var invocationData = {
			adapter: "CARSMobileServiceAdapter",
			procedure: "login"
		};
		
//		var invocationData = {
//				adapter: "AuthenticationAdapter",
//				procedure: "submitAuthentication",
//				parameters: [commonParameters]
//		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		
		// set commonParameters to null if no parameters to override
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		
//		WL.Client.invokeProcedure(invocationData, options);
		
		// remove busy.show() if using mor.ticket.util.invokeWLProcedure
//		busy.show();
		return false;
	}
	
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		if(mor.ticket.viewControl.session_out_page == ""){
			mor.ticket.viewControl.tab1_cur_page="";
			mor.ticket.viewControl.tab2_cur_page="";
			mor.ticket.viewControl.tab3_cur_page="";
			mor.ticket.viewControl.tab4_cur_page="";
			mor.ticket.viewControl.current_tab="";
			mor.ticket.leftTicketQuery.train_date = "";
			mor.ticket.leftTicketQuery.from_station_telecode = "";
			mor.ticket.leftTicketQuery.to_station_telecode = "";
			jq("#selectPassengerView").remove();
			jq("#queryOrderView").remove();
			
			
			var invocationResult = result.invocationResult;
			if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
				mor.ticket.loginUser["isAuthenticated"]="Y";
				saveUserPassIfNecessary(mor.ticket.loginUser);
				if(mor.ticket.loginUser["username"]==null||mor.ticket.loginUser["username"]==''){
					mor.ticket.loginUser["username"] = jq("#usernameInput").val();
				}
				
				// init loginUser
				var loginUser=mor.ticket.loginUser;
				loginUser.accountName=invocationResult.user_name;
				loginUser.realName=invocationResult.name;
				loginUser.id_type = invocationResult.id_type_code;
				loginUser.id_no =invocationResult.id_no;
				loginUser.mobile_no = invocationResult.mobileNo;
				loginUser.user_type=invocationResult.user_type;
				loginUser.email=invocationResult.email;
				loginUser.activeUser = invocationResult.is_active;
				//init passengerinfo	
				
				if(invocationResult.passengerResult){
					mor.ticket.passengersCache.passengers=[];
					mor.ticket.passengersCache.passengers=invocationResult.passengerResult;
					mor.ticket.passengersCache.sortPassengers();
				}
				mor.ticket.viewControl.tab1_cur_page="";
				mor.ticket.viewControl.tab2_cur_page="";
				mor.ticket.viewControl.tab3_cur_page="";
				mor.ticket.viewControl.tab4_cur_page="";
				mor.ticket.viewControl.current_tab="";
				WL.SimpleDialog.show(
					"温馨提示", 
					invocationResult.last_msg, 
					[ {text : '确定', handler: function() {jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");}}]
				);	
			} else {	
				mor.ticket.util.alertMessage(invocationResult.error_msg);
			}
		}else{
//			WL.Logger.debug("session_out_page:" + mor.ticket.viewControl.session_out_page);
			var session_out_page = mor.ticket.viewControl.session_out_page;
			mor.ticket.viewControl.session_out_page = "";
			
			
			if(mor.ticket.loginUser.isAuthenticated === "N"){
				var invocationResult = result.invocationResult;
				if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
					mor.ticket.loginUser["isAuthenticated"]="Y";
					saveUserPassIfNecessary(mor.ticket.loginUser);
					if(mor.ticket.loginUser["username"]==null||mor.ticket.loginUser["username"]==''){
						mor.ticket.loginUser["username"] = jq("#usernameInput").val();
					}
					
				
					var loginUser=mor.ticket.loginUser;
					loginUser.accountName=invocationResult.user_name;
					loginUser.realName=invocationResult.name;
					loginUser.id_type = invocationResult.id_type_code;
					loginUser.id_no =invocationResult.id_no;
					loginUser.mobile_no = invocationResult.mobileNo;
					loginUser.user_type=invocationResult.user_type;
					loginUser.email=invocationResult.email;
					loginUser.activeUser = invocationResult.is_active;
					//init passengerinfo	
					
					if(invocationResult.passengerResult){
						mor.ticket.passengersCache.passengers=[];
						mor.ticket.passengersCache.passengers=invocationResult.passengerResult;
						mor.ticket.passengersCache.sortPassengers();
					}
					mor.ticket.viewControl.tab1_cur_page="";
					mor.ticket.viewControl.tab2_cur_page="";
					mor.ticket.viewControl.tab3_cur_page="";
					mor.ticket.viewControl.tab4_cur_page="";
					mor.ticket.viewControl.current_tab="";
				}else {	
				    mor.ticket.util.alertMessage(invocationResult.error_msg);
			    }
				
				
				if (session_out_page.indexOf('views')>=0){
				
					jq.mobile.changePage('../'+session_out_page);
				}else{
					
					jq.mobile.changePage(session_out_page);
				}
				
				
			} else {	
				mor.ticket.util.alertMessage(invocationResult.error_msg);
			}

			
			
			
			
			
			
			
		}
	}
	
	function saveUserPassIfNecessary(user) {
		user.username = jq("#usernameInput").val();
		window.ticketStorage.setItem("username", user.username);
		if (user.isKeepUserPW) {
			user.password = jq("#passwordInput").val();
			if(user.password){
				//window.ticketStorage.setItem("password", user.password);
				WL.EncryptedCache.open("wlkey",true,onOpenComplete,onOpenError);
			}			
		}
	}

	function onOpenComplete(status){ 
		WL.EncryptedCache.write("userPW", mor.ticket.loginUser.password, 
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