package org.codehaus.jackson.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class InternCache extends LinkedHashMap<String, String>
{
  private static final int MAX_ENTRIES = 192;
  public static final InternCache instance = new InternCache();

  private InternCache()
  {
    super(192, 0.8F, true);
  }

  public String intern(String paramString)
  {
    monitorenter;
    try
    {
      String str = (String)get(paramString);
      if (str == null)
      {
        str = paramString.intern();
        put(str, str);
      }
      return str;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  protected boolean removeEldestEntry(Map.Entry<String, String> paramEntry)
  {
    return size() > 192;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.InternCache
 * JD-Core Version:    0.6.0
 */