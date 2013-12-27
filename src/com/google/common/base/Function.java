package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface Function<F, T>
{
  public abstract T apply(@Nullable F paramF);

  public abstract boolean equals(@Nullable Object paramObject);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Function
 * JD-Core Version:    0.6.0
 */