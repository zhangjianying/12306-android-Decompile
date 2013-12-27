package com.tl.uic.model;

import org.json.JSONObject;

public abstract interface JsonBase extends ModelBase
{
  public static final int BIT_32 = 32;
  public static final int PRIME = 31;

  public abstract JSONObject getJSON();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.tl.uic.model.JsonBase
 * JD-Core Version:    0.6.0
 */