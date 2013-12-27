package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
public abstract interface PeekingIterator<E> extends Iterator<E>
{
  public abstract E next();

  public abstract E peek();

  public abstract void remove();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.PeekingIterator
 * JD-Core Version:    0.6.0
 */