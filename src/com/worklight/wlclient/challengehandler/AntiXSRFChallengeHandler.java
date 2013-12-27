package com.worklight.wlclient.challengehandler;

import com.worklight.common.WLUtils;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.challengehandler.WLChallengeHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class AntiXSRFChallengeHandler extends WLChallengeHandler
{
  private static final String PROTOCOL_ERROR_MESSAGE = "Application will exit because wrong JSON arrived when processing it from AntiXSRFChallengeHandler with ";

  public AntiXSRFChallengeHandler(String paramString)
  {
    super(paramString);
  }

  public void handleChallenge(JSONObject paramJSONObject)
  {
    try
    {
      String str = paramJSONObject.getString("WL-Instance-Id");
      WLClient.getInstance().addGlobalHeader("WL-Instance-Id", str);
      submitChallengeAnswer(null);
      return;
    }
    catch (JSONException localJSONException)
    {
      WLUtils.error("Application will exit because wrong JSON arrived when processing it from AntiXSRFChallengeHandler with " + localJSONException.getMessage(), localJSONException);
    }
    throw new RuntimeException("Application will exit because wrong JSON arrived when processing it from AntiXSRFChallengeHandler with ");
  }

  public void handleFailure(JSONObject paramJSONObject)
  {
  }

  public void handleSuccess(JSONObject paramJSONObject)
  {
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.challengehandler.AntiXSRFChallengeHandler
 * JD-Core Version:    0.6.0
 */