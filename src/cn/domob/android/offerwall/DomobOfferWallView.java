package cn.domob.android.offerwall;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.net.URI;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class DomobOfferWallView extends RelativeLayout
{
  private static m a = new m(DomobOfferWallView.class.getSimpleName());
  private String b;
  private a c;
  private h d;
  private DomobOfferWallListener e;
  private boolean f = false;
  private boolean g = false;

  public DomobOfferWallView(Context paramContext)
  {
    this(paramContext, null);
  }

  public DomobOfferWallView(Context paramContext, String paramString)
  {
    super(paramContext);
    a.b("New instance of DomobOfferWallView.");
    setBackgroundColor(0);
    this.b = paramString;
    this.c = new a(paramContext, 0);
    c();
    this.c.loadUrl("file:///android_asset/index.html");
    addView(this.c, new RelativeLayout.LayoutParams(-1, -1));
    this.d = new h(this);
    Log.i("DomobSDK", "Current SDK version is " + l.a());
    Log.i("DomobSDK", "Publisher ID is set as " + this.b);
    Log.i("DomobSDK", "Application Package Name is " + f.a(paramContext));
    DomobOfferWallSetting.a(this.b);
  }

  private void b(String paramString)
  {
    ((Activity)getContext()).runOnUiThread(new Runnable(paramString)
    {
      public void run()
      {
        a locala = DomobOfferWallView.b(DomobOfferWallView.this);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.a;
        locala.a(String.format("domob.failed(%s)", arrayOfObject));
        DomobOfferWallView.b(DomobOfferWallView.this).invalidate();
        if ("404".equals(this.a))
          if (DomobOfferWallView.c(DomobOfferWallView.this) != null)
            DomobOfferWallView.c(DomobOfferWallView.this).onOfferWallLoadFinished(DomobOfferWallView.this);
        do
          return;
        while (DomobOfferWallView.c(DomobOfferWallView.this) == null);
        DomobOfferWallView.c(DomobOfferWallView.this).onOfferWallLoadFailed(DomobOfferWallView.this);
      }
    });
  }

  private void c()
  {
    this.c.a(new a.a()
    {
      public void a(a parama, String paramString)
      {
        DomobOfferWallView.b().b("Intercepted URL: " + paramString);
        URI localURI;
        List localList2;
        if (!l.e(paramString))
        {
          localURI = URI.create(paramString);
          if ("domob".equals(localURI.getScheme()))
          {
            if (!"click".equals(localURI.getHost()))
              break label183;
            localList2 = URLEncodedUtils.parse(localURI, "UTF-8");
            if ((localList2 == null) || (localList2.get(0) == null) || (!"id".equals(((NameValuePair)localList2.get(0)).getName())));
          }
        }
        label183: 
        do
        {
          try
          {
            int j = Integer.valueOf(((NameValuePair)localList2.get(0)).getValue()).intValue();
            DomobOfferWallView.b().b("Item was clicked with app id: " + j);
            DomobOfferWallView.a(DomobOfferWallView.this).b(j);
            return;
          }
          catch (Exception localException2)
          {
            DomobOfferWallView.b().a(localException2);
            return;
          }
          if (!"download".equals(localURI.getHost()))
            continue;
          List localList1 = URLEncodedUtils.parse(localURI, "UTF-8");
          if ((localList1 != null) && (localList1.get(0) != null) && ("id".equals(((NameValuePair)localList1.get(0)).getName())))
            try
            {
              int i = Integer.valueOf(((NameValuePair)localList1.get(0)).getValue()).intValue();
              m localm = DomobOfferWallView.b();
              Object[] arrayOfObject = new Object[1];
              arrayOfObject[0] = Integer.valueOf(i);
              localm.b(String.format("Item with ID = %d is about to be downloaded.", arrayOfObject));
              DomobOfferWallView.a(DomobOfferWallView.this).a(i);
              return;
            }
            catch (Exception localException1)
            {
              DomobOfferWallView.b().a(localException1);
              return;
            }
          DomobOfferWallView.b().e("Cannot search download info without id.");
          return;
        }
        while (!"refresh".equals(localURI.getHost()));
        DomobOfferWallView.b().b("Called refresh from web page. Load offer wall again.");
        DomobOfferWallView.this.loadOfferWall();
      }
    });
  }

  private void d()
  {
    boolean bool = true;
    m localm = a;
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = String.valueOf(this.g);
    arrayOfObject[bool] = String.valueOf(this.f);
    if (getVisibility() == 0);
    while (true)
    {
      arrayOfObject[2] = String.valueOf(bool);
      localm.b(String.format("In tryReportShowList: mIsViewOnWindow=%s, mViewHasData=%s, is visible=%s", arrayOfObject));
      if ((this.g) && (this.f) && (getVisibility() == 0))
        this.d.b();
      return;
      bool = false;
    }
  }

  private void e()
  {
    if ((!this.g) && (this.f))
      this.d.c();
  }

  protected void a()
  {
    b("");
  }

  protected void a(String paramString)
  {
    ((Activity)getContext()).runOnUiThread(new Runnable(paramString)
    {
      public void run()
      {
        a locala = DomobOfferWallView.b(DomobOfferWallView.this);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.a;
        locala.a(String.format("domob.setData(%s)", arrayOfObject));
        DomobOfferWallView.b(DomobOfferWallView.this).invalidate();
        if (DomobOfferWallView.c(DomobOfferWallView.this) != null)
          DomobOfferWallView.c(DomobOfferWallView.this).onOfferWallLoadFinished(DomobOfferWallView.this);
      }
    });
    this.f = true;
    d();
  }

  public void loadOfferWall()
  {
    a.b("Start to load Domob offer wall.");
    this.f = false;
    if (f.a(4, false))
      this.d.a();
    while (true)
    {
      if (this.e != null)
        this.e.onOfferWallLoadStart(this);
      return;
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          DomobOfferWallView.a(DomobOfferWallView.this, "404");
        }
      }
      , 1000L);
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    a.b("Offer Wall View is attached to window.");
    invalidate();
    this.g = true;
    d();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    a.b("Offer Wall View is detached from window.");
    this.g = false;
    e();
  }

  public void setOfferWallListener(DomobOfferWallListener paramDomobOfferWallListener)
  {
    this.e = paramDomobOfferWallListener;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.DomobOfferWallView
 * JD-Core Version:    0.6.0
 */