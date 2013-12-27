package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

public final class MapType extends MapLikeType
{
  private MapType(Class<?> paramClass, JavaType paramJavaType1, JavaType paramJavaType2)
  {
    super(paramClass, paramJavaType1, paramJavaType2);
  }

  public static MapType construct(Class<?> paramClass, JavaType paramJavaType1, JavaType paramJavaType2)
  {
    return new MapType(paramClass, paramJavaType1, paramJavaType2);
  }

  protected JavaType _narrow(Class<?> paramClass)
  {
    return new MapType(paramClass, this._keyType, this._valueType);
  }

  public JavaType narrowContentsBy(Class<?> paramClass)
  {
    if (paramClass == this._valueType.getRawClass())
      return this;
    return new MapType(this._class, this._keyType, this._valueType.narrowBy(paramClass)).copyHandlers(this);
  }

  public JavaType narrowKey(Class<?> paramClass)
  {
    if (paramClass == this._keyType.getRawClass())
      return this;
    return new MapType(this._class, this._keyType.narrowBy(paramClass), this._valueType).copyHandlers(this);
  }

  public String toString()
  {
    return "[map type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
  }

  public JavaType widenContentsBy(Class<?> paramClass)
  {
    if (paramClass == this._valueType.getRawClass())
      return this;
    return new MapType(this._class, this._keyType, this._valueType.widenBy(paramClass)).copyHandlers(this);
  }

  public JavaType widenKey(Class<?> paramClass)
  {
    if (paramClass == this._keyType.getRawClass())
      return this;
    return new MapType(this._class, this._keyType.widenBy(paramClass), this._valueType).copyHandlers(this);
  }

  public MapType withContentTypeHandler(Object paramObject)
  {
    return new MapType(this._class, this._keyType, this._valueType.withTypeHandler(paramObject));
  }

  public MapType withTypeHandler(Object paramObject)
  {
    MapType localMapType = new MapType(this._class, this._keyType, this._valueType);
    localMapType._typeHandler = paramObject;
    return localMapType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.type.MapType
 * JD-Core Version:    0.6.0
 */