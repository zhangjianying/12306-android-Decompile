package cn.domob.wall.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class n
{
  static n a;
  private static p b = new p(n.class.getSimpleName());
  private static final String c = "timestamp";
  private static final String d = "requestTimestamp";
  private static final String e = "cookieid";
  private static final String f = "ad_cookieid";

  static n a()
  {
    monitorenter;
    try
    {
      if (a == null)
        a = new n();
      n localn = a;
      return localn;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  long a(Context paramContext)
  {
    long l = paramContext.getSharedPreferences("timestamp", 0).getLong("requestTimestamp", 0L);
    p localp = b;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "requestTimestamp";
    arrayOfObject[1] = Long.valueOf(l);
    localp.b(String.format("Query  data from SharedPreferences  key=%s  value=%s", arrayOfObject));
    return l;
  }

  void a(Context paramContext, String paramString)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("cookieid", 0).edit();
    localEditor.putString("ad_cookieid", paramString);
    localEditor.commit();
    b.b(String.format("The SharedPreferences stored data key = %s  cookieid = %s", new Object[] { "ad_cookieid", paramString }));
  }

  void b(Context paramContext)
  {
    SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("timestamp", 0).edit();
    localEditor.putLong("requestTimestamp", System.currentTimeMillis() / 1000L);
    localEditor.commit();
    p localp = b;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "requestTimestamp";
    arrayOfObject[1] = Long.valueOf(System.currentTimeMillis());
    localp.b(String.format("The SharedPreferences stored data key = %s  timestamp = %s", arrayOfObject));
  }

  String c(Context paramContext)
  {
    String str = paramContext.getSharedPreferences("cookieid", 0).getString("ad_cookieid", "");
    b.b(String.format("Query  data from SharedPreferences  key=%s  value=%s", new Object[] { "ad_cookieid", str }));
    return str;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.n
 * JD-Core Version:    0.6.0
 */