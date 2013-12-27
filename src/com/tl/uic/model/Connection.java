package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class Connection extends ClientMessageHeader
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 4394765877843086798L;
  private long initTime;
  private long loadTime;
  private long responseDataSize;
  private long responseTime;
  private int statusCode;
  private String url;

  public Connection()
  {
    setMessageType(MessageType.CONNECTION);
  }

  public final Boolean clean()
  {
    super.clean();
    this.url = null;
    this.statusCode = 0;
    this.responseDataSize = 0L;
    this.initTime = 0L;
    this.responseTime = 0L;
    this.loadTime = 0L;
    return Boolean.valueOf(true);
  }

  public final long getInitTime()
  {
    return this.initTime;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject1 = null;
    JSONObject localJSONObject2 = new JSONObject();
    try
    {
      localJSONObject1 = super.getJSON();
      localJSONObject2.put("url", getUrl());
      localJSONObject2.put("statusCode", getStatusCode());
      localJSONObject2.put("responseDataSize", getResponseDataSize());
      localJSONObject2.put("initTime", getInitTime());
      localJSONObject2.put("responseTime", getResponseTime());
      localJSONObject2.put("loadTime", getLoadTime());
      localJSONObject1.put("connection", localJSONObject2);
      return localJSONObject1;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject1;
  }

  public final long getLoadTime()
  {
    return this.loadTime;
  }

  public final long getResponseDataSize()
  {
    return this.responseDataSize;
  }

  public final long getResponseTime()
  {
    return this.responseTime;
  }

  public final int getStatusCode()
  {
    return this.statusCode;
  }

  public final String getUrl()
  {
    return this.url;
  }

  public final void setInitTime(long paramLong)
  {
    this.initTime = paramLong;
  }

  public final void setLoadTime(long paramLong)
  {
    this.loadTime = paramLong;
  }

  public final void setResponseDataSize(long paramLong)
  {
    this.responseDataSize = paramLong;
  }

  public final void setResponseTime(long paramLong)
  {
    this.responseTime = paramLong;
  }

  public final void setStatusCode(int paramInt)
  {
    this.statusCode = paramInt;
  }

  public final void setUrl(String paramString)
  {
    this.url = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.Connection
 * JD-Core Version:    0.6.0
 */