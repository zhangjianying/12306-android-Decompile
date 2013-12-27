package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true, serializable=true)
final class SingletonImmutableSet<E> extends ImmutableSet<E>
{
  private transient Integer cachedHashCode;
  final transient E element;

  SingletonImmutableSet(E paramE)
  {
    this.element = Preconditions.checkNotNull(paramE);
  }

  SingletonImmutableSet(E paramE, int paramInt)
  {
    this.element = paramE;
    this.cachedHashCode = Integer.valueOf(paramInt);
  }

  public boolean contains(Object paramObject)
  {
    return this.element.equals(paramObject);
  }

  public boolean equals(@Nullable Object paramObject)
  {
    if (paramObject == this);
    while (true)
    {
      return true;
      if (!(paramObject instanceof Set))
        break;
      Set localSet = (Set)paramObject;
      if ((localSet.size() != 1) || (!this.element.equals(localSet.iterator().next())))
        return false;
    }
    return false;
  }

  public final int hashCode()
  {
    Integer localInteger1 = this.cachedHashCode;
    if (localInteger1 == null)
    {
      Integer localInteger2 = Integer.valueOf(this.element.hashCode());
      this.cachedHashCode = localInteger2;
      return localInteger2.intValue();
    }
    return localInteger1.intValue();
  }

  public boolean isEmpty()
  {
    return false;
  }

  boolean isHashCodeFast()
  {
    return false;
  }

  boolean isPartialView()
  {
    return false;
  }

  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.singletonIterator(this.element);
  }

  public int size()
  {
    return 1;
  }

  public Object[] toArray()
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.element;
    return arrayOfObject;
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    if (paramArrayOfT.length == 0)
      paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, 1);
    while (true)
    {
      paramArrayOfT[0] = this.element;
      return paramArrayOfT;
      if (paramArrayOfT.length <= 1)
        continue;
      paramArrayOfT[1] = null;
    }
  }

  public String toString()
  {
    String str = this.element.toString();
    return 2 + str.length() + '[' + str + ']';
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SingletonImmutableSet
 * JD-Core Version:    0.6.0
 */