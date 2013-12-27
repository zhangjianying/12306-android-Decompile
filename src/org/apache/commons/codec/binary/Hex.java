package org.apache.commons.codec.binary;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class Hex
  implements BinaryEncoder, BinaryDecoder
{
  public static final String DEFAULT_CHARSET_NAME = "UTF-8";
  private static final char[] DIGITS_LOWER = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  private static final char[] DIGITS_UPPER = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  private final String charsetName;

  public Hex()
  {
    this.charsetName = "UTF-8";
  }

  public Hex(String paramString)
  {
    this.charsetName = paramString;
  }

  public static byte[] decodeHex(char[] paramArrayOfChar)
    throws DecoderException
  {
    int i = paramArrayOfChar.length;
    if ((i & 0x1) != 0)
      throw new DecoderException("Odd number of characters.");
    byte[] arrayOfByte = new byte[i >> 1];
    int j = 0;
    int k = 0;
    while (k < i)
    {
      int m = toDigit(paramArrayOfChar[k], k) << 4;
      int n = k + 1;
      int i1 = m | toDigit(paramArrayOfChar[n], n);
      k = n + 1;
      arrayOfByte[j] = (byte)(i1 & 0xFF);
      j++;
    }
    return arrayOfByte;
  }

  public static char[] encodeHex(byte[] paramArrayOfByte)
  {
    return encodeHex(paramArrayOfByte, true);
  }

  public static char[] encodeHex(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    if (paramBoolean);
    for (char[] arrayOfChar = DIGITS_LOWER; ; arrayOfChar = DIGITS_UPPER)
      return encodeHex(paramArrayOfByte, arrayOfChar);
  }

  protected static char[] encodeHex(byte[] paramArrayOfByte, char[] paramArrayOfChar)
  {
    int i = paramArrayOfByte.length;
    char[] arrayOfChar = new char[i << 1];
    int j = 0;
    int k = 0;
    while (j < i)
    {
      int m = k + 1;
      arrayOfChar[k] = paramArrayOfChar[((0xF0 & paramArrayOfByte[j]) >>> 4)];
      k = m + 1;
      arrayOfChar[m] = paramArrayOfChar[(0xF & paramArrayOfByte[j])];
      j++;
    }
    return arrayOfChar;
  }

  public static String encodeHexString(byte[] paramArrayOfByte)
  {
    return new String(encodeHex(paramArrayOfByte));
  }

  protected static int toDigit(char paramChar, int paramInt)
    throws DecoderException
  {
    int i = Character.digit(paramChar, 16);
    if (i == -1)
      throw new DecoderException("Illegal hexadecimal character " + paramChar + " at index " + paramInt);
    return i;
  }

  public Object decode(Object paramObject)
    throws DecoderException
  {
    try
    {
      if ((paramObject instanceof String));
      char[] arrayOfChar;
      for (Object localObject = ((String)paramObject).toCharArray(); ; localObject = arrayOfChar)
      {
        return decodeHex(localObject);
        arrayOfChar = (char[])(char[])paramObject;
      }
    }
    catch (ClassCastException localClassCastException)
    {
    }
    throw new DecoderException(localClassCastException.getMessage(), localClassCastException);
  }

  public byte[] decode(byte[] paramArrayOfByte)
    throws DecoderException
  {
    try
    {
      byte[] arrayOfByte = decodeHex(new String(paramArrayOfByte, getCharsetName()).toCharArray());
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new DecoderException(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
  }

  public Object encode(Object paramObject)
    throws EncoderException
  {
    try
    {
      if ((paramObject instanceof String));
      byte[] arrayOfByte;
      for (Object localObject = ((String)paramObject).getBytes(getCharsetName()); ; localObject = arrayOfByte)
      {
        return encodeHex(localObject);
        arrayOfByte = (byte[])(byte[])paramObject;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      throw new EncoderException(localClassCastException.getMessage(), localClassCastException);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new EncoderException(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
  }

  public byte[] encode(byte[] paramArrayOfByte)
  {
    return StringUtils.getBytesUnchecked(encodeHexString(paramArrayOfByte), getCharsetName());
  }

  public String getCharsetName()
  {
    return this.charsetName;
  }

  public String toString()
  {
    return super.toString() + "[charsetName=" + this.charsetName + "]";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.binary.Hex
 * JD-Core Version:    0.6.0
 */