package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.codehaus.jackson.type.JavaType;

public abstract class MapperConfig<T extends MapperConfig<T>>
  implements ClassIntrospector.MixInResolver
{
  protected static final DateFormat DEFAULT_DATE_FORMAT = StdDateFormat.instance;
  protected Base _base;
  protected HashMap<ClassKey, Class<?>> _mixInAnnotations;
  protected boolean _mixInAnnotationsShared;
  protected SubtypeResolver _subtypeResolver;

  protected MapperConfig(ClassIntrospector<? extends BeanDescription> paramClassIntrospector, AnnotationIntrospector paramAnnotationIntrospector, VisibilityChecker<?> paramVisibilityChecker, SubtypeResolver paramSubtypeResolver, PropertyNamingStrategy paramPropertyNamingStrategy, TypeFactory paramTypeFactory, HandlerInstantiator paramHandlerInstantiator)
  {
    this._base = new Base(paramClassIntrospector, paramAnnotationIntrospector, paramVisibilityChecker, paramPropertyNamingStrategy, paramTypeFactory, null, DEFAULT_DATE_FORMAT, paramHandlerInstantiator);
    this._subtypeResolver = paramSubtypeResolver;
    this._mixInAnnotationsShared = true;
  }

  protected MapperConfig(MapperConfig<?> paramMapperConfig)
  {
    this(paramMapperConfig, paramMapperConfig._base, paramMapperConfig._subtypeResolver);
  }

  protected MapperConfig(MapperConfig<?> paramMapperConfig, Base paramBase, SubtypeResolver paramSubtypeResolver)
  {
    this._base = paramBase;
    this._subtypeResolver = paramSubtypeResolver;
    this._mixInAnnotationsShared = true;
    this._mixInAnnotations = paramMapperConfig._mixInAnnotations;
  }

  public final void addMixInAnnotations(Class<?> paramClass1, Class<?> paramClass2)
  {
    if (this._mixInAnnotations == null)
    {
      this._mixInAnnotationsShared = false;
      this._mixInAnnotations = new HashMap();
    }
    while (true)
    {
      this._mixInAnnotations.put(new ClassKey(paramClass1), paramClass2);
      return;
      if (!this._mixInAnnotationsShared)
        continue;
      this._mixInAnnotationsShared = false;
      this._mixInAnnotations = new HashMap(this._mixInAnnotations);
    }
  }

  public final void appendAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
  {
    this._base = this._base.withAnnotationIntrospector(AnnotationIntrospector.Pair.create(getAnnotationIntrospector(), paramAnnotationIntrospector));
  }

  public abstract boolean canOverrideAccessModifiers();

  public final JavaType constructType(Class<?> paramClass)
  {
    return getTypeFactory().constructType(paramClass);
  }

  public abstract T createUnshared(SubtypeResolver paramSubtypeResolver);

  @Deprecated
  public abstract T createUnshared(TypeResolverBuilder<?> paramTypeResolverBuilder, VisibilityChecker<?> paramVisibilityChecker, SubtypeResolver paramSubtypeResolver);

  public final Class<?> findMixInClassFor(Class<?> paramClass)
  {
    if (this._mixInAnnotations == null)
      return null;
    return (Class)this._mixInAnnotations.get(new ClassKey(paramClass));
  }

  public abstract void fromAnnotations(Class<?> paramClass);

  public AnnotationIntrospector getAnnotationIntrospector()
  {
    return this._base.getAnnotationIntrospector();
  }

  public ClassIntrospector<? extends BeanDescription> getClassIntrospector()
  {
    return this._base.getClassIntrospector();
  }

  public final DateFormat getDateFormat()
  {
    return this._base.getDateFormat();
  }

  public final TypeResolverBuilder<?> getDefaultTyper(JavaType paramJavaType)
  {
    return this._base.getTypeResolverBuilder();
  }

  public final VisibilityChecker<?> getDefaultVisibilityChecker()
  {
    return this._base.getVisibilityChecker();
  }

  public final HandlerInstantiator getHandlerInstantiator()
  {
    return this._base.getHandlerInstantiator();
  }

  public final PropertyNamingStrategy getPropertyNamingStrategy()
  {
    return this._base.getPropertyNamingStrategy();
  }

  public final SubtypeResolver getSubtypeResolver()
  {
    if (this._subtypeResolver == null)
      this._subtypeResolver = new StdSubtypeResolver();
    return this._subtypeResolver;
  }

  public final TypeFactory getTypeFactory()
  {
    return this._base.getTypeFactory();
  }

  public final void insertAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
  {
    this._base = this._base.withAnnotationIntrospector(AnnotationIntrospector.Pair.create(paramAnnotationIntrospector, getAnnotationIntrospector()));
  }

  public abstract <DESC extends BeanDescription> DESC introspectClassAnnotations(Class<?> paramClass);

  public abstract <DESC extends BeanDescription> DESC introspectDirectClassAnnotations(Class<?> paramClass);

  public abstract boolean isAnnotationProcessingEnabled();

  public final int mixInCount()
  {
    if (this._mixInAnnotations == null)
      return 0;
    return this._mixInAnnotations.size();
  }

  @Deprecated
  public final void setAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
  {
    this._base = this._base.withAnnotationIntrospector(paramAnnotationIntrospector);
  }

  @Deprecated
  public void setDateFormat(DateFormat paramDateFormat)
  {
    if (paramDateFormat == null)
      paramDateFormat = StdDateFormat.instance;
    this._base = this._base.withDateFormat(paramDateFormat);
  }

  @Deprecated
  public final void setIntrospector(ClassIntrospector<? extends BeanDescription> paramClassIntrospector)
  {
    this._base = this._base.withClassIntrospector(paramClassIntrospector);
  }

  public final void setMixInAnnotations(Map<Class<?>, Class<?>> paramMap)
  {
    HashMap localHashMap = null;
    if (paramMap != null)
    {
      int i = paramMap.size();
      localHashMap = null;
      if (i > 0)
      {
        localHashMap = new HashMap(paramMap.size());
        Iterator localIterator = paramMap.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          localHashMap.put(new ClassKey((Class)localEntry.getKey()), localEntry.getValue());
        }
      }
    }
    this._mixInAnnotationsShared = false;
    this._mixInAnnotations = localHashMap;
  }

  @Deprecated
  public final void setSubtypeResolver(SubtypeResolver paramSubtypeResolver)
  {
    this._subtypeResolver = paramSubtypeResolver;
  }

  public TypeIdResolver typeIdResolverInstance(Annotated paramAnnotated, Class<? extends TypeIdResolver> paramClass)
  {
    HandlerInstantiator localHandlerInstantiator = getHandlerInstantiator();
    if (localHandlerInstantiator != null)
    {
      TypeIdResolver localTypeIdResolver = localHandlerInstantiator.typeIdResolverInstance(this, paramAnnotated, paramClass);
      if (localTypeIdResolver != null)
        return localTypeIdResolver;
    }
    return (TypeIdResolver)ClassUtil.createInstance(paramClass, canOverrideAccessModifiers());
  }

  public TypeResolverBuilder<?> typeResolverBuilderInstance(Annotated paramAnnotated, Class<? extends TypeResolverBuilder<?>> paramClass)
  {
    HandlerInstantiator localHandlerInstantiator = getHandlerInstantiator();
    if (localHandlerInstantiator != null)
    {
      TypeResolverBuilder localTypeResolverBuilder = localHandlerInstantiator.typeResolverBuilderInstance(this, paramAnnotated, paramClass);
      if (localTypeResolverBuilder != null)
        return localTypeResolverBuilder;
    }
    return (TypeResolverBuilder)ClassUtil.createInstance(paramClass, canOverrideAccessModifiers());
  }

  public abstract T withAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);

  public abstract T withClassIntrospector(ClassIntrospector<? extends BeanDescription> paramClassIntrospector);

  public abstract T withDateFormat(DateFormat paramDateFormat);

  public abstract T withHandlerInstantiator(HandlerInstantiator paramHandlerInstantiator);

  public abstract T withPropertyNamingStrategy(PropertyNamingStrategy paramPropertyNamingStrategy);

  public abstract T withSubtypeResolver(SubtypeResolver paramSubtypeResolver);

  public abstract T withTypeFactory(TypeFactory paramTypeFactory);

  public abstract T withTypeResolverBuilder(TypeResolverBuilder<?> paramTypeResolverBuilder);

  public abstract T withVisibilityChecker(VisibilityChecker<?> paramVisibilityChecker);

  public static class Base
  {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final ClassIntrospector<? extends BeanDescription> _classIntrospector;
    protected final DateFormat _dateFormat;
    protected final HandlerInstantiator _handlerInstantiator;
    protected final PropertyNamingStrategy _propertyNamingStrategy;
    protected final TypeFactory _typeFactory;
    protected final TypeResolverBuilder<?> _typeResolverBuilder;
    protected final VisibilityChecker<?> _visibilityChecker;

    public Base(ClassIntrospector<? extends BeanDescription> paramClassIntrospector, AnnotationIntrospector paramAnnotationIntrospector, VisibilityChecker<?> paramVisibilityChecker, PropertyNamingStrategy paramPropertyNamingStrategy, TypeFactory paramTypeFactory, TypeResolverBuilder<?> paramTypeResolverBuilder, DateFormat paramDateFormat, HandlerInstantiator paramHandlerInstantiator)
    {
      this._classIntrospector = paramClassIntrospector;
      this._annotationIntrospector = paramAnnotationIntrospector;
      this._visibilityChecker = paramVisibilityChecker;
      this._propertyNamingStrategy = paramPropertyNamingStrategy;
      this._typeFactory = paramTypeFactory;
      this._typeResolverBuilder = paramTypeResolverBuilder;
      this._dateFormat = paramDateFormat;
      this._handlerInstantiator = paramHandlerInstantiator;
    }

    public AnnotationIntrospector getAnnotationIntrospector()
    {
      return this._annotationIntrospector;
    }

    public ClassIntrospector<? extends BeanDescription> getClassIntrospector()
    {
      return this._classIntrospector;
    }

    public DateFormat getDateFormat()
    {
      return this._dateFormat;
    }

    public HandlerInstantiator getHandlerInstantiator()
    {
      return this._handlerInstantiator;
    }

    public PropertyNamingStrategy getPropertyNamingStrategy()
    {
      return this._propertyNamingStrategy;
    }

    public TypeFactory getTypeFactory()
    {
      return this._typeFactory;
    }

    public TypeResolverBuilder<?> getTypeResolverBuilder()
    {
      return this._typeResolverBuilder;
    }

    public VisibilityChecker<?> getVisibilityChecker()
    {
      return this._visibilityChecker;
    }

    public Base withAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
    {
      return new Base(this._classIntrospector, paramAnnotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
    }

    public Base withClassIntrospector(ClassIntrospector<? extends BeanDescription> paramClassIntrospector)
    {
      return new Base(paramClassIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
    }

    public Base withDateFormat(DateFormat paramDateFormat)
    {
      return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, paramDateFormat, this._handlerInstantiator);
    }

    public Base withHandlerInstantiator(HandlerInstantiator paramHandlerInstantiator)
    {
      return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, paramHandlerInstantiator);
    }

    public Base withPropertyNamingStrategy(PropertyNamingStrategy paramPropertyNamingStrategy)
    {
      return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, paramPropertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
    }

    public Base withTypeFactory(TypeFactory paramTypeFactory)
    {
      return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, paramTypeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
    }

    public Base withTypeResolverBuilder(TypeResolverBuilder<?> paramTypeResolverBuilder)
    {
      return new Base(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, paramTypeResolverBuilder, this._dateFormat, this._handlerInstantiator);
    }

    public Base withVisibilityChecker(VisibilityChecker<?> paramVisibilityChecker)
    {
      return new Base(this._classIntrospector, this._annotationIntrospector, paramVisibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator);
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.MapperConfig
 * JD-Core Version:    0.6.0
 */