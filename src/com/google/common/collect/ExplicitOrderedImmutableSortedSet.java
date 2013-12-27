package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@GwtCompatible(emulated=true, serializable=true)
final class ExplicitOrderedImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
  private final Object[] elements;
  private final int fromIndex;
  private final int toIndex;

  ExplicitOrderedImmutableSortedSet(Object[] paramArrayOfObject, Comparator<? super E> paramComparator)
  {
    this(paramArrayOfObject, paramComparator, 0, paramArrayOfObject.length);
  }

  ExplicitOrderedImmutableSortedSet(Object[] paramArrayOfObject, Comparator<? super E> paramComparator, int paramInt1, int paramInt2)
  {
    super(paramComparator);
    this.elements = paramArrayOfObject;
    this.fromIndex = paramInt1;
    this.toIndex = paramInt2;
  }

  static <E> ImmutableSortedSet<E> create(List<E> paramList)
  {
    ExplicitOrdering localExplicitOrdering = new ExplicitOrdering(paramList);
    if (localExplicitOrdering.rankMap.isEmpty())
      return emptySet(localExplicitOrdering);
    return new ExplicitOrderedImmutableSortedSet(localExplicitOrdering.rankMap.keySet().toArray(), localExplicitOrdering);
  }

  private ImmutableSortedSet<E> createSubset(int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2)
      return new ExplicitOrderedImmutableSortedSet(this.elements, this.comparator, paramInt1, paramInt2);
    return emptySet(this.comparator);
  }

  private int findSubsetIndex(E paramE)
  {
    Integer localInteger = (Integer)rankMap().get(paramE);
    if (localInteger == null)
      throw new ClassCastException();
    if (localInteger.intValue() <= this.fromIndex)
      return this.fromIndex;
    if (localInteger.intValue() >= this.toIndex)
      return this.toIndex;
    return localInteger.intValue();
  }

  private ImmutableMap<E, Integer> rankMap()
  {
    return ((ExplicitOrdering)comparator()).rankMap;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }

  public boolean contains(Object paramObject)
  {
    Integer localInteger = (Integer)rankMap().get(paramObject);
    return (localInteger != null) && (localInteger.intValue() >= this.fromIndex) && (localInteger.intValue() < this.toIndex);
  }

  ImmutableList<E> createAsList()
  {
    return new ImmutableSortedAsList(this, new RegularImmutableList(this.elements, this.fromIndex, size()));
  }

  public E first()
  {
    return this.elements[this.fromIndex];
  }

  public int hashCode()
  {
    int i = 0;
    for (int j = this.fromIndex; j < this.toIndex; j++)
      i += this.elements[j].hashCode();
    return i;
  }

  ImmutableSortedSet<E> headSetImpl(E paramE)
  {
    return createSubset(this.fromIndex, findSubsetIndex(paramE));
  }

  int indexOf(Object paramObject)
  {
    Integer localInteger = (Integer)rankMap().get(paramObject);
    if ((localInteger != null) && (localInteger.intValue() >= this.fromIndex) && (localInteger.intValue() < this.toIndex))
      return localInteger.intValue() - this.fromIndex;
    return -1;
  }

  public boolean isEmpty()
  {
    return false;
  }

  boolean isPartialView()
  {
    return (this.fromIndex != 0) || (this.toIndex != this.elements.length);
  }

  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.forArray(this.elements, this.fromIndex, size());
  }

  public E last()
  {
    return this.elements[(-1 + this.toIndex)];
  }

  public int size()
  {
    return this.toIndex - this.fromIndex;
  }

  ImmutableSortedSet<E> subSetImpl(E paramE1, E paramE2)
  {
    return createSubset(findSubsetIndex(paramE1), findSubsetIndex(paramE2));
  }

  ImmutableSortedSet<E> tailSetImpl(E paramE)
  {
    return createSubset(findSubsetIndex(paramE), this.toIndex);
  }

  public Object[] toArray()
  {
    Object[] arrayOfObject = new Object[size()];
    Platform.unsafeArrayCopy(this.elements, this.fromIndex, arrayOfObject, 0, size());
    return arrayOfObject;
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    int i = size();
    if (paramArrayOfT.length < i)
      paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, i);
    while (true)
    {
      Platform.unsafeArrayCopy(this.elements, this.fromIndex, paramArrayOfT, 0, i);
      return paramArrayOfT;
      if (paramArrayOfT.length <= i)
        continue;
      paramArrayOfT[i] = null;
    }
  }

  Object writeReplace()
  {
    return new SerializedForm(toArray());
  }

  private static class SerializedForm<E>
    implements Serializable
  {
    private static final long serialVersionUID;
    final Object[] elements;

    public SerializedForm(Object[] paramArrayOfObject)
    {
      this.elements = paramArrayOfObject;
    }

    Object readResolve()
    {
      return ImmutableSortedSet.withExplicitOrder(Arrays.asList(this.elements));
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ExplicitOrderedImmutableSortedSet
 * JD-Core Version:    0.6.0
 */