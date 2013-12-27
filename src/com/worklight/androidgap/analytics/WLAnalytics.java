package com.worklight.androidgap.analytics;

import android.content.Context;
import android.util.Log;
import com.worklight.androidgap.WLDroidGap;
import com.worklight.androidgap.plugin.AnalyticsConfigurator;
import java.lang.reflect.Constructor;
import org.apache.cordova.CordovaWebView;

public class WLAnalytics
{
  private static final String LOG_TAG = WLAnalytics.class.getName();
  private static final String TEALEAF_CLASS = "com.tl.uic.Tealeaf";

  public static void initializeTealeaf(WLDroidGap paramWLDroidGap)
  {
    AnalyticsConfigurator.setDroidGapObject(paramWLDroidGap);
    AnalyticsConfigurator localAnalyticsConfigurator = new AnalyticsConfigurator();
    try
    {
      Class localClass = Class.forName("com.tl.uic.Tealeaf");
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = paramWLDroidGap.getApplication().getClass();
      Constructor localConstructor1 = localClass.getConstructor(arrayOfClass);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramWLDroidGap.getApplication();
      localConstructor1.newInstance(arrayOfObject);
      Constructor localConstructor2 = Class.forName("com.tl.uic.javascript.JavaScriptInterface").getConstructor(new Class[] { Context.class });
      ((CordovaWebView)(CordovaWebView)paramWLDroidGap.getCurrentFocus()).addJavascriptInterface(localConstructor2.newInstance(new Object[] { paramWLDroidGap }), "tlBridge");
      if (localAnalyticsConfigurator.isEnabledSharedPref())
        localAnalyticsConfigurator.enable();
      return;
    }
    catch (Exception localException)
    {
      Log.w(LOG_TAG, "TLF_configurator.enable was called, but the required Tealeaf libraries are not present in the classpath.");
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.analytics.WLAnalytics
 * JD-Core Version:    0.6.0
 */