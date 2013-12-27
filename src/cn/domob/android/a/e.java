package cn.domob.android.a;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import cn.domob.android.offerwall.m;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

public class e
{
  private static m a = new m(e.class.getSimpleName());
  private static final String b = "wifi";
  private static final String c = "ctwap";

  protected static String a(Context paramContext)
  {
    String str;
    if (paramContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1)
    {
      a.e(e.class.getSimpleName(), "Cannot access user's network type.  Permissions are not set.");
      str = "";
    }
    int i;
    while (true)
    {
      return str;
      NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
      if (localNetworkInfo == null)
        break label74;
      i = localNetworkInfo.getType();
      if (i != 0)
        break;
      str = localNetworkInfo.getSubtypeName();
      if (str == null)
        return "GPRS";
    }
    if (i == 1)
      return "wifi";
    label74: return "";
  }

  protected static Proxy b(Context paramContext)
  {
    try
    {
      Cursor localCursor2 = c(paramContext);
      localCursor1 = localCursor2;
      if (localCursor1 != null);
      try
      {
        if (localCursor1.getCount() > 0)
        {
          localCursor1.moveToFirst();
          String str1 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("proxy"));
          int i = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("port"));
          String str2 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("apn"));
          a.a(e.class.getSimpleName(), "Proxy: " + str1 + " port: " + i + " apnType: " + str2);
          if ((str1 == null) || (str1.equals("")) || (i == 0) || (str2.equalsIgnoreCase("ctwap")))
            break label270;
          a.a(e.class.getSimpleName(), "download use proxy " + str1 + " port:" + i);
          localProxy2 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(str1, i));
          localProxy1 = localProxy2;
          if (localCursor1 != null)
            localCursor1.close();
        }
        do
        {
          return localProxy1;
          localProxy1 = null;
        }
        while (localCursor1 == null);
        localCursor1.close();
        return null;
      }
      catch (Exception localException1)
      {
        while (true)
        {
          a.a(localException1);
          Proxy localProxy1 = null;
        }
      }
    }
    catch (Exception localException2)
    {
      while (true)
      {
        Cursor localCursor1 = null;
        continue;
        label270: Proxy localProxy2 = null;
      }
    }
  }

  private static Cursor c(Context paramContext)
  {
    String str = a(paramContext);
    if ((str != null) && (str.equals("wifi")))
    {
      a.a(e.class.getSimpleName(), "Download network is wifi, don't read apn.");
      return null;
    }
    Uri localUri = Uri.parse("content://telephony/carriers/preferapn");
    return paramContext.getContentResolver().query(localUri, null, null, null, null);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.a.e
 * JD-Core Version:    0.6.0
 */