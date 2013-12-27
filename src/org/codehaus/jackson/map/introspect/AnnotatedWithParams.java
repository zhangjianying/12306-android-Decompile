package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public abstract class AnnotatedWithParams extends AnnotatedMember
{
  protected final AnnotationMap _annotations;
  protected final AnnotationMap[] _paramAnnotations;

  protected AnnotatedWithParams(AnnotationMap paramAnnotationMap, AnnotationMap[] paramArrayOfAnnotationMap)
  {
    this._annotations = paramAnnotationMap;
    this._paramAnnotations = paramArrayOfAnnotationMap;
  }

  public final void addIfNotPresent(Annotation paramAnnotation)
  {
    this._annotations.addIfNotPresent(paramAnnotation);
  }

  public final void addOrOverride(Annotation paramAnnotation)
  {
    this._annotations.add(paramAnnotation);
  }

  public final void addOrOverrideParam(int paramInt, Annotation paramAnnotation)
  {
    AnnotationMap localAnnotationMap = this._paramAnnotations[paramInt];
    if (localAnnotationMap == null)
    {
      localAnnotationMap = new AnnotationMap();
      this._paramAnnotations[paramInt] = localAnnotationMap;
    }
    localAnnotationMap.add(paramAnnotation);
  }

  public final <A extends Annotation> A getAnnotation(Class<A> paramClass)
  {
    return this._annotations.get(paramClass);
  }

  public final int getAnnotationCount()
  {
    return this._annotations.size();
  }

  public abstract AnnotatedParameter getParameter(int paramInt);

  public final AnnotationMap getParameterAnnotations(int paramInt)
  {
    if ((this._paramAnnotations != null) && (paramInt >= 0) && (paramInt <= this._paramAnnotations.length))
      return this._paramAnnotations[paramInt];
    return null;
  }

  public abstract Class<?> getParameterClass(int paramInt);

  public abstract int getParameterCount();

  public abstract Type getParameterType(int paramInt);

  protected JavaType getType(TypeBindings paramTypeBindings, TypeVariable<?>[] paramArrayOfTypeVariable)
  {
    if ((paramArrayOfTypeVariable != null) && (paramArrayOfTypeVariable.length > 0))
    {
      paramTypeBindings = paramTypeBindings.childInstance();
      int i = paramArrayOfTypeVariable.length;
      int j = 0;
      if (j < i)
      {
        TypeVariable<?> localTypeVariable = paramArrayOfTypeVariable[j];
        paramTypeBindings._addPlaceholder(localTypeVariable.getName());
        Type localType = localTypeVariable.getBounds()[0];
        if (localType == null);
        for (JavaType localJavaType = TypeFactory.unknownType(); ; localJavaType = paramTypeBindings.resolveType(localType))
        {
          paramTypeBindings.addBinding(localTypeVariable.getName(), localJavaType);
          j++;
          break;
        }
      }
    }
    return paramTypeBindings.resolveType(getGenericType());
  }

  public final JavaType resolveParameterType(int paramInt, TypeBindings paramTypeBindings)
  {
    return paramTypeBindings.resolveType(getParameterType(paramInt));
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedWithParams
 * JD-Core Version:    0.6.0
 */