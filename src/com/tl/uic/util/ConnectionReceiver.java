package com.tl.uic.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import com.tl.uic.EnvironmentalData;
import com.tl.uic.TLFCache;
import com.tl.uic.model.ReachabilityType;

public class ConnectionReceiver extends BroadcastReceiver
{
  private Boolean _isOnline = Boolean.valueOf(false);
  private String connectionType;
  private ReachabilityType networkReachability = ReachabilityType.Unknown;

  public final String getConnectionType()
  {
    return this.connectionType;
  }

  public final ReachabilityType getNetworkReachability()
  {
    return this.networkReachability;
  }

  public final Boolean isOnline()
  {
    return this._isOnline;
  }

  public final void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getAction();
    this._isOnline = Boolean.valueOf(false);
    if (!str.equals("android.net.conn.CONNECTIVITY_CHANGE"))
      TLFCache.setCurrentLogLevel(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false));
    NetworkInfo localNetworkInfo;
    while (true)
    {
      return;
      boolean bool = paramIntent.getBooleanExtra("noConnectivity", false);
      localNetworkInfo = (NetworkInfo)paramIntent.getParcelableExtra("networkInfo");
      if (!bool)
        break;
      this.networkReachability = ReachabilityType.NotReachable;
      this._isOnline = Boolean.valueOf(false);
      TLFCache.setCurrentLogLevel(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false));
      this.connectionType = localNetworkInfo.getSubtypeName();
      LogInternal.log("Network changed");
      if (TLFCache.getEnvironmentalData() == null)
        continue;
      TLFCache.getEnvironmentalData().hasClientStateChanged();
      return;
    }
    if (localNetworkInfo.getType() == 1)
    {
      this.networkReachability = ReachabilityType.ReachableViaWIFI;
      TLFCache.setCurrentLogLevel(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false));
    }
    while (true)
    {
      this._isOnline = Boolean.valueOf(true);
      break;
      if (!localNetworkInfo.getTypeName().contains("mobile"))
        continue;
      this.networkReachability = ReachabilityType.ReachableViaWWAN;
      TLFCache.setCurrentLogLevel(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true));
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.ConnectionReceiver
 * JD-Core Version:    0.6.0
 */