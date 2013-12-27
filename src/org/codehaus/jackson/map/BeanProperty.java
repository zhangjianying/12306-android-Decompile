package org.codehaus.jackson.map;

import java.lang.annotation.Annotation;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

public abstract interface BeanProperty
{
  public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);

  public abstract <A extends Annotation> A getContextAnnotation(Class<A> paramClass);

  public abstract AnnotatedMember getMember();

  public abstract String getName();

  public abstract JavaType getType();

  public static class Std
    implements BeanProperty
  {
    protected final Annotations _contextAnnotations;
    protected final AnnotatedMember _member;
    protected final String _name;
    protected final JavaType _type;

    public Std(String paramString, JavaType paramJavaType, Annotations paramAnnotations, AnnotatedMember paramAnnotatedMember)
    {
      this._name = paramString;
      this._type = paramJavaType;
      this._member = paramAnnotatedMember;
      this._contextAnnotations = paramAnnotations;
    }

    public <A extends Annotation> A getAnnotation(Class<A> paramClass)
    {
      return this._member.getAnnotation(paramClass);
    }

    public <A extends Annotation> A getContextAnnotation(Class<A> paramClass)
    {
      if (this._contextAnnotations == null)
        return null;
      return this._contextAnnotations.get(paramClass);
    }

    public AnnotatedMember getMember()
    {
      return this._member;
    }

    public String getName()
    {
      return this._name;
    }

    public JavaType getType()
    {
      return this._type;
    }

    public Std withType(JavaType paramJavaType)
    {
      return new Std(this._name, paramJavaType, this._contextAnnotations, this._member);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.BeanProperty
 * JD-Core Version:    0.6.0
 */