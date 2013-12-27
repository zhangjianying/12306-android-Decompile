package cn.domob.wall.core.b;

import android.content.Context;
import android.provider.Settings.System;
import java.security.MessageDigest;

public class a
{
  private static final String a = "ODIN";
  private static final String b = "SHA-1";
  private static final String c = "iso-8859-1";

  public static String a(Context paramContext)
  {
    try
    {
      String str2 = Settings.System.getString(paramContext.getContentResolver(), "android_id");
      localObject = str2;
      return a((String)localObject);
    }
    catch (Exception localException1)
    {
      try
      {
        String str1 = Settings.System.getString(paramContext.getContentResolver(), "android_id");
        Object localObject = str1;
      }
      catch (Exception localException2)
      {
      }
    }
    return (String)null;
  }

  private static String a(String paramString)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-1");
      new byte[40];
      localMessageDigest.update(paramString.getBytes("iso-8859-1"), 0, paramString.length());
      String str = a(localMessageDigest.digest());
      return str;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  private static String a(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    int j;
    int k;
    if (i < paramArrayOfByte.length)
    {
      j = 0xF & paramArrayOfByte[i] >>> 4;
      k = 0;
    }
    while (true)
    {
      if ((j >= 0) && (j <= 9))
        localStringBuffer.append((char)(j + 48));
      int m;
      int n;
      while (true)
      {
        m = 0xF & paramArrayOfByte[i];
        n = k + 1;
        if (k < 1)
          break label95;
        i++;
        break;
        localStringBuffer.append((char)(97 + (j - 10)));
      }
      return localStringBuffer.toString();
      label95: k = n;
      j = m;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.b.a
 * JD-Core Version:    0.6.0
 */