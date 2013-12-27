package com.worklight.common;

import android.util.Log;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class WLX509TrustManager
  implements X509TrustManager
{
  private X509TrustManager standardTrustManager = null;

  public WLX509TrustManager(KeyStore paramKeyStore)
  {
    try
    {
      Log.d("SSL", "initial keystore");
      TrustManagerFactory localTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      localTrustManagerFactory.init(paramKeyStore);
      arrayOfTrustManager = localTrustManagerFactory.getTrustManagers();
      if (arrayOfTrustManager.length == 0)
        throw new NoSuchAlgorithmException("no trust manager found");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      TrustManager[] arrayOfTrustManager;
      Log.e("SSL", "init keystore error. NoSuchAlgorithmException\n" + localNoSuchAlgorithmException.getMessage());
      return;
      this.standardTrustManager = ((X509TrustManager)arrayOfTrustManager[0]);
      return;
    }
    catch (KeyStoreException localKeyStoreException)
    {
      Log.e("SSL", "init keystore error. KeyStoreException\n" + localKeyStoreException.getMessage());
    }
  }

  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
  {
    Log.d("SSL", "checkClientTrusted");
    try
    {
      this.standardTrustManager.checkClientTrusted(paramArrayOfX509Certificate, paramString);
      return;
    }
    catch (CertificateException localCertificateException)
    {
      Log.e("SSL", "checkClientTrusted error. \n" + localCertificateException.getMessage());
    }
  }

  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
  {
    Log.d("SSL", "checkServerTrusted");
    if (paramArrayOfX509Certificate != null);
    try
    {
      if (paramArrayOfX509Certificate.length == 1)
      {
        paramArrayOfX509Certificate[0].checkValidity();
        Log.d("SSL", "Certificate is valid. SerialNumber = " + paramArrayOfX509Certificate[0].getSerialNumber());
        return;
      }
      this.standardTrustManager.checkServerTrusted(paramArrayOfX509Certificate, paramString);
      return;
    }
    catch (CertificateException localCertificateException)
    {
      Log.e("SSL", "Invalid certificate. \n" + localCertificateException.getMessage());
    }
  }

  public X509Certificate[] getAcceptedIssuers()
  {
    Log.d("SSL", "getAcceptedIssuers");
    return this.standardTrustManager.getAcceptedIssuers();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.common.WLX509TrustManager
 * JD-Core Version:    0.6.0
 */