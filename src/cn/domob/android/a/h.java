package cn.domob.android.a;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import cn.domob.android.offerwall.m;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

class h extends Thread
{
  static String a;
  static String b;
  protected static final String c = ".temp";
  static final int d = 2097152;
  static final int e = 5242880;
  private static m f = new m(h.class.getSimpleName());
  private static final String i = "/DomobOfferWallAppDownload/";
  private i g = null;
  private Context h = null;
  private long j = 0L;
  private String k = "";
  private String l = "";
  private final String m = ".apk";
  private boolean n = false;
  private String o = "";

  static
  {
    a = "sd卡";
    b = "内存卡";
  }

  protected h(Context paramContext, String paramString1, String paramString2, i parami)
  {
    this.g = parami;
    this.h = paramContext;
    this.o = paramString2;
    this.k = (paramString1 + ".apk");
  }

  private long a(String paramString)
    throws Exception
  {
    URL localURL = new URL(paramString);
    Proxy localProxy = e.b(this.h);
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
      if (this.g != null)
        this.g.a(paramString);
    while (true)
    {
      return true;
      if (!localFile2.exists())
        break;
      f.b(this, paramString + "　download size=" + localFile2.length() + " " + "softSize:" + this.j);
      this.n = true;
      if (this.g == null)
        continue;
      this.g.b(paramString + ".temp");
      return true;
    }
    return false;
  }

  private boolean c()
  {
    String str1 = "";
    long l1 = 0L;
    while (true)
    {
      try
      {
        this.j = a(this.o);
        if (!e())
          break label409;
        StatFs localStatFs1 = new StatFs(Environment.getExternalStorageDirectory().getPath());
        l1 = localStatFs1.getBlockSize() * localStatFs1.getAvailableBlocks();
        f.a(this, "sd availaSize=" + l1 + "softsize=" + this.j);
        if (l1 <= 2097152L + this.j)
          break label409;
        str1 = Environment.getExternalStorageDirectory() + "/DomobOfferWallAppDownload/";
        c(str1);
        i1 = 1;
        if (i1 != 0)
          break label335;
        StatFs localStatFs2 = new StatFs(Environment.getDataDirectory().getPath());
        long l2 = localStatFs2.getBlockSize() * localStatFs2.getAvailableBlocks();
        f.b(this, "rom" + l2);
        if (l2 < 5242880L + this.j)
        {
          this.g.a(this.j, l1, l2);
          return false;
        }
      }
      catch (Exception localException)
      {
        this.g.d("连接下载地址错误");
        return false;
      }
      str1 = this.h.getFilesDir().getAbsolutePath() + "/DomobOfferWallAppDownload/";
      c(str1);
      File localFile = new File(str1);
      String str2 = "chmod 777 " + localFile.getAbsolutePath();
      Runtime localRuntime = Runtime.getRuntime();
      try
      {
        int i2 = localRuntime.exec(str2).waitFor();
        if (i2 == 0)
        {
          label335: this.g.c(str1 + this.k + ".temp");
          return true;
        }
        this.g.a();
        return false;
      }
      catch (IOException localIOException)
      {
        while (true)
          f.a(localIOException);
      }
      catch (InterruptedException localInterruptedException)
      {
        while (true)
          f.a(localInterruptedException);
      }
      label409: int i1 = 0;
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
    this.l = (Environment.getExternalStorageDirectory() + "/DomobOfferWallAppDownload/" + this.k);
    f.a(this, "downloadPath: " + this.l);
    if (b(this.l));
    do
    {
      return true;
      this.l = (this.h.getFilesDir().getAbsolutePath() + "/DomobOfferWallAppDownload/" + this.k);
    }
    while (b(this.l));
    return false;
  }

  private static boolean e()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }

  protected String a()
  {
    f.b(this, "app is download:" + d() + " " + "is not complete:" + this.n);
    if ((!d()) || (this.n))
      return null;
    return this.l;
  }

  public final void run()
  {
    b();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.a.h
 * JD-Core Version:    0.6.0
 */