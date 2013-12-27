package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class MapSerializer extends ContainerSerializerBase<Map<?, ?>>
  implements ResolvableSerializer
{
  protected static final JavaType UNSPECIFIED_TYPE = TypeFactory.unknownType();
  protected PropertySerializerMap _dynamicValueSerializers;
  protected final HashSet<String> _ignoredEntries;
  protected JsonSerializer<Object> _keySerializer;
  protected final JavaType _keyType;
  protected final BeanProperty _property;
  protected JsonSerializer<Object> _valueSerializer;
  protected final JavaType _valueType;
  protected final boolean _valueTypeIsStatic;
  protected final TypeSerializer _valueTypeSerializer;

  protected MapSerializer()
  {
    this((HashSet)null, null, null, false, null, null, null, null);
  }

  @Deprecated
  protected MapSerializer(HashSet<String> paramHashSet, JavaType paramJavaType1, JavaType paramJavaType2, boolean paramBoolean, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer, BeanProperty paramBeanProperty)
  {
    this(paramHashSet, paramJavaType1, paramJavaType2, paramBoolean, paramTypeSerializer, paramJsonSerializer, null, paramBeanProperty);
  }

  protected MapSerializer(HashSet<String> paramHashSet, JavaType paramJavaType1, JavaType paramJavaType2, boolean paramBoolean, TypeSerializer paramTypeSerializer, JsonSerializer<Object> paramJsonSerializer1, JsonSerializer<Object> paramJsonSerializer2, BeanProperty paramBeanProperty)
  {
    super(Map.class, false);
    this._property = paramBeanProperty;
    this._ignoredEntries = paramHashSet;
    this._keyType = paramJavaType1;
    this._valueType = paramJavaType2;
    this._valueTypeIsStatic = paramBoolean;
    this._valueTypeSerializer = paramTypeSerializer;
    this._keySerializer = paramJsonSerializer1;
    this._valueSerializer = paramJsonSerializer2;
    this._dynamicValueSerializers = PropertySerializerMap.emptyMap();
  }

  @Deprecated
  protected MapSerializer(HashSet<String> paramHashSet, JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer)
  {
    this(paramHashSet, UNSPECIFIED_TYPE, paramJavaType, paramBoolean, paramTypeSerializer, null, null, null);
  }

  @Deprecated
  public static MapSerializer construct(String[] paramArrayOfString, JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
  {
    return construct(paramArrayOfString, paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty, null, null);
  }

  public static MapSerializer construct(String[] paramArrayOfString, JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty, JsonSerializer<Object> paramJsonSerializer1, JsonSerializer<Object> paramJsonSerializer2)
  {
    HashSet localHashSet = toSet(paramArrayOfString);
    JavaType localJavaType2;
    JavaType localJavaType1;
    if (paramJavaType == null)
    {
      localJavaType2 = UNSPECIFIED_TYPE;
      localJavaType1 = localJavaType2;
      if (!paramBoolean)
        if ((localJavaType2 == null) || (!localJavaType2.isFinal()))
          break label75;
    }
    label75: for (paramBoolean = true; ; paramBoolean = false)
    {
      return new MapSerializer(localHashSet, localJavaType1, localJavaType2, paramBoolean, paramTypeSerializer, paramJsonSerializer1, paramJsonSerializer2, paramBeanProperty);
      localJavaType1 = paramJavaType.getKeyType();
      localJavaType2 = paramJavaType.getContentType();
      break;
    }
  }

  private static HashSet<String> toSet(String[] paramArrayOfString)
  {
    HashSet localHashSet;
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      localHashSet = null;
    while (true)
    {
      return localHashSet;
      localHashSet = new HashSet(paramArrayOfString.length);
      int i = paramArrayOfString.length;
      for (int j = 0; j < i; j++)
        localHashSet.add(paramArrayOfString[j]);
    }
  }

  protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap paramPropertySerializerMap, Class<?> paramClass, SerializerProvider paramSerializerProvider)
    throws JsonMappingException
  {
    PropertySerializerMap.SerializerAndMapResult localSerializerAndMapResult = paramPropertySerializerMap.findAndAddSerializer(paramClass, paramSerializerProvider, this._property);
    if (paramPropertySerializerMap != localSerializerAndMapResult.map)
      this._dynamicValueSerializers = localSerializerAndMapResult.map;
    return localSerializerAndMapResult.serializer;
  }

  protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap paramPropertySerializerMap, JavaType paramJavaType, SerializerProvider paramSerializerProvider)
    throws JsonMappingException
  {
    PropertySerializerMap.SerializerAndMapResult localSerializerAndMapResult = paramPropertySerializerMap.findAndAddSerializer(paramJavaType, paramSerializerProvider, this._property);
    if (paramPropertySerializerMap != localSerializerAndMapResult.map)
      this._dynamicValueSerializers = localSerializerAndMapResult.map;
    return localSerializerAndMapResult.serializer;
  }

  public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
  {
    MapSerializer localMapSerializer = new MapSerializer(this._ignoredEntries, this._keyType, this._valueType, this._valueTypeIsStatic, paramTypeSerializer, this._keySerializer, this._valueSerializer, this._property);
    if (this._valueSerializer != null)
      localMapSerializer._valueSerializer = this._valueSerializer;
    return localMapSerializer;
  }

  public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
  {
    return createSchemaNode("object", true);
  }

  public void resolve(SerializerProvider paramSerializerProvider)
    throws JsonMappingException
  {
    if ((this._valueTypeIsStatic) && (this._valueSerializer == null))
      this._valueSerializer = paramSerializerProvider.findValueSerializer(this._valueType, this._property);
    if (this._keySerializer == null)
      this._keySerializer = paramSerializerProvider.findKeySerializer(this._keyType, this._property);
  }

  public void serialize(Map<?, ?> paramMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeStartObject();
    if (!paramMap.isEmpty())
    {
      if (this._valueSerializer == null)
        break label36;
      serializeFieldsUsing(paramMap, paramJsonGenerator, paramSerializerProvider, this._valueSerializer);
    }
    while (true)
    {
      paramJsonGenerator.writeEndObject();
      return;
      label36: serializeFields(paramMap, paramJsonGenerator, paramSerializerProvider);
    }
  }

  protected void serializeFields(Map<?, ?> paramMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    if (this._valueTypeSerializer != null)
    {
      serializeTypedFields(paramMap, paramJsonGenerator, paramSerializerProvider);
      return;
    }
    JsonSerializer localJsonSerializer1 = this._keySerializer;
    HashSet localHashSet = this._ignoredEntries;
    int i;
    PropertySerializerMap localPropertySerializerMap;
    label59: Object localObject1;
    Object localObject2;
    if (!paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES))
    {
      i = 1;
      localPropertySerializerMap = this._dynamicValueSerializers;
      Iterator localIterator = paramMap.entrySet().iterator();
      if (!localIterator.hasNext())
        break label268;
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localObject1 = localEntry.getValue();
      localObject2 = localEntry.getKey();
      if (localObject2 != null)
        break label133;
      paramSerializerProvider.getNullKeySerializer().serialize(null, paramJsonGenerator, paramSerializerProvider);
    }
    while (true)
    {
      if (localObject1 != null)
        break label170;
      paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
      break label59;
      i = 0;
      break;
      label133: if (((i != 0) && (localObject1 == null)) || ((localHashSet != null) && (localHashSet.contains(localObject2))))
        break label59;
      localJsonSerializer1.serialize(localObject2, paramJsonGenerator, paramSerializerProvider);
    }
    label170: Class localClass = localObject1.getClass();
    JsonSerializer localJsonSerializer2 = localPropertySerializerMap.serializerFor(localClass);
    if (localJsonSerializer2 == null)
      if (!this._valueType.hasGenericTypes())
        break label270;
    label268: label270: for (localJsonSerializer2 = _findAndAddDynamic(localPropertySerializerMap, this._valueType.forcedNarrowBy(localClass), paramSerializerProvider); ; localJsonSerializer2 = _findAndAddDynamic(localPropertySerializerMap, localClass, paramSerializerProvider))
    {
      while (true)
      {
        localPropertySerializerMap = this._dynamicValueSerializers;
        try
        {
          localJsonSerializer2.serialize(localObject1, paramJsonGenerator, paramSerializerProvider);
        }
        catch (Exception localException)
        {
          wrapAndThrow(paramSerializerProvider, localException, paramMap, "" + localObject2);
        }
      }
      break label59;
      break;
    }
  }

  protected void serializeFieldsUsing(Map<?, ?> paramMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, JsonSerializer<Object> paramJsonSerializer)
    throws IOException, JsonGenerationException
  {
    JsonSerializer localJsonSerializer = this._keySerializer;
    HashSet localHashSet = this._ignoredEntries;
    TypeSerializer localTypeSerializer = this._valueTypeSerializer;
    int i;
    Iterator localIterator;
    if (!paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES))
    {
      i = 1;
      localIterator = paramMap.entrySet().iterator();
    }
    while (true)
    {
      label44: if (!localIterator.hasNext())
        return;
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getValue();
      Object localObject2 = localEntry.getKey();
      if (localObject2 == null)
        paramSerializerProvider.getNullKeySerializer().serialize(null, paramJsonGenerator, paramSerializerProvider);
      while (true)
      {
        if (localObject1 != null)
          break label155;
        paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
        break label44;
        i = 0;
        break;
        if (((i != 0) && (localObject1 == null)) || ((localHashSet != null) && (localHashSet.contains(localObject2))))
          break label44;
        localJsonSerializer.serialize(localObject2, paramJsonGenerator, paramSerializerProvider);
      }
      label155: if (localTypeSerializer == null)
      {
        try
        {
          paramJsonSerializer.serialize(localObject1, paramJsonGenerator, paramSerializerProvider);
        }
        catch (Exception localException)
        {
          wrapAndThrow(paramSerializerProvider, localException, paramMap, "" + localObject2);
        }
        continue;
      }
      paramJsonSerializer.serializeWithType(localObject1, paramJsonGenerator, paramSerializerProvider, localTypeSerializer);
    }
  }

  protected void serializeTypedFields(Map<?, ?> paramMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    JsonSerializer localJsonSerializer = this._keySerializer;
    Object localObject1 = null;
    Object localObject2 = null;
    HashSet localHashSet = this._ignoredEntries;
    int i;
    label44: Object localObject3;
    Object localObject4;
    if (!paramSerializerProvider.isEnabled(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES))
    {
      i = 1;
      Iterator localIterator = paramMap.entrySet().iterator();
      if (!localIterator.hasNext())
        return;
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localObject3 = localEntry.getValue();
      localObject4 = localEntry.getKey();
      if (localObject4 != null)
        break label118;
      paramSerializerProvider.getNullKeySerializer().serialize(null, paramJsonGenerator, paramSerializerProvider);
    }
    while (true)
    {
      if (localObject3 != null)
        break label155;
      paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
      break label44;
      i = 0;
      break;
      label118: if (((i != 0) && (localObject3 == null)) || ((localHashSet != null) && (localHashSet.contains(localObject4))))
        break label44;
      localJsonSerializer.serialize(localObject4, paramJsonGenerator, paramSerializerProvider);
    }
    label155: Class localClass = localObject3.getClass();
    Object localObject5;
    if (localClass == localObject2)
      localObject5 = localObject1;
    while (true)
    {
      try
      {
        ((JsonSerializer)localObject5).serializeWithType(localObject3, paramJsonGenerator, paramSerializerProvider, this._valueTypeSerializer);
      }
      catch (Exception localException)
      {
        wrapAndThrow(paramSerializerProvider, localException, paramMap, "" + localObject4);
      }
      break;
      localObject5 = paramSerializerProvider.findValueSerializer(localClass, this._property);
      localObject1 = localObject5;
      localObject2 = localClass;
    }
  }

  public void serializeWithType(Map<?, ?> paramMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonGenerationException
  {
    paramTypeSerializer.writeTypePrefixForObject(paramMap, paramJsonGenerator);
    if (!paramMap.isEmpty())
    {
      if (this._valueSerializer == null)
        break label42;
      serializeFieldsUsing(paramMap, paramJsonGenerator, paramSerializerProvider, this._valueSerializer);
    }
    while (true)
    {
      paramTypeSerializer.writeTypeSuffixForObject(paramMap, paramJsonGenerator);
      return;
      label42: serializeFields(paramMap, paramJsonGenerator, paramSerializerProvider);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.MapSerializer
 * JD-Core Version:    0.6.0
 */