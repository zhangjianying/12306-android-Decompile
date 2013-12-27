package cn.domob.android.offerwall;

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
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

class f
{
  private static m a = new m(f.class.getSimpleName());
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
  private static int q = 0;
  private static int r = 0;
  private static String s;
  private static final String t = "sdk";
  private static final String u = "unknown";
  private static final String v = "gprs";
  private static final String w = "wifi";

  protected static String A(Context paramContext)
  {
    a locala = a.a();
    Location localLocation = a.a(locala, paramContext);
    if (localLocation != null)
      return a.a(locala, localLocation);
    return null;
  }

  private static void B(Context paramContext)
  {
    a.b(f.class.getSimpleName(), "Start to get app info.");
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
        a.e(f.class.getSimpleName(), "Failed in getting app info.");
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
      B(paramContext);
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
      B(paramContext);
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
      B(paramContext);
    return d;
  }

  protected static int d()
  {
    return a.b(a.a());
  }

  protected static String d(Context paramContext)
  {
    if (b == null)
      B(paramContext);
    return e;
  }

  protected static long e()
  {
    return a.c(a.a());
  }

  protected static String e(Context paramContext)
  {
    StringBuffer localStringBuffer;
    if (l == null)
    {
      localStringBuffer = new StringBuffer();
      localStringBuffer.append("android");
      localStringBuffer.append(",");
      localStringBuffer.append(",");
      if (Build.VERSION.RELEASE.length() <= 0)
        break label207;
      localStringBuffer.append(Build.VERSION.RELEASE.replaceAll(",", "_"));
    }
    while (true)
    {
      localStringBuffer.append(",");
      localStringBuffer.append(",");
      String str1 = Build.MODEL;
      if (str1.length() > 0)
        localStringBuffer.append(str1.replaceAll(",", "_"));
      localStringBuffer.append(",");
      String str2 = ((TelephonyManager)paramContext.getSystemService("phone")).getNetworkOperatorName();
      if (str2 != null)
        localStringBuffer.append(str2.replaceAll(",", "_"));
      localStringBuffer.append(",");
      localStringBuffer.append(",");
      localStringBuffer.append(",");
      l = localStringBuffer.toString();
      a.b(l.class.getSimpleName(), "getUserAgent:" + l);
      return l;
      label207: localStringBuffer.append("1.5");
    }
  }

  protected static String f(Context paramContext)
  {
    if (f == null)
    {
      if (!g(paramContext))
        break label32;
      a.b("Use emulator id");
      f = "-1,-1,emulator";
    }
    while (true)
    {
      return f;
      label32: a.b("Generate device id");
      f = j(paramContext);
    }
  }

  protected static boolean g(Context paramContext)
  {
    if (h == null)
      h = l(paramContext);
    return (h == null) && (i(paramContext)) && ("sdk".equalsIgnoreCase(Build.MODEL));
  }

  protected static boolean h(Context paramContext)
  {
    return (i(paramContext)) && ("sdk".equalsIgnoreCase(Build.MODEL));
  }

  protected static boolean i(Context paramContext)
  {
    String str = k(paramContext);
    if (str == null)
      return true;
    return str.replaceAll("0", "").equals("");
  }

  protected static String j(Context paramContext)
  {
    a.b("Start to generate device id");
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      String str2 = k(paramContext);
      if (str2 != null)
        localStringBuffer.append(str2);
      while (true)
      {
        localStringBuffer.append(",");
        String str3 = ((TelephonyManager)paramContext.getSystemService("phone")).getSubscriberId();
        if (str3 == null)
          break;
        localStringBuffer.append(str3);
        localStringBuffer.append(",");
        String str1 = l(paramContext);
        if (str1 == null)
          break label181;
        localStringBuffer.append(str1);
        a.b("Generated device id: " + localStringBuffer.toString());
        return localStringBuffer.toString();
        localStringBuffer.append("-1");
      }
    }
    catch (SecurityException localSecurityException)
    {
      while (true)
      {
        a.a(localSecurityException);
        Log.e("DomobSDK", "you must set READ_PHONE_STATE permisson in AndroidManifest.xml");
        continue;
        localStringBuffer.append("-1");
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        a.a(localException);
        continue;
        label181: a.a("Android ID is null, use -1 instead");
        localStringBuffer.append("-1");
      }
    }
  }

  protected static String k(Context paramContext)
  {
    try
    {
      if (g == null)
        g = ((TelephonyManager)paramContext.getSystemService("phone")).getDeviceId();
      return g;
    }
    catch (Exception localException)
    {
      while (true)
      {
        a.e(f.class.getSimpleName(), "Failed to get android ID.");
        a.a(localException);
      }
    }
  }

  protected static String l(Context paramContext)
  {
    try
    {
      if (h == null)
        h = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
      return h;
    }
    catch (Exception localException)
    {
      while (true)
      {
        a.e(f.class.getSimpleName(), "Failed to get android ID.");
        a.a(localException);
      }
    }
  }

  protected static String m(Context paramContext)
  {
    if (i == null)
    {
      if (Build.VERSION.RELEASE.length() <= 0)
        break label32;
      i = Build.VERSION.RELEASE.replace(",", "_");
    }
    while (true)
    {
      return i;
      label32: i = "1.5";
    }
  }

  protected static String n(Context paramContext)
  {
    if ((j == null) && (Build.MODEL.length() > 0))
      j = Build.MODEL.replace(",", "_");
    return j;
  }

  protected static String o(Context paramContext)
  {
    String str;
    if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1)
    {
      a.e(f.class.getSimpleName(), "Cannot access user's network type.  Permissions are not set.");
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

  protected static boolean p(Context paramContext)
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

  protected static String q(Context paramContext)
  {
    try
    {
      if (k == null)
        k = ((TelephonyManager)paramContext.getSystemService("phone")).getNetworkOperatorName();
      return k;
    }
    catch (Exception localException)
    {
      while (true)
        a.a(localException);
    }
  }

  protected static String r(Context paramContext)
  {
    s = "v";
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if ((localDisplay.getOrientation() == 1) || (localDisplay.getOrientation() == 3))
      s = "h";
    return s;
  }

  protected static float s(Context paramContext)
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

  protected static float t(Context paramContext)
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

  protected static int u(Context paramContext)
  {
    o = Math.round(w(paramContext) * (s(paramContext) / t(paramContext)));
    return o;
  }

  protected static int v(Context paramContext)
  {
    p = Math.round(x(paramContext) * (s(paramContext) / t(paramContext)));
    return p;
  }

  protected static int w(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if (localDisplay != null)
      q = localDisplay.getWidth();
    return q;
  }

  protected static int x(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    if (localDisplay != null)
      r = localDisplay.getHeight();
    return r;
  }

  protected static String y(Context paramContext)
  {
    Cursor localCursor = z(paramContext);
    if ((localCursor != null) && (localCursor.getCount() > 0))
    {
      localCursor.moveToFirst();
      String str = localCursor.getString(localCursor.getColumnIndexOrThrow("apn"));
      localCursor.close();
      return str;
    }
    return "";
  }

  protected static Cursor z(Context paramContext)
  {
    String str = o(paramContext);
    if ((str != null) && (str.equals("wifi")))
    {
      a.b("network is wifi, don't read apn.");
      return null;
    }
    Uri localUri = Uri.parse("content://telephony/carriers/preferapn");
    return paramContext.getContentResolver().query(localUri, null, null, null, null);
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
              f.f().b("Trying to get locations from the network.");
              Criteria localCriteria = new Criteria();
              localCriteria.setAccuracy(2);
              localCriteria.setCostAllowed(false);
              str = localLocationManager.getBestProvider(localCriteria, true);
              if (str != null)
                continue;
              f.f().b(f.class.getSimpleName(), "No location providers are available.  Ads will not be geotargeted.");
              this.d = 0;
              return null;
              f.f().b(f.class.getSimpleName(), "Location provider setup successfully.");
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

    protected static a a()
    {
      return a;
    }

    private String a(Location paramLocation)
    {
      String str = null;
      if (paramLocation != null)
      {
        str = paramLocation.getLatitude() + "," + paramLocation.getLongitude();
        f.f().b(f.class.getSimpleName(), "User coordinates are " + str);
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
      f.f().a("Location send:" + localJSONObject1.toString());
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
      switch (this.c)
      {
      default:
        f.f().b(f.class.getSimpleName(), "Unknown");
      case 0:
      case 1:
      case 2:
      }
      while (true)
      {
        return this.c;
        f.f().b(f.class.getSimpleName(), "GPS");
        continue;
        f.f().b(f.class.getSimpleName(), "Base");
        continue;
        f.f().b(f.class.getSimpleName(), "Wifi");
      }
    }

    private void b(Context paramContext)
    {
      new Thread(new Runnable(paramContext)
      {
        public void run()
        {
          f.f().a(f.class.getSimpleName(), "getLocationBasedService");
          try
          {
            TelephonyManager localTelephonyManager = (TelephonyManager)this.a.getSystemService("phone");
            if (localTelephonyManager != null)
            {
              f.f().a(f.class.getSimpleName(), "tManager is not null");
              f.f().a(f.class.getSimpleName(), "Network Operator: " + localTelephonyManager.getNetworkOperator());
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
                    f.a.a(f.a.this, i, j, m, k);
                    return;
                  }
                }
            }
          }
          catch (Exception localException)
          {
            f.f().a(localException);
          }
        }
      }).start();
    }

    private int c()
    {
      switch (this.d)
      {
      case 0:
      case 1:
      case 2:
      }
      return this.d;
    }

    private long d()
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
      protected LocationManager a;

      b(LocationManager arg2)
      {
        Object localObject;
        this.a = localObject;
      }

      public final void onLocationChanged(Location paramLocation)
      {
        f.f().a(f.class.getSimpleName(), "onLocationChanged");
        f.a.a(f.a.this, paramLocation, 2);
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
 * Qualified Name:     cn.domob.android.offerwall.f
 * JD-Core Version:    0.6.0
 */