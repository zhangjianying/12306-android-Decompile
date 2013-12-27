package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

public class JsonNodeDeserializer extends BaseNodeDeserializer<JsonNode>
{

  @Deprecated
  public static final JsonNodeDeserializer instance = new JsonNodeDeserializer();

  protected JsonNodeDeserializer()
  {
    super(JsonNode.class);
  }

  public static JsonDeserializer<? extends JsonNode> getDeserializer(Class<?> paramClass)
  {
    if (paramClass == ObjectNode.class)
      return ObjectDeserializer.getInstance();
    if (paramClass == ArrayNode.class)
      return ArrayDeserializer.getInstance();
    return instance;
  }

  public JsonNode deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    return deserializeAny(paramJsonParser, paramDeserializationContext);
  }

  static final class ArrayDeserializer extends BaseNodeDeserializer<ArrayNode>
  {
    protected static final ArrayDeserializer _instance = new ArrayDeserializer();

    protected ArrayDeserializer()
    {
      super();
    }

    public static ArrayDeserializer getInstance()
    {
      return _instance;
    }

    public ArrayNode deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      if (paramJsonParser.isExpectedStartArrayToken())
        return deserializeArray(paramJsonParser, paramDeserializationContext);
      throw paramDeserializationContext.mappingException(ArrayNode.class);
    }
  }

  static final class ObjectDeserializer extends BaseNodeDeserializer<ObjectNode>
  {
    protected static final ObjectDeserializer _instance = new ObjectDeserializer();

    protected ObjectDeserializer()
    {
      super();
    }

    public static ObjectDeserializer getInstance()
    {
      return _instance;
    }

    public ObjectNode deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      if (paramJsonParser.getCurrentToken() == JsonToken.START_OBJECT)
      {
        paramJsonParser.nextToken();
        return deserializeObject(paramJsonParser, paramDeserializationContext);
      }
      if (paramJsonParser.getCurrentToken() == JsonToken.FIELD_NAME)
        return deserializeObject(paramJsonParser, paramDeserializationContext);
      throw paramDeserializationContext.mappingException(ObjectNode.class);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.JsonNodeDeserializer
 * JD-Core Version:    0.6.0
 */