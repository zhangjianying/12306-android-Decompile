package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.type.JavaType;

public final class AnnotatedConstructor extends AnnotatedWithParams
{
  protected final Constructor<?> _constructor;

  public AnnotatedConstructor(Constructor<?> paramConstructor, AnnotationMap paramAnnotationMap, AnnotationMap[] paramArrayOfAnnotationMap)
  {
    super(paramAnnotationMap, paramArrayOfAnnotationMap);
    if (paramConstructor == null)
      throw new IllegalArgumentException("Null constructor not allowed");
    this._constructor = paramConstructor;
  }

  public Constructor<?> getAnnotated()
  {
    return this._constructor;
  }

  public Class<?> getDeclaringClass()
  {
    return this._constructor.getDeclaringClass();
  }

  public Type getGenericType()
  {
    return getRawType();
  }

  public Member getMember()
  {
    return this._constructor;
  }

  public int getModifiers()
  {
    return this._constructor.getModifiers();
  }

  public String getName()
  {
    return this._constructor.getName();
  }

  public AnnotatedParameter getParameter(int paramInt)
  {
    return new AnnotatedParameter(this, getParameterType(paramInt), this._paramAnnotations[paramInt]);
  }

  public Class<?> getParameterClass(int paramInt)
  {
    Class[] arrayOfClass = this._constructor.getParameterTypes();
    if (paramInt >= arrayOfClass.length)
      return null;
    return arrayOfClass[paramInt];
  }

  public int getParameterCount()
  {
    return this._constructor.getParameterTypes().length;
  }

  public Type getParameterType(int paramInt)
  {
    Type[] arrayOfType = this._constructor.getGenericParameterTypes();
    if (paramInt >= arrayOfType.length)
      return null;
    return arrayOfType[paramInt];
  }

  public Class<?> getRawType()
  {
    return this._constructor.getDeclaringClass();
  }

  public JavaType getType(TypeBindings paramTypeBindings)
  {
    return getType(paramTypeBindings, this._constructor.getTypeParameters());
  }

  public String toString()
  {
    return "[constructor for " + getName() + ", annotations: " + this._annotations + "]";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedConstructor
 * JD-Core Version:    0.6.0
 */