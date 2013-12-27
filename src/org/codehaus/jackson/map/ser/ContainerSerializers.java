package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

public final class ContainerSerializers
{
  public static ContainerSerializerBase<?> collectionSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty, JsonSerializer<Object> paramJsonSerializer)
  {
    return new CollectionSerializer(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
  }

  public static JsonSerializer<?> enumSetSerializer(JavaType paramJavaType, BeanProperty paramBeanProperty)
  {
    return new EnumSetSerializer(paramJavaType, paramBeanProperty);
  }

  public static ContainerSerializerBase<?> indexedListSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty, JsonSerializer<Object> paramJsonSerializer)
  {
    return new IndexedListSerializer(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
  }

  public static ContainerSerializerBase<?> iterableSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
  {
    return new IterableSerializer(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty);
  }

  public static ContainerSerializerBase<?> iteratorSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
  {
    return new IteratorSerializer(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty);
  }

  public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T>
    implements ResolvableSerializer
  {
    protected PropertySerializerMap _dynamicSerializers;
    protected JsonSerializer<Object> _elementSerializer;
    protected final JavaType _elementType;
    protected final BeanProperty _property;
    protected final boolean _staticTyping;
    protected final TypeSerializer _valueTypeSerializer;

    @Deprecated
    protected AsArraySerializer(Class<?> paramClass, JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
    {
      this(paramClass, paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty, null);
    }

    protected AsArraySerializer(Class<?> paramClass, JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty, JsonSerializer<Object> paramJsonSerializer)
    {
      super(false);
      this._elementType = paramJavaType;
      boolean bool1;
      if (!paramBoolean)
      {
        bool1 = false;
        if (paramJavaType != null)
        {
          boolean bool2 = paramJavaType.isFinal();
          bool1 = false;
          if (!bool2);
        }
      }
      else
      {
        bool1 = true;
      }
      this._staticTyping = bool1;
      this._valueTypeSerializer = paramTypeSerializer;
      this._property = paramBeanProperty;
      this._elementSerializer = paramJsonSerializer;
      this._dynamicSerializers = PropertySerializerMap.emptyMap();
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap paramPropertySerializerMap, Class<?> paramClass, SerializerProvider paramSerializerProvider)
      throws JsonMappingException
    {
      PropertySerializerMap.SerializerAndMapResult localSerializerAndMapResult = paramPropertySerializerMap.findAndAddSerializer(paramClass, paramSerializerProvider, this._property);
      if (paramPropertySerializerMap != localSerializerAndMapResult.map)
        this._dynamicSerializers = localSerializerAndMapResult.map;
      return localSerializerAndMapResult.serializer;
    }

    protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap paramPropertySerializerMap, JavaType paramJavaType, SerializerProvider paramSerializerProvider)
      throws JsonMappingException
    {
      PropertySerializerMap.SerializerAndMapResult localSerializerAndMapResult = paramPropertySerializerMap.findAndAddSerializer(paramJavaType, paramSerializerProvider, this._property);
      if (paramPropertySerializerMap != localSerializerAndMapResult.map)
        this._dynamicSerializers = localSerializerAndMapResult.map;
      return localSerializerAndMapResult.serializer;
    }

    public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
      throws JsonMappingException
    {
      ObjectNode localObjectNode = createSchemaNode("array", true);
      JavaType localJavaType = null;
      if (paramType != null)
      {
        localJavaType = paramSerializerProvider.constructType(paramType).getContentType();
        if ((localJavaType == null) && ((paramType instanceof ParameterizedType)))
        {
          Type[] arrayOfType = ((ParameterizedType)paramType).getActualTypeArguments();
          if (arrayOfType.length == 1)
            localJavaType = paramSerializerProvider.constructType(arrayOfType[0]);
        }
      }
      if ((localJavaType == null) && (this._elementType != null))
        localJavaType = this._elementType;
      if (localJavaType != null)
      {
        Class localClass = localJavaType.getRawClass();
        JsonNode localJsonNode = null;
        if (localClass != Object.class)
        {
          JsonSerializer localJsonSerializer = paramSerializerProvider.findValueSerializer(localJavaType, this._property);
          boolean bool = localJsonSerializer instanceof SchemaAware;
          localJsonNode = null;
          if (bool)
            localJsonNode = ((SchemaAware)localJsonSerializer).getSchema(paramSerializerProvider, null);
        }
        if (localJsonNode == null)
          localJsonNode = JsonSchema.getDefaultSchemaNode();
        localObjectNode.put("items", localJsonNode);
      }
      return localObjectNode;
    }

    public void resolve(SerializerProvider paramSerializerProvider)
      throws JsonMappingException
    {
      if ((this._staticTyping) && (this._elementType != null) && (this._elementSerializer == null))
        this._elementSerializer = paramSerializerProvider.findValueSerializer(this._elementType, this._property);
    }

    public final void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      paramJsonGenerator.writeStartArray();
      serializeContents(paramT, paramJsonGenerator, paramSerializerProvider);
      paramJsonGenerator.writeEndArray();
    }

    protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException;

    public final void serializeWithType(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
      throws IOException, JsonGenerationException
    {
      paramTypeSerializer.writeTypePrefixForArray(paramT, paramJsonGenerator);
      serializeContents(paramT, paramJsonGenerator, paramSerializerProvider);
      paramTypeSerializer.writeTypeSuffixForArray(paramT, paramJsonGenerator);
    }
  }

  @JacksonStdImpl
  public static class CollectionSerializer extends ContainerSerializers.AsArraySerializer<Collection<?>>
  {
    @Deprecated
    public CollectionSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
    {
      this(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty, null);
    }

    public CollectionSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty, JsonSerializer<Object> paramJsonSerializer)
    {
      super(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return new CollectionSerializer(this._elementType, this._staticTyping, paramTypeSerializer, this._property);
    }

    public void serializeContents(Collection<?> paramCollection, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      if (this._elementSerializer != null)
        serializeContentsUsing(paramCollection, paramJsonGenerator, paramSerializerProvider, this._elementSerializer);
      Iterator localIterator;
      do
      {
        return;
        localIterator = paramCollection.iterator();
      }
      while (!localIterator.hasNext());
      PropertySerializerMap localPropertySerializerMap = this._dynamicSerializers;
      TypeSerializer localTypeSerializer = this._valueTypeSerializer;
      int i = 0;
      while (true)
      {
        Object localObject;
        Class localClass;
        try
        {
          localObject = localIterator.next();
          if (localObject != null)
            continue;
          paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
          i++;
          if (!localIterator.hasNext())
          {
            return;
            localClass = localObject.getClass();
            localJsonSerializer = localPropertySerializerMap.serializerFor(localClass);
            if (localJsonSerializer != null)
              continue;
            if (this._elementType.hasGenericTypes())
            {
              localJsonSerializer = _findAndAddDynamic(localPropertySerializerMap, this._elementType.forcedNarrowBy(localClass), paramSerializerProvider);
              localPropertySerializerMap = this._dynamicSerializers;
              if (localTypeSerializer != null)
                break label184;
              localJsonSerializer.serialize(localObject, paramJsonGenerator, paramSerializerProvider);
              continue;
            }
          }
        }
        catch (Exception localException)
        {
          wrapAndThrow(paramSerializerProvider, localException, paramCollection, i);
          return;
        }
        JsonSerializer localJsonSerializer = _findAndAddDynamic(localPropertySerializerMap, localClass, paramSerializerProvider);
        continue;
        label184: localJsonSerializer.serializeWithType(localObject, paramJsonGenerator, paramSerializerProvider, localTypeSerializer);
      }
    }

    public void serializeContentsUsing(Collection<?> paramCollection, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, JsonSerializer<Object> paramJsonSerializer)
      throws IOException, JsonGenerationException
    {
      Iterator localIterator = paramCollection.iterator();
      TypeSerializer localTypeSerializer;
      int i;
      if (localIterator.hasNext())
      {
        localTypeSerializer = this._valueTypeSerializer;
        i = 0;
      }
      while (true)
      {
        Object localObject = localIterator.next();
        if (localObject == null);
        try
        {
          paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
          while (true)
          {
            i++;
            if (localIterator.hasNext())
              break;
            return;
            if (localTypeSerializer != null)
              break label92;
            paramJsonSerializer.serialize(localObject, paramJsonGenerator, paramSerializerProvider);
          }
        }
        catch (Exception localException)
        {
          while (true)
          {
            wrapAndThrow(paramSerializerProvider, localException, paramCollection, i);
            continue;
            label92: paramJsonSerializer.serializeWithType(localObject, paramJsonGenerator, paramSerializerProvider, localTypeSerializer);
          }
        }
      }
    }
  }

  public static class EnumSetSerializer extends ContainerSerializers.AsArraySerializer<EnumSet<? extends Enum<?>>>
  {
    public EnumSetSerializer(JavaType paramJavaType, BeanProperty paramBeanProperty)
    {
      super(paramJavaType, true, null, paramBeanProperty);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return this;
    }

    public void serializeContents(EnumSet<? extends Enum<?>> paramEnumSet, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      JsonSerializer localJsonSerializer = this._elementSerializer;
      Iterator localIterator = paramEnumSet.iterator();
      while (localIterator.hasNext())
      {
        Enum localEnum = (Enum)localIterator.next();
        if (localJsonSerializer == null)
          localJsonSerializer = paramSerializerProvider.findValueSerializer(localEnum.getDeclaringClass(), this._property);
        localJsonSerializer.serialize(localEnum, paramJsonGenerator, paramSerializerProvider);
      }
    }
  }

  @JacksonStdImpl
  public static class IndexedListSerializer extends ContainerSerializers.AsArraySerializer<List<?>>
  {
    public IndexedListSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty, JsonSerializer<Object> paramJsonSerializer)
    {
      super(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty, paramJsonSerializer);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return new IndexedListSerializer(this._elementType, this._staticTyping, paramTypeSerializer, this._property, this._elementSerializer);
    }

    public void serializeContents(List<?> paramList, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      if (this._elementSerializer != null)
        serializeContentsUsing(paramList, paramJsonGenerator, paramSerializerProvider, this._elementSerializer);
      int i;
      do
      {
        return;
        if (this._valueTypeSerializer != null)
        {
          serializeTypedContents(paramList, paramJsonGenerator, paramSerializerProvider);
          return;
        }
        i = paramList.size();
      }
      while (i == 0);
      label184: for (int j = 0; ; j++)
        while (true)
        {
          PropertySerializerMap localPropertySerializerMap;
          Class localClass;
          try
          {
            localPropertySerializerMap = this._dynamicSerializers;
            if (j >= i)
              break;
            Object localObject1 = paramList.get(j);
            if (localObject1 != null)
              continue;
            paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
            break label184;
            localClass = localObject1.getClass();
            localObject2 = localPropertySerializerMap.serializerFor(localClass);
            if (localObject2 != null)
              continue;
            if (this._elementType.hasGenericTypes())
            {
              localObject2 = _findAndAddDynamic(localPropertySerializerMap, this._elementType.forcedNarrowBy(localClass), paramSerializerProvider);
              localPropertySerializerMap = this._dynamicSerializers;
              ((JsonSerializer)localObject2).serialize(localObject1, paramJsonGenerator, paramSerializerProvider);
            }
          }
          catch (Exception localException)
          {
            wrapAndThrow(paramSerializerProvider, localException, paramList, j);
            return;
          }
          JsonSerializer localJsonSerializer = _findAndAddDynamic(localPropertySerializerMap, localClass, paramSerializerProvider);
          Object localObject2 = localJsonSerializer;
        }
    }

    public void serializeContentsUsing(List<?> paramList, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, JsonSerializer<Object> paramJsonSerializer)
      throws IOException, JsonGenerationException
    {
      int i = paramList.size();
      if (i == 0);
      while (true)
      {
        return;
        TypeSerializer localTypeSerializer = this._valueTypeSerializer;
        for (int j = 0; j < i; j++)
        {
          Object localObject = paramList.get(j);
          if (localObject == null);
          try
          {
            paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
            continue;
            if (localTypeSerializer == null)
              paramJsonSerializer.serialize(localObject, paramJsonGenerator, paramSerializerProvider);
          }
          catch (Exception localException)
          {
            wrapAndThrow(paramSerializerProvider, localException, paramList, j);
          }
          paramJsonSerializer.serializeWithType(localObject, paramJsonGenerator, paramSerializerProvider, localTypeSerializer);
        }
      }
    }

    public void serializeTypedContents(List<?> paramList, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      int i = paramList.size();
      if (i == 0)
        return;
      label159: for (int j = 0; ; j++)
        while (true)
        {
          PropertySerializerMap localPropertySerializerMap;
          Class localClass;
          try
          {
            TypeSerializer localTypeSerializer = this._valueTypeSerializer;
            localPropertySerializerMap = this._dynamicSerializers;
            if (j >= i)
              break;
            Object localObject1 = paramList.get(j);
            if (localObject1 != null)
              continue;
            paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
            break label159;
            localClass = localObject1.getClass();
            localObject2 = localPropertySerializerMap.serializerFor(localClass);
            if (localObject2 != null)
              continue;
            if (this._elementType.hasGenericTypes())
            {
              localObject2 = _findAndAddDynamic(localPropertySerializerMap, this._elementType.forcedNarrowBy(localClass), paramSerializerProvider);
              localPropertySerializerMap = this._dynamicSerializers;
              ((JsonSerializer)localObject2).serializeWithType(localObject1, paramJsonGenerator, paramSerializerProvider, localTypeSerializer);
            }
          }
          catch (Exception localException)
          {
            wrapAndThrow(paramSerializerProvider, localException, paramList, j);
            return;
          }
          JsonSerializer localJsonSerializer = _findAndAddDynamic(localPropertySerializerMap, localClass, paramSerializerProvider);
          Object localObject2 = localJsonSerializer;
        }
    }
  }

  @JacksonStdImpl
  public static class IterableSerializer extends ContainerSerializers.AsArraySerializer<Iterable<?>>
  {
    public IterableSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
    {
      super(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return new IterableSerializer(this._elementType, this._staticTyping, paramTypeSerializer, this._property);
    }

    public void serializeContents(Iterable<?> paramIterable, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      Iterator localIterator = paramIterable.iterator();
      TypeSerializer localTypeSerializer;
      Object localObject1;
      Object localObject2;
      if (localIterator.hasNext())
      {
        localTypeSerializer = this._valueTypeSerializer;
        localObject1 = null;
        localObject2 = null;
      }
      while (true)
      {
        Object localObject3 = localIterator.next();
        if (localObject3 == null)
          paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
        while (!localIterator.hasNext())
        {
          return;
          Class localClass = localObject3.getClass();
          Object localObject4;
          if (localClass == localObject2)
            localObject4 = localObject1;
          while (true)
          {
            if (localTypeSerializer != null)
              break label118;
            ((JsonSerializer)localObject4).serialize(localObject3, paramJsonGenerator, paramSerializerProvider);
            break;
            localObject4 = paramSerializerProvider.findValueSerializer(localClass, this._property);
            localObject1 = localObject4;
            localObject2 = localClass;
          }
          label118: ((JsonSerializer)localObject4).serializeWithType(localObject3, paramJsonGenerator, paramSerializerProvider, localTypeSerializer);
        }
      }
    }
  }

  @JacksonStdImpl
  public static class IteratorSerializer extends ContainerSerializers.AsArraySerializer<Iterator<?>>
  {
    public IteratorSerializer(JavaType paramJavaType, boolean paramBoolean, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
    {
      super(paramJavaType, paramBoolean, paramTypeSerializer, paramBeanProperty);
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
    {
      return new IteratorSerializer(this._elementType, this._staticTyping, paramTypeSerializer, this._property);
    }

    public void serializeContents(Iterator<?> paramIterator, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
      throws IOException, JsonGenerationException
    {
      TypeSerializer localTypeSerializer;
      Object localObject1;
      Object localObject2;
      if (paramIterator.hasNext())
      {
        localTypeSerializer = this._valueTypeSerializer;
        localObject1 = null;
        localObject2 = null;
      }
      while (true)
      {
        Object localObject3 = paramIterator.next();
        if (localObject3 == null)
          paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
        while (!paramIterator.hasNext())
        {
          return;
          Class localClass = localObject3.getClass();
          Object localObject4;
          if (localClass == localObject2)
            localObject4 = localObject1;
          while (true)
          {
            if (localTypeSerializer != null)
              break label107;
            ((JsonSerializer)localObject4).serialize(localObject3, paramJsonGenerator, paramSerializerProvider);
            break;
            localObject4 = paramSerializerProvider.findValueSerializer(localClass, this._property);
            localObject1 = localObject4;
            localObject2 = localClass;
          }
          label107: ((JsonSerializer)localObject4).serializeWithType(localObject3, paramJsonGenerator, paramSerializerProvider, localTypeSerializer);
        }
      }
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ContainerSerializers
 * JD-Core Version:    0.6.0
 */