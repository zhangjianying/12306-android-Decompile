package org.apache.cordova;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginManager;

public class IceCreamCordovaWebViewClient extends CordovaWebViewClient
{
  public IceCreamCordovaWebViewClient(CordovaInterface paramCordovaInterface)
  {
    super(paramCordovaInterface);
  }

  public IceCreamCordovaWebViewClient(CordovaInterface paramCordovaInterface, CordovaWebView paramCordovaWebView)
  {
    super(paramCordovaInterface, paramCordovaWebView);
  }

  private WebResourceResponse generateWebResourceResponse(String paramString)
  {
    if (paramString.startsWith("file:///android_asset/"))
    {
      String str1 = paramString.replaceFirst("file:///android_asset/", "");
      if (str1.contains("?"))
        str1 = str1.split("\\?")[0];
      while (true)
      {
        boolean bool = str1.endsWith(".html");
        String str2 = null;
        if (bool)
          str2 = "text/html";
        try
        {
          WebResourceResponse localWebResourceResponse = new WebResourceResponse(str2, "UTF-8", this.cordova.getActivity().getAssets().open(Uri.parse(str1).getPath(), 2));
          return localWebResourceResponse;
          if (!str1.contains("#"))
            continue;
          str1 = str1.split("#")[0];
        }
        catch (IOException localIOException)
        {
          LOG.e("generateWebResourceResponse", localIOException.getMessage(), localIOException);
        }
      }
    }
    return null;
  }

  private WebResourceResponse getWhitelistResponse()
  {
    return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));
  }

  private static boolean needsIceCreamSpaceInAssetUrlFix(String paramString)
  {
    if (!paramString.contains("%20"))
      return false;
    switch (Build.VERSION.SDK_INT)
    {
    default:
      return false;
    case 14:
    case 15:
    }
    return true;
  }

  public WebResourceResponse shouldInterceptRequest(WebView paramWebView, String paramString)
  {
    WebResourceResponse localWebResourceResponse = super.shouldInterceptRequest(paramWebView, paramString);
    if ((!Config.isUrlWhiteListed(paramString)) && ((paramString.startsWith("http://")) || (paramString.startsWith("https://"))))
      localWebResourceResponse = getWhitelistResponse();
    do
    {
      return localWebResourceResponse;
      if ((localWebResourceResponse == null) && ((paramString.contains("?")) || (paramString.contains("#")) || (needsIceCreamSpaceInAssetUrlFix(paramString))))
        return generateWebResourceResponse(paramString);
    }
    while ((localWebResourceResponse != null) || (this.appView.pluginManager == null));
    return this.appView.pluginManager.shouldInterceptRequest(paramString);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.IceCreamCordovaWebViewClient
 * JD-Core Version:    0.6.0
 */