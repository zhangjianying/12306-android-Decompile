package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.impl.JsonParserMinimalBase;

public class TreeTraversingParser extends JsonParserMinimalBase
{
  protected boolean _closed;
  protected JsonToken _nextToken;
  protected NodeCursor _nodeCursor;
  protected ObjectCodec _objectCodec;
  protected boolean _startContainer;

  public TreeTraversingParser(JsonNode paramJsonNode)
  {
    this(paramJsonNode, null);
  }

  public TreeTraversingParser(JsonNode paramJsonNode, ObjectCodec paramObjectCodec)
  {
    super(0);
    this._objectCodec = paramObjectCodec;
    if (paramJsonNode.isArray())
    {
      this._nextToken = JsonToken.START_ARRAY;
      this._nodeCursor = new NodeCursor.Array(paramJsonNode, null);
      return;
    }
    if (paramJsonNode.isObject())
    {
      this._nextToken = JsonToken.START_OBJECT;
      this._nodeCursor = new NodeCursor.Object(paramJsonNode, null);
      return;
    }
    this._nodeCursor = new NodeCursor.RootValue(paramJsonNode, null);
  }

  protected void _handleEOF()
    throws JsonParseException
  {
    _throwInternal();
  }

  public void close()
    throws IOException
  {
    if (!this._closed)
    {
      this._closed = true;
      this._nodeCursor = null;
      this._currToken = null;
    }
  }

  protected JsonNode currentNode()
  {
    if ((this._closed) || (this._nodeCursor == null))
      return null;
    return this._nodeCursor.currentNode();
  }

  protected JsonNode currentNumericNode()
    throws JsonParseException
  {
    JsonNode localJsonNode = currentNode();
    if ((localJsonNode == null) || (!localJsonNode.isNumber()))
    {
      if (localJsonNode == null);
      for (Object localObject = null; ; localObject = localJsonNode.asToken())
        throw _constructError("Current token (" + localObject + ") not numeric, can not use numeric value accessors");
    }
    return (JsonNode)localJsonNode;
  }

  public BigInteger getBigIntegerValue()
    throws IOException, JsonParseException
  {
    return currentNumericNode().getBigIntegerValue();
  }

  public byte[] getBinaryValue(Base64Variant paramBase64Variant)
    throws IOException, JsonParseException
  {
    JsonNode localJsonNode = currentNode();
    if (localJsonNode != null)
    {
      byte[] arrayOfByte = localJsonNode.getBinaryValue();
      if (arrayOfByte != null)
        return arrayOfByte;
      if (localJsonNode.isPojo())
      {
        Object localObject = ((POJONode)localJsonNode).getPojo();
        if ((localObject instanceof byte[]))
          return (byte[])(byte[])localObject;
      }
    }
    return null;
  }

  public ObjectCodec getCodec()
  {
    return this._objectCodec;
  }

  public JsonLocation getCurrentLocation()
  {
    return JsonLocation.NA;
  }

  public String getCurrentName()
  {
    if (this._nodeCursor == null)
      return null;
    return this._nodeCursor.getCurrentName();
  }

  public BigDecimal getDecimalValue()
    throws IOException, JsonParseException
  {
    return currentNumericNode().getDecimalValue();
  }

  public double getDoubleValue()
    throws IOException, JsonParseException
  {
    return currentNumericNode().getDoubleValue();
  }

  public Object getEmbeddedObject()
  {
    if (!this._closed)
    {
      JsonNode localJsonNode = currentNode();
      if ((localJsonNode != null) && (localJsonNode.isPojo()))
        return ((POJONode)localJsonNode).getPojo();
    }
    return null;
  }

  public float getFloatValue()
    throws IOException, JsonParseException
  {
    return (float)currentNumericNode().getDoubleValue();
  }

  public int getIntValue()
    throws IOException, JsonParseException
  {
    return currentNumericNode().getIntValue();
  }

  public long getLongValue()
    throws IOException, JsonParseException
  {
    return currentNumericNode().getLongValue();
  }

  public JsonParser.NumberType getNumberType()
    throws IOException, JsonParseException
  {
    JsonNode localJsonNode = currentNumericNode();
    if (localJsonNode == null)
      return null;
    return localJsonNode.getNumberType();
  }

  public Number getNumberValue()
    throws IOException, JsonParseException
  {
    return currentNumericNode().getNumberValue();
  }

  public JsonStreamContext getParsingContext()
  {
    return this._nodeCursor;
  }

  public String getText()
  {
    if (this._closed);
    JsonNode localJsonNode;
    do
    {
      while (this._currToken == null)
      {
        return null;
        switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
        {
        default:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        }
      }
      return this._currToken.asString();
      return this._nodeCursor.getCurrentName();
      return currentNode().getTextValue();
      return String.valueOf(currentNode().getNumberValue());
      localJsonNode = currentNode();
    }
    while ((localJsonNode == null) || (!localJsonNode.isBinary()));
    return localJsonNode.getValueAsText();
  }

  public char[] getTextCharacters()
    throws IOException, JsonParseException
  {
    return getText().toCharArray();
  }

  public int getTextLength()
    throws IOException, JsonParseException
  {
    return getText().length();
  }

  public int getTextOffset()
    throws IOException, JsonParseException
  {
    return 0;
  }

  public JsonLocation getTokenLocation()
  {
    return JsonLocation.NA;
  }

  public boolean hasTextCharacters()
  {
    return false;
  }

  public boolean isClosed()
  {
    return this._closed;
  }

  public JsonToken nextToken()
    throws IOException, JsonParseException
  {
    if (this._nextToken != null)
    {
      this._currToken = this._nextToken;
      this._nextToken = null;
      return this._currToken;
    }
    if (this._startContainer)
    {
      this._startContainer = false;
      if (!this._nodeCursor.currentHasChildren())
      {
        if (this._currToken == JsonToken.START_OBJECT);
        for (JsonToken localJsonToken = JsonToken.END_OBJECT; ; localJsonToken = JsonToken.END_ARRAY)
        {
          this._currToken = localJsonToken;
          return this._currToken;
        }
      }
      this._nodeCursor = this._nodeCursor.iterateChildren();
      this._currToken = this._nodeCursor.nextToken();
      if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY))
        this._startContainer = true;
      return this._currToken;
    }
    if (this._nodeCursor == null)
    {
      this._closed = true;
      return null;
    }
    this._currToken = this._nodeCursor.nextToken();
    if (this._currToken != null)
    {
      if ((this._currToken == JsonToken.START_OBJECT) || (this._currToken == JsonToken.START_ARRAY))
        this._startContainer = true;
      return this._currToken;
    }
    this._currToken = this._nodeCursor.endToken();
    this._nodeCursor = this._nodeCursor.getParent();
    return this._currToken;
  }

  public void setCodec(ObjectCodec paramObjectCodec)
  {
    this._objectCodec = paramObjectCodec;
  }

  public JsonParser skipChildren()
    throws IOException, JsonParseException
  {
    if (this._currToken == JsonToken.START_OBJECT)
    {
      this._startContainer = false;
      this._currToken = JsonToken.END_OBJECT;
    }
    do
      return this;
    while (this._currToken != JsonToken.START_ARRAY);
    this._startContainer = false;
    this._currToken = JsonToken.END_ARRAY;
    return this;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.node.TreeTraversingParser
 * JD-Core Version:    0.6.0
 */