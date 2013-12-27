package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;

public final class BigIntegerNode extends NumericNode
{
  protected final BigInteger _value;

  public BigIntegerNode(BigInteger paramBigInteger)
  {
    this._value = paramBigInteger;
  }

  public static BigIntegerNode valueOf(BigInteger paramBigInteger)
  {
    return new BigIntegerNode(paramBigInteger);
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
    while (((BigIntegerNode)paramObject)._value == this._value);
    return false;
  }

  public BigInteger getBigIntegerValue()
  {
    return this._value;
  }

  public BigDecimal getDecimalValue()
  {
    return new BigDecimal(this._value);
  }

  public double getDoubleValue()
  {
    return this._value.doubleValue();
  }

  public int getIntValue()
  {
    return this._value.intValue();
  }

  public long getLongValue()
  {
    return this._value.longValue();
  }

  public JsonParser.NumberType getNumberType()
  {
    return JsonParser.NumberType.BIG_INTEGER;
  }

  public Number getNumberValue()
  {
    return this._value;
  }

  public boolean getValueAsBoolean(boolean paramBoolean)
  {
    return !BigInteger.ZERO.equals(this._value);
  }

  public String getValueAsText()
  {
    return this._value.toString();
  }

  public int hashCode()
  {
    return this._value.hashCode();
  }

  public boolean isBigInteger()
  {
    return true;
  }

  public boolean isIntegralNumber()
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
 * Qualified Name:     org.codehaus.jackson.node.BigIntegerNode
 * JD-Core Version:    0.6.0
 */