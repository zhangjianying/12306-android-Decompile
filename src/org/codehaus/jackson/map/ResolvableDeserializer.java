package org.codehaus.jackson.map;

public abstract interface ResolvableDeserializer
{
  public abstract void resolve(DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider)
    throws JsonMappingException;
}

/* Location:           D:\开发工具\dex2jar-0.0.9.13\classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.map.ResolvableDeserializer
 * JD-Core Version:    0.6.0
 */