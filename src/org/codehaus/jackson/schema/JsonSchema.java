package org.codehaus.jackson.schema;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

public class JsonSchema
{
  private final ObjectNode schema;

  @JsonCreator
  public JsonSchema(ObjectNode paramObjectNode)
  {
    this.schema = paramObjectNode;
  }

  public static JsonNode getDefaultSchemaNode()
  {
    ObjectNode localObjectNode = JsonNodeFactory.instance.objectNode();
    localObjectNode.put("type", "any");
    return localObjectNode;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    JsonSchema localJsonSchema;
    while (true)
    {
      return true;
      if (paramObject == null)
        return false;
      if (!(paramObject instanceof JsonSchema))
        return false;
      localJsonSchema = (JsonSchema)paramObject;
      if (this.schema != null)
        break;
      if (localJsonSchema.schema != null)
        return false;
    }
    return this.schema.equals(localJsonSchema.schema);
  }

  @JsonValue
  public ObjectNode getSchemaNode()
  {
    return this.schema;
  }

  public String toString()
  {
    return this.schema.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.schema.JsonSchema
 * JD-Core Version:    0.6.0
 */