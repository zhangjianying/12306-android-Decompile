package cn.domob.android.offerwall;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.domob.android.a.a;
import cn.domob.android.a.c;

class g
{
  m a = new m(g.class.getSimpleName());
  private Context b;
  private String c;
  private int d;
  private String e;
  private String f;
  private a g;

  g(Context paramContext, String paramString, a parama)
  {
    this.b = paramContext;
    this.c = paramString;
    this.g = parama;
  }

  private void f()
  {
    this.a.b("Start Download url:" + this.c);
    a.a(this.c, this.e, this.f, this.b, new c()
    {
      public void a()
      {
        Toast.makeText(g.a(g.this), "开始下载 。。。", 0).show();
        if (g.b(g.this) != null)
          g.b(g.this).e(g.this);
      }

      public void a(int paramInt, String paramString)
      {
        if (g.b(g.this) != null)
        {
          if (paramInt == 512)
            g.b(g.this).g(g.this);
        }
        else
          return;
        if (paramInt == 513)
        {
          g.b(g.this).h(g.this);
          return;
        }
        g.b(g.this).d(g.this);
      }

      public void a(String paramString)
      {
        if (g.b(g.this) != null)
          g.b(g.this).c(g.this);
        if (g.b(g.this) != null)
          g.b(g.this).i(g.this);
      }

      public void b()
      {
        if (g.b(g.this) != null)
          g.b(g.this).f(g.this);
      }

      public void b(String paramString)
      {
      }
    }
    , null, true);
  }

  protected int a()
  {
    return this.d;
  }

  protected void a(int paramInt)
  {
    this.d = paramInt;
  }

  protected void a(String paramString)
  {
    this.e = paramString;
  }

  protected String b()
  {
    return this.e;
  }

  protected void b(String paramString)
  {
    this.f = paramString;
  }

  protected String c()
  {
    return this.f;
  }

  protected String d()
  {
    return this.c;
  }

  protected void e()
  {
    if (l.e(this.e))
      this.e = "应用";
    Intent localIntent = a.a(this.b, this.c, this.e);
    if (localIntent != null)
    {
      if (this.b != null)
      {
        if (this.g != null)
          this.g.i(this);
        this.b.startActivity(localIntent);
      }
      return;
    }
    f();
  }

  static abstract interface a
  {
    public abstract void c(g paramg);

    public abstract void d(g paramg);

    public abstract void e(g paramg);

    public abstract void f(g paramg);

    public abstract void g(g paramg);

    public abstract void h(g paramg);

    public abstract void i(g paramg);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.android.offerwall.g
 * JD-Core Version:    0.6.0
 */