package com.worklight.androidgap.plugin.storage;

import com.worklight.androidgap.jsonstore.security.SecurityManager;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;

public class StoreDPKActionDispatcher extends BaseActionDispatcher
{
  private static final String PARAM_CBK_CLEAR = "cbkClear";
  private static final String PARAM_DPK_CLEAR = "dpkClear";
  private static final String PARAM_LOCALKEYGEN = "localKeyGen";
  private static final String PARAM_OPTIONS = "options";
  private static final String PARAM_SALT = "salt";
  private static final String PARAM_USERNAME = "username";

  protected StoreDPKActionDispatcher()
  {
    super("storeDPK");
    BaseActionDispatcher.ParameterType[] arrayOfParameterType1 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType1[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("dpkClear", true, false, arrayOfParameterType1);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType2 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType2[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("cbkClear", true, false, arrayOfParameterType2);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType3 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType3[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("salt", true, false, arrayOfParameterType3);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType4 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType4[0] = BaseActionDispatcher.ParameterType.STRING;
    addParameter("username", true, true, arrayOfParameterType4);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType5 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType5[0] = BaseActionDispatcher.ParameterType.BOOLEAN;
    addParameter("localKeyGen", true, true, arrayOfParameterType5);
    BaseActionDispatcher.ParameterType[] arrayOfParameterType6 = new BaseActionDispatcher.ParameterType[1];
    arrayOfParameterType6[0] = BaseActionDispatcher.ParameterType.OBJECT;
    addParameter("options", false, arrayOfParameterType6);
  }

  private String getCBKClear(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("cbkClear");
  }

  private String getDPKClear(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("dpkClear");
  }

  private Boolean getLocalKeyGen(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getBooleanParameter("localKeyGen");
  }

  private String getSalt(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("salt");
  }

  private String getUserName(BaseActionDispatcher.Context paramContext)
  {
    return paramContext.getStringParameter("username");
  }

  public PluginResult dispatch(BaseActionDispatcher.Context paramContext)
    throws Throwable
  {
    String str1 = getCBKClear(paramContext);
    String str2 = getDPKClear(paramContext);
    String str3 = getSalt(paramContext);
    String str4 = getUserName(paramContext);
    Boolean localBoolean = getLocalKeyGen(paramContext);
    SecurityManager.getInstance(paramContext.getCordovaContext().getActivity()).storeDPK(str1, str4, str2, str3, false, localBoolean.booleanValue());
    return new PluginResult(PluginResult.Status.OK, 0);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.storage.StoreDPKActionDispatcher
 * JD-Core Version:    0.6.0
 */