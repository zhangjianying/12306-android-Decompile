package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.EnumSet;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;

public final class EnumSetDeserializer extends StdDeserializer<EnumSet<?>>
{
  final Class<Enum> _enumClass;
  final EnumDeserializer _enumDeserializer;

  public EnumSetDeserializer(EnumResolver paramEnumResolver)
  {
    super(EnumSet.class);
    this._enumDeserializer = new EnumDeserializer(paramEnumResolver);
    this._enumClass = paramEnumResolver.getEnumClass();
  }

  private EnumSet constructSet()
  {
    return EnumSet.noneOf(this._enumClass);
  }

  public EnumSet<?> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    if (!paramJsonParser.isExpectedStartArrayToken())
      throw paramDeserializationContext.mappingException(EnumSet.class);
    EnumSet localEnumSet = constructSet();
    while (true)
    {
      JsonToken localJsonToken = paramJsonParser.nextToken();
      if (localJsonToken == JsonToken.END_ARRAY)
        break;
      if (localJsonToken == JsonToken.VALUE_NULL)
        throw paramDeserializationContext.mappingException(this._enumClass);
      localEnumSet.add(this._enumDeserializer.deserialize(paramJsonParser, paramDeserializationContext));
    }
    return localEnumSet;
  }

  public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
    throws IOException, JsonProcessingException
  {
    return paramTypeDeserializer.deserializeTypedFromArray(paramJsonParser, paramDeserializationContext);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.EnumSetDeserializer
 * JD-Core Version:    0.6.0
 */