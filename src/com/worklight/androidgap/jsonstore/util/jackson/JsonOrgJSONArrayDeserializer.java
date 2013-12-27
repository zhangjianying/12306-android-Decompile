package com.worklight.androidgap.jsonstore.util.jackson;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonOrgJSONArrayDeserializer extends StdDeserializer<JSONArray>
{
  protected static final JsonOrgJSONArrayDeserializer instance = new JsonOrgJSONArrayDeserializer();

  protected JsonOrgJSONArrayDeserializer()
  {
    super(JSONArray.class);
  }

  public JSONArray deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JacksonSerializedJSONArray localJacksonSerializedJSONArray = new JacksonSerializedJSONArray();
    JsonToken localJsonToken = paramJsonParser.nextToken();
    if (localJsonToken != JsonToken.END_ARRAY)
    {
      switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[localJsonToken.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      }
      while (true)
      {
        localJsonToken = paramJsonParser.nextToken();
        break;
        localJacksonSerializedJSONArray.put(deserialize(paramJsonParser, paramDeserializationContext));
        continue;
        localJacksonSerializedJSONArray.put(JsonOrgJSONObjectDeserializer.instance.deserialize(paramJsonParser, paramDeserializationContext));
        continue;
        localJacksonSerializedJSONArray.put(paramJsonParser.getEmbeddedObject());
        continue;
        localJacksonSerializedJSONArray.put(Boolean.FALSE);
        continue;
        localJacksonSerializedJSONArray.put(JSONObject.NULL);
        continue;
        localJacksonSerializedJSONArray.put(paramJsonParser.getNumberValue());
        continue;
        localJacksonSerializedJSONArray.put(paramJsonParser.getNumberValue());
        continue;
        localJacksonSerializedJSONArray.put(paramJsonParser.getText());
        continue;
        localJacksonSerializedJSONArray.put(Boolean.TRUE);
      }
    }
    return localJacksonSerializedJSONArray;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.jackson.JsonOrgJSONArrayDeserializer
 * JD-Core Version:    0.6.0
 */