package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class Multimaps
{
  public static <K, V> SetMultimap<K, V> forMap(Map<K, V> paramMap)
  {
    return new MapMultimap(paramMap);
  }

  public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> paramIterable, Function<? super V, K> paramFunction)
  {
    Preconditions.checkNotNull(paramFunction);
    ImmutableListMultimap.Builder localBuilder = ImmutableListMultimap.builder();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      Preconditions.checkNotNull(localObject, paramIterable);
      localBuilder.put(paramFunction.apply(localObject), localObject);
    }
    return localBuilder.build();
  }

  public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> paramMultimap, M paramM)
  {
    Preconditions.checkNotNull(paramM);
    Iterator localIterator = paramMultimap.entries().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramM.put(localEntry.getValue(), localEntry.getKey());
    }
    return paramM;
  }

  public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends List<V>> paramSupplier)
  {
    return new CustomListMultimap(paramMap, paramSupplier);
  }

  public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends Collection<V>> paramSupplier)
  {
    return new CustomMultimap(paramMap, paramSupplier);
  }

  public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends Set<V>> paramSupplier)
  {
    return new CustomSetMultimap(paramMap, paramSupplier);
  }

  public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends SortedSet<V>> paramSupplier)
  {
    return new CustomSortedSetMultimap(paramMap, paramSupplier);
  }

  public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> paramListMultimap)
  {
    return Synchronized.listMultimap(paramListMultimap, null);
  }

  public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> paramMultimap)
  {
    return Synchronized.multimap(paramMultimap, null);
  }

  public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> paramSetMultimap)
  {
    return Synchronized.setMultimap(paramSetMultimap, null);
  }

  public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> paramSortedSetMultimap)
  {
    return Synchronized.sortedSetMultimap(paramSortedSetMultimap, null);
  }

  @Beta
  @GwtIncompatible("untested")
  public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> paramListMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
  {
    return new TransformedEntriesListMultimap(paramListMultimap, paramEntryTransformer);
  }

  @Beta
  @GwtIncompatible("untested")
  public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> paramMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
  {
    return new TransformedEntriesMultimap(paramMultimap, paramEntryTransformer);
  }

  @Beta
  @GwtIncompatible("untested")
  public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> paramListMultimap, Function<? super V1, V2> paramFunction)
  {
    Preconditions.checkNotNull(paramFunction);
    return transformEntries(paramListMultimap, new Maps.EntryTransformer(paramFunction)
    {
      public V2 transformEntry(K paramK, V1 paramV1)
      {
        return this.val$function.apply(paramV1);
      }
    });
  }

  @Beta
  @GwtIncompatible("untested")
  public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> paramMultimap, Function<? super V1, V2> paramFunction)
  {
    Preconditions.checkNotNull(paramFunction);
    return transformEntries(paramMultimap, new Maps.EntryTransformer(paramFunction)
    {
      public V2 transformEntry(K paramK, V1 paramV1)
      {
        return this.val$function.apply(paramV1);
      }
    });
  }

  private static <K, V> Set<Map.Entry<K, Collection<V>>> unmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> paramSet)
  {
    return new UnmodifiableAsMapEntries(Collections.unmodifiableSet(paramSet));
  }

  private static <K, V> Map.Entry<K, Collection<V>> unmodifiableAsMapEntry(Map.Entry<K, Collection<V>> paramEntry)
  {
    Preconditions.checkNotNull(paramEntry);
    return new AbstractMapEntry(paramEntry)
    {
      public K getKey()
      {
        return this.val$entry.getKey();
      }

      public Collection<V> getValue()
      {
        return Multimaps.access$100((Collection)this.val$entry.getValue());
      }
    };
  }

  private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> paramCollection)
  {
    if ((paramCollection instanceof Set))
      return Maps.unmodifiableEntrySet((Set)paramCollection);
    return new Maps.UnmodifiableEntries(Collections.unmodifiableCollection(paramCollection));
  }

  public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> paramListMultimap)
  {
    return new UnmodifiableListMultimap(paramListMultimap);
  }

  public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> paramMultimap)
  {
    return new UnmodifiableMultimap(paramMultimap);
  }

  public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> paramSetMultimap)
  {
    return new UnmodifiableSetMultimap(paramSetMultimap);
  }

  public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> paramSortedSetMultimap)
  {
    return new UnmodifiableSortedSetMultimap(paramSortedSetMultimap);
  }

  private static <V> Collection<V> unmodifiableValueCollection(Collection<V> paramCollection)
  {
    if ((paramCollection instanceof SortedSet))
      return Collections.unmodifiableSortedSet((SortedSet)paramCollection);
    if ((paramCollection instanceof Set))
      return Collections.unmodifiableSet((Set)paramCollection);
    if ((paramCollection instanceof List))
      return Collections.unmodifiableList((List)paramCollection);
    return Collections.unmodifiableCollection(paramCollection);
  }

  private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V>
  {

    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID;
    transient Supplier<? extends List<V>> factory;

    CustomListMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends List<V>> paramSupplier)
    {
      super();
      this.factory = ((Supplier)Preconditions.checkNotNull(paramSupplier));
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      this.factory = ((Supplier)paramObjectInputStream.readObject());
      setMap((Map)paramObjectInputStream.readObject());
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      paramObjectOutputStream.writeObject(this.factory);
      paramObjectOutputStream.writeObject(backingMap());
    }

    protected List<V> createCollection()
    {
      return (List)this.factory.get();
    }
  }

  private static class CustomMultimap<K, V> extends AbstractMultimap<K, V>
  {

    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID;
    transient Supplier<? extends Collection<V>> factory;

    CustomMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends Collection<V>> paramSupplier)
    {
      super();
      this.factory = ((Supplier)Preconditions.checkNotNull(paramSupplier));
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      this.factory = ((Supplier)paramObjectInputStream.readObject());
      setMap((Map)paramObjectInputStream.readObject());
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      paramObjectOutputStream.writeObject(this.factory);
      paramObjectOutputStream.writeObject(backingMap());
    }

    protected Collection<V> createCollection()
    {
      return (Collection)this.factory.get();
    }
  }

  private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V>
  {

    @GwtIncompatible("not needed in emulated source")
    private static final long serialVersionUID;
    transient Supplier<? extends Set<V>> factory;

    CustomSetMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends Set<V>> paramSupplier)
    {
      super();
      this.factory = ((Supplier)Preconditions.checkNotNull(paramSupplier));
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      this.factory = ((Supplier)paramObjectInputStream.readObject());
      setMap((Map)paramObjectInputStream.readObject());
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      paramObjectOutputStream.writeObject(this.factory);
      paramObjectOutputStream.writeObject(backingMap());
    }

    protected Set<V> createCollection()
    {
      return (Set)this.factory.get();
    }
  }

  private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V>
  {

    @GwtIncompatible("not needed in emulated source")
    private static final long serialVersionUID;
    transient Supplier<? extends SortedSet<V>> factory;
    transient Comparator<? super V> valueComparator;

    CustomSortedSetMultimap(Map<K, Collection<V>> paramMap, Supplier<? extends SortedSet<V>> paramSupplier)
    {
      super();
      this.factory = ((Supplier)Preconditions.checkNotNull(paramSupplier));
      this.valueComparator = ((SortedSet)paramSupplier.get()).comparator();
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      this.factory = ((Supplier)paramObjectInputStream.readObject());
      this.valueComparator = ((SortedSet)this.factory.get()).comparator();
      setMap((Map)paramObjectInputStream.readObject());
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      paramObjectOutputStream.writeObject(this.factory);
      paramObjectOutputStream.writeObject(backingMap());
    }

    protected SortedSet<V> createCollection()
    {
      return (SortedSet)this.factory.get();
    }

    public Comparator<? super V> valueComparator()
    {
      return this.valueComparator;
    }
  }

  private static class MapMultimap<K, V>
    implements SetMultimap<K, V>, Serializable
  {
    private static final Joiner.MapJoiner JOINER = Joiner.on("], ").withKeyValueSeparator("=[").useForNull("null");
    private static final long serialVersionUID = 7845222491160860175L;
    transient Map<K, Collection<V>> asMap;
    final Map<K, V> map;

    MapMultimap(Map<K, V> paramMap)
    {
      this.map = ((Map)Preconditions.checkNotNull(paramMap));
    }

    public Map<K, Collection<V>> asMap()
    {
      Object localObject = this.asMap;
      if (localObject == null)
      {
        localObject = new AsMap();
        this.asMap = ((Map)localObject);
      }
      return (Map<K, Collection<V>>)localObject;
    }

    public void clear()
    {
      this.map.clear();
    }

    public boolean containsEntry(Object paramObject1, Object paramObject2)
    {
      return this.map.entrySet().contains(Maps.immutableEntry(paramObject1, paramObject2));
    }

    public boolean containsKey(Object paramObject)
    {
      return this.map.containsKey(paramObject);
    }

    public boolean containsValue(Object paramObject)
    {
      return this.map.containsValue(paramObject);
    }

    public Set<Map.Entry<K, V>> entries()
    {
      return this.map.entrySet();
    }

    public boolean equals(@Nullable Object paramObject)
    {
      if (paramObject == this);
      while (true)
      {
        return true;
        if (!(paramObject instanceof Multimap))
          break;
        Multimap localMultimap = (Multimap)paramObject;
        if ((size() != localMultimap.size()) || (!asMap().equals(localMultimap.asMap())))
          return false;
      }
      return false;
    }

    public Set<V> get(K paramK)
    {
      return new AbstractSet(paramK)
      {
        public Iterator<V> iterator()
        {
          return new Iterator()
          {
            int i;

            public boolean hasNext()
            {
              return (this.i == 0) && (Multimaps.MapMultimap.this.map.containsKey(Multimaps.MapMultimap.1.this.val$key));
            }

            public V next()
            {
              if (!hasNext())
                throw new NoSuchElementException();
              this.i = (1 + this.i);
              return Multimaps.MapMultimap.this.map.get(Multimaps.MapMultimap.1.this.val$key);
            }

            public void remove()
            {
              int j = 1;
              if (this.i == j);
              while (true)
              {
                Preconditions.checkState(j);
                this.i = -1;
                Multimaps.MapMultimap.this.map.remove(Multimaps.MapMultimap.1.this.val$key);
                return;
                int k = 0;
              }
            }
          };
        }

        public int size()
        {
          if (Multimaps.MapMultimap.this.map.containsKey(this.val$key))
            return 1;
          return 0;
        }
      };
    }

    public int hashCode()
    {
      return this.map.hashCode();
    }

    public boolean isEmpty()
    {
      return this.map.isEmpty();
    }

    public Set<K> keySet()
    {
      return this.map.keySet();
    }

    public Multiset<K> keys()
    {
      return Multisets.forSet(this.map.keySet());
    }

    public boolean put(K paramK, V paramV)
    {
      throw new UnsupportedOperationException();
    }

    public boolean putAll(Multimap<? extends K, ? extends V> paramMultimap)
    {
      throw new UnsupportedOperationException();
    }

    public boolean putAll(K paramK, Iterable<? extends V> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    public boolean remove(Object paramObject1, Object paramObject2)
    {
      return this.map.entrySet().remove(Maps.immutableEntry(paramObject1, paramObject2));
    }

    public Set<V> removeAll(Object paramObject)
    {
      HashSet localHashSet = new HashSet(2);
      if (!this.map.containsKey(paramObject))
        return localHashSet;
      localHashSet.add(this.map.remove(paramObject));
      return localHashSet;
    }

    public Set<V> replaceValues(K paramK, Iterable<? extends V> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    public int size()
    {
      return this.map.size();
    }

    public String toString()
    {
      if (this.map.isEmpty())
        return "{}";
      StringBuilder localStringBuilder = Collections2.newStringBuilderForCollection(this.map.size()).append('{');
      JOINER.appendTo(localStringBuilder, this.map);
      return "]}";
    }

    public Collection<V> values()
    {
      return this.map.values();
    }

    class AsMap extends Maps.ImprovedAbstractMap<K, Collection<V>>
    {
      AsMap()
      {
      }

      public boolean containsKey(Object paramObject)
      {
        return Multimaps.MapMultimap.this.map.containsKey(paramObject);
      }

      protected Set<Map.Entry<K, Collection<V>>> createEntrySet()
      {
        return new Multimaps.MapMultimap.AsMapEntries(Multimaps.MapMultimap.this);
      }

      public Collection<V> get(Object paramObject)
      {
        Set localSet = Multimaps.MapMultimap.this.get(paramObject);
        if (localSet.isEmpty())
          localSet = null;
        return localSet;
      }

      public Collection<V> remove(Object paramObject)
      {
        Set localSet = Multimaps.MapMultimap.this.removeAll(paramObject);
        if (localSet.isEmpty())
          localSet = null;
        return localSet;
      }
    }

    class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>>
    {
      AsMapEntries()
      {
      }

      public boolean contains(Object paramObject)
      {
        int i = 1;
        if (!(paramObject instanceof Map.Entry));
        Map.Entry localEntry;
        do
        {
          return false;
          localEntry = (Map.Entry)paramObject;
        }
        while (!(localEntry.getValue() instanceof Set));
        Set localSet = (Set)localEntry.getValue();
        if ((localSet.size() == i) && (Multimaps.MapMultimap.this.containsEntry(localEntry.getKey(), localSet.iterator().next())));
        while (true)
        {
          return i;
          i = 0;
        }
      }

      public Iterator<Map.Entry<K, Collection<V>>> iterator()
      {
        return new Iterator()
        {
          final Iterator<K> keys = Multimaps.MapMultimap.this.map.keySet().iterator();

          public boolean hasNext()
          {
            return this.keys.hasNext();
          }

          public Map.Entry<K, Collection<V>> next()
          {
            return new AbstractMapEntry(this.keys.next())
            {
              public K getKey()
              {
                return this.val$key;
              }

              public Collection<V> getValue()
              {
                return Multimaps.MapMultimap.this.get(this.val$key);
              }
            };
          }

          public void remove()
          {
            this.keys.remove();
          }
        };
      }

      public boolean remove(Object paramObject)
      {
        int i = 1;
        if (!(paramObject instanceof Map.Entry));
        Map.Entry localEntry;
        do
        {
          return false;
          localEntry = (Map.Entry)paramObject;
        }
        while (!(localEntry.getValue() instanceof Set));
        Set localSet = (Set)localEntry.getValue();
        if ((localSet.size() == i) && (Multimaps.MapMultimap.this.map.entrySet().remove(Maps.immutableEntry(localEntry.getKey(), localSet.iterator().next()))));
        while (true)
        {
          return i;
          i = 0;
        }
      }

      public int size()
      {
        return Multimaps.MapMultimap.this.map.size();
      }
    }
  }

  @GwtIncompatible("untested")
  private static final class TransformedEntriesListMultimap<K, V1, V2> extends Multimaps.TransformedEntriesMultimap<K, V1, V2>
    implements ListMultimap<K, V2>
  {
    TransformedEntriesListMultimap(ListMultimap<K, V1> paramListMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
    {
      super(paramEntryTransformer);
    }

    public List<V2> get(K paramK)
    {
      return transform(paramK, this.fromMultimap.get(paramK));
    }

    public List<V2> removeAll(Object paramObject)
    {
      return transform(paramObject, this.fromMultimap.removeAll(paramObject));
    }

    public List<V2> replaceValues(K paramK, Iterable<? extends V2> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    List<V2> transform(K paramK, Collection<V1> paramCollection)
    {
      return Lists.transform((List)paramCollection, new Function(paramK)
      {
        public V2 apply(V1 paramV1)
        {
          return Multimaps.TransformedEntriesListMultimap.this.transformer.transformEntry(this.val$key, paramV1);
        }
      });
    }
  }

  @GwtIncompatible("untested")
  private static class TransformedEntriesMultimap<K, V1, V2>
    implements Multimap<K, V2>
  {
    private transient Map<K, Collection<V2>> asMap;
    private transient Collection<Map.Entry<K, V2>> entries;
    final Multimap<K, V1> fromMultimap;
    final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
    private transient Collection<V2> values;

    TransformedEntriesMultimap(Multimap<K, V1> paramMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> paramEntryTransformer)
    {
      this.fromMultimap = ((Multimap)Preconditions.checkNotNull(paramMultimap));
      this.transformer = ((Maps.EntryTransformer)Preconditions.checkNotNull(paramEntryTransformer));
    }

    public Map<K, Collection<V2>> asMap()
    {
      if (this.asMap == null)
      {
        Map localMap = Maps.transformEntries(this.fromMultimap.asMap(), new Maps.EntryTransformer()
        {
          public Collection<V2> transformEntry(K paramK, Collection<V1> paramCollection)
          {
            return Multimaps.TransformedEntriesMultimap.this.transform(paramK, paramCollection);
          }
        });
        this.asMap = localMap;
        return localMap;
      }
      return this.asMap;
    }

    public void clear()
    {
      this.fromMultimap.clear();
    }

    public boolean containsEntry(Object paramObject1, Object paramObject2)
    {
      return get(paramObject1).contains(paramObject2);
    }

    public boolean containsKey(Object paramObject)
    {
      return this.fromMultimap.containsKey(paramObject);
    }

    public boolean containsValue(Object paramObject)
    {
      return values().contains(paramObject);
    }

    public Collection<Map.Entry<K, V2>> entries()
    {
      if (this.entries == null)
      {
        TransformedEntries localTransformedEntries = new TransformedEntries(this.transformer);
        this.entries = localTransformedEntries;
        return localTransformedEntries;
      }
      return this.entries;
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof Multimap))
      {
        Multimap localMultimap = (Multimap)paramObject;
        return asMap().equals(localMultimap.asMap());
      }
      return false;
    }

    public Collection<V2> get(K paramK)
    {
      return transform(paramK, this.fromMultimap.get(paramK));
    }

    public int hashCode()
    {
      return asMap().hashCode();
    }

    public boolean isEmpty()
    {
      return this.fromMultimap.isEmpty();
    }

    public Set<K> keySet()
    {
      return this.fromMultimap.keySet();
    }

    public Multiset<K> keys()
    {
      return this.fromMultimap.keys();
    }

    public boolean put(K paramK, V2 paramV2)
    {
      throw new UnsupportedOperationException();
    }

    public boolean putAll(Multimap<? extends K, ? extends V2> paramMultimap)
    {
      throw new UnsupportedOperationException();
    }

    public boolean putAll(K paramK, Iterable<? extends V2> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    public boolean remove(Object paramObject1, Object paramObject2)
    {
      return get(paramObject1).remove(paramObject2);
    }

    public Collection<V2> removeAll(Object paramObject)
    {
      return transform(paramObject, this.fromMultimap.removeAll(paramObject));
    }

    public Collection<V2> replaceValues(K paramK, Iterable<? extends V2> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    public int size()
    {
      return this.fromMultimap.size();
    }

    public String toString()
    {
      return asMap().toString();
    }

    Collection<V2> transform(K paramK, Collection<V1> paramCollection)
    {
      return Collections2.transform(paramCollection, new Function(paramK)
      {
        public V2 apply(V1 paramV1)
        {
          return Multimaps.TransformedEntriesMultimap.this.transformer.transformEntry(this.val$key, paramV1);
        }
      });
    }

    public Collection<V2> values()
    {
      if (this.values == null)
      {
        Collection localCollection = Collections2.transform(this.fromMultimap.entries(), new Function()
        {
          public V2 apply(Map.Entry<K, V1> paramEntry)
          {
            return Multimaps.TransformedEntriesMultimap.this.transformer.transformEntry(paramEntry.getKey(), paramEntry.getValue());
          }
        });
        this.values = localCollection;
        return localCollection;
      }
      return this.values;
    }

    private class TransformedEntries extends Collections2.TransformedCollection<Map.Entry<K, V1>, Map.Entry<K, V2>>
    {
      TransformedEntries()
      {
        super(new Function(localEntryTransformer)
        {
          public Map.Entry<K, V2> apply(Map.Entry<K, V1> paramEntry)
          {
            return new AbstractMapEntry(paramEntry)
            {
              public K getKey()
              {
                return this.val$entry.getKey();
              }

              public V2 getValue()
              {
                return Multimaps.TransformedEntriesMultimap.TransformedEntries.1.this.val$transformer.transformEntry(this.val$entry.getKey(), this.val$entry.getValue());
              }
            };
          }
        });
      }

      public boolean contains(Object paramObject)
      {
        if ((paramObject instanceof Map.Entry))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          return Multimaps.TransformedEntriesMultimap.this.containsEntry(localEntry.getKey(), localEntry.getValue());
        }
        return false;
      }

      public boolean remove(Object paramObject)
      {
        if ((paramObject instanceof Map.Entry))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          return Multimaps.TransformedEntriesMultimap.this.get(localEntry.getKey()).remove(localEntry.getValue());
        }
        return false;
      }
    }
  }

  static class UnmodifiableAsMapEntries<K, V> extends ForwardingSet<Map.Entry<K, Collection<V>>>
  {
    private final Set<Map.Entry<K, Collection<V>>> delegate;

    UnmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> paramSet)
    {
      this.delegate = paramSet;
    }

    public boolean contains(Object paramObject)
    {
      return Maps.containsEntryImpl(delegate(), paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      return standardContainsAll(paramCollection);
    }

    protected Set<Map.Entry<K, Collection<V>>> delegate()
    {
      return this.delegate;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      return standardEquals(paramObject);
    }

    public Iterator<Map.Entry<K, Collection<V>>> iterator()
    {
      return new ForwardingIterator(this.delegate.iterator())
      {
        protected Iterator<Map.Entry<K, Collection<V>>> delegate()
        {
          return this.val$iterator;
        }

        public Map.Entry<K, Collection<V>> next()
        {
          return Multimaps.access$300((Map.Entry)this.val$iterator.next());
        }
      };
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

  private static class UnmodifiableAsMapValues<V> extends ForwardingCollection<Collection<V>>
  {
    final Collection<Collection<V>> delegate;

    UnmodifiableAsMapValues(Collection<Collection<V>> paramCollection)
    {
      this.delegate = Collections.unmodifiableCollection(paramCollection);
    }

    public boolean contains(Object paramObject)
    {
      return standardContains(paramObject);
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      return standardContainsAll(paramCollection);
    }

    protected Collection<Collection<V>> delegate()
    {
      return this.delegate;
    }

    public Iterator<Collection<V>> iterator()
    {
      return new Iterator(this.delegate.iterator())
      {
        public boolean hasNext()
        {
          return this.val$iterator.hasNext();
        }

        public Collection<V> next()
        {
          return Multimaps.access$100((Collection)this.val$iterator.next());
        }

        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      };
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

  private static class UnmodifiableListMultimap<K, V> extends Multimaps.UnmodifiableMultimap<K, V>
    implements ListMultimap<K, V>
  {
    private static final long serialVersionUID;

    UnmodifiableListMultimap(ListMultimap<K, V> paramListMultimap)
    {
      super();
    }

    public ListMultimap<K, V> delegate()
    {
      return (ListMultimap)super.delegate();
    }

    public List<V> get(K paramK)
    {
      return Collections.unmodifiableList(delegate().get(paramK));
    }

    public List<V> removeAll(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public List<V> replaceValues(K paramK, Iterable<? extends V> paramIterable)
    {
      throw new UnsupportedOperationException();
    }
  }

  private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID;
    final Multimap<K, V> delegate;
    transient Collection<Map.Entry<K, V>> entries;
    transient Set<K> keySet;
    transient Multiset<K> keys;
    transient Map<K, Collection<V>> map;
    transient Collection<V> values;

    UnmodifiableMultimap(Multimap<K, V> paramMultimap)
    {
      this.delegate = ((Multimap)Preconditions.checkNotNull(paramMultimap));
    }

    public Map<K, Collection<V>> asMap()
    {
      Object localObject = this.map;
      if (localObject == null)
      {
        localObject = new ForwardingMap(Collections.unmodifiableMap(this.delegate.asMap()))
        {
          Collection<Collection<V>> asMapValues;
          Set<Map.Entry<K, Collection<V>>> entrySet;

          public boolean containsValue(Object paramObject)
          {
            return values().contains(paramObject);
          }

          protected Map<K, Collection<V>> delegate()
          {
            return this.val$unmodifiableMap;
          }

          public Set<Map.Entry<K, Collection<V>>> entrySet()
          {
            Set localSet = this.entrySet;
            if (localSet == null)
            {
              localSet = Multimaps.access$000(this.val$unmodifiableMap.entrySet());
              this.entrySet = localSet;
            }
            return localSet;
          }

          public Collection<V> get(Object paramObject)
          {
            Collection localCollection = (Collection)this.val$unmodifiableMap.get(paramObject);
            if (localCollection == null)
              return null;
            return Multimaps.access$100(localCollection);
          }

          public Collection<Collection<V>> values()
          {
            Object localObject = this.asMapValues;
            if (localObject == null)
            {
              localObject = new Multimaps.UnmodifiableAsMapValues(this.val$unmodifiableMap.values());
              this.asMapValues = ((Collection)localObject);
            }
            return (Collection<Collection<V>>)localObject;
          }
        };
        this.map = ((Map)localObject);
      }
      return (Map<K, Collection<V>>)localObject;
    }

    public void clear()
    {
      throw new UnsupportedOperationException();
    }

    protected Multimap<K, V> delegate()
    {
      return this.delegate;
    }

    public Collection<Map.Entry<K, V>> entries()
    {
      Collection localCollection = this.entries;
      if (localCollection == null)
      {
        localCollection = Multimaps.access$200(this.delegate.entries());
        this.entries = localCollection;
      }
      return localCollection;
    }

    public Collection<V> get(K paramK)
    {
      return Multimaps.access$100(this.delegate.get(paramK));
    }

    public Set<K> keySet()
    {
      Set localSet = this.keySet;
      if (localSet == null)
      {
        localSet = Collections.unmodifiableSet(this.delegate.keySet());
        this.keySet = localSet;
      }
      return localSet;
    }

    public Multiset<K> keys()
    {
      Multiset localMultiset = this.keys;
      if (localMultiset == null)
      {
        localMultiset = Multisets.unmodifiableMultiset(this.delegate.keys());
        this.keys = localMultiset;
      }
      return localMultiset;
    }

    public boolean put(K paramK, V paramV)
    {
      throw new UnsupportedOperationException();
    }

    public boolean putAll(Multimap<? extends K, ? extends V> paramMultimap)
    {
      throw new UnsupportedOperationException();
    }

    public boolean putAll(K paramK, Iterable<? extends V> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    public boolean remove(Object paramObject1, Object paramObject2)
    {
      throw new UnsupportedOperationException();
    }

    public Collection<V> removeAll(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public Collection<V> replaceValues(K paramK, Iterable<? extends V> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    public Collection<V> values()
    {
      Collection localCollection = this.values;
      if (localCollection == null)
      {
        localCollection = Collections.unmodifiableCollection(this.delegate.values());
        this.values = localCollection;
      }
      return localCollection;
    }
  }

  private static class UnmodifiableSetMultimap<K, V> extends Multimaps.UnmodifiableMultimap<K, V>
    implements SetMultimap<K, V>
  {
    private static final long serialVersionUID;

    UnmodifiableSetMultimap(SetMultimap<K, V> paramSetMultimap)
    {
      super();
    }

    public SetMultimap<K, V> delegate()
    {
      return (SetMultimap)super.delegate();
    }

    public Set<Map.Entry<K, V>> entries()
    {
      return Maps.unmodifiableEntrySet(delegate().entries());
    }

    public Set<V> get(K paramK)
    {
      return Collections.unmodifiableSet(delegate().get(paramK));
    }

    public Set<V> removeAll(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public Set<V> replaceValues(K paramK, Iterable<? extends V> paramIterable)
    {
      throw new UnsupportedOperationException();
    }
  }

  private static class UnmodifiableSortedSetMultimap<K, V> extends Multimaps.UnmodifiableSetMultimap<K, V>
    implements SortedSetMultimap<K, V>
  {
    private static final long serialVersionUID;

    UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> paramSortedSetMultimap)
    {
      super();
    }

    public SortedSetMultimap<K, V> delegate()
    {
      return (SortedSetMultimap)super.delegate();
    }

    public SortedSet<V> get(K paramK)
    {
      return Collections.unmodifiableSortedSet(delegate().get(paramK));
    }

    public SortedSet<V> removeAll(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }

    public SortedSet<V> replaceValues(K paramK, Iterable<? extends V> paramIterable)
    {
      throw new UnsupportedOperationException();
    }

    public Comparator<? super V> valueComparator()
    {
      return delegate().valueComparator();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multimaps
 * JD-Core Version:    0.6.0
 */