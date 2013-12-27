package com.worklight.wlclient.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class WLRequestOptions
{
  private boolean fromChallenge;
  private ArrayList<Header> headers = new ArrayList();
  private Object invocationContext;
  private Map<String, String> parameters = new HashMap();
  private WLResponseListener responseListener;
  private int timeout = 10000;

  void addHeader(String paramString1, String paramString2)
  {
    addHeader(new BasicHeader(paramString1, paramString2));
  }

  void addHeader(Header paramHeader)
  {
    if (paramHeader == null)
      return;
    this.headers.add(paramHeader);
  }

  void addParameter(String paramString1, String paramString2)
  {
    this.parameters.put(paramString1, paramString2);
  }

  void addParameters(Map<String, String> paramMap)
  {
    this.parameters.putAll(paramMap);
  }

  public ArrayList<Header> getHeaders()
  {
    return this.headers;
  }

  public Object getInvocationContext()
  {
    return this.invocationContext;
  }

  String getParameter(String paramString)
  {
    if (this.parameters != null)
      return (String)this.parameters.get(paramString);
    return "";
  }

  public Map<String, String> getParameters()
  {
    return this.parameters;
  }

  WLResponseListener getResponseListener()
  {
    return this.responseListener;
  }

  public int getTimeout()
  {
    return this.timeout;
  }

  public boolean isFromChallenge()
  {
    return this.fromChallenge;
  }

  public void setFromChallenge(boolean paramBoolean)
  {
    this.fromChallenge = paramBoolean;
  }

  void setHeaders(ArrayList<Header> paramArrayList)
  {
    this.headers = paramArrayList;
  }

  public void setInvocationContext(Object paramObject)
  {
    this.invocationContext = paramObject;
  }

  void setResponseListener(WLResponseListener paramWLResponseListener)
  {
    this.responseListener = paramWLResponseListener;
  }

  public void setTimeout(int paramInt)
  {
    this.timeout = paramInt;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLRequestOptions
 * JD-Core Version:    0.6.0
 */