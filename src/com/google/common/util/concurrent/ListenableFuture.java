package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Beta
public abstract interface ListenableFuture<V> extends Future<V>
{
  public abstract void addListener(Runnable paramRunnable, Executor paramExecutor);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ListenableFuture
 * JD-Core Version:    0.6.0
 */