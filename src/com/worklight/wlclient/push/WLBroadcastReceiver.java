package com.worklight.wlclient.push;

import android.content.Context;
import com.google.android.gcm.GCMBroadcastReceiver;

public class WLBroadcastReceiver extends GCMBroadcastReceiver
{
  private static final String INTENT_SERVICE = "com.worklight.wlclient.push.GCMIntentService";

  protected String getGCMIntentServiceClassName(Context paramContext)
  {
    return "com.worklight.wlclient.push.GCMIntentService";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.push.WLBroadcastReceiver
 * JD-Core Version:    0.6.0
 */