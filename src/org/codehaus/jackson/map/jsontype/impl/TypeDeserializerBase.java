package org.codehaus.jackson.map.jsontype.impl;

import java.io.IOException;
import java.util.HashMap;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.type.JavaType;

public abstract class TypeDeserializerBase extends TypeDeserializer
{
  protected final JavaType _baseType;
  protected final HashMap<String, JsonDeserializer<Object>> _deserializers;
  protected final TypeIdResolver _idResolver;
  protected final BeanProperty _property;

  protected TypeDeserializerBase(JavaType paramJavaType, TypeIdResolver paramTypeIdResolver, BeanProperty paramBeanProperty)
  {
    this._baseType = paramJavaType;
    this._idResolver = paramTypeIdResolver;
    this._property = paramBeanProperty;
    this._deserializers = new HashMap();
  }

  protected final JsonDeserializer<Object> _findDeserializer(DeserializationContext paramDeserializationContext, String paramString)
    throws IOException, JsonProcessingException
  {
    JavaType localJavaType;
    synchronized (this._deserializers)
    {
      localJsonDeserializer = (JsonDeserializer)this._deserializers.get(paramString);
      if (localJsonDeserializer != null)
        break label125;
      localJavaType = this._idResolver.typeFromId(paramString);
      if (localJavaType == null)
        throw paramDeserializationContext.unknownTypeException(this._baseType, paramString);
    }
    if ((this._baseType != null) && (this._baseType.getClass() == localJavaType.getClass()))
      localJavaType = this._baseType.narrowBy(localJavaType.getRawClass());
    JsonDeserializer localJsonDeserializer = paramDeserializationContext.getDeserializerProvider().findValueDeserializer(paramDeserializationContext.getConfig(), localJavaType, this._property);
    this._deserializers.put(paramString, localJsonDeserializer);
    label125: monitorexit;
    return localJsonDeserializer;
  }

  public String baseTypeName()
  {
    return this._baseType.getRawClass().getName();
  }

  public String getPropertyName()
  {
    return null;
  }

  public TypeIdResolver getTypeIdResolver()
  {
    return this._idResolver;
  }

  public abstract JsonTypeInfo.As getTypeInclusion();

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[').append(getClass().getName());
    localStringBuilder.append("; base-type:").append(this._baseType);
    localStringBuilder.append("; id-resolver: ").append(this._idResolver);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.TypeDeserializerBase
 * JD-Core Version:    0.6.0
 */