package com.tl.uic.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectionUtil
{
  private static volatile ReflectionUtil _myInstance;

  public static Object getField(Object paramObject, String paramString)
  {
    try
    {
      Field localField = paramObject.getClass().getDeclaredField(paramString);
      localField.setAccessible(true);
      Object localObject = localField.get(paramString);
      return localObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return null;
  }

  public static ReflectionUtil getInstance()
  {
    if (_myInstance == null)
      monitorenter;
    try
    {
      _myInstance = new ReflectionUtil();
      return _myInstance;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public static Method getMethod(Object paramObject, String paramString, Class[] paramArrayOfClass)
  {
    Method localMethod = null;
    try
    {
      localMethod = paramObject.getClass().getDeclaredMethod(paramString, paramArrayOfClass);
      localMethod.setAccessible(true);
      return localMethod;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localMethod;
  }

  public static Method getStaticMethod(Class paramClass, String paramString, Class[] paramArrayOfClass)
  {
    Method localMethod = null;
    if ((paramClass == null) || (paramString == null) || (paramArrayOfClass == null))
      return null;
    try
    {
      localMethod = paramClass.getClass().getDeclaredMethod(paramString, paramArrayOfClass);
      localMethod.setAccessible(true);
      return localMethod;
    }
    catch (Exception localException)
    {
      while (true)
        LogInternal.logException(localException);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.util.ReflectionUtil
 * JD-Core Version:    0.6.0
 */