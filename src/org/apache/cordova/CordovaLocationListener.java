package org.apache.cordova;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.cordova.api.CallbackContext;

public class CordovaLocationListener
  implements LocationListener
{
  public static int PERMISSION_DENIED = 1;
  public static int POSITION_UNAVAILABLE = 2;
  public static int TIMEOUT = 3;
  private String TAG = "[Cordova Location Listener]";
  private List<CallbackContext> callbacks = new ArrayList();
  protected LocationManager locationManager;
  private GeoBroker owner;
  protected boolean running = false;
  public HashMap<String, CallbackContext> watches = new HashMap();

  public CordovaLocationListener(LocationManager paramLocationManager, GeoBroker paramGeoBroker, String paramString)
  {
    this.locationManager = paramLocationManager;
    this.owner = paramGeoBroker;
    this.TAG = paramString;
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
    Iterator localIterator1 = this.callbacks.iterator();
    while (localIterator1.hasNext())
    {
      CallbackContext localCallbackContext = (CallbackContext)localIterator1.next();
      this.owner.win(paramLocation, localCallbackContext);
    }
    if (this.owner.isGlobalListener(this))
    {
      Log.d(this.TAG, "Stopping global listener");
      stop();
    }
    this.callbacks.clear();
    Iterator localIterator2 = this.watches.values().iterator();
    while (localIterator2.hasNext())
      this.owner.win(paramLocation, (CallbackContext)localIterator2.next());
  }

  public void addCallback(CallbackContext paramCallbackContext)
  {
    this.callbacks.add(paramCallbackContext);
    if (size() == 1)
      start();
  }

  public void addWatch(String paramString, CallbackContext paramCallbackContext)
  {
    this.watches.put(paramString, paramCallbackContext);
    if (size() == 1)
      start();
  }

  public void clearWatch(String paramString)
  {
    if (this.watches.containsKey(paramString))
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
    Iterator localIterator1 = this.callbacks.iterator();
    while (localIterator1.hasNext())
    {
      CallbackContext localCallbackContext = (CallbackContext)localIterator1.next();
      this.owner.fail(paramInt, paramString, localCallbackContext);
    }
    if (this.owner.isGlobalListener(this))
    {
      Log.d(this.TAG, "Stopping global listener");
      stop();
    }
    this.callbacks.clear();
    Iterator localIterator2 = this.watches.values().iterator();
    while (localIterator2.hasNext())
      this.owner.fail(paramInt, paramString, (CallbackContext)localIterator2.next());
  }

  public void onLocationChanged(Location paramLocation)
  {
    Log.d(this.TAG, "The location has been updated!");
    win(paramLocation);
  }

  public void onProviderDisabled(String paramString)
  {
    Log.d(this.TAG, "Location provider '" + paramString + "' disabled.");
    fail(POSITION_UNAVAILABLE, "GPS provider disabled.");
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
        this.locationManager.requestLocationUpdates("network", 60000L, 10.0F, this);
      }
    }
    else
      return;
    fail(POSITION_UNAVAILABLE, "Network provider is not available.");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.CordovaLocationListener
 * JD-Core Version:    0.6.0
 */