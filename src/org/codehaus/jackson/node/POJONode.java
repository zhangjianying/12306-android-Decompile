package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

public final class POJONode extends ValueNode
{
  protected final Object _value;

  public POJONode(Object paramObject)
  {
    this._value = paramObject;
  }

  public JsonToken asToken()
  {
    return JsonToken.VALUE_EMBEDDED_OBJECT;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    POJONode localPOJONode;
    while (true)
    {
      return true;
      if (paramObject == null)
        return false;
      if (paramObject.getClass() != getClass())
        return false;
      localPOJONode = (POJONode)paramObject;
      if (this._value != null)
        break;
      if (localPOJONode._value != null)
        return false;
    }
    return this._value.equals(localPOJONode._value);
  }

  public Object getPojo()
  {
    return this._value;
  }

  public boolean getValueAsBoolean(boolean paramBoolean)
  {
    if ((this._value != null) && ((this._value instanceof Boolean)))
      paramBoolean = ((Boolean)this._value).booleanValue();
    return paramBoolean;
  }

  public double getValueAsDouble(double paramDouble)
  {
    if ((this._value instanceof Number))
      paramDouble = ((Number)this._value).doubleValue();
    return paramDouble;
  }

  public int getValueAsInt(int paramInt)
  {
    if ((this._value instanceof Number))
      paramInt = ((Number)this._value).intValue();
    return paramInt;
  }

  public long getValueAsLong(long paramLong)
  {
    if ((this._value instanceof Number))
      paramLong = ((Number)this._value).longValue();
    return paramLong;
  }

  public String getValueAsText()
  {
    if (this._value == null)
      return "null";
    return this._value.toString();
  }

  public int hashCode()
  {
    return this._value.hashCode();
  }

  public boolean isPojo()
  {
    return true;
  }

  public final void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonProcessingException
  {
    if (this._value == null)
    {
      paramJsonGenerator.writeNull();
      return;
    }
    paramJsonGenerator.writeObject(this._value);
  }

  public String toString()
  {
    return String.valueOf(this._value);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.POJONode
 * JD-Core Version:    0.6.0
 */