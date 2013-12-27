package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.util.JsonstoreUtil;
import com.worklight.androidgap.jsonstore.util.Logger;
import com.worklight.androidgap.jsonstore.util.jackson.JsonOrgModule;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

public abstract class DispatchingPlugin extends CordovaPlugin
{
  private static final SerialExecutor executor = new SerialExecutor(null);
  private static final Logger logger = JsonstoreUtil.getCoreLogger();
  private HashMap<String, ActionDispatcher> dispatchers = new HashMap();

  private boolean doDispatch(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
  {
    ActionDispatcher localActionDispatcher = (ActionDispatcher)this.dispatchers.get(paramString);
    if (logger.isLoggable(3))
      logger.logDebug("dispatching action \"" + paramString + "\"");
    if (localActionDispatcher == null)
    {
      paramCallbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "unable to dispatch action \"" + paramString + "\""));
      return false;
    }
    executor.execute(new ActionDispatcherRunnable(localActionDispatcher, paramJSONArray, this.cordova, paramCallbackContext, null));
    return true;
  }

  protected void addDispatcher(ActionDispatcher paramActionDispatcher)
  {
    this.dispatchers.put(paramActionDispatcher.getName(), paramActionDispatcher);
  }

  public boolean execute(String paramString1, String paramString2, CallbackContext paramCallbackContext)
    throws JSONException
  {
    try
    {
      JSONArray localJSONArray = JsonOrgModule.deserializeJSONArray(paramString2);
      return doDispatch(paramString1, localJSONArray, paramCallbackContext);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new JSONException(localThrowable);
  }

  private class ActionDispatcherRunnable
    implements Runnable
  {
    private JSONArray args;
    private CallbackContext callbackContext;
    private CordovaInterface context;
    private ActionDispatcher dispatcher;

    private ActionDispatcherRunnable(ActionDispatcher paramJSONArray, JSONArray paramCordovaInterface, CordovaInterface paramCallbackContext, CallbackContext arg5)
    {
      this.args = paramCordovaInterface;
      Object localObject;
      this.callbackContext = localObject;
      this.context = paramCallbackContext;
      this.dispatcher = paramJSONArray;
    }

    public void run()
    {
      try
      {
        PluginResult localPluginResult2 = this.dispatcher.dispatch(this.context, this.args);
        localPluginResult1 = localPluginResult2;
        this.callbackContext.sendPluginResult(localPluginResult1);
        return;
      }
      catch (Throwable localThrowable)
      {
        while (true)
        {
          if (DispatchingPlugin.logger.isLoggable(6))
            DispatchingPlugin.logger.logError("error while dispatching action \"" + this.dispatcher.getName() + "\"", localThrowable);
          PluginResult localPluginResult1 = new PluginResult(PluginResult.Status.ERROR, localThrowable.getMessage());
        }
      }
    }
  }

  private static class SerialExecutor
    implements Executor
  {
    private Runnable currentRunnable;
    private LinkedBlockingQueue<Runnable> dispatcherRunnables = new LinkedBlockingQueue();
    private Executor executor = Executors.newCachedThreadPool();

    private void scheduleNextRunnable()
    {
      monitorenter;
      try
      {
        this.currentRunnable = ((Runnable)this.dispatcherRunnables.poll());
        if (this.currentRunnable != null)
          this.executor.execute(this.currentRunnable);
        monitorexit;
        return;
      }
      finally
      {
        localObject = finally;
        monitorexit;
      }
      throw localObject;
    }

    public void execute(Runnable paramRunnable)
    {
      this.dispatcherRunnables.offer(new Runnable(paramRunnable)
      {
        public void run()
        {
          try
          {
            this.val$command.run();
            return;
          }
          finally
          {
            DispatchingPlugin.SerialExecutor.this.scheduleNextRunnable();
          }
          throw localObject;
        }
      });
      if (this.currentRunnable == null)
        scheduleNextRunnable();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.DispatchingPlugin
 * JD-Core Version:    0.6.0
 */