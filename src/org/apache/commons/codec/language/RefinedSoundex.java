package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class RefinedSoundex
  implements StringEncoder
{
  public static final RefinedSoundex US_ENGLISH;
  private static final char[] US_ENGLISH_MAPPING = "01360240043788015936020505".toCharArray();
  public static final String US_ENGLISH_MAPPING_STRING = "01360240043788015936020505";
  private final char[] soundexMapping;

  static
  {
    US_ENGLISH = new RefinedSoundex();
  }

  public RefinedSoundex()
  {
    this.soundexMapping = US_ENGLISH_MAPPING;
  }

  public RefinedSoundex(String paramString)
  {
    this.soundexMapping = paramString.toCharArray();
  }

  public RefinedSoundex(char[] paramArrayOfChar)
  {
    this.soundexMapping = new char[paramArrayOfChar.length];
    System.arraycopy(paramArrayOfChar, 0, this.soundexMapping, 0, paramArrayOfChar.length);
  }

  public int difference(String paramString1, String paramString2)
    throws EncoderException
  {
    return SoundexUtils.difference(this, paramString1, paramString2);
  }

  public Object encode(Object paramObject)
    throws EncoderException
  {
    if (!(paramObject instanceof String))
      throw new EncoderException("Parameter supplied to RefinedSoundex encode is not of type java.lang.String");
    return soundex((String)paramObject);
  }

  public String encode(String paramString)
  {
    return soundex(paramString);
  }

  char getMappingCode(char paramChar)
  {
    if (!Character.isLetter(paramChar))
      return '\000';
    return this.soundexMapping[('﾿' + Character.toUpperCase(paramChar))];
  }

  public String soundex(String paramString)
  {
    if (paramString == null)
      return null;
    String str = SoundexUtils.clean(paramString);
    if (str.length() == 0)
      return str;
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(str.charAt(0));
    int i = 42;
    int j = 0;
    if (j < str.length())
    {
      char c = getMappingCode(str.charAt(j));
      if (c == i);
      while (true)
      {
        j++;
        break;
        if (c != 0)
          localStringBuffer.append(c);
        i = c;
      }
    }
    return localStringBuffer.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.language.RefinedSoundex
 * JD-Core Version:    0.6.0
 */