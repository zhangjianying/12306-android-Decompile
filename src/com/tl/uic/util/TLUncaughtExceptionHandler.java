package com.tl.uic.util;

import com.tl.uic.Tealeaf;

public class TLUncaughtExceptionHandler
  implements Thread.UncaughtExceptionHandler
{
  private final Thread.UncaughtExceptionHandler defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

  public final void setDefaultUncaughtExceptionHandler()
  {
    Thread.setDefaultUncaughtExceptionHandler(this.defaultUEH);
  }

  public final void uncaughtException(Thread paramThread, Throwable paramThrowable)
  {
    Tealeaf.logException(paramThrowable);
    this.defaultUEH.uncaughtException(paramThread, paramThrowable);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.TLUncaughtExceptionHandler
 * JD-Core Version:    0.6.0
 */