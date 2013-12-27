package com.worklight.androidgap.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WifiPlugin extends CordovaPlugin
{
  private static final String[] MACArray;
  private static final String[] SSIDArray;
  public static final String TAG = "Wifi";
  private static final Random randomGenerator = new Random();
  private Context ctx;
  WifiManager wifiManager;

  static
  {
    SSIDArray = new String[] { "foo", "spam", "last" };
    MACArray = new String[] { "egg", "bar", "least" };
  }

  private static ScanResult createScanResult(String paramString1, String paramString2, int paramInt)
  {
    try
    {
      Constructor localConstructor = ScanResult.class.getDeclaredConstructors()[0];
      localConstructor.setAccessible(true);
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = "foo";
      arrayOfObject[1] = "bar";
      arrayOfObject[2] = "";
      arrayOfObject[3] = Integer.valueOf(10);
      arrayOfObject[4] = Integer.valueOf(-10);
      ScanResult localScanResult = (ScanResult)localConstructor.newInstance(arrayOfObject);
      localScanResult.SSID = paramString1;
      localScanResult.BSSID = paramString2;
      localScanResult.level = paramInt;
      return localScanResult;
    }
    catch (Exception localException)
    {
    }
    throw new AssertionError(localException);
  }

  private WifiConnection getCurrentSsid(Context paramContext)
  {
    boolean bool1 = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getNetworkInfo(1).isConnected();
    String str1 = null;
    String str2 = null;
    if (bool1)
    {
      WifiInfo localWifiInfo = this.wifiManager.getConnectionInfo();
      str1 = null;
      str2 = null;
      if (localWifiInfo != null)
      {
        boolean bool2 = localWifiInfo.getSSID().equals("");
        str1 = null;
        str2 = null;
        if (!bool2)
        {
          str2 = localWifiInfo.getSSID();
          if (str2.indexOf("\"") == 0)
            str2 = str2.substring(1, -1 + str2.length());
          str1 = localWifiInfo.getBSSID();
        }
      }
    }
    return new WifiConnection(str2, str1);
  }

  private static int getStrength(ScanResult paramScanResult)
  {
    int i = 100;
    if (isEmulator())
      i = paramScanResult.level;
    do
    {
      return i;
      if (Build.VERSION.SDK_INT >= 14)
        return WifiManager.calculateSignalLevel(paramScanResult.level, 101);
      if (paramScanResult.level <= -100)
        return 0;
    }
    while (paramScanResult.level >= -55);
    float f = 45;
    return (int)(i * (paramScanResult.level + 100) / f);
  }

  private static boolean isEmulator()
  {
    return (Build.PRODUCT.equals("sdk_x86")) || (Build.PRODUCT.equals("google_sdk")) || (Build.PRODUCT.equals("sdk"));
  }

  private JSONArray resultToJSONArray(Iterable<ScanResult> paramIterable)
  {
    WifiConnection localWifiConnection = getCurrentSsid(this.ctx);
    String str1 = localWifiConnection.getSSID();
    String str2 = localWifiConnection.getMAC();
    JSONArray localJSONArray = new JSONArray();
    try
    {
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
      {
        ScanResult localScanResult = (ScanResult)localIterator.next();
        JSONObject localJSONObject = new JSONObject();
        localJSONObject.put("MAC", localScanResult.BSSID);
        localJSONObject.put("SSID", localScanResult.SSID);
        localJSONObject.put("strength", getStrength(localScanResult));
        if ((localScanResult.SSID.equals(str1)) && (localScanResult.BSSID.equals(str2)))
          localJSONObject.put("connected", true);
        localJSONArray.put(localJSONObject);
      }
    }
    catch (JSONException localJSONException)
    {
      throw new AssertionError(localJSONException);
    }
    return localJSONArray;
  }

  private void scanWifi(CallbackContext paramCallbackContext)
  {
    this.ctx.registerReceiver(new BroadcastReceiver(paramCallbackContext)
    {
      public void onReceive(Context paramContext, Intent paramIntent)
      {
        WifiPlugin.this.ctx.unregisterReceiver(this);
        while (true)
        {
          try
          {
            if (!WifiPlugin.access$100())
              continue;
            Object localObject = new LinkedList();
            int i = 0;
            if (i >= 6)
              continue;
            int j = WifiPlugin.randomGenerator.nextInt(3);
            String str1 = WifiPlugin.SSIDArray[j];
            int k = WifiPlugin.randomGenerator.nextInt(3);
            String str2 = WifiPlugin.MACArray[k];
            int m = WifiPlugin.randomGenerator.nextInt(3);
            if (m != 0)
            {
              n = m * 10;
              ((Collection)localObject).add(WifiPlugin.access$500(str1, str2, n));
              i++;
              continue;
              localObject = WifiPlugin.this.wifiManager.getScanResults();
              JSONArray localJSONArray = WifiPlugin.this.resultToJSONArray((Iterable)localObject);
              this.val$callbackContext.success(localJSONArray);
              return;
            }
          }
          catch (Exception localException)
          {
            Log.e("Wifi", "WifiPlugin : Error getting scan results : " + localException.getMessage());
            this.val$callbackContext.error(0);
            return;
          }
          int n = 30;
        }
      }
    }
    , new IntentFilter("android.net.wifi.SCAN_RESULTS"));
    if (isEmulator())
    {
      Intent localIntent = new Intent("android.net.wifi.SCAN_RESULTS");
      this.ctx.sendBroadcast(localIntent);
    }
    while (true)
    {
      return;
      if (!this.wifiManager.isWifiEnabled())
      {
        Log.e("Wifi", "failed to start wifi scan, wifi is disabled");
        paramCallbackContext.error(1);
        return;
      }
      try
      {
        if (this.wifiManager.startScan())
          continue;
        Log.e("Wifi", "failed to start wifi scan");
        paramCallbackContext.error(2);
        return;
      }
      catch (Exception localException)
      {
        Log.e("Wifi", "WifiPlugin : Error initiating scan, reason : " + localException.getMessage());
        paramCallbackContext.error(0);
      }
    }
  }

  private static String toAllCaps(String paramString)
  {
    Matcher localMatcher = Pattern.compile("[A-Z]?[a-z]*").matcher(paramString);
    StringBuilder localStringBuilder = new StringBuilder();
    while (localMatcher.find())
    {
      localStringBuilder.append(localMatcher.group());
      if (localMatcher.hitEnd())
        continue;
      localStringBuilder.append("_");
    }
    return localStringBuilder.toString().toUpperCase();
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    this.ctx = this.cordova.getActivity();
    try
    {
      this.wifiManager = ((WifiManager)this.ctx.getSystemService("wifi"));
      Action localAction = Action.valueOf(toAllCaps(paramString));
      Log.d("Wifi", "WifiPlugin called with action: " + localAction);
      switch (2.$SwitchMap$com$worklight$androidgap$plugin$WifiPlugin$Action[localAction.ordinal()])
      {
      case 1:
        scanWifi(paramCallbackContext);
        return true;
      }
    }
    catch (Exception localException)
    {
      Log.e("Wifi", "Error initializing WIFI scanning, reason: " + localException.getMessage());
      return false;
    }
    return false;
  }

  static enum Action
  {
    static
    {
      Action[] arrayOfAction = new Action[1];
      arrayOfAction[0] = ACQUIRE_WIFI;
      $VALUES = arrayOfAction;
    }
  }

  private class WifiConnection
  {
    private String MAC = null;
    private String SSID = null;

    public WifiConnection(String paramString1, String arg3)
    {
      this.SSID = paramString1;
      Object localObject;
      this.MAC = localObject;
    }

    public String getMAC()
    {
      return this.MAC;
    }

    public String getSSID()
    {
      return this.SSID;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.WifiPlugin
 * JD-Core Version:    0.6.0
 */