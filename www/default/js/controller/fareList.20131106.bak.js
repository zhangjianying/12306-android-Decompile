
/* JavaScript content from js/controller/fareList.20131106.bak.js in folder common */
(function() {
	/*jq("#selectFarePassengerView").live("pagecreate", function() {
		//mor.ticket.util.androidRemoveIscroll("#selectFarePassengerView");
	});*/
	
	
	jq("#selectFarePassengerView").live("pageinit", function() {
				
		mor.ticket.passenger=[];
		







		var countryMap = mor.cache.countryMap;
		if(countryMap){
			var countryList = mor.ticket.cache.country;
			for(var i = 0; i < countryList.length; i++){
				countryMap[countryList[i].id] = countryList[i].value;
			};
		}
//init university id value
		
		var universityMap = mor.cache.universityMap;
		if(universityMap){
			var universityList = mor.ticket.cache.university;
			for(var z = 0; z < universityList.length;z++){
				universityMap[universityList[z].university_code] = universityList[z].university_name;
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
				
		jq(".ui-input-search").width("80%");
		jq(".ui-listview-filter").append("<div class=\"farelistadd\"><a id=\"addFareBtn\" ></a></div>");
		jq(".ui-input-search").css("background-color: #ccc;");
			
	

		
		
		
		registerSelectFarePassengerBackClickHandler();
		
	
		
		
		
		addFareBtnClickHandler();
		
		registFareSlider_navClickHandler();
		
		
		
	});
	
	jq("#selectFarePassengerView").live("pagebeforeshow", function() {
	
			

			
		   if(mor.ticket.loginUser.isAuthenticated == "N"){
				mor.ticket.util.keepPageURL();
				jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
				return;
		   }
		   
		   mor.ticket.tmpfarelist = [];
			
			
			selectPassengerFunBtnClickHandler();
			
			registerfarePassengerListItemClickHandler();
			
			
			var passengers = mor.ticket.passengersCache.passengers;
			
			
			 if (passengers=="" || passengers==undefined){
				  passengers = [[]];
			 }
			
			
			
			if(passengers[0].length != 0){
					jq("#farePassengerList").html(generatefarePassengerList(passengers)).listview("refresh");
			}			
			
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


		



			if(mor.ticket.loginUser.id_type != "1" && mor.ticket.loginUser.id_type != "2"){
				for(var i=0;i<passengers.length;i++){
					if(passengers[i].id_type=='1' || passengers[i].id_type == '2'){
						jq("#farePassengerList li[data-index='"+i+"']").addClass("ui-disabled");
					}
				}	
			}

		
	});
	
	
	function registerSelectFarePassengerBackClickHandler(){
		jq("#selectFarePassengerBackBtn").live("tap",function(e){
			

			var session_out_page = mor.ticket.viewControl.session_out_page;
			
            if (session_out_page==""){
				jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
			}else{
				jq.mobile.changePage(vPathViewCallBack()+session_out_page);
			}
			
			return false;
			
		});

		

	}
	
	
	
	
	function bookticketFun(){
		
		    //alert("Book:"+mor.ticket.passenger.length);
			if(mor.ticket.passenger.length==undefined){
				 mor.ticket.farelist= [];
			}else{
				
				mor.ticket.farelist= [];
				mor.ticket.farelist = mor.ticket.passenger;
				mor.ticket.passenger= [];
			}
			
			
		//	alert("Book Fare:"+mor.ticket.farelist.length);
			
			jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
			return ;
	}



	function fareFun(){
		
		
		
			if (mor.ticket.farelist.length!='0' || mor.ticket.passenger.length!='0'){
				
			    mor.ticket.passenger.concat(mor.ticket.passenger,mor.ticket.farelist);
			 
	        }else if (mor.ticket.farelist.length!='0' || mor.ticket.passenger.length=='0'){
				
				mor.ticket.passenger  = mor.ticket.farelist;
				
				mor.ticket.passenger.sort(mor.ticket.passenger);
				
			}else if (mor.ticket.farelist.length=='0' || mor.ticket.passenger.length!='0'){
			
				mor.ticket.passenger  = mor.ticket.passenger;
				
				mor.ticket.passenger.sort(mor.ticket.passenger);
				mor.ticket.farelist = mor.ticket.passenger;
				
			}else{
				
				mor.ticket.farelist = [];
				
				mor.ticket.passenger = [];
			
			}
			
			jq.mobile.changePage(vPathCallBack()+"passengers.html");
			
			
	
			 
			
	}

	
	
	function selectPassengerFunBtnClickHandler(){
		jq("#selectFarePassengerFunBtn").bind("tap",function(e){
			
			
	
	     
	
	        if (mor.ticket.history.url=='bookticket'){
				
			
		  	   bookticketFun();
			   return false;
			
			}else{
				
				fareFun();
				return false;
				
			}
	       
			
			
			/*var session_out_page = mor.ticket.viewControl.session_out_page;
			
            if (session_out_page==""){
				jq.mobile.changePage(vPathViewCallBack()+"MobileTicket.html");
			}else{
				jq.mobile.changePage(vPathViewCallBack()+session_out_page);
			}*/
			
			
			
			
		});

		

	}
	
	 
	 
	function farelistAddFfareFn(){
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
		setTimeout(function(){
			jq("#addFareBtn").off.on("tap",farelistAddFfareFn);
		},1000);
		jq.mobile.changePage("modifyPassenger.html", true);
		return false;

		
		
		
	}
	 
	 function addFareBtnClickHandler(){
		 
		jq("#addFareBtn").off().on("tap",farelistAddFfareFn);
		 
		 
		 
	 }
	
	
	
	
	
	
	function registFareSlider_navClickHandler(){
		jq("#selectFarePassengerView a[id$='_Anchor']").live("tap",function(){
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
														 
			//e.stopImmediatePropagation();
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
			
			
			
			var int_auto = 0;
			
			
			for(var i in mor.ticket.tmpfarelist){
				int_auto++;

			}
			


			
			if (jq(this).find("input").is(":checked")){
				jq(this).find("input").attr("checked", false);
				
				
				
				mor.ticket.tmpfarelist.splice(index,1);
				
				alert(index+"\n"+mor.ticket.tmpfarelist.length+"\n"+mor.ticket.farelist.length);
				
			}else{
				if (int_auto>4){
					mor.ticket.util.alertMessage("乘车人最多选择5个");
				}else{
					
					jq(this).find("input").attr("checked", true);
					mor.ticket.tmpfarelist.push([selectedPassenger.id_no,selectedPassenger.user_name,selectedPassenger.id_type,selectedPassenger.mobile_no,selectedPassenger.user_type]);
				}
				
			}
			
		
		 
		  mor.ticket.passenger = mor.ticket.tmpfarelist;
		/*	
		  mor.ticket.Passenger.farelist = farelist;
			

			
			mor.ticket.passenger = farelist;
		*/	
			
			
			
			// fix '点透' 问题
	    //	mor.ticket.util.transitionFlag = true;
	    	/*setTimeout(function(){
	    		if(mor.ticket.util.transitionFlag){
	    			//history.back();
	    			jq.mobile.changePage(vPathCallBack()+"passengers.html");
	    			mor.ticket.util.transitionFlag = false;
	    		}
	    	}, 300);
			*/
/*
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
*/
			 return false;
			});
	};

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
			+ "<li data-index='{{=index}}'><a>"
			+ "<div class='ui-grid-b'>"
			+ "<div class='ui-block-a'><input id='list_{{=index}}' type=\"checkbox\" value=\"{{=index}}\"></div>"
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
		+ " <input id='selectPassengerNavLength'  type='hidden' value='{{=size}}'></input>";
	var fareSlider_navList = doT.template(fareSlider_navListTempate);
})();