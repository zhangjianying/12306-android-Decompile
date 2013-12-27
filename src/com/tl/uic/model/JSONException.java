package com.tl.uic.model;

import com.tl.uic.util.JsonUtil;
import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import java.util.HashMap;
import org.json.JSONObject;

public class JSONException extends ClientMessageHeader
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 1817811642841780549L;
  private HashMap<String, String> data;
  private String description;
  private String name;
  private String stackTrace;

  public JSONException()
  {
    setMessageType(MessageType.EXCEPTION);
    setLogLevel(0);
  }

  public final Boolean clean()
  {
    super.clean();
    this.name = null;
    this.description = null;
    this.stackTrace = null;
    return Boolean.valueOf(true);
  }

  public final HashMap<String, String> getData()
  {
    return this.data;
  }

  public final String getDescription()
  {
    return this.description;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject1 = null;
    JSONObject localJSONObject2 = new JSONObject();
    try
    {
      localJSONObject1 = super.getJSON();
      localJSONObject2.put("name", getName());
      localJSONObject2.put("description", getDescription());
      localJSONObject2.put("stackTrace", getStackTrace());
      if (getData() != null)
        localJSONObject2.put("data", JsonUtil.getHashValues(getData()));
      localJSONObject1.put("exception", localJSONObject2);
      return localJSONObject1;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject1;
  }

  public final String getName()
  {
    return this.name;
  }

  public final String getStackTrace()
  {
    return this.stackTrace;
  }

  public final void setData(HashMap<String, String> paramHashMap)
  {
    this.data = paramHashMap;
  }

  public final void setDescription(String paramString)
  {
    this.description = paramString;
  }

  public final void setName(String paramString)
  {
    this.name = paramString;
  }

  public final void setStackTrace(String paramString)
  {
    this.stackTrace = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.JSONException
 * JD-Core Version:    0.6.0
 */