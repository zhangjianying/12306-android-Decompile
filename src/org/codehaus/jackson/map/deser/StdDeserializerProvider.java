package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualDeserializer;
import org.codehaus.jackson.map.ContextualKeyDeserializer;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonDeserializer<Ljava.lang.Object;>;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class StdDeserializerProvider extends DeserializerProvider
{
  static final HashMap<JavaType, KeyDeserializer> _keyDeserializers = StdKeyDeserializers.constructAll();
  protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers = new ConcurrentHashMap(64, 0.75F, 2);
  protected DeserializerFactory _factory;
  protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers = new HashMap(8);

  public StdDeserializerProvider()
  {
    this(BeanDeserializerFactory.instance);
  }

  public StdDeserializerProvider(DeserializerFactory paramDeserializerFactory)
  {
    this._factory = paramDeserializerFactory;
  }

  protected JsonDeserializer<Object> _createAndCache2(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    JsonDeserializer localJsonDeserializer2;
    try
    {
      JsonDeserializer localJsonDeserializer1 = _createDeserializer(paramDeserializationConfig, paramJavaType, paramBeanProperty);
      localJsonDeserializer2 = localJsonDeserializer1;
      if (localJsonDeserializer2 == null)
      {
        localJsonDeserializer2 = null;
        return localJsonDeserializer2;
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new JsonMappingException(localIllegalArgumentException.getMessage(), null, localIllegalArgumentException);
    }
    boolean bool1 = localJsonDeserializer2 instanceof ResolvableDeserializer;
    if (localJsonDeserializer2.getClass() == BeanDeserializer.class);
    for (boolean bool2 = true; ; bool2 = false)
    {
      if ((!bool2) && (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.USE_ANNOTATIONS)))
      {
        AnnotationIntrospector localAnnotationIntrospector = paramDeserializationConfig.getAnnotationIntrospector();
        Boolean localBoolean = localAnnotationIntrospector.findCachability(AnnotatedClass.construct(localJsonDeserializer2.getClass(), localAnnotationIntrospector, null));
        if (localBoolean != null)
          bool2 = localBoolean.booleanValue();
      }
      if (bool1)
      {
        this._incompleteDeserializers.put(paramJavaType, localJsonDeserializer2);
        _resolveDeserializer(paramDeserializationConfig, (ResolvableDeserializer)localJsonDeserializer2);
        this._incompleteDeserializers.remove(paramJavaType);
      }
      if (!bool2)
        break;
      this._cachedDeserializers.put(paramJavaType, localJsonDeserializer2);
      return localJsonDeserializer2;
    }
  }

  protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    int i;
    synchronized (this._incompleteDeserializers)
    {
      JsonDeserializer localJsonDeserializer1 = _findCachedDeserializer(paramJavaType);
      if (localJsonDeserializer1 != null)
        return localJsonDeserializer1;
      i = this._incompleteDeserializers.size();
      if (i > 0)
      {
        JsonDeserializer localJsonDeserializer2 = (JsonDeserializer)this._incompleteDeserializers.get(paramJavaType);
        if (localJsonDeserializer2 != null)
          return localJsonDeserializer2;
      }
    }
    try
    {
      JsonDeserializer localJsonDeserializer3 = _createAndCache2(paramDeserializationConfig, paramJavaType, paramBeanProperty);
      if ((i == 0) && (this._incompleteDeserializers.size() > 0))
        this._incompleteDeserializers.clear();
      monitorexit;
      return localJsonDeserializer3;
      localObject1 = finally;
      monitorexit;
      throw localObject1;
    }
    finally
    {
      if ((i != 0) || (this._incompleteDeserializers.size() <= 0));
    }
    throw localObject2;
  }

  protected JsonDeserializer<Object> _createDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    if (paramJavaType.isEnumType())
      return this._factory.createEnumDeserializer(paramDeserializationConfig, this, paramJavaType, paramBeanProperty);
    if (paramJavaType.isContainerType())
    {
      if (paramJavaType.isArrayType())
        return this._factory.createArrayDeserializer(paramDeserializationConfig, this, (ArrayType)paramJavaType, paramBeanProperty);
      if (paramJavaType.isMapLikeType())
      {
        MapLikeType localMapLikeType = (MapLikeType)paramJavaType;
        if (localMapLikeType.isTrueMapType())
          return this._factory.createMapDeserializer(paramDeserializationConfig, this, (MapType)localMapLikeType, paramBeanProperty);
        return this._factory.createMapLikeDeserializer(paramDeserializationConfig, this, localMapLikeType, paramBeanProperty);
      }
      if (paramJavaType.isCollectionLikeType())
      {
        CollectionLikeType localCollectionLikeType = (CollectionLikeType)paramJavaType;
        if (localCollectionLikeType.isTrueCollectionType())
          return this._factory.createCollectionDeserializer(paramDeserializationConfig, this, (CollectionType)localCollectionLikeType, paramBeanProperty);
        return this._factory.createCollectionLikeDeserializer(paramDeserializationConfig, this, localCollectionLikeType, paramBeanProperty);
      }
    }
    if (JsonNode.class.isAssignableFrom(paramJavaType.getRawClass()))
      return this._factory.createTreeDeserializer(paramDeserializationConfig, this, paramJavaType, paramBeanProperty);
    return this._factory.createBeanDeserializer(paramDeserializationConfig, this, paramJavaType, paramBeanProperty);
  }

  protected JsonDeserializer<Object> _findCachedDeserializer(JavaType paramJavaType)
  {
    return (JsonDeserializer)this._cachedDeserializers.get(paramJavaType);
  }

  protected KeyDeserializer _handleUnknownKeyDeserializer(JavaType paramJavaType)
    throws JsonMappingException
  {
    throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + paramJavaType);
  }

  protected JsonDeserializer<Object> _handleUnknownValueDeserializer(JavaType paramJavaType)
    throws JsonMappingException
  {
    if (!ClassUtil.isConcrete(paramJavaType.getRawClass()))
      throw new JsonMappingException("Can not find a Value deserializer for abstract type " + paramJavaType);
    throw new JsonMappingException("Can not find a Value deserializer for type " + paramJavaType);
  }

  protected void _resolveDeserializer(DeserializationConfig paramDeserializationConfig, ResolvableDeserializer paramResolvableDeserializer)
    throws JsonMappingException
  {
    paramResolvableDeserializer.resolve(paramDeserializationConfig, this);
  }

  public int cachedDeserializersCount()
  {
    return this._cachedDeserializers.size();
  }

  public KeyDeserializer findKeyDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    KeyDeserializer localKeyDeserializer1 = this._factory.createKeyDeserializer(paramDeserializationConfig, paramJavaType, paramBeanProperty);
    if (localKeyDeserializer1 == null)
    {
      Class localClass = paramJavaType.getRawClass();
      KeyDeserializer localKeyDeserializer2;
      if ((localClass == String.class) || (localClass == Object.class))
        localKeyDeserializer2 = null;
      do
      {
        do
        {
          return localKeyDeserializer2;
          localKeyDeserializer2 = (KeyDeserializer)_keyDeserializers.get(paramJavaType);
        }
        while (localKeyDeserializer2 != null);
        if (paramJavaType.isEnumType())
          return StdKeyDeserializers.constructEnumKeyDeserializer(paramDeserializationConfig, paramJavaType);
        localKeyDeserializer2 = StdKeyDeserializers.findStringBasedKeyDeserializer(paramDeserializationConfig, paramJavaType);
      }
      while (localKeyDeserializer2 != null);
      if (localKeyDeserializer1 == null)
        return _handleUnknownKeyDeserializer(paramJavaType);
    }
    if ((localKeyDeserializer1 instanceof ContextualKeyDeserializer))
      localKeyDeserializer1 = ((ContextualKeyDeserializer)localKeyDeserializer1).createContextual(paramDeserializationConfig, paramBeanProperty);
    return localKeyDeserializer1;
  }

  public JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    Object localObject = findValueDeserializer(paramDeserializationConfig, paramJavaType, paramBeanProperty);
    TypeDeserializer localTypeDeserializer = this._factory.findTypeDeserializer(paramDeserializationConfig, paramJavaType, paramBeanProperty);
    if (localTypeDeserializer != null)
      localObject = new WrappedDeserializer(localTypeDeserializer, (JsonDeserializer)localObject);
    return (JsonDeserializer<Object>)localObject;
  }

  public JsonDeserializer<Object> findValueDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    JsonDeserializer localJsonDeserializer1 = _findCachedDeserializer(paramJavaType);
    if (localJsonDeserializer1 != null)
    {
      if ((localJsonDeserializer1 instanceof ContextualDeserializer))
        localJsonDeserializer1 = ((ContextualDeserializer)localJsonDeserializer1).createContextual(paramDeserializationConfig, paramBeanProperty);
      return localJsonDeserializer1;
    }
    JsonDeserializer localJsonDeserializer2 = _createAndCacheValueDeserializer(paramDeserializationConfig, paramJavaType, paramBeanProperty);
    if (localJsonDeserializer2 == null)
      localJsonDeserializer2 = _handleUnknownValueDeserializer(paramJavaType);
    if ((localJsonDeserializer2 instanceof ContextualDeserializer))
      localJsonDeserializer2 = ((ContextualDeserializer)localJsonDeserializer2).createContextual(paramDeserializationConfig, paramBeanProperty);
    return localJsonDeserializer2;
  }

  public void flushCachedDeserializers()
  {
    this._cachedDeserializers.clear();
  }

  public boolean hasValueDeserializerFor(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
  {
    Object localObject = _findCachedDeserializer(paramJavaType);
    if (localObject == null);
    try
    {
      JsonDeserializer localJsonDeserializer = _createAndCacheValueDeserializer(paramDeserializationConfig, paramJavaType, null);
      localObject = localJsonDeserializer;
      int i = 0;
      if (localObject != null)
        i = 1;
      return i;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public DeserializerProvider withAbstractTypeResolver(AbstractTypeResolver paramAbstractTypeResolver)
  {
    this._factory = this._factory.withAbstractTypeResolver(paramAbstractTypeResolver);
    return this;
  }

  public DeserializerProvider withAdditionalDeserializers(Deserializers paramDeserializers)
  {
    this._factory = this._factory.withAdditionalDeserializers(paramDeserializers);
    return this;
  }

  public DeserializerProvider withAdditionalKeyDeserializers(KeyDeserializers paramKeyDeserializers)
  {
    this._factory = this._factory.withAdditionalKeyDeserializers(paramKeyDeserializers);
    return this;
  }

  public DeserializerProvider withDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier)
  {
    this._factory = this._factory.withDeserializerModifier(paramBeanDeserializerModifier);
    return this;
  }

  protected static final class WrappedDeserializer extends JsonDeserializer<Object>
  {
    final JsonDeserializer<Object> _deserializer;
    final TypeDeserializer _typeDeserializer;

    public WrappedDeserializer(TypeDeserializer paramTypeDeserializer, JsonDeserializer<Object> paramJsonDeserializer)
    {
      this._typeDeserializer = paramTypeDeserializer;
      this._deserializer = paramJsonDeserializer;
    }

    public Object deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      return this._deserializer.deserializeWithType(paramJsonParser, paramDeserializationContext, this._typeDeserializer);
    }

    public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
      throws IOException, JsonProcessingException
    {
      throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdDeserializerProvider
 * JD-Core Version:    0.6.0
 */