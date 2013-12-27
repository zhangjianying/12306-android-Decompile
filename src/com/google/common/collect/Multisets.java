package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Set<Lcom.google.common.collect.Multiset.Entry<TE;>;>;
import java.util.Set<TE;>;
import javax.annotation.Nullable;

@GwtCompatible
public final class Multisets
{
  static <E> boolean addAllImpl(Multiset<E> paramMultiset, Collection<? extends E> paramCollection)
  {
    if (paramCollection.isEmpty())
      return false;
    if ((paramCollection instanceof Multiset))
    {
      Iterator localIterator = cast(paramCollection).entrySet().iterator();
      while (localIterator.hasNext())
      {
        Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
        paramMultiset.add(localEntry.getElement(), localEntry.getCount());
      }
    }
    Iterators.addAll(paramMultiset, paramCollection.iterator());
    return true;
  }

  static <T> Multiset<T> cast(Iterable<T> paramIterable)
  {
    return (Multiset)paramIterable;
  }

  static void checkNonnegative(int paramInt, String paramString)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramString;
      arrayOfObject[1] = Integer.valueOf(paramInt);
      Preconditions.checkArgument(bool, "%s cannot be negative: %s", arrayOfObject);
      return;
    }
  }

  static <E> Set<E> elementSetImpl(Multiset<E> paramMultiset)
  {
    return new ElementSetImpl(paramMultiset);
  }

  static boolean equalsImpl(Multiset<?> paramMultiset, @Nullable Object paramObject)
  {
    if (paramObject == paramMultiset);
    while (true)
      while (true)
      {
        return true;
        if ((paramObject instanceof Multiset))
        {
          Multiset localMultiset = (Multiset)paramObject;
          if ((paramMultiset.size() != localMultiset.size()) || (paramMultiset.entrySet().size() != localMultiset.entrySet().size()))
            return false;
          Iterator localIterator = localMultiset.entrySet().iterator();
          if (!localIterator.hasNext())
            continue;
          Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
          if (paramMultiset.count(localEntry.getElement()) == localEntry.getCount())
            break;
          return false;
        }
      }
    return false;
  }

  static <E> Multiset<E> forSet(Set<E> paramSet)
  {
    return new SetMultiset(paramSet);
  }

  public static <E> Multiset.Entry<E> immutableEntry(@Nullable E paramE, int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new AbstractEntry(paramE, paramInt)
      {
        public int getCount()
        {
          return this.val$n;
        }

        public E getElement()
        {
          return this.val$e;
        }
      };
    }
  }

  static int inferDistinctElements(Iterable<?> paramIterable)
  {
    if ((paramIterable instanceof Multiset))
      return ((Multiset)paramIterable).elementSet().size();
    return 11;
  }

  public static <E> Multiset<E> intersection(Multiset<E> paramMultiset, Multiset<?> paramMultiset1)
  {
    Preconditions.checkNotNull(paramMultiset);
    Preconditions.checkNotNull(paramMultiset1);
    return new AbstractMultiset(paramMultiset, paramMultiset1)
    {
      final Set<Multiset.Entry<E>> entrySet = new AbstractSet()
      {
        public boolean contains(Object paramObject)
        {
          boolean bool = paramObject instanceof Multiset.Entry;
          int i = 0;
          if (bool)
          {
            Multiset.Entry localEntry = (Multiset.Entry)paramObject;
            int j = localEntry.getCount();
            i = 0;
            if (j > 0)
            {
              int k = Multisets.2.this.count(localEntry.getElement());
              i = 0;
              if (k == j)
                i = 1;
            }
          }
          return i;
        }

        public boolean isEmpty()
        {
          return Multisets.2.this.elementSet().isEmpty();
        }

        public Iterator<Multiset.Entry<E>> iterator()
        {
          return new AbstractIterator(Multisets.2.this.val$multiset1.entrySet().iterator())
          {
            protected Multiset.Entry<E> computeNext()
            {
              while (this.val$iterator1.hasNext())
              {
                Multiset.Entry localEntry = (Multiset.Entry)this.val$iterator1.next();
                Object localObject = localEntry.getElement();
                int i = Math.min(localEntry.getCount(), Multisets.2.this.val$multiset2.count(localObject));
                if (i > 0)
                  return Multisets.immutableEntry(localObject, i);
              }
              return (Multiset.Entry)endOfData();
            }
          };
        }

        public int size()
        {
          return Multisets.2.this.elementSet().size();
        }
      };

      public int count(Object paramObject)
      {
        int i = this.val$multiset1.count(paramObject);
        if (i == 0)
          return 0;
        return Math.min(i, this.val$multiset2.count(paramObject));
      }

      Set<E> createElementSet()
      {
        return Sets.intersection(this.val$multiset1.elementSet(), this.val$multiset2.elementSet());
      }

      public Set<Multiset.Entry<E>> entrySet()
      {
        return this.entrySet;
      }
    };
  }

  static <E> Iterator<E> iteratorImpl(Multiset<E> paramMultiset)
  {
    return new MultisetIteratorImpl(paramMultiset, paramMultiset.entrySet().iterator());
  }

  static boolean removeAllImpl(Multiset<?> paramMultiset, Collection<?> paramCollection)
  {
    if ((paramCollection instanceof Multiset));
    for (Object localObject = ((Multiset)paramCollection).elementSet(); ; localObject = paramCollection)
      return paramMultiset.elementSet().removeAll((Collection)localObject);
  }

  static boolean retainAllImpl(Multiset<?> paramMultiset, Collection<?> paramCollection)
  {
    if ((paramCollection instanceof Multiset));
    for (Object localObject = ((Multiset)paramCollection).elementSet(); ; localObject = paramCollection)
      return paramMultiset.elementSet().retainAll((Collection)localObject);
  }

  static <E> int setCountImpl(Multiset<E> paramMultiset, E paramE, int paramInt)
  {
    checkNonnegative(paramInt, "count");
    int i = paramMultiset.count(paramE);
    int j = paramInt - i;
    if (j > 0)
      paramMultiset.add(paramE, j);
    do
      return i;
    while (j >= 0);
    paramMultiset.remove(paramE, -j);
    return i;
  }

  static <E> boolean setCountImpl(Multiset<E> paramMultiset, E paramE, int paramInt1, int paramInt2)
  {
    checkNonnegative(paramInt1, "oldCount");
    checkNonnegative(paramInt2, "newCount");
    if (paramMultiset.count(paramE) == paramInt1)
    {
      paramMultiset.setCount(paramE, paramInt2);
      return true;
    }
    return false;
  }

  static int sizeImpl(Multiset<?> paramMultiset)
  {
    long l = 0L;
    Iterator localIterator = paramMultiset.entrySet().iterator();
    while (localIterator.hasNext())
      l += ((Multiset.Entry)localIterator.next()).getCount();
    return Ints.saturatedCast(l);
  }

  public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> paramMultiset)
  {
    return new UnmodifiableMultiset((Multiset)Preconditions.checkNotNull(paramMultiset));
  }

  static abstract class AbstractEntry<E>
    implements Multiset.Entry<E>
  {
    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool1 = paramObject instanceof Multiset.Entry;
      int i = 0;
      if (bool1)
      {
        Multiset.Entry localEntry = (Multiset.Entry)paramObject;
        int j = getCount();
        int k = localEntry.getCount();
        i = 0;
        if (j == k)
        {
          boolean bool2 = Objects.equal(getElement(), localEntry.getElement());
          i = 0;
          if (bool2)
            i = 1;
        }
      }
      return i;
    }

    public int hashCode()
    {
      Object localObject = getElement();
      if (localObject == null);
      for (int i = 0; ; i = localObject.hashCode())
        return i ^ getCount();
    }

    public String toString()
    {
      String str = String.valueOf(getElement());
      int i = getCount();
      if (i == 1)
        return str;
      return str + " x " + i;
    }
  }

  private static final class ElementSetImpl<E> extends AbstractSet<E>
    implements Serializable
  {
    private static final long serialVersionUID;
    private final Multiset<E> multiset;

    ElementSetImpl(Multiset<E> paramMultiset)
    {
      this.multiset = paramMultiset;
    }

    public boolean add(E paramE)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public void clear()
    {
      this.multiset.clear();
    }

    public boolean contains(Object paramObject)
    {
      return this.multiset.contains(paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      return this.multiset.containsAll(paramCollection);
    }

    public boolean isEmpty()
    {
      return this.multiset.isEmpty();
    }

    public Iterator<E> iterator()
    {
      return new Iterator(this.multiset.entrySet().iterator())
      {
        public boolean hasNext()
        {
          return this.val$entryIterator.hasNext();
        }

        public E next()
        {
          return ((Multiset.Entry)this.val$entryIterator.next()).getElement();
        }

        public void remove()
        {
          this.val$entryIterator.remove();
        }
      };
    }

    public boolean remove(Object paramObject)
    {
      int i = this.multiset.count(paramObject);
      if (i > 0)
      {
        this.multiset.remove(paramObject, i);
        return true;
      }
      return false;
    }

    public int size()
    {
      return this.multiset.entrySet().size();
    }
  }

  static final class MultisetIteratorImpl<E>
    implements Iterator<E>
  {
    private boolean canRemove;
    private Multiset.Entry<E> currentEntry;
    private final Iterator<Multiset.Entry<E>> entryIterator;
    private int laterCount;
    private final Multiset<E> multiset;
    private int totalCount;

    MultisetIteratorImpl(Multiset<E> paramMultiset, Iterator<Multiset.Entry<E>> paramIterator)
    {
      this.multiset = paramMultiset;
      this.entryIterator = paramIterator;
    }

    public boolean hasNext()
    {
      return (this.laterCount > 0) || (this.entryIterator.hasNext());
    }

    public E next()
    {
      if (!hasNext())
        throw new NoSuchElementException();
      if (this.laterCount == 0)
      {
        this.currentEntry = ((Multiset.Entry)this.entryIterator.next());
        int i = this.currentEntry.getCount();
        this.laterCount = i;
        this.totalCount = i;
      }
      this.laterCount = (-1 + this.laterCount);
      this.canRemove = true;
      return this.currentEntry.getElement();
    }

    public void remove()
    {
      Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");
      if (this.totalCount == 1)
        this.entryIterator.remove();
      while (true)
      {
        this.totalCount = (-1 + this.totalCount);
        this.canRemove = false;
        return;
        this.multiset.remove(this.currentEntry.getElement());
      }
    }
  }

  private static class SetMultiset<E> extends ForwardingCollection<E>
    implements Multiset<E>, Serializable
  {
    private static final long serialVersionUID;
    final Set<E> delegate;
    transient Set<E> elementSet;
    transient Set<Multiset.Entry<E>> entrySet;

    SetMultiset(Set<E> paramSet)
    {
      this.delegate = ((Set)Preconditions.checkNotNull(paramSet));
    }

    public int add(E paramE, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean add(E paramE)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public int count(Object paramObject)
    {
      if (this.delegate.contains(paramObject))
        return 1;
      return 0;
    }

    protected Set<E> delegate()
    {
      return this.delegate;
    }

    public Set<E> elementSet()
    {
      Object localObject = this.elementSet;
      if (localObject == null)
      {
        localObject = new ElementSet();
        this.elementSet = ((Set)localObject);
      }
      return (Set<E>)localObject;
    }

    public Set<Multiset.Entry<E>> entrySet()
    {
      Object localObject = this.entrySet;
      if (localObject == null)
      {
        localObject = new EntrySet();
        this.entrySet = ((Set)localObject);
      }
      return (Set<Multiset.Entry<E>>)localObject;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool1 = paramObject instanceof Multiset;
      int i = 0;
      if (bool1)
      {
        Multiset localMultiset = (Multiset)paramObject;
        int j = size();
        int k = localMultiset.size();
        i = 0;
        if (j == k)
        {
          boolean bool2 = this.delegate.equals(localMultiset.elementSet());
          i = 0;
          if (bool2)
            i = 1;
        }
      }
      return i;
    }

    public int hashCode()
    {
      int i = 0;
      Iterator localIterator = iterator();
      if (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if (localObject == null);
        for (int j = 0; ; j = localObject.hashCode())
        {
          i += (j ^ 0x1);
          break;
        }
      }
      return i;
    }

    public int remove(Object paramObject, int paramInt)
    {
      int i = 1;
      if (paramInt == 0)
      {
        i = count(paramObject);
        return i;
      }
      if (paramInt > 0);
      int k;
      for (int j = i; ; k = 0)
      {
        Preconditions.checkArgument(j);
        if (this.delegate.remove(paramObject))
          break;
        return 0;
      }
    }

    public int setCount(E paramE, int paramInt)
    {
      Multisets.checkNonnegative(paramInt, "count");
      if (paramInt == count(paramE))
        return paramInt;
      if (paramInt == 0)
      {
        remove(paramE);
        return 1;
      }
      throw new UnsupportedOperationException();
    }

    public boolean setCount(E paramE, int paramInt1, int paramInt2)
    {
      return Multisets.setCountImpl(this, paramE, paramInt1, paramInt2);
    }

    class ElementSet extends ForwardingSet<E>
    {
      ElementSet()
      {
      }

      public boolean add(E paramE)
      {
        throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection<? extends E> paramCollection)
      {
        throw new UnsupportedOperationException();
      }

      protected Set<E> delegate()
      {
        return Multisets.SetMultiset.this.delegate;
      }
    }

    class EntrySet extends AbstractSet<Multiset.Entry<E>>
    {
      EntrySet()
      {
      }

      public Iterator<Multiset.Entry<E>> iterator()
      {
        return new Iterator()
        {
          final Iterator<E> elements = Multisets.SetMultiset.this.delegate.iterator();

          public boolean hasNext()
          {
            return this.elements.hasNext();
          }

          public Multiset.Entry<E> next()
          {
            return Multisets.immutableEntry(this.elements.next(), 1);
          }

          public void remove()
          {
            this.elements.remove();
          }
        };
      }

      public int size()
      {
        return Multisets.SetMultiset.this.delegate.size();
      }
    }
  }

  private static class UnmodifiableMultiset<E> extends ForwardingMultiset<E>
    implements Serializable
  {
    private static final long serialVersionUID;
    final Multiset<? extends E> delegate;
    transient Set<E> elementSet;
    transient Set<Multiset.Entry<E>> entrySet;

    UnmodifiableMultiset(Multiset<? extends E> paramMultiset)
    {
      this.delegate = paramMultiset;
    }

    public int add(E paramE, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean add(E paramE)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends E> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public void clear()
    {
      throw new UnsupportedOperationException();
    }

    protected Multiset<E> delegate()
    {
      return this.delegate;
    }

    public Set<E> elementSet()
    {
      Set localSet = this.elementSet;
      if (localSet == null)
      {
        localSet = Collections.unmodifiableSet(this.delegate.elementSet());
        this.elementSet = localSet;
      }
      return localSet;
    }

    public Set<Multiset.Entry<E>> entrySet()
    {
      Set localSet = this.entrySet;
      if (localSet == null)
      {
        localSet = Collections.unmodifiableSet(this.delegate.entrySet());
        this.entrySet = localSet;
      }
      return localSet;
    }

    public Iterator<E> iterator()
    {
      return Iterators.unmodifiableIterator(this.delegate.iterator());
    }

    public int remove(Object paramObject, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean remove(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public int setCount(E paramE, int paramInt)
    {
      throw new UnsupportedOperationException();
    }

    public boolean setCount(E paramE, int paramInt1, int paramInt2)
    {
      throw new UnsupportedOperationException();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multisets
 * JD-Core Version:    0.6.0
 */