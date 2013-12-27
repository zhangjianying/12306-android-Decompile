package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

abstract class BaseNodeDeserializer<N extends JsonNode> extends StdDeserializer<N>
{
  public BaseNodeDeserializer(Class<N> paramClass)
  {
    super(paramClass);
  }

  protected void _handleDuplicateField(String paramString, ObjectNode paramObjectNode, JsonNode paramJsonNode1, JsonNode paramJsonNode2)
    throws JsonProcessingException
  {
  }

  protected void _reportProblem(JsonParser paramJsonParser, String paramString)
    throws JsonMappingException
  {
    throw new JsonMappingException(paramString, paramJsonParser.getTokenLocation());
  }

  protected final JsonNode deserializeAny(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonNodeFactory localJsonNodeFactory = paramDeserializationContext.getNodeFactory();
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[paramJsonParser.getCurrentToken().ordinal()])
    {
    default:
      throw paramDeserializationContext.mappingException(getValueClass());
    case 1:
    case 2:
      return deserializeObject(paramJsonParser, paramDeserializationContext);
    case 3:
      return deserializeArray(paramJsonParser, paramDeserializationContext);
    case 4:
      return localJsonNodeFactory.textNode(paramJsonParser.getText());
    case 5:
      JsonParser.NumberType localNumberType = paramJsonParser.getNumberType();
      if ((localNumberType == JsonParser.NumberType.BIG_INTEGER) || (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_INTEGER_FOR_INTS)))
        return localJsonNodeFactory.numberNode(paramJsonParser.getBigIntegerValue());
      if (localNumberType == JsonParser.NumberType.INT)
        return localJsonNodeFactory.numberNode(paramJsonParser.getIntValue());
      return localJsonNodeFactory.numberNode(paramJsonParser.getLongValue());
    case 6:
      if ((paramJsonParser.getNumberType() == JsonParser.NumberType.BIG_DECIMAL) || (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS)))
        return localJsonNodeFactory.numberNode(paramJsonParser.getDecimalValue());
      return localJsonNodeFactory.numberNode(paramJsonParser.getDoubleValue());
    case 7:
      return localJsonNodeFactory.booleanNode(true);
    case 8:
      return localJsonNodeFactory.booleanNode(false);
    case 9:
    }
    return localJsonNodeFactory.nullNode();
  }

  protected final ArrayNode deserializeArray(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    ArrayNode localArrayNode = paramDeserializationContext.getNodeFactory().arrayNode();
    while (paramJsonParser.nextToken() != JsonToken.END_ARRAY)
      localArrayNode.add(deserializeAny(paramJsonParser, paramDeserializationContext));
    return localArrayNode;
  }

  protected final ObjectNode deserializeObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    ObjectNode localObjectNode = paramDeserializationContext.getNodeFactory().objectNode();
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.START_OBJECT);
    for (localJsonToken = paramJsonParser.nextToken(); localJsonToken == JsonToken.FIELD_NAME; localJsonToken = paramJsonParser.nextToken())
    {
      String str = paramJsonParser.getCurrentName();
      paramJsonParser.nextToken();
      JsonNode localJsonNode1 = deserializeAny(paramJsonParser, paramDeserializationContext);
      JsonNode localJsonNode2 = localObjectNode.put(str, localJsonNode1);
      if (localJsonNode2 == null)
        continue;
      _handleDuplicateField(str, localObjectNode, localJsonNode2, localJsonNode1);
    }
    return localObjectNode;
  }

  public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
    throws IOException, JsonProcessingException
  {
    return paramTypeDeserializer.deserializeTypedFromAny(paramJsonParser, paramDeserializationContext);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BaseNodeDeserializer
 * JD-Core Version:    0.6.0
 */