package com.google.common.util.concurrent;

import com.google.common.collect.ForwardingQueue;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class ForwardingBlockingQueue<E> extends ForwardingQueue<E>
  implements BlockingQueue<E>
{
  protected abstract BlockingQueue<E> delegate();

  public int drainTo(Collection<? super E> paramCollection)
  {
    return delegate().drainTo(paramCollection);
  }

  public int drainTo(Collection<? super E> paramCollection, int paramInt)
  {
    return delegate().drainTo(paramCollection, paramInt);
  }

  public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    return delegate().offer(paramE, paramLong, paramTimeUnit);
  }

  public E poll(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    return delegate().poll(paramLong, paramTimeUnit);
  }

  public void put(E paramE)
    throws InterruptedException
  {
    delegate().put(paramE);
  }

  public int remainingCapacity()
  {
    return delegate().remainingCapacity();
  }

  public E take()
    throws InterruptedException
  {
    return delegate().take();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ForwardingBlockingQueue
 * JD-Core Version:    0.6.0
 */