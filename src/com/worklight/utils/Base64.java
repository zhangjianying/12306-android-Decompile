package com.worklight.utils;

import java.io.UnsupportedEncodingException;

public class Base64
{
  private static final byte[] map = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };

  public static byte[] decode(byte[] paramArrayOfByte)
  {
    return decode(paramArrayOfByte, paramArrayOfByte.length);
  }

  public static byte[] decode(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 3 * (paramInt / 4);
    if (i == 0)
      return new byte[0];
    byte[] arrayOfByte1 = new byte[i];
    int j = 0;
    int k = paramArrayOfByte[(paramInt - 1)];
    if ((k == 10) || (k == 13) || (k == 32) || (k == 9));
    while (true)
    {
      paramInt--;
      break;
      if (k != 61)
        break label75;
      j++;
    }
    label75: int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i5;
    int i6;
    if (i1 < paramInt)
    {
      i5 = paramArrayOfByte[i1];
      if ((i5 == 10) || (i5 == 13) || (i5 == 32))
        break label408;
      if (i5 == 9)
        i6 = i2;
    }
    while (true)
    {
      i1++;
      i2 = i6;
      break;
      int i7;
      if ((i5 >= 65) && (i5 <= 90))
      {
        i7 = i5 - 65;
        label162: n = n << 6 | (byte)i7;
        if (m % 4 != 3)
          break label401;
        int i8 = i2 + 1;
        arrayOfByte1[i2] = (byte)((0xFF0000 & n) >> 16);
        int i9 = i8 + 1;
        arrayOfByte1[i8] = (byte)((0xFF00 & n) >> 8);
        i6 = i9 + 1;
        arrayOfByte1[i9] = (byte)(n & 0xFF);
      }
      while (true)
      {
        m++;
        break;
        if ((i5 >= 97) && (i5 <= 122))
        {
          i7 = i5 - 71;
          break label162;
        }
        if ((i5 >= 48) && (i5 <= 57))
        {
          i7 = i5 + 4;
          break label162;
        }
        if (i5 == 43)
        {
          i7 = 62;
          break label162;
        }
        if (i5 == 47)
        {
          i7 = 63;
          break label162;
        }
        return null;
        int i3;
        if (j > 0)
        {
          int i4 = n << j * 6;
          i3 = i2 + 1;
          arrayOfByte1[i2] = (byte)((0xFF0000 & i4) >> 16);
          if (j == 1)
          {
            i2 = i3 + 1;
            arrayOfByte1[i3] = (byte)((0xFF00 & i4) >> 8);
          }
        }
        else
        {
          i3 = i2;
        }
        byte[] arrayOfByte2 = new byte[i3];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i3);
        return arrayOfByte2;
        label401: i6 = i2;
      }
      label408: i6 = i2;
    }
  }

  public static String encode(byte[] paramArrayOfByte, String paramString)
    throws UnsupportedEncodingException
  {
    int i = 4 * paramArrayOfByte.length / 3;
    byte[] arrayOfByte = new byte[i + (3 + i / 76)];
    int j = paramArrayOfByte.length - paramArrayOfByte.length % 3;
    int k = 0;
    int m = 0;
    while (k < j)
    {
      int i8 = m + 1;
      arrayOfByte[m] = map[((0xFF & paramArrayOfByte[k]) >> 2)];
      int i9 = i8 + 1;
      arrayOfByte[i8] = map[((0x3 & paramArrayOfByte[k]) << 4 | (0xFF & paramArrayOfByte[(k + 1)]) >> 4)];
      int i10 = i9 + 1;
      arrayOfByte[i9] = map[((0xF & paramArrayOfByte[(k + 1)]) << 2 | (0xFF & paramArrayOfByte[(k + 2)]) >> 6)];
      m = i10 + 1;
      arrayOfByte[i10] = map[(0x3F & paramArrayOfByte[(k + 2)])];
      if (((m - 0) % 76 == 0) && (m != 0));
      k += 3;
    }
    switch (paramArrayOfByte.length % 3)
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      int i7;
      for (int i3 = m; ; i3 = i7)
      {
        return new String(arrayOfByte, 0, i3, paramString);
        int i4 = m + 1;
        arrayOfByte[m] = map[((0xFF & paramArrayOfByte[j]) >> 2)];
        int i5 = i4 + 1;
        arrayOfByte[i4] = map[((0x3 & paramArrayOfByte[j]) << 4)];
        int i6 = i5 + 1;
        arrayOfByte[i5] = 61;
        i7 = i6 + 1;
        arrayOfByte[i6] = 61;
      }
      int n = m + 1;
      arrayOfByte[m] = map[((0xFF & paramArrayOfByte[j]) >> 2)];
      int i1 = n + 1;
      arrayOfByte[n] = map[((0x3 & paramArrayOfByte[j]) << 4 | (0xFF & paramArrayOfByte[(j + 1)]) >> 4)];
      int i2 = i1 + 1;
      arrayOfByte[i1] = map[((0xF & paramArrayOfByte[(j + 1)]) << 2)];
      m = i2 + 1;
      arrayOfByte[i2] = 61;
    }
  }

  public static String encodeUrlSafe(byte[] paramArrayOfByte, String paramString)
    throws UnsupportedEncodingException
  {
    return encode(paramArrayOfByte, paramString).replace('+', '-').replace('/', '_');
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.utils.Base64
 * JD-Core Version:    0.6.0
 */