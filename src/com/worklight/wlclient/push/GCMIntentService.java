package com.worklight.wlclient.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.worklight.androidgap.push.WLGCMIntentService;
import com.worklight.common.WLConfig;
import com.worklight.common.WLUtils;

public class GCMIntentService extends WLGCMIntentService
{
  private static boolean isAppForeground = true;
  private BroadcastReceiver resultReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ((getResultCode() == 1) || (!GCMIntentService.isAppForeground()))
        GCMIntentService.this.onUnhandled(paramContext, paramIntent);
    }
  };

  public GCMIntentService()
  {
    setBroadcastReceiver(this.resultReceiver);
  }

  public static boolean isAppForeground()
  {
    return isAppForeground;
  }

  public static void setAppForeground(boolean paramBoolean)
  {
    isAppForeground = paramBoolean;
  }

  protected String getNotificationTitle(Context paramContext)
  {
    int i = -1;
    try
    {
      i = WLUtils.getResourceId(getApplicationContext(), "string", "push_notification_title");
      String str = paramContext.getString(i);
      return str;
    }
    catch (Exception localException1)
    {
      if (i == -1)
      {
        PackageManager localPackageManager = paramContext.getPackageManager();
        try
        {
          ApplicationInfo localApplicationInfo2 = localPackageManager.getApplicationInfo(paramContext.getPackageName(), 0);
          localApplicationInfo1 = localApplicationInfo2;
          if (localApplicationInfo1 != null)
            return (String)localPackageManager.getApplicationLabel(localApplicationInfo1);
        }
        catch (Exception localException2)
        {
          while (true)
          {
            WLUtils.warning("Notification will not have a title because application name is not available. " + localException2.getMessage());
            ApplicationInfo localApplicationInfo1 = null;
          }
        }
      }
    }
    return "";
  }

  protected String[] getSenderIds(Context paramContext)
  {
    String str = new WLConfig(paramContext).getGCMSender();
    if (str == null)
      return null;
    return new String[] { str };
  }

  protected void setResources()
  {
    try
    {
      setNotificationIcon(WLUtils.getResourceId(getApplicationContext(), "drawable", "push"));
      return;
    }
    catch (Exception localException)
    {
      WLUtils.error("Failed to find the icon resource. Add the icon file under the /res/drawable folder.");
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.push.GCMIntentService
 * JD-Core Version:    0.6.0
 */