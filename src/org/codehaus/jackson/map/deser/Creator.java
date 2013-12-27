package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

abstract class Creator
{
  static final class Delegating
  {
    protected final AnnotatedMember _creator;
    protected final Constructor<?> _ctor;
    protected JsonDeserializer<Object> _deserializer;
    protected final Method _factoryMethod;
    protected final JavaType _valueType;

    public Delegating(BasicBeanDescription paramBasicBeanDescription, AnnotatedConstructor paramAnnotatedConstructor, AnnotatedMethod paramAnnotatedMethod)
    {
      TypeBindings localTypeBindings = paramBasicBeanDescription.bindingsForBeanType();
      if (paramAnnotatedConstructor != null)
      {
        this._creator = paramAnnotatedConstructor;
        this._ctor = paramAnnotatedConstructor.getAnnotated();
        this._factoryMethod = null;
        this._valueType = localTypeBindings.resolveType(paramAnnotatedConstructor.getParameterType(0));
        return;
      }
      if (paramAnnotatedMethod != null)
      {
        this._creator = paramAnnotatedMethod;
        this._ctor = null;
        this._factoryMethod = paramAnnotatedMethod.getAnnotated();
        this._valueType = localTypeBindings.resolveType(paramAnnotatedMethod.getParameterType(0));
        return;
      }
      throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
    }

    public Object deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
      throws IOException, JsonProcessingException
    {
      Object localObject1 = this._deserializer.deserialize(paramJsonParser, paramDeserializationContext);
      try
      {
        if (this._ctor != null)
          return this._ctor.newInstance(new Object[] { localObject1 });
        Object localObject2 = this._factoryMethod.invoke(null, new Object[] { localObject1 });
        return localObject2;
      }
      catch (Exception localException)
      {
        ClassUtil.unwrapAndThrowAsIAE(localException);
      }
      return null;
    }

    public AnnotatedMember getCreator()
    {
      return this._creator;
    }

    public JavaType getValueType()
    {
      return this._valueType;
    }

    public void setDeserializer(JsonDeserializer<Object> paramJsonDeserializer)
    {
      this._deserializer = paramJsonDeserializer;
    }
  }

  static final class NumberBased
  {
    protected final Constructor<?> _intCtor;
    protected final Method _intFactoryMethod;
    protected final Constructor<?> _longCtor;
    protected final Method _longFactoryMethod;
    protected final Class<?> _valueClass;

    public NumberBased(Class<?> paramClass, AnnotatedConstructor paramAnnotatedConstructor1, AnnotatedMethod paramAnnotatedMethod1, AnnotatedConstructor paramAnnotatedConstructor2, AnnotatedMethod paramAnnotatedMethod2)
    {
      this._valueClass = paramClass;
      Constructor localConstructor1;
      Constructor localConstructor2;
      label30: Method localMethod1;
      label43: Method localMethod2;
      if (paramAnnotatedConstructor1 == null)
      {
        localConstructor1 = null;
        this._intCtor = localConstructor1;
        if (paramAnnotatedConstructor2 != null)
          break label73;
        localConstructor2 = null;
        this._longCtor = localConstructor2;
        if (paramAnnotatedMethod1 != null)
          break label83;
        localMethod1 = null;
        this._intFactoryMethod = localMethod1;
        localMethod2 = null;
        if (paramAnnotatedMethod2 != null)
          break label92;
      }
      while (true)
      {
        this._longFactoryMethod = localMethod2;
        return;
        localConstructor1 = paramAnnotatedConstructor1.getAnnotated();
        break;
        label73: localConstructor2 = paramAnnotatedConstructor2.getAnnotated();
        break label30;
        label83: localMethod1 = paramAnnotatedMethod1.getAnnotated();
        break label43;
        label92: localMethod2 = paramAnnotatedMethod2.getAnnotated();
      }
    }

    public Object construct(int paramInt)
    {
      try
      {
        if (this._intCtor != null)
        {
          Constructor localConstructor = this._intCtor;
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = Integer.valueOf(paramInt);
          return localConstructor.newInstance(arrayOfObject2);
        }
        if (this._intFactoryMethod != null)
        {
          Method localMethod = this._intFactoryMethod;
          Class localClass = this._valueClass;
          Object[] arrayOfObject1 = new Object[1];
          arrayOfObject1[0] = Integer.valueOf(paramInt);
          Object localObject = localMethod.invoke(localClass, arrayOfObject1);
          return localObject;
        }
      }
      catch (Exception localException)
      {
        ClassUtil.unwrapAndThrowAsIAE(localException);
      }
      return construct(paramInt);
    }

    public Object construct(long paramLong)
    {
      try
      {
        if (this._longCtor != null)
        {
          Constructor localConstructor = this._longCtor;
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = Long.valueOf(paramLong);
          return localConstructor.newInstance(arrayOfObject2);
        }
        if (this._longFactoryMethod != null)
        {
          Method localMethod = this._longFactoryMethod;
          Class localClass = this._valueClass;
          Object[] arrayOfObject1 = new Object[1];
          arrayOfObject1[0] = Long.valueOf(paramLong);
          Object localObject = localMethod.invoke(localClass, arrayOfObject1);
          return localObject;
        }
      }
      catch (Exception localException)
      {
        ClassUtil.unwrapAndThrowAsIAE(localException);
      }
      return null;
    }
  }

  static final class PropertyBased
  {
    protected final Constructor<?> _ctor;
    protected final Object[] _defaultValues;
    protected final Method _factoryMethod;
    protected final HashMap<String, SettableBeanProperty> _properties;

    public PropertyBased(AnnotatedConstructor paramAnnotatedConstructor, SettableBeanProperty[] paramArrayOfSettableBeanProperty1, AnnotatedMethod paramAnnotatedMethod, SettableBeanProperty[] paramArrayOfSettableBeanProperty2)
    {
      SettableBeanProperty[] arrayOfSettableBeanProperty;
      if (paramAnnotatedConstructor != null)
      {
        this._ctor = paramAnnotatedConstructor.getAnnotated();
        this._factoryMethod = null;
        arrayOfSettableBeanProperty = paramArrayOfSettableBeanProperty1;
      }
      Object[] arrayOfObject;
      while (true)
      {
        this._properties = new HashMap();
        arrayOfObject = null;
        int i = 0;
        int j = arrayOfSettableBeanProperty.length;
        while (true)
          if (i < j)
          {
            SettableBeanProperty localSettableBeanProperty = arrayOfSettableBeanProperty[i];
            this._properties.put(localSettableBeanProperty.getName(), localSettableBeanProperty);
            if (localSettableBeanProperty.getType().isPrimitive())
            {
              if (arrayOfObject == null)
                arrayOfObject = new Object[j];
              arrayOfObject[i] = ClassUtil.defaultValue(localSettableBeanProperty.getType().getRawClass());
            }
            i++;
            continue;
            if (paramAnnotatedMethod != null)
            {
              this._ctor = null;
              this._factoryMethod = paramAnnotatedMethod.getAnnotated();
              arrayOfSettableBeanProperty = paramArrayOfSettableBeanProperty2;
              break;
            }
            throw new IllegalArgumentException("Internal error: neither delegating constructor nor factory method passed");
          }
      }
      this._defaultValues = arrayOfObject;
    }

    public Object build(PropertyValueBuffer paramPropertyValueBuffer)
      throws Exception
    {
      Object localObject1 = null;
      try
      {
        Object localObject3;
        if (this._ctor != null)
          localObject3 = this._ctor.newInstance(paramPropertyValueBuffer.getParameters(this._defaultValues));
        Object localObject2;
        for (localObject1 = localObject3; ; localObject1 = localObject2)
        {
          for (PropertyValue localPropertyValue = paramPropertyValueBuffer.buffered(); localPropertyValue != null; localPropertyValue = localPropertyValue.next)
            localPropertyValue.assign(localObject1);
          localObject2 = this._factoryMethod.invoke(null, paramPropertyValueBuffer.getParameters(this._defaultValues));
        }
      }
      catch (Exception localException)
      {
        ClassUtil.throwRootCause(localException);
      }
      return localObject1;
    }

    public SettableBeanProperty findCreatorProperty(String paramString)
    {
      return (SettableBeanProperty)this._properties.get(paramString);
    }

    public Collection<SettableBeanProperty> properties()
    {
      return this._properties.values();
    }

    public PropertyValueBuffer startBuilding(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
    {
      return new PropertyValueBuffer(paramJsonParser, paramDeserializationContext, this._properties.size());
    }
  }

  static final class StringBased
  {
    protected final Constructor<?> _ctor;
    protected final Method _factoryMethod;
    protected final Class<?> _valueClass;

    public StringBased(Class<?> paramClass, AnnotatedConstructor paramAnnotatedConstructor, AnnotatedMethod paramAnnotatedMethod)
    {
      this._valueClass = paramClass;
      Constructor localConstructor;
      Method localMethod;
      if (paramAnnotatedConstructor == null)
      {
        localConstructor = null;
        this._ctor = localConstructor;
        localMethod = null;
        if (paramAnnotatedMethod != null)
          break label45;
      }
      while (true)
      {
        this._factoryMethod = localMethod;
        return;
        localConstructor = paramAnnotatedConstructor.getAnnotated();
        break;
        label45: localMethod = paramAnnotatedMethod.getAnnotated();
      }
    }

    public Object construct(String paramString)
    {
      try
      {
        if (this._ctor != null)
          return this._ctor.newInstance(new Object[] { paramString });
        if (this._factoryMethod != null)
        {
          Object localObject = this._factoryMethod.invoke(this._valueClass, new Object[] { paramString });
          return localObject;
        }
      }
      catch (Exception localException)
      {
        ClassUtil.unwrapAndThrowAsIAE(localException);
      }
      return null;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.Creator
 * JD-Core Version:    0.6.0
 */