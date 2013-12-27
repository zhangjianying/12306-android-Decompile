package com.tl.uic.model;

import com.tl.uic.util.LogInternal;
import java.io.Serializable;
import org.json.JSONObject;

public class Screenview extends ClientMessageHeader
  implements JsonBase, Serializable
{
  private static final long serialVersionUID = -7422132544374507473L;
  private String logicalPageName;
  private String referringLogicalPageName;
  private ScreenviewType screenviewType;

  public Screenview()
  {
    init();
  }

  public Screenview(String paramString1, ScreenviewType paramScreenviewType, String paramString2)
  {
    init();
    this.logicalPageName = paramString1;
    this.screenviewType = paramScreenviewType;
    this.referringLogicalPageName = paramString2;
  }

  private void init()
  {
    setMessageType(MessageType.SCREENVIEW);
    setLogLevel(0);
  }

  public final Boolean clean()
  {
    super.clean();
    this.logicalPageName = null;
    this.screenviewType = null;
    this.referringLogicalPageName = null;
    return Boolean.valueOf(true);
  }

  public final JSONObject getJSON()
  {
    JSONObject localJSONObject1 = null;
    JSONObject localJSONObject2 = new JSONObject();
    try
    {
      localJSONObject1 = super.getJSON();
      localJSONObject2.put("type", getScreenviewType());
      localJSONObject2.put("name", getLogicalPageName());
      if ((getScreenviewType().equals(ScreenviewType.LOAD)) && (getReferringLogicalPageName() != null))
        localJSONObject2.put("referrer", getReferringLogicalPageName());
      localJSONObject1.put("screenview", localJSONObject2);
      return localJSONObject1;
    }
    catch (Exception localException)
    {
      LogInternal.logException(localException);
    }
    return localJSONObject1;
  }

  public final String getLogicalPageName()
  {
    return this.logicalPageName;
  }

  public final String getReferringLogicalPageName()
  {
    return this.referringLogicalPageName;
  }

  public final ScreenviewType getScreenviewType()
  {
    return this.screenviewType;
  }

  public final void setLogicalPageName(String paramString)
  {
    this.logicalPageName = paramString;
  }

  public final void setReferringLogicalPageName(String paramString)
  {
    this.referringLogicalPageName = paramString;
  }

  public final void setScreenviewType(ScreenviewType paramScreenviewType)
  {
    this.screenviewType = paramScreenviewType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.Screenview
 * JD-Core Version:    0.6.0
 */