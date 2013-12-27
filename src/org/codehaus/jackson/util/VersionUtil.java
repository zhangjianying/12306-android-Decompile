package org.codehaus.jackson.util;

import java.util.regex.Pattern;
import org.codehaus.jackson.Version;

public class VersionUtil
{
  public static final String VERSION_FILE = "VERSION.txt";
  private static final Pattern VERSION_SEPARATOR = Pattern.compile("[-_./;:]");

  public static Version parseVersion(String paramString)
  {
    if (paramString == null);
    String[] arrayOfString;
    do
    {
      String str1;
      do
      {
        return null;
        str1 = paramString.trim();
      }
      while (str1.length() == 0);
      arrayOfString = VERSION_SEPARATOR.split(str1);
    }
    while (arrayOfString.length < 2);
    int i = parseVersionPart(arrayOfString[0]);
    int j = parseVersionPart(arrayOfString[1]);
    int k = arrayOfString.length;
    int m = 0;
    if (k > 2)
      m = parseVersionPart(arrayOfString[2]);
    int n = arrayOfString.length;
    String str2 = null;
    if (n > 3)
      str2 = arrayOfString[3];
    return new Version(i, j, m, str2);
  }

  protected static int parseVersionPart(String paramString)
  {
    String str = paramString.toString();
    int i = str.length();
    int j = 0;
    for (int k = 0; ; k++)
    {
      int m;
      if (k < i)
      {
        m = str.charAt(k);
        if ((m <= 57) && (m >= 48));
      }
      else
      {
        return j;
      }
      j = j * 10 + (m - 48);
    }
  }

  // ERROR //
  public static Version versionFor(java.lang.Class<?> paramClass)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aload_0
    //   3: ldc 8
    //   5: invokevirtual 67	java/lang/Class:getResourceAsStream	(Ljava/lang/String;)Ljava/io/InputStream;
    //   8: astore_3
    //   9: aconst_null
    //   10: astore_1
    //   11: aload_3
    //   12: ifnull +35 -> 47
    //   15: new 69	java/io/BufferedReader
    //   18: dup
    //   19: new 71	java/io/InputStreamReader
    //   22: dup
    //   23: aload_3
    //   24: ldc 73
    //   26: invokespecial 76	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   29: invokespecial 79	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   32: invokevirtual 82	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   35: invokestatic 84	org/codehaus/jackson/util/VersionUtil:parseVersion	(Ljava/lang/String;)Lorg/codehaus/jackson/Version;
    //   38: astore 6
    //   40: aload 6
    //   42: astore_1
    //   43: aload_3
    //   44: invokevirtual 89	java/io/InputStream:close	()V
    //   47: aload_1
    //   48: ifnonnull +7 -> 55
    //   51: invokestatic 93	org/codehaus/jackson/Version:unknownVersion	()Lorg/codehaus/jackson/Version;
    //   54: astore_1
    //   55: aload_1
    //   56: areturn
    //   57: astore 7
    //   59: new 95	java/lang/RuntimeException
    //   62: dup
    //   63: aload 7
    //   65: invokespecial 98	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   68: athrow
    //   69: astore_2
    //   70: goto -23 -> 47
    //   73: astore 4
    //   75: aload_3
    //   76: invokevirtual 89	java/io/InputStream:close	()V
    //   79: aload 4
    //   81: athrow
    //   82: astore 5
    //   84: new 95	java/lang/RuntimeException
    //   87: dup
    //   88: aload 5
    //   90: invokespecial 98	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   93: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   43	47	57	java/io/IOException
    //   2	9	69	java/io/IOException
    //   59	69	69	java/io/IOException
    //   79	82	69	java/io/IOException
    //   84	94	69	java/io/IOException
    //   15	40	73	finally
    //   75	79	82	java/io/IOException
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.VersionUtil
 * JD-Core Version:    0.6.0
 */