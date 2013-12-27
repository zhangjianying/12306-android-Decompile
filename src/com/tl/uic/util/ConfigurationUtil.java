package com.tl.uic.util;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import java.util.Properties;

public final class ConfigurationUtil
{
  private static final String EMPTY_STRING = "";
  private static final String EXCEPTION_LOG_TITLE = "Key was ";
  private static final int MILLISECOND_CONVERTER = 1000;
  private static final int MINIMUM_BOOLEAN_SIZE = 3;
  private static volatile ConfigurationUtil _myInstance;
  private static Application application;
  private static Properties configurableItems;

  public static Boolean getBoolean(String paramString)
  {
    Object localObject = Boolean.valueOf(false);
    try
    {
      String str = getString(paramString);
      if (str.length() > 3)
      {
        Boolean localBoolean = Boolean.valueOf(Boolean.parseBoolean(str));
        localObject = localBoolean;
      }
      return localObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException, "Key was " + paramString);
    }
    return (Boolean)localObject;
  }

  private static Properties getConfigurationSettings(String paramString)
  {
    Properties localProperties = new Properties();
    if (application == null)
      return localProperties;
    try
    {
      localProperties.load(application.getResources().getAssets().open(paramString));
      return localProperties;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException, "Trying to load properties file: " + paramString);
    }
    return localProperties;
  }

  public static ConfigurationUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new ConfigurationUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static int getInt(String paramString)
  {
    int i = -1;
    try
    {
      String str = getString(paramString);
      if (str.length() > 0)
      {
        int j = Integer.parseInt(str);
        i = j;
      }
      return i;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException, "Key was " + paramString);
    }
    return i;
  }

  public static int getIntMS(String paramString)
  {
    return 1000 * getInt(paramString);
  }

  public static long getLong(String paramString)
  {
    long l1 = -1L;
    try
    {
      String str = getString(paramString);
      if (str.length() > 0)
      {
        long l2 = Long.parseLong(str);
        l1 = l2;
      }
      return l1;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException, "Key was " + paramString);
    }
    return l1;
  }

  public static long getLongMS(String paramString)
  {
    return 1000L * getLong(paramString);
  }

  public static String getString(String paramString)
  {
    try
    {
      String str = configurableItems.getProperty(paramString, "").trim();
      return str;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException, "Key was " + paramString);
    }
    return "";
  }

  public static void init(Application paramApplication)
  {
    application = paramApplication;
    configurableItems = getConfigurationSettings("TLFConfigurableItems.properties");
    if (configurableItems != null)
    {
      LogInternal.setIsDEBUG(getBoolean("DisplayLogging").booleanValue());
      LogInternal.log(configurableItems.toString());
    }
    CookieSyncManager.createInstance(application.getApplicationContext());
    CookieManager.getInstance().setAcceptCookie(true);
  }

  public static Boolean setProperty(String paramString1, String paramString2)
  {
    Boolean localBoolean = Boolean.valueOf(false);
    if (configurableItems.containsKey(paramString1))
    {
      configurableItems.setProperty(paramString1, paramString2);
      localBoolean = Boolean.valueOf(true);
    }
    return localBoolean;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.ConfigurationUtil
 * JD-Core Version:    0.6.0
 */