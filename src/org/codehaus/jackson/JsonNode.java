package org.codehaus.jackson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class JsonNode
  implements Iterable<JsonNode>
{
  protected static final List<JsonNode> NO_NODES = Collections.emptyList();
  protected static final List<String> NO_STRINGS = Collections.emptyList();

  public abstract JsonToken asToken();

  public abstract boolean equals(Object paramObject);

  public abstract JsonNode findParent(String paramString);

  public final List<JsonNode> findParents(String paramString)
  {
    List localList = findParents(paramString, null);
    if (localList == null)
      localList = Collections.emptyList();
    return localList;
  }

  public abstract List<JsonNode> findParents(String paramString, List<JsonNode> paramList);

  public abstract JsonNode findPath(String paramString);

  public abstract JsonNode findValue(String paramString);

  public final List<JsonNode> findValues(String paramString)
  {
    List localList = findValues(paramString, null);
    if (localList == null)
      localList = Collections.emptyList();
    return localList;
  }

  public abstract List<JsonNode> findValues(String paramString, List<JsonNode> paramList);

  public final List<String> findValuesAsText(String paramString)
  {
    List localList = findValuesAsText(paramString, null);
    if (localList == null)
      localList = Collections.emptyList();
    return localList;
  }

  public abstract List<String> findValuesAsText(String paramString, List<String> paramList);

  public JsonNode get(int paramInt)
  {
    return null;
  }

  public JsonNode get(String paramString)
  {
    return null;
  }

  public BigInteger getBigIntegerValue()
  {
    return BigInteger.ZERO;
  }

  public byte[] getBinaryValue()
    throws IOException
  {
    return null;
  }

  public boolean getBooleanValue()
  {
    return false;
  }

  public BigDecimal getDecimalValue()
  {
    return BigDecimal.ZERO;
  }

  public double getDoubleValue()
  {
    return 0.0D;
  }

  @Deprecated
  public final JsonNode getElementValue(int paramInt)
  {
    return get(paramInt);
  }

  public Iterator<JsonNode> getElements()
  {
    return NO_NODES.iterator();
  }

  public Iterator<String> getFieldNames()
  {
    return NO_STRINGS.iterator();
  }

  @Deprecated
  public final JsonNode getFieldValue(String paramString)
  {
    return get(paramString);
  }

  public Iterator<Map.Entry<String, JsonNode>> getFields()
  {
    return Collections.emptyList().iterator();
  }

  public int getIntValue()
  {
    return 0;
  }

  public long getLongValue()
  {
    return 0L;
  }

  public abstract JsonParser.NumberType getNumberType();

  public Number getNumberValue()
  {
    return null;
  }

  @Deprecated
  public final JsonNode getPath(int paramInt)
  {
    return path(paramInt);
  }

  @Deprecated
  public final JsonNode getPath(String paramString)
  {
    return path(paramString);
  }

  public String getTextValue()
  {
    return null;
  }

  public boolean getValueAsBoolean()
  {
    return getValueAsBoolean(false);
  }

  public boolean getValueAsBoolean(boolean paramBoolean)
  {
    return paramBoolean;
  }

  public double getValueAsDouble()
  {
    return getValueAsDouble(0.0D);
  }

  public double getValueAsDouble(double paramDouble)
  {
    return paramDouble;
  }

  public int getValueAsInt()
  {
    return getValueAsInt(0);
  }

  public int getValueAsInt(int paramInt)
  {
    return paramInt;
  }

  public long getValueAsLong()
  {
    return getValueAsInt(0);
  }

  public long getValueAsLong(long paramLong)
  {
    return paramLong;
  }

  public abstract String getValueAsText();

  public boolean has(int paramInt)
  {
    return get(paramInt) != null;
  }

  public boolean has(String paramString)
  {
    return get(paramString) != null;
  }

  public boolean isArray()
  {
    return false;
  }

  public boolean isBigDecimal()
  {
    return false;
  }

  public boolean isBigInteger()
  {
    return false;
  }

  public boolean isBinary()
  {
    return false;
  }

  public boolean isBoolean()
  {
    return false;
  }

  public boolean isContainerNode()
  {
    return false;
  }

  public boolean isDouble()
  {
    return false;
  }

  public boolean isFloatingPointNumber()
  {
    return false;
  }

  public boolean isInt()
  {
    return false;
  }

  public boolean isIntegralNumber()
  {
    return false;
  }

  public boolean isLong()
  {
    return false;
  }

  public boolean isMissingNode()
  {
    return false;
  }

  public boolean isNull()
  {
    return false;
  }

  public boolean isNumber()
  {
    return false;
  }

  public boolean isObject()
  {
    return false;
  }

  public boolean isPojo()
  {
    return false;
  }

  public boolean isTextual()
  {
    return false;
  }

  public boolean isValueNode()
  {
    return false;
  }

  public final Iterator<JsonNode> iterator()
  {
    return getElements();
  }

  public abstract JsonNode path(int paramInt);

  public abstract JsonNode path(String paramString);

  public int size()
  {
    return 0;
  }

  public abstract String toString();

  public abstract JsonParser traverse();

  public JsonNode with(String paramString)
  {
    throw new UnsupportedOperationException("JsonNode not of type ObjectNode (but " + getClass().getName() + "), can not call with() on it");
  }

  @Deprecated
  public abstract void writeTo(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonNode
 * JD-Core Version:    0.6.0
 */