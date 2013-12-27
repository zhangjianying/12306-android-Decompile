package cn.domob.android.offerwall;

import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import java.util.Locale;

class j
  implements e.a
{
  private static final String A = "cid";
  private static final String B = "json_data";
  private static final String C = "UTF-8";
  private static final String D = "zh-cn";
  private static m a = new m(j.class.getSimpleName());
  private static final String d = "http://duomeng.cn/api/12306/";
  private static final String e = "rt";
  private static final String f = "ts";
  private static final String g = "ua";
  private static final String h = "ipb";
  private static final String i = "idv";
  private static final String j = "sdk";
  private static final String k = "v";
  private static final String l = "l";
  private static final String m = "f";
  private static final String n = "e";
  private static final String o = "so";
  private static final String p = "sw";
  private static final String q = "sh";
  private static final String r = "sd";
  private static final String s = "pb[identifier]";
  private static final String t = "pb[version]";
  private static final String u = "d[coord]";
  private static final String v = "d[coord_timestamp]";
  private static final String w = "d[pc]";
  private static final String x = "d[dob]";
  private static final String y = "d[gender]";
  private static final String z = "network";
  private Context b;
  private a c;

  j(Context paramContext)
  {
    this.b = paramContext;
  }

  private String a()
  {
    HashMap localHashMap = b();
    if (localHashMap != null)
      localHashMap.putAll(c());
    return l.a(localHashMap);
  }

  private HashMap<String, String> b()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("sdk", String.valueOf(6));
    localHashMap.put("rt", String.valueOf(1));
    localHashMap.put("ts", String.valueOf(System.currentTimeMillis()));
    String str1 = f.e(this.b);
    if (str1 == null)
      str1 = "Android,,,,,,,,";
    localHashMap.put("ua", str1);
    String str2 = DomobOfferWallSetting.a();
    StringBuffer localStringBuffer;
    if ((str2 != null) && (str2.length() > 0))
    {
      localHashMap.put("ipb", str2);
      localHashMap.put("idv", f.f(this.b));
      localHashMap.put("v", "020000-android-20120726");
      String str3 = Locale.getDefault().getLanguage();
      if (str3 == null)
        break label416;
      localStringBuffer = new StringBuffer();
      localStringBuffer.append(str3.toLowerCase());
      String str4 = Locale.getDefault().getCountry();
      if (str4 != null)
      {
        localStringBuffer.append("-");
        localStringBuffer.append(str4.toLowerCase());
      }
    }
    label416: for (String str5 = localStringBuffer.toString(); ; str5 = "zh-cn")
    {
      localHashMap.put("l", str5);
      localHashMap.put("f", "json_data");
      localHashMap.put("e", "UTF-8");
      String str6 = DomobOfferWallSetting.b();
      if (str6 == null)
        str6 = "";
      if (str6 != null)
        localHashMap.put("cid", str6);
      localHashMap.put("so", f.r(this.b));
      localHashMap.put("sw", String.valueOf(f.w(this.b)));
      localHashMap.put("sh", String.valueOf(f.x(this.b)));
      localHashMap.put("sd", String.valueOf(f.t(this.b)));
      localHashMap.put("pb[identifier]", f.a(this.b));
      localHashMap.put("pb[version]", String.valueOf(f.b(this.b)));
      String str7 = f.A(this.b);
      if (str7 != null)
      {
        localHashMap.put("d[coord]", str7);
        localHashMap.put("d[coord_timestamp]", String.valueOf(f.e()));
      }
      String str8 = f.o(this.b);
      if (str8 != null)
        localHashMap.put("network", str8);
      return localHashMap;
      Log.e("DomobSDK", "publisher id is null or empty!");
      break;
    }
  }

  private HashMap<String, String> c()
  {
    HashMap localHashMap = new HashMap();
    String str1 = DomobOfferWallSetting.c();
    if ((str1 != null) && (str1.length() > 0))
      localHashMap.put("d[pc]", str1);
    String str2 = DomobOfferWallSetting.d();
    if ((str2 != null) && (str2.length() > 0))
      localHashMap.put("d[dob]", str2);
    String str3 = DomobOfferWallSetting.e();
    if ((str3 != null) && (str3.length() > 0))
      localHashMap.put("d[gender]", str3);
    return localHashMap;
  }

  public void a(e parame)
  {
    k localk;
    if (parame != null)
    {
      String str = parame.e();
      if ((str != null) && (str.length() != 0))
      {
        a.a("Ad resp string:" + str);
        localk = k.a(str);
        if (this.c != null)
          this.c.a(localk);
      }
    }
    do
    {
      return;
      a.e("Ad respStr is null.");
      localk = null;
      break;
      a.e("Connection error.");
    }
    while (this.c == null);
    this.c.a(null);
  }

  void a(a parama)
  {
    this.c = parama;
    String str = a();
    a.b("Request params: " + str);
    new e(this.b, "http://duomeng.cn/api/12306/", str, this).b();
  }

  static abstract interface a
  {
    public abstract void a(k paramk);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.j
 * JD-Core Version:    0.6.0
 */