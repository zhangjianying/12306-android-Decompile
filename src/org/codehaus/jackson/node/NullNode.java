package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

public final class NullNode extends ValueNode
{
  public static final NullNode instance = new NullNode();

  public static NullNode getInstance()
  {
    return instance;
  }

  public JsonToken asToken()
  {
    return JsonToken.VALUE_NULL;
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
    return "null";
  }

  public boolean isNull()
  {
    return true;
  }

  public final void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonProcessingException
  {
    paramJsonGenerator.writeNull();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.NullNode
 * JD-Core Version:    0.6.0
 */