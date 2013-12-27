
/* JavaScript content from js/controller/selectPassenger.js in folder common */
(function() {
	/*jq("#selectPassengerView").live("pagecreate", function() {
		//mor.ticket.util.androidRemoveIscroll("#selectPassengerView");
	});*/
	jq("#selectPassengerView").live("pageinit", function() {
		var passengers = mor.ticket.passengersCache.passengers;
		registerPassengerListItemClickHandler(passengers);
		registerSelectPassengerBackClickHandler();
		registSlider_navClickHandler();
		if(passengers[0].length != 0){
		    jq("#passengerList").html(generatePassengerList(passengers)).listview("refresh");
		    jq("#slider_navList").html(slider_navList(passengers));
		}
	});
	function selectPsgFn(){
		var passengers = mor.ticket.passengersCache.passengers;
		if(mor.ticket.viewControl.isNeedRefreshSelectPassengers){
			if(passengers[0].length != 0){
				jq("#passengerList").html(generatePassengerList(passengers)).listview("refresh");
				jq("#slider_navList").html(slider_navList(passengers));
			}			
			mor.ticket.viewControl.isNeedRefreshSelectPassengers = false;
		}
		var deviceHeight = document.body.clientHeight + window.screenTop;
		var headerHeight = 44;
		var footerHeight = 50;
		var topHeight = 120;
		var sliderHeight = deviceHeight - footerHeight - topHeight - headerHeight;
		var size = parseInt(jq("#selectPassengerNavLength").val());
		//fix navList height bug
		jq('#selectPassenger_slider_nav-nav').height(sliderHeight);
		jq('#slider_navList').height(sliderHeight);
		jq("#slider_navList li").height(sliderHeight/size);

		if(mor.ticket.loginUser.id_type != "1" && mor.ticket.loginUser.id_type != "2"){
			for(var i=0;i<passengers.length;i++){
				if(passengers[i].id_type=='1' || passengers[i].id_type == '2'){
					jq("#passengerList li[data-index='"+i+"']").addClass("ui-disabled");
				}
			}	
		}

	}

	jq("#selectPassengerView").live("pagebeforeshow", function() {
		if(mor.ticket.loginUser.isAuthenticated === "Y"){
			selectPsgFn();
		}else{
			if (window.ticketStorage.getItem("autologin") != "true") {
				autologinFailJump()
				} else {
				registerAutoLoginHandler(selectPsgFn, autologinFailJump);
				}

			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});
	
	function registerSelectPassengerBackClickHandler(){
		jq("#selectPassengerBackBtn").live("tap",function(e){
			e.stopImmediatePropagation();
			jq.mobile.changePage(vPathCallBack()+"passengers.html");
			return false;
		});
	}
	
	function registSlider_navClickHandler(){
		jq("#selectPassengerView a[id$='_Anchor']").live("tap",function(){
			var anchor = jq(this).attr("href").charAt(1);
			var curSelector = "#selectPassengerView #slectpassengers_"+anchor;
			var jqNode=jq(curSelector);
			var top=0;
			while(!jqNode.hasClass('iscroll-scroller')){
				top+=jqNode.position().top;
				jqNode=jqNode.offsetParent();
			}
			
			var screenHeight = jq("#selectPassengerView .iscroll-wrapper").height();
			var contentHeight = jq("#selectPassengerView .iscroll-scroller").height();
			var y=(top+screenHeight<=contentHeight)?top:contentHeight-screenHeight;
			jq("#selectPassengerView .iscroll-wrapper").iscrollview('scrollTo',0,-y,0);
		    return false;
		});
	};
	
	function registerPassengerListItemClickHandler(passengers) {
		jq("#passengerList").off().on("tap", "li",function(e) {
			e.stopImmediatePropagation();
			if(jq(this).attr("data-role")!=null&&jq(this).attr("data-role")=='list-divider'){
				jq(this).removeClass("ui-btn-active");
				return false;
			}
			var index = jq(this).attr("data-index");
			var selectedPassenger = passengers[index];
			
			jq.extend( mor.ticket.currentPassenger,	{
				user_name : selectedPassenger.user_name,
				id_type : selectedPassenger.id_type,
				id_no : selectedPassenger.id_no,
				mobile_no : selectedPassenger.mobile_no,
				ticket_type : selectedPassenger.user_type
			});
			
			// fix '点透' 问题
	    	mor.ticket.util.transitionFlag = true;
	    	setTimeout(function(){
	    		if(mor.ticket.util.transitionFlag){
	    			//history.back();
	    			jq.mobile.changePage(vPathCallBack()+"passengers.html");
	    			mor.ticket.util.transitionFlag = false;
	    		}
	    	}, 300);
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

	var passengerListTempate = "{{~it :passenger:index}}"
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
			+ "<div class='ui-block-a'>{{=passenger.user_name}}</div>"
			+ "<div class='ui-block-b'>{{=mor.ticket.util.getPassengerTypeName(passenger.user_type)}}</div>"
			+ "<div class='ui-block-c'>{{=passenger.id_no}}</div>"
			+ "<div class='ui-block-d' style='display:none'>{{=passenger.user_nameSM}}</div>"
			+ "</div></a>"
			+ "</li>"
			+ "{{~}}";
	var generatePassengerList = doT.template(passengerListTempate);

	var slider_navListTempate =
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
	var slider_navList = doT.template(slider_navListTempate);
})();