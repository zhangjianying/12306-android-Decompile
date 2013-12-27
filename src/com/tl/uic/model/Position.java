package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class Position
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -6298612983396630564L;
  private int height;
  private int width;
  private int x;
  private int y;

  public final Boolean clean()
  {
    this.x = 0;
    this.y = 0;
    this.width = 0;
    this.height = 0;
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    Position localPosition;
    do
    {
      return true;
      if (paramObject == null)
        return false;
      if (getClass() != paramObject.getClass())
        return false;
      localPosition = (Position)paramObject;
      if (this.height != localPosition.height)
        return false;
      if (this.width != localPosition.width)
        return false;
      if (this.x != localPosition.x)
        return false;
    }
    while (this.y == localPosition.y);
    return false;
  }

  public final int getHeight()
  {
    return this.height;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("x", getX());
      localJSONObject.put("y", getY());
      localJSONObject.put("width", getWidth());
      localJSONObject.put("height", getHeight());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final int getWidth()
  {
    return this.width;
  }

  public final int getX()
  {
    return this.x;
  }

  public final int getY()
  {
    return this.y;
  }

  public final int hashCode()
  {
    return 31 * (31 * (31 * (31 + this.height) + this.width) + this.x) + this.y;
  }

  public final void setHeight(int paramInt)
  {
    this.height = paramInt;
  }

  public final void setWidth(int paramInt)
  {
    this.width = paramInt;
  }

  public final void setX(int paramInt)
  {
    this.x = paramInt;
  }

  public final void setY(int paramInt)
  {
    this.y = paramInt;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.Position
 * JD-Core Version:    0.6.0
 */