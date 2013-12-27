package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public final class AnnotatedParameter extends AnnotatedMember
{
  protected final AnnotationMap _annotations;
  protected final AnnotatedMember _owner;
  protected final Type _type;

  public AnnotatedParameter(AnnotatedMember paramAnnotatedMember, Type paramType, AnnotationMap paramAnnotationMap)
  {
    this._owner = paramAnnotatedMember;
    this._type = paramType;
    this._annotations = paramAnnotationMap;
  }

  public void addOrOverride(Annotation paramAnnotation)
  {
    this._annotations.add(paramAnnotation);
  }

  public AnnotatedElement getAnnotated()
  {
    return null;
  }

  public <A extends Annotation> A getAnnotation(Class<A> paramClass)
  {
    return this._annotations.get(paramClass);
  }

  public Class<?> getDeclaringClass()
  {
    return this._owner.getDeclaringClass();
  }

  public Type getGenericType()
  {
    return this._type;
  }

  public Member getMember()
  {
    return this._owner.getMember();
  }

  public int getModifiers()
  {
    return this._owner.getModifiers();
  }

  public String getName()
  {
    return "";
  }

  public Type getParameterType()
  {
    return this._type;
  }

  public Class<?> getRawType()
  {
    if ((this._type instanceof Class))
      return (Class)this._type;
    return TypeFactory.defaultInstance().constructType(this._type).getRawClass();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedParameter
 * JD-Core Version:    0.6.0
 */