package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Metaphone
  implements StringEncoder
{
  private static final String FRONTV = "EIY";
  private static final String VARSON = "CSPTG";
  private static final String VOWELS = "AEIOU";
  private int maxCodeLen = 4;

  private boolean isLastChar(int paramInt1, int paramInt2)
  {
    return paramInt2 + 1 == paramInt1;
  }

  private boolean isNextChar(StringBuffer paramStringBuffer, int paramInt, char paramChar)
  {
    int i = 0;
    if (paramInt >= 0)
    {
      int j = -1 + paramStringBuffer.length();
      i = 0;
      if (paramInt < j)
      {
        if (paramStringBuffer.charAt(paramInt + 1) != paramChar)
          break label41;
        i = 1;
      }
    }
    return i;
    label41: return false;
  }

  private boolean isPreviousChar(StringBuffer paramStringBuffer, int paramInt, char paramChar)
  {
    int i = 0;
    if (paramInt > 0)
    {
      int j = paramStringBuffer.length();
      i = 0;
      if (paramInt < j)
      {
        if (paramStringBuffer.charAt(paramInt - 1) != paramChar)
          break label39;
        i = 1;
      }
    }
    return i;
    label39: return false;
  }

  private boolean isVowel(StringBuffer paramStringBuffer, int paramInt)
  {
    return "AEIOU".indexOf(paramStringBuffer.charAt(paramInt)) >= 0;
  }

  private boolean regionMatch(StringBuffer paramStringBuffer, int paramInt, String paramString)
  {
    boolean bool = false;
    if (paramInt >= 0)
    {
      int i = -1 + (paramInt + paramString.length());
      int j = paramStringBuffer.length();
      bool = false;
      if (i < j)
        bool = paramStringBuffer.substring(paramInt, paramInt + paramString.length()).equals(paramString);
    }
    return bool;
  }

  public Object encode(Object paramObject)
    throws EncoderException
  {
    if (!(paramObject instanceof String))
      throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
    return metaphone((String)paramObject);
  }

  public String encode(String paramString)
  {
    return metaphone(paramString);
  }

  public int getMaxCodeLen()
  {
    return this.maxCodeLen;
  }

  public boolean isMetaphoneEqual(String paramString1, String paramString2)
  {
    return metaphone(paramString1).equals(metaphone(paramString2));
  }

  public String metaphone(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
      return "";
    if (paramString.length() == 1)
      return paramString.toUpperCase(Locale.ENGLISH);
    char[] arrayOfChar = paramString.toUpperCase(Locale.ENGLISH).toCharArray();
    StringBuffer localStringBuffer1 = new StringBuffer(40);
    StringBuffer localStringBuffer2 = new StringBuffer(10);
    switch (arrayOfChar[0])
    {
    default:
      localStringBuffer1.append(arrayOfChar);
    case 'G':
    case 'K':
    case 'P':
    case 'A':
    case 'W':
    case 'X':
    }
    while (true)
    {
      int i = localStringBuffer1.length();
      int j = 0;
      char c;
      while (true)
      {
        if ((localStringBuffer2.length() >= getMaxCodeLen()) || (j >= i))
          break label1320;
        c = localStringBuffer1.charAt(j);
        if ((c != 'C') && (isPreviousChar(localStringBuffer1, j, c)))
        {
          j++;
          if (localStringBuffer2.length() <= getMaxCodeLen())
            continue;
          localStringBuffer2.setLength(getMaxCodeLen());
          continue;
          if (arrayOfChar[1] == 'N')
          {
            localStringBuffer1.append(arrayOfChar, 1, -1 + arrayOfChar.length);
            break;
          }
          localStringBuffer1.append(arrayOfChar);
          break;
          if (arrayOfChar[1] == 'E')
          {
            localStringBuffer1.append(arrayOfChar, 1, -1 + arrayOfChar.length);
            break;
          }
          localStringBuffer1.append(arrayOfChar);
          break;
          if (arrayOfChar[1] == 'R')
          {
            localStringBuffer1.append(arrayOfChar, 1, -1 + arrayOfChar.length);
            break;
          }
          if (arrayOfChar[1] == 'H')
          {
            localStringBuffer1.append(arrayOfChar, 1, -1 + arrayOfChar.length);
            localStringBuffer1.setCharAt(0, 'W');
            break;
          }
          localStringBuffer1.append(arrayOfChar);
          break;
          arrayOfChar[0] = 'S';
          localStringBuffer1.append(arrayOfChar);
          break;
        }
      }
      switch (c)
      {
      default:
      case 'A':
      case 'E':
      case 'I':
      case 'O':
      case 'U':
      case 'B':
      case 'C':
      case 'D':
      case 'G':
      case 'H':
      case 'F':
      case 'J':
      case 'L':
      case 'M':
      case 'N':
      case 'R':
      case 'K':
      case 'P':
      case 'Q':
      case 'S':
      case 'T':
      case 'V':
      case 'W':
      case 'Y':
      case 'X':
      case 'Z':
      }
      while (true)
      {
        j++;
        break;
        if (j != 0)
          continue;
        localStringBuffer2.append(c);
        continue;
        if ((isPreviousChar(localStringBuffer1, j, 'M')) && (isLastChar(i, j)))
          continue;
        localStringBuffer2.append(c);
        continue;
        if ((isPreviousChar(localStringBuffer1, j, 'S')) && (!isLastChar(i, j)) && ("EIY".indexOf(localStringBuffer1.charAt(j + 1)) >= 0))
          continue;
        if (regionMatch(localStringBuffer1, j, "CIA"))
        {
          localStringBuffer2.append('X');
          continue;
        }
        if ((!isLastChar(i, j)) && ("EIY".indexOf(localStringBuffer1.charAt(j + 1)) >= 0))
        {
          localStringBuffer2.append('S');
          continue;
        }
        if ((isPreviousChar(localStringBuffer1, j, 'S')) && (isNextChar(localStringBuffer1, j, 'H')))
        {
          localStringBuffer2.append('K');
          continue;
        }
        if (isNextChar(localStringBuffer1, j, 'H'))
        {
          if ((j == 0) && (i >= 3) && (isVowel(localStringBuffer1, 2)))
          {
            localStringBuffer2.append('K');
            continue;
          }
          localStringBuffer2.append('X');
          continue;
        }
        localStringBuffer2.append('K');
        continue;
        if ((!isLastChar(i, j + 1)) && (isNextChar(localStringBuffer1, j, 'G')) && ("EIY".indexOf(localStringBuffer1.charAt(j + 2)) >= 0))
        {
          localStringBuffer2.append('J');
          j += 2;
          continue;
        }
        localStringBuffer2.append('T');
        continue;
        if (((isLastChar(i, j + 1)) && (isNextChar(localStringBuffer1, j, 'H'))) || ((!isLastChar(i, j + 1)) && (isNextChar(localStringBuffer1, j, 'H')) && (!isVowel(localStringBuffer1, j + 2))) || ((j > 0) && ((regionMatch(localStringBuffer1, j, "GN")) || (regionMatch(localStringBuffer1, j, "GNED")))))
          continue;
        if (isPreviousChar(localStringBuffer1, j, 'G'));
        for (int k = 1; ; k = 0)
        {
          if ((isLastChar(i, j)) || ("EIY".indexOf(localStringBuffer1.charAt(j + 1)) < 0) || (k != 0))
            break label945;
          localStringBuffer2.append('J');
          break;
        }
        label945: localStringBuffer2.append('K');
        continue;
        if ((isLastChar(i, j)) || ((j > 0) && ("CSPTG".indexOf(localStringBuffer1.charAt(j - 1)) >= 0)) || (!isVowel(localStringBuffer1, j + 1)))
          continue;
        localStringBuffer2.append('H');
        continue;
        localStringBuffer2.append(c);
        continue;
        if (j > 0)
        {
          if (isPreviousChar(localStringBuffer1, j, 'C'))
            continue;
          localStringBuffer2.append(c);
          continue;
        }
        localStringBuffer2.append(c);
        continue;
        if (isNextChar(localStringBuffer1, j, 'H'))
        {
          localStringBuffer2.append('F');
          continue;
        }
        localStringBuffer2.append(c);
        continue;
        localStringBuffer2.append('K');
        continue;
        if ((regionMatch(localStringBuffer1, j, "SH")) || (regionMatch(localStringBuffer1, j, "SIO")) || (regionMatch(localStringBuffer1, j, "SIA")))
        {
          localStringBuffer2.append('X');
          continue;
        }
        localStringBuffer2.append('S');
        continue;
        if ((regionMatch(localStringBuffer1, j, "TIA")) || (regionMatch(localStringBuffer1, j, "TIO")))
        {
          localStringBuffer2.append('X');
          continue;
        }
        if (regionMatch(localStringBuffer1, j, "TCH"))
          continue;
        if (regionMatch(localStringBuffer1, j, "TH"))
        {
          localStringBuffer2.append('0');
          continue;
        }
        localStringBuffer2.append('T');
        continue;
        localStringBuffer2.append('F');
        continue;
        if ((isLastChar(i, j)) || (!isVowel(localStringBuffer1, j + 1)))
          continue;
        localStringBuffer2.append(c);
        continue;
        localStringBuffer2.append('K');
        localStringBuffer2.append('S');
        continue;
        localStringBuffer2.append('S');
      }
    }
    label1320: return localStringBuffer2.toString();
  }

  public void setMaxCodeLen(int paramInt)
  {
    this.maxCodeLen = paramInt;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.language.Metaphone
 * JD-Core Version:    0.6.0
 */