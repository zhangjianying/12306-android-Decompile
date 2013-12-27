package com.tl.uic.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValueUtil
{
  private static volatile ValueUtil _myInstance;

  public static String compareListAndMask(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null));
    while (true)
    {
      return paramString2;
      String[] arrayOfString = ConfigurationUtil.getString("MaskIdList").split(",");
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
        if (Pattern.compile(arrayOfString[j]).matcher(paramString1).find())
          return maskValue(paramString2);
    }
  }

  public static ValueUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new ValueUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static String maskValue(String paramString)
  {
    String str1 = paramString;
    Boolean localBoolean1 = ConfigurationUtil.getBoolean("HasMasking");
    Boolean localBoolean2 = ConfigurationUtil.getBoolean("HasCustomMask");
    String str2 = ConfigurationUtil.getString("SensitiveSmallCaseAlphabet");
    String str3 = ConfigurationUtil.getString("SensitiveCapitalCaseAlphabet");
    String str4 = ConfigurationUtil.getString("SensitiveSymbol");
    String str5 = ConfigurationUtil.getString("SensitiveNumber");
    if (localBoolean1.booleanValue())
    {
      if (localBoolean2.booleanValue())
        str1 = str1.replaceAll("\\p{Ll}", str2).replaceAll("\\p{Lu}", str3).replaceAll("\\p{P}|\\p{S}", str4).replaceAll("\\p{N}", str5);
    }
    else
      return str1;
    return "";
  }

  public static String trimValue(String paramString)
  {
    if (paramString == null)
      return paramString;
    int i = ConfigurationUtil.getInt("MaxStringsLength");
    StringBuffer localStringBuffer = new StringBuffer(i);
    if (paramString.length() > i)
      localStringBuffer.append(paramString, 0, i);
    while (true)
    {
      return localStringBuffer.toString();
      localStringBuffer.append(paramString);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.ValueUtil
 * JD-Core Version:    0.6.0
 */