package com.tl.uic.util;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.WindowManager;
import com.tl.uic.EnvironmentalData;
import com.tl.uic.TLFCache;
import com.tl.uic.Tealeaf;

public class ScreenReceiver extends BroadcastReceiver
{
  private static final int ROTATION_18O = 180;
  private static final int ROTATION_9O = 90;
  private static final int ROTATION_NEGATIVE_9O = -90;
  private static final int ROTATION_O;
  private int height;
  private int rotation = 0;
  private int width;

  public ScreenReceiver()
  {
    screenUpdate();
  }

  private Display screenUpdate()
  {
    Display localDisplay = ((WindowManager)Tealeaf.getApplication().getApplicationContext().getSystemService("window")).getDefaultDisplay();
    this.height = localDisplay.getHeight();
    this.width = localDisplay.getWidth();
    LogInternal.log("Screen height:" + this.height + "  Screen width" + this.width);
    return localDisplay;
  }

  public final int getHeight()
  {
    return this.height;
  }

  public final int getRotation()
  {
    return this.rotation;
  }

  public final int getWidth()
  {
    return this.width;
  }

  public final void onReceive(Context paramContext, Intent paramIntent)
  {
    if ("android.intent.action.CONFIGURATION_CHANGED".equalsIgnoreCase(paramIntent.getAction()))
      switch (screenUpdate().getOrientation())
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      }
    while (true)
    {
      LogInternal.log("Screen change:" + this.rotation);
      if (TLFCache.getEnvironmentalData() != null)
        TLFCache.getEnvironmentalData().hasClientStateChanged();
      return;
      this.rotation = 0;
      continue;
      this.rotation = 90;
      continue;
      this.rotation = 180;
      continue;
      this.rotation = -90;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.ScreenReceiver
 * JD-Core Version:    0.6.0
 */