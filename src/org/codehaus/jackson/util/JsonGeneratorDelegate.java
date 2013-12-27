package org.codehaus.jackson.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.io.SerializedString;

public class JsonGeneratorDelegate extends JsonGenerator
{
  protected JsonGenerator delegate;

  public JsonGeneratorDelegate(JsonGenerator paramJsonGenerator)
  {
    this.delegate = paramJsonGenerator;
  }

  public boolean canUseSchema(FormatSchema paramFormatSchema)
  {
    return this.delegate.canUseSchema(paramFormatSchema);
  }

  public void close()
    throws IOException
  {
    this.delegate.close();
  }

  public void copyCurrentEvent(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    this.delegate.copyCurrentEvent(paramJsonParser);
  }

  public void copyCurrentStructure(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    this.delegate.copyCurrentStructure(paramJsonParser);
  }

  public JsonGenerator disable(JsonGenerator.Feature paramFeature)
  {
    return this.delegate.disable(paramFeature);
  }

  public JsonGenerator enable(JsonGenerator.Feature paramFeature)
  {
    return this.delegate.enable(paramFeature);
  }

  public void flush()
    throws IOException
  {
    this.delegate.flush();
  }

  public ObjectCodec getCodec()
  {
    return this.delegate.getCodec();
  }

  public JsonStreamContext getOutputContext()
  {
    return this.delegate.getOutputContext();
  }

  public Object getOutputTarget()
  {
    return this.delegate.getOutputTarget();
  }

  public boolean isClosed()
  {
    return this.delegate.isClosed();
  }

  public boolean isEnabled(JsonGenerator.Feature paramFeature)
  {
    return this.delegate.isEnabled(paramFeature);
  }

  public JsonGenerator setCodec(ObjectCodec paramObjectCodec)
  {
    this.delegate.setCodec(paramObjectCodec);
    return this;
  }

  public void setSchema(FormatSchema paramFormatSchema)
  {
    this.delegate.setSchema(paramFormatSchema);
  }

  public JsonGenerator useDefaultPrettyPrinter()
  {
    this.delegate.useDefaultPrettyPrinter();
    return this;
  }

  public Version version()
  {
    return this.delegate.version();
  }

  public void writeBinary(Base64Variant paramBase64Variant, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeBinary(paramBase64Variant, paramArrayOfByte, paramInt1, paramInt2);
  }

  public void writeBoolean(boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeBoolean(paramBoolean);
  }

  public void writeEndArray()
    throws IOException, JsonGenerationException
  {
    this.delegate.writeEndArray();
  }

  public void writeEndObject()
    throws IOException, JsonGenerationException
  {
    this.delegate.writeEndObject();
  }

  public void writeFieldName(String paramString)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeFieldName(paramString);
  }

  public void writeFieldName(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeFieldName(paramSerializableString);
  }

  public void writeFieldName(SerializedString paramSerializedString)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeFieldName(paramSerializedString);
  }

  public void writeNull()
    throws IOException, JsonGenerationException
  {
    this.delegate.writeNull();
  }

  public void writeNumber(double paramDouble)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeNumber(paramDouble);
  }

  public void writeNumber(float paramFloat)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeNumber(paramFloat);
  }

  public void writeNumber(int paramInt)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeNumber(paramInt);
  }

  public void writeNumber(long paramLong)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeNumber(paramLong);
  }

  public void writeNumber(String paramString)
    throws IOException, JsonGenerationException, UnsupportedOperationException
  {
    this.delegate.writeNumber(paramString);
  }

  public void writeNumber(BigDecimal paramBigDecimal)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeNumber(paramBigDecimal);
  }

  public void writeNumber(BigInteger paramBigInteger)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeNumber(paramBigInteger);
  }

  public void writeObject(Object paramObject)
    throws IOException, JsonProcessingException
  {
    this.delegate.writeObject(paramObject);
  }

  public void writeRaw(char paramChar)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRaw(paramChar);
  }

  public void writeRaw(String paramString)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRaw(paramString);
  }

  public void writeRaw(String paramString, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRaw(paramString, paramInt1, paramInt2);
  }

  public void writeRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRaw(paramArrayOfChar, paramInt1, paramInt2);
  }

  public void writeRawUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRawUTF8String(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void writeRawValue(String paramString)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRawValue(paramString);
  }

  public void writeRawValue(String paramString, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRawValue(paramString, paramInt1, paramInt2);
  }

  public void writeRawValue(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeRawValue(paramArrayOfChar, paramInt1, paramInt2);
  }

  public void writeStartArray()
    throws IOException, JsonGenerationException
  {
    this.delegate.writeStartArray();
  }

  public void writeStartObject()
    throws IOException, JsonGenerationException
  {
    this.delegate.writeStartObject();
  }

  public void writeString(String paramString)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeString(paramString);
  }

  public void writeString(SerializableString paramSerializableString)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeString(paramSerializableString);
  }

  public void writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeString(paramArrayOfChar, paramInt1, paramInt2);
  }

  public void writeTree(JsonNode paramJsonNode)
    throws IOException, JsonProcessingException
  {
    this.delegate.writeTree(paramJsonNode);
  }

  public void writeUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonGenerationException
  {
    this.delegate.writeUTF8String(paramArrayOfByte, paramInt1, paramInt2);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.JsonGeneratorDelegate
 * JD-Core Version:    0.6.0
 */