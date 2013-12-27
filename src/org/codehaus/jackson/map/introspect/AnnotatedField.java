package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

public final class AnnotatedField extends AnnotatedMember
{
  protected final AnnotationMap _annotations;
  protected final Field _field;

  public AnnotatedField(Field paramField, AnnotationMap paramAnnotationMap)
  {
    this._field = paramField;
    this._annotations = paramAnnotationMap;
  }

  public void addOrOverride(Annotation paramAnnotation)
  {
    this._annotations.add(paramAnnotation);
  }

  public Field getAnnotated()
  {
    return this._field;
  }

  public <A extends Annotation> A getAnnotation(Class<A> paramClass)
  {
    return this._annotations.get(paramClass);
  }

  public int getAnnotationCount()
  {
    return this._annotations.size();
  }

  public Class<?> getDeclaringClass()
  {
    return this._field.getDeclaringClass();
  }

  public String getFullName()
  {
    return getDeclaringClass().getName() + "#" + getName();
  }

  public Type getGenericType()
  {
    return this._field.getGenericType();
  }

  public Member getMember()
  {
    return this._field;
  }

  public int getModifiers()
  {
    return this._field.getModifiers();
  }

  public String getName()
  {
    return this._field.getName();
  }

  public Class<?> getRawType()
  {
    return this._field.getType();
  }

  public String toString()
  {
    return "[field " + getName() + ", annotations: " + this._annotations + "]";
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedField
 * JD-Core Version:    0.6.0
 */