package cn.domob.wall.core.download;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import cn.domob.wall.core.AdInfo;
import cn.domob.wall.core.p;
import cn.domob.wall.core.v;
import java.io.UnsupportedEncodingException;

public class d
{
  public static final int b = 1;
  private static final String n = "url";
  private static final String o = "auto_run";
  private static final String p = "name";
  private static final String q = "pkg";
  private static final String r = "vc";
  private static final String s = "vn";
  p a = new p(d.class.getSimpleName());
  private Context c;
  private Uri d;
  private String e;
  private boolean f = false;
  private String g;
  private String h;
  private int i;
  private String j;
  private String k;
  private a l;
  private AdInfo m;

  public d(Context paramContext, Uri paramUri, AdInfo paramAdInfo, a parama)
  {
    this.c = paramContext;
    this.m = paramAdInfo;
    this.l = parama;
    this.d = paramUri;
  }

  private String a(Uri paramUri, String paramString)
    throws UnsupportedEncodingException
  {
    String str = paramUri.getQueryParameter(paramString);
    if (str != null)
      return str;
    return null;
  }

  private void h()
  {
    try
    {
      this.e = a(this.d, "url");
      boolean bool;
      if (a(this.d, "auto_run") == null)
      {
        bool = false;
        this.f = bool;
        this.g = a(this.d, "name");
        this.h = a(this.d, "pkg");
        if (a(this.d, "vc") != null)
          break label117;
      }
      label117: int i2;
      for (int i1 = 1; ; i1 = i2)
      {
        this.i = i1;
        this.j = a(this.d, "vn");
        return;
        bool = Boolean.valueOf(a(this.d, "auto_run")).booleanValue();
        break;
        i2 = Integer.valueOf(a(this.d, "vc")).intValue();
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      this.a.e("Error happened in getting download info");
    }
  }

  private void i()
  {
    this.a.b("Start Download url:" + this.e);
    b.a(this.e, this.g, this.h, this.c, new c()
    {
      public void a()
      {
        Toast.makeText(d.c(d.this), "开始下载 。。。", 0).show();
        if (d.a(d.this) != null)
          d.a(d.this).k(d.b(d.this));
      }

      public void a(int paramInt, String paramString)
      {
        if (d.a(d.this) != null)
        {
          if (paramInt == 512)
            d.a(d.this).m(d.b(d.this));
        }
        else
          return;
        if (paramInt == 513)
        {
          d.a(d.this).n(d.b(d.this));
          return;
        }
        d.a(d.this).j(d.b(d.this));
      }

      public void a(String paramString)
      {
        if (d.a(d.this) != null)
          d.a(d.this).i(d.b(d.this));
        if (d.a(d.this) != null)
          d.a(d.this).o(d.b(d.this));
      }

      public void b()
      {
        if (d.a(d.this) != null)
          d.a(d.this).l(d.b(d.this));
      }
    }
    , this.k, true);
  }

  public String a()
  {
    return this.g;
  }

  public void a(Context paramContext)
  {
    if (this.d != null)
      h();
    if (v.g(this.g))
      this.g = "应用";
    Intent localIntent = b.a(paramContext, this.e, this.g);
    if (localIntent != null)
    {
      if (paramContext != null)
        new AlertDialog.Builder(paramContext).setTitle("安装").setMessage(this.g + "已经下载是否现在安装?").setNegativeButton("否", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
          }
        }).setPositiveButton("是", new DialogInterface.OnClickListener(paramContext, localIntent)
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            if (d.a(d.this) != null)
              d.a(d.this).o(d.b(d.this));
            this.a.startActivity(this.b);
          }
        }).show();
      return;
    }
    i();
  }

  public String b()
  {
    return this.h;
  }

  public int c()
  {
    return this.i;
  }

  public String d()
  {
    return this.j;
  }

  public boolean e()
  {
    return this.f;
  }

  public String f()
  {
    return this.e;
  }

  public Uri g()
  {
    return this.d;
  }

  public static abstract interface a
  {
    public abstract void i(AdInfo paramAdInfo);

    public abstract void j(AdInfo paramAdInfo);

    public abstract void k(AdInfo paramAdInfo);

    public abstract void l(AdInfo paramAdInfo);

    public abstract void m(AdInfo paramAdInfo);

    public abstract void n(AdInfo paramAdInfo);

    public abstract void o(AdInfo paramAdInfo);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.download.d
 * JD-Core Version:    0.6.0
 */