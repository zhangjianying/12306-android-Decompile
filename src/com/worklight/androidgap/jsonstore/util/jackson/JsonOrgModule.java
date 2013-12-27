package com.worklight.androidgap.jsonstore.util.jackson;

import java.io.StringWriter;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonOrgModule extends SimpleModule
{
  private static final ObjectMapper mapper = new ObjectMapper();

  static
  {
    mapper.registerModule(new JsonOrgModule());
  }

  private JsonOrgModule()
  {
    super("JsonOrgModule", new Version(1, 0, 0, null));
    addDeserializer(JSONArray.class, JsonOrgJSONArrayDeserializer.instance);
    addDeserializer(JSONObject.class, JsonOrgJSONObjectDeserializer.instance);
    addSerializer(JSONArray.class, JsonOrgJSONArraySerializer.instance);
    addSerializer(JSONObject.class, JsonOrgJSONObjectSerializer.instance);
  }

  public static JSONArray deserializeJSONArray(String paramString)
    throws Throwable
  {
    return (JSONArray)mapper.readValue(paramString, JSONArray.class);
  }

  public static JSONObject deserializeJSONObject(String paramString)
    throws Throwable
  {
    return (JSONObject)mapper.readValue(paramString, JSONObject.class);
  }

  public static String serialize(JSONArray paramJSONArray)
  {
    try
    {
      StringWriter localStringWriter = new StringWriter();
      mapper.writeValue(localStringWriter, paramJSONArray);
      localStringWriter.close();
      String str = localStringWriter.toString();
      return str;
    }
    catch (Throwable localThrowable)
    {
    }
    return null;
  }

  public static String serialize(JSONObject paramJSONObject)
  {
    try
    {
      StringWriter localStringWriter = new StringWriter();
      mapper.writeValue(localStringWriter, paramJSONObject);
      localStringWriter.close();
      String str = localStringWriter.toString();
      return str;
    }
    catch (Throwable localThrowable)
    {
    }
    return null;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.jackson.JsonOrgModule
 * JD-Core Version:    0.6.0
 */