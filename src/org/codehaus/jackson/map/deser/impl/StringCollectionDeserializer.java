package org.codehaus.jackson.map.deser.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collection<Ljava.lang.String;>;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.deser.ContainerDeserializer;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public final class StringCollectionDeserializer extends ContainerDeserializer<Collection<String>>
{
  protected final JavaType _collectionType;
  final Constructor<Collection<String>> _defaultCtor;
  protected final boolean _isDefaultDeserializer;
  protected final JsonDeserializer<String> _valueDeserializer;

  public StringCollectionDeserializer(JavaType paramJavaType, JsonDeserializer<?> paramJsonDeserializer, Constructor<?> paramConstructor)
  {
    super(paramJavaType.getRawClass());
    this._collectionType = paramJavaType;
    this._valueDeserializer = paramJsonDeserializer;
    this._defaultCtor = paramConstructor;
    this._isDefaultDeserializer = isDefaultSerializer(paramJsonDeserializer);
  }

  private Collection<String> deserializeUsingCustom(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Collection<String> paramCollection)
    throws IOException, JsonProcessingException
  {
    JsonDeserializer localJsonDeserializer = this._valueDeserializer;
    JsonToken localJsonToken = paramJsonParser.nextToken();
    if (localJsonToken != JsonToken.END_ARRAY)
    {
      if (localJsonToken == JsonToken.VALUE_NULL);
      for (Object localObject = null; ; localObject = (String)localJsonDeserializer.deserialize(paramJsonParser, paramDeserializationContext))
      {
        paramCollection.add(localObject);
        break;
      }
    }
    return (Collection<String>)paramCollection;
  }

  private final Collection<String> handleNonArray(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Collection<String> paramCollection)
    throws IOException, JsonProcessingException
  {
    if (!paramDeserializationContext.isEnabled(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
      throw paramDeserializationContext.mappingException(this._collectionType.getRawClass());
    JsonDeserializer localJsonDeserializer = this._valueDeserializer;
    if (paramJsonParser.getCurrentToken() == JsonToken.VALUE_NULL)
    {
      localObject = null;
      paramCollection.add(localObject);
      return paramCollection;
    }
    if (localJsonDeserializer == null);
    for (Object localObject = paramJsonParser.getText(); ; localObject = (String)localJsonDeserializer.deserialize(paramJsonParser, paramDeserializationContext))
      break;
  }

  public Collection<String> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    try
    {
      Collection localCollection = (Collection)this._defaultCtor.newInstance(new Object[0]);
      return deserialize(paramJsonParser, paramDeserializationContext, localCollection);
    }
    catch (Exception localException)
    {
    }
    throw paramDeserializationContext.instantiationException(this._collectionType.getRawClass(), localException);
  }

  public Collection<String> deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Collection<String> paramCollection)
    throws IOException, JsonProcessingException
  {
    if (!paramJsonParser.isExpectedStartArrayToken())
      paramCollection = handleNonArray(paramJsonParser, paramDeserializationContext, paramCollection);
    JsonToken localJsonToken;
    do
    {
      return paramCollection;
      if (!this._isDefaultDeserializer)
        return deserializeUsingCustom(paramJsonParser, paramDeserializationContext, paramCollection);
      localJsonToken = paramJsonParser.nextToken();
    }
    while (localJsonToken == JsonToken.END_ARRAY);
    if (localJsonToken == JsonToken.VALUE_NULL);
    for (Object localObject = null; ; localObject = paramJsonParser.getText())
    {
      paramCollection.add(localObject);
      break;
    }
  }

  public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
    throws IOException, JsonProcessingException
  {
    return paramTypeDeserializer.deserializeTypedFromArray(paramJsonParser, paramDeserializationContext);
  }

  public JsonDeserializer<Object> getContentDeserializer()
  {
    return this._valueDeserializer;
  }

  public JavaType getContentType()
  {
    return this._collectionType.getContentType();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.impl.StringCollectionDeserializer
 * JD-Core Version:    0.6.0
 */