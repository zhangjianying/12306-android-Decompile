
/* JavaScript content from js/checkForm.js in folder common */
(function(){
	jq.extendModule("mor.ticket.checkForm", {
		//只能是中英文字母
		checkChar:function(str){
			var s=this.trim(str);
			return   /^[a-zA-Z\s]+$/.test(s)||/^[\u4E00-\u9FA5]+$/.test(s);
		},
		
		checkPersonName : function(str){
			var s=this.trim(str);
			return /^[a-zA-Z\u4E00-\u9FA5]+$/.test(s);
		},
		//身份证字符验证
		checkIdValidStr:function(str){
			var tel =  /^[a-zA-Z0-9\_\-\(\)]+$/;
			return tel.test(this.trim(str));
		},
		
		validateSecIdCard:function(value){
			var iSum = 0;
			var sId= value;
			var aCity = mor.cache.provinceMap;
			if (!/^\d{17}(\d|x)$/i.test(sId)) {
				return false;
			}
			sId = sId.replace(/x$/i, "a");
			//非法地区
			if (aCity[parseInt(sId.substr(0, 2))] == null) {
				return false;
			}
			var sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2))
					+ "-" + Number(sId.substr(12, 2));
			var d = new Date(sBirthday.replace(/-/g, "/"));
			//非法生日
			if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d
					.getDate())) {
				return false;
			}
			for ( var i = 17; i >= 0; i--) {
				iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
			}
			if (iSum % 11 != 1) {
				return false;
			}
			return true;
		},
		
		validateFirIdCard:function(value){
			var iSum = 0;
			var sId;
			var aCity = mor.cache.provinceMap;
			//如果输入的为15位数字,则先转换为18位身份证号
			if (value.length == 15)
				sId = this.idCardUpdate(value);
			else
				sId = value;
			if (!/^\d{17}(\d|x)$/i.test(sId)) {
				return false;
			}
			sId = sId.replace(/x$/i, "a");
			//非法地区
			if (aCity[parseInt(sId.substr(0, 2))] == null) {
				return false;
			}
			var sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2))
					+ "-" + Number(sId.substr(12, 2));
			var d = new Date(sBirthday.replace(/-/g, "/"));
			//非法生日
			if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d
					.getDate())) {
				return false;
			}
			for ( var i = 17; i >= 0; i--) {
				iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
			}
			if (iSum % 11 != 1) {
				return false;
			}
			return true;
		},
		
		checkPassport:function(value){
			var tel2= /^[a-zA-Z]*$/;
			if(tel2.test(value)){
				return false;
			}
			var tel =  /^[a-zA-Z0-9]{5,17}$/;
			return tel.test(value);
		},
		
		checkHkongMacao:function(value){
			var tel1 =  /^[HMhm]{1}[0-9]{10}$/;
			var tel2 =  /^[HMhm]{1}[0-9]{8}$/;
			return tel1.test(value) || tel2.test(value);
		},
		
		checkTaiw:function(value){
			var tel1 =  /^[0-9]{8}$/;
			var tel2 =  /^[0-9]{10}$/;
			return tel1.test(value) || tel2.test(value);
		},
		//手机号码验证
		 isMobile:function(value) {
			var length = value.length;
			return length == 11&&/^[0-9]+$/.test(value);
		},
		
		//邮箱验证
		isEmail : function(value){
			var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			return reg.test(this.trim(value));
		},
		
		//邮政编码验证
		isZipCode : function(str){
			var tel = /^[0-9]{6}$/;
			return tel.test(this.trim(str));
		},
		
		//验证只能包含中文、英文、数字
		checkNameChar:function (str) {
			return /^[a-zA-Z0-9\u4E00-\u9FA5]+$/.test(str);
		},
		
		idCardUpdate:function(_str) {
			var idCard18;
			var regIDCard15 = /^(\d){15}$/;
			if (regIDCard15.test(_str)) {
				var nTemp = 0;
				var ArrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
						4, 2);
				var ArrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3',
						'2');
				_str = _str.substr(0, 6) + '1' + '9' + _str.substr(6, _str.length - 6);
				for ( var i = 0; i < _str.length; i++) {
					nTemp += parseInt(_str.substr(i, 1)) * ArrInt[i];
				}
				_str += ArrCh[nTemp % 11];
				idCard18 = _str;
			} else {
				idCard18 = "#";
			}
			return idCard18;
		},
		
		//只能是数字
		checkNum:function(str){
			return  /^[0-9]+$/.test(this.trim(str));
		},
		checkTrainCode:function(str){
			return /^[A-Za-z0-9]{1}[0-9]{0,4}$/.test(this.trim(str));
		},
		checkSequenceNo:function(str){
			return /^[A-Za-z]{1}[0-9]{9}$/.test(this.trim(str));
		},
		//删除左右两端的空格
		 trim:function(str) {
			return str.replace(/(^\s*)|(\s*$)/g, "");
		},
		//用户名验证只能包含字母数字下划线
		validateUsersName:function(value){
			return /^[A-Za-z]{1}([A-Za-z0-9]|[_]){0,29}$/.test(value);
		}
		
		});
})();