package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@GwtCompatible(emulated=true)
public final class MapMaker extends GenericMapMaker<Object, Object>
{
  static final Executor DEFAULT_CLEANUP_EXECUTOR = new Executor()
  {
    public void execute(Runnable paramRunnable)
    {
      paramRunnable.run();
    }
  };
  private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
  private static final int DEFAULT_EXPIRATION_NANOS = 0;
  private static final int DEFAULT_INITIAL_CAPACITY = 16;
  static final Ticker DEFAULT_TICKER = new Ticker()
  {
    public long read()
    {
      return System.nanoTime();
    }
  };
  static final int UNSET_INT = -1;
  Executor cleanupExecutor;
  int concurrencyLevel = -1;
  long expireAfterAccessNanos = -1L;
  long expireAfterWriteNanos = -1L;
  int initialCapacity = -1;
  Equivalence<Object> keyEquivalence;
  CustomConcurrentHashMap.Strength keyStrength;
  int maximumSize = -1;
  Ticker ticker;
  boolean useCustomMap;
  boolean useNullMap;
  Equivalence<Object> valueEquivalence;
  CustomConcurrentHashMap.Strength valueStrength;

  private void checkExpiration(long paramLong, TimeUnit paramTimeUnit)
  {
    boolean bool1;
    boolean bool2;
    if (this.expireAfterWriteNanos == -1L)
    {
      bool1 = true;
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Long.valueOf(this.expireAfterWriteNanos);
      Preconditions.checkState(bool1, "expireAfterWrite was already set to %s ns", arrayOfObject1);
      if (this.expireAfterAccessNanos != -1L)
        break label124;
      bool2 = true;
      label54: Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Long.valueOf(this.expireAfterAccessNanos);
      Preconditions.checkState(bool2, "expireAfterAccess was already set to %s ns", arrayOfObject2);
      if (paramLong < 0L)
        break label130;
    }
    label130: for (boolean bool3 = true; ; bool3 = false)
    {
      Object[] arrayOfObject3 = new Object[2];
      arrayOfObject3[0] = Long.valueOf(paramLong);
      arrayOfObject3[1] = paramTimeUnit;
      Preconditions.checkArgument(bool3, "duration cannot be negative: %s %s", arrayOfObject3);
      return;
      bool1 = false;
      break;
      label124: bool2 = false;
      break label54;
    }
  }

  @GwtIncompatible("java.util.concurrent.ConcurrentHashMap concurrencyLevel")
  public MapMaker concurrencyLevel(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if (this.concurrencyLevel == -1)
    {
      bool2 = bool1;
      Object[] arrayOfObject = new Object[bool1];
      arrayOfObject[0] = Integer.valueOf(this.concurrencyLevel);
      Preconditions.checkState(bool2, "concurrency level was already set to %s", arrayOfObject);
      if (paramInt <= 0)
        break label57;
    }
    while (true)
    {
      Preconditions.checkArgument(bool1);
      this.concurrencyLevel = paramInt;
      return this;
      bool2 = false;
      break;
      label57: bool1 = false;
    }
  }

  @Beta
  @GwtIncompatible("To be supported")
  public <K, V> GenericMapMaker<K, V> evictionListener(MapEvictionListener<K, V> paramMapEvictionListener)
  {
    if (this.evictionListener == null);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkState(bool);
      this.evictionListener = ((MapEvictionListener)Preconditions.checkNotNull(paramMapEvictionListener));
      this.useCustomMap = true;
      return this;
    }
  }

  @Deprecated
  public MapMaker expiration(long paramLong, TimeUnit paramTimeUnit)
  {
    return expireAfterWrite(paramLong, paramTimeUnit);
  }

  @Beta
  @GwtIncompatible("To be supported")
  public MapMaker expireAfterAccess(long paramLong, TimeUnit paramTimeUnit)
  {
    checkExpiration(paramLong, paramTimeUnit);
    this.expireAfterAccessNanos = paramTimeUnit.toNanos(paramLong);
    boolean bool1 = this.useNullMap;
    if (paramLong == 0L);
    for (boolean bool2 = true; ; bool2 = false)
    {
      this.useNullMap = (bool2 | bool1);
      this.useCustomMap = true;
      return this;
    }
  }

  @Beta
  public MapMaker expireAfterWrite(long paramLong, TimeUnit paramTimeUnit)
  {
    checkExpiration(paramLong, paramTimeUnit);
    this.expireAfterWriteNanos = paramTimeUnit.toNanos(paramLong);
    boolean bool1 = this.useNullMap;
    if (paramLong == 0L);
    for (boolean bool2 = true; ; bool2 = false)
    {
      this.useNullMap = (bool2 | bool1);
      this.useCustomMap = true;
      return this;
    }
  }

  Executor getCleanupExecutor()
  {
    return (Executor)Objects.firstNonNull(this.cleanupExecutor, DEFAULT_CLEANUP_EXECUTOR);
  }

  int getConcurrencyLevel()
  {
    if (this.concurrencyLevel == -1)
      return 4;
    return this.concurrencyLevel;
  }

  <K, V> MapEvictionListener<K, V> getEvictionListener()
  {
    if (this.evictionListener == null)
      return NullListener.INSTANCE;
    return this.evictionListener;
  }

  long getExpireAfterAccessNanos()
  {
    if (this.expireAfterAccessNanos == -1L)
      return 0L;
    return this.expireAfterAccessNanos;
  }

  long getExpireAfterWriteNanos()
  {
    if (this.expireAfterWriteNanos == -1L)
      return 0L;
    return this.expireAfterWriteNanos;
  }

  int getInitialCapacity()
  {
    if (this.initialCapacity == -1)
      return 16;
    return this.initialCapacity;
  }

  Equivalence<Object> getKeyEquivalence()
  {
    return (Equivalence)Objects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
  }

  CustomConcurrentHashMap.Strength getKeyStrength()
  {
    return (CustomConcurrentHashMap.Strength)Objects.firstNonNull(this.keyStrength, CustomConcurrentHashMap.Strength.STRONG);
  }

  Ticker getTicker()
  {
    return (Ticker)Objects.firstNonNull(this.ticker, DEFAULT_TICKER);
  }

  Equivalence<Object> getValueEquivalence()
  {
    return (Equivalence)Objects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
  }

  CustomConcurrentHashMap.Strength getValueStrength()
  {
    return (CustomConcurrentHashMap.Strength)Objects.firstNonNull(this.valueStrength, CustomConcurrentHashMap.Strength.STRONG);
  }

  public MapMaker initialCapacity(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if (this.initialCapacity == -1)
    {
      bool2 = bool1;
      Object[] arrayOfObject = new Object[bool1];
      arrayOfObject[0] = Integer.valueOf(this.initialCapacity);
      Preconditions.checkState(bool2, "initial capacity was already set to %s", arrayOfObject);
      if (paramInt < 0)
        break label57;
    }
    while (true)
    {
      Preconditions.checkArgument(bool1);
      this.initialCapacity = paramInt;
      return this;
      bool2 = false;
      break;
      label57: bool1 = false;
    }
  }

  <K, V> Cache<K, V> makeCache(Function<? super K, ? extends V> paramFunction)
  {
    if (this.useNullMap)
      return new NullComputingConcurrentMap(this, paramFunction);
    return new ComputingConcurrentHashMap(this, paramFunction);
  }

  public <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> paramFunction)
  {
    return new ComputingMapAdapter(makeCache(paramFunction));
  }

  public <K, V> ConcurrentMap<K, V> makeMap()
  {
    if (!this.useCustomMap)
      return new ConcurrentHashMap(getInitialCapacity(), 0.75F, getConcurrencyLevel());
    if (this.useNullMap)
      return new NullConcurrentMap(this);
    return new CustomConcurrentHashMap(this);
  }

  @Beta
  public MapMaker maximumSize(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    boolean bool3;
    label44: boolean bool4;
    if (this.maximumSize == -1)
    {
      bool2 = bool1;
      Object[] arrayOfObject = new Object[bool1];
      arrayOfObject[0] = Integer.valueOf(this.maximumSize);
      Preconditions.checkState(bool2, "maximum size was already set to %s", arrayOfObject);
      if (paramInt < 0)
        break label90;
      bool3 = bool1;
      Preconditions.checkArgument(bool3, "maximum size must not be negative");
      this.maximumSize = paramInt;
      this.useCustomMap = bool1;
      bool4 = this.useNullMap;
      if (this.maximumSize != 0)
        break label96;
    }
    while (true)
    {
      this.useNullMap = (bool4 | bool1);
      return this;
      bool2 = false;
      break;
      label90: bool3 = false;
      break label44;
      label96: bool1 = false;
    }
  }

  MapMaker privateKeyEquivalence(Equivalence<Object> paramEquivalence)
  {
    if (this.keyEquivalence == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.keyEquivalence;
      Preconditions.checkState(bool, "key equivalence was already set to %s", arrayOfObject);
      this.keyEquivalence = ((Equivalence)Preconditions.checkNotNull(paramEquivalence));
      this.useCustomMap = true;
      return this;
    }
  }

  MapMaker privateValueEquivalence(Equivalence<Object> paramEquivalence)
  {
    if (this.valueEquivalence == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.valueEquivalence;
      Preconditions.checkState(bool, "value equivalence was already set to %s", arrayOfObject);
      this.valueEquivalence = ((Equivalence)Preconditions.checkNotNull(paramEquivalence));
      this.useCustomMap = true;
      return this;
    }
  }

  MapMaker setKeyStrength(CustomConcurrentHashMap.Strength paramStrength)
  {
    if (this.keyStrength == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.keyStrength;
      Preconditions.checkState(bool, "Key strength was already set to %s", arrayOfObject);
      this.keyStrength = ((CustomConcurrentHashMap.Strength)Preconditions.checkNotNull(paramStrength));
      if (paramStrength != CustomConcurrentHashMap.Strength.STRONG)
        this.useCustomMap = true;
      return this;
    }
  }

  MapMaker setValueStrength(CustomConcurrentHashMap.Strength paramStrength)
  {
    if (this.valueStrength == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.valueStrength;
      Preconditions.checkState(bool, "Value strength was already set to %s", arrayOfObject);
      this.valueStrength = ((CustomConcurrentHashMap.Strength)Preconditions.checkNotNull(paramStrength));
      if (paramStrength != CustomConcurrentHashMap.Strength.STRONG)
        this.useCustomMap = true;
      return this;
    }
  }

  @GwtIncompatible("java.lang.ref.SoftReference")
  public MapMaker softKeys()
  {
    return setKeyStrength(CustomConcurrentHashMap.Strength.SOFT);
  }

  @GwtIncompatible("java.lang.ref.SoftReference")
  public MapMaker softValues()
  {
    return setValueStrength(CustomConcurrentHashMap.Strength.SOFT);
  }

  public String toString()
  {
    Objects.ToStringHelper localToStringHelper = Objects.toStringHelper(this);
    if (this.initialCapacity != -1)
      localToStringHelper.add("initialCapacity", Integer.valueOf(this.initialCapacity));
    if (this.concurrencyLevel != -1)
      localToStringHelper.add("concurrencyLevel", Integer.valueOf(this.concurrencyLevel));
    if (this.maximumSize != -1)
      localToStringHelper.add("maximumSize", Integer.valueOf(this.maximumSize));
    if (this.expireAfterWriteNanos != -1L)
      localToStringHelper.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
    if (this.expireAfterAccessNanos != -1L)
      localToStringHelper.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
    if (this.keyStrength != null)
      localToStringHelper.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
    if (this.valueStrength != null)
      localToStringHelper.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
    if (this.keyEquivalence != null)
      localToStringHelper.addValue("keyEquivalence");
    if (this.valueEquivalence != null)
      localToStringHelper.addValue("valueEquivalence");
    if (this.evictionListener != null)
      localToStringHelper.addValue("evictionListener");
    if (this.cleanupExecutor != null)
      localToStringHelper.addValue("cleanupExecutor");
    return localToStringHelper.toString();
  }

  @GwtIncompatible("java.lang.ref.WeakReference")
  public MapMaker weakKeys()
  {
    return setKeyStrength(CustomConcurrentHashMap.Strength.WEAK);
  }

  @GwtIncompatible("java.lang.ref.WeakReference")
  public MapMaker weakValues()
  {
    return setValueStrength(CustomConcurrentHashMap.Strength.WEAK);
  }

  static abstract interface Cache<K, V> extends Function<K, V>
  {
    public abstract ConcurrentMap<K, V> asMap();
  }

  static class ComputingMapAdapter<K, V> extends ForwardingConcurrentMap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID;
    final MapMaker.Cache<K, V> cache;

    ComputingMapAdapter(MapMaker.Cache<K, V> paramCache)
    {
      this.cache = paramCache;
    }

    protected ConcurrentMap<K, V> delegate()
    {
      return this.cache.asMap();
    }

    public V get(Object paramObject)
    {
      return this.cache.apply(paramObject);
    }
  }

  static final class NullComputingConcurrentMap<K, V> extends MapMaker.NullConcurrentMap<K, V>
    implements MapMaker.Cache<K, V>
  {
    private static final long serialVersionUID;
    final Function<? super K, ? extends V> computingFunction;

    NullComputingConcurrentMap(MapMaker paramMapMaker, Function<? super K, ? extends V> paramFunction)
    {
      super();
      this.computingFunction = ((Function)Preconditions.checkNotNull(paramFunction));
    }

    private V compute(K paramK)
    {
      Preconditions.checkNotNull(paramK);
      try
      {
        Object localObject = this.computingFunction.apply(paramK);
        return localObject;
      }
      catch (ComputationException localComputationException)
      {
        throw localComputationException;
      }
      catch (Throwable localThrowable)
      {
      }
      throw new ComputationException(localThrowable);
    }

    public V apply(K paramK)
    {
      Object localObject = compute(paramK);
      Preconditions.checkNotNull(localObject, this.computingFunction + " returned null for key " + paramK + ".");
      this.evictionListener.onEviction(paramK, localObject);
      return localObject;
    }

    public ConcurrentMap<K, V> asMap()
    {
      return this;
    }
  }

  static class NullConcurrentMap<K, V> extends AbstractMap<K, V>
    implements ConcurrentMap<K, V>, Serializable
  {
    private static final long serialVersionUID;
    final MapEvictionListener<K, V> evictionListener;

    NullConcurrentMap(MapMaker paramMapMaker)
    {
      this.evictionListener = paramMapMaker.getEvictionListener();
    }

    public boolean containsKey(Object paramObject)
    {
      Preconditions.checkNotNull(paramObject);
      return false;
    }

    public boolean containsValue(Object paramObject)
    {
      Preconditions.checkNotNull(paramObject);
      return false;
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
      return Collections.emptySet();
    }

    public V get(Object paramObject)
    {
      Preconditions.checkNotNull(paramObject);
      return null;
    }

    public V put(K paramK, V paramV)
    {
      Preconditions.checkNotNull(paramK);
      Preconditions.checkNotNull(paramV);
      this.evictionListener.onEviction(paramK, paramV);
      return null;
    }

    public V putIfAbsent(K paramK, V paramV)
    {
      return put(paramK, paramV);
    }

    public V remove(Object paramObject)
    {
      Preconditions.checkNotNull(paramObject);
      return null;
    }

    public boolean remove(Object paramObject1, Object paramObject2)
    {
      Preconditions.checkNotNull(paramObject1);
      Preconditions.checkNotNull(paramObject2);
      return false;
    }

    public V replace(K paramK, V paramV)
    {
      Preconditions.checkNotNull(paramK);
      Preconditions.checkNotNull(paramV);
      return null;
    }

    public boolean replace(K paramK, V paramV1, V paramV2)
    {
      Preconditions.checkNotNull(paramK);
      Preconditions.checkNotNull(paramV1);
      Preconditions.checkNotNull(paramV2);
      return false;
    }
  }

  static enum NullListener
    implements MapEvictionListener
  {
    static
    {
      NullListener[] arrayOfNullListener = new NullListener[1];
      arrayOfNullListener[0] = INSTANCE;
      $VALUES = arrayOfNullListener;
    }

    public void onEviction(Object paramObject1, Object paramObject2)
    {
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMaker
 * JD-Core Version:    0.6.0
 */