package com.MobileTicket;

import android.os.Build.VERSION;
import android.os.Bundle;
import cn.domob.ui.DViewManager;
import cn.domob.wall.core.DService;
import com.worklight.androidgap.SSLWLWebView;
import com.worklight.androidgap.WLDroidGap;
import org.apache.cordova.CordovaChromeClient;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import org.apache.cordova.api.PluginManager;

public class MobileTicket extends WLDroidGap
{
  public static final int HONEYCOMB = 11;
  private static final String PLACEMENTID = "16TLwHboApIGwNU-ypi0ThBk";
  private static final String PUBLISHERID = "56OJyf1IuNPVnYbzz4";
  public static DService mDService;
  public static DViewManager mDViewManager;

  protected void bindBrowser(CordovaWebView paramCordovaWebView, boolean paramBoolean)
  {
    super.bindBrowser(paramCordovaWebView, paramBoolean);
    mDService = new DService(this, "56OJyf1IuNPVnYbzz4", "16TLwHboApIGwNU-ypi0ThBk");
    mDViewManager = new DViewManager(this, mDService);
    mDViewManager.doAppStartReport();
    paramCordovaWebView.pluginManager.addService("WebResourcesDownloader", "com.worklight.androidgap.plugin.SSLWebResourcesDownloaderPlugin");
    paramCordovaWebView.pluginManager.addService("NativeBusyIndicator", "com.worklight.androidgap.plugin.MyBusyIndicator");
  }

  public void init()
  {
    SSLWLWebView localSSLWLWebView = new SSLWLWebView(this);
    if (Build.VERSION.SDK_INT < 11);
    for (Object localObject = new CordovaWebViewClient(this, localSSLWLWebView); ; localObject = new IceCreamCordovaWebViewClient(this, localSSLWLWebView))
    {
      super.init(localSSLWLWebView, (CordovaWebViewClient)localObject, new CordovaChromeClient(this, localSSLWLWebView));
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }

  protected void onRestart()
  {
    super.onRestart();
    if (WebPayActivity.isCallJSFlag())
    {
      this.appView.loadUrl("javascript:orderComplete()");
      WebPayActivity.setCallJSFlag(false);
    }
  }

  public void onWLInitCompleted(Bundle paramBundle)
  {
    super.setIntegerProperty("splashscreen", 2130837513);
    super.loadUrl(getWebMainFilePath());
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.MobileTicket.MobileTicket
 * JD-Core Version:    0.6.0
 */