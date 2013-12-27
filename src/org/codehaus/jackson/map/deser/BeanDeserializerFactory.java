package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonDeserializer<Ljava.lang.Object;>;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializers;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BeanDeserializerFactory extends BasicDeserializerFactory
{
  private static final Class<?>[] INIT_CAUSE_PARAMS = { Throwable.class };
  public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(null);
  protected final DeserializerFactory.Config _factoryConfig;

  @Deprecated
  public BeanDeserializerFactory()
  {
    this(null);
  }

  public BeanDeserializerFactory(DeserializerFactory.Config paramConfig)
  {
    if (paramConfig == null)
      paramConfig = new ConfigImpl();
    this._factoryConfig = paramConfig;
  }

  protected void _addDeserializerConstructors(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, VisibilityChecker<?> paramVisibilityChecker, AnnotationIntrospector paramAnnotationIntrospector, CreatorContainer paramCreatorContainer)
    throws JsonMappingException
  {
    Iterator localIterator = paramBasicBeanDescription.getConstructors().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedConstructor localAnnotatedConstructor = (AnnotatedConstructor)localIterator.next();
      int i = localAnnotatedConstructor.getParameterCount();
      if (i < 1)
        continue;
      boolean bool1 = paramAnnotationIntrospector.hasCreatorAnnotation(localAnnotatedConstructor);
      boolean bool2 = paramVisibilityChecker.isCreatorVisible(localAnnotatedConstructor);
      if (i == 1)
      {
        AnnotatedParameter localAnnotatedParameter2 = localAnnotatedConstructor.getParameter(0);
        String str2 = paramAnnotationIntrospector.findPropertyNameForParam(localAnnotatedParameter2);
        if ((str2 == null) || (str2.length() == 0))
        {
          Class localClass = localAnnotatedConstructor.getParameterClass(0);
          if (localClass == String.class)
          {
            if ((!bool1) && (!bool2))
              continue;
            paramCreatorContainer.addStringConstructor(localAnnotatedConstructor);
            continue;
          }
          if ((localClass == Integer.TYPE) || (localClass == Integer.class))
          {
            if ((!bool1) && (!bool2))
              continue;
            paramCreatorContainer.addIntConstructor(localAnnotatedConstructor);
            continue;
          }
          if ((localClass == Long.TYPE) || (localClass == Long.class))
          {
            if ((!bool1) && (!bool2))
              continue;
            paramCreatorContainer.addLongConstructor(localAnnotatedConstructor);
            continue;
          }
          if (!bool1)
            continue;
          paramCreatorContainer.addDelegatingConstructor(localAnnotatedConstructor);
          continue;
        }
        SettableBeanProperty[] arrayOfSettableBeanProperty2 = new SettableBeanProperty[1];
        arrayOfSettableBeanProperty2[0] = constructCreatorProperty(paramDeserializationConfig, paramBasicBeanDescription, str2, 0, localAnnotatedParameter2);
        paramCreatorContainer.addPropertyConstructor(localAnnotatedConstructor, arrayOfSettableBeanProperty2);
        continue;
      }
      if ((!bool1) && (!bool2))
        continue;
      int j = 0;
      int k = 0;
      SettableBeanProperty[] arrayOfSettableBeanProperty1 = new SettableBeanProperty[i];
      for (int m = 0; m < i; m++)
      {
        AnnotatedParameter localAnnotatedParameter1 = localAnnotatedConstructor.getParameter(m);
        String str1;
        int n;
        if (localAnnotatedParameter1 == null)
        {
          str1 = null;
          if ((str1 != null) && (str1.length() != 0))
            break label412;
          n = 1;
          label320: k |= n;
          if (k != 0)
            break label418;
        }
        label412: label418: for (int i1 = 1; ; i1 = 0)
        {
          j |= i1;
          if ((k == 0) || ((j == 0) && (!bool1)))
            break label424;
          throw new IllegalArgumentException("Argument #" + m + " of constructor " + localAnnotatedConstructor + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
          str1 = paramAnnotationIntrospector.findPropertyNameForParam(localAnnotatedParameter1);
          break;
          n = 0;
          break label320;
        }
        label424: arrayOfSettableBeanProperty1[m] = constructCreatorProperty(paramDeserializationConfig, paramBasicBeanDescription, str1, m, localAnnotatedParameter1);
      }
      if (j == 0)
        continue;
      paramCreatorContainer.addPropertyConstructor(localAnnotatedConstructor, arrayOfSettableBeanProperty1);
    }
  }

  protected void _addDeserializerFactoryMethods(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, VisibilityChecker<?> paramVisibilityChecker, AnnotationIntrospector paramAnnotationIntrospector, CreatorContainer paramCreatorContainer)
    throws JsonMappingException
  {
    Iterator localIterator = paramBasicBeanDescription.getFactoryMethods().iterator();
    while (localIterator.hasNext())
    {
      AnnotatedMethod localAnnotatedMethod = (AnnotatedMethod)localIterator.next();
      int i = localAnnotatedMethod.getParameterCount();
      if (i < 1)
        continue;
      boolean bool = paramAnnotationIntrospector.hasCreatorAnnotation(localAnnotatedMethod);
      if (i == 1)
      {
        String str2 = paramAnnotationIntrospector.findPropertyNameForParam(localAnnotatedMethod.getParameter(0));
        if ((str2 == null) || (str2.length() == 0))
        {
          Class localClass = localAnnotatedMethod.getParameterClass(0);
          if (localClass == String.class)
          {
            if ((!bool) && (!paramVisibilityChecker.isCreatorVisible(localAnnotatedMethod)))
              continue;
            paramCreatorContainer.addStringFactory(localAnnotatedMethod);
            continue;
          }
          if ((localClass == Integer.TYPE) || (localClass == Integer.class))
          {
            if ((!bool) && (!paramVisibilityChecker.isCreatorVisible(localAnnotatedMethod)))
              continue;
            paramCreatorContainer.addIntFactory(localAnnotatedMethod);
            continue;
          }
          if ((localClass == Long.TYPE) || (localClass == Long.class))
          {
            if ((!bool) && (!paramVisibilityChecker.isCreatorVisible(localAnnotatedMethod)))
              continue;
            paramCreatorContainer.addLongFactory(localAnnotatedMethod);
            continue;
          }
          if (!paramAnnotationIntrospector.hasCreatorAnnotation(localAnnotatedMethod))
            continue;
          paramCreatorContainer.addDelegatingFactory(localAnnotatedMethod);
          continue;
        }
      }
      else
      {
        if (!paramAnnotationIntrospector.hasCreatorAnnotation(localAnnotatedMethod))
          continue;
      }
      SettableBeanProperty[] arrayOfSettableBeanProperty = new SettableBeanProperty[i];
      for (int j = 0; j < i; j++)
      {
        AnnotatedParameter localAnnotatedParameter = localAnnotatedMethod.getParameter(j);
        String str1 = paramAnnotationIntrospector.findPropertyNameForParam(localAnnotatedParameter);
        if ((str1 == null) || (str1.length() == 0))
          throw new IllegalArgumentException("Argument #" + j + " of factory method " + localAnnotatedMethod + " has no property name annotation; must have when multiple-paramater static method annotated as Creator");
        arrayOfSettableBeanProperty[j] = constructCreatorProperty(paramDeserializationConfig, paramBasicBeanDescription, str1, j, localAnnotatedParameter);
      }
      paramCreatorContainer.addPropertyFactory(localAnnotatedMethod, arrayOfSettableBeanProperty);
    }
  }

  protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType paramArrayType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BeanProperty paramBeanProperty, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findArrayDeserializer(paramArrayType, paramDeserializationConfig, paramDeserializerProvider, paramBeanProperty, paramTypeDeserializer, paramJsonDeserializer);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType paramJavaType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findBeanDeserializer(paramJavaType, paramDeserializationConfig, paramDeserializerProvider, paramBasicBeanDescription, paramBeanProperty);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType paramCollectionType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findCollectionDeserializer(paramCollectionType, paramDeserializationConfig, paramDeserializerProvider, paramBasicBeanDescription, paramBeanProperty, paramTypeDeserializer, paramJsonDeserializer);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType paramCollectionLikeType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findCollectionLikeDeserializer(paramCollectionLikeType, paramDeserializationConfig, paramDeserializerProvider, paramBasicBeanDescription, paramBeanProperty, paramTypeDeserializer, paramJsonDeserializer);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> paramClass, DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findEnumDeserializer(paramClass, paramDeserializationConfig, paramBasicBeanDescription, paramBeanProperty);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JsonDeserializer<?> _findCustomMapDeserializer(MapType paramMapType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, KeyDeserializer paramKeyDeserializer, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findMapDeserializer(paramMapType, paramDeserializationConfig, paramDeserializerProvider, paramBasicBeanDescription, paramBeanProperty, paramKeyDeserializer, paramTypeDeserializer, paramJsonDeserializer);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType paramMapLikeType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, KeyDeserializer paramKeyDeserializer, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findMapLikeDeserializer(paramMapLikeType, paramDeserializationConfig, paramDeserializerProvider, paramBasicBeanDescription, paramBeanProperty, paramKeyDeserializer, paramTypeDeserializer, paramJsonDeserializer);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> paramClass, DeserializationConfig paramDeserializationConfig, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    Iterator localIterator = this._factoryConfig.deserializers().iterator();
    while (localIterator.hasNext())
    {
      JsonDeserializer localJsonDeserializer = ((Deserializers)localIterator.next()).findTreeNodeDeserializer(paramClass, paramDeserializationConfig, paramBeanProperty);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return null;
  }

  protected JavaType _mapAbstractType2(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
    throws JsonMappingException
  {
    Class localClass = paramJavaType.getRawClass();
    JavaType localJavaType;
    if (this._factoryConfig.hasAbstractTypeResolvers())
    {
      Iterator localIterator = this._factoryConfig.abstractTypeResolvers().iterator();
      do
      {
        if (!localIterator.hasNext())
          break;
        localJavaType = ((AbstractTypeResolver)localIterator.next()).findTypeMapping(paramDeserializationConfig, paramJavaType);
      }
      while ((localJavaType == null) || (localJavaType.getRawClass() == localClass));
    }
    do
    {
      return localJavaType;
      AbstractTypeResolver localAbstractTypeResolver = paramDeserializationConfig.getAbstractTypeResolver();
      if (localAbstractTypeResolver == null)
        break;
      localJavaType = localAbstractTypeResolver.findTypeMapping(paramDeserializationConfig, paramJavaType);
    }
    while ((localJavaType != null) && (localJavaType.getRawClass() != localClass));
    return null;
  }

  protected void addBeanProps(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, BeanDeserializerBuilder paramBeanDeserializerBuilder)
    throws JsonMappingException
  {
    VisibilityChecker localVisibilityChecker1 = paramDeserializationConfig.getDefaultVisibilityChecker();
    if (!paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_SETTERS))
      localVisibilityChecker1 = localVisibilityChecker1.withSetterVisibility(JsonAutoDetect.Visibility.NONE);
    if (!paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_FIELDS))
      localVisibilityChecker1 = localVisibilityChecker1.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
    VisibilityChecker localVisibilityChecker2 = paramDeserializationConfig.getAnnotationIntrospector().findAutoDetectVisibility(paramBasicBeanDescription.getClassInfo(), localVisibilityChecker1);
    LinkedHashMap localLinkedHashMap = paramBasicBeanDescription.findSetters(localVisibilityChecker2);
    AnnotatedMethod localAnnotatedMethod1 = paramBasicBeanDescription.findAnySetter();
    AnnotationIntrospector localAnnotationIntrospector = paramDeserializationConfig.getAnnotationIntrospector();
    Boolean localBoolean = localAnnotationIntrospector.findIgnoreUnknownProperties(paramBasicBeanDescription.getClassInfo());
    if (localBoolean != null)
      paramBeanDeserializerBuilder.setIgnoreUnknownProperties(localBoolean.booleanValue());
    HashSet localHashSet1 = ArrayBuilders.arrayToSet(localAnnotationIntrospector.findPropertiesToIgnore(paramBasicBeanDescription.getClassInfo()));
    Iterator localIterator1 = localHashSet1.iterator();
    while (localIterator1.hasNext())
      paramBeanDeserializerBuilder.addIgnorable((String)localIterator1.next());
    AnnotatedClass localAnnotatedClass = paramBasicBeanDescription.getClassInfo();
    Iterator localIterator2 = localAnnotatedClass.ignoredMemberMethods().iterator();
    while (localIterator2.hasNext())
    {
      String str4 = paramBasicBeanDescription.okNameForSetter((AnnotatedMethod)localIterator2.next());
      if (str4 == null)
        continue;
      paramBeanDeserializerBuilder.addIgnorable(str4);
    }
    Iterator localIterator3 = localAnnotatedClass.ignoredFields().iterator();
    while (localIterator3.hasNext())
      paramBeanDeserializerBuilder.addIgnorable(((AnnotatedField)localIterator3.next()).getName());
    HashMap localHashMap = new HashMap();
    Iterator localIterator4 = localLinkedHashMap.entrySet().iterator();
    while (localIterator4.hasNext())
    {
      Map.Entry localEntry3 = (Map.Entry)localIterator4.next();
      String str3 = (String)localEntry3.getKey();
      if (localHashSet1.contains(str3))
        continue;
      AnnotatedMethod localAnnotatedMethod3 = (AnnotatedMethod)localEntry3.getValue();
      if (isIgnorableType(paramDeserializationConfig, paramBasicBeanDescription, localAnnotatedMethod3.getParameterClass(0), localHashMap))
      {
        paramBeanDeserializerBuilder.addIgnorable(str3);
        continue;
      }
      SettableBeanProperty localSettableBeanProperty2 = constructSettableProperty(paramDeserializationConfig, paramBasicBeanDescription, str3, localAnnotatedMethod3);
      if (localSettableBeanProperty2 == null)
        continue;
      paramBeanDeserializerBuilder.addProperty(localSettableBeanProperty2);
    }
    if (localAnnotatedMethod1 != null)
      paramBeanDeserializerBuilder.setAnySetter(constructAnySetter(paramDeserializationConfig, paramBasicBeanDescription, localAnnotatedMethod1));
    HashSet localHashSet2 = new HashSet(localLinkedHashMap.keySet());
    Iterator localIterator5 = paramBasicBeanDescription.findDeserializableFields(localVisibilityChecker2, localHashSet2).entrySet().iterator();
    while (localIterator5.hasNext())
    {
      Map.Entry localEntry2 = (Map.Entry)localIterator5.next();
      String str2 = (String)localEntry2.getKey();
      if ((localHashSet1.contains(str2)) || (paramBeanDeserializerBuilder.hasProperty(str2)))
        continue;
      AnnotatedField localAnnotatedField = (AnnotatedField)localEntry2.getValue();
      if (isIgnorableType(paramDeserializationConfig, paramBasicBeanDescription, localAnnotatedField.getRawType(), localHashMap))
      {
        paramBeanDeserializerBuilder.addIgnorable(str2);
        continue;
      }
      SettableBeanProperty localSettableBeanProperty1 = constructSettableProperty(paramDeserializationConfig, paramBasicBeanDescription, str2, localAnnotatedField);
      if (localSettableBeanProperty1 == null)
        continue;
      paramBeanDeserializerBuilder.addProperty(localSettableBeanProperty1);
      localHashSet2.add(str2);
    }
    if (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS))
    {
      Iterator localIterator6 = paramBasicBeanDescription.findGetters(localVisibilityChecker2, localHashSet2).entrySet().iterator();
      while (localIterator6.hasNext())
      {
        Map.Entry localEntry1 = (Map.Entry)localIterator6.next();
        AnnotatedMethod localAnnotatedMethod2 = (AnnotatedMethod)localEntry1.getValue();
        Class localClass = localAnnotatedMethod2.getRawType();
        if ((!Collection.class.isAssignableFrom(localClass)) && (!Map.class.isAssignableFrom(localClass)))
          continue;
        String str1 = (String)localEntry1.getKey();
        if ((localHashSet1.contains(str1)) || (paramBeanDeserializerBuilder.hasProperty(str1)))
          continue;
        paramBeanDeserializerBuilder.addProperty(constructSetterlessProperty(paramDeserializationConfig, paramBasicBeanDescription, str1, localAnnotatedMethod2));
        localHashSet2.add(str1);
      }
    }
  }

  protected void addReferenceProperties(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, BeanDeserializerBuilder paramBeanDeserializerBuilder)
    throws JsonMappingException
  {
    Map localMap = paramBasicBeanDescription.findBackReferenceProperties();
    if (localMap != null)
    {
      Iterator localIterator = localMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str = (String)localEntry.getKey();
        AnnotatedMember localAnnotatedMember = (AnnotatedMember)localEntry.getValue();
        if ((localAnnotatedMember instanceof AnnotatedMethod))
        {
          paramBeanDeserializerBuilder.addBackReferenceProperty(str, constructSettableProperty(paramDeserializationConfig, paramBasicBeanDescription, localAnnotatedMember.getName(), (AnnotatedMethod)localAnnotatedMember));
          continue;
        }
        paramBeanDeserializerBuilder.addBackReferenceProperty(str, constructSettableProperty(paramDeserializationConfig, paramBasicBeanDescription, localAnnotatedMember.getName(), (AnnotatedField)localAnnotatedMember));
      }
    }
  }

  public JsonDeserializer<Object> buildBeanDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    BeanDeserializerBuilder localBeanDeserializerBuilder = constructBeanDeserializerBuilder(paramBasicBeanDescription);
    localBeanDeserializerBuilder.setCreators(findDeserializerCreators(paramDeserializationConfig, paramBasicBeanDescription));
    addBeanProps(paramDeserializationConfig, paramBasicBeanDescription, localBeanDeserializerBuilder);
    addReferenceProperties(paramDeserializationConfig, paramBasicBeanDescription, localBeanDeserializerBuilder);
    if (this._factoryConfig.hasDeserializerModifiers())
    {
      Iterator localIterator2 = this._factoryConfig.deserializerModifiers().iterator();
      while (localIterator2.hasNext())
        localBeanDeserializerBuilder = ((BeanDeserializerModifier)localIterator2.next()).updateBuilder(paramDeserializationConfig, paramBasicBeanDescription, localBeanDeserializerBuilder);
    }
    JsonDeserializer localJsonDeserializer = localBeanDeserializerBuilder.build(paramBeanProperty);
    if (this._factoryConfig.hasDeserializerModifiers())
    {
      Iterator localIterator1 = this._factoryConfig.deserializerModifiers().iterator();
      while (localIterator1.hasNext())
        localJsonDeserializer = ((BeanDeserializerModifier)localIterator1.next()).modifyDeserializer(paramDeserializationConfig, paramBasicBeanDescription, localJsonDeserializer);
    }
    return localJsonDeserializer;
  }

  public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    BeanDeserializerBuilder localBeanDeserializerBuilder = constructBeanDeserializerBuilder(paramBasicBeanDescription);
    localBeanDeserializerBuilder.setCreators(findDeserializerCreators(paramDeserializationConfig, paramBasicBeanDescription));
    addBeanProps(paramDeserializationConfig, paramBasicBeanDescription, localBeanDeserializerBuilder);
    AnnotatedMethod localAnnotatedMethod = paramBasicBeanDescription.findMethod("initCause", INIT_CAUSE_PARAMS);
    if (localAnnotatedMethod != null)
    {
      SettableBeanProperty localSettableBeanProperty = constructSettableProperty(paramDeserializationConfig, paramBasicBeanDescription, "cause", localAnnotatedMethod);
      if (localSettableBeanProperty != null)
        localBeanDeserializerBuilder.addProperty(localSettableBeanProperty);
    }
    localBeanDeserializerBuilder.addIgnorable("localizedMessage");
    localBeanDeserializerBuilder.addIgnorable("message");
    if (this._factoryConfig.hasDeserializerModifiers())
    {
      Iterator localIterator2 = this._factoryConfig.deserializerModifiers().iterator();
      while (localIterator2.hasNext())
        localBeanDeserializerBuilder = ((BeanDeserializerModifier)localIterator2.next()).updateBuilder(paramDeserializationConfig, paramBasicBeanDescription, localBeanDeserializerBuilder);
    }
    Object localObject = localBeanDeserializerBuilder.build(paramBeanProperty);
    if ((localObject instanceof BeanDeserializer))
      localObject = new ThrowableDeserializer((BeanDeserializer)localObject);
    if (this._factoryConfig.hasDeserializerModifiers())
    {
      Iterator localIterator1 = this._factoryConfig.deserializerModifiers().iterator();
      while (localIterator1.hasNext())
        localObject = ((BeanDeserializerModifier)localIterator1.next()).modifyDeserializer(paramDeserializationConfig, paramBasicBeanDescription, (JsonDeserializer)localObject);
    }
    return (JsonDeserializer<Object>)localObject;
  }

  protected SettableAnyProperty constructAnySetter(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, AnnotatedMethod paramAnnotatedMethod)
    throws JsonMappingException
  {
    if (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS))
      paramAnnotatedMethod.fixAccess();
    JavaType localJavaType1 = paramBasicBeanDescription.bindingsForBeanType().resolveType(paramAnnotatedMethod.getParameterType(1));
    BeanProperty.Std localStd = new BeanProperty.Std(paramAnnotatedMethod.getName(), localJavaType1, paramBasicBeanDescription.getClassAnnotations(), paramAnnotatedMethod);
    JavaType localJavaType2 = resolveType(paramDeserializationConfig, paramBasicBeanDescription, localJavaType1, paramAnnotatedMethod, localStd);
    JsonDeserializer localJsonDeserializer = findDeserializerFromAnnotation(paramDeserializationConfig, paramAnnotatedMethod, localStd);
    if (localJsonDeserializer != null)
    {
      SettableAnyProperty localSettableAnyProperty = new SettableAnyProperty(localStd, paramAnnotatedMethod, localJavaType2);
      localSettableAnyProperty.setValueDeserializer(localJsonDeserializer);
      return localSettableAnyProperty;
    }
    return new SettableAnyProperty(localStd, paramAnnotatedMethod, modifyTypeByAnnotation(paramDeserializationConfig, paramAnnotatedMethod, localJavaType2, localStd.getName()));
  }

  protected BeanDeserializerBuilder constructBeanDeserializerBuilder(BasicBeanDescription paramBasicBeanDescription)
  {
    return new BeanDeserializerBuilder(paramBasicBeanDescription);
  }

  protected SettableBeanProperty constructSettableProperty(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, String paramString, AnnotatedField paramAnnotatedField)
    throws JsonMappingException
  {
    if (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS))
      paramAnnotatedField.fixAccess();
    JavaType localJavaType1 = paramBasicBeanDescription.bindingsForBeanType().resolveType(paramAnnotatedField.getGenericType());
    BeanProperty.Std localStd = new BeanProperty.Std(paramString, localJavaType1, paramBasicBeanDescription.getClassAnnotations(), paramAnnotatedField);
    JavaType localJavaType2 = resolveType(paramDeserializationConfig, paramBasicBeanDescription, localJavaType1, paramAnnotatedField, localStd);
    if (localJavaType2 != localJavaType1)
      localStd = localStd.withType(localJavaType2);
    JsonDeserializer localJsonDeserializer = findDeserializerFromAnnotation(paramDeserializationConfig, paramAnnotatedField, localStd);
    JavaType localJavaType3 = modifyTypeByAnnotation(paramDeserializationConfig, paramAnnotatedField, localJavaType2, paramString);
    SettableBeanProperty.FieldProperty localFieldProperty = new SettableBeanProperty.FieldProperty(paramString, localJavaType3, (TypeDeserializer)localJavaType3.getTypeHandler(), paramBasicBeanDescription.getClassAnnotations(), paramAnnotatedField);
    if (localJsonDeserializer != null)
      localFieldProperty.setValueDeserializer(localJsonDeserializer);
    AnnotationIntrospector.ReferenceProperty localReferenceProperty = paramDeserializationConfig.getAnnotationIntrospector().findReferenceType(paramAnnotatedField);
    if ((localReferenceProperty != null) && (localReferenceProperty.isManagedReference()))
      localFieldProperty.setManagedReferenceName(localReferenceProperty.getName());
    return localFieldProperty;
  }

  protected SettableBeanProperty constructSettableProperty(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, String paramString, AnnotatedMethod paramAnnotatedMethod)
    throws JsonMappingException
  {
    if (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS))
      paramAnnotatedMethod.fixAccess();
    JavaType localJavaType1 = paramBasicBeanDescription.bindingsForBeanType().resolveType(paramAnnotatedMethod.getParameterType(0));
    BeanProperty.Std localStd = new BeanProperty.Std(paramString, localJavaType1, paramBasicBeanDescription.getClassAnnotations(), paramAnnotatedMethod);
    JavaType localJavaType2 = resolveType(paramDeserializationConfig, paramBasicBeanDescription, localJavaType1, paramAnnotatedMethod, localStd);
    if (localJavaType2 != localJavaType1)
      localStd = localStd.withType(localJavaType2);
    JsonDeserializer localJsonDeserializer = findDeserializerFromAnnotation(paramDeserializationConfig, paramAnnotatedMethod, localStd);
    JavaType localJavaType3 = modifyTypeByAnnotation(paramDeserializationConfig, paramAnnotatedMethod, localJavaType2, paramString);
    SettableBeanProperty.MethodProperty localMethodProperty = new SettableBeanProperty.MethodProperty(paramString, localJavaType3, (TypeDeserializer)localJavaType3.getTypeHandler(), paramBasicBeanDescription.getClassAnnotations(), paramAnnotatedMethod);
    if (localJsonDeserializer != null)
      localMethodProperty.setValueDeserializer(localJsonDeserializer);
    AnnotationIntrospector.ReferenceProperty localReferenceProperty = paramDeserializationConfig.getAnnotationIntrospector().findReferenceType(paramAnnotatedMethod);
    if ((localReferenceProperty != null) && (localReferenceProperty.isManagedReference()))
      localMethodProperty.setManagedReferenceName(localReferenceProperty.getName());
    return localMethodProperty;
  }

  protected SettableBeanProperty constructSetterlessProperty(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, String paramString, AnnotatedMethod paramAnnotatedMethod)
    throws JsonMappingException
  {
    if (paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS))
      paramAnnotatedMethod.fixAccess();
    JavaType localJavaType1 = paramAnnotatedMethod.getType(paramBasicBeanDescription.bindingsForBeanType());
    JsonDeserializer localJsonDeserializer = findDeserializerFromAnnotation(paramDeserializationConfig, paramAnnotatedMethod, new BeanProperty.Std(paramString, localJavaType1, paramBasicBeanDescription.getClassAnnotations(), paramAnnotatedMethod));
    JavaType localJavaType2 = modifyTypeByAnnotation(paramDeserializationConfig, paramAnnotatedMethod, localJavaType1, paramString);
    SettableBeanProperty.SetterlessProperty localSetterlessProperty = new SettableBeanProperty.SetterlessProperty(paramString, localJavaType2, (TypeDeserializer)localJavaType2.getTypeHandler(), paramBasicBeanDescription.getClassAnnotations(), paramAnnotatedMethod);
    if (localJsonDeserializer != null)
      localSetterlessProperty.setValueDeserializer(localJsonDeserializer);
    return localSetterlessProperty;
  }

  public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    if (paramJavaType.isAbstract())
      paramJavaType = mapAbstractType(paramDeserializationConfig, paramJavaType);
    BasicBeanDescription localBasicBeanDescription = (BasicBeanDescription)paramDeserializationConfig.introspect(paramJavaType);
    JsonDeserializer localJsonDeserializer1 = findDeserializerFromAnnotation(paramDeserializationConfig, localBasicBeanDescription.getClassInfo(), paramBeanProperty);
    if (localJsonDeserializer1 != null)
      return localJsonDeserializer1;
    JavaType localJavaType1 = modifyTypeByAnnotation(paramDeserializationConfig, localBasicBeanDescription.getClassInfo(), paramJavaType, null);
    if (localJavaType1.getRawClass() != paramJavaType.getRawClass())
    {
      paramJavaType = localJavaType1;
      localBasicBeanDescription = (BasicBeanDescription)paramDeserializationConfig.introspect(paramJavaType);
    }
    JsonDeserializer localJsonDeserializer2 = _findCustomBeanDeserializer(paramJavaType, paramDeserializationConfig, paramDeserializerProvider, localBasicBeanDescription, paramBeanProperty);
    if (localJsonDeserializer2 != null)
      return localJsonDeserializer2;
    if (paramJavaType.isThrowable())
      return buildThrowableDeserializer(paramDeserializationConfig, paramJavaType, localBasicBeanDescription, paramBeanProperty);
    if (paramJavaType.isAbstract())
    {
      JavaType localJavaType2 = materializeAbstractType(paramDeserializationConfig, localBasicBeanDescription);
      if (localJavaType2 != null)
        return buildBeanDeserializer(paramDeserializationConfig, localJavaType2, (BasicBeanDescription)paramDeserializationConfig.introspect(localJavaType2), paramBeanProperty);
    }
    JsonDeserializer localJsonDeserializer3 = findStdBeanDeserializer(paramDeserializationConfig, paramDeserializerProvider, paramJavaType, paramBeanProperty);
    if (localJsonDeserializer3 != null)
      return localJsonDeserializer3;
    if (!isPotentialBeanType(paramJavaType.getRawClass()))
      return null;
    if (paramJavaType.isAbstract())
      return new AbstractDeserializer(paramJavaType);
    return buildBeanDeserializer(paramDeserializationConfig, paramJavaType, localBasicBeanDescription, paramBeanProperty);
  }

  public KeyDeserializer createKeyDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    if (this._factoryConfig.hasKeyDeserializers())
    {
      BasicBeanDescription localBasicBeanDescription = (BasicBeanDescription)paramDeserializationConfig.introspectClassAnnotations(paramJavaType.getRawClass());
      Iterator localIterator = this._factoryConfig.keyDeserializers().iterator();
      while (localIterator.hasNext())
      {
        KeyDeserializer localKeyDeserializer = ((KeyDeserializers)localIterator.next()).findKeyDeserializer(paramJavaType, paramDeserializationConfig, localBasicBeanDescription, paramBeanProperty);
        if (localKeyDeserializer != null)
          return localKeyDeserializer;
      }
    }
    return null;
  }

  protected CreatorContainer findDeserializerCreators(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription)
    throws JsonMappingException
  {
    boolean bool = paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
    CreatorContainer localCreatorContainer = new CreatorContainer(paramBasicBeanDescription, bool);
    AnnotationIntrospector localAnnotationIntrospector = paramDeserializationConfig.getAnnotationIntrospector();
    if (paramBasicBeanDescription.getType().isConcrete())
    {
      Constructor localConstructor = paramBasicBeanDescription.findDefaultConstructor();
      if (localConstructor != null)
      {
        if (bool)
          ClassUtil.checkAndFixAccess(localConstructor);
        localCreatorContainer.setDefaultConstructor(localConstructor);
      }
    }
    VisibilityChecker localVisibilityChecker1 = paramDeserializationConfig.getDefaultVisibilityChecker();
    if (!paramDeserializationConfig.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_CREATORS))
      localVisibilityChecker1 = localVisibilityChecker1.withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
    VisibilityChecker localVisibilityChecker2 = paramDeserializationConfig.getAnnotationIntrospector().findAutoDetectVisibility(paramBasicBeanDescription.getClassInfo(), localVisibilityChecker1);
    _addDeserializerConstructors(paramDeserializationConfig, paramBasicBeanDescription, localVisibilityChecker2, localAnnotationIntrospector, localCreatorContainer);
    _addDeserializerFactoryMethods(paramDeserializationConfig, paramBasicBeanDescription, localVisibilityChecker2, localAnnotationIntrospector, localCreatorContainer);
    return localCreatorContainer;
  }

  public final DeserializerFactory.Config getConfig()
  {
    return this._factoryConfig;
  }

  protected boolean isIgnorableType(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, Class<?> paramClass, Map<Class<?>, Boolean> paramMap)
  {
    Boolean localBoolean = (Boolean)paramMap.get(paramClass);
    if (localBoolean == null)
    {
      BasicBeanDescription localBasicBeanDescription = (BasicBeanDescription)paramDeserializationConfig.introspectClassAnnotations(paramClass);
      localBoolean = paramDeserializationConfig.getAnnotationIntrospector().isIgnorableType(localBasicBeanDescription.getClassInfo());
      if (localBoolean == null)
        localBoolean = Boolean.FALSE;
    }
    return localBoolean.booleanValue();
  }

  protected boolean isPotentialBeanType(Class<?> paramClass)
  {
    String str1 = ClassUtil.canBeABeanType(paramClass);
    if (str1 != null)
      throw new IllegalArgumentException("Can not deserialize Class " + paramClass.getName() + " (of type " + str1 + ") as a Bean");
    if (ClassUtil.isProxyType(paramClass))
      throw new IllegalArgumentException("Can not deserialize Proxy class " + paramClass.getName() + " as a Bean");
    String str2 = ClassUtil.isLocalType(paramClass);
    if (str2 != null)
      throw new IllegalArgumentException("Can not deserialize Class " + paramClass.getName() + " (of type " + str2 + ") as a Bean");
    return true;
  }

  protected JavaType mapAbstractType(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType)
    throws JsonMappingException
  {
    while (true)
    {
      JavaType localJavaType = _mapAbstractType2(paramDeserializationConfig, paramJavaType);
      if (localJavaType == null)
        return paramJavaType;
      Class localClass1 = paramJavaType.getRawClass();
      Class localClass2 = localJavaType.getRawClass();
      if ((localClass1 == localClass2) || (!localClass1.isAssignableFrom(localClass2)))
        throw new IllegalArgumentException("Invalid abstract type resolution from " + paramJavaType + " to " + localJavaType + ": latter is not a subtype of former");
      paramJavaType = localJavaType;
    }
  }

  protected JavaType materializeAbstractType(DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription)
    throws JsonMappingException
  {
    AbstractTypeResolver localAbstractTypeResolver = paramDeserializationConfig.getAbstractTypeResolver();
    JavaType localJavaType3;
    if ((localAbstractTypeResolver == null) && (!this._factoryConfig.hasAbstractTypeResolvers()))
      localJavaType3 = null;
    JavaType localJavaType1;
    do
    {
      return localJavaType3;
      localJavaType1 = paramBasicBeanDescription.getType();
      if (paramDeserializationConfig.getAnnotationIntrospector().findTypeResolver(paramDeserializationConfig, paramBasicBeanDescription.getClassInfo(), localJavaType1) != null)
        return null;
      if (localAbstractTypeResolver == null)
        break;
      localJavaType3 = localAbstractTypeResolver.resolveAbstractType(paramDeserializationConfig, localJavaType1);
    }
    while (localJavaType3 != null);
    Iterator localIterator = this._factoryConfig.abstractTypeResolvers().iterator();
    while (localIterator.hasNext())
    {
      JavaType localJavaType2 = ((AbstractTypeResolver)localIterator.next()).resolveAbstractType(paramDeserializationConfig, localJavaType1);
      if (localJavaType2 != null)
        return localJavaType2;
    }
    return null;
  }

  public DeserializerFactory withConfig(DeserializerFactory.Config paramConfig)
  {
    if (this._factoryConfig == paramConfig)
      return this;
    if (getClass() != BeanDeserializerFactory.class)
      throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
    return new BeanDeserializerFactory(paramConfig);
  }

  public static class ConfigImpl extends DeserializerFactory.Config
  {
    protected static final AbstractTypeResolver[] NO_ABSTRACT_TYPE_RESOLVERS;
    protected static final KeyDeserializers[] NO_KEY_DESERIALIZERS = new KeyDeserializers[0];
    protected static final BeanDeserializerModifier[] NO_MODIFIERS = new BeanDeserializerModifier[0];
    protected final AbstractTypeResolver[] _abstractTypeResolvers;
    protected final Deserializers[] _additionalDeserializers;
    protected final KeyDeserializers[] _additionalKeyDeserializers;
    protected final BeanDeserializerModifier[] _modifiers;

    static
    {
      NO_ABSTRACT_TYPE_RESOLVERS = new AbstractTypeResolver[0];
    }

    public ConfigImpl()
    {
      this(null, null, null, null);
    }

    protected ConfigImpl(Deserializers[] paramArrayOfDeserializers, KeyDeserializers[] paramArrayOfKeyDeserializers, BeanDeserializerModifier[] paramArrayOfBeanDeserializerModifier, AbstractTypeResolver[] paramArrayOfAbstractTypeResolver)
    {
      if (paramArrayOfDeserializers == null)
        paramArrayOfDeserializers = BeanDeserializerFactory.NO_DESERIALIZERS;
      this._additionalDeserializers = paramArrayOfDeserializers;
      if (paramArrayOfKeyDeserializers == null)
        paramArrayOfKeyDeserializers = NO_KEY_DESERIALIZERS;
      this._additionalKeyDeserializers = paramArrayOfKeyDeserializers;
      if (paramArrayOfBeanDeserializerModifier == null)
        paramArrayOfBeanDeserializerModifier = NO_MODIFIERS;
      this._modifiers = paramArrayOfBeanDeserializerModifier;
      if (paramArrayOfAbstractTypeResolver == null)
        paramArrayOfAbstractTypeResolver = NO_ABSTRACT_TYPE_RESOLVERS;
      this._abstractTypeResolvers = paramArrayOfAbstractTypeResolver;
    }

    public Iterable<AbstractTypeResolver> abstractTypeResolvers()
    {
      return ArrayBuilders.arrayAsIterable(this._abstractTypeResolvers);
    }

    public Iterable<BeanDeserializerModifier> deserializerModifiers()
    {
      return ArrayBuilders.arrayAsIterable(this._modifiers);
    }

    public Iterable<Deserializers> deserializers()
    {
      return ArrayBuilders.arrayAsIterable(this._additionalDeserializers);
    }

    public boolean hasAbstractTypeResolvers()
    {
      return this._abstractTypeResolvers.length > 0;
    }

    public boolean hasDeserializerModifiers()
    {
      return this._modifiers.length > 0;
    }

    public boolean hasDeserializers()
    {
      return this._additionalDeserializers.length > 0;
    }

    public boolean hasKeyDeserializers()
    {
      return this._additionalKeyDeserializers.length > 0;
    }

    public Iterable<KeyDeserializers> keyDeserializers()
    {
      return ArrayBuilders.arrayAsIterable(this._additionalKeyDeserializers);
    }

    public DeserializerFactory.Config withAbstractTypeResolver(AbstractTypeResolver paramAbstractTypeResolver)
    {
      if (paramAbstractTypeResolver == null)
        throw new IllegalArgumentException("Can not pass null resolver");
      AbstractTypeResolver[] arrayOfAbstractTypeResolver = (AbstractTypeResolver[])ArrayBuilders.insertInListNoDup(this._abstractTypeResolvers, paramAbstractTypeResolver);
      return new ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, this._modifiers, arrayOfAbstractTypeResolver);
    }

    public DeserializerFactory.Config withAdditionalDeserializers(Deserializers paramDeserializers)
    {
      if (paramDeserializers == null)
        throw new IllegalArgumentException("Can not pass null Deserializers");
      return new ConfigImpl((Deserializers[])ArrayBuilders.insertInListNoDup(this._additionalDeserializers, paramDeserializers), this._additionalKeyDeserializers, this._modifiers, this._abstractTypeResolvers);
    }

    public DeserializerFactory.Config withAdditionalKeyDeserializers(KeyDeserializers paramKeyDeserializers)
    {
      if (paramKeyDeserializers == null)
        throw new IllegalArgumentException("Can not pass null KeyDeserializers");
      KeyDeserializers[] arrayOfKeyDeserializers = (KeyDeserializers[])ArrayBuilders.insertInListNoDup(this._additionalKeyDeserializers, paramKeyDeserializers);
      return new ConfigImpl(this._additionalDeserializers, arrayOfKeyDeserializers, this._modifiers, this._abstractTypeResolvers);
    }

    public DeserializerFactory.Config withDeserializerModifier(BeanDeserializerModifier paramBeanDeserializerModifier)
    {
      if (paramBeanDeserializerModifier == null)
        throw new IllegalArgumentException("Can not pass null modifier");
      BeanDeserializerModifier[] arrayOfBeanDeserializerModifier = (BeanDeserializerModifier[])ArrayBuilders.insertInListNoDup(this._modifiers, paramBeanDeserializerModifier);
      return new ConfigImpl(this._additionalDeserializers, this._additionalKeyDeserializers, arrayOfBeanDeserializerModifier, this._abstractTypeResolvers);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BeanDeserializerFactory
 * JD-Core Version:    0.6.0
 */