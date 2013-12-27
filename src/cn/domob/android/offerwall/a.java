package cn.domob.android.offerwall;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

class a extends WebView
{
  protected static final int a = -1;
  private static m b = new m(a.class.getSimpleName());
  private static final int k = 10;
  private a c;
  private b d;
  private c e;
  private int f;
  private boolean g = false;
  private boolean h = false;
  private boolean i = false;
  private boolean j = false;

  public a(Context paramContext, int paramInt)
  {
    super(paramContext);
    b();
    getSettings().setJavaScriptEnabled(true);
    getSettings().setPluginsEnabled(true);
    getSettings().setCacheMode(-1);
    this.f = 10;
    if (paramInt != -1)
    {
      b.a("Init DomobWebView with custom background color.");
      setBackgroundColor(paramInt);
    }
    setWebViewClient(new WebViewClient()
    {
      public void onPageFinished(WebView paramWebView, String paramString)
      {
        super.onPageFinished(paramWebView, paramString);
        a.a().a("onPageFinished");
        if (a.a(a.this))
        {
          a.b(a.this);
          return;
        }
        a.a(a.this, true);
        a.c(a.this);
      }

      public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
      {
        super.onPageStarted(paramWebView, paramString, paramBitmap);
        a.a().a("onPageStarted");
      }

      public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
      {
        super.onReceivedError(paramWebView, paramInt, paramString1, paramString2);
        a.a().a("onReceivedError");
        a.b(a.this, true);
        a.b(a.this);
      }

      public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
      {
        if (a.d(a.this) != null)
          a.d(a.this).a((a)paramWebView, paramString);
        if (paramString.startsWith("http"))
          paramWebView.loadUrl(paramString);
        return true;
      }
    });
    setDownloadListener(new DownloadListener()
    {
      public void onDownloadStart(String paramString1, String paramString2, String paramString3, String paramString4, long paramLong)
      {
        if (a.e(a.this) != null)
          a.e(a.this).a(paramString1);
      }
    });
  }

  private void b()
  {
    setHorizontalScrollBarEnabled(false);
    setHorizontalScrollbarOverlay(false);
    setVerticalScrollbarOverlay(true);
    getSettings().setSupportZoom(false);
  }

  private void c()
  {
    if ((this.d != null) && (!this.j))
    {
      this.j = true;
      b.a("WebView finish callback.");
      this.d.a(this);
    }
  }

  private void d()
  {
    if ((this.d != null) && (!this.j))
    {
      this.j = true;
      b.a("WebView failed callback.");
      this.d.b(this);
    }
  }

  private void e()
  {
    if ((this.d != null) && (!this.j))
    {
      this.j = true;
      b.a("WebView timeout callback.");
      this.d.c(this);
    }
  }

  protected void a(int paramInt)
  {
    b.a("WebView's timeout is set as :" + paramInt);
    this.f = paramInt;
  }

  protected void a(a parama)
  {
    this.c = parama;
  }

  protected void a(b paramb)
  {
    this.d = paramb;
  }

  protected void a(c paramc)
  {
    this.e = paramc;
  }

  protected void a(String paramString)
  {
    monitorenter;
    if (paramString != null);
    try
    {
      String str = "javascript:" + paramString;
      b.b(String.format("BaseWebView instance executes js: %s", new Object[] { str }));
      ((Activity)getContext()).runOnUiThread(new Runnable(str)
      {
        public void run()
        {
          a.this.loadUrl(this.a);
        }
      });
      monitorexit;
      return;
    }
    finally
    {
      localObject = finally;
      monitorexit;
    }
    throw localObject;
  }

  protected void a(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }

  public void loadData(String paramString1, String paramString2, String paramString3)
  {
    b.a(String.format("loadData() is called with data = %s.", new Object[] { paramString1 }));
    loadDataWithBaseURL(null, paramString1, paramString2, paramString3, null);
  }

  static abstract interface a
  {
    public abstract void a(a parama, String paramString);
  }

  static abstract interface b
  {
    public abstract void a(a parama);

    public abstract void b(a parama);

    public abstract void c(a parama);
  }

  static abstract interface c
  {
    public abstract void a(String paramString);
  }

  class d extends Thread
  {
    private int b = 0;

    d()
    {
    }

    public void run()
    {
      while ((this.b < a.f(a.this)) && (!a.g(a.this)))
        try
        {
          Thread.sleep(1000L);
          this.b = (1 + this.b);
        }
        catch (InterruptedException localInterruptedException)
        {
          a.a().a(localInterruptedException);
        }
      if ((this.b == a.f(a.this)) && (!a.g(a.this)) && (!a.a(a.this)))
      {
        a.a().e("WebView 加载超时");
        a.b(a.this, true);
        a.h(a.this);
        if (a.i(a.this))
          ((Activity)a.this.getContext()).runOnUiThread(new Runnable()
          {
            public void run()
            {
              a.this.stopLoading();
            }
          });
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.a
 * JD-Core Version:    0.6.0
 */