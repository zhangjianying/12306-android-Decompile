
/* JavaScript content from js/util.js in folder common */
(function() {
	jq
			.extendModule(
					"mor.ticket.util",
					{
						
						// check code plugin need set here to false, true for
						// browser debug
						isDebug : false,

						// initial deferred object
						userDef : null,
						syncDef : null,

						// fix '点透' 问题
						transitionFlag : false,
						// Adapter timeout
						adapterTimeOut : 60000, // 60 seconds
						/**
						 * Invoke Worklight Adapter method. This method will
						 * call CheckCodePlugin to generate checkcode before
						 * invoke procedure.
						 * 
						 * @param {Object}
						 *            commonParameters common parameters to
						 *            merge to the default parameters
						 * @param {Object}
						 *            invocationData invocation data for
						 *            worklight adapter
						 * @param {Object}
						 *            options callback options for worklight
						 *            adapter
						 * @param {Boolean}
						 *            isGetMethod use 'get' method to send HTTP
						 *            request
						 */
						// 登陆超时，重新登陆页面
						timeoutLogin : function() {
							mor.ticket.util.alertMessage("登录超时，请重新登录！");
							jq.mobile.changePage(vPathCallBack()
									+ "loginTicket.html");
							if (busy.isVisible()) {
								busy.hide();
							}
						},
						//by yiguo
						jsVersionFormat : function(version){
							try {
								if (version) {
									var _result = version.replace(
											/(.+?)((\d+?):(\d+?))([AP]M)/,
											function(a, b, c, d, e, f) {
												if (f == "AM") {
													return b + d + ":" + e;
												} else {
													return b + (d - 0 + 12)
															+ ":" + e;
												}
											});
									return new Date(_result).getTime();
									
									
								} else {
									return null;
								}
							} catch (e) {
								return null;
							}
							
						},
						invokeWLProcedure : function(commonParameters,
								invocationData, options, isGetMethod) {
							// call CheckCodePlugin to generate checkcode
							var common = mor.ticket.common;
							var user = mor.ticket.loginUser;

							// 自动登录成功后回调函数
							function customLogin() {

								WL.EncryptedCache
										.read(
												"userPW",
												function(value) {
													mor.ticket.loginUser.password = value;

													// AutoSendLoginRequest(mor.ticket.loginUser.username,mor.ticket.loginUser.password);
													var usernameInput = mor.ticket.loginUser.username;
													var passwordInput = mor.ticket.loginUser.password;
													var util = mor.ticket.util;

													var _commonParameters = {
														'baseDTO.user_name' : usernameInput,
														'password' : hex_md5(passwordInput)
													// 'autologinChkbox':
													// jq("#autologinChkbox").val()
													};

													var _invocationData = {
														adapter : "CARSMobileServiceAdapter",
														procedure : "login"
													};

													var _options = {
														onSuccess : function() {

															// TODO
															// 自定义登录成功后的操作，如消失浮层，重新提交
															if (busy
																	.isVisible()) {
																busy.hide();
															}

															WL.Logger
																	.error("重新提交开始");
															// 重新提交上次请求
															mor.ticket.util
																	.invokeWLProcedure(
																			commonParameters,
																			invocationData,
																			options,
																			isGetMethod);
														},
														onFailure : function(
																failResponse) {
															mor.ticket.util
																	.timeoutLogin();
														}
													};

													mor.ticket.util
															.invokeWLProcedure(
																	_commonParameters,
																	_invocationData,
																	_options);

													return false;

												}

										);
							}

							// 自动登录失败后回调函数
							function customFail() {
								//
								mor.ticket.util.loginTimeout = false;
								mor.ticket.util.timeoutLogin();
							}

							// 参数设置
							var commonOptions = {
								onSuccess : function(response) {
									WL.Logger
											.debug('In commonOptions onSuccess');
									var invocationResult = response.invocationResult;

									if(busy.isVisible()){
										busy.hide();
									}
									if (invocationResult.authRequired
											|| invocationResult.succ_flag == "-5") {
										// add by yiguo
										var latestTime = window.ticketStorage.getItem("pwdTime");
										
										// mod by yiguo
										if (window.ticketStorage
												.getItem("autologin") == "true" && (!latestTime || (Date.now() - latestTime  < 7*24*3600*1000))) {

											WL.Logger.error('超时');

											// 判断是否是第二次重新登录
											if (!mor.ticket.util.loginTimeout) {
												if (!busy.isVisible()) {
													busy.show();
												}
												mor.ticket.util.loginTimeout = true;

												// 不影响原registerAutoLoginHandler函数原流程基础上，插入自定义回调，闭包上次提交信息
												registerAutoLoginHandler(
														customLogin, customFail);
											} else {
												// 登陆接口返回超时（服务器bug），放弃重连，跳转到登陆页面
												mor.ticket.util.loginTimeout = false;
												mor.ticket.util.timeoutLogin();
											}
											// } else {
											// // by yiguo
											// WL.Logger.error("当前页面#"
											// + jq.mobile.activePage
											// .attr("data-url"));
											// if (jq.mobile.activePage.attr(
											// "data-url").indexOf(
											// "loginTicket") == -1
											// && jq.mobile.activePage
											// .attr("data-url")
											// .indexOf(
											// "bookTicket") == -1) {
											// //
											// mor.ticket.util.timeoutLogin();
											// jq.mobile
											// .changePage(vPathCallBack()
											// + "loginTicket.html");
											// if (busy.isVisible()) {
											// busy.hide();
											// }
											// }
											// }

											return;
										} else {
											mor.ticket.loginUser["isAuthenticated"] = "N";
										}

										if(busy.isVisible()){
											busy.hide();
										}

									}
									// 重置登陆超时重新登陆状态
									mor.ticket.util.loginTimeout = false;

									if (options.onSuccess) {
										options.onSuccess(response);
									}
								},
								onFailure : function(failResponse) {
									WL.Logger
											.debug('In commonOptions onFailure');

									if(busy.isVisible()){
										busy.hide();
									}
									// 重置登陆超时重新登陆状态
									mor.ticket.util.loginTimeout = false;

									if (options.onFailure) {
										options.onFailure(failResponse);
									}
								},
								timeout : mor.ticket.util.adapterTimeOut
							// Configuring adapter timeout to speed response
							// time under 2G/3G network
							};

							var serverTime = new Date();
							serverTime.setTime(Date.now()
									- common["baseDTO.time_offset"]);
							var time_str = serverTime.formatWithTimezone(
									"yyyyMMddhhmmss", 8);

							window.CheckCodePlugin.getCheckCode(
									onCheckcodeSuccess, onCheckcodeFailure,
									(time_str + common['baseDTO.device_no']));
							// get checkcode success.
							function onCheckcodeSuccess(checkcode) {
								// WL.Logger.debug('get checkcode success.
								// '+checkcode);
								var check_code = checkcode;
								var parameters = {
									"baseDTO.os_type" : common['baseDTO.os_type'],
									"baseDTO.device_no" : common['baseDTO.device_no'],
									"baseDTO.mobile_no" : common['baseDTO.mobile_no'],
									"baseDTO.time_str" : time_str,
									"baseDTO.check_code" : check_code,
									"baseDTO.version_no" : common['baseDTO.version_no'],
									"baseDTO.user_name" : user['username']
								// "password": user.password
								};

								// merge paramters
								if (commonParameters) {
									if (invocationData.procedure == "queryLeftTicket") {
										jq.extend(commonParameters, parameters);
										parameters = commonParameters;
									} else {
										jq.extend(parameters, commonParameters);
									}
								}

								// Use parameters as Adapter's procedure
								// parameters
								if (invocationData) {
									invocationData.parameters = [ parameters ];
									// • Compression response data to speed
									// response time under 2G/3G network
									invocationData.compressResponse = true;
								}

								// keep current page url, in case session out
								/*
								 * if(invocationData.adapter !=
								 * "CacheDataServiceAdapter"){
								 * mor.ticket.util.keepPageURL(); }
								 */

								if (mor.ticket.viewControl.show_busy) {
									if (!busy.isVisible()) {
										busy.show();
									}
								} else {
									mor.ticket.viewControl.show_busy = true;
								}

								if (isGetMethod && mor.ticket.util.isHybrid()) {
									var url = WL.StaticAppProps.WORKLIGHT_BASE_URL
											+ '/invoke';
									invokeProcedureByGet(url, invocationData,
											commonOptions);
								} else if (isGetMethod
										&& !mor.ticket.util.isHybrid()) {
									invokeProcedureByGet(
											'http://localhost:10080/MobileTicket/invoke',
											invocationData, commonOptions);
								} else {
									WL.Client.invokeProcedure(invocationData,
											commonOptions);
								}

								/**
								 * Use HTTP 'get' method to invoke adapter
								 * procedure. This is a workaournd for MOR CDN
								 * to cache the adapter result.
								 * 
								 * @param {Object}
								 *            invocationData
								 * @param {Object}
								 *            options
								 */
								function invokeProcedureByGet(url,
										invocationData, options) {
									WL.Validators.validateOptions({
										adapter : 'string',
										procedure : 'string',
										parameters : 'object',
										compressResponse : 'boolean'
									}, invocationData, 'invokeProcedureByGet');

									WL.Validators.validateOptions({
										onSuccess : 'function',
										onFailure : 'function',
										invocationContext : function() {
										},
										onConnectionFailure : 'function',
										timeout : 'number',
										fromChallengeRequest : 'boolean'
									}, options, 'invokeProcedureByGet');

									options = WL.Utils.extend(
													options || {},
													{
														onSuccess : function(
																response) {
															WL.Logger
																	.debug("defaultOptions:onSuccess");
														},
														onFailure : function(
																response) {
															onDefaultInitFailure(response);
														},
														invocationContext : null
													});

									function onDefaultInitFailure(response) {
										if (response.errorCode == WL.ErrorCode.CONNECTION_IN_PROGRESS) {
											return;
										}
										WL.Logger.error("Client init failed. "
												+ response.errorMsg);
										var errMsg = (response.errorMsg == WL.ClientMessages.authFailure ? response.errorMsg
												: WL.ClientMessages.unexpectedError);
										showWidgetContent();
										var callbackName = errorCodeCallbacks[response.errorCode];
										if (callbackName
												&& initOptions[callbackName]) {
											initOptions[callbackName](response);
										} else {
											showDialog(
													WL.ClientMessages.wlclientInitFailure,
													response.userMsg ? response.userMsg
															: errMsg,
													response.recoverable, true,
													response);
										}
									}
									;

									var blocked = false;

									function onInvokeProcedureSuccess(transport) {
										if (!blocked) {
											blocked = true;
											if (!transport.responseJSON.isSuccessful) {
												var failResponse = new WL.Response(
														transport,
														options.invocationContext);
												failResponse.errorCode = WL.ErrorCode.PROCEDURE_ERROR;
												failResponse.errorMsg = WL.ClientMessages.serverError;
												failResponse.invocationResult = transport.responseJSON;
												if (failResponse.invocationResult.errors) {
													failResponse.errorMsg += " "
															+ failResponse.invocationResult.errors;
													WL.Logger
															.error(failResponse.errorMsg);
												}
												options.onFailure(failResponse);
											} else {
												var response = new WL.Response(
														transport,
														options.invocationContext);
												response.invocationResult = transport.responseJSON;
												options.onSuccess(response);
											}
										}
									}

									function onInvokeProcedureFailure(transport) {
										if (!blocked) {
											blocked = true;
											WLJSX.Ajax.WLRequest.setConnected(false);
											var errorCode = transport.responseJSON.errorCode;
											if (options.onConnectionFailure
													&& (errorCode == WL.ErrorCode.UNRESPONSIVE_HOST || errorCode == WL.ErrorCode.REQUEST_TIMEOUT)) {
												options
														.onConnectionFailure(new WL.FailResponse(
																transport,
																options.invocationContext));
											} else {
												options
														.onFailure(new WL.FailResponse(
																transport,
																options.invocationContext));
											}
										}
									}

									// Build request options from invocationData
									var requestOptions = {
										onSuccess : onInvokeProcedureSuccess,
										onFailure : onInvokeProcedureFailure
									};

									if (!WLJSX.Object
											.isUndefined(options.timeout)) {
										requestOptions.timeout = options.timeout;
									}

									if (!WLJSX.Object
											.isUndefined(options.fromChallengeRequest)) {
										requestOptions.fromChallengeRequest = options.fromChallengeRequest;
									}

									requestOptions.parameters = {};
									requestOptions.parameters.adapter = invocationData.adapter;
									requestOptions.parameters.procedure = invocationData.procedure;

									var environment = WL.Client
											.getEnvironment();

									switch (environment) {
									case WL.Env.ANDROID:
									case WL.Env.IPHONE:
									case WL.Env.IPAD:
									case WL.Env.BLACKBERRY10:
									case WL.Env.WINDOWS_PHONE_8:
									case WL.Env.MOBILE_WEB:
									case WL.Env.ADOBE_AIR:
										requestOptions.parameters.compressResponse = invocationData.compressResponse;
										break;
									default:
										requestOptions.parameters.compressResponse = invocationData.compressResponse;
										break;
									}
									// use fix parameters order for queryLeftTicket
									if ((invocationData.procedure == "queryLeftTicket") && invocationData.parameters) {
										var paramsObj = invocationData.parameters[0];
										var paramsStr = "[{";
										if(paramsObj["train_date"] || paramsObj["train_date"]==""){
											paramsStr += '"train_date":"'+paramsObj["train_date"]+'"';
											delete paramsObj["train_date"];
										}
										if(paramsObj["purpose_codes"] || paramsObj["purpose_codes"]==""){
											paramsStr += ',"purpose_codes":"'+paramsObj["purpose_codes"]+'"';
											delete paramsObj["purpose_codes"];
										}
										if(paramsObj["from_station"] || paramsObj["from_station"]==""){
											paramsStr += ',"from_station":"'+paramsObj["from_station"]+'"';
											delete paramsObj["from_station"];
										}
										if(paramsObj["to_station"] || paramsObj["to_station"]==""){
											paramsStr += ',"to_station":"'+paramsObj["to_station"]+'"';
											delete paramsObj["to_station"];
										}
										if(paramsObj["station_train_code"] || paramsObj["station_train_code"]==""){
											paramsStr += ',"station_train_code":"'+paramsObj["station_train_code"]+'"';
											delete paramsObj["station_train_code"];
										}
										if(paramsObj["start_time_begin"] || paramsObj["start_time_begin"]==""){
											paramsStr += ',"start_time_begin":"'+paramsObj["start_time_begin"]+'"';
											delete paramsObj["start_time_begin"];
										}
										if(paramsObj["start_time_end"] || paramsObj["start_time_end"]==""){
											paramsStr += ',"start_time_end":"'+paramsObj["start_time_end"]+'"';
											delete paramsObj["start_time_end"];
										}
										if(paramsObj["train_headers"] || paramsObj["train_headers"]==""){
											paramsStr += ',"train_headers":"'+paramsObj["train_headers"]+'"';
											delete paramsObj["train_headers"];
										}
										if(paramsObj["train_flag"] || paramsObj["train_flag"]==""){
											paramsStr += ',"train_flag":"'+paramsObj["train_flag"]+'"';
											delete paramsObj["train_flag"];
										}
										
										var otherStr = String(WLJSX.Object.toJSON(paramsObj));
										paramsStr += ","+otherStr.slice(1)+"]";
										requestOptions.parameters.parameters = paramsStr;
									}else if ((invocationData.procedure == "syncCache") && invocationData.parameters) {
										var paramsObj = invocationData.parameters[0];
										var paramsStr = "[{";
										if(paramsObj["syncList"] || paramsObj["syncList"]==""){
											paramsStr += '"syncList":"'+paramsObj["syncList"]+'"';
											delete paramsObj["syncList"];
										}
										if(paramsObj["syncVersionList"] || paramsObj["syncVersionList"]==""){
											paramsStr += ',"syncVersionList":"'+paramsObj["syncVersionList"]+'"';
											delete paramsObj["syncVersionList"];
										}
										var otherStr = String(WLJSX.Object.toJSON(paramsObj));
										paramsStr += ","+otherStr.slice(1)+"]";
										requestOptions.parameters.parameters = paramsStr;
									}else if (invocationData.parameters) {
										requestOptions.parameters.parameters = WLJSX.Object.toJSON(invocationData.parameters);
									}

									// invoke is used for adapter
									requestOptions.method = 'get';

									// need to send device context updates when
									// calling invokeProcedure
									WL.Client.__deviceContextTransmission
											.enableDeltaSending(true);
									new WLJSX.Ajax.WLRequest(url,
											requestOptions);
									WL.Client.__deviceContextTransmission
											.enableDeltaSending(false);
								}
								;
							}
							;

							// get checkcode error.
							function onCheckcodeFailure(error) {
								// WL.Logger.debug('Get check code error. Please
								// retry.');
							}
							;
						},
						keepPageURL : function() {
							var pageURL = jq.mobile.activePage.attr("data-url");
							var m = pageURL.indexOf("views");
							if (m > 0) {
								var nowPage = pageURL.slice(m);
								if (nowPage != mor.ticket.viewControl.session_out_page) {
									mor.ticket.viewControl.session_out_page = pageURL
											.slice(m);
									// WL.Logger.debug("pageURL:" + pageURL);
									// WL.Logger.debug("session_out_page:" +
									// mor.ticket.viewControl.session_out_page);
								}
							}
						},
						prepareRequestCommonParameters : function(
								parametersToOverride) {
							var common = mor.ticket.common;
							var user = mor.ticket.loginUser;

							var serverTime = new Date();
							serverTime.setTime(Date.now()
									- common["baseDTO.time_offset"]);
							var time_str = serverTime.formatWithTimezone(
									"yyyyMMddhhmmss", 8);
							//remove check_code and use checkcode plugin to generate checkcode
							//var check_code = hex_md5('123456' + time_str + common['baseDTO.device_no']);
							var parameters = {
								"baseDTO.os_type" : common['baseDTO.os_type'],
								"baseDTO.device_no" : common['baseDTO.device_no'],
								"baseDTO.mobile_no" : common['baseDTO.mobile_no'],
								"baseDTO.time_str" : time_str,
								// "baseDTO.check_code" : check_code,
								"baseDTO.version_no" : common['baseDTO.version_no'],
								"baseDTO.user_name" : user['username']
							// "password": user.password
							};
							if (parametersToOverride) {
								jq.extend(parameters, parametersToOverride);
							}
							return parameters;
						},

						creatCommonRequestFailureHandler : function(result) {
							// worklight adaptet service or ots mobile is not
							// working/time out
							return function(result) {
								if (busy.isVisible()) {
									busy.hide();
								}
								if (result && result.status == 200) {
									if (result.invocationResult.responseID == "26") {// 调用otsmobile失败
										WL.SimpleDialog.show("温馨提示",
												"系统忙,请稍后再试。", [ {
													text : '确定',
													handler : function() {
													}
												} ]);
									} else {
										WL.SimpleDialog.show("温馨提示",
												"哎呀，网络好像有问题，请检查网络连接！", [ {
													text : '确定',
													handler : function() {
													}
												} ]);
									}
								} else {
									WL.Device
											.getNetworkInfo(function(
													networkInfo) {
												if (networkInfo.isNetworkConnected == "false") {
													WL.SimpleDialog
															.show(
																	"温馨提示",
																	"哎呀，您的网络有问题，请检查网络连接。",
																	[ {
																		text : '确定',
																		handler : function() {
																		}
																	} ]);
												} else {
													WL.SimpleDialog
															.show(
																	"温馨提示",
																	"哎呀，您的网络好像有问题，请检查网络连接。",
																	[ {
																		text : '确定',
																		handler : function() {
																		}
																	} ]);
												}
											});
								}
								;
							};
						},

						invocationIsSuccessful : function(invocationResult) {
							if (invocationResult.isSuccessful
									&& invocationResult.succ_flag == "-6") {
								var serverDate = new Date(
										invocationResult.responseHeaders.Date);
								var common = mor.ticket.common;
								common["baseDTO.time_offset"] = Date.now()
										- serverDate;
								window.ticketStorage.setItem("serverTime",
										serverDate);
								invocationResult.error_msg = "手机日期设置有误，系统已自动校正，请重试";
								return false;
							}

							if (invocationResult.authRequired
									&& invocationResult.succ_flag == "0") {
								return false;
							}
							// wl or ots session time out, require user login
							// again
							if (invocationResult.authRequired
									|| invocationResult.succ_flag == "-5") {
								WL.Logger.debug("进入登录超时判断");
								mor.ticket.util.keepPageURL();
								jq.mobile.changePage(vPathCallBack()
										+ "loginTicket.html");
								// mor.ticket.util.alertMessage("登录超时，请重新登录！");
								// registerAutoLoginHandler(customLogin,
								// customFail);

								return false;
							}
							if (invocationResult.isSuccessful
									&& invocationResult.succ_flag == "-1") {
								invocationResult.error_msg = "哎呀，网络好像有问题，请检查网络连接";
								return false;
							}

							if (invocationResult.isSuccessful
									&& invocationResult.succ_flag == "-2") {
								invocationResult.error_msg = "哎呀，网络有问题，请检查网络连接";
								return false;
							}

							return (invocationResult.isSuccessful && invocationResult.succ_flag == "1");
						},

						getStartTimeBeginCode : function(time_period) {
							switch (time_period) {
							case '0':
								return "0000";
								break;
							case '1':
								return "0000";
								break;
							case '2':
								return "0600";
								break;
							case '3':
								return "1200";
								break;
							case '4':
								return "1800";
								break;
							default:
								console.log("Invalid time_period: "
										+ time_period);
								return "0000";
							}
						},

						getStartTimeEndCode : function(time_period) {
							switch (time_period) {
							case '0':
								return "2400";
								break;
							case '1':
								return "0600";
								break;
							case '2':
								return "1200";
								break;
							case '3':
								return "1800";
								break;
							case '4':
								return "2400";
								break;
							default:
								console.log("Invalid time_period: "
										+ time_period);
								return "2400";
							}
						},
						getSeatType : function(seat_type) {
							switch (seat_type) {
							case '0':
								return "";
								break;
							case '1':
								return "M";
								break;
							case '2':
								return "O";
								break;
							case '3':
								return "9";
								break;
							case '4':
								return "P";
								break;
							case '5':
								return "Q";
								break;
							default:
								console.log("Invalid seat_type: " + seat_type);
								return "";
							}
						},
						getNewDate : function() {
							var common = mor.ticket.common;
							var date = new Date();
							date.setTime(Date.now()
									- common["baseDTO.time_offset"]);
							return date;
						},
						processDateCode : function(dateStr) {
							if (dateStr) {
								return dateStr.split("-").join("");
							} else {
								// to fix ios4.3 default train date is null
								// issue
								if (window.ticketStorage
										.getItem("set_train_date_type") != null) {
									var date = mor.ticket.util.getNewDate();
									var queryDate = new Date(
											date
													.setDate(date.getDate()
															+ parseInt(window.ticketStorage
																	.getItem("set_train_date_type"))));
									return queryDate.format("yyyyMMdd");
								} else {
									var date = mor.ticket.util.getNewDate();
									date.setDate(date.getDate() + 1);
									return date.format("yyyyMMdd");
								}
							}
						},

						changeDateType : function(dateStr) {
							if (dateStr) {
								var year = dateStr.substr(0, 4);
								var moth = dateStr.substr(4, 2);
								var day = dateStr.substr(6, 2);
								return year + "-" + moth + "-" + day;
							}
							return;
						},

						getTimePeriodDescription : function(time_period) {
							var desc = [ "00:00--24:00", "00:00--06:00",
									"06:00--12:00", "12:00--18:00",
									"18:00--24:00" ];
							return desc[time_period];
						},

						getTicketTypeName : function(type) {
							return mor.cache.ticketTypeMap[type];
						},
						
						repalceYpInfoType : function(type) {
								switch (type) {
									case "商务座":
										return  "商务";
										break;
									case "特等座":
										return  "特等";
										break;
									case "一等座":
										return  "一等";
										break;
									case "二等座":
										return  "二等";
										break;
									case "高级软卧":
										return  "高软";
										break;
									case "观光座":
										return  "观光";
										break;
									case "混编硬座":
										return "混编";
									default :
										return type;
										break;
									}
								
						},
						
						
						
						

	

						getPassengerTypeName : function(type) {
							var map = {
								"1" : "成人",
								"2" : "儿童",
								"3" : "学生",
								"4" : "残军"
							};
							return map[type];
						},

						getIdTypeName : function(type) {
							return mor.cache.idTypeMap[type];
						},
						getSeatTypeName : function(type, seat_no) {
							var typeName = "";
							if (seat_no != undefined && seat_no != '') {
								var lastnum = seat_no.substring(3, 4);
								if (lastnum >= 'a' && lastnum <= 'z') {
									switch (type) {
									case '1':
									case 'B':
										typeName = "硬卧代硬座";
										break;
									case '2':
										typeName = "软卧代硬座";
										break;
									case 'O':
										typeName = "软卧代二等座";
										break;
									default:
										typeName = "";
										break;
									}
								} else {
									typeName = mor.ticket.cache
											.getSeatTypeByCode(type);
								}
							} else {
								typeName = mor.ticket.cache
										.getSeatTypeByCode(type);
							}
							return typeName;
						},
						setCustomSelectScrollerValue : function(opt) {
							jq("#" + opt.id).val(opt.value);
							// jq("#" +
							// opt.id).val(opt.value).scroller("setValue", ["_"
							// + opt.value], true);
							jq("#" + opt.id + "_dummy").val(opt.label);
						},

						getSexName : function(type) {
							var map = {
								"M" : "男",
								"F" : "女"
							};
							return map[type];
						},

						getTrainDateTypeByCode : function(code) {
							var map = {
								"0" : "当天",
								"1" : "第二天",
								"2" : "第三天",
								"3" : "第四天"
							};
							return map[code];
						},

						getCountryByCode : function(id) {
							return mor.cache.countryMap[id];
						},

						getProvinceByCode : function(type) {
							return mor.cache.provinceMap[type];
						},

						getUniversityByCode : function(type) {
							return mor.cache.universityMap[type];
						},

						getCityByCode : function(type) {
							return mor.cache.cityMap[type];
						},

						getUniversityByProvince : function(provinceCode) {
							var universityList = mor.ticket.cache.university;
							var universitybyProvinceList = [];
							for ( var i = 0; i < universityList.length; i++) {
								if (provinceCode == universityList[i].province_code) {
									var object = universityList[i];
									universitybyProvinceList.push(object);
								}
							}
							return universitybyProvinceList;
						},

						isNoValue : function(str) {
							if (str == null || str == "") {
								return true;
							}
							return false;
						},

						clearObject : function(Object) {
							for (o in Object) {
								Object[o] = "";
							}
						},

						isHybrid : function() {
							var types = [ "iphone", "ipad", "android" ];
							for ( var i = 0; i < types.length; i++) {
								if (WL.Client.getEnvironment() === types[i]) {
									return true;
								} else {
									continue;
								}
							}
							return false;
						},
						isIPhone : function() {
							return WL.Client.getEnvironment() == "iphone"
									|| WL.Client.getEnvironment() == "ipad";
						},

						isAndroid : function() {
							return WL.Client.getEnvironment() == "android";
						},

						alertMessage : function(message) {
							if(mor.ticket.util.alertMessageCache) return;
							mor.ticket.util.alertMessageCache=true;
							WL.SimpleDialog.show("温馨提示", message, [ {
								text : '确定',
								handler : function() {
									mor.ticket.util.alertMessageCache=false;
								}
							} ]);
						},

						getLiShiStr : function(str) {
							return ((str.substring(0, 2) == '00') ? "" : (str
									.substring(0, 2) + "小时"))
									+ str.substring(3, 5) + "分钟";
						},

						formateTrainTime : function(timeStr) {
							if (timeStr && timeStr != "") {
								return timeStr.substring(0, 2) + ":"
										+ timeStr.substring(2, 4);
							} else {
								return "";
							}
						},
						// 由字符串转化时间 ——如："2012-12-12"转化为Data对象
						setMyDate : function(dateStr) {
							//mod by yiguo
							if(dateStr){
							var year = parseInt(dateStr.slice(0, 4), 10);
							var month = parseInt(dateStr.slice(5, 7), 10) - 1;
							var day = parseInt(dateStr.slice(8), 10);
							return new Date(year, month, day);}else{
								return new Date();
							}
						},
						// 由字符串转化时间 ——如："20121212"转化为Data对象
						setMyDate2 : function(dateStr) {
							//mod by yiguo
							if(dateStr){
							var year = parseInt(dateStr.substring(0, 4), 10);
							var month = parseInt(dateStr.substring(4, 6), 10) - 1;
							var day = parseInt(dateStr.substring(6), 10);
							return new Date(year, month, day);
							}else{
								return new Date();
							}
						},
						// 由字符串转化时间 ——如："201212121038"转化为Data对象
						setMyDate3 : function(pay_limit_time) {
							//mod by yigou
							if(pay_limit_time){
								var year = parseInt(pay_limit_time.substr(0, 4), 10);
								var month = parseInt(pay_limit_time.substr(4, 2),
										10) - 1;
								var day = parseInt(pay_limit_time.substr(6, 2), 10);
								var hours = parseInt(pay_limit_time.substr(8, 2),
										10);
								var minutes = parseInt(
										pay_limit_time.substr(10, 2), 10);
								var seconds = parseInt(
										pay_limit_time.substr(12, 2), 10);
								return new Date(year, month, day, hours, minutes,
										seconds);
							}else{
								return new Date();
							}
							
						},
						getLocalDateString1 : function(dateStr) {
							//mod by yiguo
							if(dateStr){
								var year = parseInt(dateStr.slice(0, 4), 10);
								var month = parseInt(dateStr.slice(5, 7), 10) - 1;
								var day = parseInt(dateStr.slice(8), 10);
								var date = new Date(year, month, day);
								return date.format("yyyy年M月d日");
							}else{
								return "";
							}
							
						},
						getLocalDateString2 : function(dateStr) {
							//mod by yiguo
							if(dateStr){
							var year = parseInt(dateStr.substr(0, 4), 10);
							var month = parseInt(dateStr.substr(4, 2), 10) - 1;
							var day = parseInt(dateStr.substr(6, 2), 10);
							var date = new Date(year, month, day);
							return date.format("yyyy年M月d日");
							}else{
								return "";
							}
						},
						
						getLocalDateString3 : function(dateStr) {
							//mod by yiguo
							if(dateStr){
								var year = parseInt(dateStr.substr(0, 4), 10);
								var month = parseInt(dateStr.substr(4, 2), 10);
								var day = parseInt(dateStr.substr(6, 2), 10);
								var hh = dateStr.substring(8, 10);
								var mm = dateStr.substring(10, 12);
								var newDateStr = year+"年"+month+"月"+day+"日 "+hh+":"+mm;
								return newDateStr;
							}else{
								return "";
							}
						},
						
						// android中去掉content中的 data-iscroll
						androidRemoveIscroll : function(view) {
							if (WL.Client.getEnvironment() == "android") {
								jq(view).children("[data-role='content']")
										.removeAttr("data-iscroll");
							}
						},

						sortPassengers : function(passengers) {
							passengers.sort(mor.ticket.util.sortfunction);
						},

						sortfunction : function(x, y) {
							return x.user_nameSM.toUpperCase().charCodeAt(0)
									- y.user_nameSM.toUpperCase().charCodeAt(0);
						},

						contentIscrollTo : function(x, y, time) {
							jq.mobile.activePage.find(".iscroll-wrapper")
									.iscrollview("scrollTo", x, y, time);
						},

						contentIscrollRefresh : function() {
							jq.mobile.activePage.find(".iscroll-wrapper")
									.iscrollview("refresh");
						},

						bindSelectFocusBlurListener : function(select) {
							var jq_select = jq(select);
							var jq_content = jq_select.parents(".ui-content");
							var util = mor.ticket.util;
							if (util.isIPhone()) {
								jq_select.bind("focus", function() {
									jq_content.addClass("ui-disabled");
									// jq.mobile.activePage.find(".iscroll-wrapper").iscrollview("option",
									// {resizeWrapper:false});
								});
								jq_select.bind("blur", function() {
									// jq.mobile.activePage.find(".iscroll-wrapper").iscrollview("option",
									// {resizeWrapper:true});
									jq_content.removeClass("ui-disabled");
								});
							}
						},

						enableAutoScroll : function(selector, focusArray) {
							var util = mor.ticket.util;
							if (util.isAndroid())
								util.enableAutoScrollAndroid(selector,
										focusArray);
							else if (util.isIPhone())
								util.enableAutoScrollIPhone(selector,
										focusArray);
						},

						enableAutoScrollAndroid : function(selector, focusArray) {
							var currScroller = jq(jq(selector)[0]).parents(
									'.iscroll-wrapper');

							var getRelHeight = function(node) {
								var oTop = 0;

								while (node.offsetParent) {
									oTop += node.offsetTop;
									node = node.offsetParent;
								}

								var keyboardHeight = 260;
								var dTop = (document.documentElement.clientHeight - keyboardHeight) / 2;

								var relHeight = oTop - dTop;
								return (relHeight < 0) ? 0 : relHeight;
							};

							var doScrollAndFocus = function(evt) {
								// scroll input to top
								currScroller.iscrollview('scrollTo', 0,
										getRelHeight(evt.target), 0, true);
							};

							var reset = function() {
								currScroller.find('.keyboard_holder').hide();
								currScroller.iscrollview('refresh');
							};

							var startSimulate = function(evt) {
								focusArray.push(1);
								// if no keyboard_holder, create one
								if (currScroller.find('.keyboard_holder').length == 0) {
									currScroller
											.find('.iscroll-content')
											.append(
													'<div class="keyboard_holder" style="height:500px;display:none;">');
								}
								if (currScroller.find('.keyboard_holder').is(
										':hidden')) {
									// show keyboard_holder
									currScroller.find('.keyboard_holder')
											.show();
									// refresh iscroll synchronously
									currScroller.iscrollview('refresh', false);
								}
								doScrollAndFocus(evt);
							};

							var onBlur = function(evt) {
								if (!document.activeElement.isContentEditable) {
									focusArray.pop();
									// must be asynchronous
									setTimeout(function() {
										if (!focusArray.length)
											reset();
									}, 0);
								}
							};

							jq(selector).click(startSimulate).blur(onBlur);
						},

						enableAutoScrollIPhone : function(selector, focusArray) {
							var currScroller = jq(jq(selector)[0]).parents(
									'.iscroll-wrapper');

							var getRelHeight = function(node) {
								var oTop = 0;

								while (node.offsetParent) {
									oTop += node.offsetTop;
									node = node.offsetParent;
								}

								var keyboardHeight = 300;
								var dTop = (document.documentElement.clientHeight - keyboardHeight) / 2;

								var relHeight = oTop - dTop;
								return relHeight < 0 ? 0 : relHeight;
							};

							var doScrollAndFocus = function(evt) {
								// scroll input to top
								currScroller.iscrollview('scrollTo', 0,
										getRelHeight(evt.target), 0, true);
								// focus to input and popup keyboard
								evt.target.focus();
							};

							var reset = function() {
								currScroller.find('.keyboard_holder').hide();
								currScroller.iscrollview('refresh');
							};

							var startSimulate = function(evt) {
								evt.preventDefault();
								focusArray.push(1);
								// if no keyboard_holder, create one
								if (currScroller.find('.keyboard_holder').length == 0) {
									currScroller
											.find('.iscroll-content')
											.append(
													'<div class="keyboard_holder" style="height:500px;display:none;">');
								}
								if (currScroller.find('.keyboard_holder').is(
										':hidden')) {
									// show keyboard_holder
									currScroller.find('.keyboard_holder')
											.show();
									// refresh iscroll synchronously
									currScroller.iscrollview('refresh', false);
								}
								doScrollAndFocus(evt);
							};

							var onBlur = function(evt) {
								focusArray.pop();
								// must be asynchronous
								setTimeout(function() {
									if (!focusArray.length)
										reset();
								}, 0);
							};

							var onFocus = function(evt) {
								if (!focusArray.length) {
									focusArray.push(1);
									currScroller.iscrollview('scrollTo', 0,
											getRelHeight(evt.target), 0, true);
								}
							};
							jq(selector).bind('vmousedown', startSimulate)
									.focus(onFocus).blur(onBlur);

						},
						IscrollBlurInput : function(view) {
							jq(view).find(".iscroll-wrapper").bind(
									"iscroll_onbeforescrollstart", function() {
										if (jq(view + " input").is(":focus")) {
											jq(view + " input").blur();
											return false;
										}
									});
						},
						paddingWidth : function(str, width, pStr) {
							str = str + '';
							for ( var i = 0, len = width - str.length; i < len; i++) {
								str = pStr + str;
							}
							return str;
						}
					});
})();