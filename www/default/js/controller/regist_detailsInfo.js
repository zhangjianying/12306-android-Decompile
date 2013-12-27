
/* JavaScript content from js/controller/regist_detailsInfo.js in folder common */
(function(){
	var prevPage;
	/*jq("#registView_details").live("pagecreate", function() {
		mor.ticket.util.androidRemoveIscroll("#registView_details");
	});*/
	var focusArray=[];
	jq("#registView_details").live("pageshow", function() {
		focusArray=[];
	});
	function registerAutoScroll(){	
		var util = mor.ticket.util;
		util.enableAutoScroll('#regist_name',focusArray);
		util.enableAutoScroll('#regist_idNo',focusArray);
	}
	
	jq("#registView_details").live("pageinit", function() {
		var util = mor.ticket.util;
		//mjl
		registerAutoScroll();
		var checkFormUtil = mor.ticket.checkForm;
	jq("#regist_detailsNextBtn").bind("click",function(){
		
		if(validata()){
			common();
		jq.mobile.changePage("regist_relationType.html");
		return false;
		}
	});
	
	
	var countryMap = mor.cache.countryMap;
	if(countryMap){
		var countryList = mor.ticket.cache.country;
		for(var i = 0; i < countryList.length; i++){
			countryMap[countryList[i].id] = countryList[i].value;
		};
	}
	
	var cityMap = mor.cache.cityMap;
	if(cityMap){
		var cityList = mor.ticket.cache.city;
		for(var zz = 0; zz < cityList.length;zz++){
			cityMap[cityList[zz].city_code] = cityList[zz].city_name;
		};
	}
	
	jq("#regist_detailsBackBtn").bind("click",function(){
		jq.mobile.changePage("regist_basicInfo.html");
	});
	jq("#regist_country").bind("tap",function(){
		jq.mobile.changePage("selectCountry.html");
		return false;
	});
	
	//鼠标离开验证 姓名 性别 出生日期 国家地区 证件类型 证件号
	jq("#regist_name").blur(function () { 
		if(util.isNoValue(jq("#regist_name").val())){
			util.alertMessage("请填写姓名");
			return;
		}
		if(!checkFormUtil.checkChar(jq("#regist_name").val())){
			util.alertMessage("真实姓名只能填写英文字母或者中文!");
			return;
		}
	} );
	jq("#regist_idNo").blur(function () { 
		if(util.isNoValue(jq("#regist_idNo").val())){
			util.alertMessage("请输入证件号码");
			return;
		}
		if(!checkFormUtil.checkIdValidStr(jq("#regist_idNo").val())){
			util.alertMessage("输入的证件编号中包含中文信息或特殊字符!");
			return;
		}
	} );
	initSelects();
	//init
	registerToDateInputChangeListener();
	});
	//DATE
	function registerToDateInputChangeListener(){
		jq("#regist_bornDateShow").bind("tap",function(){
			jq('#regist_bornDate').scroller('show');
			alert(jq("#regist_bornDateShow").val());
			return false;
		});
		jq('#regist_bornDate').bind("change",function(){
			var date = jq(this).val();
			jq("#regist_bornDateShow").val(date);
			alert(jq("#regist_bornDateShow").val());
		});
	}
	//鼠标离开验证 姓名 性别 出生日期 国家地区 证件类型 证件号
	function validata(){
		var util = mor.ticket.util;
		var checkFormUtil = mor.ticket.checkForm;
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
		return true;
	}
	
	function initSelects(){
		jq("#regist_sex,#regist_idType").scroller({
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
		
	}
	//前
	jq("#registView_details").live("pagebeforeshow", function(e, data) {
		mor.ticket.viewControl.tab4_cur_page="regist_detailsInfo.html";
		prevPage = data.prevPage.attr("id");
		var user=mor.ticket.loginUser;
		if(user.isAuthenticated==="Y"){
			jq("#registBack_DetailsValue").html("更多功能");
		}
		else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				jq("#registBack_DetailsValue").html("登录");
				} else {
					registerAutoLoginHandler(function(){
						jq("#registBack_DetailsValue").html("更多功能");

					}, function(){
						jq("#registBack_DetailsValue").html("登录");

					});
				}

		}
		jq("#registBack_DetailsBtn").bind("tap",function(){
			if("更多功能"===jq("#registBack_DetailsValue").html())
			{
			jq.mobile.changePage("moreOption.html");
			}
		else{
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
		alert(jq("#regist_bornDateShow").val());
		if(jq("#regist_bornDateShow").val()){
			jq("#regist_bornDateShow").val(mor.ticket.util.processDateCode(jq("#regist_bornDateShow").val()));
		}else{
			jq("#regist_bornDateShow").val(mor.ticket.util.processDateCode("1980-01-01"));
		}
		jq("#regist_studentEnterYear").val(mor.ticket.util.getNewDate().getFullYear());
	});
	// 替换特殊字符
	function replaceChar(str) {
		var v = str.replace(/['"<> ?]/g,"");
		return v;
	}
	
	function common(){
		var util=mor.ticket.util;
		mor.ticket.registInfo.name=replaceChar(jq("#regist_name").val());
		mor.ticket.registInfo.sex_code=jq("#regist_sex").val();
		mor.ticket.registInfo.born_date=util.processDateCode(jq("#regist_bornDate").val());
		mor.ticket.registInfo.country_code= jq("#regist_country_code").val();
		mor.ticket.registInfo.id_type_code=jq("#regist_idType").val();
		mor.ticket.registInfo.id_no=jq("#regist_idNo").val();
	}
})();