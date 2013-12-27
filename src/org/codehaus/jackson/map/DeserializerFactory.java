package org.codehaus.jackson.map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public abstract class DeserializerFactory
{
  protected static final Deserializers[] NO_DESERIALIZERS = new Deserializers[0];

  public abstract JsonDeserializer<?> createArrayDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, ArrayType paramArrayType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final JsonDeserializer<?> createArrayDeserializer(DeserializationConfig paramDeserializationConfig, ArrayType paramArrayType, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    return createArrayDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramArrayType, null);
  }

  public abstract JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    return createBeanDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramJavaType, null);
  }

  public abstract JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, CollectionType paramCollectionType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig paramDeserializationConfig, CollectionType paramCollectionType, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    return createCollectionDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramCollectionType, null);
  }

  public abstract JsonDeserializer<?> createCollectionLikeDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, CollectionLikeType paramCollectionLikeType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final JsonDeserializer<?> createEnumDeserializer(DeserializationConfig paramDeserializationConfig, Class<?> paramClass, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    return createEnumDeserializer(paramDeserializationConfig, paramDeserializerProvider, TypeFactory.type(paramClass), null);
  }

  public abstract JsonDeserializer<?> createEnumDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  public KeyDeserializer createKeyDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    return null;
  }

  public abstract JsonDeserializer<?> createMapDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, MapType paramMapType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final JsonDeserializer<?> createMapDeserializer(DeserializationConfig paramDeserializationConfig, MapType paramMapType, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    return createMapDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramMapType, null);
  }

  public abstract JsonDeserializer<?> createMapLikeDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, MapLikeType paramMapLikeType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final JsonDeserializer<?> createTreeDeserializer(DeserializationConfig paramDeserializationConfig, Class<? extends JsonNode> paramClass, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    return createTreeDeserializer(paramDeserializationConfig, paramDeserializerProvider, TypeFactory.type(paramClass), null);
  }

  public abstract JsonDeserializer<?> createTreeDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final TypeDeserializer findTypeDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
  {
    return findTypeDeserializer(paramDeserializationConfig, paramJavaType, null);
  }

  public TypeDeserializer findTypeDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
  {
    return null;
  }

  public abstract Config getConfig();

  public final DeserializerFactory withAbstractTypeResolver(AbstractTypeResolver paramAbstractTypeResolver)
  {
    return withConfig(getConfig().withAbstractTypeResolver(paramAbstractTypeResolver));
  }

  public final DeserializerFactory withAdditionalDeserializers(Deserializers paramDeserializers)
  {
    return withConfig(getConfig().withAdditionalDeserializers(paramDeserializers));
  }

  public final DeserializerFactory withAdditionalKeyDeserializers(KeyDeserializers paramKeyDeserializers)
  {
    return withConfig(getConfig().withAdditionalKeyDeserializers(paramKeyDeserializers));
  }

  public abstract DeserializerFactory withConfig(Config paramConfig);

  public final DeserializerFactory withDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier)
  {
    return withConfig(getConfig().withDeserializerModifier(paramBeanDeserializerModifier));
  }

  public static abstract class Config
  {
    public abstract Iterable<AbstractTypeResolver> abstractTypeResolvers();

    public abstract Iterable<BeanDeserializerModifier> deserializerModifiers();

    public abstract Iterable<Deserializers> deserializers();

    public abstract boolean hasAbstractTypeResolvers();

    public abstract boolean hasDeserializerModifiers();

    public abstract boolean hasDeserializers();

    public abstract boolean hasKeyDeserializers();

    public abstract Iterable<KeyDeserializers> keyDeserializers();

    public abstract Config withAbstractTypeResolver(AbstractTypeResolver paramAbstractTypeResolver);

    public abstract Config withAdditionalDeserializers(Deserializers paramDeserializers);

    public abstract Config withAdditionalKeyDeserializers(KeyDeserializers paramKeyDeserializers);

    public abstract Config withDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.DeserializerFactory
 * JD-Core Version:    0.6.0
 */