package org.codehaus.jackson.map.deser;

import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.JavaType;

public abstract class ContainerDeserializer<T> extends StdDeserializer<T>
{
  protected ContainerDeserializer(Class<?> paramClass)
  {
    super(paramClass);
  }

  public abstract JsonDeserializer<Object> getContentDeserializer();

  public abstract JavaType getContentType();
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.ContainerDeserializer
 * JD-Core Version:    0.6.0
 */