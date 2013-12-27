
/* JavaScript content from js/controller/regist.js in folder common */
(function() {
	var prevPage; 
	/*jq("#registView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#registView");
	});*/
	var focusArray=[];
	jq("#registView").live("pageshow", function() {
		focusArray=[];
	});
	
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		//基本信息
		util.enableAutoScroll('#regist_user_name',focusArray);
		util.enableAutoScroll('#regist_passwd',focusArray);
		util.enableAutoScroll('#regist_confirmPasswd',focusArray);
		util.enableAutoScroll('#regist_ivrPasswd',focusArray);
		util.enableAutoScroll('#regist_confirmivrPasswd',focusArray);
		util.enableAutoScroll('#regist_pwdAnswer',focusArray);
		util.enableAutoScroll('#regist_passCode',focusArray);
		//详细信息
		util.enableAutoScroll('#regist_name',focusArray);
		util.enableAutoScroll('#regist_idNo',focusArray);
		//联系方式
		util.enableAutoScroll('#regist_mobile',focusArray);
		util.enableAutoScroll('#regist_phone',focusArray);
		util.enableAutoScroll('#regist_email',focusArray);
		util.enableAutoScroll('#regist_address',focusArray);
		util.enableAutoScroll('#regist_postalCode',focusArray);
		//附加信息
		util.enableAutoScroll('#regist_studentDepartment',focusArray);
		util.enableAutoScroll('#regist_studentClass',focusArray);
		util.enableAutoScroll('#regist_studentNo',focusArray);
		util.enableAutoScroll('#regist_passCode',focusArray);
		if(util.isIPhone()){
			//基本信息
			util.enableAutoScroll('#regist_pwd_question',focusArray);
			//详细信息
			util.enableAutoScroll('#regist_sex',focusArray);
			util.enableAutoScroll('#regist_idType',focusArray);
			//附加信息
			util.enableAutoScroll('#regist_userType',focusArray);
			util.enableAutoScroll('#regist_studentSystem',focusArray);
			util.enableAutoScroll('#regist_studentEnterYear',focusArray);
		}
	}
	
	jq("#registView").live("pageinit", function() {
		//验证码
		refreshPassCode();	
		registerAutoScroll();
		//init university id value
		var universityMap = mor.cache.universityMap;
		if(universityMap){
			var universityList = mor.ticket.cache.university;
			for(var z = 0; z < universityList.length;z++){
				universityMap[universityList[z].university_code] = universityList[z].university_name;
			};
		}
		
		//init country id value
		
		var countryMap = mor.cache.countryMap;
		if(countryMap){
			var countryList = mor.ticket.cache.country;
			for(var i = 0; i < countryList.length; i++){
				countryMap[countryList[i].id] = countryList[i].value;
			};
		}

		
		//init city id value
		
		var cityMap = mor.cache.cityMap;
		if(cityMap){
			var cityList = mor.ticket.cache.city;
			for(var zz = 0; zz < cityList.length;zz++){
				cityMap[cityList[zz].city_code] = cityList[zz].city_name;
			};
		}
		
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		
		jq("#regist_pwd_question").bind("change",changeQuestionEvent);
		// 验证码
		jq("#regist_refreshPassCode").bind("tap",refreshPassCode);

		jq("#regist_country").bind("tap",function(){
			jq.mobile.changePage("selectCountry.html");
			return false;
		});
		
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
		
		jq("#registBtn").bind("tap",submit);
		
		jq("#regist_user_name").change(function () { 
			
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
		
		jq("#regist_passwd").change(function () { 
			if(util.isNoValue(jq("#regist_passwd").val())){
				util.alertMessage("请填写密码");
				return;
			}
			
			if(jq("#regist_passwd").val().length<6){
				util.alertMessage("密码的长度必须大于6位!");
				return;
			}
		} );
		
		jq("#regist_confirmPasswd").change(function () { 
			if(util.isNoValue(jq("#regist_confirmPasswd").val())){
				util.alertMessage("请输入确认密码");
				return;
			}
			
			if(jq("#regist_passwd").val()!=jq("#regist_confirmPasswd").val()){
				util.alertMessage("两次输入的密码不一致!");
				return;
			}
		} );
			
		jq("#regist_ivrPasswd").change(function () { 
			if(util.isNoValue(jq("#regist_ivrPasswd").val())){
				util.alertMessage("请填写语音查询密码");
				return;
			}
			if(!checkFormUtil.isZipCode(jq("#regist_ivrPasswd").val())){
				util.alertMessage("语音查询密码只能是6位数字!");
				return;
			}
			
		} );
		
		jq("#regist_confirmivrPasswd").change(function () { 
			if(util.isNoValue(jq("#regist_confirmivrPasswd").val())){
				util.alertMessage("请输入语音查询确认密码");
				return;
			}
			if(jq("#regist_ivrPasswd").val()!=jq("#regist_confirmivrPasswd").val()){
				util.alertMessage("语音查询确认密码与密码不一致!");
				return;
			}
		} );
		
		jq("#regist_otherpwdQuestion").change(function () { 
			if(util.isNoValue(jq("#regist_otherpwdQuestion").val())){
				//需要判断自定义问题是否已经被选定，并且显示出来。如果是，那么alert消息。如果不是，不alert消息。
				var visible = jq("#regist_otherQuestionOption").is(":visible");
				if(visible === "true"){
					util.alertMessage("请输入密码提示问题");
				}
				return;
			}
		} );
		
		jq("#regist_passCode").change(function () { 
			if(util.isNoValue(jq("#regist_passCode").val())){
				util.alertMessage("请填写验证码");
				return;
			}
			if(!checkFormUtil.checkNum(jq("#regist_passCode").val())){
				util.alertMessage("验证码只能为数字!");
				return;
			}
		} );
		
		jq("#regist_name").change(function () { 
			if(util.isNoValue(jq("#regist_name").val())){
				util.alertMessage("请填写姓名");
				return;
			}
			if(!checkFormUtil.checkChar(jq("#regist_name").val())){
				util.alertMessage("真实姓名只能填写英文字母或者中文!");
				return;
			}
		} );
		

		jq("#regist_idNo").change(function () { 
			if(util.isNoValue(jq("#regist_idNo").val())){
				util.alertMessage("请输入证件号码");
				return;
			}
			if(!checkFormUtil.checkIdValidStr(jq("#regist_idNo").val())){
				util.alertMessage("输入的证件编号中包含中文信息或特殊字符!");
				return;
			}
		} );
		
		jq("#regist_mobile").change(function () { 
			if(util.isNoValue(jq("#regist_mobile").val())){
				util.alertMessage("请输入手机号码");
				return;
			}
			if(!checkFormUtil.isMobile(jq("#regist_mobile").val())){
				util.alertMessage("请输入正确的手机号码!");
				return;
			}
		} );
		
		jq("#regist_email").change(function () { 
			if(util.isNoValue(jq("#regist_email").val())){
				util.alertMessage("请输入邮箱地址");
				return;
			}
			if(!checkFormUtil.isEmail(jq("#regist_email").val())){
				util.alertMessage("请输入正确的邮箱地址!");
				return;
			}
		} );
		
		jq("#regist_postalCode").change(function () { 
			if(!util.isNoValue(jq("#regist_postalCode").val())&&!checkFormUtil.isZipCode(jq("#regist_postalCode").val())){
				util.alertMessage("您输入的邮编不是有效的格式!");
				return;
			}
		} );
		
		jq("#regist_studentDepartment").change(function () { 
			if(!util.isNoValue(jq("#regist_studentDepartment").val())&&!checkFormUtil.checkNameChar(jq("#regist_studentDepartment").val())){
				util.alertMessage("填写的院系只能包含中文、英文、数字！");
				return;
			}
		} );
		
		jq("#regist_studentClass").change(function () { 
			if(!util.isNoValue(jq("#regist_studentClass").val())&&!checkFormUtil.checkNameChar(jq("#regist_studentClass").val())){
				util.alertMessage("填写的班级只能包含中文、英文、数字！");
				return;
			}
		} );
		
		jq("#regist_studentNo").change(function () { 
			if(util.isNoValue(jq("#regist_studentNo").val())){
				util.alertMessage("请输入学号");
				return;
			}
			
			if(!checkFormUtil.checkNameChar(jq("#regist_studentNo").val())){
				util.alertMessage("填写的学号只能包含中文、英文、数字!");
				return;
			}
		} );
		
		jq("#regist_preferenceCardNo").change(function () { 
			if(!util.isNoValue(jq("#regist_preferenceCardNo").val())&&!checkFormUtil.checkNameChar(jq("#regist_preferenceCardNo").val())){
				util.alertMessage("填写的优惠卡只能包含中文、英文、数字!");
				return;
			}
		} );
		initSelects();
		
		registerToDateInputChangeListener();
		//registerRegist_pwd_questionChangeHandler();
		
		//registerRegist_sexChangeHandler();
		
		//registerRegist_idTypeChangeHandler();

		//registerRegist_userTypeChangeHandler();

		//registerRegist_studentSystemChangeHandler();


	});
	
	jq("#registView").live("pagebeforeshow", function(e, data) {
		mor.ticket.viewControl.tab4_cur_page="regist.html";
			
		//jq.mobile.hidePageLoadingMsg();
		prevPage = data.prevPage.attr("id");
		if(prevPage=="moreOptionView"){
			jq("#registBackValue").html("更多功能");
		}else{
			jq("#registBackValue").html("登录");
		}
		jq("#registBackBtn").bind("tap",function(){
			if(prevPage=="moreOptionView"){
				jq.mobile.changePage("moreOption.html");
			}else{
				jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
			}
			return false;
		});
		
		if(jq("#regist_country_code").val()){
			jq("#regist_country").val(mor.ticket.util.getCountryByCode(jq("#regist_country_code").val()));
		}else{
			jq("#regist_country").val(mor.ticket.util.getCountryByCode("CN"));
			jq("#regist_country_code").val("CN");
		}
		if(jq("#regist_bornDateShow").val()){
			jq("#regist_bornDateShow").val(mor.ticket.util.processDateCode(jq("#regist_bornDateShow").val()));
		}else{
			jq("#regist_bornDateShow").val("1980-01-01");
		}
		jq("#regist_studentEnterYear").val(mor.ticket.util.getNewDate().getFullYear());
	});

	function initSelects(){		
//		jq("#regist_pwd_question,#regist_sex,#regist_idType,#regist_userType").scroller({
//			preset : 'select',
//			theme : 'ios',
//			display : 'modal',
//			mode : 'scroller',
//			setText:'确定',
//	        cancelText:'取消',
//			inputClass : 'i-txt',
//			height:40,
//	        showLabel:true
//		});
		
//		jq("#regist_studentSystem").scroller({
//			preset : 'select',
//			theme : 'ios',
//			display : 'modal',
//			mode : 'scroller',
//			setText:'确定',
//	        cancelText:'取消',
//			inputClass : 'i-txt',
//			height:40,
//	        showLabel:true
//		});
		
		jq('#regist_bornDate').scroller({
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

//		var currYear = mor.ticket.util.getNewDate().getFullYear();
		var jq_studentEnterYear = jq("#regist_studentEnterYear");	
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
		jq("#regist_studentEnterYear").selectmenu('refresh', true);
//		jq('#regist_studentEnterYear').scroller({
//			preset: 'date',
//	        theme: 'ios',
//	        yearText:'年',
//	        setText:'确定',
//	        cancelText:'取消',
//	        display: 'modal',
//	        mode: 'scroller',
//	        dateOrder: 'yy',
//	        dateFormat: 'yy',
//	        startYear: currYear - 9,
//	        endYear:currYear,
//	        height:40,
//	        showLabel:true
//		});
		
	}
	
	
	function changeQuestionEvent(){
		if(jq("#regist_pwd_question").val()=="1"){
			jq("#regist_otherQuestionOption").show();
		}else{
			jq("#regist_otherQuestionOption").hide();
		}
		contentIscrollRefresh();
	}
	
	function changeEvent(){
		if(jq("#regist_userType").val()=="3"){
			jq("#regist_schoolOptions").show();
		}else{
			jq("#regist_schoolOptions").hide();
		}
		contentIscrollRefresh();
	}
	
	function refreshPassCode(){
		var util = mor.ticket.util;		
		var commonParameters = util.prepareRequestCommonParameters({
			'baseDTO.user_name': ''
		});
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "postPassCode",
				parameters: [commonParameters]	
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
			jq("#regist_passCodeImg").attr("src", "data:image/gif;base64," + invocationResult.passcode);
		} else {
			mor.ticket.util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function submit(){
		validate();
		return false;
	}
	
	function validate(){
		
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
		
		if(util.isNoValue(jq("#regist_passCode").val())){
			util.alertMessage("请填写验证码");
			return;
		}
		
		if(!checkFormUtil.checkNum(jq("#regist_passCode").val())){
			util.alertMessage("验证码只能为数字!");
			return;
		}
		
		if(util.isNoValue(jq("#regist_name").val())){
			util.alertMessage("请填写姓名");
			return;
		}
		
		if(!checkFormUtil.checkChar(jq("#regist_name").val())){
			util.alertMessage("真实姓名只能填写英文字母或者中文!");
			return;
		}
		
		
		if(util.isNoValue(jq("#regist_sex").val())){
			util.alertMessage("请选择性别");
			return;
		}
		
		if(util.isNoValue(jq("#regist_country").val())){
			util.alertMessage("请选择国家地区");
			return;
		}
		
		if(util.isNoValue(jq("#regist_idType").val())){
			util.alertMessage("请选择证件类型");
			return;
		}
		
		if(util.isNoValue(jq("#regist_idNo").val())){
			util.alertMessage("请输入证件号码");
			return;
		}
		
		if(!checkFormUtil.checkIdValidStr(jq("#regist_idNo").val())){
			util.alertMessage("输入的证件编号中包含中文信息或特殊字符！");
			return;
		}
		// 1 2 B C G 身份证验证
		if(jq("#regist_idType").val()=="1"){
			if(!checkFormUtil.validateSecIdCard(jq("#regist_idNo").val())){
				util.alertMessage("请正确输入18位的身份证号！");
				return;
			}
		}
		
		if(jq("#regist_idType").val()=="2"){
			if(!checkFormUtil.validateFirIdCard(jq("#regist_idNo").val())){
				util.alertMessage("请正确输入15或者18位的身份证号！");
				return;
			}
		}
		
		if(jq("#regist_idType").val()=="B"){
			if(!checkFormUtil.checkPassport(jq("#regist_idNo").val())){
				util.alertMessage("请输入有效的护照号码！");
				return;
			}
		}
		
		if(jq("#regist_idType").val()=="C"){
			if(!checkFormUtil.checkHkongMacao(jq("#regist_idNo").val())){
				util.alertMessage("请输入有效的港澳居民通行证号码！");
				return;
			}
		}
		
		if(jq("#regist_idType").val()=="G"){
			if(!checkFormUtil.checkTaiw(jq("#regist_idNo").val())){
				util.alertMessage("请输入有效的台湾居民通行证号码！");
				return;
			}
		}
		
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
		
		var commonParameters = {
			'baseDTO.user_name' : '',
			'user_type': jq("#regist_userType").val() ,
			'user_name': replaceChar(jq("#regist_user_name").val()),
			'name': replaceChar(jq("#regist_name").val()) ,
			'id_type_code': jq("#regist_idType").val() ,
			'id_no': jq("#regist_idNo").val() ,
			'password': hex_md5(jq("#regist_passwd").val()) ,
			'pwd_question': jq("#regist_pwd_question").val()=="1"?jq("#regist_otherpwdQuestion").val():jq("#regist_pwd_question").val() ,
			'pwd_answer': jq("#regist_pwdAnswer").val() ,
			'sex_code': jq("#regist_sex").val() ,
			'born_date': util.processDateCode(jq("#regist_bornDateShow").val()),
			'country_code': jq("#regist_country_code").val() ,
			'mobile_no': jq("#regist_mobile").val() ,
			'phone_no': jq("#regist_phone").val() ,
			'email': jq("#regist_email").val() ,
			'address': replaceChar(jq("#regist_address").val()) ,
			'postalcode': jq("#regist_postalCode").val() ,
			'IVR_passwd': jq("#regist_ivrPasswd").val() ,
			'province_code':  jq("#regist_studentProvince_code").val(),
			'school_code': jq("#regist_studentSchool_code").val(),
			'department': jq("#regist_studentDepartment").val() ,
			'school_class': jq("#regist_studentClass").val() ,
			'student_no': jq("#regist_studentNo").val() ,
			'enter_year': jq("#regist_studentEnterYear").val(),
			'school_system': jq("#regist_studentSystem").val(),
			'preference_from_station_code':  jq("#regist_preferenceFromStation_code").val(),
			'preference_to_station_code': jq("#regist_preferenceToStation_code").val(),
			'preference_card_no': jq("#regist_preferenceCardNo").val(),
			'pass_code': jq("#regist_passCode").val() 	
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "registUser"
		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	}
	// 替换特殊字符
	function replaceChar(str) {
		var v = str.replace(/['"<> ?]/g,"");
		return v;
	}
	
	function contentIscrollRefresh(){
		if(jq("#registView .ui-content").attr("data-iscroll")!=undefined){
			jq("#registView .ui-content").iscrollview("refresh");
		}
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
	
	function registerToDateInputChangeListener(){
		jq("#regist_bornDateShow").bind("tap",function(){
			if(document.activeElement&&document.activeElement.nodeName=='INPUT'){
				document.activeElement.blur();
			}
			jq('#regist_bornDate').scroller('show');
			return false;
		});
		jq('#regist_bornDate').bind("change",function(){
			var date = jq(this).val();
			jq("#regist_bornDateShow").val(date);
		});
	}
/*	function registerRegist_pwd_questionChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#regist_pwd_question");
		jq("#regist_pwd_question").bind("change",changeQuestionEvent);

	}
	
	function registerRegist_sexChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#regist_sex");
	}
	
	function registerRegist_idTypeChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#regist_idType");
	}
	
	function registerRegist_userTypeChangeHandler(){
		jq("#regist_userType").bind("change",function(){
			if(jq("#regist_userType").val()=="3"){
				jq("#regist_schoolOptions").show();
			}else{
				jq("#regist_schoolOptions").hide();
			}
			alert(jq("#registView .iscroll-scroller").height());
			if(!mor.ticket.util.isIPhone()){
				contentIscrollRefresh();
			}	
		});
		if(mor.ticket.util.isIPhone()){
			var jq_select = jq("#regist_userType");
			var jq_content = jq_select.parents(".ui-content");
			jq_select.bind("focus", function(){					
				jq_content.addClass("ui-disabled");	
				jq.mobile.activePage.find(".iscroll-wrapper").iscrollview("option", {resizeWrapper:false}); 
				alert(jq("#registView .iscroll-scroller").height());
				return false;
			});
			jq_select.bind("blur", function(){
				jq.mobile.activePage.find(".iscroll-wrapper").iscrollview("option", {resizeWrapper:true}); 
				jq_content.removeClass("ui-disabled");
				alert(jq("#registView .iscroll-scroller").height());
				//contentIscrollRefresh();
				jq.mobile.activePage.find(".iscroll-wrapper").iscrollview("refresh");
				return false;
			});
		}
		mor.ticket.util.bindSelectFocusBlurListener("#regist_userType");
	}
	
	function registerRegist_studentSystemChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#regist_studentSystem");
	}*/
})();