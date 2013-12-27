package cn.domob.wall.core;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import cn.domob.wall.core.b.a;
import java.util.ArrayList;
import java.util.List;

public class s
{
  protected static final String A = "d[coord_acc]";
  protected static final String B = "d[coord_status]";
  protected static final String C = "d[coord_timestamp]";
  protected static final String D = "dma";
  protected static final String E = "areacode";
  protected static final String F = "cellid";
  protected static final String G = "language";
  protected static final String H = "odin1";
  protected static final String I = "locaccmeters";
  protected static final String J = "ama";
  protected static final String K = "ssid";
  protected static final String L = "scan";
  protected static final String M = "istab";
  protected static final String N = "aaid";
  private static p O = new p(s.class.getSimpleName());
  private static ArrayList<String> P = new ArrayList();
  private static String Q;
  private static int R = 0;
  private static int S = 0;
  private static String T;
  private static String U;
  private static final String V = "sdk";
  protected static final String a = "pb[identifier]";
  protected static final String b = "vc";
  protected static final String c = "vn";
  protected static final String d = "appname";
  protected static final String e = "useragent";
  protected static final String f = "ua";
  protected static final String g = "install";
  protected static final String h = "idv";
  protected static final String i = "imei";
  protected static final String j = "andoidid";
  protected static final String k = "osv";
  protected static final String l = "devicemodel";
  protected static final String m = "network";
  protected static final String n = "networkavailable";
  protected static final String o = "ip";
  protected static final String p = "timezone";
  protected static final String q = "carrier";
  protected static final String r = "orientation";
  protected static final String s = "isemulator";
  protected static final String t = "rsd";
  protected static final String u = "csd";
  protected static final String v = "rsw";
  protected static final String w = "rsh";
  protected static final String x = "csw";
  protected static final String y = "csh";
  protected static final String z = "d[coord]";

  public static String A(Context paramContext)
  {
    if (P.contains("areacode"))
      return "";
    return l.x(paramContext);
  }

  public static String B(Context paramContext)
  {
    if (P.contains("ama"))
      return "";
    return l.w(paramContext);
  }

  public static String C(Context paramContext)
  {
    if (P.contains("ssid"))
      return "";
    return l.z(paramContext);
  }

  public static String D(Context paramContext)
  {
    String str = "";
    if (!P.contains("scan"))
      str = l.A(paramContext);
    return str;
  }

  public static String[] E(Context paramContext)
  {
    if ((P.contains("areacode")) || (P.contains("cellid")))
      return new String[] { "-1", "-1", "-1", "-1" };
    return l.B(paramContext);
  }

  public static String F(Context paramContext)
  {
    if (P.contains("aaid"))
      return "";
    return l.C(paramContext);
  }

  private static boolean G(Context paramContext)
  {
    String str = j(paramContext);
    if (str == null)
      return true;
    return str.replaceAll("0", "").equals("");
  }

  private static String H(Context paramContext)
  {
    O.b("Start to generate device id");
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      String str2 = j(paramContext);
      if (str2 != null)
        localStringBuffer.append(str2);
      while (true)
      {
        localStringBuffer.append(",");
        localStringBuffer.append("-1");
        localStringBuffer.append(",");
        String str1 = k(paramContext);
        if (str1 == null)
          break;
        localStringBuffer.append(str1);
        O.b("Generated device id: " + localStringBuffer.toString());
        return localStringBuffer.toString();
        localStringBuffer.append("-1");
      }
    }
    catch (SecurityException localSecurityException)
    {
      while (true)
      {
        O.a(localSecurityException);
        O.e("you must set READ_PHONE_STATE permisson in AndroidManifest.xml");
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        O.a(localException);
        continue;
        O.a("Android ID is null, use -1 instead");
        localStringBuffer.append("-1");
      }
    }
  }

  public static String a(Context paramContext)
  {
    if (P.contains("pb[identifier]"))
      return "";
    return l.a(paramContext);
  }

  public static ArrayList<String> a()
  {
    return P;
  }

  public static void a(ArrayList<String> paramArrayList)
  {
    if (paramArrayList != null)
    {
      O.b("需要关闭的字段: " + paramArrayList);
      P = paramArrayList;
    }
  }

  public static boolean a(int paramInt, boolean paramBoolean)
  {
    return l.a(paramInt, paramBoolean);
  }

  public static boolean a(Context paramContext, String paramString)
  {
    if (P.contains("install"))
      return false;
    return l.a(paramContext, paramString);
  }

  public static int b(Context paramContext)
  {
    if (P.contains("vc"))
      return -1;
    return l.b(paramContext);
  }

  public static String b()
  {
    if (P.contains("ip"))
      return "";
    return l.a();
  }

  public static String c()
  {
    if (P.contains("timezone"))
      return "";
    return l.b();
  }

  public static String c(Context paramContext)
  {
    if (P.contains("vn"))
      return "";
    return l.c(paramContext);
  }

  public static int d()
  {
    if (P.contains("d[coord_acc]"))
      return -1;
    return l.c();
  }

  public static String d(Context paramContext)
  {
    if (P.contains("appname"))
      return "";
    return l.d(paramContext);
  }

  public static int e()
  {
    if (P.contains("d[coord_status]"))
      return -1;
    return l.d();
  }

  public static String e(Context paramContext)
  {
    if (P.contains("useragent"))
      return "";
    return l.e(paramContext);
  }

  public static long f()
  {
    if (P.contains("d[coord_timestamp]"))
      return -1L;
    return l.e();
  }

  public static String f(Context paramContext)
  {
    if (P.contains("ua"))
      return "";
    StringBuffer localStringBuffer;
    if (Q == null)
    {
      localStringBuffer = new StringBuffer();
      localStringBuffer.append("android");
      localStringBuffer.append(",");
      localStringBuffer.append(",");
      if (Build.VERSION.RELEASE.length() <= 0)
        break label216;
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
      String str2 = p(paramContext);
      if (str2 != null)
        localStringBuffer.append(str2.replaceAll(",", "_"));
      localStringBuffer.append(",");
      localStringBuffer.append(",");
      localStringBuffer.append(",");
      Q = localStringBuffer.toString();
      O.b(v.class.getSimpleName(), "getUserAgent:" + Q);
      return Q;
      label216: localStringBuffer.append("1.5");
    }
  }

  public static String g()
  {
    if (P.contains("language"))
      return "";
    return l.f();
  }

  protected static List<String> g(Context paramContext)
  {
    if (P.contains("install"))
      return new ArrayList();
    return l.f(paramContext);
  }

  public static int h()
  {
    if (P.contains("locaccmeters"))
      return -1;
    return l.g();
  }

  public static String h(Context paramContext)
  {
    if (P.contains("idv"))
      return "";
    if (U == null)
    {
      if (!i(paramContext))
        break label46;
      O.b("Use emulator id");
      U = "-1,-1,emulator";
    }
    while (true)
    {
      return U;
      label46: O.b("Generate device id");
      U = H(paramContext);
    }
  }

  public static boolean i(Context paramContext)
  {
    if (P.contains("isemulator"));
    do
    {
      return false;
      if (T != null)
        continue;
      T = k(paramContext);
    }
    while ((T != null) || (!G(paramContext)) || (!"sdk".equalsIgnoreCase(Build.MODEL)));
    return true;
  }

  public static String j(Context paramContext)
  {
    if (P.contains("imei"))
      return "-1";
    return l.g(paramContext);
  }

  public static String k(Context paramContext)
  {
    if (P.contains("andoidid"))
      return "-1";
    return l.h(paramContext);
  }

  public static String l(Context paramContext)
  {
    if (P.contains("osv"))
      return "";
    return l.i(paramContext);
  }

  public static String m(Context paramContext)
  {
    if (P.contains("devicemodel"))
      return "";
    return l.j(paramContext);
  }

  public static String n(Context paramContext)
  {
    if (P.contains("network"))
      return "";
    return l.k(paramContext);
  }

  public static boolean o(Context paramContext)
  {
    if (P.contains("networkavailable"))
      return false;
    return l.l(paramContext);
  }

  public static String p(Context paramContext)
  {
    if (P.contains("carrier"))
      return "";
    return l.m(paramContext);
  }

  public static String q(Context paramContext)
  {
    if (P.contains("orientation"))
      return "";
    return l.n(paramContext);
  }

  public static float r(Context paramContext)
  {
    if (P.contains("rsd"))
      return -1.0F;
    return l.o(paramContext);
  }

  public static float s(Context paramContext)
  {
    if (P.contains("csd"))
      return -1.0F;
    return l.p(paramContext);
  }

  public static int t(Context paramContext)
  {
    if (P.contains("rsw"))
      return -1;
    R = Math.round(v(paramContext) * (r(paramContext) / s(paramContext)));
    return R;
  }

  public static int u(Context paramContext)
  {
    if (P.contains("rsh"))
      return -1;
    S = Math.round(w(paramContext) * (r(paramContext) / s(paramContext)));
    return S;
  }

  public static int v(Context paramContext)
  {
    if (P.contains("csw"))
      return -1;
    return l.q(paramContext);
  }

  public static int w(Context paramContext)
  {
    if (P.contains("csh"))
      return -1;
    return l.r(paramContext);
  }

  public static String x(Context paramContext)
  {
    if (P.contains("d[coord]"))
      return "";
    return l.u(paramContext);
  }

  public static String y(Context paramContext)
  {
    if (P.contains("dma"))
      return "";
    return l.v(paramContext);
  }

  public static String z(Context paramContext)
  {
    if (P.contains("odin1"))
      return "";
    return a.a(paramContext.getApplicationContext());
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.s
 * JD-Core Version:    0.6.0
 */