package org.codehaus.jackson.map.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class HierarchicType
{
  protected final Type _actualType;
  protected final ParameterizedType _genericType;
  protected final Class<?> _rawClass;
  protected HierarchicType _subType;
  protected HierarchicType _superType;

  public HierarchicType(Type paramType)
  {
    this._actualType = paramType;
    if ((paramType instanceof Class))
    {
      this._rawClass = ((Class)paramType);
      this._genericType = null;
      return;
    }
    if ((paramType instanceof ParameterizedType))
    {
      this._genericType = ((ParameterizedType)paramType);
      this._rawClass = ((Class)this._genericType.getRawType());
      return;
    }
    throw new IllegalArgumentException("Type " + paramType.getClass().getName() + " can not be used to construct HierarchicType");
  }

  public ParameterizedType asGeneric()
  {
    return this._genericType;
  }

  public Class<?> getRawClass()
  {
    return this._rawClass;
  }

  public HierarchicType getSubType()
  {
    return this._subType;
  }

  public HierarchicType getSuperType()
  {
    return this._superType;
  }

  public boolean isGeneric()
  {
    return this._genericType != null;
  }

  public void setSubType(HierarchicType paramHierarchicType)
  {
    this._subType = paramHierarchicType;
  }

  public void setSuperType(HierarchicType paramHierarchicType)
  {
    this._superType = paramHierarchicType;
  }

  public String toString()
  {
    if (this._genericType != null)
      return this._genericType.toString();
    return this._rawClass.getName();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.type.HierarchicType
 * JD-Core Version:    0.6.0
 */