package com.worklight.wlclient.api;

import com.worklight.common.WLUtils;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

public class WLFailResponse extends WLResponse
{
  private static final String JSON_KEY_ERROR_CODE = "errorCode";
  private static final String JSON_KEY_ERROR_MSG = "errorMsg";
  WLErrorCode errorCode;
  String errorMsg;

  public WLFailResponse(WLErrorCode paramWLErrorCode, String paramString, WLRequestOptions paramWLRequestOptions)
  {
    super(500, "", paramWLRequestOptions);
    setErrorCode(paramWLErrorCode);
    setErrorMsg(paramString);
  }

  public WLFailResponse(WLResponse paramWLResponse)
  {
    super(paramWLResponse);
    parseErrorFromResponse();
  }

  WLFailResponse(HttpResponse paramHttpResponse)
  {
    super(paramHttpResponse);
    parseErrorFromResponse();
  }

  private void parseErrorFromResponse()
  {
    if ((getResponseText() != null) && (getResponseText().length() > 0));
    try
    {
      JSONObject localJSONObject = WLUtils.convertStringToJSON(getResponseText());
      this.errorCode = WLErrorCode.UNEXPECTED_ERROR;
      if (localJSONObject.has("errorCode"))
        this.errorCode = WLErrorCode.valueOf(localJSONObject.getString("errorCode"));
      if (localJSONObject.has("errorMsg"))
        this.errorMsg = localJSONObject.getString("errorMsg");
      return;
    }
    catch (Exception localException)
    {
      do
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = getResponseText();
        WLUtils.debug(String.format("Additional error information is not available for the current response and response text is: %s", arrayOfObject), localException);
        this.errorCode = WLErrorCode.UNEXPECTED_ERROR;
        if (getStatus() >= 500)
        {
          this.errorCode = WLErrorCode.UNRESPONSIVE_HOST;
          return;
        }
        if (getStatus() < 408)
          continue;
        this.errorCode = WLErrorCode.REQUEST_TIMEOUT;
        return;
      }
      while (getStatus() < 404);
      this.errorCode = WLErrorCode.REQUEST_SERVICE_NOT_FOUND;
    }
  }

  public WLErrorCode getErrorCode()
  {
    return this.errorCode;
  }

  public String getErrorMsg()
  {
    if (this.errorMsg == null)
      return this.errorCode.getDescription();
    return this.errorMsg;
  }

  void setErrorCode(WLErrorCode paramWLErrorCode)
  {
    this.errorCode = paramWLErrorCode;
  }

  void setErrorMsg(String paramString)
  {
    this.errorMsg = paramString;
  }

  public String toString()
  {
    return super.toString() + " WLFailResponse [errorMsg=" + this.errorMsg + ", errorCode=" + this.errorCode + "]";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLFailResponse
 * JD-Core Version:    0.6.0
 */