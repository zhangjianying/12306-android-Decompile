package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public final class Strings
{
  @Nullable
  public static String emptyToNull(@Nullable String paramString)
  {
    if (isNullOrEmpty(paramString))
      paramString = null;
    return paramString;
  }

  public static boolean isNullOrEmpty(@Nullable String paramString)
  {
    return (paramString == null) || (paramString.length() == 0);
  }

  public static String nullToEmpty(@Nullable String paramString)
  {
    if (paramString == null)
      paramString = "";
    return paramString;
  }

  public static String padEnd(String paramString, int paramInt, char paramChar)
  {
    Preconditions.checkNotNull(paramString);
    if (paramString.length() >= paramInt)
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder(paramInt);
    localStringBuilder.append(paramString);
    for (int i = paramString.length(); i < paramInt; i++)
      localStringBuilder.append(paramChar);
    return localStringBuilder.toString();
  }

  public static String padStart(String paramString, int paramInt, char paramChar)
  {
    Preconditions.checkNotNull(paramString);
    if (paramString.length() >= paramInt)
      return paramString;
    StringBuilder localStringBuilder = new StringBuilder(paramInt);
    for (int i = paramString.length(); i < paramInt; i++)
      localStringBuilder.append(paramChar);
    localStringBuilder.append(paramString);
    return localStringBuilder.toString();
  }

  public static String repeat(String paramString, int paramInt)
  {
    Preconditions.checkNotNull(paramString);
    if (paramInt >= 0);
    StringBuilder localStringBuilder;
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "invalid count: %s", arrayOfObject);
      localStringBuilder = new StringBuilder(paramInt * paramString.length());
      for (int i = 0; i < paramInt; i++)
        localStringBuilder.append(paramString);
    }
    return localStringBuilder.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Strings
 * JD-Core Version:    0.6.0
 */