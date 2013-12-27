
/* JavaScript content from js/controller/regist_relationType.js in folder common */
(function(){
	var prevPage;
	/*jq("#registView_relation").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#registView_relation");
	});*/
	var focusArray=[];
	jq("#registView_relation").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#regist_mobile',focusArray);
		util.enableAutoScroll('#regist_phone',focusArray);
		util.enableAutoScroll('#regist_email',focusArray);
		util.enableAutoScroll('#regist_address',focusArray);
		util.enableAutoScroll('#regist_postalCode',focusArray);
	}
	
	
	jq("#registView_relation").live("pageinit", function() {
		//mjl
		registerAutoScroll();
		jq("#regist_relationNextBtn").bind("click",function(){
			if(validata()){
				common();
			jq.mobile.changePage("regist_otherInfo.html");
			return false;
			}
		});
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		jq("#regist_relationBackBtn").bind("click",function(){
			jq.mobile.changePage("regist_detailsInfo.html");
		});
		jq("#regist_mobile").blur(function () { 
			if(util.isNoValue(jq("#regist_mobile").val())){
				util.alertMessage("请输入手机号码");
				return;
			}
			if(!checkFormUtil.isMobile(jq("#regist_mobile").val())){
				util.alertMessage("请输入正确的手机号码!");
				return;
			}
		} );
		
		jq("#regist_email").blur(function () { 
			if(util.isNoValue(jq("#regist_email").val())){
				util.alertMessage("请输入邮箱地址");
				return;
			}
			if(!checkFormUtil.isEmail(jq("#regist_email").val())){
				util.alertMessage("请输入正确的邮箱地址!");
				return;
			}
		} );
		
		jq("#regist_postalCode").blur(function () { 
			if(!util.isNoValue(jq("#regist_postalCode").val())&&!checkFormUtil.isZipCode(jq("#regist_postalCode").val())){
				util.alertMessage("您输入的邮编不是有效的格式!");
				return;
			}
		} );
		
	//init
	});
	
	function validata(){
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		
		if(util.isNoValue(jq("#regist_mobile").val())){
			util.alertMessage("请输入手机号码");
			return;
		}
		
		if(!checkFormUtil.isMobile(jq("#regist_mobile").val())){
			util.alertMessage("请输入正确的手机号码！");
			return;
		}
		
		if(util.isNoValue(jq("#regist_email").val())){
			util.alertMessage("请输入邮箱地址");
			return;
		}
		
		if(!checkFormUtil.isEmail(jq("#regist_email").val())){
			util.alertMessage("请输入正确的邮箱地址！");
			return;
		}
		
		if(!util.isNoValue(jq("#regist_postalCode").val())&&!checkFormUtil.isZipCode(jq("#regist_postalCode").val())){
			util.alertMessage("您输入的邮编不是有效的格式！");
			return;
		}
		return true;
	}
	
	jq("#registView_relation").live("pagebeforeshow", function(e, data) {
		mor.ticket.viewControl.tab4_cur_page="regist_relationType.html";
//		refreshPassCode();		
		//jq.mobile.hidePageLoadingMsg();
		prevPage = data.prevPage.attr("id");
		var user =mor.ticket.loginUser;
		if(user.isAuthenticated==="Y"){
			jq("#registBack_RelationValue").html("更多功能");
		}else
			{
			
			if (window.ticketStorage.getItem("autologin") != "true") {
				jq("#registBack_RelationValue").html("登录");
			} else {
				registerAutoLoginHandler(function() {
					jq("#registBack_RelationValue").html("更多功能");
				}, function() {
					jq("#registBack_RelationValue").html("登录");
				});
			}
			
			}
		jq("#registBack_RelationBtn").bind("tap",function(){
			if("更多功能"===jq("#registBack_RelationValue").html())
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
		mor.ticket.registInfo.mobile_no=jq("#regist_mobile").val();
		mor.ticket.registInfo.phone_no=jq("#regist_phone").val();
		mor.ticket.registInfo.email=jq("#regist_email").val();
		mor.ticket.registInfo.address=replaceChar(jq("#regist_address").val());
		mor.ticket.registInfo.postalcode=jq("#regist_postalCode").val();
	}
})();