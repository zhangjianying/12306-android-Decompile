package org.apache.cordova;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.util.TimeZone;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Device extends CordovaPlugin
{
  public static final String TAG = "Device";
  public static String cordovaVersion = "2.6.0";
  public static String platform = "Android";
  public static String uuid;
  BroadcastReceiver telephonyReceiver = null;

  private void initTelephonyReceiver()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.PHONE_STATE");
    this.telephonyReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramContext, Intent paramIntent)
      {
        String str;
        if ((paramIntent != null) && (paramIntent.getAction().equals("android.intent.action.PHONE_STATE")) && (paramIntent.hasExtra("state")))
        {
          str = paramIntent.getStringExtra("state");
          if (!str.equals(TelephonyManager.EXTRA_STATE_RINGING))
            break label64;
          LOG.i("Device", "Telephone RINGING");
          Device.this.webView.postMessage("telephone", "ringing");
        }
        label64: 
        do
        {
          return;
          if (!str.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            continue;
          LOG.i("Device", "Telephone OFFHOOK");
          Device.this.webView.postMessage("telephone", "offhook");
          return;
        }
        while (!str.equals(TelephonyManager.EXTRA_STATE_IDLE));
        LOG.i("Device", "Telephone IDLE");
        Device.this.webView.postMessage("telephone", "idle");
      }
    };
    this.cordova.getActivity().registerReceiver(this.telephonyReceiver, localIntentFilter);
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if (paramString.equals("getDeviceInfo"))
    {
      JSONObject localJSONObject = new JSONObject();
      localJSONObject.put("uuid", uuid);
      localJSONObject.put("version", getOSVersion());
      localJSONObject.put("platform", platform);
      localJSONObject.put("name", getProductName());
      localJSONObject.put("cordova", cordovaVersion);
      localJSONObject.put("model", getModel());
      paramCallbackContext.success(localJSONObject);
      return true;
    }
    return false;
  }

  public String getCordovaVersion()
  {
    return cordovaVersion;
  }

  public String getModel()
  {
    return Build.MODEL;
  }

  public String getOSVersion()
  {
    return Build.VERSION.RELEASE;
  }

  public String getPlatform()
  {
    return platform;
  }

  public String getProductName()
  {
    return Build.PRODUCT;
  }

  public String getSDKVersion()
  {
    return Build.VERSION.SDK;
  }

  public String getTimeZoneID()
  {
    return TimeZone.getDefault().getID();
  }

  public String getUuid()
  {
    return Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), "android_id");
  }

  public void initialize(CordovaInterface paramCordovaInterface, CordovaWebView paramCordovaWebView)
  {
    super.initialize(paramCordovaInterface, paramCordovaWebView);
    uuid = getUuid();
    initTelephonyReceiver();
  }

  public void onDestroy()
  {
    this.cordova.getActivity().unregisterReceiver(this.telephonyReceiver);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.Device
 * JD-Core Version:    0.6.0
 */