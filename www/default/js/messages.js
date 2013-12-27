
/* JavaScript content from js/messages.js in folder common */
/*
* Licensed Materials - Property of IBM
* 5725-G92 (C) Copyright IBM Corp. 2006, 2012. All Rights Reserved.
* US Government Users Restricted Rights - Use, duplication or
* disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

Messages = {
	// Add here your messages for the default language. 
	// Generate a similar file with a language suffix containing the translated messages
	// key1 : message1,
	// key2 : message2
	
	// Uncomment if you use the Authenticator example.  
	// usernameLabel : "Username:",
	// passwordLabel : "Password:",
	// invalidUsernamePassword : 'Invalid username or password.' 
};

//Translate WL Client Messages
//a
WL.ClientMessages.accessDenied = '\u62d2\u7edd\u8bbf\u95ee';//拒绝访问
WL.ClientMessages.authenticationFailure = '\u9a8c\u8bc1\u5931\u8d25';//验证失败
WL.ClientMessages.authFailure = '\u5728\u5904\u7406\u5e94\u7528\u8bf7\u6c42\u7684\u8fc7\u7a0b\u4e2d\u53d1\u751f\u4e86\u9519\u8bef\u3002';//在处理应用请求的过程中发生了错误。
WL.ClientMessages.applicationDenied = '\u5e94\u7528\u88ab\u7981\u7528';//应用被禁用
WL.ClientMessages.authenticityFailure = '\u5e94\u7528\u6821\u9a8c\u5931\u8d25\uff0c\u7248\u672c\u5f02\u5e38'; //应用校验失败，版本异常
//b
WL.ClientMessages.browserIsNotSupported = '\u5f53\u524d\u4e0d\u652f\u6301\u007b\u0030\u007d\u3002';//当前不支持{0}。
//c
WL.ClientMessages.challengeProcessorExists = '\u57df\u0022\u007b\u0030\u007d\u0022\u6709\u592a\u591a\u7684\u5b9e\u73b0\u8005\uff0c\u8bf7\u68c0\u67e5\u4ee3\u7801';//域"{0}"有太多的实现者，请检查代码
WL.ClientMessages.close = '\u5173\u95ed';//关闭
WL.ClientMessages.cookiesAreDisabled = '\u60a8\u7684\u6d4f\u89c8\u5668\u7981\u7528\u4e86\u0043\u006f\u006f\u006b\u0069\u0065\u3002\u8bf7\u542f\u7528\u0043\u006f\u006f\u006b\u0069\u0065\u4ee5\u4fdd\u8bc1\u5e94\u7528\u7684\u6b63\u5e38\u8fd0\u884c\u3002';//您的浏览器禁用了Cookie。请启用Cookie以保证应用的正常运行。
WL.ClientMessages.copyToClipboard='\u590D\u5236';//复制
//d
WL.ClientMessages.details='\u8BE6\u7EC6';//详情
WL.ClientMessages.diagApp='\u5E94\u7528\u8BCA\u65AD';//应用诊断
WL.ClientMessages.diagTime = '\u65f6\u95f4';//时间
WL.ClientMessages.diagApplicationName = '\u5e94\u7528\u540d';//应用名
WL.ClientMessages.diagApplicationVersion = '\u5e94\u7528\u7248\u672c';//应用版本
WL.ClientMessages.diagServiceURL = '\u670d\u52a1\u5730\u5740';//服务地址
WL.ClientMessages.diagDevicePlatform = '\u8bbe\u5907\u5e73\u53f0';//设备平台
WL.ClientMessages.diagDeviceVersion = '\u8bbe\u5907\u7248\u672c';//设备版本
WL.ClientMessages.diagScreenResolution = '\u5c4f\u5e55\u5206\u8fa8\u7387';//屏幕分辨率
WL.ClientMessages.diagAirplaneMode = '\u98de\u884c\u6a21\u5f0f';//飞行模式
WL.ClientMessages.diagUsingNetwork = '\u4f7f\u7528\u7f51\u7edc\u4e2d';//使用网络中
WL.ClientMessages.diagWifiName = '\u0057\u0069\u0066\u0069\u540d\u79f0';//Wifi名称
WL.ClientMessages.diagMobileNetworkType = '\u79fb\u52a8\u7f51\u7edc\u7c7b\u578b';//移动网络类型
WL.ClientMessages.diagCarrierName = '\u8fd0\u8425\u5546';//运营商
WL.ClientMessages.diagErrorCode = '\u9519\u8bef\u4ee3\u7801';//错误代码
WL.ClientMessages.diagErrorMessage = '\u9519\u8bef\u4fe1\u606f';//错误信息
WL.ClientMessages.diagHttpStatus = '\u0048\u0054\u0054\u0050\u72b6\u6001';//HTTP状态
WL.ClientMessages.diagIPAddress = '\u0049\u0050\u5730\u5740';//IP地址
WL.ClientMessages.directUpdateNotificationTitle = '\u66F4\u65B0\u63D0\u793A'; //'更新提示'
WL.ClientMessages.directUpdateNotificationMessage = '\u5E94\u7528\u73B0\u5728\u6709\u65B0\u7684\u7248\u672C\u53EF\u4F9B\u66F4\u65B0 (\u6587\u4EF6\u5927\u5C0F {0} MB).';//应用程序有可用的更新 (文件大小 {0} MB)。
WL.ClientMessages.directUpdateErrorTitle = '\u5E94\u7528\u66F4\u65B0\u5931\u8D25';//应用更新失败
WL.ClientMessages.directUpdateErrorMessageNotEnoughStorage = '\u5e94\u7528\u7a0b\u5e8f\u6709\u53ef\u7528\u7684\u66f4\u65b0\uff0c\u0020\u4f46\u8bbe\u5907\u6ca1\u6709\u8db3\u591f\u7684\u78c1\u76d8\u7a7a\u95f4\u0020\u0028\u9700\u8981\u003a\u0020\u007b\u0030\u007d\u0020\u004d\u0042\u002c\u0020\u53ef\u7528\u003a\u0020\u007b\u0031\u007d\u0020\u004d\u0042\u0029\u3002';//应用程序有可用的更新， 但设备没有足够的磁盘空间 (需要: {0} MB, 可用: {1} MB)。
WL.ClientMessages.directUpdateErrorMessageFailedProcessingZipFile='\u5904\u7406\u5E94\u7528\u5347\u7EA7\u6587\u4EF6\u5931\u8D25';//处理应用升级文件失败
WL.ClientMessages.directUpdateErrorMessageFailedDownloadingZipFile='\u4E0B\u8F7D\u5E94\u7528\u5347\u7EA7\u6587\u4EF6\u5931\u8D25';//下载应用升级文件失败
WL.ClientMessages.downloadAppWebResourcesPleaseSpecifyAppID = '\u65e0\u6cd5\u4e0b\u8f7d\u5e94\u7528\u7a0b\u5e8f\uff0c\u8bf7\u5728\u8bbe\u7f6e\u9875\u9762\u4e2d\u6307\u5b9a\u7a0b\u5e8f\u0049\u0044\u3002';//无法下载应用程序。请在设置页面中指定程序ID。
WL.ClientMessages.downloadAppWebResourcesAppIdNotExist = '\u672a\u80fd\u627e\u5230\u5e94\u7528\u7a0b\u5e8f\u0022\u007b\u0030\u007d\u0022\u3002\u0020';//未能找到应用程序"{0}"。
WL.ClientMessages.downloadAppWebResourcesPleaseSpecifyAppVersion = '\u65e0\u6cd5\u4e0b\u8f7d\u5e94\u7528\u7a0b\u5e8f\uff0c\u8bf7\u5728\u8bbe\u7f6e\u9875\u9762\u4e2d\u6307\u5b9a\u7a0b\u5e8f\u7248\u672c\u3002';//无法下载应用程序。请在设置页面中指定程序版本。
WL.ClientMessages.downloadAppWebResourcesSkinIsNotValid = '\u65e0\u6cd5\u4e0b\u8f7d\u5e94\u7528\u7a0b\u5e8f\u3002\u0020\u76ae\u80a4\u003a\u0020\u007b\u0030\u007d\u4e0d\u5b58\u5728\u3002\u8bf7\u786e\u8ba4\u0020\u0067\u0065\u0074\u0053\u006b\u0069\u006e\u004e\u0061\u006d\u0065\u0028\u0029\u8fd4\u56de\u6709\u6548\u7684\u76ae\u80a4\u540d\u79f0\u3002';//无法下载应用程序。 皮肤: {0}不存在。请确认 getSkinName()返回有效的皮肤名称。
WL.ClientMessages.downloadAppWebResourcesAppVersionNotExist = 'Cannot find app "{0}" {1} for {2}';
WL.ClientMessages.deviceAuthenticationFail='\u8FDE\u63A5\u5F02\u5E38';//连接异常
WL.ClientMessages.downloadAppWebResourcesConnectionToServerUnavailable = '\u65e0\u6cd5\u8fde\u63a5\u5230\u670d\u52a1\u5668\uff0c\u65e0\u6cd5\u4e0b\u8f7d\u5e94\u7528\u7a0b\u5e8f\u3002';//无法连接到服务器。 无法下载应用程序。
//e
WL.ClientMessages.expandWindow = '\u653e\u5927\u7a97\u53e3\u4ee5\u4f7f\u7528\u5e94\u7528\u7a0b\u5e8f';//放大窗口以使用应用程序
WL.ClientMessages.exit='\u9000\u51FA'; //退出
WL.ClientMessages.exitApplication = '\u9000\u51fa\u5e94\u7528';//退出应用
WL.ClientMessages.error = '\u9519\u8bef';//错误
//f
//g
WL.ClientMessages.gadgetUpdateAvailable = '\u5e94\u7528\u66f4\u65b0\u53ef\u7528';//应用更新可用
WL.ClientMessages.getNewVersion = '\u83b7\u53d6\u65b0\u7248\u672c';//获取新版本
//h
//i
WL.ClientMessages.invalidUsernamePassword = '\u65e0\u6548\u7684\u7528\u6237\u540d\u6216\u5bc6\u7801';//无效的用户名或密码
//j
//k
//l
WL.ClientMessages.loading = '\u52a0\u8f7d\u4e2d\u002e\u002e\u002e';//加载中。。。
WL.ClientMessages.login = '\u767b\u5f55';//登录
//m
WL.ClientMessages.minimize = '\u6700\u5c0f\u5316';//最小化
//n
WL.ClientMessages.name = '\u59d3\u540d\uff1a';//姓名：
WL.ClientMessages.noInternet = '\u65e0\u6cd5\u8fde\u63a5\u5230\u670d\u52a1\u3002';//无法连接到服务。
WL.ClientMessages.notificationTitle = '\u670d\u52a1\u901a\u77e5';//服务通知
WL.ClientMessages.notificationUpdateFailure = '\u6ce8\u518c\u63a5\u6536\u901a\u77e5\u5931\u8d25\uff0c\u5e94\u7528\u5c06\u65e0\u6cd5\u63a5\u6536\u5230\u4efb\u4f55\u901a\u77e5\u3002';//注册接收通知失败。应用将无法接收到任何通知。
WL.ClientMessages.notAvailable = '\u4e0d\u53ef\u7528';//不可用
//o
WL.ClientMessages.ok = '\u786e\u5b9a';//确定
WL.ClientMessages.osxReloadGadget = '\u8bf7\u91cd\u542f\u5e94\u7528\u7a0b\u5e8f';//请重启应用程序
WL.ClientMessages.osxReloadGadgetInstructions = '\u8bf7\u6309\u0043\u004d\u0044\u002b\u0052\u91cd\u542f\u5e94\u7528\u7a0b\u5e8f\u3002';//请按CMD+R重启应用程序。
//p
WL.ClientMessages.password = '\u5bc6\u7801\uff1a';//密码：
//q
//r
WL.ClientMessages.reload='\u91CD\u8F7D';//重载
WL.ClientMessages.restore = '\u6062\u590d';//恢复
WL.ClientMessages.requestTimeout='\u5E94\u7528\u8FDE\u63A5\u540E\u53F0\u670D\u52A1\u8D85\u65F6\uFF0C\u8BF7\u68C0\u67E5\u60A8\u7684\u7F51\u7EDC\u8FDE\u63A5';//应用连接后台服务超时，请检查您的网络连接
WL.ClientMessages.responseNotRecognized = '\u65e0\u6cd5\u89e3\u6790\u54cd\u5e94\uff0c\u0020\u8bf7\u8054\u7cfb\u652f\u6301\u4eba\u5458';//无法解析响应， 请联系支持人员
//s
WL.ClientMessages.settings='\u8BBE\u7F6E';//设置
WL.ClientMessages.serverError = '\u7a0b\u5e8f\u8c03\u7528\u51fa\u9519\u3002';//程序调用出错。
//t
WL.ClientMessages.tryAgain = '\u8bf7\u518d\u8bd5\u4e00\u6b21';//请再试一次
//u
WL.ClientMessages.userInstanceAccessViolationException = '\u60a8\u6b63\u5728\u5c1d\u8bd5\u767b\u5f55\u5230\u4e00\u4e2a\u4e0d\u662f\u60a8\u4f7f\u7528\u7684\u5e94\u7528\u7a0b\u5e8f\u3002';//您正在尝试登录到一个不是您使用的应用程序。
WL.ClientMessages.unexpectedError = '\u60a8\u6b63\u5728\u5c1d\u8bd5\u767b\u5f55\u5230\u4e00\u4e2a\u4e0d\u662f\u60a8\u4f7f\u7528\u7684\u5e94\u7528\u7a0b\u5e8f\u3002';//服务器无法处理当前应用程序的请求，请稍后再试。
WL.ClientMessages.unresponsiveHost = '\u670d\u52a1\u5f53\u524d\u4e0d\u53ef\u7528\u3002';//服务当前不可用。
WL.ClientMessages.update='\u5347\u7EA7';//更新
WL.ClientMessages.upgradeGadget = '\u5e94\u7528\u5f53\u524d\u7248\u672c\u662f\u007b\u0030\u007d\u3002\u7248\u672c\u007b\u0031\u007d\u53ef\u7528\u3002\u70b9\u51fb\u786e\u5b9a\u4e0b\u8f7d\u5e76\u5b89\u88c5\u5e94\u7528\u3002';//应用当前版本是{0}。版本{1}可用。点击确定下载并安装应用。
//v
//w
WL.ClientMessages.wlclientInitFailure='\u9519\u8BEF\u63D0\u793A';//错误提示
//x
//y
//z