package cn.domob.wall.core;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

class r
{
  static r a;
  private static p b = new p(r.class.getSimpleName());
  private static final String c = "id";
  private static final String d = "posit";
  private static final String e = "rt";
  private static final String f = "idv";
  private static final String g = "ts";
  private static final String h = "rnd";
  private static final String i = "vcode";
  private static final String j = "type";
  private static final String k = "order";
  private static final String l = "pkg";
  private static final String m = "failsafe";
  private static final String n = "network";

  static r a()
  {
    monitorenter;
    try
    {
      if (a == null)
        a = new r();
      r localr = a;
      return localr;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  class a
  {
    HashMap<String, String> a = new HashMap();
    AdInfo b;
    k c;

    a(k paramAdInfo, AdInfo paramn, r.n arg4)
    {
      this.c = paramAdInfo;
      this.b = paramn;
      this.a.put("sid", paramn.h());
      this.a.put("id", paramn.getAdId());
      a(this.a, paramn.d());
      r.n localn;
      a(k.b(), localn);
    }

    a(k paramn, r.n arg3)
    {
      this.c = paramn;
      r.n localn;
      a(k.b(), localn);
    }

    private void a(Context paramContext, r.n paramn)
    {
      try
      {
        this.a.put("sdk", String.valueOf(30));
        this.a.put("rt", String.valueOf(paramn.ordinal()));
        this.a.put("v", String.format("%s-%s-%s", new Object[] { "20131031", "android", "20130801" }));
        this.a.put("sv", "010101");
        this.a.put("idv", s.h(paramContext));
        this.a.put("ua", s.f(paramContext));
        this.a.put("ipb", this.c.c());
        this.a.put("ppid", this.c.d());
        this.a.put("network", s.n(paramContext));
        this.a.put("pb[name]", s.d(paramContext));
        this.a.put("pb[version]", s.c(paramContext));
        String str = s.y(paramContext);
        if (str != null)
          this.a.put("dma", v.a("d!j@d#g$r%s^j&h*", str.toUpperCase()));
        return;
      }
      catch (Exception localException)
      {
        r.b().a(localException);
      }
    }

    protected void a()
    {
      try
      {
        r.k localk = new r.k(r.this);
        if (DService.getEndpoint().equals("online"))
        {
          localk.a(k.b(), this.a, v.f(g.j));
          return;
        }
        localk.a(k.b(), this.a, v.f(g.n));
        return;
      }
      catch (Exception localException)
      {
        r.b().a(localException);
      }
    }

    protected void a(Map<String, String> paramMap, String paramString)
    {
      String str1 = this.c.c();
      String str2 = s.f(k.b());
      o.a locala = o.a(str1, str2, paramString);
      paramMap.put("tr", paramString);
      paramMap.put("ts", locala.a());
      paramMap.put("rnd", locala.b());
      paramMap.put("vcode", locala.c());
      paramMap.put("ipb", str1);
      paramMap.put("ua", str2);
    }
  }

  class b extends r.a
  {
    b(k paramAdInfo, AdInfo arg3)
    {
      super(paramAdInfo, localAdInfo, r.n.c);
    }

    public void a(r.i parami)
    {
      String str = "";
      switch (r.1.c[parami.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
      while (true)
      {
        this.a.put("type", str);
        return;
        str = "load_success";
        continue;
        str = "load_failed";
        continue;
        str = "load_cancel";
        continue;
        str = "close_lp";
        continue;
        str = "lp_url";
      }
    }
  }

  class c extends r.a
  {
    c(k paramAdInfo, AdInfo arg3)
    {
      super(paramAdInfo, localAdInfo, r.n.d);
    }

    protected void a()
    {
      try
      {
        r.k localk = new r.k(r.this);
        if (DService.getEndpoint().equals("online"))
          localk.a(k.b(), this.a, v.f(g.k));
        while ((this.b.f() != null) && (!((String)this.a.get("type")).equals("details_down")))
        {
          localk.a(k.b(), this.a, this.b.f());
          return;
          localk.a(k.b(), this.a, v.f(g.o));
        }
      }
      catch (Exception localException)
      {
        r.b().a(localException);
      }
    }

    public void a(int paramInt)
    {
      this.a.put("order", String.valueOf(paramInt));
    }

    public void a(r.j paramj)
    {
      String str = "";
      switch (r.1.e[paramj.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      }
      while (true)
      {
        this.a.put("type", str);
        return;
        str = "banner_ad";
        continue;
        str = "list_ad";
        continue;
        str = "listad_down";
        continue;
        str = "details_down";
      }
    }

    public void b(int paramInt)
    {
      this.a.put("posit", String.valueOf(paramInt));
    }
  }

  class d extends r.a
  {
    d(k paramAdInfo, AdInfo arg3)
    {
      super(paramAdInfo, localAdInfo, r.n.b);
      this.a.put("vc", String.valueOf(localAdInfo.getAdVersionCode()));
      this.a.put("vn", String.valueOf(localAdInfo.getAdVersionName()));
      this.a.put("pkg", localAdInfo.getAdPackageName());
      this.a.put("id", localAdInfo.getAdId());
    }

    void a(r.l paraml)
    {
      String str = "";
      switch (r.1.b[paraml.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      }
      while (true)
      {
        this.a.put("type", str);
        return;
        str = "download_start";
        continue;
        str = "download_repeat";
        continue;
        str = "download_finish";
        continue;
        str = "download_failed";
        continue;
        str = "download_cancel";
        continue;
        str = "install_success";
        continue;
        str = "run";
      }
    }
  }

  protected class e
  {
    protected static final String a = "load_success";
    protected static final String b = "load_failed";
    protected static final String c = "load_cancel";
    protected static final String d = "lp_url";
    protected static final String e = "close_lp";
    protected static final String f = "download_start";
    protected static final String g = "download_finish";
    protected static final String h = "download_cancel";
    protected static final String i = "download_failed";
    protected static final String j = "download_repeat";
    protected static final String k = "install_success";
    protected static final String l = "run";
    protected static final String m = "la_success";
    protected static final String n = "la_failed";
    protected static final String o = "la_failsafe_success";
    protected static final String p = "la_failsafe_failed";
    protected static final String q = "entry";
    protected static final String r = "exit";
    protected static final String s = "pgdn";
    protected static final String t = "start";
    protected static final String u = "banner_ad";
    protected static final String v = "list_ad";
    protected static final String w = "listad_down";
    protected static final String x = "details_down";

    protected e()
    {
    }
  }

  class f extends r.a
  {
    f(k arg2)
    {
      super(localk, r.n.e);
    }

    protected void a()
    {
      try
      {
        r.g localg = new r.g(r.this, k.b());
        if (DService.getEndpoint().equals("online"))
        {
          localg.a(this.a, String.valueOf(r.n.e.ordinal()), v.f(g.l));
          return;
        }
        localg.a(this.a, String.valueOf(r.n.e.ordinal()), v.f(g.p));
        return;
      }
      catch (Exception localException)
      {
        r.b().a(localException);
      }
    }

    void a(String paramString)
    {
      this.a.put("tr", paramString);
      a(this.a, paramString);
    }

    void b(String paramString)
    {
      this.a.put("sid", paramString);
    }
  }

  class g
    implements f.a
  {
    private Context b;
    private f c;

    protected g(Context arg2)
    {
      Object localObject;
      this.b = localObject;
    }

    public void a(f paramf)
    {
      int i = paramf.f();
      r.b().b("发送展现报告返回: " + i);
      if (i == 200)
      {
        r.b().b("Imp report finish.");
        return;
      }
      r.b().e("onConnectionFinished:respCode=" + i);
    }

    protected void a(HashMap<String, String> paramHashMap, String paramString1, String paramString2)
    {
      String str = v.a(paramHashMap);
      r.b().b("发送展现报告: " + str);
      this.c = new f(this.b, paramString2, "", null, "POST", str, 20000, this);
      this.c.run();
    }
  }

  class h extends r.a
  {
    h(k paramAdInfo, AdInfo arg3)
    {
      super(paramAdInfo, localAdInfo, r.n.g);
    }

    public void a(r.m paramm)
    {
      String str = "";
      switch (r.1.d[paramm.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      }
      while (true)
      {
        this.a.put("type", str);
        return;
        str = "la_success";
        continue;
        str = "la_failed";
        continue;
        str = "la_failsafe_success";
        continue;
        str = "la_failsafe_failed";
      }
    }

    public void a(String paramString)
    {
      if (paramString != null)
        this.a.put("failsafe", paramString);
    }
  }

  static enum i
  {
    static
    {
      i[] arrayOfi = new i[6];
      arrayOfi[0] = a;
      arrayOfi[1] = b;
      arrayOfi[2] = c;
      arrayOfi[3] = d;
      arrayOfi[4] = e;
      arrayOfi[5] = f;
      g = arrayOfi;
    }
  }

  static enum j
  {
    static
    {
      j[] arrayOfj = new j[5];
      arrayOfj[0] = a;
      arrayOfj[1] = b;
      arrayOfj[2] = c;
      arrayOfj[3] = d;
      arrayOfj[4] = e;
      f = arrayOfj;
    }
  }

  class k
    implements f.a
  {
    k()
    {
    }

    protected void a(Context paramContext, HashMap<String, String> paramHashMap, String paramString)
    {
      r.b().b("发送报告类型: " + (String)paramHashMap.get("type"));
      r.b().b("发送报告: " + paramHashMap.toString());
      f localf = new f(paramContext, paramString, "", null, "POST", v.a(paramHashMap), 20000, this);
      localf.a((String)paramHashMap.get("type"));
      localf.b();
    }

    public void a(f paramf)
    {
      int i = paramf.f();
      r.b().b("发送报告返回: " + i);
      if (i == 200)
      {
        r.b().b("Report " + paramf.a() + " finish.");
        return;
      }
      r.b().b("onConnectionFinished:respCode = " + i);
    }
  }

  static enum l
  {
    static
    {
      l[] arrayOfl = new l[8];
      arrayOfl[0] = a;
      arrayOfl[1] = b;
      arrayOfl[2] = c;
      arrayOfl[3] = d;
      arrayOfl[4] = e;
      arrayOfl[5] = f;
      arrayOfl[6] = g;
      arrayOfl[7] = h;
      i = arrayOfl;
    }
  }

  static enum m
  {
    static
    {
      m[] arrayOfm = new m[5];
      arrayOfm[0] = a;
      arrayOfm[1] = b;
      arrayOfm[2] = c;
      arrayOfm[3] = d;
      arrayOfm[4] = e;
      f = arrayOfm;
    }
  }

  static enum n
  {
    static
    {
      n[] arrayOfn = new n[7];
      arrayOfn[0] = a;
      arrayOfn[1] = b;
      arrayOfn[2] = c;
      arrayOfn[3] = d;
      arrayOfn[4] = e;
      arrayOfn[5] = f;
      arrayOfn[6] = g;
      h = arrayOfn;
    }
  }

  class o extends r.a
  {
    o(k arg2)
    {
      super(localk, r.n.f);
      this.a.put("sid", localk.f());
      a(this.a, "-1");
    }

    void a(DService.ReportUserActionType paramReportUserActionType)
    {
      String str = "";
      switch (r.1.a[paramReportUserActionType.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      }
      while (true)
      {
        this.a.put("type", str);
        return;
        str = "entry";
        continue;
        str = "exit";
        continue;
        str = "pgdn";
        continue;
        str = "start";
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.r
 * JD-Core Version:    0.6.0
 */