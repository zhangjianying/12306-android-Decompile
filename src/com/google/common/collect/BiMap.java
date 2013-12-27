package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface BiMap<K, V> extends Map<K, V>
{
  public abstract V forcePut(@Nullable K paramK, @Nullable V paramV);

  public abstract BiMap<V, K> inverse();

  public abstract V put(@Nullable K paramK, @Nullable V paramV);

  public abstract void putAll(Map<? extends K, ? extends V> paramMap);

  public abstract Set<V> values();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.BiMap
 * JD-Core Version:    0.6.0
 */