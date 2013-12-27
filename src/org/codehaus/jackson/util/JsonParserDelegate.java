package org.codehaus.jackson.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;

public class JsonParserDelegate extends JsonParser
{
  protected JsonParser delegate;

  public JsonParserDelegate(JsonParser paramJsonParser)
  {
    this.delegate = paramJsonParser;
  }

  public boolean canUseSchema(FormatSchema paramFormatSchema)
  {
    return this.delegate.canUseSchema(paramFormatSchema);
  }

  public void clearCurrentToken()
  {
    this.delegate.clearCurrentToken();
  }

  public void close()
    throws IOException
  {
    this.delegate.close();
  }

  public JsonParser disable(JsonParser.Feature paramFeature)
  {
    this.delegate.disable(paramFeature);
    return this;
  }

  public JsonParser enable(JsonParser.Feature paramFeature)
  {
    this.delegate.enable(paramFeature);
    return this;
  }

  public BigInteger getBigIntegerValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getBigIntegerValue();
  }

  public byte[] getBinaryValue(Base64Variant paramBase64Variant)
    throws IOException, JsonParseException
  {
    return this.delegate.getBinaryValue(paramBase64Variant);
  }

  public byte getByteValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getByteValue();
  }

  public ObjectCodec getCodec()
  {
    return this.delegate.getCodec();
  }

  public JsonLocation getCurrentLocation()
  {
    return this.delegate.getCurrentLocation();
  }

  public String getCurrentName()
    throws IOException, JsonParseException
  {
    return this.delegate.getCurrentName();
  }

  public JsonToken getCurrentToken()
  {
    return this.delegate.getCurrentToken();
  }

  public BigDecimal getDecimalValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getDecimalValue();
  }

  public double getDoubleValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getDoubleValue();
  }

  public float getFloatValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getFloatValue();
  }

  public Object getInputSource()
  {
    return this.delegate.getInputSource();
  }

  public int getIntValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getIntValue();
  }

  public JsonToken getLastClearedToken()
  {
    return this.delegate.getLastClearedToken();
  }

  public long getLongValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getLongValue();
  }

  public JsonParser.NumberType getNumberType()
    throws IOException, JsonParseException
  {
    return this.delegate.getNumberType();
  }

  public Number getNumberValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getNumberValue();
  }

  public JsonStreamContext getParsingContext()
  {
    return this.delegate.getParsingContext();
  }

  public short getShortValue()
    throws IOException, JsonParseException
  {
    return this.delegate.getShortValue();
  }

  public String getText()
    throws IOException, JsonParseException
  {
    return this.delegate.getText();
  }

  public char[] getTextCharacters()
    throws IOException, JsonParseException
  {
    return this.delegate.getTextCharacters();
  }

  public int getTextLength()
    throws IOException, JsonParseException
  {
    return this.delegate.getTextLength();
  }

  public int getTextOffset()
    throws IOException, JsonParseException
  {
    return this.delegate.getTextOffset();
  }

  public JsonLocation getTokenLocation()
  {
    return this.delegate.getTokenLocation();
  }

  public boolean hasCurrentToken()
  {
    return this.delegate.hasCurrentToken();
  }

  public boolean isClosed()
  {
    return this.delegate.isClosed();
  }

  public boolean isEnabled(JsonParser.Feature paramFeature)
  {
    return this.delegate.isEnabled(paramFeature);
  }

  public JsonToken nextToken()
    throws IOException, JsonParseException
  {
    return this.delegate.nextToken();
  }

  public void setCodec(ObjectCodec paramObjectCodec)
  {
    this.delegate.setCodec(paramObjectCodec);
  }

  public void setSchema(FormatSchema paramFormatSchema)
  {
    this.delegate.setSchema(paramFormatSchema);
  }

  public JsonParser skipChildren()
    throws IOException, JsonParseException
  {
    this.delegate.skipChildren();
    return this;
  }

  public Version version()
  {
    return this.delegate.version();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.JsonParserDelegate
 * JD-Core Version:    0.6.0
 */