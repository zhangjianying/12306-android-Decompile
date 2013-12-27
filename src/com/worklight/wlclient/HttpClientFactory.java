package com.worklight.wlclient;

import android.net.SSLCertificateSocketFactory;
import com.worklight.common.WLConfig;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

class HttpClientFactory
{
  private static final int SOCKET_OPERATION_TIMEOUT = 60000;
  private static DefaultHttpClient client;

  public static DefaultHttpClient getInstance(WLConfig paramWLConfig)
    throws RuntimeException
  {
    monitorenter;
    String str;
    try
    {
      DefaultHttpClient localDefaultHttpClient;
      if (client != null)
        localDefaultHttpClient = client;
      do
      {
        return localDefaultHttpClient;
        localDefaultHttpClient = null;
      }
      while (paramWLConfig == null);
      BasicHttpParams localBasicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setStaleCheckingEnabled(localBasicHttpParams, false);
      HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 60000);
      HttpConnectionParams.setSoTimeout(localBasicHttpParams, 60000);
      HttpConnectionParams.setSocketBufferSize(localBasicHttpParams, 8192);
      HttpClientParams.setRedirecting(localBasicHttpParams, false);
      SchemeRegistry localSchemeRegistry = new SchemeRegistry();
      str = paramWLConfig.getProtocol();
      int i = Integer.valueOf(paramWLConfig.getPort()).intValue();
      if (str.equalsIgnoreCase("http"))
        localSchemeRegistry.register(new Scheme(str, PlainSocketFactory.getSocketFactory(), i));
      while (true)
      {
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(localBasicHttpParams, localSchemeRegistry), localBasicHttpParams);
        localDefaultHttpClient = client;
        break;
        if (!str.equalsIgnoreCase("https"))
          break label188;
        localSchemeRegistry.register(new Scheme(str, SSLCertificateSocketFactory.getHttpSocketFactory(60000, null), i));
      }
    }
    finally
    {
      monitorexit;
    }
    label188: throw new RuntimeException("HttpClientFactory: Can't create HttpClient with protocol " + str);
  }

  static void releaseInstance()
  {
    client = null;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.HttpClientFactory
 * JD-Core Version:    0.6.0
 */