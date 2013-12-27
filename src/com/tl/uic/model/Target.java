package com.tl.uic.model;

import com.tl.uic.util.GCUtil;
import com.tl.uic.util.JsonUtil;
import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import java.util.HashMap;
import org.json.JSONObject;

public class Target
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 4998476519365613648L;
  private HashMap<String, String> currentState;
  private long dwell;
  private String id;
  private Position position;
  private HashMap<String, String> previousState;
  private String subType;
  private String tlType;
  private String type;
  private int visitedCount = -1;

  public final Boolean clean()
  {
    this.id = null;
    this.type = null;
    this.subType = null;
    if (this.position != null)
      this.position.clean();
    this.position = null;
    GCUtil.clean(this.previousState);
    this.previousState = null;
    GCUtil.clean(this.currentState);
    this.currentState = null;
    this.dwell = 0L;
    this.visitedCount = 0;
    this.tlType = null;
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    Target localTarget;
    do
    {
      return true;
      if (paramObject == null)
        return false;
      if (getClass() != paramObject.getClass())
        return false;
      localTarget = (Target)paramObject;
      if (this.id == null)
      {
        if (localTarget.id != null)
          return false;
      }
      else if (!this.id.equals(localTarget.id))
        return false;
      if (this.position == null)
      {
        if (localTarget.position != null)
          return false;
      }
      else if (!this.position.equals(localTarget.position))
        return false;
      if (this.subType == null)
      {
        if (localTarget.subType != null)
          return false;
      }
      else if (!this.subType.equals(localTarget.subType))
        return false;
      if (this.type == null)
      {
        if (localTarget.type != null)
          return false;
      }
      else if (!this.type.equals(localTarget.type))
        return false;
    }
    while (localTarget.visitedCount == this.visitedCount);
    return false;
  }

  public final HashMap<String, String> getCurrentState()
  {
    return this.currentState;
  }

  public final long getDwell()
  {
    return this.dwell;
  }

  public final String getId()
  {
    return this.id;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("id", getId());
      localJSONObject.put("type", getType());
      localJSONObject.put("subType", getSubType());
      Object localObject1;
      Object localObject2;
      if (getPosition() == null)
      {
        localObject1 = null;
        localJSONObject.put("position", localObject1);
        localJSONObject.put("prevState", JsonUtil.getHashValues(getPreviousState()));
        localJSONObject.put("currState", JsonUtil.getHashValues(getCurrentState()));
        if (getVisitedCount() > -1)
          localJSONObject.put("visitedCount", getVisitedCount());
        if (getDwell() > 0L)
          localJSONObject.put("dwell", getDwell());
        String str1 = getTlType();
        localObject2 = null;
        if (str1 != null)
          break label164;
      }
      while (true)
      {
        localJSONObject.put("tlType", localObject2);
        return localJSONObject;
        localObject1 = getPosition().getJSON();
        break;
        label164: String str2 = getTlType();
        localObject2 = str2;
      }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return (JSONObject)localJSONObject;
  }

  public final Position getPosition()
  {
    return this.position;
  }

  public final HashMap<String, String> getPreviousState()
  {
    return this.previousState;
  }

  public final String getSubType()
  {
    return this.subType;
  }

  public final String getTlType()
  {
    return this.tlType;
  }

  public final String getType()
  {
    return this.type;
  }

  public final int getVisitedCount()
  {
    return this.visitedCount;
  }

  public final int hashCode()
  {
    int i;
    int k;
    label26: int n;
    label44: int i1;
    int i2;
    if (this.id == null)
    {
      i = 0;
      int j = 31 * (i + 31);
      if (this.position != null)
        break label85;
      k = 0;
      int m = 31 * (j + k);
      if (this.subType != null)
        break label96;
      n = 0;
      i1 = 31 * (m + n);
      String str = this.type;
      i2 = 0;
      if (str != null)
        break label108;
    }
    while (true)
    {
      return i1 + i2;
      i = this.id.hashCode();
      break;
      label85: k = this.position.hashCode();
      break label26;
      label96: n = this.subType.hashCode();
      break label44;
      label108: i2 = this.type.hashCode();
    }
  }

  public final void setCurrentState(HashMap<String, String> paramHashMap)
  {
    this.currentState = paramHashMap;
  }

  public final void setDwell(long paramLong)
  {
    this.dwell = paramLong;
  }

  public final void setId(String paramString)
  {
    this.id = paramString;
  }

  public final void setPosition(Position paramPosition)
  {
    this.position = paramPosition;
  }

  public final void setPreviousState(HashMap<String, String> paramHashMap)
  {
    this.previousState = paramHashMap;
  }

  public final void setSubType(String paramString)
  {
    this.subType = paramString;
  }

  public final void setTlType(String paramString)
  {
    this.tlType = paramString;
  }

  public final void setType(String paramString)
  {
    this.type = paramString;
  }

  public final void setVisitedCount(int paramInt)
  {
    this.visitedCount = paramInt;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.Target
 * JD-Core Version:    0.6.0
 */