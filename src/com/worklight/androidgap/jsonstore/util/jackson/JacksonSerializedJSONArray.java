package com.worklight.androidgap.jsonstore.util.jackson;

import org.json.JSONArray;
import org.json.JSONException;

public class JacksonSerializedJSONArray extends JSONArray
{
  private JSONArray wrappedArray;

  public JacksonSerializedJSONArray()
  {
  }

  protected JacksonSerializedJSONArray(JSONArray paramJSONArray)
  {
    this.wrappedArray = paramJSONArray;
  }

  public String toString()
  {
    if (this.wrappedArray == null);
    while (true)
    {
      return JsonOrgModule.serialize(this);
      this = this.wrappedArray;
    }
  }

  public String toString(int paramInt)
    throws JSONException
  {
    return toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.jackson.JacksonSerializedJSONArray
 * JD-Core Version:    0.6.0
 */