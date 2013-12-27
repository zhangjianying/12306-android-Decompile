package com.google.common.collect;

import com.google.common.annotations.Beta;
import javax.annotation.Nullable;

@Beta
public abstract interface MapEvictionListener<K, V>
{
  public abstract void onEviction(@Nullable K paramK, @Nullable V paramV);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapEvictionListener
 * JD-Core Version:    0.6.0
 */