package com.worklight.wlclient.challengehandler;

import com.worklight.wlclient.api.challengehandler.BaseProvisioningChallengeHandler;
import org.json.JSONObject;

public class AutoProvisioningChallengeHandler extends BaseProvisioningChallengeHandler
{
  public AutoProvisioningChallengeHandler(String paramString)
  {
    super(paramString);
  }

  protected JSONObject createJsonCsr(String paramString1, String paramString2, String paramString3)
  {
    return null;
  }

  public void handleChallenge(JSONObject paramJSONObject)
  {
  }

  public void handleFailure(JSONObject paramJSONObject)
  {
  }

  public void handleSuccess(JSONObject paramJSONObject)
  {
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.challengehandler.AutoProvisioningChallengeHandler
 * JD-Core Version:    0.6.0
 */