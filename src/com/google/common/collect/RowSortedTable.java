package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

@Beta
@GwtCompatible
public abstract interface RowSortedTable<R, C, V> extends Table<R, C, V>
{
  public abstract SortedSet<R> rowKeySet();

  public abstract SortedMap<R, Map<C, V>> rowMap();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RowSortedTable
 * JD-Core Version:    0.6.0
 */