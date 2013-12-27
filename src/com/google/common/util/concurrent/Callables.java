package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import javax.annotation.Nullable;

public final class Callables
{
  public static <T> Callable<T> returning(@Nullable T paramT)
  {
    return new Callable(paramT)
    {
      public T call()
      {
        return this.val$value;
      }
    };
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.Callables
 * JD-Core Version:    0.6.0
 */