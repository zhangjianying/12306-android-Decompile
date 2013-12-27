package com.worklight.common;

import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class WLSSLSocketFactory
  implements LayeredSocketFactory
{
  private static final String CLIENT_AGREEMENT = "TLS";
  private SSLContext sslContext;

  public WLSSLSocketFactory(SSLConfig paramSSLConfig)
  {
    try
    {
      this.sslContext = SSLContext.getInstance("TLS");
      SSLContext localSSLContext = this.sslContext;
      TrustManager[] arrayOfTrustManager = new TrustManager[1];
      arrayOfTrustManager[0] = new WLX509TrustManager(null);
      localSSLContext.init(null, arrayOfTrustManager, null);
      return;
    }
    catch (Exception localException)
    {
      Log.e("SSL", localException.getMessage());
    }
  }

  public Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
    throws IOException, UnknownHostException, ConnectTimeoutException
  {
    Log.d("SSL", "connectSocket");
    int i = HttpConnectionParams.getConnectionTimeout(paramHttpParams);
    int j = HttpConnectionParams.getSoTimeout(paramHttpParams);
    InetSocketAddress localInetSocketAddress = new InetSocketAddress(paramString, paramInt1);
    if (paramSocket != null);
    for (Socket localSocket = paramSocket; ; localSocket = createSocket())
    {
      SSLSocket localSSLSocket = (SSLSocket)localSocket;
      localSSLSocket.connect(localInetSocketAddress, i);
      localSSLSocket.setSoTimeout(j);
      return localSSLSocket;
    }
  }

  public Socket createSocket()
    throws IOException
  {
    Log.d("SSL", "createSocket");
    return this.sslContext.getSocketFactory().createSocket();
  }

  public Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
    throws IOException, UnknownHostException
  {
    Log.d("SSL", "createSocket with params");
    return this.sslContext.getSocketFactory().createSocket(paramSocket, paramString, paramInt, paramBoolean);
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject != null) && (paramObject.getClass().equals(WLSSLSocketFactory.class));
  }

  public int hashCode()
  {
    return WLSSLSocketFactory.class.hashCode();
  }

  public boolean isSecure(Socket paramSocket)
    throws IllegalArgumentException
  {
    Log.d("SSL", "isSecure, return true");
    return true;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.common.WLSSLSocketFactory
 * JD-Core Version:    0.6.0
 */