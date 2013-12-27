package org.codehaus.jackson.map;

import java.util.Collection;
import java.util.LinkedHashMap;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

public abstract class BeanDescription
{
  protected final JavaType _type;

  protected BeanDescription(JavaType paramJavaType)
  {
    this._type = paramJavaType;
  }

  public abstract TypeBindings bindingsForBeanType();

  public abstract LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> paramVisibilityChecker, Collection<String> paramCollection);

  public abstract LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> paramVisibilityChecker);

  public Class<?> getBeanClass()
  {
    return this._type.getRawClass();
  }

  public abstract Annotations getClassAnnotations();

  public JavaType getType()
  {
    return this._type;
  }

  public abstract boolean hasKnownClassAnnotations();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.BeanDescription
 * JD-Core Version:    0.6.0
 */