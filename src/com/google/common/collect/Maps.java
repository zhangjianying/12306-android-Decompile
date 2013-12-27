package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collection<TV;>;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Set<Ljava.util.Map.Entry<TK;TV;>;>;
import java.util.Set<TK;>;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Maps
{
  static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

  static int capacity(int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return Ints.saturatedCast(Math.max(2L * paramInt, 16L));
    }
  }

  static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> paramCollection, Object paramObject)
  {
    if (!(paramObject instanceof Map.Entry))
      return false;
    return paramCollection.contains(unmodifiableEntry((Map.Entry)paramObject));
  }

  static boolean containsKeyImpl(Map<?, ?> paramMap, @Nullable Object paramObject)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
      if (Objects.equal(((Map.Entry)localIterator.next()).getKey(), paramObject))
        return true;
    return false;
  }

  static boolean containsValueImpl(Map<?, ?> paramMap, @Nullable Object paramObject)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
      if (Objects.equal(((Map.Entry)localIterator.next()).getValue(), paramObject))
        return true;
    return false;
  }

  public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> paramMap1, Map<? extends K, ? extends V> paramMap2)
  {
    HashMap localHashMap1 = newHashMap();
    HashMap localHashMap2 = new HashMap(paramMap2);
    HashMap localHashMap3 = newHashMap();
    HashMap localHashMap4 = newHashMap();
    int i = 1;
    Iterator localIterator = paramMap1.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getKey();
      Object localObject2 = localEntry.getValue();
      if (paramMap2.containsKey(localObject1))
      {
        Object localObject3 = localHashMap2.remove(localObject1);
        if (Objects.equal(localObject2, localObject3))
        {
          localHashMap3.put(localObject1, localObject2);
          continue;
        }
        localHashMap4.put(localObject1, new ValueDifferenceImpl(localObject2, localObject3));
        i = 0;
        continue;
      }
      localHashMap1.put(localObject1, localObject2);
      i = 0;
    }
    if ((i != 0) && (localHashMap2.isEmpty()));
    for (boolean bool = true; ; bool = false)
      return mapDifference(bool, localHashMap1, localHashMap2, localHashMap3, localHashMap4);
  }

  static <K, V> Set<Map.Entry<K, V>> entrySetImpl(Map<K, V> paramMap, Supplier<Iterator<Map.Entry<K, V>>> paramSupplier)
  {
    return new EntrySetImpl(paramMap, paramSupplier);
  }

  static boolean equalsImpl(Map<?, ?> paramMap, Object paramObject)
  {
    if (paramMap == paramObject)
      return true;
    if ((paramObject instanceof Map))
    {
      Map localMap = (Map)paramObject;
      return paramMap.entrySet().equals(localMap.entrySet());
    }
    return false;
  }

  public static <K, V> Map<K, V> filterEntries(Map<K, V> paramMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    if ((paramMap instanceof AbstractFilteredMap))
      return filterFiltered((AbstractFilteredMap)paramMap, paramPredicate);
    return new FilteredEntryMap((Map)Preconditions.checkNotNull(paramMap), paramPredicate);
  }

  private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> paramAbstractFilteredMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
  {
    Predicate localPredicate = Predicates.and(paramAbstractFilteredMap.predicate, paramPredicate);
    return new FilteredEntryMap(paramAbstractFilteredMap.unfiltered, localPredicate);
  }

  public static <K, V> Map<K, V> filterKeys(Map<K, V> paramMap, Predicate<? super K> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    3 local3 = new Predicate(paramPredicate)
    {
      public boolean apply(Map.Entry<K, V> paramEntry)
      {
        return this.val$keyPredicate.apply(paramEntry.getKey());
      }
    };
    if ((paramMap instanceof AbstractFilteredMap))
      return filterFiltered((AbstractFilteredMap)paramMap, local3);
    return new FilteredKeyMap((Map)Preconditions.checkNotNull(paramMap), paramPredicate, local3);
  }

  public static <K, V> Map<K, V> filterValues(Map<K, V> paramMap, Predicate<? super V> paramPredicate)
  {
    Preconditions.checkNotNull(paramPredicate);
    return filterEntries(paramMap, new Predicate(paramPredicate)
    {
      public boolean apply(Map.Entry<K, V> paramEntry)
      {
        return this.val$valuePredicate.apply(paramEntry.getValue());
      }
    });
  }

  @GwtIncompatible("java.util.Properties")
  public static ImmutableMap<String, String> fromProperties(Properties paramProperties)
  {
    ImmutableMap.Builder localBuilder = ImmutableMap.builder();
    Enumeration localEnumeration = paramProperties.propertyNames();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      localBuilder.put(str, paramProperties.getProperty(str));
    }
    return localBuilder.build();
  }

  static int hashCodeImpl(Map<?, ?> paramMap)
  {
    return Sets.hashCodeImpl(paramMap.entrySet());
  }

  @GwtCompatible(serializable=true)
  public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K paramK, @Nullable V paramV)
  {
    return new ImmutableEntry(paramK, paramV);
  }

  static <K, V> Set<K> keySetImpl(Map<K, V> paramMap)
  {
    return new AbstractMapWrapper(paramMap).keySet();
  }

  private static <K, V> MapDifference<K, V> mapDifference(boolean paramBoolean, Map<K, V> paramMap1, Map<K, V> paramMap2, Map<K, V> paramMap3, Map<K, MapDifference.ValueDifference<V>> paramMap)
  {
    return new MapDifferenceImpl(paramBoolean, Collections.unmodifiableMap(paramMap1), Collections.unmodifiableMap(paramMap2), Collections.unmodifiableMap(paramMap3), Collections.unmodifiableMap(paramMap));
  }

  public static <K, V> ConcurrentMap<K, V> newConcurrentMap()
  {
    return new MapMaker().makeMap();
  }

  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> paramClass)
  {
    return new EnumMap((Class)Preconditions.checkNotNull(paramClass));
  }

  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> paramMap)
  {
    return new EnumMap(paramMap);
  }

  public static <K, V> HashMap<K, V> newHashMap()
  {
    return new HashMap();
  }

  public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> paramMap)
  {
    return new HashMap(paramMap);
  }

  public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int paramInt)
  {
    return new HashMap(capacity(paramInt));
  }

  public static <K, V> IdentityHashMap<K, V> newIdentityHashMap()
  {
    return new IdentityHashMap();
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap()
  {
    return new LinkedHashMap();
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> paramMap)
  {
    return new LinkedHashMap(paramMap);
  }

  public static <K extends Comparable, V> TreeMap<K, V> newTreeMap()
  {
    return new TreeMap();
  }

  public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> paramComparator)
  {
    return new TreeMap(paramComparator);
  }

  public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> paramSortedMap)
  {
    return new TreeMap(paramSortedMap);
  }

  static <K, V> void putAllImpl(Map<K, V> paramMap, Map<? extends K, ? extends V> paramMap1)
  {
    Iterator localIterator = paramMap1.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramMap.put(localEntry.getKey(), localEntry.getValue());
    }
  }

  static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> paramCollection, Object paramObject)
  {
    if (!(paramObject instanceof Map.Entry))
      return false;
    return paramCollection.remove(unmodifiableEntry((Map.Entry)paramObject));
  }

  static boolean safeContainsKey(Map<?, ?> paramMap, Object paramObject)
  {
    try
    {
      boolean bool = paramMap.containsKey(paramObject);
      return bool;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    return false;
  }

  static <V> V safeGet(Map<?, V> paramMap, Object paramObject)
  {
    try
    {
      Object localObject = paramMap.get(paramObject);
      return localObject;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    return null;
  }

  public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> paramBiMap)
  {
    return Synchronized.biMap(paramBiMap, null);
  }

  static String toStringImpl(Map<?, ?> paramMap)
  {
    StringBuilder localStringBuilder = Collections2.newStringBuilderForCollection(paramMap.size()).append('{');
    STANDARD_JOINER.appendTo(localStringBuilder, paramMap);
    return '}';
  }

  @Beta
  public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> paramMap, EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
  {
    return new TransformedEntriesMap(paramMap, paramEntryTransformer);
  }

  public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> paramMap, Function<? super V1, V2> paramFunction)
  {
    Preconditions.checkNotNull(paramFunction);
    return transformEntries(paramMap, new EntryTransformer(paramFunction)
    {
      public V2 transformEntry(K paramK, V1 paramV1)
      {
        return this.val$function.apply(paramV1);
      }
    });
  }

  public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> paramIterable, Function<? super V, K> paramFunction)
  {
    Preconditions.checkNotNull(paramFunction);
    ImmutableMap.Builder localBuilder = ImmutableMap.builder();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      localBuilder.put(paramFunction.apply(localObject), localObject);
    }
    return localBuilder.build();
  }

  public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> paramBiMap)
  {
    return new UnmodifiableBiMap(paramBiMap, null);
  }

  static <K, V> Map.Entry<K, V> unmodifiableEntry(Map.Entry<K, V> paramEntry)
  {
    Preconditions.checkNotNull(paramEntry);
    return new AbstractMapEntry(paramEntry)
    {
      public K getKey()
      {
        return this.val$entry.getKey();
      }

      public V getValue()
      {
        return this.val$entry.getValue();
      }
    };
  }

  static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> paramSet)
  {
    return new UnmodifiableEntrySet(Collections.unmodifiableSet(paramSet));
  }

  static <K, V> Collection<V> valuesImpl(Map<K, V> paramMap)
  {
    return new AbstractMapWrapper(paramMap).values();
  }

  private static abstract class AbstractFilteredMap<K, V> extends AbstractMap<K, V>
  {
    final Predicate<? super Map.Entry<K, V>> predicate;
    final Map<K, V> unfiltered;
    Collection<V> values;

    AbstractFilteredMap(Map<K, V> paramMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
    {
      this.unfiltered = paramMap;
      this.predicate = paramPredicate;
    }

    boolean apply(Object paramObject, V paramV)
    {
      return this.predicate.apply(Maps.immutableEntry(paramObject, paramV));
    }

    public boolean containsKey(Object paramObject)
    {
      return (this.unfiltered.containsKey(paramObject)) && (apply(paramObject, this.unfiltered.get(paramObject)));
    }

    public V get(Object paramObject)
    {
      Object localObject = this.unfiltered.get(paramObject);
      if ((localObject != null) && (apply(paramObject, localObject)))
        return localObject;
      return null;
    }

    public boolean isEmpty()
    {
      return entrySet().isEmpty();
    }

    public V put(K paramK, V paramV)
    {
      Preconditions.checkArgument(apply(paramK, paramV));
      return this.unfiltered.put(paramK, paramV);
    }

    public void putAll(Map<? extends K, ? extends V> paramMap)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Preconditions.checkArgument(apply(localEntry.getKey(), localEntry.getValue()));
      }
      this.unfiltered.putAll(paramMap);
    }

    public V remove(Object paramObject)
    {
      if (containsKey(paramObject))
        return this.unfiltered.remove(paramObject);
      return null;
    }

    public Collection<V> values()
    {
      Object localObject = this.values;
      if (localObject == null)
      {
        localObject = new Values();
        this.values = ((Collection)localObject);
      }
      return (Collection<V>)localObject;
    }

    class Values extends AbstractCollection<V>
    {
      Values()
      {
      }

      public void clear()
      {
        Maps.AbstractFilteredMap.this.entrySet().clear();
      }

      public boolean isEmpty()
      {
        return Maps.AbstractFilteredMap.this.entrySet().isEmpty();
      }

      public Iterator<V> iterator()
      {
        return new UnmodifiableIterator(Maps.AbstractFilteredMap.this.entrySet().iterator())
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

      public boolean remove(Object paramObject)
      {
        Iterator localIterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((!Objects.equal(paramObject, localEntry.getValue())) || (!Maps.AbstractFilteredMap.this.predicate.apply(localEntry)))
            continue;
          localIterator.remove();
          return true;
        }
        return false;
      }

      public boolean removeAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        int i = 0;
        Iterator localIterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((!paramCollection.contains(localEntry.getValue())) || (!Maps.AbstractFilteredMap.this.predicate.apply(localEntry)))
            continue;
          localIterator.remove();
          i = 1;
        }
        return i;
      }

      public boolean retainAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        int i = 0;
        Iterator localIterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((paramCollection.contains(localEntry.getValue())) || (!Maps.AbstractFilteredMap.this.predicate.apply(localEntry)))
            continue;
          localIterator.remove();
          i = 1;
        }
        return i;
      }

      public int size()
      {
        return Maps.AbstractFilteredMap.this.entrySet().size();
      }

      public Object[] toArray()
      {
        return Lists.newArrayList(iterator()).toArray();
      }

      public <T> T[] toArray(T[] paramArrayOfT)
      {
        return Lists.newArrayList(iterator()).toArray(paramArrayOfT);
      }
    }
  }

  private static final class AbstractMapWrapper<K, V> extends Maps.ImprovedAbstractMap<K, V>
  {
    private final Map<K, V> map;

    AbstractMapWrapper(Map<K, V> paramMap)
    {
      this.map = ((Map)Preconditions.checkNotNull(paramMap));
    }

    public void clear()
    {
      this.map.clear();
    }

    public boolean containsKey(Object paramObject)
    {
      return this.map.containsKey(paramObject);
    }

    public boolean containsValue(Object paramObject)
    {
      return this.map.containsValue(paramObject);
    }

    protected Set<Map.Entry<K, V>> createEntrySet()
    {
      return this.map.entrySet();
    }

    public boolean isEmpty()
    {
      return this.map.isEmpty();
    }

    public V remove(Object paramObject)
    {
      return this.map.remove(paramObject);
    }

    public int size()
    {
      return this.map.size();
    }
  }

  private static class EntrySetImpl<K, V> extends AbstractSet<Map.Entry<K, V>>
  {
    private final Supplier<Iterator<Map.Entry<K, V>>> entryIteratorSupplier;
    private final Map<K, V> map;

    EntrySetImpl(Map<K, V> paramMap, Supplier<Iterator<Map.Entry<K, V>>> paramSupplier)
    {
      this.map = ((Map)Preconditions.checkNotNull(paramMap));
      this.entryIteratorSupplier = ((Supplier)Preconditions.checkNotNull(paramSupplier));
    }

    public void clear()
    {
      this.map.clear();
    }

    public boolean contains(Object paramObject)
    {
      if ((paramObject instanceof Map.Entry))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        Object localObject = localEntry.getKey();
        if (this.map.containsKey(localObject))
          return Objects.equal(this.map.get(localEntry.getKey()), localEntry.getValue());
      }
      return false;
    }

    public int hashCode()
    {
      return this.map.hashCode();
    }

    public boolean isEmpty()
    {
      return this.map.isEmpty();
    }

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return (Iterator)this.entryIteratorSupplier.get();
    }

    public boolean remove(Object paramObject)
    {
      if (contains(paramObject))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        this.map.remove(localEntry.getKey());
        return true;
      }
      return false;
    }

    public int size()
    {
      return this.map.size();
    }
  }

  @Beta
  public static abstract interface EntryTransformer<K, V1, V2>
  {
    public abstract V2 transformEntry(@Nullable K paramK, @Nullable V1 paramV1);
  }

  static class FilteredEntryMap<K, V> extends Maps.AbstractFilteredMap<K, V>
  {
    Set<Map.Entry<K, V>> entrySet;
    final Set<Map.Entry<K, V>> filteredEntrySet = Sets.filter(paramMap.entrySet(), this.predicate);
    Set<K> keySet;

    FilteredEntryMap(Map<K, V> paramMap, Predicate<? super Map.Entry<K, V>> paramPredicate)
    {
      super(paramPredicate);
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
      Object localObject = this.entrySet;
      if (localObject == null)
      {
        localObject = new EntrySet(null);
        this.entrySet = ((Set)localObject);
      }
      return (Set<Map.Entry<K, V>>)localObject;
    }

    public Set<K> keySet()
    {
      Object localObject = this.keySet;
      if (localObject == null)
      {
        localObject = new KeySet(null);
        this.keySet = ((Set)localObject);
      }
      return (Set<K>)localObject;
    }

    private class EntrySet extends ForwardingSet<Map.Entry<K, V>>
    {
      private EntrySet()
      {
      }

      protected Set<Map.Entry<K, V>> delegate()
      {
        return Maps.FilteredEntryMap.this.filteredEntrySet;
      }

      public Iterator<Map.Entry<K, V>> iterator()
      {
        return new UnmodifiableIterator(Maps.FilteredEntryMap.this.filteredEntrySet.iterator())
        {
          public boolean hasNext()
          {
            return this.val$iterator.hasNext();
          }

          public Map.Entry<K, V> next()
          {
            return new ForwardingMapEntry((Map.Entry)this.val$iterator.next())
            {
              protected Map.Entry<K, V> delegate()
              {
                return this.val$entry;
              }

              public V setValue(V paramV)
              {
                Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(this.val$entry.getKey(), paramV));
                return super.setValue(paramV);
              }
            };
          }
        };
      }
    }

    private class KeySet extends AbstractSet<K>
    {
      private KeySet()
      {
      }

      public void clear()
      {
        Maps.FilteredEntryMap.this.filteredEntrySet.clear();
      }

      public boolean contains(Object paramObject)
      {
        return Maps.FilteredEntryMap.this.containsKey(paramObject);
      }

      public Iterator<K> iterator()
      {
        return new UnmodifiableIterator(Maps.FilteredEntryMap.this.filteredEntrySet.iterator())
        {
          public boolean hasNext()
          {
            return this.val$iterator.hasNext();
          }

          public K next()
          {
            return ((Map.Entry)this.val$iterator.next()).getKey();
          }
        };
      }

      public boolean remove(Object paramObject)
      {
        if (Maps.FilteredEntryMap.this.containsKey(paramObject))
        {
          Maps.FilteredEntryMap.this.unfiltered.remove(paramObject);
          return true;
        }
        return false;
      }

      public boolean removeAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        boolean bool = false;
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
          bool |= remove(localIterator.next());
        return bool;
      }

      public boolean retainAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        int i = 0;
        Iterator localIterator = Maps.FilteredEntryMap.this.unfiltered.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if ((paramCollection.contains(localEntry.getKey())) || (!Maps.FilteredEntryMap.this.predicate.apply(localEntry)))
            continue;
          localIterator.remove();
          i = 1;
        }
        return i;
      }

      public int size()
      {
        return Maps.FilteredEntryMap.this.filteredEntrySet.size();
      }

      public Object[] toArray()
      {
        return Lists.newArrayList(iterator()).toArray();
      }

      public <T> T[] toArray(T[] paramArrayOfT)
      {
        return Lists.newArrayList(iterator()).toArray(paramArrayOfT);
      }
    }
  }

  private static class FilteredKeyMap<K, V> extends Maps.AbstractFilteredMap<K, V>
  {
    Set<Map.Entry<K, V>> entrySet;
    Predicate<? super K> keyPredicate;
    Set<K> keySet;

    FilteredKeyMap(Map<K, V> paramMap, Predicate<? super K> paramPredicate, Predicate<Map.Entry<K, V>> paramPredicate1)
    {
      super(paramPredicate1);
      this.keyPredicate = paramPredicate;
    }

    public boolean containsKey(Object paramObject)
    {
      return (this.unfiltered.containsKey(paramObject)) && (this.keyPredicate.apply(paramObject));
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
      Set localSet = this.entrySet;
      if (localSet == null)
      {
        localSet = Sets.filter(this.unfiltered.entrySet(), this.predicate);
        this.entrySet = localSet;
      }
      return localSet;
    }

    public Set<K> keySet()
    {
      Set localSet = this.keySet;
      if (localSet == null)
      {
        localSet = Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
        this.keySet = localSet;
      }
      return localSet;
    }
  }

  @GwtCompatible
  static abstract class ImprovedAbstractMap<K, V> extends AbstractMap<K, V>
  {
    private Set<Map.Entry<K, V>> entrySet;
    private Set<K> keySet;
    private Collection<V> values;

    protected abstract Set<Map.Entry<K, V>> createEntrySet();

    public Set<Map.Entry<K, V>> entrySet()
    {
      Set localSet = this.entrySet;
      if (localSet == null)
      {
        localSet = createEntrySet();
        this.entrySet = localSet;
      }
      return localSet;
    }

    public boolean isEmpty()
    {
      return entrySet().isEmpty();
    }

    public Set<K> keySet()
    {
      Object localObject = this.keySet;
      if (localObject == null)
      {
        localObject = new ForwardingSet(super.keySet())
        {
          protected Set<K> delegate()
          {
            return this.val$delegate;
          }

          public boolean isEmpty()
          {
            return Maps.ImprovedAbstractMap.this.isEmpty();
          }

          public boolean remove(Object paramObject)
          {
            if (contains(paramObject))
            {
              Maps.ImprovedAbstractMap.this.remove(paramObject);
              return true;
            }
            return false;
          }
        };
        this.keySet = ((Set)localObject);
      }
      return (Set<K>)localObject;
    }

    public Collection<V> values()
    {
      Object localObject = this.values;
      if (localObject == null)
      {
        localObject = new ForwardingCollection(super.values())
        {
          protected Collection<V> delegate()
          {
            return this.val$delegate;
          }

          public boolean isEmpty()
          {
            return Maps.ImprovedAbstractMap.this.isEmpty();
          }
        };
        this.values = ((Collection)localObject);
      }
      return (Collection<V>)localObject;
    }
  }

  static class MapDifferenceImpl<K, V>
    implements MapDifference<K, V>
  {
    final boolean areEqual;
    final Map<K, MapDifference.ValueDifference<V>> differences;
    final Map<K, V> onBoth;
    final Map<K, V> onlyOnLeft;
    final Map<K, V> onlyOnRight;

    MapDifferenceImpl(boolean paramBoolean, Map<K, V> paramMap1, Map<K, V> paramMap2, Map<K, V> paramMap3, Map<K, MapDifference.ValueDifference<V>> paramMap)
    {
      this.areEqual = paramBoolean;
      this.onlyOnLeft = paramMap1;
      this.onlyOnRight = paramMap2;
      this.onBoth = paramMap3;
      this.differences = paramMap;
    }

    public boolean areEqual()
    {
      return this.areEqual;
    }

    public Map<K, MapDifference.ValueDifference<V>> entriesDiffering()
    {
      return this.differences;
    }

    public Map<K, V> entriesInCommon()
    {
      return this.onBoth;
    }

    public Map<K, V> entriesOnlyOnLeft()
    {
      return this.onlyOnLeft;
    }

    public Map<K, V> entriesOnlyOnRight()
    {
      return this.onlyOnRight;
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      while (true)
      {
        return true;
        if (!(paramObject instanceof MapDifference))
          break;
        MapDifference localMapDifference = (MapDifference)paramObject;
        if ((!entriesOnlyOnLeft().equals(localMapDifference.entriesOnlyOnLeft())) || (!entriesOnlyOnRight().equals(localMapDifference.entriesOnlyOnRight())) || (!entriesInCommon().equals(localMapDifference.entriesInCommon())) || (!entriesDiffering().equals(localMapDifference.entriesDiffering())))
          return false;
      }
      return false;
    }

    public int hashCode()
    {
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = entriesOnlyOnLeft();
      arrayOfObject[1] = entriesOnlyOnRight();
      arrayOfObject[2] = entriesInCommon();
      arrayOfObject[3] = entriesDiffering();
      return Objects.hashCode(arrayOfObject);
    }

    public String toString()
    {
      if (this.areEqual)
        return "equal";
      StringBuilder localStringBuilder = new StringBuilder("not equal");
      if (!this.onlyOnLeft.isEmpty())
        localStringBuilder.append(": only on left=").append(this.onlyOnLeft);
      if (!this.onlyOnRight.isEmpty())
        localStringBuilder.append(": only on right=").append(this.onlyOnRight);
      if (!this.differences.isEmpty())
        localStringBuilder.append(": value differences=").append(this.differences);
      return localStringBuilder.toString();
    }
  }

  static class TransformedEntriesMap<K, V1, V2> extends AbstractMap<K, V2>
  {
    TransformedEntriesMap<K, V1, V2>.EntrySet entrySet;
    final Map<K, V1> fromMap;
    final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;

    TransformedEntriesMap(Map<K, V1> paramMap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
    {
      this.fromMap = ((Map)Preconditions.checkNotNull(paramMap));
      this.transformer = ((Maps.EntryTransformer)Preconditions.checkNotNull(paramEntryTransformer));
    }

    public void clear()
    {
      this.fromMap.clear();
    }

    public boolean containsKey(Object paramObject)
    {
      return this.fromMap.containsKey(paramObject);
    }

    public Set<Map.Entry<K, V2>> entrySet()
    {
      EntrySet localEntrySet = this.entrySet;
      if (localEntrySet == null)
      {
        localEntrySet = new EntrySet();
        this.entrySet = localEntrySet;
      }
      return localEntrySet;
    }

    public V2 get(Object paramObject)
    {
      Object localObject = this.fromMap.get(paramObject);
      if ((localObject != null) || (this.fromMap.containsKey(paramObject)))
        return this.transformer.transformEntry(paramObject, localObject);
      return null;
    }

    public V2 remove(Object paramObject)
    {
      if (this.fromMap.containsKey(paramObject))
        return this.transformer.transformEntry(paramObject, this.fromMap.remove(paramObject));
      return null;
    }

    public int size()
    {
      return this.fromMap.size();
    }

    class EntrySet extends AbstractSet<Map.Entry<K, V2>>
    {
      EntrySet()
      {
      }

      public void clear()
      {
        Maps.TransformedEntriesMap.this.fromMap.clear();
      }

      public boolean contains(Object paramObject)
      {
        if (!(paramObject instanceof Map.Entry));
        Object localObject1;
        Object localObject2;
        do
        {
          return false;
          Map.Entry localEntry = (Map.Entry)paramObject;
          localObject1 = localEntry.getKey();
          localObject2 = localEntry.getValue();
          Object localObject3 = Maps.TransformedEntriesMap.this.get(localObject1);
          if (localObject3 != null)
            return localObject3.equals(localObject2);
        }
        while ((localObject2 != null) || (!Maps.TransformedEntriesMap.this.containsKey(localObject1)));
        return true;
      }

      public Iterator<Map.Entry<K, V2>> iterator()
      {
        return new Iterator(Maps.TransformedEntriesMap.this.fromMap.entrySet().iterator())
        {
          public boolean hasNext()
          {
            return this.val$mapIterator.hasNext();
          }

          public Map.Entry<K, V2> next()
          {
            return new AbstractMapEntry((Map.Entry)this.val$mapIterator.next())
            {
              public K getKey()
              {
                return this.val$entry.getKey();
              }

              public V2 getValue()
              {
                return Maps.TransformedEntriesMap.this.transformer.transformEntry(this.val$entry.getKey(), this.val$entry.getValue());
              }
            };
          }

          public void remove()
          {
            this.val$mapIterator.remove();
          }
        };
      }

      public boolean remove(Object paramObject)
      {
        if (contains(paramObject))
        {
          Object localObject = ((Map.Entry)paramObject).getKey();
          Maps.TransformedEntriesMap.this.fromMap.remove(localObject);
          return true;
        }
        return false;
      }

      public int size()
      {
        return Maps.TransformedEntriesMap.this.size();
      }
    }
  }

  private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V>
    implements BiMap<K, V>, Serializable
  {
    private static final long serialVersionUID;
    final BiMap<? extends K, ? extends V> delegate;
    transient BiMap<V, K> inverse;
    final Map<K, V> unmodifiableMap;
    transient Set<V> values;

    UnmodifiableBiMap(BiMap<? extends K, ? extends V> paramBiMap, @Nullable BiMap<V, K> paramBiMap1)
    {
      this.unmodifiableMap = Collections.unmodifiableMap(paramBiMap);
      this.delegate = paramBiMap;
      this.inverse = paramBiMap1;
    }

    protected Map<K, V> delegate()
    {
      return this.unmodifiableMap;
    }

    public V forcePut(K paramK, V paramV)
    {
      throw new UnsupportedOperationException();
    }

    public BiMap<V, K> inverse()
    {
      Object localObject = this.inverse;
      if (localObject == null)
      {
        localObject = new UnmodifiableBiMap(this.delegate.inverse(), this);
        this.inverse = ((BiMap)localObject);
      }
      return (BiMap<V, K>)localObject;
    }

    public Set<V> values()
    {
      Set localSet = this.values;
      if (localSet == null)
      {
        localSet = Collections.unmodifiableSet(this.delegate.values());
        this.values = localSet;
      }
      return localSet;
    }
  }

  static class UnmodifiableEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>>
  {
    private final Collection<Map.Entry<K, V>> entries;

    UnmodifiableEntries(Collection<Map.Entry<K, V>> paramCollection)
    {
      this.entries = paramCollection;
    }

    public boolean add(Map.Entry<K, V> paramEntry)
    {
      throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends Map.Entry<K, V>> paramCollection)
    {
      throw new UnsupportedOperationException();
    }

    public void clear()
    {
      throw new UnsupportedOperationException();
    }

    protected Collection<Map.Entry<K, V>> delegate()
    {
      return this.entries;
    }

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new ForwardingIterator(super.iterator())
      {
        protected Iterator<Map.Entry<K, V>> delegate()
        {
          return this.val$delegate;
        }

        public Map.Entry<K, V> next()
        {
          return Maps.unmodifiableEntry((Map.Entry)super.next());
        }

        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      };
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

    public Object[] toArray()
    {
      return standardToArray();
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      return standardToArray(paramArrayOfT);
    }
  }

  static class UnmodifiableEntrySet<K, V> extends Maps.UnmodifiableEntries<K, V>
    implements Set<Map.Entry<K, V>>
  {
    UnmodifiableEntrySet(Set<Map.Entry<K, V>> paramSet)
    {
      super();
    }

    public boolean equals(@Nullable Object paramObject)
    {
      return Sets.equalsImpl(this, paramObject);
    }

    public int hashCode()
    {
      return Sets.hashCodeImpl(this);
    }
  }

  static class ValueDifferenceImpl<V>
    implements MapDifference.ValueDifference<V>
  {
    private final V left;
    private final V right;

    ValueDifferenceImpl(@Nullable V paramV1, @Nullable V paramV2)
    {
      this.left = paramV1;
      this.right = paramV2;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool1 = paramObject instanceof MapDifference.ValueDifference;
      int i = 0;
      if (bool1)
      {
        MapDifference.ValueDifference localValueDifference = (MapDifference.ValueDifference)paramObject;
        boolean bool2 = Objects.equal(this.left, localValueDifference.leftValue());
        i = 0;
        if (bool2)
        {
          boolean bool3 = Objects.equal(this.right, localValueDifference.rightValue());
          i = 0;
          if (bool3)
            i = 1;
        }
      }
      return i;
    }

    public int hashCode()
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = this.left;
      arrayOfObject[1] = this.right;
      return Objects.hashCode(arrayOfObject);
    }

    public V leftValue()
    {
      return this.left;
    }

    public V rightValue()
    {
      return this.right;
    }

    public String toString()
    {
      return "(" + this.left + ", " + this.right + ")";
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Maps
 * JD-Core Version:    0.6.0
 */