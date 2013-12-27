package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

class ComputingConcurrentHashMap<K, V> extends CustomConcurrentHashMap<K, V>
  implements MapMaker.Cache<K, V>
{
  private static final long serialVersionUID = 2L;
  final Function<? super K, ? extends V> computingFunction;

  ComputingConcurrentHashMap(MapMaker paramMapMaker, Function<? super K, ? extends V> paramFunction)
  {
    super(paramMapMaker);
    this.computingFunction = ((Function)Preconditions.checkNotNull(paramFunction));
  }

  public V apply(K paramK)
  {
    int i = hash(paramK);
    return segmentFor(i).compute(paramK, i);
  }

  public ConcurrentMap<K, V> asMap()
  {
    return this;
  }

  CustomConcurrentHashMap<K, V>.Segment createSegment(int paramInt1, int paramInt2)
  {
    return new ComputingSegment(paramInt1, paramInt2);
  }

  ComputingConcurrentHashMap<K, V>.ComputingSegment segmentFor(int paramInt)
  {
    return (ComputingSegment)super.segmentFor(paramInt);
  }

  Object writeReplace()
  {
    return new ComputingSerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.evictionListener, this, this.computingFunction);
  }

  private static class ComputationExceptionReference<K, V>
    implements CustomConcurrentHashMap.ValueReference<K, V>
  {
    final Throwable t;

    ComputationExceptionReference(Throwable paramThrowable)
    {
      this.t = paramThrowable;
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
      return null;
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
      throw new AsynchronousComputationException(this.t);
    }
  }

  private static class ComputedReference<K, V>
    implements CustomConcurrentHashMap.ValueReference<K, V>
  {
    final V value;

    ComputedReference(@Nullable V paramV)
    {
      this.value = paramV;
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
      return this.value;
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

  class ComputingSegment extends CustomConcurrentHashMap.Segment
  {
    ComputingSegment(int paramInt1, int arg3)
    {
      super(paramInt1, i);
    }

    // ERROR //
    V compute(K paramK, int paramInt)
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: iload_2
      //   3: invokevirtual 20	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:get	(Ljava/lang/Object;I)Ljava/lang/Object;
      //   6: astore_3
      //   7: aload_3
      //   8: ifnull +5 -> 13
      //   11: aload_3
      //   12: areturn
      //   13: aload_0
      //   14: invokevirtual 24	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:lock	()V
      //   17: aload_0
      //   18: invokevirtual 27	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:preWriteCleanup	()V
      //   21: aload_0
      //   22: getfield 31	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:table	Ljava/util/concurrent/atomic/AtomicReferenceArray;
      //   25: astore 5
      //   27: iload_2
      //   28: iconst_m1
      //   29: aload 5
      //   31: invokevirtual 37	java/util/concurrent/atomic/AtomicReferenceArray:length	()I
      //   34: iadd
      //   35: iand
      //   36: istore 6
      //   38: aload 5
      //   40: iload 6
      //   42: invokevirtual 40	java/util/concurrent/atomic/AtomicReferenceArray:get	(I)Ljava/lang/Object;
      //   45: checkcast 42	com/google/common/collect/CustomConcurrentHashMap$ReferenceEntry
      //   48: astore 7
      //   50: aload 7
      //   52: astore 8
      //   54: aconst_null
      //   55: astore 9
      //   57: aload 8
      //   59: ifnull +99 -> 158
      //   62: aload 8
      //   64: invokeinterface 46 1 0
      //   69: astore 10
      //   71: aload 8
      //   73: invokeinterface 49 1 0
      //   78: iload_2
      //   79: if_icmpne +206 -> 285
      //   82: aload 10
      //   84: ifnull +201 -> 285
      //   87: aload_0
      //   88: getfield 10	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:this$0	Lcom/google/common/collect/ComputingConcurrentHashMap;
      //   91: getfield 55	com/google/common/collect/ComputingConcurrentHashMap:keyEquivalence	Lcom/google/common/base/Equivalence;
      //   94: aload_1
      //   95: aload 10
      //   97: invokeinterface 61 3 0
      //   102: ifeq +183 -> 285
      //   105: aload 8
      //   107: invokeinterface 65 1 0
      //   112: invokeinterface 71 1 0
      //   117: ifne +332 -> 449
      //   120: aload_0
      //   121: aload 8
      //   123: invokevirtual 75	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:getLiveValue	(Lcom/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)Ljava/lang/Object;
      //   126: astore_3
      //   127: aload_3
      //   128: ifnull +19 -> 147
      //   131: aload_0
      //   132: aload 8
      //   134: invokevirtual 79	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:recordLockedRead	(Lcom/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)V
      //   137: aload_0
      //   138: invokevirtual 82	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:unlock	()V
      //   141: aload_0
      //   142: invokevirtual 85	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postWriteCleanup	()V
      //   145: aload_3
      //   146: areturn
      //   147: aload_0
      //   148: aload 8
      //   150: iload_2
      //   151: invokevirtual 89	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:unsetLiveEntry	(Lcom/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;I)Z
      //   154: pop
      //   155: goto +294 -> 449
      //   158: aload 9
      //   160: ifnull +22 -> 182
      //   163: aload_0
      //   164: getfield 10	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:this$0	Lcom/google/common/collect/ComputingConcurrentHashMap;
      //   167: aload 9
      //   169: invokevirtual 93	com/google/common/collect/ComputingConcurrentHashMap:isUnset	(Lcom/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)Z
      //   172: istore 12
      //   174: aconst_null
      //   175: astore 13
      //   177: iload 12
      //   179: ifeq +57 -> 236
      //   182: new 95	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference
      //   185: dup
      //   186: aload_0
      //   187: getfield 10	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:this$0	Lcom/google/common/collect/ComputingConcurrentHashMap;
      //   190: aconst_null
      //   191: invokespecial 98	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference:<init>	(Lcom/google/common/collect/ComputingConcurrentHashMap;Lcom/google/common/collect/ComputingConcurrentHashMap$1;)V
      //   194: astore 24
      //   196: aload 9
      //   198: ifnonnull +25 -> 223
      //   201: aload_0
      //   202: getfield 10	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:this$0	Lcom/google/common/collect/ComputingConcurrentHashMap;
      //   205: aload_1
      //   206: iload_2
      //   207: aload 7
      //   209: invokevirtual 102	com/google/common/collect/ComputingConcurrentHashMap:newEntry	(Ljava/lang/Object;ILcom/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)Lcom/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;
      //   212: astore 9
      //   214: aload 5
      //   216: iload 6
      //   218: aload 9
      //   220: invokevirtual 106	java/util/concurrent/atomic/AtomicReferenceArray:set	(ILjava/lang/Object;)V
      //   223: aload 9
      //   225: aload 24
      //   227: invokeinterface 110 2 0
      //   232: aload 24
      //   234: astore 13
      //   236: aload_0
      //   237: invokevirtual 82	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:unlock	()V
      //   240: aload_0
      //   241: invokevirtual 85	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postWriteCleanup	()V
      //   244: aload 13
      //   246: ifnull +94 -> 340
      //   249: aload 9
      //   251: monitorenter
      //   252: aload 13
      //   254: aload_1
      //   255: iload_2
      //   256: invokevirtual 112	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference:compute	(Ljava/lang/Object;I)Ljava/lang/Object;
      //   259: astore_3
      //   260: aload 9
      //   262: monitorexit
      //   263: aload_3
      //   264: ldc 114
      //   266: invokestatic 120	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   269: pop
      //   270: aload_3
      //   271: ifnonnull +12 -> 283
      //   274: aload_0
      //   275: aload_1
      //   276: iload_2
      //   277: aload 13
      //   279: invokevirtual 124	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:clearValue	(Ljava/lang/Object;ILcom/google/common/collect/CustomConcurrentHashMap$ValueReference;)Z
      //   282: pop
      //   283: aload_3
      //   284: areturn
      //   285: aload 8
      //   287: invokeinterface 128 1 0
      //   292: astore 11
      //   294: aload 11
      //   296: astore 8
      //   298: goto -244 -> 54
      //   301: astore 4
      //   303: aload_0
      //   304: invokevirtual 82	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:unlock	()V
      //   307: aload_0
      //   308: invokevirtual 85	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postWriteCleanup	()V
      //   311: aload 4
      //   313: athrow
      //   314: astore 21
      //   316: aload 9
      //   318: monitorexit
      //   319: aload 21
      //   321: athrow
      //   322: astore 19
      //   324: aload_3
      //   325: ifnonnull +12 -> 337
      //   328: aload_0
      //   329: aload_1
      //   330: iload_2
      //   331: aload 13
      //   333: invokevirtual 124	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:clearValue	(Ljava/lang/Object;ILcom/google/common/collect/CustomConcurrentHashMap$ValueReference;)Z
      //   336: pop
      //   337: aload 19
      //   339: athrow
      //   340: iconst_0
      //   341: istore 14
      //   343: aload 9
      //   345: invokestatic 134	java/lang/Thread:holdsLock	(Ljava/lang/Object;)Z
      //   348: ifne +52 -> 400
      //   351: iconst_1
      //   352: istore 17
      //   354: iload 17
      //   356: ldc 136
      //   358: invokestatic 140	com/google/common/base/Preconditions:checkState	(ZLjava/lang/Object;)V
      //   361: aload 9
      //   363: invokeinterface 65 1 0
      //   368: invokeinterface 143 1 0
      //   373: astore 18
      //   375: aload 18
      //   377: ifnull +29 -> 406
      //   380: aload_0
      //   381: aload 9
      //   383: invokevirtual 146	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:recordRead	(Lcom/google/common/collect/CustomConcurrentHashMap$ReferenceEntry;)V
      //   386: iload 14
      //   388: ifeq +9 -> 397
      //   391: invokestatic 150	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   394: invokevirtual 153	java/lang/Thread:interrupt	()V
      //   397: aload 18
      //   399: areturn
      //   400: iconst_0
      //   401: istore 17
      //   403: goto -49 -> 354
      //   406: iload 14
      //   408: ifeq -408 -> 0
      //   411: invokestatic 150	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   414: invokevirtual 153	java/lang/Thread:interrupt	()V
      //   417: goto -417 -> 0
      //   420: astore 16
      //   422: iconst_1
      //   423: istore 14
      //   425: goto -82 -> 343
      //   428: astore 15
      //   430: iload 14
      //   432: ifeq +9 -> 441
      //   435: invokestatic 150	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   438: invokevirtual 153	java/lang/Thread:interrupt	()V
      //   441: aload 15
      //   443: athrow
      //   444: astore 4
      //   446: goto -143 -> 303
      //   449: aload 8
      //   451: astore 9
      //   453: goto -295 -> 158
      //
      // Exception table:
      //   from	to	target	type
      //   17	50	301	finally
      //   62	82	301	finally
      //   87	127	301	finally
      //   131	137	301	finally
      //   147	155	301	finally
      //   163	174	301	finally
      //   182	196	301	finally
      //   285	294	301	finally
      //   252	263	314	finally
      //   316	319	314	finally
      //   249	252	322	finally
      //   263	270	322	finally
      //   319	322	322	finally
      //   343	351	420	java/lang/InterruptedException
      //   354	375	420	java/lang/InterruptedException
      //   380	386	420	java/lang/InterruptedException
      //   343	351	428	finally
      //   354	375	428	finally
      //   380	386	428	finally
      //   201	223	444	finally
      //   223	232	444	finally
    }
  }

  static class ComputingSerializationProxy<K, V> extends CustomConcurrentHashMap.AbstractSerializationProxy<K, V>
  {
    private static final long serialVersionUID = 2L;
    transient MapMaker.Cache<K, V> cache;
    final Function<? super K, ? extends V> computingFunction;

    ComputingSerializationProxy(CustomConcurrentHashMap.Strength paramStrength1, CustomConcurrentHashMap.Strength paramStrength2, Equivalence<Object> paramEquivalence1, Equivalence<Object> paramEquivalence2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, MapEvictionListener<? super K, ? super V> paramMapEvictionListener, ConcurrentMap<K, V> paramConcurrentMap, Function<? super K, ? extends V> paramFunction)
    {
      super(paramStrength2, paramEquivalence1, paramEquivalence2, paramLong1, paramLong2, paramInt1, paramInt2, paramMapEvictionListener, paramConcurrentMap);
      this.computingFunction = paramFunction;
    }

    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      this.cache = readMapMaker(paramObjectInputStream).makeCache(this.computingFunction);
      this.delegate = this.cache.asMap();
      readEntries(paramObjectInputStream);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      writeMapTo(paramObjectOutputStream);
    }

    public V apply(@Nullable K paramK)
    {
      return this.cache.apply(paramK);
    }

    public ConcurrentMap<K, V> asMap()
    {
      return this.delegate;
    }

    Object readResolve()
    {
      return this.cache;
    }
  }

  private class ComputingValueReference
    implements CustomConcurrentHashMap.ValueReference<K, V>
  {

    @GuardedBy("ComputingValueReference.this")
    CustomConcurrentHashMap.ValueReference<K, V> computedReference = CustomConcurrentHashMap.unset();

    private ComputingValueReference()
    {
    }

    public void clear()
    {
      setValueReference(new ComputingConcurrentHashMap.ComputedReference(null));
    }

    V compute(K paramK, int paramInt)
    {
      Object localObject;
      try
      {
        localObject = ComputingConcurrentHashMap.this.computingFunction.apply(paramK);
        if (localObject == null)
        {
          String str = ComputingConcurrentHashMap.this.computingFunction + " returned null for key " + paramK + ".";
          setValueReference(new ComputingConcurrentHashMap.NullPointerExceptionReference(str));
          throw new NullPointerException(str);
        }
      }
      catch (ComputationException localComputationException)
      {
        setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference(localComputationException.getCause()));
        throw localComputationException;
      }
      catch (Throwable localThrowable)
      {
        setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference(localThrowable));
        throw new ComputationException(localThrowable);
      }
      setValueReference(new ComputingConcurrentHashMap.ComputedReference(localObject));
      ComputingConcurrentHashMap.this.segmentFor(paramInt).put(paramK, paramInt, localObject, true);
      return localObject;
    }

    public CustomConcurrentHashMap.ValueReference<K, V> copyFor(CustomConcurrentHashMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }

    public V get()
    {
      return null;
    }

    public boolean isComputingReference()
    {
      return true;
    }

    public void notifyValueReclaimed()
    {
    }

    void setValueReference(CustomConcurrentHashMap.ValueReference<K, V> paramValueReference)
    {
      monitorenter;
      try
      {
        if (this.computedReference == CustomConcurrentHashMap.UNSET)
        {
          this.computedReference = paramValueReference;
          notifyAll();
        }
        return;
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }

    public V waitForValue()
      throws InterruptedException
    {
      if (this.computedReference == CustomConcurrentHashMap.UNSET)
        monitorenter;
      try
      {
        if (this.computedReference == CustomConcurrentHashMap.UNSET)
          wait();
        return this.computedReference.waitForValue();
      }
      finally
      {
        monitorexit;
      }
      throw localObject;
    }
  }

  private static class NullPointerExceptionReference<K, V>
    implements CustomConcurrentHashMap.ValueReference<K, V>
  {
    final String message;

    NullPointerExceptionReference(String paramString)
    {
      this.message = paramString;
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
      return null;
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
      throw new NullPointerException(this.message);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ComputingConcurrentHashMap
 * JD-Core Version:    0.6.0
 */