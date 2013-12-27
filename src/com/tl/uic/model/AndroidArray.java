package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class AndroidArray
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 5789545949290791966L;
  private String brand;
  private String fingerPrint;
  private KeyboardType keyboardType;

  public final Boolean clean()
  {
    this.brand = null;
    this.fingerPrint = null;
    this.keyboardType = null;
    return Boolean.valueOf(true);
  }

  public final String getBrand()
  {
    return this.brand;
  }

  public final String getFingerPrint()
  {
    return this.fingerPrint;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("brand", getBrand());
      localJSONObject.put("fingerPrint", getFingerPrint());
      localJSONObject.put("keyboardType", getKeyboardType());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final KeyboardType getKeyboardType()
  {
    return this.keyboardType;
  }

  public final void setBrand(String paramString)
  {
    this.brand = paramString;
  }

  public final void setFingerPrint(String paramString)
  {
    this.fingerPrint = paramString;
  }

  public final void setKeyboardType(KeyboardType paramKeyboardType)
  {
    this.keyboardType = paramKeyboardType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.AndroidArray
 * JD-Core Version:    0.6.0
 */