package com.tl.uic.model;

import com.tl.uic.util.GCUtil;
import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Session
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -5461457368043986640L;
  private ClientEnvironment clientEnvironment;
  private List<JSONMessage> messages = new CopyOnWriteArrayList();
  private String sessionID;
  private long sessionStartTime;

  public final Boolean clean()
  {
    return Boolean.valueOf(true);
  }

  public final Boolean cleanMessages()
  {
    return GCUtil.clean(this.messages);
  }

  public final ClientEnvironment getClientEnvironment()
  {
    return this.clientEnvironment;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", getSessionID());
      localJSONObject.put("startTime", getSessionStartTime());
      JSONArray localJSONArray = new JSONArray();
      Iterator localIterator = getMessages().iterator();
      while (true)
      {
        if (!localIterator.hasNext())
        {
          localJSONObject.put("messages", localJSONArray);
          localJSONObject.put("clientEnvironment", getClientEnvironment().getJSON());
          return localJSONObject;
        }
        localJSONArray.put(new JSONObject(((JSONMessage)localIterator.next()).getJsonData()));
      }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final List<JSONMessage> getMessages()
  {
    return this.messages;
  }

  public final String getSessionID()
  {
    return this.sessionID;
  }

  public final long getSessionStartTime()
  {
    return this.sessionStartTime;
  }

  public final void setClientEnvironment(ClientEnvironment paramClientEnvironment)
  {
    this.clientEnvironment = paramClientEnvironment;
  }

  public final void setMessages(List<JSONMessage> paramList)
  {
    this.messages = paramList;
  }

  public final void setSessionID(String paramString)
  {
    this.sessionID = paramString;
  }

  public final void setSessionStartTime(long paramLong)
  {
    this.sessionStartTime = paramLong;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.Session
 * JD-Core Version:    0.6.0
 */