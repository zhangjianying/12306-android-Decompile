package com.tl.uic.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.tl.uic.app.UICActivity;
import com.tl.uic.webkit.UICWebView;

public class UICOnGlobalLayoutListener
  implements ViewTreeObserver.OnGlobalLayoutListener
{
  private final UICActivity uicActivity;
  private final View view;

  public UICOnGlobalLayoutListener(UICActivity paramUICActivity, View paramView)
  {
    this.uicActivity = paramUICActivity;
    this.view = paramView;
  }

  public final void onGlobalLayout()
  {
    if ((this.view instanceof ViewGroup));
    for (int i = 0; ; i++)
    {
      if (i >= ((ViewGroup)this.view).getChildCount())
      {
        this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        this.uicActivity.setNumOnGlobalLayoutListener(-1 + this.uicActivity.getNumOnGlobalLayoutListener());
        if ((!(this.view instanceof UICWebView)) && (this.uicActivity.getTakeSnapshotAfterCreate().booleanValue()) && (!this.uicActivity.getTookImage().booleanValue()) && (this.uicActivity.getNumOnGlobalLayoutListener() == 0))
          new Thread()
          {
            public void run()
            {
              UICOnGlobalLayoutListener.this.view.postDelayed(new Runnable()
              {
                public void run()
                {
                  ScreenShotTask localScreenShotTask = new ScreenShotTask();
                  View[] arrayOfView = new View[1];
                  arrayOfView[0] = UICOnGlobalLayoutListener.access$0(UICOnGlobalLayoutListener.this);
                  localScreenShotTask.execute(arrayOfView);
                  UICOnGlobalLayoutListener.this.uicActivity.setTookImage(Boolean.valueOf(true));
                }
              }
              , UICOnGlobalLayoutListener.this.uicActivity.getMillisecondSnapshotDelay());
            }
          }
          .start();
        return;
      }
      setListenersOnChildren(((ViewGroup)this.view).getChildAt(i));
    }
  }

  public final void setListenersOnChildren(View paramView)
  {
    ViewGroup localViewGroup;
    if ((paramView instanceof ViewGroup))
      localViewGroup = (ViewGroup)paramView;
    for (int i = 0; ; i++)
    {
      if (i >= localViewGroup.getChildCount())
        return;
      View localView = localViewGroup.getChildAt(i);
      if (((localView instanceof ListView)) || ((localView instanceof ImageView)) || ((localView instanceof LinearLayout)) || ((localView instanceof TextView)))
        continue;
      localView.getViewTreeObserver().addOnGlobalLayoutListener(new UICOnGlobalLayoutListener(this.uicActivity, localView));
      this.uicActivity.setNumOnGlobalLayoutListener(1 + this.uicActivity.getNumOnGlobalLayoutListener());
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.UICOnGlobalLayoutListener
 * JD-Core Version:    0.6.0
 */