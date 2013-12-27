package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true)
final class PairwiseEquivalence<T>
  implements Equivalence<Iterable<T>>, Serializable
{
  private static final long serialVersionUID;
  final Equivalence<? super T> elementEquivalence;

  PairwiseEquivalence(Equivalence<? super T> paramEquivalence)
  {
    this.elementEquivalence = ((Equivalence)Preconditions.checkNotNull(paramEquivalence));
  }

  public boolean equals(@Nullable Object paramObject)
  {
    if ((paramObject instanceof PairwiseEquivalence))
    {
      PairwiseEquivalence localPairwiseEquivalence = (PairwiseEquivalence)paramObject;
      return this.elementEquivalence.equals(localPairwiseEquivalence.elementEquivalence);
    }
    return false;
  }

  public boolean equivalent(@Nullable Iterable<T> paramIterable1, @Nullable Iterable<T> paramIterable2)
  {
    if (paramIterable1 == null)
      if (paramIterable2 != null);
    Iterator localIterator1;
    Iterator localIterator2;
    do
    {
      return true;
      return false;
      if (paramIterable2 == null)
        return false;
      localIterator1 = paramIterable1.iterator();
      localIterator2 = paramIterable2.iterator();
      while ((localIterator1.hasNext()) && (localIterator2.hasNext()))
        if (!this.elementEquivalence.equivalent(localIterator1.next(), localIterator2.next()))
          return false;
    }
    while ((!localIterator1.hasNext()) && (!localIterator2.hasNext()));
    return false;
  }

  public int hash(@Nullable Iterable<T> paramIterable)
  {
    int i;
    if (paramIterable == null)
      i = 0;
    while (true)
    {
      return i;
      i = 78721;
      Iterator localIterator = paramIterable.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        i = i * 24943 + this.elementEquivalence.hash(localObject);
      }
    }
  }

  public int hashCode()
  {
    return 0x46A3EB07 ^ this.elementEquivalence.hashCode();
  }

  public String toString()
  {
    return "Equivalences.pairwise(" + this.elementEquivalence + ")";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.PairwiseEquivalence
 * JD-Core Version:    0.6.0
 */