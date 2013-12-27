
/* JavaScript content from js/controller/fareList.js in folder common */
(function() {
	/*jq("#selectFarePassengerView").live("pagecreate", function() {
		//mor.ticket.util.androidRemoveIscroll("#selectFarePassengerView");
	});*/
	
	
	var farelist = [];
	var cache_passenger=[];
	jq("#selectFarePassengerView").live("pageinit", function() {
				

		var countryMap = mor.cache.countryMap;
		if(countryMap){
			var countryList = mor.ticket.cache.country;
			for(var i = 0; i < countryList.length; i++){
				countryMap[countryList[i].id] = countryList[i].value;
			};
		}
		
		var universityMap = mor.cache.universityMap;
		if(universityMap){
			var universityList = mor.ticket.cache.university;
			for(var z = 0; z < universityList.length;z++){
				universityMap[universityList[z].university_code] = universityList[z].university_name;
			};
		}
		

		
		var cityMap = mor.cache.cityMap;
		if(cityMap){
			var cityList = mor.ticket.cache.city;
			for(var zz = 0; zz < cityList.length;zz++){
				cityMap[cityList[zz].city_code] = cityList[zz].city_name;
			};
		}
				
		jq(".ui-input-search").width("80%");
		jq(".ui-listview-filter").append("<div class=\"farelistadd\"><a id=\"addFareBtn\" ></a></div>");
		jq(".ui-input-search").css("background-color: #ccc;");
		registerSelectFarePassengerBackClickHandler();
		addFareBtnClickHandler();
		registFareSlider_navClickHandler();
	});
	
	function fareListFn(){
		showCheckedSelectd();
		selectPassengerFunBtnClickHandler();
		registerfarePassengerListItemClickHandler();
		mor.ticket.viewControl.isNeedRefreshSelectPassengers = true;
		var deviceHeight = document.body.clientHeight + window.screenTop;
		var headerHeight = 44;
		var footerHeight = 50;
		var topHeight = 120;
		var sliderHeight = deviceHeight - footerHeight - topHeight - headerHeight;
		var size = parseInt(jq("#selectPassengerNavLength").val());
		//fix navList height bug
		jq('#selectPassenger_slider_nav-nav').height(sliderHeight);
		jq('#fareSlider_navList').height(sliderHeight);
		jq("#fareSlider_navList li").height(sliderHeight/size);

	}
	
	jq("#selectFarePassengerView").live("pagebeforeshow", function() {
	
		   if(mor.ticket.loginUser.isAuthenticated !== "Y"){
				mor.ticket.util.keepPageURL();

			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
			} else {
				registerAutoLoginHandler(fareListFn, autologinFailJump);
			}

				
		   }else{
			   fareListFn();
		   }
	});
	
	
	function registerSelectFarePassengerBackClickHandler(){
		jq("#selectFarePassengerBackBtn").off().bind("tap",function(e){
			mor.ticket.passengerList=jq.merge([],cache_passenger);
            if (mor.ticket.history.url=='bookticket'){
                bookticketFun();
            }else{
                fareFun();
            }
			return false;
		});
	}
	
	
	
	
	function bookticketFun(){
			jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
			return ;
	}



	function fareFun(){
			jq.mobile.changePage(vPathCallBack()+"passengers.html");
	}

	
	
	function selectPassengerFunBtnClickHandler(){
		jq("#selectFarePassengerFunBtn").off().bind("tap",function(e){
	        if (mor.ticket.history.url=='bookticket'){
		  	   bookticketFun();
			}else{
				fareFun();
			}
			return false;
		});
	}
	
	 
	 
	  function farelistAddFareFn(){
			jq("#addFareBtn").off();
			var util = mor.ticket.util;
			if (mor.ticket.history.url==''){
				mor.ticket.history.url='fareList';
			}
		    var passengerInfo = mor.ticket.passengerDetail;
		    var util = mor.ticket.util;		
			util.clearObject(mor.ticket.passengerDetail);
			mor.ticket.viewControl.isModifyPassenger = false;
			jq("#modifyPassengerView").remove();
			jq.mobile.changePage("modifyPassenger.html", true);
			setTimeout(function(){		jq("#addFareBtn").off().on("tap",farelistAddFareFn);},1000);
			return false;
		}
	 
	 function addFareBtnClickHandler(){
		 
		jq("#addFareBtn").off().on("tap",farelistAddFareFn);
		 
		 
		 
	 }
	
	
	
	
	
	
	function registFareSlider_navClickHandler(){
		jq("#selectFarePassengerView a[id$='_Anchor']").off().on("tap",function(){
			var anchor = jq(this).attr("href").charAt(1);
			var curSelector = "#selectFarePassengerView #slectpassengers_"+anchor;
			var jqNode=jq(curSelector);
			var top=0;
			while(!jqNode.hasClass('iscroll-scroller')){
				top+=jqNode.position().top;
				jqNode=jqNode.offsetParent();
			}
			
			var screenHeight = jq("#selectFarePassengerView .iscroll-wrapper").height();
			var contentHeight = jq("#selectFarePassengerView .iscroll-scroller").height();
			var y=(top+screenHeight<=contentHeight)?top:contentHeight-screenHeight;
			jq("#selectFarePassengerView .iscroll-wrapper").iscrollview('scrollTo',0,-y,0);
		    return false;
		});
	};
	
	function registerfarePassengerListItemClickHandler() {

		jq("#farePassengerList").off().on("tap", "li",function(e) {
			if(jq(this).attr("data-role")!=null&&jq(this).attr("data-role")=='list-divider'){
				jq(this).removeClass("ui-btn-active");
				return false;
			}
			var index = jq(this).attr("data-index");
			var passengers = mor.ticket.passengersCache.passengers;
			 if (passengers=="" || passengers==undefined){
				  passengers = [[]];
			 }
			var selectedPassenger = passengers[index];
			var int_auto = mor.ticket.passengerList.length;
			if (jq(this).find("input").is(":checked")){
				jq(this).find("input").attr("checked", false);
				for(var i=0;i<mor.ticket.passengerList.length;i++){
					if(mor.ticket.passengerList[i].id_no== selectedPassenger.id_no && selectedPassenger.user_name == mor.ticket.passengerList[i].user_name){
			          mor.ticket.passengerList.splice(i,1);
					 break
				  }
			  }
			}else{
				if (int_auto>4){
					mor.ticket.util.alertMessage("乘车人最多选择5个");
				}else{
					var init_auto=0;
					  for(var ii =0;ii<mor.ticket.passengerList.length;ii++){
						  if(mor.ticket.passengerList[ii].id_no== selectedPassenger.id_no && selectedPassenger.user_name == mor.ticket.passengerList[ii].user_name){
							  init_auto++;
						  }
					  }
					  if (init_auto>0){
						jq(this).find("input").attr("checked", true);
					  }else{
						jq(this).find("input").attr("checked", true);
						
						 var A = [];
						 A['seat_type']     = '';
						 A['user_type']     = selectedPassenger.user_type;
						 A['id_type']       = selectedPassenger.id_type;
						 A['id_no']         = selectedPassenger.id_no;
						 A['user_name']     = selectedPassenger.user_name;
						 A['mobile_no']     = selectedPassenger.mobile_no;
						 A['ticket_type']     = selectedPassenger.user_type;
						 mor.ticket.passengerList.push(A);
					  }
				}
				
			}
			return false;
		});
		
	};
	
	
	// 初始化复选框
	function showCheckedSelectd(){
			var passengers = mor.ticket.passengersCache.passengers;
			cache_passenger=jq.merge([],mor.ticket.passengerList);
			 if (passengers=="" || passengers==undefined){
				  passengers = [[]];
			 }
			 
			if(passengers[0].length != 0){
				 for(var i=0; i < passengers.length; i++){
					 mor.ticket.passengersCache.passengers[i].checked =0; 
					 for(var ii=0; ii < mor.ticket.passengerList.length; ii++){
						   if ( mor.ticket.passengerList[ii].id_no==passengers[i].id_no && mor.ticket.passengerList[ii].user_name==passengers[i].user_name){
					          mor.ticket.passengersCache.passengers[i].checked =1; 
						   }
						  
					  }
				 }
			  
				jq("#farePassengerList").html(generatefarePassengerList(passengers)).listview("refresh");
			}	
			if(mor.ticket.loginUser.id_type != "1" && mor.ticket.loginUser.id_type != "2"){
				for(var i=0;i<passengers.length;i++){
					if(passengers[i].id_type=='1' || passengers[i].id_type == '2'){
						jq("#farePassengerList li[data-index='"+i+"']").addClass("ui-disabled");
					}
				}	
			}
   }
	

	var farePassengerListTempate = "{{~it :passenger:index}}"
			+ "{{ if(index==0) { }}"
			+ "<li data-role='list-divider' id='{{=passenger.user_nameSM.toUpperCase().charAt(0)}}'>当前用户</li>"
			+ "{{ } }}"
			+ "{{else if(index==1) { }}"//两个名字首字母相同情况下纠正
			+ "<li data-role='list-divider' id='slectpassengers_{{=it[index].user_nameSM.toUpperCase().charAt(0)}}'>{{=it[index].user_nameSM.toUpperCase().charAt(0)}}</li>"
			+ "{{ } }}"
			+ "	{{else if(passenger.user_nameSM.toUpperCase().charCodeAt(0)!=it[index-1].user_nameSM.toUpperCase().charCodeAt(0)){}}"
			+ "	    <li data-role='list-divider' id='slectpassengers_{{=passenger.user_nameSM.toUpperCase().charAt(0)}}'>{{=passenger.user_nameSM.toUpperCase().charAt(0)}}</li>"
			+ "	{{}}}" 
			+ "<li data-val='' data-index='{{=index}}'><a>"
			+ "<div class='ui-grid-b'>"
			+ " {{ if (passenger.checked==1) { }} "
			
			+ "<div class='ui-block-a'><input id='list_{{=index}}' type=\"checkbox\"  checked value=\"{{=index}}\"></div>"
			
			+ "{{ }else{ }}"
			+ "<div class='ui-block-a'><input id='list_{{=index}}' type=\"checkbox\"  value=\"{{=index}}\"></div>"
			+ "{{ } }}"
			+ "<div class='ui-block-b'>{{=passenger.user_name}}</div>"
			+ "<div class='ui-block-c'>{{=mor.ticket.util.getPassengerTypeName(passenger.user_type)}}</div>"
			+ "<div class='ui-block-d'>{{=passenger.id_no}}</div>"
			+ "<div class='ui-block-e' style='display:none'>{{=passenger.user_nameSM}}</div>"
			+ "</div></a>"
			+ "</li>"
			+ "{{~}}";
	var generatefarePassengerList = doT.template(farePassengerListTempate);

	var fareSlider_navListTempate =
		"{{var size=0;}}" 
		+ "{{ for(var i=1;i<it.length;i++){ }}"
		+ "	{{if(i==1){}}"
		+ "	    <li><a id='{{=it[i].user_nameSM.toUpperCase().charAt(0)}}_Anchor' href='#{{=it[i].user_nameSM.toUpperCase().charAt(0)}}' class='anchor'>{{=it[i].user_nameSM.toUpperCase().charAt(0)}}</a></li>"
		+ " {{size++;}}"
		+ "	{{}}}"
		+ "	{{else if(it[i].user_nameSM.toUpperCase().charCodeAt(0)!=it[i-1].user_nameSM.toUpperCase().charCodeAt(0)){}}"
		+ "	    <li><a id='{{=it[i].user_nameSM.toUpperCase().charAt(0)}}_Anchor' href='#{{=it[i].user_nameSM.toUpperCase().charAt(0)}}' class='anchor'>{{=it[i].user_nameSM.toUpperCase().charAt(0)}}</a></li>"
		+ " {{size++;}}"
		+ "	{{}}}"
		+ "{{ } }}"
		+ " <input id='selectPassengerNavLength'  type='hidden' value='{{=size}}' />";
	var fareSlider_navList = doT.template(fareSlider_navListTempate);
})();