package com.tl.uic.webkit;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UICWebViewClient extends WebViewClient
{
  public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
  {
    paramWebView.loadUrl(paramString);
    return true;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.webkit.UICWebViewClient
 * JD-Core Version:    0.6.0
 */