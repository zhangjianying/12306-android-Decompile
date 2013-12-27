package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.node.ObjectNode;

public final class ArraySerializers
{
  public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T>
  {
    protected final BeanProperty _property;
    protected final TypeSerializer _valueTypeSerializer;

    protected AsArraySerializer(Class<T> paramClass, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
    {
      super();
      this._valueTypeSerializer = paramTypeSerializer;
      this._property = paramBeanProperty;
    }

    public final void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeStartArray();
      serializeContents(paramT, paramJsonGenerator, paramSerializerProvider);
      paramJsonGenerator.writeEndArray();
    }

    protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException;

    public final void serializeWithType(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      paramTypeSerializer.writeTypePrefixForArray(paramT, paramJsonGenerator);
      serializeContents(paramT, paramJsonGenerator, paramSerializerProvider);
      paramTypeSerializer.writeTypeSuffixForArray(paramT, paramJsonGenerator);
    }
  }

  @JacksonStdImpl
  public static final class BooleanArraySerializer extends ArraySerializers.AsArraySerializer<boolean[]>
  {
    public BooleanArraySerializer()
    {
      super(null, null);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return this;
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("boolean"));
      return localObjectNode;
    }

    public void serializeContents(boolean[] paramArrayOfBoolean, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfBoolean.length;
      while (i < j)
      {
        paramJsonGenerator.writeBoolean(paramArrayOfBoolean[i]);
        i++;
      }
    }
  }

  @JacksonStdImpl
  public static final class ByteArraySerializer extends SerializerBase<byte[]>
  {
    public ByteArraySerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("string"));
      return localObjectNode;
    }

    public void serialize(byte[] paramArrayOfByte, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeBinary(paramArrayOfByte);
    }

    public void serializeWithType(byte[] paramArrayOfByte, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      paramTypeSerializer.writeTypePrefixForScalar(paramArrayOfByte, paramJsonGenerator);
      paramJsonGenerator.writeBinary(paramArrayOfByte);
      paramTypeSerializer.writeTypeSuffixForScalar(paramArrayOfByte, paramJsonGenerator);
    }
  }

  @JacksonStdImpl
  public static final class CharArraySerializer extends SerializerBase<char[]>
  {
    public CharArraySerializer()
    {
      super();
    }

    private final void _writeArrayContents(JsonGenerator paramJsonGenerator, char[] paramArrayOfChar)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfChar.length;
      while (i < j)
      {
        paramJsonGenerator.writeString(paramArrayOfChar, i, 1);
        i++;
      }
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode1 = createSchemaNode("array", true);
      ObjectNode localObjectNode2 = createSchemaNode("string");
      localObjectNode2.put("type", "string");
      localObjectNode1.put("items", localObjectNode2);
      return localObjectNode1;
    }

    public void serialize(char[] paramArrayOfChar, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      if (paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS))
      {
        paramJsonGenerator.writeStartArray();
        _writeArrayContents(paramJsonGenerator, paramArrayOfChar);
        paramJsonGenerator.writeEndArray();
        return;
      }
      paramJsonGenerator.writeString(paramArrayOfChar, 0, paramArrayOfChar.length);
    }

    public void serializeWithType(char[] paramArrayOfChar, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      if (paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS))
      {
        paramTypeSerializer.writeTypePrefixForArray(paramArrayOfChar, paramJsonGenerator);
        _writeArrayContents(paramJsonGenerator, paramArrayOfChar);
        paramTypeSerializer.writeTypeSuffixForArray(paramArrayOfChar, paramJsonGenerator);
        return;
      }
      paramTypeSerializer.writeTypePrefixForScalar(paramArrayOfChar, paramJsonGenerator);
      paramJsonGenerator.writeString(paramArrayOfChar, 0, paramArrayOfChar.length);
      paramTypeSerializer.writeTypeSuffixForScalar(paramArrayOfChar, paramJsonGenerator);
    }
  }

  @JacksonStdImpl
  public static final class DoubleArraySerializer extends ArraySerializers.AsArraySerializer<double[]>
  {
    public DoubleArraySerializer()
    {
      super(null, null);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return this;
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("number"));
      return localObjectNode;
    }

    public void serializeContents(double[] paramArrayOfDouble, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfDouble.length;
      while (i < j)
      {
        paramJsonGenerator.writeNumber(paramArrayOfDouble[i]);
        i++;
      }
    }
  }

  @JacksonStdImpl
  public static final class FloatArraySerializer extends ArraySerializers.AsArraySerializer<float[]>
  {
    public FloatArraySerializer()
    {
      this(null);
    }

    public FloatArraySerializer(TypeSerializer paramTypeSerializer)
    {
      super(paramTypeSerializer, null);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return new FloatArraySerializer(paramTypeSerializer);
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("number"));
      return localObjectNode;
    }

    public void serializeContents(float[] paramArrayOfFloat, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfFloat.length;
      while (i < j)
      {
        paramJsonGenerator.writeNumber(paramArrayOfFloat[i]);
        i++;
      }
    }
  }

  @JacksonStdImpl
  public static final class IntArraySerializer extends ArraySerializers.AsArraySerializer<int[]>
  {
    public IntArraySerializer()
    {
      super(null, null);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return this;
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("integer"));
      return localObjectNode;
    }

    public void serializeContents(int[] paramArrayOfInt, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfInt.length;
      while (i < j)
      {
        paramJsonGenerator.writeNumber(paramArrayOfInt[i]);
        i++;
      }
    }
  }

  @JacksonStdImpl
  public static final class LongArraySerializer extends ArraySerializers.AsArraySerializer<long[]>
  {
    public LongArraySerializer()
    {
      this(null);
    }

    public LongArraySerializer(TypeSerializer paramTypeSerializer)
    {
      super(paramTypeSerializer, null);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return new LongArraySerializer(paramTypeSerializer);
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("number", true));
      return localObjectNode;
    }

    public void serializeContents(long[] paramArrayOfLong, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfLong.length;
      while (i < j)
      {
        paramJsonGenerator.writeNumber(paramArrayOfLong[i]);
        i++;
      }
    }
  }

  @JacksonStdImpl
  public static final class ShortArraySerializer extends ArraySerializers.AsArraySerializer<short[]>
  {
    public ShortArraySerializer()
    {
      this(null);
    }

    public ShortArraySerializer(TypeSerializer paramTypeSerializer)
    {
      super(paramTypeSerializer, null);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return new ShortArraySerializer(paramTypeSerializer);
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("integer"));
      return localObjectNode;
    }

    public void serializeContents(short[] paramArrayOfShort, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfShort.length;
      while (i < j)
      {
        paramJsonGenerator.writeNumber(paramArrayOfShort[i]);
        i++;
      }
    }
  }

  @JacksonStdImpl
  public static final class StringArraySerializer extends ArraySerializers.AsArraySerializer<String[]>
    implements ResolvableSerializer
  {
    protected JsonSerializer<Object> _elementSerializer;

    public StringArraySerializer(BeanProperty paramBeanProperty)
    {
      super(null, paramBeanProperty);
    }

    private void serializeContentsSlow(String[] paramArrayOfString, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, JsonSerializer<Object> paramJsonSerializer)
      throws IOException, JsonGenerationException
    {
      int i = 0;
      int j = paramArrayOfString.length;
      if (i < j)
      {
        if (paramArrayOfString[i] == null)
          paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
        while (true)
        {
          i++;
          break;
          paramJsonSerializer.serialize(paramArrayOfString[i], paramJsonGenerator, paramSerializerProvider);
        }
      }
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return this;
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      localObjectNode.put("items", createSchemaNode("string"));
      return localObjectNode;
    }

    public void resolve(SerializerProvider paramSerializerProvider)
      throws JsonMappingException
    {
      JsonSerializer localJsonSerializer = paramSerializerProvider.findValueSerializer(String.class, this._property);
      if ((localJsonSerializer != null) && (localJsonSerializer.getClass().getAnnotation(JacksonStdImpl.class) == null))
        this._elementSerializer = localJsonSerializer;
    }

    public void serializeContents(String[] paramArrayOfString, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = paramArrayOfString.length;
      if (i == 0)
        return;
      if (this._elementSerializer != null)
      {
        serializeContentsSlow(paramArrayOfString, paramJsonGenerator, paramSerializerProvider, this._elementSerializer);
        return;
      }
      int j = 0;
      label32: if (j < i)
      {
        if (paramArrayOfString[j] != null)
          break label56;
        paramJsonGenerator.writeNull();
      }
      while (true)
      {
        j++;
        break label32;
        break;
        label56: paramJsonGenerator.writeString(paramArrayOfString[j]);
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ArraySerializers
 * JD-Core Version:    0.6.0
 */