package com.worklight.androidgap.jsonstore.util;

import android.util.Log;
import java.util.HashMap;

public class Logger
{
  private static final HashMap<String, Logger> instances = new HashMap();
  private String tag;

  private Logger(String paramString)
  {
    this.tag = paramString;
  }

  public static Logger getLogger(String paramString)
  {
    monitorenter;
    try
    {
      Logger localLogger = (Logger)instances.get(paramString);
      if (localLogger == null)
      {
        localLogger = new Logger(paramString);
        instances.put(paramString, localLogger);
      }
      return localLogger;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public boolean isLoggable(int paramInt)
  {
    return Log.isLoggable(this.tag, paramInt);
  }

  public void logDebug(String paramString)
  {
    Log.d(this.tag, paramString);
  }

  public void logDebug(String paramString, Throwable paramThrowable)
  {
    Log.d(this.tag, paramString, paramThrowable);
  }

  public void logError(String paramString)
  {
    Log.e(this.tag, paramString);
  }

  public void logError(String paramString, Throwable paramThrowable)
  {
    Log.e(this.tag, paramString, paramThrowable);
  }

  public void logInfo(String paramString)
  {
    Log.i(this.tag, paramString);
  }

  public void logInfo(String paramString, Throwable paramThrowable)
  {
    Log.i(this.tag, paramString, paramThrowable);
  }

  public void logWarning(String paramString)
  {
    Log.w(this.tag, paramString);
  }

  public void logWarning(String paramString, Throwable paramThrowable)
  {
    Log.w(this.tag, paramString, paramThrowable);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.Logger
 * JD-Core Version:    0.6.0
 */