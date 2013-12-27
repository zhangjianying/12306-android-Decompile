package com.google.common.base;

import java.nio.charset.Charset;

public final class Charsets
{
  public static final Charset ISO_8859_1;
  public static final Charset US_ASCII = Charset.forName("US-ASCII");
  public static final Charset UTF_16;
  public static final Charset UTF_16BE;
  public static final Charset UTF_16LE;
  public static final Charset UTF_8;

  static
  {
    ISO_8859_1 = Charset.forName("ISO-8859-1");
    UTF_8 = Charset.forName("UTF-8");
    UTF_16BE = Charset.forName("UTF-16BE");
    UTF_16LE = Charset.forName("UTF-16LE");
    UTF_16 = Charset.forName("UTF-16");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Charsets
 * JD-Core Version:    0.6.0
 */