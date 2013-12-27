package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

@JsonCachable
public class BeanDeserializer extends StdDeserializer<Object>
  implements ResolvableDeserializer
{
  protected final SettableAnyProperty _anySetter;
  protected final Map<String, SettableBeanProperty> _backRefs;
  protected final BeanPropertyMap _beanProperties;
  protected final JavaType _beanType;
  protected final Constructor<?> _defaultConstructor;
  protected final Creator.Delegating _delegatingCreator;
  protected final AnnotatedClass _forClass;
  protected final HashSet<String> _ignorableProps;
  protected final boolean _ignoreAllUnknown;
  protected final Creator.NumberBased _numberCreator;
  protected final BeanProperty _property;
  protected final Creator.PropertyBased _propertyBasedCreator;
  protected final Creator.StringBased _stringCreator;
  protected HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;

  protected BeanDeserializer(BeanDeserializer paramBeanDeserializer)
  {
    super(paramBeanDeserializer._beanType);
    this._forClass = paramBeanDeserializer._forClass;
    this._beanType = paramBeanDeserializer._beanType;
    this._property = paramBeanDeserializer._property;
    this._beanProperties = paramBeanDeserializer._beanProperties;
    this._backRefs = paramBeanDeserializer._backRefs;
    this._ignorableProps = paramBeanDeserializer._ignorableProps;
    this._ignoreAllUnknown = paramBeanDeserializer._ignoreAllUnknown;
    this._anySetter = paramBeanDeserializer._anySetter;
    this._defaultConstructor = paramBeanDeserializer._defaultConstructor;
    this._stringCreator = paramBeanDeserializer._stringCreator;
    this._numberCreator = paramBeanDeserializer._numberCreator;
    this._delegatingCreator = paramBeanDeserializer._delegatingCreator;
    this._propertyBasedCreator = paramBeanDeserializer._propertyBasedCreator;
  }

  public BeanDeserializer(AnnotatedClass paramAnnotatedClass, JavaType paramJavaType, BeanProperty paramBeanProperty, CreatorContainer paramCreatorContainer, BeanPropertyMap paramBeanPropertyMap, Map<String, SettableBeanProperty> paramMap, HashSet<String> paramHashSet, boolean paramBoolean, SettableAnyProperty paramSettableAnyProperty)
  {
    super(paramJavaType);
    this._forClass = paramAnnotatedClass;
    this._beanType = paramJavaType;
    this._property = paramBeanProperty;
    this._beanProperties = paramBeanPropertyMap;
    this._backRefs = paramMap;
    this._ignorableProps = paramHashSet;
    this._ignoreAllUnknown = paramBoolean;
    this._anySetter = paramSettableAnyProperty;
    this._stringCreator = paramCreatorContainer.stringCreator();
    this._numberCreator = paramCreatorContainer.numberCreator();
    this._delegatingCreator = paramCreatorContainer.delegatingCreator();
    this._propertyBasedCreator = paramCreatorContainer.propertyBasedCreator();
    if ((this._delegatingCreator != null) || (this._propertyBasedCreator != null))
    {
      this._defaultConstructor = null;
      return;
    }
    this._defaultConstructor = paramCreatorContainer.getDefaultConstructor();
  }

  protected final Object _deserializeUsingPropertyBased(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    Creator.PropertyBased localPropertyBased = this._propertyBasedCreator;
    PropertyValueBuffer localPropertyValueBuffer = localPropertyBased.startBuilding(paramJsonParser, paramDeserializationContext);
    TokenBuffer localTokenBuffer = null;
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    String str;
    if (localJsonToken == JsonToken.FIELD_NAME)
    {
      str = paramJsonParser.getCurrentName();
      paramJsonParser.nextToken();
      SettableBeanProperty localSettableBeanProperty1 = localPropertyBased.findCreatorProperty(str);
      if (localSettableBeanProperty1 != null)
      {
        Object localObject3 = localSettableBeanProperty1.deserialize(paramJsonParser, paramDeserializationContext);
        if (localPropertyValueBuffer.assignParameter(localSettableBeanProperty1.getCreatorIndex(), localObject3))
          paramJsonParser.nextToken();
      }
    }
    Object localObject2;
    while (true)
    {
      Object localObject5;
      try
      {
        Object localObject4 = localPropertyBased.build(localPropertyValueBuffer);
        localObject5 = localObject4;
        if (localObject5.getClass() != this._beanType.getRawClass())
        {
          localObject2 = handlePolymorphic(paramJsonParser, paramDeserializationContext, localObject5, localTokenBuffer);
          return localObject2;
        }
      }
      catch (Exception localException2)
      {
        wrapAndThrow(localException2, this._beanType.getRawClass(), str, paramDeserializationContext);
      }
      while (true)
      {
        localJsonToken = paramJsonParser.nextToken();
        break;
        if (localTokenBuffer != null)
          localObject5 = handleUnknownProperties(paramDeserializationContext, localObject5, localTokenBuffer);
        return deserialize(paramJsonParser, paramDeserializationContext, localObject5);
        SettableBeanProperty localSettableBeanProperty2 = this._beanProperties.find(str);
        if (localSettableBeanProperty2 != null)
        {
          localPropertyValueBuffer.bufferProperty(localSettableBeanProperty2, localSettableBeanProperty2.deserialize(paramJsonParser, paramDeserializationContext));
          continue;
        }
        if ((this._ignorableProps != null) && (this._ignorableProps.contains(str)))
        {
          paramJsonParser.skipChildren();
          continue;
        }
        if (this._anySetter != null)
        {
          localPropertyValueBuffer.bufferAnyProperty(this._anySetter, str, this._anySetter.deserialize(paramJsonParser, paramDeserializationContext));
          continue;
        }
        if (localTokenBuffer == null)
          localTokenBuffer = new TokenBuffer(paramJsonParser.getCodec());
        localTokenBuffer.writeFieldName(str);
        localTokenBuffer.copyCurrentStructure(paramJsonParser);
      }
      try
      {
        Object localObject1 = localPropertyBased.build(localPropertyValueBuffer);
        localObject2 = localObject1;
        if (localTokenBuffer == null)
          continue;
        if (localObject2.getClass() != this._beanType.getRawClass())
          return handlePolymorphic(null, paramDeserializationContext, localObject2, localTokenBuffer);
      }
      catch (Exception localException1)
      {
        wrapInstantiationProblem(localException1, paramDeserializationContext);
        return null;
      }
    }
    return handleUnknownProperties(paramDeserializationContext, localObject2, localTokenBuffer);
  }

  protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext paramDeserializationContext, Object paramObject, TokenBuffer paramTokenBuffer)
    throws IOException, JsonProcessingException
  {
    monitorenter;
    JsonDeserializer localJsonDeserializer;
    try
    {
      if (this._subDeserializers == null);
      for (localJsonDeserializer = null; ; localJsonDeserializer = (JsonDeserializer)this._subDeserializers.get(new ClassKey(paramObject.getClass())))
      {
        monitorexit;
        if (localJsonDeserializer == null)
          break;
        return localJsonDeserializer;
      }
    }
    finally
    {
      monitorexit;
    }
    DeserializerProvider localDeserializerProvider = paramDeserializationContext.getDeserializerProvider();
    if (localDeserializerProvider != null)
    {
      JavaType localJavaType = paramDeserializationContext.constructType(paramObject.getClass());
      localJsonDeserializer = localDeserializerProvider.findValueDeserializer(paramDeserializationContext.getConfig(), localJavaType, this._property);
      if (localJsonDeserializer != null)
      {
        monitorenter;
        try
        {
          if (this._subDeserializers == null)
            this._subDeserializers = new HashMap();
          this._subDeserializers.put(new ClassKey(paramObject.getClass()), localJsonDeserializer);
        }
        finally
        {
          monitorexit;
        }
      }
    }
    return localJsonDeserializer;
  }

  protected Object constructDefaultInstance()
  {
    try
    {
      Object localObject = this._defaultConstructor.newInstance(new Object[0]);
      return localObject;
    }
    catch (Exception localException)
    {
      ClassUtil.unwrapAndThrowAsIAE(localException);
    }
    return null;
  }

  public final Object deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.START_OBJECT)
    {
      paramJsonParser.nextToken();
      return deserializeFromObject(paramJsonParser, paramDeserializationContext);
    }
    switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[localJsonToken.ordinal()])
    {
    default:
      throw paramDeserializationContext.mappingException(getBeanClass());
    case 1:
      return deserializeFromString(paramJsonParser, paramDeserializationContext);
    case 2:
    case 3:
      return deserializeFromNumber(paramJsonParser, paramDeserializationContext);
    case 4:
      return paramJsonParser.getEmbeddedObject();
    case 5:
    case 6:
    case 7:
      return deserializeUsingCreator(paramJsonParser, paramDeserializationContext);
    case 8:
    case 9:
    }
    return deserializeFromObject(paramJsonParser, paramDeserializationContext);
  }

  public Object deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject)
    throws IOException, JsonProcessingException
  {
    JsonToken localJsonToken = paramJsonParser.getCurrentToken();
    if (localJsonToken == JsonToken.START_OBJECT)
      localJsonToken = paramJsonParser.nextToken();
    if (localJsonToken == JsonToken.FIELD_NAME)
    {
      String str = paramJsonParser.getCurrentName();
      SettableBeanProperty localSettableBeanProperty = this._beanProperties.find(str);
      paramJsonParser.nextToken();
      if (localSettableBeanProperty != null);
      while (true)
      {
        try
        {
          localSettableBeanProperty.deserializeAndSet(paramJsonParser, paramDeserializationContext, paramObject);
          localJsonToken = paramJsonParser.nextToken();
        }
        catch (Exception localException)
        {
          wrapAndThrow(localException, paramObject, str, paramDeserializationContext);
          continue;
        }
        if ((this._ignorableProps != null) && (this._ignorableProps.contains(str)))
        {
          paramJsonParser.skipChildren();
          continue;
        }
        if (this._anySetter != null)
        {
          this._anySetter.deserializeAndSet(paramJsonParser, paramDeserializationContext, paramObject, str);
          continue;
        }
        handleUnknownProperty(paramJsonParser, paramDeserializationContext, paramObject, str);
      }
    }
    return paramObject;
  }

  public Object deserializeFromNumber(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    if (this._numberCreator != null);
    switch (1.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[paramJsonParser.getNumberType().ordinal()])
    {
    default:
      if (this._delegatingCreator == null)
        break;
      return this._delegatingCreator.deserialize(paramJsonParser, paramDeserializationContext);
    case 1:
      return this._numberCreator.construct(paramJsonParser.getIntValue());
    case 2:
      return this._numberCreator.construct(paramJsonParser.getLongValue());
    }
    throw paramDeserializationContext.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON Number");
  }

  public Object deserializeFromObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    Object localObject;
    if (this._defaultConstructor == null)
      if (this._propertyBasedCreator != null)
        localObject = _deserializeUsingPropertyBased(paramJsonParser, paramDeserializationContext);
    do
    {
      return localObject;
      if (this._delegatingCreator != null)
        return this._delegatingCreator.deserialize(paramJsonParser, paramDeserializationContext);
      if (this._beanType.isAbstract())
        throw JsonMappingException.from(paramJsonParser, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
      throw JsonMappingException.from(paramJsonParser, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
      localObject = constructDefaultInstance();
    }
    while (paramJsonParser.getCurrentToken() == JsonToken.END_OBJECT);
    String str = paramJsonParser.getCurrentName();
    paramJsonParser.nextToken();
    SettableBeanProperty localSettableBeanProperty = this._beanProperties.find(str);
    if (localSettableBeanProperty != null);
    while (true)
    {
      try
      {
        localSettableBeanProperty.deserializeAndSet(paramJsonParser, paramDeserializationContext, localObject);
        paramJsonParser.nextToken();
      }
      catch (Exception localException2)
      {
        wrapAndThrow(localException2, localObject, str, paramDeserializationContext);
        continue;
      }
      if ((this._ignorableProps != null) && (this._ignorableProps.contains(str)))
      {
        paramJsonParser.skipChildren();
        continue;
      }
      if (this._anySetter != null)
      {
        try
        {
          this._anySetter.deserializeAndSet(paramJsonParser, paramDeserializationContext, localObject, str);
        }
        catch (Exception localException1)
        {
          wrapAndThrow(localException1, localObject, str, paramDeserializationContext);
        }
        continue;
      }
      handleUnknownProperty(paramJsonParser, paramDeserializationContext, localObject, str);
    }
  }

  public Object deserializeFromString(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    if (this._stringCreator != null)
      return this._stringCreator.construct(paramJsonParser.getText());
    if (this._delegatingCreator != null)
      return this._delegatingCreator.deserialize(paramJsonParser, paramDeserializationContext);
    if ((paramDeserializationContext.isEnabled(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) && (paramJsonParser.getTextLength() == 0))
      return null;
    throw paramDeserializationContext.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON String");
  }

  public Object deserializeUsingCreator(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    if (this._delegatingCreator != null)
      try
      {
        Object localObject = this._delegatingCreator.deserialize(paramJsonParser, paramDeserializationContext);
        return localObject;
      }
      catch (Exception localException)
      {
        wrapInstantiationProblem(localException, paramDeserializationContext);
      }
    throw paramDeserializationContext.mappingException(getBeanClass());
  }

  public Object deserializeWithType(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, TypeDeserializer paramTypeDeserializer)
    throws IOException, JsonProcessingException
  {
    return paramTypeDeserializer.deserializeTypedFromObject(paramJsonParser, paramDeserializationContext);
  }

  public SettableBeanProperty findBackReference(String paramString)
  {
    if (this._backRefs == null)
      return null;
    return (SettableBeanProperty)this._backRefs.get(paramString);
  }

  public final Class<?> getBeanClass()
  {
    return this._beanType.getRawClass();
  }

  public int getPropertyCount()
  {
    return this._beanProperties.size();
  }

  public JavaType getValueType()
  {
    return this._beanType;
  }

  protected Object handlePolymorphic(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject, TokenBuffer paramTokenBuffer)
    throws IOException, JsonProcessingException
  {
    JsonDeserializer localJsonDeserializer = _findSubclassDeserializer(paramDeserializationContext, paramObject, paramTokenBuffer);
    if (localJsonDeserializer != null)
    {
      if (paramTokenBuffer != null)
      {
        paramTokenBuffer.writeEndObject();
        JsonParser localJsonParser = paramTokenBuffer.asParser();
        localJsonParser.nextToken();
        paramObject = localJsonDeserializer.deserialize(localJsonParser, paramDeserializationContext, paramObject);
      }
      if (paramJsonParser != null)
        paramObject = localJsonDeserializer.deserialize(paramJsonParser, paramDeserializationContext, paramObject);
      return paramObject;
    }
    if (paramTokenBuffer != null)
      paramObject = handleUnknownProperties(paramDeserializationContext, paramObject, paramTokenBuffer);
    if (paramJsonParser != null)
      paramObject = deserialize(paramJsonParser, paramDeserializationContext, paramObject);
    return paramObject;
  }

  protected Object handleUnknownProperties(DeserializationContext paramDeserializationContext, Object paramObject, TokenBuffer paramTokenBuffer)
    throws IOException, JsonProcessingException
  {
    paramTokenBuffer.writeEndObject();
    JsonParser localJsonParser = paramTokenBuffer.asParser();
    while (localJsonParser.nextToken() != JsonToken.END_OBJECT)
    {
      String str = localJsonParser.getCurrentName();
      localJsonParser.nextToken();
      handleUnknownProperty(localJsonParser, paramDeserializationContext, paramObject, str);
    }
    return paramObject;
  }

  protected void handleUnknownProperty(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, Object paramObject, String paramString)
    throws IOException, JsonProcessingException
  {
    if ((this._ignoreAllUnknown) || ((this._ignorableProps != null) && (this._ignorableProps.contains(paramString))))
    {
      paramJsonParser.skipChildren();
      return;
    }
    super.handleUnknownProperty(paramJsonParser, paramDeserializationContext, paramObject, paramString);
  }

  public boolean hasProperty(String paramString)
  {
    return this._beanProperties.find(paramString) != null;
  }

  public Iterator<SettableBeanProperty> properties()
  {
    if (this._beanProperties == null)
      throw new IllegalStateException("Can only call before BeanDeserializer has been resolved");
    return this._beanProperties.allProperties();
  }

  public void resolve(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException
  {
    Iterator localIterator1 = this._beanProperties.allProperties();
    while (localIterator1.hasNext())
    {
      SettableBeanProperty localSettableBeanProperty2 = (SettableBeanProperty)localIterator1.next();
      if (!localSettableBeanProperty2.hasValueDeserializer())
        localSettableBeanProperty2.setValueDeserializer(findDeserializer(paramDeserializationConfig, paramDeserializerProvider, localSettableBeanProperty2.getType(), localSettableBeanProperty2));
      String str = localSettableBeanProperty2.getManagedReferenceName();
      if (str == null)
        continue;
      JsonDeserializer localJsonDeserializer2 = localSettableBeanProperty2._valueDeserializer;
      boolean bool = false;
      SettableBeanProperty localSettableBeanProperty3;
      if ((localJsonDeserializer2 instanceof BeanDeserializer))
        localSettableBeanProperty3 = ((BeanDeserializer)localJsonDeserializer2).findBackReference(str);
      while (localSettableBeanProperty3 == null)
      {
        throw new IllegalArgumentException("Can not handle managed/back reference '" + str + "': no back reference property found from type " + localSettableBeanProperty2.getType());
        if ((localJsonDeserializer2 instanceof ContainerDeserializer))
        {
          JsonDeserializer localJsonDeserializer3 = ((ContainerDeserializer)localJsonDeserializer2).getContentDeserializer();
          if (!(localJsonDeserializer3 instanceof BeanDeserializer))
            throw new IllegalArgumentException("Can not handle managed/back reference '" + str + "': value deserializer is of type ContainerDeserializer, but content type is not handled by a BeanDeserializer " + " (instead it's of type " + localJsonDeserializer3.getClass().getName() + ")");
          localSettableBeanProperty3 = ((BeanDeserializer)localJsonDeserializer3).findBackReference(str);
          bool = true;
          continue;
        }
        if ((localJsonDeserializer2 instanceof AbstractDeserializer))
          throw new IllegalArgumentException("Can not handle managed/back reference for abstract types (property " + this._beanType.getRawClass().getName() + "." + localSettableBeanProperty2.getName() + ")");
        throw new IllegalArgumentException("Can not handle managed/back reference '" + str + "': type for value deserializer is not BeanDeserializer or ContainerDeserializer, but " + localJsonDeserializer2.getClass().getName());
      }
      JavaType localJavaType1 = this._beanType;
      JavaType localJavaType2 = localSettableBeanProperty3.getType();
      if (!localJavaType2.getRawClass().isAssignableFrom(localJavaType1.getRawClass()))
        throw new IllegalArgumentException("Can not handle managed/back reference '" + str + "': back reference type (" + localJavaType2.getRawClass().getName() + ") not compatible with managed type (" + localJavaType1.getRawClass().getName() + ")");
      this._beanProperties.replace(new SettableBeanProperty.ManagedReferenceProperty(str, localSettableBeanProperty2, localSettableBeanProperty3, this._forClass.getAnnotations(), bool));
    }
    if ((this._anySetter != null) && (!this._anySetter.hasValueDeserializer()))
      this._anySetter.setValueDeserializer(findDeserializer(paramDeserializationConfig, paramDeserializerProvider, this._anySetter.getType(), this._anySetter.getProperty()));
    if (this._delegatingCreator != null)
    {
      BeanProperty.Std localStd = new BeanProperty.Std(null, this._delegatingCreator.getValueType(), this._forClass.getAnnotations(), this._delegatingCreator.getCreator());
      JsonDeserializer localJsonDeserializer1 = findDeserializer(paramDeserializationConfig, paramDeserializerProvider, this._delegatingCreator.getValueType(), localStd);
      this._delegatingCreator.setDeserializer(localJsonDeserializer1);
    }
    if (this._propertyBasedCreator != null)
    {
      Iterator localIterator2 = this._propertyBasedCreator.properties().iterator();
      while (localIterator2.hasNext())
      {
        SettableBeanProperty localSettableBeanProperty1 = (SettableBeanProperty)localIterator2.next();
        if (localSettableBeanProperty1.hasValueDeserializer())
          continue;
        localSettableBeanProperty1.setValueDeserializer(findDeserializer(paramDeserializationConfig, paramDeserializerProvider, localSettableBeanProperty1.getType(), localSettableBeanProperty1));
      }
    }
  }

  @Deprecated
  public void wrapAndThrow(Throwable paramThrowable, Object paramObject, int paramInt)
    throws IOException
  {
    wrapAndThrow(paramThrowable, paramObject, paramInt, null);
  }

  public void wrapAndThrow(Throwable paramThrowable, Object paramObject, int paramInt, DeserializationContext paramDeserializationContext)
    throws IOException
  {
    while (((paramThrowable instanceof InvocationTargetException)) && (paramThrowable.getCause() != null))
      paramThrowable = paramThrowable.getCause();
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    int i;
    if ((paramDeserializationContext == null) || (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS)))
      i = 1;
    while (true)
      if ((paramThrowable instanceof IOException))
      {
        if ((i != 0) && ((paramThrowable instanceof JsonMappingException)))
          break;
        throw ((IOException)paramThrowable);
        i = 0;
        continue;
      }
      else
      {
        if ((i != 0) || (!(paramThrowable instanceof RuntimeException)))
          break;
        throw ((RuntimeException)paramThrowable);
      }
    throw JsonMappingException.wrapWithPath(paramThrowable, paramObject, paramInt);
  }

  @Deprecated
  public void wrapAndThrow(Throwable paramThrowable, Object paramObject, String paramString)
    throws IOException
  {
    wrapAndThrow(paramThrowable, paramObject, paramString, null);
  }

  public void wrapAndThrow(Throwable paramThrowable, Object paramObject, String paramString, DeserializationContext paramDeserializationContext)
    throws IOException
  {
    while (((paramThrowable instanceof InvocationTargetException)) && (paramThrowable.getCause() != null))
      paramThrowable = paramThrowable.getCause();
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    int i;
    if ((paramDeserializationContext == null) || (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS)))
      i = 1;
    while (true)
      if ((paramThrowable instanceof IOException))
      {
        if ((i != 0) && ((paramThrowable instanceof JsonMappingException)))
          break;
        throw ((IOException)paramThrowable);
        i = 0;
        continue;
      }
      else
      {
        if ((i != 0) || (!(paramThrowable instanceof RuntimeException)))
          break;
        throw ((RuntimeException)paramThrowable);
      }
    throw JsonMappingException.wrapWithPath(paramThrowable, paramObject, paramString);
  }

  protected void wrapInstantiationProblem(Throwable paramThrowable, DeserializationContext paramDeserializationContext)
    throws IOException
  {
    while (((paramThrowable instanceof InvocationTargetException)) && (paramThrowable.getCause() != null))
      paramThrowable = paramThrowable.getCause();
    if ((paramThrowable instanceof Error))
      throw ((Error)paramThrowable);
    if ((paramDeserializationContext == null) || (paramDeserializationContext.isEnabled(DeserializationConfig.Feature.WRAP_EXCEPTIONS)));
    for (int i = 1; (paramThrowable instanceof IOException); i = 0)
      throw ((IOException)paramThrowable);
    if ((i == 0) && ((paramThrowable instanceof RuntimeException)))
      throw ((RuntimeException)paramThrowable);
    throw paramDeserializationContext.instantiationException(this._beanType.getRawClass(), paramThrowable);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BeanDeserializer
 * JD-Core Version:    0.6.0
 */