package com.worklight.androidgap.jsonstore.util.jackson;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonOrgJSONArraySerializer extends SerializerBase<JSONArray>
{
  public static final JsonOrgJSONArraySerializer instance = new JsonOrgJSONArraySerializer();

  protected JsonOrgJSONArraySerializer()
  {
    super(JSONArray.class);
  }

  private void serializeContents(JSONArray paramJSONArray, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    int i = paramJSONArray.length();
    int j = 0;
    if (j < i)
    {
      Object localObject = paramJSONArray.opt(j);
      if ((localObject == null) || (localObject == JSONObject.NULL))
        paramJsonGenerator.writeNull();
      while (true)
      {
        j++;
        break;
        Class localClass = localObject.getClass();
        if ((localClass == JSONObject.class) || (JSONObject.class.isAssignableFrom(localClass)))
        {
          JsonOrgJSONObjectSerializer.instance.serialize((JSONObject)localObject, paramJsonGenerator, paramSerializerProvider);
          continue;
        }
        if ((localClass == JSONArray.class) || (JSONArray.class.isAssignableFrom(localClass)))
        {
          serialize((JSONArray)localObject, paramJsonGenerator, paramSerializerProvider);
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
  }

  public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    throws JsonMappingException
  {
    return createSchemaNode("array", true);
  }

  public void serialize(JSONArray paramJSONArray, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeStartArray();
    serializeContents(paramJSONArray, paramJsonGenerator, paramSerializerProvider);
    paramJsonGenerator.writeEndArray();
  }

  public void serializeWithType(JSONArray paramJSONArray, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonGenerationException
  {
    paramTypeSerializer.writeTypePrefixForArray(paramJSONArray, paramJsonGenerator);
    serializeContents(paramJSONArray, paramJsonGenerator, paramSerializerProvider);
    paramTypeSerializer.writeTypeSuffixForArray(paramJSONArray, paramJsonGenerator);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     com.worklight.androidgap.jsonstore.util.jackson.JsonOrgJSONArraySerializer
 * JD-Core Version:    0.6.0
 */