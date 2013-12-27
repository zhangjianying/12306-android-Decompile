package com.worklight.androidgap.plugin;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.webkit.CookieManager;
import com.worklight.androidgap.analytics.AnalyticsUncaughtExceptionHandler;
import com.worklight.common.WLConfig;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.cordova.DroidGap;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyticsConfigurator extends CordovaPlugin
{
  private static final String CONFIGURATIONUTIL_CLASS = "com.tl.uic.util.ConfigurationUtil";
  private static final String CONFIGURATIONUTIL_CLASS_METHOD_GETSTRING = "getString";
  private static final String CONFIGURATIONUTIL_CLASS_METHOD_SETPROPERTY = "setProperty";
  private static final String LOG_TAG = AnalyticsConfigurator.class.getName();
  private static final String TEALEAF_CLASS = "com.tl.uic.Tealeaf";
  private static final String TEALEAF_CLASS_METHOD_DISABLE = "disable";
  private static final String TEALEAF_CLASS_METHOD_ENABLE = "enable";
  private static final String TEALEAF_CLASS_METHOD_GETADDITIONALCOOKIE = "getAdditionalCookie";
  private static final String TEALEAF_CLASS_METHOD_GETADDITIONALHEADERS = "getAdditionalHeaders";
  private static final String TEALEAF_CLASS_METHOD_ISENABLED = "isEnabled";
  private static final String TEALEAF_CLASS_METHOD_REQUESTMANUALSERVERPOST = "requestManualServerPost";
  private static final String TEALEAF_CLASS_METHOD_SETADDITIONALCOOKIE = "setAdditionalCookie";
  private static final String TEALEAF_CLASS_METHOD_SETADDITIONALHEADERS = "setAdditionalHeaders";
  private static final String TEALEAF_CONFIGURABLE_ITEMS_KEY_POSTMESSAGEURL = "PostMessageUrl";
  private static final String X_WL_TLT_FORWARD_COOKIES_HEADER = "x-wl-tlt-forward-cookies";
  protected static DroidGap droidGap = null;
  protected static SharedPreferences prefs = null;
  private static AnalyticsUncaughtExceptionHandler wlUEH;

  public static void setDroidGapObject(DroidGap paramDroidGap)
  {
    if (droidGap == null)
    {
      droidGap = paramDroidGap;
      prefs = droidGap.getSharedPreferences(AnalyticsConfigurator.class.getName(), 0);
    }
  }

  public void enable()
  {
    ACTIONS localACTIONS = ACTIONS.fromString(ACTIONS.enable.toString());
    try
    {
      localACTIONS.doAction(this, null);
      return;
    }
    catch (Exception localException)
    {
      Log.e(LOG_TAG, "Unexpected Exception", localException);
    }
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    while (true)
    {
      ACTIONS localACTIONS;
      try
      {
        localACTIONS = ACTIONS.fromString(paramString);
        if (localACTIONS != null)
          continue;
        paramCallbackContext.error("Invalid action: " + paramString);
        return false;
        try
        {
          int i = 1.$SwitchMap$com$worklight$androidgap$plugin$AnalyticsConfigurator$ACTIONS[localACTIONS.ordinal()];
          switch (i)
          {
          default:
            paramCallbackContext.success();
            return true;
          case 1:
          case 2:
          case 3:
            localACTIONS.doAction(this, null);
            continue;
          case 4:
          case 5:
          }
        }
        catch (MalformedURLException localMalformedURLException)
        {
          paramCallbackContext.error("Parameter: " + paramJSONArray.getString(0) + " is not a valid URL.");
          return false;
        }
      }
      catch (JSONException localJSONException)
      {
        paramCallbackContext.error("Action: " + paramString + " failed. JSON Error is: " + localJSONException.getLocalizedMessage());
        return false;
      }
      localACTIONS.doAction(this, paramJSONArray.getString(0));
    }
  }

  public boolean isEnabledSharedPref()
  {
    return prefs.getBoolean(OPTIONS.ENABLED.toString(), true);
  }

  private static abstract enum ACTIONS
  {
    final AnalyticsConfigurator configurator = null;

    static
    {
      enable = new ACTIONS("enable", 2)
      {
        public void doAction(AnalyticsConfigurator paramAnalyticsConfigurator, String paramString)
        {
          try
          {
            JSONObject localJSONObject = new JSONObject(AnalyticsConfigurator.prefs.getString(AnalyticsConfigurator.OPTIONS.TEALEAF_PROPS.toString(), "{}"));
            Method localMethod1 = Class.forName("com.tl.uic.util.ConfigurationUtil").getMethod("setProperty", new Class[] { String.class, String.class });
            Iterator localIterator = localJSONObject.keys();
            WLConfig localWLConfig = new WLConfig(AnalyticsConfigurator.droidGap.getApplication());
            String str1 = null;
            monitorenter;
            try
            {
              while (localIterator.hasNext())
              {
                String str3 = (String)localIterator.next();
                Object[] arrayOfObject3 = new Object[2];
                arrayOfObject3[0] = str3;
                arrayOfObject3[1] = ((String)localJSONObject.get(str3));
                localMethod1.invoke(null, arrayOfObject3);
                if (!str3.equals("PostMessageUrl"))
                  continue;
                str1 = (String)localJSONObject.get(str3);
              }
              monitorexit;
              if (str1 == null)
              {
                Method localMethod2 = Class.forName("com.tl.uic.util.ConfigurationUtil").getMethod("getString", new Class[] { String.class });
                Object[] arrayOfObject1 = new Object[1];
                arrayOfObject1[0] = ((String)Class.forName("com.tl.uic.Tealeaf").getField("TLF_POST_MESSAGE_URL").get(String.class));
                String str2 = (String)localMethod2.invoke(null, arrayOfObject1);
                if ((str2 != null) && (str2.equals("@USE_WORKLIGHT_DEFAULT@")))
                  str2 = localWLConfig.getAppURL() + "/analytics";
                localJSONObject.put("PostMessageUrl", str2);
                AnalyticsConfigurator.prefs.edit().putString(AnalyticsConfigurator.OPTIONS.TEALEAF_PROPS.toString(), localJSONObject.toString()).commit();
                Object[] arrayOfObject2 = new Object[2];
                arrayOfObject2[0] = ((String)Class.forName("com.tl.uic.Tealeaf").getField("TLF_POST_MESSAGE_URL").get(String.class));
                arrayOfObject2[1] = str2;
                localMethod1.invoke(null, arrayOfObject2);
              }
              Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
              Class localClass = Class.forName("com.tl.uic.Tealeaf");
              if (!((Boolean)localClass.getMethod("isEnabled", new Class[0]).invoke(null, new Object[0])).booleanValue())
              {
                localClass.getMethod("enable", new Class[0]).invoke(null, new Object[0]);
                Thread.setDefaultUncaughtExceptionHandler(localUncaughtExceptionHandler);
                AnalyticsConfigurator.access$202(new AnalyticsUncaughtExceptionHandler(localWLConfig));
                Thread.setDefaultUncaughtExceptionHandler(AnalyticsConfigurator.wlUEH);
              }
              bool = true;
              AnalyticsConfigurator.prefs.edit().putBoolean(AnalyticsConfigurator.OPTIONS.ENABLED.toString(), bool).commit();
              return;
            }
            finally
            {
              monitorexit;
            }
          }
          catch (Exception localException)
          {
            while (true)
            {
              Log.w(AnalyticsConfigurator.LOG_TAG, "enable was called, but the required Tealeaf libraries are not present in the classpath.");
              boolean bool = false;
            }
          }
        }
      };
      disable = new ACTIONS("disable", 3)
      {
        public void doAction(AnalyticsConfigurator paramAnalyticsConfigurator, String paramString)
        {
          try
          {
            Class localClass = Class.forName("com.tl.uic.Tealeaf");
            if (((Boolean)localClass.getMethod("isEnabled", new Class[0]).invoke(null, new Object[0])).booleanValue())
            {
              localClass.getMethod("disable", new Class[0]).invoke(null, new Object[0]);
              AnalyticsConfigurator.wlUEH.setDefaultUncaughtExceptionHandler();
              AnalyticsConfigurator.access$202(null);
            }
            AnalyticsConfigurator.prefs.edit().putBoolean(AnalyticsConfigurator.OPTIONS.ENABLED.toString(), false).commit();
            return;
          }
          catch (Exception localException)
          {
            while (true)
              Log.w(AnalyticsConfigurator.LOG_TAG, "disable was called, but the required Tealeaf libraries are not present in the classpath.");
          }
        }
      };
      resetConfig = new ACTIONS("resetConfig", 4)
      {
        public void doAction(AnalyticsConfigurator paramAnalyticsConfigurator, String paramString)
        {
          AnalyticsConfigurator.prefs.edit().clear().commit();
        }
      };
      ACTIONS[] arrayOfACTIONS = new ACTIONS[5];
      arrayOfACTIONS[0] = configureTealeaf;
      arrayOfACTIONS[1] = send;
      arrayOfACTIONS[2] = enable;
      arrayOfACTIONS[3] = disable;
      arrayOfACTIONS[4] = resetConfig;
      $VALUES = arrayOfACTIONS;
    }

    public static ACTIONS fromString(String paramString)
    {
      try
      {
        ACTIONS localACTIONS = valueOf(paramString);
        return localACTIONS;
      }
      catch (Exception localException)
      {
      }
      return null;
    }

    public abstract void doAction(AnalyticsConfigurator paramAnalyticsConfigurator, String paramString)
      throws MalformedURLException, JSONException;
  }

  private static enum OPTIONS
  {
    static
    {
      OPTIONS[] arrayOfOPTIONS = new OPTIONS[2];
      arrayOfOPTIONS[0] = ENABLED;
      arrayOfOPTIONS[1] = TEALEAF_PROPS;
      $VALUES = arrayOfOPTIONS;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.AnalyticsConfigurator
 * JD-Core Version:    0.6.0
 */