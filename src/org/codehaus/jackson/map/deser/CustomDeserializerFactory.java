package org.codehaus.jackson.map.deser;

import java.util.HashMap;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

@Deprecated
public class CustomDeserializerFactory extends BeanDeserializerFactory
{
  protected HashMap<ClassKey, JsonDeserializer<Object>> _directClassMappings = null;
  protected HashMap<ClassKey, Class<?>> _mixInAnnotations;

  public CustomDeserializerFactory()
  {
    this(null);
  }

  protected CustomDeserializerFactory(DeserializerFactory.Config paramConfig)
  {
    super(paramConfig);
  }

  public void addMixInAnnotationMapping(Class<?> paramClass1, Class<?> paramClass2)
  {
    if (this._mixInAnnotations == null)
      this._mixInAnnotations = new HashMap();
    this._mixInAnnotations.put(new ClassKey(paramClass1), paramClass2);
  }

  public <T> void addSpecificMapping(Class<T> paramClass, JsonDeserializer<? extends T> paramJsonDeserializer)
  {
    ClassKey localClassKey = new ClassKey(paramClass);
    if (this._directClassMappings == null)
      this._directClassMappings = new HashMap();
    this._directClassMappings.put(localClassKey, paramJsonDeserializer);
  }

  public JsonDeserializer<?> createArrayDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, ArrayType paramArrayType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    ClassKey localClassKey = new ClassKey(paramArrayType.getRawClass());
    if (this._directClassMappings != null)
    {
      JsonDeserializer localJsonDeserializer = (JsonDeserializer)this._directClassMappings.get(localClassKey);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return super.createArrayDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramArrayType, paramBeanProperty);
  }

  public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    ClassKey localClassKey = new ClassKey(paramJavaType.getRawClass());
    if (this._directClassMappings != null)
    {
      JsonDeserializer localJsonDeserializer = (JsonDeserializer)this._directClassMappings.get(localClassKey);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return super.createBeanDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramJavaType, paramBeanProperty);
  }

  public JsonDeserializer<?> createEnumDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    if (this._directClassMappings != null)
    {
      ClassKey localClassKey = new ClassKey(paramJavaType.getRawClass());
      JsonDeserializer localJsonDeserializer = (JsonDeserializer)this._directClassMappings.get(localClassKey);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return super.createEnumDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramJavaType, paramBeanProperty);
  }

  public DeserializerFactory withConfig(DeserializerFactory.Config paramConfig)
  {
    if (getClass() != CustomDeserializerFactory.class)
      throw new IllegalStateException("Subtype of CustomDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
    return new CustomDeserializerFactory(paramConfig);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.CustomDeserializerFactory
 * JD-Core Version:    0.6.0
 */