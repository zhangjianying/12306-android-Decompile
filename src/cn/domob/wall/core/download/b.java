package cn.domob.wall.core.download;

import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import cn.domob.wall.core.p;
import cn.domob.wall.core.v;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

public class b
{
  private static Hashtable<String, Integer> A;
  private static Vector<String> B;
  private static final int E = 5;
  private static final int F = 100;
  private static boolean L = false;
  private static boolean M = false;
  public static Hashtable<String, b> a;
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
  public static final int l = 516;
  public static final int m = 0;
  public static final int n = 1;
  public static final int o = 2;
  private static p p = new p(b.class.getSimpleName());
  private static Context q = null;
  private static Context r = null;
  private static final int y = 1000;
  private static int z = 0;
  private final int C = 30;
  private int D = 0;
  private String G = "";
  private String H = "";
  private String I = "";
  private String J = null;
  private boolean K = false;
  private int N = 2;
  private PendingIntent O;
  private Handler P = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      super.handleMessage(paramMessage);
      if ((paramMessage.what - b.a(b.this) < 5) && (paramMessage.what != 100));
      do
      {
        return;
        b.a(b.this, paramMessage.what);
        b.b(b.this, paramMessage.what);
        if (!b.c())
          continue;
        b.d(b.this).setLatestEventInfo(b.d(), b.b(b.this) + "正在下载", "已下载" + paramMessage.what + "%", b.c(b.this));
        b.f(b.this).notify(b.e(b.this), b.d(b.this));
      }
      while (b.g(b.this) != 100);
      b.e().a(b.class, b.b(b.this) + " download success");
      File localFile1 = new File(b.h(b.this));
      if (b.h(b.this).endsWith(".temp"))
      {
        b.a(b.this, b.h(b.this).substring(0, b.h(b.this).length() - ".temp".length()));
        File localFile2 = new File(b.h(b.this));
        b.e().a(b.class, b.b(b.this) + "FileName change to " + localFile2);
        localFile1.renameTo(localFile2);
      }
      Object localObject;
      if (b.i(b.this) == 0)
      {
        Bitmap localBitmap = BitmapFactory.decodeFile(localFile1.getAbsolutePath());
        int i = 0;
        String str;
        for (localObject = null; (i < 2) && (localObject == null); localObject = str)
        {
          str = MediaStore.Images.Media.insertImage(b.d().getContentResolver(), localBitmap, localFile1.getName(), localFile1.getName());
          i++;
        }
        b.e().b("fetch picture ablum path " + localObject);
        if (localObject == null)
        {
          b.e().b("insert " + localFile1.getAbsolutePath() + " failed, change the resType to other");
          b.c(b.this, 1);
        }
      }
      while (true)
      {
        b.a.remove(b.j(b.this));
        if (b.k(b.this) != null)
          b.k(b.this).a(b.h(b.this));
        b.b(b.this, 0);
        if (!b.c())
          break;
        b.d(b.this).icon = 17301634;
        b.d(b.this).tickerText = (b.b(b.this) + "下载完毕");
        b.d(b.this).flags = 16;
        Intent localIntent1;
        if (b.l(b.this))
          if (b.i(b.this) == 0)
            localIntent1 = b.b(localObject);
        while (true)
        {
          b.a(b.this, PendingIntent.getActivity(b.d(), b.e(b.this), localIntent1, 134217728));
          b.d(b.this).setLatestEventInfo(b.d(), b.b(b.this) + "下载完毕", "", b.c(b.this));
          b.f(b.this).notify(b.e(b.this), b.d(b.this));
          if ((b.m(b.this) == null) || (b.m(b.this).equalsIgnoreCase(v.d(b.h(b.this)))))
            break;
          b.e().b(b.d(), "md5 authorized failed");
          b.f(b.this).cancel(b.e(b.this));
          return;
          localIntent1 = new Intent();
          continue;
          localIntent1 = b.a(b.h(b.this));
        }
        Intent localIntent2;
        if (b.l(b.this))
          if (b.i(b.this) == 0)
            localIntent2 = b.b(localObject);
        while (localIntent2 != null)
        {
          localIntent2.addFlags(268435456);
          b.d().startActivity(localIntent2);
          return;
          new AlertDialog.Builder(b.f()).setTitle("确认").setMessage(b.b(b.this) + "已被下载到路径:" + b.h(b.this)).setPositiveButton("确定", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramDialogInterface, int paramInt)
            {
            }
          }).show();
          b.f(b.this).cancel(b.e(b.this));
          localIntent2 = null;
          continue;
          localIntent2 = b.a(b.h(b.this));
        }
        break;
        localObject = null;
      }
    }
  };
  private f Q;
  private Notification s = null;
  private NotificationManager t = null;
  private int u = 0;
  private int v = 0;
  private c w = null;
  private String x = "";

  static
  {
    a = new Hashtable();
    A = new Hashtable();
    b = new Hashtable();
    B = new Vector();
    L = true;
    M = true;
  }

  private b(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Context paramContext, int paramInt)
  {
    r = paramContext;
    q = paramContext.getApplicationContext();
    this.G = paramString1;
    this.H = paramString2;
    this.I = paramString3;
    if (paramInt != 2)
      this.K = true;
    this.N = paramInt;
    this.J = paramString5;
    p.a(b.class, "Start to download. appName: " + paramString2 + " pkgName: " + paramString4 + " fileName: " + paramString3);
    if (!A.containsKey(paramString3))
    {
      z = 1 + z;
      A.put(paramString3, Integer.valueOf(z));
    }
    for (this.u = z; ; this.u = ((Integer)A.get(paramString3)).intValue())
    {
      p.a(b.class, paramString2 + " notification_id is " + this.u);
      if (paramString4 != null)
      {
        b.put(paramString4, Integer.valueOf(this.u));
        B.add(paramString4);
        if (B.size() > 30)
        {
          p.a(b.class, "Remove " + (String)B.get(0) + " from AppPkgMapping");
          b.remove(B.get(0));
          B.remove(0);
        }
      }
      return;
      p.a(b.class, " notification_id for " + paramString3 + "already exists");
    }
  }

  public static Intent a(Context paramContext, String paramString1, String paramString2)
  {
    return a(paramContext, paramString1, paramString2, false, null);
  }

  public static Intent a(Context paramContext, String paramString1, String paramString2, boolean paramBoolean, String paramString3)
  {
    Uri localUri = Uri.parse(paramString1);
    Object localObject = c(localUri.getHost() + localUri.getPath());
    if ((paramBoolean) && (paramString3 != null))
      localObject = paramString3;
    String str1;
    while (true)
    {
      str1 = new n(paramContext, (String)localObject, paramString1, paramBoolean, null).a();
      if (str1 != null)
        break;
      return null;
      if ((!paramBoolean) || (paramString3 != null))
        continue;
      try
      {
        String str2 = localUri.getLastPathSegment();
        localObject = str2;
      }
      catch (Error localError)
      {
        p.a(localError);
      }
    }
    if (!paramBoolean)
    {
      p.a(b.class.getSimpleName(), paramString2 + "  exists");
      return a(str1);
    }
    p.a(b.class.getSimpleName(), paramString3 + "  exists");
    return (Intent)e(str1);
  }

  static Intent a(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setDataAndType(Uri.parse("file://" + paramString), "application/vnd.android.package-archive");
    return localIntent;
  }

  private void a(c paramc)
  {
    this.w = paramc;
  }

  private void a(String paramString, long paramLong)
  {
    p.b(this, "begin download in " + paramString);
    this.Q = new f(this.G, paramString, paramLong, new e()
    {
      public void a(int paramInt)
      {
        b.n(b.this).sendEmptyMessage(paramInt);
      }

      public void a(String paramString)
      {
        b.e().c(b.this, b.b(b.this) + "下载线程出错，错误原因：" + paramString);
        b.this.b();
        if (b.c())
          b.b(b.this, paramString);
        if (b.k(b.this) != null)
          b.k(b.this).a(6, paramString);
      }
    }
    , q);
    this.Q.start();
    a.put(this.I, this);
  }

  public static void a(String paramString1, Context paramContext, c paramc, String paramString2)
  {
    a(paramString1, "", null, paramContext, paramc, null, false, paramString2, 1);
  }

  public static void a(String paramString1, String paramString2, Context paramContext, c paramc, String paramString3, int paramInt)
  {
    a(paramString1, paramString2, null, paramContext, paramc, null, true, paramString3, paramInt);
  }

  public static void a(String paramString1, String paramString2, String paramString3, Context paramContext, c paramc)
  {
    a(paramString1, paramString2, paramString3, paramContext, paramc, null, L, null, 2);
  }

  public static void a(String paramString1, String paramString2, String paramString3, Context paramContext, c paramc, String paramString4, boolean paramBoolean)
  {
    a(paramString1, paramString2, paramString3, paramContext, paramc, paramString4, paramBoolean, null, 2);
  }

  private static void a(String paramString1, String paramString2, String paramString3, Context paramContext, c paramc, String paramString4, boolean paramBoolean, String paramString5, int paramInt)
  {
    p.a(b.class, "Download url: " + paramString1);
    Uri localUri = Uri.parse(paramString1);
    String str1 = localUri.getHost() + localUri.getPath();
    p.a(b.class, "Download uri path: " + localUri.getPath());
    p.a(b.class, "Download uri host: " + localUri.getHost());
    Object localObject = "";
    if (paramInt != 2)
      if (paramString5 != null);
    while (true)
    {
      try
      {
        String str2 = localUri.getLastPathSegment();
        localObject = str2;
        p.a(b.class, "Download filename(md5) " + (String)localObject);
        M = paramBoolean;
        if (!a.containsKey(localObject))
          break;
        paramc.a(512, "当前应用已在下载");
        p.a(b.class, "App " + paramString2 + " is downloading");
        return;
      }
      catch (Error localError)
      {
        p.a(localError);
        paramc.a(516, "当前文件的下载地址有误");
        continue;
      }
      localObject = paramString5;
      continue;
      localObject = c(str1);
    }
    if (a.size() == 1000)
    {
      paramc.a(513, "最大下载数为1000个");
      p.a(b.class, "Maximum download number is 1000");
      return;
    }
    b localb = new b(paramString1, paramString2, (String)localObject, paramString3, paramString4, paramContext, paramInt);
    localb.a(paramc);
    if (M)
      localb.g();
    localb.j();
    paramc.a();
  }

  private static String c(String paramString)
  {
    return v.c(paramString);
  }

  private void d(String paramString)
  {
    PendingIntent localPendingIntent = PendingIntent.getActivity(q, this.u, new Intent(), 134217728);
    this.s.icon = 17301624;
    this.s.tickerText = (this.H + "下载失败");
    this.s.setLatestEventInfo(q, this.H + "下载失败", "", localPendingIntent);
    this.s.flags = 16;
    this.t.notify(this.u, this.s);
  }

  private static Intent e(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setDataAndType(Uri.parse(paramString), "image/*");
    r.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(paramString)));
    return localIntent;
  }

  private void g()
  {
    this.s = new Notification();
    this.s.icon = 17301633;
    this.s.tickerText = (this.H + "正在下载，请稍候...");
    this.O = PendingIntent.getActivity(q, this.u, i(), 134217728);
    this.s.setLatestEventInfo(q, this.H + "正在下载，请稍候...", "", this.O);
    this.t = ((NotificationManager)q.getSystemService("notification"));
    h();
  }

  private void h()
  {
    this.t.notify(this.u, this.s);
  }

  private Intent i()
  {
    Intent localIntent = new Intent();
    localIntent.setClass(q, DActivity.class);
    localIntent.putExtra("appName", this.H);
    localIntent.putExtra("appId", this.I);
    localIntent.putExtra("actType", "typeCancel");
    localIntent.putExtra("ActivityType", 2);
    return localIntent;
  }

  private void j()
  {
    new n(q, this.I, this.G, this.K, new o()
    {
      public void a()
      {
        Log.e("DrwSDK", b.b(b.this) + "rom can't chmod");
        if (b.k(b.this) != null)
          b.k(b.this).a(5, "sd卡不存在");
        b.b(b.this, "sd卡不存在");
      }

      public void a(long paramLong1, long paramLong2, long paramLong3)
      {
        Log.e("DrwSDK", b.b(b.this) + "not enough size sdsize=" + paramLong2 + " romsize=" + paramLong3);
        if (b.k(b.this) != null)
          b.k(b.this).a(1, "空间不足");
        b.b(b.this, "空间不足");
      }

      public void a(String paramString)
      {
        b.e().a(b.class, b.b(b.this) + " already exists in " + paramString);
        b.a(b.this, paramString);
        b.a(b.this, b.h(b.this), 0L);
      }

      public void b(String paramString)
      {
        b.e().a(b.class, b.b(b.this) + " is download but not finished in " + paramString);
        b.a(b.this, paramString);
        File localFile = new File(b.h(b.this));
        b.a(b.this, b.h(b.this), localFile.length());
      }

      public void c(String paramString)
      {
        b.e().a(b.class, b.b(b.this) + " is  not download,it will download in " + paramString);
        b.a(b.this, paramString);
        b.a(b.this, b.h(b.this), 0L);
      }

      public void d(String paramString)
      {
        Log.e("DrwSDK", b.b(b.this) + paramString);
        if (b.k(b.this) != null)
          b.k(b.this).a(5, b.b(b.this) + paramString);
        b.b(b.this, paramString);
      }
    }).start();
  }

  public c a()
  {
    return this.w;
  }

  public void b()
  {
    p.a(b.class.getSimpleName(), "Stop download  and cancel notify if is UI mode " + this.u);
    if (this.Q != null)
      this.Q.b();
    this.t.cancel(this.u);
    a.remove(this.I);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.b
 * JD-Core Version:    0.6.0
 */