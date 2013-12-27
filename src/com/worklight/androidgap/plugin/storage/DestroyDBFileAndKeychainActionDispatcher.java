package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.database.DatabaseManager;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

public class DestroyDBFileAndKeychainActionDispatcher extends BaseActionDispatcher
{
  private static final String PARAM_OPTIONS = "options";

  protected DestroyDBFileAndKeychainActionDispatcher()
  {
    super("destroyDbFileAndKeychain");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType);
  }

  public PluginResult dispatch(BaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    DatabaseManager localDatabaseManager = DatabaseManager.getInstance();
    localDatabaseManager.destroyKeychain(paramContext.getCordovaContext().getActivity());
    localDatabaseManager.destroyPreferences(paramContext.getCordovaContext().getActivity());
    localDatabaseManager.clearDbPath();
    localDatabaseManager.clearDatabaseKey();
    if (localDatabaseManager.isDatabaseOpen())
      localDatabaseManager.closeDatabase();
    if (localDatabaseManager.destroyDatabase(paramContext.getCordovaContext().getActivity()) == 0)
      return new PluginResult(PluginResult.Status.OK, 0);
    return new PluginResult(PluginResult.Status.ERROR, 25);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.DestroyDBFileAndKeychainActionDispatcher
 * JD-Core Version:    0.6.0
 */