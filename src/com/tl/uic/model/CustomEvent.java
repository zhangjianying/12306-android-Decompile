package com.tl.uic.model;

import com.tl.uic.util.GCUtil;
import com.tl.uic.util.JsonUtil;
import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import java.util.HashMap;
import org.json.JSONObject;

public class CustomEvent extends ClientMessageHeader
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -4501300217682609050L;
  private HashMap<String, String> data;
  private String name;

  public CustomEvent()
  {
    setMessageType(MessageType.CUSTOM_EVENT);
  }

  public CustomEvent(int paramInt, String paramString, HashMap<String, String> paramHashMap)
  {
    setMessageType(MessageType.CUSTOM_EVENT);
    setLogLevel(paramInt);
    this.data = paramHashMap;
    this.name = paramString;
  }

  public final Boolean clean()
  {
    super.clean();
    this.name = null;
    GCUtil.clean(this.data);
    this.data = null;
    return Boolean.valueOf(true);
  }

  public final HashMap<String, String> getData()
  {
    return this.data;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject1 = null;
    JSONObject localJSONObject2 = new JSONObject();
    try
    {
      localJSONObject1 = super.getJSON();
      localJSONObject2.put("name", getName());
      localJSONObject2.put("data", JsonUtil.getHashValues(getData()));
      localJSONObject1.put("customEvent", localJSONObject2);
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

  public final void setData(HashMap<String, String> paramHashMap)
  {
    this.data = paramHashMap;
  }

  public final void setName(String paramString)
  {
    this.name = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.CustomEvent
 * JD-Core Version:    0.6.0
 */