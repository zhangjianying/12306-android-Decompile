package com.worklight.androidgap.jsonstore.security;

import com.worklight.common.WLUtils;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

public class FipsWrapper
{
  static
  {
    System.loadLibrary("openssl_fips");
  }

  private static native byte[] _decryptAES(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, byte[] paramArrayOfByte3, int paramInt3);

  private static native int _disableFips();

  private static native int _enableFips();

  private static native byte[] _encryptAES(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, String paramString, int paramInt3);

  private static native int _getFipsMode();

  private static native int _setFips(int paramInt);

  public static String decryptAES(String paramString1, String paramString2, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte1 = WLUtils.hexStringToByteArray(paramString1);
    byte[] arrayOfByte2 = WLUtils.hexStringToByteArray(paramString2);
    byte[] arrayOfByte3 = _decryptAES(arrayOfByte1, arrayOfByte1.length, arrayOfByte2, arrayOfByte2.length, paramArrayOfByte, paramArrayOfByte.length);
    try
    {
      Charset.forName("UTF-8").newDecoder().decode(ByteBuffer.wrap(arrayOfByte3));
      str = new String(arrayOfByte3, "UTF-8");
      Arrays.fill(arrayOfByte3, 0);
      return str;
    }
    catch (Throwable localThrowable)
    {
      while (true)
        String str = null;
    }
  }

  public static int disableFips()
  {
    return _disableFips();
  }

  public static int enableFips()
  {
    return _enableFips();
  }

  public static byte[] encryptAES(String paramString1, String paramString2, String paramString3)
  {
    byte[] arrayOfByte1 = WLUtils.hexStringToByteArray(paramString1);
    byte[] arrayOfByte2 = WLUtils.hexStringToByteArray(paramString2);
    return _encryptAES(arrayOfByte1, arrayOfByte1.length, arrayOfByte2, arrayOfByte2.length, paramString3, paramString3.length());
  }

  public static int getFipsMode()
  {
    return _getFipsMode();
  }

  public static int setFips(int paramInt)
  {
    return _setFips(paramInt);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.security.FipsWrapper
 * JD-Core Version:    0.6.0
 */