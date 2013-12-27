package org.codehaus.jackson.map.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.codehaus.jackson.io.NumberInput;

public class StdDateFormat extends DateFormat
{
  static final String[] ALL_FORMATS = { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd" };
  static final SimpleDateFormat DATE_FORMAT_ISO8601;
  static final SimpleDateFormat DATE_FORMAT_ISO8601_Z;
  static final SimpleDateFormat DATE_FORMAT_PLAIN;
  static final SimpleDateFormat DATE_FORMAT_RFC1123;
  static final String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  static final String DATE_FORMAT_STR_ISO8601_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";
  static final String DATE_FORMAT_STR_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
  public static final StdDateFormat instance;
  transient SimpleDateFormat _formatISO8601;
  transient SimpleDateFormat _formatISO8601_z;
  transient SimpleDateFormat _formatPlain;
  transient SimpleDateFormat _formatRFC1123;

  static
  {
    TimeZone localTimeZone = TimeZone.getTimeZone("GMT");
    DATE_FORMAT_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    DATE_FORMAT_RFC1123.setTimeZone(localTimeZone);
    DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    DATE_FORMAT_ISO8601.setTimeZone(localTimeZone);
    DATE_FORMAT_ISO8601_Z = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    DATE_FORMAT_ISO8601_Z.setTimeZone(localTimeZone);
    DATE_FORMAT_PLAIN = new SimpleDateFormat("yyyy-MM-dd");
    DATE_FORMAT_PLAIN.setTimeZone(localTimeZone);
    instance = new StdDateFormat();
  }

  public static DateFormat getBlueprintISO8601Format()
  {
    return DATE_FORMAT_ISO8601;
  }

  public static DateFormat getBlueprintRFC1123Format()
  {
    return DATE_FORMAT_RFC1123;
  }

  public static DateFormat getISO8601Format(TimeZone paramTimeZone)
  {
    SimpleDateFormat localSimpleDateFormat = (SimpleDateFormat)DATE_FORMAT_ISO8601.clone();
    localSimpleDateFormat.setTimeZone(paramTimeZone);
    return localSimpleDateFormat;
  }

  public static DateFormat getRFC1123Format(TimeZone paramTimeZone)
  {
    SimpleDateFormat localSimpleDateFormat = (SimpleDateFormat)DATE_FORMAT_RFC1123.clone();
    localSimpleDateFormat.setTimeZone(paramTimeZone);
    return localSimpleDateFormat;
  }

  private static final boolean hasTimeZone(String paramString)
  {
    int i = paramString.length();
    if (i >= 6)
    {
      int j = paramString.charAt(i - 6);
      if ((j == 43) || (j == 45));
      int m;
      do
      {
        int k;
        do
        {
          return true;
          k = paramString.charAt(i - 5);
        }
        while ((k == 43) || (k == 45));
        m = paramString.charAt(i - 3);
      }
      while ((m == 43) || (m == 45));
    }
    return false;
  }

  public StdDateFormat clone()
  {
    return new StdDateFormat();
  }

  public StringBuffer format(Date paramDate, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition)
  {
    if (this._formatISO8601 == null)
      this._formatISO8601 = ((SimpleDateFormat)DATE_FORMAT_ISO8601.clone());
    return this._formatISO8601.format(paramDate, paramStringBuffer, paramFieldPosition);
  }

  protected boolean looksLikeISO8601(String paramString)
  {
    int i = paramString.length();
    int j = 0;
    if (i >= 5)
    {
      boolean bool1 = Character.isDigit(paramString.charAt(0));
      j = 0;
      if (bool1)
      {
        boolean bool2 = Character.isDigit(paramString.charAt(3));
        j = 0;
        if (bool2)
        {
          int k = paramString.charAt(4);
          j = 0;
          if (k == 45)
            j = 1;
        }
      }
    }
    return j;
  }

  public Date parse(String paramString)
    throws ParseException
  {
    String str1 = paramString.trim();
    ParsePosition localParsePosition = new ParsePosition(0);
    Date localDate = parse(str1, localParsePosition);
    if (localDate != null)
      return localDate;
    StringBuilder localStringBuilder = new StringBuilder();
    String[] arrayOfString = ALL_FORMATS;
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str2 = arrayOfString[j];
      if (localStringBuilder.length() > 0)
        localStringBuilder.append("\", \"");
      while (true)
      {
        localStringBuilder.append(str2);
        j++;
        break;
        localStringBuilder.append('"');
      }
    }
    localStringBuilder.append('"');
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = str1;
    arrayOfObject[1] = localStringBuilder.toString();
    throw new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)", arrayOfObject), localParsePosition.getErrorIndex());
  }

  public Date parse(String paramString, ParsePosition paramParsePosition)
  {
    if (looksLikeISO8601(paramString))
      return parseAsISO8601(paramString, paramParsePosition);
    int i = paramString.length();
    int j;
    do
    {
      i--;
      if (i < 0)
        break;
      j = paramString.charAt(i);
    }
    while ((j >= 48) && (j <= 57));
    if ((i < 0) && (NumberInput.inLongRange(paramString, false)))
      return new Date(Long.parseLong(paramString));
    return parseAsRFC1123(paramString, paramParsePosition);
  }

  protected Date parseAsISO8601(String paramString, ParsePosition paramParsePosition)
  {
    int i = paramString.length();
    char c = paramString.charAt(i - 1);
    SimpleDateFormat localSimpleDateFormat;
    if ((i <= 10) && (Character.isDigit(c)))
    {
      localSimpleDateFormat = this._formatPlain;
      if (localSimpleDateFormat == null)
      {
        localSimpleDateFormat = (SimpleDateFormat)DATE_FORMAT_PLAIN.clone();
        this._formatPlain = localSimpleDateFormat;
      }
    }
    while (true)
    {
      return localSimpleDateFormat.parse(paramString, paramParsePosition);
      if (c == 'Z')
      {
        localSimpleDateFormat = this._formatISO8601_z;
        if (localSimpleDateFormat == null)
        {
          localSimpleDateFormat = (SimpleDateFormat)DATE_FORMAT_ISO8601_Z.clone();
          this._formatISO8601_z = localSimpleDateFormat;
        }
        if (paramString.charAt(i - 4) != ':')
          continue;
        StringBuilder localStringBuilder4 = new StringBuilder(paramString);
        localStringBuilder4.insert(i - 1, ".000");
        paramString = localStringBuilder4.toString();
        continue;
      }
      if (hasTimeZone(paramString))
      {
        int j = paramString.charAt(i - 3);
        StringBuilder localStringBuilder2;
        if (j == 58)
        {
          localStringBuilder2 = new StringBuilder(paramString);
          localStringBuilder2.delete(i - 3, i - 2);
        }
        for (paramString = localStringBuilder2.toString(); ; paramString = paramString + "00")
          do
          {
            int k = paramString.length();
            if (Character.isDigit(paramString.charAt(k - 9)))
            {
              StringBuilder localStringBuilder3 = new StringBuilder(paramString);
              localStringBuilder3.insert(k - 5, ".000");
              paramString = localStringBuilder3.toString();
            }
            localSimpleDateFormat = this._formatISO8601;
            if (this._formatISO8601 != null)
              break;
            localSimpleDateFormat = (SimpleDateFormat)DATE_FORMAT_ISO8601.clone();
            this._formatISO8601 = localSimpleDateFormat;
            break;
          }
          while ((j != 43) && (j != 45));
      }
      StringBuilder localStringBuilder1 = new StringBuilder(paramString);
      if (-1 + (i - paramString.lastIndexOf('T')) <= 8)
        localStringBuilder1.append(".000");
      localStringBuilder1.append('Z');
      paramString = localStringBuilder1.toString();
      localSimpleDateFormat = this._formatISO8601_z;
      if (localSimpleDateFormat != null)
        continue;
      localSimpleDateFormat = (SimpleDateFormat)DATE_FORMAT_ISO8601_Z.clone();
      this._formatISO8601_z = localSimpleDateFormat;
    }
  }

  protected Date parseAsRFC1123(String paramString, ParsePosition paramParsePosition)
  {
    if (this._formatRFC1123 == null)
      this._formatRFC1123 = ((SimpleDateFormat)DATE_FORMAT_RFC1123.clone());
    return this._formatRFC1123.parse(paramString, paramParsePosition);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.util.StdDateFormat
 * JD-Core Version:    0.6.0
 */