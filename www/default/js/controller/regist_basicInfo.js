
/* JavaScript content from js/controller/regist_basicInfo.js in folder common */
(function(){
	var prevPage;
	/*jq("#registView_basicInfo").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#registView_basicInfo");
	});*/
	var focusArray=[];
	jq("#registView_basicInfo").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#regist_user_name',focusArray);
		util.enableAutoScroll('#regist_passwd',focusArray);
		util.enableAutoScroll('#regist_confirmPasswd',focusArray);
		util.enableAutoScroll('#regist_ivrPasswd',focusArray);
		util.enableAutoScroll('#regist_confirmivrPasswd',focusArray);
		util.enableAutoScroll('#regist_pwdAnswer',focusArray);
	}
	
	jq("#registView_basicInfo").live("pageinit", function() {
		var util = mor.ticket.util;
		//注册iphone自动scroll
		registerAutoScroll();
		// 下一步按钮
		jq("#regist_basicNextBtn").bind("click",function(){	
			if(validata()){
				common();
			jq.mobile.changePage("regist_detailsInfo.html");
			return false;
			}
		});
		var checkFormUtil = mor.ticket.checkForm;
		function changeQuestionEvent(){
			if(jq("#regist_pwd_question").val()=="1"){
				jq("#regist_otherQuestionOption").show();
			}else{
				jq("#regist_otherQuestionOption").hide();
			}
			contentIscrollRefresh();
		}
		
		function contentIscrollRefresh(){
			if(jq("#registView_basicInfo .ui-content").attr("data-iscroll")!=undefined){
				jq("#registView_basicInfo .ui-content").iscrollview("refresh");
			}
		}
		// 提示问题
		jq("#regist_pwd_question").bind("change",changeQuestionEvent);
		// 用户名非空长度验证
	jq("#regist_user_name").blur(function () { 
			
			if(util.isNoValue(jq("#regist_user_name").val())){
				util.alertMessage("请填写用户名");
				return;
			}
			
			if(!checkFormUtil.validateUsersName(jq("#regist_user_name").val())){
				util.alertMessage("用户名只能填写字母数字下划线,开头必须为字母,且长度必须在6-30位内!");
				return;
			}
			
			if(jq("#regist_user_name").val().length>29||jq("#regist_user_name").val().length<6){
				util.alertMessage("用户名的长度必须在6-30位内!");
				return;
			}
		} );

	// 密码非空长度验证
	jq("#regist_passwd").blur(function () { 
	if(util.isNoValue(jq("#regist_passwd").val())){
		util.alertMessage("请填写密码");
		return;
	}
	
	if(jq("#regist_passwd").val().length<6){
		util.alertMessage("密码的长度必须大于6位!");
		return;
	}
	});
	// 确认密码
	jq("#regist_confirmPasswd").blur(function () { 
		if(util.isNoValue(jq("#regist_confirmPasswd").val())){
			util.alertMessage("请输入确认密码");
			return;
		}
		
		if(jq("#regist_passwd").val()!=jq("#regist_confirmPasswd").val()){
			util.alertMessage("两次输入的密码不一致!");
			return;
		}
	} );
	
	//语音密码非空长度验证
	jq("#regist_ivrPasswd").blur(function () { 
		if(util.isNoValue(jq("#regist_ivrPasswd").val())){
			util.alertMessage("请填写语音查询密码");
			return;
		}
		if(!checkFormUtil.isZipCode(jq("#regist_ivrPasswd").val())){
			util.alertMessage("语音查询密码只能是6位数字!");
			return;
		}
		
	} );

		//确认语音密码验证	
	jq("#regist_confirmivrPasswd").blur(function () { 
		if(util.isNoValue(jq("#regist_confirmivrPasswd").val())){
			util.alertMessage("请输入语音查询确认密码");
			return;
		}
		if(jq("#regist_ivrPasswd").val()!=jq("#regist_confirmivrPasswd").val()){
			util.alertMessage("语音查询确认密码与密码不一致!");
			return;
		}
			});
	
	// 提示问题非空
	jq("#regist_otherpwdQuestion").blur(function () { 
		if(util.isNoValue(jq("#regist_otherpwdQuestion").val())){
		util.alertMessage("请输入密码提示问题");
		return;
	}
		});
	
	initSelects();
	});
	
	// 滚动
	function contentIscrollRefresh(){
		if(jq("#registView_basicInfo .ui-content").attr("data-iscroll")!=undefined){
			jq("#registView_basicInfo .ui-content").iscrollview("refresh");
		}
	}

	// 提示问题选择弹出滚动
	function initSelects(){		
		jq("#regist_pwd_question").scroller({
			preset : 'select',
			theme : 'ios',
			display : 'modal',
			mode : 'scroller',
			setText:'确定',
	        cancelText:'取消',
			inputClass : 'i-txt',
			height:40,
	        showLabel:true
		});
	}
	
	function validata(){
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		if(util.isNoValue(jq("#regist_user_name").val())){
			util.alertMessage("请填用户名");
			return;
		}
		
		if(!checkFormUtil.validateUsersName(jq("#regist_user_name").val())){
			util.alertMessage("用户名只能填写字母数字下划线,开头必须为字母,且长度必须在6-30位内!");
			return;
		}
		
		if(jq("#regist_user_name").val().length>29||jq("#regist_user_name").val().length<6){
			util.alertMessage("用户名的长度必须在6-30位内！");
			return;
		}
		
		if(util.isNoValue(jq("#regist_passwd").val())){
			util.alertMessage("请填写密码");
			return;
		}
		
		if(jq("#regist_passwd").val().length<6){
			util.alertMessage("密码的长度必须大于6位！");
			return;
		}
		
		if(util.isNoValue(jq("#regist_confirmPasswd").val())){
			util.alertMessage("请输入确认密码");
			return;
		}
		
		if(jq("#regist_passwd").val()!=jq("#regist_confirmPasswd").val()){
			util.alertMessage("确认密码与密码不一致!");
			return;
		}
		
		if(util.isNoValue(jq("#regist_ivrPasswd").val())){
			util.alertMessage("请输入语音查询密码");
			return;
		}
		
		if(!checkFormUtil.isZipCode(jq("#regist_ivrPasswd").val())){
			util.alertMessage("语音查询密码只能是6位数字!");
			return;
		}
		
		if(util.isNoValue(jq("#regist_confirmivrPasswd").val())){
			util.alertMessage("请输入语音查询确认密码");
			return;
		}
		
		if(jq("#regist_ivrPasswd").val()!=jq("#regist_confirmivrPasswd").val()){
			util.alertMessage("语音查询确认密码与密码不一致!");
			return;
		}
		
		if(util.isNoValue(jq("#regist_pwd_question").val())){
			util.alertMessage("请选择密码提示问题");
			return;
		}
		
		if(jq("#regist_pwd_question").val()=="1"&&util.isNoValue(jq("#regist_otherpwdQuestion").val())){
			util.alertMessage("请输入您自定义的密码提示问题");
			return;
		}
		
		if(util.isNoValue(jq("#regist_pwdAnswer").val())){
			util.alertMessage("请输入密码提示问题答案");
			return;
		}
		return true;
		
	}
	jq("#registView_basicInfo").live("pagebeforeshow", function(e, data) {
		prevPage = data.prevPage.attr("id");
		mor.ticket.viewControl.tab4_cur_page="regist_basicInfo.html";
		
		var user=mor.ticket.loginUser;
		if(user.isAuthenticated==="Y"){
			jq("#registBack_BasciValue").html("更多功能");
		}
		else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				jq("#registBack_BasciValue").html("登录");
			} else {
				registerAutoLoginHandler(function(){
					jq("#registBack_BasciValue").html("更多功能");
				}, function(){
					jq("#registBack_BasciValue").html("登录");

				});
			}
		}
		
		
		jq("#registBack_BasicBtn").bind("tap",function(){
			
			if("更多功能"===jq("#registBack_BasciValue").html())
				{
				jq.mobile.changePage("moreOption.html");
				}
			else{
				jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
			}
			return false;
		});
	});
	// 替换特殊字符
	function replaceChar(str) {
		var v = str.replace(/['"<> ?]/g,"");
		return v;
	}
	function common(){
		mor.ticket.registInfo.user_name=replaceChar(jq("#regist_user_name").val());
		mor.ticket.registInfo.password=hex_md5(jq("#regist_passwd").val());
		mor.ticket.registInfo.pwd_question=jq("#regist_pwd_question").val()=="1"?jq("#regist_otherpwdQuestion").val():jq("#regist_pwd_question").val();
		mor.ticket.registInfo.pwd_answer= jq("#regist_pwdAnswer").val();
		mor.ticket.registInfo.IVR_passwd=jq("#regist_ivrPasswd").val();
	}
	
})();