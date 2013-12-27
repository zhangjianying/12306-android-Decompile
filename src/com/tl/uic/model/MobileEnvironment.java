package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class MobileEnvironment
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -4663909733862705899L;
  private AndroidArray androidArray;
  private String appName;
  private String appVersion;
  private String deviceModel;
  private String language;
  private String locale;
  private String manufacturer;
  private OrientationType orientationType;
  private long totalMemory;
  private long totalStorage;
  private String userID;

  public final Boolean clean()
  {
    this.totalStorage = 0L;
    this.totalMemory = 0L;
    this.locale = null;
    this.language = null;
    this.manufacturer = null;
    this.deviceModel = null;
    this.appName = null;
    this.appVersion = null;
    this.userID = null;
    this.orientationType = null;
    this.androidArray.clean();
    this.androidArray = null;
    return Boolean.valueOf(true);
  }

  public final AndroidArray getAndroidArray()
  {
    return this.androidArray;
  }

  public final String getAppName()
  {
    return this.appName;
  }

  public final String getAppVersion()
  {
    return this.appVersion;
  }

  public final String getDeviceModel()
  {
    return this.deviceModel;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("totalStorage", getTotalStorage());
      localJSONObject.put("totalMemory", getTotalMemory());
      localJSONObject.put("locale", getLocale());
      localJSONObject.put("language", getLanguage());
      localJSONObject.put("manufacturer", getManufacturer());
      localJSONObject.put("deviceModel", getDeviceModel());
      localJSONObject.put("appName", getAppName());
      localJSONObject.put("appVersion", getAppVersion());
      localJSONObject.put("userId", getUserID());
      localJSONObject.put("orientationType", getOrientationType());
      localJSONObject.put("android", getAndroidArray().getJSON());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final String getLanguage()
  {
    return this.language;
  }

  public final String getLocale()
  {
    return this.locale;
  }

  public final String getManufacturer()
  {
    return this.manufacturer;
  }

  public final OrientationType getOrientationType()
  {
    return this.orientationType;
  }

  public final long getTotalMemory()
  {
    return this.totalMemory;
  }

  public final long getTotalStorage()
  {
    return this.totalStorage;
  }

  public final String getUserID()
  {
    return this.userID;
  }

  public final void setAndroidArray(AndroidArray paramAndroidArray)
  {
    this.androidArray = paramAndroidArray;
  }

  public final void setAppName(String paramString)
  {
    this.appName = paramString;
  }

  public final void setAppVersion(String paramString)
  {
    this.appVersion = paramString;
  }

  public final void setDeviceModel(String paramString)
  {
    this.deviceModel = paramString;
  }

  public final void setLanguage(String paramString)
  {
    this.language = paramString;
  }

  public final void setLocale(String paramString)
  {
    this.locale = paramString;
  }

  public final void setManufacturer(String paramString)
  {
    this.manufacturer = paramString;
  }

  public final void setOrientationType(OrientationType paramOrientationType)
  {
    this.orientationType = paramOrientationType;
  }

  public final void setTotalMemory(long paramLong)
  {
    this.totalMemory = paramLong;
  }

  public final void setTotalStorage(long paramLong)
  {
    this.totalStorage = paramLong;
  }

  public final void setUserID(String paramString)
  {
    this.userID = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.MobileEnvironment
 * JD-Core Version:    0.6.0
 */