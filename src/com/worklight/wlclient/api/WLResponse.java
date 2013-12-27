package com.worklight.wlclient.api;

import com.worklight.common.WLUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONException;
import org.json.JSONObject;

public class WLResponse
{
  private Header[] headers;
  private HttpResponse httpResponseCache;
  private WLRequestOptions requestOptions;
  private JSONObject responseJSON;
  protected String responseText;
  private int status;

  WLResponse(int paramInt, String paramString, WLRequestOptions paramWLRequestOptions)
  {
    this.status = paramInt;
    this.requestOptions = paramWLRequestOptions;
    this.responseText = paramString;
    responseTextToJSON(paramString);
  }

  WLResponse(WLResponse paramWLResponse)
  {
    this.status = paramWLResponse.status;
    this.requestOptions = paramWLResponse.requestOptions;
    this.responseText = paramWLResponse.responseText;
    this.responseJSON = paramWLResponse.responseJSON;
    this.httpResponseCache = paramWLResponse.httpResponseCache;
  }

  public WLResponse(HttpResponse paramHttpResponse)
  {
    this.status = paramHttpResponse.getStatusLine().getStatusCode();
    this.headers = paramHttpResponse.getAllHeaders();
    this.httpResponseCache = paramHttpResponse;
    try
    {
      if (paramHttpResponse.getHeaders("Content-Encoding").length > 0)
      {
        WLUtils.error("Content encoding is " + paramHttpResponse.getHeaders("Content-Encoding")[0].getValue());
        if (paramHttpResponse.getHeaders("Content-Encoding")[0].getValue().equalsIgnoreCase("gzip"))
        {
          this.responseText = WLUtils.convertGZIPStreamToString(paramHttpResponse.getEntity().getContent());
          WLUtils.log("Content encoding is gzip", 0);
        }
        while (true)
        {
          responseTextToJSON(this.responseText);
          return;
          this.responseText = WLUtils.convertStreamToString(paramHttpResponse.getEntity().getContent());
        }
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        WLUtils.error("Response from Worklight server failed because could not read text from response " + localException.getMessage(), localException);
        continue;
        WLUtils.error("No Content-Encoding header");
        this.responseText = WLUtils.convertStreamToString(paramHttpResponse.getEntity().getContent());
      }
    }
  }

  private void responseTextToJSON(String paramString)
  {
    int i = this.responseText.indexOf('{');
    int j = this.responseText.lastIndexOf('}');
    if ((i == -1) || (j == -1))
    {
      this.responseJSON = null;
      return;
    }
    String str = this.responseText.substring(i, j + 1);
    try
    {
      this.responseJSON = new JSONObject(str);
      return;
    }
    catch (JSONException localJSONException)
    {
      WLUtils.error("Response from Worklight server failed because could not read JSON from response with text " + str, localJSONException);
      this.responseJSON = null;
    }
  }

  public Header getHeader(String paramString)
  {
    for (int i = 0; i < this.headers.length; i++)
      if (this.headers[i].getName().equalsIgnoreCase(paramString))
        return this.headers[i];
    return null;
  }

  public Header[] getHeaders()
  {
    return this.headers;
  }

  public Object getInvocationContext()
  {
    return this.requestOptions.getInvocationContext();
  }

  WLRequestOptions getOptions()
  {
    return this.requestOptions;
  }

  public JSONObject getResponseJSON()
  {
    return this.responseJSON;
  }

  public String getResponseText()
  {
    return this.responseText;
  }

  public int getStatus()
  {
    return this.status;
  }

  void setInvocationContext(Object paramObject)
  {
    this.requestOptions.setInvocationContext(paramObject);
  }

  public void setOptions(WLRequestOptions paramWLRequestOptions)
  {
    this.requestOptions = paramWLRequestOptions;
  }

  void setResponseHeader(String paramString1, String paramString2)
  {
    this.httpResponseCache.setHeader(paramString1, paramString2);
    this.headers = this.httpResponseCache.getAllHeaders();
  }

  public String toString()
  {
    return "WLResponse [invocationContext=" + this.requestOptions.getInvocationContext() + ", responseText=" + this.responseText + ", status=" + this.status + "]";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLResponse
 * JD-Core Version:    0.6.0
 */