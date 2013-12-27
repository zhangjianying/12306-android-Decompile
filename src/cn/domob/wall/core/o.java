package cn.domob.wall.core;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

final class o
{
  private static p a = new p(o.class.getSimpleName());
  private static String b = "01";

  private static byte a(int paramInt, byte[] paramArrayOfByte)
  {
    if (paramInt >= paramArrayOfByte.length)
      return 0;
    return paramArrayOfByte[paramInt];
  }

  static a a(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1.length() < 5)
      paramString1 = paramString1 + "aaaaaa";
    a locala = new a();
    try
    {
      byte[] arrayOfByte1 = paramString1.getBytes("UTF-8");
      byte[] arrayOfByte2 = paramString2.getBytes("UTF-8");
      byte[] arrayOfByte3 = paramString3.getBytes("UTF-8");
      byte[] arrayOfByte4 = new byte[10];
      int i = 3;
      for (int j = -1 + arrayOfByte1.length; j >= -4 + arrayOfByte1.length; j--)
      {
        arrayOfByte4[i] = arrayOfByte1[j];
        i--;
      }
      int k = arrayOfByte2.length;
      arrayOfByte4[4] = (byte)((0xFF00 & k) >> 8);
      arrayOfByte4[5] = (byte)(k & 0xFF);
      byte[] arrayOfByte5 = a(new String(a(arrayOfByte3, arrayOfByte4)));
      long l = System.currentTimeMillis();
      locala.a(String.valueOf(l));
      int m = (int)(2147483647.0D * Math.random());
      locala.b(String.valueOf(m));
      byte[] arrayOfByte6 = a(m ^ (int)(l / 1000L));
      for (int n = 0; n < arrayOfByte6.length; n++)
        arrayOfByte5[(3 - n)] = arrayOfByte6[n];
      byte[] arrayOfByte7 = a(arrayOfByte5);
      locala.c(b + b(arrayOfByte7));
      return locala;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      a.a(localUnsupportedEncodingException);
    }
    return locala;
  }

  private static byte[] a(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)(paramInt & 0xFF);
    arrayOfByte[1] = (byte)((0xFF00 & paramInt) >> 8);
    arrayOfByte[2] = (byte)((0xFF0000 & paramInt) >> 16);
    arrayOfByte[3] = (byte)((0xFF000000 & paramInt) >> 24);
    return arrayOfByte;
  }

  private static byte[] a(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return null;
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.reset();
      localMessageDigest.update(paramString.getBytes("UTF-8"));
      byte[] arrayOfByte = localMessageDigest.digest();
      return arrayOfByte;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  private static byte[] a(byte[] paramArrayOfByte)
  {
    for (int i = 4; i <= -4 + paramArrayOfByte.length; i += 4)
      for (int j = 3; j >= 0; j--)
        paramArrayOfByte[(i + 3 - j)] = (byte)(paramArrayOfByte[(3 - j)] ^ a(i + 3 - j, paramArrayOfByte));
    return paramArrayOfByte;
  }

  private static byte[] a(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    for (int i = 3; i < 3 + paramArrayOfByte1.length; i += 4)
    {
      if (i < 4)
        for (int k = 3; k >= 0; k--)
          paramArrayOfByte2[(9 - k)] = a(i - k, paramArrayOfByte1);
      for (int j = 3; j >= 0; j--)
        paramArrayOfByte2[(9 - j)] = (byte)(paramArrayOfByte2[(9 - j)] ^ a(i - j, paramArrayOfByte1));
    }
    return paramArrayOfByte2;
  }

  private static String b(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder("");
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
      if (str.length() == 1)
        str = "0" + str;
      localStringBuilder.append(str);
    }
    return localStringBuilder.toString().toUpperCase().trim();
  }

  static class a
  {
    private String a = "";
    private String b = "";
    private String c = "";

    protected String a()
    {
      return this.a;
    }

    protected void a(String paramString)
    {
      this.a = paramString;
    }

    protected String b()
    {
      return this.b;
    }

    protected void b(String paramString)
    {
      this.b = paramString;
    }

    protected String c()
    {
      return this.c;
    }

    protected void c(String paramString)
    {
      this.c = paramString;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     cn.domob.wall.core.o
 * JD-Core Version:    0.6.0
 */