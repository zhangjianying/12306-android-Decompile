package org.codehaus.jackson.map.ser;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.JsonSerializer<*>;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerFactory.Config;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

public class CustomSerializerFactory extends BeanSerializerFactory
{
  protected HashMap<ClassKey, JsonSerializer<?>> _directClassMappings = null;
  protected JsonSerializer<?> _enumSerializerOverride;
  protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings = null;
  protected HashMap<ClassKey, JsonSerializer<?>> _transitiveClassMappings = null;

  public CustomSerializerFactory()
  {
    this(null);
  }

  public CustomSerializerFactory(SerializerFactory.Config paramConfig)
  {
    super(paramConfig);
  }

  protected JsonSerializer<?> _findInterfaceMapping(Class<?> paramClass, ClassKey paramClassKey)
  {
    for (Class localClass : paramClass.getInterfaces())
    {
      paramClassKey.reset(localClass);
      JsonSerializer localJsonSerializer = (JsonSerializer)this._interfaceMappings.get(paramClassKey);
      if (localJsonSerializer != null);
      do
      {
        return localJsonSerializer;
        localJsonSerializer = _findInterfaceMapping(localClass, paramClassKey);
      }
      while (localJsonSerializer != null);
    }
    return null;
  }

  public <T> void addGenericMapping(Class<? extends T> paramClass, JsonSerializer<T> paramJsonSerializer)
  {
    ClassKey localClassKey = new ClassKey(paramClass);
    if (paramClass.isInterface())
    {
      if (this._interfaceMappings == null)
        this._interfaceMappings = new HashMap();
      this._interfaceMappings.put(localClassKey, paramJsonSerializer);
      return;
    }
    if (this._transitiveClassMappings == null)
      this._transitiveClassMappings = new HashMap();
    this._transitiveClassMappings.put(localClassKey, paramJsonSerializer);
  }

  public <T> void addSpecificMapping(Class<? extends T> paramClass, JsonSerializer<T> paramJsonSerializer)
  {
    ClassKey localClassKey = new ClassKey(paramClass);
    if (paramClass.isInterface())
      throw new IllegalArgumentException("Can not add specific mapping for an interface (" + paramClass.getName() + ")");
    if (Modifier.isAbstract(paramClass.getModifiers()))
      throw new IllegalArgumentException("Can not add specific mapping for an abstract class (" + paramClass.getName() + ")");
    if (this._directClassMappings == null)
      this._directClassMappings = new HashMap();
    this._directClassMappings.put(localClassKey, paramJsonSerializer);
  }

  public JsonSerializer<Object> createSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    JsonSerializer localJsonSerializer = findCustomSerializer(paramJavaType.getRawClass(), paramSerializationConfig);
    if (localJsonSerializer != null)
      return localJsonSerializer;
    return super.createSerializer(paramSerializationConfig, paramJavaType, paramBeanProperty);
  }

  protected JsonSerializer<?> findCustomSerializer(Class<?> paramClass, SerializationConfig paramSerializationConfig)
  {
    ClassKey localClassKey = new ClassKey(paramClass);
    if (this._directClassMappings != null)
    {
      JsonSerializer localJsonSerializer4 = (JsonSerializer)this._directClassMappings.get(localClassKey);
      if (localJsonSerializer4 != null)
        return localJsonSerializer4;
    }
    if ((paramClass.isEnum()) && (this._enumSerializerOverride != null))
      return this._enumSerializerOverride;
    if (this._transitiveClassMappings != null)
      for (Object localObject2 = paramClass; localObject2 != null; localObject2 = ((Class)localObject2).getSuperclass())
      {
        localClassKey.reset((Class)localObject2);
        JsonSerializer localJsonSerializer3 = (JsonSerializer)this._transitiveClassMappings.get(localClassKey);
        if (localJsonSerializer3 != null)
          return localJsonSerializer3;
      }
    if (this._interfaceMappings != null)
    {
      localClassKey.reset(paramClass);
      JsonSerializer localJsonSerializer1 = (JsonSerializer)this._interfaceMappings.get(localClassKey);
      if (localJsonSerializer1 != null)
        return localJsonSerializer1;
      for (Object localObject1 = paramClass; localObject1 != null; localObject1 = ((Class)localObject1).getSuperclass())
      {
        JsonSerializer localJsonSerializer2 = _findInterfaceMapping((Class)localObject1, localClassKey);
        if (localJsonSerializer2 != null)
          return localJsonSerializer2;
      }
    }
    return (JsonSerializer<?>)(JsonSerializer<?>)null;
  }

  public void setEnumSerializer(JsonSerializer<?> paramJsonSerializer)
  {
    this._enumSerializerOverride = paramJsonSerializer;
  }

  public SerializerFactory withConfig(SerializerFactory.Config paramConfig)
  {
    if (getClass() != CustomSerializerFactory.class)
      throw new IllegalStateException("Subtype of CustomSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
    return new CustomSerializerFactory(paramConfig);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.CustomSerializerFactory
 * JD-Core Version:    0.6.0
 */