package com.worklight.utils;

import B;
import android.content.Context;
import com.worklight.common.WLUtils;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONException;

public class SecurityUtils
{
  public static final String HASH_ALGORITH_MD5 = "MD5";
  public static final String HASH_ALGORITH_SHA1 = "SHA-1";

  public static InputStream decryptData(InputStream paramInputStream, byte[] paramArrayOfByte)
    throws Exception
  {
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte, "AES");
    Cipher localCipher = Cipher.getInstance("AES");
    localCipher.init(2, localSecretKeySpec);
    return new CipherInputStream(paramInputStream, localCipher);
  }

  public static String hashData(String paramString1, String paramString2)
  {
    byte[] arrayOfByte = hashData(paramString1.getBytes(), paramString2);
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < arrayOfByte.length; i++)
      localStringBuilder.append(Integer.toString(256 + (0xFF & arrayOfByte[i]), 16).substring(1));
    return localStringBuilder.toString();
  }

  public static byte[] hashData(byte[] paramArrayOfByte, String paramString)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance(paramString);
      localMessageDigest.reset();
      localMessageDigest.update(paramArrayOfByte);
      byte[] arrayOfByte = localMessageDigest.digest();
      return arrayOfByte;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      WLUtils.error(paramString + " is not supported on this device", localNoSuchAlgorithmException);
    }
    return null;
  }

  public static String hashDataFromJSON(Context paramContext, JSONArray paramJSONArray)
    throws JSONException, UnsupportedEncodingException
  {
    String str1 = ((String)paramJSONArray.get(0)).split(",")[0];
    JSONArray localJSONArray = (JSONArray)paramJSONArray.get(1);
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < localJSONArray.length(); i++)
      localArrayList.add(localJSONArray.getString(i));
    String str2 = Base64.encode(kpg(paramContext, (String[])localArrayList.toArray(new String[0])), "UTF-8").replaceAll("\n", "");
    return hashData(str1.trim() + str2, "SHA-1");
  }

  public static byte[] kpg(Context paramContext, String[] paramArrayOfString)
  {
    try
    {
      byte[] arrayOfByte = PaidSecurityUtils.kpg(paramContext, paramArrayOfString);
      return arrayOfByte;
    }
    catch (Throwable localThrowable)
    {
    }
    return new byte[0];
  }

  public static class PBKDF2
  {
    private static int ceil(int paramInt1, int paramInt2)
    {
      int i = paramInt1 / paramInt2;
      if (paramInt1 % paramInt2 != 0)
        i++;
      return i;
    }

    private static byte[] concat(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      byte[] arrayOfByte = new byte[paramArrayOfByte1.length + paramArrayOfByte2.length];
      System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
      System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, paramArrayOfByte2.length);
      return arrayOfByte;
    }

    private static byte[] encodedInt(int paramInt)
    {
      byte[] arrayOfByte = new byte[4];
      arrayOfByte[0] = (byte)((0xFF000000 & paramInt) >>> 24);
      arrayOfByte[1] = (byte)((0xFF0000 & paramInt) >>> 16);
      arrayOfByte[2] = (byte)((0xFF00 & paramInt) >>> 8);
      arrayOfByte[3] = (byte)(paramInt & 0xFF);
      return arrayOfByte;
    }

    private static byte[] f(Mac paramMac, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException
    {
      byte[] arrayOfByte1 = new byte[paramMac.getMacLength()];
      Object localObject = concat(paramArrayOfByte, encodedInt(paramInt2));
      for (int i = 1; i <= paramInt1; i++)
      {
        byte[] arrayOfByte2 = paramMac.doFinal(localObject);
        arrayOfByte1 = xor(arrayOfByte1, arrayOfByte2);
        localObject = arrayOfByte2;
      }
      return (B)arrayOfByte1;
    }

    public static Key genKey(char[] paramArrayOfChar, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException
    {
      Mac localMac = Mac.getInstance("HMACSHA1");
      int i = localMac.getMacLength();
      int j = ceil(paramInt2, i);
      int k = paramInt2 - i * (j - 1);
      byte[] arrayOfByte1 = new byte[paramInt2];
      localMac.init(new SecretKeySpec(new String(paramArrayOfChar).getBytes(), "HmacSHA1"));
      for (int m = 1; m <= j; m++)
      {
        byte[] arrayOfByte2 = f(localMac, paramArrayOfByte, paramInt1, m);
        int n = i;
        if (m == j)
          n = k;
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i * (m - 1), n);
      }
      return new SecretKeySpec(arrayOfByte1, "AES");
    }

    private static byte[] xor(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      for (int i = 0; i < paramArrayOfByte1.length; i++)
        paramArrayOfByte1[i] = (byte)(0xFF & (paramArrayOfByte1[i] ^ paramArrayOfByte2[i]));
      return paramArrayOfByte1;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.utils.SecurityUtils
 * JD-Core Version:    0.6.0
 */