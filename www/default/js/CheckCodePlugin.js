
/* JavaScript content from js/CheckCodePlugin.js in folder common */
var CheckCodePlugin = {
	getCheckCode: function(success, fail, timestamp){
		if (mor.ticket.util.isDebug) {
			/*var common = mor.ticket.common;
			var user = mor.ticket.loginUser;
			var serverTime=new Date();
			serverTime.setTime(Date.now()-common["baseDTO.time_offset"]);
			var time_str= serverTime.format("yyyyMMddhhmmss");
			var check_code=hex_md5('123456'+time_str+common['baseDTO.device_no']);*/
			
			var common = mor.ticket.common;
			var check_code=hex_md5('123456'+timestamp);
			
			success(check_code);
			return;
		}
		else {
//			WL.Logger.debug("*****************************CALL CheckCodePlugin"+timestamp);
			return cordova.exec(success, fail, "CheckCodePlugin", "getcheckcode", [timestamp]);
		}
	}
};

window.CheckCodePlugin = CheckCodePlugin;