
/* JavaScript content from js/controller/modifyPassenger.js in folder common */
/**
 * User global various here to workaround select country auto tap on i9100
 */
var tapLocation = {'clientX': 0, 'clientY': 0};

(function() {
	/*jq("#modifyPassengerView").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#modifyPassengerView");
	});*/
	var prevPage;
	var operation="";
	
	var focusArray=[];
	jq("#modifyPassengerView").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#modify_PassengerName',focusArray);
		util.enableAutoScroll('#modify_PassengerIdNo',focusArray);
		util.enableAutoScroll('#modify_PassengerMobileNo',focusArray);
		util.enableAutoScroll('#modify_PassengerPhoneNo',focusArray);
		util.enableAutoScroll('#modify_PassengerEmail',focusArray);
		util.enableAutoScroll('#modify_PassengerAddress',focusArray);
		util.enableAutoScroll('#modify_PassengerPostalCode',focusArray);	
		if(util.isIPhone()){
			util.enableAutoScroll('#modify_PassengerSex',focusArray);
			util.enableAutoScroll('#modify_PassengerIdType',focusArray);
			util.enableAutoScroll('#modify_PassengerUserType',focusArray);
			util.enableAutoScroll('#modify_pStudentSystem',focusArray);
			util.enableAutoScroll('#modify_pStudentEnterYear',focusArray);
			util.enableAutoScroll('#modify_PassengerSex',focusArray);
		}
	}
	
	jq("#modifyPassengerView").live("pageinit", function() {
 
		var util = mor.ticket.util;
		
		
		
	
		
		registerAutoScroll();
		
		var checkFormUtil = mor.ticket.checkForm;
		
		jq("#modify_PassengerUserType").off().bind("change",changeEvent);
		jq("#modifyPassengerBackBtn").off().bind("tap",function(){
				//	alert(mor.ticket.history.url);
				if (mor.ticket.history.url=='fareList'){
					jq.mobile.changePage("fareList.html");
				}else if(mor.ticket.history.url=='bookticket'){
					jq.mobile.changePage("fareList.html");
				}else{
					jq.mobile.changePage("passengerList.html");
				}
			 
			return false;
		});
		
		jq("#modify_PassengerCountry").off('tap').on("tap",function(){
			jq.mobile.changePage("selectCountry.html");
			return false;
		});
		
		jq("#modify_pStudentProvince").off('tap').on("tap",function(){
			jq.mobile.changePage("selectProvince.html");
			return false;
		});
		
		jq("#modify_pStudentSchool").off('tap').on("tap",function(){
			if(mor.ticket.util.isNoValue(jq("#modify_pStudentProvince").val())){
				util.alertMessage("请选择学校省份");
			}else{
				jq.mobile.changePage("selectUniversity.html");
			}
			return false;
		});
		
		jq("#modify_pPreferenceFromStation").off('tap').on("tap",function(){
			mor.ticket.viewControl.isCityGo = true;
			jq.mobile.changePage("selectCity.html");
			return false;
		});
		
		jq("#modify_pPreferenceToStation").off('tap').on("tap",function(){
			jq.mobile.changePage("selectCity.html");
			return false;
		});		
		
		jq("#modifyPassengerBtn").off('tap').on("tap",function(){
													  
//			if (!busy.isVisible()) {
//				busy.show();
//			}
		   
		   submitPassengerInfo();
		
		});	

		jq("#deletePassengerBtn").off('tap').on("tap",deletePassenger);	
		
		jq("#modify_PassengerName").change(function () { 
			if(util.isNoValue(jq("#modify_PassengerName").val())){
				util.alertMessage("请填写姓名");
				return;
			}
			if(!checkFormUtil.checkPersonName(jq("#modify_PassengerName").val())){
				util.alertMessage("真实姓名只能填写英文字母或者中文");
				return;
			}
		} );
		
		jq("#modify_PassengerIdNo").change(function () { 
			if(util.isNoValue(jq("#modify_PassengerIdNo").val())){
				util.alertMessage("请输入证件号码");
				return;
			}
			if(!checkFormUtil.checkIdValidStr(jq("#modify_PassengerIdNo").val())){
				util.alertMessage("输入的证件编号中包含中文信息或特殊字符");
				return;
			}
		} );
		
		jq("#modify_PassengerMobileNo").change(function () { 
			if(!util.isNoValue(jq("#modify_PassengerMobileNo").val())&&!checkFormUtil.isMobile(jq("#modify_PassengerMobileNo").val())){
				util.alertMessage("请输入正确的手机号码");
				return;
			}
		} );
		
		jq("#modify_PassengerEmail").change(function () { 
			if(!util.isNoValue(jq("#modify_PassengerEmail").val())&&!checkFormUtil.isEmail(jq("#modify_PassengerEmail").val())){
				util.alertMessage("请输入正确的邮箱地址");
				return;
			}
		} );
		
		jq("#modify_PassengerPostalCode").change(function () { 
			if(!util.isNoValue(jq("#modify_PassengerPostalCode").val())&&!checkFormUtil.isZipCode(jq("#modify_PassengerPostalCode").val())){
				util.alertMessage("您输入的邮编不是有效的格式");
				return;
			}
		} );
		
		jq("#modify_pStudentDepartment").change(function () { 
			if(!util.isNoValue(jq("#modify_pStudentDepartment").val())&&!checkFormUtil.checkNameChar(jq("#modify_pStudentDepartment").val())){
				util.alertMessage("填写的院系只能包含中文、英文、数字");
				return;
			}
		} );
		
		jq("#modify_pStudentClass").change(function () { 
			if(!util.isNoValue(jq("#modify_pStudentClass").val())&&!checkFormUtil.checkNameChar(jq("#modify_pStudentClass").val())){
				util.alertMessage("填写的班级只能包含中文、英文、数字");
				return;
			}
		} );
		
		jq("#modify_pStudentNo").change(function () { 
			if(util.isNoValue(jq("#modify_pStudentNo").val())){
				util.alertMessage("请输入学号");
				return;
			}
			if(!checkFormUtil.checkNameChar(jq("#modify_pStudentNo").val())){
				util.alertMessage("填写的学号只能包含中文、英文、数字");
				return;
			}
		} );
		
		jq("#modify_pPreferenceCardNo").change(function () { 
			if(!util.isNoValue(jq("#modify_pPreferenceCardNo").val())&&!checkFormUtil.checkNameChar(jq("#modify_pPreferenceCardNo").val())){
				util.alertMessage("填写的优惠卡只能包含中文、英文、数字");
				return;
			}
		} );
		
		initSelects();
		
		registerToDateInputChangeListener();
		/*
		registerModify_PassengerSexChangeHandler();
		
		registerModify_PassengerIdTypeChangeHandler();
		
		registerModify_PassengerUserTypeChangeHandler();
		
		registerModify_pStudentSystemChangeHandler();*/
	});
	function modifyPsgFn(e, data){
		var user = mor.ticket.loginUser;
		prevPage = data.prevPage.attr("id"); 
		idTypeControl();
		refreshForm();
		mor.ticket.viewControl.tab3_cur_page="modifyPassenger.html";
		jq("#modify_pStudentEnterYear").val(mor.ticket.util.getNewDate().getFullYear());
	}
	jq("#modifyPassengerView").live("pagebeforeshow", function(e,data) {
		var user = mor.ticket.loginUser;
		if (user.isAuthenticated === "Y") {
			modifyPsgFn(e, data);
		} else {
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump();
			} else {
				registerAutoLoginHandler(function() {
					modifyPsgFn(e, data);
				}, autologinFailJump);
			}

			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	
	
	

	
	
	
	//购票用户身份控制
	function idTypeControl(){
		if(mor.ticket.loginUser.id_type === "1" || mor.ticket.loginUser.id_type === "2"){
			jq("#modify_PassengerIdType").html('<option value="1" selected>二代身份证</option>'+
          		'<option value="2">一代身份证</option>'+
         		'<option value="C">港澳通行证</option>'+
          		'<option value="G">台湾通行证</option>'+
          		'<option value="B">护照</option>');
		}else{
			jq("#modify_PassengerIdType").html('<option value="C" selected>港澳通行证</option>'+
	          		'<option value="G">台湾通行证</option>'+
	          		'<option value="B">护照</option>');
		}
		jq("#modify_PassengerIdType").selectmenu('refresh', true);
	}
	
	function changeEvent(){
		if(jq("#modify_PassengerUserType").val()=="3"){
			jq("#passengerOptions").show();
		}else{
			jq("#passengerOptions").hide();
		}
		var that = this;
		setTimeout(function(){
			jq(that).parent().find(".ui-btn-text").html("<span>"+that.options[that.selectedIndex].innerHTML+"</span>");
			contentIscrollRefresh();
		},20);
	}
	
	function contentIscrollRefresh(){
		if(jq("#modifyPassengerView .ui-content").attr("data-iscroll")!=undefined){
			jq("#modifyPassengerView .ui-content").iscrollview("refresh");
		}
	}
	
	function initSelects(){
		
		/*jq("#modify_PassengerSex,#modify_PassengerIdType,#modify_PassengerUserType").scroller({
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
		
		jq("#modify_pStudentSystem").scroller({
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
		
		jq('#modify_PassengerBornDate').scroller({
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
		
		/*var currYear = mor.ticket.util.getNewDate().getFullYear();
		jq('#modify_pStudentEnterYear').scroller({
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
		var jq_studentEnterYear = jq("#modify_pStudentEnterYear");	
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
		jq("#modify_pStudentEnterYear").selectmenu('refresh', true);
		
		var prompMsg = mor.ticket.cache.promptMsg.prompt_passengerMsg;
		jq("#passengerInfoMsg").html(prompMsg);
	}
	
	function refreshForm(){
		var passengerInfo = mor.ticket.passengerDetail;
		var util = mor.ticket.util;
			if(prevPage === "passengerListView" ){
				
				
				
				if(mor.ticket.viewControl.isModifyPassenger){
					jq("#modifyPassengerView").data('username',(util.isNoValue(passengerInfo.name)?"":passengerInfo.name));
					jq("#modifyPassengerView").data('idno',(util.isNoValue(passengerInfo.card_no) ? "":passengerInfo.card_no));
					jq("#modify_PassengerName").val(util.isNoValue(passengerInfo.name)?"":passengerInfo.name);
					/*util.setCustomSelectScrollerValue({
						id: "modify_PassengerSex",
						value: util.isNoValue(passengerInfo.sex_code)?"":passengerInfo.sex_code,
						label: util.isNoValue(passengerInfo.sex_code)?"":util.getSexName(passengerInfo.sex_code)
					});*/
					jq("#modify_PassengerSex option[value="+passengerInfo.sex_code+"]").attr("selected","selected");
					jq("#modify_PassengerSex").selectmenu('refresh', true);
					jq("#modify_PassengerBornDateShow").val(util.isNoValue(passengerInfo.born_date) ? "":util.changeDateType(passengerInfo.born_date));
					
					jq("#modify_PassengerCountry").val(util.getCountryByCode(passengerInfo.country_code));
					jq("#modify_PassengerCountry_code").val(passengerInfo.country_code);
					
					/*util.setCustomSelectScrollerValue({
						id: "modify_PassengerIdType",
						value: util.isNoValue(passengerInfo.card_type)?"":passengerInfo.card_type,
						label: util.isNoValue(passengerInfo.card_type)?"":util.getIdTypeName(passengerInfo.card_type)
					});*/
					jq("#modify_PassengerIdType option[value="+passengerInfo.card_type+"]").attr("selected","selected");
					jq("#modify_PassengerIdType").selectmenu('refresh', true);
					jq("#modify_PassengerIdNo").val(util.isNoValue(passengerInfo.card_no) ? "":passengerInfo.card_no);
					
					/*util.setCustomSelectScrollerValue({
						id: "modify_PassengerUserType",
						value: util.isNoValue(passengerInfo.passenger_type)?"":passengerInfo.passenger_type,
						label: util.isNoValue(passengerInfo.passenger_type)?"":util.getPassengerTypeName(passengerInfo.passenger_type)
					});*/
					jq("#modify_PassengerUserType option[value="+passengerInfo.passenger_type+"]").attr("selected","selected");
					jq("#modify_PassengerUserType").selectmenu('refresh', true);
					jq("#modify_PassengerMobileNo").val(util.isNoValue(passengerInfo.mobile_no) ? "":passengerInfo.mobile_no);
					jq("#modify_PassengerPhoneNo").val(util.isNoValue(passengerInfo.phone_no) ? "":passengerInfo.phone_no);
					jq("#modify_PassengerEmail").val(util.isNoValue(passengerInfo.email) ? "":passengerInfo.email);
					jq("#modify_PassengerAddress").val(util.isNoValue(passengerInfo.address) ? "":passengerInfo.address);
					jq("#modify_PassengerPostalCode").val(util.isNoValue(passengerInfo.postalcode) ? "":passengerInfo.postalcode);
					if(passengerInfo.passenger_type=="3"){
						jq("#modify_pStudentProvince").val(util.getProvinceByCode(passengerInfo.province_code));
						jq("#modify_pStudentProvince_code").val(passengerInfo.province_code);
						jq("#modify_pStudentSchool").val(util.getUniversityByCode(passengerInfo.school_code));
						jq("#modify_pStudentSchool_code").val(passengerInfo.school_code);
						jq("#modify_pStudentDepartment").val(util.isNoValue(passengerInfo.department) ? "":passengerInfo.department);
						jq("#modify_pStudentClass").val(util.isNoValue(passengerInfo.school_class) ? "":passengerInfo.school_class);
						jq("#modify_pStudentNo").val(util.isNoValue(passengerInfo.student_no) ? "":passengerInfo.student_no);
						/*util.setCustomSelectScrollerValue({
							id: "modify_pStudentSystem",
							value: util.isNoValue(passengerInfo.school_system) ? "":passengerInfo.school_system,
							label: util.isNoValue(passengerInfo.school_system) ? "":passengerInfo.school_system
						});*/
						jq("#modify_pStudentSystem option[value="+passengerInfo.school_system+"]").attr("selected","selected");
						jq("#modify_pStudentSystem").selectmenu('refresh', true);
						/*util.setCustomSelectScrollerValue({
							id: "modify_pStudentEnterYear",
							value: util.isNoValue(passengerInfo.enter_year) ? "":passengerInfo.enter_year,
							label: util.isNoValue(passengerInfo.enter_year) ? "":passengerInfo.enter_year
						});*/
						jq("#modify_pStudentEnterYear option[value="+passengerInfo.enter_year+"]").attr("selected","selected");
						jq("#modify_pStudentEnterYear").selectmenu('refresh', true);
						jq("#modify_pPreferenceCardNo").val(util.isNoValue(passengerInfo.preference_card_no) ? "":passengerInfo.preference_card_no);
						jq("#modify_pPreferenceFromStation").val(passengerInfo.preference_from_station_code);
						jq("#modify_pPreferenceFromStation_code").val(util.getCityByCode(passengerInfo.preference_from_station_code));
						jq("#modify_pPreferenceToStation").val(passengerInfo.preference_to_station_code);
						jq("#modify_pPreferenceToStation_code").val(util.getCityByCode(passengerInfo.preference_to_station_code));
						jq("#passengerOptions").show();
					}
						jq("#deletePassengerOptions").show();
						
					}else{
						jq("#modify_PassengerCountry").val(util.getCountryByCode("CN"));
						jq("#modify_PassengerCountry_code").val("CN");
					}
			}
			
		   contentIscrollRefresh();
	}
	
	function submitPassengerInfo(){
		validate();
		return false;
	}
	
	function isVisiblebusy(){
	
		if(busy.isVisible()){
			busy.hide();
		}
	
	}
	
	
	function validate(){
		
		
		
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
		if(util.isNoValue(jq("#modify_PassengerName").val())){
			isVisiblebusy();
			util.alertMessage("请填写姓名");
			return;
		}
		
		if(!checkFormUtil.checkPersonName(jq("#modify_PassengerName").val())){
			isVisiblebusy();
			util.alertMessage("真实姓名只能填写英文字母或者中文");
			return;
		}
		
		if(util.isNoValue(jq("#modify_PassengerSex").val())){
			isVisiblebusy();
			util.alertMessage("请选择性别");
			return;
		}
		
		if(util.isNoValue(jq("#modify_PassengerIdType").val())){
			isVisiblebusy();
			util.alertMessage("请选择证件类型");
			return;
		}
		
		if(util.isNoValue(jq("#modify_PassengerIdNo").val())){
			isVisiblebusy();
			util.alertMessage("请输入证件号码");
			return;
		}
		
		if(!checkFormUtil.checkIdValidStr(jq("#modify_PassengerIdNo").val())){
			isVisiblebusy();
			util.alertMessage("输入的证件编号中包含中文信息或特殊字符");
			return;
		}
		
		//1 2 B C G 身份证验证
		if(jq("#modify_PassengerIdType").val()=="1"){
			if(!checkFormUtil.validateSecIdCard(jq("#modify_PassengerIdNo").val())){
			isVisiblebusy();
				util.alertMessage("请正确输入18位的身份证号");
				return;
			}
		}
		
		if(jq("#modify_PassengerIdType").val()=="2"){
			if(!checkFormUtil.validateFirIdCard(jq("#modify_PassengerIdNo").val())){
			isVisiblebusy();
				util.alertMessage("请正确输入15或者18位的身份证号");
				return;
			}
		}
		
		if(jq("#modify_PassengerIdType").val()=="B"){
			if(!checkFormUtil.checkPassport(jq("#modify_PassengerIdNo").val())){
			isVisiblebusy();
				util.alertMessage("请输入有效的护照号码");
				return;
			}
		}
		
		if(jq("#modify_PassengerIdType").val()=="C"){
			if(!checkFormUtil.checkHkongMacao(jq("#modify_PassengerIdNo").val())){
			isVisiblebusy();
				util.alertMessage("请输入有效的港澳居民通行证号码");
				return;
			}
		}
		
		if(jq("#modify_PassengerIdType").val()=="G"){
			if(!checkFormUtil.checkTaiw(jq("#modify_PassengerIdNo").val())){
			isVisiblebusy();
				util.alertMessage("请输入有效的台湾居民通行证号码");
				return;
			}
		}
		
		if(util.isNoValue(jq("#modify_PassengerUserType").val())){
			isVisiblebusy();
			util.alertMessage("请选择旅客类型");
			return;
		}
		
		if(!util.isNoValue(jq("#modify_PassengerMobileNo").val())&&!checkFormUtil.isMobile(jq("#modify_PassengerMobileNo").val())){
			isVisiblebusy();
			util.alertMessage("请输入正确的手机号码");
			return;
		}
		
		if(!util.isNoValue(jq("#modify_PassengerEmail").val())&&!checkFormUtil.isEmail(jq("#modify_PassengerEmail").val())){
			isVisiblebusy();
			util.alertMessage("请输入正确的邮箱地址");
			return;
		}
		
		if(!util.isNoValue(jq("#modify_PassengerPostalCode").val())&&!checkFormUtil.isZipCode(jq("#modify_PassengerPostalCode").val())){
			isVisiblebusy();
			util.alertMessage("您输入的邮编不是有效的格式");
			return;
		}
		
		if(jq("#modify_PassengerUserType").val()=="3"){
			
			if(util.isNoValue(jq("#modify_pStudentProvince").val())){
			isVisiblebusy();
				util.alertMessage("请选择学校省份");
				return;
			}
			
			if(util.isNoValue(jq("#modify_pStudentSchool").val())){
							isVisiblebusy();

				util.alertMessage("请选择学校名称");
				return;
			}
			
			if(!util.isNoValue(jq("#modify_pStudentDepartment").val())&&!checkFormUtil.checkNameChar(jq("#modify_pStudentDepartment").val())){
							isVisiblebusy();

				util.alertMessage("填写的院系只能包含中文、英文、数字");
				return;
			}
			
			if(!util.isNoValue(jq("#modify_pStudentClass").val())&&!checkFormUtil.checkNameChar(jq("#modify_pStudentClass").val())){
							isVisiblebusy();

				util.alertMessage("填写的班级只能包含中文、英文、数字");
				return;
			}
			
			if(util.isNoValue(jq("#modify_pStudentNo").val())){
							isVisiblebusy();

				util.alertMessage("请输入学号");
				return;
			}
			
			if(!checkFormUtil.checkNameChar(jq("#modify_pStudentNo").val())){
							isVisiblebusy();

				util.alertMessage("填写的学号只能包含中文、英文、数字");
				return;
			}
			
			if(util.isNoValue(jq("#modify_pStudentSystem").val())){
							isVisiblebusy();

				util.alertMessage("请选择学制");
				return;
			}
			

			if(util.isNoValue(jq("#modify_pStudentEnterYear").val())){
							isVisiblebusy();

				util.alertMessage("请选择入学年份");
				return;
			}
			
			if(!util.isNoValue(jq("#modify_pPreferenceCardNo").val())&&!checkFormUtil.checkNameChar(jq("#modify_pPreferenceCardNo").val())){
							isVisiblebusy();

				util.alertMessage("填写的优惠卡只能包含中文、英文、数字");
				return;
			}
			
			if(util.isNoValue(jq("#modify_pPreferenceFromStation").val())||util.isNoValue(jq("#modify_pPreferenceToStation").val())){
							isVisiblebusy();

				util.alertMessage("请选择优惠区间");
				return;
			}
		}
		
		if(mor.ticket.viewControl.isModifyPassenger&&util.isNoValue(jq("#modify_PassengerCountry").val())){
			jq("#modify_PassengerCountry_code").val("CN");
		}
		
			
		//busy.show();
		
		
		//提交数据
		var util = mor.ticket.util;
		var commonParameters = {			
		     'name' : jq("#modify_PassengerName").val(),
		     'sex_code' : jq("#modify_PassengerSex").val(),
		     'born_date' : util.processDateCode(jq("#modify_PassengerBornDateShow").val()),
		     'country_code' : jq("#modify_PassengerCountry_code").val(),
		     'card_type' : jq("#modify_PassengerIdType").val(),
		     'card_no' : jq("#modify_PassengerIdNo").val(),
		     'passenger_type' : jq("#modify_PassengerUserType").val(),
		     'mobile_no' : jq("#modify_PassengerMobileNo").val(),
		     'phone_no' : jq("#modify_PassengerPhoneNo").val(),
		     'email' : jq("#modify_PassengerEmail").val(),
		     'address' : jq("#modify_PassengerAddress").val(),
		     'postalcode' : jq("#modify_PassengerPostalCode").val(),
		     'province_code' : jq("#modify_pStudentProvince_code").val(),
		     'school_code' : jq("#modify_pStudentSchool_code").val(),
		     'department' : jq("#modify_pStudentDepartment").val(),
		     'school_class' : jq("#modify_pStudentClass").val(),
		     'student_no' : jq("#modify_pStudentNo").val(),
		     'school_system' : jq("#modify_pStudentSystem").val(),
		     'enter_year' : jq("#modify_pStudentEnterYear").val(),
		     'preference_card_no' : jq("#modify_pPreferenceCardNo").val(),
		     'preference_from_station_code' : jq("#modify_pPreferenceFromStation").val(),
		     'preference_to_station_code' : jq("#modify_pPreferenceToStation").val(),
		     'old_name' : mor.ticket.passengerDetail==null?"":mor.ticket.passengerDetail.name,
		     'old_card_type' : mor.ticket.passengerDetail==null?"":mor.ticket.passengerDetail.card_type,
		     'old_card_no' : mor.ticket.passengerDetail==null?"":mor.ticket.passengerDetail.card_no
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: mor.ticket.viewControl.isModifyPassenger?"modifyPassenger":"addPassenger"
		};
		
		var options =  {
				onSuccess: requestSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};

		mor.ticket.passengersCache.newPassenger = {
		    user_name: commonParameters.name,
		    id_type: commonParameters.card_type,
		    id_no: commonParameters.card_no,
		    mobile_no: commonParameters.mobile_no,
		    user_type: commonParameters.passenger_type	
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
	}
	
	function requestSucceeded(result) {
		/*if(busy.isVisible()){
			busy.hide();
		}*/
		
		var invocationResult = result.invocationResult;
		var util = mor.ticket.util;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			//刷新 联系人缓存
			operation = "";
	    	setTimeout(function(){
	    		refreshPassengers();
	    	}, 500);
		} else {
			util.alertMessage(invocationResult.error_msg);
		}
	}
	
	//刷新缓存中联系人的信息
	function refreshPassengers(){
		var util = mor.ticket.util;
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "queryPassenger"
		};
		
		var options =  {
				onSuccess: refreshSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(null, invocationData, options);
	}
	
	function refreshSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			mor.ticket.passengersCache.passengers=[];
			mor.ticket.passengersCache.passengers=invocationResult.passengerResult;

			mor.ticket.viewControl.isNeedRefreshSelectPassengers = true;
			var msg = mor.ticket.viewControl.isModifyPassenger?"修改联系人信息成功":"添加联系人信息成功";
			var passengers = mor.ticket.passengersCache.passengers || [];
			var findIndex = -1;
			var tag_username=jq('#modifyPassengerView').data('username');
			var tag_idno=jq('#modifyPassengerView').data('idno');
			var p;

			if(operation!=="delete"){
				// 在缓存中更细增加或修改的联系人信息
				var newP = mor.ticket.passengersCache.newPassenger;
				var newPState = false;
				tag_username = newP.user_name;
				tag_idno = newP.id_no;
				
				mor.ticket.passengersCache.newPassenger = null;
				if(newP != undefined && tag_idno&& tag_idno !==''&& tag_username && tag_username!==''){
					for (var i = 0, len = passengers.length; i < len; i++ ) {
						p = passengers[i];
						
						if (p.user_name === tag_username && p.id_no === tag_idno) {
							findIndex = i;
							break;
						}
					}
				}
				if(findIndex!==-1 ){
					passengers[findIndex]=newP;
				}else{
					passengers.push(newP);
				}
			}else{
				msg = "删除联系人信息成功";
				for (var i = 0, len = passengers.length; i < len; i++ ) {
					p = passengers[i];
					if (p['user_name'] === tag_username && p['id_no'] === tag_idno) {
						findIndex = i;
						break;
					}
				}
				if(findIndex!==-1){
					passengers.splice(findIndex,1);
				}
			}

//			if(mor.ticket.passengerList.length>0){
//				findIndex=-1;
//				for (var i = 0, len = mor.ticket.passengerList.length; i < len; i++ ) {
//					p = mor.ticket.passengerList[i];
//					if (p.user_name === tag_username && p.id_no === tag_idno) {
//						findIndex=i;
//						break;
//					}
//				}
//				if(findIndex!==-1){
//					if(operation!=="delete"){
//						mor.ticket.passengerList[findIndex].user_name=newP.user_name;
//						mor.ticket.passengerList[findIndex].id_no=newP.id_no;
//						mor.ticket.passengerList[findIndex].id_type=newP.id_type;
//						mor.ticket.passengerList[findIndex].user_type=newP.user_type;
//						mor.ticket.passengerList[findIndex].mobile_no=newP.mobile_no;
//					}else{
//						mor.ticket.passengerList.splice(findIndex,1);	
//					}
//				}
//			}

			mor.ticket.passengersCache.sortPassengers();
			
			WL.SimpleDialog.show(
					"温馨提示", 
					msg, 
					[ {text : '确定', handler: function() {
						if (mor.ticket.history.url=='fareList'){		
					     	jq.mobile.changePage("fareList.html");
							return ;
							
						}if (mor.ticket.history.url=='bookticket'){		
					     	jq.mobile.changePage("fareList.html");
							return ;
							
						}else{
							jq.mobile.changePage("passengerList.html");
						}
													  
					}}]
				);			
		} else {
			util.alertMessage(invocationResult.error_msg);
		}
	}
	
	function deletePassenger(){
		var util = mor.ticket.util;
		var commonParameters = {
			'name' : mor.ticket.passengerDetail.name,
			'card_type' : mor.ticket.passengerDetail.card_type,
			'card_no' :mor.ticket.passengerDetail.card_no,
			'passenger_type' :mor.ticket.passengerDetail.passenger_type			
		};
		
		var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "deletePassenger"
		};
		
		var options =  {
				onSuccess: deletePassengerSucceeded,
				onFailure: util.creatCommonRequestFailureHandler()
		};
		mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		return false;
	}
	
	function deletePassengerSucceeded(result){
		if(busy.isVisible()){
			busy.hide();
		}
		var invocationResult = result.invocationResult;
		var util = mor.ticket.util;
		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			operation = "delete";
			setTimeout(function(){
				refreshPassengers();
			}, 500);
		} else {
			util.alertMessage(invocationResult.error_msg);
		}
	}
	function registerToDateInputChangeListener(){
		jq("#modify_PassengerBornDateShow").bind("tap",function(){
			jq('#modify_PassengerBornDate').scroller('show');
			return false;
		});
		jq('#modify_PassengerBornDate').bind("change",function(){
			var date = jq(this).val();
			jq("#modify_PassengerBornDateShow").val(date);
		});
	}
	/*function registerModify_PassengerSexChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#modify_PassengerSex");
	}
	
	function registerModify_PassengerIdTypeChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#modify_PassengerIdType");
	}
	
	function registerModify_PassengerUserTypeChangeHandler(){
		mor.ticket.util.bindSelectFocusBlurListener("#modify_PassengerUserType");
		jq("#modify_PassengerUserType").bind("change",changeEvent);
	}
	
	function registerModify_pStudentSystemChangeHandler(){
		//mor.ticket.util.bindSelectFocusBlurListener("#modify_pStudentSystem");
	}*/

})();