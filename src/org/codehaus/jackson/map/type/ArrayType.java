package org.codehaus.jackson.map.type;

import java.lang.reflect.Array;
import org.codehaus.jackson.type.JavaType;

public final class ArrayType extends TypeBase
{
  final JavaType _componentType;
  final Object _emptyArray;

  private ArrayType(JavaType paramJavaType, Object paramObject)
  {
    super(paramObject.getClass(), paramJavaType.hashCode());
    this._componentType = paramJavaType;
    this._emptyArray = paramObject;
  }

  public static ArrayType construct(JavaType paramJavaType)
  {
    return new ArrayType(paramJavaType, Array.newInstance(paramJavaType.getRawClass(), 0));
  }

  protected JavaType _narrow(Class<?> paramClass)
  {
    if (!paramClass.isArray())
      throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + toString() + " to class " + paramClass.getName());
    Class localClass = paramClass.getComponentType();
    return construct(TypeFactory.defaultInstance().constructType(localClass));
  }

  protected String buildCanonicalName()
  {
    return this._class.getName();
  }

  public JavaType containedType(int paramInt)
  {
    if (paramInt == 0)
      return this._componentType;
    return null;
  }

  public int containedTypeCount()
  {
    return 1;
  }

  public String containedTypeName(int paramInt)
  {
    if (paramInt == 0)
      return "E";
    return null;
  }

  public boolean equals(Object paramObject)
  {
    int i;
    if (paramObject == this)
      i = 1;
    Class localClass1;
    Class localClass2;
    do
    {
      do
      {
        return i;
        i = 0;
      }
      while (paramObject == null);
      localClass1 = paramObject.getClass();
      localClass2 = getClass();
      i = 0;
    }
    while (localClass1 != localClass2);
    ArrayType localArrayType = (ArrayType)paramObject;
    return this._componentType.equals(localArrayType._componentType);
  }

  public JavaType getContentType()
  {
    return this._componentType;
  }

  public StringBuilder getErasedSignature(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append('[');
    return this._componentType.getErasedSignature(paramStringBuilder);
  }

  public StringBuilder getGenericSignature(StringBuilder paramStringBuilder)
  {
    paramStringBuilder.append('[');
    return this._componentType.getGenericSignature(paramStringBuilder);
  }

  public boolean hasGenericTypes()
  {
    return this._componentType.hasGenericTypes();
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean isArrayType()
  {
    return true;
  }

  public boolean isConcrete()
  {
    return true;
  }

  public boolean isContainerType()
  {
    return true;
  }

  public JavaType narrowContentsBy(Class<?> paramClass)
  {
    if (paramClass == this._componentType.getRawClass())
      return this;
    return construct(this._componentType.narrowBy(paramClass)).copyHandlers(this);
  }

  public String toString()
  {
    return "[array type, component type: " + this._componentType + "]";
  }

  public JavaType widenContentsBy(Class<?> paramClass)
  {
    if (paramClass == this._componentType.getRawClass())
      return this;
    return construct(this._componentType.widenBy(paramClass)).copyHandlers(this);
  }

  public ArrayType withContentTypeHandler(Object paramObject)
  {
    return new ArrayType(this._componentType.withTypeHandler(paramObject), this._emptyArray);
  }

  public ArrayType withTypeHandler(Object paramObject)
  {
    ArrayType localArrayType = new ArrayType(this._componentType, this._emptyArray);
    localArrayType._typeHandler = paramObject;
    return localArrayType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.type.ArrayType
 * JD-Core Version:    0.6.0
 */