package org.codehaus.jackson.map.type;

import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;

public final class SimpleType extends TypeBase
{
  protected final String[] _typeNames;
  protected final JavaType[] _typeParameters;

  protected SimpleType(Class<?> paramClass)
  {
    this(paramClass, null, null);
  }

  protected SimpleType(Class<?> paramClass, String[] paramArrayOfString, JavaType[] paramArrayOfJavaType)
  {
    super(paramClass, 0);
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
    {
      this._typeNames = null;
      this._typeParameters = null;
      return;
    }
    this._typeNames = paramArrayOfString;
    this._typeParameters = paramArrayOfJavaType;
  }

  public static SimpleType construct(Class<?> paramClass)
  {
    if (Map.class.isAssignableFrom(paramClass))
      throw new IllegalArgumentException("Can not construct SimpleType for a Map (class: " + paramClass.getName() + ")");
    if (Collection.class.isAssignableFrom(paramClass))
      throw new IllegalArgumentException("Can not construct SimpleType for a Collection (class: " + paramClass.getName() + ")");
    if (paramClass.isArray())
      throw new IllegalArgumentException("Can not construct SimpleType for an array (class: " + paramClass.getName() + ")");
    return new SimpleType(paramClass);
  }

  public static SimpleType constructUnsafe(Class<?> paramClass)
  {
    return new SimpleType(paramClass, null, null);
  }

  protected JavaType _narrow(Class<?> paramClass)
  {
    return new SimpleType(paramClass, this._typeNames, this._typeParameters);
  }

  protected String buildCanonicalName()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this._class.getName());
    if ((this._typeParameters != null) && (this._typeParameters.length > 0))
    {
      localStringBuilder.append('<');
      int i = 1;
      JavaType[] arrayOfJavaType = this._typeParameters;
      int j = arrayOfJavaType.length;
      int k = 0;
      if (k < j)
      {
        JavaType localJavaType = arrayOfJavaType[k];
        if (i != 0)
          i = 0;
        while (true)
        {
          localStringBuilder.append(localJavaType.toCanonical());
          k++;
          break;
          localStringBuilder.append(',');
        }
      }
      localStringBuilder.append('>');
    }
    return localStringBuilder.toString();
  }

  public JavaType containedType(int paramInt)
  {
    if ((paramInt < 0) || (this._typeParameters == null) || (paramInt >= this._typeParameters.length))
      return null;
    return this._typeParameters[paramInt];
  }

  public int containedTypeCount()
  {
    if (this._typeParameters == null)
      return 0;
    return this._typeParameters.length;
  }

  public String containedTypeName(int paramInt)
  {
    if ((paramInt < 0) || (this._typeNames == null) || (paramInt >= this._typeNames.length))
      return null;
    return this._typeNames[paramInt];
  }

  public boolean equals(Object paramObject)
  {
    int i;
    if (paramObject == this)
      i = 1;
    JavaType[] arrayOfJavaType1;
    JavaType[] arrayOfJavaType2;
    int j;
    int k;
    do
    {
      do
      {
        while (true)
        {
          return i;
          i = 0;
          if (paramObject == null)
            continue;
          Class localClass1 = paramObject.getClass();
          Class localClass2 = getClass();
          i = 0;
          if (localClass1 != localClass2)
            continue;
          SimpleType localSimpleType = (SimpleType)paramObject;
          Class localClass3 = localSimpleType._class;
          Class localClass4 = this._class;
          i = 0;
          if (localClass3 != localClass4)
            continue;
          arrayOfJavaType1 = this._typeParameters;
          arrayOfJavaType2 = localSimpleType._typeParameters;
          if (arrayOfJavaType1 != null)
            break;
          if (arrayOfJavaType2 != null)
          {
            int i1 = arrayOfJavaType2.length;
            i = 0;
            if (i1 != 0)
              continue;
          }
          return true;
        }
        i = 0;
      }
      while (arrayOfJavaType2 == null);
      j = arrayOfJavaType1.length;
      k = arrayOfJavaType2.length;
      i = 0;
    }
    while (j != k);
    int m = 0;
    int n = arrayOfJavaType1.length;
    while (true)
    {
      if (m >= n)
        break label168;
      boolean bool = arrayOfJavaType1[m].equals(arrayOfJavaType2[m]);
      i = 0;
      if (!bool)
        break;
      m++;
    }
    label168: return true;
  }

  public StringBuilder getErasedSignature(StringBuilder paramStringBuilder)
  {
    return _classSignature(this._class, paramStringBuilder, true);
  }

  public StringBuilder getGenericSignature(StringBuilder paramStringBuilder)
  {
    _classSignature(this._class, paramStringBuilder, false);
    if (this._typeParameters != null)
    {
      paramStringBuilder.append('<');
      JavaType[] arrayOfJavaType = this._typeParameters;
      int i = arrayOfJavaType.length;
      for (int j = 0; j < i; j++)
        paramStringBuilder = arrayOfJavaType[j].getGenericSignature(paramStringBuilder);
      paramStringBuilder.append('>');
    }
    paramStringBuilder.append(';');
    return paramStringBuilder;
  }

  public boolean isContainerType()
  {
    return false;
  }

  public JavaType narrowContentsBy(Class<?> paramClass)
  {
    throw new IllegalArgumentException("Internal error: SimpleType.narrowContentsBy() should never be called");
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(40);
    localStringBuilder.append("[simple type, class ").append(buildCanonicalName()).append(']');
    return localStringBuilder.toString();
  }

  public JavaType widenContentsBy(Class<?> paramClass)
  {
    throw new IllegalArgumentException("Internal error: SimpleType.widenContentsBy() should never be called");
  }

  public JavaType withContentTypeHandler(Object paramObject)
  {
    throw new IllegalArgumentException("Simple types have no content types; can not call withContenTypeHandler()");
  }

  public SimpleType withTypeHandler(Object paramObject)
  {
    SimpleType localSimpleType = new SimpleType(this._class, this._typeNames, this._typeParameters);
    localSimpleType._typeHandler = paramObject;
    return localSimpleType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.type.SimpleType
 * JD-Core Version:    0.6.0
 */