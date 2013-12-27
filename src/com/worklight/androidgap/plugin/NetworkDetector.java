package com.worklight.androidgap.plugin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import com.worklight.common.WLUtils;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkDetector extends CordovaPlugin
{
  public static final String ACTION_GET_NETWORK_INFO = "getNetworkInfo";

  private JSONObject[] getAllIpAddresses()
    throws JSONException
  {
    JSONObject localJSONObject1 = new JSONObject();
    JSONObject localJSONObject2 = new JSONObject();
    HashMap localHashMap = new HashMap();
    localHashMap.put("en0", "wifiAddress");
    localHashMap.put("en1", "wifiAddress");
    localHashMap.put("eth0", "wifiAddress");
    localHashMap.put("pdp_ip0", "3GAddress");
    localHashMap.put("rmnet0", "3GAddress");
    while (true)
    {
      InetAddress localInetAddress;
      String str1;
      String str2;
      try
      {
        Enumeration localEnumeration1 = NetworkInterface.getNetworkInterfaces();
        if (!localEnumeration1.hasMoreElements())
          continue;
        NetworkInterface localNetworkInterface = (NetworkInterface)localEnumeration1.nextElement();
        Enumeration localEnumeration2 = localNetworkInterface.getInetAddresses();
        if (!localEnumeration2.hasMoreElements())
          continue;
        localInetAddress = (InetAddress)localEnumeration2.nextElement();
        if (localInetAddress.isLoopbackAddress())
          continue;
        str1 = localInetAddress.getHostAddress().toString();
        str2 = (String)localHashMap.get(localNetworkInterface.getName());
        if (str2 != null)
          continue;
        str2 = "3GAddress";
        int i = str1.indexOf("%");
        if (i == -1)
          continue;
        str1 = str1.substring(0, i);
        if ((localInetAddress instanceof Inet4Address))
        {
          localJSONObject1.put(str2, str1);
          continue;
          continue;
        }
      }
      catch (SocketException localSocketException)
      {
        return new JSONObject[] { localJSONObject1, localJSONObject2 };
      }
      if (!(localInetAddress instanceof Inet6Address))
        continue;
      localJSONObject2.put(str2, str1);
    }
  }

  public static String getPrimaryAddress(JSONObject[] paramArrayOfJSONObject)
    throws JSONException
  {
    JSONObject localJSONObject1 = paramArrayOfJSONObject[0];
    JSONObject localJSONObject2 = paramArrayOfJSONObject[1];
    boolean bool1 = localJSONObject1.has("wifiAddress");
    String str1 = null;
    if (bool1)
      str1 = localJSONObject1.getString("wifiAddress");
    boolean bool2 = localJSONObject1.has("3GAddress");
    String str2 = null;
    if (bool2)
      str2 = localJSONObject1.getString("3GAddress");
    if (str1 != null);
    for (String str3 = str1; ; str3 = str2)
    {
      if ((str3 == null) || (str3 == ""))
      {
        if (localJSONObject2.has("wifiAddress"))
          str1 = localJSONObject2.getString("wifiAddress");
        if (localJSONObject2.has("3GAddress"))
          str2 = localJSONObject2.getString("3GAddress");
        if (str1 == null)
          break;
        str3 = str1;
      }
      return str3;
    }
    return str2;
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    if ("getNetworkInfo".equals(paramString))
    {
      paramCallbackContext.success(getNetworkData(this.cordova.getActivity()));
      return true;
    }
    paramCallbackContext.error("Invalid action: " + paramString);
    return true;
  }

  public JSONObject getNetworkData(Context paramContext)
  {
    JSONObject localJSONObject = new JSONObject();
    String str1 = "false";
    String str2 = "false";
    Object localObject1 = "";
    Object localObject2 = "";
    while (true)
    {
      TelephonyManager localTelephonyManager;
      try
      {
        NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (localNetworkInfo == null)
          continue;
        str1 = Boolean.valueOf(localNetworkInfo.isConnected()).toString();
        str2 = Boolean.valueOf(localNetworkInfo.isRoaming()).toString();
        String str3 = localNetworkInfo.getTypeName();
        localObject1 = str3;
        try
        {
          String str7 = ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getSSID();
          localObject2 = str7;
          localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
          switch (localTelephonyManager.getNetworkType())
          {
          case 7:
            if (localTelephonyManager.getNetworkOperator() == null)
              break label481;
            if (!localTelephonyManager.getNetworkOperator().equals(""))
              break label428;
            break label481;
            String str4 = localTelephonyManager.getNetworkOperatorName() + (String)localObject4;
            if (Settings.System.getInt(paramContext.getContentResolver(), "airplane_mode_on", 0) == 0)
              break label467;
            bool = true;
            String str5 = Boolean.toString(bool);
            JSONObject[] arrayOfJSONObject = getAllIpAddresses();
            localJSONObject.put("isNetworkConnected", str1);
            localJSONObject.put("isAirplaneMode", str5);
            localJSONObject.put("isRoaming", str2);
            localJSONObject.put("networkConnectionType", localObject1);
            localJSONObject.put("wifiName", localObject2);
            localJSONObject.put("telephonyNetworkType", localObject3);
            localJSONObject.put("carrierName", str4);
            localJSONObject.put("ipv4Addresses", arrayOfJSONObject[0]);
            localJSONObject.put("ipv6Addresses", arrayOfJSONObject[1]);
            localJSONObject.put("ipAddress", getPrimaryAddress(arrayOfJSONObject));
            return localJSONObject;
          default:
          case 4:
          case 2:
          case 5:
          case 6:
          case 1:
          case 3:
          case 0:
          case 8:
          case 10:
          case 9:
          }
        }
        catch (Exception localException2)
        {
          WLUtils.warning("Diagnostics information is missing Wi-Fi info with " + localException2.getMessage() + ".  Ensure application permission ACCESS_WIFI_STATE in AndroidManifest.xml.", localException2);
          continue;
        }
      }
      catch (Exception localException1)
      {
        WLUtils.error("Diagnostics information is missing because of " + localException1.getMessage(), localException1);
        return localJSONObject;
      }
      Object localObject3 = "1xRTT";
      continue;
      label428: String str6 = "(" + localTelephonyManager.getNetworkOperator() + ")";
      Object localObject4 = str6;
      continue;
      label467: boolean bool = false;
      continue;
      localObject3 = "UNDETECTABLE";
      continue;
      label481: localObject4 = "";
      continue;
      localObject3 = "CDMA";
      continue;
      localObject3 = "EDGE";
      continue;
      localObject3 = "EVDO_0";
      continue;
      localObject3 = "EVDO_A";
      continue;
      localObject3 = "GPRS";
      continue;
      localObject3 = "UMTS";
      continue;
      localObject3 = "";
      continue;
      localObject3 = "HSDPA";
      continue;
      localObject3 = "HSPA";
      continue;
      localObject3 = "HSUPA";
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.NetworkDetector
 * JD-Core Version:    0.6.0
 */