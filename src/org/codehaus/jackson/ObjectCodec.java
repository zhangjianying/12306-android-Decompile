package org.codehaus.jackson;

import java.io.IOException;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public abstract class ObjectCodec
{
  public abstract JsonNode createArrayNode();

  public abstract JsonNode createObjectNode();

  public abstract JsonNode readTree(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException;

  public abstract <T> T readValue(JsonParser paramJsonParser, Class<T> paramClass)
    throws IOException, JsonProcessingException;

  public abstract <T> T readValue(JsonParser paramJsonParser, JavaType paramJavaType)
    throws IOException, JsonProcessingException;

  public abstract <T> T readValue(JsonParser paramJsonParser, TypeReference<?> paramTypeReference)
    throws IOException, JsonProcessingException;

  public abstract JsonParser treeAsTokens(JsonNode paramJsonNode);

  public abstract <T> T treeToValue(JsonNode paramJsonNode, Class<T> paramClass)
    throws IOException, JsonProcessingException;

  public abstract void writeTree(JsonGenerator paramJsonGenerator, JsonNode paramJsonNode)
    throws IOException, JsonProcessingException;

  public abstract void writeValue(JsonGenerator paramJsonGenerator, Object paramObject)
    throws IOException, JsonProcessingException;
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.ObjectCodec
 * JD-Core Version:    0.6.0
 */