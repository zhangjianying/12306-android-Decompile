package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.database.DatabaseManager;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

public class CloseDatabaseActionDispatcher extends BaseActionDispatcher
{
  private static final String PARAM_OPTIONS = "options";

  protected CloseDatabaseActionDispatcher()
  {
    super("closeDatabase");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType);
  }

  public PluginResult dispatch(BaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    DatabaseManager localDatabaseManager = DatabaseManager.getInstance();
    try
    {
      localDatabaseManager.clearDbPath();
      localDatabaseManager.clearDatabaseKey();
      if (localDatabaseManager.isDatabaseOpen())
        localDatabaseManager.closeDatabase();
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, 0);
      return localPluginResult;
    }
    catch (Throwable localThrowable)
    {
    }
    return new PluginResult(PluginResult.Status.ERROR, 23);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.CloseDatabaseActionDispatcher
 * JD-Core Version:    0.6.0
 */