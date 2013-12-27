package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class SortedMaps
{
  public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> paramSortedMap, Map<? extends K, ? extends V> paramMap)
  {
    Comparator localComparator = orNaturalOrder(paramSortedMap.comparator());
    TreeMap localTreeMap1 = Maps.newTreeMap(localComparator);
    TreeMap localTreeMap2 = Maps.newTreeMap(localComparator);
    localTreeMap2.putAll(paramMap);
    TreeMap localTreeMap3 = Maps.newTreeMap(localComparator);
    TreeMap localTreeMap4 = Maps.newTreeMap(localComparator);
    int i = 1;
    Iterator localIterator = paramSortedMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getKey();
      Object localObject2 = localEntry.getValue();
      if (paramMap.containsKey(localObject1))
      {
        Object localObject3 = localTreeMap2.remove(localObject1);
        if (Objects.equal(localObject2, localObject3))
        {
          localTreeMap3.put(localObject1, localObject2);
          continue;
        }
        localTreeMap4.put(localObject1, new Maps.ValueDifferenceImpl(localObject2, localObject3));
        i = 0;
        continue;
      }
      localTreeMap1.put(localObject1, localObject2);
      i = 0;
    }
    if ((i != 0) && (localTreeMap2.isEmpty()));
    for (boolean bool = true; ; bool = false)
      return sortedMapDifference(bool, localTreeMap1, localTreeMap2, localTreeMap3, localTreeMap4);
  }

  @GwtIncompatible("untested")
  public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> paramSortedMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    if ((paramSortedMap instanceof FilteredSortedMap))
      return filterFiltered((FilteredSortedMap)paramSortedMap, paramPredicate);
    return new FilteredSortedMap((SortedMap)Preconditions.checkNotNull(paramSortedMap), paramPredicate);
  }

  private static <K, V> SortedMap<K, V> filterFiltered(FilteredSortedMap<K, V> paramFilteredSortedMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    Predicate localPredicate = Predicates.and(paramFilteredSortedMap.predicate, paramPredicate);
    return new FilteredSortedMap(paramFilteredSortedMap.sortedMap(), localPredicate);
  }

  @GwtIncompatible("untested")
  public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> paramSortedMap, Predicate<? super K> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    return filterEntries(paramSortedMap, new Predicate(paramPredicate)
    {
      public boolean apply(Map.Entry<K, V> paramEntry)
      {
        return this.val$keyPredicate.apply(paramEntry.getKey());
      }
    });
  }

  @GwtIncompatible("untested")
  public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> paramSortedMap, Predicate<? super V> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    return filterEntries(paramSortedMap, new Predicate(paramPredicate)
    {
      public boolean apply(Map.Entry<K, V> paramEntry)
      {
        return this.val$valuePredicate.apply(paramEntry.getValue());
      }
    });
  }

  static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> paramComparator)
  {
    if (paramComparator != null)
      return paramComparator;
    return Ordering.natural();
  }

  private static <K, V> SortedMapDifference<K, V> sortedMapDifference(boolean paramBoolean, SortedMap<K, V> paramSortedMap1, SortedMap<K, V> paramSortedMap2, SortedMap<K, V> paramSortedMap3, SortedMap<K, MapDifference.ValueDifference<V>> paramSortedMap)
  {
    return new SortedMapDifferenceImpl(paramBoolean, Collections.unmodifiableSortedMap(paramSortedMap1), Collections.unmodifiableSortedMap(paramSortedMap2), Collections.unmodifiableSortedMap(paramSortedMap3), Collections.unmodifiableSortedMap(paramSortedMap));
  }

  public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> paramSortedMap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
  {
    return new TransformedEntriesSortedMap(paramSortedMap, paramEntryTransformer);
  }

  public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> paramSortedMap, Function<? super V1, V2> paramFunction)
  {
    Preconditions.checkNotNull(paramFunction);
    return transformEntries(paramSortedMap, new Maps.EntryTransformer(paramFunction)
    {
      public V2 transformEntry(K paramK, V1 paramV1)
      {
        return this.val$function.apply(paramV1);
      }
    });
  }

  private static class FilteredSortedMap<K, V> extends Maps.FilteredEntryMap<K, V>
    implements SortedMap<K, V>
  {
    FilteredSortedMap(SortedMap<K, V> paramSortedMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
    {
      super(paramPredicate);
    }

    public Comparator<? super K> comparator()
    {
      return sortedMap().comparator();
    }

    public K firstKey()
    {
      return keySet().iterator().next();
    }

    public SortedMap<K, V> headMap(K paramK)
    {
      return new FilteredSortedMap(sortedMap().headMap(paramK), this.predicate);
    }

    public K lastKey()
    {
      Object localObject;
      for (SortedMap localSortedMap = sortedMap(); ; localSortedMap = sortedMap().headMap(localObject))
      {
        localObject = localSortedMap.lastKey();
        if (apply(localObject, this.unfiltered.get(localObject)))
          return localObject;
      }
    }

    SortedMap<K, V> sortedMap()
    {
      return (SortedMap)this.unfiltered;
    }

    public SortedMap<K, V> subMap(K paramK1, K paramK2)
    {
      return new FilteredSortedMap(sortedMap().subMap(paramK1, paramK2), this.predicate);
    }

    public SortedMap<K, V> tailMap(K paramK)
    {
      return new FilteredSortedMap(sortedMap().tailMap(paramK), this.predicate);
    }
  }

  static class SortedMapDifferenceImpl<K, V> extends Maps.MapDifferenceImpl<K, V>
    implements SortedMapDifference<K, V>
  {
    SortedMapDifferenceImpl(boolean paramBoolean, SortedMap<K, V> paramSortedMap1, SortedMap<K, V> paramSortedMap2, SortedMap<K, V> paramSortedMap3, SortedMap<K, MapDifference.ValueDifference<V>> paramSortedMap)
    {
      super(paramSortedMap1, paramSortedMap2, paramSortedMap3, paramSortedMap);
    }

    public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering()
    {
      return (SortedMap)super.entriesDiffering();
    }

    public SortedMap<K, V> entriesInCommon()
    {
      return (SortedMap)super.entriesInCommon();
    }

    public SortedMap<K, V> entriesOnlyOnLeft()
    {
      return (SortedMap)super.entriesOnlyOnLeft();
    }

    public SortedMap<K, V> entriesOnlyOnRight()
    {
      return (SortedMap)super.entriesOnlyOnRight();
    }
  }

  static class TransformedEntriesSortedMap<K, V1, V2> extends Maps.TransformedEntriesMap<K, V1, V2>
    implements SortedMap<K, V2>
  {
    TransformedEntriesSortedMap(SortedMap<K, V1> paramSortedMap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
    {
      super(paramEntryTransformer);
    }

    public Comparator<? super K> comparator()
    {
      return fromMap().comparator();
    }

    public K firstKey()
    {
      return fromMap().firstKey();
    }

    protected SortedMap<K, V1> fromMap()
    {
      return (SortedMap)this.fromMap;
    }

    public SortedMap<K, V2> headMap(K paramK)
    {
      return SortedMaps.transformEntries(fromMap().headMap(paramK), this.transformer);
    }

    public K lastKey()
    {
      return fromMap().lastKey();
    }

    public SortedMap<K, V2> subMap(K paramK1, K paramK2)
    {
      return SortedMaps.transformEntries(fromMap().subMap(paramK1, paramK2), this.transformer);
    }

    public SortedMap<K, V2> tailMap(K paramK)
    {
      return SortedMaps.transformEntries(fromMap().tailMap(paramK), this.transformer);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedMaps
 * JD-Core Version:    0.6.0
 */