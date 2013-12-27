package cn.domob.wall.core;

import android.content.Context;
import java.util.HashMap;

class d
{
  public static boolean a;
  private static p b = new p(d.class.getSimpleName());
  private static boolean c = false;
  private f d;
  private k e;
  private Context f;

  static
  {
    a = false;
  }

  public d(k paramk)
  {
    this.e = paramk;
    this.f = paramk.g();
  }

  private String b()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("sdk", String.valueOf(30));
    localHashMap.put("rt", String.valueOf(1));
    localHashMap.put("ts", String.valueOf(System.currentTimeMillis()));
    localHashMap.put("ua", s.f(this.f));
    localHashMap.put("ipb", this.e.c());
    localHashMap.put("ppid", this.e.d());
    localHashMap.put("v", String.format("%s-%s-%s", new Object[] { "20131031", "android", "20130801" }));
    localHashMap.put("sv", "010101");
    return v.a(localHashMap);
  }

  protected void a()
    throws c
  {
    if (c)
      throw new c();
    c = true;
    try
    {
      String str1 = b();
      b.b("控制请求参数:" + str1);
      if (DService.getEndpoint().equals("online"));
      for (this.d = new f(this.f, v.f(g.i), "", null, "POST", str1, 20000, null); ; this.d = new f(this.f, g.m, "", null, "POST", str1, 20000, null))
      {
        this.d.c();
        String str2 = this.d.e();
        b.b("控制请求返回:" + str2);
        if (str2 == null)
          break;
        s.a(new e(str2).a());
        a = true;
        return;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        b.a(localException);
        return;
        b.e("Config respStr is null.");
      }
    }
    finally
    {
      c = false;
    }
    throw localObject;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.d
 * JD-Core Version:    0.6.0
 */