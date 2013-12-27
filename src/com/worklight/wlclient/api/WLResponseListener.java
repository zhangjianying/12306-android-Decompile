package com.worklight.wlclient.api;

public abstract interface WLResponseListener
{
  public abstract void onFailure(WLFailResponse paramWLFailResponse);

  public abstract void onSuccess(WLResponse paramWLResponse);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.api.WLResponseListener
 * JD-Core Version:    0.6.0
 */