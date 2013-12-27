
/* JavaScript content from js/controller/modifyUserInfo.js in folder common */
(function() {
	/*jq("#modifyuserInfoView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#modifyuserInfoView");
	});*/
	var prevPage;
	var newEmail ='';
	var focusArray=[];
	jq("#modifyuserInfoView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#modify_passwd',focusArray);
		util.enableAutoScroll('#modify_languagePasswd',focusArray);
		util.enableAutoScroll('#modify_pwdQuestion',focusArray);
		util.enableAutoScroll('#modify_passCode',focusArray);
		util.enableAutoScroll('#modify_name',focusArray);
		util.enableAutoScroll('#modify_idNo',focusArray);
		util.enableAutoScroll('#modify_mobile',focusArray);
		util.enableAutoScroll('#modify_phone',focusArray);
		util.enableAutoScroll('#modify_email',focusArray);	
		util.enableAutoScroll('#modify_address',focusArray);
		util.enableAutoScroll('#modify_postalCode',focusArray);
		//附加信息
		util.enableAutoScroll('#modify_studentDepartment',focusArray);
		util.enableAutoScroll('#modify_studentClass',focusArray);
		util.enableAutoScroll('#modify_studentNo',focusArray);
		util.enableAutoScroll('#modify_preferenceCardNo',focusArray);
		if(util.isIPhone()){
			util.enableAutoScroll('#modify_pwdPrompt',focusArray);
			util.enableAutoScroll('#modify_sex',focusArray);
			util.enableAutoScroll('#modify_idType',focusArray);
			util.enableAutoScroll('#modify_userType',focusArray);
			util.enableAutoScroll('#modify_studentSystem',focusArray);
			util.enableAutoScroll('#modify_studentEnterYear',focusArray);
			util.enableAutoScroll('#modify_pwdPrompt',focusArray);
		}
	}
	
	jq("#modifyuserInfoView").live("pageinit", function() {
		registerAutoScroll();
		var util = mor.ticket.util;	
		mor.ticket.viewControl.isNeedRequest = true;
		var checkFormUtil = mor.ticket.checkForm;			
		initSelects();
		refreshPassCode();
		registerToDateInputChangeListener();
		
		jq("#modifyuserInfoBackBtn").bind("tap",function(){
			mor.ticket.viewControl.isNeedRequest = false;
			jq.mobile.changePage("userInfo.html");
			return false;
		});
		
		jq("#refreshPassCode").bind("tap",refreshPassCode);
		
		jq("#confirmUserInfoModifyBtn").bind("tap",submitmodifyUserInfo);
		
		jq("#modify_userType").bind("change",changeEvent);
		
		jq("#modify_pwdPrompt").bind("change",changeQuestionEvent);
		
		jq("#modify_country").bind("tap",function(){
			jq.mobile.changePage("selectCountry.html");
			return false;
		});
		
		jq("#modify_studentProvince").bind("tap",function(){
			jq.mobile.changePage("selectProvince.html");
			return false;
		});
		
		jq("#modify_studentSchool").bind("tap",function(){
			if(mor.ticket.util.isNoValue(jq("#modify_studentProvince").val())){
			    util.alertMessage("请选择学校省份");
			}else{
				jq.mobile.changePage("selectUniversity.html");
			}
			return false;
		});
		
		jq("#modify_preferenceFromStation").bind("tap",function(){
			mor.ticket.viewControl.isCityGo = true;
			jq.mobile.changePage("selectCity.html");
			return false;
		});
		
		jq("#modify_preferenceToStation").bind("tap",function(){
			jq.mobile.changePage("selectCity.html");
			return false;
		});		
		
		jq("#modify_passwd").change(function () { 
			if(util.isNoValue(jq("#modify_passwd").val())){
				util.alertMessage("请填写密码");
				return;
			}
			
			if(jq("#modify_passwd").val().length<6){
				util.alertMessage("密码的长度必须大于6位!");
				return;
			}
		} );
		
		jq("#modify_languagePasswd").change(function () { 
			if(util.isNoValue(jq("#modify_languagePasswd").val())){
				util.alertMessage("请填写语音查询密码");
				return;
			}
			
			if(!checkFormUtil.isZipCode(jq("#modify_languagePasswd").val())){
				util.alertMessage("语音查询密码不为6位数字");
				return;
			}
		} );
		
		jq("#modify_passCode").change(function () {
			if(util.isNoValue(jq("#modify_passCode").val())){
				util.alertMessage("请填写验证码");
				return;
			}
			
			if(!checkFormUtil.checkNum(jq("#modify_passCode").val())){
				util.alertMessage("验证码只能为数字");
				return;
			}
		});
		
		jq("#modify_name").change(function () {
			if(util.isNoValue(jq("#modify_name").val())){
				util.alertMessage("请填写姓名");
				return;
			}
			
			if(!checkFormUtil.checkChar(jq("#modify_name").val())){
				util.alertMessage("真实姓名只能填写英文字母或者中文");
				return;
			}
		});
		
		jq("#modify_idNo").change(function () {
			if(util.isNoValue(jq("#modify_idNo").val())){
				util.alertMessage("请输入证件号码");
				return;
			}
			if(!checkFormUtil.checkIdValidStr(jq("#modify_idNo").val())){
				util.alertMessage("输入的证件编号中包含中文信息或特殊字符");
				return;
			}
		});
		
		jq("#modify_mobile").change(function () { 
			if(util.isNoValue(jq("#modify_mobile").val())){
				util.alertMessage("请输入手机号码");
				return;
			}
			
			if(!checkFormUtil.isMobile(jq("#modify_mobile").val())){
				util.alertMessage("请输入正确的手机号码");
				return;
			}
		});
		
		jq("#modify_email").change(function () {
			if(util.isNoValue(jq("#modify_email").val())){
				util.alertMessage("请输入邮箱地址");
				return;
			}
			
			if(!checkFormUtil.isEmail(jq("#modify_email").val())){
				util.alertMessage("请输入正确的邮箱地址");
				return;
			}
		});
		
		jq("#modify_postalCode").change(function () { 
			if(!util.isNoValue(jq("#modify_postalCode").val())&&!checkFormUtil.isZipCode(jq("#modify_postalCode").val())){
				util.alertMessage("您输入的邮编不是有效的格式");
				return;
			}
		});
		
		jq("#modify_studentDepartment").change(function () { 
			if(!util.isNoValue(jq("#modify_studentDepartment").val())&&!checkFormUtil.checkNameChar(jq("#modify_studentDepartment").val())){
				util.alertMessage("填写的院系只能包含中文、英文、数字");
				return;
			}
		});
		
		jq("#modify_studentClass").change(function () { 
			if(!util.isNoValue(jq("#modify_studentClass").val())&&!checkFormUtil.checkNameChar(jq("#modify_studentClass").val())){
				util.alertMessage("填写的班级只能包含中文、英文、数字");
				return;
			}
		});
		
		jq("#modify_studentNo").change(function () { 
			if(util.isNoValue(jq("#modify_studentNo").val())){
				util.alertMessage("请输入学号");
				return;
			}
			if(!checkFormUtil.checkNameChar(jq("#modify_studentNo").val())){
				util.alertMessage("填写的学号只能包含中文、英文、数字");
				return;
			}
		});
		
		/*registerModify_pwdPromptChangeHandler();
		
		registerModify_sexChangeHandler();
		
		registerModify_idTypeChangeHandler();
		
		//modify_userType
		registerModify_userTypeChangeHandler();
		
		//modify_studentSystem
		registerModify_studentSystemChangeHandler();*/		
	});
	function modifyUserInfoFn(e, data){
		var user = mor.ticket.loginUser;
		prevPage = data.prevPage.attr("id");
		mor.ticket.viewControl.tab3_cur_page="modifyUserInfo.html";
		fillData();
		jq("#modify_studentEnterYear").val(mor.ticket.util.getNewDate().getFullYear());
	}
	jq("#modifyuserInfoView").live("pagebeforeshow", function(e,data) {
		var user = mor.ticket.loginUser;
		if (user.isAuthenticated === "Y") {
			modifyUserInfoFn(e, data);
		} else {
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
			} else {
				registerAutoLoginHandler(function(){modifyUserInfoFn(e, data);}, autologinFailJump);
			}
			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	function contentIscrollRefresh(){
		if(jq("#modifyuserInfoView .ui-content").attr("data-iscroll")!=undefined){
			jq("#modifyuserInfoView .ui-content").iscrollview("refresh");
		}
	}
	
	function initSelects(){
		
		/*jq("#modify_pwdPrompt,#modify_sex,#modify_idType").scroller({
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
		
		jq("#modify_userType,#modify_studentSystem").scroller({
			preset : 'select',
			theme : 'ios',
			display : 'modal',
			mode : 'scroller',
			setText:'确定',
	        cancelText:'取消',
			inputClass : 'i-txt',
			height:40,
	        showLabel:true
		});*/
		
		jq('#modify_bornDate').scroller({
	        preset: 'date',
	        theme: 'ios',
	        yearText:'年',
	        monthText:'月',
	        dayText:'日',
	        setText:'确定',
	        cancelText:'取消',
	        display: 'modal',
	        mode: 'scroller',
	        dateOrder: 'yy mm dd',
	        dateFormat: 'yy-mm-dd',
	        height:40,
	        showLabel:true
		});
		
		var jq_studentEnterYear = jq("#modify_studentEnterYear");	
		jq_studentEnterYear.empty();
		var currYear = mor.ticket.util.getNewDate().getFullYear();
		var htmlStr = "<option value='"+currYear+"' selected>"+currYear+"</option>";		
		currYear --;
		jq_studentEnterYear.append(htmlStr);
		for(var i=0; i<8; i++){	
			var htmlStr = "<option value='"+currYear+"'>"+currYear+"</option>";			
			jq_studentEnterYear.append(htmlStr);
			currYear --;
		}
		jq("#modify_studentEnterYear").selectmenu('refresh', true);
		/*jq('#modify_studentEnterYear').scroller({
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
		});*/
		
	}
	
	
	function fillData(){
		var userInfo = mor.ticket.userInfo;
		var util = mor.ticket.util;
		if(prevPage=== "userInfoView"){
		    jq("#modify_username").html(userInfo.user_name);
			jq("#modify_languagePasswd").val(userInfo.ivr_passwd);
			/*util.setCustomSelectScrollerValue({
				id: "modify_pwdPrompt",
				value: userInfo.pwd_question,
				label: userInfo.pwd_question
			});*/
			jq("#modify_pwdPrompt option[value="+userInfo.pwd_question+"]").attr("selected","selected");
			jq("#modify_pwdPrompt").selectmenu('refresh', true);
			jq("#modify_name").val(userInfo.name);
			jq("#modify_country").val(userInfo.country_code==null ? "":util.getCountryByCode(userInfo.country_code));
			jq("#modify_country_code").val(userInfo.country_code);
			/*util.setCustomSelectScrollerValue({
				id: "modify_sex",
				value: userInfo.sex_code,
				label: util.getSexName(userInfo.sex_code)
			});*/
			jq("#modify_sex option[value="+userInfo.sex_code+"]").attr("selected","selected");
			jq("#modify_sex").selectmenu('refresh', true);
			/*jq("#modify_bornDate").val(userInfo.born_date==null ? "":util.changeDateType(userInfo.born_date));*/
			/*util.setCustomSelectScrollerValue({
				id: "modify_idType",
				value: userInfo.id_type_code,
				label: util.getIdTypeName(userInfo.id_type_code)
			});*/
			jq("#modify_idType option[value="+userInfo.id_type_code+"]").attr("selected","selected");
			jq("#modify_idType").selectmenu('refresh', true);
			jq("#modify_bornDateShow").val(userInfo.born_date==null ? "":util.changeDateType(userInfo.born_date));
			
			jq("#modify_idNo").val(userInfo.id_no);
			jq("#modify_mobile").val(userInfo.mobile_no);
			jq("#modify_phone").val(userInfo.phone_no);
			jq("#modify_email").val(userInfo.email);
			jq("#modify_address").val(userInfo.address);
			jq("#modify_postalCode").val(userInfo.postalcode);
			/*util.setCustomSelectScrollerValue({
				id: "modify_userType",
				value: userInfo.user_type,
				label: util.getPassengerTypeName(userInfo.user_type)
			});*/
			jq("#modify_userType option[value="+userInfo.user_type+"]").attr("selected","selected");
			jq("#modify_userType").selectmenu('refresh', true);
			if(userInfo.user_type=="3"){
				jq("#modify_studentProvince").val(userInfo.student_province_code==null?"":util.getProvinceByCode(userInfo.student_province_code));
				jq("#modify_studentProvince_code").val(userInfo.student_province_code);
				jq("#modify_studentSchool").val(userInfo.student_school_code==null?"":util.getUniversityByCode(userInfo.student_school_code));
				jq("#modify_studentSchool_code").val(userInfo.student_school_code);
				jq("#modify_studentDepartment").val(userInfo.student_department);
				jq("#modify_studentClass").val(userInfo.student_school_class);
				jq("#modify_studentNo").val(userInfo.student_student_no);
				/*util.setCustomSelectScrollerValue({
					id: "modify_studentSystem",
					value: userInfo.student_school_system,
					label: userInfo.student_school_system
				});*/
				jq("#modify_studentSystem option[value="+userInfo.student_school_system+"]").attr("selected","selected");
				jq("#modify_studentSystem").selectmenu('refresh', true);
				/*util.setCustomSelectScrollerValue({
					id: "modify_studentEnterYear",
					value: userInfo.student_enter_year,
					label: userInfo.student_enter_year
				});*/
				jq("#modify_studentEnterYear option[value="+userInfo.student_enter_year+"]").attr("selected","selected");
				jq("#modify_studentEnterYear").selectmenu('refresh', true);
				jq("#modify_preferenceCardNo").val(userInfo.student_card_no);
				jq("#modify_preferenceFromStation").val(userInfo.student_from_station_code==null?"":mor.cache.cityMap[userInfo.student_from_station_code]);
				jq("#modify_preferenceFromStation_code").val(userInfo.student_from_station_code);
				jq("#modify_preferenceToStation").val(userInfo.student_to_station_code==null?"":mor.cache.cityMap[userInfo.student_to_station_code]);
				jq("#modify_preferenceToStation_code").val(userInfo.student_to_station_code);
				jq("#schoolOptions").show();
		  }
		}
		contentIscrollRefresh();
	}
	
	function changeEvent(){
		if(jq("#modify_userType").val()=="3"){
			jq("#schoolOptions").show();
		}else{
			jq("#schoolOptions").hide();
		}
		contentIscrollRefresh();
	}
	
	function changeQuestionEvent(){
		if(jq("#modify_pwdPrompt").val()=="1"){
			jq("#otherpwdPromptOption").show();
		}else{
			jq("#otherpwdPromptOption").hide();
		}
		contentIscrollRefresh();
	}
	
	function submitmodifyUserInfo(){
		validate();
		return false;
	}
	
	function requestSucceeded(result) {
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			//mor.ticket.util.alertMessage(invocationResult.error_msg);
			if(newEmail!=mor.ticket.userInfo.email){
				var loginUser=mor.ticket.loginUser;
				loginUser.activeUser = "N";
				var msgStr = "修改成功，已发送激活链接到您的新邮箱  "+newEmail+" 请进入邮箱并按提示重新激活用户帐户";
				WL.SimpleDialog.show("温馨提示",
						msgStr, [ {
							text : '确定',
							handler : function() {
							}
						} ]);
			}
			var loginUserS=mor.ticket.loginUser;
			jq.mobile.changePage("userInfo.html");
		} else {
			refreshPassCode();
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function validate(){
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		if(util.isNoValue(jq("#modify_passwd").val())){
			util.alertMessage("请填写密码");
			return;
		}
		
		if(jq("#modify_passwd").val().length<6){
			util.alertMessage("密码的长度必须大于6位");
			return;
		}
		
		if(util.isNoValue(jq("#modify_languagePasswd").val())){
			util.alertMessage("请填写语音查询密码");
			return;
		}
		
		if(!checkFormUtil.isZipCode(jq("#modify_languagePasswd").val())){
			util.alertMessage("语音查询密码不为6位数字");
			return;
		}
		
		if(util.isNoValue(jq("#modify_passCode").val())){
			util.alertMessage("请填写验证码");
			return;
		}
		
		if(!checkFormUtil.checkNum(jq("#modify_passCode").val())){
			util.alertMessage("验证码只能为数字");
			return;
		}
		
		if(util.isNoValue(jq("#modify_name").val())){
			util.alertMessage("请填写姓名");
			return;
		}
		
		if(!checkFormUtil.checkChar(jq("#modify_name").val())){
			util.alertMessage("真实姓名只能填写英文字母或者中文");
			return;
		}
		
		
		if(util.isNoValue(jq("#modify_sex").val())){
			util.alertMessage("请选择性别");
			return;
		}
		
		if(util.isNoValue(jq("#modify_idType").val())){
			util.alertMessage("请选择证件类型");
			return;
		}
		
		if(util.isNoValue(jq("#modify_idNo").val())){
			util.alertMessage("请输入证件号码");
			return;
		}
		
		if(!checkFormUtil.checkIdValidStr(jq("#modify_idNo").val())){
			util.alertMessage("输入的证件编号中包含中文信息或特殊字符");
			return;
		}
		//1 2 B C G 身份证验证
		if(jq("#modify_idType").val()=="1"){
			if(!checkFormUtil.validateSecIdCard(jq("#modify_idNo").val())){
				util.alertMessage("请正确输入18位的身份证号");
				return;
			}
		}
		
		if(jq("#modify_idType").val()=="2"){
			if(!checkFormUtil.validateFirIdCard(jq("#modify_idNo").val())){
				util.alertMessage("请正确输入15或者18位的身份证号");
				return;
			}
		}
		
		if(jq("#modify_idType").val()=="B"){
			if(!checkFormUtil.checkPassport(jq("#modify_idNo").val())){
				util.alertMessage("请输入有效的护照号码");
				return;
			}
		}
		
		if(jq("#modify_idType").val()=="C"){
			if(!checkFormUtil.checkHkongMacao(jq("#modify_idNo").val())){
				util.alertMessage("请输入有效的港澳居民通行证号码");
				return;
			}
		}
		
		if(jq("#modify_idType").val()=="G"){
			if(!checkFormUtil.checkTaiw(jq("#modify_idNo").val())){
				util.alertMessage("请输入有效的台湾居民通行证号码");
				return;
			}
		}
		
		if(util.isNoValue(jq("#modify_userType").val())){
			util.alertMessage("请选择旅客类型");
			return;
		}
		
		if(util.isNoValue(jq("#modify_mobile").val())){
			util.alertMessage("请输入手机号码");
			return;
		}
		
		if(!checkFormUtil.isMobile(jq("#modify_mobile").val())){
			util.alertMessage("请输入正确的手机号码");
			return;
		}
		
		if(util.isNoValue(jq("#modify_email").val())){
			util.alertMessage("请输入邮箱地址");
			return;
		}
		
		if(!checkFormUtil.isEmail(jq("#modify_email").val())){
			util.alertMessage("请输入正确的邮箱地址");
			return;
		}else{
			newEmail = jq("#modify_email").val();
		}
		
		if(!util.isNoValue(jq("#modify_postalCode").val())&&!checkFormUtil.isZipCode(jq("#modify_postalCode").val())){
			util.alertMessage("您输入的邮编不是有效的格式");
			return;
		}
		
		if(jq("#modify_userType").val()=="3"){
			
			if(util.isNoValue(jq("#modify_studentProvince").val())){
				util.alertMessage("请选择学校省份");
				return;
			}
			
			if(util.isNoValue(jq("#modify_studentSchool").val())){
				util.alertMessage("请选择学校名称");
				return;
			}
			
			if(!util.isNoValue(jq("#modify_studentDepartment").val())&&!checkFormUtil.checkNameChar(jq("#modify_studentDepartment").val())){
				util.alertMessage("填写的院系只能包含中文、英文、数字");
				return;
			}
			
			if(!util.isNoValue(jq("#modify_studentClass").val())&&!checkFormUtil.checkNameChar(jq("#modify_studentClass").val())){
				util.alertMessage("填写的班级只能包含中文、英文、数字");
				return;
			}
			
			if(util.isNoValue(jq("#modify_studentNo").val())){
				util.alertMessage("请输入学号");
				return;
			}
			
			if(!checkFormUtil.checkNameChar(jq("#modify_studentNo").val())){
				util.alertMessage("填写的学号只能包含中文、英文、数字");
				return;
			}
			
			if(util.isNoValue(jq("#modify_studentSystem").val())){
				util.alertMessage("请选择学制");
				return;
			}
			

			if(util.isNoValue(jq("#modify_studentEnterYear").val())){
				util.alertMessage("请选择入学年份");
				return;
			}
			
			if(!util.isNoValue(jq("#modify_preferenceCardNo").val())&&!checkFormUtil.checkNameChar(jq("#modify_preferenceCardNo").val())){
				util.alertMessage("填写的优惠卡只能包含中文、英文、数字");
				return;
			}
			
			if(util.isNoValue(jq("#modify_preferenceFromStation").val())||util.isNoValue(jq("#modify_preferenceToStation").val())){
				util.alertMessage("请选择优惠区间");
				return;
			}
		}
		var pws_question = "";
		if(jq("#modify_pwdPrompt").val()=="1"){
			pws_question =jq("#modify_otherpwdPrompt").val()==null?"":jq("#modify_otherpwdPrompt").val();
		}else{
			pws_question =jq("#modify_pwdPrompt").val()==null?"":jq("#modify_pwdPrompt").val();
		}
		
		var commonParameters = {			
			'id_type_code': jq("#modify_idType").val() ,
			'country_code': jq("#modify_country_code").val() ,
			'pass_word': hex_md5(jq("#modify_passwd").val()) ,
			'address': replaceChar(jq("#modify_address").val()) ,
			'name': replaceChar(jq("#modify_name").val()) ,
			'user_type': jq("#modify_userType").val() ,
			'id_no': jq("#modify_idNo").val() ,
			'mobile_no': jq("#modify_mobile").val() ,
			'phone_no': jq("#modify_phone").val() ,
			'email': jq("#modify_email").val() ,
			'born_date': util.processDateCode(jq("#modify_bornDateShow").val()),
			'IVR_passwd': jq("#modify_languagePasswd").val() ,
			'postalcode': jq("#modify_postalCode").val() ,
			'pwd_answer': jq("#modify_pwdQuestion").val() ,
			'pwd_question': pws_question ,
			'sex_code': jq("#modify_sex").val() ,
			'province_code':  jq("#modify_studentProvince_code").val(),
			'school_code': jq("#modify_studentSchool_code").val(),
			'department': jq("#modify_studentDepartment").val()==null?"":jq("#modify_studentDepartment").val() ,
			'school_class': jq("#modify_studentClass").val()==null?"":jq("#modify_studentClass").val() ,
			'student_no': jq("#modify_studentNo").val()==null?"":jq("#modify_studentNo").val() ,
			'enter_year': jq("#modify_studentEnterYear").val()==null?"":jq("#modify_studentEnterYear").val() ,
			'school_system': jq("#modify_studentSystem").val()==null?"":jq("#modify_studentSystem").val() ,
			'preference_from_station_code':  jq("#modify_preferenceFromStation_code").val(),
			'preference_to_station_code': jq("#modify_preferenceToStation_code").val(),
			'preference_card_no': jq("#modify_preferenceCardNo").val()==null?"":jq("#modify_preferenceCardNo").val(),
			'pass_code': jq("#modify_passCode").val() 				
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "changeUser"
		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	}
	//替换特殊字符
	function replaceChar(str) {
		var v = str.replace(/['"<> ?]/g,"");
		return v;
	}
	
	function refreshPassCode(){
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "postPassCode"
		};
		var options = {
				onSuccess: requestPassCodeSucceeded,
				onFailure: mor.ticket.util.creatCommonRequestFailureHandler()
		};
		
		mor.ticket.util.invokeWLProcedure(null, invocationData, options);
		return false;
	}
	
	
	function requestPassCodeSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (invocationResult.isSuccessful &&
				invocationResult.succ_flag === "1") {
			jq("#passCodeImg").attr("src", "data:image/gif;base64," + invocationResult.passcode);
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function registerToDateInputChangeListener(){
		jq("#modify_bornDateShow").bind("tap",function(){
			if(document.activeElement&&document.activeElement.nodeName=='INPUT'){
				document.activeElement.blur();
			}
			jq('#modify_bornDate').scroller('show');
			return false;
		});
		jq('#modify_bornDate').bind("change",function(){
			var date = jq(this).val();
			jq("#modify_bornDateShow").val(date);
		});
	}
	/*function registerModify_pwdPromptChangeHandler(){
		jq("#modify_pwdPrompt").bind("change",changeQuestionEvent);
		mor.ticket.util.bindSelectFocusBlurListener("#modify_pwdPrompt");
	}
	
	function registerModify_sexChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#modify_sex");
	}
	
	function registerModify_idTypeChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#modify_idType");
	}
	
	function registerModify_userTypeChangeHandler(){
		jq("#modify_userType").bind("change",changeEvent);
		mor.ticket.util.bindSelectFocusBlurListener("#modify_userType");
	}
	
	function registerModify_studentSystemChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#modify_studentSystem");
	}*/
	
})();