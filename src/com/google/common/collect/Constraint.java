package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public abstract interface Constraint<E>
{
  public abstract E checkElement(E paramE);

  public abstract String toString();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Constraint
 * JD-Core Version:    0.6.0
 */