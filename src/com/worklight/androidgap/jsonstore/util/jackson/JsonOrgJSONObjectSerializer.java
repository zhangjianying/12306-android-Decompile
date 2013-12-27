package com.worklight.androidgap.jsonstore.util.jackson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonOrgJSONObjectSerializer extends SerializerBase<JSONObject>
{
  public static final JsonOrgJSONObjectSerializer instance = new JsonOrgJSONObjectSerializer();

  public JsonOrgJSONObjectSerializer()
  {
    super(JSONObject.class);
  }

  public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    throws JsonMappingException
  {
    return createSchemaNode("object", true);
  }

  public void serialize(JSONObject paramJSONObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeStartObject();
    serializeContents(paramJSONObject, paramJsonGenerator, paramSerializerProvider);
    paramJsonGenerator.writeEndObject();
  }

  protected void serializeContents(JSONObject paramJSONObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    Iterator localIterator = paramJSONObject.keys();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Object localObject;
      try
      {
        localObject = paramJSONObject.get(str);
        if ((localObject == null) || (localObject == JSONObject.NULL))
          paramJsonGenerator.writeNullField(str);
      }
      catch (JSONException localJSONException)
      {
        throw new JsonGenerationException(localJSONException);
      }
      paramJsonGenerator.writeFieldName(str);
      Class localClass = localObject.getClass();
      if ((localClass == JSONObject.class) || (JSONObject.class.isAssignableFrom(localClass)))
      {
        serialize((JSONObject)localObject, paramJsonGenerator, paramSerializerProvider);
        continue;
      }
      if ((localClass == JSONArray.class) || (JSONArray.class.isAssignableFrom(localClass)))
      {
        JsonOrgJSONArraySerializer.instance.serialize((JSONArray)localObject, paramJsonGenerator, paramSerializerProvider);
        continue;
      }
      if (localClass == String.class)
      {
        paramJsonGenerator.writeString((String)localObject);
        continue;
      }
      if (localClass == Integer.class)
      {
        paramJsonGenerator.writeNumber(((Integer)localObject).intValue());
        continue;
      }
      if (localClass == Long.class)
      {
        paramJsonGenerator.writeNumber(((Long)localObject).longValue());
        continue;
      }
      if (localClass == Boolean.class)
      {
        paramJsonGenerator.writeBoolean(((Boolean)localObject).booleanValue());
        continue;
      }
      if (localClass == Double.class)
      {
        paramJsonGenerator.writeNumber(((Double)localObject).doubleValue());
        continue;
      }
      paramSerializerProvider.defaultSerializeValue(localObject, paramJsonGenerator);
    }
  }

  public void serializeWithType(JSONObject paramJSONObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonGenerationException
  {
    paramTypeSerializer.writeTypePrefixForObject(paramJSONObject, paramJsonGenerator);
    serializeContents(paramJSONObject, paramJsonGenerator, paramSerializerProvider);
    paramTypeSerializer.writeTypeSuffixForObject(paramJSONObject, paramJsonGenerator);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.jackson.JsonOrgJSONObjectSerializer
 * JD-Core Version:    0.6.0
 */