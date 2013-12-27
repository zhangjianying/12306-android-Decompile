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

public final class DoubleNode extends NumericNode
{
  protected final double _value;

  public DoubleNode(double paramDouble)
  {
    this._value = paramDouble;
  }

  public static DoubleNode valueOf(double paramDouble)
  {
    return new DoubleNode(paramDouble);
  }

  public JsonToken asToken()
  {
    return JsonToken.VALUE_NUMBER_FLOAT;
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
    while (((DoubleNode)paramObject)._value == this._value);
    return false;
  }

  public BigInteger getBigIntegerValue()
  {
    return getDecimalValue().toBigInteger();
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
    return ()this._value;
  }

  public JsonParser.NumberType getNumberType()
  {
    return JsonParser.NumberType.DOUBLE;
  }

  public Number getNumberValue()
  {
    return Double.valueOf(this._value);
  }

  public String getValueAsText()
  {
    return NumberOutput.toString(this._value);
  }

  public int hashCode()
  {
    long l = Double.doubleToLongBits(this._value);
    return (int)l ^ (int)(l >> 32);
  }

  public boolean isDouble()
  {
    return true;
  }

  public boolean isFloatingPointNumber()
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
 * Qualified Name:     org.codehaus.jackson.node.DoubleNode
 * JD-Core Version:    0.6.0
 */