
/* JavaScript content from js/controller/my12306.js in folder common */
(function() {
	jq("#my12306View").live("pageinit", function() {
		
		//init country id value
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
		
		jq("#favoriteContacts").bind("tap", function() {
			jq.mobile.changePage("passengerList.html");
			return false;
		});

		jq("#userInformation").bind("tap", function() {
			jq.mobile.changePage("userInfo.html");
			return false;
		});

		jq("#changPasswd").bind("tap", function() {
			jq.mobile.changePage("changePwd.html");
			return false;
		});
	});
	
	jq("#my12306View").live("pagebeforeshow", function() {
		mor.ticket.viewControl.tab3_cur_page="";
		var user = mor.ticket.loginUser;
		if (user.isAuthenticated === "Y") {
//			WL.Logger.debug("my12306View show");
		} else {
			var latestTime = window.ticketStorage.getItem("pwdTime");
			
			// mod by yiguo
			if (window.ticketStorage
					.getItem("autologin") == "true" && (!latestTime || (Date.now() - latestTime < 7*24*3600*1000))) {
				registerAutoLoginHandler(function() {
				}, autologinFailJump);
			}else{
				autologinFailJump();
			}

			//jq.mobile.changePage(vPathCallBack()+"loginTicket.html");
		}
	});

})();