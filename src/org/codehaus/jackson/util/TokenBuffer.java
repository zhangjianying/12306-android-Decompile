package org.codehaus.jackson.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.impl.JsonParserMinimalBase;
import org.codehaus.jackson.impl.JsonReadContext;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.SerializedString;

public class TokenBuffer extends JsonGenerator
{
  protected static final int DEFAULT_PARSER_FEATURES = JsonParser.Feature.collectDefaults();
  protected int _appendOffset;
  protected boolean _closed;
  protected Segment _first;
  protected int _generatorFeatures;
  protected Segment _last;
  protected ObjectCodec _objectCodec;
  protected JsonWriteContext _writeContext;

  public TokenBuffer(ObjectCodec paramObjectCodec)
  {
    this._objectCodec = paramObjectCodec;
    this._generatorFeatures = DEFAULT_PARSER_FEATURES;
    this._writeContext = JsonWriteContext.createRootContext();
    Segment localSegment = new Segment();
    this._last = localSegment;
    this._first = localSegment;
    this._appendOffset = 0;
  }

  protected final void _append(JsonToken paramJsonToken)
  {
    Segment localSegment = this._last.append(this._appendOffset, paramJsonToken);
    if (localSegment == null)
    {
      this._appendOffset = (1 + this._appendOffset);
      return;
    }
    this._last = localSegment;
    this._appendOffset = 1;
  }

  protected final void _append(JsonToken paramJsonToken, Object paramObject)
  {
    Segment localSegment = this._last.append(this._appendOffset, paramJsonToken, paramObject);
    if (localSegment == null)
    {
      this._appendOffset = (1 + this._appendOffset);
      return;
    }
    this._last = localSegment;
    this._appendOffset = 1;
  }

  protected void _reportUnsupportedOperation()
  {
    throw new UnsupportedOperationException("Called operation not supported for TokenBuffer");
  }

  public JsonParser asParser()
  {
    return asParser(this._objectCodec);
  }

  public JsonParser asParser(JsonParser paramJsonParser)
  {
    Parser localParser = new Parser(this._first, paramJsonParser.getCodec());
    localParser.setLocation(paramJsonParser.getTokenLocation());
    return localParser;
  }

  public JsonParser asParser(ObjectCodec paramObjectCodec)
  {
    return new Parser(this._first, paramObjectCodec);
  }

  public void close()
    throws IOException
  {
    this._closed = true;
  }

  public void copyCurrentEvent(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[paramJsonParser.getCurrentToken().ordinal()])
    {
    default:
      throw new RuntimeException("Internal error: should never end up through this code path");
    case 1:
      writeStartObject();
      return;
    case 2:
      writeEndObject();
      return;
    case 3:
      writeStartArray();
      return;
    case 4:
      writeEndArray();
      return;
    case 5:
      writeFieldName(paramJsonParser.getCurrentName());
      return;
    case 6:
      if (paramJsonParser.hasTextCharacters())
      {
        writeString(paramJsonParser.getTextCharacters(), paramJsonParser.getTextOffset(), paramJsonParser.getTextLength());
        return;
      }
      writeString(paramJsonParser.getText());
      return;
    case 7:
      switch (1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[paramJsonParser.getNumberType().ordinal()])
      {
      default:
        writeNumber(paramJsonParser.getLongValue());
        return;
      case 1:
        writeNumber(paramJsonParser.getIntValue());
        return;
      case 2:
      }
      writeNumber(paramJsonParser.getBigIntegerValue());
      return;
    case 8:
      switch (1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[paramJsonParser.getNumberType().ordinal()])
      {
      default:
        writeNumber(paramJsonParser.getDoubleValue());
        return;
      case 3:
        writeNumber(paramJsonParser.getDecimalValue());
        return;
      case 4:
      }
      writeNumber(paramJsonParser.getFloatValue());
      return;
    case 9:
      writeBoolean(true);
      return;
    case 10:
      writeBoolean(false);
      return;
    case 11:
      writeNull();
      return;
    case 12:
    }
    writeObject(paramJsonParser.getEmbeddedObject());
  }

  public void copyCurrentStructure(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.FIELD_NAME)
    {
      writeFieldName(paramJsonParser.getCurrentName());
      localJsonToken = paramJsonParser.nextToken();
    }
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[localJsonToken.ordinal()])
    {
    case 2:
    default:
      copyCurrentEvent(paramJsonParser);
      return;
    case 3:
      writeStartArray();
      while (paramJsonParser.nextToken() != JsonToken.END_ARRAY)
        copyCurrentStructure(paramJsonParser);
      writeEndArray();
      return;
    case 1:
    }
    writeStartObject();
    while (paramJsonParser.nextToken() != JsonToken.END_OBJECT)
      copyCurrentStructure(paramJsonParser);
    writeEndObject();
  }

  public JsonGenerator disable(JsonGenerator.Feature paramFeature)
  {
    this._generatorFeatures &= (0xFFFFFFFF ^ paramFeature.getMask());
    return this;
  }

  public JsonGenerator enable(JsonGenerator.Feature paramFeature)
  {
    this._generatorFeatures |= paramFeature.getMask();
    return this;
  }

  public void flush()
    throws IOException
  {
  }

  public ObjectCodec getCodec()
  {
    return this._objectCodec;
  }

  public final JsonWriteContext getOutputContext()
  {
    return this._writeContext;
  }

  public boolean isClosed()
  {
    return this._closed;
  }

  public boolean isEnabled(JsonGenerator.Feature paramFeature)
  {
    return (this._generatorFeatures & paramFeature.getMask()) != 0;
  }

  public void serialize(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException
  {
    Segment localSegment = this._first;
    int i = -1;
    while (true)
    {
      i++;
      if (i >= 16)
      {
        localSegment = localSegment.next();
        i = 0;
        if (localSegment != null);
      }
      JsonToken localJsonToken;
      do
      {
        return;
        localJsonToken = localSegment.type(i);
      }
      while (localJsonToken == null);
      switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[localJsonToken.ordinal()])
      {
      default:
        throw new RuntimeException("Internal error: should never end up through this code path");
      case 1:
        paramJsonGenerator.writeStartObject();
        break;
      case 2:
        paramJsonGenerator.writeEndObject();
        break;
      case 3:
        paramJsonGenerator.writeStartArray();
        break;
      case 4:
        paramJsonGenerator.writeEndArray();
        break;
      case 5:
        Object localObject3 = localSegment.get(i);
        if ((localObject3 instanceof SerializableString))
        {
          paramJsonGenerator.writeFieldName((SerializableString)localObject3);
          continue;
        }
        paramJsonGenerator.writeFieldName((String)localObject3);
        break;
      case 6:
        Object localObject2 = localSegment.get(i);
        if ((localObject2 instanceof SerializableString))
        {
          paramJsonGenerator.writeString((SerializableString)localObject2);
          continue;
        }
        paramJsonGenerator.writeString((String)localObject2);
        break;
      case 7:
        Number localNumber = (Number)localSegment.get(i);
        if ((localNumber instanceof BigInteger))
        {
          paramJsonGenerator.writeNumber((BigInteger)localNumber);
          continue;
        }
        if ((localNumber instanceof Long))
        {
          paramJsonGenerator.writeNumber(localNumber.longValue());
          continue;
        }
        paramJsonGenerator.writeNumber(localNumber.intValue());
        break;
      case 8:
        Object localObject1 = localSegment.get(i);
        if ((localObject1 instanceof BigDecimal))
        {
          paramJsonGenerator.writeNumber((BigDecimal)localObject1);
          continue;
        }
        if ((localObject1 instanceof Float))
        {
          paramJsonGenerator.writeNumber(((Float)localObject1).floatValue());
          continue;
        }
        if ((localObject1 instanceof Double))
        {
          paramJsonGenerator.writeNumber(((Double)localObject1).doubleValue());
          continue;
        }
        if (localObject1 == null)
        {
          paramJsonGenerator.writeNull();
          continue;
        }
        if ((localObject1 instanceof String))
        {
          paramJsonGenerator.writeNumber((String)localObject1);
          continue;
        }
        throw new JsonGenerationException("Unrecognized value type for VALUE_NUMBER_FLOAT: " + localObject1.getClass().getName() + ", can not serialize");
      case 9:
        paramJsonGenerator.writeBoolean(true);
        break;
      case 10:
        paramJsonGenerator.writeBoolean(false);
        break;
      case 11:
        paramJsonGenerator.writeNull();
        break;
      case 12:
        paramJsonGenerator.writeObject(localSegment.get(i));
      }
    }
  }

  public JsonGenerator setCodec(ObjectCodec paramObjectCodec)
  {
    this._objectCodec = paramObjectCodec;
    return this;
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[TokenBuffer: ");
    JsonParser localJsonParser = asParser();
    for (int i = 0; ; i++)
    {
      JsonToken localJsonToken;
      try
      {
        localJsonToken = localJsonParser.nextToken();
        if (localJsonToken == null)
        {
          if (i >= 100)
            localStringBuilder.append(" ... (truncated ").append(i - 100).append(" entries)");
          localStringBuilder.append(']');
          return localStringBuilder.toString();
        }
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException(localIOException);
      }
      if (i >= 100)
        continue;
      if (i > 0)
        localStringBuilder.append(", ");
      localStringBuilder.append(localJsonToken.toString());
    }
  }

  public JsonGenerator useDefaultPrettyPrinter()
  {
    return this;
  }

  public void writeBinary(Base64Variant paramBase64Variant, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    writeObject(arrayOfByte);
  }

  public void writeBoolean(boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (paramBoolean);
    for (JsonToken localJsonToken = JsonToken.VALUE_TRUE; ; localJsonToken = JsonToken.VALUE_FALSE)
    {
      _append(localJsonToken);
      return;
    }
  }

  public final void writeEndArray()
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.END_ARRAY);
    JsonWriteContext localJsonWriteContext = this._writeContext.getParent();
    if (localJsonWriteContext != null)
      this._writeContext = localJsonWriteContext;
  }

  public final void writeEndObject()
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.END_OBJECT);
    JsonWriteContext localJsonWriteContext = this._writeContext.getParent();
    if (localJsonWriteContext != null)
      this._writeContext = localJsonWriteContext;
  }

  public final void writeFieldName(String paramString)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.FIELD_NAME, paramString);
    this._writeContext.writeFieldName(paramString);
  }

  public void writeFieldName(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.FIELD_NAME, paramSerializableString);
    this._writeContext.writeFieldName(paramSerializableString.getValue());
  }

  public void writeFieldName(SerializedString paramSerializedString)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.FIELD_NAME, paramSerializedString);
    this._writeContext.writeFieldName(paramSerializedString.getValue());
  }

  public void writeNull()
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.VALUE_NULL);
  }

  public void writeNumber(double paramDouble)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(paramDouble));
  }

  public void writeNumber(float paramFloat)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(paramFloat));
  }

  public void writeNumber(int paramInt)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(paramInt));
  }

  public void writeNumber(long paramLong)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.VALUE_NUMBER_INT, Long.valueOf(paramLong));
  }

  public void writeNumber(String paramString)
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.VALUE_NUMBER_FLOAT, paramString);
  }

  public void writeNumber(BigDecimal paramBigDecimal)
    throws IOException, JsonGenerationException
  {
    if (paramBigDecimal == null)
    {
      writeNull();
      return;
    }
    _append(JsonToken.VALUE_NUMBER_FLOAT, paramBigDecimal);
  }

  public void writeNumber(BigInteger paramBigInteger)
    throws IOException, JsonGenerationException
  {
    if (paramBigInteger == null)
    {
      writeNull();
      return;
    }
    _append(JsonToken.VALUE_NUMBER_INT, paramBigInteger);
  }

  public void writeObject(Object paramObject)
    throws IOException, JsonProcessingException
  {
    _append(JsonToken.VALUE_EMBEDDED_OBJECT, paramObject);
  }

  public void writeRaw(char paramChar)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public void writeRaw(String paramString)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public void writeRaw(String paramString, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public void writeRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public void writeRawUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public void writeRawValue(String paramString)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public void writeRawValue(String paramString, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public void writeRawValue(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  public final void writeStartArray()
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.START_ARRAY);
    this._writeContext = this._writeContext.createChildArrayContext();
  }

  public final void writeStartObject()
    throws IOException, JsonGenerationException
  {
    _append(JsonToken.START_OBJECT);
    this._writeContext = this._writeContext.createChildObjectContext();
  }

  public void writeString(String paramString)
    throws IOException, JsonGenerationException
  {
    if (paramString == null)
    {
      writeNull();
      return;
    }
    _append(JsonToken.VALUE_STRING, paramString);
  }

  public void writeString(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    if (paramSerializableString == null)
    {
      writeNull();
      return;
    }
    _append(JsonToken.VALUE_STRING, paramSerializableString);
  }

  public void writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    writeString(new String(paramArrayOfChar, paramInt1, paramInt2));
  }

  public void writeTree(JsonNode paramJsonNode)
    throws IOException, JsonProcessingException
  {
    _append(JsonToken.VALUE_EMBEDDED_OBJECT, paramJsonNode);
  }

  public void writeUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    _reportUnsupportedOperation();
  }

  protected static final class Parser extends JsonParserMinimalBase
  {
    protected transient ByteArrayBuilder _byteBuilder;
    protected boolean _closed;
    protected ObjectCodec _codec;
    protected JsonLocation _location = null;
    protected JsonReadContext _parsingContext;
    protected TokenBuffer.Segment _segment;
    protected int _segmentPtr;

    public Parser(TokenBuffer.Segment paramSegment, ObjectCodec paramObjectCodec)
    {
      super();
      this._segment = paramSegment;
      this._segmentPtr = -1;
      this._codec = paramObjectCodec;
      this._parsingContext = JsonReadContext.createRootContext(-1, -1);
    }

    protected final void _checkIsNumber()
      throws JsonParseException
    {
      if ((this._currToken == null) || (!this._currToken.isNumeric()))
        throw _constructError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
    }

    protected final Object _currentObject()
    {
      return this._segment.get(this._segmentPtr);
    }

    protected void _decodeBase64(String paramString, ByteArrayBuilder paramByteArrayBuilder, Base64Variant paramBase64Variant)
      throws IOException, JsonParseException
    {
      int i = 0;
      int j = paramString.length();
      if (i < j);
      while (true)
      {
        int k = i + 1;
        char c1 = paramString.charAt(i);
        if (k >= j)
          return;
        if (c1 > ' ')
        {
          int m = paramBase64Variant.decodeBase64Char(c1);
          if (m < 0)
            _reportInvalidBase64(paramBase64Variant, c1, 0, null);
          if (k >= j)
            _reportBase64EOF();
          int n = k + 1;
          char c2 = paramString.charAt(k);
          int i1 = paramBase64Variant.decodeBase64Char(c2);
          if (i1 < 0)
            _reportInvalidBase64(paramBase64Variant, c2, 1, null);
          int i2 = i1 | m << 6;
          if (n >= j)
            _reportBase64EOF();
          int i3 = n + 1;
          char c3 = paramString.charAt(n);
          int i4 = paramBase64Variant.decodeBase64Char(c3);
          if (i4 < 0)
          {
            if (i4 != -2)
              _reportInvalidBase64(paramBase64Variant, c3, 2, null);
            if (i3 >= j)
              _reportBase64EOF();
            i = i3 + 1;
            char c5 = paramString.charAt(i3);
            if (!paramBase64Variant.usesPaddingChar(c5))
              _reportInvalidBase64(paramBase64Variant, c5, 3, "expected padding character '" + paramBase64Variant.getPaddingChar() + "'");
            paramByteArrayBuilder.append(i2 >> 4);
            break;
          }
          int i5 = i4 | i2 << 6;
          if (i3 >= j)
            _reportBase64EOF();
          i = i3 + 1;
          char c4 = paramString.charAt(i3);
          int i6 = paramBase64Variant.decodeBase64Char(c4);
          if (i6 < 0)
          {
            if (i6 != -2)
              _reportInvalidBase64(paramBase64Variant, c4, 3, null);
            paramByteArrayBuilder.appendTwoBytes(i5 >> 2);
            break;
          }
          paramByteArrayBuilder.appendThreeBytes(i6 | i5 << 6);
          break;
        }
        i = k;
      }
    }

    protected void _handleEOF()
      throws JsonParseException
    {
      _throwInternal();
    }

    protected void _reportBase64EOF()
      throws JsonParseException
    {
      throw _constructError("Unexpected end-of-String in base64 content");
    }

    protected void _reportInvalidBase64(Base64Variant paramBase64Variant, char paramChar, int paramInt, String paramString)
      throws JsonParseException
    {
      String str;
      if (paramChar <= ' ')
        str = "Illegal white space character (code 0x" + Integer.toHexString(paramChar) + ") as character #" + (paramInt + 1) + " of 4-char base64 unit: can only used between units";
      while (true)
      {
        if (paramString != null)
          str = str + ": " + paramString;
        throw _constructError(str);
        if (paramBase64Variant.usesPaddingChar(paramChar))
        {
          str = "Unexpected padding character ('" + paramBase64Variant.getPaddingChar() + "') as character #" + (paramInt + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
          continue;
        }
        if ((!Character.isDefined(paramChar)) || (Character.isISOControl(paramChar)))
        {
          str = "Illegal character (code 0x" + Integer.toHexString(paramChar) + ") in base64 content";
          continue;
        }
        str = "Illegal character '" + paramChar + "' (code 0x" + Integer.toHexString(paramChar) + ") in base64 content";
      }
    }

    public void close()
      throws IOException
    {
      if (!this._closed)
        this._closed = true;
    }

    public BigInteger getBigIntegerValue()
      throws IOException, JsonParseException
    {
      Number localNumber = getNumberValue();
      if ((localNumber instanceof BigInteger))
        return (BigInteger)localNumber;
      switch (TokenBuffer.1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[getNumberType().ordinal()])
      {
      default:
        return BigInteger.valueOf(localNumber.longValue());
      case 3:
      }
      return ((BigDecimal)localNumber).toBigInteger();
    }

    public byte[] getBinaryValue(Base64Variant paramBase64Variant)
      throws IOException, JsonParseException
    {
      if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT)
      {
        Object localObject = _currentObject();
        if ((localObject instanceof byte[]))
          return (byte[])(byte[])localObject;
      }
      if (this._currToken != JsonToken.VALUE_STRING)
        throw _constructError("Current token (" + this._currToken + ") not VALUE_STRING (or VALUE_EMBEDDED_OBJECT with byte[]), can not access as binary");
      String str = getText();
      if (str == null)
        return null;
      ByteArrayBuilder localByteArrayBuilder = this._byteBuilder;
      if (localByteArrayBuilder == null)
      {
        localByteArrayBuilder = new ByteArrayBuilder(100);
        this._byteBuilder = localByteArrayBuilder;
      }
      _decodeBase64(str, localByteArrayBuilder, paramBase64Variant);
      return localByteArrayBuilder.toByteArray();
    }

    public ObjectCodec getCodec()
    {
      return this._codec;
    }

    public JsonLocation getCurrentLocation()
    {
      if (this._location == null)
        return JsonLocation.NA;
      return this._location;
    }

    public String getCurrentName()
    {
      return this._parsingContext.getCurrentName();
    }

    public BigDecimal getDecimalValue()
      throws IOException, JsonParseException
    {
      Number localNumber = getNumberValue();
      if ((localNumber instanceof BigDecimal))
        return (BigDecimal)localNumber;
      switch (TokenBuffer.1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[getNumberType().ordinal()])
      {
      case 3:
      case 4:
      default:
        return BigDecimal.valueOf(localNumber.doubleValue());
      case 1:
      case 5:
        return BigDecimal.valueOf(localNumber.longValue());
      case 2:
      }
      return new BigDecimal((BigInteger)localNumber);
    }

    public double getDoubleValue()
      throws IOException, JsonParseException
    {
      return getNumberValue().doubleValue();
    }

    public Object getEmbeddedObject()
    {
      if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT)
        return _currentObject();
      return null;
    }

    public float getFloatValue()
      throws IOException, JsonParseException
    {
      return getNumberValue().floatValue();
    }

    public int getIntValue()
      throws IOException, JsonParseException
    {
      if (this._currToken == JsonToken.VALUE_NUMBER_INT)
        return ((Number)_currentObject()).intValue();
      return getNumberValue().intValue();
    }

    public long getLongValue()
      throws IOException, JsonParseException
    {
      return getNumberValue().longValue();
    }

    public JsonParser.NumberType getNumberType()
      throws IOException, JsonParseException
    {
      Number localNumber = getNumberValue();
      if ((localNumber instanceof Integer))
        return JsonParser.NumberType.INT;
      if ((localNumber instanceof Long))
        return JsonParser.NumberType.LONG;
      if ((localNumber instanceof Double))
        return JsonParser.NumberType.DOUBLE;
      if ((localNumber instanceof BigDecimal))
        return JsonParser.NumberType.BIG_DECIMAL;
      if ((localNumber instanceof Float))
        return JsonParser.NumberType.FLOAT;
      if ((localNumber instanceof BigInteger))
        return JsonParser.NumberType.BIG_INTEGER;
      return null;
    }

    public final Number getNumberValue()
      throws IOException, JsonParseException
    {
      _checkIsNumber();
      return (Number)_currentObject();
    }

    public JsonStreamContext getParsingContext()
    {
      return this._parsingContext;
    }

    public String getText()
    {
      Object localObject1;
      String str;
      if ((this._currToken == JsonToken.VALUE_STRING) || (this._currToken == JsonToken.FIELD_NAME))
      {
        localObject1 = _currentObject();
        if ((localObject1 instanceof String))
          str = (String)localObject1;
      }
      Object localObject2;
      do
      {
        JsonToken localJsonToken;
        do
        {
          do
          {
            return str;
            str = null;
          }
          while (localObject1 == null);
          return localObject1.toString();
          localJsonToken = this._currToken;
          str = null;
        }
        while (localJsonToken == null);
        switch (TokenBuffer.1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()])
        {
        default:
          return this._currToken.asString();
        case 7:
        case 8:
        }
        localObject2 = _currentObject();
        str = null;
      }
      while (localObject2 == null);
      return localObject2.toString();
    }

    public char[] getTextCharacters()
    {
      String str = getText();
      if (str == null)
        return null;
      return str.toCharArray();
    }

    public int getTextLength()
    {
      String str = getText();
      if (str == null)
        return 0;
      return str.length();
    }

    public int getTextOffset()
    {
      return 0;
    }

    public JsonLocation getTokenLocation()
    {
      return getCurrentLocation();
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
      if ((this._closed) || (this._segment == null));
      do
      {
        return null;
        int i = 1 + this._segmentPtr;
        this._segmentPtr = i;
        if (i < 16)
          break;
        this._segmentPtr = 0;
        this._segment = this._segment.next();
      }
      while (this._segment == null);
      this._currToken = this._segment.type(this._segmentPtr);
      Object localObject;
      String str;
      if (this._currToken == JsonToken.FIELD_NAME)
      {
        localObject = _currentObject();
        if ((localObject instanceof String))
        {
          str = (String)localObject;
          this._parsingContext.setCurrentName(str);
        }
      }
      while (true)
      {
        return this._currToken;
        str = localObject.toString();
        break;
        if (this._currToken == JsonToken.START_OBJECT)
        {
          this._parsingContext = this._parsingContext.createChildObjectContext(-1, -1);
          continue;
        }
        if (this._currToken == JsonToken.START_ARRAY)
        {
          this._parsingContext = this._parsingContext.createChildArrayContext(-1, -1);
          continue;
        }
        if ((this._currToken != JsonToken.END_OBJECT) && (this._currToken != JsonToken.END_ARRAY))
          continue;
        this._parsingContext = this._parsingContext.getParent();
        if (this._parsingContext != null)
          continue;
        this._parsingContext = JsonReadContext.createRootContext(-1, -1);
      }
    }

    public JsonToken peekNextToken()
      throws IOException, JsonParseException
    {
      if (this._closed);
      while (true)
      {
        return null;
        TokenBuffer.Segment localSegment = this._segment;
        int i = 1 + this._segmentPtr;
        if (i >= 16)
        {
          i = 0;
          if (localSegment != null)
            break label45;
          localSegment = null;
        }
        while (localSegment != null)
        {
          return localSegment.type(i);
          label45: localSegment = localSegment.next();
          i = 0;
        }
      }
    }

    public void setCodec(ObjectCodec paramObjectCodec)
    {
      this._codec = paramObjectCodec;
    }

    public void setLocation(JsonLocation paramJsonLocation)
    {
      this._location = paramJsonLocation;
    }
  }

  protected static final class Segment
  {
    public static final int TOKENS_PER_SEGMENT = 16;
    private static final JsonToken[] TOKEN_TYPES_BY_INDEX = new JsonToken[16];
    protected Segment _next;
    protected long _tokenTypes;
    protected final Object[] _tokens = new Object[16];

    static
    {
      JsonToken[] arrayOfJsonToken = JsonToken.values();
      System.arraycopy(arrayOfJsonToken, 1, TOKEN_TYPES_BY_INDEX, 1, Math.min(15, -1 + arrayOfJsonToken.length));
    }

    public Segment append(int paramInt, JsonToken paramJsonToken)
    {
      if (paramInt < 16)
      {
        set(paramInt, paramJsonToken);
        return null;
      }
      this._next = new Segment();
      this._next.set(0, paramJsonToken);
      return this._next;
    }

    public Segment append(int paramInt, JsonToken paramJsonToken, Object paramObject)
    {
      if (paramInt < 16)
      {
        set(paramInt, paramJsonToken, paramObject);
        return null;
      }
      this._next = new Segment();
      this._next.set(0, paramJsonToken, paramObject);
      return this._next;
    }

    public Object get(int paramInt)
    {
      return this._tokens[paramInt];
    }

    public Segment next()
    {
      return this._next;
    }

    public void set(int paramInt, JsonToken paramJsonToken)
    {
      long l = paramJsonToken.ordinal();
      if (paramInt > 0)
        l <<= paramInt << 2;
      this._tokenTypes = (l | this._tokenTypes);
    }

    public void set(int paramInt, JsonToken paramJsonToken, Object paramObject)
    {
      this._tokens[paramInt] = paramObject;
      long l = paramJsonToken.ordinal();
      if (paramInt > 0)
        l <<= paramInt << 2;
      this._tokenTypes = (l | this._tokenTypes);
    }

    public JsonToken type(int paramInt)
    {
      long l = this._tokenTypes;
      if (paramInt > 0)
        l >>= paramInt << 2;
      int i = 0xF & (int)l;
      return TOKEN_TYPES_BY_INDEX[i];
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.TokenBuffer
 * JD-Core Version:    0.6.0
 */