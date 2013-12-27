package org.codehaus.jackson.io;

public final class NumberInput
{
  static final long L_BILLION = 1000000000L;
  static final String MAX_LONG_STR;
  static final String MIN_LONG_STR_NO_SIGN = String.valueOf(-9223372036854775808L).substring(1);
  public static final String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";

  static
  {
    MAX_LONG_STR = String.valueOf(9223372036854775807L);
  }

  public static final boolean inLongRange(String paramString, boolean paramBoolean)
  {
    String str;
    int i;
    int j;
    if (paramBoolean)
    {
      str = MIN_LONG_STR_NO_SIGN;
      i = str.length();
      j = paramString.length();
      if (j >= i)
        break label34;
    }
    label34: label82: 
    while (true)
    {
      return true;
      str = MAX_LONG_STR;
      break;
      if (j > i)
        return false;
      for (int k = 0; ; k++)
      {
        if (k >= i)
          break label82;
        int m = paramString.charAt(k) - str.charAt(k);
        if (m == 0)
          continue;
        if (m < 0)
          break;
        return false;
      }
    }
  }

  public static final boolean inLongRange(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    String str;
    int i;
    if (paramBoolean)
    {
      str = MIN_LONG_STR_NO_SIGN;
      i = str.length();
      if (paramInt2 >= i)
        break label32;
    }
    label32: label82: 
    while (true)
    {
      return true;
      str = MAX_LONG_STR;
      break;
      if (paramInt2 > i)
        return false;
      for (int j = 0; ; j++)
      {
        if (j >= i)
          break label82;
        int k = paramArrayOfChar[(paramInt1 + j)] - str.charAt(j);
        if (k == 0)
          continue;
        if (k < 0)
          break;
        return false;
      }
    }
  }

  public static double parseAsDouble(String paramString, double paramDouble)
  {
    if (paramString == null);
    String str;
    do
    {
      return paramDouble;
      str = paramString.trim();
    }
    while (str.length() == 0);
    try
    {
      double d = parseDouble(str);
      return d;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return paramDouble;
  }

  public static int parseAsInt(String paramString, int paramInt)
  {
    if (paramString == null);
    String str;
    int i;
    do
    {
      return paramInt;
      str = paramString.trim();
      i = str.length();
    }
    while (i == 0);
    int j = 0;
    int n;
    if (i < 0)
    {
      n = str.charAt(0);
      if (n != 43)
        break label90;
      str = str.substring(1);
      i = str.length();
    }
    while (j < i)
    {
      int m = str.charAt(j);
      if ((m > 57) || (m < 48))
        try
        {
          double d = parseDouble(str);
          return (int)d;
          label90: j = 0;
          if (n != 45)
            continue;
          j = 0 + 1;
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          return paramInt;
        }
      j++;
    }
    try
    {
      int k = Integer.parseInt(str);
      return k;
    }
    catch (NumberFormatException localNumberFormatException1)
    {
    }
    return paramInt;
  }

  public static long parseAsLong(String paramString, long paramLong)
  {
    if (paramString == null);
    String str;
    int i;
    do
    {
      return paramLong;
      str = paramString.trim();
      i = str.length();
    }
    while (i == 0);
    int j = 0;
    int m;
    if (i < 0)
    {
      m = str.charAt(0);
      if (m != 43)
        break label95;
      str = str.substring(1);
      i = str.length();
    }
    while (j < i)
    {
      int k = str.charAt(j);
      if ((k > 57) || (k < 48))
        try
        {
          double d = parseDouble(str);
          return ()d;
          label95: j = 0;
          if (m != 45)
            continue;
          j = 0 + 1;
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          return paramLong;
        }
      j++;
    }
    try
    {
      long l = Long.parseLong(str);
      return l;
    }
    catch (NumberFormatException localNumberFormatException1)
    {
    }
    return paramLong;
  }

  public static final double parseDouble(String paramString)
    throws NumberFormatException
  {
    if ("2.2250738585072012e-308".equals(paramString))
      return 2.225073858507201E-308D;
    return Double.parseDouble(paramString);
  }

  public static final int parseInt(String paramString)
  {
    int i = paramString.charAt(0);
    int j = paramString.length();
    int k = 0;
    if (i == 45)
      k = 1;
    int i1;
    int m;
    if (k != 0)
    {
      if ((j == 1) || (j > 10))
      {
        i1 = Integer.parseInt(paramString);
        return i1;
      }
      m = 1 + 1;
      i = paramString.charAt(1);
    }
    while (true)
    {
      if ((i > 57) || (i < 48))
      {
        int n = Integer.parseInt(paramString);
        return n;
        if (j > 9)
          return Integer.parseInt(paramString);
      }
      else
      {
        i1 = i - 48;
        if (m < j)
        {
          int i2 = m + 1;
          int i3 = paramString.charAt(m);
          if ((i3 > 57) || (i3 < 48))
            return Integer.parseInt(paramString);
          i1 = i1 * 10 + (i3 - 48);
          if (i2 >= j)
            break label280;
          m = i2 + 1;
          int i4 = paramString.charAt(i2);
          if ((i4 > 57) || (i4 < 48))
          {
            int i5 = Integer.parseInt(paramString);
            return i5;
          }
          i1 = i1 * 10 + (i4 - 48);
          if (m < j)
            do
            {
              int i6 = m;
              m = i6 + 1;
              int i7 = paramString.charAt(i6);
              if ((i7 > 57) || (i7 < 48))
              {
                int i8 = Integer.parseInt(paramString);
                return i8;
              }
              i1 = i1 * 10 + (i7 - 48);
            }
            while (m < j);
        }
        label280: if (k == 0)
          break;
        return -i1;
      }
      m = 1;
    }
  }

  public static final int parseInt(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = '￐' + paramArrayOfChar[paramInt1];
    int j = paramInt2 + paramInt1;
    int k = paramInt1 + 1;
    if (k < j)
    {
      i = i * 10 + ('￐' + paramArrayOfChar[k]);
      int m = k + 1;
      if (m < j)
      {
        i = i * 10 + ('￐' + paramArrayOfChar[m]);
        int n = m + 1;
        if (n < j)
        {
          i = i * 10 + ('￐' + paramArrayOfChar[n]);
          int i1 = n + 1;
          if (i1 < j)
          {
            i = i * 10 + ('￐' + paramArrayOfChar[i1]);
            int i2 = i1 + 1;
            if (i2 < j)
            {
              i = i * 10 + ('￐' + paramArrayOfChar[i2]);
              int i3 = i2 + 1;
              if (i3 < j)
              {
                i = i * 10 + ('￐' + paramArrayOfChar[i3]);
                int i4 = i3 + 1;
                if (i4 < j)
                {
                  i = i * 10 + ('￐' + paramArrayOfChar[i4]);
                  int i5 = i4 + 1;
                  if (i5 < j)
                    i = i * 10 + ('￐' + paramArrayOfChar[i5]);
                }
              }
            }
          }
        }
      }
    }
    return i;
  }

  public static final long parseLong(String paramString)
  {
    if (paramString.length() <= 9)
      return parseInt(paramString);
    return Long.parseLong(paramString);
  }

  public static final long parseLong(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = paramInt2 - 9;
    return 1000000000L * parseInt(paramArrayOfChar, paramInt1, i) + parseInt(paramArrayOfChar, paramInt1 + i, 9);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.NumberInput
 * JD-Core Version:    0.6.0
 */