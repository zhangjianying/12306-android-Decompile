package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

@Beta
public final class EvictionListeners
{
  public static <K, V> MapEvictionListener<K, V> asynchronous(MapEvictionListener<K, V> paramMapEvictionListener, Executor paramExecutor)
  {
    return new MapEvictionListener(paramExecutor, paramMapEvictionListener)
    {
      public void onEviction(@Nullable K paramK, @Nullable V paramV)
      {
        this.val$executor.execute(new Runnable(paramK, paramV)
        {
          public void run()
          {
            EvictionListeners.1.this.val$listener.onEviction(this.val$key, this.val$value);
          }
        });
      }
    };
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EvictionListeners
 * JD-Core Version:    0.6.0
 */