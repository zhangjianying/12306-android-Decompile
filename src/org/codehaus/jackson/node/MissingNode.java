package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;

public final class MissingNode extends BaseJsonNode
{
  private static final MissingNode instance = new MissingNode();

  public static MissingNode getInstance()
  {
    return instance;
  }

  public JsonToken asToken()
  {
    return JsonToken.NOT_AVAILABLE;
  }

  public boolean equals(Object paramObject)
  {
    return paramObject == this;
  }

  public double getValueAsDouble(double paramDouble)
  {
    return 0.0D;
  }

  public int getValueAsInt(int paramInt)
  {
    return 0;
  }

  public long getValueAsLong(long paramLong)
  {
    return 0L;
  }

  public String getValueAsText()
  {
    return null;
  }

  public boolean isMissingNode()
  {
    return true;
  }

  public JsonNode path(int paramInt)
  {
    return this;
  }

  public JsonNode path(String paramString)
  {
    return this;
  }

  public final void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonProcessingException
  {
    paramJsonGenerator.writeNull();
  }

  public void serializeWithType(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonProcessingException
  {
    paramJsonGenerator.writeNull();
  }

  public String toString()
  {
    return "";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.MissingNode
 * JD-Core Version:    0.6.0
 */