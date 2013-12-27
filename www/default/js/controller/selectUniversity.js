
/* JavaScript content from js/controller/selectUniversity.js in folder common */
(function(){
	var prevPage; 
	
	var pagePerSize=20;
	var pageKeepSize=1;
	var pageIndex=0;
	var pageTotalItems=[];
	jq("#selectUniversityView").live("pageinit", function() {
		jq("#selectUniversityView .iscroll-wrapper").bind({
			"iscroll_onpulldown" : onPullDown,
			"iscroll_onpullup" : onPullUp,
			"iscroll_onpullupreset" : onPullUpReset
		});
		
		registerSearchHandler();
	});
	
	function registerSearchHandler(){
		jq(document).on("input change", "#searchUniversityInput", function(e) {
			e.stopImmediatePropagation();
			var key = jq(this).val();
			setTimeout(function(){searchAndUpdateList(key);},0);
			return false;
		});
	}
	
	function searchAndUpdateList(key){
		if(!key||!checkSearch(key)){
			jq("#universityList").html(generateSchoolOption(fetchPageContent(0))).listview("refresh");
		}else{
			var lowerKey=key.toLowerCase();
			var filteredArray=jq.grep(mor.ticket.cache.university,function(item,index){
				return (item.pinyin.toLowerCase().indexOf(lowerKey)>=0)||(item.university_name.indexOf(lowerKey)>=0);
			});
			jq("#universityList").html(generateSchoolOption(filteredArray)).listview("refresh");
		}
	}
	function checkSearch(str){
		return  (/^[a-zA-Z\s]+$/.test(str) && str.length>=6)||(/^[\u4E00-\u9FA5]+$/.test(str) &&str.length>=2);
	}
	function fetchPageContent(offset){
		if(offset>0){
			if(pageIndex==pageTotalItems.length)return null;
			pageIndex=(pageIndex+pagePerSize<=pageTotalItems.length)?(pageIndex+pagePerSize):pageTotalItems.length;
			if(pageIndex>=pageKeepSize)pageIndex-=pageKeepSize;
		}
		else if(offset<0){
			if(pageIndex==0)return null;
			pageIndex+=pageKeepSize;
			pageIndex=(pageIndex-pagePerSize>=0)?(pageIndex-pagePerSize):0;
		}
		var pageArray=pageTotalItems.filter(function(item, index, array){
			return (index>=pageIndex)&&(index<pageIndex+pagePerSize);
		});
		return pageArray;
	}

	function onPullDown() {
		if(jq("#searchUniversityInput").val()!="")return;
		var pageArray=fetchPageContent(-1);
		if(pageArray){
			if (!busy.isVisible()) {
				busy.show();
			}
			jq("#universityList").html(generateSchoolOption(pageArray)).listview("refresh");
			//scroll to bottom
			var wrapperHeight=jq("#selectUniversityView .iscroll-wrapper")[0].clientHeight;
			var contentHeight=jq("#selectUniversityView .iscroll-content")[0].clientHeight;
			jq("#selectUniversityView .iscroll-wrapper").iscrollview("scrollTo",0,wrapperHeight-contentHeight,0);
		}
	}

	function onPullUp() {
		if(jq("#searchUniversityInput").val()!="")return;
		var pageArray=fetchPageContent(1);
		if(pageArray){
			if (!busy.isVisible()) {
				busy.show();
			}
			jq("#universityList").html(generateSchoolOption(pageArray)).listview("refresh");
			//scroll to top
			jq("#selectUniversityView .iscroll-wrapper").iscrollview("scrollTo",0,0,0);
		}
	}
	
	function onPullUpReset(){
		setTimeout(function(){
			if(busy.isVisible()){
				busy.hide();
			}
    	}, 500);
	}
		
	jq("#selectUniversityView").live("pagebeforeshow", function(e,data){
		var util = mor.ticket.util;
		var provinceCode = "";
		prevPage = data.prevPage.attr("id"); 
		if(prevPage=== "modifyPassengerView"){
			provinceCode = jq("#modify_pStudentProvince_code").val();
//		}else if(prevPage=== "registView_other"){
		}else if(prevPage=== "registView"){	
			provinceCode = jq("#regist_studentProvince_code").val();
		}else if(prevPage=== "modifyuserInfoView"){
			provinceCode = jq("#modify_studentProvince_code").val();
		}else{
			provinceCode = "";
		}
		
		pageTotalItems  = util.getUniversityByProvince(provinceCode);
//		pageTotalItems = mor.ticket.cache.university;
		jq("#universityList").html(generateSchoolOption(fetchPageContent(0))).listview("refresh");
		registerUniversityListItemClickHandler();
	});
	
	var universityTemplate =
	       
        "{{ for(var i=0;i<it.length;i++) { }}"+ 
            "<li universityid='{{=it[i].university_code}}' data-filtertext='{{=it[i].pinyin}}' ><a>{{=it[i].university_name}}</a></li>"+
        "{{ } }}";
	var generateSchoolOption = doT.template(universityTemplate);
	
	function registerUniversityListItemClickHandler(){
		jq("#universityList").off().on("tap", "li", function(e){
			e.stopImmediatePropagation();
			jq(this).addClass("ui-btn-active")
				.siblings().removeClass("ui-btn-active");
			var util = mor.ticket.util;
			var school_code =  jq(this).attr("universityid");
			if(prevPage=== "modifyPassengerView"){
				jq("#modify_pStudentSchool").val(util.getUniversityByCode(school_code));
				jq("#modify_pStudentSchool_code").val(school_code);
			}else if(prevPage=== "registView"){
				jq("#regist_studentSchool").val(util.getUniversityByCode(school_code));
				jq("#regist_studentSchool_code").val(school_code);
			}else if(prevPage=== "modifyuserInfoView"){
				jq("#modify_studentSchool").val(util.getUniversityByCode(school_code));
				jq("#modify_studentSchool_code").val(school_code);
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
	}
	
})();