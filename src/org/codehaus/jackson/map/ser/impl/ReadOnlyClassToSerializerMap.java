package org.codehaus.jackson.map.ser.impl;

import java.util.HashMap;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ser.SerializerCache.TypeKey;
import org.codehaus.jackson.type.JavaType;

public final class ReadOnlyClassToSerializerMap
{
  protected final SerializerCache.TypeKey _cacheKey = new SerializerCache.TypeKey(getClass(), false);
  protected final JsonSerializerMap _map;

  private ReadOnlyClassToSerializerMap(JsonSerializerMap paramJsonSerializerMap)
  {
    this._map = paramJsonSerializerMap;
  }

  public static ReadOnlyClassToSerializerMap from(HashMap<SerializerCache.TypeKey, JsonSerializer<Object>> paramHashMap)
  {
    return new ReadOnlyClassToSerializerMap(new JsonSerializerMap(paramHashMap));
  }

  public ReadOnlyClassToSerializerMap instance()
  {
    return new ReadOnlyClassToSerializerMap(this._map);
  }

  public JsonSerializer<Object> typedValueSerializer(Class<?> paramClass)
  {
    this._cacheKey.resetTyped(paramClass);
    return this._map.find(this._cacheKey);
  }

  public JsonSerializer<Object> typedValueSerializer(JavaType paramJavaType)
  {
    this._cacheKey.resetTyped(paramJavaType);
    return this._map.find(this._cacheKey);
  }

  public JsonSerializer<Object> untypedValueSerializer(Class<?> paramClass)
  {
    this._cacheKey.resetUntyped(paramClass);
    return this._map.find(this._cacheKey);
  }

  public JsonSerializer<Object> untypedValueSerializer(JavaType paramJavaType)
  {
    this._cacheKey.resetUntyped(paramJavaType);
    return this._map.find(this._cacheKey);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.ReadOnlyClassToSerializerMap
 * JD-Core Version:    0.6.0
 */