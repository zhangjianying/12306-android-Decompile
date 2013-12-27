package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class SerializationConfig extends MapperConfig<SerializationConfig>
{
  protected static final int DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
  protected int _featureFlags = DEFAULT_FEATURE_FLAGS;
  protected FilterProvider _filterProvider;
  protected JsonSerialize.Inclusion _serializationInclusion = null;
  protected Class<?> _serializationView;

  public SerializationConfig(ClassIntrospector<? extends BeanDescription> paramClassIntrospector, AnnotationIntrospector paramAnnotationIntrospector, VisibilityChecker<?> paramVisibilityChecker, SubtypeResolver paramSubtypeResolver, PropertyNamingStrategy paramPropertyNamingStrategy, TypeFactory paramTypeFactory, HandlerInstantiator paramHandlerInstantiator)
  {
    super(paramClassIntrospector, paramAnnotationIntrospector, paramVisibilityChecker, paramSubtypeResolver, paramPropertyNamingStrategy, paramTypeFactory, paramHandlerInstantiator);
    this._filterProvider = null;
  }

  protected SerializationConfig(SerializationConfig paramSerializationConfig)
  {
    this(paramSerializationConfig, paramSerializationConfig._base);
  }

  protected SerializationConfig(SerializationConfig paramSerializationConfig, Class<?> paramClass)
  {
    super(paramSerializationConfig);
    this._featureFlags = paramSerializationConfig._featureFlags;
    this._serializationInclusion = paramSerializationConfig._serializationInclusion;
    this._serializationView = paramClass;
    this._filterProvider = paramSerializationConfig._filterProvider;
  }

  protected SerializationConfig(SerializationConfig paramSerializationConfig, HashMap<ClassKey, Class<?>> paramHashMap, SubtypeResolver paramSubtypeResolver)
  {
    this(paramSerializationConfig, paramSerializationConfig._base);
    this._mixInAnnotations = paramHashMap;
    this._subtypeResolver = paramSubtypeResolver;
  }

  protected SerializationConfig(SerializationConfig paramSerializationConfig, MapperConfig.Base paramBase)
  {
    super(paramSerializationConfig, paramBase, paramSerializationConfig._subtypeResolver);
    this._featureFlags = paramSerializationConfig._featureFlags;
    this._serializationInclusion = paramSerializationConfig._serializationInclusion;
    this._serializationView = paramSerializationConfig._serializationView;
    this._filterProvider = paramSerializationConfig._filterProvider;
  }

  protected SerializationConfig(SerializationConfig paramSerializationConfig, FilterProvider paramFilterProvider)
  {
    super(paramSerializationConfig);
    this._featureFlags = paramSerializationConfig._featureFlags;
    this._serializationInclusion = paramSerializationConfig._serializationInclusion;
    this._serializationView = paramSerializationConfig._serializationView;
    this._filterProvider = paramFilterProvider;
  }

  public boolean canOverrideAccessModifiers()
  {
    return isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
  }

  public SerializationConfig createUnshared(SubtypeResolver paramSubtypeResolver)
  {
    HashMap localHashMap = this._mixInAnnotations;
    this._mixInAnnotationsShared = true;
    return new SerializationConfig(this, localHashMap, paramSubtypeResolver);
  }

  @Deprecated
  public SerializationConfig createUnshared(TypeResolverBuilder<?> paramTypeResolverBuilder, VisibilityChecker<?> paramVisibilityChecker, SubtypeResolver paramSubtypeResolver)
  {
    return createUnshared(paramSubtypeResolver).withTypeResolverBuilder(paramTypeResolverBuilder).withVisibilityChecker(paramVisibilityChecker);
  }

  public void disable(Feature paramFeature)
  {
    this._featureFlags &= (0xFFFFFFFF ^ paramFeature.getMask());
  }

  public void enable(Feature paramFeature)
  {
    this._featureFlags |= paramFeature.getMask();
  }

  public void fromAnnotations(Class<?> paramClass)
  {
    AnnotationIntrospector localAnnotationIntrospector = getAnnotationIntrospector();
    AnnotatedClass localAnnotatedClass = AnnotatedClass.construct(paramClass, localAnnotationIntrospector, null);
    this._base = this._base.withVisibilityChecker(localAnnotationIntrospector.findAutoDetectVisibility(localAnnotatedClass, getDefaultVisibilityChecker()));
    JsonSerialize.Inclusion localInclusion = localAnnotationIntrospector.findSerializationInclusion(localAnnotatedClass, null);
    if (localInclusion != this._serializationInclusion)
      setSerializationInclusion(localInclusion);
    JsonSerialize.Typing localTyping = localAnnotationIntrospector.findSerializationTyping(localAnnotatedClass);
    Feature localFeature;
    if (localTyping != null)
    {
      localFeature = Feature.USE_STATIC_TYPING;
      if (localTyping != JsonSerialize.Typing.STATIC)
        break label92;
    }
    label92: for (boolean bool = true; ; bool = false)
    {
      set(localFeature, bool);
      return;
    }
  }

  public AnnotationIntrospector getAnnotationIntrospector()
  {
    if (isEnabled(Feature.USE_ANNOTATIONS))
      return super.getAnnotationIntrospector();
    return AnnotationIntrospector.nopInstance();
  }

  public FilterProvider getFilterProvider()
  {
    return this._filterProvider;
  }

  public JsonSerialize.Inclusion getSerializationInclusion()
  {
    if (this._serializationInclusion != null)
      return this._serializationInclusion;
    if (isEnabled(Feature.WRITE_NULL_PROPERTIES))
      return JsonSerialize.Inclusion.ALWAYS;
    return JsonSerialize.Inclusion.NON_NULL;
  }

  public Class<?> getSerializationView()
  {
    return this._serializationView;
  }

  public <T extends BeanDescription> T introspect(JavaType paramJavaType)
  {
    return getClassIntrospector().forSerialization(this, paramJavaType, this);
  }

  public <T extends BeanDescription> T introspectClassAnnotations(Class<?> paramClass)
  {
    return getClassIntrospector().forClassAnnotations(this, paramClass, this);
  }

  public <T extends BeanDescription> T introspectDirectClassAnnotations(Class<?> paramClass)
  {
    return getClassIntrospector().forDirectClassAnnotations(this, paramClass, this);
  }

  public boolean isAnnotationProcessingEnabled()
  {
    return isEnabled(Feature.USE_ANNOTATIONS);
  }

  public final boolean isEnabled(Feature paramFeature)
  {
    return (this._featureFlags & paramFeature.getMask()) != 0;
  }

  public JsonSerializer<Object> serializerInstance(Annotated paramAnnotated, Class<? extends JsonSerializer<?>> paramClass)
  {
    HandlerInstantiator localHandlerInstantiator = getHandlerInstantiator();
    if (localHandlerInstantiator != null)
    {
      JsonSerializer localJsonSerializer = localHandlerInstantiator.serializerInstance(this, paramAnnotated, paramClass);
      if (localJsonSerializer != null)
        return localJsonSerializer;
    }
    return (JsonSerializer)ClassUtil.createInstance(paramClass, canOverrideAccessModifiers());
  }

  public void set(Feature paramFeature, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      enable(paramFeature);
      return;
    }
    disable(paramFeature);
  }

  @Deprecated
  public final void setDateFormat(DateFormat paramDateFormat)
  {
    super.setDateFormat(paramDateFormat);
    Feature localFeature = Feature.WRITE_DATES_AS_TIMESTAMPS;
    if (paramDateFormat == null);
    for (boolean bool = true; ; bool = false)
    {
      set(localFeature, bool);
      return;
    }
  }

  public void setSerializationInclusion(JsonSerialize.Inclusion paramInclusion)
  {
    this._serializationInclusion = paramInclusion;
    if (paramInclusion == JsonSerialize.Inclusion.NON_NULL)
    {
      disable(Feature.WRITE_NULL_PROPERTIES);
      return;
    }
    enable(Feature.WRITE_NULL_PROPERTIES);
  }

  @Deprecated
  public void setSerializationView(Class<?> paramClass)
  {
    this._serializationView = paramClass;
  }

  public String toString()
  {
    return "[SerializationConfig: flags=0x" + Integer.toHexString(this._featureFlags) + "]";
  }

  public SerializationConfig withAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
  {
    return new SerializationConfig(this, this._base.withAnnotationIntrospector(paramAnnotationIntrospector));
  }

  public SerializationConfig withClassIntrospector(ClassIntrospector<? extends BeanDescription> paramClassIntrospector)
  {
    return new SerializationConfig(this, this._base.withClassIntrospector(paramClassIntrospector));
  }

  public SerializationConfig withDateFormat(DateFormat paramDateFormat)
  {
    SerializationConfig localSerializationConfig = new SerializationConfig(this, this._base.withDateFormat(paramDateFormat));
    Feature localFeature = Feature.WRITE_DATES_AS_TIMESTAMPS;
    if (paramDateFormat == null);
    for (boolean bool = true; ; bool = false)
    {
      localSerializationConfig.set(localFeature, bool);
      return localSerializationConfig;
    }
  }

  public SerializationConfig withFilters(FilterProvider paramFilterProvider)
  {
    return new SerializationConfig(this, paramFilterProvider);
  }

  public SerializationConfig withHandlerInstantiator(HandlerInstantiator paramHandlerInstantiator)
  {
    return new SerializationConfig(this, this._base.withHandlerInstantiator(paramHandlerInstantiator));
  }

  public SerializationConfig withPropertyNamingStrategy(PropertyNamingStrategy paramPropertyNamingStrategy)
  {
    return new SerializationConfig(this, this._base.withPropertyNamingStrategy(paramPropertyNamingStrategy));
  }

  public SerializationConfig withSubtypeResolver(SubtypeResolver paramSubtypeResolver)
  {
    SerializationConfig localSerializationConfig = new SerializationConfig(this);
    localSerializationConfig._subtypeResolver = paramSubtypeResolver;
    return localSerializationConfig;
  }

  public SerializationConfig withTypeFactory(TypeFactory paramTypeFactory)
  {
    return new SerializationConfig(this, this._base.withTypeFactory(paramTypeFactory));
  }

  public SerializationConfig withTypeResolverBuilder(TypeResolverBuilder<?> paramTypeResolverBuilder)
  {
    return new SerializationConfig(this, this._base.withTypeResolverBuilder(paramTypeResolverBuilder));
  }

  public SerializationConfig withView(Class<?> paramClass)
  {
    return new SerializationConfig(this, paramClass);
  }

  public SerializationConfig withVisibilityChecker(VisibilityChecker<?> paramVisibilityChecker)
  {
    return new SerializationConfig(this, this._base.withVisibilityChecker(paramVisibilityChecker));
  }

  public static enum Feature
  {
    final boolean _defaultState;

    static
    {
      AUTO_DETECT_GETTERS = new Feature("AUTO_DETECT_GETTERS", 1, true);
      AUTO_DETECT_IS_GETTERS = new Feature("AUTO_DETECT_IS_GETTERS", 2, true);
      AUTO_DETECT_FIELDS = new Feature("AUTO_DETECT_FIELDS", 3, true);
      CAN_OVERRIDE_ACCESS_MODIFIERS = new Feature("CAN_OVERRIDE_ACCESS_MODIFIERS", 4, true);
      WRITE_NULL_PROPERTIES = new Feature("WRITE_NULL_PROPERTIES", 5, true);
      USE_STATIC_TYPING = new Feature("USE_STATIC_TYPING", 6, false);
      DEFAULT_VIEW_INCLUSION = new Feature("DEFAULT_VIEW_INCLUSION", 7, true);
      WRAP_ROOT_VALUE = new Feature("WRAP_ROOT_VALUE", 8, false);
      INDENT_OUTPUT = new Feature("INDENT_OUTPUT", 9, false);
      SORT_PROPERTIES_ALPHABETICALLY = new Feature("SORT_PROPERTIES_ALPHABETICALLY", 10, false);
      FAIL_ON_EMPTY_BEANS = new Feature("FAIL_ON_EMPTY_BEANS", 11, true);
      WRAP_EXCEPTIONS = new Feature("WRAP_EXCEPTIONS", 12, true);
      CLOSE_CLOSEABLE = new Feature("CLOSE_CLOSEABLE", 13, false);
      FLUSH_AFTER_WRITE_VALUE = new Feature("FLUSH_AFTER_WRITE_VALUE", 14, true);
      WRITE_DATES_AS_TIMESTAMPS = new Feature("WRITE_DATES_AS_TIMESTAMPS", 15, true);
      WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS = new Feature("WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS", 16, false);
      WRITE_ENUMS_USING_TO_STRING = new Feature("WRITE_ENUMS_USING_TO_STRING", 17, false);
      WRITE_NULL_MAP_VALUES = new Feature("WRITE_NULL_MAP_VALUES", 18, true);
      Feature[] arrayOfFeature = new Feature[19];
      arrayOfFeature[0] = USE_ANNOTATIONS;
      arrayOfFeature[1] = AUTO_DETECT_GETTERS;
      arrayOfFeature[2] = AUTO_DETECT_IS_GETTERS;
      arrayOfFeature[3] = AUTO_DETECT_FIELDS;
      arrayOfFeature[4] = CAN_OVERRIDE_ACCESS_MODIFIERS;
      arrayOfFeature[5] = WRITE_NULL_PROPERTIES;
      arrayOfFeature[6] = USE_STATIC_TYPING;
      arrayOfFeature[7] = DEFAULT_VIEW_INCLUSION;
      arrayOfFeature[8] = WRAP_ROOT_VALUE;
      arrayOfFeature[9] = INDENT_OUTPUT;
      arrayOfFeature[10] = SORT_PROPERTIES_ALPHABETICALLY;
      arrayOfFeature[11] = FAIL_ON_EMPTY_BEANS;
      arrayOfFeature[12] = WRAP_EXCEPTIONS;
      arrayOfFeature[13] = CLOSE_CLOSEABLE;
      arrayOfFeature[14] = FLUSH_AFTER_WRITE_VALUE;
      arrayOfFeature[15] = WRITE_DATES_AS_TIMESTAMPS;
      arrayOfFeature[16] = WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS;
      arrayOfFeature[17] = WRITE_ENUMS_USING_TO_STRING;
      arrayOfFeature[18] = WRITE_NULL_MAP_VALUES;
      $VALUES = arrayOfFeature;
    }

    private Feature(boolean paramBoolean)
    {
      this._defaultState = paramBoolean;
    }

    public static int collectDefaults()
    {
      int i = 0;
      for (Feature localFeature : values())
      {
        if (!localFeature.enabledByDefault())
          continue;
        i |= localFeature.getMask();
      }
      return i;
    }

    public boolean enabledByDefault()
    {
      return this._defaultState;
    }

    public int getMask()
    {
      return 1 << ordinal();
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.SerializationConfig
 * JD-Core Version:    0.6.0
 */