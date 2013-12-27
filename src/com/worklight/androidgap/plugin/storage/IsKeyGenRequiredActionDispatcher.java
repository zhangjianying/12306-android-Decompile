package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.security.SecurityManager;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

public class IsKeyGenRequiredActionDispatcher extends BaseActionDispatcher
{
  private static final String PARAM_USERNAME = "username";

  protected IsKeyGenRequiredActionDispatcher()
  {
    super("isKeyGenRequired");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("username", true, true, arrayOfParameterType);
  }

  private String getUserName(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("username");
  }

  public PluginResult dispatch(BaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    String str = getUserName(paramContext);
    if (SecurityManager.getInstance(paramContext.getCordovaContext().getActivity()).isDPKAvailable(str));
    for (int i = 1; ; i = 0)
      return new PluginResult(PluginResult.Status.OK, i);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.IsKeyGenRequiredActionDispatcher
 * JD-Core Version:    0.6.0
 */