
/* JavaScript content from js/controller/orderManager.js in folder common */
(function(){
	jq.extendModule("mor.ticket.orderManager", {
		orderTicketList:[],
		ticketMode:"",
		orderParameters:{},
		ifConfirmedPassenger:false,
		totalTicketNum:0,//欲购买票数
		preTicketNum:0,//往程票数
		queueMessage:'',//需要排队时，队列信息
		paymentOrder:{
			myTicketList:[]
		},
		clearTicketList :function(){
			if(mor.ticket.viewControl.bookMode != "fc"){
				this.orderTicketList=[];
			}		
		},		
		clearFarelist :function(){
			
			
				/*var passengers = mor.ticket.passengersCache.passengers;
				for(var i =0;i<passengers.length;i++){
					//mor.ticket.passengersCache.passengers[i].checked =0;
					
				}*/
				mor.ticket.passengerList = [];
	
				
		},		
		
		
		
		confirmedTicket:function(){},
		
		addTicket:function(ticket){this.orderTicketList.push(ticket);},
		
		getTicketList:function(){return this.orderTicketList;},
		
		setTotalTicketNum:function(length){			
			if(mor.ticket.viewControl.bookMode === "fc"){
				this.totalTicketNum = this.preTicketNum + length;
			}else {
				this.totalTicketNum = length;
				if(mor.ticket.viewControl.bookMode === "wc"){
					this.preTicketNum = length;
				}
			}
		},
		
		getActualTotalTicketPrice:function(){
			orderTicketList = this.orderTicketList;
			var actualTotalTicketPrice=0;
			for(var i=0; i<orderTicketList.length; i++){
				actualTotalTicketPrice += parseFloat(orderTicketList[i].ticket_price);
			}
			return actualTotalTicketPrice;
		},
		
/*      直接调用confirmPassengerInfo接口，此方法不用了
 * 		getQueueCountMessage:function(parameters){
        	mor.ticket.orderManager.orderParameters=parameters;
			var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "getQueueCountMessage",
				"parameters": [parameters]
			};
        	var invocationData = {
    				adapter: "CARSMobileServiceAdapter",
    				procedure: "getQueueCountMessage"
    			};
			var options = {
					onSuccess: mor.ticket.orderManager.getQueueCountSucceeded,
					onFailure: mor.ticket.orderManager.orderManagerRequestFailure
			};
			
//			WL.Client.invokeProcedure(invocationData, options);
			mor.ticket.util.invokeWLProcedure(parameters, invocationData, options);
//			busy.show();
        },
        
        getQueueCountSucceeded:function(result){
        	if(busy.isVisible()){
        		busy.hide();
        	}
    		var invocationResult = result.invocationResult;
    		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
    			if(invocationResult.queueflag=="1"){
    				WL.SimpleDialog.show(
    	    				"温馨提示", 
    	    				invocationResult.queueMsg, 
    	    				[ {text : '确定', handler: function(){}}]
    	    		);  
    			}else{
    				WL.SimpleDialog.show(
    	    				"温馨提示", 
    	    				invocationResult.queueMsg, 
    	    				[ {text : '取消', handler: function(){}},
    	    				  {text : '确定', handler: function(){mor.ticket.orderManager.confirmPassengerInfo();}} ]
    	    		);  
    			}
    		}else {
    			mor.ticket.orderManager.refreshCaptcha2();
    			mor.ticket.util.alertMessage(invocationResult.error_msg);
    		}	
        },*/
        
       confirmPassengerInfo:function(parameters) {
    		//确认订单
    		var mode = mor.ticket.viewControl.bookMode;
    		var invocationData = null;
    		
			
			
    		if (mode === "wc"){
    			/*invocationData = {
    					adapter: "CARSMobileServiceAdapter",
    					procedure: "confirmPassengerInfoGo",
    					"parameters": [parameters]
    			};*/
    			invocationData = {
    					adapter: "CARSMobileServiceAdapter",
    					procedure: "confirmPassengerInfoGo"
    			};
    		}else if(mode === "fc"){
				/*invocationData = {
						adapter: "CARSMobileServiceAdapter",
						procedure: "confirmPassengerInfoBack",
						"parameters": [parameters]
				};*/
    			invocationData = {
						adapter: "CARSMobileServiceAdapter",
						procedure: "confirmPassengerInfoBack"
				};
    		} else if(mode === "gc"){
    			/*invocationData = {
    					adapter: "CARSMobileServiceAdapter",
    					procedure: "requestChangeTicket",
    					"parameters": [parameters]
    				};*/
    			invocationData = {
    					adapter: "CARSMobileServiceAdapter",
    					procedure: "requestChangeTicket"
    				};
    		} else {
    			/*invocationData = {
    					adapter: "CARSMobileServiceAdapter",
    					procedure: "confirmPassengerInfoSingle",
    					"parameters": [parameters]
    				};*/
    			invocationData = {
    					adapter: "CARSMobileServiceAdapter",
    					procedure: "confirmPassengerInfoSingle"
    				};
    		}
    		var failureFunction;
			
    		if(mode === "fc"){
    			failureFunction = mor.ticket.orderManager.FCRequestFailureHandler;
    		}else{
			    failureFunction = mor.ticket.orderManager.orderManagerRequestFailure;	
			}
			
    		var options = {
    				onSuccess: mor.ticket.orderManager.requestOrderSucceeded,
    				onFailure: failureFunction
    		};		
			
			
//    		WL.Client.invokeProcedure(invocationData, options);	
    		mor.ticket.util.invokeWLProcedure(parameters, invocationData, options);
    		
//    		busy.show();
    	},
    	
    	//返程情况下调用失败方法重写
    	FCRequestFailureHandler:function(){    		
    		if(busy.isVisible()){
    			busy.hide();
    		}
    		mor.ticket.viewControl.tab1_cur_page=vPathViewCallBack()+"MobileTicket.html";
			WL.SimpleDialog.show(
				"温馨提示", 
				"哎呀，网络好像有问题，请检查网络连接", 
				[ {text : '确定', handler: function(){jq.mobile.changePage("queryOrder.html");}}]
			);	
    	},
    	
    	requestOrderSucceeded:function(result){
    		if(busy.isVisible()){
    			busy.hide();
    		}
    		var invocationResult = result.invocationResult;
    		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
        		var mode = mor.ticket.viewControl.bookMode;
    			if(invocationResult.queueFlag==0){
    			//购票成功，无需排队 
    				mor.ticket.orderManager.clearTicketList();
					mor.ticket.orderManager.clearFarelist();
					
    				var pay_limit_time;//get支付倒计时
    				if(mode === "gc"){
    					for(var i=0; i<invocationResult.orderList.length; i++){	
        					invocationResult.orderList[i].station_train_code=mor.ticket.currentTicket.station_train_code;	        					
        					mor.ticket.orderManager.addTicket(invocationResult.orderList[i]);
        				}
    					pay_limit_time = mor.ticket.orderManager.orderTicketList[0].myTicketList[0].pay_limit_time;
    				}
    				
    				//对于返程订票支付倒计时，需要显示的是orderTicketList中最后的一张票的pay_limit_time
        			else if(mode == "fc"){
        				for(var i=0; i<invocationResult.ticketResult.length; i++){	
            				invocationResult.ticketResult[i].station_train_code=mor.ticket.currentTicket.station_train_code;			
            				mor.ticket.orderManager.addTicket(invocationResult.ticketResult[i]);            				
            			}
        				pay_limit_time = mor.ticket.orderManager.orderTicketList[orderTicketList.length-1].pay_limit_time;
        				
        			}
    				else {
    					for(var i=0; i<invocationResult.ticketResult.length; i++){	
        					invocationResult.ticketResult[i].station_train_code=mor.ticket.currentTicket.station_train_code;
        					mor.ticket.orderManager.addTicket(invocationResult.ticketResult[i]);
        				}
    					//pay_limit_time = mor.ticket.orderManager.orderTicketList[0].myTicketList[0].pay_limit_time;
    					//mod by yiguo
    					pay_limit_time = mor.ticket.orderManager.orderTicketList[0].pay_limit_time;

    				}	    				
    				//显示支付倒计时
        			mor.ticket.paytimer.endTime = mor.ticket.util.setMyDate3(pay_limit_time);
        			
    				var payment=mor.ticket.payment;
    				payment['interfaceName'] = invocationResult.interfaceName;
    				payment['interfaceVersion'] = invocationResult.interfaceVersion;
    				payment['tranData'] = invocationResult.tranData;
    				payment['merSignMsg'] = invocationResult.merSignMsg;
    				payment['appId'] = invocationResult.appId;
    				payment['transType'] = invocationResult.transType;
    				payment['epayurl'] = invocationResult.epayurl;
                	
    				jq.mobile.changePage("orderDetails.html");
    			
    			}else if(invocationResult.queueFlag==1&&invocationResult.queueCode==1){
    				mor.ticket.orderManager.queueMessage = invocationResult.queueMessage;
    	            //排队，获取排队时间
    				mor.ticket.orderManager.getWaitTimeProcedure();
    			}else if(invocationResult.queueFlag==1){
    				//排队失败  
    				mor.ticket.orderManager.refreshCaptcha2();
    				mor.ticket.util.alertMessage(invocationResult.queueMessage);
    			} 
    		} else {
    			//调用失败
    			if(invocationResult.succ_flag=="-1" && mor.ticket.viewControl.bookMode === "fc") {
    				WL.SimpleDialog.show(
    						"温馨提示", 
    						"哎呀，网络好像有问题，请检查网络连接", 
    						[ {text : '确定', handler: function() {jq.mobile.changePage("queryOrder.html");}}]
    					); 				
        		}else{
        			mor.ticket.orderManager.refreshCaptcha2();
        			mor.ticket.util.alertMessage(invocationResult.error_msg);
        		}
    		}
    	
    	},
    	
    	getWaitTimeProcedure:function(){
    		var util = mor.ticket.util;  
    		var viewControl = mor.ticket.viewControl;
    		var mode = viewControl.unFinishedOrderTourFlag;
    		if(!mode){
    			mode = viewControl.bookMode;
    		}
			var parameters = util.prepareRequestCommonParameters({
				tourFlag:mode
			});
			/*var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "getWaitTime",
					"parameters": [parameters]
				};*/
			var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "getWaitTime"
				};
				
				var options = {
						onSuccess: mor.ticket.orderManager.getWaitTimeSucceeded,
						onFailure: mor.ticket.orderManager.orderManagerRequestFailure
				};
				
//				busy.show();
//				WL.Client.invokeProcedure(invocationData, options);
				mor.ticket.viewControl.show_busy = false;
				mor.ticket.util.invokeWLProcedure(parameters, invocationData, options);
    	},
    	
    	getWaitTimeSucceeded:function(result){

    		if(busy.isVisible()){
    			busy.hide();
    		}
			
			//mor.ticket.orderManager.clearFarelist()
    		
			var invocationResult = result.invocationResult;
    		var waitTime=invocationResult.waitTime;
    		var interval=30;
    		if(waitTime>=60){
    			interval=30;
    		}else if(waitTime<60&&waitTime>10){
    			interval=interval/2;
    		}else if(waitTime<10){
    			interval=5;
    		}
    		mor.ticket.poll.timer = jq.timer(
    				function() {
    					mor.ticket.orderManager.pollServerForWaitTime();
    				},	interval*1000, true);
    		
    		var util=mor.ticket.util;
    		var viewControl = mor.ticket.viewControl;
    		var mode = viewControl.unFinishedOrderTourFlag;
    		//alert(1);
    		if(!mode){
    			//alert(2);
    			if(util.isHybrid()){
    				//alert(3);
    			var msg = "";
    			var parameters ={};
    			if(waitTime<=5){
    				msg=mor.ticket.orderManager.queueMessage;
    				parameters={ "message": msg,
    						  "buttonText":"进入未完成订单",
    						  "title":"温馨提示"};
    	          window.plugins.childBrowser.openDialog(parameters); 
    	          mor.ticket.poll.endTime = endTime;
      	    	  mor.ticket.poll.intervalId=setInterval(function(){;}, 1000);     	          
    	          return;
    			}else{
    				msg=mor.ticket.orderManager.queueMessage + "\n最新预估排队等待时间:";
    			}
      			  parameters={ "message": msg,
      						"buttonText":"进入未完成订单",
      						"title":"温馨提示"};
      	          window.plugins.childBrowser.openDialog(parameters); 
      	          var endTime = new Date(Date.now()+waitTime*1000);
      	          mor.ticket.poll.endTime = endTime;
      	          //var endTime=mor.ticket.poll.endTime;
      	    	  //endTime = new Date(Date.now()+waitTime*1000);
      	    	  mor.ticket.poll.intervalId=setInterval(function(){
      	    		  jq('<div>').each(function(){
	      		          var nowTime = new Date();
	      		          //endTime=mor.ticket.poll.endTime;
	      		          var nMS=mor.ticket.poll.endTime.getTime() - nowTime.getTime() ;
	      		          var myD=Math.floor(nMS/(1000 * 60 * 60 * 24));
	      		          var myM=Math.floor(nMS/(1000*60)) % 60;
	      		          var myS=Math.floor(nMS/1000) % 60;
	      		          var str;
	      		          if(myD>= 0){
                            str = myM+"分"+myS+"秒";
	      		  			window.plugins.childBrowser.updateDialog(str);
	      		          }else{
	      		  			str = "";	
	      		  		  }
      	    		  });
      	    	  }, 1000);     	          
	      		}else{
	      			//alert(4);
	      			jq("#counter_line").css('display','block'); 
	      			var endTime=mor.ticket.poll.endTime;
	      			endTime = new Date(Date.now()+waitTime*1000);
	      			mor.ticket.poll.intervalId=setInterval(function(){
	      		    	  jq("#waitTime").each(function(){
	      		          var obj = jq(this);
	      		          var nowTime = new Date();
	      		          var nMS=endTime.getTime() - nowTime.getTime() ;
	      		          var myD=Math.floor(nMS/(1000 * 60 * 60 * 24));
	      		          //var myH=Math.floor(nMS/(1000*60*60)) % 24;
	      		          var myM=Math.floor(nMS/(1000*60)) % 60;
	      		          var myS=Math.floor(nMS/1000) % 60;
	      		         // var myMS=Math.floor(nMS/100) % 10;
	      		          var str;
	      		          if(myD>= 0){
	      		  			str = myM+"分"+myS+"秒";
	      		          }else{
	      		  			str = "";	
	      		  		}
	      		  		obj.html(str);});}, 1000);
	      		}
    		} else{
    			//alert(5);
    			jq(".unfinishedOrderWaitTimeContain").show(); 
      			//var endTime=mor.ticket.poll.endTime;
      			//endTime = new Date(Date.now()+waitTime*1000);
    			var endTime = new Date(Date.now()+waitTime*1000);
     	        mor.ticket.poll.endTime = endTime;
      			mor.ticket.poll.intervalId=setInterval(function(){
      		    	  jq(".unfinishedOrderWaitTime").each(function(){
      		          var obj = jq(this);
      		          var nowTime = new Date();
      		          //var nMS=endTime.getTime() - nowTime.getTime() ;
      		          var nMS=mor.ticket.poll.endTime.getTime() - nowTime.getTime() ;
      		          var myD=Math.floor(nMS/(1000 * 60 * 60 * 24));
      		          var myM=Math.floor(nMS/(1000*60)) % 60;
      		          var myS=Math.floor(nMS/1000) % 60;
      		          var str;
      		          if(myD>= 0){
      		  			str = myM+"分"+myS+"秒";
      		          }else{
      		  			str = "";	
      		  		}
      		  		obj.html(str);});}, 1000);
    		}    	
    	},
    	
    	pollServerForWaitTime: function(){

    		var util = mor.ticket.util;
    		var viewControl = mor.ticket.viewControl;
    		var mode = viewControl.unFinishedOrderTourFlag;
    		if(!mode){
    			mode = viewControl.bookMode;
    		}
    		var parameters = util.prepareRequestCommonParameters({
    			tourFlag:mode
    		});
    		/*var invocationData = {
    				adapter: "CARSMobileServiceAdapter",
    				procedure: "getWaitTime",
    				"parameters": [parameters]
    			};*/
    		
    		var invocationData = {
    				adapter: "CARSMobileServiceAdapter",
    				procedure: "getWaitTime"
    			};
			var options = {
					onSuccess: mor.ticket.orderManager.pollServerSucceeded,
					onFailure: mor.ticket.orderManager.orderManagerRequestFailure
			};
			
			if(mor.ticket.poll.timer.isActive){
//				WL.Client.invokeProcedure(invocationData, options);
//				busy.show();
				mor.ticket.viewControl.show_busy = false;
				mor.ticket.util.invokeWLProcedure(parameters, invocationData, options);
			}
    	
    	},
        
    	pollServerSucceeded :function(result){
    		if(busy.isVisible()){
    			busy.hide();
    		}
    		if(!mor.ticket.poll.timer.isActive){
    			return false;
    		}
    		var invocationResult = result.invocationResult;
    		var viewControl = mor.ticket.viewControl;
    		var mode = viewControl.unFinishedOrderTourFlag;
    		if(invocationResult.succ_flag !=1){			
    			mor.ticket.poll.timer.stop();
                window.plugins.childBrowser.closeDialog();
    			clearInterval(mor.ticket.poll.intervalId);
    			if(!mode){
    				jq("#counter_line").hide();
    				mor.ticket.orderManager.refreshCaptcha2();
        			WL.SimpleDialog.show(
        					"温馨提示", 
        					invocationResult.error_msg, 
        					[ {text : '确定', handler: function(){}}]
        			);
    			}else{
    				mor.ticket.orderManager.unfinishedOrderRequest("");
    			}
    		}else{		
    			if(invocationResult.waitTime==-1){
    				//finally get ticket successfully
    				mor.ticket.poll.timer.stop();

    				//call confirmSingleSuc			
    				var util = mor.ticket.util;
    				var parameters = util.prepareRequestCommonParameters({
    					orderId:invocationResult.orderId
    				});
    				
    				var viewControl = mor.ticket.viewControl;
    	    		var mode = viewControl.unFinishedOrderTourFlag;
    	    		if(!mode){
    	    			mode = viewControl.bookMode;
    	    		}
    				var invocationData;
    				if(mode == "gc"){
    					/*invocationData = {
        						adapter: "CARSMobileServiceAdapter",
        						procedure: "confirmResignSuc",
        						"parameters": [parameters]
        				};*/
    					invocationData = {
        						adapter: "CARSMobileServiceAdapter",
        						procedure: "confirmResignSuc"
        				};
    				}else {
    					/*invocationData = {
        						adapter: "CARSMobileServiceAdapter",
        						procedure: "confirmSingleSuccess",
        						"parameters": [parameters]
        				};*/
    					invocationData = {
        						adapter: "CARSMobileServiceAdapter",
        						procedure: "confirmSingleSuccess"
    					};
    				}				
    					
					var options = {
							onSuccess: mor.ticket.orderManager.confirmSingleSucceeded,
							onFailure: mor.ticket.orderManager.orderManagerRequestFailure 
					};
//					busy.show();
//					WL.Client.invokeProcedure(invocationData, options);
					mor.ticket.viewControl.show_busy = false;
					mor.ticket.util.invokeWLProcedure(parameters, invocationData, options);
    	
    			}else if(invocationResult.waitTime>0){
    				//得到新的等待时间
    				//alert("wait time: "+invocationResult.waitTime);
    				//set new interval and update endtime
    				//endTime = new Date(Date.now()+waitTime*1000);
    				var waitTime=invocationResult.waitTime;
    				var interval=30;
    				if(waitTime>=60){
    					interval=30;
    				}else if(waitTime<60&&waitTime>10){
    					interval=interval/2;
    				}else if(waitTime<10){
    					interval=5;
    				}
    				mor.ticket.poll.timer.set({time:interval*1000});
    				mor.ticket.poll.endTime = new Date(Date.now()+waitTime*1000);
    			}
    		}
    	
    	},
    	
    	confirmSingleSucceeded :function(result){
    		if(busy.isVisible()){
    			busy.hide();
    		}
    		var intervalId = mor.ticket.poll.intervalId;
    		if(intervalId != ""){
    			mor.ticket.poll.timer.stop();
    			clearInterval(intervalId);
    		}
    		var viewControl = mor.ticket.viewControl;
    		var mode = viewControl.unFinishedOrderTourFlag;
    		if(!mode){    			
    			window.plugins.childBrowser.closeDialog();
    		}	
    		var invocationResult = result.invocationResult;		
    		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
    			if(!mode){
    				mode = viewControl.bookMode;

    				mor.ticket.orderManager.clearTicketList();
					mor.ticket.orderManager.clearFarelist();

        			var pay_limit_time;//显示支付倒计时		
        			if(mode == "gc"){
        				for(var i=0; i<invocationResult.orderList.length; i++){	
            				invocationResult.orderList[i].station_train_code=mor.ticket.currentTicket.station_train_code;			
            				mor.ticket.orderManager.addTicket(invocationResult.orderList[i]);            				
            			}
        				pay_limit_time = mor.ticket.orderManager.orderTicketList[0].myTicketList[0].pay_limit_time;
        			}
        			
        			//对于返程订票支付倒计时，需要显示的是orderTicketList中最后的一张票的pay_limit_time
        			else if(mode == "fc"){
        				for(var i=0; i<invocationResult.ticketResult.length; i++){	
            				invocationResult.ticketResult[i].station_train_code=mor.ticket.currentTicket.station_train_code;			
            				mor.ticket.orderManager.addTicket(invocationResult.ticketResult[i]);            				
            			}
        				pay_limit_time = mor.ticket.orderManager.orderTicketList[orderTicketList.length-1].pay_limit_time;
        				
        			}else {
        				for(var i=0; i<invocationResult.ticketResult.length; i++){	
            				invocationResult.ticketResult[i].station_train_code=mor.ticket.currentTicket.station_train_code;			
            				mor.ticket.orderManager.addTicket(invocationResult.ticketResult[i]);            				
            			}
        				pay_limit_time = mor.ticket.orderManager.orderTicketList[0].pay_limit_time;
        			}
        			mor.ticket.paytimer.endTime = mor.ticket.util.setMyDate3(pay_limit_time);
        			
        			var payment=mor.ticket.payment;
        			payment['interfaceName'] = invocationResult.interfaceName;
        			payment['interfaceVersion'] = invocationResult.interfaceVersion;
        			payment['tranData'] = invocationResult.tranData;
        			payment['merSignMsg'] = invocationResult.merSignMsg;
        			payment['appId'] = invocationResult.appId;
        			payment['transType'] = invocationResult.transType;
        			payment['epayurl'] = invocationResult.epayurl;
        			mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
        			
        			jq("#counter_line").hide(); 
        			jq.mobile.changePage("orderDetails.html");
    			}else{
    				mor.ticket.orderManager.unfinishedOrderRequest(invocationResult.ticketResult[0].sequence_no);			    				    				
    			}   			
    		}
    	},
    	
    	unfinishedOrderRequest:function(sequence_no){
    		var util = mor.ticket.util;
			var commonParameters = {	
				'sequence_no':sequence_no,
				'query_class': '1'
			};
			
			/*var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "queryOrder",
					parameters: [commonParameters]
			};*/
			var invocationData = {
					adapter: "CARSMobileServiceAdapter",
					procedure: "queryOrder"
			};
			
			var options =  {
					onSuccess: mor.ticket.orderManager.unfinishedOrderRequestSucceeded,
					onFailure: util.creatCommonRequestFailureHandler()
			};
//			WL.Client.invokeProcedure(invocationData, options);
//			busy.show(); 
			mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
    	},
    	
    	unfinishedOrderRequestSucceeded:function(result){
    		if(busy.isVisible()){
    			busy.hide();
    		}
    		var invocationResult = result.invocationResult;	
    		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
    			if(invocationResult.orderList.length && invocationResult.orderList[0].order_date != undefined){
    				//init queryOrder queryOrderList
    				mor.ticket.queryOrder.queryOrderList=[];
    				mor.ticket.queryOrder.originPaidQrderList=[];
    				mor.ticket.queryOrder.setUnfinishedOrderList(invocationResult.orderList);
        			mor.ticket.queryOrder.hasChangeTicket();
    				mor.ticket.queryOrder.updateCurrentQueryOrder(invocationResult.orderList[0]);				
        			mor.ticket.viewControl.unFinishedOrderTourFlag = "";
    				jq.mobile.changePage("orderList.html",{reloadPage:true,transition:"none"});
    			}else {//从后台等不到相关数据时，处理。 
    				//mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
    				//jq.mobile.changePage("queryOrder.html");
    				mor.ticket.util.alertMessage("未查询到您的订单处理状态！"); 
    				jq.mobile.changePage("queryOrder.html");
    				setTimeout(function(){
    					jq("#refreshUnfinishedorder").trigger("tap");
    				},1000);
    			}
    		}else {
    			mor.ticket.util.alertMessage(invocationResult.error_msg);
    		}
    	},
    	
    	orderManagerRequestFailure:function(){
    		mor.ticket.orderManager.refreshCaptcha2();
//			mor.ticket.util.creatCommonRequestFailureHandler();			
    		if(busy.isVisible()){
				busy.hide();
			}
			if(result && result.status==200){
				if(result.invocationResult.responseID=="26"){//调用otsmobile失败
					WL.SimpleDialog.show(
							"温馨提示", 
							"系统忙,请稍后再试。", 
							[ {text : '确定', handler: function() {}}]
						);
			    }else{
			    	 WL.SimpleDialog.show(
								"温馨提示", 
								"哎呀，网络好像有问题，请检查网络连接！", 
								[ {text : '确定', handler: function() {}}]
							);   	
			    }
			}
			else{
				WL.Device.getNetworkInfo(function (networkInfo) {
					if(networkInfo.isNetworkConnected=="false"){
						WL.SimpleDialog.show(
							"温馨提示", 
							"哎呀，您的网络有问题，请检查网络连接。", 
							[ {text : '确定', handler: function() {}}]
						);	
					}else{
						WL.SimpleDialog.show(
							"温馨提示", 
							"哎呀，您的网络好像有问题，请检查网络连接。", 
							[ {text : '确定', handler: function() {}}]
						);	
					}
				});	
			}
		},
    	
    	refreshCaptcha:function (){
            if(jq('#captchaLoading').length>0){
                return false;
            }
    		jq("#captchaInput").val("");
            jq("#captchaInput").after('<span id="captchaLoading">加载中</span>');
            jq("#captchaImg").hide();
    		/*var invocationData = {
    				adapter: "CARSMobileServiceAdapter",
    				procedure: "postPassCode",
    				parameters: [mor.ticket.util.prepareRequestCommonParameters()]	
    		};*/
    		var invocationData = {
    				adapter: "CARSMobileServiceAdapter",
    				procedure: "postPassCode"
    		};
    		var options = {
    				onSuccess: mor.ticket.orderManager.requestCaptchaSucceeded,
    				onFailure: mor.ticket.util.creatCommonRequestFailureHandler()
    		};
//    		busy.show();
//    		WL.Client.invokeProcedure(invocationData, options);
    		mor.ticket.util.invokeWLProcedure(null, invocationData, options);
    		return false;
    	},
    	
    	requestCaptchaSucceeded: function(result){
    		if(busy.isVisible()){
    			busy.hide();
    		}
            jq("#captchaLoading").remove();
    		var invocationResult = result.invocationResult;
    		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
    			jq("#captchaImg").attr("src", "data:image/gif;base64," + invocationResult.passcode);
    			mor.ticket.history.checkCodeImg="data:image/gif;base64," + invocationResult.passcode;
                jq("#captchaImg").show();
    		} else {
    			mor.ticket.util.alertMessage(invocationResult.error_msg);
    		}
    	},
    	
    	refreshCaptcha2:function (){
            if(jq('#captchaLoading').length>0){
                return false;
            }
    		jq("#captchaInput").val("");
            jq("#captchaInput").after('<span id="captchaLoading">加载中</span>');
            jq("#captchaImg").hide();
    		/*var invocationData = {
    				adapter: "CARSMobileServiceAdapter",
    				procedure: "postPassCode",
    				parameters: [mor.ticket.util.prepareRequestCommonParameters()]	
    		};*/
    		var invocationData = {
    				adapter: "CARSMobileServiceAdapter",
    				procedure: "postPassCode"
    		};
    		var options = {
    				onSuccess: mor.ticket.orderManager.requestCaptchaSucceeded2,
    				onFailure: mor.ticket.util.creatCommonRequestFailureHandler()
    		};
    		mor.ticket.viewControl.show_busy = false;
    		mor.ticket.util.invokeWLProcedure(null, invocationData, options);
//    		WL.Client.invokeProcedure(invocationData, options);
    		return false;
    	},
    	
    	requestCaptchaSucceeded2: function(result){
    		if(busy.isVisible()){
    			busy.hide();
    		}
            jq("#captchaLoading").remove();
    		var invocationResult = result.invocationResult;
    		if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
				//alert(invocationResult.passcode);
    			jq("#captchaImg").attr("src", "data:image/gif;base64," + invocationResult.passcode);
                mor.ticket.history.checkCodeImg="data:image/gif;base64," + invocationResult.passcode;
                jq("#captchaImg").show();
    		} else {
    			mor.ticket.util.alertMessage(invocationResult.error_msg);
    		}
    	},
    	
    	getRoundTripOrderList:function(){
    		var currentOrderList = [];
    		var preOrderList = [];
    		var orderTicketList = this.orderTicketList;
    		for(var i=0; i<orderTicketList.length; i++){
    			if(!orderTicketList[i].isRoundTripFlag){
    				currentOrderList.push(orderTicketList[i]);
    			} else {
    				preOrderList.push(orderTicketList[i]);
    			}
    		}
    		return {current:currentOrderList,
    			pre : preOrderList};
    	},
    	
    	preTicketDetail:{},
    	setPreTicketDetail:function(ticketDetail){ 
    		for(o in ticketDetail){
    			this.preTicketDetail[o] = ticketDetail[o];
    		}
    	},
		getPreTicketDetail:function(){ return this.preTicketDetail;},
		//确认往返程 往程票务信息:添加购票人电话 以及标记往程票flag
		confirmRoundTripTicketList:function(passengerList){
			var orderTicketList = this.orderTicketList;
			for(var i=0; i < orderTicketList.length; i++){
				for(var j=0; j<passengerList.length; j++){
					if(orderTicketList[i].passenger_id_no == passengerList[j].id_no){
						orderTicketList[i].mobile_no = passengerList[j].mobile_no;
					}
				}
				orderTicketList[i].isRoundTripFlag = true;
			}
		},
		
		//赋值默认的座位类型
		getConfirmOrderListPassengers:function(passengerList, seat_type){
			var orderTicketList = this.orderTicketList;
			for(var i=0; i<orderTicketList.length; i++){
				var passenger = {};
				passenger.id_no = orderTicketList[i].passenger_id_no;
				passenger.id_type = orderTicketList[i].passenger_id_type_code;
				passenger.mobile_no = orderTicketList[i].mobile_no;
				passenger.user_name = orderTicketList[i].passenger_name;
				passenger.ticket_type = orderTicketList[i].ticket_type_code;
				passenger.seat_type = seat_type;
				passengerList.push(passenger);				
			}
		},
		
		//根据 orderTicketList 获取返程联系人列表。 因为在选择联系人那里重新对联系人的字段进行了修改，
		// 所以这里也只能进行修改.
		getFcPassengers: function(){
			var passengerList = [];
			jq.each(this.orderTicketList, function(i, orderTicket) {
				var passenger = {};
				passenger.id_no = orderTicket.passenger_id_no;
				passenger.id_type = orderTicket.passenger_id_type_code;
				passenger.mobile_no = orderTicket.mobile_no;
				passenger.user_name = orderTicket.passenger_name;
				passenger.ticket_type = orderTicket.ticket_type_code;
				passenger.seat_type = orderTicket.seat_type;
				passengerList.push(passenger);
			});
			return passengerList;
		}
	});
})();