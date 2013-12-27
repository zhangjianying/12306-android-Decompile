package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.map.TypeSerializer;

public abstract class ContainerSerializerBase<T> extends SerializerBase<T>
{
  protected ContainerSerializerBase(Class<T> paramClass)
  {
    super(paramClass);
  }

  protected ContainerSerializerBase(Class<?> paramClass, boolean paramBoolean)
  {
    super(paramClass, paramBoolean);
  }

  public abstract ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer);

  public ContainerSerializerBase<?> withValueTypeSerializer(TypeSerializer paramTypeSerializer)
  {
    if (paramTypeSerializer == null)
      return this;
    return _withValueTypeSerializer(paramTypeSerializer);
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ContainerSerializerBase
 * JD-Core Version:    0.6.0
 */