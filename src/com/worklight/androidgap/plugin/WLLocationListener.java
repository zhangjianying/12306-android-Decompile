package com.worklight.androidgap.plugin;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.cordova.api.CallbackContext;

public abstract class WLLocationListener
  implements LocationListener
{
  public static int PERMISSION_DENIED = 1;
  public static int POSITION_UNAVAILABLE = 2;
  public static int TIMEOUT = 3;
  private String TAG = "[Worklight Location Listener]";
  protected HashMap<String, CallbackContext> callbacks = new HashMap();
  protected LocationManager locationManager;
  protected int maximumAge = 0;
  protected int minChangeDistance = 10;
  protected int minChangeTime = 60000;
  private WLGeolocationPlugin owner;
  protected String provider;
  protected boolean running = false;
  protected HashMap<String, CallbackContext> watches = new HashMap();

  public WLLocationListener(LocationManager paramLocationManager, WLGeolocationPlugin paramWLGeolocationPlugin, String paramString)
  {
    this.locationManager = paramLocationManager;
    this.owner = paramWLGeolocationPlugin;
    this.TAG = ("[WL " + paramString + " Listener]");
    this.provider = paramString;
  }

  private void stop()
  {
    if (this.running)
    {
      this.locationManager.removeUpdates(this);
      this.running = false;
    }
  }

  private void win(Location paramLocation)
  {
    long l = System.currentTimeMillis() - paramLocation.getTime();
    if (l > this.maximumAge)
      Log.d(this.TAG, "Acquired location age: " + l + " milliseconds. More than maximumAge of " + this.maximumAge + " milliseconds. Ignoring.");
    do
    {
      return;
      Iterator localIterator1 = this.callbacks.values().iterator();
      while (localIterator1.hasNext())
        this.owner.win(paramLocation, (CallbackContext)localIterator1.next(), false);
      if ((this.owner.isGlobalListener(this)) && (this.watches.size() == 0))
      {
        Log.d(this.TAG, "Stopping global listener");
        stop();
      }
      this.callbacks.clear();
      Iterator localIterator2 = this.watches.values().iterator();
      while (localIterator2.hasNext())
        this.owner.win(paramLocation, (CallbackContext)localIterator2.next(), true);
    }
    while (!this.watches.isEmpty());
    stop();
  }

  public void addCallback(String paramString, CallbackContext paramCallbackContext, int paramInt)
  {
    this.maximumAge = paramInt;
    this.callbacks.put(paramString, paramCallbackContext);
    if (size() == 1)
      start();
  }

  public void addWatch(String paramString, CallbackContext paramCallbackContext, int paramInt1, int paramInt2)
  {
    this.minChangeDistance = paramInt1;
    this.minChangeTime = paramInt2;
    this.watches.put(paramString, paramCallbackContext);
    stop();
    start();
  }

  public void clearWatch(String paramString)
  {
    this.watches.remove(paramString);
    if (size() == 0)
      stop();
  }

  public void destroy()
  {
    stop();
  }

  protected void fail(int paramInt, String paramString)
  {
    Iterator localIterator1 = this.callbacks.values().iterator();
    while (localIterator1.hasNext())
      this.owner.fail(paramInt, paramString, (CallbackContext)localIterator1.next(), false);
    if ((this.owner.isGlobalListener(this)) && (this.watches.size() == 0))
    {
      Log.d(this.TAG, "Stopping global listener");
      stop();
    }
    this.callbacks.clear();
    Iterator localIterator2 = this.watches.values().iterator();
    while (localIterator2.hasNext())
      this.owner.fail(paramInt, paramString, (CallbackContext)localIterator2.next(), true);
  }

  public Location getLastKnownLocation()
  {
    try
    {
      Location localLocation = this.locationManager.getLastKnownLocation(this.provider);
      return localLocation;
    }
    catch (SecurityException localSecurityException)
    {
    }
    return null;
  }

  public void onLocationChanged(Location paramLocation)
  {
    Log.d(this.TAG, "The location has been updated!");
    win(paramLocation);
  }

  public void onProviderDisabled(String paramString)
  {
    Log.d(this.TAG, "Location provider '" + paramString + "' disabled.");
    fail(POSITION_UNAVAILABLE, paramString + " provider disabled.");
  }

  public void onProviderEnabled(String paramString)
  {
    Log.d(this.TAG, "Location provider " + paramString + " has been enabled");
  }

  public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
  {
    Log.d(this.TAG, "The status of the provider " + paramString + " has changed");
    if (paramInt == 0)
    {
      Log.d(this.TAG, paramString + " is OUT OF SERVICE");
      fail(POSITION_UNAVAILABLE, "Provider " + paramString + " is out of service.");
      return;
    }
    if (paramInt == 1)
    {
      Log.d(this.TAG, paramString + " is TEMPORARILY_UNAVAILABLE");
      return;
    }
    Log.d(this.TAG, paramString + " is AVAILABLE");
  }

  public void removeCallback(String paramString)
  {
    this.callbacks.remove(paramString);
    if (size() == 0)
      stop();
  }

  public int size()
  {
    return this.watches.size() + this.callbacks.size();
  }

  protected void start()
  {
    if (!this.running)
    {
      if (this.locationManager.getProvider("network") != null)
      {
        this.running = true;
        this.locationManager.requestLocationUpdates("network", this.minChangeTime, this.minChangeDistance, this);
      }
    }
    else
      return;
    fail(POSITION_UNAVAILABLE, "Network provider is not available.");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.WLLocationListener
 * JD-Core Version:    0.6.0
 */