package cn.domob.wall.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

public class DService
{
  private static p a = new p(DService.class.getSimpleName());
  private static String e = "online";
  private k b;
  private b c;
  private cn.domob.wall.core.download.k d;

  public DService(Context paramContext, String paramString1, String paramString2)
  {
    this.b = new k(paramContext, paramString1, paramString2);
    this.c = new b(paramContext, this.b);
    this.d = cn.domob.wall.core.download.k.a(paramContext);
  }

  public static String getEndpoint()
  {
    return e;
  }

  public static void setEndpoint(String paramString)
  {
    e = paramString;
  }

  public void doImpReport(AdInfo paramAdInfo)
  {
    q.a().a(this.b, paramAdInfo);
  }

  public void doImpReports(ArrayList<AdInfo> paramArrayList)
  {
    m.a().a(this.b, paramArrayList);
  }

  public void doUserActionReport(ReportUserActionType paramReportUserActionType)
  {
    m.a().a(this.b, paramReportUserActionType);
  }

  public void onClickDetailsPageButton(DetailsPageInfo paramDetailsPageInfo)
  {
    this.c.a(paramDetailsPageInfo);
    a.b("监听到详情页面上的按钮被点击");
  }

  public void onClickWallItem(AdInfo paramAdInfo)
  {
    this.c.b(paramAdInfo);
    a.b("监听到广告被点击");
  }

  public void onClickWallItemButton(AdInfo paramAdInfo)
  {
    this.c.a(paramAdInfo);
    a.b("监听到广告列表上的按钮被点击");
  }

  public void requestDataAsyn()
  {
    this.b.a();
  }

  public Drawable requestImage(Context paramContext, String paramString)
  {
    return u.a().a(paramContext, paramString);
  }

  public void requestImage(String paramString, ImageView paramImageView, OnImageDownload paramOnImageDownload)
  {
    this.d.a(paramString, paramImageView, paramOnImageDownload);
  }

  public void requestImageAsyn(Context paramContext, String paramString, ImageView paramImageView)
  {
    u.a().a(paramContext, paramString, paramImageView, 0);
  }

  public void requestImageAsyn(Context paramContext, String paramString, ImageView paramImageView, int paramInt)
  {
    u.a().a(paramContext, paramString, paramImageView, paramInt);
  }

  public void setReceiveDataListener(ReceiveDataListener paramReceiveDataListener)
  {
    this.b.a(paramReceiveDataListener);
  }

  public void setShowDetailsPageListener(ShowDetailsPageListener paramShowDetailsPageListener)
  {
    this.c.a(paramShowDetailsPageListener);
  }

  public void setloadImgLock()
  {
    this.d.b();
  }

  public void setloadImgUnLock()
  {
    this.d.c();
  }

  public void wallTurnHide()
  {
  }

  public static enum ErrorCode
  {
    static
    {
      ErrorCode[] arrayOfErrorCode = new ErrorCode[4];
      arrayOfErrorCode[0] = INTERNAL_ERROR;
      arrayOfErrorCode[1] = INVALID_REQUEST;
      arrayOfErrorCode[2] = NETWORK_ERROR;
      arrayOfErrorCode[3] = NO_FILL;
      a = arrayOfErrorCode;
    }
  }

  public static abstract interface OnImageDownload
  {
    public abstract void onDownloadSuc(Bitmap paramBitmap, String paramString, ImageView paramImageView);
  }

  public static abstract interface ReceiveDataListener
  {
    public abstract void onFailReceiveData(DService.ErrorCode paramErrorCode, String paramString);

    public abstract void onSuccessReceiveData(List<AdInfo> paramList1, List<AdInfo> paramList2, ControlInfo paramControlInfo);
  }

  public static enum ReportUserActionType
  {
    static
    {
      ENTRY = new ReportUserActionType("ENTRY", 1);
      EXIT = new ReportUserActionType("EXIT", 2);
      PGDN = new ReportUserActionType("PGDN", 3);
      START = new ReportUserActionType("START", 4);
      ReportUserActionType[] arrayOfReportUserActionType = new ReportUserActionType[5];
      arrayOfReportUserActionType[0] = NONE;
      arrayOfReportUserActionType[1] = ENTRY;
      arrayOfReportUserActionType[2] = EXIT;
      arrayOfReportUserActionType[3] = PGDN;
      arrayOfReportUserActionType[4] = START;
      a = arrayOfReportUserActionType;
    }
  }

  public static abstract interface ShowDetailsPageListener
  {
    public abstract void onShowDetailsPage(DetailsPageInfo paramDetailsPageInfo);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.DService
 * JD-Core Version:    0.6.0
 */