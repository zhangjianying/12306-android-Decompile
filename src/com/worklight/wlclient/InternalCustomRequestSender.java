package com.worklight.wlclient;

import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import java.net.SocketTimeoutException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;

class InternalCustomRequestSender
  implements Runnable
{
  HttpRequestBase httpRequest;
  WLResponseListener listener;
  int requestTimeoutInMilliseconds;

  protected InternalCustomRequestSender(HttpRequestBase paramHttpRequestBase, int paramInt, WLResponseListener paramWLResponseListener)
  {
    this.httpRequest = paramHttpRequestBase;
    this.requestTimeoutInMilliseconds = paramInt;
    this.listener = paramWLResponseListener;
  }

  public void run()
  {
    WLClient localWLClient = WLClient.getInstance();
    HttpClient localHttpClient = AsynchronousRequestSender.getHttpClient();
    if (this.requestTimeoutInMilliseconds > 0)
    {
      HttpConnectionParams.setSoTimeout(localHttpClient.getParams(), this.requestTimeoutInMilliseconds);
      HttpConnectionParams.setConnectionTimeout(localHttpClient.getParams(), this.requestTimeoutInMilliseconds);
    }
    try
    {
      WLResponse localWLResponse = new WLResponse(localHttpClient.execute(this.httpRequest, localWLClient.GetHttpContext()));
      this.listener.onSuccess(localWLResponse);
      return;
    }
    catch (SocketTimeoutException localSocketTimeoutException)
    {
      this.listener.onFailure(new WLFailResponse(WLErrorCode.REQUEST_TIMEOUT, WLErrorCode.REQUEST_TIMEOUT.getDescription(), new WLRequestOptions()));
      return;
    }
    catch (ConnectTimeoutException localConnectTimeoutException)
    {
      this.listener.onFailure(new WLFailResponse(WLErrorCode.UNRESPONSIVE_HOST, WLErrorCode.UNRESPONSIVE_HOST.getDescription(), new WLRequestOptions()));
      return;
    }
    catch (Exception localException)
    {
      this.listener.onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, WLErrorCode.UNEXPECTED_ERROR.getDescription(), new WLRequestOptions()));
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.InternalCustomRequestSender
 * JD-Core Version:    0.6.0
 */