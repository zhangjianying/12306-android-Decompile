package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class ClientState extends ClientMessageHeader
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = 1149075321417838161L;
  private MobileState mobileState;

  public ClientState()
  {
    setMessageType(MessageType.CLIENT_STATE);
  }

  public final Boolean clean()
  {
    super.clean();
    this.mobileState.clean();
    this.mobileState = null;
    return Boolean.valueOf(true);
  }

  public final boolean equals(Object paramObject)
  {
    if (this == paramObject);
    ClientState localClientState;
    do
      while (true)
      {
        return true;
        if ((!super.equals(paramObject)) || (getClass() != paramObject.getClass()))
          return false;
        localClientState = (ClientState)paramObject;
        if (this.mobileState != null)
          break;
        if (localClientState.mobileState != null)
          return false;
      }
    while (this.mobileState.equals(localClientState.mobileState));
    return false;
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject = null;
    try
    {
      localJSONObject = super.getJSON();
      localJSONObject.put("mobileState", getMobileState().getJSON());
      return localJSONObject;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject;
  }

  public final MobileState getMobileState()
  {
    return this.mobileState;
  }

  public final int hashCode()
  {
    int i = 31 * super.hashCode();
    if (this.mobileState == null);
    for (int j = 0; ; j = this.mobileState.hashCode())
      return j + i;
  }

  public final void setMobileState(MobileState paramMobileState)
  {
    this.mobileState = paramMobileState;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.ClientState
 * JD-Core Version:    0.6.0
 */