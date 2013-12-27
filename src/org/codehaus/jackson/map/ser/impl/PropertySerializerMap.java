package org.codehaus.jackson.map.ser.impl;

import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.type.JavaType;

public abstract class PropertySerializerMap
{
  public static PropertySerializerMap emptyMap()
  {
    return Empty.instance;
  }

  public final SerializerAndMapResult findAndAddSerializer(Class<?> paramClass, SerializerProvider paramSerializerProvider, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    JsonSerializer localJsonSerializer = paramSerializerProvider.findValueSerializer(paramClass, paramBeanProperty);
    return new SerializerAndMapResult(localJsonSerializer, newWith(paramClass, localJsonSerializer));
  }

  public final SerializerAndMapResult findAndAddSerializer(JavaType paramJavaType, SerializerProvider paramSerializerProvider, BeanProperty paramBeanProperty)
    throws JsonMappingException
  {
    JsonSerializer localJsonSerializer = paramSerializerProvider.findValueSerializer(paramJavaType, paramBeanProperty);
    return new SerializerAndMapResult(localJsonSerializer, newWith(paramJavaType.getRawClass(), localJsonSerializer));
  }

  protected abstract PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer);

  public abstract JsonSerializer<Object> serializerFor(Class<?> paramClass);

  private static final class Double extends PropertySerializerMap
  {
    private final JsonSerializer<Object> _serializer1;
    private final JsonSerializer<Object> _serializer2;
    private final Class<?> _type1;
    private final Class<?> _type2;

    public Double(Class<?> paramClass1, JsonSerializer<Object> paramJsonSerializer1, Class<?> paramClass2, JsonSerializer<Object> paramJsonSerializer2)
    {
      this._type1 = paramClass1;
      this._serializer1 = paramJsonSerializer1;
      this._type2 = paramClass2;
      this._serializer2 = paramJsonSerializer2;
    }

    protected PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer)
    {
      PropertySerializerMap.TypeAndSerializer[] arrayOfTypeAndSerializer = new PropertySerializerMap.TypeAndSerializer[2];
      arrayOfTypeAndSerializer[0] = new PropertySerializerMap.TypeAndSerializer(this._type1, this._serializer1);
      arrayOfTypeAndSerializer[1] = new PropertySerializerMap.TypeAndSerializer(this._type2, this._serializer2);
      return new PropertySerializerMap.Multi(arrayOfTypeAndSerializer);
    }

    public JsonSerializer<Object> serializerFor(Class<?> paramClass)
    {
      if (paramClass == this._type1)
        return this._serializer1;
      if (paramClass == this._type2)
        return this._serializer2;
      return null;
    }
  }

  private static final class Empty extends PropertySerializerMap
  {
    protected static final Empty instance = new Empty();

    protected PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer)
    {
      return new PropertySerializerMap.Single(paramClass, paramJsonSerializer);
    }

    public JsonSerializer<Object> serializerFor(Class<?> paramClass)
    {
      return null;
    }
  }

  private static final class Multi extends PropertySerializerMap
  {
    private static final int MAX_ENTRIES = 8;
    private final PropertySerializerMap.TypeAndSerializer[] _entries;

    public Multi(PropertySerializerMap.TypeAndSerializer[] paramArrayOfTypeAndSerializer)
    {
      this._entries = paramArrayOfTypeAndSerializer;
    }

    protected PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer)
    {
      int i = this._entries.length;
      if (i == 8)
        return this;
      PropertySerializerMap.TypeAndSerializer[] arrayOfTypeAndSerializer = new PropertySerializerMap.TypeAndSerializer[i + 1];
      System.arraycopy(this._entries, 0, arrayOfTypeAndSerializer, 0, i);
      arrayOfTypeAndSerializer[i] = new PropertySerializerMap.TypeAndSerializer(paramClass, paramJsonSerializer);
      return new Multi(arrayOfTypeAndSerializer);
    }

    public JsonSerializer<Object> serializerFor(Class<?> paramClass)
    {
      int i = 0;
      int j = this._entries.length;
      while (i < j)
      {
        PropertySerializerMap.TypeAndSerializer localTypeAndSerializer = this._entries[i];
        if (localTypeAndSerializer.type == paramClass)
          return localTypeAndSerializer.serializer;
        i++;
      }
      return null;
    }
  }

  public static final class SerializerAndMapResult
  {
    public final PropertySerializerMap map;
    public final JsonSerializer<Object> serializer;

    public SerializerAndMapResult(JsonSerializer<Object> paramJsonSerializer, PropertySerializerMap paramPropertySerializerMap)
    {
      this.serializer = paramJsonSerializer;
      this.map = paramPropertySerializerMap;
    }
  }

  private static final class Single extends PropertySerializerMap
  {
    private final JsonSerializer<Object> _serializer;
    private final Class<?> _type;

    public Single(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer)
    {
      this._type = paramClass;
      this._serializer = paramJsonSerializer;
    }

    protected PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer)
    {
      return new PropertySerializerMap.Double(this._type, this._serializer, paramClass, paramJsonSerializer);
    }

    public JsonSerializer<Object> serializerFor(Class<?> paramClass)
    {
      if (paramClass == this._type)
        return this._serializer;
      return null;
    }
  }

  private static final class TypeAndSerializer
  {
    public final JsonSerializer<Object> serializer;
    public final Class<?> type;

    public TypeAndSerializer(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer)
    {
      this.type = paramClass;
      this.serializer = paramJsonSerializer;
    }
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.PropertySerializerMap
 * JD-Core Version:    0.6.0
 */