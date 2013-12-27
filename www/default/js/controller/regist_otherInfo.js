
/* JavaScript content from js/controller/regist_otherInfo.js in folder common */
(function(){
	var prevPage;
	/*jq("#registView_other").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#registView_other");
	});*/
	var focusArray=[];
	jq("#registView_other").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#regist_passCode',focusArray);
	}
	
	jq("#registView_other").live("pageinit", function() {
		registerAutoScroll();
		refreshPassCode();
		var universityMap = mor.cache.universityMap;
		if(universityMap){
			var universityList = mor.ticket.cache.university;
			for(var z = 0; z < universityList.length;z++){
				universityMap[universityList[z].university_code] = universityList[z].university_name;
			};
		}
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		jq("#regist_refreshPassCode").bind("tap",refreshPassCode);
		jq("#regist_userType").bind("change",changeEvent);
		jq("#regist_studentProvince").bind("tap",function(){
			jq.mobile.changePage("selectProvince.html");
			return false;
		});
		jq("#regist_studentSchool").bind("tap",function(){
			if(mor.ticket.util.isNoValue(jq("#regist_studentProvince").val())){
				util.alertMessage("请选择学校省份");
			}else{
				jq.mobile.changePage("selectUniversity.html");
			}
			return false;
		});
		
		jq("#regist_preferenceFromStation").bind("tap",function(){
			mor.ticket.viewControl.isCityGo = true;
			jq.mobile.changePage("selectCity.html");
			return false;
		});
		
		jq("#regist_preferenceToStation").bind("tap",function(){
			jq.mobile.changePage("selectCity.html");
			return false;
		});
		jq("#regist_otherBackBtn").bind("click",function(){
			jq.mobile.changePage("regist_relationType.html");
		});
		jq("#regist_otherNextBtn").bind("tap",submit);
		jq("#regist_studentDepartment").blur(function () { 
			if(!util.isNoValue(jq("#regist_studentDepartment").val())&&!checkFormUtil.checkNameChar(jq("#regist_studentDepartment").val())){
				util.alertMessage("填写的院系只能包含中文、英文、数字！");
				return;
			}
		} );
		
		jq("#regist_studentClass").blur(function () { 
			if(!util.isNoValue(jq("#regist_studentClass").val())&&!checkFormUtil.checkNameChar(jq("#regist_studentClass").val())){
				util.alertMessage("填写的班级只能包含中文、英文、数字！");
				return;
			}
		} );
		
		jq("#regist_studentNo").blur(function () { 
			if(util.isNoValue(jq("#regist_studentNo").val())){
				util.alertMessage("请输入学号");
				return;
			}
			
			if(!checkFormUtil.checkNameChar(jq("#regist_studentNo").val())){
				util.alertMessage("填写的学号只能包含中文、英文、数字!");
				return;
			}
		} );
		
		jq("#regist_preferenceCardNo").blur(function () { 
			if(!util.isNoValue(jq("#regist_preferenceCardNo").val())&&!checkFormUtil.checkNameChar(jq("#regist_preferenceCardNo").val())){
				util.alertMessage("填写的优惠卡只能包含中文、英文、数字!");
				return;
			}
		} );
		initSelects();
	});
	function initSelects(){
		jq("#regist_userType").scroller({
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
		jq("#regist_studentSystem").scroller({
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
		var currYear = mor.ticket.util.getNewDate().getFullYear();
		jq('#regist_studentEnterYear').scroller({
			preset: 'date',
	        theme: 'ios',
	        yearText:'年',
	        setText:'确定',
	        cancelText:'取消',
	        display: 'modal',
	        mode: 'scroller',
	        dateOrder: 'yy',
	        dateFormat: 'yy',
	        startYear: currYear - 9,
	        endYear:currYear,
	        height:40,
	        showLabel:true 
		});
	}
	//---------------------------------------
	function refreshPassCode(){
		var util = mor.ticket.util;		
		var commonParameters = {
			'baseDTO.user_name': ''
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "postPassCode"
		};
		var options = {
				onSuccess: requestPassCodeSucceeded,
				onFailure: requestPassCodeFailed
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function requestPassCodeSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (invocationResult.isSuccessful &&
				invocationResult.succ_flag === "1") {
			jq("#regist_passCodeImg").attr("src", "data:image/gif;base64," + invocationResult.passcode);
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function requestPassCodeFailed(result){
		if(busy.isVisible()){
			busy.hide();
		}
		mor.ticket.util.alertMessage(result.invocationResult.errors.join("\n"));
	}
	
	function submit(){
		validata();
		return false;
	}
	
	function validata(){
		var util=mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm; 
		if(util.isNoValue(jq("#regist_userType").val())){
			util.alertMessage("请选择旅客类型");
			return;
		}
		if(jq("#regist_userType").val()=="3"){
		if(util.isNoValue(jq("#regist_studentProvince").val())){
			util.alertMessage("请选择学校省份");
			return;
			}
		if(util.isNoValue(jq("#regist_studentSchool").val())){
			util.alertMessage("请选择学校名称");
			return;
		}
		
		if(!util.isNoValue(jq("#regist_studentDepartment").val())&&!checkFormUtil.checkNameChar(jq("#regist_studentDepartment").val())){
			util.alertMessage("填写的院系只能包含中文、英文、数字！");
			return;
		}
		
		if(!util.isNoValue(jq("#regist_studentClass").val())&&!checkFormUtil.checkNameChar(jq("#regist_studentClass").val())){
			util.alertMessage("填写的班级只能包含中文、英文、数字！");
			return;
		}
		
		if(util.isNoValue(jq("#regist_studentNo").val())){
			util.alertMessage("请输入学号");
			return;
		}
		
		if(!checkFormUtil.checkNameChar(jq("#regist_studentNo").val())){
			util.alertMessage("填写的学号只能包含中文、英文、数字！");
			return;
		}
		
		if(util.isNoValue(jq("#regist_studentSystem").val())){
			util.alertMessage("请选择学制");
			return;
		}
		

		if(util.isNoValue(jq("#regist_studentEnterYear").val())){
			util.alertMessage("请选择入学年份");
			return;
		}
		
		if(!util.isNoValue(jq("#regist_preferenceCardNo").val())&&!checkFormUtil.checkNameChar(jq("#regist_preferenceCardNo").val())){
			util.alertMessage("填写的优惠卡只能包含中文、英文、数字！");
			return;
		}
		
		if(util.isNoValue(jq("#regist_preferenceFromStation").val())||util.isNoValue(jq("#regist_preferenceToStation").val())){
			util.alertMessage("请选择优惠区间");
			return;
		}
	}
		var agree = document.getElementById("agreeChkbox").checked;
		if(!agree){
			util.alertMessage("请选择同意服务条款");
			return;
		}
		common();
		var commonParameters = {
			'baseDTO.user_name' : '',
			'user_type': mor.ticket.registInfo.user_type ,
			'user_name': mor.ticket.registInfo.user_name,
			'name': mor.ticket.registInfo.name,
			'id_type_code': mor.ticket.registInfo.id_type_code,
			'id_no': mor.ticket.registInfo.id_no,
			'password': mor.ticket.registInfo.password,
			'pwd_question': mor.ticket.registInfo.pwd_question,
			'pwd_answer': mor.ticket.registInfo.pwd_answer,
			'sex_code': mor.ticket.registInfo.sex_code,
			'born_date': mor.ticket.registInfo.born_date,
			'country_code': mor.ticket.registInfo.country_code,
			'mobile_no': mor.ticket.registInfo.mobile_no,
			'phone_no': mor.ticket.registInfo.phone_no,
			'email': mor.ticket.registInfo.email,
			'address': mor.ticket.registInfo.address,
			'postalcode': mor.ticket.registInfo.postalcode,
			'IVR_passwd': mor.ticket.registInfo.IVR_passwd,
			'province_code': mor.ticket.registInfo.province_code,
			'school_code': mor.ticket.registInfo.school_code,
			'department': mor.ticket.registInfo.department,
			'school_class': mor.ticket.registInfo.school_class,
			'student_no': mor.ticket.registInfo.student_no,
			'enter_year': mor.ticket.registInfo.enter_year,
			'school_system': mor.ticket.registInfo.school_system,
			'preference_from_station_code': mor.ticket.registInfo.preference_from_station_code,
			'preference_to_station_code': mor.ticket.registInfo.preference_to_station_code,
			'preference_card_no': mor.ticket.registInfo.preference_card_no,
			'pass_code': mor.ticket.registInfo.pass_code 	
		};
		
		var invocationData={
				adapter:"CARSMobileServiceAdapter",
				procedure:"registUser"
		};
		var options={
				onSuccess:requestSucceeded,
				onFailure:util.creatCommonRequestFailureHandler()
		};

		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
}
	//--------------------------------------------
	function changeEvent(){
		if(jq("#regist_userType").val()=="3"){
			jq("#regist_schoolOptions").show();
		}else{
			jq("#regist_schoolOptions").hide();
		}
		contentIscrollRefresh();
	}
	function contentIscrollRefresh(){
		if(jq("#registView_other .ui-content").attr("data-iscroll")!=undefined){
			jq("#registView_other .ui-content").iscrollview("refresh");
		}
	}
	jq("#registView_other").live("pagebeforeshow", function(e, data) {
		mor.ticket.viewControl.tab4_cur_page="regist_otherInfo.html";
		prevPage = data.prevPage.attr("id");
		var user=mor.ticket.loginUser;
		if(user.isAuthenticated==="Y"){
			jq("#registBack_OtherValue").html("更多功能");
		} else {
			if (window.ticketStorage.getItem("autologin") != "true") {
				jq("#registBack_OtherValue").html("登录");
			} else {
				registerAutoLoginHandler(function() {
					jq("#registBack_OtherValue").html("更多功能");
				}, function() {
					jq("#registBack_OtherValue").html("登录");
				});
			}

		}
		jq("#registBack_OtherBtn").bind("tap",function(){
			if("更多功能"===jq("#registBack_OtherValue").html())
			{
			jq.mobile.changePage("moreOption.html");
			}
		else{
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
			return false;
		});
		
		jq("#regist_studentEnterYear").val(mor.ticket.util.getNewDate().getFullYear());
	});
	function common(){
		mor.ticket.registInfo.user_type=jq("#regist_userType").val();
		mor.ticket.registInfo.province_code=jq("#regist_studentProvince_code").val();
		mor.ticket.registInfo.school_code=jq("#regist_studentSchool_code").val();
		mor.ticket.registInfo.department= jq("#regist_studentDepartment").val();
		mor.ticket.registInfo.school_class=jq("#regist_studentClass").val();
		mor.ticket.registInfo.student_no=jq("#regist_studentNo").val();
		mor.ticket.registInfo.school_system= jq("#regist_studentSystem").val();
		mor.ticket.registInfo.enter_year= jq("#regist_studentEnterYear").val();
		mor.ticket.registInfo.preference_card_no=jq("#regist_preferenceCardNo").val();
		mor.ticket.registInfo.preference_from_station_code=jq("#regist_preferenceFromStation_code").val();
		mor.ticket.registInfo.preference_to_station_code= jq("#regist_preferenceToStation_code").val();
		mor.ticket.registInfo.pass_code= jq("#regist_passCode").val();
	}
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
			jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		} else {  
			refreshPassCode();
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
		
	}
})();