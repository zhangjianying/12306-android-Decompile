package org.apache.cordova.api;

import android.util.Log;

public class LOG
{
  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int INFO = 4;
  public static int LOGLEVEL = 0;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;

  public static void d(String paramString1, String paramString2)
  {
    if (3 >= LOGLEVEL)
      Log.d(paramString1, paramString2);
  }

  public static void d(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (3 >= LOGLEVEL)
      Log.d(paramString1, paramString2, paramThrowable);
  }

  public static void d(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (3 >= LOGLEVEL)
      Log.d(paramString1, String.format(paramString2, paramArrayOfObject));
  }

  public static void e(String paramString1, String paramString2)
  {
    if (6 >= LOGLEVEL)
      Log.e(paramString1, paramString2);
  }

  public static void e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (6 >= LOGLEVEL)
      Log.e(paramString1, paramString2, paramThrowable);
  }

  public static void e(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (6 >= LOGLEVEL)
      Log.e(paramString1, String.format(paramString2, paramArrayOfObject));
  }

  public static void i(String paramString1, String paramString2)
  {
    if (4 >= LOGLEVEL)
      Log.i(paramString1, paramString2);
  }

  public static void i(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (4 >= LOGLEVEL)
      Log.i(paramString1, paramString2, paramThrowable);
  }

  public static void i(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (4 >= LOGLEVEL)
      Log.i(paramString1, String.format(paramString2, paramArrayOfObject));
  }

  public static boolean isLoggable(int paramInt)
  {
    return paramInt >= LOGLEVEL;
  }

  public static void setLogLevel(int paramInt)
  {
    LOGLEVEL = paramInt;
    Log.i("CordovaLog", "Changing log level to " + paramInt);
  }

  public static void setLogLevel(String paramString)
  {
    if ("VERBOSE".equals(paramString))
      LOGLEVEL = 2;
    while (true)
    {
      Log.i("CordovaLog", "Changing log level to " + paramString + "(" + LOGLEVEL + ")");
      return;
      if ("DEBUG".equals(paramString))
      {
        LOGLEVEL = 3;
        continue;
      }
      if ("INFO".equals(paramString))
      {
        LOGLEVEL = 4;
        continue;
      }
      if ("WARN".equals(paramString))
      {
        LOGLEVEL = 5;
        continue;
      }
      if (!"ERROR".equals(paramString))
        continue;
      LOGLEVEL = 6;
    }
  }

  public static void v(String paramString1, String paramString2)
  {
    if (2 >= LOGLEVEL)
      Log.v(paramString1, paramString2);
  }

  public static void v(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (2 >= LOGLEVEL)
      Log.v(paramString1, paramString2, paramThrowable);
  }

  public static void v(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (2 >= LOGLEVEL)
      Log.v(paramString1, String.format(paramString2, paramArrayOfObject));
  }

  public static void w(String paramString1, String paramString2)
  {
    if (5 >= LOGLEVEL)
      Log.w(paramString1, paramString2);
  }

  public static void w(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (5 >= LOGLEVEL)
      Log.w(paramString1, paramString2, paramThrowable);
  }

  public static void w(String paramString1, String paramString2, Object[] paramArrayOfObject)
  {
    if (5 >= LOGLEVEL)
      Log.w(paramString1, String.format(paramString2, paramArrayOfObject));
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.api.LOG
 * JD-Core Version:    0.6.0
 */