package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class ClientEnvironment
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -5008152574755865321L;
  private int height;
  private MobileEnvironment mobileEnvironment;
  private String osVersion;
  private int width;

  public final Boolean clean()
  {
    this.osVersion = null;
    this.height = 0;
    this.width = 0;
    if (this.mobileEnvironment != null)
      this.mobileEnvironment.clean();
    this.mobileEnvironment = null;
    return Boolean.valueOf(true);
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
      localJSONObject.put("osVersion", getOsVersion());
      localJSONObject.put("height", getHeight());
      localJSONObject.put("width", getWidth());
      localJSONObject.put("mobileEnvironment", getMobileEnvironment().getJSON());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final MobileEnvironment getMobileEnvironment()
  {
    return this.mobileEnvironment;
  }

  public final String getOsVersion()
  {
    return this.osVersion;
  }

  public final int getWidth()
  {
    return this.width;
  }

  public final void setHeight(int paramInt)
  {
    this.height = paramInt;
  }

  public final void setMobileEnvironment(MobileEnvironment paramMobileEnvironment)
  {
    this.mobileEnvironment = paramMobileEnvironment;
  }

  public final void setOsVersion(String paramString)
  {
    this.osVersion = paramString;
  }

  public final void setWidth(int paramInt)
  {
    this.width = paramInt;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.ClientEnvironment
 * JD-Core Version:    0.6.0
 */