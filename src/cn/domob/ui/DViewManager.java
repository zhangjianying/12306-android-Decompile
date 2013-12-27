package cn.domob.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import cn.domob.wall.core.AdInfo;
import cn.domob.wall.core.ControlInfo;
import cn.domob.wall.core.DService;
import cn.domob.wall.core.DService.ErrorCode;
import cn.domob.wall.core.DService.ReceiveDataListener;
import cn.domob.wall.core.DService.ReportUserActionType;
import cn.domob.wall.core.DService.ShowDetailsPageListener;
import cn.domob.wall.core.DetailsPageInfo;
import java.util.ArrayList;
import java.util.List;

public class DViewManager
{
  private static o d = new o(DViewManager.class.getSimpleName());
  private static final int g = 0;
  private static final int h = 1;
  boolean a = false;
  boolean b = false;
  boolean c = true;
  private Context e;
  private DService f = null;
  private ViewGroup i = null;
  private ViewGroup j = null;
  private View k;
  private View l;
  private Dialog m;
  private View n;
  private Dialog o;
  private l p;
  private ArrayList<AdInfo> q = new ArrayList();
  private ArrayList<AdInfo> r = new ArrayList();
  private ControlInfo s;
  private boolean t = false;
  private GetDataListener u;

  public DViewManager(Context paramContext, DService paramDService)
  {
    this.e = paramContext;
    this.f = paramDService;
  }

  private RelativeLayout.LayoutParams a(int paramInt)
  {
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
    switch (paramInt)
    {
    default:
      return localLayoutParams;
    case 1:
      localLayoutParams.addRule(15, -1);
      localLayoutParams.addRule(14, -1);
      return localLayoutParams;
    case 0:
    }
    localLayoutParams.addRule(15, -1);
    localLayoutParams.addRule(9, -1);
    return localLayoutParams;
  }

  private void a(DetailsPageInfo paramDetailsPageInfo)
  {
    ((Activity)this.e).runOnUiThread(new Runnable(paramDetailsPageInfo)
    {
      public void run()
      {
        DViewManager.c(DViewManager.this, new j(DViewManager.f(DViewManager.this), DViewManager.e(DViewManager.this)).a(this.a));
        DViewManager.o(DViewManager.this).findViewById(d.a(DViewManager.f(DViewManager.this), "details_close")).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            DViewManager.n(DViewManager.this);
          }
        });
        DViewManager.a(DViewManager.this, new Dialog(DViewManager.f(DViewManager.this)));
        DViewManager.p(DViewManager.this).requestWindowFeature(1);
        DViewManager.p(DViewManager.this).setContentView(DViewManager.o(DViewManager.this));
        DViewManager.p(DViewManager.this).show();
      }
    });
  }

  private void a(String paramString, DService.ErrorCode paramErrorCode)
  {
    ((Activity)this.e).runOnUiThread(new Runnable(paramErrorCode, paramString)
    {
      public void run()
      {
        if (DViewManager.a(DViewManager.this) == null)
        {
          DViewManager.a().e("错误码：" + this.a.toString() + "错误描述：" + this.b);
          DViewManager.i(DViewManager.this).a();
          return;
        }
        Toast.makeText(DViewManager.f(DViewManager.this), "网络链接错误", 0).show();
      }
    });
  }

  private void a(boolean paramBoolean)
  {
    ((Activity)this.e).runOnUiThread(new Runnable(paramBoolean)
    {
      public void run()
      {
        DViewManager.b(DViewManager.this, DViewManager.i(DViewManager.this).a(this.a, DViewManager.f(DViewManager.this), DViewManager.j(DViewManager.this), DViewManager.k(DViewManager.this), DViewManager.d(DViewManager.this), DViewManager.e(DViewManager.this)));
        DViewManager.m(DViewManager.this).findViewById(d.a(DViewManager.f(DViewManager.this), "bar_button")).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            DViewManager.a().b("关闭应用墙首页");
            DViewManager.l(DViewManager.this).dismiss();
          }
        });
        ((Activity)DViewManager.f(DViewManager.this)).setRequestedOrientation(1);
        DViewManager.l(DViewManager.this).setContentView(DViewManager.m(DViewManager.this));
        DViewManager.l(DViewManager.this).show();
        DViewManager.l(DViewManager.this).setOnDismissListener(new DialogInterface.OnDismissListener()
        {
          public void onDismiss(DialogInterface paramDialogInterface)
          {
            DViewManager.a().b("应用墙关闭");
            DViewManager.e(DViewManager.this).wallTurnHide();
            DViewManager.e(DViewManager.this).doUserActionReport(DService.ReportUserActionType.EXIT);
            ((Activity)DViewManager.f(DViewManager.this)).setRequestedOrientation(2);
          }
        });
      }
    });
  }

  private void b()
  {
    ((Activity)this.e).runOnUiThread(new Runnable()
    {
      public void run()
      {
        DViewManager.a(DViewManager.this, new k(DViewManager.f(DViewManager.this), DViewManager.e(DViewManager.this), DViewManager.d(DViewManager.this)).a());
        if (DViewManager.g(DViewManager.this) == null)
          DViewManager.a(DViewManager.this).addView(DViewManager.h(DViewManager.this), DViewManager.a(DViewManager.this, 0));
        while (true)
        {
          DViewManager.h(DViewManager.this).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              DViewManager.a().b("打开应用墙首页");
              DViewManager.e(DViewManager.this).doUserActionReport(DService.ReportUserActionType.ENTRY);
              DViewManager.b(DViewManager.this, true);
            }
          });
          return;
          DViewManager.g(DViewManager.this).addView(DViewManager.h(DViewManager.this));
        }
      }
    });
  }

  private void c()
  {
    d.b("关闭应用墙详情界面");
    this.m.dismiss();
  }

  public void addView()
  {
    addView(null, null);
  }

  public void addView(ViewGroup paramViewGroup)
  {
    addView(paramViewGroup, null);
  }

  public void addView(ViewGroup paramViewGroup1, ViewGroup paramViewGroup2)
  {
    if ((e.a(this.e, "l_handle") == -1) || (e.a(this.e, "l_home") == -1))
    {
      Log.e("尊敬的开发者：", "请参照文档添加res下资源");
      Toast.makeText(this.e, "亲，请参照文档添加res下资源", 1).show();
    }
    do
      return;
    while (!this.c);
    this.c = false;
    this.p = new l();
    devModelInit();
    this.o = new Dialog(this.e, 16973840);
    this.i = paramViewGroup1;
    if (paramViewGroup2 != null)
      this.j = paramViewGroup2;
    this.f.setShowDetailsPageListener(new DService.ShowDetailsPageListener()
    {
      public void onShowDetailsPage(DetailsPageInfo paramDetailsPageInfo)
      {
        DViewManager.a().b("打开广告详情页面");
        if (paramDetailsPageInfo != null)
          DViewManager.a(DViewManager.this, paramDetailsPageInfo);
      }
    });
    this.f.setReceiveDataListener(new DService.ReceiveDataListener()
    {
      public void onFailReceiveData(DService.ErrorCode paramErrorCode, String paramString)
      {
        DViewManager.this.c = true;
        DViewManager.a(DViewManager.this, paramString, paramErrorCode);
        if (DViewManager.c(DViewManager.this) != null)
          DViewManager.c(DViewManager.this).onFailReceiveData();
      }

      public void onSuccessReceiveData(List<AdInfo> paramList1, List<AdInfo> paramList2, ControlInfo paramControlInfo)
      {
        DViewManager.this.c = true;
        DViewManager.a().b("获取到广告信息");
        DViewManager.a(DViewManager.this, (ArrayList)paramList2);
        DViewManager.b(DViewManager.this, (ArrayList)paramList1);
        DViewManager.a(DViewManager.this, paramControlInfo);
        if (DViewManager.a(DViewManager.this) == null);
        while (true)
        {
          DViewManager.a(DViewManager.this, true);
          if ((DViewManager.c(DViewManager.this) != null) && (DViewManager.d(DViewManager.this) != null))
            DViewManager.this.initHomeView();
          return;
          DViewManager.a().b("显示把手");
          DViewManager.b(DViewManager.this);
        }
      }
    });
    this.f.requestDataAsyn();
  }

  public void addViewAndShow()
  {
    if (!this.c)
      return;
    this.c = false;
    this.p = new l();
    devModelInit();
    this.o = new Dialog(this.e, 16973840);
    this.f.setShowDetailsPageListener(new DService.ShowDetailsPageListener()
    {
      public void onShowDetailsPage(DetailsPageInfo paramDetailsPageInfo)
      {
        DViewManager.a().b("打开广告详情页面");
        if (paramDetailsPageInfo != null)
          DViewManager.a(DViewManager.this, paramDetailsPageInfo);
      }
    });
    this.f.setReceiveDataListener(new DService.ReceiveDataListener()
    {
      public void onFailReceiveData(DService.ErrorCode paramErrorCode, String paramString)
      {
        DViewManager.this.c = true;
        DViewManager.a(DViewManager.this, paramString, paramErrorCode);
        if (DViewManager.c(DViewManager.this) != null)
          DViewManager.c(DViewManager.this).onFailReceiveData();
      }

      public void onSuccessReceiveData(List<AdInfo> paramList1, List<AdInfo> paramList2, ControlInfo paramControlInfo)
      {
        DViewManager.this.c = true;
        DViewManager.a().b("获取到广告信息");
        DViewManager.a(DViewManager.this, (ArrayList)paramList2);
        DViewManager.b(DViewManager.this, (ArrayList)paramList1);
        DViewManager.a(DViewManager.this, paramControlInfo);
        DViewManager.a(DViewManager.this, true);
        if ((DViewManager.c(DViewManager.this) != null) && (DViewManager.d(DViewManager.this) != null))
          DViewManager.this.initHomeView();
        DViewManager.a().b("打开应用墙首页页面");
        DViewManager.b(DViewManager.this, true);
        DViewManager.e(DViewManager.this).doUserActionReport(DService.ReportUserActionType.ENTRY);
      }
    });
    this.f.requestDataAsyn();
  }

  public void devModelInit()
  {
    this.a = true;
    this.b = false;
    this.p.a(false, this.b, false);
    this.p.a(this.a);
  }

  public void doAppStartReport()
  {
    this.f.doUserActionReport(DService.ReportUserActionType.START);
  }

  public void doEntranceClickReport()
  {
    this.f.doUserActionReport(DService.ReportUserActionType.ENTRY);
    if (this.a)
    {
      if (this.q != null)
      {
        for (int i2 = 0; i2 < this.q.size(); i2++)
          ((AdInfo)this.q.get(i2)).setAdActualPosition(i2);
        this.f.doImpReports(this.q);
      }
      if (this.b)
      {
        ArrayList localArrayList = this.r;
        int i1 = 0;
        if (localArrayList != null)
        {
          while (i1 < this.r.size())
          {
            ((AdInfo)this.r.get(i1)).setAdActualPosition(i1);
            i1++;
          }
          this.f.doImpReports(this.r);
        }
      }
    }
  }

  public void doExitReport()
  {
    this.f.doUserActionReport(DService.ReportUserActionType.EXIT);
  }

  public int getNewAdTotals()
  {
    if (this.s != null)
      return this.s.getNumberOfNewAd();
    return 0;
  }

  public View getView()
  {
    return this.n;
  }

  public void initHomeView()
  {
    ((Activity)this.e).runOnUiThread(new Runnable()
    {
      public void run()
      {
        DViewManager.b(DViewManager.this, DViewManager.i(DViewManager.this).a(true, DViewManager.f(DViewManager.this), DViewManager.j(DViewManager.this), DViewManager.k(DViewManager.this), DViewManager.d(DViewManager.this), DViewManager.e(DViewManager.this)));
        DViewManager.m(DViewManager.this).findViewById(d.a(DViewManager.f(DViewManager.this), "bar_button")).setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramView)
          {
            DViewManager.a().b("关闭应用墙首页");
            DViewManager.l(DViewManager.this).dismiss();
          }
        });
        DViewManager.c(DViewManager.this).onSuccessReceiveData(DViewManager.d(DViewManager.this).getNumberOfNewAd());
      }
    });
  }

  public boolean isAdDataReady()
  {
    return this.t;
  }

  public void setAddViewListener(GetDataListener paramGetDataListener)
  {
    this.u = paramGetDataListener;
  }

  public void showWall()
  {
    if (isAdDataReady())
    {
      d.b("打开应用墙首页页面");
      a(true);
      this.f.doUserActionReport(DService.ReportUserActionType.ENTRY);
    }
    do
      return;
    while (!this.c);
    addViewAndShow();
  }

  public static abstract interface GetDataListener
  {
    public abstract void onFailReceiveData();

    public abstract void onSuccessReceiveData(int paramInt);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.ui.DViewManager
 * JD-Core Version:    0.6.0
 */