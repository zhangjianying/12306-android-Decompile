package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.database.DatabaseManager;
import com.worklight.androidgap.jsonstore.security.SecurityManager;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

public class ChangePasswordActionDispatcher extends BaseActionDispatcher
{
  private static final String PARAM_NEW_PW = "newPW";
  private static final String PARAM_OLD_PW = "oldPW";
  private static final String PARAM_OPTIONS = "options";
  private static final String PARAM_USERNAME = "username";

  public ChangePasswordActionDispatcher()
  {
    super("changePassword");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("oldPW", true, false, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("newPW", true, false, arrayOfParameterType2);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType3 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType3[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("username", true, true, arrayOfParameterType3);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType4 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType4[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType4);
  }

  private String getNewPW(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("newPW");
  }

  private String getOldPW(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("oldPW");
  }

  private String getUserName(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("username");
  }

  public PluginResult dispatch(BaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    if (!DatabaseManager.getInstance().isDatabaseOpen())
      return new PluginResult(PluginResult.Status.ERROR, -50);
    String str1 = getNewPW(paramContext);
    String str2 = getOldPW(paramContext);
    String str3 = getUserName(paramContext);
    SecurityManager localSecurityManager = SecurityManager.getInstance(paramContext.getCordovaContext().getActivity());
    try
    {
      String str4 = localSecurityManager.getDPK(str2, str3);
      String str5 = localSecurityManager.getSalt(str3);
      localSecurityManager.storeDPK(str1, str3, str4, str5, true, false);
      return new PluginResult(PluginResult.Status.OK, 0);
    }
    catch (Throwable localThrowable)
    {
    }
    return new PluginResult(PluginResult.Status.ERROR, 24);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.ChangePasswordActionDispatcher
 * JD-Core Version:    0.6.0
 */