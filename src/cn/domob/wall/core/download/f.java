package cn.domob.wall.core.download;

import android.content.Context;
import cn.domob.wall.core.p;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

class f extends Thread
{
  private static p d = new p(f.class.getSimpleName());
  String a;
  String b;
  String c;
  private int e = 0;
  private int f;
  private int g;
  private int h = 1;
  private int i = 0;
  private e j = null;
  private boolean k;
  private long l = 0L;
  private a m = new a();
  private Context n = null;

  public f(String paramString1, String paramString2, long paramLong, e parame, Context paramContext)
  {
    this.a = paramString1;
    this.c = paramString2;
    this.l = paramLong;
    this.j = parame;
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
    h[] arrayOfh = new h[this.h];
    URL localURL;
    try
    {
      localURL = new URL(this.a);
      Proxy localProxy = g.b(this.n);
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
      for (h localh = new h(localURL, localFile, i2 * this.f + this.l, -1 + (i2 + 1) * this.f, this.m, this.j, this.n); localh.a(); localh = new h(localURL, localFile, i2 * this.f + this.l, -1 + (i2 + 1) * this.f + this.g, this.m, this.j, this.n))
      {
        this.j.a(100);
        return;
      }
      localh.setName("Thread" + i2);
      localh.start();
      arrayOfh[i2] = localh;
    }
    this.k = false;
    if ((!this.k) && (!this.m.a))
    {
      this.i = this.g;
      this.k = true;
    }
    for (int i3 = 0; ; i3++)
      if (i3 < arrayOfh.length)
      {
        this.i = (int)(this.i + arrayOfh[i3].b());
        if (arrayOfh[i3].a())
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
 * Qualified Name:     cn.domob.wall.core.download.f
 * JD-Core Version:    0.6.0
 */