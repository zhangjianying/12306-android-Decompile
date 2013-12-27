package com.worklight.wlclient.api.challengehandler;

import org.json.JSONObject;

public abstract class BaseProvisioningChallengeHandler extends BaseDeviceAuthChallengeHandler
{
  public BaseProvisioningChallengeHandler(String paramString)
  {
    super(paramString);
  }

  protected abstract JSONObject createJsonCsr(String paramString1, String paramString2, String paramString3);

  protected boolean isCertificateChallengeResponse(JSONObject paramJSONObject)
  {
    return false;
  }

  protected void onDeviceAuthDataReady(JSONObject paramJSONObject)
  {
  }

  protected void submitCsr(JSONObject paramJSONObject)
  {
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.challengehandler.BaseProvisioningChallengeHandler
 * JD-Core Version:    0.6.0
 */