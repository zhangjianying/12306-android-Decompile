package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Throwables;
import com.google.common.collect.ForwardingObject;
import java.util.concurrent.ExecutionException;

@Beta
public abstract class ForwardingService extends ForwardingObject
  implements Service
{
  protected abstract Service delegate();

  public boolean isRunning()
  {
    return delegate().isRunning();
  }

  protected Service.State standardStartAndWait()
  {
    try
    {
      Service.State localState = (Service.State)Futures.makeUninterruptible(start()).get();
      return localState;
    }
    catch (ExecutionException localExecutionException)
    {
    }
    throw Throwables.propagate(localExecutionException.getCause());
  }

  protected Service.State standardStopAndWait()
  {
    try
    {
      Service.State localState = (Service.State)Futures.makeUninterruptible(stop()).get();
      return localState;
    }
    catch (ExecutionException localExecutionException)
    {
    }
    throw Throwables.propagate(localExecutionException.getCause());
  }

  public ListenableFuture<Service.State> start()
  {
    return delegate().start();
  }

  public Service.State startAndWait()
  {
    return delegate().startAndWait();
  }

  public Service.State state()
  {
    return delegate().state();
  }

  public ListenableFuture<Service.State> stop()
  {
    return delegate().stop();
  }

  public Service.State stopAndWait()
  {
    return delegate().stopAndWait();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ForwardingService
 * JD-Core Version:    0.6.0
 */