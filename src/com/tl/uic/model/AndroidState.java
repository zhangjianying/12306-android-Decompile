package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class AndroidState
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 1263057271140459227L;
  private KeyboardStateType keyboardStateType;

  public final Boolean clean()
  {
    this.keyboardStateType = null;
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    AndroidState localAndroidState;
    do
    {
      return true;
      if ((paramObject == null) || (getClass() != paramObject.getClass()))
        return false;
      localAndroidState = (AndroidState)paramObject;
    }
    while (this.keyboardStateType == localAndroidState.keyboardStateType);
    return false;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("keyboardState", getKeyboardStateType());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final KeyboardStateType getKeyboardStateType()
  {
    return this.keyboardStateType;
  }

  public final int hashCode()
  {
    if (this.keyboardStateType == null);
    for (int i = 0; ; i = this.keyboardStateType.hashCode())
      return i + 31;
  }

  public final void setKeyboardStateType(KeyboardStateType paramKeyboardStateType)
  {
    this.keyboardStateType = paramKeyboardStateType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.AndroidState
 * JD-Core Version:    0.6.0
 */