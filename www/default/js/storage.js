
/* JavaScript content from js/storage.js in folder common */
/**
 * @discription 对 localstorage 对象封装，加入异常捕获与定制处理功能
 * @author      已过
 */
window.ticketStorage = {
		setItem : function(key, value, fn/*异常处理函数*/){
			try{
				localStorage.setItem(key, value);
			}catch(e){
				//console.log(e.message);
				fn && fn(key, value);
			}
		},
		getItem : function(key, fn/*异常处理函数*/){
			try{
				return localStorage.getItem(key);
			}catch(e){
				//console.log(e.message);
				fn && fn(key);
			}
		}
};

