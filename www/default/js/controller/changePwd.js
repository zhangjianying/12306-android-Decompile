
/* JavaScript content from js/controller/changePwd.js in folder common */
(function() {
	/*jq("#changePwdView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#changePwdView");
	});*/
	var focusArray=[];
	jq("#changePwdView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#oldPasswdInput',focusArray);
		util.enableAutoScroll('#newPasswdInput',focusArray);
		util.enableAutoScroll('#confirmPasswdInput',focusArray);
	}
	
	jq("#changePwdView").live("pageinit", function() {
		//mjl
		registerAutoScroll();
		
		jq("#changPwdBackBtn").bind("tap",function(){
			jq.mobile.changePage("my12306.html");
			return false;
		});
		
		jq("#changPwdBtn").bind("tap", function() {
			var util = mor.ticket.util;
			if(util.isNoValue(jq("#oldPasswdInput").val())){
				util.alertMessage("请填写原密码");
				return;
			}
			if(util.isNoValue(jq("#newPasswdInput").val())){
				util.alertMessage("请填写新密码");
				return;
			}
			
			if(util.isNoValue(jq("#confirmPasswdInput").val())){
				util.alertMessage("请填写确认密码");
				return;
			}
			var newPasswd = jq("#newPasswdInput").val();
			if(newPasswd.length<6){
				util.alertMessage("新密码的长度必须大于6位");
				return;
			}
			if(jq("#confirmPasswdInput").val()!=jq("#newPasswdInput").val()){
				util.alertMessage("确认密码不等于新密码");
				return;
			}
			var commonParameters = {
				'oldPassWd': hex_md5(jq("#oldPasswdInput").val()),
				'newPassWd': hex_md5(jq("#newPasswdInput").val())
			};
			
			var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "changePass"
			};
			
			var options =  {
					onSuccess: requestSucceeded,
					onFailure: util.creatCommonRequestFailureHandler()
			};
			
			mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
			return false;
		});
	});
	jq("#changePwdView").live("pagebeforeshow", function() {
		jq("#oldPasswdInput").val("");
		jq("#newPasswdInput").val("");
		jq("#confirmPasswdInput").val("");
		var user = mor.ticket.loginUser;
		if (user.isAuthenticated === "Y") {
			mor.ticket.viewControl.tab3_cur_page="changePwd.html";
		} else {
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
				} else {
				registerAutoLoginHandler(function(){mor.ticket.viewControl.tab3_cur_page="changePwd.html";
}, autologinFailJump);
			}
		}
	});
	
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		var util = mor.ticket.util;		
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			util.alertMessage(invocationResult.error_msg);
			mor.ticket.loginUser["isAuthenticated"]="N";
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		} else {
			util.alertMessage(invocationResult.error_msg);
		}
	}

})();