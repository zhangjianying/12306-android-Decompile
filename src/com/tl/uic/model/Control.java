package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class Control extends ClientMessageHeader
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -859543818892459798L;
  private EventInfo eventInfo;
  private long focusInOffset;
  private Target target;

  public Control()
  {
    setMessageType(MessageType.CONTROL);
  }

  public final Boolean clean()
  {
    super.clean();
    this.target.clean();
    this.target = null;
    this.eventInfo.clean();
    this.eventInfo = null;
    this.focusInOffset = 0L;
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    Control localControl;
    do
      while (true)
      {
        return true;
        if (!super.equals(paramObject))
          return false;
        if (getClass() != paramObject.getClass())
          return false;
        localControl = (Control)paramObject;
        if (this.eventInfo == null)
        {
          if (localControl.eventInfo != null)
            return false;
        }
        else if (!this.eventInfo.equals(localControl.eventInfo))
          return false;
        if (this.target != null)
          break;
        if (localControl.target != null)
          return false;
      }
    while (this.target.equals(localControl.target));
    return false;
  }

  public final EventInfo getEventInfo()
  {
    return this.eventInfo;
  }

  public final long getFocusInOffset()
  {
    return this.focusInOffset;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = null;
    try
    {
      localJSONObject = super.getJSON();
      if (getFocusInOffset() > 0L)
        localJSONObject.put("focusInOffset", getFocusInOffset());
      localJSONObject.put("target", getTarget().getJSON());
      if (getEventInfo() != null)
        localJSONObject.put("event", getEventInfo().getJSON());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final Target getTarget()
  {
    return this.target;
  }

  public final int hashCode()
  {
    int i = 31 * super.hashCode();
    int j;
    int k;
    int m;
    if (this.eventInfo == null)
    {
      j = 0;
      k = 31 * (i + j);
      Target localTarget = this.target;
      m = 0;
      if (localTarget != null)
        break label54;
    }
    while (true)
    {
      return k + m;
      j = this.eventInfo.hashCode();
      break;
      label54: m = this.target.hashCode();
    }
  }

  public final void setEventInfo(EventInfo paramEventInfo)
  {
    this.eventInfo = paramEventInfo;
  }

  public final void setFocusInOffset(long paramLong)
  {
    this.focusInOffset = paramLong;
  }

  public final void setTarget(Target paramTarget)
  {
    this.target = paramTarget;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.Control
 * JD-Core Version:    0.6.0
 */