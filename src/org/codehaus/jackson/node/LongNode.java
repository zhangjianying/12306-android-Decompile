package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.map.SerializerProvider;

public final class LongNode extends NumericNode
{
  final long _value;

  public LongNode(long paramLong)
  {
    this._value = paramLong;
  }

  public static LongNode valueOf(long paramLong)
  {
    return new LongNode(paramLong);
  }

  public JsonToken asToken()
  {
    return JsonToken.VALUE_NUMBER_INT;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    do
    {
      return true;
      if (paramObject == null)
        return false;
      if (paramObject.getClass() != getClass())
        return false;
    }
    while (((LongNode)paramObject)._value == this._value);
    return false;
  }

  public BigInteger getBigIntegerValue()
  {
    return BigInteger.valueOf(this._value);
  }

  public BigDecimal getDecimalValue()
  {
    return BigDecimal.valueOf(this._value);
  }

  public double getDoubleValue()
  {
    return this._value;
  }

  public int getIntValue()
  {
    return (int)this._value;
  }

  public long getLongValue()
  {
    return this._value;
  }

  public JsonParser.NumberType getNumberType()
  {
    return JsonParser.NumberType.LONG;
  }

  public Number getNumberValue()
  {
    return Long.valueOf(this._value);
  }

  public boolean getValueAsBoolean(boolean paramBoolean)
  {
    return this._value != 0L;
  }

  public String getValueAsText()
  {
    return NumberOutput.toString(this._value);
  }

  public int hashCode()
  {
    return (int)this._value ^ (int)(this._value >> 32);
  }

  public boolean isIntegralNumber()
  {
    return true;
  }

  public boolean isLong()
  {
    return true;
  }

  public final void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonProcessingException
  {
    paramJsonGenerator.writeNumber(this._value);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.LongNode
 * JD-Core Version:    0.6.0
 */