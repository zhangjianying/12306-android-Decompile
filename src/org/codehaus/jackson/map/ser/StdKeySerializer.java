package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.SerializerProvider;

public final class StdKeySerializer extends SerializerBase<Object>
{
  static final StdKeySerializer instace = new StdKeySerializer();

  public StdKeySerializer()
  {
    super(Object.class);
  }

  public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    throws JsonMappingException
  {
    return createSchemaNode("string");
  }

  public void serialize(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    if (paramObject.getClass() == String.class);
    for (String str = (String)paramObject; ; str = paramObject.toString())
    {
      paramJsonGenerator.writeFieldName(str);
      return;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.StdKeySerializer
 * JD-Core Version:    0.6.0
 */