
/* JavaScript content from js/controller/getPwd.js in folder common */
(function() {
//	jq("#getPwdView").live("pagecreate", function() {
//		mor.ticket.util.androidRemoveIscroll("#getPwdView");
//	});
	var focusArray=[];
	jq("#getPwdView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#getPwd_email',focusArray);
		util.enableAutoScroll('#getPwd_verify',focusArray);
		
		util.enableAutoScroll('#getPwd2_username',focusArray);
		util.enableAutoScroll('#getPwd2_pwdAnswer',focusArray);
		util.enableAutoScroll('#getPwd2_newPsswd',focusArray);
		util.enableAutoScroll('#getPwd2_confirmPsswd',focusArray);
	}
	
	jq("#getPwdView").live("pageinit", function(){
		registerAutoScroll();
	});
	
	jq("#getPwdView").live("pageshow", function() {
		var util = mor.ticket.util;
		mor.ticket.viewControl.findPwdMode=="email";
		var checkFormUtil = mor.ticket.checkForm;
		// 验证码
		jq("#getpwd_refreshPassCode").bind("tap",refreshPassCode);

		jq("#findPwdBtn").bind("tap",submit);
		
		jq("#getPwd2_getQuestion").bind("tap",getQuestion);
		
		jq("#getPwd1Choice").bind("tap",function(){
			jq("#getPwd1Choice").addClass("ui-btn-active ui-state-persist");
			jq("#getPwd2Choice").removeClass("ui-btn-active ui-state-persist");
			jq("#getPwd2Option").hide();
			jq("#getPwd1Option").show();
			mor.ticket.viewControl.findPwdMode="email";
			return false;
		});
		
        jq("#getPwd2Choice").bind("tap",function(){
        	jq("#getPwd2Choice").addClass("ui-btn-active ui-state-persist");
			jq("#getPwd1Choice").removeClass("ui-btn-active ui-state-persist");
			jq("#getPwd1Option").hide();
			jq("#getPwd2Option").show();
			mor.ticket.viewControl.findPwdMode="passwd";
			return false;
		});
		refreshPassCode();
		
		jq("#getPwd_email").change(function () {
			if(util.isNoValue(jq("#getPwd_email").val())){
				util.alertMessage("请输入邮箱地址");
				return;
			}
			
			if(!checkFormUtil.isEmail(jq("#getPwd_email").val())){
				util.alertMessage("请输入正确的邮箱地址");
				return;
			}
		});
		
		jq("#getPwd2_username").change(function () {
			if(util.isNoValue(jq("#getPwd2_username").val())){
				util.alertMessage("请输入用户名");
				return;
			}
		});
		
		jq("#getPwd2_pwdAnswer").change(function () {
			if(util.isNoValue(jq("#getPwd2_pwdAnswer").val())){
				util.alertMessage("密码提示答案不能为空");
				return;
			}
		});
		
		jq("#getPwd2_newPsswd").change(function () {
			if(util.isNoValue(jq("#getPwd2_newPsswd").val())){
				util.alertMessage("新密码不能为空");
				return;
			}
			
			if(jq("#getPwd2_newPsswd").val().length<6){
				util.alertMessage("密码的长度必须大于6位");
				return;
			}
		});
		
		jq("#getPwd2_confirmPsswd").change(function () {
			if(util.isNoValue(jq("#getPwd2_confirmPsswd").val())){
				util.alertMessage("确认密码不能为空");
				return;
			}
			if(jq("#getPwd2_confirmPsswd").val()!=jq("#getPwd2_newPsswd").val()){
				util.alertMessage("确认密码和新密码不同");
				return;
			}
		});
		
		jq("#getPwd_verify").change(function () {
			if(util.isNoValue(jq("#getPwd_verify").val())){
				util.alertMessage("请填写验证码");
				return;
			}
			
			if(!checkFormUtil.checkNum(jq("#getPwd_verify").val())){
				util.alertMessage("验证码只能为数字");
				return;
			}
		});
	});


	function refreshPassCode(){	
		var commonParameters = {
			'baseDTO.user_name': ''
		};
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "postPassCode"
		};
		var options = {
				onSuccess: requestPassCodeSucceeded,
				onFailure: mor.ticket.util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function requestPassCodeSucceeded(result){
		var util = mor.ticket.util;		
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (invocationResult.isSuccessful &&
				invocationResult.succ_flag === "1") {
			jq("#getPwd_verifyImage").attr("src", "data:image/gif;base64," + invocationResult.passcode);
		} else {
			util.alertMessage(invocationResult.error_msg);
		}
	}

	function submit(){
		validate();
		return false;
	}
	
	function validate(){
		var util = mor.ticket.util;	
		var checkFormUtil = mor.ticket.checkForm;
		if(util.isNoValue(jq("#getPwd_verify").val())){
			util.alertMessage("请填写验证码");
			return;
		}
		
		if(!checkFormUtil.checkNum(jq("#getPwd_verify").val())){
			util.alertMessage("验证码只能为数字");
			return;
		}
		if(mor.ticket.viewControl.findPwdMode=="email")
		{
			if(util.isNoValue(jq("#getPwd_email").val())){
				util.alertMessage("请输入邮箱地址");
				return;
			}
			
			if(!checkFormUtil.isEmail(jq("#getPwd_email").val())){
				util.alertMessage("请输入正确的邮箱地址");
				return;
			}
			
			var commonParameters = {
				'baseDTO.user_name' : '',
				'email': jq("#getPwd_email").val() ,
				'randCode': jq("#getPwd_verify").val() 	
			};
			var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "findPwdByMail"
			};
			
			var options =  {
					onSuccess: requestSucceeded,
					onFailure: util.creatCommonRequestFailureHandler()
			};
			mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		}else if(mor.ticket.viewControl.findPwdMode=="passwd"){
			if(util.isNoValue(jq("#getPwd2_username").val())){
				util.alertMessage("请输入用户名");
				return;
			}
			
			if(util.isNoValue(jq("#getPwd2_pwdQuestion").val())){
				util.alertMessage("密码提示问题不能为空");
				return;
			}
			
			if(util.isNoValue(jq("#getPwd2_pwdAnswer").val())){
				util.alertMessage("密码提示答案不能为空");
				return;
			}
			
			if(util.isNoValue(jq("#getPwd2_newPsswd").val())){
				util.alertMessage("新密码不能为空");
				return;
			}
			
			if(jq("#getPwd2_newPsswd").val().length<6){
				util.alertMessage("密码的长度必须大于6位");
				return;
			}
			
			if(util.isNoValue(jq("#getPwd2_confirmPsswd").val())){
				util.alertMessage("确认密码不能为空");
				return;
			}
			if(jq("#getPwd2_confirmPsswd").val()!=jq("#getPwd2_newPsswd").val()){
				util.alertMessage("确认密码和新密码不同");
				return;
			}
			
			var commonParameters = {
				'baseDTO.user_name' : '',
				'retrieve_name': jq("#getPwd2_username").val() ,
				'password_new': hex_md5(jq("#getPwd2_newPsswd").val()),
				'pwd_question': jq("#getPwd2_pwdQuestion").val() ,
				'pwd_answer': jq("#getPwd2_pwdAnswer").val() ,
				'randCode': jq("#getPwd_verify").val()  	
			};			
			
			var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "findPwdByAnswer"
			};
			
			var options =  {
					onSuccess: requestSucceeded,
					onFailure: util.creatCommonRequestFailureHandler()
			};
			
			mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		}
		
	}
	
	
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			var msg = "";
			if(mor.ticket.viewControl.findPwdMode=="email"){
				msg = "找回密码成功！我们已经向邮箱"+jq("#getPwd_email").val()+"发送了一封密码找回邮件，请您登录您的邮箱"+jq("#getPwd_email").val()+"获取密码信息。";
			}else if(mor.ticket.viewControl.findPwdMode=="passwd"){
				msg = "找回密码成功";
			}
			mor.ticket.util.alertMessage(msg);
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		} else {
			refreshPassCode();
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function getQuestion(){
		var util = mor.ticket.util;	
		if(util.isNoValue(jq("#getPwd2_username").val())){
			util.alertMessage("请输入用户名");
			return;
		}
		
		var commonParameters = {
			'baseDTO.user_name' : jq("#getPwd2_username").val()
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "findAnswerByUname"	
		};
		var options = {
				onSuccess: requestAnswerSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function requestAnswerSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			jq("#getPwd2_pwdQuestion").val(invocationResult.message);
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
})();