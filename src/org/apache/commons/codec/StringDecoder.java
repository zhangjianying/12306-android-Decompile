package org.apache.commons.codec;

public abstract interface StringDecoder extends Decoder
{
  public abstract String decode(String paramString)
    throws DecoderException;
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.StringDecoder
 * JD-Core Version:    0.6.0
 */