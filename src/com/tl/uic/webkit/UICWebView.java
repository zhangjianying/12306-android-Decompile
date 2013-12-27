package com.tl.uic.webkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import com.tl.uic.TLFCache;
import com.tl.uic.Tealeaf;
import com.tl.uic.app.UICActivity;
import com.tl.uic.javascript.JavaScriptInterface;
import java.util.Date;
import java.util.Map;
import org.apache.http.HttpResponse;

public class UICWebView extends WebView
{
  private long connectionInitFromSession;
  private Date endLoad;
  private HttpResponse httpResponse;
  private Date initTime;
  private long responseTime;
  private Date startLoad;
  private String url;

  public UICWebView(Context paramContext)
  {
    super(paramContext);
    init();
  }

  public UICWebView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }

  public UICWebView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }

  private void init()
  {
    setWebViewClient(new UICWebViewClient());
    setWebChromeClient(new UICWebChromeClient((UICActivity)getContext()));
    addJavascriptInterface(new JavaScriptInterface(getContext()), "tlBridge");
  }

  public final Date getEndLoad()
  {
    return this.endLoad;
  }

  public final HttpResponse getHttpResponse()
  {
    return this.httpResponse;
  }

  public final Date getInitTime()
  {
    return this.initTime;
  }

  public final long getResponseTime()
  {
    return this.responseTime;
  }

  public final Date getStartLoad()
  {
    return this.startLoad;
  }

  public void loadData(String paramString1, String paramString2, String paramString3)
  {
    this.url = null;
    this.initTime = null;
    this.connectionInitFromSession = 0L;
    this.responseTime = 0L;
    this.httpResponse = null;
    this.startLoad = new Date();
    super.loadDataWithBaseURL(this.url, paramString1, paramString2, paramString3, "");
  }

  @SuppressLint({"NewApi"})
  public void loadUrl(String paramString)
  {
    loadUrl(paramString, null);
  }

  public final void loadUrl(String paramString, Map<String, String> paramMap)
  {
    this.url = paramString;
    Tealeaf.setTLCookie(this.url);
    super.loadUrl(paramString, paramMap);
  }

  public final void logConnection()
  {
    long l = getEndLoad().getTime() - getStartLoad().getTime();
    Tealeaf.logConnection(this.url, this.httpResponse, this.connectionInitFromSession, l, this.responseTime);
  }

  public final void setEndLoad(Date paramDate)
  {
    this.endLoad = paramDate;
  }

  public final void setHttpResponse(HttpResponse paramHttpResponse)
  {
    this.httpResponse = paramHttpResponse;
  }

  public final void setInitTime(Date paramDate)
  {
    this.initTime = paramDate;
    this.connectionInitFromSession = TLFCache.timestampFromSession();
  }

  public final void setResponseTime(long paramLong)
  {
    this.responseTime = paramLong;
  }

  public final void setStartLoad(Date paramDate)
  {
    this.startLoad = paramDate;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.webkit.UICWebView
 * JD-Core Version:    0.6.0
 */