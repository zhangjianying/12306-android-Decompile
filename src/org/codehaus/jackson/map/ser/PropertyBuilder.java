package org.codehaus.jackson.map.ser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

public class PropertyBuilder
{
  protected final AnnotationIntrospector _annotationIntrospector;
  protected final BasicBeanDescription _beanDesc;
  protected final SerializationConfig _config;
  protected Object _defaultBean;
  protected final JsonSerialize.Inclusion _outputProps;

  public PropertyBuilder(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription)
  {
    this._config = paramSerializationConfig;
    this._beanDesc = paramBasicBeanDescription;
    this._outputProps = paramBasicBeanDescription.findSerializationInclusion(paramSerializationConfig.getSerializationInclusion());
    this._annotationIntrospector = this._config.getAnnotationIntrospector();
  }

  protected Object _throwWrapped(Exception paramException, String paramString, Object paramObject)
  {
    for (Object localObject = paramException; ((Throwable)localObject).getCause() != null; localObject = ((Throwable)localObject).getCause());
    if ((localObject instanceof Error))
      throw ((Error)localObject);
    if ((localObject instanceof RuntimeException))
      throw ((RuntimeException)localObject);
    throw new IllegalArgumentException("Failed to get property '" + paramString + "' of default " + paramObject.getClass().getName() + " instance");
  }

  protected BeanPropertyWriter buildWriter(String paramString, JavaType paramJavaType, JsonSerializer<Object> paramJsonSerializer, TypeSerializer paramTypeSerializer1, TypeSerializer paramTypeSerializer2, AnnotatedMember paramAnnotatedMember, boolean paramBoolean)
  {
    Method localMethod;
    Field localField;
    if ((paramAnnotatedMember instanceof AnnotatedField))
    {
      localMethod = null;
      localField = ((AnnotatedField)paramAnnotatedMember).getAnnotated();
    }
    JavaType localJavaType;
    while (true)
    {
      localJavaType = findSerializationType(paramAnnotatedMember, paramBoolean, paramJavaType);
      if (paramTypeSerializer2 == null)
        break;
      if (localJavaType == null)
        localJavaType = paramJavaType;
      if (localJavaType.getContentType() == null)
      {
        throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + paramString + "' (of type " + this._beanDesc.getType() + "); serialization type " + localJavaType + " has no content");
        localMethod = ((AnnotatedMethod)paramAnnotatedMember).getAnnotated();
        localField = null;
        continue;
      }
      localJavaType = localJavaType.withContentTypeHandler(paramTypeSerializer2);
      localJavaType.getContentType();
    }
    JsonSerialize.Inclusion localInclusion = this._annotationIntrospector.findSerializationInclusion(paramAnnotatedMember, this._outputProps);
    boolean bool = false;
    Object localObject = null;
    if (localInclusion != null)
    {
      int i = 1.$SwitchMap$org$codehaus$jackson$map$annotate$JsonSerialize$Inclusion[localInclusion.ordinal()];
      bool = false;
      localObject = null;
      switch (i)
      {
      default:
      case 1:
      case 2:
      }
    }
    while (true)
    {
      return new BeanPropertyWriter(paramAnnotatedMember, this._beanDesc.getClassAnnotations(), paramString, paramJavaType, paramJsonSerializer, paramTypeSerializer1, localJavaType, localMethod, localField, bool, localObject);
      localObject = getDefaultValue(paramString, localMethod, localField);
      bool = false;
      if (localObject != null)
        continue;
      bool = true;
      continue;
      bool = true;
      localObject = null;
    }
  }

  protected JavaType findSerializationType(Annotated paramAnnotated, boolean paramBoolean, JavaType paramJavaType)
  {
    Class localClass1 = this._annotationIntrospector.findSerializationType(paramAnnotated);
    Class localClass2;
    if (localClass1 != null)
    {
      localClass2 = paramJavaType.getRawClass();
      if (localClass1.isAssignableFrom(localClass2))
      {
        paramJavaType = paramJavaType.widenBy(localClass1);
        paramBoolean = true;
      }
    }
    else
    {
      JavaType localJavaType = BeanSerializerFactory.modifySecondaryTypesByAnnotation(this._config, paramAnnotated, paramJavaType);
      if (localJavaType != paramJavaType)
      {
        paramBoolean = true;
        paramJavaType = localJavaType;
      }
      if (!paramBoolean)
      {
        JsonSerialize.Typing localTyping = this._annotationIntrospector.findSerializationTyping(paramAnnotated);
        if (localTyping != null)
          if (localTyping != JsonSerialize.Typing.STATIC)
            break label173;
      }
    }
    label173: for (paramBoolean = true; ; paramBoolean = false)
    {
      if (!paramBoolean)
        break label178;
      return paramJavaType;
      if (!localClass2.isAssignableFrom(localClass1))
        throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + paramAnnotated.getName() + "': class " + localClass1.getName() + " not a super-type of (declared) class " + localClass2.getName());
      paramJavaType = paramJavaType.forcedNarrowBy(localClass1);
      break;
    }
    label178: return null;
  }

  public Annotations getClassAnnotations()
  {
    return this._beanDesc.getClassAnnotations();
  }

  protected Object getDefaultBean()
  {
    if (this._defaultBean == null)
    {
      this._defaultBean = this._beanDesc.instantiateBean(this._config.isEnabled(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
      if (this._defaultBean == null)
      {
        Class localClass = this._beanDesc.getClassInfo().getAnnotated();
        throw new IllegalArgumentException("Class " + localClass.getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
      }
    }
    return this._defaultBean;
  }

  protected Object getDefaultValue(String paramString, Method paramMethod, Field paramField)
  {
    Object localObject1 = getDefaultBean();
    if (paramMethod != null);
    try
    {
      return paramMethod.invoke(localObject1, new Object[0]);
      Object localObject2 = paramField.get(localObject1);
      return localObject2;
    }
    catch (Exception localException)
    {
    }
    return _throwWrapped(localException, paramString, localObject1);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.PropertyBuilder
 * JD-Core Version:    0.6.0
 */