package com.worklight.androidgap.jsonstore.security;

import android.content.Context;
import android.util.Base64;

public class SecurityManager
{
  private static final int IV_NUM_BYTES = 16;
  private static final int LOCAL_KEY_NUM_BYTES = 32;
  private static SecurityManager instance;
  private Keychain keychain;

  private SecurityManager(Context paramContext)
  {
    this.keychain = new Keychain(paramContext);
  }

  public static SecurityManager getInstance(Context paramContext)
  {
    monitorenter;
    try
    {
      if (instance == null)
        instance = new SecurityManager(paramContext);
      SecurityManager localSecurityManager = instance;
      return localSecurityManager;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public void destroyKeychain()
  {
    this.keychain.destroy();
  }

  public String getDPK(String paramString1, String paramString2)
    throws Exception
  {
    DPKBean localDPKBean = this.keychain.getDPKBean(paramString2);
    return new String(SecurityUtils.decode(SecurityUtils.encodeKeyAsHexString(SecurityUtils.generateKey(paramString1, localDPKBean.getSalt())), new String(Base64.decode(localDPKBean.getEncryptedDPK(), 0)), localDPKBean.getIV()));
  }

  public String getSalt(String paramString)
    throws Exception
  {
    DPKBean localDPKBean = this.keychain.getDPKBean(paramString);
    if (localDPKBean == null)
      return null;
    return localDPKBean.getSalt();
  }

  public boolean isDPKAvailable(String paramString)
  {
    return this.keychain.isDPKAvailable(paramString);
  }

  public boolean storeDPK(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2)
    throws Exception
  {
    String str1 = paramString3;
    if (paramBoolean2)
      str1 = SecurityUtils.encodeBytesAsHexString(SecurityUtils.generateLocalKey(32));
    while (true)
    {
      String str2 = SecurityUtils.encodeBytesAsHexString(SecurityUtils.generateIV(16));
      DPKBean localDPKBean = new DPKBean(Base64.encodeToString(SecurityUtils.encodeBytesAsHexString(SecurityUtils.encrypt(SecurityUtils.encodeKeyAsHexString(SecurityUtils.generateKey(paramString1, paramString4)), str1, str2)).getBytes(), 0), str2, paramString4, 10000);
      this.keychain.setDPKBean(paramString2, localDPKBean);
      return false;
      if (paramBoolean1)
        continue;
      str1 = SecurityUtils.encodeKeyAsHexString(SecurityUtils.generateKey(paramString3, paramString4));
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.security.SecurityManager
 * JD-Core Version:    0.6.0
 */