package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;

public class BCodec extends RFC1522Codec
  implements StringEncoder, StringDecoder
{
  private final String charset;

  public BCodec()
  {
    this("UTF-8");
  }

  public BCodec(String paramString)
  {
    this.charset = paramString;
  }

  public Object decode(Object paramObject)
    throws DecoderException
  {
    if (paramObject == null)
      return null;
    if ((paramObject instanceof String))
      return decode((String)paramObject);
    throw new DecoderException("Objects of type " + paramObject.getClass().getName() + " cannot be decoded using BCodec");
  }

  public String decode(String paramString)
    throws DecoderException
  {
    if (paramString == null)
      return null;
    try
    {
      String str = decodeText(paramString);
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new DecoderException(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
  }

  protected byte[] doDecoding(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return null;
    return Base64.decodeBase64(paramArrayOfByte);
  }

  protected byte[] doEncoding(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return null;
    return Base64.encodeBase64(paramArrayOfByte);
  }

  public Object encode(Object paramObject)
    throws EncoderException
  {
    if (paramObject == null)
      return null;
    if ((paramObject instanceof String))
      return encode((String)paramObject);
    throw new EncoderException("Objects of type " + paramObject.getClass().getName() + " cannot be encoded using BCodec");
  }

  public String encode(String paramString)
    throws EncoderException
  {
    if (paramString == null)
      return null;
    return encode(paramString, getDefaultCharset());
  }

  public String encode(String paramString1, String paramString2)
    throws EncoderException
  {
    if (paramString1 == null)
      return null;
    try
    {
      String str = encodeText(paramString1, paramString2);
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new EncoderException(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
  }

  public String getDefaultCharset()
  {
    return this.charset;
  }

  protected String getEncoding()
  {
    return "B";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.net.BCodec
 * JD-Core Version:    0.6.0
 */