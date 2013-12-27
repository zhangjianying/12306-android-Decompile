package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.io.NumberInput;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;

public abstract class StdKeyDeserializer extends KeyDeserializer
{
  protected final Class<?> _keyClass;

  protected StdKeyDeserializer(Class<?> paramClass)
  {
    this._keyClass = paramClass;
  }

  protected abstract Object _parse(String paramString, DeserializationContext paramDeserializationContext)
    throws Exception;

  protected double _parseDouble(String paramString)
    throws IllegalArgumentException
  {
    return NumberInput.parseDouble(paramString);
  }

  protected int _parseInt(String paramString)
    throws IllegalArgumentException
  {
    return Integer.parseInt(paramString);
  }

  protected long _parseLong(String paramString)
    throws IllegalArgumentException
  {
    return Long.parseLong(paramString);
  }

  public final Object deserializeKey(String paramString, DeserializationContext paramDeserializationContext)
    throws IOException, JsonProcessingException
  {
    Object localObject2;
    if (paramString == null)
      localObject2 = null;
    while (true)
    {
      return localObject2;
      try
      {
        Object localObject1 = _parse(paramString, paramDeserializationContext);
        localObject2 = localObject1;
        if (localObject2 != null)
          continue;
        throw paramDeserializationContext.weirdKeyException(this._keyClass, paramString, "not a valid representation");
      }
      catch (Exception localException)
      {
      }
    }
    throw paramDeserializationContext.weirdKeyException(this._keyClass, paramString, "not a valid representation: " + localException.getMessage());
  }

  public Class<?> getKeyClass()
  {
    return this._keyClass;
  }

  static final class BoolKD extends StdKeyDeserializer
  {
    BoolKD()
    {
      super();
    }

    public Boolean _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      if ("true".equals(paramString))
        return Boolean.TRUE;
      if ("false".equals(paramString))
        return Boolean.FALSE;
      throw paramDeserializationContext.weirdKeyException(this._keyClass, paramString, "value not 'true' or 'false'");
    }
  }

  static final class ByteKD extends StdKeyDeserializer
  {
    ByteKD()
    {
      super();
    }

    public Byte _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      int i = _parseInt(paramString);
      if ((i < -128) || (i > 127))
        throw paramDeserializationContext.weirdKeyException(this._keyClass, paramString, "overflow, value can not be represented as 8-bit value");
      return Byte.valueOf((byte)i);
    }
  }

  static final class CharKD extends StdKeyDeserializer
  {
    CharKD()
    {
      super();
    }

    public Character _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      if (paramString.length() == 1)
        return Character.valueOf(paramString.charAt(0));
      throw paramDeserializationContext.weirdKeyException(this._keyClass, paramString, "can only convert 1-character Strings");
    }
  }

  static final class DoubleKD extends StdKeyDeserializer
  {
    DoubleKD()
    {
      super();
    }

    public Double _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      return Double.valueOf(_parseDouble(paramString));
    }
  }

  static final class EnumKD extends StdKeyDeserializer
  {
    final EnumResolver<?> _resolver;

    EnumKD(EnumResolver<?> paramEnumResolver)
    {
      super();
      this._resolver = paramEnumResolver;
    }

    public Enum<?> _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      Enum localEnum = this._resolver.findEnum(paramString);
      if (localEnum == null)
        throw paramDeserializationContext.weirdKeyException(this._keyClass, paramString, "not one of values for Enum class");
      return localEnum;
    }
  }

  static final class FloatKD extends StdKeyDeserializer
  {
    FloatKD()
    {
      super();
    }

    public Float _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      return Float.valueOf((float)_parseDouble(paramString));
    }
  }

  static final class IntKD extends StdKeyDeserializer
  {
    IntKD()
    {
      super();
    }

    public Integer _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      return Integer.valueOf(_parseInt(paramString));
    }
  }

  static final class LongKD extends StdKeyDeserializer
  {
    LongKD()
    {
      super();
    }

    public Long _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      return Long.valueOf(_parseLong(paramString));
    }
  }

  static final class ShortKD extends StdKeyDeserializer
  {
    ShortKD()
    {
      super();
    }

    public Short _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws JsonMappingException
    {
      int i = _parseInt(paramString);
      if ((i < -32768) || (i > 32767))
        throw paramDeserializationContext.weirdKeyException(this._keyClass, paramString, "overflow, value can not be represented as 16-bit value");
      return Short.valueOf((short)i);
    }
  }

  static final class StringCtorKeyDeserializer extends StdKeyDeserializer
  {
    final Constructor<?> _ctor;

    public StringCtorKeyDeserializer(Constructor<?> paramConstructor)
    {
      super();
      this._ctor = paramConstructor;
    }

    public Object _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws Exception
    {
      return this._ctor.newInstance(new Object[] { paramString });
    }
  }

  static final class StringFactoryKeyDeserializer extends StdKeyDeserializer
  {
    final Method _factoryMethod;

    public StringFactoryKeyDeserializer(Method paramMethod)
    {
      super();
      this._factoryMethod = paramMethod;
    }

    public Object _parse(String paramString, DeserializationContext paramDeserializationContext)
      throws Exception
    {
      return this._factoryMethod.invoke(null, new Object[] { paramString });
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdKeyDeserializer
 * JD-Core Version:    0.6.0
 */