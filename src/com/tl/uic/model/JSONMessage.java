package com.tl.uic.model;

import java.io.Serializable;
import org.json.JSONObject;

public class JSONMessage
  implements Serializable
{
  private static final long serialVersionUID = 3976757161355685264L;
  private String jsonData;
  private int logLevel;
  private long size;

  public JSONMessage()
  {
    this.logLevel = 0;
    this.jsonData = "";
    this.size = 0L;
  }

  public JSONMessage(ClientMessageHeader paramClientMessageHeader)
  {
    this.logLevel = paramClientMessageHeader.getLogLevel();
    this.jsonData = paramClientMessageHeader.getJSON().toString();
    this.size = 0L;
  }

  public final Boolean clean()
  {
    this.jsonData = null;
    this.logLevel = 0;
    this.size = 0L;
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (paramObject == null);
    JSONMessage localJSONMessage;
    do
      while (true)
      {
        return false;
        if (this == paramObject)
          return true;
        if (getClass() != paramObject.getClass())
          continue;
        localJSONMessage = (JSONMessage)paramObject;
        if (this.jsonData != null)
          break;
        if (localJSONMessage.jsonData != null)
          continue;
        if (this.logLevel == localJSONMessage.logLevel)
          return true;
      }
    while (this.jsonData.equals(localJSONMessage.jsonData));
    return false;
  }

  public final String getJsonData()
  {
    return this.jsonData;
  }

  public final int getLogLevel()
  {
    return this.logLevel;
  }

  public final long getSize()
  {
    return this.size;
  }

  public final int hashCode()
  {
    if (this.jsonData == null);
    for (int i = 0; ; i = this.jsonData.hashCode())
      return 31 * (i + 31) + this.logLevel;
  }

  public final void setJsonData(String paramString)
  {
    this.jsonData = paramString;
  }

  public final void setLogLevel(int paramInt)
  {
    this.logLevel = paramInt;
  }

  public final void setSize(long paramLong)
  {
    this.size = paramLong;
  }

  public final String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("JSONMessage [jsonData=");
    localStringBuffer.append(this.jsonData);
    localStringBuffer.append(", logLevel=");
    localStringBuffer.append(this.logLevel);
    localStringBuffer.append(", size=");
    localStringBuffer.append(this.size);
    localStringBuffer.append(']');
    return localStringBuffer.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.JSONMessage
 * JD-Core Version:    0.6.0
 */