package cn.domob.wall.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import cn.domob.wall.core.a.b.a;
import cn.domob.wall.core.download.d;
import cn.domob.wall.core.download.d.a;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

public class b
  implements cn.domob.wall.core.a.a.a, b.a, cn.domob.wall.core.download.a.a, d.a
{
  private static p a = new p(b.class.getSimpleName());
  private static final String b = "inapp";
  private static final String c = "launch";
  private static final String d = "url";
  private static final String e = "download";
  private boolean f = false;
  private boolean g = false;
  private boolean h = false;
  private DService.ShowDetailsPageListener i;
  private k j;
  private Context k;
  private boolean l = false;

  b(Context paramContext, k paramk)
  {
    this.j = paramk;
    this.k = paramContext;
    cn.domob.wall.core.download.a.a(paramContext, this);
  }

  private Context a()
  {
    return this.k;
  }

  private boolean a(AdInfo paramAdInfo, String paramString)
    throws Exception
  {
    Uri localUri = Uri.parse(paramString);
    String str1 = localUri.getHost();
    if (str1.equals("inapp"))
    {
      a.b("overrideUri=" + localUri);
      String str3 = URLDecoder.decode(localUri.getQueryParameter("url"), "UTF-8");
      a.a("Open landing page with URL:" + str3);
      Context localContext = a();
      if (localContext != null)
      {
        b();
        new cn.domob.wall.core.a.b(localContext, str3, null, this, paramAdInfo).a().show();
      }
    }
    while (true)
    {
      return true;
      if (str1.equals("download"))
      {
        if (!this.l)
          this.l = true;
        String str2 = URLDecoder.decode(localUri.getQueryParameter("url"), "UTF-8");
        new d(a(), localUri, paramAdInfo, this).a(this.k);
        a.b("Download app with URL:" + str2);
        continue;
      }
      if (str1.equals("launch"))
      {
        new cn.domob.wall.core.a.a(this.k, localUri, this, paramAdInfo).a();
        continue;
      }
      if (!paramString.startsWith("http"))
        break;
      Intent localIntent = v.a(this.k, Uri.parse(paramString));
      if (localIntent == null)
        localIntent = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
      this.k.startActivity(localIntent);
    }
    a.e("Handle unknown action : " + str1);
    return false;
  }

  private void b()
  {
    this.f = false;
    this.g = false;
    this.h = false;
  }

  private boolean c()
  {
    return (!this.f) && (!this.h) && (!this.g);
  }

  private r.j r(AdInfo paramAdInfo)
  {
    AdInfo.AdStyle localAdStyle = paramAdInfo.i();
    if (localAdStyle == AdInfo.AdStyle.BANNER)
      return r.j.b;
    if ((localAdStyle == AdInfo.AdStyle.HOUSE_AD) || (localAdStyle == AdInfo.AdStyle.XX))
      return r.j.c;
    a.e("ad style error: " + localAdStyle);
    return r.j.a;
  }

  void a(AdInfo paramAdInfo)
  {
    c(paramAdInfo);
    m.a().a(this.j, paramAdInfo, r.j.d);
  }

  void a(DService.ShowDetailsPageListener paramShowDetailsPageListener)
  {
    this.i = paramShowDetailsPageListener;
  }

  void a(DetailsPageInfo paramDetailsPageInfo)
  {
    AdInfo localAdInfo = paramDetailsPageInfo.a();
    if ((localAdInfo.getAdActionType().equals(AdInfo.ClickActionType.DOWNLOAD)) && (localAdInfo.g()))
    {
      c(localAdInfo);
      m.a().a(this.j, localAdInfo, r.j.e);
    }
  }

  public void a(String paramString, WebView paramWebView, AdInfo paramAdInfo)
  {
  }

  public void a(String paramString, AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.m.c, paramString);
  }

  public void a(String paramString1, String paramString2, AdInfo paramAdInfo)
  {
    a.a(String.format("LandingPage 内下载，地址为%s，infoURL为%s", new Object[] { paramString1, paramString2 }));
    if (paramString2 != null);
    try
    {
      String str2;
      if (paramString2.length() != 0)
      {
        HashMap localHashMap = v.b(Uri.parse(paramString2).getQuery());
        if (localHashMap.containsKey("url"))
          localHashMap.put("url", paramString1);
        str2 = v.a(localHashMap);
      }
      String str1;
      for (Object localObject = v.a() + "://download?" + str2; ; localObject = str1)
      {
        a(paramAdInfo, (String)localObject);
        return;
        str1 = v.a() + "://download?url=" + URLEncoder.encode(paramString1, "UTF-8");
      }
    }
    catch (Exception localException)
    {
      a.e(String.format("解析LandingPage中下载出现错误，地址为%s，infoURL为%s", new Object[] { paramString1, paramString2 }));
    }
  }

  void b(AdInfo paramAdInfo)
  {
    if (paramAdInfo.b())
    {
      if ((paramAdInfo.getAdActionType().equals(AdInfo.ClickActionType.DOWNLOAD)) && (paramAdInfo.g()))
        if (this.i != null)
          this.i.onShowDetailsPage(new DetailsPageInfo(paramAdInfo));
      while (true)
      {
        m.a().a(this.j, paramAdInfo, r(paramAdInfo));
        return;
        c(paramAdInfo);
      }
    }
    Log.w("DrwSDK", "Please implement the method setAdActualPosition(int position) of AdInfo, otherwise it is impossible to achieve onClickWallItem(AdInfo itemInfo)");
  }

  public void b(String paramString, AdInfo paramAdInfo)
  {
    try
    {
      if (a(paramAdInfo, paramString))
      {
        m.a().a(this.j, paramAdInfo, r.m.d, paramString);
        return;
      }
      m.a().a(this.j, paramAdInfo, r.m.e, paramString);
      return;
    }
    catch (Exception localException)
    {
      a.a(localException);
    }
  }

  boolean c(AdInfo paramAdInfo)
  {
    String str1 = paramAdInfo.e();
    try
    {
      if (str1.startsWith(v.a()))
      {
        String str2 = Uri.parse(str1).getScheme();
        if (v.a().equals(str2))
        {
          a.a("Scheme xxx action scheme =" + str2);
          return a(paramAdInfo, str1);
        }
      }
      else if (str1.startsWith("http"))
      {
        Intent localIntent = v.a(this.k, Uri.parse(str1));
        if (localIntent == null)
          localIntent = new Intent("android.intent.action.VIEW", Uri.parse(str1));
        this.k.startActivity(localIntent);
      }
      return true;
    }
    catch (Exception localException)
    {
      a.e("Exception in click.");
      a.a(localException);
    }
    return false;
  }

  public void d(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.m.b, null);
  }

  public void e(AdInfo paramAdInfo)
  {
    if (c())
    {
      m.a().a(this.j, paramAdInfo, r.i.b);
      this.f = true;
    }
  }

  public void f(AdInfo paramAdInfo)
  {
    if (c())
    {
      m.a().a(this.j, paramAdInfo, r.i.c);
      this.h = true;
    }
  }

  public void g(AdInfo paramAdInfo)
  {
    if (c())
    {
      m.a().a(this.j, paramAdInfo, r.i.d);
      this.g = true;
    }
  }

  public void h(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.i.e);
  }

  public void i(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.l.d);
  }

  public void j(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.l.e);
  }

  public void k(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.l.b);
  }

  public void l(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.l.f);
  }

  public void m(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.l.c);
    if (this.k != null)
      ((Activity)this.k).runOnUiThread(new Runnable()
      {
        public void run()
        {
          Toast.makeText(b.a(b.this), "应用正在下载", 0).show();
        }
      });
  }

  public void n(AdInfo paramAdInfo)
  {
  }

  public void o(AdInfo paramAdInfo)
  {
    cn.domob.wall.core.download.a.a().a(paramAdInfo);
  }

  public void p(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.l.g);
  }

  public void q(AdInfo paramAdInfo)
  {
    m.a().a(this.j, paramAdInfo, r.l.h);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.b
 * JD-Core Version:    0.6.0
 */