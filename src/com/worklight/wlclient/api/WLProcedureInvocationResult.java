package com.worklight.wlclient.api;

import com.worklight.common.WLUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WLProcedureInvocationResult extends WLResponse
{
  private static final String JSON_KEY_IS_SUCCESSFUL = "isSuccessful";
  private JSONObject jsonResult;

  WLProcedureInvocationResult(WLResponse paramWLResponse)
  {
    super(paramWLResponse);
  }

  public JSONObject getResult()
    throws JSONException
  {
    if (this.jsonResult == null)
      this.jsonResult = WLUtils.convertStringToJSON(this.responseText);
    return this.jsonResult;
  }

  public boolean isSuccessful()
  {
    try
    {
      boolean bool = getResult().getBoolean("isSuccessful");
      return bool;
    }
    catch (JSONException localJSONException)
    {
    }
    return false;
  }

  public String toString()
  {
    return "WLProcedureInvocationResult [isSuccessful=" + isSuccessful() + ", result=" + this.jsonResult + "]";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLProcedureInvocationResult
 * JD-Core Version:    0.6.0
 */