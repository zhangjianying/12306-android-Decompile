package com.worklight.androidgap.plugin;

import android.location.LocationManager;

public class WLNetworkListener extends WLLocationListener
{
  public WLNetworkListener(LocationManager paramLocationManager, WLGeolocationPlugin paramWLGeolocationPlugin)
  {
    super(paramLocationManager, paramWLGeolocationPlugin, "network");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.WLNetworkListener
 * JD-Core Version:    0.6.0
 */