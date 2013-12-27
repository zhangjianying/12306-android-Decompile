package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map<Ljava.lang.Object;Ljava.lang.Object;>;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class MapDeserializer extends ContainerDeserializer<Map<Object, Object>>
  implements ResolvableDeserializer
{
  protected final Constructor<Map<Object, Object>> _defaultCtor;
  protected HashSet<String> _ignorableProperties;
  protected final KeyDeserializer _keyDeserializer;
  protected final JavaType _mapType;
  protected Creator.PropertyBased _propertyBasedCreator;
  protected final JsonDeserializer<Object> _valueDeserializer;
  protected final TypeDeserializer _valueTypeDeserializer;

  public MapDeserializer(JavaType paramJavaType, Constructor<Map<Object, Object>> paramConstructor, KeyDeserializer paramKeyDeserializer, JsonDeserializer<Object> paramJsonDeserializer, TypeDeserializer paramTypeDeserializer)
  {
    super(Map.class);
    this._mapType = paramJavaType;
    this._defaultCtor = paramConstructor;
    this._keyDeserializer = paramKeyDeserializer;
    this._valueDeserializer = paramJsonDeserializer;
    this._valueTypeDeserializer = paramTypeDeserializer;
  }

  public Map<Object, Object> _deserializeUsingCreator(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    Creator.PropertyBased localPropertyBased = this._propertyBasedCreator;
    PropertyValueBuffer localPropertyValueBuffer = localPropertyBased.startBuilding(paramJsonParser, paramDeserializationContext);
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    if (localJsonToken1 == JsonToken.START_OBJECT)
      localJsonToken1 = paramJsonParser.nextToken();
    JsonDeserializer localJsonDeserializer = this._valueDeserializer;
    TypeDeserializer localTypeDeserializer = this._valueTypeDeserializer;
    if (localJsonToken1 == JsonToken.FIELD_NAME)
    {
      String str1 = paramJsonParser.getCurrentName();
      JsonToken localJsonToken2 = paramJsonParser.nextToken();
      if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(str1)))
        paramJsonParser.skipChildren();
      SettableBeanProperty localSettableBeanProperty;
      Object localObject3;
      do
      {
        localJsonToken1 = paramJsonParser.nextToken();
        break;
        localSettableBeanProperty = localPropertyBased.findCreatorProperty(str1);
        if (localSettableBeanProperty == null)
          break label179;
        localObject3 = localSettableBeanProperty.deserialize(paramJsonParser, paramDeserializationContext);
      }
      while (!localPropertyValueBuffer.assignParameter(localSettableBeanProperty.getCreatorIndex(), localObject3));
      paramJsonParser.nextToken();
      try
      {
        Map localMap2 = (Map)localPropertyBased.build(localPropertyValueBuffer);
        _readAndBind(paramJsonParser, paramDeserializationContext, localMap2);
        return localMap2;
      }
      catch (Exception localException2)
      {
        wrapAndThrow(localException2, this._mapType.getRawClass());
        return null;
      }
      label179: String str2 = paramJsonParser.getCurrentName();
      Object localObject1;
      label196: Object localObject2;
      if (this._keyDeserializer == null)
      {
        localObject1 = str2;
        if (localJsonToken2 != JsonToken.VALUE_NULL)
          break label234;
        localObject2 = null;
      }
      while (true)
      {
        localPropertyValueBuffer.bufferMapProperty(localObject1, localObject2);
        break;
        localObject1 = this._keyDeserializer.deserializeKey(str2, paramDeserializationContext);
        break label196;
        label234: if (localTypeDeserializer == null)
        {
          localObject2 = localJsonDeserializer.deserialize(paramJsonParser, paramDeserializationContext);
          continue;
        }
        localObject2 = localJsonDeserializer.deserializeWithType(paramJsonParser, paramDeserializationContext, localTypeDeserializer);
      }
    }
    try
    {
      Map localMap1 = (Map)localPropertyBased.build(localPropertyValueBuffer);
      return localMap1;
    }
    catch (Exception localException1)
    {
      wrapAndThrow(localException1, this._mapType.getRawClass());
    }
    return (Map<Object, Object>)null;
  }

  protected final void _readAndBind(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Map<Object, Object> paramMap)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken1 = paramJsonParser.getCurrentToken();
    if (localJsonToken1 == JsonToken.START_OBJECT)
      localJsonToken1 = paramJsonParser.nextToken();
    KeyDeserializer localKeyDeserializer = this._keyDeserializer;
    JsonDeserializer localJsonDeserializer = this._valueDeserializer;
    TypeDeserializer localTypeDeserializer = this._valueTypeDeserializer;
    if (localJsonToken1 == JsonToken.FIELD_NAME)
    {
      String str = paramJsonParser.getCurrentName();
      if (localKeyDeserializer == null);
      JsonToken localJsonToken2;
      for (Object localObject1 = str; ; localObject1 = localKeyDeserializer.deserializeKey(str, paramDeserializationContext))
      {
        localJsonToken2 = paramJsonParser.nextToken();
        if ((this._ignorableProperties == null) || (!this._ignorableProperties.contains(str)))
          break label113;
        paramJsonParser.skipChildren();
        localJsonToken1 = paramJsonParser.nextToken();
        break;
      }
      label113: Object localObject2;
      if (localJsonToken2 == JsonToken.VALUE_NULL)
        localObject2 = null;
      while (true)
      {
        paramMap.put(localObject1, localObject2);
        break;
        if (localTypeDeserializer == null)
        {
          localObject2 = localJsonDeserializer.deserialize(paramJsonParser, paramDeserializationContext);
          continue;
        }
        localObject2 = localJsonDeserializer.deserializeWithType(paramJsonParser, paramDeserializationContext, localTypeDeserializer);
      }
    }
  }

  public Map<Object, Object> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if ((localJsonToken != JsonToken.START_OBJECT) && (localJsonToken != JsonToken.FIELD_NAME) && (localJsonToken != JsonToken.END_OBJECT))
      throw paramDeserializationContext.mappingException(getMapClass());
    if (this._propertyBasedCreator != null)
      return _deserializeUsingCreator(paramJsonParser, paramDeserializationContext);
    if (this._defaultCtor == null)
      throw paramDeserializationContext.instantiationException(getMapClass(), "No default constructor found");
    try
    {
      Map localMap = (Map)this._defaultCtor.newInstance(new Object[0]);
      _readAndBind(paramJsonParser, paramDeserializationContext, localMap);
      return localMap;
    }
    catch (Exception localException)
    {
    }
    throw paramDeserializationContext.instantiationException(getMapClass(), localException);
  }

  public Map<Object, Object> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Map<Object, Object> paramMap)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if ((localJsonToken != JsonToken.START_OBJECT) && (localJsonToken != JsonToken.FIELD_NAME))
      throw paramDeserializationContext.mappingException(getMapClass());
    _readAndBind(paramJsonParser, paramDeserializationContext, paramMap);
    return paramMap;
  }

  public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
    throws IOException, JsonProcessingException
  {
    return paramTypeDeserializer.deserializeTypedFromObject(paramJsonParser, paramDeserializationContext);
  }

  public JsonDeserializer<Object> getContentDeserializer()
  {
    return this._valueDeserializer;
  }

  public JavaType getContentType()
  {
    return this._mapType.getContentType();
  }

  public final Class<?> getMapClass()
  {
    return this._mapType.getRawClass();
  }

  public JavaType getValueType()
  {
    return this._mapType;
  }

  public void resolve(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    if (this._propertyBasedCreator != null)
    {
      Iterator localIterator = this._propertyBasedCreator.properties().iterator();
      while (localIterator.hasNext())
      {
        SettableBeanProperty localSettableBeanProperty = (SettableBeanProperty)localIterator.next();
        localSettableBeanProperty.setValueDeserializer(findDeserializer(paramDeserializationConfig, paramDeserializerProvider, localSettableBeanProperty.getType(), localSettableBeanProperty));
      }
    }
  }

  public void setCreators(CreatorContainer paramCreatorContainer)
  {
    this._propertyBasedCreator = paramCreatorContainer.propertyBasedCreator();
  }

  public void setIgnorableProperties(String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0));
    for (HashSet localHashSet = null; ; localHashSet = ArrayBuilders.arrayToSet(paramArrayOfString))
    {
      this._ignorableProperties = localHashSet;
      return;
    }
  }

  protected void wrapAndThrow(Throwable paramThrowable, Object paramObject)
    throws IOException
  {
    while (((paramThrowable instanceof InvocationTargetException)) && (paramThrowable.getCause() != null))
      paramThrowable = paramThrowable.getCause();
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    if (((paramThrowable instanceof IOException)) && (!(paramThrowable instanceof JsonMappingException)))
      throw ((IOException)paramThrowable);
    throw JsonMappingException.wrapWithPath(paramThrowable, paramObject, null);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.MapDeserializer
 * JD-Core Version:    0.6.0
 */