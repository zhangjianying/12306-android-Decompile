package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Set<Lcom.google.common.collect.Multiset.Entry<TE;>;>;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true, serializable=true)
public class ImmutableMultiset<E> extends ImmutableCollection<E>
  implements Multiset<E>
{
  private static final long serialVersionUID;
  private transient ImmutableSet<Multiset.Entry<E>> entrySet;
  private final transient ImmutableMap<E, Integer> map;
  private final transient int size;

  ImmutableMultiset(ImmutableMap<E, Integer> paramImmutableMap, int paramInt)
  {
    this.map = paramImmutableMap;
    this.size = paramInt;
  }

  public static <E> Builder<E> builder()
  {
    return new Builder();
  }

  public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> paramIterable)
  {
    if ((paramIterable instanceof ImmutableMultiset))
    {
      ImmutableMultiset localImmutableMultiset = (ImmutableMultiset)paramIterable;
      if (!localImmutableMultiset.isPartialView())
        return localImmutableMultiset;
    }
    if ((paramIterable instanceof Multiset));
    for (Object localObject = Multisets.cast(paramIterable); ; localObject = LinkedHashMultiset.create(paramIterable))
      return copyOfInternal((Multiset)localObject);
  }

  public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> paramIterator)
  {
    LinkedHashMultiset localLinkedHashMultiset = LinkedHashMultiset.create();
    Iterators.addAll(localLinkedHashMultiset, paramIterator);
    return copyOfInternal(localLinkedHashMultiset);
  }

  public static <E> ImmutableMultiset<E> copyOf(E[] paramArrayOfE)
  {
    return copyOf(Arrays.asList(paramArrayOfE));
  }

  private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> paramMultiset)
  {
    long l = 0L;
    ImmutableMap.Builder localBuilder = ImmutableMap.builder();
    Iterator localIterator = paramMultiset.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
      int i = localEntry.getCount();
      if (i <= 0)
        continue;
      localBuilder.put(localEntry.getElement(), Integer.valueOf(i));
      l += i;
    }
    if (l == 0L)
      return of();
    return new ImmutableMultiset(localBuilder.build(), Ints.saturatedCast(l));
  }

  private static <E> ImmutableMultiset<E> copyOfInternal(E[] paramArrayOfE)
  {
    return copyOf(Arrays.asList(paramArrayOfE));
  }

  public static <E> ImmutableMultiset<E> of()
  {
    return EmptyImmutableMultiset.INSTANCE;
  }

  public static <E> ImmutableMultiset<E> of(E paramE)
  {
    return copyOfInternal(new Object[] { paramE });
  }

  public static <E> ImmutableMultiset<E> of(E paramE1, E paramE2)
  {
    return copyOfInternal(new Object[] { paramE1, paramE2 });
  }

  public static <E> ImmutableMultiset<E> of(E paramE1, E paramE2, E paramE3)
  {
    return copyOfInternal(new Object[] { paramE1, paramE2, paramE3 });
  }

  public static <E> ImmutableMultiset<E> of(E paramE1, E paramE2, E paramE3, E paramE4)
  {
    return copyOfInternal(new Object[] { paramE1, paramE2, paramE3, paramE4 });
  }

  public static <E> ImmutableMultiset<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5)
  {
    return copyOfInternal(new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5 });
  }

  public static <E> ImmutableMultiset<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E[] paramArrayOfE)
  {
    ArrayList localArrayList = new ArrayList(6 + paramArrayOfE.length);
    Collections.addAll(localArrayList, new Object[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6 });
    Collections.addAll(localArrayList, paramArrayOfE);
    return copyOf(localArrayList);
  }

  @Deprecated
  public static <E> ImmutableMultiset<E> of(E[] paramArrayOfE)
  {
    return copyOf(Arrays.asList(paramArrayOfE));
  }

  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    int i = paramObjectInputStream.readInt();
    ImmutableMap.Builder localBuilder = ImmutableMap.builder();
    long l = 0L;
    for (int j = 0; j < i; j++)
    {
      Object localObject = paramObjectInputStream.readObject();
      int k = paramObjectInputStream.readInt();
      if (k <= 0)
        throw new InvalidObjectException("Invalid count " + k);
      localBuilder.put(localObject, Integer.valueOf(k));
      l += k;
    }
    FieldSettersHolder.MAP_FIELD_SETTER.set(this, localBuilder.build());
    FieldSettersHolder.SIZE_FIELD_SETTER.set(this, Ints.saturatedCast(l));
  }

  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    Serialization.writeMultiset(this, paramObjectOutputStream);
  }

  public int add(E paramE, int paramInt)
  {
    throw new UnsupportedOperationException();
  }

  public boolean contains(@Nullable Object paramObject)
  {
    return this.map.containsKey(paramObject);
  }

  public int count(@Nullable Object paramObject)
  {
    Integer localInteger = (Integer)this.map.get(paramObject);
    if (localInteger == null)
      return 0;
    return localInteger.intValue();
  }

  public Set<E> elementSet()
  {
    return this.map.keySet();
  }

  public Set<Multiset.Entry<E>> entrySet()
  {
    Object localObject = this.entrySet;
    if (localObject == null)
    {
      localObject = new EntrySet(this);
      this.entrySet = ((ImmutableSet)localObject);
    }
    return (Set<Multiset.Entry<E>>)localObject;
  }

  public boolean equals(@Nullable Object paramObject)
  {
    if (paramObject == this);
    while (true)
      while (true)
      {
        return true;
        if ((paramObject instanceof Multiset))
        {
          Multiset localMultiset = (Multiset)paramObject;
          if (size() != localMultiset.size())
            return false;
          Iterator localIterator = localMultiset.entrySet().iterator();
          if (!localIterator.hasNext())
            continue;
          Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
          if (count(localEntry.getElement()) == localEntry.getCount())
            break;
          return false;
        }
      }
    return false;
  }

  public int hashCode()
  {
    return this.map.hashCode();
  }

  boolean isPartialView()
  {
    return this.map.isPartialView();
  }

  public UnmodifiableIterator<E> iterator()
  {
    return new UnmodifiableIterator(this.map.entrySet().iterator())
    {
      E element;
      int remaining;

      public boolean hasNext()
      {
        return (this.remaining > 0) || (this.val$mapIterator.hasNext());
      }

      public E next()
      {
        if (this.remaining <= 0)
        {
          Map.Entry localEntry = (Map.Entry)this.val$mapIterator.next();
          this.element = localEntry.getKey();
          this.remaining = ((Integer)localEntry.getValue()).intValue();
        }
        this.remaining = (-1 + this.remaining);
        return this.element;
      }
    };
  }

  public int remove(Object paramObject, int paramInt)
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

  public int size()
  {
    return this.size;
  }

  public String toString()
  {
    return entrySet().toString();
  }

  @GwtIncompatible("java serialization not supported.")
  Object writeReplace()
  {
    return this;
  }

  public static final class Builder<E> extends ImmutableCollection.Builder<E>
  {
    private final Multiset<E> contents = LinkedHashMultiset.create();

    public Builder<E> add(E paramE)
    {
      this.contents.add(Preconditions.checkNotNull(paramE));
      return this;
    }

    public Builder<E> add(E[] paramArrayOfE)
    {
      super.add(paramArrayOfE);
      return this;
    }

    public Builder<E> addAll(Iterable<? extends E> paramIterable)
    {
      if ((paramIterable instanceof Multiset))
      {
        Iterator localIterator = Multisets.cast(paramIterable).entrySet().iterator();
        while (localIterator.hasNext())
        {
          Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
          addCopies(localEntry.getElement(), localEntry.getCount());
        }
      }
      super.addAll(paramIterable);
      return this;
    }

    public Builder<E> addAll(Iterator<? extends E> paramIterator)
    {
      super.addAll(paramIterator);
      return this;
    }

    public Builder<E> addCopies(E paramE, int paramInt)
    {
      this.contents.add(Preconditions.checkNotNull(paramE), paramInt);
      return this;
    }

    public ImmutableMultiset<E> build()
    {
      return ImmutableMultiset.copyOf(this.contents);
    }

    public Builder<E> setCount(E paramE, int paramInt)
    {
      this.contents.setCount(Preconditions.checkNotNull(paramE), paramInt);
      return this;
    }
  }

  private static class EntrySet<E> extends ImmutableSet<Multiset.Entry<E>>
  {
    private static final long serialVersionUID;
    final ImmutableMultiset<E> multiset;

    public EntrySet(ImmutableMultiset<E> paramImmutableMultiset)
    {
      this.multiset = paramImmutableMultiset;
    }

    public boolean contains(Object paramObject)
    {
      Multiset.Entry localEntry;
      if ((paramObject instanceof Multiset.Entry))
      {
        localEntry = (Multiset.Entry)paramObject;
        if (localEntry.getCount() > 0)
          break label23;
      }
      label23: 
      do
        return false;
      while (this.multiset.count(localEntry.getElement()) != localEntry.getCount());
      return true;
    }

    public int hashCode()
    {
      return this.multiset.map.hashCode();
    }

    boolean isPartialView()
    {
      return this.multiset.isPartialView();
    }

    public UnmodifiableIterator<Multiset.Entry<E>> iterator()
    {
      return new UnmodifiableIterator(this.multiset.map.entrySet().iterator())
      {
        public boolean hasNext()
        {
          return this.val$mapIterator.hasNext();
        }

        public Multiset.Entry<E> next()
        {
          Map.Entry localEntry = (Map.Entry)this.val$mapIterator.next();
          return Multisets.immutableEntry(localEntry.getKey(), ((Integer)localEntry.getValue()).intValue());
        }
      };
    }

    public int size()
    {
      return this.multiset.map.size();
    }

    public Object[] toArray()
    {
      return toArray(new Object[size()]);
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      int i = size();
      if (paramArrayOfT.length < i)
        paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, i);
      while (true)
      {
        T[] arrayOfT = paramArrayOfT;
        int j = 0;
        Iterator localIterator = iterator();
        while (localIterator.hasNext())
        {
          Multiset.Entry localEntry = (Multiset.Entry)localIterator.next();
          int k = j + 1;
          arrayOfT[j] = localEntry;
          j = k;
        }
        if (paramArrayOfT.length <= i)
          continue;
        paramArrayOfT[i] = null;
      }
      return paramArrayOfT;
    }

    @GwtIncompatible("not needed in emulated source.")
    Object writeReplace()
    {
      return this;
    }
  }

  @GwtIncompatible("java serialization is not supported.")
  private static class FieldSettersHolder
  {
    static final Serialization.FieldSetter<ImmutableMultiset> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultiset.class, "map");
    static final Serialization.FieldSetter<ImmutableMultiset> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultiset.class, "size");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableMultiset
 * JD-Core Version:    0.6.0
 */