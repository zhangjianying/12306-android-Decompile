package org.codehaus.jackson.map.ser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.RandomAccess;
import java.util.TimeZone;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualSerializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.JsonSerializer.None;
import org.codehaus.jackson.map.JsonSerializer<*>;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.impl.IndexedStringListSerializer;
import org.codehaus.jackson.map.ser.impl.InetAddressSerializer;
import org.codehaus.jackson.map.ser.impl.ObjectArraySerializer;
import org.codehaus.jackson.map.ser.impl.StringCollectionSerializer;
import org.codehaus.jackson.map.ser.impl.TimeZoneSerializer;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

public abstract class BasicSerializerFactory extends SerializerFactory
{
  protected static final HashMap<String, JsonSerializer<?>> _arraySerializers;
  protected static final HashMap<String, JsonSerializer<?>> _concrete = new HashMap();
  protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy = new HashMap();
  protected OptionalHandlerFactory optionalHandlers = OptionalHandlerFactory.instance;

  static
  {
    _concrete.put(String.class.getName(), new StdSerializers.StringSerializer());
    ToStringSerializer localToStringSerializer = ToStringSerializer.instance;
    _concrete.put(StringBuffer.class.getName(), localToStringSerializer);
    _concrete.put(StringBuilder.class.getName(), localToStringSerializer);
    _concrete.put(Character.class.getName(), localToStringSerializer);
    _concrete.put(Character.TYPE.getName(), localToStringSerializer);
    _concrete.put(Boolean.TYPE.getName(), new StdSerializers.BooleanSerializer(true));
    _concrete.put(Boolean.class.getName(), new StdSerializers.BooleanSerializer(false));
    StdSerializers.IntegerSerializer localIntegerSerializer = new StdSerializers.IntegerSerializer();
    _concrete.put(Integer.class.getName(), localIntegerSerializer);
    _concrete.put(Integer.TYPE.getName(), localIntegerSerializer);
    _concrete.put(Long.class.getName(), StdSerializers.LongSerializer.instance);
    _concrete.put(Long.TYPE.getName(), StdSerializers.LongSerializer.instance);
    _concrete.put(Byte.class.getName(), StdSerializers.IntLikeSerializer.instance);
    _concrete.put(Byte.TYPE.getName(), StdSerializers.IntLikeSerializer.instance);
    _concrete.put(Short.class.getName(), StdSerializers.IntLikeSerializer.instance);
    _concrete.put(Short.TYPE.getName(), StdSerializers.IntLikeSerializer.instance);
    _concrete.put(Float.class.getName(), StdSerializers.FloatSerializer.instance);
    _concrete.put(Float.TYPE.getName(), StdSerializers.FloatSerializer.instance);
    _concrete.put(Double.class.getName(), StdSerializers.DoubleSerializer.instance);
    _concrete.put(Double.TYPE.getName(), StdSerializers.DoubleSerializer.instance);
    StdSerializers.NumberSerializer localNumberSerializer = new StdSerializers.NumberSerializer();
    _concrete.put(BigInteger.class.getName(), localNumberSerializer);
    _concrete.put(BigDecimal.class.getName(), localNumberSerializer);
    _concrete.put(Calendar.class.getName(), StdSerializers.CalendarSerializer.instance);
    _concrete.put(java.util.Date.class.getName(), StdSerializers.UtilDateSerializer.instance);
    _concrete.put(java.sql.Date.class.getName(), new StdSerializers.SqlDateSerializer());
    _concrete.put(Time.class.getName(), new StdSerializers.SqlTimeSerializer());
    _concrete.put(Timestamp.class.getName(), StdSerializers.UtilDateSerializer.instance);
    Iterator localIterator = new JdkSerializers().provide().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject = localEntry.getValue();
      if ((localObject instanceof JsonSerializer))
      {
        _concrete.put(((Class)localEntry.getKey()).getName(), (JsonSerializer)localObject);
        continue;
      }
      if ((localObject instanceof Class))
      {
        Class localClass = (Class)localObject;
        _concreteLazy.put(((Class)localEntry.getKey()).getName(), localClass);
        continue;
      }
      throw new IllegalStateException("Internal error: unrecognized value of type " + localEntry.getClass().getName());
    }
    _concreteLazy.put(TokenBuffer.class.getName(), StdSerializers.TokenBufferSerializer.class);
    _arraySerializers = new HashMap();
    _arraySerializers.put([Z.class.getName(), new ArraySerializers.BooleanArraySerializer());
    _arraySerializers.put([B.class.getName(), new ArraySerializers.ByteArraySerializer());
    _arraySerializers.put([C.class.getName(), new ArraySerializers.CharArraySerializer());
    _arraySerializers.put([S.class.getName(), new ArraySerializers.ShortArraySerializer());
    _arraySerializers.put([I.class.getName(), new ArraySerializers.IntArraySerializer());
    _arraySerializers.put([J.class.getName(), new ArraySerializers.LongArraySerializer());
    _arraySerializers.put([F.class.getName(), new ArraySerializers.FloatArraySerializer());
    _arraySerializers.put([D.class.getName(), new ArraySerializers.DoubleArraySerializer());
  }

  protected static JsonSerializer<Object> findContentSerializer(SerializationConfig paramSerializationConfig, Annotated paramAnnotated, BeanProperty paramBeanProperty)
  {
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    Class localClass = localAnnotationIntrospector.findContentSerializer(paramAnnotated);
    if (((localClass == null) || (localClass == JsonSerializer.None.class)) && (paramBeanProperty != null))
      localClass = localAnnotationIntrospector.findContentSerializer(paramBeanProperty.getMember());
    if ((localClass != null) && (localClass != JsonSerializer.None.class))
      return paramSerializationConfig.serializerInstance(paramAnnotated, localClass);
    return null;
  }

  protected static JsonSerializer<Object> findKeySerializer(SerializationConfig paramSerializationConfig, Annotated paramAnnotated, BeanProperty paramBeanProperty)
  {
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    Class localClass = localAnnotationIntrospector.findKeySerializer(paramAnnotated);
    if (((localClass == null) || (localClass == JsonSerializer.None.class)) && (paramBeanProperty != null))
      localClass = localAnnotationIntrospector.findKeySerializer(paramBeanProperty.getMember());
    if ((localClass != null) && (localClass != JsonSerializer.None.class))
      return paramSerializationConfig.serializerInstance(paramAnnotated, localClass);
    return null;
  }

  // ERROR //
  protected static <T extends JavaType> T modifySecondaryTypesByAnnotation(SerializationConfig paramSerializationConfig, Annotated paramAnnotated, T paramT)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 251	org/codehaus/jackson/map/SerializationConfig:getAnnotationIntrospector	()Lorg/codehaus/jackson/map/AnnotationIntrospector;
    //   4: astore_3
    //   5: aload_2
    //   6: invokevirtual 280	org/codehaus/jackson/type/JavaType:isContainerType	()Z
    //   9: ifeq +101 -> 110
    //   12: aload_3
    //   13: aload_1
    //   14: aload_2
    //   15: invokevirtual 284	org/codehaus/jackson/type/JavaType:getKeyType	()Lorg/codehaus/jackson/type/JavaType;
    //   18: invokevirtual 288	org/codehaus/jackson/map/AnnotationIntrospector:findSerializationKeyType	(Lorg/codehaus/jackson/map/introspect/Annotated;Lorg/codehaus/jackson/type/JavaType;)Ljava/lang/Class;
    //   21: astore 4
    //   23: aload 4
    //   25: ifnull +58 -> 83
    //   28: aload_2
    //   29: instanceof 290
    //   32: ifne +37 -> 69
    //   35: new 275	java/lang/IllegalArgumentException
    //   38: dup
    //   39: new 48	java/lang/StringBuilder
    //   42: dup
    //   43: invokespecial 172	java/lang/StringBuilder:<init>	()V
    //   46: ldc_w 292
    //   49: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: aload_2
    //   53: invokevirtual 295	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   56: ldc_w 297
    //   59: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: invokevirtual 187	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   65: invokespecial 298	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   68: athrow
    //   69: aload_2
    //   70: checkcast 290	org/codehaus/jackson/map/type/MapType
    //   73: aload 4
    //   75: invokevirtual 302	org/codehaus/jackson/map/type/MapType:widenKey	(Ljava/lang/Class;)Lorg/codehaus/jackson/type/JavaType;
    //   78: astore 9
    //   80: aload 9
    //   82: astore_2
    //   83: aload_3
    //   84: aload_1
    //   85: aload_2
    //   86: invokevirtual 305	org/codehaus/jackson/type/JavaType:getContentType	()Lorg/codehaus/jackson/type/JavaType;
    //   89: invokevirtual 308	org/codehaus/jackson/map/AnnotationIntrospector:findSerializationContentType	(Lorg/codehaus/jackson/map/introspect/Annotated;Lorg/codehaus/jackson/type/JavaType;)Ljava/lang/Class;
    //   92: astore 5
    //   94: aload 5
    //   96: ifnull +14 -> 110
    //   99: aload_2
    //   100: aload 5
    //   102: invokevirtual 311	org/codehaus/jackson/type/JavaType:widenContentsBy	(Ljava/lang/Class;)Lorg/codehaus/jackson/type/JavaType;
    //   105: astore 7
    //   107: aload 7
    //   109: astore_2
    //   110: aload_2
    //   111: areturn
    //   112: astore 8
    //   114: new 275	java/lang/IllegalArgumentException
    //   117: dup
    //   118: new 48	java/lang/StringBuilder
    //   121: dup
    //   122: invokespecial 172	java/lang/StringBuilder:<init>	()V
    //   125: ldc_w 313
    //   128: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: aload_2
    //   132: invokevirtual 295	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   135: ldc_w 315
    //   138: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: aload 4
    //   143: invokevirtual 31	java/lang/Class:getName	()Ljava/lang/String;
    //   146: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: ldc_w 317
    //   152: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: aload 8
    //   157: invokevirtual 320	java/lang/IllegalArgumentException:getMessage	()Ljava/lang/String;
    //   160: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: invokevirtual 187	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   166: invokespecial 298	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   169: athrow
    //   170: astore 6
    //   172: new 275	java/lang/IllegalArgumentException
    //   175: dup
    //   176: new 48	java/lang/StringBuilder
    //   179: dup
    //   180: invokespecial 172	java/lang/StringBuilder:<init>	()V
    //   183: ldc_w 322
    //   186: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: aload_2
    //   190: invokevirtual 295	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   193: ldc_w 324
    //   196: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   199: aload 5
    //   201: invokevirtual 31	java/lang/Class:getName	()Ljava/lang/String;
    //   204: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: ldc_w 317
    //   210: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: aload 6
    //   215: invokevirtual 320	java/lang/IllegalArgumentException:getMessage	()Ljava/lang/String;
    //   218: invokevirtual 178	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: invokevirtual 187	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   224: invokespecial 298	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   227: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   69	80	112	java/lang/IllegalArgumentException
    //   99	107	170	java/lang/IllegalArgumentException
  }

  protected JsonSerializer<?> buildArraySerializer(SerializationConfig paramSerializationConfig, ArrayType paramArrayType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer)
  {
    Class localClass = paramArrayType.getRawClass();
    Object localObject;
    if ([Ljava.lang.String.class == localClass)
      localObject = new ArraySerializers.StringArraySerializer(paramBeanProperty);
    do
    {
      return localObject;
      localObject = (JsonSerializer)_arraySerializers.get(localClass.getName());
    }
    while (localObject != null);
    return (JsonSerializer<?>)new ObjectArraySerializer(paramArrayType.getContentType(), paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
  }

  protected JsonSerializer<?> buildCollectionLikeSerializer(SerializationConfig paramSerializationConfig, CollectionLikeType paramCollectionLikeType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer)
  {
    Iterator localIterator = customSerializers().iterator();
    while (localIterator.hasNext())
    {
      JsonSerializer localJsonSerializer = ((Serializers)localIterator.next()).findCollectionLikeSerializer(paramSerializationConfig, paramCollectionLikeType, paramBasicBeanDescription, paramBeanProperty, paramTypeSerializer, paramJsonSerializer);
      if (localJsonSerializer != null)
        return localJsonSerializer;
    }
    return null;
  }

  protected JsonSerializer<?> buildCollectionSerializer(SerializationConfig paramSerializationConfig, CollectionType paramCollectionType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer)
  {
    Iterator localIterator = customSerializers().iterator();
    while (localIterator.hasNext())
    {
      JsonSerializer localJsonSerializer = ((Serializers)localIterator.next()).findCollectionSerializer(paramSerializationConfig, paramCollectionType, paramBasicBeanDescription, paramBeanProperty, paramTypeSerializer, paramJsonSerializer);
      if (localJsonSerializer != null)
        return localJsonSerializer;
    }
    Class localClass1 = paramCollectionType.getRawClass();
    if (EnumSet.class.isAssignableFrom(localClass1))
      return buildEnumSetSerializer(paramSerializationConfig, paramCollectionType, paramBasicBeanDescription, paramBeanProperty, paramBoolean, paramTypeSerializer, paramJsonSerializer);
    Class localClass2 = paramCollectionType.getContentType().getRawClass();
    if (isIndexedList(localClass1))
    {
      if (localClass2 == String.class)
        return new IndexedStringListSerializer(paramBeanProperty);
      return ContainerSerializers.indexedListSerializer(paramCollectionType.getContentType(), paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
    }
    if (localClass2 == String.class)
      return new StringCollectionSerializer(paramBeanProperty);
    return ContainerSerializers.collectionSerializer(paramCollectionType.getContentType(), paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
  }

  public JsonSerializer<?> buildContainerSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean)
  {
    TypeSerializer localTypeSerializer = createTypeSerializer(paramSerializationConfig, paramJavaType.getContentType(), paramBeanProperty);
    if (localTypeSerializer != null)
      paramBoolean = false;
    JsonSerializer localJsonSerializer1;
    while (true)
    {
      localJsonSerializer1 = findContentSerializer(paramSerializationConfig, paramBasicBeanDescription.getClassInfo(), paramBeanProperty);
      if (!paramJavaType.isMapLikeType())
        break;
      MapLikeType localMapLikeType = (MapLikeType)paramJavaType;
      JsonSerializer localJsonSerializer2 = findKeySerializer(paramSerializationConfig, paramBasicBeanDescription.getClassInfo(), paramBeanProperty);
      if (localMapLikeType.isTrueMapType())
      {
        return buildMapSerializer(paramSerializationConfig, (MapType)localMapLikeType, paramBasicBeanDescription, paramBeanProperty, paramBoolean, localJsonSerializer2, localTypeSerializer, localJsonSerializer1);
        if (paramBoolean)
          continue;
        paramBoolean = usesStaticTyping(paramSerializationConfig, paramBasicBeanDescription, localTypeSerializer, paramBeanProperty);
        continue;
      }
      return buildMapLikeSerializer(paramSerializationConfig, localMapLikeType, paramBasicBeanDescription, paramBeanProperty, paramBoolean, localJsonSerializer2, localTypeSerializer, localJsonSerializer1);
    }
    if (paramJavaType.isCollectionLikeType())
    {
      CollectionLikeType localCollectionLikeType = (CollectionLikeType)paramJavaType;
      if (localCollectionLikeType.isTrueCollectionType())
        return buildCollectionSerializer(paramSerializationConfig, (CollectionType)localCollectionLikeType, paramBasicBeanDescription, paramBeanProperty, paramBoolean, localTypeSerializer, localJsonSerializer1);
      return buildCollectionLikeSerializer(paramSerializationConfig, localCollectionLikeType, paramBasicBeanDescription, paramBeanProperty, paramBoolean, localTypeSerializer, localJsonSerializer1);
    }
    if (paramJavaType.isArrayType())
      return buildArraySerializer(paramSerializationConfig, (ArrayType)paramJavaType, paramBasicBeanDescription, paramBeanProperty, paramBoolean, localTypeSerializer, localJsonSerializer1);
    return null;
  }

  protected JsonSerializer<?> buildEnumMapSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer)
  {
    JavaType localJavaType = paramJavaType.getKeyType();
    boolean bool = localJavaType.isEnumType();
    EnumValues localEnumValues = null;
    if (bool)
      localEnumValues = EnumValues.construct(localJavaType.getRawClass(), paramSerializationConfig.getAnnotationIntrospector());
    return new EnumMapSerializer(paramJavaType.getContentType(), paramBoolean, localEnumValues, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
  }

  protected JsonSerializer<?> buildEnumSetSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer)
  {
    JavaType localJavaType = paramJavaType.getContentType();
    if (!localJavaType.isEnumType())
      localJavaType = null;
    return ContainerSerializers.enumSetSerializer(localJavaType, paramBeanProperty);
  }

  protected JsonSerializer<?> buildIterableSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean)
  {
    JavaType localJavaType = paramJavaType.containedType(0);
    if (localJavaType == null)
      localJavaType = TypeFactory.unknownType();
    TypeSerializer localTypeSerializer = createTypeSerializer(paramSerializationConfig, localJavaType, paramBeanProperty);
    return ContainerSerializers.iterableSerializer(localJavaType, usesStaticTyping(paramSerializationConfig, paramBasicBeanDescription, localTypeSerializer, paramBeanProperty), localTypeSerializer, paramBeanProperty);
  }

  protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean)
  {
    JavaType localJavaType = paramJavaType.containedType(0);
    if (localJavaType == null)
      localJavaType = TypeFactory.unknownType();
    TypeSerializer localTypeSerializer = createTypeSerializer(paramSerializationConfig, localJavaType, paramBeanProperty);
    return ContainerSerializers.iteratorSerializer(localJavaType, usesStaticTyping(paramSerializationConfig, paramBasicBeanDescription, localTypeSerializer, paramBeanProperty), localTypeSerializer, paramBeanProperty);
  }

  protected JsonSerializer<?> buildMapLikeSerializer(SerializationConfig paramSerializationConfig, MapLikeType paramMapLikeType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean, JsonSerializer<Object> paramJsonSerializer1, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer2)
  {
    Iterator localIterator = customSerializers().iterator();
    while (localIterator.hasNext())
    {
      JsonSerializer localJsonSerializer = ((Serializers)localIterator.next()).findMapLikeSerializer(paramSerializationConfig, paramMapLikeType, paramBasicBeanDescription, paramBeanProperty, paramJsonSerializer1, paramTypeSerializer, paramJsonSerializer2);
      if (localJsonSerializer != null)
        return localJsonSerializer;
    }
    return null;
  }

  protected JsonSerializer<?> buildMapSerializer(SerializationConfig paramSerializationConfig, MapType paramMapType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean, JsonSerializer<Object> paramJsonSerializer1, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer2)
  {
    Iterator localIterator = customSerializers().iterator();
    while (localIterator.hasNext())
    {
      JsonSerializer localJsonSerializer = ((Serializers)localIterator.next()).findMapSerializer(paramSerializationConfig, paramMapType, paramBasicBeanDescription, paramBeanProperty, paramJsonSerializer1, paramTypeSerializer, paramJsonSerializer2);
      if (localJsonSerializer != null)
        return localJsonSerializer;
    }
    if (EnumMap.class.isAssignableFrom(paramMapType.getRawClass()))
      return buildEnumMapSerializer(paramSerializationConfig, paramMapType, paramBasicBeanDescription, paramBeanProperty, paramBoolean, paramTypeSerializer, paramJsonSerializer2);
    return MapSerializer.construct(paramSerializationConfig.getAnnotationIntrospector().findPropertiesToIgnore(paramBasicBeanDescription.getClassInfo()), paramMapType, paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer1, paramJsonSerializer2);
  }

  public abstract JsonSerializer<Object> createSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
    throws JsonMappingException;

  public TypeSerializer createTypeSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BeanProperty paramBeanProperty)
  {
    AnnotatedClass localAnnotatedClass = ((BasicBeanDescription)paramSerializationConfig.introspectClassAnnotations(paramJavaType.getRawClass())).getClassInfo();
    AnnotationIntrospector localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
    TypeResolverBuilder localTypeResolverBuilder = localAnnotationIntrospector.findTypeResolver(paramSerializationConfig, localAnnotatedClass, paramJavaType);
    Collection localCollection = null;
    if (localTypeResolverBuilder == null)
      localTypeResolverBuilder = paramSerializationConfig.getDefaultTyper(paramJavaType);
    while (localTypeResolverBuilder == null)
    {
      return null;
      localCollection = paramSerializationConfig.getSubtypeResolver().collectAndResolveSubtypes(localAnnotatedClass, paramSerializationConfig, localAnnotationIntrospector);
    }
    return localTypeResolverBuilder.buildTypeSerializer(paramSerializationConfig, paramJavaType, localCollection, paramBeanProperty);
  }

  protected abstract Iterable<Serializers> customSerializers();

  public final JsonSerializer<?> findSerializerByAddonType(SerializationConfig paramSerializationConfig, JavaType paramJavaType, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean)
    throws JsonMappingException
  {
    Class localClass = paramJavaType.getRawClass();
    if (Iterator.class.isAssignableFrom(localClass))
      return buildIteratorSerializer(paramSerializationConfig, paramJavaType, paramBasicBeanDescription, paramBeanProperty, paramBoolean);
    if (Iterable.class.isAssignableFrom(localClass))
      return buildIterableSerializer(paramSerializationConfig, paramJavaType, paramBasicBeanDescription, paramBeanProperty, paramBoolean);
    if (CharSequence.class.isAssignableFrom(localClass))
      return ToStringSerializer.instance;
    return null;
  }

  public final JsonSerializer<?> findSerializerByLookup(JavaType paramJavaType, SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean)
  {
    String str = paramJavaType.getRawClass().getName();
    JsonSerializer localJsonSerializer1 = (JsonSerializer)_concrete.get(str);
    if (localJsonSerializer1 != null)
      return localJsonSerializer1;
    Class localClass = (Class)_concreteLazy.get(str);
    if (localClass != null)
      try
      {
        JsonSerializer localJsonSerializer2 = (JsonSerializer)localClass.newInstance();
        return localJsonSerializer2;
      }
      catch (Exception localException)
      {
        throw new IllegalStateException("Failed to instantiate standard serializer (of type " + localClass.getName() + "): " + localException.getMessage(), localException);
      }
    return null;
  }

  public final JsonSerializer<?> findSerializerByPrimaryType(JavaType paramJavaType, SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, boolean paramBoolean)
    throws JsonMappingException
  {
    Class localClass = paramJavaType.getRawClass();
    Object localObject;
    if (JsonSerializable.class.isAssignableFrom(localClass))
      if (JsonSerializableWithType.class.isAssignableFrom(localClass))
        localObject = StdSerializers.SerializableWithTypeSerializer.instance;
    do
    {
      return localObject;
      return StdSerializers.SerializableSerializer.instance;
      AnnotatedMethod localAnnotatedMethod = paramBasicBeanDescription.findJsonValueMethod();
      if (localAnnotatedMethod != null)
      {
        JsonSerializer localJsonSerializer = findSerializerFromAnnotation(paramSerializationConfig, localAnnotatedMethod, paramBeanProperty);
        return new JsonValueSerializer(localAnnotatedMethod.getAnnotated(), localJsonSerializer, paramBeanProperty);
      }
      if (InetAddress.class.isAssignableFrom(localClass))
        return InetAddressSerializer.instance;
      if (TimeZone.class.isAssignableFrom(localClass))
        return TimeZoneSerializer.instance;
      localObject = this.optionalHandlers.findSerializer(paramSerializationConfig, paramJavaType);
    }
    while (localObject != null);
    if (Number.class.isAssignableFrom(localClass))
      return StdSerializers.NumberSerializer.instance;
    if (Enum.class.isAssignableFrom(localClass))
      return EnumSerializer.construct(localClass, paramSerializationConfig, paramBasicBeanDescription);
    if (Calendar.class.isAssignableFrom(localClass))
      return StdSerializers.CalendarSerializer.instance;
    if (java.util.Date.class.isAssignableFrom(localClass))
      return StdSerializers.UtilDateSerializer.instance;
    return (JsonSerializer<?>)null;
  }

  protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig paramSerializationConfig, Annotated paramAnnotated, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    Object localObject = paramSerializationConfig.getAnnotationIntrospector().findSerializer(paramAnnotated);
    JsonSerializer localJsonSerializer;
    if (localObject == null)
      localJsonSerializer = null;
    do
    {
      while (true)
      {
        return localJsonSerializer;
        if (!(localObject instanceof JsonSerializer))
          break;
        localJsonSerializer = (JsonSerializer)localObject;
        if ((localJsonSerializer instanceof ContextualSerializer))
          return ((ContextualSerializer)localJsonSerializer).createContextual(paramSerializationConfig, paramBeanProperty);
      }
      if (!(localObject instanceof Class))
        throw new IllegalStateException("AnnotationIntrospector returned value of type " + localObject.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
      Class localClass = (Class)localObject;
      if (!JsonSerializer.class.isAssignableFrom(localClass))
        throw new IllegalStateException("AnnotationIntrospector returned Class " + localClass.getName() + "; expected Class<JsonSerializer>");
      localJsonSerializer = paramSerializationConfig.serializerInstance(paramAnnotated, localClass);
    }
    while (!(localJsonSerializer instanceof ContextualSerializer));
    return ((ContextualSerializer)localJsonSerializer).createContextual(paramSerializationConfig, paramBeanProperty);
  }

  public final JsonSerializer<?> getNullSerializer()
  {
    return NullSerializer.instance;
  }

  protected boolean isIndexedList(Class<?> paramClass)
  {
    return RandomAccess.class.isAssignableFrom(paramClass);
  }

  protected <T extends JavaType> T modifyTypeByAnnotation(SerializationConfig paramSerializationConfig, Annotated paramAnnotated, T paramT)
  {
    Class localClass = paramSerializationConfig.getAnnotationIntrospector().findSerializationType(paramAnnotated);
    if (localClass != null);
    try
    {
      JavaType localJavaType = paramT.widenBy(localClass);
      paramT = localJavaType;
      return modifySecondaryTypesByAnnotation(paramSerializationConfig, paramAnnotated, paramT);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    throw new IllegalArgumentException("Failed to widen type " + paramT + " with concrete-type annotation (value " + localClass.getName() + "), method '" + paramAnnotated.getName() + "': " + localIllegalArgumentException.getMessage());
  }

  protected boolean usesStaticTyping(SerializationConfig paramSerializationConfig, BasicBeanDescription paramBasicBeanDescription, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
  {
    if (paramTypeSerializer != null);
    AnnotationIntrospector localAnnotationIntrospector;
    JavaType localJavaType;
    do
    {
      do
      {
        do
        {
          return false;
          localAnnotationIntrospector = paramSerializationConfig.getAnnotationIntrospector();
          JsonSerialize.Typing localTyping = localAnnotationIntrospector.findSerializationTyping(paramBasicBeanDescription.getClassInfo());
          if (localTyping != null)
          {
            if (localTyping == JsonSerialize.Typing.STATIC)
              return true;
          }
          else if (paramSerializationConfig.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING))
            return true;
        }
        while (paramBeanProperty == null);
        localJavaType = paramBeanProperty.getType();
      }
      while (!localJavaType.isContainerType());
      if (localAnnotationIntrospector.findSerializationContentType(paramBeanProperty.getMember(), paramBeanProperty.getType()) != null)
        return true;
    }
    while ((!(localJavaType instanceof MapType)) || (localAnnotationIntrospector.findSerializationKeyType(paramBeanProperty.getMember(), paramBeanProperty.getType()) == null));
    return true;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BasicSerializerFactory
 * JD-Core Version:    0.6.0
 */