package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.type.JavaType;

public final class AnnotatedMethod extends AnnotatedWithParams
{
  protected final Method _method;
  protected Class<?>[] _paramTypes;

  public AnnotatedMethod(Method paramMethod, AnnotationMap paramAnnotationMap, AnnotationMap[] paramArrayOfAnnotationMap)
  {
    super(paramAnnotationMap, paramArrayOfAnnotationMap);
    this._method = paramMethod;
  }

  public Method getAnnotated()
  {
    return this._method;
  }

  public Class<?> getDeclaringClass()
  {
    return this._method.getDeclaringClass();
  }

  public String getFullName()
  {
    return getDeclaringClass().getName() + "#" + getName() + "(" + getParameterCount() + " params)";
  }

  public Type getGenericType()
  {
    return this._method.getGenericReturnType();
  }

  public Member getMember()
  {
    return this._method;
  }

  public int getModifiers()
  {
    return this._method.getModifiers();
  }

  public String getName()
  {
    return this._method.getName();
  }

  public AnnotatedParameter getParameter(int paramInt)
  {
    return new AnnotatedParameter(this, getParameterType(paramInt), this._paramAnnotations[paramInt]);
  }

  public Class<?> getParameterClass(int paramInt)
  {
    Class[] arrayOfClass = this._method.getParameterTypes();
    if (paramInt >= arrayOfClass.length)
      return null;
    return arrayOfClass[paramInt];
  }

  public Class<?>[] getParameterClasses()
  {
    if (this._paramTypes == null)
      this._paramTypes = this._method.getParameterTypes();
    return this._paramTypes;
  }

  public int getParameterCount()
  {
    return getParameterTypes().length;
  }

  public Type getParameterType(int paramInt)
  {
    Type[] arrayOfType = this._method.getGenericParameterTypes();
    if (paramInt >= arrayOfType.length)
      return null;
    return arrayOfType[paramInt];
  }

  public Type[] getParameterTypes()
  {
    return this._method.getGenericParameterTypes();
  }

  public Class<?> getRawType()
  {
    return this._method.getReturnType();
  }

  public JavaType getType(TypeBindings paramTypeBindings)
  {
    return getType(paramTypeBindings, this._method.getTypeParameters());
  }

  public String toString()
  {
    return "[method " + getName() + ", annotations: " + this._annotations + "]";
  }

  public AnnotatedMethod withMethod(Method paramMethod)
  {
    return new AnnotatedMethod(paramMethod, this._annotations, this._paramAnnotations);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedMethod
 * JD-Core Version:    0.6.0
 */