package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public final class ExecutionList
  implements Runnable
{
  private static final Logger log = Logger.getLogger(ExecutionList.class.getName());
  private boolean executed = false;
  private final Queue<RunnableExecutorPair> runnables = Lists.newLinkedList();

  public void add(Runnable paramRunnable, Executor paramExecutor)
  {
    Preconditions.checkNotNull(paramRunnable, "Runnable was null.");
    Preconditions.checkNotNull(paramExecutor, "Executor was null.");
    int i = 0;
    synchronized (this.runnables)
    {
      if (!this.executed)
      {
        this.runnables.add(new RunnableExecutorPair(paramRunnable, paramExecutor));
        if (i != 0)
          paramExecutor.execute(paramRunnable);
        return;
      }
      i = 1;
    }
  }

  public void run()
  {
    synchronized (this.runnables)
    {
      this.executed = true;
      if (!this.runnables.isEmpty())
        ((RunnableExecutorPair)this.runnables.poll()).execute();
    }
  }

  private static class RunnableExecutorPair
  {
    final Executor executor;
    final Runnable runnable;

    RunnableExecutorPair(Runnable paramRunnable, Executor paramExecutor)
    {
      this.runnable = paramRunnable;
      this.executor = paramExecutor;
    }

    void execute()
    {
      try
      {
        this.executor.execute(this.runnable);
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        ExecutionList.log.log(Level.SEVERE, "RuntimeException while executing runnable " + this.runnable + " with executor " + this.executor, localRuntimeException);
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.ExecutionList
 * JD-Core Version:    0.6.0
 */