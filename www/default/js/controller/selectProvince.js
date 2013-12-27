
/* JavaScript content from js/controller/selectProvince.js in folder common */
(function(){
	var prevPage; 
	jq("#selectProvinceView").live("pagebeforeshow", function(e,data){
		var provinceOptions = mor.ticket.cache.province;
		jq("#provinceList").html(generateProvinceOption(provinceOptions)).listview("refresh");
		prevPage = data.prevPage.attr("id"); 
		registerProvinceListItemClickHandler();
		
	});

	var provinceOptionsTemplate =
       
        "{{ for(var i=0;i<it.length;i++) { }}"+ 
            "<li provinceid='{{=it[i].id}}' data-filtertext='{{=it[i].pinyin}}'><a>{{=it[i].value}}</a></li>"+
        "{{ } }}";
	var generateProvinceOption = doT.template(provinceOptionsTemplate);
	
	function registerProvinceListItemClickHandler(){
		jq("#provinceList").off().on("tap", "li", function(e){
			e.stopImmediatePropagation();
			jq(this).addClass("ui-btn-active")
				.siblings().removeClass("ui-btn-active");
			var util = mor.ticket.util;
			var province_code = jq(this).attr("provinceid");
//			if(prevPage=== "registView_other"){
			if(prevPage==="registView"){
				jq("#regist_studentProvince").val(util.getProvinceByCode(province_code));
				jq("#regist_studentProvince_code").val(province_code);
			}else if(prevPage=== "modifyuserInfoView"){
				jq("#modify_studentProvince").val(util.getProvinceByCode(province_code));
				jq("#modify_studentProvince_code").val(province_code);
			}else if(prevPage=== "modifyPassengerView"){
				jq("#modify_pStudentProvince").val(util.getProvinceByCode(province_code));
				jq("#modify_pStudentProvince_code").val(province_code);
			}else{
				
			}
			// fix '点透' 问题
		    if(mor.ticket.util.isAndroid()&& (parseFloat(device.version) > 3.0)){
		    	mor.ticket.util.transitionFlag = true;
		    	setTimeout(function(){
		    		if(mor.ticket.util.transitionFlag){
		    			history.back();
		    			mor.ticket.util.transitionFlag = false;
		    		}
		    	}, 300);
		    }else{
		    	history.back();
		    }

			return false;
		});
	};
	
})();