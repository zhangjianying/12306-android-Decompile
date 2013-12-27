package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class MemberKey
{
  static final Class<?>[] NO_CLASSES = new Class[0];
  final Class<?>[] _argTypes;
  final String _name;

  public MemberKey(String paramString, Class<?>[] paramArrayOfClass)
  {
    this._name = paramString;
    if (paramArrayOfClass == null)
      paramArrayOfClass = NO_CLASSES;
    this._argTypes = paramArrayOfClass;
  }

  public MemberKey(Constructor<?> paramConstructor)
  {
    this("", paramConstructor.getParameterTypes());
  }

  public MemberKey(Method paramMethod)
  {
    this(paramMethod.getName(), paramMethod.getParameterTypes());
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this)
      return true;
    if (paramObject == null)
      return false;
    if (paramObject.getClass() != getClass())
      return false;
    MemberKey localMemberKey = (MemberKey)paramObject;
    if (!this._name.equals(localMemberKey._name))
      return false;
    Class[] arrayOfClass = localMemberKey._argTypes;
    int i = this._argTypes.length;
    if (arrayOfClass.length != i)
      return false;
    int j = 0;
    label71: Class localClass1;
    Class localClass2;
    if (j < i)
    {
      localClass1 = arrayOfClass[j];
      localClass2 = this._argTypes[j];
      if (localClass1 != localClass2)
        break label106;
    }
    label106: 
    do
    {
      j++;
      break label71;
      break;
    }
    while ((localClass1.isAssignableFrom(localClass2)) || (localClass2.isAssignableFrom(localClass1)));
    return false;
  }

  public int hashCode()
  {
    return this._name.hashCode() + this._argTypes.length;
  }

  public String toString()
  {
    return this._name + "(" + this._argTypes.length + "-args)";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.MemberKey
 * JD-Core Version:    0.6.0
 */