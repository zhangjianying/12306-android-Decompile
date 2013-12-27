package com.worklight.androidgap.plugin;

import com.worklight.common.WLUtils;
import com.worklight.common.security.WLDeviceAuthManager;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceAuthPlugin extends CordovaPlugin
{
  private static final String RESULT_ERROR = "result:error";

  private boolean callback(String paramString, CallbackContext paramCallbackContext)
  {
    if ("result:error".equals(paramString))
    {
      paramCallbackContext.error(PluginResult.Status.ERROR.name());
      return true;
    }
    paramCallbackContext.success(paramString);
    return true;
  }

  private String getDeviceUUID()
  {
    try
    {
      String str = WLDeviceAuthManager.getInstance().getDeviceUUID(this.cordova.getActivity());
      return str;
    }
    catch (Exception localException)
    {
      WLUtils.error("Authentication with Worklight server failed, because device provisioning is unable to get device UUID with " + localException.getMessage(), localException);
    }
    return "result:error";
  }

  private String init(JSONArray paramJSONArray)
  {
    WLDeviceAuthManager.getInstance().init(this.cordova.getActivity(), this.webView);
    return PluginResult.Status.OK.name();
  }

  private String isIfCertificateExists(JSONArray paramJSONArray)
  {
    try
    {
      String str1 = paramJSONArray.getString(0);
      String str2 = Boolean.toString(WLDeviceAuthManager.getInstance().isCertificateExists(str1));
      return str2;
    }
    catch (Exception localException)
    {
      WLUtils.error("Authentication with Worklight server failed, because failed to check if the required certificate exists on the device with " + localException.getMessage(), localException);
    }
    return "result:error";
  }

  private String saveCertificate(JSONArray paramJSONArray)
  {
    try
    {
      String str1 = paramJSONArray.getString(0);
      String str2 = paramJSONArray.getString(1);
      String str3 = paramJSONArray.getString(2);
      WLDeviceAuthManager.getInstance().saveCertificate(str1, str2, str3);
      String str4 = PluginResult.Status.OK.name();
      return str4;
    }
    catch (Exception localException)
    {
      WLUtils.error("Authentication with Worklight server failed, because device provisioning is unable to save required certificate with " + localException.getMessage(), localException);
    }
    return "result:error";
  }

  private String signCsr(JSONArray paramJSONArray)
  {
    try
    {
      JSONObject localJSONObject = paramJSONArray.getJSONObject(0);
      String str1 = paramJSONArray.getString(1);
      WLDeviceAuthManager.getInstance().generateKeyPair(str1);
      String str2 = WLDeviceAuthManager.getInstance().signCsr(localJSONObject, str1);
      return str2;
    }
    catch (Exception localException)
    {
      WLUtils.error("Authentication with Work light server failed, because device provisioning is unable sign CSR with " + localException.getMessage(), localException);
    }
    return "result:error";
  }

  private String signDeviceAuth(JSONArray paramJSONArray)
  {
    try
    {
      String str1 = paramJSONArray.getString(0);
      String str2 = paramJSONArray.getString(1);
      boolean bool = paramJSONArray.getBoolean(2);
      String str3 = WLDeviceAuthManager.getInstance().signDeviceAuth(str1, str2, bool);
      return str3;
    }
    catch (Exception localException)
    {
      WLUtils.error("Authentication with Worklight server failed, because device provisioning is unable to sign with current certificate with " + localException.getMessage(), localException);
    }
    return "result:error";
  }

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    ACTION localACTION = ACTION.fromString(paramString);
    if (localACTION != null)
    {
      switch (1.$SwitchMap$com$worklight$androidgap$plugin$DeviceAuthPlugin$ACTION[localACTION.ordinal()])
      {
      default:
        paramCallbackContext.error("Invalid action: " + paramString);
        return true;
      case 1:
        return callback(init(paramJSONArray), paramCallbackContext);
      case 2:
        return callback(isIfCertificateExists(paramJSONArray), paramCallbackContext);
      case 3:
        return callback(signDeviceAuth(paramJSONArray), paramCallbackContext);
      case 4:
        return callback(signCsr(paramJSONArray), paramCallbackContext);
      case 5:
        return callback(saveCertificate(paramJSONArray), paramCallbackContext);
      case 6:
      }
      return callback(getDeviceUUID(), paramCallbackContext);
    }
    paramCallbackContext.error("Null action");
    return true;
  }

  public static enum ACTION
  {
    static
    {
      signCsr = new ACTION("signCsr", 3);
      saveCertificate = new ACTION("saveCertificate", 4);
      getDeviceUUID = new ACTION("getDeviceUUID", 5);
      ACTION[] arrayOfACTION = new ACTION[6];
      arrayOfACTION[0] = init;
      arrayOfACTION[1] = isCertificateExists;
      arrayOfACTION[2] = signDeviceAuth;
      arrayOfACTION[3] = signCsr;
      arrayOfACTION[4] = saveCertificate;
      arrayOfACTION[5] = getDeviceUUID;
      $VALUES = arrayOfACTION;
    }

    public static ACTION fromString(String paramString)
    {
      try
      {
        ACTION localACTION = valueOf(paramString);
        return localACTION;
      }
      catch (Exception localException)
      {
      }
      return null;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.plugin.DeviceAuthPlugin
 * JD-Core Version:    0.6.0
 */