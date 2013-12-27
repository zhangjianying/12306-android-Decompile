package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Beta
@GwtCompatible(emulated=true)
public abstract class GenericMapMaker<K0, V0>
{

  @GwtIncompatible("To be supported")
  MapEvictionListener<K0, V0> evictionListener;

  @GwtIncompatible("java.util.concurrent.ConcurrentHashMap concurrencyLevel")
  public abstract GenericMapMaker<K0, V0> concurrencyLevel(int paramInt);

  public abstract GenericMapMaker<K0, V0> expiration(long paramLong, TimeUnit paramTimeUnit);

  @Beta
  @GwtIncompatible("To be supported")
  public abstract GenericMapMaker<K0, V0> expireAfterAccess(long paramLong, TimeUnit paramTimeUnit);

  @Beta
  public abstract GenericMapMaker<K0, V0> expireAfterWrite(long paramLong, TimeUnit paramTimeUnit);

  public abstract GenericMapMaker<K0, V0> initialCapacity(int paramInt);

  public abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> paramFunction);

  public abstract <K extends K0, V extends V0> ConcurrentMap<K, V> makeMap();

  @Beta
  public abstract GenericMapMaker<K0, V0> maximumSize(int paramInt);

  @GwtIncompatible("java.lang.ref.SoftReference")
  public abstract GenericMapMaker<K0, V0> softKeys();

  @GwtIncompatible("java.lang.ref.SoftReference")
  public abstract GenericMapMaker<K0, V0> softValues();

  @GwtIncompatible("java.lang.ref.WeakReference")
  public abstract GenericMapMaker<K0, V0> weakKeys();

  @GwtIncompatible("java.lang.ref.WeakReference")
  public abstract GenericMapMaker<K0, V0> weakValues();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.GenericMapMaker
 * JD-Core Version:    0.6.0
 */