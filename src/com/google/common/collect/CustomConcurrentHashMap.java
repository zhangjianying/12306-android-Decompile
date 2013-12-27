package com.google.common.collect;

import TV;;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Equivalences;
import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableSoftReference;
import com.google.common.base.FinalizableWeakReference;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

class CustomConcurrentHashMap<K, V> extends AbstractMap<K, V>
  implements ConcurrentMap<K, V>, Serializable
{
  static final int CLEANUP_MAX = 16;
  static final Queue<? extends ReferenceEntry<?, ?>> DISCARDING_QUEUE;
  static final int DRAIN_THRESHOLD = 63;
  static final int MAXIMUM_CAPACITY = 1073741824;
  static final int MAX_SEGMENTS = 65536;
  static final ValueReference<Object, Object> UNSET = new ValueReference()
  {
    public void clear()
    {
    }

    public CustomConcurrentHashMap.ValueReference<Object, Object> copyFor(CustomConcurrentHashMap.ReferenceEntry<Object, Object> paramReferenceEntry)
    {
      return this;
    }

    public Object get()
    {
      return null;
    }

    public boolean isComputingReference()
    {
      return false;
    }

    public void notifyValueReclaimed()
    {
    }

    public Object waitForValue()
    {
      return null;
    }
  };
  private static final long serialVersionUID = 4L;
  final Executor cleanupExecutor;
  final int concurrencyLevel;
  final transient EntryFactory entryFactory;
  Set<Map.Entry<K, V>> entrySet;
  final MapEvictionListener<? super K, ? super V> evictionListener;
  final Queue<ReferenceEntry<K, V>> evictionNotificationQueue;
  final long expireAfterAccessNanos;
  final long expireAfterWriteNanos;
  final Equivalence<Object> keyEquivalence;
  Set<K> keySet;
  final Strength keyStrength;
  final int maximumSize;
  final transient int segmentMask;
  final transient int segmentShift;
  final transient CustomConcurrentHashMap<K, V>[].Segment segments;
  final Ticker ticker;
  final Equivalence<Object> valueEquivalence;
  final Strength valueStrength;
  Collection<V> values;

  static
  {
    DISCARDING_QUEUE = new AbstractQueue()
    {
      public Iterator<CustomConcurrentHashMap.ReferenceEntry<?, ?>> iterator()
      {
        return Iterators.emptyIterator();
      }

      public boolean offer(CustomConcurrentHashMap.ReferenceEntry<?, ?> paramReferenceEntry)
      {
        return true;
      }

      public CustomConcurrentHashMap.ReferenceEntry<?, ?> peek()
      {
        return null;
      }

      public CustomConcurrentHashMap.ReferenceEntry<?, ?> poll()
      {
        return null;
      }

      public int size()
      {
        return 0;
      }
    };
  }

  CustomConcurrentHashMap(MapMaker paramMapMaker)
  {
    this.concurrencyLevel = Math.min(paramMapMaker.getConcurrencyLevel(), 65536);
    this.keyStrength = paramMapMaker.getKeyStrength();
    this.valueStrength = paramMapMaker.getValueStrength();
    this.keyEquivalence = paramMapMaker.getKeyEquivalence();
    this.valueEquivalence = paramMapMaker.getValueEquivalence();
    this.maximumSize = paramMapMaker.maximumSize;
    this.expireAfterAccessNanos = paramMapMaker.getExpireAfterAccessNanos();
    this.expireAfterWriteNanos = paramMapMaker.getExpireAfterWriteNanos();
    this.entryFactory = EntryFactory.getFactory(this.keyStrength, expires(), evictsBySize());
    this.cleanupExecutor = paramMapMaker.getCleanupExecutor();
    this.ticker = paramMapMaker.getTicker();
    this.evictionListener = paramMapMaker.getEvictionListener();
    if (this.evictionListener == MapMaker.NullListener.INSTANCE);
    int i;
    int j;
    int k;
    for (Object localObject = discardingQueue(); ; localObject = new ConcurrentLinkedQueue())
    {
      this.evictionNotificationQueue = ((Queue)localObject);
      i = Math.min(paramMapMaker.getInitialCapacity(), 1073741824);
      if (evictsBySize())
        i = Math.min(i, this.maximumSize);
      j = 0;
      k = 1;
      while ((k < this.concurrencyLevel) && ((!evictsBySize()) || (k * 2 <= this.maximumSize)))
      {
        j++;
        k <<= 1;
      }
    }
    this.segmentShift = (32 - j);
    this.segmentMask = (k - 1);
    this.segments = newSegmentArray(k);
    int m = i / k;
    if (m * k < i)
      m++;
    int n = 1;
    while (n < m)
      n <<= 1;
    if (evictsBySize())
    {
      int i2 = 1 + this.maximumSize / k;
      int i3 = this.maximumSize % k;
      for (int i4 = 0; i4 < this.segments.length; i4++)
      {
        if (i4 == i3)
          i2--;
        this.segments[i4] = createSegment(n, i2);
      }
    }
    for (int i1 = 0; i1 < this.segments.length; i1++)
      this.segments[i1] = createSegment(n, -1);
  }

  @GuardedBy("Segment.this")
  static <K, V> void connectEvictables(ReferenceEntry<K, V> paramReferenceEntry1, ReferenceEntry<K, V> paramReferenceEntry2)
  {
    paramReferenceEntry1.setNextEvictable(paramReferenceEntry2);
    paramReferenceEntry2.setPreviousEvictable(paramReferenceEntry1);
  }

  @GuardedBy("Segment.this")
  static <K, V> void connectExpirables(ReferenceEntry<K, V> paramReferenceEntry1, ReferenceEntry<K, V> paramReferenceEntry2)
  {
    paramReferenceEntry1.setNextExpirable(paramReferenceEntry2);
    paramReferenceEntry2.setPreviousExpirable(paramReferenceEntry1);
  }

  static <E> Queue<E> discardingQueue()
  {
    return DISCARDING_QUEUE;
  }

  static <K, V> ReferenceEntry<K, V> nullEntry()
  {
    return NullEntry.INSTANCE;
  }

  @GuardedBy("Segment.this")
  static <K, V> void nullifyEvictable(ReferenceEntry<K, V> paramReferenceEntry)
  {
    ReferenceEntry localReferenceEntry = nullEntry();
    paramReferenceEntry.setNextEvictable(localReferenceEntry);
    paramReferenceEntry.setPreviousEvictable(localReferenceEntry);
  }

  @GuardedBy("Segment.this")
  static <K, V> void nullifyExpirable(ReferenceEntry<K, V> paramReferenceEntry)
  {
    ReferenceEntry localReferenceEntry = nullEntry();
    paramReferenceEntry.setNextExpirable(localReferenceEntry);
    paramReferenceEntry.setPreviousExpirable(localReferenceEntry);
  }

  private static int rehash(int paramInt)
  {
    int i = paramInt + (0xFFFFCD7D ^ paramInt << 15);
    int j = i ^ i >>> 10;
    int k = j + (j << 3);
    int m = k ^ k >>> 6;
    int n = m + ((m << 2) + (m << 14));
    return n ^ n >>> 16;
  }

  static <K, V> ValueReference<K, V> unset()
  {
    return UNSET;
  }

  public void clear()
  {
    Segment[] arrayOfSegment = this.segments;
    int i = arrayOfSegment.length;
    for (int j = 0; j < i; j++)
      arrayOfSegment[j].clear();
  }

  public boolean containsKey(Object paramObject)
  {
    int i = hash(paramObject);
    return segmentFor(i).containsKey(paramObject, i);
  }

  public boolean containsValue(Object paramObject)
  {
    Preconditions.checkNotNull(paramObject);
    Segment[] arrayOfSegment = this.segments;
    for (int i = 0; i < arrayOfSegment.length; i++)
      if (arrayOfSegment[i].containsValue(paramObject))
        return true;
    return false;
  }

  @GuardedBy("Segment.this")
  ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> paramReferenceEntry1, ReferenceEntry<K, V> paramReferenceEntry2)
  {
    ValueReference localValueReference = paramReferenceEntry1.getValueReference();
    ReferenceEntry localReferenceEntry = this.entryFactory.copyEntry(this, paramReferenceEntry1, paramReferenceEntry2);
    localReferenceEntry.setValueReference(localValueReference.copyFor(localReferenceEntry));
    return localReferenceEntry;
  }

  CustomConcurrentHashMap<K, V>.Segment createSegment(int paramInt1, int paramInt2)
  {
    return new Segment(paramInt1, paramInt2);
  }

  void enqueueNotification(K paramK, int paramInt, ValueReference<K, V> paramValueReference)
  {
    if (this.evictionNotificationQueue == DISCARDING_QUEUE)
      return;
    ReferenceEntry localReferenceEntry = newEntry(paramK, paramInt, null);
    localReferenceEntry.setValueReference(paramValueReference.copyFor(localReferenceEntry));
    this.evictionNotificationQueue.offer(localReferenceEntry);
  }

  public Set<Map.Entry<K, V>> entrySet()
  {
    Set localSet = this.entrySet;
    if (localSet != null)
      return localSet;
    EntrySet localEntrySet = new EntrySet();
    this.entrySet = localEntrySet;
    return localEntrySet;
  }

  boolean evictsBySize()
  {
    return this.maximumSize != -1;
  }

  boolean expires()
  {
    return (expiresAfterWrite()) || (expiresAfterAccess());
  }

  boolean expiresAfterAccess()
  {
    return this.expireAfterAccessNanos > 0L;
  }

  boolean expiresAfterWrite()
  {
    return this.expireAfterWriteNanos > 0L;
  }

  public V get(Object paramObject)
  {
    int i = hash(paramObject);
    return segmentFor(i).get(paramObject, i);
  }

  @VisibleForTesting
  ReferenceEntry<K, V> getEntry(Object paramObject)
  {
    int i = hash(paramObject);
    return segmentFor(i).getEntry(paramObject, i);
  }

  int hash(Object paramObject)
  {
    return rehash(this.keyEquivalence.hash(Preconditions.checkNotNull(paramObject)));
  }

  boolean isCollected(ReferenceEntry<K, V> paramReferenceEntry)
  {
    if (paramReferenceEntry.getKey() == null);
    ValueReference localValueReference;
    do
    {
      return true;
      localValueReference = paramReferenceEntry.getValueReference();
      if (localValueReference.isComputingReference())
        return false;
    }
    while (localValueReference.get() == null);
    return false;
  }

  public boolean isEmpty()
  {
    Segment[] arrayOfSegment = this.segments;
    int[] arrayOfInt = new int[arrayOfSegment.length];
    int i = 0;
    for (int j = 0; j < arrayOfSegment.length; j++)
    {
      if (arrayOfSegment[j].count != 0)
        return false;
      int m = arrayOfSegment[j].modCount;
      arrayOfInt[j] = m;
      i += m;
    }
    if (i != 0)
      for (int k = 0; ; k++)
      {
        if (k >= arrayOfSegment.length)
          break label104;
        if ((arrayOfSegment[k].count != 0) || (arrayOfInt[k] != arrayOfSegment[k].modCount))
          break;
      }
    label104: return true;
  }

  boolean isExpired(ReferenceEntry<K, V> paramReferenceEntry)
  {
    return isExpired(paramReferenceEntry, this.ticker.read());
  }

  boolean isExpired(ReferenceEntry<K, V> paramReferenceEntry, long paramLong)
  {
    return paramLong - paramReferenceEntry.getExpirationTime() > 0L;
  }

  boolean isInlineCleanup()
  {
    return this.cleanupExecutor == MapMaker.DEFAULT_CLEANUP_EXECUTOR;
  }

  @VisibleForTesting
  boolean isLive(ReferenceEntry<K, V> paramReferenceEntry)
  {
    return segmentFor(paramReferenceEntry.getHash()).getLiveValue(paramReferenceEntry) != null;
  }

  boolean isUnset(ReferenceEntry<K, V> paramReferenceEntry)
  {
    return isUnset(paramReferenceEntry.getValueReference());
  }

  boolean isUnset(ValueReference<K, V> paramValueReference)
  {
    return paramValueReference == UNSET;
  }

  public Set<K> keySet()
  {
    Set localSet = this.keySet;
    if (localSet != null)
      return localSet;
    KeySet localKeySet = new KeySet();
    this.keySet = localKeySet;
    return localKeySet;
  }

  @GuardedBy("Segment.this")
  ReferenceEntry<K, V> newEntry(K paramK, int paramInt, @Nullable ReferenceEntry<K, V> paramReferenceEntry)
  {
    return this.entryFactory.newEntry(this, paramK, paramInt, paramReferenceEntry);
  }

  final CustomConcurrentHashMap<K, V>[].Segment newSegmentArray(int paramInt)
  {
    return (Segment[])(Segment[])Array.newInstance(Segment.class, paramInt);
  }

  @GuardedBy("Segment.this")
  ValueReference<K, V> newValueReference(ReferenceEntry<K, V> paramReferenceEntry, V paramV)
  {
    return this.valueStrength.referenceValue(paramReferenceEntry, paramV);
  }

  void processPendingNotifications()
  {
    while (true)
    {
      ReferenceEntry localReferenceEntry = (ReferenceEntry)this.evictionNotificationQueue.poll();
      if (localReferenceEntry == null)
        break;
      this.evictionListener.onEviction(localReferenceEntry.getKey(), localReferenceEntry.getValueReference().get());
    }
  }

  public V put(K paramK, V paramV)
  {
    int i = hash(paramK);
    return segmentFor(i).put(paramK, i, paramV, false);
  }

  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      put(localEntry.getKey(), localEntry.getValue());
    }
  }

  public V putIfAbsent(K paramK, V paramV)
  {
    int i = hash(paramK);
    return segmentFor(i).put(paramK, i, paramV, true);
  }

  void reclaimKey(ReferenceEntry<K, V> paramReferenceEntry)
  {
    int i = paramReferenceEntry.getHash();
    segmentFor(i).unsetKey(paramReferenceEntry, i);
  }

  void reclaimValue(ReferenceEntry<K, V> paramReferenceEntry, ValueReference<K, V> paramValueReference)
  {
    int i = paramReferenceEntry.getHash();
    Segment localSegment = segmentFor(i);
    localSegment.unsetValue(paramReferenceEntry.getKey(), i, paramValueReference);
    if (!localSegment.isHeldByCurrentThread())
      localSegment.postWriteCleanup();
  }

  public V remove(Object paramObject)
  {
    int i = hash(paramObject);
    return segmentFor(i).remove(paramObject, i);
  }

  public boolean remove(Object paramObject1, Object paramObject2)
  {
    int i = hash(paramObject1);
    return segmentFor(i).remove(paramObject1, i, paramObject2);
  }

  public V replace(K paramK, V paramV)
  {
    int i = hash(paramK);
    return segmentFor(i).replace(paramK, i, paramV);
  }

  public boolean replace(K paramK, V paramV1, V paramV2)
  {
    int i = hash(paramK);
    return segmentFor(i).replace(paramK, i, paramV1, paramV2);
  }

  CustomConcurrentHashMap<K, V>.Segment segmentFor(int paramInt)
  {
    return this.segments[(paramInt >>> this.segmentShift & this.segmentMask)];
  }

  public int size()
  {
    Segment[] arrayOfSegment = this.segments;
    long l = 0L;
    for (int i = 0; i < arrayOfSegment.length; i++)
      l += arrayOfSegment[i].count;
    return Ints.saturatedCast(l);
  }

  public Collection<V> values()
  {
    Collection localCollection = this.values;
    if (localCollection != null)
      return localCollection;
    Values localValues = new Values();
    this.values = localValues;
    return localValues;
  }

  Object writeReplace()
  {
    return new SerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.evictionListener, this);
  }

  static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID = 2L;
    final int concurrencyLevel;
    transient ConcurrentMap<K, V> delegate;
    final MapEvictionListener<? super K, ? super V> evictionListener;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final Equivalence<Object> keyEquivalence;
    final CustomConcurrentHashMap.Strength keyStrength;
    final int maximumSize;
    final Equivalence<Object> valueEquivalence;
    final CustomConcurrentHashMap.Strength valueStrength;

    AbstractSerializationProxy(CustomConcurrentHashMap.Strength paramStrength1, CustomConcurrentHashMap.Strength paramStrength2, Equivalence<Object> paramEquivalence1, Equivalence<Object> paramEquivalence2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, MapEvictionListener<? super K, ? super V> paramMapEvictionListener, ConcurrentMap<K, V> paramConcurrentMap)
    {
      this.keyStrength = paramStrength1;
      this.valueStrength = paramStrength2;
      this.keyEquivalence = paramEquivalence1;
      this.valueEquivalence = paramEquivalence2;
      this.expireAfterWriteNanos = paramLong1;
      this.expireAfterAccessNanos = paramLong2;
      this.maximumSize = paramInt1;
      this.concurrencyLevel = paramInt2;
      this.evictionListener = paramMapEvictionListener;
      this.delegate = paramConcurrentMap;
    }

    protected ConcurrentMap<K, V> delegate()
    {
      return this.delegate;
    }

    void readEntries(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      while (true)
      {
        Object localObject1 = paramObjectInputStream.readObject();
        if (localObject1 == null)
          return;
        Object localObject2 = paramObjectInputStream.readObject();
        this.delegate.put(localObject1, localObject2);
      }
    }

    MapMaker readMapMaker(ObjectInputStream paramObjectInputStream)
      throws IOException
    {
      int i = paramObjectInputStream.readInt();
      MapMaker localMapMaker = new MapMaker().initialCapacity(i).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).privateKeyEquivalence(this.keyEquivalence).privateValueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel);
      localMapMaker.evictionListener(this.evictionListener);
      if (this.expireAfterWriteNanos > 0L)
        localMapMaker.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
      if (this.expireAfterAccessNanos > 0L)
        localMapMaker.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
      if (this.maximumSize != -1)
        localMapMaker.maximumSize(this.maximumSize);
      return localMapMaker;
    }

    void writeMapTo(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.writeInt(this.delegate.size());
      Iterator localIterator = this.delegate.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramObjectOutputStream.writeObject(localEntry.getKey());
        paramObjectOutputStream.writeObject(localEntry.getValue());
      }
      paramObjectOutputStream.writeObject(null);
    }
  }

  static abstract enum EntryFactory
  {
    static final int EVICTABLE_MASK = 2;
    static final int EXPIRABLE_MASK = 1;
    static final EntryFactory[][] factories;

    static
    {
      STRONG_EVICTABLE = new EntryFactory("STRONG_EVICTABLE", 2)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyEvictableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.StrongEvictableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      STRONG_EXPIRABLE_EVICTABLE = new EntryFactory("STRONG_EXPIRABLE_EVICTABLE", 3)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyExpirableEntry(paramReferenceEntry1, localReferenceEntry);
          copyEvictableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.StrongExpirableEvictableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      SOFT = new EntryFactory("SOFT", 4)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.SoftEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      SOFT_EXPIRABLE = new EntryFactory("SOFT_EXPIRABLE", 5)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyExpirableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.SoftExpirableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      SOFT_EVICTABLE = new EntryFactory("SOFT_EVICTABLE", 6)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyEvictableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.SoftEvictableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      SOFT_EXPIRABLE_EVICTABLE = new EntryFactory("SOFT_EXPIRABLE_EVICTABLE", 7)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyExpirableEntry(paramReferenceEntry1, localReferenceEntry);
          copyEvictableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.SoftExpirableEvictableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      WEAK = new EntryFactory("WEAK", 8)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.WeakEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      WEAK_EXPIRABLE = new EntryFactory("WEAK_EXPIRABLE", 9)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyExpirableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.WeakExpirableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      WEAK_EVICTABLE = new EntryFactory("WEAK_EVICTABLE", 10)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyEvictableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.WeakEvictableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      WEAK_EXPIRABLE_EVICTABLE = new EntryFactory("WEAK_EXPIRABLE_EVICTABLE", 11)
      {
        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = super.copyEntry(paramCustomConcurrentHashMap, paramReferenceEntry1, paramReferenceEntry2);
          copyExpirableEntry(paramReferenceEntry1, localReferenceEntry);
          copyEvictableEntry(paramReferenceEntry1, localReferenceEntry);
          return localReferenceEntry;
        }

        <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          return new CustomConcurrentHashMap.WeakExpirableEvictableEntry(paramCustomConcurrentHashMap, paramK, paramInt, paramReferenceEntry);
        }
      };
      EntryFactory[] arrayOfEntryFactory1 = new EntryFactory[12];
      arrayOfEntryFactory1[0] = STRONG;
      arrayOfEntryFactory1[1] = STRONG_EXPIRABLE;
      arrayOfEntryFactory1[2] = STRONG_EVICTABLE;
      arrayOfEntryFactory1[3] = STRONG_EXPIRABLE_EVICTABLE;
      arrayOfEntryFactory1[4] = SOFT;
      arrayOfEntryFactory1[5] = SOFT_EXPIRABLE;
      arrayOfEntryFactory1[6] = SOFT_EVICTABLE;
      arrayOfEntryFactory1[7] = SOFT_EXPIRABLE_EVICTABLE;
      arrayOfEntryFactory1[8] = WEAK;
      arrayOfEntryFactory1[9] = WEAK_EXPIRABLE;
      arrayOfEntryFactory1[10] = WEAK_EVICTABLE;
      arrayOfEntryFactory1[11] = WEAK_EXPIRABLE_EVICTABLE;
      $VALUES = arrayOfEntryFactory1;
      EntryFactory[][] arrayOfEntryFactory; = new EntryFactory[3][];
      EntryFactory[] arrayOfEntryFactory2 = new EntryFactory[4];
      arrayOfEntryFactory2[0] = STRONG;
      arrayOfEntryFactory2[1] = STRONG_EXPIRABLE;
      arrayOfEntryFactory2[2] = STRONG_EVICTABLE;
      arrayOfEntryFactory2[3] = STRONG_EXPIRABLE_EVICTABLE;
      arrayOfEntryFactory;[0] = arrayOfEntryFactory2;
      EntryFactory[] arrayOfEntryFactory3 = new EntryFactory[4];
      arrayOfEntryFactory3[0] = SOFT;
      arrayOfEntryFactory3[1] = SOFT_EXPIRABLE;
      arrayOfEntryFactory3[2] = SOFT_EVICTABLE;
      arrayOfEntryFactory3[3] = SOFT_EXPIRABLE_EVICTABLE;
      arrayOfEntryFactory;[1] = arrayOfEntryFactory3;
      EntryFactory[] arrayOfEntryFactory4 = new EntryFactory[4];
      arrayOfEntryFactory4[0] = WEAK;
      arrayOfEntryFactory4[1] = WEAK_EXPIRABLE;
      arrayOfEntryFactory4[2] = WEAK_EVICTABLE;
      arrayOfEntryFactory4[3] = WEAK_EXPIRABLE_EVICTABLE;
      arrayOfEntryFactory;[2] = arrayOfEntryFactory4;
      factories = arrayOfEntryFactory;;
    }

    static EntryFactory getFactory(CustomConcurrentHashMap.Strength paramStrength, boolean paramBoolean1, boolean paramBoolean2)
    {
      if (paramBoolean1);
      for (int i = 1; ; i = 0)
      {
        int j = 0;
        if (paramBoolean2)
          j = 2;
        int k = i | j;
        return factories[paramStrength.ordinal()][k];
      }
    }

    @GuardedBy("Segment.this")
    <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> copyEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
    {
      return newEntry(paramCustomConcurrentHashMap, paramReferenceEntry1.getKey(), paramReferenceEntry1.getHash(), paramReferenceEntry2);
    }

    @GuardedBy("Segment.this")
    <K, V> void copyEvictableEntry(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
    {
      CustomConcurrentHashMap.connectEvictables(paramReferenceEntry1.getPreviousEvictable(), paramReferenceEntry2);
      CustomConcurrentHashMap.connectEvictables(paramReferenceEntry2, paramReferenceEntry1.getNextEvictable());
      CustomConcurrentHashMap.nullifyEvictable(paramReferenceEntry1);
    }

    @GuardedBy("Segment.this")
    <K, V> void copyExpirableEntry(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
    {
      paramReferenceEntry2.setExpirationTime(paramReferenceEntry1.getExpirationTime());
      CustomConcurrentHashMap.connectExpirables(paramReferenceEntry1.getPreviousExpirable(), paramReferenceEntry2);
      CustomConcurrentHashMap.connectExpirables(paramReferenceEntry2, paramReferenceEntry1.getNextExpirable());
      CustomConcurrentHashMap.nullifyExpirable(paramReferenceEntry1);
    }

    abstract <K, V> CustomConcurrentHashMap.ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry);
  }

  final class EntryIterator extends CustomConcurrentHashMap<K, V>.HashIterator
    implements Iterator<Map.Entry<K, V>>
  {
    EntryIterator()
    {
      super();
    }

    public Map.Entry<K, V> next()
    {
      return nextEntry();
    }
  }

  final class EntrySet extends AbstractSet<Map.Entry<K, V>>
  {
    EntrySet()
    {
    }

    public void clear()
    {
      CustomConcurrentHashMap.this.clear();
    }

    public boolean contains(Object paramObject)
    {
      if (!(paramObject instanceof Map.Entry));
      Map.Entry localEntry;
      Object localObject2;
      do
      {
        Object localObject1;
        do
        {
          return false;
          localEntry = (Map.Entry)paramObject;
          localObject1 = localEntry.getKey();
        }
        while (localObject1 == null);
        localObject2 = CustomConcurrentHashMap.this.get(localObject1);
      }
      while ((localObject2 == null) || (!CustomConcurrentHashMap.this.valueEquivalence.equivalent(localEntry.getValue(), localObject2)));
      return true;
    }

    public boolean isEmpty()
    {
      return CustomConcurrentHashMap.this.isEmpty();
    }

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new CustomConcurrentHashMap.EntryIterator(CustomConcurrentHashMap.this);
    }

    public boolean remove(Object paramObject)
    {
      if (!(paramObject instanceof Map.Entry));
      Map.Entry localEntry;
      Object localObject;
      do
      {
        return false;
        localEntry = (Map.Entry)paramObject;
        localObject = localEntry.getKey();
      }
      while ((localObject == null) || (!CustomConcurrentHashMap.this.remove(localObject, localEntry.getValue())));
      return true;
    }

    public int size()
    {
      return CustomConcurrentHashMap.this.size();
    }
  }

  abstract class HashIterator
  {
    AtomicReferenceArray<CustomConcurrentHashMap.ReferenceEntry<K, V>> currentTable;
    CustomConcurrentHashMap<K, V>.WriteThroughEntry lastReturned;
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextEntry;
    CustomConcurrentHashMap<K, V>.WriteThroughEntry nextExternal;
    int nextSegmentIndex = -1 + CustomConcurrentHashMap.this.segments.length;
    int nextTableIndex = -1;

    HashIterator()
    {
      advance();
    }

    final void advance()
    {
      this.nextExternal = null;
      if (nextInChain());
      label12: 
      do
      {
        CustomConcurrentHashMap.Segment localSegment;
        do
        {
          do
          {
            return;
            continue;
            break label12;
            continue;
            while (nextInTable());
          }
          while (this.nextSegmentIndex < 0);
          CustomConcurrentHashMap.Segment[] arrayOfSegment = CustomConcurrentHashMap.this.segments;
          int i = this.nextSegmentIndex;
          this.nextSegmentIndex = (i - 1);
          localSegment = arrayOfSegment[i];
        }
        while (localSegment.count == 0);
        this.currentTable = localSegment.table;
        this.nextTableIndex = (-1 + this.currentTable.length());
      }
      while (!nextInTable());
    }

    boolean advanceTo(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      Object localObject1 = paramReferenceEntry.getKey();
      Object localObject2 = paramReferenceEntry.getValueReference().get();
      if ((localObject1 != null) && (localObject2 != null) && ((!CustomConcurrentHashMap.this.expires()) || (!CustomConcurrentHashMap.this.isExpired(paramReferenceEntry))))
      {
        this.nextExternal = new CustomConcurrentHashMap.WriteThroughEntry(CustomConcurrentHashMap.this, localObject1, localObject2);
        return true;
      }
      return false;
    }

    public boolean hasNext()
    {
      return this.nextExternal != null;
    }

    CustomConcurrentHashMap<K, V>.WriteThroughEntry nextEntry()
    {
      if (this.nextExternal == null)
        throw new NoSuchElementException();
      this.lastReturned = this.nextExternal;
      advance();
      return this.lastReturned;
    }

    boolean nextInChain()
    {
      if (this.nextEntry != null)
        for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext())
          if (advanceTo(this.nextEntry))
            return true;
      return false;
    }

    boolean nextInTable()
    {
      while (this.nextTableIndex >= 0)
      {
        AtomicReferenceArray localAtomicReferenceArray = this.currentTable;
        int i = this.nextTableIndex;
        this.nextTableIndex = (i - 1);
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(i);
        this.nextEntry = localReferenceEntry;
        if ((localReferenceEntry != null) && ((advanceTo(this.nextEntry)) || (nextInChain())))
          return true;
      }
      return false;
    }

    public void remove()
    {
      if (this.lastReturned != null);
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkState(bool);
        CustomConcurrentHashMap.this.remove(this.lastReturned.getKey());
        this.lastReturned = null;
        return;
      }
    }
  }

  final class KeyIterator extends CustomConcurrentHashMap<K, V>.HashIterator
    implements Iterator<K>
  {
    KeyIterator()
    {
      super();
    }

    public K next()
    {
      return nextEntry().getKey();
    }
  }

  final class KeySet extends AbstractSet<K>
  {
    KeySet()
    {
    }

    public void clear()
    {
      CustomConcurrentHashMap.this.clear();
    }

    public boolean contains(Object paramObject)
    {
      return CustomConcurrentHashMap.this.containsKey(paramObject);
    }

    public boolean isEmpty()
    {
      return CustomConcurrentHashMap.this.isEmpty();
    }

    public Iterator<K> iterator()
    {
      return new CustomConcurrentHashMap.KeyIterator(CustomConcurrentHashMap.this);
    }

    public boolean remove(Object paramObject)
    {
      return CustomConcurrentHashMap.this.remove(paramObject) != null;
    }

    public int size()
    {
      return CustomConcurrentHashMap.this.size();
    }
  }

  private static enum NullEntry
    implements CustomConcurrentHashMap.ReferenceEntry<Object, Object>
  {
    static
    {
      NullEntry[] arrayOfNullEntry = new NullEntry[1];
      arrayOfNullEntry[0] = INSTANCE;
      $VALUES = arrayOfNullEntry;
    }

    public long getExpirationTime()
    {
      return 0L;
    }

    public int getHash()
    {
      return 0;
    }

    public Object getKey()
    {
      return null;
    }

    public CustomConcurrentHashMap.ReferenceEntry<Object, Object> getNext()
    {
      return null;
    }

    public CustomConcurrentHashMap.ReferenceEntry<Object, Object> getNextEvictable()
    {
      return this;
    }

    public CustomConcurrentHashMap.ReferenceEntry<Object, Object> getNextExpirable()
    {
      return this;
    }

    public CustomConcurrentHashMap.ReferenceEntry<Object, Object> getPreviousEvictable()
    {
      return this;
    }

    public CustomConcurrentHashMap.ReferenceEntry<Object, Object> getPreviousExpirable()
    {
      return this;
    }

    public CustomConcurrentHashMap.ValueReference<Object, Object> getValueReference()
    {
      return null;
    }

    public void notifyKeyReclaimed()
    {
    }

    public void notifyValueReclaimed(CustomConcurrentHashMap.ValueReference<Object, Object> paramValueReference)
    {
    }

    public void setExpirationTime(long paramLong)
    {
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<Object, Object> paramReferenceEntry)
    {
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<Object, Object> paramReferenceEntry)
    {
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<Object, Object> paramReferenceEntry)
    {
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<Object, Object> paramReferenceEntry)
    {
    }

    public void setValueReference(CustomConcurrentHashMap.ValueReference<Object, Object> paramValueReference)
    {
    }
  }

  private static class QueueHolder
  {
    static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
  }

  static abstract interface ReferenceEntry<K, V>
  {
    public abstract long getExpirationTime();

    public abstract int getHash();

    public abstract K getKey();

    public abstract ReferenceEntry<K, V> getNext();

    public abstract ReferenceEntry<K, V> getNextEvictable();

    public abstract ReferenceEntry<K, V> getNextExpirable();

    public abstract ReferenceEntry<K, V> getPreviousEvictable();

    public abstract ReferenceEntry<K, V> getPreviousExpirable();

    public abstract CustomConcurrentHashMap.ValueReference<K, V> getValueReference();

    public abstract void notifyKeyReclaimed();

    public abstract void notifyValueReclaimed(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference);

    public abstract void setExpirationTime(long paramLong);

    public abstract void setNextEvictable(ReferenceEntry<K, V> paramReferenceEntry);

    public abstract void setNextExpirable(ReferenceEntry<K, V> paramReferenceEntry);

    public abstract void setPreviousEvictable(ReferenceEntry<K, V> paramReferenceEntry);

    public abstract void setPreviousExpirable(ReferenceEntry<K, V> paramReferenceEntry);

    public abstract void setValueReference(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference);
  }

  class Segment extends ReentrantLock
  {
    final Queue<CustomConcurrentHashMap.ReferenceEntry<K, V>> cleanupQueue = new ConcurrentLinkedQueue();
    final Runnable cleanupRunnable = new Runnable()
    {
      public void run()
      {
        CustomConcurrentHashMap.Segment.this.runCleanup();
      }
    };
    volatile int count;

    @GuardedBy("Segment.this")
    final Queue<CustomConcurrentHashMap.ReferenceEntry<K, V>> evictionQueue;

    @GuardedBy("Segment.this")
    final Queue<CustomConcurrentHashMap.ReferenceEntry<K, V>> expirationQueue;
    final int maxSegmentSize;
    int modCount;
    final AtomicInteger readCount = new AtomicInteger();
    final Queue<CustomConcurrentHashMap.ReferenceEntry<K, V>> recencyQueue;
    volatile AtomicReferenceArray<CustomConcurrentHashMap.ReferenceEntry<K, V>> table;
    int threshold;

    Segment(int paramInt1, int arg3)
    {
      int i;
      this.maxSegmentSize = i;
      initTable(newEntryArray(paramInt1));
      Object localObject1;
      Object localObject2;
      if ((CustomConcurrentHashMap.this.evictsBySize()) || (CustomConcurrentHashMap.this.expiresAfterAccess()))
      {
        localObject1 = new ConcurrentLinkedQueue();
        this.recencyQueue = ((Queue)localObject1);
        if (!CustomConcurrentHashMap.this.evictsBySize())
          break label141;
        localObject2 = new EvictionQueue();
        label103: this.evictionQueue = ((Queue)localObject2);
        if (!CustomConcurrentHashMap.this.expires())
          break label149;
      }
      label141: label149: for (Object localObject3 = new ExpirationQueue(); ; localObject3 = CustomConcurrentHashMap.discardingQueue())
      {
        this.expirationQueue = ((Queue)localObject3);
        return;
        localObject1 = CustomConcurrentHashMap.discardingQueue();
        break;
        localObject2 = CustomConcurrentHashMap.discardingQueue();
        break label103;
      }
    }

    void clear()
    {
      if (this.count != 0)
        lock();
      try
      {
        AtomicReferenceArray localAtomicReferenceArray = this.table;
        for (int i = 0; i < localAtomicReferenceArray.length(); i++)
          localAtomicReferenceArray.set(i, null);
        this.evictionQueue.clear();
        this.expirationQueue.clear();
        this.readCount.set(0);
        this.modCount = (1 + this.modCount);
        this.count = 0;
        return;
      }
      finally
      {
        unlock();
      }
      throw localObject;
    }

    boolean clearValue(K paramK, int paramInt, CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      lock();
      try
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
        for (Object localObject2 = getFirst(paramInt); localObject2 != null; localObject2 = localReferenceEntry)
        {
          Object localObject3 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getKey();
          if ((((CustomConcurrentHashMap.ReferenceEntry)localObject2).getHash() == paramInt) && (localObject3 != null) && (CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramK, localObject3)))
          {
            if (((CustomConcurrentHashMap.ReferenceEntry)localObject2).getValueReference() == paramValueReference)
            {
              enqueueCleanup((CustomConcurrentHashMap.ReferenceEntry)localObject2);
              return true;
            }
            return false;
          }
          localReferenceEntry = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getNext();
        }
        return false;
      }
      finally
      {
        unlock();
      }
      throw localObject1;
    }

    boolean containsKey(Object paramObject, int paramInt)
    {
      int i = this.count;
      int j = 0;
      if (i != 0)
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = getFirst(paramInt);
        j = 0;
        if (localReferenceEntry != null)
        {
          if (localReferenceEntry.getHash() != paramInt);
          Object localObject1;
          do
          {
            localReferenceEntry = localReferenceEntry.getNext();
            break;
            localObject1 = localReferenceEntry.getKey();
          }
          while ((localObject1 == null) || (!CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramObject, localObject1)));
          Object localObject2 = getLiveValue(localReferenceEntry);
          j = 0;
          if (localObject2 != null)
            j = 1;
        }
      }
      return j;
    }

    boolean containsValue(Object paramObject)
    {
      if (this.count != 0)
      {
        AtomicReferenceArray localAtomicReferenceArray = this.table;
        int i = localAtomicReferenceArray.length();
        for (int j = 0; j < i; j++)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(j);
          if (localReferenceEntry == null)
            continue;
          Object localObject = getLiveValue(localReferenceEntry);
          if (localObject == null);
          do
          {
            localReferenceEntry = localReferenceEntry.getNext();
            break;
          }
          while (!CustomConcurrentHashMap.this.valueEquivalence.equivalent(paramObject, localObject));
          return true;
        }
      }
      return false;
    }

    @GuardedBy("Segment.this")
    void drainRecencyQueue()
    {
      while (true)
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = (CustomConcurrentHashMap.ReferenceEntry)this.recencyQueue.poll();
        if (localReferenceEntry == null)
          break;
        if (this.evictionQueue.contains(localReferenceEntry))
          this.evictionQueue.add(localReferenceEntry);
        if ((!CustomConcurrentHashMap.this.expiresAfterAccess()) || (!this.expirationQueue.contains(localReferenceEntry)))
          continue;
        this.expirationQueue.add(localReferenceEntry);
      }
    }

    @GuardedBy("Segment.this")
    void enqueueCleanup(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      paramReferenceEntry.setValueReference(CustomConcurrentHashMap.unset());
      this.cleanupQueue.offer(paramReferenceEntry);
      this.evictionQueue.remove(paramReferenceEntry);
      this.expirationQueue.remove(paramReferenceEntry);
    }

    @GuardedBy("Segment.this")
    boolean evictEntries()
    {
      if ((CustomConcurrentHashMap.this.evictsBySize()) && (this.count >= this.maxSegmentSize))
      {
        drainRecencyQueue();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = (CustomConcurrentHashMap.ReferenceEntry)this.evictionQueue.remove();
        if (!unsetEntry(localReferenceEntry, localReferenceEntry.getHash()))
          throw new AssertionError();
        return true;
      }
      return false;
    }

    @GuardedBy("Segment.this")
    void expand()
    {
      AtomicReferenceArray localAtomicReferenceArray1 = this.table;
      int i = localAtomicReferenceArray1.length();
      if (i >= 1073741824)
        return;
      AtomicReferenceArray localAtomicReferenceArray2 = newEntryArray(i << 1);
      this.threshold = (3 * localAtomicReferenceArray2.length() / 4);
      int j = -1 + localAtomicReferenceArray2.length();
      int k = 0;
      while (k < i)
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray1.get(k);
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry2;
        int m;
        if (localReferenceEntry1 != null)
        {
          localReferenceEntry2 = localReferenceEntry1.getNext();
          m = j & localReferenceEntry1.getHash();
          if (localReferenceEntry2 == null)
            localAtomicReferenceArray2.set(m, localReferenceEntry1);
        }
        else
        {
          k++;
          continue;
        }
        Object localObject = localReferenceEntry1;
        int n = m;
        for (CustomConcurrentHashMap.ReferenceEntry localReferenceEntry3 = localReferenceEntry2; localReferenceEntry3 != null; localReferenceEntry3 = localReferenceEntry3.getNext())
        {
          int i2 = j & localReferenceEntry3.getHash();
          if (i2 == n)
            continue;
          n = i2;
          localObject = localReferenceEntry3;
        }
        localAtomicReferenceArray2.set(n, localObject);
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry4 = localReferenceEntry1;
        label178: if (localReferenceEntry4 != localObject)
        {
          if (!CustomConcurrentHashMap.this.isCollected(localReferenceEntry4))
            break label223;
          unsetLiveEntry(localReferenceEntry4, localReferenceEntry4.getHash());
        }
        while (true)
        {
          localReferenceEntry4 = localReferenceEntry4.getNext();
          break label178;
          break;
          label223: int i1 = j & localReferenceEntry4.getHash();
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry5 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray2.get(i1);
          localAtomicReferenceArray2.set(i1, CustomConcurrentHashMap.this.copyEntry(localReferenceEntry4, localReferenceEntry5));
        }
      }
      this.table = localAtomicReferenceArray2;
    }

    @GuardedBy("Segment.this")
    void expireEntries()
    {
      drainRecencyQueue();
      if (this.expirationQueue.isEmpty())
        return;
      long l = CustomConcurrentHashMap.this.ticker.read();
      CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
      do
      {
        localReferenceEntry = (CustomConcurrentHashMap.ReferenceEntry)this.expirationQueue.peek();
        if ((localReferenceEntry == null) || (!CustomConcurrentHashMap.this.isExpired(localReferenceEntry, l)))
          break;
      }
      while (unsetEntry(localReferenceEntry, localReferenceEntry.getHash()));
      throw new AssertionError();
    }

    V get(Object paramObject, int paramInt)
    {
      try
      {
        if (this.count != 0)
        {
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = getFirst(paramInt);
          if (localReferenceEntry != null)
          {
            if (localReferenceEntry.getHash() != paramInt);
            Object localObject2;
            do
            {
              localReferenceEntry = localReferenceEntry.getNext();
              break;
              localObject2 = localReferenceEntry.getKey();
            }
            while ((localObject2 == null) || (!CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramObject, localObject2)));
            Object localObject3 = getLiveValue(localReferenceEntry);
            if (localObject3 != null)
              recordRead(localReferenceEntry);
            return localObject3;
          }
        }
        return null;
      }
      finally
      {
        postReadCleanup();
      }
      throw localObject1;
    }

    @VisibleForTesting
    CustomConcurrentHashMap.ReferenceEntry<K, V> getEntry(Object paramObject, int paramInt)
    {
      CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = getFirst(paramInt);
      if (localReferenceEntry != null)
      {
        if (localReferenceEntry.getHash() != paramInt);
        Object localObject;
        do
        {
          localReferenceEntry = localReferenceEntry.getNext();
          break;
          localObject = localReferenceEntry.getKey();
        }
        while ((localObject == null) || (!CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramObject, localObject)));
        return localReferenceEntry;
      }
      return null;
    }

    CustomConcurrentHashMap.ReferenceEntry<K, V> getFirst(int paramInt)
    {
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      return (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(paramInt & -1 + localAtomicReferenceArray.length());
    }

    V getLiveValue(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      Object localObject;
      if (paramReferenceEntry.getKey() == null)
        localObject = null;
      do
      {
        return localObject;
        localObject = paramReferenceEntry.getValueReference().get();
        if (localObject == null)
          return null;
      }
      while ((!CustomConcurrentHashMap.this.expires()) || (!CustomConcurrentHashMap.this.isExpired(paramReferenceEntry)));
      tryExpireEntries();
      return null;
    }

    void initTable(AtomicReferenceArray<CustomConcurrentHashMap.ReferenceEntry<K, V>> paramAtomicReferenceArray)
    {
      this.threshold = (3 * paramAtomicReferenceArray.length() / 4);
      if (this.threshold == this.maxSegmentSize)
        this.threshold = (1 + this.threshold);
      this.table = paramAtomicReferenceArray;
    }

    AtomicReferenceArray<CustomConcurrentHashMap.ReferenceEntry<K, V>> newEntryArray(int paramInt)
    {
      return new AtomicReferenceArray(paramInt);
    }

    void postReadCleanup()
    {
      if ((0x3F & this.readCount.incrementAndGet()) == 0)
      {
        if (!CustomConcurrentHashMap.this.isInlineCleanup())
          break label28;
        runCleanup();
      }
      label28: 
      do
        return;
      while (isHeldByCurrentThread());
      CustomConcurrentHashMap.this.cleanupExecutor.execute(this.cleanupRunnable);
    }

    void postWriteCleanup()
    {
      if (CustomConcurrentHashMap.this.isInlineCleanup())
        if (isHeldByCurrentThread())
          runLockedCleanup();
      do
      {
        return;
        runUnlockedCleanup();
        return;
      }
      while (isHeldByCurrentThread());
      CustomConcurrentHashMap.this.cleanupExecutor.execute(this.cleanupRunnable);
    }

    @GuardedBy("Segment.this")
    void preWriteCleanup()
    {
      if (CustomConcurrentHashMap.this.isInlineCleanup())
      {
        runLockedCleanup();
        return;
      }
      expireEntries();
    }

    @GuardedBy("Segment.this")
    void processPendingCleanup()
    {
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int i = 0;
      label113: 
      while (i < 16)
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)this.cleanupQueue.poll();
        if (localReferenceEntry1 == null)
          break;
        int j = localReferenceEntry1.getHash() & -1 + localAtomicReferenceArray.length();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry2 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(j);
        for (CustomConcurrentHashMap.ReferenceEntry localReferenceEntry3 = localReferenceEntry2; ; localReferenceEntry3 = localReferenceEntry3.getNext())
        {
          if (localReferenceEntry3 == null)
            break label113;
          if (localReferenceEntry3 != localReferenceEntry1)
            continue;
          if (!CustomConcurrentHashMap.this.isUnset(localReferenceEntry3))
            break;
          localAtomicReferenceArray.set(j, removeFromChain(localReferenceEntry2, localReferenceEntry3));
          i++;
          break;
        }
      }
    }

    V put(K paramK, int paramInt, V paramV, boolean paramBoolean)
    {
      Preconditions.checkNotNull(paramV);
      lock();
      try
      {
        preWriteCleanup();
        int i = 1 + this.count;
        if (i > this.threshold)
        {
          expand();
          i = 1 + this.count;
        }
        AtomicReferenceArray localAtomicReferenceArray = this.table;
        int j = paramInt & -1 + localAtomicReferenceArray.length();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(j);
        for (CustomConcurrentHashMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1; localReferenceEntry2 != null; localReferenceEntry2 = localReferenceEntry2.getNext())
        {
          Object localObject2 = localReferenceEntry2.getKey();
          if ((localReferenceEntry2.getHash() != paramInt) || (localObject2 == null) || (!CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramK, localObject2)))
            continue;
          CustomConcurrentHashMap.ValueReference localValueReference = localReferenceEntry2.getValueReference();
          Object localObject3 = localValueReference.get();
          if (localObject3 == null)
          {
            this.modCount = (1 + this.modCount);
            localValueReference.notifyValueReclaimed();
            evictEntries();
            this.count = (1 + this.count);
          }
          do
          {
            setValue(localReferenceEntry2, paramV);
            return localObject3;
          }
          while (!paramBoolean);
          recordLockedRead(localReferenceEntry2);
          return localObject3;
        }
        if (evictEntries())
        {
          i = 1 + this.count;
          localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(j);
        }
        this.modCount = (1 + this.modCount);
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry3 = CustomConcurrentHashMap.this.newEntry(paramK, paramInt, localReferenceEntry1);
        setValue(localReferenceEntry3, paramV);
        localAtomicReferenceArray.set(j, localReferenceEntry3);
        this.count = i;
        return null;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
      throw localObject1;
    }

    void recordExpirationTime(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, long paramLong)
    {
      paramReferenceEntry.setExpirationTime(paramLong + CustomConcurrentHashMap.this.ticker.read());
    }

    @GuardedBy("Segment.this")
    void recordLockedRead(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.evictionQueue.add(paramReferenceEntry);
      if (CustomConcurrentHashMap.this.expiresAfterAccess())
      {
        recordExpirationTime(paramReferenceEntry, CustomConcurrentHashMap.this.expireAfterAccessNanos);
        this.expirationQueue.add(paramReferenceEntry);
      }
    }

    void recordRead(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      if (CustomConcurrentHashMap.this.expiresAfterAccess())
        recordExpirationTime(paramReferenceEntry, CustomConcurrentHashMap.this.expireAfterAccessNanos);
      this.recencyQueue.add(paramReferenceEntry);
    }

    @GuardedBy("Segment.this")
    void recordWrite(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      drainRecencyQueue();
      this.evictionQueue.add(paramReferenceEntry);
      long l;
      if (CustomConcurrentHashMap.this.expires())
      {
        if (!CustomConcurrentHashMap.this.expiresAfterAccess())
          break label61;
        l = CustomConcurrentHashMap.this.expireAfterAccessNanos;
      }
      while (true)
      {
        recordExpirationTime(paramReferenceEntry, l);
        this.expirationQueue.add(paramReferenceEntry);
        return;
        label61: l = CustomConcurrentHashMap.this.expireAfterWriteNanos;
      }
    }

    V remove(Object paramObject, int paramInt)
    {
      lock();
      while (true)
      {
        try
        {
          preWriteCleanup();
          (-1 + this.count);
          AtomicReferenceArray localAtomicReferenceArray = this.table;
          int i = paramInt & -1 + localAtomicReferenceArray.length();
          CustomConcurrentHashMap.ReferenceEntry localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(i);
          localObject2 = localReferenceEntry1;
          if (localObject2 == null)
            break;
          Object localObject3 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getKey();
          if ((((CustomConcurrentHashMap.ReferenceEntry)localObject2).getHash() == paramInt) && (localObject3 != null) && (CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramObject, localObject3)))
          {
            Object localObject4 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getValueReference().get();
            if (localObject4 != null)
              continue;
            unsetLiveEntry((CustomConcurrentHashMap.ReferenceEntry)localObject2, paramInt);
            return localObject4;
            this.modCount = (1 + this.modCount);
            CustomConcurrentHashMap.ReferenceEntry localReferenceEntry3 = removeFromChain(localReferenceEntry1, (CustomConcurrentHashMap.ReferenceEntry)localObject2);
            int j = -1 + this.count;
            localAtomicReferenceArray.set(i, localReferenceEntry3);
            this.count = j;
            continue;
          }
        }
        finally
        {
          unlock();
          postWriteCleanup();
        }
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry2 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getNext();
        Object localObject2 = localReferenceEntry2;
      }
      unlock();
      postWriteCleanup();
      return (TV)null;
    }

    boolean remove(Object paramObject1, int paramInt, Object paramObject2)
    {
      Preconditions.checkNotNull(paramObject2);
      lock();
      try
      {
        preWriteCleanup();
        (-1 + this.count);
        AtomicReferenceArray localAtomicReferenceArray = this.table;
        int i = paramInt & -1 + localAtomicReferenceArray.length();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(i);
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry2;
        for (Object localObject2 = localReferenceEntry1; localObject2 != null; localObject2 = localReferenceEntry2)
        {
          Object localObject3 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getKey();
          if ((((CustomConcurrentHashMap.ReferenceEntry)localObject2).getHash() == paramInt) && (localObject3 != null) && (CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramObject1, localObject3)))
          {
            Object localObject4 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getValueReference().get();
            if (localObject4 == null)
              unsetLiveEntry((CustomConcurrentHashMap.ReferenceEntry)localObject2, paramInt);
            do
              return false;
            while (!CustomConcurrentHashMap.this.valueEquivalence.equivalent(paramObject2, localObject4));
            this.modCount = (1 + this.modCount);
            CustomConcurrentHashMap.ReferenceEntry localReferenceEntry3 = removeFromChain(localReferenceEntry1, (CustomConcurrentHashMap.ReferenceEntry)localObject2);
            int j = -1 + this.count;
            localAtomicReferenceArray.set(i, localReferenceEntry3);
            this.count = j;
            return true;
          }
          localReferenceEntry2 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getNext();
        }
        return false;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
      throw localObject1;
    }

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> removeFromChain(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry1, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry2)
    {
      this.evictionQueue.remove(paramReferenceEntry2);
      this.expirationQueue.remove(paramReferenceEntry2);
      CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = paramReferenceEntry2.getNext();
      Object localObject = paramReferenceEntry1;
      if (localObject != paramReferenceEntry2)
      {
        if (CustomConcurrentHashMap.this.isCollected((CustomConcurrentHashMap.ReferenceEntry)localObject))
          unsetLiveEntry((CustomConcurrentHashMap.ReferenceEntry)localObject, ((CustomConcurrentHashMap.ReferenceEntry)localObject).getHash());
        while (true)
        {
          localObject = ((CustomConcurrentHashMap.ReferenceEntry)localObject).getNext();
          break;
          localReferenceEntry = CustomConcurrentHashMap.this.copyEntry((CustomConcurrentHashMap.ReferenceEntry)localObject, localReferenceEntry);
        }
      }
      return (CustomConcurrentHashMap.ReferenceEntry<K, V>)localReferenceEntry;
    }

    V replace(K paramK, int paramInt, V paramV)
    {
      Preconditions.checkNotNull(paramV);
      lock();
      try
      {
        preWriteCleanup();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
        for (Object localObject2 = getFirst(paramInt); localObject2 != null; localObject2 = localReferenceEntry)
        {
          Object localObject3 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getKey();
          if ((((CustomConcurrentHashMap.ReferenceEntry)localObject2).getHash() == paramInt) && (localObject3 != null) && (CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramK, localObject3)))
          {
            Object localObject4 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getValueReference().get();
            if (localObject4 == null)
            {
              unsetLiveEntry((CustomConcurrentHashMap.ReferenceEntry)localObject2, paramInt);
              return null;
            }
            setValue((CustomConcurrentHashMap.ReferenceEntry)localObject2, paramV);
            return localObject4;
          }
          localReferenceEntry = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getNext();
        }
        return null;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
      throw localObject1;
    }

    boolean replace(K paramK, int paramInt, V paramV1, V paramV2)
    {
      Preconditions.checkNotNull(paramV1);
      Preconditions.checkNotNull(paramV2);
      lock();
      try
      {
        preWriteCleanup();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
        for (Object localObject2 = getFirst(paramInt); localObject2 != null; localObject2 = localReferenceEntry)
        {
          Object localObject3 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getKey();
          if ((((CustomConcurrentHashMap.ReferenceEntry)localObject2).getHash() == paramInt) && (localObject3 != null) && (CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramK, localObject3)))
          {
            Object localObject4 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getValueReference().get();
            if (localObject4 == null)
            {
              unsetLiveEntry((CustomConcurrentHashMap.ReferenceEntry)localObject2, paramInt);
              return false;
            }
            if (CustomConcurrentHashMap.this.valueEquivalence.equivalent(paramV1, localObject4))
            {
              setValue((CustomConcurrentHashMap.ReferenceEntry)localObject2, paramV2);
              return true;
            }
            recordLockedRead((CustomConcurrentHashMap.ReferenceEntry)localObject2);
            return false;
          }
          localReferenceEntry = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getNext();
        }
        return false;
      }
      finally
      {
        unlock();
        postWriteCleanup();
      }
      throw localObject1;
    }

    void runCleanup()
    {
      runLockedCleanup();
      runUnlockedCleanup();
    }

    void runLockedCleanup()
    {
      lock();
      try
      {
        expireEntries();
        processPendingCleanup();
        this.readCount.set(0);
        return;
      }
      finally
      {
        unlock();
      }
      throw localObject;
    }

    void runUnlockedCleanup()
    {
      CustomConcurrentHashMap.this.processPendingNotifications();
    }

    @GuardedBy("Segment.this")
    void setValue(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, V paramV)
    {
      recordWrite(paramReferenceEntry);
      paramReferenceEntry.setValueReference(CustomConcurrentHashMap.this.newValueReference(paramReferenceEntry, paramV));
    }

    void tryExpireEntries()
    {
      if (tryLock());
      try
      {
        expireEntries();
        return;
      }
      finally
      {
        unlock();
      }
      throw localObject;
    }

    @GuardedBy("Segment.this")
    boolean unsetEntry(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, int paramInt)
    {
      for (CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = getFirst(paramInt); localReferenceEntry != null; localReferenceEntry = localReferenceEntry.getNext())
        if (localReferenceEntry == paramReferenceEntry)
          return unsetLiveEntry(paramReferenceEntry, paramInt);
      return false;
    }

    boolean unsetKey(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, int paramInt)
    {
      lock();
      try
      {
        int i = -1 + this.count;
        AtomicReferenceArray localAtomicReferenceArray = this.table;
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
        for (Object localObject2 = (CustomConcurrentHashMap.ReferenceEntry)localAtomicReferenceArray.get(paramInt & -1 + localAtomicReferenceArray.length()); localObject2 != null; localObject2 = localReferenceEntry)
        {
          if (localObject2 == paramReferenceEntry)
          {
            this.modCount = (1 + this.modCount);
            CustomConcurrentHashMap.this.enqueueNotification(((CustomConcurrentHashMap.ReferenceEntry)localObject2).getKey(), paramInt, ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getValueReference());
            enqueueCleanup((CustomConcurrentHashMap.ReferenceEntry)localObject2);
            this.count = i;
            return true;
          }
          localReferenceEntry = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getNext();
        }
        return false;
      }
      finally
      {
        unlock();
      }
      throw localObject1;
    }

    @GuardedBy("Segment.this")
    boolean unsetLiveEntry(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, int paramInt)
    {
      if (CustomConcurrentHashMap.this.isUnset(paramReferenceEntry));
      int i;
      CustomConcurrentHashMap.ValueReference localValueReference;
      do
      {
        return false;
        i = -1 + this.count;
        this.modCount = (1 + this.modCount);
        localValueReference = paramReferenceEntry.getValueReference();
      }
      while (localValueReference.isComputingReference());
      Object localObject = paramReferenceEntry.getKey();
      CustomConcurrentHashMap.this.enqueueNotification(localObject, paramInt, localValueReference);
      enqueueCleanup(paramReferenceEntry);
      this.count = i;
      return true;
    }

    boolean unsetValue(K paramK, int paramInt, CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      lock();
      try
      {
        int i = -1 + this.count;
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
        for (Object localObject2 = getFirst(paramInt); localObject2 != null; localObject2 = localReferenceEntry)
        {
          Object localObject3 = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getKey();
          if ((((CustomConcurrentHashMap.ReferenceEntry)localObject2).getHash() == paramInt) && (localObject3 != null) && (CustomConcurrentHashMap.this.keyEquivalence.equivalent(paramK, localObject3)))
          {
            if (((CustomConcurrentHashMap.ReferenceEntry)localObject2).getValueReference() == paramValueReference)
            {
              this.modCount = (1 + this.modCount);
              CustomConcurrentHashMap.this.enqueueNotification(paramK, paramInt, paramValueReference);
              enqueueCleanup((CustomConcurrentHashMap.ReferenceEntry)localObject2);
              this.count = i;
              return true;
            }
            return false;
          }
          localReferenceEntry = ((CustomConcurrentHashMap.ReferenceEntry)localObject2).getNext();
        }
        return false;
      }
      finally
      {
        unlock();
      }
      throw localObject1;
    }

    @VisibleForTesting
    class EvictionQueue extends AbstractQueue<CustomConcurrentHashMap.ReferenceEntry<K, V>>
    {

      @VisibleForTesting
      final CustomConcurrentHashMap.ReferenceEntry<K, V> head = new CustomConcurrentHashMap.ReferenceEntry()
      {
        CustomConcurrentHashMap.ReferenceEntry<K, V> nextEvictable = this;
        CustomConcurrentHashMap.ReferenceEntry<K, V> previousEvictable = this;

        public long getExpirationTime()
        {
          throw new UnsupportedOperationException();
        }

        public int getHash()
        {
          throw new UnsupportedOperationException();
        }

        public K getKey()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getNext()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
        {
          return this.nextEvictable;
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
        {
          return this.previousEvictable;
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ValueReference<K, V> getValueReference()
        {
          throw new UnsupportedOperationException();
        }

        public void notifyKeyReclaimed()
        {
        }

        public void notifyValueReclaimed(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
        {
          throw new UnsupportedOperationException();
        }

        public void setExpirationTime(long paramLong)
        {
          throw new UnsupportedOperationException();
        }

        public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          this.nextEvictable = paramReferenceEntry;
        }

        public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          throw new UnsupportedOperationException();
        }

        public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          this.previousEvictable = paramReferenceEntry;
        }

        public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          throw new UnsupportedOperationException();
        }

        public void setValueReference(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
        {
          throw new UnsupportedOperationException();
        }
      };

      EvictionQueue()
      {
      }

      public void clear()
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
        for (Object localObject = this.head.getNextEvictable(); localObject != this.head; localObject = localReferenceEntry)
        {
          localReferenceEntry = ((CustomConcurrentHashMap.ReferenceEntry)localObject).getNextEvictable();
          CustomConcurrentHashMap.nullifyEvictable((CustomConcurrentHashMap.ReferenceEntry)localObject);
        }
        this.head.setNextEvictable(this.head);
        this.head.setPreviousEvictable(this.head);
      }

      public boolean contains(Object paramObject)
      {
        return ((CustomConcurrentHashMap.ReferenceEntry)paramObject).getNextEvictable() != CustomConcurrentHashMap.NullEntry.INSTANCE;
      }

      public boolean isEmpty()
      {
        return this.head.getNextEvictable() == this.head;
      }

      public Iterator<CustomConcurrentHashMap.ReferenceEntry<K, V>> iterator()
      {
        return new AbstractLinkedIterator(peek())
        {
          protected CustomConcurrentHashMap.ReferenceEntry<K, V> computeNext(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
          {
            CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = paramReferenceEntry.getNextEvictable();
            if (localReferenceEntry == CustomConcurrentHashMap.Segment.EvictionQueue.this.head)
              localReferenceEntry = null;
            return localReferenceEntry;
          }
        };
      }

      public boolean offer(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
      {
        CustomConcurrentHashMap.connectEvictables(paramReferenceEntry.getPreviousEvictable(), paramReferenceEntry.getNextEvictable());
        CustomConcurrentHashMap.connectEvictables(this.head.getPreviousEvictable(), paramReferenceEntry);
        CustomConcurrentHashMap.connectEvictables(paramReferenceEntry, this.head);
        return true;
      }

      public CustomConcurrentHashMap.ReferenceEntry<K, V> peek()
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = this.head.getNextEvictable();
        if (localReferenceEntry == this.head)
          localReferenceEntry = null;
        return localReferenceEntry;
      }

      public CustomConcurrentHashMap.ReferenceEntry<K, V> poll()
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = this.head.getNextEvictable();
        if (localReferenceEntry == this.head)
          return null;
        remove(localReferenceEntry);
        return localReferenceEntry;
      }

      public boolean remove(Object paramObject)
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)paramObject;
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1.getPreviousEvictable();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry3 = localReferenceEntry1.getNextEvictable();
        CustomConcurrentHashMap.connectEvictables(localReferenceEntry2, localReferenceEntry3);
        CustomConcurrentHashMap.nullifyEvictable(localReferenceEntry1);
        return localReferenceEntry3 != CustomConcurrentHashMap.NullEntry.INSTANCE;
      }

      public int size()
      {
        int i = 0;
        for (CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = this.head.getNextEvictable(); localReferenceEntry != this.head; localReferenceEntry = localReferenceEntry.getNextEvictable())
          i++;
        return i;
      }
    }

    @VisibleForTesting
    class ExpirationQueue extends AbstractQueue<CustomConcurrentHashMap.ReferenceEntry<K, V>>
    {

      @VisibleForTesting
      final CustomConcurrentHashMap.ReferenceEntry<K, V> head = new CustomConcurrentHashMap.ReferenceEntry()
      {
        CustomConcurrentHashMap.ReferenceEntry<K, V> nextExpirable = this;
        CustomConcurrentHashMap.ReferenceEntry<K, V> previousExpirable = this;

        public long getExpirationTime()
        {
          return 9223372036854775807L;
        }

        public int getHash()
        {
          throw new UnsupportedOperationException();
        }

        public K getKey()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getNext()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
        {
          return this.nextExpirable;
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
        {
          throw new UnsupportedOperationException();
        }

        public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
        {
          return this.previousExpirable;
        }

        public CustomConcurrentHashMap.ValueReference<K, V> getValueReference()
        {
          throw new UnsupportedOperationException();
        }

        public void notifyKeyReclaimed()
        {
        }

        public void notifyValueReclaimed(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
        {
          throw new UnsupportedOperationException();
        }

        public void setExpirationTime(long paramLong)
        {
        }

        public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          throw new UnsupportedOperationException();
        }

        public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          this.nextExpirable = paramReferenceEntry;
        }

        public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          throw new UnsupportedOperationException();
        }

        public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
        {
          this.previousExpirable = paramReferenceEntry;
        }

        public void setValueReference(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
        {
          throw new UnsupportedOperationException();
        }
      };

      ExpirationQueue()
      {
      }

      public void clear()
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry;
        for (Object localObject = this.head.getNextExpirable(); localObject != this.head; localObject = localReferenceEntry)
        {
          localReferenceEntry = ((CustomConcurrentHashMap.ReferenceEntry)localObject).getNextExpirable();
          CustomConcurrentHashMap.nullifyExpirable((CustomConcurrentHashMap.ReferenceEntry)localObject);
        }
        this.head.setNextExpirable(this.head);
        this.head.setPreviousExpirable(this.head);
      }

      public boolean contains(Object paramObject)
      {
        return ((CustomConcurrentHashMap.ReferenceEntry)paramObject).getNextExpirable() != CustomConcurrentHashMap.NullEntry.INSTANCE;
      }

      public boolean isEmpty()
      {
        return this.head.getNextExpirable() == this.head;
      }

      public Iterator<CustomConcurrentHashMap.ReferenceEntry<K, V>> iterator()
      {
        return new AbstractLinkedIterator(peek())
        {
          protected CustomConcurrentHashMap.ReferenceEntry<K, V> computeNext(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
          {
            CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = paramReferenceEntry.getNextExpirable();
            if (localReferenceEntry == CustomConcurrentHashMap.Segment.ExpirationQueue.this.head)
              localReferenceEntry = null;
            return localReferenceEntry;
          }
        };
      }

      public boolean offer(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
      {
        CustomConcurrentHashMap.connectExpirables(paramReferenceEntry.getPreviousExpirable(), paramReferenceEntry.getNextExpirable());
        CustomConcurrentHashMap.connectExpirables(this.head.getPreviousExpirable(), paramReferenceEntry);
        CustomConcurrentHashMap.connectExpirables(paramReferenceEntry, this.head);
        return true;
      }

      public CustomConcurrentHashMap.ReferenceEntry<K, V> peek()
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = this.head.getNextExpirable();
        if (localReferenceEntry == this.head)
          localReferenceEntry = null;
        return localReferenceEntry;
      }

      public CustomConcurrentHashMap.ReferenceEntry<K, V> poll()
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = this.head.getNextExpirable();
        if (localReferenceEntry == this.head)
          return null;
        remove(localReferenceEntry);
        return localReferenceEntry;
      }

      public boolean remove(Object paramObject)
      {
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry1 = (CustomConcurrentHashMap.ReferenceEntry)paramObject;
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1.getPreviousExpirable();
        CustomConcurrentHashMap.ReferenceEntry localReferenceEntry3 = localReferenceEntry1.getNextExpirable();
        CustomConcurrentHashMap.connectExpirables(localReferenceEntry2, localReferenceEntry3);
        CustomConcurrentHashMap.nullifyExpirable(localReferenceEntry1);
        return localReferenceEntry3 != CustomConcurrentHashMap.NullEntry.INSTANCE;
      }

      public int size()
      {
        int i = 0;
        for (CustomConcurrentHashMap.ReferenceEntry localReferenceEntry = this.head.getNextExpirable(); localReferenceEntry != this.head; localReferenceEntry = localReferenceEntry.getNextExpirable())
          i++;
        return i;
      }
    }
  }

  private static class SerializationProxy<K, V> extends CustomConcurrentHashMap.AbstractSerializationProxy<K, V>
  {
    private static final long serialVersionUID = 2L;

    SerializationProxy(CustomConcurrentHashMap.Strength paramStrength1, CustomConcurrentHashMap.Strength paramStrength2, Equivalence<Object> paramEquivalence1, Equivalence<Object> paramEquivalence2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, MapEvictionListener<? super K, ? super V> paramMapEvictionListener, ConcurrentMap<K, V> paramConcurrentMap)
    {
      super(paramStrength2, paramEquivalence1, paramEquivalence2, paramLong1, paramLong2, paramInt1, paramInt2, paramMapEvictionListener, paramConcurrentMap);
    }

    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      this.delegate = readMapMaker(paramObjectInputStream).makeMap();
      readEntries(paramObjectInputStream);
    }

    private Object readResolve()
    {
      return this.delegate;
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      writeMapTo(paramObjectOutputStream);
    }
  }

  private static class SoftEntry<K, V> extends FinalizableSoftReference<K>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {
    final int hash;
    final CustomConcurrentHashMap<K, V> map;
    final CustomConcurrentHashMap.ReferenceEntry<K, V> next;
    volatile CustomConcurrentHashMap.ValueReference<K, V> valueReference = CustomConcurrentHashMap.unset();

    SoftEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(CustomConcurrentHashMap.QueueHolder.queue);
      this.map = paramCustomConcurrentHashMap;
      this.hash = paramInt;
      this.next = paramReferenceEntry;
    }

    public void finalizeReferent()
    {
      notifyKeyReclaimed();
    }

    public long getExpirationTime()
    {
      throw new UnsupportedOperationException();
    }

    public int getHash()
    {
      return this.hash;
    }

    public K getKey()
    {
      return get();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNext()
    {
      return this.next;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ValueReference<K, V> getValueReference()
    {
      return this.valueReference;
    }

    public void notifyKeyReclaimed()
    {
      this.map.reclaimKey(this);
    }

    public void notifyValueReclaimed(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      this.map.reclaimValue(this, paramValueReference);
    }

    public void setExpirationTime(long paramLong)
    {
      throw new UnsupportedOperationException();
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setValueReference(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      CustomConcurrentHashMap.ValueReference localValueReference = this.valueReference;
      this.valueReference = paramValueReference;
      localValueReference.clear();
    }
  }

  private static class SoftEvictableEntry<K, V> extends CustomConcurrentHashMap.SoftEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousEvictable = CustomConcurrentHashMap.nullEntry();

    SoftEvictableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      return this.nextEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      return this.previousEvictable;
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextEvictable = paramReferenceEntry;
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousEvictable = paramReferenceEntry;
    }
  }

  private static class SoftExpirableEntry<K, V> extends CustomConcurrentHashMap.SoftEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextExpirable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousExpirable = CustomConcurrentHashMap.nullEntry();
    volatile long time = 9223372036854775807L;

    SoftExpirableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public long getExpirationTime()
    {
      return this.time;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      return this.nextExpirable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      return this.previousExpirable;
    }

    public void setExpirationTime(long paramLong)
    {
      this.time = paramLong;
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextExpirable = paramReferenceEntry;
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousExpirable = paramReferenceEntry;
    }
  }

  private static class SoftExpirableEvictableEntry<K, V> extends CustomConcurrentHashMap.SoftEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextExpirable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousExpirable = CustomConcurrentHashMap.nullEntry();
    volatile long time = 9223372036854775807L;

    SoftExpirableEvictableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public long getExpirationTime()
    {
      return this.time;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      return this.nextEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      return this.nextExpirable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      return this.previousEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      return this.previousExpirable;
    }

    public void setExpirationTime(long paramLong)
    {
      this.time = paramLong;
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextEvictable = paramReferenceEntry;
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextExpirable = paramReferenceEntry;
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousEvictable = paramReferenceEntry;
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousExpirable = paramReferenceEntry;
    }
  }

  private static class SoftValueReference<K, V> extends FinalizableSoftReference<V>
    implements CustomConcurrentHashMap.ValueReference<K, V>
  {
    final CustomConcurrentHashMap.ReferenceEntry<K, V> entry;

    SoftValueReference(V paramV, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(CustomConcurrentHashMap.QueueHolder.queue);
      this.entry = paramReferenceEntry;
    }

    public CustomConcurrentHashMap.ValueReference<K, V> copyFor(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return new SoftValueReference(get(), paramReferenceEntry);
    }

    public void finalizeReferent()
    {
      this.entry.notifyValueReclaimed(this);
    }

    public boolean isComputingReference()
    {
      return false;
    }

    public void notifyValueReclaimed()
    {
      finalizeReferent();
    }

    public V waitForValue()
    {
      return get();
    }
  }

  static abstract enum Strength
  {
    static
    {
      SOFT = new Strength("SOFT", 1)
      {
        Equivalence<Object> defaultEquivalence()
        {
          return Equivalences.identity();
        }

        <K, V> CustomConcurrentHashMap.ValueReference<K, V> referenceValue(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, V paramV)
        {
          return new CustomConcurrentHashMap.SoftValueReference(paramV, paramReferenceEntry);
        }
      };
      WEAK = new Strength("WEAK", 2)
      {
        Equivalence<Object> defaultEquivalence()
        {
          return Equivalences.identity();
        }

        <K, V> CustomConcurrentHashMap.ValueReference<K, V> referenceValue(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, V paramV)
        {
          return new CustomConcurrentHashMap.WeakValueReference(paramV, paramReferenceEntry);
        }
      };
      Strength[] arrayOfStrength = new Strength[3];
      arrayOfStrength[0] = STRONG;
      arrayOfStrength[1] = SOFT;
      arrayOfStrength[2] = WEAK;
      $VALUES = arrayOfStrength;
    }

    abstract Equivalence<Object> defaultEquivalence();

    abstract <K, V> CustomConcurrentHashMap.ValueReference<K, V> referenceValue(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry, V paramV);
  }

  private static class StrongEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {
    final int hash;
    final K key;
    final CustomConcurrentHashMap<K, V> map;
    final CustomConcurrentHashMap.ReferenceEntry<K, V> next;
    volatile CustomConcurrentHashMap.ValueReference<K, V> valueReference = CustomConcurrentHashMap.unset();

    StrongEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.map = paramCustomConcurrentHashMap;
      this.key = paramK;
      this.hash = paramInt;
      this.next = paramReferenceEntry;
    }

    public long getExpirationTime()
    {
      throw new UnsupportedOperationException();
    }

    public int getHash()
    {
      return this.hash;
    }

    public K getKey()
    {
      return this.key;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNext()
    {
      return this.next;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ValueReference<K, V> getValueReference()
    {
      return this.valueReference;
    }

    public void notifyKeyReclaimed()
    {
    }

    public void notifyValueReclaimed(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      this.map.reclaimValue(this, paramValueReference);
    }

    public void setExpirationTime(long paramLong)
    {
      throw new UnsupportedOperationException();
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setValueReference(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      CustomConcurrentHashMap.ValueReference localValueReference = this.valueReference;
      this.valueReference = paramValueReference;
      localValueReference.clear();
    }
  }

  private static class StrongEvictableEntry<K, V> extends CustomConcurrentHashMap.StrongEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousEvictable = CustomConcurrentHashMap.nullEntry();

    StrongEvictableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      return this.nextEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      return this.previousEvictable;
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextEvictable = paramReferenceEntry;
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousEvictable = paramReferenceEntry;
    }
  }

  private static class StrongExpirableEntry<K, V> extends CustomConcurrentHashMap.StrongEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextExpirable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousExpirable = CustomConcurrentHashMap.nullEntry();
    volatile long time = 9223372036854775807L;

    StrongExpirableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public long getExpirationTime()
    {
      return this.time;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      return this.nextExpirable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      return this.previousExpirable;
    }

    public void setExpirationTime(long paramLong)
    {
      this.time = paramLong;
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextExpirable = paramReferenceEntry;
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousExpirable = paramReferenceEntry;
    }
  }

  private static class StrongExpirableEvictableEntry<K, V> extends CustomConcurrentHashMap.StrongEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextExpirable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousExpirable = CustomConcurrentHashMap.nullEntry();
    volatile long time = 9223372036854775807L;

    StrongExpirableEvictableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public long getExpirationTime()
    {
      return this.time;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      return this.nextEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      return this.nextExpirable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      return this.previousEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      return this.previousExpirable;
    }

    public void setExpirationTime(long paramLong)
    {
      this.time = paramLong;
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextEvictable = paramReferenceEntry;
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextExpirable = paramReferenceEntry;
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousEvictable = paramReferenceEntry;
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousExpirable = paramReferenceEntry;
    }
  }

  private static class StrongValueReference<K, V>
    implements CustomConcurrentHashMap.ValueReference<K, V>
  {
    final V referent;

    StrongValueReference(V paramV)
    {
      this.referent = paramV;
    }

    public void clear()
    {
    }

    public CustomConcurrentHashMap.ValueReference<K, V> copyFor(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }

    public V get()
    {
      return this.referent;
    }

    public boolean isComputingReference()
    {
      return false;
    }

    public void notifyValueReclaimed()
    {
    }

    public V waitForValue()
    {
      return get();
    }
  }

  final class ValueIterator extends CustomConcurrentHashMap<K, V>.HashIterator
    implements Iterator<V>
  {
    ValueIterator()
    {
      super();
    }

    public V next()
    {
      return nextEntry().getValue();
    }
  }

  static abstract interface ValueReference<K, V>
  {
    public abstract void clear();

    public abstract ValueReference<K, V> copyFor(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry);

    public abstract V get();

    public abstract boolean isComputingReference();

    public abstract void notifyValueReclaimed();

    public abstract V waitForValue()
      throws InterruptedException;
  }

  final class Values extends AbstractCollection<V>
  {
    Values()
    {
    }

    public void clear()
    {
      CustomConcurrentHashMap.this.clear();
    }

    public boolean contains(Object paramObject)
    {
      return CustomConcurrentHashMap.this.containsValue(paramObject);
    }

    public boolean isEmpty()
    {
      return CustomConcurrentHashMap.this.isEmpty();
    }

    public Iterator<V> iterator()
    {
      return new CustomConcurrentHashMap.ValueIterator(CustomConcurrentHashMap.this);
    }

    public int size()
    {
      return CustomConcurrentHashMap.this.size();
    }
  }

  private static class WeakEntry<K, V> extends FinalizableWeakReference<K>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {
    final int hash;
    final CustomConcurrentHashMap<K, V> map;
    final CustomConcurrentHashMap.ReferenceEntry<K, V> next;
    volatile CustomConcurrentHashMap.ValueReference<K, V> valueReference = CustomConcurrentHashMap.unset();

    WeakEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(CustomConcurrentHashMap.QueueHolder.queue);
      this.map = paramCustomConcurrentHashMap;
      this.hash = paramInt;
      this.next = paramReferenceEntry;
    }

    public void finalizeReferent()
    {
      notifyKeyReclaimed();
    }

    public long getExpirationTime()
    {
      throw new UnsupportedOperationException();
    }

    public int getHash()
    {
      return this.hash;
    }

    public K getKey()
    {
      return get();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNext()
    {
      return this.next;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      throw new UnsupportedOperationException();
    }

    public CustomConcurrentHashMap.ValueReference<K, V> getValueReference()
    {
      return this.valueReference;
    }

    public void notifyKeyReclaimed()
    {
      this.map.reclaimKey(this);
    }

    public void notifyValueReclaimed(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      this.map.reclaimValue(this, paramValueReference);
    }

    public void setExpirationTime(long paramLong)
    {
      throw new UnsupportedOperationException();
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      throw new UnsupportedOperationException();
    }

    public void setValueReference(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      CustomConcurrentHashMap.ValueReference localValueReference = this.valueReference;
      this.valueReference = paramValueReference;
      localValueReference.clear();
    }
  }

  private static class WeakEvictableEntry<K, V> extends CustomConcurrentHashMap.WeakEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousEvictable = CustomConcurrentHashMap.nullEntry();

    WeakEvictableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      return this.nextEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      return this.previousEvictable;
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextEvictable = paramReferenceEntry;
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousEvictable = paramReferenceEntry;
    }
  }

  private static class WeakExpirableEntry<K, V> extends CustomConcurrentHashMap.WeakEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextExpirable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousExpirable = CustomConcurrentHashMap.nullEntry();
    volatile long time = 9223372036854775807L;

    WeakExpirableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public long getExpirationTime()
    {
      return this.time;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      return this.nextExpirable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      return this.previousExpirable;
    }

    public void setExpirationTime(long paramLong)
    {
      this.time = paramLong;
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextExpirable = paramReferenceEntry;
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousExpirable = paramReferenceEntry;
    }
  }

  private static class WeakExpirableEvictableEntry<K, V> extends CustomConcurrentHashMap.WeakEntry<K, V>
    implements CustomConcurrentHashMap.ReferenceEntry<K, V>
  {

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> nextExpirable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousEvictable = CustomConcurrentHashMap.nullEntry();

    @GuardedBy("Segment.this")
    CustomConcurrentHashMap.ReferenceEntry<K, V> previousExpirable = CustomConcurrentHashMap.nullEntry();
    volatile long time = 9223372036854775807L;

    WeakExpirableEvictableEntry(CustomConcurrentHashMap<K, V> paramCustomConcurrentHashMap, K paramK, int paramInt, @Nullable CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(paramK, paramInt, paramReferenceEntry);
    }

    public long getExpirationTime()
    {
      return this.time;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextEvictable()
    {
      return this.nextEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getNextExpirable()
    {
      return this.nextExpirable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousEvictable()
    {
      return this.previousEvictable;
    }

    public CustomConcurrentHashMap.ReferenceEntry<K, V> getPreviousExpirable()
    {
      return this.previousExpirable;
    }

    public void setExpirationTime(long paramLong)
    {
      this.time = paramLong;
    }

    public void setNextEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextEvictable = paramReferenceEntry;
    }

    public void setNextExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.nextExpirable = paramReferenceEntry;
    }

    public void setPreviousEvictable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousEvictable = paramReferenceEntry;
    }

    public void setPreviousExpirable(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      this.previousExpirable = paramReferenceEntry;
    }
  }

  private static class WeakValueReference<K, V> extends FinalizableWeakReference<V>
    implements CustomConcurrentHashMap.ValueReference<K, V>
  {
    final CustomConcurrentHashMap.ReferenceEntry<K, V> entry;

    WeakValueReference(V paramV, CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      super(CustomConcurrentHashMap.QueueHolder.queue);
      this.entry = paramReferenceEntry;
    }

    public CustomConcurrentHashMap.ValueReference<K, V> copyFor(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return new WeakValueReference(get(), paramReferenceEntry);
    }

    public void finalizeReferent()
    {
      this.entry.notifyValueReclaimed(this);
    }

    public boolean isComputingReference()
    {
      return false;
    }

    public void notifyValueReclaimed()
    {
      finalizeReferent();
    }

    public V waitForValue()
    {
      return get();
    }
  }

  final class WriteThroughEntry extends AbstractMapEntry<K, V>
  {
    final K key;
    V value;

    WriteThroughEntry(V arg2)
    {
      Object localObject1;
      this.key = localObject1;
      Object localObject2;
      this.value = localObject2;
    }

    public boolean equals(@Nullable Object paramObject)
    {
      boolean bool1 = paramObject instanceof Map.Entry;
      int i = 0;
      if (bool1)
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        boolean bool2 = this.key.equals(localEntry.getKey());
        i = 0;
        if (bool2)
        {
          boolean bool3 = this.value.equals(localEntry.getValue());
          i = 0;
          if (bool3)
            i = 1;
        }
      }
      return i;
    }

    public K getKey()
    {
      return this.key;
    }

    public V getValue()
    {
      return this.value;
    }

    public int hashCode()
    {
      return this.key.hashCode() ^ this.value.hashCode();
    }

    public V setValue(V paramV)
    {
      Object localObject = CustomConcurrentHashMap.this.put(this.key, paramV);
      this.value = paramV;
      return localObject;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.CustomConcurrentHashMap
 * JD-Core Version:    0.6.0
 */