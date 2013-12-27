
/* JavaScript content from js/controller/userInfo.js in folder common */
(function() {
	jq("#userInfoView").live("pagebeforecreate", function() {
		if(mor.ticket.viewControl.tab3_cur_page=="userInfo.html"){
			mor.ticket.viewControl.isNeedRequest=false;
		}
		if(mor.ticket.viewControl.isNeedRequest){
			initUserInfo();
		}else{
			var	userInfo = mor.ticket.userInfo;
			var util = mor.ticket.util;
			if(!util.isNoValue(userInfo)){
				jq("#label_username").html("  "+(util.isNoValue(userInfo.user_name)?"":userInfo.user_name));
				jq("#label_pwd_question").html("  "+(util.isNoValue(userInfo.pwd_question)?"":userInfo.pwd_question));
				jq("#label_name").html("  "+(util.isNoValue(userInfo.name)?"":userInfo.name));
				jq("#label_sex").html("  "+(util.isNoValue(userInfo.sex_code)?"":util.getSexName(userInfo.sex_code)));
				jq("#label_born_date").html("  "+(util.isNoValue(userInfo.born_date)?"":util.changeDateType(userInfo.born_date)));
				jq("#label_country").html("  "+(util.isNoValue(userInfo.country_code)?"":util.getCountryByCode(userInfo.country_code)));
				jq("#label_id_type").html("  "+(util.isNoValue(userInfo.id_type_code)?"":util.getIdTypeName(userInfo.id_type_code)));
				jq("#label_id_no").html("  "+(util.isNoValue(userInfo.id_no)?"":userInfo.id_no));
				jq("#label_mobile_no").html("  "+(util.isNoValue(userInfo.mobile_no)?"":userInfo.mobile_no));
				jq("#label_phone_no").html("  "+(util.isNoValue(userInfo.phone_no)?"":userInfo.phone_no));
				jq("#label_email").html("  "+(util.isNoValue(userInfo.email)?"":userInfo.email));
				jq("#label_address").html("  "+(util.isNoValue(userInfo.address)?"":userInfo.address));
				jq("#label_postalcode").html("  "+(util.isNoValue(userInfo.postalcode)?"":userInfo.postalcode));
				jq("#label_user_type").html("  "+(util.isNoValue(userInfo.user_type)?"":util.getPassengerTypeName(userInfo.user_type)));
				if(userInfo.user_type=="3"){
					jq("#label_studentProvince").html("  "+ util.getProvinceByCode(userInfo.student_province_code));
					jq("#label_studentSchool").html("  "+ util.getUniversityByCode(userInfo.student_school_code));
					jq("#label_studentDepartment").html("  "+userInfo.student_department);
					jq("#label_studentClass").html("  "+userInfo.student_school_class);
					jq("#label_studentNo").html("  "+userInfo.student_student_no);
					jq("#label_studentSystem").html("  "+userInfo.student_school_system);
					jq("#label_studentEnterYear").html("  "+userInfo.student_enter_year);
					jq("#label_preferenceCardNo").html("  "+userInfo.student_card_no);
					jq("#label_preferenceFromStation").html("  "+util.getCityByCode(userInfo.student_from_station_code));
					jq("#label_preferenceToStation").html("  "+util.getCityByCode(userInfo.student_to_station_code));
					jq("#studentOptions").show();
				}else{
					jq("#studentOptions").hide();
				}
				contentIscrollRefresh();
		   }
			mor.ticket.viewControl.isNeedRequest = true;
		}
		
		function userinfoModifyFn(){
			jq("#modifyUserInfoBtn").off();
				jq("#modifyuserInfoView").remove();
				jq.mobile.changePage("modifyUserInfo.html",true);
				setTimeout(function(){
					jq("#modifyUserInfoBtn").off().on("tap",userinfoModifyFn);
				},1000);
				return false;
		}
		jq("#modifyUserInfoBtn").off().on("tap",userinfoModifyFn);
		
		jq("#userInfoBackBtn").bind("tap",function(){
			jq.mobile.changePage("my12306.html");
			return false;
		});
		
	});
	
	
	function initUserInfo(){
		var util = mor.ticket.util;
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "initUser"
		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		
		mor.ticket.util.invokeWLProcedure(null, invocationData, options);
	}
	
	function requestSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
		mor.ticket.userInfo = invocationResult;
		var	userInfo = mor.ticket.userInfo;
		var util = mor.ticket.util;
		if(!util.isNoValue(userInfo)){
			jq("#label_username").html("  "+(util.isNoValue(userInfo.user_name)?"":userInfo.user_name));
			jq("#label_pwd_question").html("  "+(util.isNoValue(userInfo.pwd_question)?"":userInfo.pwd_question));
			jq("#label_name").html("  "+(util.isNoValue(userInfo.name)?"":userInfo.name));
			jq("#label_sex").html("  "+(util.isNoValue(userInfo.sex_code)?"":util.getSexName(userInfo.sex_code)));
			jq("#label_born_date").html("  "+(util.isNoValue(userInfo.born_date)?"":util.changeDateType(userInfo.born_date)));
			jq("#label_country").html("  "+(util.isNoValue(userInfo.country_code)?"":util.getCountryByCode(userInfo.country_code)));
			jq("#label_id_type").html("  "+(util.isNoValue(userInfo.id_type_code)?"":util.getIdTypeName(userInfo.id_type_code)));
			jq("#label_id_no").html("  "+(util.isNoValue(userInfo.id_no)?"":userInfo.id_no));
			jq("#label_mobile_no").html("  "+(util.isNoValue(userInfo.mobile_no)?"":userInfo.mobile_no));
			jq("#label_phone_no").html("  "+(util.isNoValue(userInfo.phone_no)?"":userInfo.phone_no));
			jq("#label_email").html("  "+(util.isNoValue(userInfo.email)?"":userInfo.email));
			jq("#label_address").html("  "+(util.isNoValue(userInfo.address)?"":userInfo.address));
			jq("#label_postalcode").html("  "+(util.isNoValue(userInfo.postalcode)?"":userInfo.postalcode));
			jq("#label_user_type").html("  "+(util.isNoValue(userInfo.user_type)?"":util.getPassengerTypeName(userInfo.user_type)));
			if(userInfo.user_type=="3"){
				jq("#label_studentProvince").html("  "+ util.getProvinceByCode(userInfo.student_province_code));
				jq("#label_studentSchool").html("  "+ util.getUniversityByCode(userInfo.student_school_code));
				jq("#label_studentDepartment").html("  "+userInfo.student_department);
				jq("#label_studentClass").html("  "+userInfo.student_school_class);
				jq("#label_studentNo").html("  "+userInfo.student_student_no);
				jq("#label_studentSystem").html("  "+userInfo.student_school_system);
				jq("#label_studentEnterYear").html("  "+userInfo.student_enter_year);
				jq("#label_preferenceCardNo").html("  "+userInfo.student_card_no);
				jq("#label_preferenceFromStation").html("  "+util.getCityByCode(userInfo.student_from_station_code));
				jq("#label_preferenceToStation").html("  "+util.getCityByCode(userInfo.student_to_station_code));
				jq("#studentOptions").show();
			}else{
				jq("#studentOptions").hide();
			}
			contentIscrollRefresh();
	   }
		}else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}	
	}
	
	function contentIscrollRefresh(){
		jq("#userInfoView .ui-content").iscrollview("refresh");
	}
	
	jq("#userInfoView").live("pagebeforeshow", function() {
		var user = mor.ticket.loginUser;
		if (user.isAuthenticated === "Y") {
			mor.ticket.viewControl.tab3_cur_page="userInfo.html";
		} else {
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump();
				} else {
				registerAutoLoginHandler(function(){
					mor.ticket.viewControl.tab3_cur_page="userInfo.html";
				}, autologinFailJump);
				}

		}
	});
})();