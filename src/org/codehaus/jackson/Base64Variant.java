package org.codehaus.jackson;

import java.util.Arrays;

public final class Base64Variant
{
  public static final int BASE64_VALUE_INVALID = -1;
  public static final int BASE64_VALUE_PADDING = -2;
  static final char PADDING_CHAR_NONE;
  private final int[] _asciiToBase64 = new int[''];
  private final byte[] _base64ToAsciiB = new byte[64];
  private final char[] _base64ToAsciiC = new char[64];
  final int _maxLineLength;
  final String _name;
  final char _paddingChar;
  final boolean _usesPadding;

  public Base64Variant(String paramString1, String paramString2, boolean paramBoolean, char paramChar, int paramInt)
  {
    this._name = paramString1;
    this._usesPadding = paramBoolean;
    this._paddingChar = paramChar;
    this._maxLineLength = paramInt;
    int i = paramString2.length();
    if (i != 64)
      throw new IllegalArgumentException("Base64Alphabet length must be exactly 64 (was " + i + ")");
    paramString2.getChars(0, i, this._base64ToAsciiC, 0);
    Arrays.fill(this._asciiToBase64, -1);
    for (int j = 0; j < i; j++)
    {
      int k = this._base64ToAsciiC[j];
      this._base64ToAsciiB[j] = (byte)k;
      this._asciiToBase64[k] = j;
    }
    if (paramBoolean)
      this._asciiToBase64[paramChar] = -2;
  }

  public Base64Variant(Base64Variant paramBase64Variant, String paramString, int paramInt)
  {
    this(paramBase64Variant, paramString, paramBase64Variant._usesPadding, paramBase64Variant._paddingChar, paramInt);
  }

  public Base64Variant(Base64Variant paramBase64Variant, String paramString, boolean paramBoolean, char paramChar, int paramInt)
  {
    this._name = paramString;
    byte[] arrayOfByte = paramBase64Variant._base64ToAsciiB;
    System.arraycopy(arrayOfByte, 0, this._base64ToAsciiB, 0, arrayOfByte.length);
    char[] arrayOfChar = paramBase64Variant._base64ToAsciiC;
    System.arraycopy(arrayOfChar, 0, this._base64ToAsciiC, 0, arrayOfChar.length);
    int[] arrayOfInt = paramBase64Variant._asciiToBase64;
    System.arraycopy(arrayOfInt, 0, this._asciiToBase64, 0, arrayOfInt.length);
    this._usesPadding = paramBoolean;
    this._paddingChar = paramChar;
    this._maxLineLength = paramInt;
  }

  public int decodeBase64Byte(byte paramByte)
  {
    if (paramByte <= 127)
      return this._asciiToBase64[paramByte];
    return -1;
  }

  public int decodeBase64Char(char paramChar)
  {
    if (paramChar <= '')
      return this._asciiToBase64[paramChar];
    return -1;
  }

  public int decodeBase64Char(int paramInt)
  {
    if (paramInt <= 127)
      return this._asciiToBase64[paramInt];
    return -1;
  }

  public String encode(byte[] paramArrayOfByte)
  {
    return encode(paramArrayOfByte, false);
  }

  public String encode(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    int i = paramArrayOfByte.length;
    StringBuilder localStringBuilder = new StringBuilder(i + (i >> 2) + (i >> 3));
    if (paramBoolean)
      localStringBuilder.append('"');
    int j = getMaxLineLength() >> 2;
    int k = i - 3;
    int i7;
    for (int m = 0; m <= k; m = i7)
    {
      int i3 = m + 1;
      int i4 = paramArrayOfByte[m] << 8;
      int i5 = i3 + 1;
      int i6 = (i4 | 0xFF & paramArrayOfByte[i3]) << 8;
      i7 = i5 + 1;
      encodeBase64Chunk(localStringBuilder, i6 | 0xFF & paramArrayOfByte[i5]);
      j--;
      if (j > 0)
        continue;
      localStringBuilder.append('\\');
      localStringBuilder.append('n');
      j = getMaxLineLength() >> 2;
    }
    int n = i - m;
    if (n > 0)
    {
      int i1 = m + 1;
      int i2 = paramArrayOfByte[m] << 16;
      if (n == 2)
      {
        (i1 + 1);
        i2 |= (0xFF & paramArrayOfByte[i1]) << 8;
      }
      encodeBase64Partial(localStringBuilder, i2, n);
    }
    while (true)
    {
      if (paramBoolean)
        localStringBuilder.append('"');
      return localStringBuilder.toString();
    }
  }

  public byte encodeBase64BitsAsByte(int paramInt)
  {
    return this._base64ToAsciiB[paramInt];
  }

  public char encodeBase64BitsAsChar(int paramInt)
  {
    return this._base64ToAsciiC[paramInt];
  }

  public int encodeBase64Chunk(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = this._base64ToAsciiB[(0x3F & paramInt1 >> 18)];
    int j = i + 1;
    paramArrayOfByte[i] = this._base64ToAsciiB[(0x3F & paramInt1 >> 12)];
    int k = j + 1;
    paramArrayOfByte[j] = this._base64ToAsciiB[(0x3F & paramInt1 >> 6)];
    int m = k + 1;
    paramArrayOfByte[k] = this._base64ToAsciiB[(paramInt1 & 0x3F)];
    return m;
  }

  public int encodeBase64Chunk(int paramInt1, char[] paramArrayOfChar, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfChar[paramInt2] = this._base64ToAsciiC[(0x3F & paramInt1 >> 18)];
    int j = i + 1;
    paramArrayOfChar[i] = this._base64ToAsciiC[(0x3F & paramInt1 >> 12)];
    int k = j + 1;
    paramArrayOfChar[j] = this._base64ToAsciiC[(0x3F & paramInt1 >> 6)];
    int m = k + 1;
    paramArrayOfChar[k] = this._base64ToAsciiC[(paramInt1 & 0x3F)];
    return m;
  }

  public void encodeBase64Chunk(StringBuilder paramStringBuilder, int paramInt)
  {
    paramStringBuilder.append(this._base64ToAsciiC[(0x3F & paramInt >> 18)]);
    paramStringBuilder.append(this._base64ToAsciiC[(0x3F & paramInt >> 12)]);
    paramStringBuilder.append(this._base64ToAsciiC[(0x3F & paramInt >> 6)]);
    paramStringBuilder.append(this._base64ToAsciiC[(paramInt & 0x3F)]);
  }

  public int encodeBase64Partial(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
  {
    int i = paramInt3 + 1;
    paramArrayOfByte[paramInt3] = this._base64ToAsciiB[(0x3F & paramInt1 >> 18)];
    int j = i + 1;
    paramArrayOfByte[i] = this._base64ToAsciiB[(0x3F & paramInt1 >> 12)];
    int m;
    int i1;
    if (this._usesPadding)
    {
      m = (byte)this._paddingChar;
      int n = j + 1;
      if (paramInt2 == 2)
      {
        i1 = this._base64ToAsciiB[(0x3F & paramInt1 >> 6)];
        paramArrayOfByte[j] = i1;
        j = n + 1;
        paramArrayOfByte[n] = m;
      }
    }
    do
    {
      return j;
      i1 = m;
      break;
    }
    while (paramInt2 != 2);
    int k = j + 1;
    paramArrayOfByte[j] = this._base64ToAsciiB[(0x3F & paramInt1 >> 6)];
    return k;
  }

  public int encodeBase64Partial(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
  {
    int i = paramInt3 + 1;
    paramArrayOfChar[paramInt3] = this._base64ToAsciiC[(0x3F & paramInt1 >> 18)];
    int j = i + 1;
    paramArrayOfChar[i] = this._base64ToAsciiC[(0x3F & paramInt1 >> 12)];
    int n;
    if (this._usesPadding)
    {
      int m = j + 1;
      if (paramInt2 == 2)
      {
        n = this._base64ToAsciiC[(0x3F & paramInt1 >> 6)];
        paramArrayOfChar[j] = n;
        j = m + 1;
        paramArrayOfChar[m] = this._paddingChar;
      }
    }
    do
    {
      return j;
      n = this._paddingChar;
      break;
    }
    while (paramInt2 != 2);
    int k = j + 1;
    paramArrayOfChar[j] = this._base64ToAsciiC[(0x3F & paramInt1 >> 6)];
    return k;
  }

  public void encodeBase64Partial(StringBuilder paramStringBuilder, int paramInt1, int paramInt2)
  {
    paramStringBuilder.append(this._base64ToAsciiC[(0x3F & paramInt1 >> 18)]);
    paramStringBuilder.append(this._base64ToAsciiC[(0x3F & paramInt1 >> 12)]);
    char c;
    if (this._usesPadding)
      if (paramInt2 == 2)
      {
        c = this._base64ToAsciiC[(0x3F & paramInt1 >> 6)];
        paramStringBuilder.append(c);
        paramStringBuilder.append(this._paddingChar);
      }
    do
    {
      return;
      c = this._paddingChar;
      break;
    }
    while (paramInt2 != 2);
    paramStringBuilder.append(this._base64ToAsciiC[(0x3F & paramInt1 >> 6)]);
  }

  public int getMaxLineLength()
  {
    return this._maxLineLength;
  }

  public String getName()
  {
    return this._name;
  }

  public byte getPaddingByte()
  {
    return (byte)this._paddingChar;
  }

  public char getPaddingChar()
  {
    return this._paddingChar;
  }

  public String toString()
  {
    return this._name;
  }

  public boolean usesPadding()
  {
    return this._usesPadding;
  }

  public boolean usesPaddingChar(char paramChar)
  {
    return paramChar == this._paddingChar;
  }

  public boolean usesPaddingChar(int paramInt)
  {
    return paramInt == this._paddingChar;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.Base64Variant
 * JD-Core Version:    0.6.0
 */