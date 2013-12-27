package com.worklight.wlclient.api;

import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class WLProcedureInvocationResponse extends WLProcedureInvocationResult
{
  @Deprecated
  public WLProcedureInvocationResponse(WLResponse paramWLResponse)
  {
    super(paramWLResponse);
  }

  @Deprecated
  public JSONObject getJSONResult()
    throws JSONException
  {
    return super.getResult();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLProcedureInvocationResponse
 * JD-Core Version:    0.6.0
 */