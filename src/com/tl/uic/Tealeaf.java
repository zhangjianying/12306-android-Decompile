package com.tl.uic;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.tl.uic.model.Screenview;
import com.tl.uic.model.ScreenviewType;
import com.tl.uic.util.ConfigurationUtil;
import com.tl.uic.util.ImageUtil;
import com.tl.uic.util.LogInternal;
import com.tl.uic.util.PostTask;
import com.tl.uic.util.TLUncaughtExceptionHandler;
import java.util.HashMap;
import org.apache.http.HttpResponse;

public class Tealeaf
{
  public static final String DISPLAY_LOGGING = "DisplayLogging";
  public static final String TAG = "UICAndroid";
  public static final String TLF_ADD_COOKIE_DOMAIN = "AddCookieDomain";
  public static final String TLF_ADD_COOKIE_PATH = "AddCookiePath";
  public static final String TLF_ADD_MESSAGE_TYPE_HEADER = "AddMessageTypeHeader";
  public static final String TLF_BUFFER_LIMIT = "BufferLimit";
  public static final String TLF_BUFFER_PERCENT = "BufferPercent";
  public static final String TLF_CACHED_FILE_MAX_BYTES_SIZE = "CachedFileMaxBytesSize";
  public static final String TLF_CACHED_LEVEL = "CachingLevel";
  public static final String TLF_CACHE_DIR = "TLFCache";
  public static final String TLF_COMPRESS_POST_MESSAGE = "CompressPostMessage";
  public static final String TLF_CONFIGURATION_FILENAME = "TLFConfigurableItems.properties";
  public static final String TLF_COOKIE_DOMAIN = "CookieDomain";
  public static final String TLF_COOKIE_PARAM = "CookieParam";
  public static final String TLF_COOKIE_PATH = "CookiePath";
  public static final String TLF_COOKIE_URL = "CookieUrl";
  public static final String TLF_DO_POSTS_ON_INTERVALS = "DoPostOnIntervals";
  public static final String TLF_FILTER_MESSAGE_TYPES = "FilterMessageTypes";
  public static final String TLF_HAS_CUSTOM_MASK = "HasCustomMask";
  public static final String TLF_HAS_MASKING = "HasMasking";
  public static final String TLF_HAS_TO_PERSIST_LOCAL_CACHE = "HasToPersistLocalCache";
  public static final String TLF_HEADER = "X-Tealeaf";
  public static final String TLF_KILL_SWITCH_ENABLED = "KillSwitchEnabled";
  public static final String TLF_KILL_SWITCH_MAX_NUMBER_OF_TRIES = "KillSwitchMaxNumberOfTries";
  public static final String TLF_KILL_SWITCH_TIME_INTERVAL = "KillSwitchTimeInterval";
  public static final String TLF_KILL_SWITCH_URL = "KillSwitchUrl";
  public static final String TLF_LIBRARY_VERSION = "LibraryVersion";
  public static final String TLF_LOGGING_LEVEL = "LoggingLevel";
  public static final String TLF_MANUAL_POST_ENABLED = "ManualPostEnabled";
  public static final String TLF_MASK_ID_LIST = "MaskIdList";
  public static final String TLF_MAX_STRINGS_LENGTH = "MaxStringsLength";
  public static final String TLF_MESSAGE_TYPES = "MessageTypes";
  public static final String TLF_MESSAGE_TYPE_HEADER = "MessageTypeHeader";
  public static final String TLF_MESSAGE_VERSION = "MessageVersion";
  public static final String TLF_ON_DRAWER_CLOSED = "OnDrawerClosed";
  public static final String TLF_ON_DRAWER_OPENED = "OnDrawerOpened";
  public static final String TLF_ON_FOCUS_CHANGE_IN = "OnFocusChange_In";
  public static final String TLF_ON_FOCUS_CHANGE_OUT = "OnFocusChange_Out";
  public static final String TLF_ON_GROUP_COLLAPSE = "OnGroupCollapse";
  public static final String TLF_ON_GROUP_EXPAND = "OnGroupExpand";
  public static final String TLF_POST_MESSAGE_LEVEL_CELLULAR = "PostMessageLevelCellular";
  public static final String TLF_POST_MESSAGE_LEVEL_WIFI = "PostMessageLevelWiFi";
  public static final String TLF_POST_MESSAGE_MAX_BYTES_SIZE = "PostMessageMaxBytesSize";
  public static final String TLF_POST_MESSAGE_SOCKET_TIMEOUT = "PostMessageSocketTimeout";
  public static final String TLF_POST_MESSAGE_TIMEOUT = "PostMessageTimeout";
  public static final String TLF_POST_MESSAGE_TIME_INTERVALS = "PostMessageTimeIntervals";
  public static final String TLF_POST_MESSAGE_URL = "PostMessageUrl";
  public static final String TLF_PROPERTY_HEADER = "X-Tealeaf-Property";
  public static final String TLF_RANDOM_SAMPLE_PARAM = "RandomSampleParam";
  public static final String TLF_SENSITIVE_CAPITAL_CASE_ALPHABET = "SensitiveCapitalCaseAlphabet";
  public static final String TLF_SENSITIVE_NUMBER = "SensitiveNumber";
  public static final String TLF_SENSITIVE_SMALL_CASE_ALPHABET = "SensitiveSmallCaseAlphabet";
  public static final String TLF_SENSITIVE_SYMBOL = "SensitiveSymbol";
  public static final String TLF_SESSION_HEADER = "X-Tealeaf-Session";
  public static final String TLF_TIME_INTERVAL_BETWEEN_SNAPSHOTS = "TimeIntervalBetweenSnapshots";
  public static final String TLF_USE_RANDOM_SAMPLE = "UseRandomSample";
  public static final String TLF_USE_WHITE_LIST = "UseWhiteList";
  public static final String TLF_WHITE_LIST_PARAM = "WhiteListParam";
  public static final int VERBOSE = 5;
  private static Boolean _appLoaded;
  private static int _applicationInForegroundCounter;
  private static Boolean _isEnabled = Boolean.valueOf(false);
  private static volatile Tealeaf _myInstance;
  private static String additionalCookies;
  private static HashMap<String, String> additionalHeaders;
  private static Application application;
  private static Logger logger;
  private static String phoneId;
  private static TLUncaughtExceptionHandler tlUncaughtExceptionHandler;

  static
  {
    _appLoaded = Boolean.valueOf(false);
    _applicationInForegroundCounter = 0;
  }

  private Tealeaf()
  {
  }

  public Tealeaf(Application paramApplication)
  {
    monitorenter;
    try
    {
      application = paramApplication;
      monitorexit;
      ConfigurationUtil.init(application);
      return;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static Boolean disable()
  {
    _isEnabled = Boolean.valueOf(false);
    logger.terminate();
    tlUncaughtExceptionHandler.setDefaultUncaughtExceptionHandler();
    tlUncaughtExceptionHandler = null;
    return _isEnabled;
  }

  public static Boolean enable()
  {
    return enable(null);
  }

  public static Boolean enable(String paramString)
  {
    if (_isEnabled.booleanValue())
      return _isEnabled;
    if (tlUncaughtExceptionHandler == null)
    {
      tlUncaughtExceptionHandler = new TLUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler(tlUncaughtExceptionHandler);
    }
    if (logger == null)
      logger = new Logger(application);
    _isEnabled = logger.enable(paramString);
    return _isEnabled;
  }

  public static Boolean flush()
  {
    return TLFCache.saveToCache(Boolean.valueOf(true));
  }

  public static String getAdditionalCookie()
  {
    return additionalCookies;
  }

  public static HashMap<String, String> getAdditionalHeaders()
  {
    return additionalHeaders;
  }

  public static Application getApplication()
  {
    return application;
  }

  public static long getApplicationScreenviewOffset()
  {
    if (logger == null)
      return 0L;
    return logger.getApplicationScreenviewOffset();
  }

  public static Screenview getCurrentScreenView()
  {
    if (logger == null)
      return null;
    return logger.getCurrentScreenview();
  }

  public static String getCurrentSessionId()
  {
    return TLFCache.getCurrentSessionId();
  }

  public static String getHttpXTealeafProperty()
  {
    if ((TLFCache.getEnvironmentalData() == null) || (TLFCache.getEnvironmentalData().getHttpXTealeafProperty() == null))
      return "";
    return TLFCache.getEnvironmentalData().getHttpXTealeafProperty();
  }

  public static Tealeaf getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new Tealeaf();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static String getLibraryVersion()
  {
    return ConfigurationUtil.getString("LibraryVersion");
  }

  public static String getMessageVersion()
  {
    return ConfigurationUtil.getString("MessageVersion");
  }

  public static String getPhoneId()
  {
    return phoneId;
  }

  public static String getTLCookie()
  {
    return getTLCookie(null);
  }

  public static String getTLCookie(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(ConfigurationUtil.getString("CookieParam"));
    localStringBuffer.append('=');
    if (paramString == null)
      paramString = getCurrentSessionId();
    localStringBuffer.append(paramString);
    if (ConfigurationUtil.getBoolean("AddCookiePath").booleanValue())
    {
      localStringBuffer.append(";Path=");
      localStringBuffer.append(ConfigurationUtil.getString("CookiePath"));
    }
    if (ConfigurationUtil.getBoolean("AddCookieDomain").booleanValue())
    {
      localStringBuffer.append(";Domain=");
      localStringBuffer.append(ConfigurationUtil.getString("CookieDomain"));
    }
    String str1 = ConfigurationUtil.getString("CookieUrl");
    if (str1 != null)
    {
      String str2 = CookieManager.getInstance().getCookie(str1);
      if (str2 != null)
      {
        localStringBuffer.append(';');
        localStringBuffer.append(str2);
      }
    }
    if (additionalCookies != null)
    {
      if (localStringBuffer.length() > 0)
        localStringBuffer.append(';');
      localStringBuffer.append(additionalCookies);
    }
    return localStringBuffer.toString();
  }

  public static Boolean isApplicationInBackground()
  {
    if (_applicationInForegroundCounter <= 0)
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  public static Boolean isEnabled()
  {
    return _isEnabled;
  }

  public static Boolean logConnection(String paramString, HttpResponse paramHttpResponse, long paramLong1, long paramLong2, long paramLong3)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logConnection(paramString, paramHttpResponse, paramLong1, paramLong2, paramLong3);
  }

  public static Boolean logCustomEvent(String paramString)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logCustomEvent(paramString, null, ConfigurationUtil.getInt("LoggingLevel"));
  }

  public static Boolean logCustomEvent(String paramString, int paramInt)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logCustomEvent(paramString, null, paramInt);
  }

  public static Boolean logCustomEvent(String paramString, HashMap<String, String> paramHashMap)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logCustomEvent(paramString, paramHashMap, ConfigurationUtil.getInt("LoggingLevel"));
  }

  public static Boolean logCustomEvent(String paramString, HashMap<String, String> paramHashMap, int paramInt)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logCustomEvent(paramString, paramHashMap, paramInt);
  }

  public static Boolean logEvent(View paramView)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logEvent(paramView, null, ConfigurationUtil.getInt("LoggingLevel"));
  }

  public static Boolean logEvent(View paramView, String paramString)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logEvent(paramView, paramString, ConfigurationUtil.getInt("LoggingLevel"));
  }

  public static Boolean logEvent(View paramView, String paramString, int paramInt)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logEvent(paramView, paramString, paramInt);
  }

  public static Boolean logException(Throwable paramThrowable)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logException(paramThrowable, null, null);
  }

  public static Boolean logException(Throwable paramThrowable, String paramString)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logException(paramThrowable, paramString);
  }

  public static Boolean logException(Throwable paramThrowable, HashMap<String, String> paramHashMap)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logException(paramThrowable, paramHashMap);
  }

  public static Boolean logScreenview(Activity paramActivity, String paramString, ScreenviewType paramScreenviewType)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logScreenview(paramActivity, paramString, paramScreenviewType, null);
  }

  public static Boolean logScreenview(Activity paramActivity, String paramString1, ScreenviewType paramScreenviewType, String paramString2)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logScreenview(paramActivity, paramString1, paramScreenviewType, paramString2);
  }

  public static Boolean logTLLibErrorException(Throwable paramThrowable, String paramString)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.logTLLibErrorException(paramThrowable, paramString);
  }

  public static Boolean onDestroy(Activity paramActivity, String paramString)
  {
    if (logger != null)
      logger.onDestroy(paramString);
    return Boolean.valueOf(true);
  }

  public static Boolean onLowMemory()
  {
    long l = 0L;
    if (TLFCache.getEnvironmentalData() != null)
      l = TLFCache.getEnvironmentalData().getAvailableMemory();
    HashMap localHashMap = new HashMap();
    localHashMap.put("memoryLevelMB", String.valueOf(l));
    logCustomEvent("Low Memory", localHashMap, 0);
    return disable();
  }

  public static Boolean onPause(Activity paramActivity, String paramString)
  {
    if (logger != null)
      logger.onPause(paramActivity, paramString);
    _applicationInForegroundCounter = -1 + _applicationInForegroundCounter;
    CookieSyncManager.getInstance().sync();
    return Boolean.valueOf(true);
  }

  public static Boolean onPauseNoActivityInForeground()
  {
    if (!_appLoaded.booleanValue())
      return Boolean.valueOf(false);
    flush();
    if (logger != null)
      logger.onPauseNoActivityInForeground();
    return Boolean.valueOf(true);
  }

  public static Boolean onResume(Activity paramActivity, String paramString)
  {
    if (logger != null)
      logger.onResume(paramString, paramActivity);
    _applicationInForegroundCounter = 1 + _applicationInForegroundCounter;
    _appLoaded = Boolean.valueOf(true);
    CookieSyncManager.getInstance().stopSync();
    return Boolean.valueOf(true);
  }

  public static Boolean registerFormField(View paramView, Activity paramActivity)
  {
    return registerFormField(paramView, paramActivity, ConfigurationUtil.getInt("LoggingLevel"));
  }

  public static Boolean registerFormField(View paramView, Activity paramActivity, int paramInt)
  {
    if (logger == null)
      return Boolean.valueOf(false);
    return logger.registerFormField(paramView, paramActivity, paramInt);
  }

  public static Boolean requestManualServerPost()
  {
    Boolean localBoolean = Boolean.valueOf(false);
    PostTask localPostTask;
    if ((ConfigurationUtil.getBoolean("ManualPostEnabled").booleanValue()) && (!ConfigurationUtil.getBoolean("DoPostOnIntervals").booleanValue()))
    {
      TLFCache.saveToCache(Boolean.valueOf(true));
      localPostTask = new PostTask();
      localPostTask.execute(new Void[0]);
    }
    try
    {
      localBoolean = (Boolean)localPostTask.get();
      return localBoolean;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localBoolean;
  }

  public static void setAdditionalCookie(String paramString)
  {
    additionalCookies = paramString;
  }

  public static void setAdditionalHeaders(HashMap<String, String> paramHashMap)
  {
    additionalHeaders = paramHashMap;
  }

  public static void setPhoneId(String paramString)
  {
    phoneId = paramString;
  }

  public static void setTLCookie()
  {
    setTLCookie(ConfigurationUtil.getString("CookieUrl"));
  }

  public static void setTLCookie(String paramString)
  {
    CookieManager localCookieManager = CookieManager.getInstance();
    localCookieManager.setAcceptCookie(true);
    localCookieManager.acceptCookie();
    if (paramString != null)
      localCookieManager.setCookie(paramString, getTLCookie());
    CookieSyncManager.getInstance().sync();
  }

  public static Boolean startSession()
  {
    return startSession(null);
  }

  public static Boolean startSession(String paramString)
  {
    Boolean localBoolean = Boolean.valueOf(true);
    if (logger != null)
      logger.startSession(paramString);
    return localBoolean;
  }

  public static Boolean takeScreenShot(View paramView, String paramString)
  {
    return ImageUtil.snapShot(paramView, "TLFCache", paramString);
  }

  public static Boolean terminate()
  {
    logger = null;
    return Boolean.valueOf(true);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.Tealeaf
 * JD-Core Version:    0.6.0
 */