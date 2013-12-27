package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class URLCodec
  implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
{
  protected static final byte ESCAPE_CHAR = 37;
  static final int RADIX = 16;
  protected static final BitSet WWW_FORM_URL = new BitSet(256);
  protected String charset;

  static
  {
    for (int i = 97; i <= 122; i++)
      WWW_FORM_URL.set(i);
    for (int j = 65; j <= 90; j++)
      WWW_FORM_URL.set(j);
    for (int k = 48; k <= 57; k++)
      WWW_FORM_URL.set(k);
    WWW_FORM_URL.set(45);
    WWW_FORM_URL.set(95);
    WWW_FORM_URL.set(46);
    WWW_FORM_URL.set(42);
    WWW_FORM_URL.set(32);
  }

  public URLCodec()
  {
    this("UTF-8");
  }

  public URLCodec(String paramString)
  {
    this.charset = paramString;
  }

  public static final byte[] decodeUrl(byte[] paramArrayOfByte)
    throws DecoderException
  {
    if (paramArrayOfByte == null)
      return null;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 0;
    if (i < paramArrayOfByte.length)
    {
      int j = paramArrayOfByte[i];
      if (j == 43)
        localByteArrayOutputStream.write(32);
      while (true)
      {
        i++;
        break;
        if (j == 37)
        {
          int k = i + 1;
          try
          {
            int m = Utils.digit16(paramArrayOfByte[k]);
            i = k + 1;
            localByteArrayOutputStream.write((char)(Utils.digit16(paramArrayOfByte[i]) + (m << 4)));
          }
          catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
          {
            throw new DecoderException("Invalid URL encoding: ", localArrayIndexOutOfBoundsException);
          }
        }
        localByteArrayOutputStream.write(j);
      }
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public static final byte[] encodeUrl(BitSet paramBitSet, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return null;
    if (paramBitSet == null)
      paramBitSet = WWW_FORM_URL;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 0;
    if (i < paramArrayOfByte.length)
    {
      int j = paramArrayOfByte[i];
      if (j < 0)
        j += 256;
      if (paramBitSet.get(j))
      {
        if (j == 32)
          j = 43;
        localByteArrayOutputStream.write(j);
      }
      while (true)
      {
        i++;
        break;
        localByteArrayOutputStream.write(37);
        int k = Character.toUpperCase(Character.forDigit(0xF & j >> 4, 16));
        int m = Character.toUpperCase(Character.forDigit(j & 0xF, 16));
        localByteArrayOutputStream.write(k);
        localByteArrayOutputStream.write(m);
      }
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public Object decode(Object paramObject)
    throws DecoderException
  {
    if (paramObject == null)
      return null;
    if ((paramObject instanceof byte[]))
      return decode((byte[])(byte[])paramObject);
    if ((paramObject instanceof String))
      return decode((String)paramObject);
    throw new DecoderException("Objects of type " + paramObject.getClass().getName() + " cannot be URL decoded");
  }

  public String decode(String paramString)
    throws DecoderException
  {
    if (paramString == null)
      return null;
    try
    {
      String str = decode(paramString, getDefaultCharset());
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new DecoderException(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
  }

  public String decode(String paramString1, String paramString2)
    throws DecoderException, UnsupportedEncodingException
  {
    if (paramString1 == null)
      return null;
    return new String(decode(StringUtils.getBytesUsAscii(paramString1)), paramString2);
  }

  public byte[] decode(byte[] paramArrayOfByte)
    throws DecoderException
  {
    return decodeUrl(paramArrayOfByte);
  }

  public Object encode(Object paramObject)
    throws EncoderException
  {
    if (paramObject == null)
      return null;
    if ((paramObject instanceof byte[]))
      return encode((byte[])(byte[])paramObject);
    if ((paramObject instanceof String))
      return encode((String)paramObject);
    throw new EncoderException("Objects of type " + paramObject.getClass().getName() + " cannot be URL encoded");
  }

  public String encode(String paramString)
    throws EncoderException
  {
    if (paramString == null)
      return null;
    try
    {
      String str = encode(paramString, getDefaultCharset());
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new EncoderException(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
  }

  public String encode(String paramString1, String paramString2)
    throws UnsupportedEncodingException
  {
    if (paramString1 == null)
      return null;
    return StringUtils.newStringUsAscii(encode(paramString1.getBytes(paramString2)));
  }

  public byte[] encode(byte[] paramArrayOfByte)
  {
    return encodeUrl(WWW_FORM_URL, paramArrayOfByte);
  }

  public String getDefaultCharset()
  {
    return this.charset;
  }

  public String getEncoding()
  {
    return this.charset;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.net.URLCodec
 * JD-Core Version:    0.6.0
 */