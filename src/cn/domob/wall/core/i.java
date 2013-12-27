package cn.domob.wall.core;

import android.content.Context;
import java.util.HashMap;

class i
  implements f.a
{
  private static p a = new p(i.class.getSimpleName());
  private static final String e = "idv";
  private static final String f = "l";
  private static final String g = "so";
  private static final String h = "sw";
  private static final String i = "sh";
  private static final String j = "sd";
  private static final String k = "d[coord_timestamp]";
  private static final String l = "d[coord]";
  private static final String m = "d[coord_acc]";
  private static final String n = "d[coord_status]";
  private static final String o = "d[coord_accuracy]";
  private static final String p = "ama";
  private static final String q = "an";
  private static final String r = "lac";
  private static final String s = "cell";
  private static final String t = "mcc";
  private static final String u = "mnc";
  private static final String v = "scan";
  private static final String w = "aaid";
  private static final String x = "network";
  private static final String y = "c";
  private static final int z = 2;
  private a A;
  private f b;
  private k c;
  private Context d;

  public i(k paramk)
  {
    a.b("New instance of ...AdRequest.");
    this.c = paramk;
    this.d = paramk.g();
    this.A = paramk;
  }

  private String b()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("cid", n.a().c(this.d));
    localHashMap.put("sdk", String.valueOf(30));
    localHashMap.put("rt", String.valueOf(2));
    localHashMap.put("ts", String.valueOf(System.currentTimeMillis()));
    localHashMap.put("ua", s.f(this.d));
    localHashMap.put("ipb", this.c.c());
    localHashMap.put("ppid", this.c.d());
    localHashMap.put("idv", s.h(this.d));
    localHashMap.put("v", String.format("%s-%s-%s", new Object[] { "20131031", "android", "20130801" }));
    localHashMap.put("sv", "010101");
    localHashMap.put("l", s.g());
    localHashMap.put("c", String.format("%s,%s,%s", new Object[] { "ltx", "la", "iad" }));
    localHashMap.put("so", s.q(this.d));
    localHashMap.put("sw", String.valueOf(s.t(this.d)));
    localHashMap.put("sh", String.valueOf(s.u(this.d)));
    localHashMap.put("sd", String.valueOf(s.s(this.d)));
    localHashMap.put("pb[identifier]", s.a(this.d));
    localHashMap.put("pb[name]", s.d(this.d));
    localHashMap.put("pb[version]", s.c(this.d));
    localHashMap.put("network", s.n(this.d));
    String str1 = s.y(this.d);
    String str2;
    if (str1 != null)
    {
      a.b("des encode dma:" + str1.toUpperCase() + "-->" + v.a("d!j@d#g$r%s^j&h*", str1.toUpperCase()));
      if (str1.equals(""))
        localHashMap.put("dma", "");
    }
    else
    {
      str2 = s.z(this.d);
      if (!str2.equals(""))
        break label833;
      localHashMap.put("odin1", "");
      label429: String str3 = s.x(this.d);
      if (str3 == null)
        break label846;
      localHashMap.put("d[coord_timestamp]", String.valueOf(s.f()));
      localHashMap.put("d[coord]", str3);
      localHashMap.put("d[coord_acc]", String.valueOf(s.d()));
      localHashMap.put("d[coord_accuracy]", String.valueOf(s.h()));
    }
    while (true)
    {
      String[] arrayOfString = s.E(this.d);
      p localp = a;
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = arrayOfString[0];
      arrayOfObject[1] = arrayOfString[1];
      arrayOfObject[2] = arrayOfString[2];
      arrayOfObject[3] = arrayOfString[3];
      localp.b(String.format("Base info: cid=%s, lac=%s, mcc=%s, mnc=%s", arrayOfObject));
      localHashMap.put("cell", arrayOfString[0]);
      localHashMap.put("lac", arrayOfString[1]);
      localHashMap.put("mcc", arrayOfString[2]);
      localHashMap.put("mnc", arrayOfString[3]);
      String str4 = s.B(this.d);
      if (str4 != null)
      {
        a.b("des encode ama:" + str4.toUpperCase() + "-->" + v.a("d!j@d#g$r%s^j&h*", str4.toUpperCase()));
        localHashMap.put("ama", v.a("d!j@d#g$r%s^j&h*", str4.toUpperCase()));
      }
      String str5 = s.C(this.d);
      if (str5 != null)
      {
        a.b("des encode apSSID:" + str5.toUpperCase() + "-->" + v.a("d!j@d#g$r%s^j&h*", str5));
        localHashMap.put("an", v.a("d!j@d#g$r%s^j&h*", str5));
      }
      String str6 = s.F(this.d);
      if (!v.a(str6))
        localHashMap.put("aaid", str6);
      a.b("广告请求参数:" + localHashMap.toString());
      return v.a(localHashMap);
      localHashMap.put("dma", v.a("d!j@d#g$r%s^j&h*", str1.toUpperCase()));
      break;
      label833: localHashMap.put("odin1", str2);
      break label429;
      label846: localHashMap.put("d[coord_status]", String.valueOf(s.e()));
    }
  }

  protected void a()
  {
    a.b("Start to request ad.");
    try
    {
      String str = b();
      if (DService.getEndpoint().equals("online"))
      {
        this.b = new f(this.d, v.f(g.i), "", null, "POST", str, 20000, this);
        this.b.b();
        return;
      }
      this.b = new f(this.d, g.m, "", null, "POST", str, 20000, this);
      this.b.b();
      return;
    }
    catch (Exception localException)
    {
      a.e("Error happens when sending ad request");
      a.a(localException);
    }
  }

  public void a(f paramf)
  {
    String str = paramf.e();
    a.b("广告请求返回:" + str);
    if ((str != null) && (str.length() != 0));
    for (j localj = new j().a(this.d, str); ; localj = null)
    {
      if (this.A != null)
        this.A.a(localj, paramf.f());
      return;
      a.e("Ad respStr is null.");
    }
  }

  public static abstract interface a
  {
    public abstract void a(j paramj, int paramInt);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.i
 * JD-Core Version:    0.6.0
 */