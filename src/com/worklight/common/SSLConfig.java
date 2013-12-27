package com.worklight.common;

import android.content.Context;

public class SSLConfig
{
  public static final String WL_CLIENT_PROPS_NAME = "wlclient.properties";
  public static final String WL_HTTPS_PORT = "wlServerHttpsPort";
  public static final String WL_SSL_IGNORE_CERTIFICATE = "ignoreSSLCertificate";
  private Context context;
  private int httpsPort = 443;
  private boolean isIgnoreSSLCertificate = true;
  private boolean isPrivateCA = true;
  private String keystore = "server_trust2";
  private String keystorePassword = "passw0rd";

  public SSLConfig(Context paramContext)
  {
    this.context = paramContext;
  }

  public Context getContext()
  {
    return this.context;
  }

  public int getHttpsPort()
  {
    return this.httpsPort;
  }

  public String getKeystore()
  {
    return this.keystore;
  }

  public String getKeystorePassword()
  {
    return this.keystorePassword;
  }

  public boolean isIgnoreSSLCertificate()
  {
    return this.isIgnoreSSLCertificate;
  }

  public boolean isPrivateCA()
  {
    return this.isPrivateCA;
  }

  public void setContext(Context paramContext)
  {
    this.context = paramContext;
  }

  public void setHttpsPort(int paramInt)
  {
    this.httpsPort = paramInt;
  }

  public void setIgnoreSSLCertificate(boolean paramBoolean)
  {
    this.isIgnoreSSLCertificate = paramBoolean;
  }

  public void setKeystore(String paramString)
  {
    this.keystore = paramString;
  }

  public void setKeystorePassword(String paramString)
  {
    this.keystorePassword = paramString;
  }

  public void setPrivateCA(boolean paramBoolean)
  {
    this.isPrivateCA = paramBoolean;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.common.SSLConfig
 * JD-Core Version:    0.6.0
 */