package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;

@JacksonStdImpl
public final class IndexedStringListSerializer extends StaticListSerializerBase<List<String>>
  implements ResolvableSerializer
{
  protected JsonSerializer<String> _serializer;

  public IndexedStringListSerializer(BeanProperty paramBeanProperty)
  {
    super(List.class, paramBeanProperty);
  }

  private final void serializeContents(List<String> paramList, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    for (int i = 0; ; i++)
    {
      try
      {
        int j = paramList.size();
        if (i < j)
        {
          String str = (String)paramList.get(i);
          if (str == null)
          {
            paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
            continue;
          }
          paramJsonGenerator.writeString(str);
        }
      }
      catch (Exception localException)
      {
        wrapAndThrow(paramSerializerProvider, localException, paramList, i);
      }
      return;
    }
  }

  private final void serializeUsingCustom(List<String> paramList, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    for (int i = 0; ; i++)
    {
      try
      {
        int j = paramList.size();
        JsonSerializer localJsonSerializer = this._serializer;
        i = 0;
        if (i < j)
        {
          String str = (String)paramList.get(i);
          if (str == null)
          {
            paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
            continue;
          }
          localJsonSerializer.serialize(str, paramJsonGenerator, paramSerializerProvider);
        }
      }
      catch (Exception localException)
      {
        wrapAndThrow(paramSerializerProvider, localException, paramList, i);
      }
      return;
    }
  }

  protected JsonNode contentSchema()
  {
    return createSchemaNode("string", true);
  }

  public void resolve(SerializerProvider paramSerializerProvider)
    throws JsonMappingException
  {
    JsonSerializer localJsonSerializer = paramSerializerProvider.findValueSerializer(String.class, this._property);
    if (!isDefaultSerializer(localJsonSerializer))
      this._serializer = localJsonSerializer;
  }

  public void serialize(List<String> paramList, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeStartArray();
    if (this._serializer == null)
      serializeContents(paramList, paramJsonGenerator, paramSerializerProvider);
    while (true)
    {
      paramJsonGenerator.writeEndArray();
      return;
      serializeUsingCustom(paramList, paramJsonGenerator, paramSerializerProvider);
    }
  }

  public void serializeWithType(List<String> paramList, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonGenerationException
  {
    paramTypeSerializer.writeTypePrefixForArray(paramList, paramJsonGenerator);
    if (this._serializer == null)
      serializeContents(paramList, paramJsonGenerator, paramSerializerProvider);
    while (true)
    {
      paramTypeSerializer.writeTypeSuffixForArray(paramList, paramJsonGenerator);
      return;
      serializeUsingCustom(paramList, paramJsonGenerator, paramSerializerProvider);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.IndexedStringListSerializer
 * JD-Core Version:    0.6.0
 */