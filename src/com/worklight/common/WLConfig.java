package com.worklight.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class WLConfig
{
  public static final String ENABLE_SETTINGS = "enableSettings";
  private static final String IGNORED_FILE_EXTENSIONS = "ignoredFileExtensions";
  public static final String WL_APP_ID = "wlAppId";
  public static final String WL_APP_VERSION = "wlAppVersion";
  public static final String WL_CLIENT_PROPS_NAME = "wlclient.properties";
  private static final String WL_ENVIRONMENT = "wlEnvironment";
  public static final String WL_GCM_SENDER = "GcmSenderId";
  public static final String WL_MAIN_FILE_PATH = "wlMainFilePath";
  public static final String WL_PLATFORM_VERSION = "wlPlatformVersion";
  private static final String WL_PREFS = "WLPrefs";
  public static final String WL_SERVER_CONTEXT = "wlServerContext";
  public static final String WL_SERVER_HOST = "wlServerHost";
  public static final String WL_SERVER_PORT = "wlServerPort";
  public static final String WL_SERVER_PROTOCOL = "wlServerProtocol";
  private static final String WL_TEST_WEB_RESOURCES_CHECKSUM = "testWebResourcesChecksum";
  public static final String WL_WEB_RESOURCES_UNPACKD_SIZE = "webResourcesSize";
  public static final String WL_X_VERSION_HEADER = "x-wl-app-version";
  private SharedPreferences prefs = null;
  private Properties wlProperties = new Properties();

  public WLConfig(Activity paramActivity)
  {
    this(paramActivity.getApplication());
  }

  public WLConfig(Context paramContext)
  {
    try
    {
      this.wlProperties.load(paramContext.getAssets().open("wlclient.properties"));
      this.prefs = paramContext.getSharedPreferences("WLPrefs", 0);
      return;
    }
    catch (IOException localIOException)
    {
    }
    throw new RuntimeException("WLConfig(): Can't load wlclient.properties file");
  }

  private String getPropertyOrPref(String paramString1, String paramString2)
  {
    String str = this.prefs.getString(paramString2, null);
    if (str == null)
      str = (String)this.wlProperties.get(paramString1);
    return str;
  }

  public String getAppId()
  {
    return getPropertyOrPref("wlAppId", "appIdPref");
  }

  public URL getAppURL()
  {
    try
    {
      URL localURL = new URL(getRootURL() + "/apps/services/api/" + getAppId() + "/" + getWLEnvironment() + "/");
      return localURL;
    }
    catch (MalformedURLException localMalformedURLException)
    {
    }
    throw new RuntimeException("Could not parse URL; check assets/wlclient.properties. " + localMalformedURLException.getMessage(), localMalformedURLException);
  }

  public String getApplicationVersion()
  {
    return getPropertyOrPref("wlAppVersion", "appVersionPref");
  }

  public String getContext()
  {
    return (String)this.wlProperties.get("wlServerContext");
  }

  public String getDefaultRootUrl()
  {
    int i;
    String str1;
    if (("https".equalsIgnoreCase(getProtocol())) && ("443".equals(getPort())))
    {
      i = 1;
      if ((!WLUtils.isStringEmpty(getPort())) && (i == 0))
        break label142;
      str1 = "";
      label43: if ((!WLUtils.isStringEmpty(getContext())) && (!getContext().equals("/")))
        break label168;
    }
    label142: label168: for (String str2 = ""; ; str2 = getContext())
    {
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = getProtocol();
      arrayOfObject[1] = getHost();
      arrayOfObject[2] = str1;
      arrayOfObject[3] = str2;
      String str3 = String.format("%s://%s%s%s", arrayOfObject);
      if (str3.endsWith("/"))
        str3 = str3.substring(0, -1 + str3.length());
      return str3;
      i = 0;
      break;
      str1 = ":" + getPort();
      break label43;
    }
  }

  public String getGCMSender()
  {
    String str = this.wlProperties.getProperty("GcmSenderId");
    if (str != null)
      str = str.trim();
    return str;
  }

  public String getHost()
  {
    return (String)this.wlProperties.get("wlServerHost");
  }

  public String getMainFileFromDescriptor()
  {
    return (String)this.wlProperties.get("wlMainFilePath");
  }

  public String getMainFilePath()
  {
    return getAppId() + ".html";
  }

  public String[] getMediaExtensions()
  {
    String str = (String)this.wlProperties.get("ignoredFileExtensions");
    if (str != null)
      return str.replaceAll(" ", "").split(",");
    return null;
  }

  public String getPlatformVersion()
  {
    return (String)this.wlProperties.get("wlPlatformVersion");
  }

  public String getPort()
  {
    return (String)this.wlProperties.get("wlServerPort");
  }

  public String getProtocol()
  {
    return (String)this.wlProperties.get("wlServerProtocol");
  }

  public String getRootURL()
  {
    String str = this.prefs.getString("WLServerURL", null);
    if (str == null)
      str = getDefaultRootUrl();
    return str;
  }

  public String getSettingsFlag()
  {
    return (String)this.wlProperties.get("enableSettings");
  }

  public String getTestWebResourcesChecksumFlag()
  {
    return (String)this.wlProperties.get("testWebResourcesChecksum");
  }

  public String getWLEnvironment()
  {
    String str = this.wlProperties.getProperty("wlEnvironment", null);
    if (str == null)
      str = "android";
    return str;
  }

  public String getWebResourcesUnpackedSize()
  {
    return (String)this.wlProperties.get("webResourcesSize");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.common.WLConfig
 * JD-Core Version:    0.6.0
 */