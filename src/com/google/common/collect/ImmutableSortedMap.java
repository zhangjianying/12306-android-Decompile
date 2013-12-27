package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true, serializable=true)
public class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V>
  implements SortedMap<K, V>
{
  private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP;
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
  private static final long serialVersionUID;
  private final transient Comparator<? super K> comparator;
  final transient ImmutableList<Map.Entry<K, V>> entries;
  private transient ImmutableSet<Map.Entry<K, V>> entrySet;
  final transient Function<Map.Entry<K, V>, K> keyFunction = new Function()
  {
    public K apply(Map.Entry<K, V> paramEntry)
    {
      return paramEntry.getKey();
    }
  };
  private transient ImmutableSortedSet<K> keySet;
  private transient ImmutableCollection<V> values;

  static
  {
    NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableList.of(), NATURAL_ORDER);
  }

  ImmutableSortedMap(ImmutableList<Map.Entry<K, V>> paramImmutableList, Comparator<? super K> paramComparator)
  {
    this.entries = paramImmutableList;
    this.comparator = paramComparator;
  }

  public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> paramMap)
  {
    return copyOfInternal(paramMap, Ordering.natural());
  }

  public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> paramMap, Comparator<? super K> paramComparator)
  {
    return copyOfInternal(paramMap, (Comparator)Preconditions.checkNotNull(paramComparator));
  }

  private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> paramMap, Comparator<? super K> paramComparator)
  {
    boolean bool1 = paramMap instanceof SortedMap;
    boolean bool2 = false;
    Comparator localComparator;
    if (bool1)
    {
      localComparator = ((SortedMap)paramMap).comparator();
      if (localComparator != null)
        break label69;
      if (paramComparator != NATURAL_ORDER)
        break label64;
      bool2 = true;
    }
    while ((bool2) && ((paramMap instanceof ImmutableSortedMap)))
    {
      ImmutableSortedMap localImmutableSortedMap = (ImmutableSortedMap)paramMap;
      if (localImmutableSortedMap.isPartialView())
        break;
      return localImmutableSortedMap;
      label64: bool2 = false;
      continue;
      label69: bool2 = paramComparator.equals(localComparator);
    }
    Map.Entry[] arrayOfEntry = (Map.Entry[])paramMap.entrySet().toArray(new Map.Entry[0]);
    for (int i = 0; i < arrayOfEntry.length; i++)
    {
      Map.Entry localEntry = arrayOfEntry[i];
      arrayOfEntry[i] = entryOf(localEntry.getKey(), localEntry.getValue());
    }
    List localList = Arrays.asList(arrayOfEntry);
    if (!bool2)
    {
      sortEntries(localList, paramComparator);
      validateEntries(localList, paramComparator);
    }
    return new ImmutableSortedMap(ImmutableList.copyOf(localList), paramComparator);
  }

  public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> paramSortedMap)
  {
    Comparator localComparator = paramSortedMap.comparator();
    if (localComparator == null)
      localComparator = NATURAL_ORDER;
    return copyOfInternal(paramSortedMap, localComparator);
  }

  private ImmutableSet<Map.Entry<K, V>> createEntrySet()
  {
    if (isEmpty())
      return ImmutableSet.of();
    return new EntrySet(this);
  }

  private ImmutableSortedSet<K> createKeySet()
  {
    if (isEmpty())
      return ImmutableSortedSet.emptySet(this.comparator);
    return new RegularImmutableSortedSet(new TransformedImmutableList(this.entries)
    {
      K transform(Map.Entry<K, V> paramEntry)
      {
        return paramEntry.getKey();
      }
    }
    , this.comparator);
  }

  private ImmutableSortedMap<K, V> createSubmap(int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2)
      return new ImmutableSortedMap(this.entries.subList(paramInt1, paramInt2), this.comparator);
    return emptyMap(this.comparator);
  }

  private static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> paramComparator)
  {
    if (NATURAL_ORDER.equals(paramComparator))
      return NATURAL_EMPTY_MAP;
    return new ImmutableSortedMap(ImmutableList.of(), paramComparator);
  }

  private int findSubmapIndex(K paramK)
  {
    return SortedLists.binarySearch(Lists.transform(this.entries, this.keyFunction), paramK, this.comparator, SortedLists.Relation.CEILING, false);
  }

  public static <K extends Comparable<K>, V> Builder<K, V> naturalOrder()
  {
    return new Builder(Ordering.natural());
  }

  public static <K, V> ImmutableSortedMap<K, V> of()
  {
    return NATURAL_EMPTY_MAP;
  }

  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K paramK, V paramV)
  {
    return new ImmutableSortedMap(ImmutableList.of(entryOf(paramK, paramV)), Ordering.natural());
  }

  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2)
  {
    return new Builder(Ordering.natural()).put(paramK1, paramV1).put(paramK2, paramV2).build();
  }

  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3)
  {
    return new Builder(Ordering.natural()).put(paramK1, paramV1).put(paramK2, paramV2).put(paramK3, paramV3).build();
  }

  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3, K paramK4, V paramV4)
  {
    return new Builder(Ordering.natural()).put(paramK1, paramV1).put(paramK2, paramV2).put(paramK3, paramV3).put(paramK4, paramV4).build();
  }

  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3, K paramK4, V paramV4, K paramK5, V paramV5)
  {
    return new Builder(Ordering.natural()).put(paramK1, paramV1).put(paramK2, paramV2).put(paramK3, paramV3).put(paramK4, paramV4).put(paramK5, paramV5).build();
  }

  public static <K, V> Builder<K, V> orderedBy(Comparator<K> paramComparator)
  {
    return new Builder(paramComparator);
  }

  public static <K extends Comparable<K>, V> Builder<K, V> reverseOrder()
  {
    return new Builder(Ordering.natural().reverse());
  }

  private static <K, V> void sortEntries(List<Map.Entry<K, V>> paramList, Comparator<? super K> paramComparator)
  {
    Collections.sort(paramList, new Comparator(paramComparator)
    {
      public int compare(Map.Entry<K, V> paramEntry1, Map.Entry<K, V> paramEntry2)
      {
        return this.val$comparator.compare(paramEntry1.getKey(), paramEntry2.getKey());
      }
    });
  }

  private static <K, V> void validateEntries(List<Map.Entry<K, V>> paramList, Comparator<? super K> paramComparator)
  {
    for (int i = 1; i < paramList.size(); i++)
    {
      if (paramComparator.compare(((Map.Entry)paramList.get(i - 1)).getKey(), ((Map.Entry)paramList.get(i)).getKey()) != 0)
        continue;
      throw new IllegalArgumentException("Duplicate keys in mappings " + paramList.get(i - 1) + " and " + paramList.get(i));
    }
  }

  public Comparator<? super K> comparator()
  {
    return this.comparator;
  }

  public boolean containsValue(@Nullable Object paramObject)
  {
    if (paramObject == null)
      return false;
    return Iterators.contains(valueIterator(), paramObject);
  }

  public ImmutableSet<Map.Entry<K, V>> entrySet()
  {
    ImmutableSet localImmutableSet = this.entrySet;
    if (localImmutableSet == null)
    {
      localImmutableSet = createEntrySet();
      this.entrySet = localImmutableSet;
    }
    return localImmutableSet;
  }

  public K firstKey()
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return ((Map.Entry)this.entries.get(0)).getKey();
  }

  public V get(@Nullable Object paramObject)
  {
    if (paramObject == null);
    while (true)
    {
      return null;
      try
      {
        int i = SortedLists.binarySearch(Lists.transform(this.entries, this.keyFunction), paramObject, unsafeComparator(), SortedLists.Relation.EQUAL, false);
        if (i >= 0)
          return ((Map.Entry)this.entries.get(i)).getValue();
      }
      catch (ClassCastException localClassCastException)
      {
      }
    }
    return null;
  }

  public ImmutableSortedMap<K, V> headMap(K paramK)
  {
    return createSubmap(0, findSubmapIndex(Preconditions.checkNotNull(paramK)));
  }

  boolean isPartialView()
  {
    return this.entries.isPartialView();
  }

  public ImmutableSortedSet<K> keySet()
  {
    ImmutableSortedSet localImmutableSortedSet = this.keySet;
    if (localImmutableSortedSet == null)
    {
      localImmutableSortedSet = createKeySet();
      this.keySet = localImmutableSortedSet;
    }
    return localImmutableSortedSet;
  }

  public K lastKey()
  {
    if (isEmpty())
      throw new NoSuchElementException();
    return ((Map.Entry)this.entries.get(-1 + size())).getKey();
  }

  public int size()
  {
    return this.entries.size();
  }

  public ImmutableSortedMap<K, V> subMap(K paramK1, K paramK2)
  {
    Preconditions.checkNotNull(paramK1);
    Preconditions.checkNotNull(paramK2);
    if (this.comparator.compare(paramK1, paramK2) <= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return createSubmap(findSubmapIndex(paramK1), findSubmapIndex(paramK2));
    }
  }

  public ImmutableSortedMap<K, V> tailMap(K paramK)
  {
    return createSubmap(findSubmapIndex(Preconditions.checkNotNull(paramK)), size());
  }

  Comparator<Object> unsafeComparator()
  {
    return this.comparator;
  }

  UnmodifiableIterator<V> valueIterator()
  {
    return new UnmodifiableIterator(this.entries.iterator())
    {
      public boolean hasNext()
      {
        return this.val$entryIterator.hasNext();
      }

      public V next()
      {
        return ((Map.Entry)this.val$entryIterator.next()).getValue();
      }
    };
  }

  public ImmutableCollection<V> values()
  {
    Object localObject = this.values;
    if (localObject == null)
    {
      localObject = new Values(this);
      this.values = ((ImmutableCollection)localObject);
    }
    return (ImmutableCollection<V>)localObject;
  }

  Object writeReplace()
  {
    return new SerializedForm(this);
  }

  public static class Builder<K, V> extends ImmutableMap.Builder<K, V>
  {
    private final Comparator<? super K> comparator;

    public Builder(Comparator<? super K> paramComparator)
    {
      this.comparator = ((Comparator)Preconditions.checkNotNull(paramComparator));
    }

    public ImmutableSortedMap<K, V> build()
    {
      ImmutableSortedMap.access$000(this.entries, this.comparator);
      ImmutableSortedMap.access$100(this.entries, this.comparator);
      return new ImmutableSortedMap(ImmutableList.copyOf(this.entries), this.comparator);
    }

    public Builder<K, V> put(K paramK, V paramV)
    {
      this.entries.add(ImmutableMap.entryOf(paramK, paramV));
      return this;
    }

    public Builder<K, V> putAll(Map<? extends K, ? extends V> paramMap)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        put(localEntry.getKey(), localEntry.getValue());
      }
      return this;
    }
  }

  private static class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>>
  {
    final transient ImmutableSortedMap<K, V> map;

    EntrySet(ImmutableSortedMap<K, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    public boolean contains(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Map.Entry;
      int i = 0;
      if (bool1)
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        Object localObject = this.map.get(localEntry.getKey());
        i = 0;
        if (localObject != null)
        {
          boolean bool2 = localObject.equals(localEntry.getValue());
          i = 0;
          if (bool2)
            i = 1;
        }
      }
      return i;
    }

    boolean isPartialView()
    {
      return this.map.isPartialView();
    }

    public UnmodifiableIterator<Map.Entry<K, V>> iterator()
    {
      return this.map.entries.iterator();
    }

    public int size()
    {
      return this.map.size();
    }

    Object writeReplace()
    {
      return new ImmutableSortedMap.EntrySetSerializedForm(this.map);
    }
  }

  private static class EntrySetSerializedForm<K, V>
    implements Serializable
  {
    private static final long serialVersionUID;
    final ImmutableSortedMap<K, V> map;

    EntrySetSerializedForm(ImmutableSortedMap<K, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    Object readResolve()
    {
      return this.map.entrySet();
    }
  }

  private static class SerializedForm extends ImmutableMap.SerializedForm
  {
    private static final long serialVersionUID;
    private final Comparator<Object> comparator;

    SerializedForm(ImmutableSortedMap<?, ?> paramImmutableSortedMap)
    {
      super();
      this.comparator = paramImmutableSortedMap.comparator();
    }

    Object readResolve()
    {
      return createMap(new ImmutableSortedMap.Builder(this.comparator));
    }
  }

  private static class Values<V> extends ImmutableCollection<V>
  {
    private final ImmutableSortedMap<?, V> map;

    Values(ImmutableSortedMap<?, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    public boolean contains(Object paramObject)
    {
      return this.map.containsValue(paramObject);
    }

    boolean isPartialView()
    {
      return true;
    }

    public UnmodifiableIterator<V> iterator()
    {
      return this.map.valueIterator();
    }

    public int size()
    {
      return this.map.size();
    }

    Object writeReplace()
    {
      return new ImmutableSortedMap.ValuesSerializedForm(this.map);
    }
  }

  private static class ValuesSerializedForm<V>
    implements Serializable
  {
    private static final long serialVersionUID;
    final ImmutableSortedMap<?, V> map;

    ValuesSerializedForm(ImmutableSortedMap<?, V> paramImmutableSortedMap)
    {
      this.map = paramImmutableSortedMap;
    }

    Object readResolve()
    {
      return this.map.values();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSortedMap
 * JD-Core Version:    0.6.0
 */