package org.codehaus.jackson;

public abstract interface SerializableString
{
  public abstract char[] asQuotedChars();

  public abstract byte[] asQuotedUTF8();

  public abstract byte[] asUnquotedUTF8();

  public abstract int charLength();

  public abstract String getValue();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.SerializableString
 * JD-Core Version:    0.6.0
 */