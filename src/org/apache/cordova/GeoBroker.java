package org.apache.cordova;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoBroker extends CordovaPlugin
{
  private GPSListener gpsListener;
  private LocationManager locationManager;
  private NetworkListener networkListener;

  private void addWatch(String paramString, CallbackContext paramCallbackContext, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.gpsListener.addWatch(paramString, paramCallbackContext);
      return;
    }
    this.networkListener.addWatch(paramString, paramCallbackContext);
  }

  private void clearWatch(String paramString)
  {
    this.gpsListener.clearWatch(paramString);
    this.networkListener.clearWatch(paramString);
  }

  private void getCurrentLocation(CallbackContext paramCallbackContext, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.gpsListener.addCallback(paramCallbackContext);
      return;
    }
    this.networkListener.addCallback(paramCallbackContext);
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if (this.locationManager == null)
    {
      this.locationManager = ((LocationManager)this.cordova.getActivity().getSystemService("location"));
      this.networkListener = new NetworkListener(this.locationManager, this);
      this.gpsListener = new GPSListener(this.locationManager, this);
    }
    boolean bool;
    String str;
    if ((this.locationManager.isProviderEnabled("gps")) || (this.locationManager.isProviderEnabled("network")))
      if (paramString.equals("getLocation"))
      {
        bool = paramJSONArray.getBoolean(0);
        int i = paramJSONArray.getInt(1);
        LocationManager localLocationManager = this.locationManager;
        if (bool)
        {
          str = "gps";
          Location localLocation = localLocationManager.getLastKnownLocation(str);
          if ((localLocation == null) || (System.currentTimeMillis() - localLocation.getTime() > i))
            break label181;
          paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, returnLocationJSON(localLocation)));
        }
      }
    while (true)
    {
      return true;
      str = "network";
      break;
      label181: getCurrentLocation(paramCallbackContext, bool);
      continue;
      if (paramString.equals("addWatch"))
      {
        addWatch(paramJSONArray.getString(0), paramCallbackContext, paramJSONArray.getBoolean(1));
        continue;
      }
      if (paramString.equals("clearWatch"))
      {
        clearWatch(paramJSONArray.getString(0));
        continue;
      }
      return false;
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.NO_RESULT, "Location API is not available for this device."));
    }
  }

  public void fail(int paramInt, String paramString, CallbackContext paramCallbackContext)
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

  public boolean isGlobalListener(CordovaLocationListener paramCordovaLocationListener)
  {
    GPSListener localGPSListener = this.gpsListener;
    int i = 0;
    if (localGPSListener != null)
    {
      NetworkListener localNetworkListener = this.networkListener;
      i = 0;
      if (localNetworkListener != null)
        if (!this.gpsListener.equals(paramCordovaLocationListener))
        {
          boolean bool = this.networkListener.equals(paramCordovaLocationListener);
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

  public void win(Location paramLocation, CallbackContext paramCallbackContext)
  {
    paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, returnLocationJSON(paramLocation)));
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.GeoBroker
 * JD-Core Version:    0.6.0
 */