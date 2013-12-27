package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class EventInfo
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 7080612713953252184L;
  private String subType;
  private String tlEvent;
  private String type;

  public final Boolean clean()
  {
    this.type = null;
    this.subType = null;
    this.tlEvent = null;
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    EventInfo localEventInfo;
    do
      while (true)
      {
        return true;
        if (paramObject == null)
          return false;
        if (getClass() != paramObject.getClass())
          return false;
        localEventInfo = (EventInfo)paramObject;
        if (this.subType == null)
        {
          if (localEventInfo.subType != null)
            return false;
        }
        else if (!this.subType.equals(localEventInfo.subType))
          return false;
        if (this.type != null)
          break;
        if (localEventInfo.type != null)
          return false;
      }
    while (this.type.equals(localEventInfo.type));
    return false;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("type", getType());
      if (getSubType() != null)
        localJSONObject.put("subType", getSubType());
      localJSONObject.put("tlEvent", getTlEvent());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final String getSubType()
  {
    return this.subType;
  }

  public final String getTlEvent()
  {
    return this.tlEvent;
  }

  public final String getType()
  {
    return this.type;
  }

  public final int hashCode()
  {
    int i;
    int j;
    int k;
    if (this.subType == null)
    {
      i = 0;
      j = 31 * (i + 31);
      String str = this.type;
      k = 0;
      if (str != null)
        break label45;
    }
    while (true)
    {
      return j + k;
      i = this.subType.hashCode();
      break;
      label45: k = this.type.hashCode();
    }
  }

  public final void setSubType(String paramString)
  {
    this.subType = paramString;
  }

  public final void setTlEvent(String paramString)
  {
    this.tlEvent = paramString;
  }

  public final void setType(String paramString)
  {
    this.type = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.EventInfo
 * JD-Core Version:    0.6.0
 */