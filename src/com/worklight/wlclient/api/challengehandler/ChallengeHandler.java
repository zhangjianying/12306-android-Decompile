package com.worklight.wlclient.api.challengehandler;

import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;
import com.worklight.wlclient.AsynchronousRequestSender;
import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLProcedureInvocationData;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public abstract class ChallengeHandler extends BaseChallengeHandler<WLResponse>
  implements WLResponseListener
{
  public ChallengeHandler(String paramString)
  {
    super(paramString);
  }

  private void addGetParams(HttpGet paramHttpGet, Map<String, String> paramMap)
  {
    BasicHttpParams localBasicHttpParams = new BasicHttpParams();
    localBasicHttpParams.setParameter("isAjaxRequest", "true");
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localBasicHttpParams.setParameter(str, paramMap.get(str));
    }
    paramHttpGet.setParams(localBasicHttpParams);
  }

  private void addPostParams(HttpPost paramHttpPost, Map<String, String> paramMap)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new BasicNameValuePair("isAjaxRequest", "true"));
    if (paramMap != null)
    {
      Iterator localIterator = paramMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localArrayList.add(new BasicNameValuePair(str, (String)paramMap.get(str)));
      }
    }
    try
    {
      UrlEncodedFormEntity localUrlEncodedFormEntity = new UrlEncodedFormEntity(localArrayList, "UTF-8");
      paramHttpPost.setEntity(localUrlEncodedFormEntity);
      return;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      WLUtils.error(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
    }
    throw new RuntimeException(localUnsupportedEncodingException);
  }

  public abstract boolean isCustomResponse(WLResponse paramWLResponse);

  public void submitAdapterAuthentication(WLProcedureInvocationData paramWLProcedureInvocationData, WLRequestOptions paramWLRequestOptions)
  {
    if (paramWLRequestOptions == null)
      paramWLRequestOptions = new WLRequestOptions();
    paramWLRequestOptions.setFromChallenge(true);
    WLClient.getInstance().sendInvoke(paramWLProcedureInvocationData, this, paramWLRequestOptions);
  }

  protected void submitLoginForm(String paramString1, Map<String, String> paramMap1, Map<String, String> paramMap2, int paramInt, String paramString2)
  {
    WLUtils.debug("Request [login]");
    WLClient localWLClient = WLClient.getInstance();
    String str2;
    Object localObject;
    if ((paramString1.indexOf("http") == 0) && (paramString1.indexOf(":") > 0))
    {
      str2 = paramString1;
      if (!paramString2.equalsIgnoreCase("post"))
        break label214;
      localObject = new HttpPost(str2);
      addPostParams((HttpPost)localObject, paramMap1);
    }
    while (true)
    {
      ((HttpRequestBase)localObject).addHeader("x-wl-app-version", localWLClient.getConfig().getApplicationVersion());
      if (paramMap2 == null)
        break label258;
      Iterator localIterator = paramMap2.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str3 = (String)localIterator.next();
        ((HttpRequestBase)localObject).addHeader(str3, (String)paramMap2.get(str3));
      }
      String str1 = localWLClient.getConfig().getAppURL().toExternalForm();
      if ((str1.charAt(-1 + str1.length()) == '/') && (paramString1.length() > 0) && (paramString1.charAt(0) == '/'))
        paramString1 = paramString1.substring(1);
      str2 = str1 + paramString1;
      break;
      label214: if (!paramString2.equalsIgnoreCase("get"))
        break label248;
      localObject = new HttpGet(str2);
      addGetParams((HttpGet)localObject, paramMap1);
    }
    label248: throw new RuntimeException("CustomChallengeHandler.submitLoginForm: invalid request method.");
    label258: AsynchronousRequestSender.getInstance().sendCustomRequestAsync((HttpRequestBase)localObject, paramInt, this);
  }

  protected void submitSuccess(WLResponse paramWLResponse)
  {
    monitorenter;
    try
    {
      this.activeRequest.removeExpectedAnswer(getRealm());
      this.activeRequest = null;
      releaseWaitingList();
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.challengehandler.ChallengeHandler
 * JD-Core Version:    0.6.0
 */