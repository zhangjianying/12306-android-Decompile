package org.codehaus.jackson.map;

import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.type.JavaType;

public abstract class SerializerFactory
{
  public abstract JsonSerializer<Object> createKeySerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  public abstract JsonSerializer<Object> createSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final JsonSerializer<Object> createSerializer(JavaType paramJavaType, SerializationConfig paramSerializationConfig)
  {
    try
    {
      JsonSerializer localJsonSerializer = createSerializer(paramSerializationConfig, paramJavaType, null);
      return localJsonSerializer;
    }
    catch (JsonMappingException localJsonMappingException)
    {
    }
    throw new RuntimeJsonMappingException(localJsonMappingException);
  }

  public abstract TypeSerializer createTypeSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  @Deprecated
  public final TypeSerializer createTypeSerializer(JavaType paramJavaType, SerializationConfig paramSerializationConfig)
  {
    try
    {
      TypeSerializer localTypeSerializer = createTypeSerializer(paramSerializationConfig, paramJavaType, null);
      return localTypeSerializer;
    }
    catch (JsonMappingException localJsonMappingException)
    {
    }
    throw new RuntimeException(localJsonMappingException);
  }

  public abstract Config getConfig();

  public final SerializerFactory withAdditionalKeySerializers(Serializers paramSerializers)
  {
    return withConfig(getConfig().withAdditionalKeySerializers(paramSerializers));
  }

  public final SerializerFactory withAdditionalSerializers(Serializers paramSerializers)
  {
    return withConfig(getConfig().withAdditionalSerializers(paramSerializers));
  }

  public abstract SerializerFactory withConfig(Config paramConfig);

  public final SerializerFactory withSerializerModifier(BeanSerializerModifier paramBeanSerializerModifier)
  {
    return withConfig(getConfig().withSerializerModifier(paramBeanSerializerModifier));
  }

  public static abstract class Config
  {
    public abstract boolean hasKeySerializers();

    public abstract boolean hasSerializerModifiers();

    public abstract boolean hasSerializers();

    public abstract Iterable<Serializers> keySerializers();

    public abstract Iterable<BeanSerializerModifier> serializerModifiers();

    public abstract Iterable<Serializers> serializers();

    public abstract Config withAdditionalKeySerializers(Serializers paramSerializers);

    public abstract Config withAdditionalSerializers(Serializers paramSerializers);

    public abstract Config withSerializerModifier(BeanSerializerModifier paramBeanSerializerModifier);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.SerializerFactory
 * JD-Core Version:    0.6.0
 */