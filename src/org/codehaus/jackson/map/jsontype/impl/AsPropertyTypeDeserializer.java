package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.JsonParserSequence;
import org.codehaus.jackson.util.TokenBuffer;

public class AsPropertyTypeDeserializer extends AsArrayTypeDeserializer
{
  protected final String _typePropertyName;

  public AsPropertyTypeDeserializer(JavaType paramJavaType, TypeIdResolver paramTypeIdResolver, BeanProperty paramBeanProperty, String paramString)
  {
    super(paramJavaType, paramTypeIdResolver, paramBeanProperty);
    this._typePropertyName = paramString;
  }

  public Object deserializeTypedFromAny(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    if (paramJsonParser.getCurrentToken() == JsonToken.START_ARRAY)
      return super.deserializeTypedFromArray(paramJsonParser, paramDeserializationContext);
    return deserializeTypedFromObject(paramJsonParser, paramDeserializationContext);
  }

  public Object deserializeTypedFromObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    TokenBuffer localTokenBuffer;
    if (localJsonToken == JsonToken.START_OBJECT)
    {
      localJsonToken = paramJsonParser.nextToken();
      localTokenBuffer = null;
    }
    while (true)
    {
      if (localJsonToken != JsonToken.FIELD_NAME)
        break label169;
      String str = paramJsonParser.getCurrentName();
      paramJsonParser.nextToken();
      if (this._typePropertyName.equals(str))
      {
        JsonDeserializer localJsonDeserializer = _findDeserializer(paramDeserializationContext, paramJsonParser.getText());
        if (localTokenBuffer != null)
          paramJsonParser = JsonParserSequence.createFlattened(localTokenBuffer.asParser(paramJsonParser), paramJsonParser);
        paramJsonParser.nextToken();
        return localJsonDeserializer.deserialize(paramJsonParser, paramDeserializationContext);
        if (localJsonToken == JsonToken.FIELD_NAME)
          break;
        throw paramDeserializationContext.wrongTokenException(paramJsonParser, JsonToken.START_OBJECT, "need JSON Object to contain As.PROPERTY type information (for class " + baseTypeName() + ")");
      }
      if (localTokenBuffer == null)
        localTokenBuffer = new TokenBuffer(null);
      localTokenBuffer.writeFieldName(str);
      localTokenBuffer.copyCurrentStructure(paramJsonParser);
      localJsonToken = paramJsonParser.nextToken();
    }
    label169: throw paramDeserializationContext.wrongTokenException(paramJsonParser, JsonToken.FIELD_NAME, "missing property '" + this._typePropertyName + "' that is to contain type id  (for class " + baseTypeName() + ")");
  }

  public String getPropertyName()
  {
    return this._typePropertyName;
  }

  public JsonTypeInfo.As getTypeInclusion()
  {
    return JsonTypeInfo.As.PROPERTY;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.AsPropertyTypeDeserializer
 * JD-Core Version:    0.6.0
 */