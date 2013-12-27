package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessageFormat
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 7470897849248685953L;
  private String messageVersion;
  private int serialNumber;
  private Session session;

  public final Boolean clean()
  {
    this.messageVersion = null;
    this.serialNumber = 0;
    if (this.session != null)
      this.session.clean();
    this.session = null;
    return Boolean.valueOf(true);
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("messageVersion", getMessageVersion());
      localJSONObject.put("serialNumber", getSerialNumber());
      JSONArray localJSONArray = new JSONArray();
      localJSONArray.put(getSession().getJSON());
      localJSONObject.put("sessions", localJSONArray);
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final String getMessageVersion()
  {
    return this.messageVersion;
  }

  public final int getSerialNumber()
  {
    return this.serialNumber;
  }

  public final Session getSession()
  {
    return this.session;
  }

  public final void setMessageVersion(String paramString)
  {
    this.messageVersion = paramString;
  }

  public final void setSerialNumber(int paramInt)
  {
    this.serialNumber = paramInt;
  }

  public final void setSession(Session paramSession)
  {
    this.session = paramSession;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.MessageFormat
 * JD-Core Version:    0.6.0
 */