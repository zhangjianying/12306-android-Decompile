
/* JavaScript content from js/controller/selectCountry.js in folder common */
(function() {
	var prevPage; 
	
	
	

	
	jq("#selectCountryView").live(
			"pagebeforeshow",
			function(e, data) {
				var countryOptions = mor.ticket.cache.country;
				jq("#countryList").html(generateCountryOption(countryOptions))
						.listview("refresh");		
				prevPage = data.prevPage.attr("id"); 
				registerStationListItemClickHandler();




				jq("#selectCountryBackBtn").live("tap",function(){
					jq.mobile.changePage("modifyPassenger.html");
					return false;
				});
				
			});

	var countryOptionsTemplate = "{{ for(var i=0;i<it.length;i++) { }}"
			+ "<li id='{{=it[i].id}}' data-filtertext='{{=it[i].pinyin}}'><a>{{=it[i].value}}</a></li>"
			+ "{{ } }}";
	var generateCountryOption = doT.template(countryOptionsTemplate);

	function registerStationListItemClickHandler() {
		jq("#countryList").off().on("tap", "li", function(e,data) {
													
			//e.stopImmediatePropagation();
			/**
			 * workaround country list auto tap.   
			 * tapLocation is defined in modifyPassenger.js
			 */			
			if(tapLocation && tapLocation.clientX && tapLocation.clientY){
				if((e.clientX===tapLocation.clientX) && (e.clientY===tapLocation.clientY)){
//					WL.Logger.debug('WARNING: found the same tap event, ignore this event');
					return false;
				}
			}
			
			
			var util = mor.ticket.util;
			jq(this).addClass("ui-btn-active").siblings().removeClass(
					"ui-btn-active");
			var country_code = jq(this).attr("id");
//			if(prevPage=== "registView_details"){
			var skipPage="modifyPassenger.html";
			switch (prevPage){
				case "registView":
					jq("#regist_country").val(util.getCountryByCode(country_code));
					jq("#regist_country_code").val(country_code);
					skipPage="regist.html";
					break;
				case "modifyuserInfoView":
					jq("#modify_country").val(util.getCountryByCode(country_code));
					jq("#modify_country_code").val(country_code);
					skipPage="modifyUserInfo.html";
					break;
				case "modifyPassengerView":
					jq("#modify_PassengerCountry").val(util.getCountryByCode(country_code));
					jq("#modify_PassengerCountry_code").val(country_code);
					break;
			}
			jq.mobile.changePage(skipPage);
			
			
			
			
			/*
			alert(history.back());
			*/
			

			return false;
		});
	}

})();