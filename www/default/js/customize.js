
/* JavaScript content from js/customize.js in folder common */
window.jq=jQuery.noConflict();




jq(document).bind("mobileinit", function() {
	jq.extend(jq, {
		namespace: function(namespace){
			var names = namespace.split(".");
			var parent = window;
			for (var i = 0; i < names.length; ++i){
				var name = names[i];
				if (typeof parent[name] === "undefined") {
					parent[name] = {};
				}
				parent = parent[name];
			}
			return parent;
		},
		extendModule: function(name, publicInterface){
			var module = this.namespace(name);
			this.extend(module, publicInterface);
			return module;
		}
	});
	
	var str = 'none';
	if(WL.Client.getEnvironment() == "iphone" || WL.Client.getEnvironment() == "ipad"){
		str = 'slide';
	}
	jq.extend(jq.mobile, {
		// forbid page transition animation
		defaultPageTransition : str,
		minScrollBack:'infinity',
		pushStateEnabled: false, //android and ios bug on HTML5 pushState, see: https://github.com/jquery/jquery-mobile/issues/3939
		pageLoadErrorMessage:'',
		loadingMessage:false//cancel jquery default loading message
	});
});

Date.prototype.format = function(format){
	 var o = {
		  "M+" :  this.getMonth()+1,  //month
		  "d+" :  this.getDate(),     //day	
		  "h+" :  this.getHours(),    //hour
	      "m+" :  this.getMinutes(),  //minute
	      "s+" :  this.getSeconds(), //second
	      "q+" :  Math.floor((this.getMonth()+3)/3),  //quarter
	      "S"  :  this.getMilliseconds() //millisecond
	   };

	   if(/(y+)/.test(format)) {
	    format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	   }

	   for(var k in o) {
	    if(new RegExp("("+ k +")").test(format)) {
	      format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
	    }
	   }
	 return format;
	};

Date.prototype.formatWithTimezone = function(format, timezone){
	var localTimezone = -this.getTimezoneOffset()/60;
	var copy = new Date(this.getTime()+(timezone-localTimezone)*60*60*1000);
	return copy.format(format);
};