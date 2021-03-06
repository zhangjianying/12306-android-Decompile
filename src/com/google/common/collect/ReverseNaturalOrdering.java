package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;

@GwtCompatible(serializable=true)
final class ReverseNaturalOrdering extends Ordering<Comparable>
  implements Serializable
{
  static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
  private static final long serialVersionUID;

  private Object readResolve()
  {
    return INSTANCE;
  }

  public int compare(Comparable paramComparable1, Comparable paramComparable2)
  {
    Preconditions.checkNotNull(paramComparable1);
    if (paramComparable1 == paramComparable2)
      return 0;
    return paramComparable2.compareTo(paramComparable1);
  }

  public <E extends Comparable> E max(E paramE1, E paramE2)
  {
    return (Comparable)NaturalOrdering.INSTANCE.min(paramE1, paramE2);
  }

  public <E extends Comparable> E max(E paramE1, E paramE2, E paramE3, E[] paramArrayOfE)
  {
    return (Comparable)NaturalOrdering.INSTANCE.min(paramE1, paramE2, paramE3, paramArrayOfE);
  }

  public <E extends Comparable> E max(Iterable<E> paramIterable)
  {
    return (Comparable)NaturalOrdering.INSTANCE.min(paramIterable);
  }

  public <E extends Comparable> E min(E paramE1, E paramE2)
  {
    return (Comparable)NaturalOrdering.INSTANCE.max(paramE1, paramE2);
  }

  public <E extends Comparable> E min(E paramE1, E paramE2, E paramE3, E[] paramArrayOfE)
  {
    return (Comparable)NaturalOrdering.INSTANCE.max(paramE1, paramE2, paramE3, paramArrayOfE);
  }

  public <E extends Comparable> E min(Iterable<E> paramIterable)
  {
    return (Comparable)NaturalOrdering.INSTANCE.max(paramIterable);
  }

  public <S extends Comparable> Ordering<S> reverse()
  {
    return Ordering.natural();
  }

  public String toString()
  {
    return "Ordering.natural().reverse()";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ReverseNaturalOrdering
 * JD-Core Version:    0.6.0
 */