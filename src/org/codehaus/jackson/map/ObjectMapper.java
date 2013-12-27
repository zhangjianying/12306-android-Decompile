package org.codehaus.jackson.map;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.io.SegmentedStringWriter;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.introspect.BasicClassIntrospector;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.introspect.VisibilityChecker.Std;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.type.TypeModifier;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.TokenBuffer;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectMapper extends ObjectCodec
  implements Versioned
{
  protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR;
  protected static final ClassIntrospector<? extends BeanDescription> DEFAULT_INTROSPECTOR;
  private static final JavaType JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
  protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER;
  protected DeserializationConfig _deserializationConfig;
  protected DeserializerProvider _deserializerProvider;
  protected final JsonFactory _jsonFactory;
  protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers = new ConcurrentHashMap(64, 0.6F, 2);
  protected SerializationConfig _serializationConfig;
  protected SerializerFactory _serializerFactory;
  protected SerializerProvider _serializerProvider;
  protected SubtypeResolver _subtypeResolver;
  protected TypeFactory _typeFactory;

  static
  {
    DEFAULT_INTROSPECTOR = BasicClassIntrospector.instance;
    DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
    STD_VISIBILITY_CHECKER = VisibilityChecker.Std.defaultInstance();
  }

  public ObjectMapper()
  {
    this(null, null, null);
  }

  public ObjectMapper(JsonFactory paramJsonFactory)
  {
    this(paramJsonFactory, null, null);
  }

  public ObjectMapper(JsonFactory paramJsonFactory, SerializerProvider paramSerializerProvider, DeserializerProvider paramDeserializerProvider)
  {
    this(paramJsonFactory, paramSerializerProvider, paramDeserializerProvider, null, null);
  }

  public ObjectMapper(JsonFactory paramJsonFactory, SerializerProvider paramSerializerProvider, DeserializerProvider paramDeserializerProvider, SerializationConfig paramSerializationConfig, DeserializationConfig paramDeserializationConfig)
  {
    if (paramJsonFactory == null)
      paramJsonFactory = new MappingJsonFactory(this);
    this._jsonFactory = paramJsonFactory;
    this._typeFactory = TypeFactory.defaultInstance();
    if (paramSerializationConfig != null)
    {
      this._serializationConfig = paramSerializationConfig;
      if (paramDeserializationConfig == null)
        break label137;
    }
    while (true)
    {
      this._deserializationConfig = paramDeserializationConfig;
      if (paramSerializerProvider == null)
        paramSerializerProvider = new StdSerializerProvider();
      this._serializerProvider = paramSerializerProvider;
      if (paramDeserializerProvider == null)
        paramDeserializerProvider = new StdDeserializerProvider();
      this._deserializerProvider = paramDeserializerProvider;
      this._serializerFactory = BeanSerializerFactory.instance;
      return;
      paramSerializationConfig = new SerializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null);
      break;
      label137: paramDeserializationConfig = new DeserializationConfig(DEFAULT_INTROSPECTOR, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, null, this._typeFactory, null);
    }
  }

  @Deprecated
  public ObjectMapper(SerializerFactory paramSerializerFactory)
  {
    this(null, null, null);
    setSerializerFactory(paramSerializerFactory);
  }

  // ERROR //
  private final void _configAndWriteCloseable(JsonGenerator paramJsonGenerator, Object paramObject, SerializationConfig paramSerializationConfig)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    // Byte code:
    //   0: aload_2
    //   1: checkcast 145	java/io/Closeable
    //   4: astore 4
    //   6: aload_0
    //   7: getfield 109	org/codehaus/jackson/map/ObjectMapper:_serializerProvider	Lorg/codehaus/jackson/map/SerializerProvider;
    //   10: aload_3
    //   11: aload_1
    //   12: aload_2
    //   13: aload_0
    //   14: getfield 121	org/codehaus/jackson/map/ObjectMapper:_serializerFactory	Lorg/codehaus/jackson/map/SerializerFactory;
    //   17: invokevirtual 151	org/codehaus/jackson/map/SerializerProvider:serializeValue	(Lorg/codehaus/jackson/map/SerializationConfig;Lorg/codehaus/jackson/JsonGenerator;Ljava/lang/Object;Lorg/codehaus/jackson/map/SerializerFactory;)V
    //   20: aload_1
    //   21: astore 8
    //   23: aconst_null
    //   24: astore_1
    //   25: aload 8
    //   27: invokevirtual 156	org/codehaus/jackson/JsonGenerator:close	()V
    //   30: aload 4
    //   32: astore 9
    //   34: aconst_null
    //   35: astore 4
    //   37: aload 9
    //   39: invokeinterface 157 1 0
    //   44: iconst_0
    //   45: ifeq +7 -> 52
    //   48: aconst_null
    //   49: invokevirtual 156	org/codehaus/jackson/JsonGenerator:close	()V
    //   52: iconst_0
    //   53: ifeq +9 -> 62
    //   56: aconst_null
    //   57: invokeinterface 157 1 0
    //   62: return
    //   63: astore 5
    //   65: aload_1
    //   66: ifnull +7 -> 73
    //   69: aload_1
    //   70: invokevirtual 156	org/codehaus/jackson/JsonGenerator:close	()V
    //   73: aload 4
    //   75: ifnull +10 -> 85
    //   78: aload 4
    //   80: invokeinterface 157 1 0
    //   85: aload 5
    //   87: athrow
    //   88: astore 11
    //   90: goto -38 -> 52
    //   93: astore 10
    //   95: return
    //   96: astore 7
    //   98: goto -25 -> 73
    //   101: astore 6
    //   103: goto -18 -> 85
    //
    // Exception table:
    //   from	to	target	type
    //   6	20	63	finally
    //   25	30	63	finally
    //   37	44	63	finally
    //   48	52	88	java/io/IOException
    //   56	62	93	java/io/IOException
    //   69	73	96	java/io/IOException
    //   78	85	101	java/io/IOException
  }

  // ERROR //
  private final void _writeCloseableValue(JsonGenerator paramJsonGenerator, Object paramObject, SerializationConfig paramSerializationConfig)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    // Byte code:
    //   0: aload_2
    //   1: checkcast 145	java/io/Closeable
    //   4: astore 4
    //   6: aload_0
    //   7: getfield 109	org/codehaus/jackson/map/ObjectMapper:_serializerProvider	Lorg/codehaus/jackson/map/SerializerProvider;
    //   10: aload_3
    //   11: aload_1
    //   12: aload_2
    //   13: aload_0
    //   14: getfield 121	org/codehaus/jackson/map/ObjectMapper:_serializerFactory	Lorg/codehaus/jackson/map/SerializerFactory;
    //   17: invokevirtual 151	org/codehaus/jackson/map/SerializerProvider:serializeValue	(Lorg/codehaus/jackson/map/SerializationConfig;Lorg/codehaus/jackson/JsonGenerator;Ljava/lang/Object;Lorg/codehaus/jackson/map/SerializerFactory;)V
    //   20: aload_3
    //   21: getstatic 164	org/codehaus/jackson/map/SerializationConfig$Feature:FLUSH_AFTER_WRITE_VALUE	Lorg/codehaus/jackson/map/SerializationConfig$Feature;
    //   24: invokevirtual 168	org/codehaus/jackson/map/SerializationConfig:isEnabled	(Lorg/codehaus/jackson/map/SerializationConfig$Feature;)Z
    //   27: ifeq +7 -> 34
    //   30: aload_1
    //   31: invokevirtual 171	org/codehaus/jackson/JsonGenerator:flush	()V
    //   34: aload 4
    //   36: astore 7
    //   38: aconst_null
    //   39: astore 4
    //   41: aload 7
    //   43: invokeinterface 157 1 0
    //   48: iconst_0
    //   49: ifeq +9 -> 58
    //   52: aconst_null
    //   53: invokeinterface 157 1 0
    //   58: return
    //   59: astore 5
    //   61: aload 4
    //   63: ifnull +10 -> 73
    //   66: aload 4
    //   68: invokeinterface 157 1 0
    //   73: aload 5
    //   75: athrow
    //   76: astore 8
    //   78: return
    //   79: astore 6
    //   81: goto -8 -> 73
    //
    // Exception table:
    //   from	to	target	type
    //   6	34	59	finally
    //   41	48	59	finally
    //   52	58	76	java/io/IOException
    //   66	73	79	java/io/IOException
  }

  protected final void _configAndWriteValue(JsonGenerator paramJsonGenerator, Object paramObject)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    SerializationConfig localSerializationConfig = copySerializationConfig();
    if (localSerializationConfig.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT))
      paramJsonGenerator.useDefaultPrettyPrinter();
    if ((localSerializationConfig.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((paramObject instanceof Closeable)))
      _configAndWriteCloseable(paramJsonGenerator, paramObject, localSerializationConfig);
    while (true)
    {
      return;
      int i = 0;
      try
      {
        this._serializerProvider.serializeValue(localSerializationConfig, paramJsonGenerator, paramObject, this._serializerFactory);
        i = 1;
        paramJsonGenerator.close();
        if (i != 0)
          continue;
        try
        {
          paramJsonGenerator.close();
          return;
        }
        catch (IOException localIOException2)
        {
          return;
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
      label93: throw localObject;
    }
    catch (IOException localIOException1)
    {
      break label93;
    }
  }

  protected final void _configAndWriteValue(JsonGenerator paramJsonGenerator, Object paramObject, Class<?> paramClass)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    SerializationConfig localSerializationConfig = copySerializationConfig().withView(paramClass);
    if (localSerializationConfig.isEnabled(SerializationConfig.Feature.INDENT_OUTPUT))
      paramJsonGenerator.useDefaultPrettyPrinter();
    if ((localSerializationConfig.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((paramObject instanceof Closeable)))
      _configAndWriteCloseable(paramJsonGenerator, paramObject, localSerializationConfig);
    while (true)
    {
      return;
      int i = 0;
      try
      {
        this._serializerProvider.serializeValue(localSerializationConfig, paramJsonGenerator, paramObject, this._serializerFactory);
        i = 1;
        paramJsonGenerator.close();
        if (i != 0)
          continue;
        try
        {
          paramJsonGenerator.close();
          return;
        }
        catch (IOException localIOException2)
        {
          return;
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
      label102: throw localObject;
    }
    catch (IOException localIOException1)
    {
      break label102;
    }
  }

  protected Object _convert(Object paramObject, JavaType paramJavaType)
    throws IllegalArgumentException
  {
    if (paramObject == null)
      return null;
    TokenBuffer localTokenBuffer = new TokenBuffer(this);
    try
    {
      writeValue(localTokenBuffer, paramObject);
      JsonParser localJsonParser = localTokenBuffer.asParser();
      Object localObject = readValue(localJsonParser, paramJavaType);
      localJsonParser.close();
      return localObject;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException(localIOException.getMessage(), localIOException);
  }

  protected DeserializationContext _createDeserializationContext(JsonParser paramJsonParser, DeserializationConfig paramDeserializationConfig)
  {
    return new StdDeserializationContext(paramDeserializationConfig, paramJsonParser, this._deserializerProvider);
  }

  protected PrettyPrinter _defaultPrettyPrinter()
  {
    return new DefaultPrettyPrinter();
  }

  protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
    throws JsonMappingException
  {
    JsonDeserializer localJsonDeserializer1 = (JsonDeserializer)this._rootDeserializers.get(paramJavaType);
    if (localJsonDeserializer1 != null)
      return localJsonDeserializer1;
    JsonDeserializer localJsonDeserializer2 = this._deserializerProvider.findTypedValueDeserializer(paramDeserializationConfig, paramJavaType, null);
    if (localJsonDeserializer2 == null)
      throw new JsonMappingException("Can not find a deserializer for type " + paramJavaType);
    this._rootDeserializers.put(paramJavaType, localJsonDeserializer2);
    return localJsonDeserializer2;
  }

  protected JsonToken _initForReading(JsonParser paramJsonParser)
    throws IOException, JsonParseException, JsonMappingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == null)
    {
      localJsonToken = paramJsonParser.nextToken();
      if (localJsonToken == null)
        throw new EOFException("No content to map to Object due to end of input");
    }
    return localJsonToken;
  }

  // ERROR //
  protected Object _readMapAndClose(JsonParser paramJsonParser, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 291	org/codehaus/jackson/map/ObjectMapper:_initForReading	(Lorg/codehaus/jackson/JsonParser;)Lorg/codehaus/jackson/JsonToken;
    //   5: astore 5
    //   7: aload 5
    //   9: getstatic 297	org/codehaus/jackson/JsonToken:VALUE_NULL	Lorg/codehaus/jackson/JsonToken;
    //   12: if_acmpeq +87 -> 99
    //   15: aload 5
    //   17: getstatic 300	org/codehaus/jackson/JsonToken:END_ARRAY	Lorg/codehaus/jackson/JsonToken;
    //   20: if_acmpeq +79 -> 99
    //   23: aload 5
    //   25: getstatic 303	org/codehaus/jackson/JsonToken:END_OBJECT	Lorg/codehaus/jackson/JsonToken;
    //   28: if_acmpne +17 -> 45
    //   31: goto +68 -> 99
    //   34: aload_1
    //   35: invokevirtual 306	org/codehaus/jackson/JsonParser:clearCurrentToken	()V
    //   38: aload_1
    //   39: invokevirtual 217	org/codehaus/jackson/JsonParser:close	()V
    //   42: aload 6
    //   44: areturn
    //   45: aload_0
    //   46: invokevirtual 310	org/codehaus/jackson/map/ObjectMapper:copyDeserializationConfig	()Lorg/codehaus/jackson/map/DeserializationConfig;
    //   49: astore 8
    //   51: aload_0
    //   52: aload_1
    //   53: aload 8
    //   55: invokevirtual 312	org/codehaus/jackson/map/ObjectMapper:_createDeserializationContext	(Lorg/codehaus/jackson/JsonParser;Lorg/codehaus/jackson/map/DeserializationConfig;)Lorg/codehaus/jackson/map/DeserializationContext;
    //   58: astore 9
    //   60: aload_0
    //   61: aload 8
    //   63: aload_2
    //   64: invokevirtual 314	org/codehaus/jackson/map/ObjectMapper:_findRootDeserializer	(Lorg/codehaus/jackson/map/DeserializationConfig;Lorg/codehaus/jackson/type/JavaType;)Lorg/codehaus/jackson/map/JsonDeserializer;
    //   67: aload_1
    //   68: aload 9
    //   70: invokevirtual 318	org/codehaus/jackson/map/JsonDeserializer:deserialize	(Lorg/codehaus/jackson/JsonParser;Lorg/codehaus/jackson/map/DeserializationContext;)Ljava/lang/Object;
    //   73: astore 10
    //   75: aload 10
    //   77: astore 6
    //   79: goto -45 -> 34
    //   82: astore_3
    //   83: aload_1
    //   84: invokevirtual 217	org/codehaus/jackson/JsonParser:close	()V
    //   87: aload_3
    //   88: athrow
    //   89: astore 7
    //   91: aload 6
    //   93: areturn
    //   94: astore 4
    //   96: goto -9 -> 87
    //   99: aconst_null
    //   100: astore 6
    //   102: goto -68 -> 34
    //
    // Exception table:
    //   from	to	target	type
    //   0	31	82	finally
    //   34	38	82	finally
    //   45	75	82	finally
    //   38	42	89	java/io/IOException
    //   83	87	94	java/io/IOException
  }

  protected Object _readValue(DeserializationConfig paramDeserializationConfig, JsonParser paramJsonParser, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    JsonToken localJsonToken = _initForReading(paramJsonParser);
    if ((localJsonToken == JsonToken.VALUE_NULL) || (localJsonToken == JsonToken.END_ARRAY) || (localJsonToken == JsonToken.END_OBJECT));
    DeserializationContext localDeserializationContext;
    for (Object localObject = null; ; localObject = _findRootDeserializer(paramDeserializationConfig, paramJavaType).deserialize(paramJsonParser, localDeserializationContext))
    {
      paramJsonParser.clearCurrentToken();
      return localObject;
      localDeserializationContext = _createDeserializationContext(paramJsonParser, paramDeserializationConfig);
    }
  }

  public boolean canDeserialize(JavaType paramJavaType)
  {
    return this._deserializerProvider.hasValueDeserializerFor(copyDeserializationConfig(), paramJavaType);
  }

  public boolean canSerialize(Class<?> paramClass)
  {
    return this._serializerProvider.hasSerializerFor(copySerializationConfig(), paramClass, this._serializerFactory);
  }

  public ObjectMapper configure(JsonGenerator.Feature paramFeature, boolean paramBoolean)
  {
    this._jsonFactory.configure(paramFeature, paramBoolean);
    return this;
  }

  public ObjectMapper configure(JsonParser.Feature paramFeature, boolean paramBoolean)
  {
    this._jsonFactory.configure(paramFeature, paramBoolean);
    return this;
  }

  public ObjectMapper configure(DeserializationConfig.Feature paramFeature, boolean paramBoolean)
  {
    this._deserializationConfig.set(paramFeature, paramBoolean);
    return this;
  }

  public ObjectMapper configure(SerializationConfig.Feature paramFeature, boolean paramBoolean)
  {
    this._serializationConfig.set(paramFeature, paramBoolean);
    return this;
  }

  public JavaType constructType(Type paramType)
  {
    return this._typeFactory.constructType(paramType);
  }

  public <T> T convertValue(Object paramObject, Class<T> paramClass)
    throws IllegalArgumentException
  {
    return _convert(paramObject, this._typeFactory.constructType(paramClass));
  }

  public <T> T convertValue(Object paramObject, JavaType paramJavaType)
    throws IllegalArgumentException
  {
    return _convert(paramObject, paramJavaType);
  }

  public <T> T convertValue(Object paramObject, TypeReference paramTypeReference)
    throws IllegalArgumentException
  {
    return _convert(paramObject, this._typeFactory.constructType(paramTypeReference));
  }

  public DeserializationConfig copyDeserializationConfig()
  {
    return this._deserializationConfig.createUnshared(this._subtypeResolver);
  }

  public SerializationConfig copySerializationConfig()
  {
    return this._serializationConfig.createUnshared(this._subtypeResolver);
  }

  public ArrayNode createArrayNode()
  {
    return this._deserializationConfig.getNodeFactory().arrayNode();
  }

  public ObjectNode createObjectNode()
  {
    return this._deserializationConfig.getNodeFactory().objectNode();
  }

  public ObjectWriter defaultPrettyPrintingWriter()
  {
    return new ObjectWriter(this, copySerializationConfig(), null, _defaultPrettyPrinter());
  }

  public ObjectMapper disableDefaultTyping()
  {
    return setDefaultTyping(null);
  }

  public ObjectMapper enableDefaultTyping()
  {
    return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
  }

  public ObjectMapper enableDefaultTyping(DefaultTyping paramDefaultTyping)
  {
    return enableDefaultTyping(paramDefaultTyping, JsonTypeInfo.As.WRAPPER_ARRAY);
  }

  public ObjectMapper enableDefaultTyping(DefaultTyping paramDefaultTyping, JsonTypeInfo.As paramAs)
  {
    return setDefaultTyping(new DefaultTypeResolverBuilder(paramDefaultTyping).init(JsonTypeInfo.Id.CLASS, null).inclusion(paramAs));
  }

  public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping paramDefaultTyping, String paramString)
  {
    return setDefaultTyping(new DefaultTypeResolverBuilder(paramDefaultTyping).init(JsonTypeInfo.Id.CLASS, null).inclusion(JsonTypeInfo.As.PROPERTY).typeProperty(paramString));
  }

  public ObjectWriter filteredWriter(FilterProvider paramFilterProvider)
  {
    return new ObjectWriter(this, copySerializationConfig().withFilters(paramFilterProvider));
  }

  public JsonSchema generateJsonSchema(Class<?> paramClass)
    throws JsonMappingException
  {
    return generateJsonSchema(paramClass, copySerializationConfig());
  }

  public JsonSchema generateJsonSchema(Class<?> paramClass, SerializationConfig paramSerializationConfig)
    throws JsonMappingException
  {
    return this._serializerProvider.generateJsonSchema(paramClass, paramSerializationConfig, this._serializerFactory);
  }

  public DeserializationConfig getDeserializationConfig()
  {
    return this._deserializationConfig;
  }

  public DeserializerProvider getDeserializerProvider()
  {
    return this._deserializerProvider;
  }

  public JsonFactory getJsonFactory()
  {
    return this._jsonFactory;
  }

  public JsonNodeFactory getNodeFactory()
  {
    return this._deserializationConfig.getNodeFactory();
  }

  public SerializationConfig getSerializationConfig()
  {
    return this._serializationConfig;
  }

  public SerializerProvider getSerializerProvider()
  {
    return this._serializerProvider;
  }

  public SubtypeResolver getSubtypeResolver()
  {
    if (this._subtypeResolver == null)
      this._subtypeResolver = new StdSubtypeResolver();
    return this._subtypeResolver;
  }

  public TypeFactory getTypeFactory()
  {
    return this._typeFactory;
  }

  public VisibilityChecker<?> getVisibilityChecker()
  {
    return this._serializationConfig.getDefaultVisibilityChecker();
  }

  public ObjectWriter prettyPrintingWriter(PrettyPrinter paramPrettyPrinter)
  {
    if (paramPrettyPrinter == null)
      paramPrettyPrinter = ObjectWriter.NULL_PRETTY_PRINTER;
    return new ObjectWriter(this, copySerializationConfig(), null, paramPrettyPrinter);
  }

  public JsonNode readTree(InputStream paramInputStream)
    throws IOException, JsonProcessingException
  {
    Object localObject = (JsonNode)readValue(paramInputStream, JSON_NODE_TYPE);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(Reader paramReader)
    throws IOException, JsonProcessingException
  {
    Object localObject = (JsonNode)readValue(paramReader, JSON_NODE_TYPE);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(String paramString)
    throws IOException, JsonProcessingException
  {
    Object localObject = (JsonNode)readValue(paramString, JSON_NODE_TYPE);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public JsonNode readTree(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    return readTree(paramJsonParser, copyDeserializationConfig());
  }

  public JsonNode readTree(JsonParser paramJsonParser, DeserializationConfig paramDeserializationConfig)
    throws IOException, JsonProcessingException
  {
    Object localObject = (JsonNode)_readValue(paramDeserializationConfig, paramJsonParser, JSON_NODE_TYPE);
    if (localObject == null)
      localObject = NullNode.instance;
    return (JsonNode)localObject;
  }

  public <T> T readValue(File paramFile, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramFile), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(File paramFile, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramFile), paramJavaType);
  }

  public <T> T readValue(File paramFile, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramFile), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(InputStream paramInputStream, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramInputStream), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(InputStream paramInputStream, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramInputStream), paramJavaType);
  }

  public <T> T readValue(InputStream paramInputStream, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramInputStream), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(Reader paramReader, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramReader), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(Reader paramReader, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramReader), paramJavaType);
  }

  public <T> T readValue(Reader paramReader, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramReader), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(String paramString, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramString), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(String paramString, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramString), paramJavaType);
  }

  public <T> T readValue(String paramString, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramString), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(URL paramURL, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramURL), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(URL paramURL, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramURL), paramJavaType);
  }

  public <T> T readValue(URL paramURL, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramURL), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(JsonNode paramJsonNode, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(copyDeserializationConfig(), treeAsTokens(paramJsonNode), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(JsonNode paramJsonNode, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(copyDeserializationConfig(), treeAsTokens(paramJsonNode), paramJavaType);
  }

  public <T> T readValue(JsonNode paramJsonNode, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(copyDeserializationConfig(), treeAsTokens(paramJsonNode), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(JsonParser paramJsonParser, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(copyDeserializationConfig(), paramJsonParser, this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(JsonParser paramJsonParser, Class<T> paramClass, DeserializationConfig paramDeserializationConfig)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(paramDeserializationConfig, paramJsonParser, this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(JsonParser paramJsonParser, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(copyDeserializationConfig(), paramJsonParser, paramJavaType);
  }

  public <T> T readValue(JsonParser paramJsonParser, JavaType paramJavaType, DeserializationConfig paramDeserializationConfig)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(paramDeserializationConfig, paramJsonParser, paramJavaType);
  }

  public <T> T readValue(JsonParser paramJsonParser, TypeReference<?> paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(copyDeserializationConfig(), paramJsonParser, this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(JsonParser paramJsonParser, TypeReference<?> paramTypeReference, DeserializationConfig paramDeserializationConfig)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readValue(paramDeserializationConfig, paramJsonParser, this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte, paramInt1, paramInt2), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte, paramInt1, paramInt2), paramJavaType);
  }

  public <T> T readValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte, paramInt1, paramInt2), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> T readValue(byte[] paramArrayOfByte, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte), this._typeFactory.constructType(paramClass));
  }

  public <T> T readValue(byte[] paramArrayOfByte, JavaType paramJavaType)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte), paramJavaType);
  }

  public <T> T readValue(byte[] paramArrayOfByte, TypeReference paramTypeReference)
    throws IOException, JsonParseException, JsonMappingException
  {
    return _readMapAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte), this._typeFactory.constructType(paramTypeReference));
  }

  public <T> MappingIterator<T> readValues(JsonParser paramJsonParser, Class<?> paramClass)
    throws IOException, JsonProcessingException
  {
    return readValues(paramJsonParser, this._typeFactory.constructType(paramClass));
  }

  public <T> MappingIterator<T> readValues(JsonParser paramJsonParser, JavaType paramJavaType)
    throws IOException, JsonProcessingException
  {
    DeserializationConfig localDeserializationConfig = copyDeserializationConfig();
    return new MappingIterator(paramJavaType, paramJsonParser, _createDeserializationContext(paramJsonParser, localDeserializationConfig), _findRootDeserializer(localDeserializationConfig, paramJavaType));
  }

  public <T> MappingIterator<T> readValues(JsonParser paramJsonParser, TypeReference<?> paramTypeReference)
    throws IOException, JsonProcessingException
  {
    return readValues(paramJsonParser, this._typeFactory.constructType(paramTypeReference));
  }

  public ObjectReader reader()
  {
    return new ObjectReader(this, copyDeserializationConfig());
  }

  public ObjectReader reader(Class<?> paramClass)
  {
    return reader(this._typeFactory.constructType(paramClass));
  }

  public ObjectReader reader(JsonNodeFactory paramJsonNodeFactory)
  {
    return new ObjectReader(this, copyDeserializationConfig()).withNodeFactory(paramJsonNodeFactory);
  }

  public ObjectReader reader(JavaType paramJavaType)
  {
    return new ObjectReader(this, copyDeserializationConfig(), paramJavaType, null, null);
  }

  public ObjectReader reader(TypeReference<?> paramTypeReference)
  {
    return reader(this._typeFactory.constructType(paramTypeReference));
  }

  public void registerModule(Module paramModule)
  {
    if (paramModule.getModuleName() == null)
      throw new IllegalArgumentException("Module without defined name");
    if (paramModule.version() == null)
      throw new IllegalArgumentException("Module without defined version");
    paramModule.setupModule(new Module.SetupContext(this)
    {
      public void addAbstractTypeResolver(AbstractTypeResolver paramAbstractTypeResolver)
      {
        this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withAbstractTypeResolver(paramAbstractTypeResolver);
      }

      public void addBeanDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier)
      {
        this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withDeserializerModifier(paramBeanDeserializerModifier);
      }

      public void addBeanSerializerModifier(BeanSerializerModifier paramBeanSerializerModifier)
      {
        this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withSerializerModifier(paramBeanSerializerModifier);
      }

      public void addDeserializers(Deserializers paramDeserializers)
      {
        this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withAdditionalDeserializers(paramDeserializers);
      }

      public void addKeyDeserializers(KeyDeserializers paramKeyDeserializers)
      {
        this.val$mapper._deserializerProvider = this.val$mapper._deserializerProvider.withAdditionalKeyDeserializers(paramKeyDeserializers);
      }

      public void addKeySerializers(Serializers paramSerializers)
      {
        this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withAdditionalKeySerializers(paramSerializers);
      }

      public void addSerializers(Serializers paramSerializers)
      {
        this.val$mapper._serializerFactory = this.val$mapper._serializerFactory.withAdditionalSerializers(paramSerializers);
      }

      public void addTypeModifier(TypeModifier paramTypeModifier)
      {
        TypeFactory localTypeFactory = this.val$mapper._typeFactory.withModifier(paramTypeModifier);
        this.val$mapper.setTypeFactory(localTypeFactory);
      }

      public void appendAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
      {
        this.val$mapper._deserializationConfig.appendAnnotationIntrospector(paramAnnotationIntrospector);
        this.val$mapper._serializationConfig.appendAnnotationIntrospector(paramAnnotationIntrospector);
      }

      public DeserializationConfig getDeserializationConfig()
      {
        return this.val$mapper.getDeserializationConfig();
      }

      public Version getMapperVersion()
      {
        return ObjectMapper.this.version();
      }

      public SerializationConfig getSerializationConfig()
      {
        return this.val$mapper.getSerializationConfig();
      }

      public void insertAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
      {
        this.val$mapper._deserializationConfig.insertAnnotationIntrospector(paramAnnotationIntrospector);
        this.val$mapper._serializationConfig.insertAnnotationIntrospector(paramAnnotationIntrospector);
      }

      public void setMixInAnnotations(Class<?> paramClass1, Class<?> paramClass2)
      {
        this.val$mapper._deserializationConfig.addMixInAnnotations(paramClass1, paramClass2);
        this.val$mapper._serializationConfig.addMixInAnnotations(paramClass1, paramClass2);
      }
    });
  }

  public void registerSubtypes(Class<?>[] paramArrayOfClass)
  {
    getSubtypeResolver().registerSubtypes(paramArrayOfClass);
  }

  public void registerSubtypes(NamedType[] paramArrayOfNamedType)
  {
    getSubtypeResolver().registerSubtypes(paramArrayOfNamedType);
  }

  public ObjectReader schemaBasedReader(FormatSchema paramFormatSchema)
  {
    return new ObjectReader(this, copyDeserializationConfig(), null, null, paramFormatSchema);
  }

  public ObjectWriter schemaBasedWriter(FormatSchema paramFormatSchema)
  {
    return new ObjectWriter(this, copySerializationConfig(), paramFormatSchema);
  }

  public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
  {
    this._serializationConfig = this._serializationConfig.withAnnotationIntrospector(paramAnnotationIntrospector);
    this._deserializationConfig = this._deserializationConfig.withAnnotationIntrospector(paramAnnotationIntrospector);
    return this;
  }

  public void setDateFormat(DateFormat paramDateFormat)
  {
    this._deserializationConfig = this._deserializationConfig.withDateFormat(paramDateFormat);
    this._serializationConfig = this._serializationConfig.withDateFormat(paramDateFormat);
  }

  public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> paramTypeResolverBuilder)
  {
    this._deserializationConfig = this._deserializationConfig.withTypeResolverBuilder(paramTypeResolverBuilder);
    this._serializationConfig = this._serializationConfig.withTypeResolverBuilder(paramTypeResolverBuilder);
    return this;
  }

  public ObjectMapper setDeserializationConfig(DeserializationConfig paramDeserializationConfig)
  {
    this._deserializationConfig = paramDeserializationConfig;
    return this;
  }

  public ObjectMapper setDeserializerProvider(DeserializerProvider paramDeserializerProvider)
  {
    this._deserializerProvider = paramDeserializerProvider;
    return this;
  }

  public void setFilters(FilterProvider paramFilterProvider)
  {
    this._serializationConfig = this._serializationConfig.withFilters(paramFilterProvider);
  }

  public void setHandlerInstantiator(HandlerInstantiator paramHandlerInstantiator)
  {
    this._deserializationConfig = this._deserializationConfig.withHandlerInstantiator(paramHandlerInstantiator);
    this._serializationConfig = this._serializationConfig.withHandlerInstantiator(paramHandlerInstantiator);
  }

  public ObjectMapper setNodeFactory(JsonNodeFactory paramJsonNodeFactory)
  {
    this._deserializationConfig = this._deserializationConfig.withNodeFactory(paramJsonNodeFactory);
    return this;
  }

  public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy paramPropertyNamingStrategy)
  {
    this._serializationConfig = this._serializationConfig.withPropertyNamingStrategy(paramPropertyNamingStrategy);
    this._deserializationConfig = this._deserializationConfig.withPropertyNamingStrategy(paramPropertyNamingStrategy);
    return this;
  }

  public ObjectMapper setSerializationConfig(SerializationConfig paramSerializationConfig)
  {
    this._serializationConfig = paramSerializationConfig;
    return this;
  }

  public ObjectMapper setSerializerFactory(SerializerFactory paramSerializerFactory)
  {
    this._serializerFactory = paramSerializerFactory;
    return this;
  }

  public ObjectMapper setSerializerProvider(SerializerProvider paramSerializerProvider)
  {
    this._serializerProvider = paramSerializerProvider;
    return this;
  }

  public void setSubtypeResolver(SubtypeResolver paramSubtypeResolver)
  {
    this._subtypeResolver = paramSubtypeResolver;
  }

  public ObjectMapper setTypeFactory(TypeFactory paramTypeFactory)
  {
    this._typeFactory = paramTypeFactory;
    this._deserializationConfig = this._deserializationConfig.withTypeFactory(paramTypeFactory);
    this._serializationConfig = this._serializationConfig.withTypeFactory(paramTypeFactory);
    return this;
  }

  public void setVisibilityChecker(VisibilityChecker<?> paramVisibilityChecker)
  {
    this._deserializationConfig = this._deserializationConfig.withVisibilityChecker(paramVisibilityChecker);
    this._serializationConfig = this._serializationConfig.withVisibilityChecker(paramVisibilityChecker);
  }

  public JsonParser treeAsTokens(JsonNode paramJsonNode)
  {
    return new TreeTraversingParser(paramJsonNode, this);
  }

  public <T> T treeToValue(JsonNode paramJsonNode, Class<T> paramClass)
    throws IOException, JsonParseException, JsonMappingException
  {
    return readValue(treeAsTokens(paramJsonNode), paramClass);
  }

  public ObjectWriter typedWriter(Class<?> paramClass)
  {
    if (paramClass == null);
    for (JavaType localJavaType = null; ; localJavaType = this._typeFactory.constructType(paramClass))
      return new ObjectWriter(this, copySerializationConfig(), localJavaType, null);
  }

  public ObjectWriter typedWriter(JavaType paramJavaType)
  {
    return new ObjectWriter(this, copySerializationConfig(), paramJavaType, null);
  }

  public ObjectWriter typedWriter(TypeReference<?> paramTypeReference)
  {
    if (paramTypeReference == null);
    for (JavaType localJavaType = null; ; localJavaType = this._typeFactory.constructType(paramTypeReference))
      return new ObjectWriter(this, copySerializationConfig(), localJavaType, null);
  }

  public ObjectReader updatingReader(Object paramObject)
  {
    JavaType localJavaType = this._typeFactory.constructType(paramObject.getClass());
    return new ObjectReader(this, copyDeserializationConfig(), localJavaType, paramObject, null);
  }

  public <T extends JsonNode> T valueToTree(Object paramObject)
    throws IllegalArgumentException
  {
    if (paramObject == null)
      return null;
    TokenBuffer localTokenBuffer = new TokenBuffer(this);
    try
    {
      writeValue(localTokenBuffer, paramObject);
      JsonParser localJsonParser = localTokenBuffer.asParser();
      JsonNode localJsonNode = readTree(localJsonParser);
      localJsonParser.close();
      return localJsonNode;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException(localIOException.getMessage(), localIOException);
  }

  public Version version()
  {
    return VersionUtil.versionFor(getClass());
  }

  public ObjectWriter viewWriter(Class<?> paramClass)
  {
    return new ObjectWriter(this, copySerializationConfig().withView(paramClass));
  }

  public ObjectMapper withModule(Module paramModule)
  {
    registerModule(paramModule);
    return this;
  }

  public void writeTree(JsonGenerator paramJsonGenerator, JsonNode paramJsonNode)
    throws IOException, JsonProcessingException
  {
    SerializationConfig localSerializationConfig = copySerializationConfig();
    this._serializerProvider.serializeValue(localSerializationConfig, paramJsonGenerator, paramJsonNode, this._serializerFactory);
    if (localSerializationConfig.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
      paramJsonGenerator.flush();
  }

  public void writeTree(JsonGenerator paramJsonGenerator, JsonNode paramJsonNode, SerializationConfig paramSerializationConfig)
    throws IOException, JsonProcessingException
  {
    this._serializerProvider.serializeValue(paramSerializationConfig, paramJsonGenerator, paramJsonNode, this._serializerFactory);
    if (paramSerializationConfig.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE))
      paramJsonGenerator.flush();
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
    SerializationConfig localSerializationConfig = copySerializationConfig();
    if ((localSerializationConfig.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((paramObject instanceof Closeable)))
      _writeCloseableValue(paramJsonGenerator, paramObject, localSerializationConfig);
    do
    {
      return;
      this._serializerProvider.serializeValue(localSerializationConfig, paramJsonGenerator, paramObject, this._serializerFactory);
    }
    while (!localSerializationConfig.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE));
    paramJsonGenerator.flush();
  }

  public void writeValue(JsonGenerator paramJsonGenerator, Object paramObject, SerializationConfig paramSerializationConfig)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    if ((paramSerializationConfig.isEnabled(SerializationConfig.Feature.CLOSE_CLOSEABLE)) && ((paramObject instanceof Closeable)))
      _writeCloseableValue(paramJsonGenerator, paramObject, paramSerializationConfig);
    do
    {
      return;
      this._serializerProvider.serializeValue(paramSerializationConfig, paramJsonGenerator, paramObject, this._serializerFactory);
    }
    while (!paramSerializationConfig.isEnabled(SerializationConfig.Feature.FLUSH_AFTER_WRITE_VALUE));
    paramJsonGenerator.flush();
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

  @Deprecated
  public void writeValueUsingView(OutputStream paramOutputStream, Object paramObject, Class<?> paramClass)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    _configAndWriteValue(this._jsonFactory.createJsonGenerator(paramOutputStream, JsonEncoding.UTF8), paramObject, paramClass);
  }

  @Deprecated
  public void writeValueUsingView(Writer paramWriter, Object paramObject, Class<?> paramClass)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    _configAndWriteValue(this._jsonFactory.createJsonGenerator(paramWriter), paramObject, paramClass);
  }

  @Deprecated
  public void writeValueUsingView(JsonGenerator paramJsonGenerator, Object paramObject, Class<?> paramClass)
    throws IOException, JsonGenerationException, JsonMappingException
  {
    _configAndWriteValue(paramJsonGenerator, paramObject, paramClass);
  }

  public ObjectWriter writer()
  {
    return new ObjectWriter(this, copySerializationConfig());
  }

  public static class DefaultTypeResolverBuilder extends StdTypeResolverBuilder
  {
    protected final ObjectMapper.DefaultTyping _appliesFor;

    public DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping paramDefaultTyping)
    {
      this._appliesFor = paramDefaultTyping;
    }

    public TypeDeserializer buildTypeDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, Collection<NamedType> paramCollection, BeanProperty paramBeanProperty)
    {
      if (useForType(paramJavaType))
        return super.buildTypeDeserializer(paramDeserializationConfig, paramJavaType, paramCollection, paramBeanProperty);
      return null;
    }

    public TypeSerializer buildTypeSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, Collection<NamedType> paramCollection, BeanProperty paramBeanProperty)
    {
      if (useForType(paramJavaType))
        return super.buildTypeSerializer(paramSerializationConfig, paramJavaType, paramCollection, paramBeanProperty);
      return null;
    }

    public boolean useForType(JavaType paramJavaType)
    {
      switch (ObjectMapper.2.$SwitchMap$org$codehaus$jackson$map$ObjectMapper$DefaultTyping[this._appliesFor.ordinal()])
      {
      default:
        if (paramJavaType.getRawClass() != Object.class)
          break;
      case 1:
      case 2:
      case 3:
        do
        {
          return true;
          if (paramJavaType.isArrayType())
            paramJavaType = paramJavaType.getContentType();
          int i;
          if (paramJavaType.getRawClass() != Object.class)
          {
            boolean bool = paramJavaType.isConcrete();
            i = 0;
            if (bool);
          }
          else
          {
            i = 1;
          }
          return i;
          if (!paramJavaType.isArrayType())
            continue;
          paramJavaType = paramJavaType.getContentType();
        }
        while (!paramJavaType.isFinal());
        return false;
      }
      return false;
    }
  }

  public static enum DefaultTyping
  {
    static
    {
      NON_CONCRETE_AND_ARRAYS = new DefaultTyping("NON_CONCRETE_AND_ARRAYS", 2);
      NON_FINAL = new DefaultTyping("NON_FINAL", 3);
      DefaultTyping[] arrayOfDefaultTyping = new DefaultTyping[4];
      arrayOfDefaultTyping[0] = JAVA_LANG_OBJECT;
      arrayOfDefaultTyping[1] = OBJECT_AND_NON_CONCRETE;
      arrayOfDefaultTyping[2] = NON_CONCRETE_AND_ARRAYS;
      arrayOfDefaultTyping[3] = NON_FINAL;
      $VALUES = arrayOfDefaultTyping;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ObjectMapper
 * JD-Core Version:    0.6.0
 */