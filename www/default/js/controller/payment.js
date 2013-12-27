
/* JavaScript content from js/controller/payment.js in folder common */
(function(){
	jq.extendModule("mor.ticket.payment", {

		onCounterDialogOk:function(){
			var intervalId = mor.ticket.poll.intervalId;
			if(intervalId != ""){
				mor.ticket.poll.timer.stop();
				clearInterval(mor.ticket.poll.intervalId);
			}
			window.plugins.childBrowser.closeDialog();
			//如果当前的mode为gc那么在改签提交订单后更改mode为dc
			if(mor.ticket.viewControl.bookMode == "gc"){
				mor.ticket.viewControl.bookMode = "dc";
			}
			
			mor.ticket.viewControl.tab1_cur_page = vPathViewCallBack()+"MobileTicket.html";
			//mod by yiguo
			//jq.mobile.changePage("queryOrder.html");
//			busy.show();
			
			window.pullOrderData(function(result){
				if(busy.isVisible()){
					busy.hide();
				}
				
				//mod by yiguo
				
				//TODO get current order index & jump to orderlist.html
				 var invocationResult = result.invocationResult;
			        if (mor.ticket.util.invocationIsSuccessful(invocationResult)) {
			            var queryOrder = mor.ticket.queryOrder;
			            //init queryOrder queryOrderList
			            queryOrder.queryOrderList = [];
			            queryOrder.originPaidQrderList = [];
			            
			            
			            queryOrder.setUnfinishedOrderList(invocationResult.orderList);
			            //displayNoFinishedOrders();
			           
			            if(invocationResult.orderList && invocationResult.orderList.length){
			            	var noFinishedOrderList = queryOrder.queryOrderList;


				            if (noFinishedOrderList && noFinishedOrderList[0]) {

				                queryOrder.hasChangeTicket();

				            }

				            
				            	
				            	// init queryOrder content
				            	queryOrder.currentQueryOrder = {};
				            	queryOrder.currentUnfinishOrderIndex = noFinishedOrderList.length -1;
				            	mor.ticket.queryOrder.setCurrentUnfinishedOrders(queryOrder.currentUnfinishOrderIndex);
				            	
				            	
				            	jq.mobile.changePage("orderList.html",{reloadPage:true,transition:"none"});
				            

			            }else{
							jq.mobile.changePage("queryOrder.html");
			            }
			            			            
			            
			            //jq.mobile.changePage("orderList.html",{reloadPage:true});
			        } else {
			            mor.ticket.util.alertMessage(invocationResult.error_msg);
			        }
				 				

				//jq.mobile.changePage(vPathCallBack() + "orderList.html");
   
			},
			function(){
				if(busy.isVisible()){
					busy.hide();
				}
				util.alertMessage("订单状态获取失败，暂时无法转入订单查询页面！");
			});
		},
		
		orderList:function(){
			mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
			jq.mobile.changePage("queryOrder.html");
			setTimeout(function(){
				jq("#refreshUnfinishedorder").trigger("tap");
			},1001);
			setTimeout(function(){
			window.plugins.childBrowser.close();
			},1000);
		},
		
		orderComplete:function(){
//			WL.Logger.debug("#######################orderComplete#######################");
			jq.mobile.loadPage("payFinish.html");
			var ticketList= mor.ticket.orderManager.paymentOrder.myTicketList;			
			var sequence_no=ticketList[0].sequence_no;	
//			WL.Logger.debug("#######################支付完成查询："+sequence_no+"#######################");
			var commonParameters = {
				sequence_no: sequence_no,
				query_class: '5'//query all orders
			};
		    var invocationData = {
				adapter: "CARSMobileServiceAdapter",
				procedure: "queryOrder"
			};
			
			var options = {
					onSuccess: mor.ticket.payment.judgetOrderIsSuccess,
					onFailure: mor.ticket.util.creatCommonRequestFailureHandler()
			};
			mor.ticket.util.invokeWLProcedure(commonParameters, invocationData, options);
		},
		
		judgetOrderIsSuccess:function(result){
//			WL.Logger.debug("#######################judgetOrderIsSuccess");
			var invocationResult = result.invocationResult;
			if(mor.ticket.util.invocationIsSuccessful(invocationResult)){
//				WL.Logger.debug("#######################orderList[0].myTicketList.length:"+invocationResult.orderList[0].myTicketList.length);

				/*for(var i=0;i<mor.ticket.payment.batch_nos.length;i++){
//					WL.Logger.debug("#######################mor.ticket.payment.batch_nos:"+mor.ticket.payment.batch_nos[i]);
				}*/
				var ticketList = [];
				for(var i=0;i<invocationResult.orderList[0].myTicketList.length;i++){
					var batch_no = invocationResult.orderList[0].myTicketList[i].batch_no;
//					WL.Logger.debug("#######################batch_no:"+batch_no);
					if(jq.inArray(batch_no,mor.ticket.payment.batch_nos)>=0){
						var ticket = invocationResult.orderList[0].myTicketList[i];
//						WL.Logger.debug("#######################going to push orderList:"+ticket.batch_no);
						ticketList.push(ticket);
					}
				}
				/*WL.Logger.debug("#######################ticketList.length:"+ticketList.length);
				for(var i=0;i<ticketList.length;i++){
					WL.Logger.debug("#######################ticketList.batch_nos:"+ticketList[i].batch_no);
					WL.Logger.debug("#######################ticketList.pay_mode_code" + ticketList[i].pay_mode_code);
				}*/
				
//				WL.Logger.debug("#######################判断支付状态：" + ticketList[0].pay_mode_code);
				if((ticketList[0].pay_mode_code == 'Z')||(ticketList[0].pay_mode_code=='N')){//改签后支付状态为‘Z’，其他情况下支付后状态为‘N’
					mor.ticket.orderManager.paymentOrder = invocationResult.orderList[0];
					mor.ticket.orderManager.paymentOrder.myTicketList = ticketList;
					mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
					jq.mobile.changePage("payFinish.html");

				}else{
					mor.ticket.viewControl.isNeedRefreshUnfinishedOrder = true;
					mor.ticket.util.alertMessage("支付失败。请检查订单状态后尝试重新支付。");
					jq.mobile.changePage("queryOrder.html");
					setTimeout(function(){
						jq("#refreshUnfinishedorder").trigger("tap");
					},1000);
				}
			}
			setTimeout(function(){
				if(busy.isVisible()){
					busy.hide();
				}
				window.plugins.childBrowser.close();			
			},1000);
		}
		
	});
})();