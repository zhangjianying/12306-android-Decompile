package com.worklight.wlclient;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;

public abstract interface WLRequestListener
{
  public abstract void onFailure(WLFailResponse paramWLFailResponse);

  public abstract void onSuccess(WLResponse paramWLResponse);
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.wlclient.WLRequestListener
 * JD-Core Version:    0.6.0
 */