package org.codehaus.jackson.map;

public abstract interface ContextualSerializer<T>
{
  public abstract JsonSerializer<T> createContextual(SerializationConfig paramSerializationConfig, BeanProperty paramBeanProperty)
    throws JsonMappingException;
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ContextualSerializer
 * JD-Core Version:    0.6.0
 */