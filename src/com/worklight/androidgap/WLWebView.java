package com.worklight.androidgap;

import android.content.Context;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import com.worklight.androidgap.plugin.WebResourcesDownloaderPlugin;
import java.io.File;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.api.PluginManager;

public class WLWebView extends CordovaWebView
{
  public WLWebView(Context paramContext)
  {
    super(paramContext);
    WebSettings localWebSettings = getSettings();
    localWebSettings.setDefaultTextEncodingName("utf-8");
    localWebSettings.setDatabaseEnabled(true);
    String str = paramContext.getApplicationContext().getDir("database", 0).getPath();
    if (str != null)
      localWebSettings.setDatabasePath(str);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = false;
    switch (paramInt)
    {
    default:
    case 82:
    case 4:
    }
    do
    {
      bool = super.onKeyDown(paramInt, paramKeyEvent);
      return bool;
    }
    while (!((WebResourcesDownloaderPlugin)this.pluginManager.getPlugin("WebResourcesDownloader")).isUpdating());
    return false;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.WLWebView
 * JD-Core Version:    0.6.0
 */