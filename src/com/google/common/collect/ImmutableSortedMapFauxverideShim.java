package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class ImmutableSortedMapFauxverideShim<K, V> extends ImmutableMap<K, V>
{
  @Deprecated
  public static <K, V> ImmutableSortedMap.Builder<K, V> builder()
  {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  public static <K, V> ImmutableSortedMap<K, V> of(K paramK, V paramV)
  {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  public static <K, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2)
  {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  public static <K, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3)
  {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  public static <K, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3, K paramK4, V paramV4)
  {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  public static <K, V> ImmutableSortedMap<K, V> of(K paramK1, V paramV1, K paramK2, V paramV2, K paramK3, V paramV3, K paramK4, V paramV4, K paramK5, V paramV5)
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSortedMapFauxverideShim
 * JD-Core Version:    0.6.0
 */