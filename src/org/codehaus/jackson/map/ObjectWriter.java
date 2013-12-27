package org.codehaus.jackson.map;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.io.SegmentedStringWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectWriter
  implements Versioned
{
  protected static final PrettyPrinter NULL_PRETTY_PRINTER = new MinimalPrettyPrinter();
  protected final SerializationConfig _config;
  protected final JsonFactory _jsonFactory;
  protected final PrettyPrinter _prettyPrinter;
  protected final SerializerProvider _provider;
  protected final JavaType _rootType;
  protected final FormatSchema _schema;
  protected final SerializerFactory _serializerFactory;

  protected ObjectWriter(ObjectMapper paramObjectMapper, SerializationConfig paramSerializationConfig)
  {
    this._config = paramSerializationConfig;
    this._provider = paramObjectMapper._serializerProvider;
    this._serializerFactory = paramObjectMapper._serializerFactory;
    this._jsonFactory = paramObjectMapper._jsonFactory;
    this._rootType = null;
    this._prettyPrinter = null;
    this._schema = null;
  }

  protected ObjectWriter(ObjectMapper paramObjectMapper, SerializationConfig paramSerializationConfig, FormatSchema paramFormatSchema)
  {
    this._config = paramSerializationConfig;
    this._provider = paramObjectMapper._serializerProvider;
    this._serializerFactory = paramObjectMapper._serializerFactory;
    this._jsonFactory = paramObjectMapper._jsonFactory;
    this._rootType = null;
    this._prettyPrinter = null;
    this._schema = paramFormatSchema;
  }

  protected ObjectWriter(ObjectMapper paramObjectMapper, SerializationConfig paramSerializationConfig, JavaType paramJavaType, PrettyPrinter paramPrettyPrinter)
  {
    this._config = paramSerializationConfig;
    this._provider = paramObjectMapper._serializerProvider;
    this._serializerFactory = paramObjectMapper._serializerFactory;
    this._jsonFactory = paramObjectMapper._jsonFactory;
    this._rootType = paramJavaType;
    this._prettyPrinter = paramPrettyPrinter;
    this._schema = null;
  }

  protected ObjectWriter(ObjectWriter paramObjectWriter, SerializationConfig paramSerializationConfig)
  {
    this._config = paramSerializationConfig;
    this._provider = paramObjectWriter._provider;
    this._serializerFactory = paramObjectWriter._serializerFactory;
    this._jsonFactory = paramObjectWriter._jsonFactory;
    this._schema = paramObjectWriter._schema;
    this._rootType = paramObjectWriter._rootType;
    this._prettyPrinter = paramObjectWriter._prettyPrinter;
  }

  protected ObjectWriter(ObjectWriter paramObjectWriter, SerializationConfig paramSerializationConfig, JavaType paramJavaType, PrettyPrinter paramPrettyPrinter, FormatSchema paramFormatSchema)
  {
    this._config = paramSerializationConfig;
    this._provider = paramObjectWriter._provider;
    this._serializerFactory = paramObjectWriter._serializerFactory;
    this._jsonFactory = paramObjectWriter._jsonFactory;
    this._rootType = paramJavaType;
    this._prettyPrinter = paramPrettyPrinter;
    this._schema = paramFormatSchema;
  }

  // ERROR //
  private final void _configAndWriteCloseable(JsonGenerator paramJsonGenerator, Object paramObject, SerializationConfig paramSerializationConfig)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    // Byte code:
    //   0: aload_2
    //   1: checkcast 67	java/io/Closeable
    //   4: astore 4
    //   6: aload_0
    //   7: getfield 49	org/codehaus/jackson/map/ObjectWriter:_rootType	Lorg/codehaus/jackson/type/JavaType;
    //   10: ifnonnull +75 -> 85
    //   13: aload_0
    //   14: getfield 41	org/codehaus/jackson/map/ObjectWriter:_provider	Lorg/codehaus/jackson/map/SerializerProvider;
    //   17: aload_3
    //   18: aload_1
    //   19: aload_2
    //   20: aload_0
    //   21: getfield 44	org/codehaus/jackson/map/ObjectWriter:_serializerFactory	Lorg/codehaus/jackson/map/SerializerFactory;
    //   24: invokevirtual 73	org/codehaus/jackson/map/SerializerProvider:serializeValue	(Lorg/codehaus/jackson/map/SerializationConfig;Lorg/codehaus/jackson/JsonGenerator;Ljava/lang/Object;Lorg/codehaus/jackson/map/SerializerFactory;)V
    //   27: aload_0
    //   28: getfield 53	org/codehaus/jackson/map/ObjectWriter:_schema	Lorg/codehaus/jackson/FormatSchema;
    //   31: ifnull +11 -> 42
    //   34: aload_1
    //   35: aload_0
    //   36: getfield 53	org/codehaus/jackson/map/ObjectWriter:_schema	Lorg/codehaus/jackson/FormatSchema;
    //   39: invokevirtual 79	org/codehaus/jackson/JsonGenerator:setSchema	(Lorg/codehaus/jackson/FormatSchema;)V
    //   42: aload_1
    //   43: astore 11
    //   45: aconst_null
    //   46: astore_1
    //   47: aload 11
    //   49: invokevirtual 82	org/codehaus/jackson/JsonGenerator:close	()V
    //   52: aload 4
    //   54: astore 12
    //   56: aconst_null
    //   57: astore 4
    //   59: aload 12
    //   61: invokeinterface 83 1 0
    //   66: iconst_0
    //   67: ifeq +7 -> 74
    //   70: aconst_null
    //   71: invokevirtual 82	org/codehaus/jackson/JsonGenerator:close	()V
    //   74: iconst_0
    //   75: ifeq +9 -> 84
    //   78: aconst_null
    //   79: invokeinterface 83 1 0
    //   84: return
    //   85: aload_0
    //   86: getfield 41	org/codehaus/jackson/map/ObjectWriter:_provider	Lorg/codehaus/jackson/map/SerializerProvider;
    //   89: astore 8
    //   91: aload_0
    //   92: getfield 49	org/codehaus/jackson/map/ObjectWriter:_rootType	Lorg/codehaus/jackson/type/JavaType;
    //   95: astore 9
    //   97: aload_0
    //   98: getfield 44	org/codehaus/jackson/map/ObjectWriter:_serializerFactory	Lorg/codehaus/jackson/map/SerializerFactory;
    //   101: astore 10
    //   103: aload 8
    //   105: aload_3
    //   106: aload_1
    //   107: aload_2
    //   108: aload 9
    //   110: aload 10
    //   112: invokevirtual 86	org/codehaus/jackson/map/SerializerProvider:serializeValue	(Lorg/codehaus/jackson/map/SerializationConfig;Lorg/codehaus/jackson/JsonGenerator;Ljava/lang/Object;Lorg/codehaus/jackson/type/JavaType;Lorg/codehaus/jackson/map/SerializerFactory;)V
    //   115: goto -88 -> 27
    //   118: astore 5
    //   120: aload_1
    //   121: ifnull +7 -> 128
    //   124: aload_1
    //   125: invokevirtual 82	org/codehaus/jackson/JsonGenerator:close	()V
    //   128: aload 4
    //   130: ifnull +10 -> 140
    //   133: aload 4
    //   135: invokeinterface 83 1 0
    //   140: aload 5
    //   142: athrow
    //   143: astore 14
    //   145: goto -71 -> 74
    //   148: astore 13
    //   150: return
    //   151: astore 7
    //   153: goto -25 -> 128
    //   156: astore 6
    //   158: goto -18 -> 140
    //
    // Exception table:
    //   from	to	target	type
    //   6	27	118	finally
    //   27	42	118	finally
    //   47	52	118	finally
    //   59	66	118	finally
    //   85	115	118	finally
    //   70	74	143	java/io/IOException
    //   78	84	148	java/io/IOException
    //   124	128	151	java/io/IOException
    //   133	140	156	java/io/IOException
  }

  // ERROR //
  private final void _writeCloseableValue(JsonGenerator paramJsonGenerator, Object paramObject, SerializationConfig paramSerializationConfig)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    // Byte code:
    //   0: aload_2
    //   1: checkcast 67	java/io/Closeable
    //   4: astore 4
    //   6: aload_0
    //   7: getfield 49	org/codehaus/jackson/map/ObjectWriter:_rootType	Lorg/codehaus/jackson/type/JavaType;
    //   10: ifnonnull +59 -> 69
    //   13: aload_0
    //   14: getfield 41	org/codehaus/jackson/map/ObjectWriter:_provider	Lorg/codehaus/jackson/map/SerializerProvider;
    //   17: aload_3
    //   18: aload_1
    //   19: aload_2
    //   20: aload_0
    //   21: getfield 44	org/codehaus/jackson/map/ObjectWriter:_serializerFactory	Lorg/codehaus/jackson/map/SerializerFactory;
    //   24: invokevirtual 73	org/codehaus/jackson/map/SerializerProvider:serializeValue	(Lorg/codehaus/jackson/map/SerializationConfig;Lorg/codehaus/jackson/JsonGenerator;Ljava/lang/Object;Lorg/codehaus/jackson/map/SerializerFactory;)V
    //   27: aload_0
    //   28: getfield 34	org/codehaus/jackson/map/ObjectWriter:_config	Lorg/codehaus/jackson/map/SerializationConfig;
    //   31: getstatic 93	org/codehaus/jackson/map/SerializationConfig$Feature:FLUSH_AFTER_WRITE_VALUE	Lorg/codehaus/jackson/map/SerializationConfig$Feature;
    //   34: invokevirtual 99	org/codehaus/jackson/map/SerializationConfig:isEnabled	(Lorg/codehaus/jackson/map/SerializationConfig$Feature;)Z
    //   37: ifeq +7 -> 44
    //   40: aload_1
    //   41: invokevirtual 102	org/codehaus/jackson/JsonGenerator:flush	()V
    //   44: aload 4
    //   46: astore 7
    //   48: aconst_null
    //   49: astore 4
    //   51: aload 7
    //   53: invokeinterface 83 1 0
    //   58: iconst_0
    //   59: ifeq +9 -> 68
    //   62: aconst_null
    //   63: invokeinterface 83 1 0
    //   68: return
    //   69: aload_0
    //   70: getfield 41	org/codehaus/jackson/map/ObjectWriter:_provider	Lorg/codehaus/jackson/map/SerializerProvider;
    //   73: aload_3
    //   74: aload_1
    //   75: aload_2
    //   76: aload_0
    //   77: getfield 49	org/codehaus/jackson/map/ObjectWriter:_rootType	Lorg/codehaus/jackson/type/JavaType;
    //   80: aload_0
    //   81: getfield 44	org/codehaus/jackson/map/ObjectWriter:_serializerFactory	Lorg/codehaus/jackson/map/SerializerFactory;
    //   84: invokevirtual 86	org/codehaus/jackson/map/SerializerProvider:serializeValue	(Lorg/codehaus/jackson/map/SerializationConfig;Lorg/codehaus/jackson/JsonGenerator;Ljava/lang/Object;Lorg/codehaus/jackson/type/JavaType;Lorg/codehaus/jackson/map/SerializerFactory;)V
    //   87: goto -60 -> 27
    //   90: astore 5
    //   92: aload 4
    //   94: ifnull +10 -> 104
    //   97: aload 4
    //   99: invokeinterface 83 1 0
    //   104: aload 5
    //   106: athrow
    //   107: astore 8
    //   109: return
    //   110: astore 6
    //   112: goto -8 -> 104
    //
    // Exception table:
    //   from	to	target	type
    //   6	27	90	finally
    //   27	44	90	finally
    //   51	58	90	finally
    //   69	87	90	finally
    //   62	68	107	java/io/IOException
    //   97	104	110	java/io/IOException
  }

  protected final void _configAndWriteValue(JsonGenerator paramJsonGenerator, Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    if (this._prettyPrinter != null)
    {
      PrettyPrinter localPrettyPrinter = this._prettyPrinter;
      if (localPrettyPrinter == NULL_PRETTY_PRINTER)
        localPrettyPrinter = null;
      paramJsonGenerator.setPrettyPrinter(localPrettyPrinter);
      if (this._schema != null)
        paramJsonGenerator.setSchema(this._schema);
      if ((!this._config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) || (!(paramObject instanceof Closeable)))
        break label98;
      _configAndWriteCloseable(paramJsonGenerator, paramObject, this._config);
    }
    while (true)
    {
      return;
      if (!this._config.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT))
        break;
      paramJsonGenerator.useDefaultPrettyPrinter();
      break;
      label98: int i = 0;
      try
      {
        JavaType localJavaType = this._rootType;
        i = 0;
        if (localJavaType == null)
          this._provider.serializeValue(this._config, paramJsonGenerator, paramObject, this._serializerFactory);
        while (true)
        {
          i = 1;
          paramJsonGenerator.close();
          if (i != 0)
            break;
          try
          {
            paramJsonGenerator.close();
            return;
          }
          catch (IOException localIOException2)
          {
            return;
          }
          this._provider.serializeValue(this._config, paramJsonGenerator, paramObject, this._rootType, this._serializerFactory);
        }
      }
      finally
      {
        if (i != 0);
      }
    }
    try
    {
      paramJsonGenerator.close();
      label187: throw localObject;
    }
    catch (IOException localIOException1)
    {
      break label187;
    }
  }

  public boolean canSerialize(Class<?> paramClass)
  {
    return this._provider.hasSerializerFor(this._config, paramClass, this._serializerFactory);
  }

  public Version version()
  {
    return VersionUtil.versionFor(getClass());
  }

  public ObjectWriter withDefaultPrettyPrinter()
  {
    return withPrettyPrinter(new DefaultPrettyPrinter());
  }

  public ObjectWriter withFilters(FilterProvider paramFilterProvider)
  {
    if (paramFilterProvider == this._config.getFilterProvider())
      return this;
    return new ObjectWriter(this, this._config.withFilters(paramFilterProvider));
  }

  public ObjectWriter withPrettyPrinter(PrettyPrinter paramPrettyPrinter)
  {
    if (paramPrettyPrinter == this._prettyPrinter)
      return this;
    if (paramPrettyPrinter == null)
      paramPrettyPrinter = NULL_PRETTY_PRINTER;
    SerializationConfig localSerializationConfig = this._config;
    JavaType localJavaType = this._rootType;
    FormatSchema localFormatSchema = this._schema;
    return new ObjectWriter(this, localSerializationConfig, localJavaType, paramPrettyPrinter, localFormatSchema);
  }

  public ObjectWriter withSchema(FormatSchema paramFormatSchema)
  {
    if (this._schema == paramFormatSchema)
      return this;
    return new ObjectWriter(this, this._config, this._rootType, this._prettyPrinter, paramFormatSchema);
  }

  public ObjectWriter withType(Class<?> paramClass)
  {
    return withType(this._config.constructType(paramClass));
  }

  public ObjectWriter withType(JavaType paramJavaType)
  {
    if (paramJavaType == this._rootType)
      return this;
    return new ObjectWriter(this, this._config, paramJavaType, this._prettyPrinter, this._schema);
  }

  public ObjectWriter withType(TypeReference<?> paramTypeReference)
  {
    return withType(this._config.getTypeFactory().constructType(paramTypeReference.getType()));
  }

  public ObjectWriter withView(Class<?> paramClass)
  {
    if (paramClass == this._config.getSerializationView())
      return this;
    return new ObjectWriter(this, this._config.withView(paramClass));
  }

  public void writeValue(File paramFile, Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    _configAndWriteValue(this._jsonFactory.createJsonGenerator(paramFile, JsonEncoding.UTF8), paramObject);
  }

  public void writeValue(OutputStream paramOutputStream, Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    _configAndWriteValue(this._jsonFactory.createJsonGenerator(paramOutputStream, JsonEncoding.UTF8), paramObject);
  }

  public void writeValue(Writer paramWriter, Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    _configAndWriteValue(this._jsonFactory.createJsonGenerator(paramWriter), paramObject);
  }

  public void writeValue(JsonGenerator paramJsonGenerator, Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    if ((this._config.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((paramObject instanceof Closeable)))
      _writeCloseableValue(paramJsonGenerator, paramObject, this._config);
    while (true)
    {
      return;
      if (this._rootType == null)
        this._provider.serializeValue(this._config, paramJsonGenerator, paramObject, this._serializerFactory);
      while (this._config.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
      {
        paramJsonGenerator.flush();
        return;
        this._provider.serializeValue(this._config, paramJsonGenerator, paramObject, this._rootType, this._serializerFactory);
      }
    }
  }

  public byte[] writeValueAsBytes(Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    ByteArrayBuilder localByteArrayBuilder = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
    _configAndWriteValue(this._jsonFactory.createJsonGenerator(localByteArrayBuilder, JsonEncoding.UTF8), paramObject);
    byte[] arrayOfByte = localByteArrayBuilder.toByteArray();
    localByteArrayBuilder.release();
    return arrayOfByte;
  }

  public String writeValueAsString(Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    SegmentedStringWriter localSegmentedStringWriter = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
    _configAndWriteValue(this._jsonFactory.createJsonGenerator(localSegmentedStringWriter), paramObject);
    return localSegmentedStringWriter.getAndClear();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ObjectWriter
 * JD-Core Version:    0.6.0
 */