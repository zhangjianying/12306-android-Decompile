package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface Predicate<T>
{
  public abstract boolean apply(@Nullable T paramT);

  public abstract boolean equals(@Nullable Object paramObject);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Predicate
 * JD-Core Version:    0.6.0
 */