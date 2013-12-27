package org.codehaus.jackson.map.type;

import java.util.Map;
import org.codehaus.jackson.type.JavaType;

public class MapLikeType extends TypeBase
{
  protected final JavaType _keyType;
  protected final JavaType _valueType;

  protected MapLikeType(Class<?> paramClass, JavaType paramJavaType1, JavaType paramJavaType2)
  {
    super(paramClass, paramJavaType1.hashCode() ^ paramJavaType2.hashCode());
    this._keyType = paramJavaType1;
    this._valueType = paramJavaType2;
  }

  public static MapLikeType construct(Class<?> paramClass, JavaType paramJavaType1, JavaType paramJavaType2)
  {
    return new MapLikeType(paramClass, paramJavaType1, paramJavaType2);
  }

  protected JavaType _narrow(Class<?> paramClass)
  {
    return new MapLikeType(paramClass, this._keyType, this._valueType);
  }

  protected String buildCanonicalName()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this._class.getName());
    if (this._keyType != null)
    {
      localStringBuilder.append('<');
      localStringBuilder.append(this._keyType.toCanonical());
      localStringBuilder.append(',');
      localStringBuilder.append(this._valueType.toCanonical());
      localStringBuilder.append('>');
    }
    return localStringBuilder.toString();
  }

  public JavaType containedType(int paramInt)
  {
    if (paramInt == 0)
      return this._keyType;
    if (paramInt == 1)
      return this._valueType;
    return null;
  }

  public int containedTypeCount()
  {
    return 2;
  }

  public String containedTypeName(int paramInt)
  {
    if (paramInt == 0)
      return "K";
    if (paramInt == 1)
      return "V";
    return null;
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    MapLikeType localMapLikeType;
    do
    {
      return true;
      if (paramObject == null)
        return false;
      if (paramObject.getClass() != getClass())
        return false;
      localMapLikeType = (MapLikeType)paramObject;
    }
    while ((this._class == localMapLikeType._class) && (this._keyType.equals(localMapLikeType._keyType)) && (this._valueType.equals(localMapLikeType._valueType)));
    return false;
  }

  public JavaType getContentType()
  {
    return this._valueType;
  }

  public StringBuilder getErasedSignature(StringBuilder paramStringBuilder)
  {
    return _classSignature(this._class, paramStringBuilder, true);
  }

  public StringBuilder getGenericSignature(StringBuilder paramStringBuilder)
  {
    _classSignature(this._class, paramStringBuilder, false);
    paramStringBuilder.append('<');
    this._keyType.getGenericSignature(paramStringBuilder);
    this._valueType.getGenericSignature(paramStringBuilder);
    paramStringBuilder.append(">;");
    return paramStringBuilder;
  }

  public JavaType getKeyType()
  {
    return this._keyType;
  }

  public boolean isContainerType()
  {
    return true;
  }

  public boolean isMapLikeType()
  {
    return true;
  }

  public boolean isTrueMapType()
  {
    return Map.class.isAssignableFrom(this._class);
  }

  public JavaType narrowContentsBy(Class<?> paramClass)
  {
    if (paramClass == this._valueType.getRawClass())
      return this;
    return new MapLikeType(this._class, this._keyType, this._valueType.narrowBy(paramClass)).copyHandlers(this);
  }

  public JavaType narrowKey(Class<?> paramClass)
  {
    if (paramClass == this._keyType.getRawClass())
      return this;
    return new MapLikeType(this._class, this._keyType.narrowBy(paramClass), this._valueType).copyHandlers(this);
  }

  public String toString()
  {
    return "[map-like type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
  }

  public JavaType widenContentsBy(Class<?> paramClass)
  {
    if (paramClass == this._valueType.getRawClass())
      return this;
    return new MapLikeType(this._class, this._keyType, this._valueType.widenBy(paramClass)).copyHandlers(this);
  }

  public JavaType widenKey(Class<?> paramClass)
  {
    if (paramClass == this._keyType.getRawClass())
      return this;
    return new MapLikeType(this._class, this._keyType.widenBy(paramClass), this._valueType).copyHandlers(this);
  }

  public MapLikeType withContentTypeHandler(Object paramObject)
  {
    return new MapLikeType(this._class, this._keyType, this._valueType.withTypeHandler(paramObject));
  }

  public MapLikeType withTypeHandler(Object paramObject)
  {
    MapLikeType localMapLikeType = new MapLikeType(this._class, this._keyType, this._valueType);
    localMapLikeType._typeHandler = paramObject;
    return localMapLikeType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.type.MapLikeType
 * JD-Core Version:    0.6.0
 */