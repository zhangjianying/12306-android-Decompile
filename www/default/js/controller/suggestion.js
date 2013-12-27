
/* JavaScript content from js/controller/suggestion.js in folder common */
(function(){
	var focusArray=[];
	jq("#suggestionView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#suggest_username',focusArray);
		util.enableAutoScroll('#suggest_email',focusArray);
		util.enableAutoScroll('#suggest_mobile',focusArray);
		util.enableAutoScroll('#suggest_name',focusArray);
		util.enableAutoScroll('#suggestion',focusArray);
	}
	
	/*jq("#suggestionView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#suggestionView");
	});*/
	jq("#suggestionView").live("pageinit", function(){
		registerAutoScroll();
		jq("#suggestionBackBtn").bind("tap",function(){
													 
			jq.mobile.changePage("moreOption.html");
			return false;
		});
		
		
		
		
		
	});
	function suggestionFn(){
		var user = mor.ticket.loginUser;

		jq("#suggest_username").val(user.accountName);
		jq("#suggest_username").addClass("ui-disabled");
//		jq("#suggest_username").attr("readonly", "readonly");
		jq("#suggest_email").val(user.email);
		jq("#suggest_mobile").val(user.mobile_no);
		jq("#suggest_name").val(user.realName);
		jq("#suggestion").val("");
	    jq("#suggestionBtn").removeClass("ui-btn-right-dis");
	  
	}
	jq("#suggestionView").live("pagebeforeshow", function(){
		
		var user = mor.ticket.loginUser;
		
		if (user.isAuthenticated === "Y") {
			suggestionFn();
		}else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				jq("#suggestionBtn").addClass("ui-btn-right-dis");
				jq("#suggest_username").val("");
				jq("#suggest_username").addClass("ui-disabled");
			} else {
				registerAutoLoginHandler(suggestionFn, function(){
					jq("#suggestionBtn").addClass("ui-btn-right-dis");
					jq("#suggest_username").val("");
					jq("#suggest_username").addClass("ui-disabled");
				});
				
				}

		    

		}
		
		 jq("#suggestionBtn").off().bind("tap",joinFunRequest);
		
		
		
		
		mor.ticket.viewControl.tab4_cur_page="suggestion.html";
	});
	
	
	

	
	function joinFunRequest(){
		var user = mor.ticket.loginUser;
		if (user.isAuthenticated === "Y") {
			sendSuggestionRequest();
		}else{
			AlertSuggestionRequest();
		}
		return;
	}
	
	function AlertSuggestionRequest(){
		
		  WL.SimpleDialog.show("温馨提示", "请登录后提交意见反馈。", [ {
				text : '确定',
				handler : function() {
				}
			} ]);
		
		return;
	}
	
	function sendSuggestionRequest() {
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		if(util.isNoValue(jq("#suggest_email").val())){
			util.alertMessage("请填写邮箱地址");
			return;
		}
		
		if(!checkFormUtil.isEmail(jq("#suggest_email").val())){
			util.alertMessage("请输入正确的邮箱地址");
			return;
		}
		if(util.isNoValue(jq("#suggest_mobile").val())){
			util.alertMessage("请输入手机号码");
			return;
		}
		if(!checkFormUtil.isMobile(jq("#suggest_mobile").val())){
			util.alertMessage("请输入正确的手机号码");
			return;
		}
		if(util.isNoValue(jq("#suggest_name").val())){
			util.alertMessage("请输入姓名");
			return;
		}
		if(util.isNoValue(jq("#suggestion").val())){
			util.alertMessage("请输入反馈意见");
			return;
		}
		
		if(jq("#suggestion").val().length>250||jq("#suggestion").val().length<10){
			util.alertMessage("反馈意见必须大于10个字符，小于250个字符");
			return;
		}
		
		var commonParameters = {
			'username': jq("#suggest_username").val(),
			'tellphone': jq("#suggest_mobile").val(),
			'emaill': jq("#suggest_email").val(),
			'name': jq("#suggest_name").val(),
			'remark': jq("#suggestion").val(),
			'device_name':device.name,
			'device_platform':device.platform,
			'device_version':device.version,
			'agentmsg' : navigator.userAgent
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "getRefundInfo"
		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	
	
	
	
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		var util = mor.ticket.util;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			//util.alertMessage("您已提交反馈信息成功");
			jq.mobile.changePage("moreOption.html");
		} else {
			util.alertMessage(invocationResult.error_msg);
		}
	}
	
	
})();