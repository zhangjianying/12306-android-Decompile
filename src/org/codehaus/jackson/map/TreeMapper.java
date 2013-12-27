package org.codehaus.jackson.map;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;

@Deprecated
public class TreeMapper extends JsonNodeFactory
{
  protected ObjectMapper _objectMapper;

  public TreeMapper()
  {
    this(null);
  }

  public TreeMapper(ObjectMapper paramObjectMapper)
  {
    this._objectMapper = paramObjectMapper;
  }

  public JsonFactory getJsonFactory()
  {
    return objectMapper().getJsonFactory();
  }

  protected ObjectMapper objectMapper()
  {
    monitorenter;
    try
    {
      if (this._objectMapper == null)
        this._objectMapper = new ObjectMapper();
      ObjectMapper localObjectMapper = this._objectMapper;
      return localObjectMapper;
    }
    finally
    {
      monitorexit;
    }
    throw localObject;
  }

  public JsonNode readTree(File paramFile)
    throws IOException, JsonParseException
  {
    Object localObject = (JsonNode)objectMapper().readValue(paramFile, JsonNode.class);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(InputStream paramInputStream)
    throws IOException, JsonParseException
  {
    Object localObject = (JsonNode)objectMapper().readValue(paramInputStream, JsonNode.class);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(Reader paramReader)
    throws IOException, JsonParseException
  {
    Object localObject = (JsonNode)objectMapper().readValue(paramReader, JsonNode.class);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(String paramString)
    throws IOException, JsonParseException
  {
    Object localObject = (JsonNode)objectMapper().readValue(paramString, JsonNode.class);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(URL paramURL)
    throws IOException, JsonParseException
  {
    Object localObject = (JsonNode)objectMapper().readValue(paramURL, JsonNode.class);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(JsonParser paramJsonParser)
    throws IOException, JsonParseException
  {
    if ((paramJsonParser.getCurrentToken() == null) && (paramJsonParser.nextToken() == null))
      return null;
    return objectMapper().readTree(paramJsonParser);
  }

  public JsonNode readTree(byte[] paramArrayOfByte)
    throws IOException, JsonParseException
  {
    Object localObject = (JsonNode)objectMapper().readValue(paramArrayOfByte, 0, paramArrayOfByte.length, JsonNode.class);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public void writeTree(JsonNode paramJsonNode, File paramFile)
    throws IOException, JsonParseException
  {
    objectMapper().writeValue(paramFile, paramJsonNode);
  }

  public void writeTree(JsonNode paramJsonNode, OutputStream paramOutputStream)
    throws IOException, JsonParseException
  {
    objectMapper().writeValue(paramOutputStream, paramJsonNode);
  }

  public void writeTree(JsonNode paramJsonNode, Writer paramWriter)
    throws IOException, JsonParseException
  {
    objectMapper().writeValue(paramWriter, paramJsonNode);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.TreeMapper
 * JD-Core Version:    0.6.0
 */