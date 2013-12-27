package com.worklight.androidgap.plugin;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import java.util.HashMap;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WLGeolocationPlugin extends CordovaPlugin
{
  private WLGPSListener gpsListener;
  private LocationManager locationManager;
  private WLNetworkListener networkListener;

  private boolean addWatch(JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    WLLocationListener localWLLocationListener = getListener(paramJSONArray, 1, 2, paramCallbackContext);
    if (localWLLocationListener == null)
      return false;
    localWLLocationListener.addWatch(paramJSONArray.getString(0), paramCallbackContext, paramJSONArray.getInt(3), paramJSONArray.getInt(4));
    return true;
  }

  private boolean clearWatch(JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    String str = paramJSONArray.getString(0);
    if (this.gpsListener.watches.containsKey(str))
      this.gpsListener.clearWatch(str);
    while (true)
    {
      return true;
      if (!this.networkListener.watches.containsKey(str))
        continue;
      this.networkListener.clearWatch(str);
    }
  }

  private WLLocationListener getListener(JSONArray paramJSONArray, int paramInt1, int paramInt2, CallbackContext paramCallbackContext)
    throws JSONException
  {
    boolean bool = paramJSONArray.getBoolean(paramInt1);
    Criteria localCriteria = new Criteria();
    int j;
    int i;
    String str1;
    String str2;
    if (bool)
    {
      j = paramJSONArray.getInt(paramInt2);
      if (j < 100)
      {
        i = 3;
        localCriteria.setHorizontalAccuracy(i);
        str1 = this.locationManager.getBestProvider(localCriteria, true);
        if (i != 3)
          break label135;
        str2 = "ACCURACY_HIGH";
      }
    }
    while (true)
    {
      if (str1 != null)
        break label155;
      fail(WLLocationListener.PERMISSION_DENIED, "Android couldn't find a location provider for accuracy level " + str2 + " according to permissions in the Manifest and provider enablements in the device settings", paramCallbackContext, false);
      return null;
      if (j <= 500)
      {
        i = 2;
        break;
      }
      i = 1;
      break;
      i = 1;
      break;
      label135: if (i == 2)
      {
        str2 = "ACCURACY_MEDIUM";
        continue;
      }
      str2 = "ACCURACY_LOW";
    }
    label155: if (str1.equals("gps"))
      return this.gpsListener;
    if (i == 3)
      Log.w("WL Geolocation Plugin", "Network provider selected for accuracy level " + str2 + ". Check the manifest for the ACCESS_FINE_LOCATION permission");
    return this.networkListener;
  }

  private boolean getLocation(JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    int i = paramJSONArray.getInt(2);
    Location localLocation1 = this.gpsListener.getLastKnownLocation();
    Location localLocation2 = this.networkListener.getLastKnownLocation();
    Location localLocation3;
    if ((localLocation1 != null) && (localLocation2 != null))
      if (localLocation1.getTime() >= localLocation2.getTime())
        localLocation3 = localLocation1;
    while ((localLocation3 != null) && (System.currentTimeMillis() - localLocation3.getTime() <= i))
    {
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, returnLocationJSON(localLocation3)));
      return true;
      localLocation3 = localLocation2;
      continue;
      if (localLocation1 != null)
      {
        localLocation3 = localLocation1;
        continue;
      }
      localLocation3 = null;
      if (localLocation2 == null)
        continue;
      localLocation3 = localLocation2;
    }
    String str = paramJSONArray.getString(0);
    WLLocationListener localWLLocationListener = getListener(paramJSONArray, 1, 3, paramCallbackContext);
    if (localWLLocationListener == null)
      return false;
    localWLLocationListener.addCallback(str, paramCallbackContext, i);
    return true;
  }

  private boolean removeCallback(JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    String str = paramJSONArray.getString(0);
    if (this.gpsListener.callbacks.containsKey(str))
      this.gpsListener.removeCallback(str);
    while (true)
    {
      return true;
      if (!this.networkListener.callbacks.containsKey(str))
        continue;
      this.networkListener.removeCallback(str);
    }
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if (this.locationManager == null)
    {
      this.locationManager = ((LocationManager)this.cordova.getActivity().getSystemService("location"));
      this.networkListener = new WLNetworkListener(this.locationManager, this);
      this.gpsListener = new WLGPSListener(this.locationManager, this);
    }
    if (paramString.equals("getLocation"))
    {
      getLocation(paramJSONArray, paramCallbackContext);
      return true;
    }
    if (paramString.equals("addWatch"))
    {
      addWatch(paramJSONArray, paramCallbackContext);
      return true;
    }
    if (paramString.equals("clearWatch"))
    {
      clearWatch(paramJSONArray, paramCallbackContext);
      return true;
    }
    if (paramString.equals("removeCallback"))
    {
      removeCallback(paramJSONArray, paramCallbackContext);
      return true;
    }
    return false;
  }

  public void fail(int paramInt, String paramString, CallbackContext paramCallbackContext, boolean paramBoolean)
  {
    JSONObject localJSONObject = new JSONObject();
    String str = null;
    try
    {
      localJSONObject.put("code", paramInt);
      localJSONObject.put("message", paramString);
      if (localJSONObject != null)
      {
        localPluginResult = new PluginResult(PluginResult.Status.ERROR, localJSONObject);
        localPluginResult.setKeepCallback(paramBoolean);
        paramCallbackContext.sendPluginResult(localPluginResult);
        return;
      }
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        str = "{'code':" + paramInt + ",'message':'" + paramString.replaceAll("'", "'") + "'}";
        localJSONObject = null;
        continue;
        PluginResult localPluginResult = new PluginResult(PluginResult.Status.ERROR, str);
      }
    }
  }

  public boolean isGlobalListener(WLLocationListener paramWLLocationListener)
  {
    WLGPSListener localWLGPSListener = this.gpsListener;
    int i = 0;
    if (localWLGPSListener != null)
    {
      WLNetworkListener localWLNetworkListener = this.networkListener;
      i = 0;
      if (localWLNetworkListener != null)
        if (!this.gpsListener.equals(paramWLLocationListener))
        {
          boolean bool = this.networkListener.equals(paramWLLocationListener);
          i = 0;
          if (!bool);
        }
        else
        {
          i = 1;
        }
    }
    return i;
  }

  public void onDestroy()
  {
    if (this.networkListener != null)
    {
      this.networkListener.destroy();
      this.networkListener = null;
    }
    if (this.gpsListener != null)
    {
      this.gpsListener.destroy();
      this.gpsListener = null;
    }
  }

  public void onReset()
  {
    onDestroy();
  }

  public JSONObject returnLocationJSON(Location paramLocation)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("latitude", paramLocation.getLatitude());
      localJSONObject.put("longitude", paramLocation.getLongitude());
      if (paramLocation.hasAltitude());
      for (Double localDouble = Double.valueOf(paramLocation.getAltitude()); ; localDouble = null)
      {
        localJSONObject.put("altitude", localDouble);
        localJSONObject.put("accuracy", paramLocation.getAccuracy());
        boolean bool1 = paramLocation.hasBearing();
        Float localFloat = null;
        if (bool1)
        {
          boolean bool2 = paramLocation.hasSpeed();
          localFloat = null;
          if (bool2)
            localFloat = Float.valueOf(paramLocation.getBearing());
        }
        localJSONObject.put("heading", localFloat);
        localJSONObject.put("velocity", paramLocation.getSpeed());
        localJSONObject.put("timestamp", paramLocation.getTime());
        return localJSONObject;
      }
    }
    catch (JSONException localJSONException)
    {
      localJSONException.printStackTrace();
    }
    return localJSONObject;
  }

  public void win(Location paramLocation, CallbackContext paramCallbackContext, boolean paramBoolean)
  {
    PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, returnLocationJSON(paramLocation));
    localPluginResult.setKeepCallback(paramBoolean);
    paramCallbackContext.sendPluginResult(localPluginResult);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.WLGeolocationPlugin
 * JD-Core Version:    0.6.0
 */