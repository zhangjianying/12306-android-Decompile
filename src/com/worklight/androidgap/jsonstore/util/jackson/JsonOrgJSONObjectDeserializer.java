package com.worklight.androidgap.jsonstore.util.jackson;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonOrgJSONObjectDeserializer extends StdDeserializer<JSONObject>
{
  protected static final JsonOrgJSONObjectDeserializer instance = new JsonOrgJSONObjectDeserializer();

  protected JsonOrgJSONObjectDeserializer()
  {
    super(JSONObject.class);
  }

  public JSONObject deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JacksonSerializedJSONObject localJacksonSerializedJSONObject = new JacksonSerializedJSONObject();
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    if (localJsonToken1 == JsonToken.START_OBJECT)
      localJsonToken1 = paramJsonParser.nextToken();
    try
    {
      if (localJsonToken1 == JsonToken.END_OBJECT)
        break label278;
      if (localJsonToken1 != JsonToken.FIELD_NAME)
        throw paramDeserializationContext.wrongTokenException(paramJsonParser, JsonToken.FIELD_NAME, "");
    }
    catch (JSONException localJSONException)
    {
      throw paramDeserializationContext.mappingException(localJSONException.getMessage());
    }
    String str = paramJsonParser.getCurrentName();
    JsonToken localJsonToken2 = paramJsonParser.nextToken();
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[localJsonToken2.ordinal()])
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
      localJsonToken1 = paramJsonParser.nextToken();
      break;
      localJacksonSerializedJSONObject.put(str, JsonOrgJSONArrayDeserializer.instance.deserialize(paramJsonParser, paramDeserializationContext));
      continue;
      localJacksonSerializedJSONObject.put(str, deserialize(paramJsonParser, paramDeserializationContext));
      continue;
      localJacksonSerializedJSONObject.put(str, paramJsonParser.getEmbeddedObject());
      continue;
      localJacksonSerializedJSONObject.put(str, Boolean.FALSE);
      continue;
      localJacksonSerializedJSONObject.put(str, JSONObject.NULL);
      continue;
      localJacksonSerializedJSONObject.put(str, paramJsonParser.getNumberValue());
      continue;
      localJacksonSerializedJSONObject.put(str, paramJsonParser.getNumberValue());
      continue;
      localJacksonSerializedJSONObject.put(str, paramJsonParser.getText());
      continue;
      localJacksonSerializedJSONObject.put(str, Boolean.TRUE);
    }
    label278: return localJacksonSerializedJSONObject;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.jackson.JsonOrgJSONObjectDeserializer
 * JD-Core Version:    0.6.0
 */