package com.MobileTicket;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class CheckCodePlugin extends CordovaPlugin
{
  private static final String GET_ACTION = "getcheckcode";

  public boolean execute(String paramString, JSONArray paramJSONArray, CallbackContext paramCallbackContext)
    throws JSONException
  {
    boolean bool = paramString.equals("getcheckcode");
    int i = 0;
    if (bool);
    try
    {
      paramCallbackContext.success(CheckCodeUtil.checkcode("", paramJSONArray.getString(0)));
      i = 1;
      return i;
    }
    catch (JSONException localJSONException)
    {
      paramCallbackContext.error("Failed to parse parameters");
    }
    return false;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.MobileTicket.CheckCodePlugin
 * JD-Core Version:    0.6.0
 */