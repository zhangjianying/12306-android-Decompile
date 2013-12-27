package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

@Beta
public final class Futures
{
  public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends ListenableFuture<? extends O>> paramFunction)
  {
    return chain(paramListenableFuture, paramFunction, MoreExecutors.sameThreadExecutor());
  }

  public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends ListenableFuture<? extends O>> paramFunction, Executor paramExecutor)
  {
    ChainingListenableFuture localChainingListenableFuture = new ChainingListenableFuture(paramFunction, paramListenableFuture, null);
    paramListenableFuture.addListener(localChainingListenableFuture, paramExecutor);
    return localChainingListenableFuture;
  }

  public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V paramV)
  {
    SettableFuture localSettableFuture = SettableFuture.create();
    localSettableFuture.set(paramV);
    return makeChecked(localSettableFuture, new Function()
    {
      public X apply(Exception paramException)
      {
        throw new AssertionError("impossible");
      }
    });
  }

  public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X paramX)
  {
    Preconditions.checkNotNull(paramX);
    return makeChecked(immediateFailedFuture(paramX), new Function(paramX)
    {
      public X apply(Exception paramException)
      {
        return this.val$exception;
      }
    });
  }

  public static <V> ListenableFuture<V> immediateFailedFuture(Throwable paramThrowable)
  {
    Preconditions.checkNotNull(paramThrowable);
    SettableFuture localSettableFuture = SettableFuture.create();
    localSettableFuture.setException(paramThrowable);
    return localSettableFuture;
  }

  public static <V> ListenableFuture<V> immediateFuture(@Nullable V paramV)
  {
    SettableFuture localSettableFuture = SettableFuture.create();
    localSettableFuture.set(paramV);
    return localSettableFuture;
  }

  public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> paramListenableFuture, Function<Exception, X> paramFunction)
  {
    return new MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(paramListenableFuture), paramFunction);
  }

  public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(Future<V> paramFuture, Function<Exception, X> paramFunction)
  {
    return new MappingCheckedFuture(makeListenable(paramFuture), paramFunction);
  }

  public static <V> ListenableFuture<V> makeListenable(Future<V> paramFuture)
  {
    if ((paramFuture instanceof ListenableFuture))
      return (ListenableFuture)paramFuture;
    return new ListenableFutureAdapter(paramFuture);
  }

  static <V> ListenableFuture<V> makeListenable(Future<V> paramFuture, Executor paramExecutor)
  {
    Preconditions.checkNotNull(paramExecutor);
    if ((paramFuture instanceof ListenableFuture))
      return (ListenableFuture)paramFuture;
    return new ListenableFutureAdapter(paramFuture, paramExecutor);
  }

  public static <V> UninterruptibleFuture<V> makeUninterruptible(Future<V> paramFuture)
  {
    Preconditions.checkNotNull(paramFuture);
    if ((paramFuture instanceof UninterruptibleFuture))
      return (UninterruptibleFuture)paramFuture;
    return new UninterruptibleFuture(paramFuture)
    {
      public boolean cancel(boolean paramBoolean)
      {
        return this.val$future.cancel(paramBoolean);
      }

      // ERROR //
      public V get()
        throws ExecutionException
      {
        // Byte code:
        //   0: iconst_0
        //   1: istore_1
        //   2: aload_0
        //   3: getfield 18	com/google/common/util/concurrent/Futures$1:val$future	Ljava/util/concurrent/Future;
        //   6: invokeinterface 35 1 0
        //   11: astore 4
        //   13: iload_1
        //   14: ifeq +9 -> 23
        //   17: invokestatic 41	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   20: invokevirtual 44	java/lang/Thread:interrupt	()V
        //   23: aload 4
        //   25: areturn
        //   26: astore_3
        //   27: iconst_1
        //   28: istore_1
        //   29: goto -27 -> 2
        //   32: astore_2
        //   33: iload_1
        //   34: ifeq +9 -> 43
        //   37: invokestatic 41	java/lang/Thread:currentThread	()Ljava/lang/Thread;
        //   40: invokevirtual 44	java/lang/Thread:interrupt	()V
        //   43: aload_2
        //   44: athrow
        //
        // Exception table:
        //   from	to	target	type
        //   2	13	26	java/lang/InterruptedException
        //   2	13	32	finally
      }

      public V get(long paramLong, TimeUnit paramTimeUnit)
        throws TimeoutException, ExecutionException
      {
        int i = 0;
        try
        {
          long l1 = System.nanoTime();
          long l2 = paramTimeUnit.toNanos(paramLong);
          long l3 = l1 + l2;
          while (true)
            try
            {
              Object localObject2 = this.val$future.get(l3 - System.nanoTime(), TimeUnit.NANOSECONDS);
              if (i == 0)
                continue;
              Thread.currentThread().interrupt();
              return localObject2;
            }
            catch (InterruptedException localInterruptedException)
            {
              i = 1;
            }
        }
        finally
        {
          if (i != 0)
            Thread.currentThread().interrupt();
        }
        throw localObject1;
      }

      public boolean isCancelled()
      {
        return this.val$future.isCancelled();
      }

      public boolean isDone()
      {
        return this.val$future.isDone();
      }
    };
  }

  public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends O> paramFunction)
  {
    return transform(paramListenableFuture, paramFunction, MoreExecutors.sameThreadExecutor());
  }

  public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> paramListenableFuture, Function<? super I, ? extends O> paramFunction, Executor paramExecutor)
  {
    Preconditions.checkNotNull(paramFunction);
    return chain(paramListenableFuture, new Function(paramFunction)
    {
      public ListenableFuture<O> apply(I paramI)
      {
        return Futures.immediateFuture(this.val$function.apply(paramI));
      }
    }
    , paramExecutor);
  }

  public static <I, O> Future<O> transform(Future<I> paramFuture, Function<? super I, ? extends O> paramFunction)
  {
    if ((paramFuture instanceof ListenableFuture))
      return transform((ListenableFuture)paramFuture, paramFunction);
    Preconditions.checkNotNull(paramFuture);
    Preconditions.checkNotNull(paramFunction);
    return new Future(paramFuture, paramFunction)
    {
      private ExecutionException exception = null;
      private final Object lock = new Object();
      private boolean set = false;
      private O value = null;

      private O apply(I paramI)
        throws ExecutionException
      {
        synchronized (this.lock)
        {
          boolean bool = this.set;
          if (bool);
        }
        Object localObject3;
        try
        {
          this.value = this.val$function.apply(paramI);
          this.set = true;
          if (this.exception != null)
          {
            throw this.exception;
            localObject2 = finally;
            monitorexit;
            throw localObject2;
          }
        }
        catch (RuntimeException localRuntimeException)
        {
          while (true)
            this.exception = new ExecutionException(localRuntimeException);
        }
        catch (Error localError)
        {
          while (true)
            this.exception = new ExecutionException(localError);
          localObject3 = this.value;
          monitorexit;
        }
        return localObject3;
      }

      public boolean cancel(boolean paramBoolean)
      {
        return this.val$future.cancel(paramBoolean);
      }

      public O get()
        throws InterruptedException, ExecutionException
      {
        return apply(this.val$future.get());
      }

      public O get(long paramLong, TimeUnit paramTimeUnit)
        throws InterruptedException, ExecutionException, TimeoutException
      {
        return apply(this.val$future.get(paramLong, paramTimeUnit));
      }

      public boolean isCancelled()
      {
        return this.val$future.isCancelled();
      }

      public boolean isDone()
      {
        return this.val$future.isDone();
      }
    };
  }

  private static class ChainingListenableFuture<I, O> extends AbstractListenableFuture<O>
    implements Runnable
  {
    private Function<? super I, ? extends ListenableFuture<? extends O>> function;
    private ListenableFuture<? extends I> inputFuture;
    private final BlockingQueue<Boolean> mayInterruptIfRunningChannel = new LinkedBlockingQueue(1);
    private final CountDownLatch outputCreated = new CountDownLatch(1);
    private volatile ListenableFuture<? extends O> outputFuture;

    private ChainingListenableFuture(Function<? super I, ? extends ListenableFuture<? extends O>> paramFunction, ListenableFuture<? extends I> paramListenableFuture)
    {
      this.function = ((Function)Preconditions.checkNotNull(paramFunction));
      this.inputFuture = ((ListenableFuture)Preconditions.checkNotNull(paramListenableFuture));
    }

    private void cancel(@Nullable Future<?> paramFuture, boolean paramBoolean)
    {
      if (paramFuture != null)
        paramFuture.cancel(paramBoolean);
    }

    public boolean cancel(boolean paramBoolean)
    {
      if (cancel())
        try
        {
          this.mayInterruptIfRunningChannel.put(Boolean.valueOf(paramBoolean));
          cancel(this.inputFuture, paramBoolean);
          cancel(this.outputFuture, paramBoolean);
          return true;
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
            Thread.currentThread().interrupt();
        }
      return false;
    }

    public O get()
      throws InterruptedException, ExecutionException
    {
      if (!isDone())
      {
        ListenableFuture localListenableFuture1 = this.inputFuture;
        if (localListenableFuture1 != null)
          localListenableFuture1.get();
        this.outputCreated.await();
        ListenableFuture localListenableFuture2 = this.outputFuture;
        if (localListenableFuture2 != null)
          localListenableFuture2.get();
      }
      return super.get();
    }

    public O get(long paramLong, TimeUnit paramTimeUnit)
      throws TimeoutException, ExecutionException, InterruptedException
    {
      if (!isDone())
      {
        if (paramTimeUnit != TimeUnit.NANOSECONDS)
        {
          paramLong = TimeUnit.NANOSECONDS.convert(paramLong, paramTimeUnit);
          paramTimeUnit = TimeUnit.NANOSECONDS;
        }
        ListenableFuture localListenableFuture1 = this.inputFuture;
        if (localListenableFuture1 != null)
        {
          long l2 = System.nanoTime();
          localListenableFuture1.get(paramLong, paramTimeUnit);
          paramLong -= Math.max(0L, System.nanoTime() - l2);
        }
        long l1 = System.nanoTime();
        if (!this.outputCreated.await(paramLong, paramTimeUnit))
          throw new TimeoutException();
        paramLong -= Math.max(0L, System.nanoTime() - l1);
        ListenableFuture localListenableFuture2 = this.outputFuture;
        if (localListenableFuture2 != null)
          localListenableFuture2.get(paramLong, paramTimeUnit);
      }
      return super.get(paramLong, paramTimeUnit);
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   4: invokestatic 154	com/google/common/util/concurrent/Futures:makeUninterruptible	(Ljava/util/concurrent/Future;)Lcom/google/common/util/concurrent/UninterruptibleFuture;
      //   7: invokeinterface 157 1 0
      //   12: astore 12
      //   14: aload_0
      //   15: getfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   18: aload 12
      //   20: invokeinterface 160 2 0
      //   25: checkcast 49	com/google/common/util/concurrent/ListenableFuture
      //   28: astore 13
      //   30: aload_0
      //   31: aload 13
      //   33: putfield 58	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   36: aload_0
      //   37: invokevirtual 163	com/google/common/util/concurrent/Futures$ChainingListenableFuture:isCancelled	()Z
      //   40: istore 14
      //   42: iload 14
      //   44: ifeq +145 -> 189
      //   47: aload 13
      //   49: aload_0
      //   50: getfield 32	com/google/common/util/concurrent/Futures$ChainingListenableFuture:mayInterruptIfRunningChannel	Ljava/util/concurrent/BlockingQueue;
      //   53: invokeinterface 166 1 0
      //   58: checkcast 73	java/lang/Boolean
      //   61: invokevirtual 169	java/lang/Boolean:booleanValue	()Z
      //   64: invokeinterface 170 2 0
      //   69: pop
      //   70: aload_0
      //   71: aconst_null
      //   72: putfield 58	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   75: aload_0
      //   76: aconst_null
      //   77: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   80: aload_0
      //   81: aconst_null
      //   82: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   85: aload_0
      //   86: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   89: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   92: return
      //   93: astore 10
      //   95: aload_0
      //   96: invokevirtual 71	com/google/common/util/concurrent/Futures$ChainingListenableFuture:cancel	()Z
      //   99: pop
      //   100: aload_0
      //   101: aconst_null
      //   102: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   105: aload_0
      //   106: aconst_null
      //   107: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   110: aload_0
      //   111: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   114: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   117: return
      //   118: astore 8
      //   120: aload_0
      //   121: aload 8
      //   123: invokevirtual 177	java/util/concurrent/ExecutionException:getCause	()Ljava/lang/Throwable;
      //   126: invokevirtual 181	com/google/common/util/concurrent/Futures$ChainingListenableFuture:setException	(Ljava/lang/Throwable;)Z
      //   129: pop
      //   130: aload_0
      //   131: aconst_null
      //   132: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   135: aload_0
      //   136: aconst_null
      //   137: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   140: aload_0
      //   141: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   144: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   147: return
      //   148: astore 15
      //   150: invokestatic 91	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   153: invokevirtual 94	java/lang/Thread:interrupt	()V
      //   156: goto -86 -> 70
      //   159: astore 6
      //   161: aload_0
      //   162: aload 6
      //   164: invokevirtual 182	java/lang/reflect/UndeclaredThrowableException:getCause	()Ljava/lang/Throwable;
      //   167: invokevirtual 181	com/google/common/util/concurrent/Futures$ChainingListenableFuture:setException	(Ljava/lang/Throwable;)Z
      //   170: pop
      //   171: aload_0
      //   172: aconst_null
      //   173: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   176: aload_0
      //   177: aconst_null
      //   178: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   181: aload_0
      //   182: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   185: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   188: return
      //   189: aload 13
      //   191: new 184	com/google/common/util/concurrent/Futures$ChainingListenableFuture$1
      //   194: dup
      //   195: aload_0
      //   196: aload 13
      //   198: invokespecial 187	com/google/common/util/concurrent/Futures$ChainingListenableFuture$1:<init>	(Lcom/google/common/util/concurrent/Futures$ChainingListenableFuture;Lcom/google/common/util/concurrent/ListenableFuture;)V
      //   201: invokestatic 193	com/google/common/util/concurrent/MoreExecutors:sameThreadExecutor	()Ljava/util/concurrent/ExecutorService;
      //   204: invokeinterface 197 3 0
      //   209: aload_0
      //   210: aconst_null
      //   211: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   214: aload_0
      //   215: aconst_null
      //   216: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   219: aload_0
      //   220: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   223: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   226: return
      //   227: astore 4
      //   229: aload_0
      //   230: aload 4
      //   232: invokevirtual 181	com/google/common/util/concurrent/Futures$ChainingListenableFuture:setException	(Ljava/lang/Throwable;)Z
      //   235: pop
      //   236: aload_0
      //   237: aconst_null
      //   238: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   241: aload_0
      //   242: aconst_null
      //   243: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   246: aload_0
      //   247: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   250: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   253: return
      //   254: astore_2
      //   255: aload_0
      //   256: aload_2
      //   257: invokevirtual 181	com/google/common/util/concurrent/Futures$ChainingListenableFuture:setException	(Ljava/lang/Throwable;)Z
      //   260: pop
      //   261: aload_0
      //   262: aconst_null
      //   263: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   266: aload_0
      //   267: aconst_null
      //   268: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   271: aload_0
      //   272: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   275: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   278: return
      //   279: astore_1
      //   280: aload_0
      //   281: aconst_null
      //   282: putfield 47	com/google/common/util/concurrent/Futures$ChainingListenableFuture:function	Lcom/google/common/base/Function;
      //   285: aload_0
      //   286: aconst_null
      //   287: putfield 51	com/google/common/util/concurrent/Futures$ChainingListenableFuture:inputFuture	Lcom/google/common/util/concurrent/ListenableFuture;
      //   290: aload_0
      //   291: getfield 37	com/google/common/util/concurrent/Futures$ChainingListenableFuture:outputCreated	Ljava/util/concurrent/CountDownLatch;
      //   294: invokevirtual 173	java/util/concurrent/CountDownLatch:countDown	()V
      //   297: aload_1
      //   298: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   0	14	93	java/util/concurrent/CancellationException
      //   0	14	118	java/util/concurrent/ExecutionException
      //   47	70	148	java/lang/InterruptedException
      //   0	14	159	java/lang/reflect/UndeclaredThrowableException
      //   14	42	159	java/lang/reflect/UndeclaredThrowableException
      //   47	70	159	java/lang/reflect/UndeclaredThrowableException
      //   70	75	159	java/lang/reflect/UndeclaredThrowableException
      //   95	100	159	java/lang/reflect/UndeclaredThrowableException
      //   120	130	159	java/lang/reflect/UndeclaredThrowableException
      //   150	156	159	java/lang/reflect/UndeclaredThrowableException
      //   189	209	159	java/lang/reflect/UndeclaredThrowableException
      //   0	14	227	java/lang/RuntimeException
      //   14	42	227	java/lang/RuntimeException
      //   47	70	227	java/lang/RuntimeException
      //   70	75	227	java/lang/RuntimeException
      //   95	100	227	java/lang/RuntimeException
      //   120	130	227	java/lang/RuntimeException
      //   150	156	227	java/lang/RuntimeException
      //   189	209	227	java/lang/RuntimeException
      //   0	14	254	java/lang/Error
      //   14	42	254	java/lang/Error
      //   47	70	254	java/lang/Error
      //   70	75	254	java/lang/Error
      //   95	100	254	java/lang/Error
      //   120	130	254	java/lang/Error
      //   150	156	254	java/lang/Error
      //   189	209	254	java/lang/Error
      //   0	14	279	finally
      //   14	42	279	finally
      //   47	70	279	finally
      //   70	75	279	finally
      //   95	100	279	finally
      //   120	130	279	finally
      //   150	156	279	finally
      //   161	171	279	finally
      //   189	209	279	finally
      //   229	236	279	finally
      //   255	261	279	finally
    }
  }

  private static class ListenableFutureAdapter<V> extends ForwardingFuture<V>
    implements ListenableFuture<V>
  {
    private static final Executor defaultAdapterExecutor;
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("ListenableFutureAdapter-thread-%d").build();
    private final Executor adapterExecutor;
    private final Future<V> delegate;
    private final ExecutionList executionList = new ExecutionList();
    private final AtomicBoolean hasListeners = new AtomicBoolean(false);

    static
    {
      defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
    }

    ListenableFutureAdapter(Future<V> paramFuture)
    {
      this(paramFuture, defaultAdapterExecutor);
    }

    ListenableFutureAdapter(Future<V> paramFuture, Executor paramExecutor)
    {
      this.delegate = ((Future)Preconditions.checkNotNull(paramFuture));
      this.adapterExecutor = ((Executor)Preconditions.checkNotNull(paramExecutor));
    }

    public void addListener(Runnable paramRunnable, Executor paramExecutor)
    {
      this.executionList.add(paramRunnable, paramExecutor);
      if (this.hasListeners.compareAndSet(false, true))
      {
        if (this.delegate.isDone())
          this.executionList.run();
      }
      else
        return;
      this.adapterExecutor.execute(new Runnable()
      {
        public void run()
        {
          try
          {
            Futures.ListenableFutureAdapter.this.delegate.get();
            label13: Futures.ListenableFutureAdapter.this.executionList.run();
            return;
          }
          catch (Error localError)
          {
            throw localError;
          }
          catch (InterruptedException localInterruptedException)
          {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Adapter thread interrupted!", localInterruptedException);
          }
          catch (Throwable localThrowable)
          {
            break label13;
          }
        }
      });
    }

    protected Future<V> delegate()
    {
      return this.delegate;
    }
  }

  private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X>
  {
    final Function<Exception, X> mapper;

    MappingCheckedFuture(ListenableFuture<V> paramListenableFuture, Function<Exception, X> paramFunction)
    {
      super();
      this.mapper = ((Function)Preconditions.checkNotNull(paramFunction));
    }

    protected X mapException(Exception paramException)
    {
      return (Exception)this.mapper.apply(paramException);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.Futures
 * JD-Core Version:    0.6.0
 */