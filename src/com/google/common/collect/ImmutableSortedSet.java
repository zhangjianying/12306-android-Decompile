package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

@GwtCompatible(emulated=true, serializable=true)
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E>
  implements SortedSet<E>
{
  private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET;
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
  final transient Comparator<? super E> comparator;

  static
  {
    NATURAL_EMPTY_SET = new EmptyImmutableSortedSet(NATURAL_ORDER);
  }

  ImmutableSortedSet(Comparator<? super E> paramComparator)
  {
    this.comparator = paramComparator;
  }

  public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> paramIterable)
  {
    return copyOf(Ordering.natural(), paramIterable);
  }

  public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> paramCollection)
  {
    return copyOf(Ordering.natural(), paramCollection);
  }

  public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> paramComparator, Iterable<? extends E> paramIterable)
  {
    Preconditions.checkNotNull(paramComparator);
    return copyOfInternal(paramComparator, paramIterable, false);
  }

  public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> paramComparator, Collection<? extends E> paramCollection)
  {
    Preconditions.checkNotNull(paramComparator);
    return copyOfInternal(paramComparator, paramCollection, false);
  }

  public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> paramComparator, Iterator<? extends E> paramIterator)
  {
    Preconditions.checkNotNull(paramComparator);
    return copyOfInternal(paramComparator, paramIterator);
  }

  public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> paramIterator)
  {
    return copyOfInternal(Ordering.natural(), paramIterator);
  }

  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] paramArrayOfE)
  {
    return copyOf(Ordering.natural(), Arrays.asList(paramArrayOfE));
  }

  private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> paramComparator, Iterable<? extends E> paramIterable, boolean paramBoolean)
  {
    int i;
    ImmutableSortedSet localImmutableSortedSet;
    if ((paramBoolean) || (hasSameComparator(paramIterable, paramComparator)))
    {
      i = 1;
      if ((i == 0) || (!(paramIterable instanceof ImmutableSortedSet)))
        break label78;
      localImmutableSortedSet = (ImmutableSortedSet)paramIterable;
      if (!localImmutableSortedSet.isEmpty())
        break label47;
    }
    label47: ImmutableList localImmutableList2;
    ImmutableList localImmutableList3;
    do
    {
      return localImmutableSortedSet;
      i = 0;
      break;
      localImmutableList2 = localImmutableSortedSet.asList();
      localImmutableList3 = ImmutableList.copyOf(paramIterable);
    }
    while (localImmutableList2 == localImmutableList3);
    return new RegularImmutableSortedSet(localImmutableList3, paramComparator);
    label78: ImmutableList localImmutableList1 = immutableSortedUniqueCopy(paramComparator, Lists.newArrayList(paramIterable));
    if (localImmutableList1.isEmpty())
      return emptySet(paramComparator);
    return new RegularImmutableSortedSet(localImmutableList1, paramComparator);
  }

  private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> paramComparator, Iterator<? extends E> paramIterator)
  {
    if (!paramIterator.hasNext())
      return emptySet(paramComparator);
    return new RegularImmutableSortedSet(immutableSortedUniqueCopy(paramComparator, Lists.newArrayList(paramIterator)), paramComparator);
  }

  public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> paramSortedSet)
  {
    Comparator localComparator = paramSortedSet.comparator();
    if (localComparator == null)
      localComparator = NATURAL_ORDER;
    return copyOfInternal(localComparator, paramSortedSet, true);
  }

  private static <E> ImmutableSortedSet<E> emptySet()
  {
    return NATURAL_EMPTY_SET;
  }

  static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> paramComparator)
  {
    if (NATURAL_ORDER.equals(paramComparator))
      return emptySet();
    return new EmptyImmutableSortedSet(paramComparator);
  }

  static boolean hasSameComparator(Iterable<?> paramIterable, Comparator<?> paramComparator)
  {
    boolean bool = paramIterable instanceof SortedSet;
    int i = 0;
    Comparator localComparator;
    if (bool)
    {
      localComparator = ((SortedSet)paramIterable).comparator();
      if (localComparator != null)
        break label44;
      Ordering localOrdering = Ordering.natural();
      i = 0;
      if (paramComparator == localOrdering)
        i = 1;
    }
    return i;
    label44: return paramComparator.equals(localComparator);
  }

  private static <E> ImmutableList<E> immutableSortedUniqueCopy(Comparator<? super E> paramComparator, List<E> paramList)
  {
    if (paramList.isEmpty())
      return ImmutableList.of();
    Collections.sort(paramList, paramComparator);
    int i = 1;
    for (int j = 1; j < paramList.size(); j++)
    {
      Object localObject = paramList.get(j);
      if (paramComparator.compare(localObject, paramList.get(i - 1)) == 0)
        continue;
      int k = i + 1;
      paramList.set(i, localObject);
      i = k;
    }
    return ImmutableList.copyOf(paramList.subList(0, i));
  }

  public static <E extends Comparable<E>> Builder<E> naturalOrder()
  {
    return new Builder(Ordering.natural());
  }

  public static <E> ImmutableSortedSet<E> of()
  {
    return emptySet();
  }

  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E paramE)
  {
    return new RegularImmutableSortedSet(ImmutableList.of(paramE), Ordering.natural());
  }

  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E paramE1, E paramE2)
  {
    return copyOf(Ordering.natural(), Arrays.asList(new Comparable[] { paramE1, paramE2 }));
  }

  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E paramE1, E paramE2, E paramE3)
  {
    return copyOf(Ordering.natural(), Arrays.asList(new Comparable[] { paramE1, paramE2, paramE3 }));
  }

  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E paramE1, E paramE2, E paramE3, E paramE4)
  {
    return copyOf(Ordering.natural(), Arrays.asList(new Comparable[] { paramE1, paramE2, paramE3, paramE4 }));
  }

  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5)
  {
    return copyOf(Ordering.natural(), Arrays.asList(new Comparable[] { paramE1, paramE2, paramE3, paramE4, paramE5 }));
  }

  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E paramE1, E paramE2, E paramE3, E paramE4, E paramE5, E paramE6, E[] paramArrayOfE)
  {
    ArrayList localArrayList = new ArrayList(6 + paramArrayOfE.length);
    Collections.addAll(localArrayList, new Comparable[] { paramE1, paramE2, paramE3, paramE4, paramE5, paramE6 });
    Collections.addAll(localArrayList, paramArrayOfE);
    return copyOf(Ordering.natural(), localArrayList);
  }

  @Deprecated
  public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E[] paramArrayOfE)
  {
    return copyOf(paramArrayOfE);
  }

  public static <E> Builder<E> orderedBy(Comparator<E> paramComparator)
  {
    return new Builder(paramComparator);
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }

  public static <E extends Comparable<E>> Builder<E> reverseOrder()
  {
    return new Builder(Ordering.natural().reverse());
  }

  static int unsafeCompare(Comparator<?> paramComparator, Object paramObject1, Object paramObject2)
  {
    return paramComparator.compare(paramObject1, paramObject2);
  }

  @Deprecated
  @Beta
  public static <E> ImmutableSortedSet<E> withExplicitOrder(E paramE, E[] paramArrayOfE)
  {
    return withExplicitOrder(Lists.asList(paramE, paramArrayOfE));
  }

  @Deprecated
  @Beta
  public static <E> ImmutableSortedSet<E> withExplicitOrder(List<E> paramList)
  {
    return ExplicitOrderedImmutableSortedSet.create(paramList);
  }

  public Comparator<? super E> comparator()
  {
    return this.comparator;
  }

  public ImmutableSortedSet<E> headSet(E paramE)
  {
    return headSetImpl(Preconditions.checkNotNull(paramE));
  }

  abstract ImmutableSortedSet<E> headSetImpl(E paramE);

  abstract int indexOf(Object paramObject);

  public ImmutableSortedSet<E> subSet(E paramE1, E paramE2)
  {
    Preconditions.checkNotNull(paramE1);
    Preconditions.checkNotNull(paramE2);
    if (this.comparator.compare(paramE1, paramE2) <= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return subSetImpl(paramE1, paramE2);
    }
  }

  abstract ImmutableSortedSet<E> subSetImpl(E paramE1, E paramE2);

  public ImmutableSortedSet<E> tailSet(E paramE)
  {
    return tailSetImpl(Preconditions.checkNotNull(paramE));
  }

  abstract ImmutableSortedSet<E> tailSetImpl(E paramE);

  int unsafeCompare(Object paramObject1, Object paramObject2)
  {
    return unsafeCompare(this.comparator, paramObject1, paramObject2);
  }

  Object writeReplace()
  {
    return new SerializedForm(this.comparator, toArray());
  }

  public static final class Builder<E> extends ImmutableSet.Builder<E>
  {
    private final Comparator<? super E> comparator;

    public Builder(Comparator<? super E> paramComparator)
    {
      this.comparator = ((Comparator)Preconditions.checkNotNull(paramComparator));
    }

    public Builder<E> add(E paramE)
    {
      super.add(paramE);
      return this;
    }

    public Builder<E> add(E[] paramArrayOfE)
    {
      super.add(paramArrayOfE);
      return this;
    }

    public Builder<E> addAll(Iterable<? extends E> paramIterable)
    {
      super.addAll(paramIterable);
      return this;
    }

    public Builder<E> addAll(Iterator<? extends E> paramIterator)
    {
      super.addAll(paramIterator);
      return this;
    }

    public ImmutableSortedSet<E> build()
    {
      return ImmutableSortedSet.access$000(this.comparator, this.contents.iterator());
    }
  }

  private static class SerializedForm<E>
    implements Serializable
  {
    private static final long serialVersionUID;
    final Comparator<? super E> comparator;
    final Object[] elements;

    public SerializedForm(Comparator<? super E> paramComparator, Object[] paramArrayOfObject)
    {
      this.comparator = paramComparator;
      this.elements = paramArrayOfObject;
    }

    Object readResolve()
    {
      return new ImmutableSortedSet.Builder(this.comparator).add((Object[])this.elements).build();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSortedSet
 * JD-Core Version:    0.6.0
 */