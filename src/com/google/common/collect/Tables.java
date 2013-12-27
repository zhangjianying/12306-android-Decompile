package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class Tables
{
  public static <R, C, V> Table.Cell<R, C, V> immutableCell(@Nullable R paramR, @Nullable C paramC, @Nullable V paramV)
  {
    return new ImmutableCell(paramR, paramC, paramV);
  }

  public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> paramTable)
  {
    if ((paramTable instanceof TransposeTable))
      return ((TransposeTable)paramTable).original;
    return new TransposeTable(paramTable);
  }

  static abstract class AbstractCell<R, C, V>
    implements Table.Cell<R, C, V>
  {
    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      while (true)
      {
        return true;
        if (!(paramObject instanceof Table.Cell))
          break;
        Table.Cell localCell = (Table.Cell)paramObject;
        if ((!Objects.equal(getRowKey(), localCell.getRowKey())) || (!Objects.equal(getColumnKey(), localCell.getColumnKey())) || (!Objects.equal(getValue(), localCell.getValue())))
          return false;
      }
      return false;
    }

    public int hashCode()
    {
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = getRowKey();
      arrayOfObject[1] = getColumnKey();
      arrayOfObject[2] = getValue();
      return Objects.hashCode(arrayOfObject);
    }

    public String toString()
    {
      return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
    }
  }

  private static class ImmutableCell<R, C, V> extends Tables.AbstractCell<R, C, V>
    implements Serializable
  {
    private static final long serialVersionUID;
    final C columnKey;
    final R rowKey;
    final V value;

    ImmutableCell(@Nullable R paramR, @Nullable C paramC, @Nullable V paramV)
    {
      this.rowKey = paramR;
      this.columnKey = paramC;
      this.value = paramV;
    }

    public C getColumnKey()
    {
      return this.columnKey;
    }

    public R getRowKey()
    {
      return this.rowKey;
    }

    public V getValue()
    {
      return this.value;
    }
  }

  private static class TransposeTable<C, R, V>
    implements Table<C, R, V>
  {
    private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function()
    {
      public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> paramCell)
      {
        return Tables.immutableCell(paramCell.getColumnKey(), paramCell.getRowKey(), paramCell.getValue());
      }
    };
    TransposeTable<C, R, V>.CellSet cellSet;
    final Table<R, C, V> original;

    TransposeTable(Table<R, C, V> paramTable)
    {
      this.original = ((Table)Preconditions.checkNotNull(paramTable));
    }

    public Set<Table.Cell<C, R, V>> cellSet()
    {
      CellSet localCellSet = this.cellSet;
      if (localCellSet == null)
      {
        localCellSet = new CellSet();
        this.cellSet = localCellSet;
      }
      return localCellSet;
    }

    public void clear()
    {
      this.original.clear();
    }

    public Map<C, V> column(R paramR)
    {
      return this.original.row(paramR);
    }

    public Set<R> columnKeySet()
    {
      return this.original.rowKeySet();
    }

    public Map<R, Map<C, V>> columnMap()
    {
      return this.original.rowMap();
    }

    public boolean contains(@Nullable Object paramObject1, @Nullable Object paramObject2)
    {
      return this.original.contains(paramObject2, paramObject1);
    }

    public boolean containsColumn(@Nullable Object paramObject)
    {
      return this.original.containsRow(paramObject);
    }

    public boolean containsRow(@Nullable Object paramObject)
    {
      return this.original.containsColumn(paramObject);
    }

    public boolean containsValue(@Nullable Object paramObject)
    {
      return this.original.containsValue(paramObject);
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
      return this.original.get(paramObject2, paramObject1);
    }

    public int hashCode()
    {
      return cellSet().hashCode();
    }

    public boolean isEmpty()
    {
      return this.original.isEmpty();
    }

    public V put(C paramC, R paramR, V paramV)
    {
      return this.original.put(paramR, paramC, paramV);
    }

    public void putAll(Table<? extends C, ? extends R, ? extends V> paramTable)
    {
      this.original.putAll(Tables.transpose(paramTable));
    }

    public V remove(@Nullable Object paramObject1, @Nullable Object paramObject2)
    {
      return this.original.remove(paramObject2, paramObject1);
    }

    public Map<R, V> row(C paramC)
    {
      return this.original.column(paramC);
    }

    public Set<C> rowKeySet()
    {
      return this.original.columnKeySet();
    }

    public Map<C, Map<R, V>> rowMap()
    {
      return this.original.columnMap();
    }

    public int size()
    {
      return this.original.size();
    }

    public String toString()
    {
      return rowMap().toString();
    }

    public Collection<V> values()
    {
      return this.original.values();
    }

    class CellSet extends Collections2.TransformedCollection<Table.Cell<R, C, V>, Table.Cell<C, R, V>>
      implements Set<Table.Cell<C, R, V>>
    {
      CellSet()
      {
        super(Tables.TransposeTable.TRANSPOSE_CELL);
      }

      public boolean contains(Object paramObject)
      {
        if ((paramObject instanceof Table.Cell))
        {
          Table.Cell localCell = (Table.Cell)paramObject;
          return Tables.TransposeTable.this.original.cellSet().contains(Tables.immutableCell(localCell.getColumnKey(), localCell.getRowKey(), localCell.getValue()));
        }
        return false;
      }

      public boolean equals(Object paramObject)
      {
        int i;
        if (paramObject == this)
          i = 1;
        Set localSet;
        int j;
        int k;
        do
        {
          boolean bool;
          do
          {
            return i;
            bool = paramObject instanceof Set;
            i = 0;
          }
          while (!bool);
          localSet = (Set)paramObject;
          j = localSet.size();
          k = size();
          i = 0;
        }
        while (j != k);
        return containsAll(localSet);
      }

      public int hashCode()
      {
        return Sets.hashCodeImpl(this);
      }

      public boolean remove(Object paramObject)
      {
        if ((paramObject instanceof Table.Cell))
        {
          Table.Cell localCell = (Table.Cell)paramObject;
          return Tables.TransposeTable.this.original.cellSet().remove(Tables.immutableCell(localCell.getColumnKey(), localCell.getRowKey(), localCell.getValue()));
        }
        return false;
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Tables
 * JD-Core Version:    0.6.0
 */