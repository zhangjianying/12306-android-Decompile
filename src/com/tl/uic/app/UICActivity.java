package com.tl.uic.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import com.tl.uic.Tealeaf;
import com.tl.uic.util.UICOnGlobalLayoutListener;
import java.util.Date;

public abstract class UICActivity extends Activity
{
  private int imageBackground = -16777216;
  private String logicalPageName = "";
  private long millisecondSnapshotDelay = 0L;
  private int numOnGlobalLayoutListener = 0;
  private Boolean takeSnapshotAfterCreate = Boolean.valueOf(false);
  private Boolean tookImage = Boolean.valueOf(false);
  private View view;

  public final int getImageBackground()
  {
    return this.imageBackground;
  }

  public final String getLogicalPageName()
  {
    if ((this.logicalPageName == null) || (this.logicalPageName.equals("")))
      this.logicalPageName = (getClass().getName().substring(1 + getClass().getName().lastIndexOf('.')) + "_" + new Date().getTime());
    return this.logicalPageName;
  }

  public final long getMillisecondSnapshotDelay()
  {
    return this.millisecondSnapshotDelay;
  }

  public final int getNumOnGlobalLayoutListener()
  {
    return this.numOnGlobalLayoutListener;
  }

  public final Boolean getTakeSnapshotAfterCreate()
  {
    return this.takeSnapshotAfterCreate;
  }

  public final Boolean getTookImage()
  {
    return this.tookImage;
  }

  public final View getView()
  {
    return this.view;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.view = ((ViewGroup)getWindow().getDecorView()).getChildAt(0);
    this.view.setBackgroundColor(getImageBackground());
    this.view.getViewTreeObserver().addOnGlobalLayoutListener(new UICOnGlobalLayoutListener(this, this.view));
    this.numOnGlobalLayoutListener = (1 + this.numOnGlobalLayoutListener);
  }

  protected void onDestroy()
  {
    Tealeaf.onDestroy(this, getLogicalPageName());
    super.onDestroy();
  }

  protected void onPause()
  {
    Tealeaf.onPause(this, getLogicalPageName());
    super.onPause();
  }

  protected void onResume()
  {
    Tealeaf.onResume(this, getLogicalPageName());
    super.onResume();
  }

  public final void setImageBackground(int paramInt)
  {
    this.imageBackground = paramInt;
  }

  public final void setLogicalPageName(String paramString)
  {
    this.logicalPageName = paramString;
  }

  public final void setMillisecondSnapshotDelay(long paramLong)
  {
    this.millisecondSnapshotDelay = paramLong;
  }

  public final void setNumOnGlobalLayoutListener(int paramInt)
  {
    this.numOnGlobalLayoutListener = paramInt;
  }

  public final void setTakeSnapshotAfterCreate(Boolean paramBoolean)
  {
    this.takeSnapshotAfterCreate = paramBoolean;
  }

  public final void setTookImage(Boolean paramBoolean)
  {
    this.tookImage = paramBoolean;
  }

  public final void setView(View paramView)
  {
    this.view = paramView;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.app.UICActivity
 * JD-Core Version:    0.6.0
 */