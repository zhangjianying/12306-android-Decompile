package org.codehaus.jackson.map;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.FormatSchema;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.map.deser.StdDeserializationContext;
import org.codehaus.jackson.map.type.SimpleType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.TreeTraversingParser;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.VersionUtil;

public class ObjectReader extends ObjectCodec
  implements Versioned
{
  private static final JavaType JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
  protected final DeserializationConfig _config;
  protected final JsonFactory _jsonFactory;
  protected final DeserializerProvider _provider;
  protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
  protected final FormatSchema _schema;
  protected final Object _valueToUpdate;
  protected final JavaType _valueType;

  protected ObjectReader(ObjectMapper paramObjectMapper, DeserializationConfig paramDeserializationConfig)
  {
    this(paramObjectMapper, paramDeserializationConfig, null, null, null);
  }

  protected ObjectReader(ObjectMapper paramObjectMapper, DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, Object paramObject, FormatSchema paramFormatSchema)
  {
    this._config = paramDeserializationConfig;
    this._rootDeserializers = paramObjectMapper._rootDeserializers;
    this._provider = paramObjectMapper._deserializerProvider;
    this._jsonFactory = paramObjectMapper._jsonFactory;
    this._valueType = paramJavaType;
    this._valueToUpdate = paramObject;
    if ((paramObject != null) && (paramJavaType.isArrayType()))
      throw new IllegalArgumentException("Can not update an array value");
    this._schema = paramFormatSchema;
  }

  protected ObjectReader(ObjectReader paramObjectReader, DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, Object paramObject, FormatSchema paramFormatSchema)
  {
    this._config = paramDeserializationConfig;
    this._rootDeserializers = paramObjectReader._rootDeserializers;
    this._provider = paramObjectReader._provider;
    this._jsonFactory = paramObjectReader._jsonFactory;
    this._valueType = paramJavaType;
    this._valueToUpdate = paramObject;
    if ((paramObject != null) && (paramJavaType.isArrayType()))
      throw new IllegalArgumentException("Can not update an array value");
    this._schema = paramFormatSchema;
  }

  protected static JsonToken _initForReading(JsonParser paramJsonParser)
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

  protected Object _bind(JsonParser paramJsonParser)
    throws IOException, JsonParseException, JsonMappingException
  {
    JsonToken localJsonToken = _initForReading(paramJsonParser);
    Object localObject;
    if ((localJsonToken == JsonToken.VALUE_NULL) || (localJsonToken == JsonToken.END_ARRAY) || (localJsonToken == JsonToken.END_OBJECT))
      localObject = this._valueToUpdate;
    while (true)
    {
      paramJsonParser.clearCurrentToken();
      return localObject;
      DeserializationContext localDeserializationContext = _createDeserializationContext(paramJsonParser, this._config);
      if (this._valueToUpdate == null)
      {
        localObject = _findRootDeserializer(this._config, this._valueType).deserialize(paramJsonParser, localDeserializationContext);
        continue;
      }
      _findRootDeserializer(this._config, this._valueType).deserialize(paramJsonParser, localDeserializationContext, this._valueToUpdate);
      localObject = this._valueToUpdate;
    }
  }

  // ERROR //
  protected Object _bindAndClose(JsonParser paramJsonParser)
    throws IOException, JsonParseException, JsonMappingException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 75	org/codehaus/jackson/map/ObjectReader:_schema	Lorg/codehaus/jackson/FormatSchema;
    //   4: ifnull +11 -> 15
    //   7: aload_1
    //   8: aload_0
    //   9: getfield 75	org/codehaus/jackson/map/ObjectReader:_schema	Lorg/codehaus/jackson/FormatSchema;
    //   12: invokevirtual 139	org/codehaus/jackson/JsonParser:setSchema	(Lorg/codehaus/jackson/FormatSchema;)V
    //   15: aload_1
    //   16: invokestatic 102	org/codehaus/jackson/map/ObjectReader:_initForReading	(Lorg/codehaus/jackson/JsonParser;)Lorg/codehaus/jackson/JsonToken;
    //   19: astore 4
    //   21: aload 4
    //   23: getstatic 108	org/codehaus/jackson/JsonToken:VALUE_NULL	Lorg/codehaus/jackson/JsonToken;
    //   26: if_acmpeq +19 -> 45
    //   29: aload 4
    //   31: getstatic 111	org/codehaus/jackson/JsonToken:END_ARRAY	Lorg/codehaus/jackson/JsonToken;
    //   34: if_acmpeq +11 -> 45
    //   37: aload 4
    //   39: getstatic 114	org/codehaus/jackson/JsonToken:END_OBJECT	Lorg/codehaus/jackson/JsonToken;
    //   42: if_acmpne +16 -> 58
    //   45: aload_0
    //   46: getfield 60	org/codehaus/jackson/map/ObjectReader:_valueToUpdate	Ljava/lang/Object;
    //   49: astore 5
    //   51: aload_1
    //   52: invokevirtual 142	org/codehaus/jackson/JsonParser:close	()V
    //   55: aload 5
    //   57: areturn
    //   58: aload_0
    //   59: aload_1
    //   60: aload_0
    //   61: getfield 43	org/codehaus/jackson/map/ObjectReader:_config	Lorg/codehaus/jackson/map/DeserializationConfig;
    //   64: invokevirtual 121	org/codehaus/jackson/map/ObjectReader:_createDeserializationContext	(Lorg/codehaus/jackson/JsonParser;Lorg/codehaus/jackson/map/DeserializationConfig;)Lorg/codehaus/jackson/map/DeserializationContext;
    //   67: astore 7
    //   69: aload_0
    //   70: getfield 60	org/codehaus/jackson/map/ObjectReader:_valueToUpdate	Ljava/lang/Object;
    //   73: ifnonnull +26 -> 99
    //   76: aload_0
    //   77: aload_0
    //   78: getfield 43	org/codehaus/jackson/map/ObjectReader:_config	Lorg/codehaus/jackson/map/DeserializationConfig;
    //   81: aload_0
    //   82: getfield 58	org/codehaus/jackson/map/ObjectReader:_valueType	Lorg/codehaus/jackson/type/JavaType;
    //   85: invokevirtual 125	org/codehaus/jackson/map/ObjectReader:_findRootDeserializer	(Lorg/codehaus/jackson/map/DeserializationConfig;Lorg/codehaus/jackson/type/JavaType;)Lorg/codehaus/jackson/map/JsonDeserializer;
    //   88: aload_1
    //   89: aload 7
    //   91: invokevirtual 131	org/codehaus/jackson/map/JsonDeserializer:deserialize	(Lorg/codehaus/jackson/JsonParser;Lorg/codehaus/jackson/map/DeserializationContext;)Ljava/lang/Object;
    //   94: astore 5
    //   96: goto -45 -> 51
    //   99: aload_0
    //   100: aload_0
    //   101: getfield 43	org/codehaus/jackson/map/ObjectReader:_config	Lorg/codehaus/jackson/map/DeserializationConfig;
    //   104: aload_0
    //   105: getfield 58	org/codehaus/jackson/map/ObjectReader:_valueType	Lorg/codehaus/jackson/type/JavaType;
    //   108: invokevirtual 125	org/codehaus/jackson/map/ObjectReader:_findRootDeserializer	(Lorg/codehaus/jackson/map/DeserializationConfig;Lorg/codehaus/jackson/type/JavaType;)Lorg/codehaus/jackson/map/JsonDeserializer;
    //   111: aload_1
    //   112: aload 7
    //   114: aload_0
    //   115: getfield 60	org/codehaus/jackson/map/ObjectReader:_valueToUpdate	Ljava/lang/Object;
    //   118: invokevirtual 134	org/codehaus/jackson/map/JsonDeserializer:deserialize	(Lorg/codehaus/jackson/JsonParser;Lorg/codehaus/jackson/map/DeserializationContext;Ljava/lang/Object;)Ljava/lang/Object;
    //   121: pop
    //   122: aload_0
    //   123: getfield 60	org/codehaus/jackson/map/ObjectReader:_valueToUpdate	Ljava/lang/Object;
    //   126: astore 5
    //   128: goto -77 -> 51
    //   131: astore_2
    //   132: aload_1
    //   133: invokevirtual 142	org/codehaus/jackson/JsonParser:close	()V
    //   136: aload_2
    //   137: athrow
    //   138: astore 6
    //   140: aload 5
    //   142: areturn
    //   143: astore_3
    //   144: goto -8 -> 136
    //
    // Exception table:
    //   from	to	target	type
    //   15	45	131	finally
    //   45	51	131	finally
    //   58	96	131	finally
    //   99	128	131	finally
    //   51	55	138	java/io/IOException
    //   132	136	143	java/io/IOException
  }

  // ERROR //
  protected JsonNode _bindAndCloseAsTree(JsonParser paramJsonParser)
    throws IOException, JsonParseException, JsonMappingException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 75	org/codehaus/jackson/map/ObjectReader:_schema	Lorg/codehaus/jackson/FormatSchema;
    //   4: ifnull +11 -> 15
    //   7: aload_1
    //   8: aload_0
    //   9: getfield 75	org/codehaus/jackson/map/ObjectReader:_schema	Lorg/codehaus/jackson/FormatSchema;
    //   12: invokevirtual 139	org/codehaus/jackson/JsonParser:setSchema	(Lorg/codehaus/jackson/FormatSchema;)V
    //   15: aload_0
    //   16: aload_1
    //   17: invokevirtual 147	org/codehaus/jackson/map/ObjectReader:_bindAsTree	(Lorg/codehaus/jackson/JsonParser;)Lorg/codehaus/jackson/JsonNode;
    //   20: astore 4
    //   22: aload_1
    //   23: invokevirtual 142	org/codehaus/jackson/JsonParser:close	()V
    //   26: aload 4
    //   28: areturn
    //   29: astore_2
    //   30: aload_1
    //   31: invokevirtual 142	org/codehaus/jackson/JsonParser:close	()V
    //   34: aload_2
    //   35: athrow
    //   36: astore 5
    //   38: aload 4
    //   40: areturn
    //   41: astore_3
    //   42: goto -8 -> 34
    //
    // Exception table:
    //   from	to	target	type
    //   15	22	29	finally
    //   22	26	36	java/io/IOException
    //   30	34	41	java/io/IOException
  }

  protected JsonNode _bindAsTree(JsonParser paramJsonParser)
    throws IOException, JsonParseException, JsonMappingException
  {
    JsonToken localJsonToken = _initForReading(paramJsonParser);
    if ((localJsonToken == JsonToken.VALUE_NULL) || (localJsonToken == JsonToken.END_ARRAY) || (localJsonToken == JsonToken.END_OBJECT));
    DeserializationContext localDeserializationContext;
    for (Object localObject = NullNode.instance; ; localObject = (JsonNode)_findRootDeserializer(this._config, JSON_NODE_TYPE).deserialize(paramJsonParser, localDeserializationContext))
    {
      paramJsonParser.clearCurrentToken();
      return localObject;
      localDeserializationContext = _createDeserializationContext(paramJsonParser, this._config);
    }
  }

  protected DeserializationContext _createDeserializationContext(JsonParser paramJsonParser, DeserializationConfig paramDeserializationConfig)
  {
    return new StdDeserializationContext(paramDeserializationConfig, paramJsonParser, this._provider);
  }

  protected JsonDeserializer<Object> _findRootDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
    throws JsonMappingException
  {
    JsonDeserializer localJsonDeserializer1 = (JsonDeserializer)this._rootDeserializers.get(paramJavaType);
    if (localJsonDeserializer1 != null)
      return localJsonDeserializer1;
    JsonDeserializer localJsonDeserializer2 = this._provider.findTypedValueDeserializer(paramDeserializationConfig, paramJavaType, null);
    if (localJsonDeserializer2 == null)
      throw new JsonMappingException("Can not find a deserializer for type " + paramJavaType);
    this._rootDeserializers.put(paramJavaType, localJsonDeserializer2);
    return localJsonDeserializer2;
  }

  public JsonNode createArrayNode()
  {
    return this._config.getNodeFactory().arrayNode();
  }

  public JsonNode createObjectNode()
  {
    return this._config.getNodeFactory().objectNode();
  }

  public JsonNode readTree(InputStream paramInputStream)
    throws IOException, JsonProcessingException
  {
    return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(paramInputStream));
  }

  public JsonNode readTree(Reader paramReader)
    throws IOException, JsonProcessingException
  {
    return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(paramReader));
  }

  public JsonNode readTree(String paramString)
    throws IOException, JsonProcessingException
  {
    return _bindAndCloseAsTree(this._jsonFactory.createJsonParser(paramString));
  }

  public JsonNode readTree(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    return _bindAsTree(paramJsonParser);
  }

  public <T> T readValue(File paramFile)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(this._jsonFactory.createJsonParser(paramFile));
  }

  public <T> T readValue(InputStream paramInputStream)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(this._jsonFactory.createJsonParser(paramInputStream));
  }

  public <T> T readValue(Reader paramReader)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(this._jsonFactory.createJsonParser(paramReader));
  }

  public <T> T readValue(String paramString)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(this._jsonFactory.createJsonParser(paramString));
  }

  public <T> T readValue(URL paramURL)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(this._jsonFactory.createJsonParser(paramURL));
  }

  public <T> T readValue(JsonNode paramJsonNode)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(treeAsTokens(paramJsonNode));
  }

  public <T> T readValue(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    return _bind(paramJsonParser);
  }

  public <T> T readValue(JsonParser paramJsonParser, Class<T> paramClass)
    throws IOException, JsonProcessingException
  {
    return withType(paramClass).readValue(paramJsonParser);
  }

  public <T> T readValue(JsonParser paramJsonParser, JavaType paramJavaType)
    throws IOException, JsonProcessingException
  {
    return withType(paramJavaType).readValue(paramJsonParser);
  }

  public <T> T readValue(JsonParser paramJsonParser, TypeReference<?> paramTypeReference)
    throws IOException, JsonProcessingException
  {
    return withType(paramTypeReference).readValue(paramJsonParser);
  }

  public <T> T readValue(byte[] paramArrayOfByte)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte));
  }

  public <T> T readValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonProcessingException
  {
    return _bindAndClose(this._jsonFactory.createJsonParser(paramArrayOfByte, paramInt1, paramInt2));
  }

  public <T> MappingIterator<T> readValues(File paramFile)
    throws IOException, JsonProcessingException
  {
    JsonParser localJsonParser = this._jsonFactory.createJsonParser(paramFile);
    if (this._schema != null)
      localJsonParser.setSchema(this._schema);
    DeserializationContext localDeserializationContext = _createDeserializationContext(localJsonParser, this._config);
    return new MappingIterator(this._valueType, localJsonParser, localDeserializationContext, _findRootDeserializer(this._config, this._valueType));
  }

  public <T> MappingIterator<T> readValues(InputStream paramInputStream)
    throws IOException, JsonProcessingException
  {
    JsonParser localJsonParser = this._jsonFactory.createJsonParser(paramInputStream);
    if (this._schema != null)
      localJsonParser.setSchema(this._schema);
    DeserializationContext localDeserializationContext = _createDeserializationContext(localJsonParser, this._config);
    return new MappingIterator(this._valueType, localJsonParser, localDeserializationContext, _findRootDeserializer(this._config, this._valueType));
  }

  public <T> MappingIterator<T> readValues(Reader paramReader)
    throws IOException, JsonProcessingException
  {
    JsonParser localJsonParser = this._jsonFactory.createJsonParser(paramReader);
    if (this._schema != null)
      localJsonParser.setSchema(this._schema);
    DeserializationContext localDeserializationContext = _createDeserializationContext(localJsonParser, this._config);
    return new MappingIterator(this._valueType, localJsonParser, localDeserializationContext, _findRootDeserializer(this._config, this._valueType));
  }

  public <T> MappingIterator<T> readValues(String paramString)
    throws IOException, JsonProcessingException
  {
    JsonParser localJsonParser = this._jsonFactory.createJsonParser(paramString);
    if (this._schema != null)
      localJsonParser.setSchema(this._schema);
    DeserializationContext localDeserializationContext = _createDeserializationContext(localJsonParser, this._config);
    return new MappingIterator(this._valueType, localJsonParser, localDeserializationContext, _findRootDeserializer(this._config, this._valueType));
  }

  public <T> MappingIterator<T> readValues(URL paramURL)
    throws IOException, JsonProcessingException
  {
    JsonParser localJsonParser = this._jsonFactory.createJsonParser(paramURL);
    if (this._schema != null)
      localJsonParser.setSchema(this._schema);
    DeserializationContext localDeserializationContext = _createDeserializationContext(localJsonParser, this._config);
    return new MappingIterator(this._valueType, localJsonParser, localDeserializationContext, _findRootDeserializer(this._config, this._valueType));
  }

  public <T> MappingIterator<T> readValues(JsonParser paramJsonParser)
    throws IOException, JsonProcessingException
  {
    DeserializationContext localDeserializationContext = _createDeserializationContext(paramJsonParser, this._config);
    return new MappingIterator(this._valueType, paramJsonParser, localDeserializationContext, _findRootDeserializer(this._config, this._valueType));
  }

  public <T> MappingIterator<T> readValues(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException, JsonProcessingException
  {
    JsonParser localJsonParser = this._jsonFactory.createJsonParser(paramArrayOfByte, paramInt1, paramInt2);
    if (this._schema != null)
      localJsonParser.setSchema(this._schema);
    DeserializationContext localDeserializationContext = _createDeserializationContext(localJsonParser, this._config);
    return new MappingIterator(this._valueType, localJsonParser, localDeserializationContext, _findRootDeserializer(this._config, this._valueType));
  }

  public JsonParser treeAsTokens(JsonNode paramJsonNode)
  {
    return new TreeTraversingParser(paramJsonNode, this);
  }

  public <T> T treeToValue(JsonNode paramJsonNode, Class<T> paramClass)
    throws IOException, JsonProcessingException
  {
    return readValue(treeAsTokens(paramJsonNode), paramClass);
  }

  public Version version()
  {
    return VersionUtil.versionFor(getClass());
  }

  public ObjectReader withNodeFactory(JsonNodeFactory paramJsonNodeFactory)
  {
    if (paramJsonNodeFactory == this._config.getNodeFactory())
      return this;
    return new ObjectReader(this, this._config.withNodeFactory(paramJsonNodeFactory), this._valueType, this._valueToUpdate, this._schema);
  }

  public ObjectReader withSchema(FormatSchema paramFormatSchema)
  {
    if (this._schema == paramFormatSchema)
      return this;
    return new ObjectReader(this, this._config, this._valueType, this._valueToUpdate, paramFormatSchema);
  }

  public ObjectReader withType(Class<?> paramClass)
  {
    return withType(this._config.constructType(paramClass));
  }

  public ObjectReader withType(Type paramType)
  {
    return withType(this._config.getTypeFactory().constructType(paramType));
  }

  public ObjectReader withType(JavaType paramJavaType)
  {
    if (paramJavaType == this._valueType)
      return this;
    return new ObjectReader(this, this._config, paramJavaType, this._valueToUpdate, this._schema);
  }

  public ObjectReader withType(TypeReference<?> paramTypeReference)
  {
    return withType(this._config.getTypeFactory().constructType(paramTypeReference.getType()));
  }

  public ObjectReader withValueToUpdate(Object paramObject)
  {
    if (paramObject == this._valueToUpdate)
      return this;
    if (paramObject == null)
      throw new IllegalArgumentException("cat not update null value");
    JavaType localJavaType = this._config.constructType(paramObject.getClass());
    return new ObjectReader(this, this._config, localJavaType, paramObject, this._schema);
  }

  public void writeTree(JsonGenerator paramJsonGenerator, JsonNode paramJsonNode)
    throws IOException, JsonProcessingException
  {
    throw new UnsupportedOperationException("Not implemented for ObjectReader");
  }

  public void writeValue(JsonGenerator paramJsonGenerator, Object paramObject)
    throws IOException, JsonProcessingException
  {
    throw new UnsupportedOperationException("Not implemented for ObjectReader");
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ObjectReader
 * JD-Core Version:    0.6.0
 */