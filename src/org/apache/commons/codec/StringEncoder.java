package org.apache.commons.codec;

public abstract interface StringEncoder extends Encoder
{
  public abstract String encode(String paramString)
    throws EncoderException;
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.StringEncoder
 * JD-Core Version:    0.6.0
 */