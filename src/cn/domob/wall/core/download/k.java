package cn.domob.wall.core.download;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import cn.domob.wall.core.DService.OnImageDownload;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class k
{
  private static k b;
  DService.OnImageDownload a;
  private ExecutorService c = Executors.newFixedThreadPool(1 + Runtime.getRuntime().availableProcessors());
  private l d;
  private i e;
  private Map<String, ImageView> f;
  private boolean g = true;

  private k(Context paramContext)
  {
    this.d = new l(paramContext);
    this.e = new i();
    this.f = new HashMap();
  }

  private Bitmap a(String paramString)
  {
    Bitmap localBitmap1 = this.d.a(paramString);
    Bitmap localBitmap2;
    if (localBitmap1 == null)
    {
      localBitmap2 = this.e.a(paramString);
      if (localBitmap2 != null)
        break label55;
      localBitmap1 = j.a(paramString);
      if (localBitmap1 != null)
      {
        this.e.a(localBitmap1, paramString);
        this.d.a(paramString, localBitmap1);
      }
    }
    return localBitmap1;
    label55: this.d.a(paramString, localBitmap2);
    return localBitmap2;
  }

  public static k a(Context paramContext)
  {
    if (b == null)
      b = new k(paramContext);
    return b;
  }

  private void a(String paramString, ImageView paramImageView)
  {
    this.c.submit(new b(new a(paramString, paramImageView), paramString));
  }

  private void d()
  {
    synchronized (this.f)
    {
      Iterator localIterator = this.f.values().iterator();
      while (localIterator.hasNext())
      {
        ImageView localImageView = (ImageView)localIterator.next();
        if ((localImageView == null) || (localImageView.getTag() == null))
          continue;
        a((String)localImageView.getTag(), localImageView);
      }
    }
    this.f.clear();
    monitorexit;
  }

  public void a()
  {
    this.g = true;
  }

  public void a(String paramString, ImageView paramImageView, DService.OnImageDownload paramOnImageDownload)
  {
    this.a = paramOnImageDownload;
    Bitmap localBitmap = this.d.a(paramString);
    if (localBitmap != null)
      if (this.a != null)
        this.a.onDownloadSuc(localBitmap, paramString, paramImageView);
    while (true)
    {
      return;
      synchronized (this.f)
      {
        this.f.put(Integer.toString(paramImageView.hashCode()), paramImageView);
        if (!this.g)
          continue;
        d();
        return;
      }
    }
  }

  public void b()
  {
    this.g = false;
  }

  public void c()
  {
    this.g = true;
    d();
  }

  private class a extends Handler
  {
    String a;
    ImageView b;

    public a(String paramImageView, ImageView arg3)
    {
      this.a = paramImageView;
      Object localObject;
      this.b = localObject;
    }

    public void handleMessage(Message paramMessage)
    {
      if ((this.b.getTag().equals(this.a)) && (paramMessage.obj != null))
      {
        Bitmap localBitmap = (Bitmap)paramMessage.obj;
        if ((localBitmap != null) && (k.this.a != null))
          k.this.a.onDownloadSuc(localBitmap, this.a, this.b);
      }
    }
  }

  private class b
    implements Callable<String>
  {
    private String b;
    private Handler c;

    public b(Handler paramString, String arg3)
    {
      Object localObject;
      this.b = localObject;
      this.c = paramString;
    }

    public String a()
      throws Exception
    {
      Message localMessage = new Message();
      localMessage.obj = k.a(k.this, this.b);
      if (localMessage.obj != null)
        this.c.sendMessage(localMessage);
      return this.b;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.k
 * JD-Core Version:    0.6.0
 */