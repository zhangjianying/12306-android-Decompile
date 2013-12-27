package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSortedSet<E> extends ForwardingSet<E>
  implements SortedSet<E>
{
  private int unsafeCompare(Object paramObject1, Object paramObject2)
  {
    Comparator localComparator = comparator();
    if (localComparator == null)
      return ((Comparable)paramObject1).compareTo(paramObject2);
    return localComparator.compare(paramObject1, paramObject2);
  }

  public Comparator<? super E> comparator()
  {
    return delegate().comparator();
  }

  protected abstract SortedSet<E> delegate();

  public E first()
  {
    return delegate().first();
  }

  public SortedSet<E> headSet(E paramE)
  {
    return delegate().headSet(paramE);
  }

  public E last()
  {
    return delegate().last();
  }

  @Beta
  protected boolean standardContains(@Nullable Object paramObject)
  {
    try
    {
      int i = unsafeCompare(tailSet(paramObject).first(), paramObject);
      int j = 0;
      if (i == 0)
        j = 1;
      return j;
    }
    catch (ClassCastException localClassCastException)
    {
      return false;
    }
    catch (NoSuchElementException localNoSuchElementException)
    {
      return false;
    }
    catch (NullPointerException localNullPointerException)
    {
    }
    return false;
  }

  @Beta
  protected boolean standardRemove(@Nullable Object paramObject)
  {
    try
    {
      Iterator localIterator = tailSet(paramObject).iterator();
      boolean bool = localIterator.hasNext();
      int i = 0;
      if (bool)
      {
        int j = unsafeCompare(localIterator.next(), paramObject);
        i = 0;
        if (j == 0)
        {
          localIterator.remove();
          i = 1;
        }
      }
      return i;
    }
    catch (ClassCastException localClassCastException)
    {
      return false;
    }
    catch (NullPointerException localNullPointerException)
    {
    }
    return false;
  }

  @Beta
  protected SortedSet<E> standardSubSet(E paramE1, E paramE2)
  {
    return tailSet(paramE1).headSet(paramE2);
  }

  public SortedSet<E> subSet(E paramE1, E paramE2)
  {
    return delegate().subSet(paramE1, paramE2);
  }

  public SortedSet<E> tailSet(E paramE)
  {
    return delegate().tailSet(paramE);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingSortedSet
 * JD-Core Version:    0.6.0
 */