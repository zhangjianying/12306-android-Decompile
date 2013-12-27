package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@Beta
@GwtCompatible
public abstract class UnmodifiableListIterator<E> extends UnmodifiableIterator<E>
  implements ListIterator<E>
{
  public final void add(E paramE)
  {
    throw new UnsupportedOperationException();
  }

  public final void set(E paramE)
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.UnmodifiableListIterator
 * JD-Core Version:    0.6.0
 */