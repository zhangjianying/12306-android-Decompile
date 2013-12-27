package com.tl.uic.http;

import android.webkit.CookieManager;
import com.tl.uic.Tealeaf;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.RequestLine;
import org.apache.http.protocol.HttpContext;

public class TLHttpRequestInterceptor
  implements HttpRequestInterceptor
{
  private final String sessionId;
  private String url;

  public TLHttpRequestInterceptor()
  {
    this.sessionId = null;
  }

  public TLHttpRequestInterceptor(String paramString)
  {
    this.sessionId = paramString;
  }

  public final String getUrl()
  {
    return this.url;
  }

  public final void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    try
    {
      this.url = paramHttpRequest.getRequestLine().getUri();
      if (!paramHttpRequest.containsHeader("X-Tealeaf"))
        paramHttpRequest.addHeader("X-Tealeaf", "device (Android) Lib/" + Tealeaf.getLibraryVersion());
      if (!paramHttpRequest.containsHeader("X-Tealeaf-Property"))
        paramHttpRequest.addHeader("X-Tealeaf-Property", Tealeaf.getHttpXTealeafProperty());
      Iterator localIterator;
      if (Tealeaf.getAdditionalHeaders() != null)
        localIterator = Tealeaf.getAdditionalHeaders().entrySet().iterator();
      while (true)
      {
        if (!localIterator.hasNext())
        {
          StringBuffer localStringBuffer = new StringBuffer(Tealeaf.getTLCookie(this.sessionId));
          if (getUrl() != null)
          {
            String str = CookieManager.getInstance().getCookie(getUrl());
            if (str != null)
            {
              localStringBuffer.append(';');
              localStringBuffer.append(str);
            }
          }
          paramHttpRequest.addHeader("Cookie", localStringBuffer.toString());
          return;
        }
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramHttpRequest.addHeader((String)localEntry.getKey(), (String)localEntry.getValue());
      }
    }
    catch (Exception localException)
    {
      Tealeaf.logException(localException);
    }
  }

  public final void setUrl(String paramString)
  {
    this.url = paramString;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.http.TLHttpRequestInterceptor
 * JD-Core Version:    0.6.0
 */