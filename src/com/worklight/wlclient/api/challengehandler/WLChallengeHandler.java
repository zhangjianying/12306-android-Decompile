package com.worklight.wlclient.api.challengehandler;

import com.worklight.wlclient.WLRequest;
import org.json.JSONObject;

public abstract class WLChallengeHandler extends BaseChallengeHandler<JSONObject>
{
  public WLChallengeHandler(String paramString)
  {
    super(paramString);
  }

  public abstract void handleFailure(JSONObject paramJSONObject);

  public abstract void handleSuccess(JSONObject paramJSONObject);

  public void submitChallengeAnswer(Object paramObject)
  {
    if (paramObject == null)
      this.activeRequest.removeExpectedAnswer(getRealm());
    while (true)
    {
      this.activeRequest = null;
      return;
      this.activeRequest.submitAnswer(getRealm(), paramObject);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.challengehandler.WLChallengeHandler
 * JD-Core Version:    0.6.0
 */