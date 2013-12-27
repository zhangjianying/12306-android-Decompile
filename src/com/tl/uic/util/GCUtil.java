package com.tl.uic.util;

import com.tl.uic.Tealeaf;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class GCUtil
{
  private static volatile GCUtil _myInstance;

  public static <T> Boolean clean(Object paramObject)
  {
    if (paramObject == null)
      return Boolean.valueOf(false);
    invokeCleanMethod(paramObject);
    return Boolean.valueOf(true);
  }

  public static <K, V> Boolean clean(HashMap<K, V> paramHashMap)
  {
    if ((paramHashMap == null) || (paramHashMap.size() <= 0))
      return Boolean.valueOf(false);
    Iterator localIterator = paramHashMap.keySet().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        paramHashMap.clear();
        return Boolean.valueOf(true);
      }
      invokeCleanMethod(localIterator.next());
    }
  }

  public static <T> Boolean clean(List<T> paramList)
  {
    if ((paramList == null) || (paramList.size() <= 0))
      return Boolean.valueOf(false);
    do
      invokeCleanMethod(paramList.remove(0));
    while (!paramList.isEmpty());
    paramList.clear();
    return Boolean.valueOf(true);
  }

  public static <K, V> Boolean clean(ConcurrentHashMap<K, V> paramConcurrentHashMap)
  {
    if ((paramConcurrentHashMap == null) || (paramConcurrentHashMap.size() <= 0))
      return Boolean.valueOf(false);
    Iterator localIterator = paramConcurrentHashMap.keySet().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        paramConcurrentHashMap.clear();
        return Boolean.valueOf(true);
      }
      invokeCleanMethod(localIterator.next());
    }
  }

  public static GCUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new GCUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static Boolean invokeCleanMethod(Object paramObject)
  {
    if (paramObject == null)
      return Boolean.valueOf(false);
    Class localClass = paramObject.getClass();
    try
    {
      localClass.getDeclaredMethod("clean", new Class[0]).invoke(paramObject, new Object[0]);
      return Boolean.valueOf(true);
    }
    catch (Exception localException)
    {
      while (true)
        Tealeaf.logException(localException);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.GCUtil
 * JD-Core Version:    0.6.0
 */