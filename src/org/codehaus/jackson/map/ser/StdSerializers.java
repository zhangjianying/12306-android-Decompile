package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Calendar;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.util.TokenBuffer;

public class StdSerializers
{
  @JacksonStdImpl
  public static final class BooleanSerializer extends StdSerializers.NonTypedScalarSerializer<Boolean>
  {
    final boolean _forPrimitive;

    public BooleanSerializer(boolean paramBoolean)
    {
      super();
      this._forPrimitive = paramBoolean;
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      if (!this._forPrimitive);
      for (boolean bool = true; ; bool = false)
        return createSchemaNode("boolean", bool);
    }

    public void serialize(Boolean paramBoolean, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeBoolean(paramBoolean.booleanValue());
    }
  }

  @JacksonStdImpl
  public static final class CalendarSerializer extends ScalarSerializerBase<Calendar>
  {
    public static final CalendarSerializer instance = new CalendarSerializer();

    public CalendarSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      if (paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS));
      for (String str = "number"; ; str = "string")
        return createSchemaNode(str, true);
    }

    public void serialize(Calendar paramCalendar, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramSerializerProvider.defaultSerializeDateValue(paramCalendar.getTimeInMillis(), paramJsonGenerator);
    }
  }

  @JacksonStdImpl
  public static final class DoubleSerializer extends StdSerializers.NonTypedScalarSerializer<Double>
  {
    static final DoubleSerializer instance = new DoubleSerializer();

    public DoubleSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("number", true);
    }

    public void serialize(Double paramDouble, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeNumber(paramDouble.doubleValue());
    }
  }

  @JacksonStdImpl
  public static final class FloatSerializer extends ScalarSerializerBase<Float>
  {
    static final FloatSerializer instance = new FloatSerializer();

    public FloatSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("number", true);
    }

    public void serialize(Float paramFloat, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeNumber(paramFloat.floatValue());
    }
  }

  @JacksonStdImpl
  public static final class IntLikeSerializer extends ScalarSerializerBase<Number>
  {
    static final IntLikeSerializer instance = new IntLikeSerializer();

    public IntLikeSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("integer", true);
    }

    public void serialize(Number paramNumber, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeNumber(paramNumber.intValue());
    }
  }

  @JacksonStdImpl
  public static final class IntegerSerializer extends StdSerializers.NonTypedScalarSerializer<Integer>
  {
    public IntegerSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("integer", true);
    }

    public void serialize(Integer paramInteger, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeNumber(paramInteger.intValue());
    }
  }

  @JacksonStdImpl
  public static final class LongSerializer extends ScalarSerializerBase<Long>
  {
    static final LongSerializer instance = new LongSerializer();

    public LongSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("number", true);
    }

    public void serialize(Long paramLong, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeNumber(paramLong.longValue());
    }
  }

  protected static abstract class NonTypedScalarSerializer<T> extends ScalarSerializerBase<T>
  {
    protected NonTypedScalarSerializer(Class<T> paramClass)
    {
      super();
    }

    public final void serializeWithType(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      serialize(paramT, paramJsonGenerator, paramSerializerProvider);
    }
  }

  @JacksonStdImpl
  public static final class NumberSerializer extends ScalarSerializerBase<Number>
  {
    public static final NumberSerializer instance = new NumberSerializer();

    public NumberSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("number", true);
    }

    public void serialize(Number paramNumber, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      if ((paramNumber instanceof BigDecimal))
      {
        paramJsonGenerator.writeNumber((BigDecimal)paramNumber);
        return;
      }
      if ((paramNumber instanceof BigInteger))
      {
        paramJsonGenerator.writeNumber((BigInteger)paramNumber);
        return;
      }
      if ((paramNumber instanceof Double))
      {
        paramJsonGenerator.writeNumber(((Double)paramNumber).doubleValue());
        return;
      }
      if ((paramNumber instanceof Float))
      {
        paramJsonGenerator.writeNumber(((Float)paramNumber).floatValue());
        return;
      }
      paramJsonGenerator.writeNumber(paramNumber.toString());
    }
  }

  @JacksonStdImpl
  public static final class SerializableSerializer extends SerializerBase<JsonSerializable>
  {
    protected static final SerializableSerializer instance = new SerializableSerializer();

    private SerializableSerializer()
    {
      super();
    }

    // ERROR //
    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
      throws org.codehaus.jackson.map.JsonMappingException
    {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual 30	org/codehaus/jackson/map/ser/StdSerializers$SerializableSerializer:createObjectNode	()Lorg/codehaus/jackson/node/ObjectNode;
      //   4: astore_3
      //   5: ldc 32
      //   7: astore 4
      //   9: aconst_null
      //   10: astore 5
      //   12: aconst_null
      //   13: astore 6
      //   15: aload_2
      //   16: ifnull +115 -> 131
      //   19: aload_2
      //   20: invokestatic 38	org/codehaus/jackson/map/type/TypeFactory:type	(Ljava/lang/reflect/Type;)Lorg/codehaus/jackson/type/JavaType;
      //   23: invokevirtual 44	org/codehaus/jackson/type/JavaType:getRawClass	()Ljava/lang/Class;
      //   26: astore 11
      //   28: aload 11
      //   30: ldc 46
      //   32: invokevirtual 52	java/lang/Class:isAnnotationPresent	(Ljava/lang/Class;)Z
      //   35: istore 12
      //   37: aconst_null
      //   38: astore 5
      //   40: aconst_null
      //   41: astore 6
      //   43: iload 12
      //   45: ifeq +86 -> 131
      //   48: aload 11
      //   50: ldc 46
      //   52: invokevirtual 56	java/lang/Class:getAnnotation	(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
      //   55: checkcast 46	org/codehaus/jackson/schema/JsonSerializableSchema
      //   58: astore 13
      //   60: aload 13
      //   62: invokeinterface 60 1 0
      //   67: astore 4
      //   69: ldc 62
      //   71: aload 13
      //   73: invokeinterface 65 1 0
      //   78: invokevirtual 71	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   81: istore 14
      //   83: aconst_null
      //   84: astore 6
      //   86: iload 14
      //   88: ifne +12 -> 100
      //   91: aload 13
      //   93: invokeinterface 65 1 0
      //   98: astore 6
      //   100: ldc 62
      //   102: aload 13
      //   104: invokeinterface 74 1 0
      //   109: invokevirtual 71	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   112: istore 15
      //   114: aconst_null
      //   115: astore 5
      //   117: iload 15
      //   119: ifne +12 -> 131
      //   122: aload 13
      //   124: invokeinterface 74 1 0
      //   129: astore 5
      //   131: aload_3
      //   132: ldc 75
      //   134: aload 4
      //   136: invokevirtual 81	org/codehaus/jackson/node/ObjectNode:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   139: aload 6
      //   141: ifnull +27 -> 168
      //   144: aload_3
      //   145: ldc 83
      //   147: new 85	org/codehaus/jackson/map/ObjectMapper
      //   150: dup
      //   151: invokespecial 86	org/codehaus/jackson/map/ObjectMapper:<init>	()V
      //   154: aload 6
      //   156: ldc 88
      //   158: invokevirtual 92	org/codehaus/jackson/map/ObjectMapper:readValue	(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
      //   161: checkcast 88	org/codehaus/jackson/JsonNode
      //   164: invokevirtual 95	org/codehaus/jackson/node/ObjectNode:put	(Ljava/lang/String;Lorg/codehaus/jackson/JsonNode;)Lorg/codehaus/jackson/JsonNode;
      //   167: pop
      //   168: aload 5
      //   170: ifnull +27 -> 197
      //   173: aload_3
      //   174: ldc 97
      //   176: new 85	org/codehaus/jackson/map/ObjectMapper
      //   179: dup
      //   180: invokespecial 86	org/codehaus/jackson/map/ObjectMapper:<init>	()V
      //   183: aload 5
      //   185: ldc 88
      //   187: invokevirtual 92	org/codehaus/jackson/map/ObjectMapper:readValue	(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
      //   190: checkcast 88	org/codehaus/jackson/JsonNode
      //   193: invokevirtual 95	org/codehaus/jackson/node/ObjectNode:put	(Ljava/lang/String;Lorg/codehaus/jackson/JsonNode;)Lorg/codehaus/jackson/JsonNode;
      //   196: pop
      //   197: aload_3
      //   198: areturn
      //   199: astore 9
      //   201: new 99	java/lang/IllegalStateException
      //   204: dup
      //   205: aload 9
      //   207: invokespecial 102	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
      //   210: athrow
      //   211: astore 7
      //   213: new 99	java/lang/IllegalStateException
      //   216: dup
      //   217: aload 7
      //   219: invokespecial 102	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
      //   222: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   144	168	199	java/io/IOException
      //   173	197	211	java/io/IOException
    }

    public void serialize(JsonSerializable paramJsonSerializable, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonSerializable.serialize(paramJsonGenerator, paramSerializerProvider);
    }

    public final void serializeWithType(JsonSerializable paramJsonSerializable, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      if ((paramJsonSerializable instanceof JsonSerializableWithType))
      {
        ((JsonSerializableWithType)paramJsonSerializable).serializeWithType(paramJsonGenerator, paramSerializerProvider, paramTypeSerializer);
        return;
      }
      serialize(paramJsonSerializable, paramJsonGenerator, paramSerializerProvider);
    }
  }

  @JacksonStdImpl
  public static final class SerializableWithTypeSerializer extends SerializerBase<JsonSerializableWithType>
  {
    protected static final SerializableWithTypeSerializer instance = new SerializableWithTypeSerializer();

    private SerializableWithTypeSerializer()
    {
      super();
    }

    // ERROR //
    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
      throws org.codehaus.jackson.map.JsonMappingException
    {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual 30	org/codehaus/jackson/map/ser/StdSerializers$SerializableWithTypeSerializer:createObjectNode	()Lorg/codehaus/jackson/node/ObjectNode;
      //   4: astore_3
      //   5: ldc 32
      //   7: astore 4
      //   9: aconst_null
      //   10: astore 5
      //   12: aconst_null
      //   13: astore 6
      //   15: aload_2
      //   16: ifnull +112 -> 128
      //   19: aload_2
      //   20: invokestatic 38	org/codehaus/jackson/map/type/TypeFactory:rawClass	(Ljava/lang/reflect/Type;)Ljava/lang/Class;
      //   23: astore 11
      //   25: aload 11
      //   27: ldc 40
      //   29: invokevirtual 46	java/lang/Class:isAnnotationPresent	(Ljava/lang/Class;)Z
      //   32: istore 12
      //   34: aconst_null
      //   35: astore 5
      //   37: aconst_null
      //   38: astore 6
      //   40: iload 12
      //   42: ifeq +86 -> 128
      //   45: aload 11
      //   47: ldc 40
      //   49: invokevirtual 50	java/lang/Class:getAnnotation	(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
      //   52: checkcast 40	org/codehaus/jackson/schema/JsonSerializableSchema
      //   55: astore 13
      //   57: aload 13
      //   59: invokeinterface 54 1 0
      //   64: astore 4
      //   66: ldc 56
      //   68: aload 13
      //   70: invokeinterface 59 1 0
      //   75: invokevirtual 65	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   78: istore 14
      //   80: aconst_null
      //   81: astore 6
      //   83: iload 14
      //   85: ifne +12 -> 97
      //   88: aload 13
      //   90: invokeinterface 59 1 0
      //   95: astore 6
      //   97: ldc 56
      //   99: aload 13
      //   101: invokeinterface 68 1 0
      //   106: invokevirtual 65	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   109: istore 15
      //   111: aconst_null
      //   112: astore 5
      //   114: iload 15
      //   116: ifne +12 -> 128
      //   119: aload 13
      //   121: invokeinterface 68 1 0
      //   126: astore 5
      //   128: aload_3
      //   129: ldc 70
      //   131: aload 4
      //   133: invokevirtual 76	org/codehaus/jackson/node/ObjectNode:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   136: aload 6
      //   138: ifnull +27 -> 165
      //   141: aload_3
      //   142: ldc 78
      //   144: new 80	org/codehaus/jackson/map/ObjectMapper
      //   147: dup
      //   148: invokespecial 81	org/codehaus/jackson/map/ObjectMapper:<init>	()V
      //   151: aload 6
      //   153: ldc 83
      //   155: invokevirtual 87	org/codehaus/jackson/map/ObjectMapper:readValue	(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
      //   158: checkcast 83	org/codehaus/jackson/JsonNode
      //   161: invokevirtual 90	org/codehaus/jackson/node/ObjectNode:put	(Ljava/lang/String;Lorg/codehaus/jackson/JsonNode;)Lorg/codehaus/jackson/JsonNode;
      //   164: pop
      //   165: aload 5
      //   167: ifnull +27 -> 194
      //   170: aload_3
      //   171: ldc 92
      //   173: new 80	org/codehaus/jackson/map/ObjectMapper
      //   176: dup
      //   177: invokespecial 81	org/codehaus/jackson/map/ObjectMapper:<init>	()V
      //   180: aload 5
      //   182: ldc 83
      //   184: invokevirtual 87	org/codehaus/jackson/map/ObjectMapper:readValue	(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
      //   187: checkcast 83	org/codehaus/jackson/JsonNode
      //   190: invokevirtual 90	org/codehaus/jackson/node/ObjectNode:put	(Ljava/lang/String;Lorg/codehaus/jackson/JsonNode;)Lorg/codehaus/jackson/JsonNode;
      //   193: pop
      //   194: aload_3
      //   195: areturn
      //   196: astore 9
      //   198: new 94	java/lang/IllegalStateException
      //   201: dup
      //   202: aload 9
      //   204: invokespecial 97	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
      //   207: athrow
      //   208: astore 7
      //   210: new 94	java/lang/IllegalStateException
      //   213: dup
      //   214: aload 7
      //   216: invokespecial 97	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
      //   219: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   141	165	196	java/io/IOException
      //   170	194	208	java/io/IOException
    }

    public void serialize(JsonSerializableWithType paramJsonSerializableWithType, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonSerializableWithType.serialize(paramJsonGenerator, paramSerializerProvider);
    }

    public final void serializeWithType(JsonSerializableWithType paramJsonSerializableWithType, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      paramJsonSerializableWithType.serializeWithType(paramJsonGenerator, paramSerializerProvider, paramTypeSerializer);
    }
  }

  @JacksonStdImpl
  public static final class SqlDateSerializer extends ScalarSerializerBase<java.sql.Date>
  {
    public SqlDateSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("string", true);
    }

    public void serialize(java.sql.Date paramDate, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeString(paramDate.toString());
    }
  }

  @JacksonStdImpl
  public static final class SqlTimeSerializer extends ScalarSerializerBase<Time>
  {
    public SqlTimeSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("string", true);
    }

    public void serialize(Time paramTime, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeString(paramTime.toString());
    }
  }

  @JacksonStdImpl
  public static final class StringSerializer extends StdSerializers.NonTypedScalarSerializer<String>
  {
    public StringSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("string", true);
    }

    public void serialize(String paramString, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeString(paramString);
    }
  }

  @JacksonStdImpl
  public static final class TokenBufferSerializer extends SerializerBase<TokenBuffer>
  {
    public TokenBufferSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      return createSchemaNode("any", true);
    }

    public void serialize(TokenBuffer paramTokenBuffer, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramTokenBuffer.serialize(paramJsonGenerator);
    }

    public final void serializeWithType(TokenBuffer paramTokenBuffer, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      paramTypeSerializer.writeTypePrefixForScalar(paramTokenBuffer, paramJsonGenerator);
      serialize(paramTokenBuffer, paramJsonGenerator, paramSerializerProvider);
      paramTypeSerializer.writeTypeSuffixForScalar(paramTokenBuffer, paramJsonGenerator);
    }
  }

  @JacksonStdImpl
  public static final class UtilDateSerializer extends ScalarSerializerBase<java.util.Date>
  {
    public static final UtilDateSerializer instance = new UtilDateSerializer();

    public UtilDateSerializer()
    {
      super();
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    {
      if (paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS));
      for (String str = "number"; ; str = "string")
        return createSchemaNode(str, true);
    }

    public void serialize(java.util.Date paramDate, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramSerializerProvider.defaultSerializeDateValue(paramDate, paramJsonGenerator);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.StdSerializers
 * JD-Core Version:    0.6.0
 */