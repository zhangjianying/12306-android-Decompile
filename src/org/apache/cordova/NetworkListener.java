package org.apache.cordova;

import android.location.LocationManager;

public class NetworkListener extends CordovaLocationListener
{
  public NetworkListener(LocationManager paramLocationManager, GeoBroker paramGeoBroker)
  {
    super(paramLocationManager, paramGeoBroker, "[Cordova NetworkListener]");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.NetworkListener
 * JD-Core Version:    0.6.0
 */