package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.InternCache;

public abstract class SettableBeanProperty
  implements BeanProperty
{
  protected final Annotations _contextAnnotations;
  protected String _managedReferenceName;
  protected NullProvider _nullProvider;
  protected final String _propName;
  protected int _propertyIndex = -1;
  protected final JavaType _type;
  protected JsonDeserializer<Object> _valueDeserializer;
  protected TypeDeserializer _valueTypeDeserializer;

  protected SettableBeanProperty(String paramString, JavaType paramJavaType, TypeDeserializer paramTypeDeserializer, Annotations paramAnnotations)
  {
    if ((paramString == null) || (paramString.length() == 0));
    for (this._propName = ""; ; this._propName = InternCache.instance.intern(paramString))
    {
      this._type = paramJavaType;
      this._contextAnnotations = paramAnnotations;
      this._valueTypeDeserializer = paramTypeDeserializer;
      return;
    }
  }

  protected IOException _throwAsIOE(Exception paramException)
    throws IOException
  {
    if ((paramException instanceof IOException))
      throw ((IOException)paramException);
    if ((paramException instanceof RuntimeException))
      throw ((RuntimeException)paramException);
    for (Object localObject = paramException; ((Throwable)localObject).getCause() != null; localObject = ((Throwable)localObject).getCause());
    throw new JsonMappingException(((Throwable)localObject).getMessage(), null, (Throwable)localObject);
  }

  protected void _throwAsIOE(Exception paramException, Object paramObject)
    throws IOException
  {
    if ((paramException instanceof IllegalArgumentException))
    {
      String str1;
      StringBuilder localStringBuilder;
      if (paramObject == null)
      {
        str1 = "[NULL]";
        localStringBuilder = new StringBuilder("Problem deserializing property '").append(getPropertyName());
        localStringBuilder.append("' (expected type: ").append(getType());
        localStringBuilder.append("; actual type: ").append(str1).append(")");
        String str2 = paramException.getMessage();
        if (str2 == null)
          break label117;
        localStringBuilder.append(", problem: ").append(str2);
      }
      while (true)
      {
        throw new JsonMappingException(localStringBuilder.toString(), null, paramException);
        str1 = paramObject.getClass().getName();
        break;
        label117: localStringBuilder.append(" (no error message provided)");
      }
    }
    _throwAsIOE(paramException);
  }

  public void assignIndex(int paramInt)
  {
    if (this._propertyIndex != -1)
      throw new IllegalStateException("Property '" + getName() + "' already had index (" + this._propertyIndex + "), trying to assign " + paramInt);
    this._propertyIndex = paramInt;
  }

  public final Object deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    if (paramJsonParser.getCurrentToken() == JsonToken.VALUE_NULL)
    {
      if (this._nullProvider == null)
        return null;
      return this._nullProvider.nullValue(paramDeserializationContext);
    }
    if (this._valueTypeDeserializer != null)
      return this._valueDeserializer.deserializeWithType(paramJsonParser, paramDeserializationContext, this._valueTypeDeserializer);
    return this._valueDeserializer.deserialize(paramJsonParser, paramDeserializationContext);
  }

  public abstract void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
    throws IOException, JsonProcessingException;

  public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);

  public <A extends Annotation> A getContextAnnotation(Class<A> paramClass)
  {
    return this._contextAnnotations.get(paramClass);
  }

  public int getCreatorIndex()
  {
    return -1;
  }

  protected final Class<?> getDeclaringClass()
  {
    return getMember().getDeclaringClass();
  }

  public String getManagedReferenceName()
  {
    return this._managedReferenceName;
  }

  public abstract AnnotatedMember getMember();

  public final String getName()
  {
    return this._propName;
  }

  @Deprecated
  public String getPropertyName()
  {
    return this._propName;
  }

  public int getProperytIndex()
  {
    return this._propertyIndex;
  }

  public JavaType getType()
  {
    return this._type;
  }

  public boolean hasValueDeserializer()
  {
    return this._valueDeserializer != null;
  }

  public abstract void set(Object paramObject1, Object paramObject2)
    throws IOException;

  public void setManagedReferenceName(String paramString)
  {
    this._managedReferenceName = paramString;
  }

  public void setValueDeserializer(JsonDeserializer<Object> paramJsonDeserializer)
  {
    if (this._valueDeserializer != null)
      throw new IllegalStateException("Already had assigned deserializer for property '" + getName() + "' (class " + getDeclaringClass().getName() + ")");
    this._valueDeserializer = paramJsonDeserializer;
    Object localObject = this._valueDeserializer.getNullValue();
    if (localObject == null);
    for (NullProvider localNullProvider = null; ; localNullProvider = new NullProvider(this._type, localObject))
    {
      this._nullProvider = localNullProvider;
      return;
    }
  }

  public String toString()
  {
    return "[property '" + getName() + "']";
  }

  public static final class CreatorProperty extends SettableBeanProperty
  {
    protected final AnnotatedParameter _annotated;
    protected final int _index;

    public CreatorProperty(String paramString, JavaType paramJavaType, TypeDeserializer paramTypeDeserializer, Annotations paramAnnotations, AnnotatedParameter paramAnnotatedParameter, int paramInt)
    {
      super(paramJavaType, paramTypeDeserializer, paramAnnotations);
      this._annotated = paramAnnotatedParameter;
      this._index = paramInt;
    }

    public void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
      throws IOException, JsonProcessingException
    {
      set(paramObject, deserialize(paramJsonParser, paramDeserializationContext));
    }

    public <A extends Annotation> A getAnnotation(Class<A> paramClass)
    {
      return this._annotated.getAnnotation(paramClass);
    }

    public int getCreatorIndex()
    {
      return this._index;
    }

    public AnnotatedMember getMember()
    {
      return this._annotated;
    }

    public void set(Object paramObject1, Object paramObject2)
      throws IOException
    {
    }
  }

  public static final class FieldProperty extends SettableBeanProperty
  {
    protected final AnnotatedField _annotated;
    protected final Field _field;

    public FieldProperty(String paramString, JavaType paramJavaType, TypeDeserializer paramTypeDeserializer, Annotations paramAnnotations, AnnotatedField paramAnnotatedField)
    {
      super(paramJavaType, paramTypeDeserializer, paramAnnotations);
      this._annotated = paramAnnotatedField;
      this._field = paramAnnotatedField.getAnnotated();
    }

    public void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
      throws IOException, JsonProcessingException
    {
      set(paramObject, deserialize(paramJsonParser, paramDeserializationContext));
    }

    public <A extends Annotation> A getAnnotation(Class<A> paramClass)
    {
      return this._annotated.getAnnotation(paramClass);
    }

    public AnnotatedMember getMember()
    {
      return this._annotated;
    }

    public final void set(Object paramObject1, Object paramObject2)
      throws IOException
    {
      try
      {
        this._field.set(paramObject1, paramObject2);
        return;
      }
      catch (Exception localException)
      {
        _throwAsIOE(localException, paramObject2);
      }
    }
  }

  public static final class ManagedReferenceProperty extends SettableBeanProperty
  {
    protected final SettableBeanProperty _backProperty;
    protected final boolean _isContainer;
    protected final SettableBeanProperty _managedProperty;
    protected final String _referenceName;

    public ManagedReferenceProperty(String paramString, SettableBeanProperty paramSettableBeanProperty1, SettableBeanProperty paramSettableBeanProperty2, Annotations paramAnnotations, boolean paramBoolean)
    {
      super(paramSettableBeanProperty1.getType(), paramSettableBeanProperty1._valueTypeDeserializer, paramAnnotations);
      this._referenceName = paramString;
      this._managedProperty = paramSettableBeanProperty1;
      this._backProperty = paramSettableBeanProperty2;
      this._isContainer = paramBoolean;
    }

    public void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
      throws IOException, JsonProcessingException
    {
      set(paramObject, this._managedProperty.deserialize(paramJsonParser, paramDeserializationContext));
    }

    public <A extends Annotation> A getAnnotation(Class<A> paramClass)
    {
      return this._managedProperty.getAnnotation(paramClass);
    }

    public AnnotatedMember getMember()
    {
      return this._managedProperty.getMember();
    }

    public final void set(Object paramObject1, Object paramObject2)
      throws IOException
    {
      this._managedProperty.set(paramObject1, paramObject2);
      if (paramObject2 != null)
      {
        if (this._isContainer)
        {
          if ((paramObject2 instanceof Object[]))
            for (Object localObject3 : (Object[])(Object[])paramObject2)
            {
              if (localObject3 == null)
                continue;
              this._backProperty.set(localObject3, paramObject1);
            }
          if ((paramObject2 instanceof Collection))
          {
            Iterator localIterator2 = ((Collection)paramObject2).iterator();
            while (localIterator2.hasNext())
            {
              Object localObject2 = localIterator2.next();
              if (localObject2 == null)
                continue;
              this._backProperty.set(localObject2, paramObject1);
            }
          }
          if ((paramObject2 instanceof Map))
          {
            Iterator localIterator1 = ((Map)paramObject2).values().iterator();
            while (localIterator1.hasNext())
            {
              Object localObject1 = localIterator1.next();
              if (localObject1 == null)
                continue;
              this._backProperty.set(localObject1, paramObject1);
            }
          }
          throw new IllegalStateException("Unsupported container type (" + paramObject2.getClass().getName() + ") when resolving reference '" + this._referenceName + "'");
        }
        this._backProperty.set(paramObject2, paramObject1);
      }
    }
  }

  public static final class MethodProperty extends SettableBeanProperty
  {
    protected final AnnotatedMethod _annotated;
    protected final Method _setter;

    public MethodProperty(String paramString, JavaType paramJavaType, TypeDeserializer paramTypeDeserializer, Annotations paramAnnotations, AnnotatedMethod paramAnnotatedMethod)
    {
      super(paramJavaType, paramTypeDeserializer, paramAnnotations);
      this._annotated = paramAnnotatedMethod;
      this._setter = paramAnnotatedMethod.getAnnotated();
    }

    public void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
      throws IOException, JsonProcessingException
    {
      set(paramObject, deserialize(paramJsonParser, paramDeserializationContext));
    }

    public <A extends Annotation> A getAnnotation(Class<A> paramClass)
    {
      return this._annotated.getAnnotation(paramClass);
    }

    public AnnotatedMember getMember()
    {
      return this._annotated;
    }

    public final void set(Object paramObject1, Object paramObject2)
      throws IOException
    {
      try
      {
        this._setter.invoke(paramObject1, new Object[] { paramObject2 });
        return;
      }
      catch (Exception localException)
      {
        _throwAsIOE(localException, paramObject2);
      }
    }
  }

  protected static final class NullProvider
  {
    private final boolean _isPrimitive;
    private final Object _nullValue;
    private final Class<?> _rawType;

    protected NullProvider(JavaType paramJavaType, Object paramObject)
    {
      this._nullValue = paramObject;
      this._isPrimitive = paramJavaType.isPrimitive();
      this._rawType = paramJavaType.getRawClass();
    }

    public Object nullValue(DeserializationContext paramDeserializationContext)
      throws JsonProcessingException
    {
      if ((this._isPrimitive) && (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES)))
        throw paramDeserializationContext.mappingException("Can not map JSON null into type " + this._rawType.getName() + " (set DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)");
      return this._nullValue;
    }
  }

  public static final class SetterlessProperty extends SettableBeanProperty
  {
    protected final AnnotatedMethod _annotated;
    protected final Method _getter;

    public SetterlessProperty(String paramString, JavaType paramJavaType, TypeDeserializer paramTypeDeserializer, Annotations paramAnnotations, AnnotatedMethod paramAnnotatedMethod)
    {
      super(paramJavaType, paramTypeDeserializer, paramAnnotations);
      this._annotated = paramAnnotatedMethod;
      this._getter = paramAnnotatedMethod.getAnnotated();
    }

    public final void deserializeAndSet(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
      throws IOException, JsonProcessingException
    {
      if (paramJsonParser.getCurrentToken() == JsonToken.VALUE_NULL)
        return;
      Object localObject;
      try
      {
        localObject = this._getter.invoke(paramObject, new Object[0]);
        if (localObject == null)
          throw new JsonMappingException("Problem deserializing 'setterless' property '" + getName() + "': get method returned null");
      }
      catch (Exception localException)
      {
        _throwAsIOE(localException);
        return;
      }
      this._valueDeserializer.deserialize(paramJsonParser, paramDeserializationContext, localObject);
    }

    public <A extends Annotation> A getAnnotation(Class<A> paramClass)
    {
      return this._annotated.getAnnotation(paramClass);
    }

    public AnnotatedMember getMember()
    {
      return this._annotated;
    }

    public final void set(Object paramObject1, Object paramObject2)
      throws IOException
    {
      throw new UnsupportedOperationException("Should never call 'set' on setterless property");
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.SettableBeanProperty
 * JD-Core Version:    0.6.0
 */