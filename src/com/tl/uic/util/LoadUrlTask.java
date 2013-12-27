package com.tl.uic.util;

import android.os.AsyncTask;
import com.tl.uic.http.TLDefaultHttpClient;
import com.tl.uic.webkit.UICWebView;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

public class LoadUrlTask extends AsyncTask<String, Void, HttpResponse>
{
  private static final int BUFFER_SIZE = 8192;
  private final Map<String, String> extraHeaders;
  private String urlToLoad;
  private final UICWebView webView;

  public LoadUrlTask(UICWebView paramUICWebView, Map<String, String> paramMap)
  {
    this.webView = paramUICWebView;
    this.extraHeaders = paramMap;
    this.webView.setInitTime(new Date());
  }

  private HttpResponse doRequest(String paramString)
  {
    try
    {
      BasicHttpParams localBasicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, ConfigurationUtil.getInt("PostMessageTimeout"));
      HttpConnectionParams.setSoTimeout(localBasicHttpParams, ConfigurationUtil.getInt("PostMessageSocketTimeout"));
      HttpProtocolParams.setVersion(localBasicHttpParams, HttpVersion.HTTP_1_0);
      HttpProtocolParams.setUseExpectContinue(localBasicHttpParams, false);
      HttpConnectionParams.setSocketBufferSize(localBasicHttpParams, 8192);
      HttpGet localHttpGet = new HttpGet(paramString);
      Iterator localIterator;
      if (this.extraHeaders != null)
        localIterator = this.extraHeaders.entrySet().iterator();
      while (true)
      {
        if (!localIterator.hasNext())
          return new TLDefaultHttpClient(localBasicHttpParams).execute(localHttpGet);
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localHttpGet.addHeader((String)localEntry.getKey(), (String)localEntry.getValue());
      }
    }
    catch (IOException localIOException)
    {
      LogInternal.logException(localIOException);
    }
    return null;
  }

  protected final HttpResponse doInBackground(String[] paramArrayOfString)
  {
    this.urlToLoad = paramArrayOfString[0];
    return doRequest(this.urlToLoad);
  }

  protected final void onPostExecute(HttpResponse paramHttpResponse)
  {
    try
    {
      this.webView.setResponseTime(new Date().getTime() - this.webView.getInitTime().getTime());
      if (paramHttpResponse != null)
      {
        if (paramHttpResponse.getStatusLine().getStatusCode() != 200)
          return;
        HttpEntity localHttpEntity = paramHttpResponse.getEntity();
        if ((localHttpEntity != null) && (localHttpEntity.getContentLength() != 0L))
        {
          String str = HTTPUtil.convertStreamToString(localHttpEntity.getContent());
          this.webView.setHttpResponse(paramHttpResponse);
          this.webView.setStartLoad(new Date());
          this.webView.loadDataWithBaseURL(this.urlToLoad, str, "text/html", "utf-8", null);
          return;
        }
      }
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.LoadUrlTask
 * JD-Core Version:    0.6.0
 */