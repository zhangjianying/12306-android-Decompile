package cn.domob.wall.core.a;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.VideoView;
import cn.domob.wall.core.AdInfo;
import cn.domob.wall.core.p;
import cn.domob.wall.core.s;
import cn.domob.wall.core.v;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class b
{
  private static final String A = "a_banner.png";
  private static final String B = "a_exit.png";
  private static final String C = "a_exit_on.png";
  private static final String D = "a_loading.png";
  private static final String E = "a_next_off.png";
  private static final String F = "a_next.png";
  private static final String G = "a_next_on.png";
  private static final String H = "a_out.png";
  private static final String I = "a_out_on.png";
  private static final String J = "a_preview_off.png";
  private static final String K = "a_preview.png";
  private static final String L = "a_preview_on.png";
  private static final String M = "a_refresh.png";
  private static final String N = "a_refresh_on.png";
  private static final int O = 35;
  private static final int P = 35;
  private static final int Q = 45;
  private static final int R = 500;
  private static final String[] S;
  private static p b = new p(b.class.getSimpleName());
  private static final String x = "close";
  private static final String y = "inapp";
  private static final String z = "url";
  AdInfo a;
  private WebView c;
  private String d = null;
  private Context e = null;
  private String f;
  private Dialog g = null;
  private a h;
  private Handler i = new Handler();
  private RelativeLayout j;
  private float k = 1.0F;
  private View l;
  private ImageButton m;
  private ImageButton n;
  private ImageButton o;
  private ImageButton p;
  private ImageButton q;
  private ImageView r;
  private RotateAnimation s;
  private Animation t;
  private Animation u;
  private boolean v = false;
  private boolean w = false;

  static
  {
    S = new String[] { ".mp4", ".3gp", ".asf", ".avi", ".m4u", ".m4v", ".mov", ".mp4", ".mpe", ".mpeg", ".mpg", ".mpg4" };
  }

  public b(Context paramContext, String paramString1, String paramString2, a parama, AdInfo paramAdInfo)
  {
    this.a = paramAdInfo;
    b.a(this, "Initialize LandingPageBuilder");
    this.e = paramContext;
    this.k = s.s(this.e);
    this.c = new WebView(this.e);
    this.d = paramString1;
    this.f = paramString2;
    this.h = parama;
    try
    {
      this.l = d();
      e();
      return;
    }
    catch (Exception localException)
    {
      b.a(localException);
    }
  }

  private Drawable a(Context paramContext, String paramString)
  {
    try
    {
      BitmapDrawable localBitmapDrawable = new BitmapDrawable(BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("assets/" + paramString)));
      return localBitmapDrawable;
    }
    catch (Exception localException)
    {
      b.a(localException);
      b.e("Failed to load source file:" + paramString);
    }
    return null;
  }

  private LinearLayout a(String paramString, ImageButton paramImageButton)
  {
    LinearLayout localLinearLayout = new LinearLayout(this.e);
    paramImageButton.setBackgroundDrawable(a(this.e, paramString));
    paramImageButton.setLayoutParams(new LinearLayout.LayoutParams((int)(35.0F * this.k), (int)(35.0F * this.k)));
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2, 1.0F));
    localLinearLayout.setGravity(17);
    localLinearLayout.addView(paramImageButton);
    return localLinearLayout;
  }

  private boolean a(int paramInt1, int paramInt2)
  {
    int i1 = -1 + Integer.toBinaryString(paramInt2).length();
    return (paramInt1 >>> i1) % 2 == (paramInt2 >>> i1) % 2;
  }

  private View d()
    throws IOException
  {
    LinearLayout localLinearLayout = new LinearLayout(this.e);
    localLinearLayout.setOrientation(0);
    localLinearLayout.setGravity(17);
    localLinearLayout.setBackgroundDrawable(a(this.e, "a_banner.png"));
    localLinearLayout.addView(j());
    localLinearLayout.addView(l());
    localLinearLayout.addView(p());
    localLinearLayout.addView(n());
    localLinearLayout.addView(h());
    return localLinearLayout;
  }

  private void e()
    throws IOException
  {
    this.r = new ImageView(this.e);
    this.r.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeStream(getClass().getClassLoader().getResourceAsStream("assets/a_loading.png"))));
    this.r.setVisibility(8);
    this.s = new RotateAnimation(0.0F, 360.0F, 1, 0.5F, 1, 0.5F);
    this.s.setDuration(1000L);
    this.s.setInterpolator(new LinearInterpolator());
    this.s.setRepeatCount(-1);
  }

  private boolean f()
  {
    if ((this.e instanceof Activity))
      return a(((Activity)this.e).getWindow().getAttributes().flags, 1024);
    return false;
  }

  private WebView g()
  {
    s();
    this.c.setVisibility(0);
    this.c.getSettings().setJavaScriptEnabled(true);
    this.c.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    this.c.getSettings().setUseWideViewPort(true);
    this.c.setDownloadListener(new DownloadListener()
    {
      public void onDownloadStart(String paramString1, String paramString2, String paramString3, String paramString4, long paramLong)
      {
        String str = paramString1.substring(paramString1.lastIndexOf("."));
        b.b().a(b.this, paramString1 + "----on Download start:" + str);
        if (Arrays.asList(b.c()).contains(str))
        {
          boolean bool = b.e(b.this) instanceof Activity;
          Activity localActivity = null;
          if (bool)
            localActivity = (Activity)b.e(b.this);
          if ((localActivity == null) || (!localActivity.isFinishing()))
          {
            b.a(b.this, true);
            b.f(b.this);
            Toast.makeText(b.e(b.this), "Loading video...", 0).show();
            Dialog localDialog = new Dialog(b.e(b.this), 16973831);
            localDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
              public void onDismiss(DialogInterface paramDialogInterface)
              {
                b.b().b(b.this, "Video dialog dismissed.");
                b.a(b.this, false);
                b.g(b.this);
                if (b.h(b.this) == null)
                  b.i(b.this);
              }
            });
            VideoView localVideoView = new VideoView(b.e(b.this));
            localVideoView.setVideoURI(Uri.parse(paramString1));
            MediaController localMediaController = new MediaController(b.e(b.this));
            localMediaController.setMediaPlayer(localVideoView);
            localMediaController.setAnchorView(localVideoView);
            localVideoView.setMediaController(localMediaController);
            localVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener()
            {
              public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
              {
                b.b().e(b.this, "Video play error.");
                return false;
              }
            });
            localVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener(localDialog)
            {
              public void onCompletion(MediaPlayer paramMediaPlayer)
              {
                this.a.dismiss();
              }
            });
            RelativeLayout localRelativeLayout = new RelativeLayout(b.e(b.this));
            localRelativeLayout.setBackgroundColor(-16777216);
            localRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
            localLayoutParams.addRule(13);
            localRelativeLayout.addView(localVideoView, localLayoutParams);
            localDialog.setContentView(localRelativeLayout);
            localDialog.show();
            localVideoView.start();
            localVideoView.requestFocus();
          }
        }
        do
        {
          return;
          b.b().b("should not alert a dialog now");
          return;
        }
        while (b.c(b.this) == null);
        b.c(b.this).a(paramString1, b.h(b.this), b.this.a);
      }
    });
    this.c.setWebViewClient(new b());
    this.c.setWebChromeClient(new WebChromeClient()
    {
      public void onProgressChanged(WebView paramWebView, int paramInt)
      {
        b.j(b.this);
        super.onProgressChanged(paramWebView, paramInt);
      }
    });
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    localLayoutParams.addRule(12);
    this.c.setLayoutParams(localLayoutParams);
    this.c.loadUrl(this.d);
    return this.c;
  }

  private LinearLayout h()
  {
    this.q = new ImageButton(this.e);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams((int)(35.0F * this.k), (int)(35.0F * this.k));
    this.q.setLayoutParams(localLayoutParams);
    this.q.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        b.i(b.this);
      }
    });
    this.q.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
          b.k(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_exit_on.png"));
        while (true)
        {
          return false;
          if (paramMotionEvent.getAction() != 1)
            continue;
          b.k(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_exit.png"));
        }
      }
    });
    return a("a_exit.png", this.q);
  }

  private void i()
  {
    this.u = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 0.0F, 1, 1.0F);
    this.u.setDuration(500L);
    this.j.startAnimation(this.u);
    this.i.postDelayed(new Runnable()
    {
      public void run()
      {
        b.l(b.this).dismiss();
      }
    }
    , 500L);
  }

  private LinearLayout j()
  {
    this.m = new ImageButton(this.e);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams((int)(35.0F * this.k), (int)(35.0F * this.k));
    this.m.setLayoutParams(localLayoutParams);
    if (this.c.canGoBack());
    for (String str = "a_preview.png"; ; str = "a_preview_off.png")
    {
      this.m.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramView)
        {
          b.m(b.this);
        }
      });
      this.m.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
        {
          if (b.a(b.this).canGoBack())
          {
            if (paramMotionEvent.getAction() != 0)
              break label48;
            b.n(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_preview_on.png"));
          }
          while (true)
          {
            return false;
            label48: if (paramMotionEvent.getAction() != 1)
              continue;
            b.n(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_preview.png"));
          }
        }
      });
      return a(str, this.m);
    }
  }

  private void k()
  {
    if (this.c == null);
    do
      return;
    while (!this.c.canGoBack());
    this.c.goBack();
    r();
  }

  private LinearLayout l()
  {
    this.n = new ImageButton(this.e);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams((int)(35.0F * this.k), (int)(35.0F * this.k));
    this.n.setLayoutParams(localLayoutParams);
    this.n.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        b.o(b.this);
      }
    });
    this.n.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (b.a(b.this).canGoForward())
        {
          if (paramMotionEvent.getAction() != 0)
            break label48;
          b.p(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_next_on.png"));
        }
        while (true)
        {
          return false;
          label48: if (paramMotionEvent.getAction() != 1)
            continue;
          b.p(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_next.png"));
        }
      }
    });
    return a("a_next_off.png", this.n);
  }

  private void m()
  {
    if (this.c == null);
    do
      return;
    while (!this.c.canGoForward());
    this.c.goForward();
    r();
  }

  private LinearLayout n()
  {
    this.o = new ImageButton(this.e);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams((int)(35.0F * this.k), (int)(35.0F * this.k));
    this.o.setLayoutParams(localLayoutParams);
    this.o.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          b.q(b.this);
          return;
        }
        catch (Exception localException)
        {
          b.b().e(this, "intent " + b.r(b.this) + " error");
        }
      }
    });
    this.o.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
          b.s(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_out_on.png"));
        while (true)
        {
          return false;
          if (paramMotionEvent.getAction() != 1)
            continue;
          b.s(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_out.png"));
        }
      }
    });
    return a("a_out.png", this.o);
  }

  private void o()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(this.d));
    this.e.startActivity(localIntent);
  }

  private LinearLayout p()
  {
    this.p = new ImageButton(this.e);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams((int)(35.0F * this.k), (int)(35.0F * this.k));
    this.p.setLayoutParams(localLayoutParams);
    this.p.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        try
        {
          b.t(b.this);
          return;
        }
        catch (Exception localException)
        {
          b.b().e(this, "intent " + b.r(b.this) + " error");
        }
      }
    });
    this.p.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        if (paramMotionEvent.getAction() == 0)
          b.u(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_refresh_on.png"));
        while (true)
        {
          return false;
          if (paramMotionEvent.getAction() != 1)
            continue;
          b.u(b.this).setBackgroundDrawable(b.a(b.this, b.e(b.this), "a_refresh.png"));
        }
      }
    });
    return a("a_refresh.png", this.p);
  }

  private void q()
  {
    if (this.c == null)
      return;
    this.c.reload();
  }

  private void r()
  {
    if (this.c.canGoBack())
      this.m.setBackgroundDrawable(a(this.e, "a_preview.png"));
    while (this.c.canGoForward())
    {
      this.n.setBackgroundDrawable(a(this.e, "a_next.png"));
      return;
      this.m.setBackgroundDrawable(a(this.e, "a_preview_off.png"));
    }
    this.n.setBackgroundDrawable(a(this.e, "a_next_off.png"));
  }

  private void s()
  {
    int i1 = 1;
    int i2;
    if (this.r != null)
    {
      i2 = i1;
      if (this.r == null)
        break label49;
    }
    while (true)
    {
      if ((i2 & i1) != 0)
      {
        this.r.setVisibility(0);
        this.r.startAnimation(this.s);
      }
      return;
      i2 = 0;
      break;
      label49: i1 = 0;
    }
  }

  private void t()
  {
    int i1 = 1;
    int i2;
    int i3;
    label20: int i4;
    if (this.r != null)
    {
      i2 = i1;
      if (this.r == null)
        break label61;
      i3 = i1;
      i4 = i2 & i3;
      if (this.w)
        break label66;
    }
    while (true)
    {
      if ((i4 & i1) != 0)
      {
        this.r.clearAnimation();
        this.r.setVisibility(8);
      }
      return;
      i2 = 0;
      break;
      label61: i3 = 0;
      break label20;
      label66: i1 = 0;
    }
  }

  public Dialog a()
    throws Exception
  {
    b.a(this, "Start to build FS/RFS landingpage");
    this.j = new RelativeLayout(this.e);
    this.j.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
    this.j.addView(g());
    RelativeLayout.LayoutParams localLayoutParams1 = new RelativeLayout.LayoutParams(-2, -2);
    localLayoutParams1.addRule(13);
    this.j.addView(this.r, localLayoutParams1);
    if (f())
    {
      this.g = new Dialog(this.e, 16973841);
      ((RelativeLayout.LayoutParams)this.c.getLayoutParams()).bottomMargin = (int)(45.0F * this.k);
      RelativeLayout.LayoutParams localLayoutParams3 = new RelativeLayout.LayoutParams(-1, (int)(45.0F * this.k));
      localLayoutParams3.addRule(12);
      this.j.addView(this.l, localLayoutParams3);
    }
    while (true)
    {
      this.t = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, 1.0F, 1, 0.0F);
      this.t.setDuration(500L);
      this.j.startAnimation(this.t);
      this.g.getWindow().getAttributes().dimAmount = 0.5F;
      this.g.getWindow().setFlags(2, 2);
      this.g.setContentView(this.j);
      this.g.setOnDismissListener(new DialogInterface.OnDismissListener()
      {
        public void onDismiss(DialogInterface paramDialogInterface)
        {
          b.b(b.this).removeView(b.a(b.this));
          if (b.c(b.this) != null)
            b.c(b.this).h(b.this.a);
          if ((!b.d(b.this)) && (b.c(b.this) != null))
            b.c(b.this).g(b.this.a);
        }
      });
      return this.g;
      this.g = new Dialog(this.e, 16973840);
      ((RelativeLayout.LayoutParams)this.c.getLayoutParams()).bottomMargin = (int)(45.0F * this.k);
      RelativeLayout.LayoutParams localLayoutParams2 = new RelativeLayout.LayoutParams(-1, (int)(45.0F * this.k));
      localLayoutParams2.addRule(12);
      this.j.addView(this.l, localLayoutParams2);
    }
  }

  public static abstract interface a
  {
    public abstract void a(String paramString, WebView paramWebView, AdInfo paramAdInfo);

    public abstract void a(String paramString1, String paramString2, AdInfo paramAdInfo);

    public abstract void e(AdInfo paramAdInfo);

    public abstract void f(AdInfo paramAdInfo);

    public abstract void g(AdInfo paramAdInfo);

    public abstract void h(AdInfo paramAdInfo);
  }

  class b extends WebViewClient
  {
    b()
    {
    }

    public void onPageFinished(WebView paramWebView, String paramString)
    {
      super.onPageFinished(paramWebView, paramString);
      b.b().a("onPageFinished with URL:" + paramString);
      b.b(b.this, true);
      if (b.c(b.this) != null)
        b.c(b.this).e(b.this.a);
      b.g(b.this);
      b.j(b.this);
    }

    public void onPageStarted(WebView paramWebView, String paramString, Bitmap paramBitmap)
    {
      super.onPageStarted(paramWebView, paramString, paramBitmap);
      b.b().a("onPageStarted with URL:" + paramString);
      b.f(b.this);
      b.j(b.this);
    }

    public void onReceivedError(WebView paramWebView, int paramInt, String paramString1, String paramString2)
    {
      super.onReceivedError(paramWebView, paramInt, paramString1, paramString2);
      if (b.c(b.this) != null)
        b.c(b.this).f(b.this.a);
      p localp = b.b();
      b localb = b.this;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      arrayOfObject[1] = paramString1;
      arrayOfObject[2] = paramString2;
      localp.e(localb, String.format("WebView ReceivedError, errorCode=%d, description=%s, failingUrl=%s", arrayOfObject));
    }

    public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
    {
      b.b().a("Override URL loading in landing page:" + paramString);
      Uri localUri;
      if (paramString.startsWith(v.a()))
      {
        localUri = Uri.parse(paramString);
        String str1 = localUri.getHost();
        if (str1.equals("close"))
        {
          b.i(b.this);
          return true;
        }
        if (!str1.equals("inapp"));
      }
      while (true)
      {
        try
        {
          String str3 = localUri.getQueryParameter("url");
          str2 = str3;
          b.b().a("Load landing page with URL:" + str2);
          paramWebView.loadUrl(str2);
          if (b.c(b.this) == null)
            break;
          b.c(b.this).a(paramString, b.a(b.this), b.this.a);
          return true;
        }
        catch (Exception localException)
        {
          b.b().e("Error happened during loading Landing Page.");
          b.b().a(localException);
          String str2 = null;
          continue;
        }
        if (!paramString.startsWith("http"))
          continue;
        paramWebView.loadUrl(paramString);
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.a.b
 * JD-Core Version:    0.6.0
 */