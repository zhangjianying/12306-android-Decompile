package cn.domob.android.a;

import android.content.Context;
import cn.domob.android.offerwall.m;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;

public class g extends Thread
{
  private static m a = new m(g.class.getSimpleName());
  private static final int b = 10240;
  private static final int l = 40000;
  private static final int m = 60000;
  private static final int n = 30;
  private URL c;
  private File d;
  private long e;
  private long f;
  private long g;
  private boolean h = false;
  private long i = 0L;
  private f.a j;
  private d k;
  private Context o = null;

  g(URL paramURL, File paramFile, long paramLong1, long paramLong2, f.a parama, d paramd, Context paramContext)
  {
    this.c = paramURL;
    this.d = paramFile;
    this.e = paramLong1;
    this.g = paramLong1;
    this.f = paramLong2;
    this.j = parama;
    this.k = paramd;
    this.i = paramLong1;
    this.o = paramContext;
    if ((paramLong1 > paramLong2) && (paramLong2 > 0L))
    {
      a.a(this, "Start postition is larger than end position, set finished to true");
      this.h = true;
    }
    a.a(this, "download st:" + paramLong1 + " ed:" + paramLong2);
  }

  public boolean a()
  {
    return this.h;
  }

  public long b()
  {
    return this.i;
  }

  public void run()
  {
    byte[] arrayOfByte = new byte[10240];
    while (true)
    {
      int i1;
      try
      {
        Proxy localProxy = e.b(this.o);
        if (localProxy == null)
          continue;
        a.a(this, "Proxy exists");
        HttpURLConnection localHttpURLConnection = (HttpURLConnection)this.c.openConnection(localProxy);
        localHttpURLConnection.setConnectTimeout(40000);
        localHttpURLConnection.setReadTimeout(60000);
        localHttpURLConnection.setAllowUserInteraction(true);
        localHttpURLConnection.setRequestProperty("Range", "bytes=" + this.e + "-" + this.f);
        localRandomAccessFile = new RandomAccessFile(this.d, "rw");
        if (!this.d.getAbsoluteFile().toString().startsWith("/data/data/"))
          continue;
        a.a(this, "download in rom change chmod " + this.d.getAbsolutePath());
        String str = "chmod 777 " + this.d.getAbsolutePath();
        Runtime.getRuntime().exec(str);
        if (localHttpURLConnection.getResponseCode() != 206)
          continue;
        a.b(this, "support range parameter,continue to download");
        localRandomAccessFile.seek(this.e);
        localBufferedInputStream = new BufferedInputStream(localHttpURLConnection.getInputStream());
        if (this.g >= this.f)
          continue;
        if (this.j.a)
        {
          return;
          a.a(this, "Proxy is null");
          localHttpURLConnection = (HttpURLConnection)this.c.openConnection();
          continue;
          if (this.e <= 0L)
            continue;
          a.b(this, "don't support range parameter,download from beginning");
          localRandomAccessFile.seek(0L);
          this.i = 0L;
          this.g = 0L;
          continue;
        }
      }
      catch (SocketTimeoutException localSocketTimeoutException)
      {
        BufferedInputStream localBufferedInputStream;
        a.e(this, "download SocketTimeoutException ");
        this.k.a("下载过程中网络出现异常");
        return;
        i1 = localBufferedInputStream.read(arrayOfByte, 0, 10240);
        if (i1 != -1)
          continue;
        a.a(this, "Total downloadsize: " + this.i);
        this.h = true;
        localBufferedInputStream.close();
        localRandomAccessFile.close();
        return;
      }
      catch (IOException localIOException)
      {
        RandomAccessFile localRandomAccessFile;
        a.e(this, "download IOException " + localIOException.getMessage());
        this.k.a("下载过程中出现异常");
        return;
        localRandomAccessFile.write(arrayOfByte, 0, i1);
        this.g += i1;
        if (this.g > this.f)
        {
          this.i += 1L + (i1 - (this.g - this.f));
          try
          {
            Thread.sleep(30L);
          }
          catch (InterruptedException localInterruptedException)
          {
            a.a(localInterruptedException);
            this.k.a("下载过程中出现异常");
          }
          continue;
        }
      }
      catch (Exception localException)
      {
        a.e(this, "download error " + localException.getMessage());
        this.k.a("下载过程中出现异常");
        a.a(localException);
        return;
      }
      this.i += i1;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.a.g
 * JD-Core Version:    0.6.0
 */