package org.apache.cordova;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;

public class NetworkManager extends CordovaPlugin
{
  public static final String CDMA = "cdma";
  public static final String EDGE = "edge";
  public static final String EHRPD = "ehrpd";
  public static final String GPRS = "gprs";
  public static final String GSM = "gsm";
  public static final String HSDPA = "hsdpa";
  public static final String HSPA = "hspa";
  public static final String HSPA_PLUS = "hspa+";
  public static final String HSUPA = "hsupa";
  private static final String LOG_TAG = "NetworkManager";
  public static final String LTE = "lte";
  public static final String MOBILE = "mobile";
  public static int NOT_REACHABLE = 0;
  public static final String ONEXRTT = "1xrtt";
  public static int REACHABLE_VIA_CARRIER_DATA_NETWORK = 0;
  public static int REACHABLE_VIA_WIFI_NETWORK = 0;
  public static final String TYPE_2G = "2g";
  public static final String TYPE_3G = "3g";
  public static final String TYPE_4G = "4g";
  public static final String TYPE_ETHERNET = "ethernet";
  public static final String TYPE_NONE = "none";
  public static final String TYPE_UNKNOWN = "unknown";
  public static final String TYPE_WIFI = "wifi";
  public static final String UMB = "umb";
  public static final String UMTS = "umts";
  public static final String WIFI = "wifi";
  public static final String WIMAX = "wimax";
  private CallbackContext connectionCallbackContext;
  private String lastStatus = "";
  BroadcastReceiver receiver = null;
  private boolean registered = false;
  ConnectivityManager sockMan;

  private String getConnectionInfo(NetworkInfo paramNetworkInfo)
  {
    String str = "none";
    if (paramNetworkInfo != null)
      if (paramNetworkInfo.isConnected())
        break label44;
    label44: for (str = "none"; ; str = getType(paramNetworkInfo))
    {
      Log.d("CordovaNetworkManager", "Connection Type: " + str);
      return str;
    }
  }

  private String getType(NetworkInfo paramNetworkInfo)
  {
    if (paramNetworkInfo != null)
    {
      String str1 = paramNetworkInfo.getTypeName();
      if (str1.toLowerCase().equals("wifi"))
        return "wifi";
      if (str1.toLowerCase().equals("mobile"))
      {
        String str2 = paramNetworkInfo.getSubtypeName();
        if ((str2.toLowerCase().equals("gsm")) || (str2.toLowerCase().equals("gprs")) || (str2.toLowerCase().equals("edge")))
          return "2g";
        if ((str2.toLowerCase().startsWith("cdma")) || (str2.toLowerCase().equals("umts")) || (str2.toLowerCase().equals("1xrtt")) || (str2.toLowerCase().equals("ehrpd")) || (str2.toLowerCase().equals("hsupa")) || (str2.toLowerCase().equals("hsdpa")) || (str2.toLowerCase().equals("hspa")))
          return "3g";
        if ((str2.toLowerCase().equals("lte")) || (str2.toLowerCase().equals("umb")) || (str2.toLowerCase().equals("hspa+")))
          return "4g";
      }
    }
    else
    {
      return "none";
    }
    return "unknown";
  }

  private void sendUpdate(String paramString)
  {
    if (this.connectionCallbackContext != null)
    {
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, paramString);
      localPluginResult.setKeepCallback(true);
      this.connectionCallbackContext.sendPluginResult(localPluginResult);
    }
    this.webView.postMessage("networkconnection", paramString);
  }

  private void updateConnectionInfo(NetworkInfo paramNetworkInfo)
  {
    String str = getConnectionInfo(paramNetworkInfo);
    if (!str.equals(this.lastStatus))
    {
      sendUpdate(str);
      this.lastStatus = str;
    }
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    if (paramString.equals("getConnectionInfo"))
    {
      this.connectionCallbackContext = paramCallbackContext;
      NetworkInfo localNetworkInfo = this.sockMan.getActiveNetworkInfo();
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, getConnectionInfo(localNetworkInfo));
      localPluginResult.setKeepCallback(true);
      paramCallbackContext.sendPluginResult(localPluginResult);
      return true;
    }
    return false;
  }

  public void initialize(CordovaInterface paramCordovaInterface, CordovaWebView paramCordovaWebView)
  {
    super.initialize(paramCordovaInterface, paramCordovaWebView);
    this.sockMan = ((ConnectivityManager)paramCordovaInterface.getActivity().getSystemService("connectivity"));
    this.connectionCallbackContext = null;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    if (this.receiver == null)
    {
      this.receiver = new BroadcastReceiver()
      {
        public void onReceive(Context paramContext, Intent paramIntent)
        {
          if (NetworkManager.this.webView != null)
            NetworkManager.this.updateConnectionInfo(NetworkManager.this.sockMan.getActiveNetworkInfo());
        }
      };
      paramCordovaInterface.getActivity().registerReceiver(this.receiver, localIntentFilter);
      this.registered = true;
    }
  }

  public void onDestroy()
  {
    if ((this.receiver != null) && (this.registered));
    try
    {
      this.cordova.getActivity().unregisterReceiver(this.receiver);
      this.registered = false;
      return;
    }
    catch (Exception localException)
    {
      Log.e("NetworkManager", "Error unregistering network receiver: " + localException.getMessage(), localException);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.NetworkManager
 * JD-Core Version:    0.6.0
 */