package org.codehaus.jackson.type;

import java.lang.reflect.Type;

public class TypeReference<T>
{
  private final Type genericType = ((java.lang.reflect.ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

  public Type getType()
  {
    return this.genericType;
  }
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.type.TypeReference
 * JD-Core Version:    0.6.0
 */