package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class EnumMapSerializer extends ContainerSerializerBase<EnumMap<? extends Enum<?>, ?>>
  implements ResolvableSerializer
{
  protected final EnumValues _keyEnums;
  protected final BeanProperty _property;
  protected final boolean _staticTyping;
  protected JsonSerializer<Object> _valueSerializer;
  protected final JavaType _valueType;
  protected final TypeSerializer _valueTypeSerializer;

  @Deprecated
  public EnumMapSerializer(JavaType paramJavaType, boolean paramBoolean, EnumValues paramEnumValues, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty)
  {
    this(paramJavaType, paramBoolean, paramEnumValues, paramTypeSerializer, paramBeanProperty, null);
  }

  public EnumMapSerializer(JavaType paramJavaType, boolean paramBoolean, EnumValues paramEnumValues, TypeSerializer paramTypeSerializer, BeanProperty paramBeanProperty, JsonSerializer<Object> paramJsonSerializer)
  {
    super(EnumMap.class, false);
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
    this._valueType = paramJavaType;
    this._keyEnums = paramEnumValues;
    this._valueTypeSerializer = paramTypeSerializer;
    this._property = paramBeanProperty;
    this._valueSerializer = paramJsonSerializer;
  }

  public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer)
  {
    return new EnumMapSerializer(this._valueType, this._staticTyping, this._keyEnums, paramTypeSerializer, this._property);
  }

  public JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType)
    throws JsonMappingException
  {
    ObjectNode localObjectNode1 = createSchemaNode("object", true);
    if ((paramType instanceof ParameterizedType))
    {
      Type[] arrayOfType = ((ParameterizedType)paramType).getActualTypeArguments();
      if (arrayOfType.length == 2)
      {
        JavaType localJavaType1 = paramSerializerProvider.constructType(arrayOfType[0]);
        JavaType localJavaType2 = paramSerializerProvider.constructType(arrayOfType[1]);
        ObjectNode localObjectNode2 = JsonNodeFactory.instance.objectNode();
        Enum[] arrayOfEnum = (Enum[])localJavaType1.getRawClass().getEnumConstants();
        int i = arrayOfEnum.length;
        int j = 0;
        if (j < i)
        {
          Enum localEnum = arrayOfEnum[j];
          JsonSerializer localJsonSerializer = paramSerializerProvider.findValueSerializer(localJavaType2.getRawClass(), this._property);
          if ((localJsonSerializer instanceof SchemaAware));
          for (JsonNode localJsonNode = ((SchemaAware)localJsonSerializer).getSchema(paramSerializerProvider, null); ; localJsonNode = JsonSchema.getDefaultSchemaNode())
          {
            localObjectNode2.put(paramSerializerProvider.getConfig().getAnnotationIntrospector().findEnumValue(localEnum), localJsonNode);
            j++;
            break;
          }
        }
        localObjectNode1.put("properties", localObjectNode2);
      }
    }
    return localObjectNode1;
  }

  public void resolve(SerializerProvider paramSerializerProvider)
    throws JsonMappingException
  {
    if ((this._staticTyping) && (this._valueSerializer == null))
      this._valueSerializer = paramSerializerProvider.findValueSerializer(this._valueType, this._property);
  }

  public void serialize(EnumMap<? extends Enum<?>, ?> paramEnumMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    paramJsonGenerator.writeStartObject();
    if (!paramEnumMap.isEmpty())
      serializeContents(paramEnumMap, paramJsonGenerator, paramSerializerProvider);
    paramJsonGenerator.writeEndObject();
  }

  protected void serializeContents(EnumMap<? extends Enum<?>, ?> paramEnumMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
    throws IOException, JsonGenerationException
  {
    if (this._valueSerializer != null)
    {
      serializeContentsUsing(paramEnumMap, paramJsonGenerator, paramSerializerProvider, this._valueSerializer);
      return;
    }
    Object localObject1 = null;
    Object localObject2 = null;
    EnumValues localEnumValues = this._keyEnums;
    Iterator localIterator = paramEnumMap.entrySet().iterator();
    label42: Map.Entry localEntry;
    Object localObject3;
    Class localClass;
    Object localObject4;
    while (localIterator.hasNext())
    {
      localEntry = (Map.Entry)localIterator.next();
      Enum localEnum = (Enum)localEntry.getKey();
      if (localEnumValues == null)
        localEnumValues = ((EnumSerializer)(SerializerBase)paramSerializerProvider.findValueSerializer(localEnum.getDeclaringClass(), this._property)).getEnumValues();
      paramJsonGenerator.writeFieldName(localEnumValues.serializedValueFor(localEnum));
      localObject3 = localEntry.getValue();
      if (localObject3 == null)
      {
        paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
        continue;
      }
      localClass = localObject3.getClass();
      if (localClass != localObject2)
        break label194;
      localObject4 = localObject1;
    }
    while (true)
    {
      try
      {
        ((JsonSerializer)localObject4).serialize(localObject3, paramJsonGenerator, paramSerializerProvider);
      }
      catch (Exception localException)
      {
        wrapAndThrow(paramSerializerProvider, localException, paramEnumMap, ((Enum)localEntry.getKey()).name());
      }
      break label42;
      break;
      label194: localObject4 = paramSerializerProvider.findValueSerializer(localClass, this._property);
      localObject1 = localObject4;
      localObject2 = localClass;
    }
  }

  protected void serializeContentsUsing(EnumMap<? extends Enum<?>, ?> paramEnumMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, JsonSerializer<Object> paramJsonSerializer)
    throws IOException, JsonGenerationException
  {
    EnumValues localEnumValues = this._keyEnums;
    Iterator localIterator = paramEnumMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Enum localEnum = (Enum)localEntry.getKey();
      if (localEnumValues == null)
        localEnumValues = ((EnumSerializer)(SerializerBase)paramSerializerProvider.findValueSerializer(localEnum.getDeclaringClass(), this._property)).getEnumValues();
      paramJsonGenerator.writeFieldName(localEnumValues.serializedValueFor(localEnum));
      Object localObject = localEntry.getValue();
      if (localObject == null)
      {
        paramSerializerProvider.defaultSerializeNull(paramJsonGenerator);
        continue;
      }
      try
      {
        paramJsonSerializer.serialize(localObject, paramJsonGenerator, paramSerializerProvider);
      }
      catch (Exception localException)
      {
        wrapAndThrow(paramSerializerProvider, localException, paramEnumMap, ((Enum)localEntry.getKey()).name());
      }
    }
  }

  public void serializeWithType(EnumMap<? extends Enum<?>, ?> paramEnumMap, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
    throws IOException, JsonGenerationException
  {
    paramTypeSerializer.writeTypePrefixForObject(paramEnumMap, paramJsonGenerator);
    if (!paramEnumMap.isEmpty())
      serializeContents(paramEnumMap, paramJsonGenerator, paramSerializerProvider);
    paramTypeSerializer.writeTypeSuffixForObject(paramEnumMap, paramJsonGenerator);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.EnumMapSerializer
 * JD-Core Version:    0.6.0
 */