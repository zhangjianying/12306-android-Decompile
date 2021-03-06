package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;

@GwtCompatible
public final class SignedBytes
{
  public static byte checkedCast(long paramLong)
  {
    int i = (byte)(int)paramLong;
    if (i == paramLong);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Long.valueOf(paramLong);
      Preconditions.checkArgument(bool, "Out of range: %s", arrayOfObject);
      return i;
    }
  }

  public static int compare(byte paramByte1, byte paramByte2)
  {
    return paramByte1 - paramByte2;
  }

  public static String join(String paramString, byte[] paramArrayOfByte)
  {
    Preconditions.checkNotNull(paramString);
    if (paramArrayOfByte.length == 0)
      return "";
    StringBuilder localStringBuilder = new StringBuilder(5 * paramArrayOfByte.length);
    localStringBuilder.append(paramArrayOfByte[0]);
    for (int i = 1; i < paramArrayOfByte.length; i++)
      localStringBuilder.append(paramString).append(paramArrayOfByte[i]);
    return localStringBuilder.toString();
  }

  public static Comparator<byte[]> lexicographicalComparator()
  {
    return LexicographicalComparator.INSTANCE;
  }

  public static byte max(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length > 0);
    int i;
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      i = paramArrayOfByte[0];
      for (int j = 1; j < paramArrayOfByte.length; j++)
      {
        if (paramArrayOfByte[j] <= i)
          continue;
        i = paramArrayOfByte[j];
      }
    }
    return i;
  }

  public static byte min(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length > 0);
    int i;
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      i = paramArrayOfByte[0];
      for (int j = 1; j < paramArrayOfByte.length; j++)
      {
        if (paramArrayOfByte[j] >= i)
          continue;
        i = paramArrayOfByte[j];
      }
    }
    return i;
  }

  public static byte saturatedCast(long paramLong)
  {
    if (paramLong > 127L)
      return 127;
    if (paramLong < -128L)
      return -128;
    return (byte)(int)paramLong;
  }

  private static enum LexicographicalComparator
    implements Comparator<byte[]>
  {
    static
    {
      LexicographicalComparator[] arrayOfLexicographicalComparator = new LexicographicalComparator[1];
      arrayOfLexicographicalComparator[0] = INSTANCE;
      $VALUES = arrayOfLexicographicalComparator;
    }

    public int compare(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      int i = Math.min(paramArrayOfByte1.length, paramArrayOfByte2.length);
      for (int j = 0; j < i; j++)
      {
        int k = SignedBytes.compare(paramArrayOfByte1[j], paramArrayOfByte2[j]);
        if (k != 0)
          return k;
      }
      return paramArrayOfByte1.length - paramArrayOfByte2.length;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.primitives.SignedBytes
 * JD-Core Version:    0.6.0
 */