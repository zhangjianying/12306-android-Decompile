package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

class StdKeyDeserializers
{
  final HashMap<JavaType, KeyDeserializer> _keyDeserializers = new HashMap();

  private StdKeyDeserializers()
  {
    add(new StdKeyDeserializer.BoolKD());
    add(new StdKeyDeserializer.ByteKD());
    add(new StdKeyDeserializer.CharKD());
    add(new StdKeyDeserializer.ShortKD());
    add(new StdKeyDeserializer.IntKD());
    add(new StdKeyDeserializer.LongKD());
    add(new StdKeyDeserializer.FloatKD());
    add(new StdKeyDeserializer.DoubleKD());
  }

  private void add(StdKeyDeserializer paramStdKeyDeserializer)
  {
    Class localClass = paramStdKeyDeserializer.getKeyClass();
    this._keyDeserializers.put(TypeFactory.defaultInstance().constructType(localClass), paramStdKeyDeserializer);
  }

  public static HashMap<JavaType, KeyDeserializer> constructAll()
  {
    return new StdKeyDeserializers()._keyDeserializers;
  }

  public static KeyDeserializer constructEnumKeyDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
  {
    return new StdKeyDeserializer.EnumKD(EnumResolver.constructUnsafe(paramJavaType.getRawClass(), paramDeserializationConfig.getAnnotationIntrospector()));
  }

  public static KeyDeserializer findStringBasedKeyDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
  {
    BasicBeanDescription localBasicBeanDescription = (BasicBeanDescription)paramDeserializationConfig.introspect(paramJavaType);
    Constructor localConstructor = localBasicBeanDescription.findSingleArgConstructor(new Class[] { String.class });
    if (localConstructor != null)
      return new StdKeyDeserializer.StringCtorKeyDeserializer(localConstructor);
    Method localMethod = localBasicBeanDescription.findFactoryMethod(new Class[] { String.class });
    if (localMethod != null)
      return new StdKeyDeserializer.StringFactoryKeyDeserializer(localMethod);
    return null;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdKeyDeserializers
 * JD-Core Version:    0.6.0
 */