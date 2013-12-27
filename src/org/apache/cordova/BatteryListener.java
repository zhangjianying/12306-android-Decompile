package org.apache.cordova;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BatteryListener extends CordovaPlugin
{
  private static final String LOG_TAG = "BatteryManager";
  private CallbackContext batteryCallbackContext = null;
  BroadcastReceiver receiver = null;

  private JSONObject getBatteryInfo(Intent paramIntent)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("level", paramIntent.getIntExtra("level", 0));
      int i = paramIntent.getIntExtra("plugged", -1);
      boolean bool = false;
      if (i > 0)
        bool = true;
      localJSONObject.put("isPlugged", bool);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      Log.e("BatteryManager", localJSONException.getMessage(), localJSONException);
    }
    return localJSONObject;
  }

  private void removeBatteryListener()
  {
    if (this.receiver != null);
    try
    {
      this.cordova.getActivity().unregisterReceiver(this.receiver);
      this.receiver = null;
      return;
    }
    catch (Exception localException)
    {
      Log.e("BatteryManager", "Error unregistering battery receiver: " + localException.getMessage(), localException);
    }
  }

  private void sendUpdate(JSONObject paramJSONObject, boolean paramBoolean)
  {
    if (this.batteryCallbackContext != null)
    {
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, paramJSONObject);
      localPluginResult.setKeepCallback(paramBoolean);
      this.batteryCallbackContext.sendPluginResult(localPluginResult);
    }
  }

  private void updateBatteryInfo(Intent paramIntent)
  {
    sendUpdate(getBatteryInfo(paramIntent), true);
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    if (paramString.equals("start"))
    {
      if (this.batteryCallbackContext != null)
      {
        paramCallbackContext.error("Battery listener already running.");
        return true;
      }
      this.batteryCallbackContext = paramCallbackContext;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.BATTERY_CHANGED");
      if (this.receiver == null)
      {
        this.receiver = new BroadcastReceiver()
        {
          public void onReceive(Context paramContext, Intent paramIntent)
          {
            BatteryListener.this.updateBatteryInfo(paramIntent);
          }
        };
        this.cordova.getActivity().registerReceiver(this.receiver, localIntentFilter);
      }
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
      localPluginResult.setKeepCallback(true);
      paramCallbackContext.sendPluginResult(localPluginResult);
      return true;
    }
    if (paramString.equals("stop"))
    {
      removeBatteryListener();
      sendUpdate(new JSONObject(), false);
      this.batteryCallbackContext = null;
      paramCallbackContext.success();
      return true;
    }
    return false;
  }

  public void onDestroy()
  {
    removeBatteryListener();
  }

  public void onReset()
  {
    removeBatteryListener();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.BatteryListener
 * JD-Core Version:    0.6.0
 */