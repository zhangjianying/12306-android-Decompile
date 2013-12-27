package com.worklight.wlclient;

import android.os.Build;
import android.os.Build.VERSION;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.api.WLRequestOptions;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

class InternalRequestSender
  implements Runnable
{
  WLRequest request;

  protected InternalRequestSender(WLRequest paramWLRequest)
  {
    this.request = paramWLRequest;
  }

  private void addInstanceAuthHeader()
  {
    if (!WLUtils.isStringEmpty(WLCookieManager.getInstanceAuthId()))
    {
      if (this.request.getPostRequest().getHeaders("WL-Instance-Id") != null)
        this.request.getPostRequest().removeHeaders("WL-Instance-Id");
      this.request.getPostRequest().addHeader("WL-Instance-Id", WLCookieManager.getInstanceAuthId());
    }
  }

  private void setConnectionTimeout(HttpClient paramHttpClient)
  {
    HttpConnectionParams.setSoTimeout(paramHttpClient.getParams(), this.request.getOptions().getTimeout());
    HttpConnectionParams.setConnectionTimeout(paramHttpClient.getParams(), this.request.getOptions().getTimeout());
  }

  private void setUserAgentHeader(HttpClient paramHttpClient)
  {
    String str1 = (String)paramHttpClient.getParams().getParameter("http.useragent");
    String str2 = " WLNativeAPI(" + Build.DEVICE + "; " + Build.DISPLAY + "; " + Build.MODEL + "; SDK " + Build.VERSION.SDK + "; Android " + Build.VERSION.RELEASE + ")";
    if ((str1 != null) && (str1.indexOf("WLNativeAPI(") < 0))
      paramHttpClient.getParams().setParameter("http.useragent", str1 + str2);
    do
      return;
    while (str1 != null);
    paramHttpClient.getParams().setParameter("http.useragent", str2);
  }

  // ERROR //
  public void run()
  {
    // Byte code:
    //   0: new 89	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   7: ldc 146
    //   9: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   12: aload_0
    //   13: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   16: invokevirtual 34	com/worklight/wlclient/WLRequest:getPostRequest	()Lorg/apache/http/client/methods/HttpPost;
    //   19: invokevirtual 150	org/apache/http/client/methods/HttpPost:getURI	()Ljava/net/URI;
    //   22: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   25: invokevirtual 127	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   28: invokestatic 156	com/worklight/common/WLUtils:debug	(Ljava/lang/String;)V
    //   31: aload_0
    //   32: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   35: invokevirtual 160	com/worklight/wlclient/WLRequest:getConfig	()Lcom/worklight/common/WLConfig;
    //   38: invokestatic 166	com/worklight/wlclient/HttpClientFactory:getInstance	(Lcom/worklight/common/WLConfig;)Lorg/apache/http/impl/client/DefaultHttpClient;
    //   41: astore 4
    //   43: aload_0
    //   44: aload 4
    //   46: invokespecial 168	com/worklight/wlclient/InternalRequestSender:setUserAgentHeader	(Lorg/apache/http/client/HttpClient;)V
    //   49: aload_0
    //   50: aload 4
    //   52: invokespecial 170	com/worklight/wlclient/InternalRequestSender:setConnectionTimeout	(Lorg/apache/http/client/HttpClient;)V
    //   55: aload_0
    //   56: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   59: invokestatic 173	com/worklight/wlclient/WLCookieManager:addCookies	(Lcom/worklight/wlclient/WLRequest;)V
    //   62: aload_0
    //   63: invokespecial 175	com/worklight/wlclient/InternalRequestSender:addInstanceAuthHeader	()V
    //   66: new 177	com/worklight/wlclient/api/WLResponse
    //   69: dup
    //   70: aload 4
    //   72: aload_0
    //   73: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   76: invokevirtual 34	com/worklight/wlclient/WLRequest:getPostRequest	()Lorg/apache/http/client/methods/HttpPost;
    //   79: aload_0
    //   80: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   83: invokevirtual 181	com/worklight/wlclient/WLRequest:getHttpContext	()Lorg/apache/http/protocol/HttpContext;
    //   86: invokeinterface 185 3 0
    //   91: invokespecial 188	com/worklight/wlclient/api/WLResponse:<init>	(Lorg/apache/http/HttpResponse;)V
    //   94: astore 5
    //   96: aload 5
    //   98: aload_0
    //   99: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   102: invokevirtual 62	com/worklight/wlclient/WLRequest:getOptions	()Lcom/worklight/wlclient/api/WLRequestOptions;
    //   105: invokevirtual 192	com/worklight/wlclient/api/WLResponse:setOptions	(Lcom/worklight/wlclient/api/WLRequestOptions;)V
    //   108: aload_0
    //   109: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   112: aload 5
    //   114: invokevirtual 196	com/worklight/wlclient/WLRequest:requestFinished	(Lcom/worklight/wlclient/api/WLResponse;)V
    //   117: return
    //   118: astore_3
    //   119: aload_0
    //   120: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   123: invokevirtual 200	com/worklight/wlclient/WLRequest:getRequestListener	()Lcom/worklight/wlclient/WLRequestListener;
    //   126: new 202	com/worklight/wlclient/api/WLFailResponse
    //   129: dup
    //   130: getstatic 208	com/worklight/wlclient/api/WLErrorCode:REQUEST_TIMEOUT	Lcom/worklight/wlclient/api/WLErrorCode;
    //   133: getstatic 208	com/worklight/wlclient/api/WLErrorCode:REQUEST_TIMEOUT	Lcom/worklight/wlclient/api/WLErrorCode;
    //   136: invokevirtual 211	com/worklight/wlclient/api/WLErrorCode:getDescription	()Ljava/lang/String;
    //   139: aload_0
    //   140: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   143: invokevirtual 62	com/worklight/wlclient/WLRequest:getOptions	()Lcom/worklight/wlclient/api/WLRequestOptions;
    //   146: invokespecial 214	com/worklight/wlclient/api/WLFailResponse:<init>	(Lcom/worklight/wlclient/api/WLErrorCode;Ljava/lang/String;Lcom/worklight/wlclient/api/WLRequestOptions;)V
    //   149: invokeinterface 220 2 0
    //   154: return
    //   155: astore_2
    //   156: aload_0
    //   157: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   160: invokevirtual 200	com/worklight/wlclient/WLRequest:getRequestListener	()Lcom/worklight/wlclient/WLRequestListener;
    //   163: new 202	com/worklight/wlclient/api/WLFailResponse
    //   166: dup
    //   167: getstatic 223	com/worklight/wlclient/api/WLErrorCode:UNRESPONSIVE_HOST	Lcom/worklight/wlclient/api/WLErrorCode;
    //   170: getstatic 223	com/worklight/wlclient/api/WLErrorCode:UNRESPONSIVE_HOST	Lcom/worklight/wlclient/api/WLErrorCode;
    //   173: invokevirtual 211	com/worklight/wlclient/api/WLErrorCode:getDescription	()Ljava/lang/String;
    //   176: aload_0
    //   177: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   180: invokevirtual 62	com/worklight/wlclient/WLRequest:getOptions	()Lcom/worklight/wlclient/api/WLRequestOptions;
    //   183: invokespecial 214	com/worklight/wlclient/api/WLFailResponse:<init>	(Lcom/worklight/wlclient/api/WLErrorCode;Ljava/lang/String;Lcom/worklight/wlclient/api/WLRequestOptions;)V
    //   186: invokeinterface 220 2 0
    //   191: return
    //   192: astore_1
    //   193: aload_0
    //   194: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   197: invokevirtual 200	com/worklight/wlclient/WLRequest:getRequestListener	()Lcom/worklight/wlclient/WLRequestListener;
    //   200: new 202	com/worklight/wlclient/api/WLFailResponse
    //   203: dup
    //   204: getstatic 226	com/worklight/wlclient/api/WLErrorCode:UNEXPECTED_ERROR	Lcom/worklight/wlclient/api/WLErrorCode;
    //   207: getstatic 226	com/worklight/wlclient/api/WLErrorCode:UNEXPECTED_ERROR	Lcom/worklight/wlclient/api/WLErrorCode;
    //   210: invokevirtual 211	com/worklight/wlclient/api/WLErrorCode:getDescription	()Ljava/lang/String;
    //   213: aload_0
    //   214: getfield 15	com/worklight/wlclient/InternalRequestSender:request	Lcom/worklight/wlclient/WLRequest;
    //   217: invokevirtual 62	com/worklight/wlclient/WLRequest:getOptions	()Lcom/worklight/wlclient/api/WLRequestOptions;
    //   220: invokespecial 214	com/worklight/wlclient/api/WLFailResponse:<init>	(Lcom/worklight/wlclient/api/WLErrorCode;Ljava/lang/String;Lcom/worklight/wlclient/api/WLRequestOptions;)V
    //   223: invokeinterface 220 2 0
    //   228: return
    //   229: astore 8
    //   231: goto -38 -> 193
    //   234: astore 7
    //   236: goto -80 -> 156
    //   239: astore 6
    //   241: goto -122 -> 119
    //
    // Exception table:
    //   from	to	target	type
    //   31	96	118	java/net/SocketTimeoutException
    //   31	96	155	org/apache/http/conn/ConnectTimeoutException
    //   31	96	192	java/lang/Exception
    //   96	108	229	java/lang/Exception
    //   96	108	234	org/apache/http/conn/ConnectTimeoutException
    //   96	108	239	java/net/SocketTimeoutException
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.InternalRequestSender
 * JD-Core Version:    0.6.0
 */