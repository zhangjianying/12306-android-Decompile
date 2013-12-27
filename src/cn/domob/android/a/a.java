package cn.domob.android.a;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import cn.domob.android.offerwall.DomobActivity;
import cn.domob.android.offerwall.l;
import cn.domob.android.offerwall.m;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

public class a
{
  private static final int A = 100;
  private static boolean F = false;
  private static boolean G = false;
  public static Hashtable<String, a> a;
  public static Hashtable<String, Integer> b;
  public static final String c = "typeCancel";
  public static final String d = "typeInstall";
  public static final String e = "actType";
  public static final String f = "appName";
  public static final String g = "appId";
  public static final String h = "notifyId";
  public static final String i = "downloadPath";
  public static final int j = 512;
  public static final int k = 513;
  private static m l = new m(a.class.getSimpleName());
  private static Context m = null;
  private static final int t = 1000;
  private static int u = 0;
  private static Hashtable<String, Integer> v;
  private static Vector<String> w;
  private static final int z = 5;
  private String B = "";
  private String C = "";
  private String D = "";
  private String E = null;
  private PendingIntent H;
  private Handler I = new b(this);
  private f J;
  private Notification n = null;
  private NotificationManager o = null;
  private int p = 0;
  private int q = 0;
  private c r = null;
  private String s = "";
  private final int x = 30;
  private int y = 0;

  static
  {
    a = new Hashtable();
    v = new Hashtable();
    b = new Hashtable();
    w = new Vector();
    F = true;
    G = true;
  }

  private a(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Context paramContext)
  {
    m = paramContext.getApplicationContext();
    this.B = paramString1;
    this.C = paramString2;
    this.D = paramString3;
    this.E = paramString5;
    l.a(a.class, "Start to download. appName: " + paramString2 + " pkgName: " + paramString4 + " fileName: " + paramString3);
    if (!v.containsKey(paramString3))
    {
      u = 1 + u;
      v.put(paramString3, Integer.valueOf(u));
    }
    for (this.p = u; ; this.p = ((Integer)v.get(paramString3)).intValue())
    {
      l.a(a.class, paramString2 + " notification_id is " + this.p);
      if (paramString4 != null)
      {
        b.put(paramString4, Integer.valueOf(this.p));
        w.add(paramString4);
        if (w.size() > 30)
        {
          l.a(a.class, "Remove " + (String)w.get(0) + " from AppPkgMapping");
          b.remove(w.get(0));
          w.remove(0);
        }
      }
      return;
      l.a(a.class, " notification_id for " + paramString3 + "already exists");
    }
  }

  public static Intent a(Context paramContext, String paramString1, String paramString2)
  {
    Uri localUri = Uri.parse(paramString1);
    h localh = new h(paramContext, b(localUri.getHost() + localUri.getPath()), paramString1, null);
    l.a(a.class.getSimpleName(), paramString2 + "  exists");
    String str = localh.a();
    if (str == null)
      return null;
    return a(str);
  }

  static Intent a(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setDataAndType(Uri.parse("file://" + paramString), "application/vnd.android.package-archive");
    return localIntent;
  }

  private void a(c paramc)
  {
    this.r = paramc;
  }

  private void a(String paramString, long paramLong)
  {
    l.b(this, "begin download in " + paramString);
    this.J = new f(this.B, paramString, paramLong, new d()
    {
      public void a(int paramInt)
      {
        a.l(a.this).sendEmptyMessage(paramInt);
      }

      public void a(String paramString)
      {
        a.e().c(a.this, a.b(a.this) + "下载线程出错，错误原因：" + paramString);
        a.this.b();
        if (a.c())
          a.b(a.this, paramString);
        if (a.j(a.this) != null)
          a.j(a.this).a(6, paramString);
      }
    }
    , m);
    this.J.start();
    a.put(this.D, this);
  }

  public static void a(String paramString1, String paramString2, String paramString3, Context paramContext, c paramc)
  {
    a(paramString1, paramString2, paramString3, paramContext, paramc, null, F);
  }

  public static void a(String paramString1, String paramString2, String paramString3, Context paramContext, c paramc, String paramString4, boolean paramBoolean)
  {
    l.a(a.class, "Download url: " + paramString1);
    Uri localUri = Uri.parse(paramString1);
    String str1 = localUri.getHost() + localUri.getPath();
    l.a(a.class, "Download uri path: " + localUri.getPath());
    l.a(a.class, "Download uri host: " + localUri.getHost());
    String str2 = b(str1);
    l.a(a.class, "Download filename(md5) " + str2);
    G = paramBoolean;
    if (a.containsKey(str2))
    {
      paramc.a(512, "当前应用已在下载");
      l.a(a.class, "App " + paramString2 + " is downloading");
      return;
    }
    if (a.size() == 1000)
    {
      paramc.a(513, "最大下载数为1000个");
      l.a(a.class, "Maximum download number is 1000");
      return;
    }
    a locala = new a(paramString1, paramString2, str2, paramString3, paramString4, paramContext);
    locala.a(paramc);
    if (G)
      locala.f();
    locala.i();
    paramc.a();
  }

  private static String b(String paramString)
  {
    return l.b(paramString);
  }

  private void c(String paramString)
  {
    PendingIntent localPendingIntent = PendingIntent.getActivity(m, this.p, new Intent(), 134217728);
    this.n.icon = 17301624;
    this.n.tickerText = (this.C + "下载失败");
    this.n.setLatestEventInfo(m, this.C + "下载失败", "", localPendingIntent);
    this.n.flags = 16;
    this.o.notify(this.p, this.n);
  }

  private void f()
  {
    this.n = new Notification();
    this.n.icon = 17301633;
    this.n.tickerText = (this.C + "正在下载，请稍候...");
    this.H = PendingIntent.getActivity(m, this.p, h(), 134217728);
    this.n.setLatestEventInfo(m, this.C + "正在下载，请稍候...", "", this.H);
    this.o = ((NotificationManager)m.getSystemService("notification"));
    g();
  }

  private void g()
  {
    this.o.notify(this.p, this.n);
  }

  private Intent h()
  {
    Intent localIntent = new Intent();
    localIntent.setClass(m, DomobActivity.class);
    localIntent.putExtra("appName", this.C);
    localIntent.putExtra("appId", this.D);
    localIntent.putExtra("actType", "typeCancel");
    localIntent.putExtra("DomobActivityType", 2);
    return localIntent;
  }

  private void i()
  {
    new h(m, this.D, this.B, new i()
    {
      public void a()
      {
        Log.e("DomobSDK", a.b(a.this) + "rom can't chmod");
        if (a.j(a.this) != null)
          a.j(a.this).a(5, "sd卡不存在");
        a.b(a.this, "sd卡不存在");
      }

      public void a(long paramLong1, long paramLong2, long paramLong3)
      {
        Log.e("DomobSDK", a.b(a.this) + "not enough size sdsize=" + paramLong2 + " romsize=" + paramLong3);
        if (a.j(a.this) != null)
          a.j(a.this).a(1, "空间不足");
        a.b(a.this, "空间不足");
      }

      public void a(String paramString)
      {
        a.e().a(a.class, a.b(a.this) + " already exists in " + paramString);
        a.a(a.this, paramString);
        a.a(a.this, a.h(a.this), 0L);
      }

      public void b(String paramString)
      {
        a.e().a(a.class, a.b(a.this) + " is download but not finished in " + paramString);
        a.a(a.this, paramString);
        File localFile = new File(a.h(a.this));
        a.a(a.this, a.h(a.this), localFile.length());
      }

      public void c(String paramString)
      {
        a.e().a(a.class, a.b(a.this) + " is  not download,it will download in " + paramString);
        a.a(a.this, paramString);
        a.a(a.this, a.h(a.this), 0L);
      }

      public void d(String paramString)
      {
        Log.e("DomobSDK", a.b(a.this) + "无法连接到下载地址");
        if (a.j(a.this) != null)
          a.j(a.this).a(5, a.b(a.this) + paramString);
        a.b(a.this, "无法连接到下载地址");
      }
    }).start();
  }

  public c a()
  {
    return this.r;
  }

  public void b()
  {
    l.a(a.class.getSimpleName(), "Stop download  and cancel notify if is UI mode " + this.p);
    if (this.J != null)
      this.J.b();
    this.o.cancel(this.p);
    a.remove(this.D);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.a.a
 * JD-Core Version:    0.6.0
 */