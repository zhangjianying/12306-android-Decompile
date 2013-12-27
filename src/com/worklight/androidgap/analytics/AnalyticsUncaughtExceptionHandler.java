package com.worklight.androidgap.analytics;

import android.util.Log;
import com.worklight.common.WLConfig;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

public class AnalyticsUncaughtExceptionHandler
  implements Thread.UncaughtExceptionHandler
{
  private final Thread.UncaughtExceptionHandler defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
  private final WLConfig wlConfig;

  public AnalyticsUncaughtExceptionHandler(WLConfig paramWLConfig)
  {
    this.wlConfig = paramWLConfig;
  }

  public void setDefaultUncaughtExceptionHandler()
  {
    Thread.setDefaultUncaughtExceptionHandler(this.defaultUEH);
  }

  public void uncaughtException(Thread paramThread, Throwable paramThrowable)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("appName", this.wlConfig.getAppId());
    localHashMap.put("timestamp", "" + new Date().getTime());
    localHashMap.put("environment", this.wlConfig.getWLEnvironment());
    localHashMap.put("appVersion", this.wlConfig.getApplicationVersion());
    localHashMap.put("exceptionType", "UncaughtException");
    try
    {
      Class.forName("com.tl.uic.Tealeaf").getMethod("logException", new Class[] { Throwable.class, HashMap.class }).invoke(null, new Object[] { paramThrowable, localHashMap });
      label135: this.defaultUEH.uncaughtException(paramThread, paramThrowable);
      return;
    }
    catch (Throwable localThrowable)
    {
      while (true)
        Log.w("AnalyticsUncaughtExceptionHandler", "There was an error logging the exception on Tealeaf with the WL context.");
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      break label135;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.analytics.AnalyticsUncaughtExceptionHandler
 * JD-Core Version:    0.6.0
 */