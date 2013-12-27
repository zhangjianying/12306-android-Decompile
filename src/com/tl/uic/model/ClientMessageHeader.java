package com.tl.uic.model;

import com.tl.uic.TLFCache;
import com.tl.uic.Tealeaf;
import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class ClientMessageHeader
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 876537710682369925L;
  private Boolean fromWeb = Boolean.valueOf(false);
  private int logLevel;
  private MessageType messageType;
  private long offset = TLFCache.timestampFromSession();
  private long screenviewOffset = Tealeaf.getApplicationScreenviewOffset();

  public Boolean clean()
  {
    this.messageType = null;
    this.offset = 0L;
    this.logLevel = 0;
    this.screenviewOffset = 0L;
    this.fromWeb = null;
    return Boolean.valueOf(true);
  }

  public boolean equals(Object paramObject)
  {
    int i = 1;
    if ((paramObject == null) || (getClass() != paramObject.getClass()))
      i = 0;
    ClientMessageHeader localClientMessageHeader;
    do
    {
      do
        return i;
      while (this == paramObject);
      localClientMessageHeader = (ClientMessageHeader)paramObject;
      if (this.screenviewOffset != localClientMessageHeader.screenviewOffset)
        return false;
      if (this.logLevel != localClientMessageHeader.logLevel)
        return false;
      if (this.messageType != localClientMessageHeader.messageType)
        return false;
    }
    while (this.offset == localClientMessageHeader.offset);
    return false;
  }

  public final Boolean getFromWeb()
  {
    return this.fromWeb;
  }

  public JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("fromWeb", getFromWeb());
      localJSONObject.put("type", getMessageType().getValue());
      localJSONObject.put("offset", getOffset());
      localJSONObject.put("screenviewOffset", getScreenviewOffset());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final int getLogLevel()
  {
    return this.logLevel;
  }

  public final MessageType getMessageType()
  {
    return this.messageType;
  }

  public final long getOffset()
  {
    return this.offset;
  }

  public final long getScreenviewOffset()
  {
    return this.screenviewOffset;
  }

  public int hashCode()
  {
    int i = 31 * (31 * (31 + (int)(this.screenviewOffset ^ this.screenviewOffset >>> 32)) + this.logLevel);
    if (this.messageType == null);
    for (int j = 0; ; j = this.messageType.hashCode())
      return 31 * (i + j) + (int)(this.offset ^ this.offset >>> 32);
  }

  public final void setFromWeb(Boolean paramBoolean)
  {
    this.fromWeb = paramBoolean;
  }

  public final void setLogLevel(int paramInt)
  {
    this.logLevel = paramInt;
  }

  public final void setMessageType(MessageType paramMessageType)
  {
    this.messageType = paramMessageType;
  }

  public final void setOffset(long paramLong)
  {
    this.offset = paramLong;
  }

  public final void setScreenviewOffset(long paramLong)
  {
    this.screenviewOffset = paramLong;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.ClientMessageHeader
 * JD-Core Version:    0.6.0
 */