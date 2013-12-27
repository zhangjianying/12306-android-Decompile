package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.lang.reflect.Array;
import java.util.logging.Logger;

@GwtCompatible(emulated=true)
class Platform
{
  private static final Logger logger = Logger.getLogger(Platform.class.getCanonicalName());

  static <T> T[] clone(T[] paramArrayOfT)
  {
    return (Object[])paramArrayOfT.clone();
  }

  @GwtIncompatible("Array.newInstance(Class, int)")
  static <T> T[] newArray(Class<T> paramClass, int paramInt)
  {
    return (Object[])(Object[])Array.newInstance(paramClass, paramInt);
  }

  static <T> T[] newArray(T[] paramArrayOfT, int paramInt)
  {
    return (Object[])(Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), paramInt);
  }

  static MapMaker tryWeakKeys(MapMaker paramMapMaker)
  {
    return paramMapMaker.weakKeys();
  }

  static void unsafeArrayCopy(Object[] paramArrayOfObject1, int paramInt1, Object[] paramArrayOfObject2, int paramInt2, int paramInt3)
  {
    System.arraycopy(paramArrayOfObject1, paramInt1, paramArrayOfObject2, paramInt2, paramInt3);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Platform
 * JD-Core Version:    0.6.0
 */