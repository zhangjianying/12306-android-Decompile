package com.worklight.wlclient.api;

import com.worklight.common.WLUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.List<Ljava.lang.String;>;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public final class WLProcedureInvocationFailResponse extends WLFailResponse
{
  private static final String JSON_KEY_ERROR_MESSAGES = "errors";
  private JSONObject jsonResult;

  WLProcedureInvocationFailResponse(WLFailResponse paramWLFailResponse)
  {
    super(paramWLFailResponse);
    setErrorCode(paramWLFailResponse.getErrorCode());
    setErrorMsg(paramWLFailResponse.getErrorMsg());
  }

  WLProcedureInvocationFailResponse(WLResponse paramWLResponse)
  {
    super(paramWLResponse);
  }

  WLProcedureInvocationFailResponse(HttpResponse paramHttpResponse)
  {
    super(paramHttpResponse);
  }

  @Deprecated
  public JSONObject getJSONResult()
    throws JSONException
  {
    return getResult();
  }

  public List<String> getProcedureInvocationErrors()
  {
    Object localObject = new ArrayList();
    try
    {
      if ((getResult() != null) && (getResult().get("errors") != null))
      {
        List localList = WLUtils.convertJSONArrayToList(getResult().getJSONArray("errors"));
        localObject = localList;
      }
      return localObject;
    }
    catch (JSONException localJSONException)
    {
    }
    return (List<String>)localObject;
  }

  public JSONObject getResult()
    throws JSONException
  {
    if ((this.jsonResult == null) && (this.responseText != null) && (this.responseText.length() > 0))
      this.jsonResult = WLUtils.convertStringToJSON(this.responseText);
    return this.jsonResult;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLProcedureInvocationFailResponse
 * JD-Core Version:    0.6.0
 */