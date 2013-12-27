package org.codehaus.jackson.map.ser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.List<Lorg.codehaus.jackson.map.ser.BeanPropertyWriter;>;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.JsonSerializer<Ljava.lang.Object;>;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerFactory.Config;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BeanSerializerFactory extends BasicSerializerFactory
{
  public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);
  protected final SerializerFactory.Config _factoryConfig;

  @Deprecated
  protected BeanSerializerFactory()
  {
    this(null);
  }

  protected BeanSerializerFactory(SerializerFactory.Config paramConfig)
  {
    if (paramConfig == null)
      paramConfig = new ConfigImpl();
    this._factoryConfig = paramConfig;
  }

  protected BeanPropertyWriter _constructWriter(SerializationConfig paramSerializationConfig, TypeBindings paramTypeBindings, PropertyBuilder paramPropertyBuilder, boolean paramBoolean, String paramString, AnnotatedMember paramAnnotatedMember)
    throws JsonMappingException
  {
    if (paramSerializationConfig.isEnabled(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS))
      paramAnnotatedMember.fixAccess();
    JavaType localJavaType = paramAnnotatedMember.getType(paramTypeBindings);
    BeanProperty.Std localStd = new BeanProperty.Std(paramString, localJavaType, paramPropertyBuilder.getClassAnnotations(), paramAnnotatedMember);
    JsonSerializer localJsonSerializer = findSerializerFromAnnotation(paramSerializationConfig, paramAnnotatedMember, localStd);
    boolean bool = ClassUtil.isCollectionMapOrArray(localJavaType.getRawClass());
    TypeSerializer localTypeSerializer = null;
    if (bool)
      localTypeSerializer = findPropertyContentTypeSerializer(localJavaType, paramSerializationConfig, paramAnnotatedMember, localStd);
    BeanPropertyWriter localBeanPropertyWriter = paramPropertyBuilder.buildWriter(paramString, localJavaType, localJsonSerializer, findPropertyTypeSerializer(localJavaType, paramSerializationConfig, paramAnnotatedMember, localStd), localTypeSerializer, paramAnnotatedMember, paramBoolean);
    localBeanPropertyWriter.setViews(paramSerializationConfig.getAnnotationIntrospector().findSerializationViews(paramAnnotatedMember));
    return localBeanPropertyWriter;
  }

  protected List<BeanPropertyWriter> _sortBeanProperties(List<BeanPropertyWriter> paramList, List<String> paramList1, String[] paramArrayOfString, boolean paramBoolean)
  {
    int i = paramList.size();
    if (paramBoolean);
    for (Object localObject = new TreeMap(); ; localObject = new LinkedHashMap(i * 2))
    {
      Iterator localIterator1 = paramList.iterator();
      while (localIterator1.hasNext())
      {
        BeanPropertyWriter localBeanPropertyWriter3 = (BeanPropertyWriter)localIterator1.next();
        ((Map)localObject).put(localBeanPropertyWriter3.getName(), localBeanPropertyWriter3);
      }
    }
    LinkedHashMap localLinkedHashMap = new LinkedHashMap(i * 2);
    if (paramArrayOfString != null)
    {
      int j = paramArrayOfString.length;
      for (int k = 0; k < j; k++)
      {
        String str2 = paramArrayOfString[k];
        BeanPropertyWriter localBeanPropertyWriter2 = (BeanPropertyWriter)((Map)localObject).get(str2);
        if (localBeanPropertyWriter2 == null)
          continue;
        localLinkedHashMap.put(str2, localBeanPropertyWriter2);
      }
    }
    Iterator localIterator2 = paramList1.iterator();
    while (localIterator2.hasNext())
    {
      String str1 = (String)localIterator2.next();
      BeanPropertyWriter localBeanPropertyWriter1 = (BeanPropertyWriter)((Map)localObject).get(str1);
      if (localBeanPropertyWriter1 == null)
        continue;
      localLinkedHashMap.put(str1, localBeanPropertyWriter1);
    }
    localLinkedHashMap.putAll((Map)localObject);
    return (List<BeanPropertyWriter>)new ArrayList(localLinkedHashMap.values());
  }

  protected JsonSerializer<Object> constructBeanSerializer(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    if (paramBasicBeanDescription.getBeanClass() == Object.class)
      throw new IllegalArgumentException("Can not create bean serializer for Object.class");
    BeanSerializerBuilder localBeanSerializerBuilder = constructBeanSerializerBuilder(paramBasicBeanDescription);
    Object localObject = findBeanProperties(paramSerializationConfig, paramBasicBeanDescription);
    AnnotatedMethod localAnnotatedMethod = paramBasicBeanDescription.findAnyGetter();
    if (this._factoryConfig.hasSerializerModifiers())
    {
      if (localObject == null)
        localObject = new ArrayList();
      Iterator localIterator3 = this._factoryConfig.serializerModifiers().iterator();
      while (localIterator3.hasNext())
        localObject = ((BeanSerializerModifier)localIterator3.next()).changeProperties(paramSerializationConfig, paramBasicBeanDescription, (List)localObject);
    }
    if ((localObject == null) || (((List)localObject).size() == 0))
      if (localAnnotatedMethod == null)
      {
        if (paramBasicBeanDescription.hasKnownClassAnnotations())
          return localBeanSerializerBuilder.createDummy();
        return null;
      }
    for (List localList = Collections.emptyList(); this._factoryConfig.hasSerializerModifiers(); localList = sortBeanProperties(paramSerializationConfig, paramBasicBeanDescription, filterBeanProperties(paramSerializationConfig, paramBasicBeanDescription, (List)localObject)))
    {
      Iterator localIterator2 = this._factoryConfig.serializerModifiers().iterator();
      while (localIterator2.hasNext())
        localList = ((BeanSerializerModifier)localIterator2.next()).orderProperties(paramSerializationConfig, paramBasicBeanDescription, localList);
    }
    localBeanSerializerBuilder.setProperties(localList);
    localBeanSerializerBuilder.setFilterId(findFilterId(paramSerializationConfig, paramBasicBeanDescription));
    if (localAnnotatedMethod != null)
    {
      JavaType localJavaType = localAnnotatedMethod.getType(paramBasicBeanDescription.bindingsForBeanType());
      localBeanSerializerBuilder.setAnyGetter(new AnyGetterWriter(localAnnotatedMethod, MapSerializer.construct(null, localJavaType, paramSerializationConfig.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING), createTypeSerializer(paramSerializationConfig, localJavaType.getContentType(), paramBeanProperty), paramBeanProperty, null, null)));
    }
    processViews(paramSerializationConfig, localBeanSerializerBuilder);
    if (this._factoryConfig.hasSerializerModifiers())
    {
      Iterator localIterator1 = this._factoryConfig.serializerModifiers().iterator();
      while (localIterator1.hasNext())
        localBeanSerializerBuilder = ((BeanSerializerModifier)localIterator1.next()).updateBuilder(paramSerializationConfig, paramBasicBeanDescription, localBeanSerializerBuilder);
    }
    return (JsonSerializer<Object>)localBeanSerializerBuilder.build();
  }

  protected BeanSerializerBuilder constructBeanSerializerBuilder(BasicBeanDescription paramBasicBeanDescription)
  {
    return new BeanSerializerBuilder(paramBasicBeanDescription);
  }

  protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter paramBeanPropertyWriter, Class<?>[] paramArrayOfClass)
  {
    return FilteredBeanPropertyWriter.constructViewBased(paramBeanPropertyWriter, paramArrayOfClass);
  }

  protected PropertyBuilder constructPropertyBuilder(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription)
  {
    return new PropertyBuilder(paramSerializationConfig, paramBasicBeanDescription);
  }

  public JsonSerializer<Object> createKeySerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
  {
    JsonSerializer localJsonSerializer;
    if (!this._factoryConfig.hasKeySerializers())
      localJsonSerializer = null;
    do
    {
      BasicBeanDescription localBasicBeanDescription;
      Iterator localIterator;
      while (!localIterator.hasNext())
      {
        return localJsonSerializer;
        localBasicBeanDescription = (BasicBeanDescription)paramSerializationConfig.introspectClassAnnotations(paramJavaType.getRawClass());
        localJsonSerializer = null;
        localIterator = this._factoryConfig.keySerializers().iterator();
      }
      localJsonSerializer = ((Serializers)localIterator.next()).findSerializer(paramSerializationConfig, paramJavaType, localBasicBeanDescription, paramBeanProperty);
    }
    while (localJsonSerializer == null);
    return localJsonSerializer;
  }

  public JsonSerializer<Object> createSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    BasicBeanDescription localBasicBeanDescription = (BasicBeanDescription)paramSerializationConfig.introspect(paramJavaType);
    JsonSerializer localJsonSerializer1 = findSerializerFromAnnotation(paramSerializationConfig, localBasicBeanDescription.getClassInfo(), paramBeanProperty);
    if (localJsonSerializer1 != null)
      return localJsonSerializer1;
    JavaType localJavaType = modifyTypeByAnnotation(paramSerializationConfig, localBasicBeanDescription.getClassInfo(), paramJavaType);
    if (localJavaType != paramJavaType);
    for (boolean bool = true; paramJavaType.isContainerType(); bool = false)
      return buildContainerSerializer(paramSerializationConfig, localJavaType, localBasicBeanDescription, paramBeanProperty, bool);
    Iterator localIterator = this._factoryConfig.serializers().iterator();
    while (localIterator.hasNext())
    {
      JsonSerializer localJsonSerializer5 = ((Serializers)localIterator.next()).findSerializer(paramSerializationConfig, localJavaType, localBasicBeanDescription, paramBeanProperty);
      if (localJsonSerializer5 != null)
        return localJsonSerializer5;
    }
    JsonSerializer localJsonSerializer2 = findSerializerByLookup(localJavaType, paramSerializationConfig, localBasicBeanDescription, paramBeanProperty, bool);
    if (localJsonSerializer2 != null)
      return localJsonSerializer2;
    JsonSerializer localJsonSerializer3 = findSerializerByPrimaryType(localJavaType, paramSerializationConfig, localBasicBeanDescription, paramBeanProperty, bool);
    if (localJsonSerializer3 != null)
      return localJsonSerializer3;
    JsonSerializer localJsonSerializer4 = findBeanSerializer(paramSerializationConfig, localJavaType, localBasicBeanDescription, paramBeanProperty);
    if (localJsonSerializer4 == null)
      localJsonSerializer4 = super.findSerializerByAddonType(paramSerializationConfig, localJavaType, localBasicBeanDescription, paramBeanProperty, bool);
    return localJsonSerializer4;
  }

  protected Iterable<Serializers> customSerializers()
  {
    return this._factoryConfig.serializers();
  }

  protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription, List<BeanPropertyWriter> paramList)
  {
    String[] arrayOfString = paramSerializationConfig.getAnnotationIntrospector().findPropertiesToIgnore(paramBasicBeanDescription.getClassInfo());
    if ((arrayOfString != null) && (arrayOfString.length > 0))
    {
      HashSet localHashSet = ArrayBuilders.arrayToSet(arrayOfString);
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        if (!localHashSet.contains(((BeanPropertyWriter)localIterator.next()).getName()))
          continue;
        localIterator.remove();
      }
    }
    return paramList;
  }

  protected List<BeanPropertyWriter> findBeanProperties(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription)
    throws JsonMappingException
  {
    VisibilityChecker localVisibilityChecker1 = paramSerializationConfig.getDefaultVisibilityChecker();
    if (!paramSerializationConfig.isEnabled(SerializationConfig.Feature.AUTO_DETECT_GETTERS))
    {
      JsonAutoDetect.Visibility localVisibility3 = JsonAutoDetect.Visibility.NONE;
      localVisibilityChecker1 = localVisibilityChecker1.withGetterVisibility(localVisibility3);
    }
    if (!paramSerializationConfig.isEnabled(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS))
    {
      JsonAutoDetect.Visibility localVisibility2 = JsonAutoDetect.Visibility.NONE;
      localVisibilityChecker1 = localVisibilityChecker1.withIsGetterVisibility(localVisibility2);
    }
    if (!paramSerializationConfig.isEnabled(SerializationConfig.Feature.AUTO_DETECT_FIELDS))
    {
      JsonAutoDetect.Visibility localVisibility1 = JsonAutoDetect.Visibility.NONE;
      localVisibilityChecker1 = localVisibilityChecker1.withFieldVisibility(localVisibility1);
    }
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    VisibilityChecker localVisibilityChecker2 = localAnnotationIntrospector.findAutoDetectVisibility(paramBasicBeanDescription.getClassInfo(), localVisibilityChecker1);
    LinkedHashMap localLinkedHashMap1 = paramBasicBeanDescription.findGetters(localVisibilityChecker2, null);
    LinkedHashMap localLinkedHashMap2 = paramBasicBeanDescription.findSerializableFields(localVisibilityChecker2, localLinkedHashMap1.keySet());
    removeIgnorableTypes(paramSerializationConfig, paramBasicBeanDescription, localLinkedHashMap1);
    removeIgnorableTypes(paramSerializationConfig, paramBasicBeanDescription, localLinkedHashMap2);
    ArrayList localArrayList;
    if ((localLinkedHashMap1.isEmpty()) && (localLinkedHashMap2.isEmpty()))
      localArrayList = null;
    while (true)
    {
      return localArrayList;
      boolean bool = usesStaticTyping(paramSerializationConfig, paramBasicBeanDescription, null, null);
      PropertyBuilder localPropertyBuilder = constructPropertyBuilder(paramSerializationConfig, paramBasicBeanDescription);
      int i = localLinkedHashMap1.size();
      localArrayList = new ArrayList(i);
      TypeBindings localTypeBindings = paramBasicBeanDescription.bindingsForBeanType();
      Iterator localIterator1 = localLinkedHashMap2.entrySet().iterator();
      while (localIterator1.hasNext())
      {
        Map.Entry localEntry2 = (Map.Entry)localIterator1.next();
        AnnotationIntrospector.ReferenceProperty localReferenceProperty2 = localAnnotationIntrospector.findReferenceType((AnnotatedMember)localEntry2.getValue());
        if ((localReferenceProperty2 != null) && (localReferenceProperty2.isBackReference()))
          continue;
        BeanPropertyWriter localBeanPropertyWriter2 = _constructWriter(paramSerializationConfig, localTypeBindings, localPropertyBuilder, bool, (String)localEntry2.getKey(), (AnnotatedMember)localEntry2.getValue());
        localArrayList.add(localBeanPropertyWriter2);
      }
      Iterator localIterator2 = localLinkedHashMap1.entrySet().iterator();
      while (localIterator2.hasNext())
      {
        Map.Entry localEntry1 = (Map.Entry)localIterator2.next();
        AnnotationIntrospector.ReferenceProperty localReferenceProperty1 = localAnnotationIntrospector.findReferenceType((AnnotatedMember)localEntry1.getValue());
        if ((localReferenceProperty1 != null) && (localReferenceProperty1.isBackReference()))
          continue;
        BeanPropertyWriter localBeanPropertyWriter1 = _constructWriter(paramSerializationConfig, localTypeBindings, localPropertyBuilder, bool, (String)localEntry1.getKey(), (AnnotatedMember)localEntry1.getValue());
        localArrayList.add(localBeanPropertyWriter1);
      }
    }
  }

  public JsonSerializer<Object> findBeanSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    JsonSerializer localJsonSerializer;
    if (!isPotentialBeanType(paramJavaType.getRawClass()))
      localJsonSerializer = null;
    while (true)
    {
      return localJsonSerializer;
      localJsonSerializer = constructBeanSerializer(paramSerializationConfig, paramBasicBeanDescription, paramBeanProperty);
      if (!this._factoryConfig.hasSerializerModifiers())
        continue;
      Iterator localIterator = this._factoryConfig.serializerModifiers().iterator();
      while (localIterator.hasNext())
        localJsonSerializer = ((BeanSerializerModifier)localIterator.next()).modifySerializer(paramSerializationConfig, paramBasicBeanDescription, localJsonSerializer);
    }
  }

  protected Object findFilterId(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription)
  {
    return paramSerializationConfig.getAnnotationIntrospector().findFilterId(paramBasicBeanDescription.getClassInfo());
  }

  public TypeSerializer findPropertyContentTypeSerializer(JavaType paramJavaType, SerializationConfig paramSerializationConfig, AnnotatedMember paramAnnotatedMember, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    JavaType localJavaType = paramJavaType.getContentType();
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    TypeResolverBuilder localTypeResolverBuilder = localAnnotationIntrospector.findPropertyContentTypeResolver(paramSerializationConfig, paramAnnotatedMember, paramJavaType);
    if (localTypeResolverBuilder == null)
      return createTypeSerializer(paramSerializationConfig, localJavaType, paramBeanProperty);
    return localTypeResolverBuilder.buildTypeSerializer(paramSerializationConfig, localJavaType, paramSerializationConfig.getSubtypeResolver().collectAndResolveSubtypes(paramAnnotatedMember, paramSerializationConfig, localAnnotationIntrospector), paramBeanProperty);
  }

  public TypeSerializer findPropertyTypeSerializer(JavaType paramJavaType, SerializationConfig paramSerializationConfig, AnnotatedMember paramAnnotatedMember, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    TypeResolverBuilder localTypeResolverBuilder = localAnnotationIntrospector.findPropertyTypeResolver(paramSerializationConfig, paramAnnotatedMember, paramJavaType);
    if (localTypeResolverBuilder == null)
      return createTypeSerializer(paramSerializationConfig, paramJavaType, paramBeanProperty);
    return localTypeResolverBuilder.buildTypeSerializer(paramSerializationConfig, paramJavaType, paramSerializationConfig.getSubtypeResolver().collectAndResolveSubtypes(paramAnnotatedMember, paramSerializationConfig, localAnnotationIntrospector), paramBeanProperty);
  }

  public SerializerFactory.Config getConfig()
  {
    return this._factoryConfig;
  }

  protected boolean isPotentialBeanType(Class<?> paramClass)
  {
    return (ClassUtil.canBeABeanType(paramClass) == null) && (!ClassUtil.isProxyType(paramClass));
  }

  protected void processViews(SerializationConfig paramSerializationConfig, BeanSerializerBuilder paramBeanSerializerBuilder)
  {
    List localList = paramBeanSerializerBuilder.getProperties();
    boolean bool = paramSerializationConfig.isEnabled(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION);
    int i = localList.size();
    int j = 0;
    BeanPropertyWriter[] arrayOfBeanPropertyWriter = new BeanPropertyWriter[i];
    int k = 0;
    if (k < i)
    {
      BeanPropertyWriter localBeanPropertyWriter = (BeanPropertyWriter)localList.get(k);
      Class[] arrayOfClass = localBeanPropertyWriter.getViews();
      if (arrayOfClass == null)
        if (bool)
          arrayOfBeanPropertyWriter[k] = localBeanPropertyWriter;
      while (true)
      {
        k++;
        break;
        j++;
        arrayOfBeanPropertyWriter[k] = constructFilteredBeanWriter(localBeanPropertyWriter, arrayOfClass);
      }
    }
    if ((bool) && (j == 0))
      return;
    paramBeanSerializerBuilder.setFilteredProperties(arrayOfBeanPropertyWriter);
  }

  protected <T extends AnnotatedMember> void removeIgnorableTypes(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription, Map<String, T> paramMap)
  {
    if (paramMap.isEmpty());
    while (true)
    {
      return;
      AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
      Iterator localIterator = paramMap.entrySet().iterator();
      HashMap localHashMap = new HashMap();
      while (localIterator.hasNext())
      {
        Class localClass = ((AnnotatedMember)((Map.Entry)localIterator.next()).getValue()).getRawType();
        Boolean localBoolean = (Boolean)localHashMap.get(localClass);
        if (localBoolean == null)
        {
          localBoolean = localAnnotationIntrospector.isIgnorableType(((BasicBeanDescription)paramSerializationConfig.introspectClassAnnotations(localClass)).getClassInfo());
          if (localBoolean == null)
            localBoolean = Boolean.FALSE;
          localHashMap.put(localClass, localBoolean);
        }
        if (!localBoolean.booleanValue())
          continue;
        localIterator.remove();
      }
    }
  }

  protected List<BeanPropertyWriter> sortBeanProperties(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription, List<BeanPropertyWriter> paramList)
  {
    List localList = paramBasicBeanDescription.findCreatorPropertyNames();
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    AnnotatedClass localAnnotatedClass = paramBasicBeanDescription.getClassInfo();
    String[] arrayOfString = localAnnotationIntrospector.findSerializationPropertyOrder(localAnnotatedClass);
    Boolean localBoolean = localAnnotationIntrospector.findSerializationSortAlphabetically(localAnnotatedClass);
    if (localBoolean == null);
    for (boolean bool = paramSerializationConfig.isEnabled(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY); ; bool = localBoolean.booleanValue())
    {
      if ((bool) || (!localList.isEmpty()) || (arrayOfString != null))
        paramList = _sortBeanProperties(paramList, localList, arrayOfString, bool);
      return paramList;
    }
  }

  public SerializerFactory withConfig(SerializerFactory.Config paramConfig)
  {
    if (this._factoryConfig == paramConfig)
      return this;
    if (getClass() != BeanSerializerFactory.class)
      throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
    return new BeanSerializerFactory(paramConfig);
  }

  public static class ConfigImpl extends SerializerFactory.Config
  {
    protected static final BeanSerializerModifier[] NO_MODIFIERS;
    protected static final Serializers[] NO_SERIALIZERS = new Serializers[0];
    protected final Serializers[] _additionalKeySerializers;
    protected final Serializers[] _additionalSerializers;
    protected final BeanSerializerModifier[] _modifiers;

    static
    {
      NO_MODIFIERS = new BeanSerializerModifier[0];
    }

    public ConfigImpl()
    {
      this(null, null, null);
    }

    protected ConfigImpl(Serializers[] paramArrayOfSerializers1, Serializers[] paramArrayOfSerializers2, BeanSerializerModifier[] paramArrayOfBeanSerializerModifier)
    {
      if (paramArrayOfSerializers1 == null)
        paramArrayOfSerializers1 = NO_SERIALIZERS;
      this._additionalSerializers = paramArrayOfSerializers1;
      if (paramArrayOfSerializers2 == null)
        paramArrayOfSerializers2 = NO_SERIALIZERS;
      this._additionalKeySerializers = paramArrayOfSerializers2;
      if (paramArrayOfBeanSerializerModifier == null)
        paramArrayOfBeanSerializerModifier = NO_MODIFIERS;
      this._modifiers = paramArrayOfBeanSerializerModifier;
    }

    public boolean hasKeySerializers()
    {
      return this._additionalKeySerializers.length > 0;
    }

    public boolean hasSerializerModifiers()
    {
      return this._modifiers.length > 0;
    }

    public boolean hasSerializers()
    {
      return this._additionalSerializers.length > 0;
    }

    public Iterable<Serializers> keySerializers()
    {
      return ArrayBuilders.arrayAsIterable(this._additionalKeySerializers);
    }

    public Iterable<BeanSerializerModifier> serializerModifiers()
    {
      return ArrayBuilders.arrayAsIterable(this._modifiers);
    }

    public Iterable<Serializers> serializers()
    {
      return ArrayBuilders.arrayAsIterable(this._additionalSerializers);
    }

    public SerializerFactory.Config withAdditionalKeySerializers(Serializers paramSerializers)
    {
      if (paramSerializers == null)
        throw new IllegalArgumentException("Can not pass null Serializers");
      Serializers[] arrayOfSerializers = (Serializers[])ArrayBuilders.insertInListNoDup(this._additionalKeySerializers, paramSerializers);
      return new ConfigImpl(this._additionalSerializers, arrayOfSerializers, this._modifiers);
    }

    public SerializerFactory.Config withAdditionalSerializers(Serializers paramSerializers)
    {
      if (paramSerializers == null)
        throw new IllegalArgumentException("Can not pass null Serializers");
      return new ConfigImpl((Serializers[])ArrayBuilders.insertInListNoDup(this._additionalSerializers, paramSerializers), this._additionalKeySerializers, this._modifiers);
    }

    public SerializerFactory.Config withSerializerModifier(BeanSerializerModifier paramBeanSerializerModifier)
    {
      if (paramBeanSerializerModifier == null)
        throw new IllegalArgumentException("Can not pass null modifier");
      BeanSerializerModifier[] arrayOfBeanSerializerModifier = (BeanSerializerModifier[])ArrayBuilders.insertInListNoDup(this._modifiers, paramBeanSerializerModifier);
      return new ConfigImpl(this._additionalSerializers, this._additionalKeySerializers, arrayOfBeanSerializerModifier);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BeanSerializerFactory
 * JD-Core Version:    0.6.0
 */