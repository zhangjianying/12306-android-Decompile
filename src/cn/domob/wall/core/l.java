package cn.domob.wall.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

class l
{
  private static p a = new p(l.class.getSimpleName());
  private static String b;
  private static int c = 0;
  private static String d;
  private static String e;
  private static String f;
  private static String g;
  private static String h;
  private static String i;
  private static String j;
  private static String k;
  private static String l;
  private static float m = 0.0F;
  private static float n = 0.0F;
  private static int o = 0;
  private static int p = 0;
  private static String q;
  private static final String r = "unknown";
  private static final String s = "gprs";
  private static final String t = "wifi";

  protected static String A(Context paramContext)
  {
    int i1 = 0;
    while (true)
    {
      JSONObject localJSONObject;
      try
      {
        if ((v.b(paramContext)) && (v.c(paramContext)))
        {
          List localList = ((WifiManager)paramContext.getSystemService("wifi")).getScanResults();
          ScanResult[] arrayOfScanResult = new ScanResult[localList.size()];
          int i2 = 0;
          if (i2 >= localList.size())
            continue;
          arrayOfScanResult[i2] = ((ScanResult)localList.get(i2));
          i2++;
          continue;
          Arrays.sort(arrayOfScanResult, new Comparator()
          {
            public int a(ScanResult paramScanResult1, ScanResult paramScanResult2)
            {
              int i = paramScanResult2.level - paramScanResult1.level;
              int j;
              if (i > 0)
                j = 1;
              do
              {
                return j;
                j = 0;
              }
              while (i >= 0);
              return -1;
            }
          });
          localJSONObject = new JSONObject();
          if (arrayOfScanResult.length > 20)
            break label242;
          i3 = arrayOfScanResult.length;
          if (i1 >= i3)
            break label226;
          ScanResult localScanResult = arrayOfScanResult[i1];
          String str1 = localScanResult.BSSID;
          Object localObject = localScanResult.SSID;
          if (v.a(str1))
            break label236;
          if (!v.a(str1))
            continue;
          localObject = "#";
          if (((String)localObject).length() <= 16)
            continue;
          String str3 = ((String)localObject).substring(0, 16);
          localObject = str3;
          try
          {
            String str2 = URLEncoder.encode((String)localObject, "UTF-8");
            localJSONObject.put(str1, str2);
          }
          catch (UnsupportedEncodingException localUnsupportedEncodingException)
          {
            a.a(localUnsupportedEncodingException);
          }
        }
      }
      catch (Exception localException)
      {
        a.a(localException);
      }
      return "";
      label226: String str4 = localJSONObject.toString();
      return str4;
      label236: i1++;
      continue;
      label242: int i3 = 20;
      i1 = 0;
    }
  }

  protected static String[] B(Context paramContext)
  {
    int i1 = -1;
    a.a(l.class.getSimpleName(), "getLocationBasedService");
    String[] arrayOfString = { "-1", "-1", "-1", "-1" };
    if ((!v.a(paramContext, "android.permission.ACCESS_COARSE_LOCATION")) && (!v.a(paramContext, "android.permission.ACCESS_FINE_LOCATION")))
    {
      a.d("No permission to access locationBaseInfo");
      return arrayOfString;
    }
    try
    {
      TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
      if (localTelephonyManager != null)
      {
        a.a(l.class.getSimpleName(), "tManager is not null");
        a.a(l.class.getSimpleName(), "Network Operator: " + localTelephonyManager.getNetworkOperator());
        int i2 = localTelephonyManager.getPhoneType();
        CellLocation localCellLocation = localTelephonyManager.getCellLocation();
        if (localCellLocation != null)
          switch (i2)
          {
          default:
            a.b("无法获取基站信息");
            break;
          case 1:
          case 2:
          }
        else
          while (true)
          {
            arrayOfString[0] = String.valueOf(i1);
            arrayOfString[1] = String.valueOf(i6);
            if ((localTelephonyManager.getNetworkOperator() == null) || (localTelephonyManager.getNetworkOperator().length() < 5))
              break label366;
            int i3 = Integer.valueOf(localTelephonyManager.getNetworkOperator().substring(0, 3)).intValue();
            int i4 = Integer.valueOf(localTelephonyManager.getNetworkOperator().substring(3, 5)).intValue();
            arrayOfString[2] = String.valueOf(i3);
            arrayOfString[3] = String.valueOf(i4);
            break label366;
            GsmCellLocation localGsmCellLocation = (GsmCellLocation)localCellLocation;
            if (localGsmCellLocation == null)
              break;
            i1 = ((GsmCellLocation)localGsmCellLocation).getCid();
            i6 = ((GsmCellLocation)localGsmCellLocation).getLac();
            continue;
            CdmaCellLocation localCdmaCellLocation = (CdmaCellLocation)localCellLocation;
            if (localCdmaCellLocation == null)
              break;
            i1 = ((CdmaCellLocation)localCdmaCellLocation).getBaseStationId();
            int i5 = ((CdmaCellLocation)localCdmaCellLocation).getNetworkId();
            i6 = i5;
          }
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        a.a(localException);
        break;
        int i6 = i1;
      }
    }
    label366: return arrayOfString;
  }

  public static String C(Context paramContext)
  {
    if ((l == null) && (s.a(9, true)));
    try
    {
      Class localClass = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
      Object localObject = localClass.getMethod("getAdvertisingIdInfo", new Class[] { Context.class }).invoke(localClass.newInstance(), new Object[] { paramContext });
      l = String.valueOf(localObject.getClass().getMethod("getId", new Class[0]).invoke(localObject, new Object[0]));
      return l;
    }
    catch (Exception localException)
    {
      while (true)
        a.a(localException);
    }
  }

  private static void D(Context paramContext)
  {
    a.b(l.class.getSimpleName(), "Start to get app info.");
    while (true)
    {
      ApplicationInfo localApplicationInfo;
      try
      {
        PackageManager localPackageManager = paramContext.getPackageManager();
        if (localPackageManager == null)
          continue;
        PackageInfo localPackageInfo = localPackageManager.getPackageInfo(paramContext.getPackageName(), 0);
        if (localPackageInfo == null)
          continue;
        b = localPackageInfo.packageName;
        c = localPackageInfo.versionCode;
        d = localPackageInfo.versionName;
        localApplicationInfo = localPackageManager.getApplicationInfo(paramContext.getPackageName(), 128);
        if (localApplicationInfo == null)
          break;
        if (localApplicationInfo.labelRes == 0)
          continue;
        e = paramContext.getResources().getString(localApplicationInfo.labelRes);
        return;
        if (localApplicationInfo.nonLocalizedLabel == null)
        {
          localObject = null;
          e = localObject;
          return;
        }
      }
      catch (Exception localException)
      {
        a.e(l.class.getSimpleName(), "Failed in getting app info.");
        a.a(localException);
        return;
      }
      String str = localApplicationInfo.nonLocalizedLabel.toString();
      Object localObject = str;
    }
  }

  protected static String a()
  {
    try
    {
      while (true)
      {
        Enumeration localEnumeration1 = NetworkInterface.getNetworkInterfaces();
        while (true)
          if (localEnumeration1.hasMoreElements())
          {
            Enumeration localEnumeration2 = ((NetworkInterface)localEnumeration1.nextElement()).getInetAddresses();
            if (!localEnumeration2.hasMoreElements())
              continue;
            InetAddress localInetAddress = (InetAddress)localEnumeration2.nextElement();
            if (localInetAddress.isLoopbackAddress())
              break;
            String str = localInetAddress.getHostAddress().toString();
            return str;
          }
      }
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return null;
  }

  protected static String a(Context paramContext)
  {
    if (b == null)
    {
      D(paramContext);
      if (!v.g(b))
      {
        Log.i("DrwSDK", "Current package name is " + b);
        Log.i("DrwSDK", "SDK_VERSION:010101,SDK_BUILD_DATE:20131031");
      }
    }
    return b;
  }

  protected static boolean a(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
      if (Build.VERSION.SDK_INT < paramInt);
    do
    {
      return true;
      return false;
    }
    while (Build.VERSION.SDK_INT > paramInt);
    return false;
  }

  protected static boolean a(Context paramContext, String paramString)
  {
    if ((paramString != null) && (!paramString.equals("")))
      try
      {
        if (paramContext.getPackageManager().getPackageInfo(paramString, 1) != null)
        {
          a.a("Already insalled pkgName = " + paramString);
          return true;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
      }
    return false;
  }

  protected static int b(Context paramContext)
  {
    if (b == null)
      D(paramContext);
    return c;
  }

  protected static String b()
  {
    return new SimpleDateFormat("Z").format(new Date());
  }

  protected static int c()
  {
    return a.a(a.a());
  }

  protected static String c(Context paramContext)
  {
    if (b == null)
      D(paramContext);
    return d;
  }

  protected static int d()
  {
    return a.b(a.a());
  }

  protected static String d(Context paramContext)
  {
    if (b == null)
      D(paramContext);
    return e;
  }

  protected static long e()
  {
    return a.c(a.a());
  }

  protected static String e(Context paramContext)
  {
    if (k == null)
      k = new WebView(paramContext).getSettings().getUserAgentString();
    return k;
  }

  protected static String f()
  {
    return Locale.getDefault().getLanguage();
  }

  protected static List<String> f(Context paramContext)
  {
    List localList = paramContext.getPackageManager().getInstalledPackages(0);
    ArrayList localArrayList = new ArrayList();
    if (localList != null)
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        PackageInfo localPackageInfo = (PackageInfo)localIterator.next();
        if (localPackageInfo == null)
          continue;
        String str = localPackageInfo.packageName;
        if ((str != null) && (str.length() > 0))
          localArrayList.add(str);
        a.b("the phone has been installed packageName: " + str);
      }
    }
    return localArrayList;
  }

  protected static int g()
  {
    return a.d(a.a());
  }

  protected static String g(Context paramContext)
  {
    try
    {
      if (f == null)
        f = ((TelephonyManager)paramContext.getSystemService("phone")).getDeviceId();
      return f;
    }
    catch (Exception localException)
    {
      while (true)
      {
        a.e(l.class.getSimpleName(), "Failed to get android ID.");
        a.a(localException);
      }
    }
  }

  protected static String h(Context paramContext)
  {
    try
    {
      if (g == null)
        g = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
      return g;
    }
    catch (Exception localException)
    {
      while (true)
      {
        a.e(l.class.getSimpleName(), "Failed to get android ID.");
        a.a(localException);
      }
    }
  }

  protected static String i(Context paramContext)
  {
    if (h == null)
    {
      if (Build.VERSION.RELEASE.length() <= 0)
        break label34;
      h = Build.VERSION.RELEASE.replace(",", "_");
    }
    while (true)
    {
      return h;
      label34: h = "1.5";
    }
  }

  protected static String j(Context paramContext)
  {
    if ((i == null) && (Build.MODEL.length() > 0))
      i = Build.MODEL.replace(",", "_");
    return i;
  }

  protected static String k(Context paramContext)
  {
    String str;
    if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1)
    {
      a.e(l.class.getSimpleName(), "Cannot access user's network type.  Permissions are not set.");
      str = "unknown";
    }
    int i1;
    while (true)
    {
      return str;
      NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo == null)
        break label77;
      i1 = localNetworkInfo.getType();
      if (i1 != 0)
        break;
      str = localNetworkInfo.getSubtypeName();
      if (str == null)
        return "gprs";
    }
    if (i1 == 1)
      return "wifi";
    label77: return "unknown";
  }

  protected static boolean l(Context paramContext)
  {
    try
    {
      NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo != null)
      {
        boolean bool = localNetworkInfo.isConnected();
        if (bool)
          return true;
      }
      return false;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
    return false;
  }

  protected static String m(Context paramContext)
  {
    try
    {
      if (j == null)
        j = ((TelephonyManager)paramContext.getSystemService("phone")).getNetworkOperatorName();
      return j;
    }
    catch (Exception localException)
    {
      while (true)
        a.a(localException);
    }
  }

  protected static String n(Context paramContext)
  {
    q = "v";
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if ((localDisplay.getOrientation() == 1) || (localDisplay.getOrientation() == 3))
      q = "h";
    return q;
  }

  protected static float o(Context paramContext)
  {
    try
    {
      if (m == 0.0F)
      {
        Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        localDisplay.getMetrics(localDisplayMetrics);
        m = localDisplayMetrics.density;
      }
      return m;
    }
    catch (Exception localException)
    {
      while (true)
        a.a(localException);
    }
  }

  protected static float p(Context paramContext)
  {
    try
    {
      if (n == 0.0F)
        n = paramContext.getResources().getDisplayMetrics().density;
      return n;
    }
    catch (Exception localException)
    {
      while (true)
        a.a(localException);
    }
  }

  protected static int q(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if (localDisplay != null)
      o = localDisplay.getWidth();
    return o;
  }

  protected static int r(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if (localDisplay != null)
      p = localDisplay.getHeight();
    return p;
  }

  public static String s(Context paramContext)
  {
    Cursor localCursor = t(paramContext);
    if ((localCursor != null) && (localCursor.getCount() > 0))
    {
      localCursor.moveToFirst();
      String str = localCursor.getString(localCursor.getColumnIndexOrThrow("apn"));
      localCursor.close();
      return str;
    }
    return "";
  }

  public static Cursor t(Context paramContext)
  {
    try
    {
      String str = k(paramContext);
      if ((str != null) && (str.equals("wifi")))
      {
        a.b("network is wifi, don't read apn.");
        return null;
      }
      Uri localUri = Uri.parse("content://telephony/carriers/preferapn");
      Cursor localCursor = paramContext.getContentResolver().query(localUri, null, null, null, null);
      return localCursor;
    }
    catch (Exception localException)
    {
      a.a(localException);
      return null;
    }
    catch (Error localError)
    {
      a.a(localError);
    }
    return null;
  }

  protected static String u(Context paramContext)
  {
    a locala = a.a();
    Location localLocation = a.a(locala, paramContext);
    if (localLocation != null)
      return a.a(locala, localLocation);
    return null;
  }

  protected static String v(Context paramContext)
  {
    if (v.b(paramContext))
      return ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    return null;
  }

  protected static String w(Context paramContext)
  {
    if ((v.b(paramContext)) && (v.c(paramContext)))
      return ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getBSSID();
    return null;
  }

  protected static String x(Context paramContext)
  {
    TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
    if (localTelephonyManager.getPhoneType() == 1)
    {
      GsmCellLocation localGsmCellLocation = (GsmCellLocation)localTelephonyManager.getCellLocation();
      if (localGsmCellLocation != null)
        return localGsmCellLocation.getLac() + "";
    }
    return "-1";
  }

  protected static String y(Context paramContext)
  {
    TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
    if (localTelephonyManager.getPhoneType() == 1)
    {
      GsmCellLocation localGsmCellLocation = (GsmCellLocation)localTelephonyManager.getCellLocation();
      if (localGsmCellLocation != null)
        return localGsmCellLocation.getCid() + "";
    }
    return "-1";
  }

  protected static String z(Context paramContext)
  {
    if ((v.b(paramContext)) && (v.c(paramContext)))
      return ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getSSID();
    return null;
  }

  private static class a
  {
    private static a a = new a();
    private static final long f = 600000L;
    private Location b;
    private int c = -1;
    private int d = -1;
    private boolean e = true;

    private Location a(Context paramContext)
    {
      try
      {
        if (!this.e)
          return null;
        LocationManager localLocationManager = (LocationManager)paramContext.getSystemService("location");
        if (localLocationManager != null)
          if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0)
            break label293;
        label287: label293: for (Location localLocation1 = localLocationManager.getLastKnownLocation("gps"); ; localLocation1 = null)
        {
          if (localLocation1 == null)
          {
            Location localLocation2 = localLocationManager.getLastKnownLocation("network");
            if ((localLocation2 != null) && (System.currentTimeMillis() - localLocation2.getTime() < 600000L))
            {
              this.c = 2;
              this.b = localLocation2;
              return localLocation2;
            }
          }
          else if (System.currentTimeMillis() - localLocation1.getTime() < 600000L)
          {
            this.c = 0;
            this.b = localLocation1;
            return localLocation1;
          }
          if ((paramContext != null) && ((this.b == null) || (System.currentTimeMillis() > 600000L + this.b.getTime())))
            monitorenter;
          while (true)
          {
            try
            {
              if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0)
                break label287;
              l.h().b("Trying to get locations from the network.");
              Criteria localCriteria = new Criteria();
              localCriteria.setAccuracy(2);
              localCriteria.setCostAllowed(false);
              str = localLocationManager.getBestProvider(localCriteria, true);
              if (str != null)
                continue;
              l.h().b(l.class.getSimpleName(), "No location providers are available.  Ads will not be geotargeted.");
              this.d = 0;
              return null;
              l.h().b(l.class.getSimpleName(), "Location provider setup successfully.");
              localLocationManager.requestLocationUpdates(str, 0L, 0.0F, new b(localLocationManager), paramContext.getMainLooper());
              monitorexit;
              b(paramContext);
              this.d = 2;
              return null;
            }
            finally
            {
              monitorexit;
            }
            Location localLocation3 = this.b;
            return localLocation3;
            String str = null;
          }
        }
      }
      catch (Exception localException)
      {
      }
      return null;
    }

    public static a a()
    {
      return a;
    }

    private String a(Location paramLocation)
    {
      String str = null;
      if (paramLocation != null)
      {
        str = paramLocation.getLatitude() + "," + paramLocation.getLongitude();
        l.h().b(l.class.getSimpleName(), "User coordinates are " + str);
      }
      return str;
    }

    private void a(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
      throws Exception
    {
      JSONObject localJSONObject1 = new JSONObject();
      localJSONObject1.put("version", "1.1.0");
      localJSONObject1.put("host", "maps.google.com");
      localJSONObject1.put("request_address", true);
      JSONArray localJSONArray = new JSONArray();
      JSONObject localJSONObject2 = new JSONObject();
      localJSONObject2.put("cell_id", paramInt1);
      localJSONObject2.put("location_area_code", paramInt2);
      localJSONObject2.put("mobile_country_code", paramInt4);
      localJSONObject2.put("mobile_network_code", paramInt3);
      localJSONArray.put(localJSONObject2);
      localJSONObject1.put("cell_towers", localJSONArray);
      l.h().a("Location send:" + localJSONObject1.toString());
      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
      HttpPost localHttpPost = new HttpPost("http://www.google.com/loc/json");
      localHttpPost.setEntity(new StringEntity(localJSONObject1.toString()));
      HttpEntity localHttpEntity = localDefaultHttpClient.execute(localHttpPost).getEntity();
      if (localHttpEntity != null)
      {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localHttpEntity.getContent()));
        StringBuffer localStringBuffer = new StringBuffer();
        for (String str1 = localBufferedReader.readLine(); str1 != null; str1 = localBufferedReader.readLine())
          localStringBuffer.append(str1);
        if (localStringBuffer != null)
        {
          JSONObject localJSONObject3 = new JSONObject(new JSONTokener(localStringBuffer.toString()));
          if ((localJSONObject3 != null) && (localJSONObject3.has("location")))
          {
            String str2 = localJSONObject3.optJSONObject("location").optString("longitude");
            String str3 = localJSONObject3.optJSONObject("location").optString("latitude");
            long l = System.currentTimeMillis();
            Location localLocation = new Location("jizhan");
            localLocation.setLongitude(Double.parseDouble(str2));
            localLocation.setLatitude(Double.parseDouble(str3));
            localLocation.setTime(l);
            a(localLocation, 1);
          }
        }
      }
    }

    private void a(Location paramLocation, int paramInt)
    {
      this.b = paramLocation;
      this.c = paramInt;
    }

    private int b()
    {
      if (this.b == null);
      for (int i = 0; ; i = (int)this.b.getAccuracy())
      {
        l.h().b("location accuracy is " + i + " meters");
        return i;
      }
    }

    private void b(Context paramContext)
    {
      new Thread(new Runnable(paramContext)
      {
        public void run()
        {
          l.h().a(l.class.getSimpleName(), "getLocationBasedService");
          try
          {
            TelephonyManager localTelephonyManager = (TelephonyManager)this.a.getSystemService("phone");
            if (localTelephonyManager != null)
            {
              l.h().a(l.class.getSimpleName(), "tManager is not null");
              l.h().a(l.class.getSimpleName(), "Network Operator: " + localTelephonyManager.getNetworkOperator());
              if ((localTelephonyManager.getNetworkOperator() != null) && (localTelephonyManager.getNetworkOperator().length() >= 5))
                switch (localTelephonyManager.getPhoneType())
                {
                case 1:
                  GsmCellLocation localGsmCellLocation = (GsmCellLocation)localTelephonyManager.getCellLocation();
                  if (localGsmCellLocation != null)
                  {
                    int i = ((GsmCellLocation)localGsmCellLocation).getCid();
                    int j = ((GsmCellLocation)localGsmCellLocation).getLac();
                    int k = Integer.valueOf(localTelephonyManager.getNetworkOperator().substring(0, 3)).intValue();
                    int m = Integer.valueOf(localTelephonyManager.getNetworkOperator().substring(3, 5)).intValue();
                    l.a.a(l.a.this, i, j, m, k);
                    return;
                  }
                }
            }
          }
          catch (Exception localException)
          {
            l.h().a(localException);
          }
        }
      }).start();
    }

    private int c()
    {
      switch (this.c)
      {
      default:
        l.h().b(l.class.getSimpleName(), "Unknown");
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        return this.c;
        l.h().b(l.class.getSimpleName(), "GPS");
        continue;
        l.h().b(l.class.getSimpleName(), "Base");
        continue;
        l.h().b(l.class.getSimpleName(), "Wifi");
      }
    }

    private int d()
    {
      switch (this.d)
      {
      case 0:
      case 1:
      case 2:
      }
      return this.d;
    }

    private long e()
    {
      if (this.b != null)
        return this.b.getTime();
      return 0L;
    }

    private class a
    {
      static final int a = 0;
      static final int b = 1;
      static final int c = 2;

      private a()
      {
      }
    }

    private class b
      implements LocationListener
    {
      public LocationManager a;

      b(LocationManager arg2)
      {
        Object localObject;
        this.a = localObject;
      }

      public final void onLocationChanged(Location paramLocation)
      {
        l.h().a(l.class.getSimpleName(), "onLocationChanged");
        l.a.a(l.a.this, paramLocation, 2);
        this.a.removeUpdates(this);
      }

      public final void onProviderDisabled(String paramString)
      {
      }

      public final void onProviderEnabled(String paramString)
      {
      }

      public final void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
      {
      }
    }

    private class c
    {
      static final int a = 0;
      static final int b = 1;
      static final int c = 2;

      private c()
      {
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.l
 * JD-Core Version:    0.6.0
 */