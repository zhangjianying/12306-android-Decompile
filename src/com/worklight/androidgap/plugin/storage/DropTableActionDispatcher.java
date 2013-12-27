package com.worklight.androidgap.plugin.storage;

import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

public class DropTableActionDispatcher extends DatabaseActionDispatcher
{
  private static final String PARAM_OPTIONS = "options";

  protected DropTableActionDispatcher()
  {
    super("dropTable");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType);
  }

  public PluginResult dispatch(DatabaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    try
    {
      paramContext.clearDatabase();
      PluginResult localPluginResult = new PluginResult(PluginResult.Status.OK, 0);
      return localPluginResult;
    }
    catch (Throwable localThrowable)
    {
    }
    return new PluginResult(PluginResult.Status.ERROR, 26);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.DropTableActionDispatcher
 * JD-Core Version:    0.6.0
 */