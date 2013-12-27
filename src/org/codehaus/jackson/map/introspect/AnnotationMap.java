package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import org.codehaus.jackson.map.util.Annotations;

public final class AnnotationMap
  implements Annotations
{
  protected HashMap<Class<? extends Annotation>, Annotation> _annotations;

  protected final void _add(Annotation paramAnnotation)
  {
    if (this._annotations == null)
      this._annotations = new HashMap();
    this._annotations.put(paramAnnotation.annotationType(), paramAnnotation);
  }

  public void add(Annotation paramAnnotation)
  {
    _add(paramAnnotation);
  }

  public void addIfNotPresent(Annotation paramAnnotation)
  {
    if ((this._annotations == null) || (!this._annotations.containsKey(paramAnnotation.annotationType())))
      _add(paramAnnotation);
  }

  public <A extends Annotation> A get(Class<A> paramClass)
  {
    if (this._annotations == null)
      return null;
    return (Annotation)this._annotations.get(paramClass);
  }

  public int size()
  {
    if (this._annotations == null)
      return 0;
    return this._annotations.size();
  }

  public String toString()
  {
    if (this._annotations == null)
      return "[null]";
    return this._annotations.toString();
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotationMap
 * JD-Core Version:    0.6.0
 */