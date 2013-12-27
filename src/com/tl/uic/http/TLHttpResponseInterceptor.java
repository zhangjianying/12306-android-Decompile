package com.tl.uic.http;

import com.tl.uic.TLFCache;
import com.tl.uic.Tealeaf;
import com.tl.uic.util.LogInternal;
import java.io.IOException;
import java.util.Date;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

public class TLHttpResponseInterceptor
  implements HttpResponseInterceptor
{
  private final long initTime;
  private final Date startTime;
  private final TLHttpRequestInterceptor tlHttpRequestInterceptor;

  public TLHttpResponseInterceptor(TLHttpRequestInterceptor paramTLHttpRequestInterceptor)
  {
    this.tlHttpRequestInterceptor = paramTLHttpRequestInterceptor;
    this.startTime = new Date();
    this.initTime = TLFCache.timestampFromSession();
  }

  public final void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws HttpException, IOException
  {
    try
    {
      Date localDate1 = new Date();
      Date localDate2 = new Date();
      long l1 = new Date().getTime() - localDate2.getTime();
      long l2 = localDate1.getTime() - this.startTime.getTime();
      Tealeaf.logConnection(this.tlHttpRequestInterceptor.getUrl(), paramHttpResponse, this.initTime, l1, l2);
      return;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.http.TLHttpResponseInterceptor
 * JD-Core Version:    0.6.0
 */