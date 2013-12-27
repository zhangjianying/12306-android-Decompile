package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.LinkedNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.type.JavaType;

public class DeserializationConfig extends MapperConfig<DeserializationConfig>
{
  protected static final int DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
  protected AbstractTypeResolver _abstractTypeResolver;
  protected int _featureFlags = DEFAULT_FEATURE_FLAGS;
  protected JsonNodeFactory _nodeFactory;
  protected LinkedNode<DeserializationProblemHandler> _problemHandlers;

  public DeserializationConfig(ClassIntrospector<? extends BeanDescription> paramClassIntrospector, AnnotationIntrospector paramAnnotationIntrospector, VisibilityChecker<?> paramVisibilityChecker, SubtypeResolver paramSubtypeResolver, PropertyNamingStrategy paramPropertyNamingStrategy, TypeFactory paramTypeFactory, HandlerInstantiator paramHandlerInstantiator)
  {
    super(paramClassIntrospector, paramAnnotationIntrospector, paramVisibilityChecker, paramSubtypeResolver, paramPropertyNamingStrategy, paramTypeFactory, paramHandlerInstantiator);
    this._nodeFactory = JsonNodeFactory.instance;
  }

  protected DeserializationConfig(DeserializationConfig paramDeserializationConfig)
  {
    this(paramDeserializationConfig, paramDeserializationConfig._base);
  }

  private DeserializationConfig(DeserializationConfig paramDeserializationConfig, HashMap<ClassKey, Class<?>> paramHashMap, SubtypeResolver paramSubtypeResolver)
  {
    this(paramDeserializationConfig, paramDeserializationConfig._base);
    this._mixInAnnotations = paramHashMap;
    this._subtypeResolver = paramSubtypeResolver;
  }

  protected DeserializationConfig(DeserializationConfig paramDeserializationConfig, MapperConfig.Base paramBase)
  {
    super(paramDeserializationConfig, paramBase, paramDeserializationConfig._subtypeResolver);
    this._featureFlags = paramDeserializationConfig._featureFlags;
    this._abstractTypeResolver = paramDeserializationConfig._abstractTypeResolver;
    this._problemHandlers = paramDeserializationConfig._problemHandlers;
    this._nodeFactory = paramDeserializationConfig._nodeFactory;
  }

  protected DeserializationConfig(DeserializationConfig paramDeserializationConfig, JsonNodeFactory paramJsonNodeFactory)
  {
    super(paramDeserializationConfig);
    this._featureFlags = paramDeserializationConfig._featureFlags;
    this._abstractTypeResolver = paramDeserializationConfig._abstractTypeResolver;
    this._problemHandlers = paramDeserializationConfig._problemHandlers;
    this._nodeFactory = paramJsonNodeFactory;
  }

  public void addHandler(DeserializationProblemHandler paramDeserializationProblemHandler)
  {
    if (!LinkedNode.contains(this._problemHandlers, paramDeserializationProblemHandler))
      this._problemHandlers = new LinkedNode(paramDeserializationProblemHandler, this._problemHandlers);
  }

  public boolean canOverrideAccessModifiers()
  {
    return isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
  }

  public void clearHandlers()
  {
    this._problemHandlers = null;
  }

  public DeserializationConfig createUnshared(SubtypeResolver paramSubtypeResolver)
  {
    HashMap localHashMap = this._mixInAnnotations;
    this._mixInAnnotationsShared = true;
    return new DeserializationConfig(this, localHashMap, paramSubtypeResolver);
  }

  @Deprecated
  public DeserializationConfig createUnshared(TypeResolverBuilder<?> paramTypeResolverBuilder, VisibilityChecker<?> paramVisibilityChecker, SubtypeResolver paramSubtypeResolver)
  {
    return createUnshared(paramSubtypeResolver).withTypeResolverBuilder(paramTypeResolverBuilder).withVisibilityChecker(paramVisibilityChecker);
  }

  public JsonDeserializer<Object> deserializerInstance(Annotated paramAnnotated, Class<? extends JsonDeserializer<?>> paramClass)
  {
    HandlerInstantiator localHandlerInstantiator = getHandlerInstantiator();
    if (localHandlerInstantiator != null)
    {
      JsonDeserializer localJsonDeserializer = localHandlerInstantiator.deserializerInstance(this, paramAnnotated, paramClass);
      if (localJsonDeserializer != null)
        return localJsonDeserializer;
    }
    return (JsonDeserializer)ClassUtil.createInstance(paramClass, canOverrideAccessModifiers());
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
    VisibilityChecker localVisibilityChecker = getDefaultVisibilityChecker();
    this._base = this._base.withVisibilityChecker(localAnnotationIntrospector.findAutoDetectVisibility(localAnnotatedClass, localVisibilityChecker));
  }

  @Deprecated
  public AbstractTypeResolver getAbstractTypeResolver()
  {
    return this._abstractTypeResolver;
  }

  public AnnotationIntrospector getAnnotationIntrospector()
  {
    if (isEnabled(Feature.USE_ANNOTATIONS))
      return super.getAnnotationIntrospector();
    return NopAnnotationIntrospector.instance;
  }

  public Base64Variant getBase64Variant()
  {
    return Base64Variants.getDefaultVariant();
  }

  public final JsonNodeFactory getNodeFactory()
  {
    return this._nodeFactory;
  }

  public LinkedNode<DeserializationProblemHandler> getProblemHandlers()
  {
    return this._problemHandlers;
  }

  public <T extends BeanDescription> T introspect(JavaType paramJavaType)
  {
    return getClassIntrospector().forDeserialization(this, paramJavaType, this);
  }

  public <T extends BeanDescription> T introspectClassAnnotations(Class<?> paramClass)
  {
    return getClassIntrospector().forClassAnnotations(this, paramClass, this);
  }

  public <T extends BeanDescription> T introspectDirectClassAnnotations(Class<?> paramClass)
  {
    return getClassIntrospector().forDirectClassAnnotations(this, paramClass, this);
  }

  public <T extends BeanDescription> T introspectForCreation(JavaType paramJavaType)
  {
    return getClassIntrospector().forCreation(this, paramJavaType, this);
  }

  public boolean isAnnotationProcessingEnabled()
  {
    return isEnabled(Feature.USE_ANNOTATIONS);
  }

  public final boolean isEnabled(Feature paramFeature)
  {
    return (this._featureFlags & paramFeature.getMask()) != 0;
  }

  public KeyDeserializer keyDeserializerInstance(Annotated paramAnnotated, Class<? extends KeyDeserializer> paramClass)
  {
    HandlerInstantiator localHandlerInstantiator = getHandlerInstantiator();
    if (localHandlerInstantiator != null)
    {
      KeyDeserializer localKeyDeserializer = localHandlerInstantiator.keyDeserializerInstance(this, paramAnnotated, paramClass);
      if (localKeyDeserializer != null)
        return localKeyDeserializer;
    }
    return (KeyDeserializer)ClassUtil.createInstance(paramClass, canOverrideAccessModifiers());
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
  public void setAbstractTypeResolver(AbstractTypeResolver paramAbstractTypeResolver)
  {
    this._abstractTypeResolver = paramAbstractTypeResolver;
  }

  @Deprecated
  public void setNodeFactory(JsonNodeFactory paramJsonNodeFactory)
  {
    this._nodeFactory = paramJsonNodeFactory;
  }

  public DeserializationConfig withAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector)
  {
    return new DeserializationConfig(this, this._base.withAnnotationIntrospector(paramAnnotationIntrospector));
  }

  public DeserializationConfig withClassIntrospector(ClassIntrospector<? extends BeanDescription> paramClassIntrospector)
  {
    return new DeserializationConfig(this, this._base.withClassIntrospector(paramClassIntrospector));
  }

  public DeserializationConfig withDateFormat(DateFormat paramDateFormat)
  {
    if (paramDateFormat == this._base.getDateFormat())
      return this;
    return new DeserializationConfig(this, this._base.withDateFormat(paramDateFormat));
  }

  public DeserializationConfig withHandlerInstantiator(HandlerInstantiator paramHandlerInstantiator)
  {
    if (paramHandlerInstantiator == this._base.getHandlerInstantiator())
      return this;
    return new DeserializationConfig(this, this._base.withHandlerInstantiator(paramHandlerInstantiator));
  }

  public DeserializationConfig withNodeFactory(JsonNodeFactory paramJsonNodeFactory)
  {
    return new DeserializationConfig(this, paramJsonNodeFactory);
  }

  public DeserializationConfig withPropertyNamingStrategy(PropertyNamingStrategy paramPropertyNamingStrategy)
  {
    return new DeserializationConfig(this, this._base.withPropertyNamingStrategy(paramPropertyNamingStrategy));
  }

  public DeserializationConfig withSubtypeResolver(SubtypeResolver paramSubtypeResolver)
  {
    DeserializationConfig localDeserializationConfig = new DeserializationConfig(this);
    localDeserializationConfig._subtypeResolver = paramSubtypeResolver;
    return localDeserializationConfig;
  }

  public DeserializationConfig withTypeFactory(TypeFactory paramTypeFactory)
  {
    if (paramTypeFactory == this._base.getTypeFactory())
      return this;
    return new DeserializationConfig(this, this._base.withTypeFactory(paramTypeFactory));
  }

  public DeserializationConfig withTypeResolverBuilder(TypeResolverBuilder<?> paramTypeResolverBuilder)
  {
    return new DeserializationConfig(this, this._base.withTypeResolverBuilder(paramTypeResolverBuilder));
  }

  public DeserializationConfig withVisibilityChecker(VisibilityChecker<?> paramVisibilityChecker)
  {
    return new DeserializationConfig(this, this._base.withVisibilityChecker(paramVisibilityChecker));
  }

  public static enum Feature
  {
    final boolean _defaultState;

    static
    {
      AUTO_DETECT_SETTERS = new Feature("AUTO_DETECT_SETTERS", 1, true);
      AUTO_DETECT_CREATORS = new Feature("AUTO_DETECT_CREATORS", 2, true);
      AUTO_DETECT_FIELDS = new Feature("AUTO_DETECT_FIELDS", 3, true);
      USE_GETTERS_AS_SETTERS = new Feature("USE_GETTERS_AS_SETTERS", 4, true);
      CAN_OVERRIDE_ACCESS_MODIFIERS = new Feature("CAN_OVERRIDE_ACCESS_MODIFIERS", 5, true);
      USE_BIG_DECIMAL_FOR_FLOATS = new Feature("USE_BIG_DECIMAL_FOR_FLOATS", 6, false);
      USE_BIG_INTEGER_FOR_INTS = new Feature("USE_BIG_INTEGER_FOR_INTS", 7, false);
      READ_ENUMS_USING_TO_STRING = new Feature("READ_ENUMS_USING_TO_STRING", 8, false);
      FAIL_ON_UNKNOWN_PROPERTIES = new Feature("FAIL_ON_UNKNOWN_PROPERTIES", 9, true);
      FAIL_ON_NULL_FOR_PRIMITIVES = new Feature("FAIL_ON_NULL_FOR_PRIMITIVES", 10, false);
      FAIL_ON_NUMBERS_FOR_ENUMS = new Feature("FAIL_ON_NUMBERS_FOR_ENUMS", 11, false);
      WRAP_EXCEPTIONS = new Feature("WRAP_EXCEPTIONS", 12, true);
      WRAP_ROOT_VALUE = new Feature("WRAP_ROOT_VALUE", 13, false);
      ACCEPT_EMPTY_STRING_AS_NULL_OBJECT = new Feature("ACCEPT_EMPTY_STRING_AS_NULL_OBJECT", 14, false);
      ACCEPT_SINGLE_VALUE_AS_ARRAY = new Feature("ACCEPT_SINGLE_VALUE_AS_ARRAY", 15, false);
      Feature[] arrayOfFeature = new Feature[16];
      arrayOfFeature[0] = USE_ANNOTATIONS;
      arrayOfFeature[1] = AUTO_DETECT_SETTERS;
      arrayOfFeature[2] = AUTO_DETECT_CREATORS;
      arrayOfFeature[3] = AUTO_DETECT_FIELDS;
      arrayOfFeature[4] = USE_GETTERS_AS_SETTERS;
      arrayOfFeature[5] = CAN_OVERRIDE_ACCESS_MODIFIERS;
      arrayOfFeature[6] = USE_BIG_DECIMAL_FOR_FLOATS;
      arrayOfFeature[7] = USE_BIG_INTEGER_FOR_INTS;
      arrayOfFeature[8] = READ_ENUMS_USING_TO_STRING;
      arrayOfFeature[9] = FAIL_ON_UNKNOWN_PROPERTIES;
      arrayOfFeature[10] = FAIL_ON_NULL_FOR_PRIMITIVES;
      arrayOfFeature[11] = FAIL_ON_NUMBERS_FOR_ENUMS;
      arrayOfFeature[12] = WRAP_EXCEPTIONS;
      arrayOfFeature[13] = WRAP_ROOT_VALUE;
      arrayOfFeature[14] = ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
      arrayOfFeature[15] = ACCEPT_SINGLE_VALUE_AS_ARRAY;
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
 * Qualified Name:     org.codehaus.jackson.map.DeserializationConfig
 * JD-Core Version:    0.6.0
 */