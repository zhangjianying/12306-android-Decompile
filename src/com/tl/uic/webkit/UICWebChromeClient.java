package com.tl.uic.webkit;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.tl.uic.app.UICActivity;
import com.tl.uic.util.LogInternal;
import com.tl.uic.util.ScreenShotTask;
import java.util.Date;

public class UICWebChromeClient extends WebChromeClient
{
  private static final int PROGRESS_100_PERCENT = 100;
  private static final int PROGRESS_CONVERTER_TO_100 = 1000;
  private final UICActivity uicActivity;

  public UICWebChromeClient(UICActivity paramUICActivity)
  {
    this.uicActivity = paramUICActivity;
  }

  public final void onConsoleMessage(String paramString1, int paramInt, String paramString2)
  {
    LogInternal.log("WebView: " + paramString1 + " -- From line " + paramInt + " of " + paramString2);
  }

  public final void onProgressChanged(WebView paramWebView, int paramInt)
  {
    this.uicActivity.setProgress(paramInt * 1000);
    if (((paramWebView instanceof UICWebView)) && (paramInt == 100))
    {
      ((UICWebView)paramWebView).setEndLoad(new Date());
      if ((this.uicActivity.getTakeSnapshotAfterCreate().booleanValue()) && (!this.uicActivity.getTookImage().booleanValue()))
        new Thread(paramWebView)
        {
          public void run()
          {
            this.val$view.postDelayed(new Runnable(this.val$view)
            {
              public void run()
              {
                ScreenShotTask localScreenShotTask = new ScreenShotTask();
                View[] arrayOfView = new View[1];
                arrayOfView[0] = this.val$view;
                localScreenShotTask.execute(arrayOfView);
                UICWebChromeClient.this.uicActivity.setTookImage(Boolean.valueOf(true));
              }
            }
            , UICWebChromeClient.this.uicActivity.getMillisecondSnapshotDelay());
          }
        }
        .start();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.webkit.UICWebChromeClient
 * JD-Core Version:    0.6.0
 */