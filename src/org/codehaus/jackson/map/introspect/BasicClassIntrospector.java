package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ClassIntrospector;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BasicClassIntrospector extends ClassIntrospector<BasicBeanDescription>
{
  public static final GetterMethodFilter DEFAULT_GETTER_FILTER = new GetterMethodFilter(null);
  public static final SetterAndGetterMethodFilter DEFAULT_SETTER_AND_GETTER_FILTER;
  public static final SetterMethodFilter DEFAULT_SETTER_FILTER = new SetterMethodFilter();
  public static final BasicClassIntrospector instance;

  static
  {
    DEFAULT_SETTER_AND_GETTER_FILTER = new SetterAndGetterMethodFilter();
    instance = new BasicClassIntrospector();
  }

  public BasicBeanDescription forClassAnnotations(MapperConfig<?> paramMapperConfig, Class<?> paramClass, ClassIntrospector.MixInResolver paramMixInResolver)
  {
    boolean bool = paramMapperConfig.isAnnotationProcessingEnabled();
    AnnotationIntrospector localAnnotationIntrospector = paramMapperConfig.getAnnotationIntrospector();
    if (bool);
    while (true)
    {
      AnnotatedClass localAnnotatedClass = AnnotatedClass.construct(paramClass, localAnnotationIntrospector, paramMixInResolver);
      return new BasicBeanDescription(paramMapperConfig, paramMapperConfig.constructType(paramClass), localAnnotatedClass);
      localAnnotationIntrospector = null;
    }
  }

  public BasicBeanDescription forCreation(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, ClassIntrospector.MixInResolver paramMixInResolver)
  {
    boolean bool = paramDeserializationConfig.isAnnotationProcessingEnabled();
    AnnotationIntrospector localAnnotationIntrospector = paramDeserializationConfig.getAnnotationIntrospector();
    Class localClass = paramJavaType.getRawClass();
    if (bool);
    while (true)
    {
      AnnotatedClass localAnnotatedClass = AnnotatedClass.construct(localClass, localAnnotationIntrospector, paramMixInResolver);
      localAnnotatedClass.resolveCreators(true);
      return new BasicBeanDescription(paramDeserializationConfig, paramJavaType, localAnnotatedClass);
      localAnnotationIntrospector = null;
    }
  }

  public BasicBeanDescription forDeserialization(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, ClassIntrospector.MixInResolver paramMixInResolver)
  {
    boolean bool = paramDeserializationConfig.isAnnotationProcessingEnabled();
    AnnotationIntrospector localAnnotationIntrospector = paramDeserializationConfig.getAnnotationIntrospector();
    Class localClass = paramJavaType.getRawClass();
    if (bool);
    while (true)
    {
      AnnotatedClass localAnnotatedClass = AnnotatedClass.construct(localClass, localAnnotationIntrospector, paramMixInResolver);
      localAnnotatedClass.resolveMemberMethods(getDeserializationMethodFilter(paramDeserializationConfig), true);
      localAnnotatedClass.resolveCreators(true);
      localAnnotatedClass.resolveFields(true);
      return new BasicBeanDescription(paramDeserializationConfig, paramJavaType, localAnnotatedClass);
      localAnnotationIntrospector = null;
    }
  }

  public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> paramMapperConfig, Class<?> paramClass, ClassIntrospector.MixInResolver paramMixInResolver)
  {
    boolean bool = paramMapperConfig.isAnnotationProcessingEnabled();
    AnnotationIntrospector localAnnotationIntrospector = paramMapperConfig.getAnnotationIntrospector();
    if (bool);
    while (true)
    {
      AnnotatedClass localAnnotatedClass = AnnotatedClass.constructWithoutSuperTypes(paramClass, localAnnotationIntrospector, paramMixInResolver);
      return new BasicBeanDescription(paramMapperConfig, paramMapperConfig.constructType(paramClass), localAnnotatedClass);
      localAnnotationIntrospector = null;
    }
  }

  public BasicBeanDescription forSerialization(SerializationConfig paramSerializationConfig, JavaType paramJavaType, ClassIntrospector.MixInResolver paramMixInResolver)
  {
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    AnnotatedClass localAnnotatedClass = AnnotatedClass.construct(paramJavaType.getRawClass(), localAnnotationIntrospector, paramMixInResolver);
    localAnnotatedClass.resolveMemberMethods(getSerializationMethodFilter(paramSerializationConfig), false);
    localAnnotatedClass.resolveCreators(true);
    localAnnotatedClass.resolveFields(false);
    return new BasicBeanDescription(paramSerializationConfig, paramJavaType, localAnnotatedClass);
  }

  protected MethodFilter getDeserializationMethodFilter(DeserializationConfig paramDeserializationConfig)
  {
    if (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS))
      return DEFAULT_SETTER_AND_GETTER_FILTER;
    return DEFAULT_SETTER_FILTER;
  }

  protected MethodFilter getSerializationMethodFilter(SerializationConfig paramSerializationConfig)
  {
    return DEFAULT_GETTER_FILTER;
  }

  public static class GetterMethodFilter
    implements MethodFilter
  {
    public boolean includeMethod(Method paramMethod)
    {
      return ClassUtil.hasGetterSignature(paramMethod);
    }
  }

  public static final class SetterAndGetterMethodFilter extends BasicClassIntrospector.SetterMethodFilter
  {
    public boolean includeMethod(Method paramMethod)
    {
      if (super.includeMethod(paramMethod));
      Class localClass;
      do
      {
        return true;
        if (!ClassUtil.hasGetterSignature(paramMethod))
          return false;
        localClass = paramMethod.getReturnType();
      }
      while ((Collection.class.isAssignableFrom(localClass)) || (Map.class.isAssignableFrom(localClass)));
      return false;
    }
  }

  public static class SetterMethodFilter
    implements MethodFilter
  {
    public boolean includeMethod(Method paramMethod)
    {
      if (Modifier.isStatic(paramMethod.getModifiers()))
        return false;
      switch (paramMethod.getParameterTypes().length)
      {
      default:
        return false;
      case 1:
        return true;
      case 2:
      }
      return true;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.BasicClassIntrospector
 * JD-Core Version:    0.6.0
 */