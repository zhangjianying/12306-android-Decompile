package cn.domob.android.a;

import android.content.Context;
import cn.domob.android.offerwall.m;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

class f extends Thread
{
  private static m d = new m(f.class.getSimpleName());
  String a;
  String b;
  String c;
  private int e = 0;
  private int f;
  private int g;
  private int h = 1;
  private int i = 0;
  private d j = null;
  private boolean k;
  private long l = 0L;
  private a m = new a();
  private Context n = null;

  public f(String paramString1, String paramString2, long paramLong, d paramd, Context paramContext)
  {
    this.a = paramString1;
    this.c = paramString2;
    this.l = paramLong;
    this.j = paramd;
    this.n = paramContext;
    d.b(f.class.getSimpleName(), "build DownloadTask url=" + paramString1 + ",fileName=" + paramString2);
  }

  protected boolean a()
  {
    return this.k;
  }

  protected void b()
  {
    this.m.a = true;
  }

  public void run()
  {
    g[] arrayOfg = new g[this.h];
    URL localURL;
    try
    {
      localURL = new URL(this.a);
      Proxy localProxy = e.b(this.n);
      HttpURLConnection localHttpURLConnection;
      if (localProxy != null)
      {
        d.a(this, "Use Proxy");
        localHttpURLConnection = (HttpURLConnection)localURL.openConnection(localProxy);
      }
      while (true)
      {
        localHttpURLConnection.setRequestMethod("HEAD");
        int i1 = localHttpURLConnection.getResponseCode();
        if ((i1 >= 200) && (i1 < 300))
        {
          this.e = localHttpURLConnection.getContentLength();
          d.a(this, "Total file size: " + this.e);
          if (this.e > 0)
            break;
          this.j.a("下载连接过程中出现错误");
          return;
          d.a(this, "Proxy is null");
          localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
          continue;
        }
        else
        {
          this.j.a("下载连接过程中出现错误");
          return;
        }
      }
    }
    catch (Exception localException)
    {
      d.a(localException);
      this.j.a("下载过程中出现错误");
      return;
    }
    d.b(f.class.getSimpleName(), "fileSize:" + this.e + " downloadSizeMore:" + this.g);
    this.f = (this.e / this.h);
    this.g = (this.e % this.h);
    File localFile = new File(this.c);
    for (int i2 = 0; i2 < this.h; i2++)
    {
      if (i2 != -1 + this.h);
      for (g localg = new g(localURL, localFile, i2 * this.f + this.l, -1 + (i2 + 1) * this.f, this.m, this.j, this.n); localg.a(); localg = new g(localURL, localFile, i2 * this.f + this.l, -1 + (i2 + 1) * this.f + this.g, this.m, this.j, this.n))
      {
        this.j.a(100);
        return;
      }
      localg.setName("Thread" + i2);
      localg.start();
      arrayOfg[i2] = localg;
    }
    this.k = false;
    if ((!this.k) && (!this.m.a))
    {
      this.i = this.g;
      this.k = true;
    }
    for (int i3 = 0; ; i3++)
      if (i3 < arrayOfg.length)
      {
        this.i = (int)(this.i + arrayOfg[i3].b());
        if (arrayOfg[i3].a())
          continue;
        this.k = false;
      }
      else
      {
        if ((this.j != null) && (!this.m.a))
        {
          int i4 = Double.valueOf(100.0D * (1.0D * this.i / this.e)).intValue();
          this.j.a(i4);
        }
        sleep(1000L);
        break;
        return;
      }
  }

  protected class a
  {
    protected boolean a = false;

    protected a()
    {
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.a.f
 * JD-Core Version:    0.6.0
 */