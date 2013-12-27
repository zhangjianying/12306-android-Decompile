package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.database.DatabaseAccessorImpl;
import com.worklight.androidgap.jsonstore.database.DatabaseManager;
import com.worklight.androidgap.jsonstore.database.DatabaseSchema;
import com.worklight.androidgap.jsonstore.database.ReadableDatabase;
import com.worklight.androidgap.jsonstore.database.WritableDatabase;
import net.sqlcipher.database.SQLiteDatabase;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class DatabaseActionDispatcher extends BaseActionDispatcher
{
  private static final String PARAM_DBNAME = "dbName";

  public DatabaseActionDispatcher(String paramString)
  {
    super(paramString);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("dbName", true, arrayOfParameterType);
  }

  public PluginResult dispatch(BaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    Context localContext = new Context(paramContext, null);
    if (!DatabaseManager.getInstance().isDatabaseOpen())
      return new PluginResult(PluginResult.Status.ERROR, -50);
    localContext.initialize();
    return dispatch(localContext);
  }

  public abstract PluginResult dispatch(Context paramContext)
    throws Throwable;

  public static class Context extends BaseActionDispatcher.Context
  {
    private DatabaseAccessorImpl dbAccessor;
    private String dbName;
    private BaseActionDispatcher.Context parentContext;

    private Context(BaseActionDispatcher.Context paramContext)
    {
      super();
      this.parentContext = paramContext;
    }

    private DatabaseAccessorImpl getDatabaseAccessor()
      throws Throwable
    {
      if (this.dbAccessor == null)
        this.dbAccessor = ((DatabaseAccessorImpl)DatabaseManager.getInstance().getDatabase(this.dbName));
      return this.dbAccessor;
    }

    private void initialize()
      throws Throwable
    {
      this.dbName = getStringParameter("dbName");
    }

    public void clearDatabase()
      throws Throwable
    {
      synchronized (getDatabaseAccessor())
      {
        ???.dropTable();
        return;
      }
    }

    public JSONArray getArrayParameter(String paramString)
    {
      return this.parentContext.getArrayParameter(paramString);
    }

    public String getDatabaseName()
    {
      return this.dbName;
    }

    public DatabaseSchema getDatabaseSchema()
    {
      return this.dbAccessor.getSchema();
    }

    public float getFloatParameter(String paramString)
    {
      return this.parentContext.getFloatParameter(paramString);
    }

    public int getIntParameter(String paramString)
    {
      return this.parentContext.getIntParameter(paramString);
    }

    public JSONObject getObjectParameter(String paramString)
    {
      return this.parentContext.getObjectParameter(paramString);
    }

    public String getStringParameter(String paramString)
    {
      return this.parentContext.getStringParameter(paramString);
    }

    public Object getUntypedParameter(String paramString)
    {
      return this.parentContext.getUntypedParameter(paramString);
    }

    public <T> T performReadableDatabaseAction(DatabaseActionDispatcher.ReadableDatabaseAction<T> paramReadableDatabaseAction)
      throws Throwable
    {
      synchronized (getDatabaseAccessor())
      {
        ReadableDatabase localReadableDatabase = ???.getReadableDatabase();
        try
        {
          Object localObject2 = paramReadableDatabaseAction.performAction(???.getSchema(), localReadableDatabase);
          return localObject2;
        }
        catch (Throwable localThrowable)
        {
          throw localThrowable;
        }
      }
    }

    public <T> T performWritableDatabaseAction(DatabaseActionDispatcher.WritableDatabaseAction<T> paramWritableDatabaseAction)
      throws Throwable
    {
      synchronized (getDatabaseAccessor())
      {
        WritableDatabase localWritableDatabase = ???.getWritableDatabase();
        SQLiteDatabase localSQLiteDatabase = ???.getRawDatabase();
        try
        {
          localSQLiteDatabase.beginTransaction();
          Object localObject3 = paramWritableDatabaseAction.performAction(???.getSchema(), localWritableDatabase);
          if ((localObject3 instanceof Integer))
            localSQLiteDatabase.setTransactionSuccessful();
          localSQLiteDatabase.endTransaction();
          return localObject3;
        }
        catch (Throwable localThrowable)
        {
          throw localThrowable;
        }
        finally
        {
          localSQLiteDatabase.endTransaction();
        }
      }
    }
  }

  public static abstract interface ReadableDatabaseAction<T>
  {
    public abstract T performAction(DatabaseSchema paramDatabaseSchema, ReadableDatabase paramReadableDatabase)
      throws Throwable;
  }

  public static abstract interface WritableDatabaseAction<T>
  {
    public abstract T performAction(DatabaseSchema paramDatabaseSchema, WritableDatabase paramWritableDatabase)
      throws Throwable;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.DatabaseActionDispatcher
 * JD-Core Version:    0.6.0
 */