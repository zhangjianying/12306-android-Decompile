package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public abstract interface Equivalence<T>
{
  public abstract boolean equivalent(@Nullable T paramT1, @Nullable T paramT2);

  public abstract int hash(@Nullable T paramT);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Equivalence
 * JD-Core Version:    0.6.0
 */