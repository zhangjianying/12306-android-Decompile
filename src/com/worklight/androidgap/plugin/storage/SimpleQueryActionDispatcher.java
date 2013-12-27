package com.worklight.androidgap.plugin.storage;

import android.database.Cursor;
import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.ReadableDatabase;
import com.worklight.androidgap.jsonstore.util.Logger;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

public abstract class SimpleQueryActionDispatcher extends DatabaseActionDispatcher
{
  public SimpleQueryActionDispatcher(String paramString)
  {
    super(paramString);
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    int i = -1;
    try
    {
      int k = ((Integer)paramContext.performReadableDatabaseAction(new SimpleQueryAction(paramContext, null))).intValue();
      i = k;
      int j = getModifiedResultValue(i);
      logResult(paramContext, j);
      return new PluginResult(PluginResult.Status.OK, j);
    }
    catch (Throwable localThrowable)
    {
      while (true)
      {
        if (!this.logger.isLoggable(6))
          continue;
        this.logger.logError("error occurred while performing query:");
        this.logger.logError("   " + getFormattedQuery(paramContext), localThrowable);
      }
    }
  }

  protected abstract String getFormattedQuery(DatabaseActionDispatcher.Context paramContext)
    throws Throwable;

  protected int getModifiedResultValue(int paramInt)
  {
    return paramInt;
  }

  protected int getNotFoundResultValue()
  {
    return -1;
  }

  protected void logResult(DatabaseActionDispatcher.Context paramContext, int paramInt)
    throws Throwable
  {
  }

  private class SimpleQueryAction
    implements DatabaseActionDispatcher.ReadableDatabaseAction<Integer>
  {
    private DatabaseActionDispatcher.Context context;

    private SimpleQueryAction(DatabaseActionDispatcher.Context arg2)
    {
      Object localObject;
      this.context = localObject;
    }

    public Integer performAction(DatabaseSchema paramDatabaseSchema, ReadableDatabase paramReadableDatabase)
      throws Throwable
    {
      Cursor localCursor = paramReadableDatabase.rawQuery(SimpleQueryActionDispatcher.this.getFormattedQuery(this.context), null);
      int i = SimpleQueryActionDispatcher.this.getNotFoundResultValue();
      if (localCursor.getCount() > 0)
      {
        localCursor.moveToNext();
        i = localCursor.getInt(0);
      }
      localCursor.close();
      return Integer.valueOf(i);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.SimpleQueryActionDispatcher
 * JD-Core Version:    0.6.0
 */