
/* JavaScript content from js/controller/about.js in folder common */
(function(){
	jq("#aboutView").live("pageinit", function(){
		jq("#aboutBackBtn").off().bind("tap",function(){
			jq.mobile.changePage("moreOption.html");
			return false;
		});
	});
	
	jq("#aboutView").live("pagebeforeshow", function(){
		mor.ticket.viewControl.tab4_cur_page="about.html";		
	});
	
})();