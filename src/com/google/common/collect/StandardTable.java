package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Set<TC;>;
import javax.annotation.Nullable;

@GwtCompatible
class StandardTable<R, C, V>
  implements Table<R, C, V>, Serializable
{
  private static final long serialVersionUID;
  final Map<R, Map<C, V>> backingMap;
  private transient StandardTable<R, C, V>.CellSet cellSet;
  private transient Set<C> columnKeySet;
  private transient StandardTable<R, C, V>.ColumnMap columnMap;
  final Supplier<? extends Map<C, V>> factory;
  private transient StandardTable<R, C, V>.RowKeySet rowKeySet;
  private transient StandardTable<R, C, V>.RowMap rowMap;
  private transient StandardTable<R, C, V>.Values values;

  StandardTable(Map<R, Map<C, V>> paramMap, Supplier<? extends Map<C, V>> paramSupplier)
  {
    this.backingMap = paramMap;
    this.factory = paramSupplier;
  }

  private boolean containsMapping(Object paramObject1, Object paramObject2, Object paramObject3)
  {
    return (paramObject3 != null) && (paramObject3.equals(get(paramObject1, paramObject2)));
  }

  private Map<C, V> getOrCreate(R paramR)
  {
    Map localMap = (Map)this.backingMap.get(paramR);
    if (localMap == null)
    {
      localMap = (Map)this.factory.get();
      this.backingMap.put(paramR, localMap);
    }
    return localMap;
  }

  static <K, V> Iterator<K> keyIteratorImpl(Map<K, V> paramMap)
  {
    return new Iterator(paramMap.entrySet().iterator())
    {
      public boolean hasNext()
      {
        return this.val$entryIterator.hasNext();
      }

      public K next()
      {
        return ((Map.Entry)this.val$entryIterator.next()).getKey();
      }

      public void remove()
      {
        this.val$entryIterator.remove();
      }
    };
  }

  private Map<R, V> removeColumn(Object paramObject)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    Iterator localIterator = this.backingMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject = ((Map)localEntry.getValue()).remove(paramObject);
      if (localObject == null)
        continue;
      localLinkedHashMap.put(localEntry.getKey(), localObject);
      if (!((Map)localEntry.getValue()).isEmpty())
        continue;
      localIterator.remove();
    }
    return localLinkedHashMap;
  }

  private boolean removeMapping(Object paramObject1, Object paramObject2, Object paramObject3)
  {
    if (containsMapping(paramObject1, paramObject2, paramObject3))
    {
      remove(paramObject1, paramObject2);
      return true;
    }
    return false;
  }

  static <K, V> Iterator<V> valueIteratorImpl(Map<K, V> paramMap)
  {
    return new Iterator(paramMap.entrySet().iterator())
    {
      public boolean hasNext()
      {
        return this.val$entryIterator.hasNext();
      }

      public V next()
      {
        return ((Map.Entry)this.val$entryIterator.next()).getValue();
      }

      public void remove()
      {
        this.val$entryIterator.remove();
      }
    };
  }

  public Set<Table.Cell<R, C, V>> cellSet()
  {
    CellSet localCellSet = this.cellSet;
    if (localCellSet == null)
    {
      localCellSet = new CellSet(null);
      this.cellSet = localCellSet;
    }
    return localCellSet;
  }

  public void clear()
  {
    this.backingMap.clear();
  }

  public Map<R, V> column(C paramC)
  {
    return new Column(paramC);
  }

  public Set<C> columnKeySet()
  {
    Object localObject = this.columnKeySet;
    if (localObject == null)
    {
      localObject = new ColumnKeySet(null);
      this.columnKeySet = ((Set)localObject);
    }
    return (Set<C>)localObject;
  }

  public Map<C, Map<R, V>> columnMap()
  {
    ColumnMap localColumnMap = this.columnMap;
    if (localColumnMap == null)
    {
      localColumnMap = new ColumnMap(null);
      this.columnMap = localColumnMap;
    }
    return localColumnMap;
  }

  public boolean contains(@Nullable Object paramObject1, @Nullable Object paramObject2)
  {
    if ((paramObject1 == null) || (paramObject2 == null));
    Map localMap;
    do
    {
      return false;
      localMap = (Map)Maps.safeGet(this.backingMap, paramObject1);
    }
    while ((localMap == null) || (!Maps.safeContainsKey(localMap, paramObject2)));
    return true;
  }

  public boolean containsColumn(@Nullable Object paramObject)
  {
    if (paramObject == null);
    Iterator localIterator;
    do
      while (!localIterator.hasNext())
      {
        return false;
        localIterator = this.backingMap.values().iterator();
      }
    while (!Maps.safeContainsKey((Map)localIterator.next(), paramObject));
    return true;
  }

  public boolean containsRow(@Nullable Object paramObject)
  {
    return (paramObject != null) && (Maps.safeContainsKey(this.backingMap, paramObject));
  }

  public boolean containsValue(@Nullable Object paramObject)
  {
    if (paramObject == null);
    Iterator localIterator;
    do
      while (!localIterator.hasNext())
      {
        return false;
        localIterator = this.backingMap.values().iterator();
      }
    while (!((Map)localIterator.next()).containsValue(paramObject));
    return true;
  }

  Iterator<C> createColumnKeyIterator()
  {
    return new ColumnKeyIterator(null);
  }

  public boolean equals(@Nullable Object paramObject)
  {
    if (paramObject == this)
      return true;
    if ((paramObject instanceof Table))
    {
      Table localTable = (Table)paramObject;
      return cellSet().equals(localTable.cellSet());
    }
    return false;
  }

  public V get(@Nullable Object paramObject1, @Nullable Object paramObject2)
  {
    if ((paramObject1 == null) || (paramObject2 == null));
    Map localMap;
    do
    {
      return null;
      localMap = (Map)Maps.safeGet(this.backingMap, paramObject1);
    }
    while (localMap == null);
    return Maps.safeGet(localMap, paramObject2);
  }

  public int hashCode()
  {
    return cellSet().hashCode();
  }

  public boolean isEmpty()
  {
    return this.backingMap.isEmpty();
  }

  public V put(R paramR, C paramC, V paramV)
  {
    Preconditions.checkNotNull(paramR);
    Preconditions.checkNotNull(paramC);
    Preconditions.checkNotNull(paramV);
    return getOrCreate(paramR).put(paramC, paramV);
  }

  public void putAll(Table<? extends R, ? extends C, ? extends V> paramTable)
  {
    Iterator localIterator = paramTable.cellSet().iterator();
    while (localIterator.hasNext())
    {
      Table.Cell localCell = (Table.Cell)localIterator.next();
      put(localCell.getRowKey(), localCell.getColumnKey(), localCell.getValue());
    }
  }

  public V remove(@Nullable Object paramObject1, @Nullable Object paramObject2)
  {
    Object localObject = null;
    if (paramObject1 != null)
    {
      localObject = null;
      if (paramObject2 != null)
        break label14;
    }
    label14: Map localMap;
    do
    {
      do
      {
        return localObject;
        localMap = (Map)Maps.safeGet(this.backingMap, paramObject1);
        localObject = null;
      }
      while (localMap == null);
      localObject = localMap.remove(paramObject2);
    }
    while (!localMap.isEmpty());
    this.backingMap.remove(paramObject1);
    return localObject;
  }

  public Map<C, V> row(R paramR)
  {
    return new Row(paramR);
  }

  public Set<R> rowKeySet()
  {
    RowKeySet localRowKeySet = this.rowKeySet;
    if (localRowKeySet == null)
    {
      localRowKeySet = new RowKeySet();
      this.rowKeySet = localRowKeySet;
    }
    return localRowKeySet;
  }

  public Map<R, Map<C, V>> rowMap()
  {
    RowMap localRowMap = this.rowMap;
    if (localRowMap == null)
    {
      localRowMap = new RowMap();
      this.rowMap = localRowMap;
    }
    return localRowMap;
  }

  public int size()
  {
    int i = 0;
    Iterator localIterator = this.backingMap.values().iterator();
    while (localIterator.hasNext())
      i += ((Map)localIterator.next()).size();
    return i;
  }

  public String toString()
  {
    return rowMap().toString();
  }

  public Collection<V> values()
  {
    Values localValues = this.values;
    if (localValues == null)
    {
      localValues = new Values(null);
      this.values = localValues;
    }
    return localValues;
  }

  private class CellIterator
    implements Iterator<Table.Cell<R, C, V>>
  {
    Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
    Map.Entry<R, Map<C, V>> rowEntry;
    final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();

    private CellIterator()
    {
    }

    public boolean hasNext()
    {
      return (this.rowIterator.hasNext()) || (this.columnIterator.hasNext());
    }

    public Table.Cell<R, C, V> next()
    {
      if (!this.columnIterator.hasNext())
      {
        this.rowEntry = ((Map.Entry)this.rowIterator.next());
        this.columnIterator = ((Map)this.rowEntry.getValue()).entrySet().iterator();
      }
      Map.Entry localEntry = (Map.Entry)this.columnIterator.next();
      return Tables.immutableCell(this.rowEntry.getKey(), localEntry.getKey(), localEntry.getValue());
    }

    public void remove()
    {
      this.columnIterator.remove();
      if (((Map)this.rowEntry.getValue()).isEmpty())
        this.rowIterator.remove();
    }
  }

  private class CellSet extends StandardTable<R, C, V>.TableSet<Table.Cell<R, C, V>>
  {
    private CellSet()
    {
      super(null);
    }

    public boolean contains(Object paramObject)
    {
      if ((paramObject instanceof Table.Cell))
      {
        Table.Cell localCell = (Table.Cell)paramObject;
        return StandardTable.this.containsMapping(localCell.getRowKey(), localCell.getColumnKey(), localCell.getValue());
      }
      return false;
    }

    public Iterator<Table.Cell<R, C, V>> iterator()
    {
      return new StandardTable.CellIterator(StandardTable.this, null);
    }

    public boolean remove(Object paramObject)
    {
      if ((paramObject instanceof Table.Cell))
      {
        Table.Cell localCell = (Table.Cell)paramObject;
        return StandardTable.this.removeMapping(localCell.getRowKey(), localCell.getColumnKey(), localCell.getValue());
      }
      return false;
    }

    public int size()
    {
      return StandardTable.this.size();
    }
  }

  private class Column extends Maps.ImprovedAbstractMap<R, V>
  {
    final C columnKey;
    StandardTable<R, C, V>.Column.Values columnValues;
    StandardTable<R, C, V>.Column.KeySet keySet;

    Column()
    {
      Object localObject;
      this.columnKey = Preconditions.checkNotNull(localObject);
    }

    public boolean containsKey(Object paramObject)
    {
      return StandardTable.this.contains(paramObject, this.columnKey);
    }

    public Set<Map.Entry<R, V>> createEntrySet()
    {
      return new EntrySet();
    }

    public V get(Object paramObject)
    {
      return StandardTable.this.get(paramObject, this.columnKey);
    }

    public Set<R> keySet()
    {
      KeySet localKeySet = this.keySet;
      if (localKeySet == null)
      {
        localKeySet = new KeySet();
        this.keySet = localKeySet;
      }
      return localKeySet;
    }

    public V put(R paramR, V paramV)
    {
      return StandardTable.this.put(paramR, this.columnKey, paramV);
    }

    public V remove(Object paramObject)
    {
      return StandardTable.this.remove(paramObject, this.columnKey);
    }

    boolean removePredicate(Predicate<? super Map.Entry<R, V>> paramPredicate)
    {
      int i = 0;
      Iterator localIterator = StandardTable.this.backingMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Map localMap = (Map)localEntry.getValue();
        Object localObject = localMap.get(this.columnKey);
        if ((localObject == null) || (!paramPredicate.apply(new ImmutableEntry(localEntry.getKey(), localObject))))
          continue;
        localMap.remove(this.columnKey);
        i = 1;
        if (!localMap.isEmpty())
          continue;
        localIterator.remove();
      }
      return i;
    }

    public Collection<V> values()
    {
      Values localValues = this.columnValues;
      if (localValues == null)
      {
        localValues = new Values();
        this.columnValues = localValues;
      }
      return localValues;
    }

    class EntrySet extends AbstractSet<Map.Entry<R, V>>
    {
      EntrySet()
      {
      }

      public void clear()
      {
        Predicate localPredicate = Predicates.alwaysTrue();
        StandardTable.Column.this.removePredicate(localPredicate);
      }

      public boolean contains(Object paramObject)
      {
        if ((paramObject instanceof Map.Entry))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          return StandardTable.this.containsMapping(localEntry.getKey(), StandardTable.Column.this.columnKey, localEntry.getValue());
        }
        return false;
      }

      public boolean isEmpty()
      {
        return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
      }

      public Iterator<Map.Entry<R, V>> iterator()
      {
        return new StandardTable.Column.EntrySetIterator(StandardTable.Column.this);
      }

      public boolean remove(Object paramObject)
      {
        if ((paramObject instanceof Map.Entry))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          return StandardTable.this.removeMapping(localEntry.getKey(), StandardTable.Column.this.columnKey, localEntry.getValue());
        }
        return false;
      }

      public boolean removeAll(Collection<?> paramCollection)
      {
        boolean bool = false;
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
          bool |= remove(localIterator.next());
        return bool;
      }

      public boolean retainAll(Collection<?> paramCollection)
      {
        return StandardTable.Column.this.removePredicate(Predicates.not(Predicates.in(paramCollection)));
      }

      public int size()
      {
        int i = 0;
        Iterator localIterator = StandardTable.this.backingMap.values().iterator();
        while (localIterator.hasNext())
        {
          if (!((Map)localIterator.next()).containsKey(StandardTable.Column.this.columnKey))
            continue;
          i++;
        }
        return i;
      }
    }

    class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>>
    {
      final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();

      EntrySetIterator()
      {
      }

      protected Map.Entry<R, V> computeNext()
      {
        while (this.iterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)this.iterator.next();
          if (((Map)localEntry.getValue()).containsKey(StandardTable.Column.this.columnKey))
            return new AbstractMapEntry(localEntry)
            {
              public R getKey()
              {
                return this.val$entry.getKey();
              }

              public V getValue()
              {
                return ((Map)this.val$entry.getValue()).get(StandardTable.Column.this.columnKey);
              }

              public V setValue(V paramV)
              {
                return ((Map)this.val$entry.getValue()).put(StandardTable.Column.this.columnKey, Preconditions.checkNotNull(paramV));
              }
            };
        }
        return (Map.Entry)endOfData();
      }
    }

    class KeySet extends AbstractSet<R>
    {
      KeySet()
      {
      }

      public void clear()
      {
        StandardTable.Column.this.entrySet().clear();
      }

      public boolean contains(Object paramObject)
      {
        return StandardTable.this.contains(paramObject, StandardTable.Column.this.columnKey);
      }

      public boolean isEmpty()
      {
        return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
      }

      public Iterator<R> iterator()
      {
        return StandardTable.keyIteratorImpl(StandardTable.Column.this);
      }

      public boolean remove(Object paramObject)
      {
        return StandardTable.this.remove(paramObject, StandardTable.Column.this.columnKey) != null;
      }

      public boolean removeAll(Collection<?> paramCollection)
      {
        boolean bool = false;
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
          bool |= remove(localIterator.next());
        return bool;
      }

      public boolean retainAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        1 local1 = new Predicate(paramCollection)
        {
          public boolean apply(Map.Entry<R, V> paramEntry)
          {
            return !this.val$c.contains(paramEntry.getKey());
          }
        };
        return StandardTable.Column.this.removePredicate(local1);
      }

      public int size()
      {
        return StandardTable.Column.this.entrySet().size();
      }
    }

    class Values extends AbstractCollection<V>
    {
      Values()
      {
      }

      public void clear()
      {
        StandardTable.Column.this.entrySet().clear();
      }

      public boolean isEmpty()
      {
        return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
      }

      public Iterator<V> iterator()
      {
        return StandardTable.valueIteratorImpl(StandardTable.Column.this);
      }

      public boolean remove(Object paramObject)
      {
        if (paramObject == null);
        Iterator localIterator;
        Map localMap;
        do
        {
          while (!localIterator.hasNext())
          {
            return false;
            localIterator = StandardTable.this.backingMap.values().iterator();
          }
          localMap = (Map)localIterator.next();
        }
        while (!localMap.entrySet().remove(new ImmutableEntry(StandardTable.Column.this.columnKey, paramObject)));
        if (localMap.isEmpty())
          localIterator.remove();
        return true;
      }

      public boolean removeAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        1 local1 = new Predicate(paramCollection)
        {
          public boolean apply(Map.Entry<R, V> paramEntry)
          {
            return this.val$c.contains(paramEntry.getValue());
          }
        };
        return StandardTable.Column.this.removePredicate(local1);
      }

      public boolean retainAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        2 local2 = new Predicate(paramCollection)
        {
          public boolean apply(Map.Entry<R, V> paramEntry)
          {
            return !this.val$c.contains(paramEntry.getValue());
          }
        };
        return StandardTable.Column.this.removePredicate(local2);
      }

      public int size()
      {
        return StandardTable.Column.this.entrySet().size();
      }
    }
  }

  private class ColumnKeyIterator extends AbstractIterator<C>
  {
    Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
    final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
    final Map<C, V> seen = (Map)StandardTable.this.factory.get();

    private ColumnKeyIterator()
    {
    }

    protected C computeNext()
    {
      while (true)
      {
        if (this.entryIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)this.entryIterator.next();
          if (this.seen.containsKey(localEntry.getKey()))
            continue;
          this.seen.put(localEntry.getKey(), localEntry.getValue());
          return localEntry.getKey();
        }
        if (!this.mapIterator.hasNext())
          break;
        this.entryIterator = ((Map)this.mapIterator.next()).entrySet().iterator();
      }
      return endOfData();
    }
  }

  private class ColumnKeySet extends StandardTable<R, C, V>.TableSet<C>
  {
    private ColumnKeySet()
    {
      super(null);
    }

    public boolean contains(Object paramObject)
    {
      if (paramObject == null);
      Iterator localIterator;
      do
        while (!localIterator.hasNext())
        {
          return false;
          localIterator = StandardTable.this.backingMap.values().iterator();
        }
      while (!((Map)localIterator.next()).containsKey(paramObject));
      return true;
    }

    public Iterator<C> iterator()
    {
      return StandardTable.this.createColumnKeyIterator();
    }

    public boolean remove(Object paramObject)
    {
      int i;
      if (paramObject == null)
        i = 0;
      while (true)
      {
        return i;
        i = 0;
        Iterator localIterator = StandardTable.this.backingMap.values().iterator();
        while (localIterator.hasNext())
        {
          Map localMap = (Map)localIterator.next();
          if (!localMap.keySet().remove(paramObject))
            continue;
          i = 1;
          if (!localMap.isEmpty())
            continue;
          localIterator.remove();
        }
      }
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      Preconditions.checkNotNull(paramCollection);
      int i = 0;
      Iterator localIterator = StandardTable.this.backingMap.values().iterator();
      while (localIterator.hasNext())
      {
        Map localMap = (Map)localIterator.next();
        if (!Iterators.removeAll(localMap.keySet().iterator(), paramCollection))
          continue;
        i = 1;
        if (!localMap.isEmpty())
          continue;
        localIterator.remove();
      }
      return i;
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      Preconditions.checkNotNull(paramCollection);
      int i = 0;
      Iterator localIterator = StandardTable.this.backingMap.values().iterator();
      while (localIterator.hasNext())
      {
        Map localMap = (Map)localIterator.next();
        if (!localMap.keySet().retainAll(paramCollection))
          continue;
        i = 1;
        if (!localMap.isEmpty())
          continue;
        localIterator.remove();
      }
      return i;
    }

    public int size()
    {
      return Iterators.size(iterator());
    }
  }

  private class ColumnMap extends Maps.ImprovedAbstractMap<C, Map<R, V>>
  {
    StandardTable<R, C, V>.ColumnMap.ColumnMapValues columnMapValues;

    private ColumnMap()
    {
    }

    public boolean containsKey(Object paramObject)
    {
      return StandardTable.this.containsColumn(paramObject);
    }

    public Set<Map.Entry<C, Map<R, V>>> createEntrySet()
    {
      return new ColumnMapEntrySet();
    }

    public Map<R, V> get(Object paramObject)
    {
      if (StandardTable.this.containsColumn(paramObject))
        return StandardTable.this.column(paramObject);
      return null;
    }

    public Set<C> keySet()
    {
      return StandardTable.this.columnKeySet();
    }

    public Map<R, V> remove(Object paramObject)
    {
      if (StandardTable.this.containsColumn(paramObject))
        return StandardTable.this.removeColumn(paramObject);
      return null;
    }

    public Collection<Map<R, V>> values()
    {
      ColumnMapValues localColumnMapValues = this.columnMapValues;
      if (localColumnMapValues == null)
      {
        localColumnMapValues = new ColumnMapValues(null);
        this.columnMapValues = localColumnMapValues;
      }
      return localColumnMapValues;
    }

    class ColumnMapEntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>>
    {
      ColumnMapEntrySet()
      {
        super(null);
      }

      public boolean contains(Object paramObject)
      {
        if ((paramObject instanceof Map.Entry))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          if (StandardTable.this.containsColumn(localEntry.getKey()))
          {
            Object localObject = localEntry.getKey();
            return StandardTable.ColumnMap.this.get(localObject).equals(localEntry.getValue());
          }
        }
        return false;
      }

      public Iterator<Map.Entry<C, Map<R, V>>> iterator()
      {
        return new UnmodifiableIterator(StandardTable.this.columnKeySet().iterator())
        {
          public boolean hasNext()
          {
            return this.val$columnIterator.hasNext();
          }

          public Map.Entry<C, Map<R, V>> next()
          {
            Object localObject = this.val$columnIterator.next();
            return new ImmutableEntry(localObject, StandardTable.this.column(localObject));
          }
        };
      }

      public boolean remove(Object paramObject)
      {
        if (contains(paramObject))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          StandardTable.this.removeColumn(localEntry.getKey());
          return true;
        }
        return false;
      }

      public boolean removeAll(Collection<?> paramCollection)
      {
        boolean bool = false;
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
          bool |= remove(localIterator.next());
        return bool;
      }

      public boolean retainAll(Collection<?> paramCollection)
      {
        int i = 0;
        Iterator localIterator = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
        while (localIterator.hasNext())
        {
          Object localObject = localIterator.next();
          if (paramCollection.contains(new ImmutableEntry(localObject, StandardTable.this.column(localObject))))
            continue;
          StandardTable.this.removeColumn(localObject);
          i = 1;
        }
        return i;
      }

      public int size()
      {
        return StandardTable.this.columnKeySet().size();
      }
    }

    private class ColumnMapValues extends StandardTable<R, C, V>.TableCollection<Map<R, V>>
    {
      private ColumnMapValues()
      {
        super(null);
      }

      public Iterator<Map<R, V>> iterator()
      {
        return StandardTable.valueIteratorImpl(StandardTable.ColumnMap.this);
      }

      public boolean remove(Object paramObject)
      {
        Iterator localIterator = StandardTable.ColumnMap.this.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if (!((Map)localEntry.getValue()).equals(paramObject))
            continue;
          StandardTable.this.removeColumn(localEntry.getKey());
          return true;
        }
        return false;
      }

      public boolean removeAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        int i = 0;
        Iterator localIterator = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
        while (localIterator.hasNext())
        {
          Object localObject = localIterator.next();
          if (!paramCollection.contains(StandardTable.this.column(localObject)))
            continue;
          StandardTable.this.removeColumn(localObject);
          i = 1;
        }
        return i;
      }

      public boolean retainAll(Collection<?> paramCollection)
      {
        Preconditions.checkNotNull(paramCollection);
        int i = 0;
        Iterator localIterator = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
        while (localIterator.hasNext())
        {
          Object localObject = localIterator.next();
          if (paramCollection.contains(StandardTable.this.column(localObject)))
            continue;
          StandardTable.this.removeColumn(localObject);
          i = 1;
        }
        return i;
      }

      public int size()
      {
        return StandardTable.this.columnKeySet().size();
      }
    }
  }

  private class Row extends Maps.ImprovedAbstractMap<C, V>
  {
    final R rowKey;

    Row()
    {
      Object localObject;
      this.rowKey = Preconditions.checkNotNull(localObject);
    }

    public boolean containsKey(Object paramObject)
    {
      return StandardTable.this.contains(this.rowKey, paramObject);
    }

    protected Set<Map.Entry<C, V>> createEntrySet()
    {
      return new RowEntrySet(null);
    }

    public V get(Object paramObject)
    {
      return StandardTable.this.get(this.rowKey, paramObject);
    }

    public V put(C paramC, V paramV)
    {
      return StandardTable.this.put(this.rowKey, paramC, paramV);
    }

    public V remove(Object paramObject)
    {
      return StandardTable.this.remove(this.rowKey, paramObject);
    }

    private class RowEntrySet extends AbstractSet<Map.Entry<C, V>>
    {
      private RowEntrySet()
      {
      }

      public void clear()
      {
        StandardTable.this.backingMap.remove(StandardTable.Row.this.rowKey);
      }

      public boolean contains(Object paramObject)
      {
        if ((paramObject instanceof Map.Entry))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          return StandardTable.this.containsMapping(StandardTable.Row.this.rowKey, localEntry.getKey(), localEntry.getValue());
        }
        return false;
      }

      public Iterator<Map.Entry<C, V>> iterator()
      {
        Map localMap = (Map)StandardTable.this.backingMap.get(StandardTable.Row.this.rowKey);
        if (localMap == null)
          return Iterators.emptyModifiableIterator();
        return new Iterator(localMap.entrySet().iterator(), localMap)
        {
          public boolean hasNext()
          {
            return this.val$iterator.hasNext();
          }

          public Map.Entry<C, V> next()
          {
            return new ForwardingMapEntry((Map.Entry)this.val$iterator.next())
            {
              protected Map.Entry<C, V> delegate()
              {
                return this.val$entry;
              }

              public V setValue(V paramV)
              {
                return super.setValue(Preconditions.checkNotNull(paramV));
              }
            };
          }

          public void remove()
          {
            this.val$iterator.remove();
            if (this.val$map.isEmpty())
              StandardTable.this.backingMap.remove(StandardTable.Row.this.rowKey);
          }
        };
      }

      public boolean remove(Object paramObject)
      {
        if ((paramObject instanceof Map.Entry))
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          return StandardTable.this.removeMapping(StandardTable.Row.this.rowKey, localEntry.getKey(), localEntry.getValue());
        }
        return false;
      }

      public int size()
      {
        Map localMap = (Map)StandardTable.this.backingMap.get(StandardTable.Row.this.rowKey);
        if (localMap == null)
          return 0;
        return localMap.size();
      }
    }
  }

  class RowKeySet extends StandardTable<R, C, V>.TableSet<R>
  {
    RowKeySet()
    {
      super(null);
    }

    public boolean contains(Object paramObject)
    {
      return StandardTable.this.containsRow(paramObject);
    }

    public Iterator<R> iterator()
    {
      return StandardTable.keyIteratorImpl(StandardTable.this.rowMap());
    }

    public boolean remove(Object paramObject)
    {
      return (paramObject != null) && (StandardTable.this.backingMap.remove(paramObject) != null);
    }

    public int size()
    {
      return StandardTable.this.backingMap.size();
    }
  }

  class RowMap extends Maps.ImprovedAbstractMap<R, Map<C, V>>
  {
    RowMap()
    {
    }

    public boolean containsKey(Object paramObject)
    {
      return StandardTable.this.containsRow(paramObject);
    }

    protected Set<Map.Entry<R, Map<C, V>>> createEntrySet()
    {
      return new EntrySet();
    }

    public Map<C, V> get(Object paramObject)
    {
      if (StandardTable.this.containsRow(paramObject))
        return StandardTable.this.row(paramObject);
      return null;
    }

    public Set<R> keySet()
    {
      return StandardTable.this.rowKeySet();
    }

    public Map<C, V> remove(Object paramObject)
    {
      if (paramObject == null)
        return null;
      return (Map)StandardTable.this.backingMap.remove(paramObject);
    }

    class EntryIterator
      implements Iterator<Map.Entry<R, Map<C, V>>>
    {
      final Iterator<R> delegate = StandardTable.this.backingMap.keySet().iterator();

      EntryIterator()
      {
      }

      public boolean hasNext()
      {
        return this.delegate.hasNext();
      }

      public Map.Entry<R, Map<C, V>> next()
      {
        Object localObject = this.delegate.next();
        return new ImmutableEntry(localObject, StandardTable.this.row(localObject));
      }

      public void remove()
      {
        this.delegate.remove();
      }
    }

    class EntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>>
    {
      EntrySet()
      {
        super(null);
      }

      public boolean contains(Object paramObject)
      {
        boolean bool1 = paramObject instanceof Map.Entry;
        int i = 0;
        if (bool1)
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          Object localObject = localEntry.getKey();
          i = 0;
          if (localObject != null)
          {
            boolean bool2 = localEntry.getValue() instanceof Map;
            i = 0;
            if (bool2)
            {
              boolean bool3 = Collections2.safeContains(StandardTable.this.backingMap.entrySet(), localEntry);
              i = 0;
              if (bool3)
                i = 1;
            }
          }
        }
        return i;
      }

      public Iterator<Map.Entry<R, Map<C, V>>> iterator()
      {
        return new StandardTable.RowMap.EntryIterator(StandardTable.RowMap.this);
      }

      public boolean remove(Object paramObject)
      {
        boolean bool1 = paramObject instanceof Map.Entry;
        int i = 0;
        if (bool1)
        {
          Map.Entry localEntry = (Map.Entry)paramObject;
          Object localObject = localEntry.getKey();
          i = 0;
          if (localObject != null)
          {
            boolean bool2 = localEntry.getValue() instanceof Map;
            i = 0;
            if (bool2)
            {
              boolean bool3 = StandardTable.this.backingMap.entrySet().remove(localEntry);
              i = 0;
              if (bool3)
                i = 1;
            }
          }
        }
        return i;
      }

      public int size()
      {
        return StandardTable.this.backingMap.size();
      }
    }
  }

  private abstract class TableCollection<T> extends AbstractCollection<T>
  {
    private TableCollection()
    {
    }

    public void clear()
    {
      StandardTable.this.backingMap.clear();
    }

    public boolean isEmpty()
    {
      return StandardTable.this.backingMap.isEmpty();
    }
  }

  private abstract class TableSet<T> extends AbstractSet<T>
  {
    private TableSet()
    {
    }

    public void clear()
    {
      StandardTable.this.backingMap.clear();
    }

    public boolean isEmpty()
    {
      return StandardTable.this.backingMap.isEmpty();
    }
  }

  private class Values extends StandardTable<R, C, V>.TableCollection<V>
  {
    private Values()
    {
      super(null);
    }

    public Iterator<V> iterator()
    {
      return new Iterator(StandardTable.this.cellSet().iterator())
      {
        public boolean hasNext()
        {
          return this.val$cellIterator.hasNext();
        }

        public V next()
        {
          return ((Table.Cell)this.val$cellIterator.next()).getValue();
        }

        public void remove()
        {
          this.val$cellIterator.remove();
        }
      };
    }

    public int size()
    {
      return StandardTable.this.size();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.StandardTable
 * JD-Core Version:    0.6.0
 */