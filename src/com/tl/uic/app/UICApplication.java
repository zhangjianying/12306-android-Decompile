package com.tl.uic.app;

import android.app.Application;
import com.tl.uic.Tealeaf;

public class UICApplication extends Application
{
  private Tealeaf tealeaf;

  public final Tealeaf getTealeaf()
  {
    return this.tealeaf;
  }

  public void onCreate()
  {
    super.onCreate();
    this.tealeaf = new Tealeaf(this);
  }

  public void onLowMemory()
  {
    Tealeaf.onLowMemory();
    super.onLowMemory();
  }

  public void onTerminate()
  {
    Tealeaf.disable();
    super.onTerminate();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.app.UICApplication
 * JD-Core Version:    0.6.0
 */