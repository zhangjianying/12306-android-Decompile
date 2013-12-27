package cn.domob.wall.core.download;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import cn.domob.wall.core.p;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

class n extends Thread
{
  protected static final String a = "/DrwResDownload/";
  static String b;
  static String c;
  protected static final String d = ".temp";
  static final int e = 2097152;
  static final int f = 5242880;
  private static p g = new p(n.class.getSimpleName());
  private static final String k = "/DrwAppDownload/";
  private o h = null;
  private Context i = null;
  private String j = "/DrwAppDownload/";
  private long l = 0L;
  private String m = "";
  private String n = "";
  private final String o = ".apk";
  private boolean p = false;
  private String q = "";
  private boolean r = true;

  static
  {
    b = "sd卡";
    c = "内存卡";
  }

  protected n(Context paramContext, String paramString1, String paramString2, boolean paramBoolean, o paramo)
  {
    this.h = paramo;
    this.i = paramContext;
    this.q = paramString2;
    if (paramBoolean)
    {
      this.m = paramString1;
      this.j = "/DrwResDownload/";
      this.r = false;
      return;
    }
    this.m = (paramString1 + ".apk");
    this.j = "/DrwAppDownload/";
    this.r = true;
  }

  private long a(String paramString)
    throws Exception
  {
    URL localURL = new URL(paramString);
    Proxy localProxy = g.b(this.i);
    if (localProxy != null);
    for (HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURL.openConnection(localProxy); ; localHttpURLConnection = (HttpURLConnection)localURL.openConnection())
    {
      localHttpURLConnection.setRequestMethod("HEAD");
      int i1 = localHttpURLConnection.getResponseCode();
      if ((i1 < 200) || (i1 >= 300))
        break;
      return localHttpURLConnection.getContentLength();
    }
    return -1L;
  }

  private void b()
  {
    if (d());
    do
      return;
    while (c());
  }

  private boolean b(String paramString)
  {
    File localFile1 = new File(paramString);
    File localFile2 = new File(paramString + ".temp");
    if (localFile1.exists())
      if (this.h != null)
        this.h.a(paramString);
    while (true)
    {
      return true;
      if (!localFile2.exists())
        break;
      g.b(this, paramString + "　download size=" + localFile2.length() + " " + "softSize:" + this.l);
      this.p = true;
      if (this.h == null)
        continue;
      this.h.b(paramString + ".temp");
      return true;
    }
    return false;
  }

  private boolean c()
  {
    String str1 = "";
    long l1 = 0L;
    try
    {
      this.l = a(this.q);
      if (this.l == -1L)
      {
        this.h.d("连接下载地址信息错误");
        return false;
      }
    }
    catch (Exception localException)
    {
      this.h.d("连接下载地址错误");
      return false;
    }
    if (e())
    {
      StatFs localStatFs1 = new StatFs(Environment.getExternalStorageDirectory().getPath());
      l1 = localStatFs1.getBlockSize() * localStatFs1.getAvailableBlocks();
      g.a(this, "sd availaSize=" + l1 + "softsize=" + this.l);
      if (l1 > 2097152L + this.l)
      {
        str1 = Environment.getExternalStorageDirectory() + this.j;
        c(str1);
      }
    }
    for (int i1 = 1; ; i1 = 0)
    {
      String str2;
      Runtime localRuntime;
      if ((i1 == 0) && (this.r))
      {
        StatFs localStatFs2 = new StatFs(Environment.getDataDirectory().getPath());
        long l2 = localStatFs2.getBlockSize() * localStatFs2.getAvailableBlocks();
        g.b(this, "rom" + l2);
        if (l2 < 5242880L + this.l)
        {
          this.h.a(this.l, l1, l2);
          return false;
        }
        str1 = this.i.getFilesDir().getAbsolutePath() + this.j;
        c(str1);
        File localFile = new File(str1);
        str2 = "chmod 777 " + localFile.getAbsolutePath();
        localRuntime = Runtime.getRuntime();
      }
      try
      {
        int i2 = localRuntime.exec(str2).waitFor();
        if (i2 == 0)
        {
          this.h.c(str1 + this.m + ".temp");
          return true;
        }
        this.h.a();
        return false;
      }
      catch (IOException localIOException)
      {
        while (true)
          g.a(localIOException);
      }
      catch (InterruptedException localInterruptedException)
      {
        while (true)
          g.a(localInterruptedException);
      }
    }
  }

  private boolean c(String paramString)
  {
    File localFile = new File(paramString);
    if (!localFile.exists())
      return localFile.mkdirs();
    return true;
  }

  private boolean d()
  {
    this.n = (Environment.getExternalStorageDirectory() + this.j + this.m);
    g.a(this, "downloadPath: " + this.n);
    if (b(this.n));
    do
    {
      return true;
      this.n = (this.i.getFilesDir().getAbsolutePath() + this.j + this.m);
    }
    while (b(this.n));
    return false;
  }

  private static boolean e()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }

  protected String a()
  {
    g.b(this, "app is download:" + d() + " " + "is not complete:" + this.p);
    if ((!d()) || (this.p))
      return null;
    return this.n;
  }

  public final void run()
  {
    b();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.n
 * JD-Core Version:    0.6.0
 */