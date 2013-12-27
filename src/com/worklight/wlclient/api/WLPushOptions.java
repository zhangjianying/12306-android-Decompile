package com.worklight.wlclient.api;

import java.util.HashMap;
import java.util.Map;

public class WLPushOptions
{
  private boolean alert = true;
  private boolean badge = true;
  private Map<String, String> parameters = new HashMap();
  private boolean sound = true;

  public void addSubscriptionParameter(String paramString1, String paramString2)
  {
    this.parameters.put(paramString1, paramString2);
  }

  public void addSubscriptionParameters(Map<String, String> paramMap)
  {
    this.parameters.putAll(paramMap);
  }

  public String getSubscriptionParameter(String paramString)
  {
    if (this.parameters != null)
      return (String)this.parameters.get(paramString);
    return "";
  }

  public Map<String, String> getSubscriptionParameters()
  {
    return this.parameters;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLPushOptions
 * JD-Core Version:    0.6.0
 */