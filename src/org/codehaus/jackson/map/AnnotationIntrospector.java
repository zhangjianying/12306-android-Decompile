package org.codehaus.jackson.map;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.type.JavaType;

public abstract class AnnotationIntrospector
{
  public static AnnotationIntrospector nopInstance()
  {
    return NopAnnotationIntrospector.instance;
  }

  public static AnnotationIntrospector pair(AnnotationIntrospector paramAnnotationIntrospector1, AnnotationIntrospector paramAnnotationIntrospector2)
  {
    return new Pair(paramAnnotationIntrospector1, paramAnnotationIntrospector2);
  }

  public Collection<AnnotationIntrospector> allIntrospectors()
  {
    return Collections.singletonList(this);
  }

  public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> paramCollection)
  {
    paramCollection.add(this);
    return paramCollection;
  }

  public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass paramAnnotatedClass, VisibilityChecker<?> paramVisibilityChecker)
  {
    return paramVisibilityChecker;
  }

  public abstract Boolean findCachability(AnnotatedClass paramAnnotatedClass);

  public abstract Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated paramAnnotated);

  public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated paramAnnotated)
  {
    return null;
  }

  public abstract String findDeserializablePropertyName(AnnotatedField paramAnnotatedField);

  public abstract Class<?> findDeserializationContentType(Annotated paramAnnotated, JavaType paramJavaType, String paramString);

  public abstract Class<?> findDeserializationKeyType(Annotated paramAnnotated, JavaType paramJavaType, String paramString);

  public abstract Class<?> findDeserializationType(Annotated paramAnnotated, JavaType paramJavaType, String paramString);

  public Object findDeserializer(Annotated paramAnnotated)
  {
    return findDeserializer(paramAnnotated, null);
  }

  @Deprecated
  public Object findDeserializer(Annotated paramAnnotated, BeanProperty paramBeanProperty)
  {
    if (paramBeanProperty != null)
      return findDeserializer(paramAnnotated);
    return null;
  }

  public abstract String findEnumValue(Enum<?> paramEnum);

  public Object findFilterId(AnnotatedClass paramAnnotatedClass)
  {
    return null;
  }

  public abstract String findGettablePropertyName(AnnotatedMethod paramAnnotatedMethod);

  public abstract Boolean findIgnoreUnknownProperties(AnnotatedClass paramAnnotatedClass);

  public abstract Class<? extends KeyDeserializer> findKeyDeserializer(Annotated paramAnnotated);

  public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated paramAnnotated)
  {
    return null;
  }

  public abstract String[] findPropertiesToIgnore(AnnotatedClass paramAnnotatedClass);

  public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> paramMapperConfig, AnnotatedMember paramAnnotatedMember, JavaType paramJavaType)
  {
    return null;
  }

  public abstract String findPropertyNameForParam(AnnotatedParameter paramAnnotatedParameter);

  public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> paramMapperConfig, AnnotatedMember paramAnnotatedMember, JavaType paramJavaType)
  {
    return null;
  }

  public ReferenceProperty findReferenceType(AnnotatedMember paramAnnotatedMember)
  {
    return null;
  }

  public abstract String findRootName(AnnotatedClass paramAnnotatedClass);

  public abstract String findSerializablePropertyName(AnnotatedField paramAnnotatedField);

  public Class<?> findSerializationContentType(Annotated paramAnnotated, JavaType paramJavaType)
  {
    return null;
  }

  public JsonSerialize.Inclusion findSerializationInclusion(Annotated paramAnnotated, JsonSerialize.Inclusion paramInclusion)
  {
    return paramInclusion;
  }

  public Class<?> findSerializationKeyType(Annotated paramAnnotated, JavaType paramJavaType)
  {
    return null;
  }

  public abstract String[] findSerializationPropertyOrder(AnnotatedClass paramAnnotatedClass);

  public abstract Boolean findSerializationSortAlphabetically(AnnotatedClass paramAnnotatedClass);

  public abstract Class<?> findSerializationType(Annotated paramAnnotated);

  public abstract JsonSerialize.Typing findSerializationTyping(Annotated paramAnnotated);

  public abstract Class<?>[] findSerializationViews(Annotated paramAnnotated);

  public Object findSerializer(Annotated paramAnnotated)
  {
    return findSerializer(paramAnnotated, null);
  }

  @Deprecated
  public Object findSerializer(Annotated paramAnnotated, BeanProperty paramBeanProperty)
  {
    if (paramBeanProperty != null)
      return findSerializer(paramAnnotated);
    return null;
  }

  public abstract String findSettablePropertyName(AnnotatedMethod paramAnnotatedMethod);

  public List<NamedType> findSubtypes(Annotated paramAnnotated)
  {
    return null;
  }

  public String findTypeName(AnnotatedClass paramAnnotatedClass)
  {
    return null;
  }

  public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> paramMapperConfig, AnnotatedClass paramAnnotatedClass, JavaType paramJavaType)
  {
    return null;
  }

  public boolean hasAnyGetterAnnotation(AnnotatedMethod paramAnnotatedMethod)
  {
    return false;
  }

  public boolean hasAnySetterAnnotation(AnnotatedMethod paramAnnotatedMethod)
  {
    return false;
  }

  public abstract boolean hasAsValueAnnotation(AnnotatedMethod paramAnnotatedMethod);

  public boolean hasCreatorAnnotation(Annotated paramAnnotated)
  {
    return false;
  }

  public abstract boolean isHandled(Annotation paramAnnotation);

  public abstract boolean isIgnorableConstructor(AnnotatedConstructor paramAnnotatedConstructor);

  public abstract boolean isIgnorableField(AnnotatedField paramAnnotatedField);

  public abstract boolean isIgnorableMethod(AnnotatedMethod paramAnnotatedMethod);

  public Boolean isIgnorableType(AnnotatedClass paramAnnotatedClass)
  {
    return null;
  }

  public static class Pair extends AnnotationIntrospector
  {
    protected final AnnotationIntrospector _primary;
    protected final AnnotationIntrospector _secondary;

    public Pair(AnnotationIntrospector paramAnnotationIntrospector1, AnnotationIntrospector paramAnnotationIntrospector2)
    {
      this._primary = paramAnnotationIntrospector1;
      this._secondary = paramAnnotationIntrospector2;
    }

    public static AnnotationIntrospector create(AnnotationIntrospector paramAnnotationIntrospector1, AnnotationIntrospector paramAnnotationIntrospector2)
    {
      if (paramAnnotationIntrospector1 == null)
        return paramAnnotationIntrospector2;
      if (paramAnnotationIntrospector2 == null)
        return paramAnnotationIntrospector1;
      return new Pair(paramAnnotationIntrospector1, paramAnnotationIntrospector2);
    }

    public Collection<AnnotationIntrospector> allIntrospectors()
    {
      return allIntrospectors(new ArrayList());
    }

    public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> paramCollection)
    {
      this._primary.allIntrospectors(paramCollection);
      this._secondary.allIntrospectors(paramCollection);
      return paramCollection;
    }

    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass paramAnnotatedClass, VisibilityChecker<?> paramVisibilityChecker)
    {
      VisibilityChecker localVisibilityChecker = this._secondary.findAutoDetectVisibility(paramAnnotatedClass, paramVisibilityChecker);
      return this._primary.findAutoDetectVisibility(paramAnnotatedClass, localVisibilityChecker);
    }

    public Boolean findCachability(AnnotatedClass paramAnnotatedClass)
    {
      Boolean localBoolean = this._primary.findCachability(paramAnnotatedClass);
      if (localBoolean == null)
        localBoolean = this._secondary.findCachability(paramAnnotatedClass);
      return localBoolean;
    }

    public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated paramAnnotated)
    {
      Class localClass = this._primary.findContentDeserializer(paramAnnotated);
      if ((localClass == null) || (localClass == JsonDeserializer.None.class))
        localClass = this._secondary.findContentDeserializer(paramAnnotated);
      return localClass;
    }

    public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated paramAnnotated)
    {
      Class localClass = this._primary.findContentSerializer(paramAnnotated);
      if ((localClass == null) || (localClass == JsonSerializer.None.class))
        localClass = this._secondary.findContentSerializer(paramAnnotated);
      return localClass;
    }

    public String findDeserializablePropertyName(AnnotatedField paramAnnotatedField)
    {
      String str1 = this._primary.findDeserializablePropertyName(paramAnnotatedField);
      if (str1 == null)
        str1 = this._secondary.findDeserializablePropertyName(paramAnnotatedField);
      String str2;
      do
      {
        do
          return str1;
        while (str1.length() != 0);
        str2 = this._secondary.findDeserializablePropertyName(paramAnnotatedField);
      }
      while (str2 == null);
      return str2;
    }

    public Class<?> findDeserializationContentType(Annotated paramAnnotated, JavaType paramJavaType, String paramString)
    {
      Class localClass = this._primary.findDeserializationContentType(paramAnnotated, paramJavaType, paramString);
      if (localClass == null)
        localClass = this._secondary.findDeserializationContentType(paramAnnotated, paramJavaType, paramString);
      return localClass;
    }

    public Class<?> findDeserializationKeyType(Annotated paramAnnotated, JavaType paramJavaType, String paramString)
    {
      Class localClass = this._primary.findDeserializationKeyType(paramAnnotated, paramJavaType, paramString);
      if (localClass == null)
        localClass = this._secondary.findDeserializationKeyType(paramAnnotated, paramJavaType, paramString);
      return localClass;
    }

    public Class<?> findDeserializationType(Annotated paramAnnotated, JavaType paramJavaType, String paramString)
    {
      Class localClass = this._primary.findDeserializationType(paramAnnotated, paramJavaType, paramString);
      if (localClass == null)
        localClass = this._secondary.findDeserializationType(paramAnnotated, paramJavaType, paramString);
      return localClass;
    }

    public Object findDeserializer(Annotated paramAnnotated)
    {
      Object localObject = this._primary.findDeserializer(paramAnnotated);
      if (localObject == null)
        localObject = this._secondary.findDeserializer(paramAnnotated);
      return localObject;
    }

    public Object findDeserializer(Annotated paramAnnotated, BeanProperty paramBeanProperty)
    {
      Object localObject = this._primary.findDeserializer(paramAnnotated, paramBeanProperty);
      if (localObject == null)
        localObject = this._secondary.findDeserializer(paramAnnotated, paramBeanProperty);
      return localObject;
    }

    public String findEnumValue(Enum<?> paramEnum)
    {
      String str = this._primary.findEnumValue(paramEnum);
      if (str == null)
        str = this._secondary.findEnumValue(paramEnum);
      return str;
    }

    public Object findFilterId(AnnotatedClass paramAnnotatedClass)
    {
      Object localObject = this._primary.findFilterId(paramAnnotatedClass);
      if (localObject == null)
        localObject = this._secondary.findFilterId(paramAnnotatedClass);
      return localObject;
    }

    public String findGettablePropertyName(AnnotatedMethod paramAnnotatedMethod)
    {
      String str1 = this._primary.findGettablePropertyName(paramAnnotatedMethod);
      if (str1 == null)
        str1 = this._secondary.findGettablePropertyName(paramAnnotatedMethod);
      String str2;
      do
      {
        do
          return str1;
        while (str1.length() != 0);
        str2 = this._secondary.findGettablePropertyName(paramAnnotatedMethod);
      }
      while (str2 == null);
      return str2;
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass paramAnnotatedClass)
    {
      Boolean localBoolean = this._primary.findIgnoreUnknownProperties(paramAnnotatedClass);
      if (localBoolean == null)
        localBoolean = this._secondary.findIgnoreUnknownProperties(paramAnnotatedClass);
      return localBoolean;
    }

    public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated paramAnnotated)
    {
      Class localClass = this._primary.findKeyDeserializer(paramAnnotated);
      if ((localClass == null) || (localClass == KeyDeserializer.None.class))
        localClass = this._secondary.findKeyDeserializer(paramAnnotated);
      return localClass;
    }

    public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated paramAnnotated)
    {
      Class localClass = this._primary.findKeySerializer(paramAnnotated);
      if ((localClass == null) || (localClass == JsonSerializer.None.class))
        localClass = this._secondary.findKeySerializer(paramAnnotated);
      return localClass;
    }

    public String[] findPropertiesToIgnore(AnnotatedClass paramAnnotatedClass)
    {
      String[] arrayOfString = this._primary.findPropertiesToIgnore(paramAnnotatedClass);
      if (arrayOfString == null)
        arrayOfString = this._secondary.findPropertiesToIgnore(paramAnnotatedClass);
      return arrayOfString;
    }

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> paramMapperConfig, AnnotatedMember paramAnnotatedMember, JavaType paramJavaType)
    {
      TypeResolverBuilder localTypeResolverBuilder = this._primary.findPropertyContentTypeResolver(paramMapperConfig, paramAnnotatedMember, paramJavaType);
      if (localTypeResolverBuilder == null)
        localTypeResolverBuilder = this._secondary.findPropertyContentTypeResolver(paramMapperConfig, paramAnnotatedMember, paramJavaType);
      return localTypeResolverBuilder;
    }

    public String findPropertyNameForParam(AnnotatedParameter paramAnnotatedParameter)
    {
      String str = this._primary.findPropertyNameForParam(paramAnnotatedParameter);
      if (str == null)
        str = this._secondary.findPropertyNameForParam(paramAnnotatedParameter);
      return str;
    }

    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> paramMapperConfig, AnnotatedMember paramAnnotatedMember, JavaType paramJavaType)
    {
      TypeResolverBuilder localTypeResolverBuilder = this._primary.findPropertyTypeResolver(paramMapperConfig, paramAnnotatedMember, paramJavaType);
      if (localTypeResolverBuilder == null)
        localTypeResolverBuilder = this._secondary.findPropertyTypeResolver(paramMapperConfig, paramAnnotatedMember, paramJavaType);
      return localTypeResolverBuilder;
    }

    public AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember paramAnnotatedMember)
    {
      AnnotationIntrospector.ReferenceProperty localReferenceProperty = this._primary.findReferenceType(paramAnnotatedMember);
      if (localReferenceProperty == null)
        localReferenceProperty = this._secondary.findReferenceType(paramAnnotatedMember);
      return localReferenceProperty;
    }

    public String findRootName(AnnotatedClass paramAnnotatedClass)
    {
      String str1 = this._primary.findRootName(paramAnnotatedClass);
      if (str1 == null)
        str1 = this._secondary.findRootName(paramAnnotatedClass);
      String str2;
      do
      {
        do
          return str1;
        while (str1.length() > 0);
        str2 = this._secondary.findRootName(paramAnnotatedClass);
      }
      while (str2 == null);
      return str2;
    }

    public String findSerializablePropertyName(AnnotatedField paramAnnotatedField)
    {
      String str1 = this._primary.findSerializablePropertyName(paramAnnotatedField);
      if (str1 == null)
        str1 = this._secondary.findSerializablePropertyName(paramAnnotatedField);
      String str2;
      do
      {
        do
          return str1;
        while (str1.length() != 0);
        str2 = this._secondary.findSerializablePropertyName(paramAnnotatedField);
      }
      while (str2 == null);
      return str2;
    }

    public Class<?> findSerializationContentType(Annotated paramAnnotated, JavaType paramJavaType)
    {
      Class localClass = this._primary.findSerializationContentType(paramAnnotated, paramJavaType);
      if (localClass == null)
        localClass = this._secondary.findSerializationContentType(paramAnnotated, paramJavaType);
      return localClass;
    }

    public JsonSerialize.Inclusion findSerializationInclusion(Annotated paramAnnotated, JsonSerialize.Inclusion paramInclusion)
    {
      JsonSerialize.Inclusion localInclusion = this._secondary.findSerializationInclusion(paramAnnotated, paramInclusion);
      return this._primary.findSerializationInclusion(paramAnnotated, localInclusion);
    }

    public Class<?> findSerializationKeyType(Annotated paramAnnotated, JavaType paramJavaType)
    {
      Class localClass = this._primary.findSerializationKeyType(paramAnnotated, paramJavaType);
      if (localClass == null)
        localClass = this._secondary.findSerializationKeyType(paramAnnotated, paramJavaType);
      return localClass;
    }

    public String[] findSerializationPropertyOrder(AnnotatedClass paramAnnotatedClass)
    {
      String[] arrayOfString = this._primary.findSerializationPropertyOrder(paramAnnotatedClass);
      if (arrayOfString == null)
        arrayOfString = this._secondary.findSerializationPropertyOrder(paramAnnotatedClass);
      return arrayOfString;
    }

    public Boolean findSerializationSortAlphabetically(AnnotatedClass paramAnnotatedClass)
    {
      Boolean localBoolean = this._primary.findSerializationSortAlphabetically(paramAnnotatedClass);
      if (localBoolean == null)
        localBoolean = this._secondary.findSerializationSortAlphabetically(paramAnnotatedClass);
      return localBoolean;
    }

    public Class<?> findSerializationType(Annotated paramAnnotated)
    {
      Class localClass = this._primary.findSerializationType(paramAnnotated);
      if (localClass == null)
        localClass = this._secondary.findSerializationType(paramAnnotated);
      return localClass;
    }

    public JsonSerialize.Typing findSerializationTyping(Annotated paramAnnotated)
    {
      JsonSerialize.Typing localTyping = this._primary.findSerializationTyping(paramAnnotated);
      if (localTyping == null)
        localTyping = this._secondary.findSerializationTyping(paramAnnotated);
      return localTyping;
    }

    public Class<?>[] findSerializationViews(Annotated paramAnnotated)
    {
      Class[] arrayOfClass = this._primary.findSerializationViews(paramAnnotated);
      if (arrayOfClass == null)
        arrayOfClass = this._secondary.findSerializationViews(paramAnnotated);
      return arrayOfClass;
    }

    public Object findSerializer(Annotated paramAnnotated)
    {
      Object localObject = this._primary.findSerializer(paramAnnotated);
      if (localObject == null)
        localObject = this._secondary.findSerializer(paramAnnotated);
      return localObject;
    }

    public Object findSerializer(Annotated paramAnnotated, BeanProperty paramBeanProperty)
    {
      Object localObject = this._primary.findSerializer(paramAnnotated, paramBeanProperty);
      if (localObject == null)
        localObject = this._secondary.findSerializer(paramAnnotated, paramBeanProperty);
      return localObject;
    }

    public String findSettablePropertyName(AnnotatedMethod paramAnnotatedMethod)
    {
      String str1 = this._primary.findSettablePropertyName(paramAnnotatedMethod);
      if (str1 == null)
        str1 = this._secondary.findSettablePropertyName(paramAnnotatedMethod);
      String str2;
      do
      {
        do
          return str1;
        while (str1.length() != 0);
        str2 = this._secondary.findSettablePropertyName(paramAnnotatedMethod);
      }
      while (str2 == null);
      return str2;
    }

    public List<NamedType> findSubtypes(Annotated paramAnnotated)
    {
      List localList1 = this._primary.findSubtypes(paramAnnotated);
      List localList2 = this._secondary.findSubtypes(paramAnnotated);
      if ((localList1 == null) || (localList1.isEmpty()))
        return localList2;
      if ((localList2 == null) || (localList2.isEmpty()))
        return localList1;
      ArrayList localArrayList = new ArrayList(localList1.size() + localList2.size());
      localArrayList.addAll(localList1);
      localArrayList.addAll(localList2);
      return localArrayList;
    }

    public String findTypeName(AnnotatedClass paramAnnotatedClass)
    {
      String str = this._primary.findTypeName(paramAnnotatedClass);
      if ((str == null) || (str.length() == 0))
        str = this._secondary.findTypeName(paramAnnotatedClass);
      return str;
    }

    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> paramMapperConfig, AnnotatedClass paramAnnotatedClass, JavaType paramJavaType)
    {
      TypeResolverBuilder localTypeResolverBuilder = this._primary.findTypeResolver(paramMapperConfig, paramAnnotatedClass, paramJavaType);
      if (localTypeResolverBuilder == null)
        localTypeResolverBuilder = this._secondary.findTypeResolver(paramMapperConfig, paramAnnotatedClass, paramJavaType);
      return localTypeResolverBuilder;
    }

    public boolean hasAnyGetterAnnotation(AnnotatedMethod paramAnnotatedMethod)
    {
      return (this._primary.hasAnyGetterAnnotation(paramAnnotatedMethod)) || (this._secondary.hasAnyGetterAnnotation(paramAnnotatedMethod));
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod paramAnnotatedMethod)
    {
      return (this._primary.hasAnySetterAnnotation(paramAnnotatedMethod)) || (this._secondary.hasAnySetterAnnotation(paramAnnotatedMethod));
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod paramAnnotatedMethod)
    {
      return (this._primary.hasAsValueAnnotation(paramAnnotatedMethod)) || (this._secondary.hasAsValueAnnotation(paramAnnotatedMethod));
    }

    public boolean hasCreatorAnnotation(Annotated paramAnnotated)
    {
      return (this._primary.hasCreatorAnnotation(paramAnnotated)) || (this._secondary.hasCreatorAnnotation(paramAnnotated));
    }

    public boolean isHandled(Annotation paramAnnotation)
    {
      return (this._primary.isHandled(paramAnnotation)) || (this._secondary.isHandled(paramAnnotation));
    }

    public boolean isIgnorableConstructor(AnnotatedConstructor paramAnnotatedConstructor)
    {
      return (this._primary.isIgnorableConstructor(paramAnnotatedConstructor)) || (this._secondary.isIgnorableConstructor(paramAnnotatedConstructor));
    }

    public boolean isIgnorableField(AnnotatedField paramAnnotatedField)
    {
      return (this._primary.isIgnorableField(paramAnnotatedField)) || (this._secondary.isIgnorableField(paramAnnotatedField));
    }

    public boolean isIgnorableMethod(AnnotatedMethod paramAnnotatedMethod)
    {
      return (this._primary.isIgnorableMethod(paramAnnotatedMethod)) || (this._secondary.isIgnorableMethod(paramAnnotatedMethod));
    }

    public Boolean isIgnorableType(AnnotatedClass paramAnnotatedClass)
    {
      Boolean localBoolean = this._primary.isIgnorableType(paramAnnotatedClass);
      if (localBoolean == null)
        localBoolean = this._secondary.isIgnorableType(paramAnnotatedClass);
      return localBoolean;
    }
  }

  public static class ReferenceProperty
  {
    private final String _name;
    private final Type _type;

    public ReferenceProperty(Type paramType, String paramString)
    {
      this._type = paramType;
      this._name = paramString;
    }

    public static ReferenceProperty back(String paramString)
    {
      return new ReferenceProperty(Type.BACK_REFERENCE, paramString);
    }

    public static ReferenceProperty managed(String paramString)
    {
      return new ReferenceProperty(Type.MANAGED_REFERENCE, paramString);
    }

    public String getName()
    {
      return this._name;
    }

    public Type getType()
    {
      return this._type;
    }

    public boolean isBackReference()
    {
      return this._type == Type.BACK_REFERENCE;
    }

    public boolean isManagedReference()
    {
      return this._type == Type.MANAGED_REFERENCE;
    }

    public static enum Type
    {
      static
      {
        BACK_REFERENCE = new Type("BACK_REFERENCE", 1);
        Type[] arrayOfType = new Type[2];
        arrayOfType[0] = MANAGED_REFERENCE;
        arrayOfType[1] = BACK_REFERENCE;
        $VALUES = arrayOfType;
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.AnnotationIntrospector
 * JD-Core Version:    0.6.0
 */