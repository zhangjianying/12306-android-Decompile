package com.worklight.androidgap.jsonstore.security;

import com.worklight.common.WLUtils;
import com.worklight.utils.SecurityUtils.PBKDF2;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;

public class SecurityUtils
{
  private static final int KEY_SIZE_AES256 = 32;
  protected static final int PBKDF2_ITERATIONS = 10000;

  public static byte[] decode(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    String str = FipsWrapper.decryptAES(paramString1, paramString3, WLUtils.hexStringToByteArray(paramString2));
    if ((str == null) || (str.length() == 0))
      throw new BadPaddingException("Decryption failed");
    return str.getBytes();
  }

  public static String encodeBytesAsHexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramArrayOfByte != null)
    {
      int i = paramArrayOfByte.length;
      for (int j = 0; j < i; j++)
      {
        byte b = paramArrayOfByte[j];
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Byte.valueOf(b);
        localStringBuilder.append(String.format("%02X", arrayOfObject));
      }
    }
    return localStringBuilder.toString();
  }

  public static String encodeKeyAsHexString(SecretKey paramSecretKey)
  {
    return encodeBytesAsHexString(paramSecretKey.getEncoded());
  }

  public static byte[] encrypt(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    return FipsWrapper.encryptAES(paramString1, paramString3, paramString2);
  }

  public static byte[] generateIV(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    new SecureRandom().nextBytes(arrayOfByte);
    return arrayOfByte;
  }

  public static SecretKey generateKey(String paramString1, String paramString2)
    throws Exception
  {
    return (SecretKey)SecurityUtils.PBKDF2.genKey(paramString1.toCharArray(), paramString2.getBytes("UTF-8"), 10000, 32);
  }

  public static byte[] generateLocalKey(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    new SecureRandom().nextBytes(arrayOfByte);
    return arrayOfByte;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.security.SecurityUtils
 * JD-Core Version:    0.6.0
 */