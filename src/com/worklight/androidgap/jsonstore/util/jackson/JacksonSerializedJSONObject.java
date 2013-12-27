package com.worklight.androidgap.jsonstore.util.jackson;

import org.json.JSONException;
import org.json.JSONObject;

public class JacksonSerializedJSONObject extends JSONObject
{
  private JSONObject wrappedObject;

  public JacksonSerializedJSONObject()
  {
  }

  protected JacksonSerializedJSONObject(JSONObject paramJSONObject)
  {
    this.wrappedObject = paramJSONObject;
  }

  public String toString()
  {
    if (this.wrappedObject == null);
    while (true)
    {
      return JsonOrgModule.serialize(this);
      this = this.wrappedObject;
    }
  }

  public String toString(int paramInt)
    throws JSONException
  {
    return toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONObject
 * JD-Core Version:    0.6.0
 */