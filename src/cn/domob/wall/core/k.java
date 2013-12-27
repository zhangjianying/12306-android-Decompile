package cn.domob.wall.core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class k
  implements i.a
{
  private static p a = new p(k.class.getSimpleName());
  private static Context b = null;
  private static Boolean m = Boolean.valueOf(false);
  private static final int n = 1;
  private static final int o = 2;
  private static final int p = 3;
  private static final int q = 4;
  private static final int r = 5;
  private Context c;
  private DService.ReceiveDataListener d;
  private String e = null;
  private String f = null;
  private String g = null;
  private boolean h = false;
  private Thread i = null;
  private ExecutorService j;
  private int k = 300000;
  private int l = 10;

  k(Context paramContext, String paramString1, String paramString2)
  {
    this.c = paramContext;
    this.f = paramString2;
    this.e = paramString1;
    if (b == null)
      b = paramContext.getApplicationContext();
    h.a(paramContext);
    this.j = Executors.newSingleThreadExecutor();
    if (!m.booleanValue())
    {
      t.a().b();
      m = Boolean.valueOf(true);
    }
  }

  public static Context b()
  {
    return b;
  }

  private void c(k paramk)
  {
    if ((this.i == null) || (!this.i.isAlive()))
    {
      this.i = new Thread(new Runnable()
      {
        public void run()
        {
          while (true)
            try
            {
              p localp = k.i();
              Object[] arrayOfObject = new Object[1];
              arrayOfObject[0] = Integer.valueOf(300);
              localp.b(String.format("imp report thread start to sleep %s seconds", arrayOfObject));
              Thread.sleep(k.a(k.this));
              continue;
            }
            catch (InterruptedException localInterruptedException)
            {
              k.i().a(localInterruptedException);
            }
        }
      });
      this.i.start();
    }
  }

  private boolean j()
  {
    if (!this.h)
    {
      if ((v.g(this.e)) || (v.g(this.f)))
        break label54;
      if (v.a(this.c))
        this.h = true;
    }
    else
    {
      return true;
    }
    Log.w("DrwSDK", "Permission denied.");
    while (true)
    {
      return false;
      label54: Log.w("DrwSDK", "Please set your publisherID and placementID first.");
    }
  }

  public void a()
  {
    if (!j())
      return;
    new Thread()
    {
      public void run()
      {
        try
        {
          if (!d.a)
            new d(k.this).a();
          new i(k.this).a();
          return;
        }
        catch (c localc)
        {
          k.i().d("Config request is ongoing, Thread will sleep 1 seconds, and then restart to request");
          return;
        }
        catch (Exception localException)
        {
          k.i().a(localException);
        }
      }
    }
    .start();
  }

  protected void a(DService.ErrorCode paramErrorCode, String paramString)
  {
    a.d("failed to load data.");
    ((Activity)this.c).runOnUiThread(new Runnable(paramErrorCode, paramString)
    {
      public void run()
      {
        if (k.b(k.this) != null)
          k.b(k.this).onFailReceiveData(this.a, this.b);
      }
    });
  }

  public void a(DService.ReceiveDataListener paramReceiveDataListener)
  {
    this.d = paramReceiveDataListener;
  }

  protected void a(j paramj)
  {
    if (this.d != null)
    {
      this.d.onSuccessReceiveData(paramj.e(), paramj.d(), paramj.c());
      this.g = paramj.a();
      this.k = paramj.c().c();
      this.l = paramj.c().b();
    }
  }

  public void a(j paramj, int paramInt)
  {
    if (paramj != null)
    {
      if (paramj.b() != null)
      {
        String str1 = paramj.b().b();
        int i1 = paramj.b().a();
        String str2 = paramj.a();
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = Integer.valueOf(i1);
        arrayOfObject[1] = str2;
        arrayOfObject[2] = str1;
        Log.e("error", String.format("Response contains error info. Error code is [%d-%s] and error content is [%s]", arrayOfObject));
        switch (i1 / 1000)
        {
        default:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        }
        while (true)
        {
          a(DService.ErrorCode.INTERNAL_ERROR, str1);
          return;
          a(DService.ErrorCode.INTERNAL_ERROR, str1);
          return;
          a(DService.ErrorCode.INTERNAL_ERROR, str1);
          return;
          a(DService.ErrorCode.INVALID_REQUEST, str1);
          return;
          a(DService.ErrorCode.NO_FILL, str1);
          return;
          a(DService.ErrorCode.INTERNAL_ERROR, str1);
        }
      }
      if ((paramj.e() != null) && (paramj.d() != null))
      {
        a(paramj);
        a.b("Get data response successfully.");
        return;
      }
      a(DService.ErrorCode.INTERNAL_ERROR, "");
      return;
    }
    a.e("AdResponse instance is null. Try to request again after refresh interval.");
    a(DService.ErrorCode.INTERNAL_ERROR, "");
  }

  protected String c()
  {
    return this.e;
  }

  protected String d()
  {
    return this.f;
  }

  protected int e()
  {
    return this.l;
  }

  protected String f()
  {
    return this.g;
  }

  protected Context g()
  {
    return this.c;
  }

  protected ExecutorService h()
  {
    return this.j;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.k
 * JD-Core Version:    0.6.0
 */