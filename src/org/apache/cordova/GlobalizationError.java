package org.apache.cordova;

import org.json.JSONException;
import org.json.JSONObject;

public class GlobalizationError extends Exception
{
  public static final String FORMATTING_ERROR = "FORMATTING_ERROR";
  public static final String PARSING_ERROR = "PARSING_ERROR";
  public static final String PATTERN_ERROR = "PATTERN_ERROR";
  public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";
  private static final long serialVersionUID = 1L;
  int error = 0;

  public GlobalizationError()
  {
  }

  public GlobalizationError(String paramString)
  {
    if (paramString.equalsIgnoreCase("FORMATTING_ERROR"))
      this.error = 1;
    do
    {
      return;
      if (!paramString.equalsIgnoreCase("PARSING_ERROR"))
        continue;
      this.error = 2;
      return;
    }
    while (!paramString.equalsIgnoreCase("PATTERN_ERROR"));
    this.error = 3;
  }

  public int getErrorCode()
  {
    return this.error;
  }

  public String getErrorString()
  {
    switch (this.error)
    {
    default:
      return "";
    case 0:
      return "UNKNOWN_ERROR";
    case 1:
      return "FORMATTING_ERROR";
    case 2:
      return "PARSING_ERROR";
    case 3:
    }
    return "PATTERN_ERROR";
  }

  public JSONObject toJson()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("code", getErrorCode());
      localJSONObject.put("message", getErrorString());
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
    }
    return localJSONObject;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.apache.cordova.GlobalizationError
 * JD-Core Version:    0.6.0
 */